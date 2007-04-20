package eu.simuline.util;

import java.util.Vector;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * The FIFOList class represents a first-in-first-out (FIFO) stack of objects. 
 * <!--It extends class Vector with five operations 
 * that allow a vector to be treated as a stack. --> 
 * The usual push and pop operations are provided, 
 * as well as a method to peek at the top item on the stack, 
 * a method to test for whether the stack is empty, 
 * and a method to search the stack for an item 
 * and discover how far it is from the top.
 * <p>
 * When a stack is first created, it contains no items.
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
public class FIFOList<E> extends Vector<E> implements Queue<E> {

    /* -------------------------------------------------------------------- *
     * constants.                                                           *
     * -------------------------------------------------------------------- */


    private static final long serialVersionUID = -2479143000061671589L;

    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Creates an empty FIFOList.
     */
    protected FIFOList() {
	// is empty. 
    } // FIFOList constructor

    /**
     * Constructs a vector 
     * containing the elements of the specified collection, 
     * in the order they are returned by the collection's iterator. 
     *
     * @param coll
     *    the collection whose elements are to be placed into this FIFOList. 
     * @throws NullPointerException 
     *    if the specified collection is null.     
     */
    protected FIFOList(Collection<? extends E> coll) {
	super(coll);
    } // FIFOList constructor


    public static <E> FIFOList<E> create() {
	return new FIFOList<E>();
    }

    public static <E> FIFOList<E> create(Collection<? extends E> coll) {
	return new FIFOList<E>(coll);
    }

    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */


    /**
     * Constructs a vector 
     * @param item
     *    the element to insert. 
     * @return 
     *    true since nothing can prevent the item from being inserted. 
     */
    public boolean offer(E item) {
	add(item);
	return true;
    }

    // api-docs provided by javadoc. 
    public E poll() {
	return isEmpty() ? null : remove(0);
    }

    // api-docs provided by javadoc. 
    public E remove() {
	try {
	    return remove(0);
	} catch (ArrayIndexOutOfBoundsException e) {
	    throw new NoSuchElementException();
	}
    }

    // api-docs provided by javadoc. 
    public E peek() {
	return isEmpty() ? null : get(0);
    }

    // api-docs provided by javadoc. 
    public E element() {
	try {
	    return get(0);
	} catch (ArrayIndexOutOfBoundsException e) {
	    throw new NoSuchElementException();
	}
    }
} // FIFOList
