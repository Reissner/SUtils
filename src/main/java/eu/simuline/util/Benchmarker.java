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
   * Indicates whether timer is started. 
   * Initially this is <code>false</code>. 
   * If not, this allows invoking {@link #mtic} 
   * to start the timer. 
   * If it is set, this allows invoking {@link #mtoc} to stop the timer.
   * In addition, this leads to different interpretations of {@link #TIME_TIC_NS}
   */
  private static boolean isStarted = false;

  private static final Runtime RUNTIME = Runtime.getRuntime();

  /**
   * If {@link #isStarted}, this is the start time in nanoseconds (10^{-9} seconds), 
   * while if stopped, this is the time it ran last, i.e. between starting and stopping. 
   * Before first started this is <code>0</code>. 
   */
  private static long TIME_TIC_NS;
  private static long TIME_TIC_MS;

  /**
   * If {@link #isStarted}, this is the used memory in bytes at start, 
   * while if stopped, this is the additional memory since started, 
   * which may also be negative indicating freed memory. 
   * Before first started this is <code>0</code>. 
   * For proper evaluation, before measuring the JVM is asked to run the garbage collector. 
   */
  private static long MEMORY_BYTES;

  private Benchmarker() {
    // solely to avoid instantiation 
  }

  private static long usedMemoryBytes() {
    RUNTIME.gc();
    return RUNTIME.maxMemory() - RUNTIME.freeMemory();
  }

  public static long mtic() {
    assert !isStarted;
    isStarted = !isStarted;
    MEMORY_BYTES = usedMemoryBytes();
    //return 
    TIME_TIC_NS = System.nanoTime();
    return TIME_TIC_MS = System.currentTimeMillis();
  }

  public static long mtoc() {
    assert isStarted;
    isStarted = !isStarted;
    TIME_TIC_NS = System.nanoTime() - TIME_TIC_NS;
    TIME_TIC_MS = System.currentTimeMillis() - TIME_TIC_MS;
    MEMORY_BYTES = usedMemoryBytes() - MEMORY_BYTES;
    return TIME_TIC_MS;
  }

  // public static long getStartTimeMs() {
  //   assert isStarted;
  //   return TIME_TIC_MS;
  // }
  // public static double getStartTimeD() {
  //   assert isStarted;
  //   double res = (double)TIME_TIC_MS;
  //   assert res == TIME_TIC_MS;
  //   return TIME_TIC_MS;
  // }


  // public static long getStartTimeP() {
  //   assert isStarted;
  //   return TIME_TIC_NS;
  // }

  public static double getTimeMs() {
    assert !isStarted;
    double res = (double)TIME_TIC_MS;
    assert res == TIME_TIC_MS;
    return res;//TIME_TIC_MS;///1_000_000.;
    //return TIME_TIC_MS;///1_000_000.;
  }

  // public static double getTimeMsD() {
  //   assert !isStarted;
  //   double res = (double)TIME_TIC_MS;
  //   assert res == TIME_TIC_MS;
  //   return res;//TIME_TIC_MS;///1_000_000.;
  // }

  public static long getTimeMsP() {
    assert !isStarted;
    return TIME_TIC_NS;///1_000_000.;
  }

  public static double getMemoryMB() {
    assert !isStarted;
    return MEMORY_BYTES/1_000_000.;
  }

  public static boolean isStarted() {
    return isStarted;
  }
}