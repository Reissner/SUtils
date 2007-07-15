
package eu.simuline.util;

import eu.simuline.testhelpers.Actions;
import eu.simuline.testhelpers.Accessor;
import static eu.simuline.testhelpers.Assert.assertEquals;
import static eu.simuline.testhelpers.Assert.assertArraysEquals;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;


import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;

import java.lang.reflect.InvocationTargetException;

@RunWith(Suite.class)
@SuiteClasses({MathExtTest.TestAll.class})
public class MathExtTest {


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    static MathExtTest TEST = new MathExtTest();


    public static class TestAll {
	@Before public void setUp() {
	    testcase = 1;
	    repetition = 1;
	}
	@Test public void test() throws Exception {
	    MathExtTest.TEST.testSME();	    
	}
    } // class TestAll



    static Class getTestedClass() {
	return MathExt.class;
    }

    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    static int testcase;
    static int repetition;
    static long step;



    public void testSME() {
	repetition = 100;
	double num;
	for (int i = 0; i < repetition; i++) {
	    num = Math.random();
//System.out.println("i: "+i);
	    assertEquals(num, 
			 MathExt.sgn(num)*
			 MathExt.mantissaL(num)*Math.pow(2,
							 MathExt.expL(num)));
	}


    }


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(MathExtTest.class);
    }


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {
	Actions.run(MathExtTest.class);
    }

}
