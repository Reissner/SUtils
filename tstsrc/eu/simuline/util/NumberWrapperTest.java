
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
@SuiteClasses({NumberWrapperTest.TestAll.class})
public class NumberWrapperTest {


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    static NumberWrapperTest TEST = new NumberWrapperTest();


    public static class TestAll {
	@Before public void setUp() {
	    testcase = 1;
	    repetition = 1;
	}
	@Test public void testConstructor() throws Exception {
	    NumberWrapperTest.TEST.testConstructor();	    
	}
	@Test public void testShiftLeft2() throws Exception {
	    NumberWrapperTest.TEST.testShiftLeft2(); 
	}
    } // class TestAll



    static Class getTestedClass() {
	return NumberWrapper.class;
    }

    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    static int testcase;
    static int repetition;
    static long step;



    public void testConstructor() throws Exception {
	testConstructor(NumberWrapper.Dbl   .class);
	testConstructor(NumberWrapper.Long53.class);
    }

    public void testConstructor(Class<? extends NumberWrapper> cls) 
	throws Exception {
	double num;
	NumberWrapper cand, cand2;

	repetition = 10000;
	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    num = Math.random();
	    cand = Accessor.create(cls,num);
	    assertEquals((Object)Double.toString(num),cand.toString());
	    cand2 = cand.copy();
	    assertEquals(cand,cand2);
	}
	System.err.println("cls "+cls+" elapsed: "+
			   (System.currentTimeMillis()-step));
	

    } // testConstructor 

    public void testShiftLeft2() throws Exception {
	testShiftLeft2(NumberWrapper.Dbl   .class);
	testShiftLeft2(NumberWrapper.Long53.class);
    }

    public void testShiftLeft2(Class<? extends NumberWrapper> cls) 
	throws Exception {
	double num, num2, dig;
	NumberWrapper cand, cand2;
	
	num = 0.5;
	cand = Accessor.create(cls,num);
	num2 = 0;
	dig = 0.5;
	while (!cand.isZero()) {
	    num2 += dig*cand.shiftLeft2();
	    dig /= 2;
	}

	assertEquals(num,num2);


	repetition = 1000;
	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    num = Math.random();
	    cand = Accessor.create(cls,num);
	    num2 = 0;
	    dig = 0.5;
	    while (!cand.isZero()) {
		num2 += dig*cand.shiftLeft2();
		dig /= 2;
	    }

	    assertEquals(num,num2);
	}

	System.err.println("cls "+cls+" elapsed: "+
			   (System.currentTimeMillis()-step));
    } // testShiftLeft2 




    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(NumberWrapperTest.class);
    }


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {
/*
	JUnitCore core = new JUnitCore();
	core.addListener(new GUIRunListener());
	core.run(NumberWrapperTest.class);
*/
	Actions.run(NumberWrapperTest.class);



//junit.swingui.TestRunner.main(new String[] {NumberWrapperTest.class.getName()});
    }

}
