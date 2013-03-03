package eu.simuline.util;

import eu.simuline.testhelpers.Actions;
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
import java.util.List;

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
	@Test public void testEquals() {
	    BitSetListTest.TEST.testEquals();	    
	}
	@Test public void testAdd() {
	    BitSetListTest.TEST.testAdd();	    
	}
	@Test public void testContains() {
	    BitSetListTest.TEST.testContains();	    
	}
	@Test public void testSize() {
	    BitSetListTest.TEST.testSize();	    
	}
	@Test public void testRemove() {
	    BitSetListTest.TEST.testRemove();	    
	}
	@Test public void testSet() {
	    BitSetListTest.TEST.testSet();	    
	}
	@Test public void testGet() {
	    BitSetListTest.TEST.testGet();	    
	}
    }


    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    public void testConstr() throws Exception {
	BitSetList<Integer> bitSetList;


	bitSetList = new BitSetList<Integer>(25);
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

    public void testEquals() {
	BitSetList<Integer> bitSetList;
	List<Integer> listCmp;

	bitSetList = new BitSetList<Integer>(25);
	listCmp = new ArrayList<Integer>();

	bitSetList.add(0);
	bitSetList.add(1);
	bitSetList.add(0);
	bitSetList.add(0);
	bitSetList.add(1);

	listCmp.add(0);
	listCmp.add(1);
	listCmp.add(0);
	listCmp.add(0);
	listCmp.add(1);

	assertEquals(listCmp,bitSetList);


	bitSetList.remove(3);
	listCmp.remove(3);
	assertEquals(listCmp,bitSetList);

	bitSetList.add(3,1);
	listCmp.add(3,1);
	assertEquals(listCmp,bitSetList);

	bitSetList.add(3,0);
	listCmp.add(3,0);
	assertEquals(listCmp,bitSetList);

    } // testEquals

    public void testAdd() {
	BitSetList<Integer> bitSetList;
	bitSetList = new BitSetList<Integer>();

	assertEquals(0,bitSetList.size());
	assertTrue(bitSetList.isEmpty());
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

    public void testSize() {
	BitSetList<Integer> bitSetList;
	bitSetList = new BitSetList<Integer>();

	assertEquals(0,bitSetList.size());

	bitSetList.add(1);
	bitSetList.add(1);
	bitSetList.add(0);
	bitSetList.add(0);
	assertEquals(4,bitSetList.size());
	bitSetList.remove(0);
	assertEquals(3,bitSetList.size());
	assertEquals("[1, 0, 0]",bitSetList.toString());

    }

    public void testRemove() {
	BitSetList<Integer> bitSetList;
	bitSetList = new BitSetList<Integer>();

	try {
	    bitSetList.remove(0);
	    fail();
	} catch (IndexOutOfBoundsException e) {
	    // ok
	}

	bitSetList.add(1);
	bitSetList.add(1);
	bitSetList.add(0);
	bitSetList.add(0);

	bitSetList.remove(0);
	bitSetList.remove(2);

assertEquals("[1, 0]",bitSetList.toString());
assertEquals(2,bitSetList.size());
    }

    public void testSet() {
	BitSetList<Integer> bitSetList;
	bitSetList = new BitSetList<Integer>();
	try {
	    bitSetList.set(0,1);
	    fail();
	} catch (IndexOutOfBoundsException e) {
	    // ok
	}

	bitSetList.add(1);
	bitSetList.add(1);
	bitSetList.add(0);
	bitSetList.add(0);

	bitSetList.set(3,1);
	bitSetList.set(0,0);
	bitSetList.set(1,1);
assertEquals("[0, 1, 0, 1]",bitSetList.toString());
assertEquals(4,bitSetList.size());

    }

    public void testGet() {
	BitSetList<Integer> bitSetList;
	bitSetList = new BitSetList<Integer>();
	bitSetList.add(1);
	bitSetList.add(1);
	bitSetList.add(0);
	bitSetList.add(0);

	assertEquals(1,bitSetList.get(0));
	assertEquals(1,bitSetList.get(1));
	assertEquals(0,bitSetList.get(2));
	assertEquals(0,bitSetList.get(3));
	try {
	    bitSetList.get(-1);
	    fail();
	} catch (IndexOutOfBoundsException  e) {
	    // ok
	}
	try {
	    bitSetList.get(4);
	    fail();
	} catch (IndexOutOfBoundsException  e) {
	    // ok
	}

    }

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

	Actions.run(BitSetListTest.class);
    }

}
