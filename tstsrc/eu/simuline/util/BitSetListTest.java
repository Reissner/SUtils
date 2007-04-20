package eu.simuline.util;

import eu.simuline.testhelpers.GUIRunListener;
import eu.simuline.testhelpers.Accessor;
import eu.simuline.testhelpers.Assert;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;




import java.util.BitSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Describe class TestBitSetList here.
 *
 *
 * Created: Mon May 29 21:02:25 2006
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({BitSetListTest.TestAll.class})
public class BitSetListTest {

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    static BitSetListTest TEST = new BitSetListTest();


    public static class TestAll {
	@Test public void testConstr() throws Exception {
	    BitSetListTest.TEST.testConstr();	    
	}
	@Test public void testAdd() {
	    BitSetListTest.TEST.testAdd();	    
	}
	@Test public void testContains() {
	    BitSetListTest.TEST.testContains();	    
	}
    }


    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    public void testConstr() throws Exception {
	BitSetList<Integer> bitSetList;


	bitSetList = new BitSetList<Integer>(25,0);
	assertTrue(bitSetList.isEmpty());
	assertEquals(64,
		     ((BitSet)Accessor.getField(bitSetList,"wrapped")).size());


	bitSetList = new BitSetList<Integer>(Arrays.asList(new Integer[] {
	    new Integer(0), 
	    new Integer(1), 
	    new Integer(1), 
	    new Integer(0), 
	    new Integer(0)
	}));

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0), 
	    new Integer(1), 
	    new Integer(1), 
	    new Integer(0), 
	    new Integer(0)
	},
			    bitSetList.toArray());
	assertEquals(5,
		     ((Integer)Accessor.getField(bitSetList,"size"))
		     .intValue());

    } // testConstr() 

    public void testAdd() {
	BitSetList<Integer> bitSetList;
	bitSetList = new BitSetList<Integer>();

	assertEquals(0,bitSetList.size());
	assertTrue(bitSetList.isEmpty());
	assertTrue(!bitSetList.contains(new Integer(3)));
	try {
	    bitSetList.contains(null);
	    fail("Exception expected");
	} catch (NullPointerException e) {
	    // ok 
	}
	try {
	    bitSetList.contains(new Double(0));
	    fail("Exception expected");
	} catch (ClassCastException e) {
	    // ok 
	}
	assertTrue(!bitSetList.iterator().hasNext());
	assertTrue(!bitSetList.listIterator().hasNext());
	assertEquals(0,bitSetList.toArray().length);


	try {
	    bitSetList.add(null);
	    fail("Exception expected");
	} catch (NullPointerException e) {
	    // ok 
	}

	
	assertTrue(bitSetList.add(new Integer(0)));
	assertTrue(bitSetList.add(new Integer(1)));
	assertTrue(bitSetList.add(new Integer(1)));
	assertTrue(bitSetList.add(new Integer(0)));
	assertTrue(bitSetList.add(new Integer(0)));
	assertEquals(5,bitSetList.size());
	Assert.assertArraysEquals(new Object[] {
	    new Integer(0), 
	    new Integer(1), 
	    new Integer(1), 
	    new Integer(0), 
	    new Integer(0)
	},
			    bitSetList.toArray());


	try {
	    bitSetList.add(6,new Integer(0));
	    fail("Exception expected");
	} catch (IndexOutOfBoundsException e) {
	    // ok 
	}




	bitSetList.add(1,new Integer(0));
	Assert.assertArraysEquals(new Object[] {
	    new Integer(0), 
	    new Integer(0), 
	    new Integer(1), 
	    new Integer(1), 
	    new Integer(0), 
	    new Integer(0)
	},
			    bitSetList.toArray());


	bitSetList.add(6,new Integer(1));
	Assert.assertArraysEquals(new Object[] {
	    new Integer(0), 
	    new Integer(0), 
	    new Integer(1), 
	    new Integer(1), 
	    new Integer(0), 
	    new Integer(0),
	    new Integer(1)
	},
			    bitSetList.toArray());




    } // testAdd() 

    public void testContains() {

	BitSetList<Integer> bitSetList;
	bitSetList = new BitSetList<Integer>();


	assertTrue(!bitSetList.contains(new Integer(3)));
	try {
	    bitSetList.contains(null);
	    fail("Exception expected");
	} catch (NullPointerException e) {
	    // ok 
	}
	try {
	    bitSetList.contains(new Double(0));
	    fail("Exception expected");
	} catch (ClassCastException e) {
	    // ok 
	}


    } // testContains() 

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(BitSetListTest.class);
    }


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {

	JUnitCore core = new JUnitCore();
	core.addListener(new GUIRunListener());
	core.run(BitSetListTest.class);
    }

}
