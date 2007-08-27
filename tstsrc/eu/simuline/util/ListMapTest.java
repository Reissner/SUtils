
package eu.simuline.util;

import eu.simuline.testhelpers.Actions;
//import eu.simuline.testhelpers.Accessor;
import eu.simuline.testhelpers.Assert;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;

@RunWith(Suite.class)
@SuiteClasses({ListMapTest.TestAll.class})
public class ListMapTest {

    ListMap<Integer,Integer> listMap;

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    static ListMapTest TEST = new ListMapTest();

    public static class TestAll {
	@Test public void testPut() {
	    ListMapTest.TEST.testPut();	    
	}
    } // class TestAll


    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    public void testPut() {
	this.listMap = new ListMap<Integer,Integer>();
	this.listMap.put(3,1);
	this.listMap.put(2,2);
	this.listMap.put(1,3);
	Assert.assertArraysEquals(new Object[] {3, 2, 1},
				  this.listMap.keySet().toArray());
    } // testPut 

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(ListMapTest.class);
    }


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {

	Actions.run(ListMapTest.class);

    }
}

