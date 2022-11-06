package eu.simuline.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Stack;

/**
 * Provides methods for benchmarking 
 * inspired by according MATLAB functions <code>tic</code> and <code>toc</code> 
 * which are also available with octave 
 * and which are described in  https://www.mathworks.com/help/matlab/ref/tic.html 
 * and in https://octave.sourceforge.io/octave/function/tic.html. 
 * <p>
 * Whereas the original implementation from MATLAB and octave offer cpu benchmarking only, 
 * this implementation also provides memory benchmarking. 
 * To distinguish, 
 * the according (static) methods are called {@link #mtic()} and {@link #mtoc()}, 
 * with <code>m</code> standing for 'memory'. 
 * Note that in general in java used memory cannot be measured exactly, 
 * because there is no way to force a full garbage collection. 
 * So the quality of the measurements depends on the implementation of the virtual machine. 
 * <p>
 * As with the original benchmarking, measurement is started with {@link #mtic()} 
 * which determines the current point of time and memory consumption, 
 * and it is ended with {@link #mtoc()} 
 * measuring the span of time passed between {@link #mtic()} and {@link #mtoc()} 
 * and the difference in used memory. 
 * Unlike the time passed, memory difference may well be negative indicating freed memory. 
 * Unlike the original <code>tic</code> and <code>toc</code> mechanism, 
 * this class also provides a way to {@link #pause()} and to {@link #resume()} a measurement 
 * and it is pretty clear what this means. 
 * <p>
 * MATLABs <code>tic</code> and <code>toc</code> measurements may also overlap 
 * using the identifier returned by <code>tic</code> and consumed by <code>toc</code>. 
 * As benchmarking follows program structures which consist of nested function invocations 
 * and also nested structures in function bodies, 
 * in fact no general overlapping measurements are required 
 * but only nested measurements. 
 * A new measurement is always added as the innermost of the nested measurements 
 * if there are any ongoing measurements. 
 * An ongoing measurement may be paused or running. 
 * <p>
 * A measurement is represented as a {@link Snapshot}. 
 * The containment hierarchy is implemented as a stack {@link #snapshots} of snapshots, 
 * where {@link #mtic()} adds a new snapshot to the stack 
 * and {@link #mtoc()} stops a measurement, pops it from the stack and returns it. 
 * In contrast, {@link #mtic()} returns only the (immutual) hashcode of the snapshot as an identifier 
 * to identify corresponding invocations of {@link #mtic()} and {@link #mtoc()}. 
 * Only stopped measurements which cannot be resumed are handed over to the user. 
 * Thus {@link #pause()} and {@link #resume()} have no return values. 
 * Only snapshots on the stack can be paused or resumed, 
 * whereas {@link #mtoc()} is a final stop. 
 * <p>
 * Whereas neither of the above methods require a lot of memory, 
 * all of them are time consuming and above all, it is because of their memory measurement. 
 * To avoid influence on time measurement requires specific considerations. 
 * Methods starting or resuming a measurement, measure memory before time and 
 * methods pausing or ending a measurement measure time before memory. 
 * This ensures influence on the current time measurement. 
 * For enclosing measurements this is done by the constraint that 
 * (if there is an enclosing measurement at all) on the stack all measurements are paused, 
 * except possibly the last one. 
 * A new measurement can be started via {@link #mtic()} only, 
 * if there is no enclosing measurement or if the innermost enclosing is running. 
 * Then it is paused before adding the new running measurement. 
 * Conversely {@link #mtoc()} assumes that there is a current measurement, 
 * and that it is running, whereas enclosing measurements, if any, are paused. 
 * Then {@link #mtoc()} stops the current measurement, returns the according snapshot 
 * and resumes the enclosing measurement, if any. 
 * That way, all methods have to pause/resume no more than 2 measurements 
 * limiting influence of this benchmarker on the time measurement. 
 * Note that effectively {@link #pause()} pauses all ongoing measurements not only the innermost one, 
 * and accordingly {@link #resume()} resumes all of them. 
 * <p>
 * The original tic/toc mechanism provided by MATLAB allows to start more than one measurement at one time: 
 * just a single <code>tic</code> can be the startpoint of various measurements with <code>toc</code>. 
 * Essentially, we had to invoke {@link #mtic()} more than once 
 * which leads to a minor overhead which may still be confusing. 
 * To overcome this, a method {@link #mtic(int)} allowing to start multiple measurements is provided. 
 * Accordingly, {@link #mtoc(int)} completes multiple measurements. 
 * MATLAB does not provide a counterpart for that. 
 * Note that in a degenerate case, one may start and stop several measurements simultaneously, 
 * so that they cover the same span of time. 
 * Still they are nested in a predefined order given by the ordering the according snapshots are stored on the stack. 
 * <p>
 * Planned are improvements providing cpu-time and that like. 
 * Useful links are e.g. https://stackoverflow.com/questions/7467245/cpu-execution-time-in-java 
 * and https://github.com/openjdk/jmh. 
 * See also https://www.baeldung.com/java-microbenchmark-harness
 */
public final class Benchmarker {

  /* ----------------------------------------------------------------------- *
   * inner classes. *
   * ----------------------------------------------------------------------- */

  /**
   * A snapshot can either be started or stopped. 
   * If started it represents the point of time and the amount of used memory 
   * when started. 
   * If stopped, 
   * it represents the span of time elapsed and an amount of memory allocated; 
   * negative values representing freed memory. 
   * After creation via {@link #Snapshot(boolean)}, it is started/stopped, 
   * but it can switch between states started and stopped 
   * invoking {@link #toggleStartStop(boolean)}. 
   */
  public static class Snapshot {

    /* --------------------------------------------------------------------- *
     * fields. *
     * --------------------------------------------------------------------- */

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
     * in nanoseconds (10^{-9} seconds) 
     * as returned by {@link System#nanoTime()}, 
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
     * before measuring the memory, 
     * the JVM is asked to run the garbage collector. 
     * <p>
     * More general, recording can be started and stopped more than once. 
     * Then if stopped, 
     * this field contains the accumulated elapsed memory while running, 
     * whereas when running it is the (theoretic) used memory when started, 
     * so that when stopping 
     * it again contains the accumulated memory elapsed. 
     */
    private long memBytes;

    /* --------------------------------------------------------------------- *
     * constructors. *
     * --------------------------------------------------------------------- */

     /**
      * Constructor to create a new snapshot which is started if specified so. 
      *
      * @param doStart
      *    whether the snapshot to be created is started or not. 
      */
    Snapshot(boolean doStart) {
      this.isStopped = true;
      if (doStart) {
        toggleStartStop(false);
      }
    }

    // TBD: maybe this shall be eliminated: 
    // if Benchmarker.snap is eliminated. 
    Snapshot(Snapshot other) {
      assert other.isStopped();
      this.isStopped = other.isStopped;
      this.timeNs    = other.timeNs;
      this.memBytes  = other.memBytes;
    }

    /* --------------------------------------------------------------------- *
     * methods. *
     * --------------------------------------------------------------------- */

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
     * Since there is no way 
     * to force the VM to perform a complete garbage collection, 
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

  /* ----------------------------------------------------------------------- *
   * fields. *
   * ----------------------------------------------------------------------- */

   /**
    * An object needed to determine memory consumption. 
    */
  private static final MemoryMXBean MEM_BEAN =
    ManagementFactory.getMemoryMXBean();

  /**
   * A stack of measurements, one enclosing the next, 
   * which is empty at least initially. 
   * Here, enclosing refers to the span of time 
   * of ongoing measurement including pauses. 
   * All but the last (innermost) measurements must be paused 
   * and the last measurement may be paused or not. 
   * <ul>
   * <li>{@link #mtic()} pauses the top entry if any 
   *     and adds a new running one. 
   * <li>{@link #mtic(int)} pauses the top entry if any and adds new ones, 
   *     all paused but the innermost which is running. 
   * <li>{@link #pause()} pauses the top entry if any and if running. 
   * <li>{@link #resume()} resumes the top entry if any and if paused. 
   * <li>{@link #mtoc()} stops the top entry, 
   *     removes it from the stack and returns it. 
   *     As a side effect, 
   *     it adds time and memory consumption to the new top level if any 
   *     and restarts it. 
   * <li>{@link #mtoc(int)} stops the top entry, 
   *     removes it from the stack and 
   *     adds time and memory consumption to the new top level. 
   *     Does so the given number of times. 
   *     Then restarts the remaining top level if any. 
   *     Finally returns the snapshots 
   *     removed from the stack in order from outer to inner. 
   * </ul>
   */
  private static final Stack<Snapshot> snapshots = new Stack<Snapshot>();

  /**
   * An array with length 1 to hold a hash code 
   * used to implement {@link #mtic()} 
   * in terms of {@link #mtic(int[])}. 
   */
  private static final int[] HASH_CODES1 = new int[1];

  /**
   * An array with length 1 to hold a {@link Snapshot} 
   * used to implement {@link #mtoc()} 
   * in terms of {@link #mtoc(Snapshot[])}. 
   */
  private static final Snapshot[] SNAPSHOTS1 = new Snapshot[1];


  /* ----------------------------------------------------------------------- *
   * constructor. *
   * ----------------------------------------------------------------------- */

  private Benchmarker() {
    // solely to avoid instantiation 
  }

  /* ----------------------------------------------------------------------- *
   * methods (static only). *
   * ----------------------------------------------------------------------- */

  private static final long usedMemoryBytes() {
    MEM_BEAN.gc();
    MemoryUsage memUsageHeap    = MEM_BEAN.getHeapMemoryUsage();
    MemoryUsage memUsageNonHeap = MEM_BEAN.getNonHeapMemoryUsage();
    return memUsageHeap.getUsed() + memUsageNonHeap.getUsed();
  }

  /**
   * Resets benchmarking erasing all measurements. 
   * The state is as at class initialization. 
   */
  public static void reset() {
    snapshots.clear();
  }

  /**
   * Starts a new time/memory measurement represented by a {@link Snapshot} 
   * and returns its hashcode. 
   * This is allowed only if there is no enclosing measurement at all 
   * or if the (innermost) enclosing measurement is running. 
   * <p>
   * As a side effect, pauses the innermost enclosing measurement 
   * before starting the new one. 
   * That way, time consumed by {@link #mtic()} itself 
   * invoking garbage collection which is required for memory measurement 
   * is not taken into account. 
   * <p>
   * Initially, the innermost enclosing measurement is represented 
   * by the top {@link Snapshot} on the stack {@link #snapshots}, 
   * finally, 
   * the new measurement is pushed on top of the stack as a new snapshot. 
   * 
   * @return
   *   The hash code of the current snapshot holding time and memory. 
   *   Note that the hash code reflects the identity 
   *   and does not change 
   *   if a snapshot is started or stopped or else changed. 
   *   Also note that a snapshot itself can be returned only, 
   *   if stopped and cannot be restarted. 
   *   In this implementation this means it is off the stack. 
   *   Thus what is returned is the hash code only, 
   *   rather than the Snapshot itself. 
   *   The hashcode returned belongs to the topmost snapshot on the stack. 
   * @throws IllegalStateException
   *   If there is an enclosing mtic and this is stopped. 
   * @see #mtoc()
   * @see #mtic(int)
   */
  public static int mtic() {
    return mtic(HASH_CODES1)[0];
  }

  /**
   * Defines the start 
   * of <code>numTics</code> new time/memory measurements at the same time 
   * each represented as a {@link Snapshot} and return their hashcodes. 
   * This is allowed only if there is no enclosing measurement at all 
   * or if the (innermost) enclosing measurement is running. 
   * <p>
   * As a side effect, 
   * pauses the innermost enclosing measurement before adding the new ones. 
   * Keep all but the innermost new measurements paused also 
   * only the innermost start as running. 
   * This sets all new measurements to the same start time and start memory. 
   * That way, 
   * impact of running this method on the time measurement is minimized. 
   * 
   * @param numTics
   *   A positive number 
   *   signifying the number of measurements defined and in parallel. 
   * @return
   *   The <code>numTics</code> hash codes 
   *   of the snapshots defining the new masurements. 
   *   Note that the hash code reflects the identity 
   *   and does not change 
   *   if a snapshot is started or stopped or else changed. 
   *   Also note that a snapshot itself can be returned only, 
   *   if stopped and cannot be restarted. 
   *   In this implementation this means it is off the stack. 
   *   Thus, what is returned 
   *   are the hash codes only rather than the Snapshots themselves. 
   *   The ordering of the hash codes 
   *   reflects the ordering of the Snapshots on the stack {@link #snapshots}, 
   *   where the last hash code belongs to the topmost Snapshot 
   *   which is the only one which is started. 
   * @throws IllegalArgumentException
   *   if <code>numTics</code> is not positive. 
   * @throws IllegalStateException
   *   If there is an enclosing mtic and this is stopped. 
   * @see #mtoc(int)
   * @see #mtic()
   */
  public static int[] mtic(int numTics) {
    if (numTics <= 0) {
      throw new IllegalArgumentException
       ("Expected a positive number of tics but found " + numTics + ". ");
    }
    assert numTics > 0;
    return mtic(new int[numTics]);
  }

  /**
   * Fills <code>hashCodes</code> with hash codes of new {@link Snapshot}s 
   * which are pushed to {@link #snapshots} as a side effect 
   * and returns <code>hashCodes</code>. 
   * The details are so that {@link #mtic(int)} can use this method 
   * just by passing an array of accoring type and length to this method 
   * and returning the result directly. 
   * Also {@link #mtic()} can use this method 
   * passing {@link #HASH_CODES1} to this method 
   * and returning the 0th entry of the return value of this method. 
   * 
   * @param hashCodes
   *   A non-empty array for holding hash codes. 
   *   CAUTION: no exception if the array is not empty. 
   * @return
   *   The array <code>hashCodes</code> filled with hash codes of shapshots. 
   */
  private static int[] mtic(int[] hashCodes) {
    int numTics = hashCodes.length;
    Snapshot snap;
    if (!snapshots.isEmpty()) {
      snap = snapshots.peek();
      if (snap.isStopped()) {
        throw new IllegalStateException("Added tic on stopped tic. ");
      }
      //assert !snap.isStopped();
      snap.toggleStartStop(true);
      assert snap.isStopped();
    }
    for (int i = 0; i < numTics-1; i++) {
      snap = new Snapshot(false);
      assert snap.isStopped();
      snapshots.push(snap);
      hashCodes[i] = snap.hashCode();
    }
    snap = new Snapshot(true);
    assert !snap.isStopped();// also not empty
    snapshots.push(snap);
    hashCodes[numTics-1] = snap.hashCode();
    return hashCodes;
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

  // TBD: maybe this shall be eliminated. 
  /**
   * Performs an intermediate a time/memory meansurement 
   * initiated with {@link #mtic()}
   * which presupposes that there is a measurement and that it is not stopped. 
   * @return
   *   A stopped {@link Snapshot} containing time and memory consumption 
   *   since the last tic. 
   *   In contrast to {@link #mtoc()} the snapshot returned 
   *   is not required to have the hash code 
   *   returned by the according {@link #mtic()}. 
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
   * Ends a time/memory measurement 
   * initiated with {@link #mtic()} or with {@link #mtic(int)}
   * which presupposes that there is a current measurement 
   * and that it is not paused. 
   * First that measurement is stopped and removed from the stack. 
   * If there is an enclosing measurement, 
   * it is already paused and so time and memory consumption is added to it 
   * and then it is restarted, 
   * because it can be assumed, 
   * that it was running when starting this meansurement. 
   * Finally, 
   * returns a snapshot mit time/memory consumption of this measurement. 
   * 
   * @return
   *   The snapshot of the current measurement 
   *   containing time and memory consumption 
   *   since the last according tic. 
   *   This snapshot cannot be resumed any more, 
   *   so its values are fixed. 
   *   Its hash code is the number returned by the according {@link #mtic()}. 
   * @throws IllegalStateException
   *   If there is not tic at all or that tic is stopped. 
   * @see #mtic()
   * @see #mtoc(int)
   */
  public static Snapshot mtoc() {
    return mtoc(SNAPSHOTS1)[0];
  }

  /**
   * Ends <code>numTocs</code> time/memory measurements 
   * initiated with {@link #mtic()} or with {@link #mtic(int)}
   * which presupposes that there are <code>numTocs</code> current measurements 
   * and that the innermost is not paused. 
   * First that measurement is stopped.  
   * There shall be at least <code>numTocs</code> measurements 
   * and so time and memory consumption is added 
   * in a cascade accumulating these values 
   * and these snapshots are removed from the stack. 
   * If there is still an enclosing environment, 
   * it is already paused 
   * and so time and memory consumption accumulated so far is added to it 
   * and then it is restarted, 
   * because it can be assumed 
   * that it was running when starting this meansurement. 
   * Finally, 
   * returns an array of snapshots mit time/memory consumption 
   * of this measurement 
   * removed from the stack in order so that the topmost (innermost) is last. 
   * 
  * @param numTocs
   *   A positive number 
   *   signifying the number of measurements completed in parallel. 
   * @return
   *   The <code>numTics</code> stopped {@link Snapshot}s 
   *   containing time and memory consumption 
   *   of the innermost measurements ended. 
   *   This snapshot cannot be resumed any more, 
   *   so their values are fixed. 
   *   Their hash codes are the numbers 
   *   returned by the according {@link #mtic()}s. 
   *   The ordering of the snapshots 
   *   reflects the ordering of the Snapshots 
   *   they had on the stack {@link #snapshots}, 
   *   where the last hash code belongs to the topmost Snapshot 
   *   which is the only one which is started. 
   * @throws IllegalArgumentException
   *   if <code>numTocs</code> is not positive. 
   * @throws IllegalStateException
   *   If there are not enough enclosing tics or the innermost tic is stopped. 
   * @see #mtic(int)
   * @see #mtoc()
   */
  public static Snapshot[] mtoc(int numTocs) {
    if (numTocs <= 0) {
      throw new IllegalArgumentException
       ("Expected a positive number of tics but found " + numTocs + ". ");
    }
    assert numTocs > 0;
    return mtoc(new Snapshot[numTocs]);
  }

 /**
   * Fills <code>snaps</code> with {@link Snapshot}s 
   * from {@link #snapshots} and returns <code>snaps</code>. 
   * The details are so that {@link #mtoc(int)} can use this method 
   * just by passing an array of accoring type and length to this method 
   * and returning the result directly. 
   * Also {@link #mtoc()} can use this method 
   * passing {@link #SNAPSHOTS1} to this method 
   * and returning the 0th entry of the return value of this method. 
   * 
   * @param snaps
   *   A non-empty array for holding {@link Snapshot}s. 
   *   CAUTION: no exception if the array is not empty. 
   * @return
   *   The array <code>snaps</code> filled with snapshots. 
   */
  private static Snapshot[] mtoc(Snapshot[] snaps) {
    int numTocs = snaps.length;
    if (snapshots.size() < numTocs) {
      throw new IllegalStateException
        (String.format("Only %d tic(s) for %d toc(s). ",
        snapshots.size(), numTocs));
    }
    // throws exception if the according tic is not running 
    //Snapshot[] snaps = new Snapshot[numTocs];
    Snapshot snap;
    // throws an IllegalStateException if the top snapshot is not running 
    Snapshot snapLast = snapshots.pop().toggleStartStop(true);
    snaps[numTocs-1] = snapLast;
    for (int i = numTocs-2; i >= 0; i--) {
      snap = snapshots.pop();
      snap.add(snapLast);
      snaps[i] = snap;
      snapLast = snap;
    }
    // Here, res contains only stopped snapshots 
    if (!snapshots.isEmpty()) {
      snap = snapshots.peek();
      // checks that both snap and res[last] are stopped 
      snap.add(snapLast);
      snap.toggleStartStop(false);
      assert !snap.isStopped();
    }
    return snaps;
  }


  /**
   * Returns the number of nested measurements. 
   * Initially this is <code>0</code>, {@link #mtic} increases this by one, 
   * and, if not 0, {@link #mtoc} decreases by one. 
   * to start the timer. 
   * A precondition for invoking {@link #mtoc} is 
   * that the return value is positive, 
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