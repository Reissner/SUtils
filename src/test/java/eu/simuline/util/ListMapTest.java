
package eu.simuline.util;

import eu.simuline.testhelpers.Actions;
//import eu.simuline.testhelpers.Accessor;
import eu.simuline.testhelpers.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
//import static org.junit.Assert.fail;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Collection;

@RunWith(Suite.class)
@SuiteClasses({ListMapTest.TestAll.class})
public class ListMapTest {

    ListMap<Integer,Integer> listMap;
    ListMap<Integer,Integer> listMap2;

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    static ListMapTest TEST = new ListMapTest();

    public static class TestAll {

	@Test public void testClearIsEmpty() {
	    ListMapTest.TEST.testClearIsEmpty();	    
	}

	@Test public void testContains() {
	    ListMapTest.TEST.testContains();	    
	}

	@Ignore @Test public void testEntrySet() {
	    ListMapTest.TEST.testEntrySet();
	}

	@Test public void testEquals() {
	    ListMapTest.TEST.testEquals();	    
	}

 	@Test public void testGetPutRemove() {
	    ListMapTest.TEST.testGetPutRemove();	    
	}

    } // class TestAll


    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    public void testClearIsEmpty() {
	this.listMap = new ListMap<Integer,Integer>();
	this.listMap.put(3,1);
	this.listMap.put(2,2);
	this.listMap.put(1,3);
	Assert.assertArraysEquals(new Object[] {3, 2, 1},
				  this.listMap.keySet().toArray());

	assertTrue(!this.listMap.isEmpty());
	this.listMap.clear();
	assertTrue( this.listMap.isEmpty());

    } // testClear

    public void testContains() {
	this.listMap = new ListMap<Integer,Integer>();
	this.listMap.put(3,10);
	this.listMap.put(2,20);
	this.listMap.put(1,30);
	//this.listMap.put(null,30);// should first be tested with ArraySet ****

	assertTrue( this.listMap.containsKey(3));
	assertTrue(!this.listMap.containsKey(30));
	assertTrue( this.listMap.containsValue(30));
	assertTrue(!this.listMap.containsValue(3));
    } // testContains 

    // **** to be finished 
    public void testEntrySet() {
	Set<Map.Entry<Integer,Integer>> entries;
	this.listMap = new ListMap<Integer,Integer>();
	this.listMap.put(3,10);
	this.listMap.put(2,20);
	this.listMap.put(1,30);

	entries = this.listMap.entrySet();


    } // testEntrySet() 

    public void testEquals() {
	Set<Map.Entry<Integer,Integer>> entries;
	this.listMap = new ListMap<Integer,Integer>();
	this.listMap.put(3,10);
	this.listMap.put(2,20);
	this.listMap.put(1,30);

	this.listMap2 = new ListMap<Integer,Integer>();
	this.listMap2.put(2,20);
	this.listMap2.put(1,30);
	this.listMap2.put(3,10);

	assertTrue( this.listMap.equals(this.listMap2));
	assertEquals(20, (int)this.listMap2.remove(2));
	assertTrue(!this.listMap.equals(this.listMap2));

    } // testEquals() 

    public void testGetPutRemove() {
	Set<Map.Entry<Integer,Integer>> entries;
	this.listMap = new ListMap<Integer,Integer>();
	assertNull(this.listMap.put(3,10));
	assertNull(this.listMap.put(2,20));
	assertNull(this.listMap.put(1,30));

	assertEquals(20, (int)this.listMap.put(2,21));
	assertEquals(21, (int)this.listMap.get(2));
	assertEquals(21, (int)this.listMap.get(2));

	this.listMap = new ListMap<Integer,Integer>();
	this.listMap.put(3,1);
	this.listMap.put(2,2);
	this.listMap.put(1,3);
	Assert.assertArraysEquals(new Object[] {3, 2, 1},
				  this.listMap.keySet().toArray());
	Assert.assertArraysEquals(new Object[] {1, 2, 3},
				  this.listMap.values().toArray());

	assertEquals(3, this.listMap.size());
	assertEquals(1, (int)this.listMap.remove(3));
	assertEquals(2, this.listMap.size());
	assertEquals(2, (int)this.listMap.remove(2));
	assertEquals(1, this.listMap.size());
	assertEquals(3, (int)this.listMap.remove(1));
	assertEquals(0, this.listMap.size());
    } // testGetPutRemove 


    public void testKeySet() {
	Set<Integer> keys;
	this.listMap = new ListMap<Integer,Integer>();
	this.listMap.put(3,10);
	this.listMap.put(2,20);
	this.listMap.put(1,30);

	keys = new HashSet<Integer>();
	keys.add(3);
	keys.add(2);
	keys.add(1);

	// based on method contains(Object) 
	assertEquals(keys, this.listMap.keySet());

	assertTrue(!this.listMap.keySet().remove(4));
	assertTrue( this.listMap.keySet().remove(3));
	assertTrue(!this.listMap.keySet().remove(3));
	assertNull( this.listMap.remove(3));

	assertNull( this.listMap.put(3, 10));
	assertTrue( this.listMap.keySet().contains(3));
	assertNull( this.listMap.remove(3));
	assertTrue(!this.listMap.keySet().contains(3));

    } // testKeySet() 

    // **** not correct 
   public void testValues() {
	Collection<Integer> values;
	this.listMap = new ListMap<Integer,Integer>();
	this.listMap.put(3,10);
	this.listMap.put(2,10);
	this.listMap.put(1,30);

	values = new HashSet<Integer>();
	values.add(30);
	values.add(20);
	values.add(10);

	// based on method contains(Object) 
	assertEquals(values, this.listMap.values());

	assertTrue(!this.listMap.values().remove(4));
	assertTrue( this.listMap.values().remove(30));
	assertTrue(!this.listMap.values().remove(30));
	assertNull( this.listMap.remove(3));

	assertNull( this.listMap.put(3, 10));
	assertTrue( this.listMap.values().contains(10));
	assertNull( this.listMap.remove(3));
	assertTrue(!this.listMap.values().contains(10));

    } // testValues() 


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

