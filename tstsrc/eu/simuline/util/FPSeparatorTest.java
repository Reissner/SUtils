package eu.simuline.util;

import eu.simuline.arithmetics.MathExt;

import eu.simuline.testhelpers.GUIRunListener;
import eu.simuline.testhelpers.Actions;
//import eu.simuline.testhelpers.Accessor;
//import static eu.simuline.testhelpers.Assert;


import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
//import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;

import java.math.BigDecimal;


/**
 * FPSeparatorTest.java
 *
 *
 * Created: Wed Jul 13 18:10:28 2005
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({FPSeparatorTest.TestAll.class})
public class FPSeparatorTest {

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    static FPSeparatorTest TEST = new FPSeparatorTest();

    public static class TestAll {
	@Test public void testConstr() {
	    FPSeparatorTest.TEST.testConstr();
	}
    } // class TestAll 

    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    public void testConstr() {
	FPSeparator sep;
	double num;

	num = Double.NaN;
	try {
	    new FPSeparator(num);
	    fail("Exception expected. ");
	} catch (IllegalArgumentException e) {
	    assertEquals("No NaN allowed but found " + num + ". ",
			 e.getMessage());
	} // end of try-catch
	


	num = 1.0/0.0;
	try {
	    new FPSeparator(num);
	    fail("Exception expected. ");
	} catch (IllegalArgumentException e) {
	    assertEquals("Only finite values allowed but found " + num + ". ",
			 e.getMessage());
	}


	num = 0.0;
	try {
	    new FPSeparator(num);
	    fail("Exception expected. ");
	} catch (IllegalArgumentException e) {
	    assertEquals("Only nonzero values allowed but found " + num + ". ",
			 e.getMessage());
	}



	num = 4.0;
	sep = new FPSeparator(num);
	assertEquals(1,sep.sign());
	assertEquals(3,sep.exp());
	assertEquals(0.5,sep.mantissa(),0.0);



	for (int i = 0; i < 1000; i++) {
	    num = Math.random();
	    num *= Math.pow(2,100*Math.random());
	    num *= MathExt.sgn(Math.random()-0.5);
	    sep = new FPSeparator(num);
	    assertEquals(num,
			 sep.sign()*sep.mantissa()*Math.pow(2,sep.exp()));
	}
    } // testConstr 


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(FPSeparatorTest.class);
    }


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {
	Actions.run(FPSeparatorTest.class);
    }

} // FPSeparatorTest
