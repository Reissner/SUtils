package eu.simuline.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.simuline.testhelpers.Actions;

/**
 * Testclass for class <code>Benchmarker</code>. 
 * This is rudimentary only. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({BenchmarkerTest.TestAll.class})
public class BenchmarkerTest {

  /* -------------------------------------------------------------------- *
   * framework.                                                           *
   * -------------------------------------------------------------------- */

  static BenchmarkerTest TEST = new BenchmarkerTest();

  public static class TestAll {

    @Test 
    public void testTicToc() throws InterruptedException {
      int timeMs = 1000;
      int mem0 = 0;
      int hash1, hash2;
      Benchmarker.Snapshot snap1, snap2;

      // single tic-toc pair. 
      hash1 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      snap1 = Benchmarker.mtoc();
      assertEquals(timeMs, snap1.getTimeMs(), 1.0);
      assertEquals(mem0,   snap1.getMemoryMB(), 20.0);
      assertEquals(hash1,  snap1.hashCode());
      assertTrue(snap1.isStopped());


      // nested tic-toc pairs
      hash1 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      hash2 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      snap2 = Benchmarker.mtoc();
      Thread.sleep(timeMs);
      snap1 = Benchmarker.mtoc();

      assertEquals(3*timeMs, snap1.getTimeMs(), 1.0);
      assertEquals(  timeMs, snap2.getTimeMs(), 1.0);
      assertEquals(mem0,     snap1.getMemoryMB(), 20.0);
      assertEquals(mem0,     snap2.getMemoryMB(), 20.0);
      assertEquals(hash1,  snap1.hashCode());
      assertEquals(hash2,  snap2.hashCode());
      assertTrue(snap1.isStopped());
      assertTrue(snap1.isStopped());

      // pause and resume
      hash1 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      Benchmarker.pause();
      Thread.sleep(timeMs);
      Benchmarker.resume();
      Thread.sleep(timeMs);
      snap1 = Benchmarker.mtoc();
      assertEquals(2*timeMs, snap1.getTimeMs(), 1.0);
      assertEquals(mem0,   snap1.getMemoryMB(), 20.0);
      assertEquals(hash1,  snap1.hashCode());
      assertTrue(snap1.isStopped());

 

    } // void testTicToc() 
  } // class TestAll 

     /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String... args) {
			Actions.runFromMain();
    }

}
