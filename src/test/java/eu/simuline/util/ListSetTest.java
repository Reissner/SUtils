
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
import org.junit.runner.JUnitCore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;



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
	ListSet<Integer> arraySet;

	// empty set with natural ordering 
	arraySet = new ListSet<Integer>();
	assertEquals(0,arraySet.size());
	assertTrue(arraySet.isEmpty());
	assertTrue(!arraySet.contains(3));
	assertTrue(!arraySet.contains(null));
	assertTrue(!arraySet.contains(new Double(0)));
	assertTrue(!arraySet.    iterator().hasNext());
	assertTrue(!arraySet.listIterator().hasNext());
	assertEquals(0,arraySet.toArray().length);
	assertNull(arraySet.comparator());


	// set with natural ordering 
	assertTrue( arraySet.add(3));
	assertTrue( arraySet.add(2));
	assertTrue( arraySet.add(1));
	Assert.assertArraysEquals(new Object[] {1, 2, 3},
				  arraySet.toArray());
	assertTrue(!arraySet.add(2));
	Assert.assertArraysEquals(new Object[] {1, 2, 3	},
				  arraySet.toArray());


	// set with ordering given by a comparator 
	Comparator<Integer> cmp = new Comparator<Integer>() {
	    public int compare(Integer o1,Integer o2) {
		return -o1.compareTo(o2);
	    }
	};
	arraySet = new ListSet<Integer>(cmp);
	assertTrue( arraySet.add(3));
	assertTrue( arraySet.add(2));
	assertTrue( arraySet.add(1));
	Assert.assertArraysEquals(new Object[] {3, 2, 1},
				  arraySet.toArray());
	assertTrue(!arraySet.add(2));
	Assert.assertArraysEquals(new Object[] {3, 2, 1},
				  arraySet.toArray());


	// set with ordering given by successive adding 
	arraySet = ListSet.sortedAsAdded();
	assertTrue( arraySet.add(2));
	assertTrue( arraySet.add(3));
	assertTrue( arraySet.add(1));

	Assert.assertArraysEquals(new Object[] {2, 3, 1},
				  arraySet.toArray());
	assertTrue(!arraySet.add(2));
	Assert.assertArraysEquals(new Object[] {2, 3, 1},
				  arraySet.toArray());


    } // testAdd 

    public void testContains() {
	ListSet<Integer> arraySet;

	// check with natural ordering 
	arraySet = new ListSet<Integer>(asList(new Integer[] {
		    3, 2, 1}));
	Assert.assertArraysEquals(new Object[] {1, 2, 3}, arraySet.toArray());

	assertTrue( arraySet.contains(3));
	assertTrue( arraySet.contains(2));
	assertTrue( arraySet.contains(1));
	assertTrue(!arraySet.contains(0));
	assertTrue(!arraySet.contains(""));

	// check with ordering given by initial list 
	arraySet = ListSet.sortedAsListed(asList(new Integer[] {
		    3, 2, 1}));
	Assert.assertArraysEquals(new Integer[] {3, 2, 1}, arraySet.toArray());

	assertTrue( arraySet.contains(3));
	assertTrue( arraySet.contains(2));
	assertTrue( arraySet.contains(1));
	assertTrue(!arraySet.contains(0));
	assertTrue(!arraySet.contains(""));

    } // testContains 

    public void testContainsAll() {
	ListSet<Integer> arraySet1, arraySet2;

	arraySet1 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	arraySet2 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	assertTrue(arraySet1.containsAll(arraySet2));

	arraySet1 = new ListSet<Integer>(asList(new Integer[] {
	    4, 3, 2, 1}));
	arraySet2 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	assertTrue( arraySet1.containsAll(arraySet2));
	assertTrue(!arraySet2.containsAll(arraySet1));

	arraySet1 = new ListSet<Integer>(asList(new Integer[] {
	    }));
	arraySet2 = new ListSet<Integer>(asList(new Integer[] {
	    4, 3, 2, 1}));
	assertTrue(!arraySet1.containsAll(arraySet2));
	assertTrue( arraySet2.containsAll(arraySet1));

    } // testContainsAll 

    public void testAddAll() {
	ListSet<Integer> arraySet1, arraySet2, arraySet3;

	arraySet1 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	arraySet2 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	arraySet3 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	assertTrue( !arraySet1.addAll(arraySet2));
	assertEquals(arraySet1,arraySet3);

	arraySet1 = new ListSet<Integer>(asList(new Integer[] {
	    3, 2, 1}));
	arraySet2 = new ListSet<Integer>(asList(new Integer[] {
	    4, 2, 1}));
	arraySet3 = new ListSet<Integer>(asList(new Integer[] {
	    4, 3, 2, 1}));
	assertTrue(  arraySet1.addAll(arraySet2));
	assertEquals(arraySet1,arraySet3);
     } // testAddAll

    public void testSubSet() {
	ListSet<Integer> arraySet1, arraySet2;

	arraySet1 = new ListSet<Integer>(asList(new Integer[] {
		    0, 2, 3, 4}));
	Assert.assertArraysEquals(new Object[] {0, 2, 3, 4},
				  arraySet1.toArray());

	arraySet2 = arraySet1.subSet(0, 3);
	Assert.assertArraysEquals(new Object[] {0, 2},
				  arraySet2.toArray());

	arraySet2 = arraySet1.subSet(0, 4);
	Assert.assertArraysEquals(new Object[] {0, 2, 3},
				  arraySet2.toArray());

	arraySet2 = arraySet1.subSet(0, 1);
	Assert.assertArraysEquals(new Object[] {0},
				  arraySet2.toArray());

	arraySet2 = arraySet1.subSet(1, 4);
	Assert.assertArraysEquals(new Object[] {2, 3},
				  arraySet2.toArray());

	arraySet2 = arraySet1.subSet(1, 5);
	Assert.assertArraysEquals(new Object[] {2, 3, 4},
				  arraySet2.toArray());


    } // testSubSet 

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(ListSetTest.class);
    }


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {
	Actions.run(ListSetTest.class);
    }
}

