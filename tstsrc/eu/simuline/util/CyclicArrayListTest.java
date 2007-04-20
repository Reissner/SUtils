
package eu.simuline.util;

import eu.simuline.testhelpers.GUIRunListener;
//import eu.simuline.testhelpers.Accessor;
import eu.simuline.testhelpers.Assert;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;


import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;




import java.util.List;
import java.util.Arrays;


@RunWith(Suite.class)
@SuiteClasses({CyclicArrayListTest.TestAll.class})
public class CyclicArrayListTest {

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    static CyclicArrayListTest TEST = new CyclicArrayListTest();


    public static class TestAll {

	/** 
	 * The following method may be given or not.
	 *
	 * It is called before each test method and may be used to
	 * prepare the necessary prerequisites for the test.
	 */
	@Before public void setUp() {
	    testcase = 1;
	    repetition = 1;
	}

	@Test public void testGetCopy() {
	    CyclicArrayListTest.TEST.testGetCopy();	    
	}
	@Test public void testAdd() {
	    CyclicArrayListTest.TEST.testAdd();	    
	}
	@Test public void testAddAll() {
	    CyclicArrayListTest.TEST.testAddAll();	    
	}
	@Test public void testEquals() {
	    CyclicArrayListTest.TEST.testEquals();	    
	}
	@Test public void testToArray() {
	    CyclicArrayListTest.TEST.testToArray();	    
	}
	@Test public void testGetInverse() {
	    CyclicArrayListTest.TEST.testGetInverse();	    
	}
    } // class TestAll


    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */



    static int testcase;
    static int repetition;
    static long step;

    CyclicList<Integer> cList1, cList2, cList3;
    List<Integer> list1, list2, list3;
    boolean flag;
    Integer[] objArray1, objArray2;





    public void testGetCopy() {

	// testcase 1
	//
	// len < 0. 
	//
	cList1 = new CyclicArrayList<Integer>(new Integer[] {
	});

	step = System.currentTimeMillis();
	try {
	    cList1.getCopy(-6);
	    fail("Exception expected. ");
	} catch(Exception e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals(new IllegalArgumentException
		("Positive length expected; found: " + (-6) + ". ")
		.toString(),
			 e.toString());
	}
	//reportTestCase("  getCopy:            ");


	// testcase 2
	//
	// empty list length != 0. 
	//
	cList1 = new CyclicArrayList<Integer>(new Integer[] {
	});

	step = System.currentTimeMillis();
	try {
	    cList1.getCopy(6);
	    fail("Exception expected. ");
	} catch(Exception e) {
	    step = System.currentTimeMillis()-step;
	    assertEquals(new EmptyCyclicListException().toString(),
			 e.toString());
	}
	//reportTestCase("  getCopy:            ");


	// testcase 3
	//
	// empty list length == 0. 
	//
	cList1 = new CyclicArrayList<Integer>(Arrays.asList(new Integer[] {
	}));

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList2 = cList1.getCopy(0);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
		},
				  cList2.toArray(0));
	//reportTestCase("  get        ");


	// testcase 4
	//
	//
	// empty list length != 0. 
	// new list shorter than old one. 
	//
	cList1 = new CyclicArrayList<Integer>(Arrays.asList(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3)
	}));

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList2 = cList1.getCopy(0);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
		},
				  cList2.toArray(0));
	//reportTestCase("  getCopy:            ");


	// testcase 5
	//
	// empty list length != 0. 
	// new list shorter than old one. 
	//
	cList1 = new CyclicArrayList<Integer>(Arrays.asList(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3)
	}));

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList2 = cList1.getCopy(3);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		},
				  cList2.toArray(0));
	//reportTestCase("  getCopy:            ");


	// testcase 6
	//
	// empty list length != 0. 
	// new list multiple of old one. 
	//
	cList1 = new CyclicArrayList<Integer>(Arrays.asList(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3)
	}));

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList2 = cList1.getCopy(3*4);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3),

	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3),

	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3)
		},
				  cList2.toArray(0));
	//reportTestCase("  getCopy:            ");

	// testcase 6
	//
	// empty list length != 0. 
	// new list more than multiple of old one. 
	//
	cList1 = new CyclicArrayList<Integer>(Arrays.asList(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3)
	}));

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList2 = cList1.getCopy(3*4+2);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3),

	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3),

	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3),

	    new Integer(0),
	    new Integer(1)
		},
				  cList2.toArray(0));
	//reportTestCase("  getCopy:            ");

    } // getCopy 

    public void testAdd() {

	// testcase 1
	//
	// add to empty list
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(Arrays.asList(new Integer[] {
	    }));
	    cList1.add(6,new Integer(0));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0)
		},
				  cList1.toArray(0));
	//reportTestCase("  add:                ");


	// testcase 2
	//
	// add to list with length 1
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(Arrays.asList(new Integer[] {
		new Integer(0)
	    }));
	    cList1.add(1,new Integer(1));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertEquals(Arrays.asList(new Object[] {
	    new Integer(0),
	    new Integer(1)}),
				  cList1.asList(0));
	//reportTestCase("  add:                ");


	// testcase 3
	//
	// add to list with length 1
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(Arrays.asList(new Integer[] {
		new Integer(0)
	    }));
	    cList1.add(5,new Integer(1));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1)},
				  cList1.toArray(0));
	//reportTestCase("  add:                ");


	// testcase 4
	//
	// add to list with length 1
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0)
	    });
	    cList1.add(0,new Integer(1));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(1),
	    new Integer(0)},
				  cList1.toArray(0));
	//reportTestCase("  add:                ");


	// testcase 5
	//
	// add to list with length 1
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0)
	    });
	    cList1.add(-10,new Integer(1));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(1),
	    new Integer(0)},
				  cList1.toArray(0));
	//reportTestCase("  add:                ");


	// testcase 6
	//
	// add to list with length 2
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1)
	    });
	    cList1.add(2+3*8,new Integer(2));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		},
				  cList1.toArray(0));
	//reportTestCase("  add:                ");


	// testcase 7
	//
	// add to list with length 2
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1)
	    });
	    cList1.add(1-3*8,new Integer(2));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(2),
	    new Integer(1)
		},
				  cList1.toArray(0));
	//reportTestCase("  add:                ");


	// testcase 8
	//
	// add to list with length 2
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1)
	    });
	    cList1.add(0-3*8,new Integer(2));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(2),
	    new Integer(0),
	    new Integer(1)
		},
				  cList1.toArray(0));
	//reportTestCase("  add:                ");


	// testcase 9
	//
	// add to list with length 3
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
	    });
	    cList1.add(3-4*8,new Integer(3));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3)
		},
				  cList1.toArray(0));
	//reportTestCase("  add:                ");


	// testcase 10
	//
	// add to list with length 3
	//

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
	    });
	    cList1.add(2-4*8,new Integer(3));
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(3),
	    new Integer(2)
		},
				  cList1.toArray(0));
	//reportTestCase(" add:                ");



    } // testAdd 


    public void testAddAll() {

	// testcase 1
	//
	// add to empty list
	//
	list1 = Arrays.asList(new Integer[] {
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
	    });
	    cList1.addAll(6,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		},
				  cList1.toArray(0));
	//reportTestCase("  addAll:             ");


	// testcase 2
	//
	// add to list with length 1 at end. 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0)
	    });
	    cList1.addAll(1,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(1));
	//reportTestCase("  addAll:             ");


	// testcase 3
	//
	// add to list with length 1 splitted list. 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0)
	    });
	    cList1.addAll(3,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(0),
	    new Integer(-1)
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(3));
	//reportTestCase("  addAll:             ");


	// testcase 4
	//
	// add to list with length 1 at end. 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0)
	    });
	    cList1.addAll(5,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(5));
	//reportTestCase("  addAll:             ");


	// testcase 5
	//
	// add to list with length 1 at beginning. 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0)
	    });
	    cList1.addAll(0,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(0)},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(0));
	//reportTestCase("  addAll:             ");


	// testcase 5
	//
	// add to list with length 1
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0)
	    });
	    cList1.addAll(-12,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(0)},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(-12));
	//reportTestCase("  addAll:             ");



	// testcase 6
	//
	// add to list with length 2 list in center. 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1)
	    });
	    cList1.addAll(2+3*8,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(1),
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(2+3*8));
	//reportTestCase("  addAll:             ");


	// testcase 7
	//
	// add to list with length 2 list at end 
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1)
	    });
	    cList1.addAll(1-3*8,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(1-3*8));
	//reportTestCase("  addAll:             ");


	// testcase 8
	//
	// add to list with length 2 list in center
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1)
	    });
	    cList1.addAll(0-3*8,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(1)
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(0-3*8));
	//reportTestCase("  addAll:             ");


	// testcase 9
	//
	// add to list with length 3 list in center
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
	    });
	    cList1.addAll(3-4*8,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(1),
	    new Integer(2),
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(3-4*8));
	//reportTestCase("  addAll:             ");


	// testcase 10
	//
	// add to list with length 3 list at beginning
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
	    });
	    cList1.addAll(2-4*8,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(2-4*8));
	//reportTestCase(" addAll:              ");

	// testcase 11
	//
	// add to list with length 3 list at beginning
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
	    });
	    cList1.addAll(1-4*6,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(1),
	    new Integer(2),
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(1-4*6));
	//reportTestCase(" addAll:              ");


	// testcase 11
	//
	// add to list with length 3 list at beginning
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
	    });
	    cList1.addAll(2-4*6,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(2),
		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(2-4*6));
	//reportTestCase(" addAll:              ");


	// testcase 12
	//
	// add to list with length 3 list at beginning
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
	    });
	    cList1.addAll(3-4*6,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3),

		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(3-4*6));
	//reportTestCase(" addAll:              ");


	// testcase 13
	//
	// add to list with length 3 list at beginning
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
	    });
	    cList1.addAll(4-4*6,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(-3),
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(-1),
	    new Integer(-2),

		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(4-4*6));
	//reportTestCase(" addAll:              ");


	// testcase 14
	//
	// add to list with length 3 list at beginning
	//
	list1 = Arrays.asList(new Integer[] {
	    new Integer(-1),
	    new Integer(-2),
	    new Integer(-3)
	});

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList1 = new CyclicArrayList<Integer>(new Integer[] {
		new Integer(0),
		new Integer(1),
		new Integer(2)
	    });
	    cList1.addAll(5-4*6,list1);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertArraysEquals(new Object[] {
	    new Integer(-2),
	    new Integer(-3),
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(-1),

		},
				  cList1.toArray(0));
	assertEquals(list1.get(0),cList1.get(5-4*6));
	//reportTestCase(" addAll:              ");




    } // testAddAll 

    public void testEquals() {

	// testcase 1
	//
	// exactly equal. 
	//
	cList1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		});
	cList2 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		});
	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cList1.equals(cList2);
	}
	step = System.currentTimeMillis()-step;
	assertTrue(flag);
	//reportTestCase(" equals:              ");


	// testcase 2
	//
	// equal up to a cyclic permutation. 
	//
	cList1 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		});
	cList2 = new CyclicArrayList<Integer>(new Integer[] {
	    new Integer(1),
	    new Integer(2),
	    new Integer(0)
		});
	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cList1.equals(cList2);
	}
	step = System.currentTimeMillis()-step;
	assertTrue(flag);
	//reportTestCase(" equals:              ");

	// testcase 3
	//
	// both empty. 
	//
	cList1 = new CyclicArrayList<Integer>(new Integer[] {
		});
	cList2 = new CyclicArrayList<Integer>(new Integer[] {
		});
	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    flag = cList1.equals(cList2);
	}
	step = System.currentTimeMillis()-step;
	assertTrue(flag);
	//reportTestCase(" equals:              ");

    } // testEquals 

    public void testToArray() {


	// testcase 1
	//
	// given an array which is to small to comprise this cyclic list. 
	//
	objArray1 = new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		};
	cList1 = new CyclicArrayList<Integer>(objArray1);

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    objArray1 = (Integer[])cList1.toArray(0,new Integer[2]);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertTrue(objArray1 instanceof Integer[]);
	objArray2 = new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		};
	Assert.assertArraysEquals(objArray2,objArray1);
	//reportTestCase(" toArray:             ");


	// testcase 2
	//
	// given an array which is to small to comprise this cyclic list. 
	//
	objArray1 = new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3),
	    new Integer(4)
		};
	cList1 = new CyclicArrayList<Integer>(objArray1);

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    objArray1 = cList1.toArray(3,new Integer[5]);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertTrue(objArray1 instanceof Integer[]);
	objArray2 = new Integer[] {
	    new Integer(3),
	    new Integer(4),
	    new Integer(0),
	    new Integer(1),
	    new Integer(2)
		};
	Assert.assertTrue(objArray1.length == objArray2.length);
	for (int i = 0; i < objArray1.length; i++) {
	    Assert.assertTrue("Found " + objArray1[i] + 
			      " instead of " + objArray2[i] + 
			      " in " + i + "th entry. ",
			      objArray2[i].equals(objArray1[i]));
	}
	//reportTestCase(" toArray:             ");


	// testcase 3
	//
	// given an array which is to small to comprise this cyclic list. 
	//
	objArray1 = new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3),
	    new Integer(4)
		};
	cList1 = new CyclicArrayList<Integer>(objArray1);
	
	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    objArray1 = (Integer[])cList1.toArray(-3,new Integer[5]);
	}
	step = System.currentTimeMillis()-step;

	Assert.assertTrue(objArray1 instanceof Integer[]);
	objArray2 = new Integer[] {
	    new Integer(2),
	    new Integer(3),
	    new Integer(4),
	    new Integer(0),
	    new Integer(1)
		};
	Assert.assertTrue(objArray1.length == objArray2.length);
	for (int i = 0; i < objArray1.length; i++) {
	    Assert.assertTrue("Found " + objArray1[i] + 
			      " instead of " + objArray2[i] + 
			      " in " + i + "th entry. ",
			      objArray2[i].equals(objArray1[i]));
	}
	//reportTestCase(" toArray:             ");
    } // testToArray 

    public void testGetInverse() {

	// testcase 1
	//
	// general list 
	//
	objArray1 = new Integer[] {
	    new Integer(0),
	    new Integer(1),
	    new Integer(2),
	    new Integer(3),
	    new Integer(4)
		};
	cList1 = new CyclicArrayList<Integer>(objArray1);

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList2 = cList1.getInverse();
	}
	step = System.currentTimeMillis()-step;

	objArray1 = new Integer[] {
	    new Integer(4),
	    new Integer(3),
	    new Integer(2),
	    new Integer(1),
	    new Integer(0)
		};
	cList3 = new CyclicArrayList<Integer>(objArray1);
	Assert.assertEquals(cList3,cList2);
	//reportTestCase(" getInverse:          ");


	// testcase 2
	//
	// empty list
	//
	objArray1 = new Integer[] {
		};
	cList1 = new CyclicArrayList<Integer>(objArray1);

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList2 = cList1.getInverse();
	}
	step = System.currentTimeMillis()-step;

	objArray1 = new Integer[] {
		};
	cList3 = new CyclicArrayList<Integer>(objArray1);
	Assert.assertEquals(cList3,cList2);
	//reportTestCase(" getInverse:          ");



	// testcase 2
	//
	// one element in list
	//
	objArray1 = new Integer[] {
	    new Integer(0)
		};
	cList1 = new CyclicArrayList<Integer>(objArray1);

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList2 = cList1.getInverse();
	}
	step = System.currentTimeMillis()-step;

	objArray1 = new Integer[] {
	    new Integer(0)
		};
	cList3 = new CyclicArrayList<Integer>(objArray1);
	Assert.assertEquals(cList3,cList2);
	//reportTestCase(" getInverse:          ");


	// testcase 2
	//
	// two elements in list
	//
	objArray1 = new Integer[] {
	    new Integer(0),
	    new Integer(1)
		};
	cList1 = new CyclicArrayList<Integer>(objArray1);

	step = System.currentTimeMillis();
	for (int i = 0; i < repetition; i++) {
	    cList2 = cList1.getInverse();
	}
	step = System.currentTimeMillis()-step;

	objArray1 = new Integer[] {
	    new Integer(0),
	    new Integer(1)
		};
	cList3 = new CyclicArrayList<Integer>(objArray1);
	Assert.assertEquals(cList3,cList2);
	//reportTestCase(" getInverse:          ");


    } // testGetInverse 

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    public static junit.framework.Test suite() {
	return new JUnit4TestAdapter(CyclicArrayListTest.class);
    }


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {

	JUnitCore core = new JUnitCore();
	core.addListener(new GUIRunListener());
	core.run(CyclicArrayListTest.class);
    }

}
