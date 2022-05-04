
package eu.simuline.util;

import eu.simuline.testhelpers.Actions;
import eu.simuline.testhelpers.Accessor;
import static eu.simuline.testhelpers.Assert.assertEquals;
import static eu.simuline.testhelpers.Assert.assertArraysEquals;

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;


import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


import java.lang.reflect.InvocationTargetException;

@RunWith(Suite.class)
@SuiteClasses({ArraysExtTest.TestAll.class})
public class ArraysExtTest {


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    static ArraysExtTest TEST = new ArraysExtTest();


    public static class TestAll {
	@Before public void setUp() {
	    testcase = 1;
	    repetition = 1;
	}
	// @Test public void testGetArrayCls() {
	//     ArraysExtTest.TEST.testGetArrayCls();	    
	// }
	@Test public void testCreateEmptyArray() 
	    throws InvocationTargetException {
	    ArraysExtTest.TEST.testCreateEmptyArray();
	}
	@Test public void testUNWrap() {
	    ArraysExtTest.TEST.testUNWrap();	    
	}
    } // class TestAll



    static Class<?> getTestedClass() {
	return ArraysExt.class;
    }

    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    static int testcase;
    static int repetition;
    static long step;



    // public void testGetArrayCls() {

    // 	Class<?> result = null;

    // 	// testcase 1
    // 	//
    // 	// dim = 0 
    // 	//
    // 	step = System.currentTimeMillis();
    // 	for (int i = 0; i < repetition; i++) {
    // 	    result = ArraysExt.getArrayCls(Boolean.TYPE,0);
    // 	}
    // 	step = System.currentTimeMillis()-step;

    // 	assertEquals(Boolean.TYPE,result);
    // 	//reportTestCase(" getArrayCls:                 ");


    // 	// testcase 2
    // 	//
    // 	// dim = 0 
    // 	//
    // 	step = System.currentTimeMillis();
    // 	for (int i = 0; i < repetition; i++) {
    // 	    result = ArraysExt.getArrayCls(Boolean.TYPE,3);
    // 	}
    // 	step = System.currentTimeMillis()-step;

    // 	assertEquals(new boolean[][][] {}.getClass(),result);
    // 	//reportTestCase(" getArrayCls:                 ");


    // 	// testcase 3
    // 	//
    // 	// dim = 0 
    // 	//
    // 	step = System.currentTimeMillis();
    // 	for (int i = 0; i < repetition; i++) {
    // 	    result = ArraysExt.getArrayCls(Boolean.TYPE,2);
    // 	}
    // 	step = System.currentTimeMillis()-step;

    // 	assertEquals(new boolean[][] {}.getClass(),result);
    // 	//reportTestCase(" getArrayCls:                 ");

    // } // testGetArrayCls 

    /**
     * Tests for methods {@link ArraysExt#createWrappedEmptyArray} 
     * and for {@link ArraysExt#createUnWrappedEmptyArray}. 
     *
     * @throws InvocationTargetException 
     *    because of invokcation via reflection. 
     */
    public void testCreateEmptyArray() throws InvocationTargetException {

	Object emptyArray;
	Object emptyArrayConv = null;

	// testcase 1
	//
	// array but not elementary. 
	emptyArray = new Integer[] {};

	step = System.currentTimeMillis();
	try {
	    Accessor.invokeStatic(getTestedClass(),
				  "createWrappedEmptyArray",
				  emptyArray);
	    fail("exception expected. ");
	} catch(InvocationTargetException e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals("java.lang.IllegalArgumentException: " + 
			 "Expected primitive type but not void; " + 
			 "found class java.lang.Integer. ",
			 e.getCause().toString());
	}
	//reportTestCase(" createWrappedEmptyArray:     ");


	// testcase 2
	//
	// no array at all. 
	emptyArray = Integer.valueOf(0);

	step = System.currentTimeMillis();
	try {
	    Accessor.invokeStatic(getTestedClass(),
				  "createWrappedEmptyArray",
				  emptyArray);
	    fail("exception expected. ");
	} catch(InvocationTargetException e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals("java.lang.AssertionError",
			 e.getCause().toString());
	}
	//reportTestCase(" createWrappedEmptyArray:     ");


	// testcase 3
	//
	// empty array; dimension = 1. 
	emptyArray = new int[] {};

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    emptyArrayConv = Accessor.invokeStatic(getTestedClass(),
						   "createWrappedEmptyArray",
						   emptyArray);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new Integer[] {}, emptyArrayConv);
	//reportTestCase(" createWrappedEmptyArray:  ");


	// testcase 4
	//
	// empty ay array; dimension > 1. 
	emptyArray = new int[][][] {};

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    emptyArrayConv = Accessor.invokeStatic(getTestedClass(),
						   "createWrappedEmptyArray",
						   emptyArray);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new Integer[][][] {}, emptyArrayConv);
	//reportTestCase(" createWrappedEmptyArray:     ");


	testcase = 1;

	// testcase 1
	//
	// array but no wrapper type entries. 
	emptyArray = new Object[] {};

	step = System.currentTimeMillis();
	try {
	    Accessor.invokeStatic(getTestedClass(),
				  "createUnWrappedEmptyArray",
				  emptyArray);
	    fail("exception expected. ");
	} catch(Exception e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals("java.lang.IllegalArgumentException: " + 
			 "Expected wrapper class of primitive type " + 
			 "but not void; found class java.lang.Object. ",
			 e.getCause().toString());
	}
	//reportTestCase(" createUnWrappedEmptyArray:   ");


	// testcase 2
	//
	// empty array; dimension = 1. 
	emptyArray = new Integer[] {};

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    emptyArrayConv = Accessor.invokeStatic(getTestedClass(),
						   "createUnWrappedEmptyArray",
						   emptyArray);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new int[] {},
				  emptyArrayConv);
	//reportTestCase(" createWrappedEmptyArray:     ");


	// testcase 3
	//
	// empty array; dimension > 1. 
	emptyArray = new Integer[][][] {};

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    emptyArrayConv = Accessor.invokeStatic(getTestedClass(),
						   "createUnWrappedEmptyArray",
						   emptyArray);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new int[][][] {},
				  emptyArrayConv);
	//reportTestCase(" createWrappedEmptyArray:     ");

    } // testCreateEmptyArray 

    /**
     * Tests for methods <code>wrapArray</code> and <code>unWrapArray</code>. 
     */
    public void testUNWrap() {

	//Integer[] wrapped, wrappedCmp;
	Object[] dwrapped;//, dwrappedCmp;

	//int[] unWrapped, unWrappedCmp;
	Object dunWrapped, dunWrappedCmp;


	testcase = 1;

	// testcase 1
	//
	// non-elementary entry type, empty array. 
	dunWrapped = Integer.valueOf(0);

	step = System.currentTimeMillis();
	try {
	    ArraysExt.wrapArray(dunWrapped);
	    fail("exception expected. ");
	} catch(Exception e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals("java.lang.IllegalArgumentException: " + 
			 "Required an array; found " + dunWrapped + ". ",
			 e.toString());
	}
	//reportTestCase(" wrapArray:            ");


	// testcase 2
	//
	// non-elementary entry type, empty array. 
	dunWrapped = new Integer[][] {};

	step = System.currentTimeMillis();
	try {
	    ArraysExt.wrapArray(dunWrapped);
	    fail("exception expected. ");
	} catch(Exception e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals("java.lang.IllegalArgumentException: " + 
			 "Expected primitive type but not void; " + 
			 "found class java.lang.Integer. ",
			 e.toString());
	}
	//reportTestCase(" wrapArray:            ");


	// testcase 3
	//
	// non-elementary entry type, empty array. 
	dunWrapped = new int[][] {};
	dwrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dwrapped = (Integer[][])ArraysExt.wrapArray(dunWrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new Integer[][] {}, dwrapped);
	//reportTestCase(" wrapArray:            ");


	// testcase 4
	//
	// non-elementary entry type, non empty array of empty array. 
	dunWrapped = new int[][] {
	    new int[] {}
	};
	dwrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dwrapped = (Integer[][])ArraysExt.wrapArray(dunWrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new Integer[][] {
	    new Integer[] {}
	}, dwrapped);
	//reportTestCase(" wrapArray:            ");


	// testcase 5
	//
	// non-elementary entry type, non empty array of two empty arrays. 
	dunWrapped = new int[][] {
	    new int[] {},
	    new int[] {}
	};
	dwrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dwrapped = (Integer[][])ArraysExt.wrapArray(dunWrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new Integer[][] {
	    new Integer[] {},
	    new Integer[] {}
	}, dwrapped);
	//reportTestCase(" wrapArray:            ");


	// testcase 6
	//
	// elementary entry type, empty array. 
	dunWrapped = new int[] {};
	dwrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dwrapped = (Integer[])ArraysExt.wrapArray(dunWrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new Integer[] {}, dwrapped);
	//reportTestCase(" wrapArray:            ");


	// testcase 7
	//
	// elementary entry type, array with one element. 
	dunWrapped = new int[] {0};
	dwrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dwrapped = (Integer[])ArraysExt.wrapArray(dunWrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new Integer[] {Integer.valueOf(0)}, dwrapped);
	//reportTestCase(" wrapArray:            ");


	// testcase 8
	//
	// elementary entry type, array with more than one element. 
	dunWrapped = new int[] {0,1,2,3};
	dwrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dwrapped = (Integer[])ArraysExt.wrapArray(dunWrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new Integer[] {
		Integer.valueOf(0),
		Integer.valueOf(1),
		Integer.valueOf(2),
		Integer.valueOf(3)
		},
				  dwrapped);
	//reportTestCase(" wrapArray:            ");


	// testcase 9
	//
	// elementary entry type, array with more than one element. 
	//unWrapped = new int[] {0, 1, 2, 3};
	dunWrapped = new int[][] {
	    new int[] {},
	    new int[] {0},
	    new int[] {0,1},
	    new int[] {0,1,2},
	};
	dwrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dwrapped = (Integer[][])ArraysExt.wrapArray(dunWrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new Integer[][] {
	    new Integer[] {},
	    new Integer[] {Integer.valueOf(0)},
	    new Integer[] {Integer.valueOf(0),Integer.valueOf(1)},
	    new Integer[] {Integer.valueOf(0),Integer.valueOf(1),Integer.valueOf(2)},
	}, dwrapped);
	//reportTestCase(" wrapArray:            ");


	testcase = 1;

	// testcase 1
	//
	// elementary entry type, empty array. 
	dwrapped = new int[][] {};

	step = System.currentTimeMillis();
	try {
	    ArraysExt.unWrapArray(dwrapped);
	    fail("exception expected. ");
	} catch(Exception e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals("java.lang.IllegalArgumentException: " + 
			 "Expected wrapper class of primitive type " + 
			 "but not void; found int. ",
			 e.toString());
	}
	//reportTestCase(" unWrapArray:          ");


	// testcase 2
	//
	// non-elementary entry type, empty array. 
	dwrapped = new Integer[][] {};
	dunWrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dunWrapped = ArraysExt.unWrapArray(dwrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new int[][] {}, dunWrapped);
	//reportTestCase(" unWrapArray:          ");


	// testcase 3
	//
	// non-elementary entry type, non empty array of empty array. 
	dwrapped = new Integer[][] {
	    new Integer[] {}
	};
	dunWrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dunWrapped = ArraysExt.unWrapArray(dwrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new int[][] {
	    new int[] {}
	}, dunWrapped);
	//reportTestCase(" unWrapArray:          ");


	// testcase 4
	//
	// non-elementary entry type, non empty array of two empty arrays. 
	dwrapped = new Integer[][] {
	    new Integer[] {},
	    new Integer[] {}
	};
	dunWrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dunWrapped = ArraysExt.unWrapArray(dwrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new int[][] {
	    new int[] {},
	    new int[] {}
	}, dunWrapped);
	//reportTestCase(" unWrapArray:          ");


	// testcase 5
	//
	// elementary entry type, empty array. 
	dwrapped = new Integer[] {};
	dunWrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dunWrapped = ArraysExt.unWrapArray(dwrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new int[] {}, dunWrapped);
	//reportTestCase(" unWrapArray:          ");


	// testcase 6
	//
	// elementary entry type, array with one element. 
	dwrapped = new Integer[] {Integer.valueOf(0)};
	dunWrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dunWrapped = ArraysExt.unWrapArray(dwrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new int[] {0}, dunWrapped);
	//reportTestCase(" unWrapArray:          ");


	// testcase 7
	//
	// elementary entry type, array with more than one element. 
	dwrapped = new Integer[] {
		Integer.valueOf(0),
		Integer.valueOf(1),
		Integer.valueOf(2),
		Integer.valueOf(3)
		};
	dunWrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dunWrapped = ArraysExt.unWrapArray(dwrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new int[] {0,1,2,3},dunWrapped);
	//reportTestCase(" unWrapArray:          ");


	// testcase 8
	//
	// elementary entry type, array with more than one element. 
	dwrapped = new Integer[][] {
	    new Integer[] {},
	    new Integer[] {Integer.valueOf(0)},
	    new Integer[] {Integer.valueOf(0),Integer.valueOf(1)},
	    new Integer[] {Integer.valueOf(0),Integer.valueOf(1),Integer.valueOf(2)},
	};
	dunWrapped = null;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    dunWrapped = ArraysExt.unWrapArray(dwrapped);
	}
	step = System.currentTimeMillis()-step;

	assertArraysEquals(new int[][] {
	    new int[] {},
	    new int[] {0},
	    new int[] {0,1},
	    new int[] {0,1,2},
	}, dunWrapped);
	//reportTestCase(" unWrapArray:          ");

    } // testUNWrap 


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {
	Actions.runFromMain();
    }

}

