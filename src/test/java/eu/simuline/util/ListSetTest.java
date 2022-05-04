
package eu.simuline.util;

import eu.simuline.testhelpers.Actions;
//import eu.simuline.testhelpers.Accessor;
import eu.simuline.testhelpers.Assert;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
//import static org.junit.Assert.fail;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.Comparator;
import static java.util.Arrays.asList;



@RunWith(Suite.class)
@SuiteClasses({ListSetTest.TestAll.class})
public class ListSetTest {


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    static final ListSetTest TEST = new ListSetTest();

    public static class TestAll {
	@Test public void testAdd() {
	    ListSetTest.TEST.testAdd();	    
	}
	@Test public void testContains() {
	    ListSetTest.TEST.testContains();	    
	}
	@Test public void testContainsAll() {
	    ListSetTest.TEST.testContainsAll();	    
	}
	@Test public void testAddAll() {
	    ListSetTest.TEST.testAddAll();
	}
	@Test public void testSubSet() {
	    ListSetTest.TEST.testSubSet();
	}
    } // class TestAll



    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    public void testAdd() {
	ListSet<Integer> listSet;

	// empty set with natural ordering 
	listSet = new ListSet<Integer>();
	assertEquals(0,listSet.size());
	assertTrue(listSet.isEmpty());
	assertTrue(!listSet.contains(3));
	assertTrue(!listSet.contains(null));
	//assertTrue(!listSet.contains(Double.valueOf(0)));
	assertTrue(!listSet.    iterator().hasNext());
	assertTrue(!listSet.listIterator().hasNext());
	assertEquals(0,listSet.toArray().length);
	assertNull(listSet.comparator());


	// set with natural ordering 
	assertTrue( listSet.add(3));
	assertTrue( listSet.add(2));
	assertTrue( listSet.add(1));
	Assert.assertArraysEquals(new Object[] {1, 2, 3},
				  listSet.toArray());
	assertTrue(!listSet.add(2));
	Assert.assertArraysEquals(new Object[] {1, 2, 3	},
				  listSet.toArray());


	// set with ordering given by a comparator 
	Comparator<Integer> cmp = new Comparator<Integer>() {
	    public int compare(Integer o1,Integer o2) {
		return -o1.compareTo(o2);
	    }
	};
	listSet = new ListSet<Integer>(cmp);
	assertTrue( listSet.add(3));
	assertTrue( listSet.add(2));
	assertTrue( listSet.add(1));
	Assert.assertArraysEquals(new Object[] {3, 2, 1},
				  listSet.toArray());
	assertTrue(!listSet.add(2));
	Assert.assertArraysEquals(new Object[] {3, 2, 1},
				  listSet.toArray());


	// set with ordering given by successive adding 
	listSet = ListSet.sortedAsAdded();
	assertTrue( listSet.add(2));
	assertTrue( listSet.add(3));
	assertTrue( listSet.add(1));

	Assert.assertArraysEquals(new Object[] {2, 3, 1},
				  listSet.toArray());
	assertTrue(!listSet.add(2));
	Assert.assertArraysEquals(new Object[] {2, 3, 1},
				  listSet.toArray());


    } // testAdd 

    public void testContains() {
	ListSet<Integer> listSet;

	// check with natural ordering 
	listSet = new ListSet<Integer>(asList(new Integer[] {
		    3, 2, 1}));
	Assert.assertArraysEquals(new Object[] {1, 2, 3}, listSet.toArray());

	assertTrue( listSet.contains(3));
	assertTrue( listSet.contains(2));
	assertTrue( listSet.contains(1));
	assertTrue(!listSet.contains(0));
	//assertTrue(!listSet.contains(""));

	// check with ordering given by initial list 
	listSet = ListSet.sortedAsListed(asList(new Integer[] {
		    3, 2, 1}));
	Assert.assertArraysEquals(new Object[] {3, 2, 1}, listSet.toArray());

	assertTrue( listSet.contains(3));
	assertTrue( listSet.contains(2));
	assertTrue( listSet.contains(1));
	assertTrue(!listSet.contains(0));
	//assertTrue(!listSet.contains(""));

    } // testContains 

    public void testContainsAll() {
	ListSet<Integer> listSet1, listSet2;

	listSet1 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	listSet2 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	assertTrue(listSet1.containsAll(listSet2));

	listSet1 = new ListSet<Integer>(asList(new Integer[] {
	    4, 3, 2, 1}));
	listSet2 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	assertTrue( listSet1.containsAll(listSet2));
	assertTrue(!listSet2.containsAll(listSet1));

	listSet1 = new ListSet<Integer>(asList(new Integer[] {
	    }));
	listSet2 = new ListSet<Integer>(asList(new Integer[] {
	    4, 3, 2, 1}));
	assertTrue(!listSet1.containsAll(listSet2));
	assertTrue( listSet2.containsAll(listSet1));

    } // testContainsAll 

    public void testAddAll() {
	ListSet<Integer> listSet1, listSet2, listSet3;

	listSet1 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	listSet2 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	listSet3 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	assertTrue( !listSet1.addAll(listSet2));
	assertEquals(listSet1,listSet3);

	listSet1 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	listSet2 = new ListSet<Integer>(asList(new Integer[] {
	    4, 2, 1}));
	listSet3 = new ListSet<Integer>(asList(new Integer[] {
	    4, 3, 2, 1}));
	assertTrue(  listSet1.addAll(listSet2));
	assertEquals(listSet1,listSet3);
     } // testAddAll

    public void testSubSet() {
	ListSet<Integer> listSet1, listSet2;

	listSet1 = new ListSet<Integer>(asList(new Integer[] {
		    0, 2, 3, 4}));
	Assert.assertArraysEquals(new Object[] {0, 2, 3, 4},
				  listSet1.toArray());

	listSet2 = listSet1.subSet(0, 3);
	Assert.assertArraysEquals(new Object[] {0, 2},
				  listSet2.toArray());

	listSet2 = listSet1.subSet(0, 4);
	Assert.assertArraysEquals(new Object[] {0, 2, 3},
				  listSet2.toArray());

	listSet2 = listSet1.subSet(0, 1);
	Assert.assertArraysEquals(new Object[] {0},
				  listSet2.toArray());

	listSet2 = listSet1.subSet(1, 4);
	Assert.assertArraysEquals(new Object[] {2, 3},
				  listSet2.toArray());

	listSet2 = listSet1.subSet(1, 5);
	Assert.assertArraysEquals(new Object[] {2, 3, 4},
				  listSet2.toArray());


    } // testSubSet 

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

