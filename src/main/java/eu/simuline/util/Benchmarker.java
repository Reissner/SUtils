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
   * A snapshot represents a time and an amount of memory. 
   * after creation via {@link #Snapshot()}, 
   * this is the current time and the current memory, 
   * whereas after invocation of {@link #toggleStartStop()} it is the time elapsed 
   * and the memory allocated since creation. 
   * Freed memory is indicated as negative allocated memory. 
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
     * constructor. *
     * ------------------------------------------------------------------ */

    Snapshot() {
      this.timeNs = System.nanoTime();
      this.memBytes = usedMemoryBytes();
      this.isStopped = false;
    }

    /* ------------------------------------------------------------------ *
     * methods. *
     * ------------------------------------------------------------------ */

    protected Snapshot toggleStartStop(boolean doStop) {
      assert this.isStopped != doStop;
      // The following two lines of code hold whether stopped or not, 
      // but the meaning depends. 
      this.timeNs    = System.nanoTime() - this.timeNs;
      this.memBytes  = usedMemoryBytes() - this.memBytes;
      this.isStopped = doStop;
      return this;
    }

    protected void add(Snapshot snap) {
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
   * is not taken into account. TBD: NOT TRUE
   * 
   * @return
   *   The hash code of the current snapshot holding time and memory. 
   *   Note that a snapshot itself can be returned only, 
   *   if stopped and cannot be restarted. 
   *   In this implementation this means it is off the stack. 
   * @see #mtoc
   */
  public static int mtic() {
    //assert snapshots.isEmpty();
    Snapshot snap;
    if (!snapshots.isEmpty()) {
      snap = snapshots.peek();
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

  public static void pause() {
    assert !snapshots.isEmpty();
    Snapshot snap = snapshots.peek();
    snap.toggleStartStop(true);
    assert snap.isStopped();// also not empty
  }

  public static void resume() {
    assert !snapshots.isEmpty();
    Snapshot snap = snapshots.peek();
    snap.toggleStartStop(false);
    assert !snap.isStopped();// also not empty
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
   *   A stopped {@link Snapshot} containing time and memory consumption. 
   */
  public static Snapshot mtoc() {
    assert !snapshots.isEmpty();
    Snapshot res = snapshots.pop().toggleStartStop(true);
    //assert snapshots.isEmpty();
    if (!snapshots.isEmpty()) {
      Snapshot snap = snapshots.peek();
      // checks that both snap and res are stopped 
      snap.add(res);
      //assert snap.isStopped();
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
   * If it is set, this allows invoking {@link #mtoc} to stop the timer.
   * In addition, 
   * this leads to different interpretations of time and memory in {@link #snapshot}. 
   */
  public static int numNestedMeasurements() {
    return snapshots.size();
  }

  /**
   * Returns whether the topmost measurement is stopped. 
   * This may be invoked only if there is a measurement 
   * according to {@link #numNestedMeasurements()}. 
   * Only if it is not stopped, {@link #mtic()} may be invoked. 
   * 
   * @return
   *    whether the topmost measurement is stopped. 
   */
  public static boolean isStopped() {
    assert !snapshots.isEmpty();
    return snapshots.peek().isStopped;
  }
}