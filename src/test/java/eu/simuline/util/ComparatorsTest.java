package eu.simuline.util;

import        eu.simuline.testhelpers.Actions;
//import        eu.simuline.testhelpers.Accessor;
import        eu.simuline.testhelpers.Assert;
import static eu.simuline.testhelpers.Assert.assertIs;
import static eu.simuline.testhelpers.Assert.CmpObj.EQUAL;
import static eu.simuline.testhelpers.Assert.CmpObj.LESS;
import static eu.simuline.testhelpers.Assert.CmpObj.GREATER;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
//import static org.junit.Assert.fail;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;


/**
 * Describe class ComparatorsTest here.
 *
 *
 * Created: Thu Oct  3 14:12:02 2013
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({ComparatorsTest.TestAll.class})
public class ComparatorsTest {

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    static final ComparatorsTest TEST = new ComparatorsTest();

    public static class TestAll {
	@Test public void testAsListed() {
	    ComparatorsTest.TEST.testAsListed();	    
	}

    } // class TestAll 


    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    public void testAsListed() {
	List<Integer> list;
	Comparator<Integer> cmp;

	// tests with empty list 
	list = new ArrayList<Integer>();
	cmp = Comparators.getAsListed(list);
	// objects not listed in list are 'equal' 
	assertIs(EQUAL, -1, 1, cmp);

	// tests with one-element list 
	list = new ArrayList<Integer>();
	list.add(1);
	cmp = Comparators.getAsListed(list);
	// objects not listed in list are 'equal' 
	assertIs(EQUAL, -1,  2, cmp);
	// objects not listed are greater than all those listed 
	assertIs(LESS,   1, -1, cmp);
	assertIs(EQUAL,  1,  1, cmp);

	// tests with one-element list 
	list = new ArrayList<Integer>();
	list.add(3);
	list.add(1);
	cmp = Comparators.getAsListed(list);
	// objects not listed in list are 'equal' 
	assertIs(EQUAL, -1,  2, cmp);
	// objects not listed are greater than all those listed 
	assertIs(LESS,   1, -1, cmp);
	assertIs(LESS,   3, -1, cmp);
	// objects listed are equal to themselves 
	assertIs(EQUAL,  1,  1, cmp);
	assertIs(EQUAL,  3,  3, cmp);
	// elements listed in list come in ascending order 
	assertIs(LESS,   3,  1, cmp);

    } // testAsListed 

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
