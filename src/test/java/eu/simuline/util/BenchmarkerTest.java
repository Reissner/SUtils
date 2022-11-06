package eu.simuline.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
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

  //private static final BenchmarkerTest TEST = new BenchmarkerTest();

  private BenchmarkerTest() {

  }

  public static class TestAll {

    @Before public void setUp() {
	    Benchmarker.reset();
	  }

    @Test 
    public void testTicToc() throws InterruptedException {
      int timeDelta = 4;
      int memDelta = 20;
      int timeMs = 1000;
      int mem0 = 0;
      int memMB = 500;
      int memTolMB = 3;
      int hash1, hash2;
      Benchmarker.Snapshot snap1, snap2;
      byte[] arr;

      // single tic-toc pair: time. 
      hash1 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      snap1 = Benchmarker.mtoc();
      assertEquals("time elapsed out of range. ",
        timeMs, snap1.getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,   snap1.getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hash1,  snap1.hashCode());
      assertTrue("toc not stopped. ", snap1.isStopped());
      assertEquals("Incomplete measurements. ",
        0, Benchmarker.numNestedMeasurements());


      // single tic-toc pairs: memory. 
      hash1 = Benchmarker.mtic();
      arr = new byte[1_000_000*memMB];
      snap1 = Benchmarker.mtoc();
      hash2 = Benchmarker.mtic();
      arr = null;// NOPMD
      assert arr == null;// To calm down the code checker 
      snap2 = Benchmarker.mtoc();

      System.out.println("time ms   allocate: " + snap1.getTimeMs());
      System.out.println("time ms deallocate: " + snap2.getTimeMs());
      assertEquals("memory allocated out of range. ",
        +memMB,   snap1.getMemoryMB(), memTolMB);
      assertEquals("memory freed out of range. ",
        -memMB,   snap2.getMemoryMB(), memTolMB);
      assertEquals("wrong hash. ",
      hash1,  snap1.hashCode());
      assertEquals("wrong hash. ",
      hash2,  snap2.hashCode());
      assertTrue("toc not stopped. ", snap1.isStopped());
      assertTrue("toc not stopped. ", snap2.isStopped());
      assertEquals("Incomplete measurements. ",
      0, Benchmarker.numNestedMeasurements());


      // nested tic-toc pairs
      hash1 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      hash2 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      snap2 = Benchmarker.mtoc();
      Thread.sleep(timeMs);
      snap1 = Benchmarker.mtoc();

      assertEquals("time elapsed out of range. ",
        3*timeMs, snap1.getTimeMs(), timeDelta);
      assertEquals("time elapsed out of range. ",
        1*timeMs, snap2.getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,     snap1.getMemoryMB(), memDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,     snap2.getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hash1,  snap1.hashCode());
      assertEquals("wrong hash. ",
        hash2,  snap2.hashCode());
      assertTrue("toc not stopped. ", snap1.isStopped());
      assertTrue("toc not stopped. ", snap1.isStopped());
      assertEquals("Incomplete measurements. ",
        0, Benchmarker.numNestedMeasurements());


      // pause and resume
      hash1 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      Benchmarker.pause();
      Thread.sleep(timeMs);
      Benchmarker.resume();
      Thread.sleep(timeMs);
      snap1 = Benchmarker.mtoc();
      assertEquals("time elapsed out of range. ",
        2*timeMs, snap1.getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,   snap1.getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hash1,  snap1.hashCode());
      assertTrue("toc not stopped. ", snap1.isStopped());

      // snapshotting 
      hash1 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      snap2 = Benchmarker.snap();
      assertEquals("time elapsed out of range. ",
        1*timeMs, snap2.getTimeMs(), timeDelta);
      Thread.sleep(timeMs);
      snap2 = Benchmarker.snap();
      assertEquals("time elapsed out of range. ",
        2*timeMs, snap2.getTimeMs(), timeDelta);
      Thread.sleep(timeMs);
      snap1 = Benchmarker.mtoc();
      assertEquals("time elapsed out of range. ",
        3*timeMs, snap1.getTimeMs(), timeDelta);
      assertEquals("wrong hash. ",
        hash1,  snap1.hashCode());
      assertTrue("toc not stopped. ", snap1.isStopped());

      // test reset/numNestedMeasurements
      Benchmarker.mtic();
      Benchmarker.mtic();
      Benchmarker.reset();
      assertEquals("Incomplete measurements. ",
        0, Benchmarker.numNestedMeasurements());

    } // testTicToc()

    @SuppressWarnings("PMD.NPathComplexity")
    @Test
    public void testTicTocExceptions() {
      // test exceptions of mtoc()
      Benchmarker.mtic();
      Benchmarker.mtoc();
      try {
        Benchmarker.mtoc();
        assertTrue("IllegalStateException expected. ", false);// NOPMD
        return;
      } catch(IllegalStateException e) {
        assertEquals("Wrong exception message. ",
          "No tic to toc. ", e.getMessage());
      }

      // test exceptions of mtic() 
      Benchmarker.reset();
      Benchmarker.mtic();
      Benchmarker.pause();
      try {
        Benchmarker.mtic();
        assertTrue("IllegalStateException expected. ", false);// NOPMD
      } catch(IllegalStateException e) {
        assertEquals("Wrong exception message. ",
          "Added tic on stopped tic. ", e.getMessage());
      }


      // test exceptions of pause() 
      Benchmarker.reset();
      try {
        Benchmarker.pause();
        assertTrue("IllegalStateException expected. ", false);// NOPMD
        return;
      } catch(IllegalStateException e) {
        assertEquals("Wrong exception message. ",
          "No tic to pause. ", e.getMessage());
      }

      Benchmarker.reset();
      Benchmarker.mtic();
      Benchmarker.pause();
      try {
        Benchmarker.pause();
        assertTrue("IllegalStateException expected. ", false);// NOPMD
      } catch(IllegalStateException e) {
        assertEquals("Wrong exception message. ",
          "Tried to pause already stopped. ", e.getMessage());
      }

      // test exceptions of resume() 
      Benchmarker.reset();
      try {
        Benchmarker.resume();
        assertTrue("IllegalStateException expected. ", false);// NOPMD
      } catch(IllegalStateException e) {
        assertEquals("Wrong exception message. ",
          "No tic to resume. ", e.getMessage());
      }

      Benchmarker.reset();
      Benchmarker.mtic();
      //Benchmarker.pause();
      try {
        Benchmarker.resume();
        assertTrue("IllegalStateException expected. ", false);// NOPMD
      } catch(IllegalStateException e) {
        assertEquals("Wrong exception message. ",
          "Tried to resume already running. ", e.getMessage());
      }


      // test exceptions of snapshot() 
      Benchmarker.reset();
      try {
        Benchmarker.snap();
        assertTrue("IllegalStateException expected. ", false);// NOPMD
      } catch(IllegalStateException e) {
        assertEquals("Wrong exception message. ",
          "No tic to snapshot. ", e.getMessage());
      }

      Benchmarker.reset();
      Benchmarker.mtic();
      Benchmarker.pause();
      try {
        Benchmarker.snap();
        assertTrue("IllegalStateException expected. ", false);// NOPMD
      } catch(IllegalStateException e) {
        assertEquals("Wrong exception message. ",
          "Tried to pause already stopped. ", e.getMessage());
      }
    } // void testTicTocExceptions() 


    @Test
    public void testTicTocMult() throws InterruptedException {
      int timeDelta = 4;
      int memDelta = 20;
      int timeMs = 1000;
      int mem0 = 0;
      int hash1, hash2;
      int[] hashs1;
      Benchmarker.Snapshot snap1, snap2;
      Benchmarker.Snapshot[] snaps1;
      int numTics, numTocs;

      // single tic-toc pair. 
      numTics = 4;
      numTocs = numTics;
      assert numTics == numTocs;
      hashs1 = Benchmarker.mtic(numTics);
      Thread.sleep(timeMs);
      snaps1 = Benchmarker.mtoc(numTocs);

      assertEquals("Wrong number of hashs. ", numTics, hashs1.length);
      assertEquals("Wrong number of snaps. ", numTocs, snaps1.length);
      for (int idx = 0; idx < numTocs; idx++) {
        snap1 = snaps1[idx];
        assertEquals("time elapsed out of range. ",
          timeMs,    snap1.getTimeMs(), timeDelta);
        assertEquals("memory elapsed out of range. ",
          mem0,      snap1.getMemoryMB(), memDelta);
        assertEquals("wrong hash. ",
          hashs1[idx], snap1.hashCode());
        assertTrue("toc not stopped. ", snap1.isStopped());
      }
      snap1 = snaps1[numTocs-1];
      for (int idx = 0; idx < numTocs-1; idx++) {
        assertEquals("time elapsed not the same. ",
          snap1.getTimeMs(),    snaps1[idx].getTimeMs(), 0);
        assertEquals("memory elapsed not the same. ", snap1.getMemoryMB(),
          snaps1[idx].getMemoryMB(), 0);
      }
      assertEquals("Incomplete measurements. ", 
        0, Benchmarker.numNestedMeasurements());

      // single tic, split toc 
      hashs1 = Benchmarker.mtic(4);
      Thread.sleep(timeMs);
      snap1 = Benchmarker.mtoc();
      Thread.sleep(timeMs);
      snaps1 = Benchmarker.mtoc(2);
      Thread.sleep(timeMs);
      snap2 = Benchmarker.mtoc();

      assertEquals("Wrong number of hashs. ", 4, hashs1.length);
      assertEquals("Wrong number of snaps. ", 2, snaps1.length);
      assertEquals("time elapsed out of range. ",
        timeMs,    snap1.getTimeMs(), timeDelta);
      assertEquals("time elapsed out of range. ",
        mem0,      snap1.getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hashs1[3], snap1.hashCode());
      assertTrue("toc not stopped. ", snap1.isStopped());

      assertEquals("time elapsed out of range. ",
        2*timeMs,  snaps1[1].getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,      snaps1[1].getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hashs1[2], snaps1[1].hashCode());
      assertTrue("toc not stopped. ", snaps1[1].isStopped());
      assertEquals("time elapsed out of range. ",
        2*timeMs,  snaps1[0].getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,      snaps1[0].getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hashs1[1], snaps1[0].hashCode());
      assertTrue("toc not stopped. ", snaps1[0].isStopped());

      assertEquals("time elapsed out of range. ",
        3*timeMs,  snap2.getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,      snap2.getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hashs1[0], snap2.hashCode());
      assertTrue("toc not stopped. ", snaps1[1].isStopped());

      assertEquals("Incomplete measurements. ",
        0, Benchmarker.numNestedMeasurements());


      // split tic, single toc 
      hash1 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      hashs1 = Benchmarker.mtic(2);
      Thread.sleep(timeMs);
      hash2 = Benchmarker.mtic();
      Thread.sleep(timeMs);
      snaps1 = Benchmarker.mtoc(4);

      assertEquals("Wrong number of hashs. ", 2, hashs1.length);
      assertEquals("Wrong number of snaps. ", 4, snaps1.length);

      assertEquals("time elapsed out of range. ",
        1*timeMs, snaps1[3].getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,     snaps1[3].getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hash2,    snaps1[3].hashCode());
      assertTrue("toc not stopped. ", snaps1[3].isStopped());

      assertEquals("time elapsed out of range. ",
        2*timeMs,  snaps1[2].getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,      snaps1[2].getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hashs1[1], snaps1[2].hashCode());
      assertTrue("toc not stopped. ", snaps1[2].isStopped());

      assertEquals("time elapsed out of range. ",
        2*timeMs,  snaps1[1].getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,      snaps1[1].getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hashs1[0], snaps1[1].hashCode());
      assertTrue("toc not stopped. ", snaps1[1].isStopped());

      assertEquals("time elapsed out of range. ",
        3*timeMs, snaps1[0].getTimeMs(), timeDelta);
      assertEquals("memory elapsed out of range. ",
        mem0,     snaps1[0].getMemoryMB(), memDelta);
      assertEquals("wrong hash. ",
        hash1,    snaps1[0].hashCode());
      assertTrue("toc not stopped. ", snaps1[0].isStopped());

      assertEquals("Incomplete measurements. ",
        0, Benchmarker.numNestedMeasurements());

   } // testTicTocMult() 

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
