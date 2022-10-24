package eu.simuline.util;

import java.util.Stack;

/**
 * Provides methods for benchmarking. 
 * It is inspired by according matlab functions tic and toc 
 * but besides cpu benchmarking (at the moment wall-time) 
 * also provides memory benchmarking. 
 * <p>
 * To be close to matlab and a bit to octave see 
 * https://www.mathworks.com/help/matlab/ref/tic.html. 
 * This implementes a kind of stacked time spans, 
 * whereas https://octave.sourceforge.io/octave/function/tic.html works with IDs. 
 * While the first variant seems more intuitive, the second one is more general. 
 * It is possible to implement both at once. 
 * We start with the first one. 
 * <p>
 * Planned are improvements providing cpu-time and that like. 
 * Useful links are e.g. https://stackoverflow.com/questions/7467245/cpu-execution-time-in-java 
 * and https://github.com/openjdk/jmh. 
 * 
 */
public final class Benchmarker {

  /* -------------------------------------------------------------------- *
   * inner classes. *
   * -------------------------------------------------------------------- */

  /**
   * A snapshot can either be started or stopped. 
   * If started it represents the point of time and the amount of used memory 
   * when started. 
   * If stopped, it represents the span of time elapsed and an amount of memory allocated; 
   * negative values representing freed memory. 
   * After creation via {@link #Snapshot()}, it is started, 
   * but it can switch between states started and stopped 
   * invoking {@link #toggleStartStop(boolean)}. 
   */
  public static class Snapshot {

    /* ------------------------------------------------------------------ *
     * fields. *
     * ------------------------------------------------------------------ */

    /**
     * Indicates whether recording stopped. 
     * Initially, 
     * i.e. after invocation of the constructor {@link #Snapshot}, 
     * this is false. 
     * This can be toggled via invocation of {@link #toggleStartStop(boolean)} 
     * with the proper parameter. 
     * <p>
     * This value determines the meaning of the values {@link #timeNs} 
     * and {@link #memBytes}. 
     */
    private boolean isStopped;

    /**
     * Immediately after creation, 
     * this is the current point of time 
     * in nanoseconds (10^{-9} seconds) as returned by {@link System#nanoTime()}, 
     * while if stopped, this is the span of time it ran, 
     * i.e. between starting and stopping. 
     * <p>
     * More general, recording can be started and stopped more than once. 
     * Then if stopped, 
     * this field contains the accumulated (span of) running time, 
     * whereas when running it is the (theoretic) starting (point of) time 
     * so that when stopping 
     * it again contains the accumulated (span of) running time. 
     * This is so to speak the start time leading to the correct running time 
     * if running were without a pause. 
     */
    private long timeNs;

    /**
     * Immediately after creation, this is the used memory in bytes at start, 
     * while if stopped, this is the additional memory elapsed since started, 
     * which may also be negative indicating freed memory. 
     * So in the first case it is memory used at a point in time, 
     * whereas in the second case 
     * it is the additional memory elapsed in a span of time. 
     * For proper evaluation, 
     * before measuring the memory, the JVM is asked to run the garbage collector. 
     * <p>
     * More general, recording can be started and stopped more than once. 
     * Then if stopped, 
     * this field contains the accumulated elapsed memory while running, 
     * whereas when running it is the (theoretic) used memory when started, 
     * so that when stopping 
     * it again contains the accumulated memory elapsed. 
     */
    private long memBytes;

    /* ------------------------------------------------------------------ *
     * constructors. *
     * ------------------------------------------------------------------ */

    Snapshot() {
      this.isStopped = true;
      toggleStartStop(false);
    }

    Snapshot(Snapshot other) {
      assert other.isStopped();
      this.isStopped = other.isStopped;
      this.timeNs    = other.timeNs;
      this.memBytes  = other.memBytes;
    }

    /* ------------------------------------------------------------------ *
     * methods. *
     * ------------------------------------------------------------------ */

     /**
      * If this snapshot is started, stop and if stop start. 
      * The parameter <code>doStop</code> is redundant 
      * and for consistency check only. 
      * 
      * @param doStop
      * @return
      *    this snapshot. 
      * @throws IllegalStateException
      *    if trying to resume if already running 
      *    or if trying to pause if already stopped. 
      */
    protected Snapshot toggleStartStop(boolean doStop) {
      if (this.isStopped == doStop) {
        // Exception
        String msg = doStop
        ? "Tried to pause already stopped. "
        : "Tried to resume already running. ";
        throw new IllegalStateException(msg);
      }
      assert this.isStopped != doStop;
      // The two lines of code hold whether stopping or starting, 
      // but the meaning depends. 
      // The order is chosen 
      // to avoid recording the time needed by usedMemoryBytes 
      // and in particular garbage collection 
      if (doStop) {
        // stopping 
        this.timeNs    = System.nanoTime() - this.timeNs;
        this.memBytes  = usedMemoryBytes() - this.memBytes;
      } else {
        // starting 
        this.memBytes  = usedMemoryBytes() - this.memBytes;
        this.timeNs    = System.nanoTime() - this.timeNs;
      }
      this.isStopped = doStop;
      return this;
    }

    /**
     * Assuming that both this and <code>snap</code> are stopped, 
     * i.e. both represent a span of time and memory increase, 
     * add the time and the memory and store the result in this snapshot. 
     * 
     * @param snap
     *    another stopped snapshot. 
     * @throws IllegalStateException
     *    If this or <code>snap</code> is not stopped. 
     */
    protected void add(Snapshot snap) {
      if (!(snap.isStopped() && this.isStopped())) {
        throw new IllegalStateException
          ("Trying to add two snapshots which are not both stopped. ");
      }
      assert snap.isStopped();
      assert this.isStopped();
      this.timeNs   += snap.timeNs;
      this.memBytes += snap.memBytes;
    }

    public double getTimeMs() {
      assert this.isStopped;
      return this.timeNs/1_000_000.;
    }

    /**
     * The memory allocated on MB. 
     * This is the difference 
     * between memory when starting the test and when finishing. 
     * So this may well be negative indicating freed memory. 
     * Memory is determined after the garbage collector is triggered. 
     * Since there is no way to force the VM to perform a complete garbage collection, 
     * there is no guarantee on the precision of this value. 
     * In an extreme case, the garbage collector is not run at all 
     * and so the result is not very significant. 
     */
    public double getMemoryMB() {
      assert this.isStopped;
      return this.memBytes/1_000_000.;
    }

    public boolean isStopped() {
      return this.isStopped;
    }

    public String toString() {
      String es = isStopped ? "Elapsed " : "Snapshot";
      return String.format("%s time: %fms mem %f",
        es, getTimeMs(), getMemoryMB());
    }

    /**
     * The hash code does not depend on the content. 
     * It is immutable in lifetime of a snapshot. 
     */
    public final int hashCode() {
      return super.hashCode();
    }

    @SuppressWarnings("PMD.UselessOverridingMethod")
    public final boolean equals(Object obj) {
      return super.equals(obj);
    }
  } // class Snapshot 

  /* -------------------------------------------------------------------- *
   * fields. *
   * -------------------------------------------------------------------- */

   /**
    * The runtime needed to determine memory consumption. 
    */
  private static final Runtime RUNTIME = Runtime.getRuntime();

  /**
   * A stack of enclosing measurements which is empty at least initially. 
   * All but the last measurements must be paused 
   * and the last measurement may be paused or not. 
   * <ul>
   * <li>{@link #mtic()} pauses the top entry and adds a new running one. 
   * <li>{@link #pause()} pauses the top entry. 
   * <li>{@link #resume()} resumes the top entry. 
   * <li>{@link #mtoc()} stops the top entry, removes it from the stack 
   * and returns it. 
   * As a side effect, 
   * it adds time and memory consumption to the new top level 
   * and restarts it. 
  * </ul>
   */
  private static final Stack<Snapshot> snapshots = new Stack<Snapshot>();

  /* -------------------------------------------------------------------- *
   * constructor. *
   * -------------------------------------------------------------------- */

  private Benchmarker() {
    // solely to avoid instantiation 
  }

  /* -------------------------------------------------------------------- *
   * methods (static only). *
   * -------------------------------------------------------------------- */

  private static long usedMemoryBytes() {
    RUNTIME.gc();
    return RUNTIME.maxMemory() - RUNTIME.freeMemory();
  }

  /**
   * Resets benchmarking erasing all measurements. 
   * The state is as at class initialization. 
   */
  public static void reset() {
    snapshots.clear();
  }

  /**
   * Starts a new time/memory measurement, 
   * which is allowed only if there is no enclosing measurement at all 
   * or if the enclosing measurement is not paused. 
   * Equivalently: no enclosing measurements is paused. 
   * This holds in particular if there is no enclosing measurement. 
   * <p>
   * As a side effect, pauses the enclosing measurement 
   * before starting the new one. 
   * That way, time consumed by {@link #mtic()} itself 
   * invoking garbage collection which is required for memory measurement 
   * is not taken into account. 
   * 
   * @return
   *   The hash code of the current snapshot holding time and memory. 
   *   Note that a snapshot itself can be returned only, 
   *   if stopped and cannot be restarted. 
   *   In this implementation this means it is off the stack. 
   * @throws IllegalStateException
   *   If there is an enclosing mtic and this is stopped. 
   * @see #mtoc
   */
  public static int mtic() {
    Snapshot snap;
    if (!snapshots.isEmpty()) {
      snap = snapshots.peek();
      if (snap.isStopped()) {
        throw new IllegalStateException("Added tic on stopped tic. ");
      }
      assert !snap.isStopped();
      snap.toggleStartStop(true);
      assert snap.isStopped();
    }
    // Note here, 
    snap = new Snapshot();
    snapshots.push(snap);
    assert !snap.isStopped();// also not empty
    return snap.hashCode();
  }

  // no return value: by design: only stopped snapshots are returned. 
  /**
   * Pauses the current tic if present and running. 
   * 
   * @throws IllegalStateException
   *   If there is not tic at all or that tic is stopped already. 
   */
  public static void pause() {
    if (snapshots.isEmpty()) {
      throw new IllegalStateException("No tic to pause. ");
    }
    assert !snapshots.isEmpty();
    Snapshot snap = snapshots.peek();
    // throws exception if stopped already 
    snap.toggleStartStop(true);
    assert snap.isStopped();// also not empty
  }

  // no return value: by design: only stopped snapshots are returned. 
  /**
   * Resumes the current tic if present and paused. 
   * 
   * @throws IllegalStateException
   *   If there is not tic at all or that tic is running already. 
   */
  public static void resume() {
    if (snapshots.isEmpty()) {
      throw new IllegalStateException("No tic to resume. ");
    }
    assert !snapshots.isEmpty();
    Snapshot snap = snapshots.peek();
    // throws exception if running already 
    snap.toggleStartStop(false);
    assert !snap.isStopped();// also not empty
  }

  /**
   * Performs an intermediate a time/memory meansurement initiated with {@link #mtic()}
   * which presupposes that there is a measurement and that it is not stopped. 
   * @return
   *   A stopped {@link Snapshot} containing time and memory consumption 
   *   since the last tic. 
   *   In contrast to {@link #mtoc()} the snapshot returned 
   *   is not required to have the hash code returned by the according {@link #mtic()}. 
   * @throws IllegalStateException
   *   If there is not tic at all or that tic is stopped. 
   * @see #mtic()
   * @see #mtoc()
   */
  public static Snapshot snap() {
    if (snapshots.isEmpty()) {
      throw new IllegalStateException("No tic to snapshot. ");
    }
    assert !snapshots.isEmpty();
    Snapshot snap = snapshots.peek();
    // throws exception if stopped already 
    snap.toggleStartStop(true);
    Snapshot res = new Snapshot(snap);
    snap.toggleStartStop(false);
    return res;
  }

  /**
   * Ends a time/memory meansurement initiated with {@link #mtic()}
   * which presupposes that there is a measurement and that it is not stopped. 
   * If there is an enclosing measurement, 
   * it was paused and so time and memory consumption is added to it 
   * and then it is restarted, 
   * because it can be assumed that it was running when starting this meansurement. 
   * Finally, returns a snapshot mit time/memory consumption of this measurement. 
   * 
   * @return
   *   A stopped {@link Snapshot} containing time and memory consumption 
   *   since the last according tic. 
   *   This snapshot cannot be resumed any more, 
   *   so its values are fixed. 
   *   Its hash code is the number returned by the according {@link #mtic()}. 
   * @throws IllegalStateException
   *   If there is not tic at all or that tic is stopped. 
    * @see #mtic()
   */
  public static Snapshot mtoc() {
    if (snapshots.isEmpty()) {
      throw new IllegalStateException("No tic to toc. ");
    }
    assert !snapshots.isEmpty();
    // throws exception if the according tic is not running 
    Snapshot res = snapshots.pop().toggleStartStop(true);
    if (!snapshots.isEmpty()) {
      Snapshot snap = snapshots.peek();
      // checks that both snap and res are stopped 
      snap.add(res);
      snap.toggleStartStop(false);
      assert !snap.isStopped();
    }
    assert res.isStopped();
    return res;
  }

  /**
   * Returns the number of nested measurements. 
   * Initially this is <code>0</code>, {@link #mtic} increases this by one, 
   * and, if not 0, {@link #mtoc} decreases by one. 
   * to start the timer. 
   * A precondition for invoking {@link #mtoc} is that the return value is positive, 
   * whereas {@link #mtic} can be invoked if it is zero. 
   * For more conditions see {@link #isStopped()}
   */
  public static int numNestedMeasurements() {
    return snapshots.size();
  }

  /**
   * Returns whether the topmost measurement is stopped. 
   * This may be invoked only if there is a measurement 
   * according to {@link #numNestedMeasurements()}. 
   * Assumed there is a measurement at all, 
   * both {@link #mtic()} and {@link #mtoc()} may be invoked 
   * if and only if the top level measurement is not stopped. 
   * Note that {@link #mtoc()} returns a stopped snapshot, 
   * whereas {@link #mtic()} returns the hash of a started snapshot only. 
   * 
   * @return
   *    whether the topmost measurement is stopped. 
   */
  public static boolean isStopped() {
    assert !snapshots.isEmpty();
    return snapshots.peek().isStopped;
  }
}