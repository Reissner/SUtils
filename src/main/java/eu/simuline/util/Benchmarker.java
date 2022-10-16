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
      this.timeNs = System.nanoTime() - this.timeNs;
      this.memBytes  = usedMemoryBytes() - this.memBytes;
      this.isStopped = doStop;
      return this;
    }

    public double getTimeMs() {
      assert this.isStopped;
      return this.timeNs/1_000_000.;
    }

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

  private static final Runtime RUNTIME = Runtime.getRuntime();

  private static Stack<Snapshot> snapshots = new Stack<Snapshot>();

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

  public static int mtic() {
    assert snapshots.isEmpty();
    Snapshot snap = new Snapshot();
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

  public static Snapshot mtoc() {
    assert !snapshots.isEmpty();
    Snapshot snap = snapshots.pop().toggleStartStop(true);
    assert snapshots.isEmpty();
    assert snap.isStopped();
    return snap;
  }

  /**
   * Indicates whether timer is started. 
   * Initially this is <code>false</code>. 
   * If not, this allows invoking {@link #mtic} 
   * to start the timer. 
   * If it is set, this allows invoking {@link #mtoc} to stop the timer.
   * In addition, 
   * this leads to different interpretations of time and memory in {@link #snapshot}. 
   */
  public static boolean isStarted() {
    return !snapshots.isEmpty();
  }
}