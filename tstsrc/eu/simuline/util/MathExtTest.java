
package eu.simuline.util;

import eu.simuline.arithmetics.dual.MathContextAbsRel;
import eu.simuline.arithmetics.BuiltInTypes;

import eu.simuline.testhelpers.Actions;
import eu.simuline.testhelpers.Accessor;

import static eu.simuline.testhelpers.Assert.assertEquals;
import static eu.simuline.testhelpers.Assert.assertArraysEquals;

import java.lang.reflect.InvocationTargetException;

import java.math.RoundingMode;

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
	@Test public void testSME() throws Exception {
	    MathExtTest.TEST.testSME();
	}
	@Test public void testRoundAbsRel() throws Exception {
	    MathExtTest.TEST.testRoundAbsRel();
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

    } // testSME()


    public void testRoundAbsRel() {
	testRoundAbsRel(RoundingMode.DOWN);
//	testRoundAbsRel(RoundingMode.UP);
//	testRoundAbsRel(RoundingMode.FLOOR);
//	testRoundAbsRel(RoundingMode.CEILING);
//	testRoundAbsRel(RoundingMode.HALF_DOWN);
//	testRoundAbsRel(RoundingMode.HALF_UP);
//	testRoundAbsRel(RoundingMode.HALF_EVEN);
    }

    private void testRoundAbsRel(RoundingMode rndMode) {
	double num1, num2, num;
	MathContextAbsRel mcF1, mcF2;
	System.out.println("  RoundingMode   : "+rndMode);
	for (int numRed = -41; numRed < 52; numRed++) {
	mcF2 =  MathContextAbsRel.createRel(BuiltInTypes.DOUBLE.mantissaLen()
					    -numRed, 
					    rndMode);//
	    System.out.println(" numRed    : "+numRed);
	for (int i = 0; i < 1000; i++) {
	    System.out.println("  i   : "+i);
	    num1  = Math.random();
	    num1 *= Math.pow(2.0,100*Math.random()-50);
	    num1 *= MathExt.sgn(Math.random()-0.5);

	    mcF1 =  MathContextAbsRel.createAbs(MathExt.expL2(num1)-53+numRed, 
					       RoundingMode.DOWN);//
	    assertEquals(MathExt.round(num1, mcF2), //num1, //
			 MathExt.round(num1, mcF1));
	} // for i
	} // for numRed


    } // testRoundAbsRel(RoundingMode)

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
