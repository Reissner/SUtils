package eu.simuline.util;

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
  /**
   * A snapshot represents a time and an amount of memory. 
   * after creation, this is the current time and the current memory 
   * whereas after invocation of {@link #stop()} it is the time elapsed 
   * and the memory allocated since creation. 
   * Freed memory is indicated as negative allocated memory. 
   */
  public static class Snapshot {

    /**
     * If {@link #isStarted}, this is the start time in nanoseconds (10^{-9} seconds), 
     * while if stopped, this is the time it ran last, i.e. between starting and stopping. 
     * Before first started this is <code>0</code>. 
     */
    private long timeTicNs;

    /**
     * If {@link #isStarted}, this is the used memory in bytes at start, 
     * while if stopped, this is the additional memory since started, 
     * which may also be negative indicating freed memory. 
     * Before first started this is <code>0</code>. 
     * For proper evaluation, before measuring the JVM is asked to run the garbage collector. 
     */
    private long memBytes;

    Snapshot() {
      this.timeTicNs = System.nanoTime();
      this.memBytes = usedMemoryBytes();
    }

    public void stop() {
      this.timeTicNs = System.nanoTime() - this.timeTicNs;
      this.memBytes  = usedMemoryBytes() - this.memBytes;
    }

    public double getTimeMs() {
      assert !isStarted;
      return this.timeTicNs/1_000_000.;
    }

    public double getMemoryMB() {
      assert !isStarted;
      return this.memBytes/1_000_000.;
    }

  } // class Snapshot 

  /**
   * Indicates whether timer is started. 
   * Initially this is <code>false</code>. 
   * If not, this allows invoking {@link #mtic} 
   * to start the timer. 
   * If it is set, this allows invoking {@link #mtoc} to stop the timer.
   * In addition, this leads to different interpretations of {@link #TIME_TIC_NS}
   */
  private static boolean isStarted = false;

  private static final Runtime RUNTIME = Runtime.getRuntime();

  private static Snapshot snapshot = null;

  private Benchmarker() {
    // solely to avoid instantiation 
  }

  private static long usedMemoryBytes() {
    RUNTIME.gc();
    return RUNTIME.maxMemory() - RUNTIME.freeMemory();
  }

  public static void mtic() {
    assert isStarted == (snapshot != null);
    assert !isStarted;
    isStarted = !isStarted;
     snapshot = new Snapshot();
    assert isStarted == (snapshot != null);
  }

  public static Snapshot mtoc() {
    assert isStarted == (snapshot != null);
    assert isStarted;
    isStarted = !isStarted;
    snapshot.stop();
    Snapshot res = snapshot;
    snapshot = null;
    assert isStarted == (snapshot != null);
    return res;
  }

  public static boolean isStarted() {
    return isStarted;
  }
}