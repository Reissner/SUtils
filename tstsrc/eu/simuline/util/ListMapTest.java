
package eu.simuline.util;

import eu.simuline.testhelpers.GUIRunListener;
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

    ListMap listMap;

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
	this.listMap = new ListMap();
	this.listMap.put(new Integer(3),new Integer(1));
	this.listMap.put(new Integer(2),new Integer(2));
	this.listMap.put(new Integer(1),new Integer(3));
	Assert.assertArraysEquals(new Object[] {
	    new Integer(3), new Integer(2), new Integer(1)
	},
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

	JUnitCore core = new JUnitCore();
	core.addListener(new GUIRunListener());
	core.run(ListMapTest.class);

    }
}

