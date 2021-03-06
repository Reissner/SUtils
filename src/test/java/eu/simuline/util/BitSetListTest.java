package eu.simuline.util;

import eu.simuline.testhelpers.Actions;
import eu.simuline.testhelpers.Accessor;
import eu.simuline.testhelpers.Assert;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
//import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

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
	BitSetList bitSetList;


	bitSetList = new BitSetList(25);
	assertTrue(bitSetList.isEmpty());
	assertEquals(64,
		     ((BitSet)Accessor.getField(bitSetList,"wrapped")).size());


	bitSetList = new BitSetList(Arrays.asList(new Integer[] {
		Integer.valueOf(0), 
		Integer.valueOf(1), 
		Integer.valueOf(1), 
	    Integer.valueOf(0), 
	    Integer.valueOf(0)
	}));

	Assert.assertArraysEquals(new Object[] {
		Integer.valueOf(0), 
		Integer.valueOf(1), 
		Integer.valueOf(1), 
	    Integer.valueOf(0), 
	    Integer.valueOf(0)
	},
			    bitSetList.toArray());
	assertEquals(5,
		     ((Integer)Accessor.getField(bitSetList,"size"))
		     .intValue());

    } // testConstr() 

    public void testEquals() {
	BitSetList bitSetList;
	List<Integer> listCmp;

	bitSetList = new BitSetList(25);
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
	BitSetList bitSetList;
	bitSetList = new BitSetList();

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

	
	assertTrue(bitSetList.add(Integer.valueOf(0)));
	assertTrue(bitSetList.add(Integer.valueOf(1)));
	assertTrue(bitSetList.add(Integer.valueOf(1)));
	assertTrue(bitSetList.add(Integer.valueOf(0)));
	assertTrue(bitSetList.add(Integer.valueOf(0)));
	assertEquals(5,bitSetList.size());
	Assert.assertArraysEquals(new Object[] {
		Integer.valueOf(0), 
		Integer.valueOf(1), 
		Integer.valueOf(1), 
	    Integer.valueOf(0), 
	    Integer.valueOf(0)
	},
			    bitSetList.toArray());


	try {
	    bitSetList.add(6,Integer.valueOf(0));
	    fail("Exception expected");
	} catch (IndexOutOfBoundsException e) {
	    // ok 
	}




	bitSetList.add(1,Integer.valueOf(0));
	Assert.assertArraysEquals(new Object[] {
		Integer.valueOf(0), 
		Integer.valueOf(0), 
	    Integer.valueOf(1), 
	    Integer.valueOf(1), 
	    Integer.valueOf(0), 
	    Integer.valueOf(0)
	},
			    bitSetList.toArray());


	bitSetList.add(6,Integer.valueOf(1));
	Assert.assertArraysEquals(new Object[] {
		Integer.valueOf(0), 
		Integer.valueOf(0), 
	    Integer.valueOf(1), 
	    Integer.valueOf(1), 
	    Integer.valueOf(0), 
	    Integer.valueOf(0),
	    Integer.valueOf(1)
	},
			    bitSetList.toArray());




    } // testAdd() 

    public void testContains() {

	BitSetList bitSetList;
	bitSetList = new BitSetList();


	assertTrue(!bitSetList.contains(Integer.valueOf(3)));
	try {
	    bitSetList.contains(null);
	    fail("Exception expected");
	} catch (NullPointerException e) {
	    // ok 
	}
	// try {
	//     bitSetList.contains(Double.valueOf(0));
	//     fail("Exception expected");
	// } catch (ClassCastException e) {
	//     // ok 
	// }


    } // testContains() 

    public void testSize() {
	BitSetList bitSetList;
	bitSetList = new BitSetList();

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
	BitSetList bitSetList;
	bitSetList = new BitSetList();

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
	BitSetList bitSetList;
	bitSetList = new BitSetList();
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
	BitSetList bitSetList;
	bitSetList = new BitSetList();
	bitSetList.add(1);
	bitSetList.add(1);
	bitSetList.add(0);
	bitSetList.add(0);

	assertEquals(1,bitSetList.get(0).intValue());
	assertEquals(1,bitSetList.get(1).intValue());
	assertEquals(0,bitSetList.get(2).intValue());
	assertEquals(0,bitSetList.get(3).intValue());
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


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {
	Actions.runFromMain();
   }

}
