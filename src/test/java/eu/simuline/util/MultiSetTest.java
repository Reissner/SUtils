
package eu.simuline.util;

import eu.simuline.testhelpers.Actions;
import eu.simuline.testhelpers.Accessor;
//import static eu.simuline.testhelpers.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.Map;
import java.util.NoSuchElementException;

@RunWith(Suite.class)
@SuiteClasses({MultiSetTest.TestAll.class})
public class MultiSetTest {

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    static MultiSetTest TEST = new MultiSetTest();


    @RunWith(Suite.class)
    @SuiteClasses({
	MultiSetTest.TestBase.class,
	MultiSetTest.TestQueries.class,
	MultiSetTest.TestModifications.class,
	MultiSetTest.TestIterator.class
    })
    public static class TestAll {
    } // class TestAll 

    public static class TestBase {
	@Test public void testConstructors() {
	    MultiSetTest.TEST.testConstructors();	    
	}
	@Test public void testEquals() {
	    MultiSetTest.TEST.testEquals();	    
	}
	@Test public void testHashCode() {
	    MultiSetTest.TEST.testHashCode();	    
	}
    } // class TestBase 

    public static class TestQueries {
	@Test public void testSizeWithMult() {
	    MultiSetTest.TEST.testSizeWithMult();	    
	}
	@Test public void testIsEmpty() {
	    MultiSetTest.TEST.testIsEmpty();	    
	}
	@Test public void testGetMaxObjWithMult() throws Exception {
	    MultiSetTest.TEST.testGetMaxObjWithMult();	    
	}
	@Test public void testGetMultiplicity() {
	    MultiSetTest.TEST.testGetMultiplicity();	    
	}
    } //  class TestQueries 

    public static class TestModifications {
	@Test public void testAddWithMult() {
	    MultiSetTest.TEST.testAddWithMult();	    
	}
	@Test public void testRemoveWithMult() {
	    MultiSetTest.TEST.testRemoveWithMult();	    
	}
	@Test public void testAddAll() {
	    MultiSetTest.TEST.testAddAll();	    
	}
    } // class TestModifications


    public static class TestIterator {

	@Test public void testHasNextNext() {
	    MultiSetTest.TEST.testHasNextNext();	    
	}

	@Test public void testRemoveIter() {
	    MultiSetTest.TEST.testRemoveIter();	    
	}

	@Test public void testSetMultIter() {
	    MultiSetTest.TEST.testSetMultIter();	    
	}

	@Test public void testRemoveMultIter() {
	    MultiSetTest.TEST.testRemoveMultIter();	    
	}

    } // class TestIterator 

    @Before public void setUp() {
	testcase = 1;
	repetition = 1;
    }


    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    int testcase;
    int repetition;
    long step;



    public void testConstructors() {

	MultiSet<String> result;
	MultiSet<String> cmp;

	// testcase 1
	//
	// The default constructor
	//
	assertTrue(new TreeMultiSet<String>().isEmpty());


	// testcase 2
	//
	// copy constructor applied to empty set 
	//
	cmp = new TreeMultiSet<String>();
	result = new TreeMultiSet<String>(cmp);
	assertEquals(cmp,result);


	// testcase 3
	//
	// copy constructor applied to non-empty set 
	//
	cmp = new TreeMultiSet<String>();
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element2");
	cmp.add("Element2");
	result = new TreeMultiSet<String>(cmp);
	assertEquals(cmp,result);

    } // testConstructors 

    public void testSizeWithMult() {

	MultiSet<String> result;

	// testcase 1
	//
	// empty set 
	//
	result = new TreeMultiSet<String>();
	assertEquals(0,result.sizeWithMult());


	// testcase 2
	//
	// non-empty set 
	//
	result = new TreeMultiSet<String>();
	assertEquals(0,result.sizeWithMult());
	assertTrue( result.add("Element1"));
	assertEquals(1,result.sizeWithMult());
	assertTrue(!result.add("Element1"));
	assertTrue(!result.add("Element1"));
	assertTrue( result.add("Element2"));
	assertTrue(!result.add("Element2"));
	assertEquals(5,result.sizeWithMult());

    } // testSizeWithMult 

    public void testIsEmpty() {

	MultiSet<String> result;
	MultiSet<String> cmp;

	// testcase 1
	//
	// The empty set
	//
	assertTrue(TreeMultiSet.emptyMultiSet().isEmpty());


	// testcase 2
	//
	// The empty set
	//
	result = new TreeMultiSet<String>();
	assertTrue(result.isEmpty());


	// testcase 3
	//
	// The empty set
	//
	result = new TreeMultiSet<String>();
	result.add("anElement");
	assertTrue(!result.isEmpty());


    } // testIsEmpty 

    public void testGetMaxObjWithMult() throws Exception {

	MultiSet<String> mset;
	Map.Entry result;

	// testcase 1
	//
	// empty set 
	//
	mset = new TreeMultiSet<String>();
	result = (Map.Entry)Accessor.invoke(mset,
					    "getMaxObjWithMult");
	assertNull(result);


	// testcase 2
	//
	// non-empty set 
	//
	mset = new TreeMultiSet<String>();
	mset.add("Element1");
	mset.add("Element1");
	mset.add("Element1");
	mset.add("Element2");
	mset.add("Element2");
	result = (Map.Entry)Accessor.invoke(mset,
					    "getMaxObjWithMult");
	assertEquals("Element1",result.getKey());
	assertEquals(new Integer(3),Accessor.invoke(result.getValue(),
						    "get"));

    } // testGetMaxObjWithMult 

    public void testGetMultiplicity() {
	MultiSet<String> result;
	MultiSet<String> cmp;

	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element1");
	result.add("Element1");
	result.add("Element2");
	result.add("Element2");

	// testcase 1
	//
	// 
	//
	assertEquals(0,result.getMultiplicity("noElement"));


	// testcase 2
	//
	// 
	//
	assertEquals(3,result.getMultiplicity("Element1"));


	// testcase 3
	//
	// 
	//
	assertEquals(2,result.getMultiplicity("Element2"));

    } // testGetMultiplicity 

    public void testAddWithMult() {

	MultiSet<String> result;
	String obj;

	// testcase 1
	//
	// 
	//
	result = new TreeMultiSet<String>();
	try {
	    result.addWithMult(null);
	    fail("NullPointerException expected. ");
	} catch (NullPointerException e) {
	    // everything as expected. 
	}


	// testcase 2
	//
	// 
	//
	result = new TreeMultiSet<String>();
	obj = "Element1";
	assertEquals(1,result.addWithMult(obj));
	assertEquals(1,result.getMultiplicity(obj));


	// testcase 3
	//
	// 
	//
	result = new TreeMultiSet<String>();
	obj = "Element1";
	result.add(obj);
	assertEquals(2,result.addWithMult(obj));
	assertEquals(2,result.getMultiplicity(obj));


	// testcase 4
	//
	// 
	//
	result = new TreeMultiSet<String>();
	try {
	    result.addWithMult(null,0);
	    fail("NullPointerException expected. ");
	} catch (NullPointerException e) {
	    // everything as expected. 
	}


	// testcase 4
	//
	// 
	//
	result = new TreeMultiSet<String>();
	try {
	    result.addWithMult("",-1);
	    fail("IllegalArgumentException expected. ");
	} catch (Exception e) {
	    assertEquals((Object)new IllegalArgumentException
			 ("Expected non-negative multiplicity; found -1. ")
			 .getMessage(),
			 e.getMessage());
	    // everything as expected. 
	}


	// testcase 5
	//
	// 
	//
	result = new TreeMultiSet<String>();
	assertEquals(0,result.addWithMult("Element1",0));
	assertEquals(0,result.getMultiplicity(obj));


	// testcase 6
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.addWithMult("Element1",3);
	assertEquals(3,result.getMultiplicity(obj));
	result.addWithMult("Element1",2);
	assertEquals(5,result.getMultiplicity(obj));

    } // testAddWithMult 

    public void testRemoveWithMult() {

	MultiSet<String> result;

	// testcase 1
	//
	// 
	//
	result = new TreeMultiSet<String>();
	try {
	    result.removeWithMult(null);
	    fail("NullPointerException expected. ");
	} catch (NullPointerException e) {
	    // everything as expected. 
	}


	// testcase 2
	//
	// 
	//
	result = new TreeMultiSet<String>();
	try {
	    result.removeWithMult ("Element1");
	} catch (IllegalArgumentException e) {
	    assertEquals("Tried to remove object " + "Element1" + 
			 " which is not in this MultiSet. ", 
			 e.getMessage());
	}


	// testcase 3
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	assertEquals(1,result.removeWithMult ("Element1"));
	assertEquals(0,result.getMultiplicity("Element1"));


	// testcase 4
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element1");
	result.add("Element1");
	assertEquals(3,result.removeWithMult ("Element1"));
	assertEquals(2,result.getMultiplicity("Element1"));



	// testcase 5
	//
	// 
	//
	result = new TreeMultiSet<String>();
	try {
	    result.removeWithMult(null,0);
	    fail("NullPointerException expected. ");
	} catch (NullPointerException e) {
	    // everything as expected. 
	}


	// testcase 6
	//
	// 
	//
	result = new TreeMultiSet<String>();
	try {
	    result.removeWithMult("Element1",-1);
	    fail("IllegalArgumentException expected. ");
	} catch (Exception e) {
	    assertEquals((Object)new IllegalArgumentException
			 ("Expected non-negative multiplicity; found -1. ")
			 .getMessage(),
			 e.getMessage());
	    // everything as expected. 
	}


	// testcase 7
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	try {
	    result.removeWithMult("Element1",2);
	    fail("IllegalArgumentException expected. ");
	} catch (Exception e) {
	    assertEquals((Object)new IllegalArgumentException
			 ("Resulting multiplicity " + 
			  1 + " + " + (-2) + 
			  " should be non-negative. ")
			 .getMessage(),
			 e.getMessage());
	    // everything as expected. 
	}


	// testcase 8
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element1");
	assertEquals(2,result.removeWithMult ("Element1",2));
	assertEquals(0,result.getMultiplicity("Element1"));


	// testcase 9
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element1");
	result.add("Element1");
	assertEquals(3,result.removeWithMult ("Element1",2));
	assertEquals(1,result.getMultiplicity("Element1"));


    } // testRemoveWithMult 

    public void testAddAll() {

	MultiSet<String> ms1,ms2;
	MultiSet<String> cmp;

	// testcase 1
	//
	// 
	//
	ms1 = new TreeMultiSet<String>();
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element2");
	ms1.add("Element2");
	ms2 = new TreeMultiSet<String>();
	assertTrue(!ms1.addAll(ms2));
	cmp = new TreeMultiSet<String>();
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element2");
	cmp.add("Element2");
	assertEquals(cmp,ms1);


	// testcase 2
	//
	// 
	//
	ms1 = new TreeMultiSet<String>();
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element2");
	ms1.add("Element2");
	ms2 = new TreeMultiSet<String>();
	assertTrue(ms2.addAll(ms1));
	cmp = new TreeMultiSet<String>();
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element2");
	cmp.add("Element2");
	assertEquals(cmp,ms2);


	// testcase 3
	//
	// 
	//
	ms1 = new TreeMultiSet<String>();
	ms1.addWithMult("Element1",3);
	ms1.addWithMult("Element2",2);
	ms2 = new TreeMultiSet<String>();
	ms2.addWithMult("Element2",3);
	ms2.addWithMult("Element3",1);
	assertTrue(ms1.addAll(ms2));
	cmp = new TreeMultiSet<String>();
	cmp.addWithMult("Element1",3);
	cmp.addWithMult("Element2",5);
	cmp.addWithMult("Element3",1);
	assertEquals(cmp,ms1);
    } //  testAddAll 

    public void testEquals() {

	MultiSet<String> result;
	MultiSet<String> cmp;

	// testcase 1
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element1");
	result.add("Element1");
	result.add("Element2");
	result.add("Element2");
	cmp = new TreeMultiSet<String>();
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element2");
	cmp.add("Element2");
	assertTrue(result.equals(cmp));


	// testcase 2
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element1");
	result.add("Element1");
	result.add("Element2");
	result.add("Element2");
	cmp = new TreeMultiSet<String>();
	cmp.add("Element1");
	cmp.add("Element2");
	assertTrue(!result.equals(cmp));


	// testcase 3
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element1");
	result.add("Element1");
	result.add("Element2");
	result.add("Element2");
	cmp = new TreeMultiSet<String>();
	cmp.add("Element1");
	cmp.add("Element2");
	assertTrue(!cmp.equals(result));


	// testcase 4
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element2");
	result.add("Element3");
	cmp = new TreeMultiSet<String>();
	cmp.add("Element1");
	cmp.add("Element2");
	assertTrue(!result.equals(cmp));


	// testcase 5
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element2");
	result.add("Element3");
	cmp = new TreeMultiSet<String>();
	cmp.add("Element1");
	cmp.add("Element2");
	assertTrue(!cmp.equals(result));


    } // testEquals 

    public void testHashCode() {

	MultiSet<String> result;
	MultiSet<String> cmp;

	// testcase 1
	//
	// 
	//
	result = new TreeMultiSet<String>();
	result.add("Element1");
	result.add("Element1");
	result.add("Element1");
	result.add("Element2");
	result.add("Element2");
	cmp = new TreeMultiSet<String>();
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element1");
	cmp.add("Element2");
	cmp.add("Element2");
	assertEquals(cmp.hashCode(),result.hashCode());

    } // testHashCode


    void testHasNextNext() {
	MultiSet<String> ms1;
	MultiSetIterator<String> iter;
	String str;

 	ms1 = new TreeMultiSet<String>();
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element2");
	ms1.add("Element2");

	iter = ms1.iterator();

	assertTrue(iter.hasNext());
	str = iter.next();
	assertEquals("Element1", str);
	assertEquals(3, iter.getMult());
	assertEquals(3, iter.getMult());

	assertTrue(iter.hasNext());
	str = iter.next();
	assertEquals("Element2", str);
	assertEquals(2, iter.getMult());
	assertEquals(2, iter.getMult());

	assertTrue(!iter.hasNext());

	try {
	    iter.next();
	    fail("exception expected");
	} catch (NoSuchElementException e) {

	}

   } // testHasNextNext() 

   void testRemoveIter() {
	MultiSet<String> ms1;
	MultiSetIterator<String> iter;
	String str;

 	ms1 = new TreeMultiSet<String>();
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element2");
	ms1.add("Element2");

	iter = ms1.iterator();
	try {
	    iter.remove();
	    fail("exception expected");
	} catch (IllegalStateException e) {

	}

	assertTrue(iter.hasNext());
	str = iter.next();
	assertEquals("Element1", str);
	assertEquals(3, iter.getMult());

	iter.remove();
	try {
	    iter.remove();
	    fail("exception expected");
	} catch (IllegalStateException e) {

	}

	try {
	    iter.getMult();
	    fail("exception expected");
	} catch (IllegalStateException e) {

	}

	assertEquals(2, ms1.getMultiplicity("Element2"));
	assertTrue(!ms1.contains("Element1"));

   } // testRemoveIter() 

   void testSetMultIter() {
	MultiSet<String> ms1;
	MultiSetIterator<String> iter;
	String str;

 	ms1 = new TreeMultiSet<String>();
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element2");
	ms1.add("Element2");

	iter = ms1.iterator();
	try {
	    iter.setMult(4);
	    fail("exception expected");
	} catch (IllegalStateException e) {

	}

	assertTrue(iter.hasNext());
	str = iter.next();
	assertEquals("Element1", str);
	assertEquals(3, iter.getMult());

	try {
	    iter.setMult(-1);
	    fail("exception expected");
	} catch (IllegalArgumentException e) {

	}

	assertEquals(3, iter.setMult(4));
	assertEquals(4, iter.getMult());
	assertEquals(4, iter.setMult(0));
	try {
	    iter.getMult();
	    fail("exception expected");
	} catch (IllegalStateException e) {

	}
	try {
	    iter.setMult(3);
	    fail("exception expected");
	} catch (IllegalStateException e) {

	}


	assertEquals(2, ms1.getMultiplicity("Element2"));
	assertTrue(!ms1.contains("Element1"));

   } // testSetMultIter() 

   void testRemoveMultIter() {
	MultiSet<String> ms1;
	MultiSetIterator<String> iter;
	String str;

 	ms1 = new TreeMultiSet<String>();
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element1");
	ms1.add("Element2");
	ms1.add("Element2");

	iter = ms1.iterator();
	try {
	    iter.removeMult(4);
	    fail("exception expected");
	} catch (IllegalStateException e) {

	}

	assertTrue(iter.hasNext());
	str = iter.next();
	assertEquals("Element1", str);
	assertEquals(3, iter.getMult());

	try {
	    iter.removeMult(-1);
	    fail("exception expected");
	} catch (IllegalArgumentException e) {

	}

	try {
	    iter.removeMult(4);
	    fail("exception expected");
	} catch (IllegalArgumentException e) {

	}

	assertEquals(3, iter.removeMult(1));
	assertEquals(2, iter.getMult());
	assertEquals(2, iter.removeMult(2));
	try {
	    iter.getMult();
	    fail("exception expected");
	} catch (IllegalStateException e) {

	}


	assertEquals(2, ms1.getMultiplicity("Element2"));
	assertTrue(!ms1.contains("Element1"));

   } // testRemoveMultIter() 

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
