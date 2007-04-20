
package eu.simuline.util;

import eu.simuline.testhelpers.GUIRunListener;
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
import java.util.Arrays;



@RunWith(Suite.class)
@SuiteClasses({ArraySetTest.TestAll.class})
public class ArraySetTest {


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    static ArraySetTest TEST = new ArraySetTest();

    public static class TestAll {
	@Test public void testAdd() {
	    ArraySetTest.TEST.testAdd();	    
	}
	@Test public void testContains() {
	    ArraySetTest.TEST.testContains();	    
	}
	@Test public void testContainsAll() {
	    ArraySetTest.TEST.testContainsAll();	    
	}
	@Test public void testAddAll() {
	    ArraySetTest.TEST.testAddAll();
	}
    } // class TestAll



    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    public void testAdd() {
	ArraySet arraySet;

	arraySet = new ArraySet<Integer>();
	assertEquals(0,arraySet.size());
	assertTrue(arraySet.isEmpty());
	assertTrue(!arraySet.contains(new Integer(3)));
	assertTrue(!arraySet.contains(null));
	assertTrue(!arraySet.contains(new Double(0)));
	assertTrue(!arraySet.iterator().hasNext());
	assertTrue(!arraySet.listIterator().hasNext());
	assertEquals(0,arraySet.toArray().length);
	assertNull(arraySet.comparator());


	assertTrue(arraySet.add(new Integer(3)));
	assertTrue(arraySet.add(new Integer(2)));
	assertTrue(arraySet.add(new Integer(1)));
	Assert.assertArraysEquals(new Object[] {
	    new Integer(1), new Integer(2), new Integer(3)
	},
			    arraySet.toArray());
	assertTrue(!arraySet.add(new Integer(2)));
	Assert.assertArraysEquals(new Object[] {
	    new Integer(1), new Integer(2), new Integer(3)
	},
			    arraySet.toArray());

	Comparator<Integer> cmp = new Comparator<Integer>() {
	    public int compare(Integer o1,Integer o2) {
		return -o1.compareTo(o2);
	    }
	};
	arraySet = new ArraySet<Integer>(cmp);
	assertTrue(arraySet.add(new Integer(3)));
	assertTrue(arraySet.add(new Integer(2)));
	assertTrue(arraySet.add(new Integer(1)));
	Assert.assertArraysEquals(new Object[] {
	    new Integer(3), new Integer(2), new Integer(1)
	},
			    arraySet.toArray());
	assertTrue(!arraySet.add(new Integer(2)));
	Assert.assertArraysEquals(new Object[] {
	    new Integer(3), new Integer(2), new Integer(1)
	},
			    arraySet.toArray());

    } // testAdd 

    public void testContains() {
	ArraySet arraySet;

	arraySet = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(3), new Integer(2), new Integer(1)}));
	Assert.assertArraysEquals(new Object[] {
	    new Integer(1), new Integer(2), new Integer(3)
	},
				  arraySet.toArray());

	assertTrue(arraySet.contains(new Integer(3)));
	assertTrue(arraySet.contains(new Integer(2)));
	assertTrue(arraySet.contains(new Integer(1)));
	assertTrue(!arraySet.contains(new Integer(0)));
	assertTrue(!arraySet.contains(""));
    } // testContains 

    public void testContainsAll() {
	ArraySet arraySet1, arraySet2;

	arraySet1 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(3), new Integer(2), new Integer(1)}));
	arraySet2 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(3), new Integer(2), new Integer(1)}));
	assertTrue(arraySet1.containsAll(arraySet2));

	arraySet1 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(4), new Integer(3), new Integer(2), new Integer(1)}));
	arraySet2 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(3), new Integer(2), new Integer(1)}));
	assertTrue(arraySet1.containsAll(arraySet2));

	arraySet1 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(3), new Integer(2), new Integer(1)}));
	arraySet2 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(4), new Integer(3), new Integer(2), new Integer(1)}));
	assertTrue(!arraySet1.containsAll(arraySet2));

	arraySet1 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    }));
	arraySet2 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(4), new Integer(3), new Integer(2), new Integer(1)}));
	assertTrue(!arraySet1.containsAll(arraySet2));

    } // testContainsAll 

    public void testAddAll() {
	ArraySet arraySet1, arraySet2, arraySet3;

	arraySet1 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(3), new Integer(2), new Integer(1)}));
	arraySet2 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(3), new Integer(2), new Integer(1)}));
	arraySet3 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(3), new Integer(2), new Integer(1)}));
	assertTrue(!arraySet1.addAll(arraySet2));
	assertEquals(arraySet1,arraySet3);

	arraySet1 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(3), new Integer(2), new Integer(1)}));
	arraySet2 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(4), new Integer(2), new Integer(1)}));
	arraySet3 = new ArraySet<Integer>(Arrays.asList(new Integer[] {
	    new Integer(4), new Integer(3), new Integer(2), new Integer(1)}));
	assertTrue(arraySet1.addAll(arraySet2));
	assertEquals(arraySet1,arraySet3);
     } // testAddAll

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(ArraySetTest.class);
    }


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {

	JUnitCore core = new JUnitCore();
	core.addListener(new GUIRunListener());
	core.run(ArraySetTest.class);
    }
}

