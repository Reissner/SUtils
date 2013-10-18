
package eu.simuline.util;

import eu.simuline.testhelpers.Actions;
//import eu.simuline.testhelpers.Accessor;
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





import java.util.NoSuchElementException;
import java.util.List;

import java.util.Arrays;

@RunWith(Suite.class)
@SuiteClasses({CyclicArrayListIteratorTest.TestAll.class})
public class CyclicArrayListIteratorTest {

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    static CyclicArrayListIteratorTest TEST = 
    new CyclicArrayListIteratorTest();

    public static class TestAll {

	@Before public void setUp() {
	    testcase = 1;
	    repetition = 100;
	}

	@Test public void testEmptyList() {
	    CyclicArrayListIteratorTest.TEST.testEmptyList();	    
	}
	@Test public void testGetObjects() {
	    CyclicArrayListIteratorTest.TEST.testGetObjects();	    
	}
	@Test public void testModifications() {
	    CyclicArrayListIteratorTest.TEST.testModifications();	    
	}
	@Test public void testRetEquals() {
	    CyclicArrayListIteratorTest.TEST.testRetEquals();	    
	}
    } // class TestAll

    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    static int testcase;
    static int repetition;
    static long step;



    CyclicIterator<Integer> cIter1, cIter2;
    CyclicList<Integer> cList1, cList2;
    List<Integer> list1, list2;

    boolean flag;
    Integer obj0, obj1, obj2, obj3, obj4, obj5;
    int index;



    public void testConstructor() {


    }

    public void testEmptyList() {

	// testcase 1
	//
	// hasNext/hasPrev
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	Assert.assertTrue(!cIter1.hasNext());
	Assert.assertTrue(!cIter1.hasPrev());
	//reportTestCase(" hasNext/Prev:  ");


	// testcase 2
	//
	// hasNext/hasPrev
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	Assert.assertEquals(-1,cIter1.getFirstIndex());
	Assert.assertEquals(-1,cIter1.getIndex());
	//reportTestCase(" get(First)Index:  ");


	// testcase 3
	//
	// refresh
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter2.refresh();
	Assert.assertEquals(cIter1,cIter2);
	//reportTestCase(" get(First)Index:  ");


	// testcase 4
	//
	// setIndex/getIndex**********
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	Assert.assertEquals(-1,cIter1.getIndex());
	cIter1.setIndex(10);
	Assert.assertEquals(-1,cIter1.getIndex());
	//reportTestCase(" s/getIndex:       ");


	// testcase 5
	//
	// getCyclicList
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	Assert.assertEquals(new CyclicArrayList<Integer>(new Integer[] {
		}),cIter1.getCyclicList());
	//reportTestCase(" :getCyclicList    ");


	// testcase 6
	//
	// next/prev
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	try {
	    cIter1.next();
	    fail("exception expected. ");
	}catch (Exception e) {
	    Assert.assertEquals("java.util.NoSuchElementException",
				e.toString());
	}
	//reportTestCase(" :next/prev    ");


	// testcase 7
	//
	// next/prev
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	try {
	    cIter1.previous();
	    fail("exception expected. ");
	}catch (Exception e) {
	    Assert.assertEquals("java.util.NoSuchElementException",
				e.toString());
	}
	//reportTestCase(" :next/prev    ");


	// testcase 8
	//
	// getNextIndexOf
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	Assert.assertEquals(-1,cIter1.getNextIndexOf(null));
	//reportTestCase(" :getNextIndexOf    ");



	// testcase 9
	//
	// equals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	Assert.assertEquals(cIter1,cIter2);
	//reportTestCase(" :equals    ");


	// testcase 10
	//
	// equals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
	    null
		}).cyclicIterator(0);
	Assert.assertTrue(!cIter1.equals(cIter2));
	//reportTestCase(" :equals    ");


	// testcase 11
	//
	// equals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
	    null
		}).cyclicIterator(0);
	Assert.assertTrue(!cIter2.equals(cIter1));
	//reportTestCase(" :equals    ");


	// testcase 12
	//
	// equals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	Assert.assertEquals("<CyclicIterator firstIndex=\"-1\" index=\"-1\">\n" + 
			    "<CyclicList>\n</CyclicList>\n" + 
			    "</CyclicIterator>\n",
			    cIter1.toString());
	//reportTestCase(" :equals    ");


	// testcase 13
	//
	// add
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter1.add(null);
	Assert.assertEquals(new CyclicArrayList<Integer>(new Integer[] {
	    null
		}),cIter1.getCyclicList());
	//reportTestCase(" :add    ");


	// testcase 14
	//
	// addAll
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter1.addAll(Arrays.asList(new Integer[] {
	    null
		}));
	Assert.assertEquals(new CyclicArrayList<Integer>(new Integer[] {
	    null
		}),cIter1.getCyclicList());
	//reportTestCase(" :addAll    ");


	// testcase 15
	//
	// set
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	try {
	cIter1.set(null);
	fail("exception expected. ");
	} catch (Exception e) {
	    Assert.assertEquals("java.lang.IllegalStateException: " + 
				"No pointer to set object <" + null + ">. ",
				e.toString());
	}
	//reportTestCase(" :set    ");



	// testcase 16
	//
	// set
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	try {
	cIter1.remove();
	fail("exception expected. ");
	} catch (Exception e) {
	    Assert.assertEquals("java.lang.IllegalStateException: " + 
				"No pointer to remove object. ",
				e.toString());
	}
	//reportTestCase(" :remove   ");


	// testcase 17
	//
	// equals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	
	Assert.assertTrue(cIter1.equals(cIter2));
	//reportTestCase(" :equals    ");


	// testcase 18
	//
	// equals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    null
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	
	Assert.assertTrue(!cIter1.equals(cIter2));
	//reportTestCase(" :equals    ");


	// testcase 19
	//
	// equals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
	    null
		}).cyclicIterator(0);
	
	Assert.assertTrue(!cIter1.equals(cIter2));
	//reportTestCase(" :equals    ");


	// testcase 20
	//
	// retEquals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	
	Assert.assertTrue(cIter1.retEquals(cIter2));
	//reportTestCase(" :retEquals    ");


	// testcase 21
	//
	// retEquals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    null
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	
	Assert.assertTrue(!cIter1.retEquals(cIter2));
	//reportTestCase(" :retEquals    ");


	// testcase 22
	//
	// retEquals
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
	    null
		}).cyclicIterator(0);
	
	Assert.assertTrue(!cIter1.retEquals(cIter2));
	//reportTestCase(" :retEquals    ");


    }


    public void testGetObjects() {

	testcase = 1;

	// testcase 1
	//
	// empty list
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	flag = true;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cIter1.hasNext();
	}
	step = System.currentTimeMillis()-step;

	Assert.assertTrue(!flag);
	//reportTestCase(" hasNext:       ");


	// testcase 2
	//
	// list with one element. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0)
		}).cyclicIterator(0);
	flag = false;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cIter1.hasNext();
	}
	step = System.currentTimeMillis()-step;

	Assert.assertTrue(flag);
	//reportTestCase(" hasNext:       ");


	// testcase 3
	//
	// list with one element. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0)
		}).cyclicIterator(0);
	cIter1.next();
	flag = true;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cIter1.hasNext();
	}
	step = System.currentTimeMillis()-step;

	Assert.assertTrue(!flag);
	//reportTestCase(" hasNext:       ");


	testcase = 1;

	// testcase 1
	//
	// empty list
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);
	flag = true;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cIter1.hasPrev();
	}
	step = System.currentTimeMillis()-step;

	Assert.assertTrue(!flag);
	//reportTestCase(" hasPrev:       ");



	// testcase 2
	//
	// list with one element. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0)
		}).cyclicIterator(0);
	flag = true;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cIter1.hasPrev();
	}
	step = System.currentTimeMillis()-step;

	Assert.assertTrue(!flag);
	//reportTestCase(" hasPrev:       ");


	// testcase 3
	//
	// list with one element. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0)
		}).cyclicIterator(0);
	cIter1.next();
	flag = false;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cIter1.hasPrev();
	}
	step = System.currentTimeMillis()-step;

	Assert.assertTrue(flag);
	//reportTestCase(" hasPrev:       ");


	testcase = 1;

	// testcase 1
	//
	// empty list
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);

	step = System.currentTimeMillis();
	try {
	    cIter1.next();
	    fail("Exception expected. ");
	} catch(Exception e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals(new NoSuchElementException().toString(),
			 e.toString());
	}
	//reportTestCase(" next:          ");


	// testcase 2
	//
	// 
	//
	cList1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1 = cList1.cyclicIterator(0);
	    obj0 = (Integer)cIter1.next();
	    obj1 = (Integer)cIter1.next();
	    obj2 = (Integer)cIter1.next();
	}
	step = System.currentTimeMillis()-step;

	Assert.assertEquals(new Integer(0),obj0);
	Assert.assertEquals(new Integer(1),obj1);
	Assert.assertEquals(new Integer(2),obj2);
	try {
	    cIter1.next();
	    fail("Exception expected. ");
	} catch(Exception e) {
	    assertEquals(new NoSuchElementException().toString(),
			 e.toString());
	}
	//reportTestCase(" next:          ");


	testcase = 1;

	// testcase 1
	//
	// empty list
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		}).cyclicIterator(0);

	step = System.currentTimeMillis();
	try {
	    cIter1.previous();
	    fail("Exception expected. ");
	} catch(Exception e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals(new NoSuchElementException().toString(),
			 e.toString());
	}
	//reportTestCase(" previous:      ");


	// testcase 2
	//
	// 
	//
	cList1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1 = cList1.cyclicIterator(0);
	    cIter1.next();
	    cIter1.next();
	    cIter1.next();
	    obj2 = (Integer)cIter1.previous();
	    obj1 = (Integer)cIter1.previous();
	    obj0 = (Integer)cIter1.previous();
	}
	step = System.currentTimeMillis()-step;

	Assert.assertEquals(new Integer(0),obj0);
	Assert.assertEquals(new Integer(1),obj1);
	Assert.assertEquals(new Integer(2),obj2);
	try {
	    cIter1.previous();
	    fail("Exception expected. ");
	} catch(Exception e) {
	    assertEquals(new NoSuchElementException().toString(),
			 e.toString());
	}
	//reportTestCase(" previous:      ");

	testcase = 1;


	// testcase 1
	//
	// element not in list
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		}).cyclicIterator(0);
	index = -20;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    index = cIter1.getNextIndexOf(new Integer(-3));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertEquals(-1,index);  
	//reportTestCase(" getNextIndexOf: ");


	// testcase 2
	//
	// element not in part of listwhich is to be read. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		}).cyclicIterator(0);
	cIter1.next();
	index = -20;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    index = cIter1.getNextIndexOf(new Integer(0));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertEquals(-1,index);  
	//reportTestCase(" getNextIndexOf: ");


	// testcase 3
	//
	// element not in part of listwhich is to be read. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(1),
	    new Integer(2),
		}).cyclicIterator(0);
	cIter1.next();
	index = 2;

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    index = cIter1.getNextIndexOf(new Integer(2));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertEquals(2,index);  
	//reportTestCase(" getNextIndexOf: ");



    }

    public void testModifications() {


	//report("\n Test methods modifying the underlying list \n");

	// testcase 1
	//
	// empty list 
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		    }).cyclicIterator(0);
	    cIter1.add(new Integer(10));
	    }
	step = System.currentTimeMillis()-step;
//System.out.println("cIter1:"+cList1);

	assertTrue(!cIter1.hasNext());
	assertEquals(new Integer(10),cIter1.previous());
	assertTrue("Shall have no previous element. ",
		   !cIter1.hasPrev());
	//reportTestCase(" add:           ");


	// testcase 2
	//
	// 
	// add before the first entry. 

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
		    }).cyclicIterator(0);
	    cIter1.add(new Integer(10));
	    }
	step = System.currentTimeMillis()-step;
//System.out.println("cIter1:"+cList1);

	assertEquals(new Integer(0),cIter1.next());
	cIter1.previous();
	assertEquals(new Integer(10),cIter1.previous());
	assertTrue("Shall have no previous element. ",
		   !cIter1.hasPrev());
	cIter1.next();
	assertEquals(new Integer(0),cIter1.next());
	assertEquals(new Integer(1),cIter1.next());
	assertEquals(new Integer(2),cIter1.next());
	assertTrue(!cIter1.hasNext());
	//reportTestCase(" add:           ");


	// testcase 3
	//
	// add at a mid-position. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		}).cyclicIterator(0);
	cIter1.next();

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1.add(new Integer(10));
	}
	step = System.currentTimeMillis()-step;

	assertEquals(new Integer(1),cIter1.next());
	cIter1.previous();
	assertEquals(new Integer(10),cIter1.previous());
	//reportTestCase(" add:           ");


	// testcase 4
	//
	// add at a mid-position. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		}).cyclicIterator(0);
	cIter1.next();
	cIter1.next();

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1.add(new Integer(10));
	}
	step = System.currentTimeMillis()-step;

	assertEquals(new Integer(2),cIter1.next());
	cIter1.previous();
	assertEquals(new Integer(10),cIter1.previous());
	//reportTestCase(" add:           ");


	// testcase 5
	//
	// add at the end of the list. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		}).cyclicIterator(0);
	cIter1.next();
	cIter1.next();
	cIter1.next();

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1.add(new Integer(10));
	}
	step = System.currentTimeMillis()-step;

	assertEquals(new Integer(10),cIter1.previous());
	//reportTestCase(" add:           ");


	testcase = 1;

	// testcase 1
	//
	// add empty list 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    //new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		    }).cyclicIterator(0);
	    cIter1.addAll(list1);
	    }
	step = System.currentTimeMillis()-step;
//System.out.println("cIter1:"+cList1);

	assertTrue(!cIter1.hasNext());
	assertEquals(new Integer(-2),cIter1.previous());
	assertEquals(new Integer(-1),cIter1.previous());
	assertTrue("Shall have no previous element. ",
		   !cIter1.hasPrev());
	//reportTestCase(" addAll:        ");


	// testcase 2
	//
	// 
	// add before the first entry. 
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
		    }).cyclicIterator(0);
	    cIter1.addAll(list1);
	    }
	step = System.currentTimeMillis()-step;
//System.out.println("cIter1:"+cList1);

	assertEquals(new Integer(0),cIter1.next());
	assertEquals(new Integer(1),cIter1.next());
	assertEquals(new Integer(2),cIter1.next());
	assertTrue(!cIter1.hasNext());
	cIter1.previous();
	cIter1.previous();
	cIter1.previous();
	assertEquals(new Integer(-3),cIter1.previous());
	assertEquals(new Integer(-2),cIter1.previous());
	assertEquals(new Integer(-1),cIter1.previous());
	assertTrue("Shall have no previous element. ",
		   !cIter1.hasPrev());
	//reportTestCase(" addAll:        ");


	// testcase 3
	//
	// add at a mid-position. 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
		    }).cyclicIterator(0);
	    cIter1.next();
	    cIter1.addAll(list1);
	}
	step = System.currentTimeMillis()-step;

	assertEquals(new Integer(1),cIter1.next());
	assertEquals(new Integer(2),cIter1.next());
	assertTrue(!cIter1.hasNext());
	cIter1.previous();
	cIter1.previous();
	assertEquals(new Integer(-3),cIter1.previous());
	assertEquals(new Integer(-2),cIter1.previous());
	assertEquals(new Integer(-1),cIter1.previous());
	assertEquals(new Integer(0),cIter1.previous());
	assertTrue("Shall have no previous element. ",
		   !cIter1.hasPrev());
	//reportTestCase(" addAll:        ");


	// testcase 4
	//
	// add at a mid-position. 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
		    }).cyclicIterator(0);
	    cIter1.next();
	    cIter1.next();

	    cIter1.addAll(list1);
	}
	step = System.currentTimeMillis()-step;

	assertEquals(new Integer(2),cIter1.next());
	assertTrue(!cIter1.hasNext());
	cIter1.previous();
	assertEquals(new Integer(-3),cIter1.previous());
	assertEquals(new Integer(-2),cIter1.previous());
	assertEquals(new Integer(-1),cIter1.previous());
	assertEquals(new Integer(1),cIter1.previous());
	assertEquals(new Integer(0),cIter1.previous());
	assertTrue("Shall have no previous element. ",
		   !cIter1.hasPrev());
	//reportTestCase(" addAll:        ");


	// testcase 5
	//
	// add at the end of the list. 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cIter1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
		    }).cyclicIterator(0);
	    cIter1.next();
	    cIter1.next();
	    cIter1.next();
	    cIter1.addAll(list1);
	}
	step = System.currentTimeMillis()-step;

	assertTrue(!cIter1.hasNext());
	assertEquals(new Integer(-3),cIter1.previous());
	assertEquals(new Integer(-2),cIter1.previous());
	assertEquals(new Integer(-1),cIter1.previous());
	assertEquals(new Integer(2),cIter1.previous());
	assertEquals(new Integer(1),cIter1.previous());
	assertEquals(new Integer(0),cIter1.previous());
	assertTrue("Shall have no previous element. ",
		   !cIter1.hasPrev());
	//reportTestCase(" addAll:        ");


    } // testModifications 

    public void testRetEquals() {


	// testcase 1
	//
	// exactly equal. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		}).cyclicIterator(0);
	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cIter1.retEquals(cIter2);
	}
	step = System.currentTimeMillis()-step;
	assertTrue(flag);
	//reportTestCase(" retEquals:           ");


	// testcase 2
	//
	// exactly equal. 
	//
	cIter1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		}).cyclicIterator(0);
	cIter2 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(2),
	    new Integer(0),
	    new Integer(1)
		}).cyclicIterator(1);
	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cIter1.retEquals(cIter2);
	}
	step = System.currentTimeMillis()-step;
	assertTrue(flag);
	//reportTestCase(" retEquals:           ");
    }


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(CyclicArrayListIteratorTest.class);
    }


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {
 	Actions.run(CyclicArrayListIteratorTest.class);
    }
}
