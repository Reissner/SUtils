package eu.simuline.util;

import eu.simuline.testhelpers.Actions;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Tests for class TwoSidedList. 
 *
 *
 * Created: Mon Aug 27 11:15:48 2007
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({TwoSidedListTest.TestAll.class})
public class TwoSidedListTest {

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    private static final TwoSidedListTest TEST = new TwoSidedListTest();

    public static class TestAll {

	@Test public void testEquals() {
	    TEST.testEquals();
	}

	@Test public void testConstructors() {
	    TEST.testConstructors();
	}

	@Test public void testAdd() {
	    TEST.testAdd();
	}

	@Test public void testRemove() {
	    TEST.testRemove();
	}

	@Test public void testAddAll() {
	    TEST.testAddAll();
	}

	@Test public void testIndexOf() {
	    TEST.testIndexOf();
	}

	@Test public void testSetGet() {
	    TEST.testSetGet();
	}
	@Test public void testRemoveAll() {
	    TEST.testRemoveAll();
	}
	@Test public void testRetainAll() {
	    TEST.testRetainAll();
	}
	@Test public void testSubList() {
	    TEST.testSubList();
	}
	@Test public void testListIterator() {
	    TEST.testListIterator();
	}
    } // class TestAll 

    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */


    void testEquals() {
	TwoSidedList<Integer> tsList1,tsList2;

	tsList1 = new TwoSidedList<Integer>(0);
	tsList2 = new TwoSidedList<Integer>(1);
	assertNotEquals(tsList1, tsList2);
    }

    // contains tests for constructors and for factory methods 
    void testConstructors() {
	List<         Integer> list;
	List<? extends Number> list2;
	TwoSidedList<Integer> tsList;//, tsListCmp;
	TwoSidedList<Number>  tsList2;//, tsListCmp2;
	    
	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	// create a deep copy and modify the original 
	list2 = list;
	tsList2 = TwoSidedList.create(list2,3);
	tsList.remove(3,TwoSidedList.Direction.Right2Left);

	// test that the copy is not affected 
	assertEquals("", Arrays.asList(new Integer[] {3,4,5}),tsList2.list());
    }

    void testAdd() {
	List<Integer> list;
	TwoSidedList<Integer> tsList, tsListCmp;


	// test addFirst and addLast 

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	// add something 
	tsList.addLast(6);
	tsList.addFirst(2);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    2,3,4,5,6
	}));
	tsListCmp = new TwoSidedList<Integer>(list,2);
	// compare 
	assertEquals("", tsListCmp, tsList);


	// test add(index,E,Direction)

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	// add something 
	tsList.add(4,-4,TwoSidedList.Direction.Left2Right);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,-4,4,5}));
	tsListCmp = new TwoSidedList<Integer>(list,3);
	// compare 
	assertEquals("", tsListCmp, tsList);


	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	tsList.add(4,-4,TwoSidedList.Direction.Right2Left);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,-4,5}));
	tsListCmp = new TwoSidedList<Integer>(list,2);
	// compare 
	assertEquals("", tsListCmp, tsList);

    } // testAdd 

    void testAddAll() {
	List<Integer> list;
	TwoSidedList<Integer> tsList, tsListCmp;//tsListAdd, 
	boolean changed;

	// test addAllFirst and addAllLast 

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	// add something 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {1,2}));
	changed = tsList.addAllFirst(list);
	assertTrue("", changed);
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {6,7,8}));
	changed = tsList.addAllLast(list);
	assertTrue("", changed);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    1,2,3,4,5,6,7,8
	}));
	tsListCmp = new TwoSidedList<Integer>(list,1);
	// compare 
	assertEquals("", tsListCmp, tsList);


	// test addAll(index,coll,Direction)

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	// add something 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {1,2}));
	changed = tsList.addAll(4,list,TwoSidedList.Direction.Left2Right);
	assertTrue("", changed);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,1,2,4,5}));
	tsListCmp = new TwoSidedList<Integer>(list,3);
	// compare 
	assertEquals("", tsListCmp, tsList);


	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	// add something 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {1,2}));
	changed = tsList.addAll(4,list,TwoSidedList.Direction.Right2Left);
	assertTrue("", changed);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,4,5,1,2}));
	tsListCmp = new TwoSidedList<Integer>(list,1);
	// compare 
	assertEquals("", tsListCmp, tsList);


    } // testAddAll() 



    void testIndexOf() {
	List<Integer> list;
	TwoSidedList<Integer> tsList;

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,4,6}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer 4 occurs twice. 

	// check if obj \notin tsList 
	assertEquals("", 2,tsList.indexOf(-1));

	// check if obj \in tsList 
	assertEquals("", 4,tsList.indexOf(4));

	// check if obj \notin tsList 
	assertEquals("", 2,tsList.lastIndexOf(-1));

	// check if obj \in tsList 
	assertEquals("", 5,tsList.lastIndexOf(4));

    } // testIndexOf


    void testSetGet() {
	List<Integer> list;
	TwoSidedList<Integer> tsList;

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5,6}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	assertEquals("", 4,tsList.get(4)  .intValue());
	assertEquals("", 4,tsList.set(4,8).intValue());
	assertEquals("", 8,tsList.get(4)  .intValue());

	tsList.shiftRight(1);
	assertEquals("", 8,tsList.get(5).intValue());
    }

    void testRemove() {
	List<Integer> list;
	TwoSidedList<Integer> tsList, tsListCmp;
	int removed;

	// test removeFirst and removeLast 

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,2,4,3,5,2
	}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	// remove something 
	tsList.removeLast(3);
	tsList.removeFirst(2);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,4,5,2
	}));
	tsListCmp = new TwoSidedList<Integer>(list,4);
	// compare 
	assertEquals("", tsListCmp, tsList);

	// test remove(index,E,Direction)

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	// remove something 
	removed = tsList.remove(4,TwoSidedList.Direction.Left2Right);
	assertEquals("", 4, removed);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,5}));
	tsListCmp = new TwoSidedList<Integer>(list,3);
	// compare 
	assertEquals("", tsListCmp, tsList);


	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,4,5}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	removed = tsList.remove(4,TwoSidedList.Direction.Right2Left);
	assertEquals("", 4, removed);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {3,5}));
	tsListCmp = new TwoSidedList<Integer>(list,4);
	// compare 
	assertEquals("", tsListCmp, tsList);

    } // testRemove 


    void testRemoveAll() {
	List<Integer> list;
	TwoSidedList<Integer> tsList, tsListCmp;//tsListAdd, 
	boolean changed;

	// test removeAll(coll,Direction)

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,4,5,4,7,8
	}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, mostly integer n is at the nth place 

	// remove something 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {4,8}));
	changed = tsList.removeAll(list,TwoSidedList.Direction.Left2Right);
	assertTrue("", changed);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,5,7
	}));
	tsListCmp = new TwoSidedList<Integer>(list,3);
	// compare 
	assertEquals("", tsListCmp, tsList);


	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,4,5,4,7,8
	}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, mostly integer n is at the nth place 

	// remove something 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {4,8}));
	changed = tsList.removeAll(list,TwoSidedList.Direction.Right2Left);
	assertTrue("", changed);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,5,7
	}));
	tsListCmp = new TwoSidedList<Integer>(list,6);
	// compare 
	assertEquals("", tsListCmp, tsList);

// **** missing: testcases remove which leave this list unchanged. 
// **** missing: testcases add    which leave this list unchanged. 
// **** missing: testcases retain which leave this list unchanged. 
// **** missing: testcases for exceptions 
//	in particular IndexOutOfBoundsException. 

    } // void testRemoveAll 

    void testRetainAll() {
	List<Integer> list;
	TwoSidedList<Integer> tsList, tsListCmp;//tsListAdd, 
	boolean changed;

	// test retainAll(coll,Direction)

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,4,8,5,4,7,5
	}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, mostly integer n is at the nth place 

	// retain something 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {4,8}));
	changed = tsList.retainAll(list,TwoSidedList.Direction.Left2Right);
	assertTrue("", changed);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    4,8,4
	}));
	tsListCmp = new TwoSidedList<Integer>(list,3);
	// compare 
	assertEquals("", tsListCmp, tsList);


	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,4,8,5,4,7,5
	}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, mostly integer n is at the nth place 

	// retain something 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {4,8}));
	changed = tsList.retainAll(list,TwoSidedList.Direction.Right2Left);
	assertTrue("", changed);

	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    4,8,4
	}));
	tsListCmp = new TwoSidedList<Integer>(list,7);
	// compare 
	assertEquals("", tsListCmp, tsList);

    } // testRetainAll 

    void testSubList() {
	List<Integer> list;
	TwoSidedList<Integer> tsList, tsListCmp;

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,4,5,6,7,8
	}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	tsList = tsList.subList2(5,7);
	// create expected result 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    5,6
	}));
	tsListCmp = new TwoSidedList<Integer>(list,5);
	// compare 
	assertEquals("", tsListCmp, tsList);

    } // testSubList 

    void testListIterator() {
	List<Integer> list;
	TwoSidedList<Integer> tsList;//, tsListCmp;
	ListIterator<Integer> iter;

	// create a TSList 
	list = new ArrayList<Integer>(Arrays.asList(new Integer[] {
	    3,4,5,6,7,8
	}));
	tsList = new TwoSidedList<Integer>(list,3);
	// Here, integer n is at the nth place 

	iter = tsList.listIterator(6);
	assertEquals("", 6,iter.next().intValue());
	assertEquals("", 7,iter.next().intValue());
	assertEquals("", 8,iter.next().intValue());
	assertFalse("", iter.hasNext());

	iter = tsList.listIterator(6);
	assertEquals("", 5,iter.previous().intValue());
	assertEquals("", 4,iter.previous().intValue());
	assertEquals("", 3,iter.previous().intValue());
	assertFalse("", iter.hasPrevious());
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
