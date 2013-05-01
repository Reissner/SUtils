
package eu.simuline.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Resizable-array implementation of the <tt>CyclicList</tt> interface. 
 * Implements all optional operations, and permits all elements, 
 * including<tt>null</tt>. 
 * In addition to implementing the <tt>>CyclicList</tt> interface, 
 * this class provides methods to manipulate the size of the array that is
 * used internally to store the list. 
 * <p>
 * The <tt>size</tt>, <tt>isEmpty</tt>, <tt>get</tt>, <tt>set</tt>,
 * and <tt>iterator</tt> operations run in constant time. 
 * The <tt>add</tt> operation runs in <i>amortized constant time</i>, 
 * that is, adding n elements requires O(n) time. 
 * All of the other operations run in linear time (roughly speaking). 
 * <!--The constant factor is low compared 
 * to that for the <tt>LinkedList</tt> implementation. -->
 *<p>
 * Each <tt>CyclicArrayList</tt> instance has a <i>capacity</i>. 
 * The capacity is the size of the array 
 * used to store the elements in the list. 
 * It is always at least as large as the list size. 
 * As elements are added an ArrayList, its capacity grows automatically. 
 * The details of the growth policy are not specified beyond the fact 
 * that adding an element has constant amortized time cost. 
 * <p>
 * An application can increase the capacity 
 * of a <tt>CyclicArrayList</tt> instance
 * before adding a large number of elements 
 * using the <tt>ensureCapacity</tt> operation. 
 * This may reduce the amount of incremental reallocation. 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> 
 * if Multiple threads access an <tt>CyclicArrayList</tt> instance 
 * concurrently, 
 * and at least one of the threads modifies the list structurally, 
 * it <i>must</i> be synchronized externally. 
 * (A structural modification is any operation 
 * that adds or deletes one or more elements, 
 * or explicitly resizes the backing array; 
 * merely setting the value of an element is not a structural modification.) 
 * This is typically accomplished by synchronizing on some object 
 * that naturally encapsulates the list. 
 * <!--If no such object exists, the list should be "wrapped" 
 * using the <tt>Collections.synchronizedList</tt>
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the list:
 * <pre>
 *      List list = Collections.synchronizedList(new ArrayList(...));
 * </pre>-->
 * <p>
 * The iterator returned by this class's <tt>iterator</tt> and
 * <tt>iterator(int)</tt> methods are <i>fail-fast</i>: 
 * if list is structurally modified 
 * at any time after the iterator is created, 
 * in any way except through the iterator's own remove or add methods, 
 * the iterator will throw a <tt>ConcurrentModificationException</tt>. 
 * Thus, in the face of concurrent modification, 
 * the iterator fails quickly and cleanly, rather than risking arbitrary, 
 * non-deterministic behavior at an undetermined time in the future.
 * 
 * @author <a href="mailto:Ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
public class CyclicArrayList<E> implements CyclicList<E>, Cloneable {// NOPMD

    /*----------------------------------------------------------------------*/
    /* Inner classes                                                        */
    /*----------------------------------------------------------------------*/

    /**
     * An iterator over a {@link CyclicList}. 
     * <code>CyclicIterator</code> corresponds with <code>CyclicList</code>s 
     * as <code>Iterator</code>s or <code>ListIterator</code>s does 
     * with <code>List</code>s. 
     * Nevertheless, <code>CyclicIterator</code> 
     * does not implement <code>java.util.Iterator</code>. 
     *
     * @see CyclicList
     * @author <a href="mailto:Ernst.Reissner@eu.simuline.de">Ernst Reissner</a>
     * @version 1.0
     */
    public static class CyclicArrayIterator<E> implements CyclicIterator<E> {

	/*------------------------------------------------------------------*/
	/* Fields                                                           */
	/*------------------------------------------------------------------*/

	/**
	 * The condition <code>calledLast == CALLED_NOTHING</code> means 
	 * that neither of the methods 
	 * {@link #next}, {@link #previous}, {@link #add} and {@link #remove} 
	 * was ever successfully invoked 
	 * since this iterator was created or refreshed. 
	 *
	 * @see #refresh
	 */
	private final static int CALLED_NOTHING = 0;


	/**
	 * The condition <code>calledLast == CALLED_PREVIOUS</code> means 
	 * that among the methods 
	 * {@link #next}, {@link #previous}, {@link #add} and {@link #remove} 
	 * the method {@link #previous} was the last successfully invoked 
	 * since this iterator was created or refreshed. 
	 */
	private final static int CALLED_PREVIOUS = 1;

	/**
	 * The condition <code>calledLast == CALLED_NEXT</code> means 
	 * that among the methods 
	 * {@link #next}, {@link #previous}, {@link #add} and {@link #remove} 
	 * the method {@link #next} was the last successfully invoked 
	 * since this iterator was created or refreshed. 
	 */
	private final static int CALLED_NEXT = 2;

	/**
	 * The condition <code>calledLast == CALLED_ADD</code> means 
	 * that among the methods 
	 * {@link #next}, {@link #previous}, {@link #add} and {@link #remove} 
	 * the method {@link #add} was the last successfully invoked 
	 * since this iterator was created or refreshed. 
	 */
	private final static int CALLED_ADD = 3;

	/**
	 * The condition <code>calledLast == CALLED_REMOVE</code> means 
	 * that among the methods 
	 * {@link #next}, {@link #previous}, {@link #add} and {@link #remove} 
	 * the method {@link #previous} was the last invoked 
	 * since this iterator was created or refreshed. 
	 */
	private final static int CALLED_REMOVE = 4;

	/**
	 * Indicates the last method invoked. 
	 *
	 * @see #CALLED_NOTHING
	 * @see #CALLED_PREVIOUS
	 * @see #CALLED_NEXT
	 * @see #CALLED_ADD
	 * @see #CALLED_REMOVE
	 * @see #refresh
	 */
	protected int calledLast;

	/**
	 * A non-negative index which points, 
	 * modulo <code>list.size()-1</code>, 
	 * to the <tt>0,...,list.size()-1</tt>th element 
	 * of <code>list</code> 
	 * provided {@link #list} is not empty; 
	 * if it is empty, <tt>index == -1</tt>
	 */
	protected int index;

	/**
	 * Points to the beginning of this cyclic list: 
	 * <code>this.hasPrev() == false</code> 
	 * iff <code>index == startIndex</code>. 
	 * It also determines the end of this list implicitly: 
	 * <code>this.hasNext() == false</code> 
	 * iff <code>this.index &lt; this.startIndex+this.list.size()</code>. 
	 * This is true also for <code>{@link #list}.isEmpty()<code>
	 *
	 * @see #hasNext
	 * @see #hasPrev
	 * @see #index
	 */
	protected int startIndex;

	protected CyclicArrayList<E> cal;

	/*------------------------------------------------------------------*/
	/* Constructors                                                     */
	/*------------------------------------------------------------------*/

	/**
	 * Creates a new <code>CyclicIterator</code> 
	 * for the given list, 
	 * pointing to the element with the position given. 
	 * This position is modulo <code>list.size()</code> 
	 * and may also be negative. 
	 * <p>
	 * Note that this list may be empty 
	 * (in which case the indices are -1) 
	 * but by construction it may not be <code>null</code>. 
	 *
	 * @param index 
	 *    an index modulo <code>list.size()</code>, 
	 *    provided <code>list</code> is not empty. 
	 *    In the latter case, <code>index</code> is ignored
	 */
	public CyclicArrayIterator(CyclicArrayList<E> cal, 
				   int index) {
	    this.cal = cal;
	    // Initialize indices. 
	    this.index = 
		this.cal.isEmpty() 
		? -1 
		: this.cal.shiftIndex(index);
	    this.startIndex = this.index;
	    this.calledLast = CALLED_NOTHING;
	}

	/**
	 * Creates a fresh <code>CyclicPtIterator</code> 
	 * with the same list and the same pointer 
	 * as the <code>CyclicPtIterator</code> given. 
	 * For the definition of "fresh" see 
	 * {@link eu.simuline.graphDV.model.ClosedPolyline.CyclicPtIterator class}. 
	 *
	 * @param iter 
	 *    some <code>CyclicPtItererator</code>. 
	 */
	public CyclicArrayIterator(CyclicArrayList<E> cal, 
				   CyclicArrayIterator<E> iter) {
	    this(cal,iter.index);
	}

	public int getFirstIndex() {
	    return this.startIndex;
	}

	/**
	 * Reinitializes this iterator without changing the cursor 
	 * (i.e. modulo <code>list.size()</code>) 
	 * but such that all elements of the corresponding cyclic list 
	 * may be accessed successively through {@link #next}. 
	 * On the other hand, {@link #previous} throws an exception. 
	 */
	public void refresh() {
	    if (this.cal.isEmpty()) {
		return;
	    }
	    // Here, the underlying list is not empty. 
	    this.index = this.cal.shiftIndex(this.index);
	    this.startIndex = this.index;
	    this.calledLast = CALLED_NOTHING;
	}

	/**
	 * Sets the pointer to the given index modulo the length of the list. 
	 * For empty list: no change. 
	 *
	 * @param index 
	 *    an <code>int</code> 
	 *    representing a pointer on the underlying list. 
	 *    This may also be negative. 
	 */
	public void setIndex(int index) {
	    if (this.cal.isEmpty()) {
		return;
	    }
	    
	    this.index = this.cal.shiftIndex(index);
	    // Here, this.index = -list.size(), ..., list.size()-1. 
	    while (this.index < this.startIndex) {
		this.index += this.cal.size();
	    }
	    // Here, this.index = 0, ..., list.size()-1. 
	}
    
	/**
	 * Returns the current index of this iterator. 
	 *
	 * @return an <code>int</code> value
	 * <!--deprecated replaced by nextIndex-->
	 */
	public int getIndex() {

	    return this.cal.size() == 0 
		? this.index
		: this.index%this.cal.size();
	}

	/**
	 * Returns the index of the element 
	 * that would be returned by a subsequent call to <tt>next</tt>.
	 *
	 * @return 
	 *    the index of the element 
	 *    that would be returned by a subsequent call to <tt>next</tt>. 
	 *    The range is <tt>0,...,size()-1</tt>. 
	 */
	//public int nextIndex() {
	//	return this.index%this.cal.size();
	//}


	/**
	 * Returns the <code>CyclicList</code> this iterator points to. 
	 *
	 * @return 
	 *    the <code>CyclicList</code> this iterator points to. 
	 */
	public CyclicList<E> getCyclicList() {
	    return this.cal;
	}

	/*------------------------------------------------------------------*/
	/* Methods for queries (boolean and others)                         */
	/*------------------------------------------------------------------*/


	/**
	 * Returns whether a subsequent call to {@link #next}
	 * would return an element rather than throwing an exception. 
	 *
	 * @return 
	 *    whether a subsequent call to <code>next()</code>
	 *    would return an element rather than throwing an exception. 
	 */
	public boolean hasNext() {
	    return this.index < this.startIndex+this.cal.size();
	}
 
	/**
	 * Returns the next element in the interation. 
	 * This method may be called repeatedly to iterate through the list, 
	 * or intermixed with calls to <tt>previous</tt> to go back and forth. 
	 * (Note that alternating calls to <tt>next</tt> and <tt>previous</tt> 
	 * will return the same element repeatedly.)
	 *
	 * @return 
	 *    the next element in the interation.
	 * @exception NoSuchElementException 
	 *    iteration has no more elements.
	 */
	public E next() throws NoSuchElementException {

	    if (!hasNext()) {
		throw new NoSuchElementException();
	    }
	    this.calledLast = CALLED_NEXT;
	    return this.cal.get(this.index++);
	}

	/**
	 * Returns whether a subsequent call to {@link #previous} 
	 * does not throw an exception. 
	 *
	 * @return 
	 *    Returns whether a subsequent call to {@link #previous} 
	 *    does not throw an exception. 
	 */
	public boolean hasPrev() {
	    return this.index > this.startIndex;
	}

	/**
	 * Returns the previous element in the cyclic list. 
	 * This method may be called repeatedly 
	 * to iterate through the list backwards, 
	 * or intermixed with calls to <tt>next</tt> to go back and forth. 
	 * (Note that alternating calls to <tt>next</tt> and <tt>previous</tt> 
	 * will return the same element repeatedly.) 
	 *
	 * @return 
	 *    the previous element in the list. 
	 *
	 * @exception NoSuchElementException 
	 *    if the iteration has no previous element. 
	 */
	public E previous() throws NoSuchElementException {
	    if (!hasPrev()) {
		throw new NoSuchElementException();
	    }
	    this.calledLast = CALLED_PREVIOUS;
	    return this.cal.get(--this.index);
	}

	/**
	 * Returns the (non-negative) index 
	 * of the next object returned by <code>next</code> 
	 * which equals the given one, if possible; 
	 * otherwise returns <code>-1</code>. 
	 *
	 * @param obj
	 *     an object. 
	 * @return 
	 *    <ul>
	 *    <li> 
	 *    the index minimal index 
	 *    <tt>ind in {0,...,this.cal.size()-1}</tt> 
	 *    satisfying 
	 *    <code>obj.equals(this.cal.get(ind))</code> 
	 *    if possible;
	 *    <li>
	 *    <code>-1</code> if there is no such index. 
	 *    </ul>
	 */
	public int getNextIndexOf(E obj) {

	    if (this.cal.isEmpty()) {
		return -1;
	    }
	    // Here, the underlying list is nontrivial. 

	    // store current pointer, to restore it afterwards. 
	    int oldPointer = getIndex();
	    if (obj == null) {
		// Here, obj == null. 
		while (this.hasNext()) {
		    if (next() == null) {
			previous();
			return getIndex();
		    }
		}		 
	    } else {
		// Here, obj != null. 
		while (this.hasNext()) {
		    if (obj.equals(next())) {
			previous();
			return getIndex();
		    }
		}
	    }
	    // Here, no such element is found. 

	    setIndex(oldPointer);
	    return -1;
	}

	/*------------------------------------------------------------------*/
	/* Methods for modifications                                        */
	/*------------------------------------------------------------------*/

	/* **** old docu: 
	 * Inserts the given object at the current position. 
	 * As a result of this, 
	 * method <code>next</code> will return this object next 
	 * even if formerly <code>hasNext()</code> was false. 
	 *
	 * @param obj 
	 *    An object to be inserted in the list this iterator points to. 
	 */
	/**
	 * Inserts the specified element into the cyclic list. 
	 * The element is inserted immediately before the next element 
	 * that would be returned by <tt>next</tt>, if any, 
	 * and after the next element 
	 * that would be returned by <tt>previous</tt>, if any. 
	 * (If the cyclic list is empty, 
	 * the new element becomes the sole element on the cyclic list.) 
	 * <p>
	 * The new element is inserted before the implicit cursor: 
	 * a subsequent call to <tt>next</tt> would be unaffected, 
	 * and a subsequent call to <tt>previous</tt> 
	 * would return the new element. 
	 * (This call increases by one the value 
	 * that would be returned by a call 
	 * to <tt>nextIndex</tt> or to <tt>previousIndex</tt>.) 
	 *
	 * @param obj 
	 *    An object to be inserted in the list this iterator points to. 
	 */
	public void add(E obj) {

	    this.cal.add(this.index,obj);
	    if (this.index      % this.cal.size() < 
		this.startIndex % this.cal.size()) {
		this.startIndex++;
	    }

	    this.index++;
	    this.calledLast = CALLED_ADD;
	}

	/**
	 * Inserts the specified list into the underlying cyclic list. 
	 * The list is inserted immediately before the next element 
	 * that would be returned by <tt>next</tt>, if any, 
	 * and after the next element 
	 * that would be returned by <tt>previous</tt>, if any. 
	 * (If the cyclic list is empty, 
	 * the new cyclic list comprises the given list.) 
	 * <p>
	 * The given list is inserted before the implicit cursor: 
	 * a subsequent call to <tt>next</tt> would be unaffected, 
	 * and a subsequent call to <tt>previous</tt> 
	 * would return the given list in reversed order. 
	 * (This call increases by <code>list.size()</code> 
	 * the value that would be returned by a call 
	 * to <tt>nextIndex</tt> or <tt>previousIndex</tt>.) 
	 * <p>
	 * If <code>list.size()</code> contains a single element <tt>e</tt>, 
	 * <code>addAll(list)</code> is equivalent with <code>add(e)</code>.
	 *
	 * @param addList 
	 *    the list to be inserted.
	 */
	public void addAll(List<? extends E> addList) {

	    this.cal.addAll(this.index,addList);
	    if (this.cal.isEmpty()) {
		this.startIndex = this.index = -1;
		return;
	    }
	    // Here, the result is not an empty list. 
	    if (this.index      % this.cal.size() < 
		this.startIndex % this.cal.size()) {
		this.startIndex += addList.size();
	    }

	    this.index += addList.size();
	    this.calledLast = CALLED_ADD;
	}

	/**
	 * Replaces the last element 
	 * returned by <tt>next</tt> or <tt>previous</tt> 
	 * with the specified element (optional operation). 
	 * This call can be made only 
	 * if neither <tt>ListIterator.remove</tt> nor <tt>add</tt> 
	 * have been called after the last call to 
	 * <tt>next</tt> or <tt>previous</tt>. 
	 *
	 * @param obj
	 *    the element with which to replace the last element 
	 *    returned by next or previous. 
	 * @exception IllegalStateException 
	 *    if neither <tt>next</tt> nor <tt>previous</tt> have been called, 
	 *    or <tt>remove</tt> or <tt>add</tt> have been called 
	 *    after the last call to <tt>next</tt> or <tt>previous</tt>. 
	 */
	public void set(E obj) {

	    switch (this.calledLast) {
	    case CALLED_ADD:
		// fall through. 
	    case CALLED_REMOVE:
		// fall through. 
	    case CALLED_NOTHING:
		throw new IllegalStateException
		    ("No pointer to set object <" + obj + ">. ");
	    case CALLED_NEXT:
		this.cal.set(this.index-1,obj);
		break;
	    case CALLED_PREVIOUS:
		this.cal.set(this.index,obj);
		break;	     
	    default:
		throw new IllegalStateException("****");
	    }
	}

	/**
	 * Removes from the underlying <code>CyclicList</code> 
	 * the last element returned by the iterator. 
	 * This method can be called only once 
	 * per call to <code>next</code> or to <code>previous</code>. 
	 * The behavior of an iterator is unspecified 
	 * if the underlying collection is modified 
	 * while the iteration is in progress in any way other 
	 * than by calling this method.
	 *
	 * @exception IllegalStateException 
	 *    if the <code>next</code> method has not yet been called, 
	 *    or the <code>remove</code> method has already been called 
	 *    after the last call to the <code>next</code> method.       
	 */
	public void remove() {

	    switch (calledLast) {
	    case CALLED_REMOVE:
		// fall through 
	    case CALLED_ADD:
		// fall through. 
	    case CALLED_NOTHING:
		//this.calledLast = CALLED_REMOVE;
		throw new IllegalStateException
		    ("No pointer to remove object. ");

	    case CALLED_NEXT:
		this.index--;
		this.cal.remove(this.index);
		// **** what if this.cal.size() == 0: impossible 
		if (this.index <= this.startIndex%this.cal.size()) {
		    this.startIndex--;
		}
		break;
	    case CALLED_PREVIOUS:
		this.cal.remove(this.index); 
		break;
	    default:
		throw new IllegalStateException("****");
	    }
	    this.calledLast = CALLED_REMOVE;
	}


	/*------------------------------------------------------------------*/
	/* Methods visualization and for tests                              */
	/*------------------------------------------------------------------*/

	/**
	 * Returns whether <code>other</code> 
	 * is also an instance of <code>CyclicIterator</code> 
	 * and, if so, whether the underlying list and the indices coincide. 
	 *
	 * @param other 
	 *    another <code>Object</code>; possibly <code>null<code>. 
	 * @return 
	 *    Returns <code>false</code> 
	 *    if <code>other</code> is not an instance 
	 *    of <code>CyclicIterator</code>. 
	 *    Otherwise returns <code>true</code> 
	 *    if all of the following methods return equal values: 
	 *    {@link #getIndex}, {@link #getFirstIndex} 
	 *    and {@link #getCyclicList}. 
	 */
	public boolean equals(Object other) {

	    if (!(other instanceof CyclicIterator)) {
		return false; 
	    }
	    CyclicIterator otherIter = (CyclicIterator)other;
	    if (this.cal.equals(otherIter.getCyclicList()) && 
		this.getFirstIndex() == otherIter.getFirstIndex() &&
		this.getIndex()      == otherIter.getIndex()) {
		return true;
	    }
	    return false;
	}

	/**
	 * Returns whether the two cyclic iterators given 
	 * return the same elements in the same order 
	 * if method {@link #next} is invoked sequentially. 
	 *
	 * @param other 
	 *    another <code>CyclicIterator</code>. 
	 * @return 
	 *    whether the two cyclic iterators given 
	 *    return the same elements in the same order 
	 *    if method {@link #next} is invoked sequentially 
	 *    as long as possible. 
	 *    This imposes that the lengths of the sequences coincide. 
	 *    The elements in the sequence may well be <code>null</code>. 
	 */
	public boolean retEquals(CyclicIterator<E> other) {

	    while (this.hasNext() && other.hasNext()) {
		// Here, this.hasNext() and other.hasNext(). 
		Object thi = this.next();
		Object obj = other.next();
		if (thi == null) {
		    if (obj != null) {
			// Here, there the current elements are not equal. 
			return false;
		    }
		} else {
		    // Here, thi != null. 
		    if (!thi.equals(obj)) {
			// Here, there the current elements are not equal. 
			return false;
		    }
		}
	    } // while 
	    // Here, !this.hasNext() || !other.hasNext(). 

	    if (this.hasNext() ^ other.hasNext()) {
		// Here, exactly one ot the iterators has a next element. 
		// Thus the number of elements is not equal. 
		//System.out.println("!eq len:    ");
		return false;
	    }
	    // Here, !this.hasNext() and !other.hasNext(). 
	    // Moreover, up to now, this.next().equals(other.next()). 
	    return true;
	}

	public double dist(CyclicIterator<E> other) {
	    throw new eu.simuline.util.NotYetImplementedException();
	}

	/**
	 * Returns a string representation consisting of 
	 * <ul>
	 * <li>
	 * the cyclic list corresponding with this iterator . 
	 * <li>
	 * The current pointer. 
	 * <li>
	 * The first index i of this iterator. 
	 * and the last one (which is i+size()-1). 
	 * ******* empty list?!?
	 * </ul>
	 *
	 * @return 
	 *    a <code>String</code> representing this iterator. 
	 */
	public String toString() {
	    StringBuffer ret = new StringBuffer(70);
	    ret.append("<CyclicIterator firstIndex=\"");
	    ret.append(Integer.toString(this.startIndex));
	    ret.append("\" index=\"");
	    ret.append(Integer.toString(this.index));
	    ret.append("\">\n");
	    ret.append(getCyclicList().toString());
	    ret.append("</CyclicIterator>\n");

	    return ret.toString();
	}

    } // class CyclicArrayIterator 

    /*----------------------------------------------------------------------*/
    /* Fields                                                               */
    /*----------------------------------------------------------------------*/

    /**
     * The array this implementation of CyclicList is based on. 
     * it satisfies <code>this.get(i) == list.get(i)</code> 
     * for all indices <code>i</code> for which the right hand side is valid. 
     */
    private List<E> list;


    /*----------------------------------------------------------------------*/
    /* Constructors                                                         */
    /*----------------------------------------------------------------------*/

    /**
     * Creates a new empty <code>CyclicArrayList</code>. 
     */
    public CyclicArrayList() {
	this(new ArrayList<E>());
    }

    /**
     * Creates a new <code>CyclicArrayList</code> 
     * such that <code>new CyclicArrayList(list).get(i) == list.get(i)</code> 
     * for all indices <code>i</code> for which the right hand side is valid. 
     *
     * @param list 
     *    some array of objects. 
     */
    public CyclicArrayList(E[] list) {
	this.list = new ArrayList<E>(Arrays.asList(list));
    }

    /**
     * Creates a new <code>CyclicArrayList</code> 
     * such that <code>new CyclicArrayList(list).get(i) == list.get(i)</code> 
     * for all indices <code>i</code> for which the right hand side is valid. 
     *
     * @param list 
     *    some list of objects. 
     */
    public CyclicArrayList(List<? extends E> list) {
	this.list = new ArrayList<E>(list);
    }

    /**
     * Copy constructor. 
     *
     * @param other 
     *    some cyclic list of objects. 
     */
    public CyclicArrayList(CyclicList<? extends E> other) {
	this.list = new ArrayList<E>(other.asList());
    }

    /*----------------------------------------------------------------------*/
    /* methods implementing CyclicList                                      */
    /*----------------------------------------------------------------------*/

    /**
     * Returns the number of elements in this list. 
     * If this list contains more than <tt>Integer.MAX_VALUE</tt> elements, 
     * returns <tt>Integer.MAX_VALUE</tt>.
     *
     * @return 
     *    the number of elements in this list. 
     */
    public int size() {
	return this.list.size();
    }

    /**
     * Returns <tt>true</tt> iff this list contains no elements.
     *
     * @return <tt>true</tt> iff this list contains no elements.
     */
    public boolean isEmpty() {
	return size() == 0;
    }

    /**
     * Returns the inverse of this cyclic list: 
     * the list with inverse order. 
     *
     * @return 
     *    The list with the same entries but inverse order. 
     */
    public CyclicList<E> getInverse() {

	CyclicList<E> result = new CyclicArrayList<E>();
	for (int i = 0; i < this.size(); i++) {
	    result.add(0,this.get(i));
	}
	return result;
    }

    /**
     *
     * Returns <tt>true</tt> if this list contains the specified element. 
     * More formally, returns <tt>true</tt> 
     * if and only if this list contains at least one element <tt>e</tt> 
     * such that 
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>. 
     *
     * @param obj 
     *    element whose presence in this list is to be tested.
     * @return 
     *    <tt>true</tt> if this list contains the specified element.
     */
    public boolean contains(Object obj) {
	for (int i = 0; i < size(); i++) {
	    if (obj.equals(get(i))) {
		return true;
	    }
	}	
	return false;
    }

    public boolean containsAll(Collection<?> coll) {
	Iterator iter = coll.iterator();
	while (iter.hasNext()) {
	    if (contains(iter.next())) {
		return true;
	    }
	}	
	return false;
    }

    /**
     * Returns {@link #iterator(int) iterator(index)} 
     * for some unspecified <code>index</code>. 
     * ***** what if this list is empty. 
     *
     * @return 
     *    {@link #iterator(int) iterator(index)} 
     *    for some unspecified <code>index</code>. 
     */
    public CyclicIterator<E> cyclicIterator() {
	return cyclicIterator(0);
    }

    /**
     * Returns a <code>CyclicIterator</code> 
     * of the elements in this list (in proper sequence), 
     * starting at the specified position in this list. 
     * The specified index indicates the first element 
     * that would be returned by an initial call to the <tt>next</tt> method. 
     * An initial call to the <tt>previous</tt> method 
     * would return the element with the specified index minus one 
     * (modulo the length of this cyclic list).
     *
     * @param index 
     *    index of first element to be returned from the list iterator 
     *    (by a call to the <tt>next</tt> method). 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even a negative one) is valid. 
     * @return 
     *    a cyclic iterator of the elements in this list 
     *    (in proper sequence), 
     *    starting at the specified position in this list. 
     */
    public CyclicIterator<E> cyclicIterator(int index) {
	return new CyclicArrayIterator<E>(this,index);
    }

    public Iterator<E> iterator() {
	return iterator(0);
    }
    public Iterator<E> iterator(int ind) {
	return cyclicIterator(ind);
    }

    // **** unspecified order 
    public Object[] toArray() {
	return toArray(0);
    }

    public <E> E[] toArray(E[] ret) {
	return toArray(0,ret);
    }

    /**
     * Returns an array containing all of the elements in this list 
     * in proper sequence. 
     * Modifying the return value does not modify this CyclicList. 
     *
     * @param index 
     *    index of the element in the cyclic list 
     *    which comes first in the array returned. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    an array containing all of the elements in this list 
     *    in proper sequence.
     */
    public Object[] toArray(int index) {
	return toArray(index,new Object[this.list.size()]);
    }

    /**
     * Returns an array containing all of the elements in this list 
     * in proper sequence; 
     * the runtime type of the returned array is that of the specified array. 
     * Modifying the return value does not modify this CyclicList. 
     *
     * @param index 
     *    index of the element in the cyclic list 
     *    which comes first in the array returned. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @param ret
     *    the array into which the elements of this list are to be stored, 
     *    if it is big enough; 
     *    otherwise, a new array of the same runtime type 
     *    is allocated for this purpose. 
     * @return 
     *    an array containing all of the elements in this list 
     *    in proper sequence. 
     * @throws ArrayStoreException 
     *    if the runtime type of the specified array 
     *    is not a supertype of the runtime type 
     *    of every element in this list. 
     */
    public <E> E[] toArray(int index, E[] ret) {
	return cycle(index).list.toArray(ret);
/*
	// if ret.length is not sufficient to store the result... 
	if (ret.length < this.list.size()) {
	    // ... replace ret by a new array. 
	    ret = (Object[])
	    Array.newInstance(ret.getClass().getComponentType(),
			      this.list.size());
	}
	// Here, ret is an array of the appropriate type 
	// which is long enough to store this cyclic list. 
	if (size() == 0) {
	    return ret;
	}
	
	while (index < 0) {
	    index += size();
	}
	while (index >= size()) {
	    System.out.println("index: "+index);
	    
	    index -= size();
	}
	assert 0 <= index && index < size();

	System.arraycopy(this.list, index, 
			 ret,       0,         this.list.size()-index);
	System.arraycopy(this.list, 0,     
			 ret,       this.list.size()-index, index);
	return ret;
*/
    }

    public List<E> asList(int index) {
	return cycle(index).asList();
    }

    public List<E> asList() {
	return new ArrayList<E>(this.list);
    }

    /**
     * Returns a cyclic permutation <tt>p</tt> of this cyclic list. 
     *
     * @param index 
     *    index of the element in the cyclic list 
     *    which comes first in the cyclic list returned. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    a cyclic permutation <tt>p</tt> of this cyclic list. 
     *    It satisfies <code>p.size() == this.size()</code> and 
     *    <code>p.get(i) == this.get(i+num)</code>. 
     */
    public CyclicArrayList<E> cycle(int index) {
	if (size() == 0) {
	    return new CyclicArrayList<E>(this);
	}
	
	while (index < 0) {
	    index += size();
	}
	while (index >= size()) {
	    System.out.println("index: "+index);
	    
	    index -= size();
	}
	assert 0 <= index && index < size();
	ArrayList<E> ret = new ArrayList<E>();
	ret.addAll(this.list.subList(index,this.list.size()));
	ret.addAll(this.list.subList(0,    index));
/*
	System.arraycopy(this.list, index, 
			 ret,       0,         this.list.size()-index);
	System.arraycopy(this.list, 0,     
			 ret,       this.list.size()-index, index);
*/
	return new CyclicArrayList<E>(ret);
    }

    // Modification Operations

    /**
     * Removes all of the elements from this list (optional operation). 
     * This list will be empty after this call returns 
     * (unless it throws an exception).
     */
    public void clear() {
	this.list.clear();
    }

    /**
     * Compares the specified object with this cyclic list for equality. 
     * Returns <tt>true</tt> 
     * if and only if the specified object is also a cyclic list, 
     * both lists have the same size, 
     * and, up to a cyclic permutation, 
     * all corresponding pairs of elements the two lists are <i>equal</i>. 
     * (Two elements <tt>e1</tt> and <tt>e2</tt> are <i>equal</i> 
     * if <tt>(e1==null ? e2==null : e1.equals(e2))</tt>.) 
     * In other words, two lists are defined to be 
     * equal if they contain the same elements in the same order 
     * up to a cyclic permutation. 
     * This definition ensures that the equals method works properly 
     * across different implementations 
     * of the <tt>CyclicList</tt> interface. 
     *
     * @param obj 
     *    the object to be compared for equality with this list. 
     * @return 
     *    <tt>true</tt> if the specified object is equal to this list. 
     */
    public boolean equals(Object obj) {
	//System.out.println("CyclicList.eq(:    ");

	if (!(obj instanceof CyclicList)) {
	    return false;
	}

	CyclicList<E> other = (CyclicList<E>)obj;
	if (this.size() != other.size()) {
	    return false;
	}
	// Here, the two lists of points have the same size. 

	if (size() == 0) {
	    return true;
	}
	// Here, the two lists of points have the same, positive size. 

	CyclicIterator<E> thisIt;
	CyclicIterator<E> otherIt;
	for (int i = 0; i < this.size(); i++) {
	    thisIt  = this .cyclicIterator(i);
	    otherIt = other.cyclicIterator(0);
	    if (thisIt.retEquals(otherIt)) {
		return true;
	    }
	}
	// Here, no index was found that thisIt and otherIt 
	//return the same sequence. 
	return false;
    }

    /**
     * Returns the hash code value for this cyclic list. 
     * The hash code of a list 
     * is defined to be the result of the following calculation: 
     * <pre>
     *  hashCode = 1;
     *  Iterator i = list.iterator();
     *  while (i.hasNext()) {
     *      Object obj = i.next();
     *      hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
     *  }
     * </pre>
     * This ensures that <tt>list1.equals(list2)</tt> implies that 
     * <tt>list1.hashCode()==list2.hashCode()</tt> for any two lists, 
     * <tt>list1</tt> and <tt>list2</tt>, 
     * as required by the general contract of <tt>Object.hashCode</tt>. 
     *
     * @return the hash code value for this list.
     * @see Object#hashCode()
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    public int hashCode() {
	int hashCode = 1;
	for (int i = 0; i < this.size(); i++) {
	    Object obj = this.list.get(i);
	    hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
	}
	return hashCode;
    }

    // Positional Access Operations 

    /**
     * Returns the number which equals <code>index</code> 
     * modulo {@link #size this.size()}, 
     * provided this list is not empty. 
     *
     * @param index 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    the number which equals <code>index</code> 
     *    modulo {@link #size this.size()}, 
     *    provided this list is not empty. 
     * @throws EmptyCyclicListException 
     *    if this list is empty. 
     */
    public int shiftIndex(int index) throws EmptyCyclicListException {
	return shiftIndex(index,size());
    }
    public int shiftIndex(int index,int size) throws EmptyCyclicListException {
	if (size == 0) {
	    throw new EmptyCyclicListException();
	}
	return (index >= 0 ? index%size : (index%size+size)%size);
    }

    /**
     * Returns the element at the specified position in this list, 
     * provided this list is not empty. 
     *
     * @param index 
     *    index of element to return. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    the element at the specified position in this list. 
     * @throws EmptyCyclicListException 
     *    if this list is empty. 
     */
    public E get(int index) throws EmptyCyclicListException {
	return this.list.get(shiftIndex(index));
    }

    /**
     * Replaces the element at the specified position in this list 
     * with the specified element (optional operation), 
     * provided this list is not empty.
     *
     * @param index 
     *    index of the element to replace. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @param element 
     *    element to be stored at the specified position.
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    the element previously at the specified position.
     * @throws EmptyCyclicListException 
     *    if this list is empty. 
     */
    public E set(int index, 
		 E element) throws EmptyCyclicListException {
	index = shiftIndex(index);
	//Object ret = this.list.get(index);
	return this.list.set(index,element);
    }

    /**
     * Replaces the element at the specified position in this list 
     * with the cyclic list of the specified iterator (optional operation). 
     * Places the elements of that list as returned by <code>iter.next</code> 
     * in this list. 
     *
     * @param index index 
     *    index of element to replace. 
     * @param iter 
     *    a <code>CyclicIterator</code> which determines an index in a list 
     *    which replaces <code>this.get(i)</code>. 
     * @throws EmptyCyclicListException 
     *    if this list is empty. 
     * @throws IllegalArgumentException 
     *    if the specified iterator is empty. 
     */
    public void replace(int index, CyclicIterator<E> iter) {
	if (!iter.hasNext()) {
	    throw new IllegalArgumentException
		("Could not replace " + index + 
		 "th element because of void iterator " + iter + ". ");
	}
	set(index++,iter.next());
	addAll(index,iter);
    }

    public void replace(int index, List<E> list) {
	if (list.isEmpty()) {
	    throw new IllegalArgumentException
		("Could not replace " + index + 
		 "th element with empty list. ");
	}
	set(index++,list.get(0));
	addAll(index,list.subList(1,list.size()));
    }

    /**
     * Inserts the cyclic list of the specified iterator 
     * at the specified position in this list (optional operation). 
     * In contrast to {@link #replace(int, CyclicIterator)}, 
     * the element currently at the specified position is not lost. 
     *
     * @param index 
     *    index at which the specified list is to be inserted.
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @param iter 
     *    iterator delivering the elements to be inserted.
     */
    public void addAll(int index, CyclicIterator<E> iter) {
	// ***** very bad implementation. **** 
	while (iter.hasNext()) {
	    add(index++, iter.next());
	}
    }

    /**
     * Inserts the specified list at the given position 
     * in this cyclic list. 
     * ***** done for collections! 
     * Shifts the element currently at that position (if any) 
     * and any subsequent elements to the right (increases their indices). 
     * The new elements will appear in this list 
     * in the order that they are returned 
     * by the specified collection's iterator. 
     * The behavior of this operation is unspecified 
     * if the specified collection is modified 
     * while the operation is in progress. 
     * (Note that this will occur 
     * if the specified collection is this list, and it's nonempty.) 
     * Contract: 
     * <code>list.addAll(i,l);
     * return list.get(i+k)</code> yields <code>list.get(k)</code>, 
     * for all <tt>k</tt> in <tt>0,..,l.size()-1</tt>. 
     * <p>
     * Note that for <tt>l</tt> containing a single element <tt>e</tt>, 
     * <code>list.addAll(i,l)</code> 
     * is equivalent with <code>list.add(i,e)</code>. 
     *
     * @param index 
     *    index at which the specified list is to be inserted.
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @param addList 
     *    the list to be inserted. 
     * **** this is much more complicated! **** 
     */
    public void addAll(int index, List<? extends E> addList) {


	if (addList.isEmpty()) {
	    // nothing to do. 
	    return;
	}
	// Here, addList is not empty. 


	List<E> oldList = this.list;
	this.list = new ArrayList<E>(size()+addList.size());
//System.out.println("oldList[0]: "+oldList[0]);
	// since addList is not empty, size() != 0 
	// and so shiftIndex is defined. 
	int newSize = oldList.size()+addList.size();
	index = shiftIndex(index,newSize);
System.out.println("newSize: "+newSize);
System.out.println("index: "+index);

	// Two cases: 
	//
	// | cyclic list part 1 | list | cyclic list part 2 
	//                        index
	// and 
	// | list part 2 | cyclic list | list part 1 
	//                 ind1          index
	//                 ind1 = (index+addList.size())%size();

	if (index+addList.size() <= newSize) {
	    // Here, the second copy procedure works. 
	    // | cyclic list part 1 | list | cyclic list part 2 
	    //                        index


	    this.list.addAll(oldList.subList(0,index));
	    this.list.addAll(addList);
	    this.list.addAll(oldList.subList(index,newSize-addList.size()));
/*
	    System.arraycopy(oldList,  0,    this.list, 0,    index);
	    // copy indices index-1,...,index+addList.length-1 
	    // (addList.length entries). 
	    System.arraycopy(addList, 0,    this.list, index, addList.size());
	    // copy indices the rest of the old entries 
	    // (this.list.size()-index entries). 
	    System.arraycopy(oldList, index, this.list, index+addList.size(),
			     size()-index-addList.length);
*/
	} else {
	    // | list part 2 | cyclic list | list part 1 
	    //                 ind1          index
	    int ind1 = (index+addList.size())%newSize;
	    int addLen1 = addList.size()-ind1;
	    assert ind1 <= index;
	    this.list.addAll(addList.subList(addLen1,addLen1+ind1));
	    this.list.addAll(oldList);
	    this.list.addAll(addList.subList(0,addLen1));
/*
	    System.arraycopy(oldList,  0,      this.list, ind1,  oldList.length);
	    System.arraycopy(addList, 0,       this.list, index, addLen1);
	    System.arraycopy(addList, addLen1, this.list, 0,     ind1);
*/
	}
    }

    public boolean addAll(Collection<? extends E> coll) {
	throw new UnsupportedOperationException();
    }

    /**
     * Inserts the specified element at the specified position in this list. 
     * Contract: 
     * <code>list.add(i,o);return list.get(i)</code> yields <code>o</code>. 
     * In contrast to {@link #set}, 
     * the element currently at the specified position is not lost. 
     * Also note that this operation is allowed for empty cyclic lists. 
     * In this case, <code>index</code> is irrelevant. 
     *
     * @param index 
     *    index at which the specified element is to be inserted.
     *    This is interpreted modulo the length of this cyclic list plus one 
     *    (The list emerging after the insertion). 
     *    In contrast to {@link java.util.List#add(int,Object)} 
     *    any index (even a negative one) is valid. 
     * @param element 
     *    element to be inserted. 
     */
    public void add(int index, E element) {
	List<E> oldList = this.list;
	this.list = new ArrayList<E>(this.list.size()+1);
	index = shiftIndex(index,oldList.size()+1);
	
	// Copy the elements which remain unchanged. 
	this.list.addAll(oldList.subList(0,index));
	this.list.add(element);
	this.list.addAll(oldList.subList(index,oldList.size()));
/*
	System.arraycopy(oldList,0,    this.list,0,      index);
	System.arraycopy(oldList,index,this.list,index+1,size()-index-1);
	// Add the new element. 
	this.list.set(index,element);
*/
    }

    // **** there is no end of list. 
    public boolean add(E element) {
	//throw new UnsupportedOperationException();
	add(size(),element);
	return true;
    }

    /**
     * Removes the element at the specified position in this list 
     * (optional operation). 
     * Returns the element that was removed from the list, 
     * provided this list is not empty. 
     *
     *
     * @param index 
     *    the index of the element to removed. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    the element previously at the specified position. 
     *
     * @throws EmptyCyclicListException 
     *    if this list is empty. 
     */
    public E remove(int index) throws EmptyCyclicListException {
	// Determine the proper index and allocate the new list. 
	index = shiftIndex(index);
	return this.list.remove(index);
/*
	List<E> newList = new ArrayList<E>(size()-1);
	// Copy the elements which remain unchanged. 
	System.arraycopy(this.list,0,      newList,0,    index);
	System.arraycopy(this.list,index+1,newList,index,size()-index-1);
	this.list = newList;
	return ret;
*/
    }

    public boolean remove(Object obj) {
	throw new UnsupportedOperationException();
	/*
	int index = getIndexOf(obj);
	if (index < 0) {
	    return false;
	}
	remove(index);
	return true;
	*/
    }

    public boolean removeAll(Collection<?> coll) {
	boolean result = false;
	Iterator iter = coll.iterator();
	while (iter.hasNext()) {
	    result |= remove(iter.next());
	}
	return result;
    }


    public boolean retainAll(Collection<?> coll) {
	boolean result = false;
	Iterator iter = this.iterator();
	Object cand;
	while (iter.hasNext()) {
	    cand = iter.next();
	    if (!coll.contains(cand)) {
		remove(cand);
		result = true;
	    }
	}
	return result;
    }

    /**
     * Returns the non-negative index in this cyclic list 
     * of the first occurrence of the specified element, 
     * or some negative index 
     * if this cyclic list does not contain this element. 
     * More formally, returns the lowest index <tt>i</tt> such that 
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, 
     * or some negative index if there is no such index. 
     * <p>
     * This negative index may be e.g. <tt>-insertion point-1</tt> 
     * for binary searches. 
     * Note that this specification slightly differs from 
     * {@link java.util.List#indexOf}. 
     * 
     * @param obj 
     *    element to search for. 
     * @return 
     *    the index in this cyclic list 
     *    of the first occurrence of the specified
     *    element, or some negative index 
     *    if this list does not contain this element.
     */
    public int getIndexOf(E obj) {
	for (int i = 0; i < this.list.size(); i++) {
	    if (this.list.get(i).equals(obj)) {
		return i;
	    }
	}
	// Here, obj is not found in this cyclic list. 
	return -1;
    }

    /**
     * Returns a <code>CyclicList</code> 
     * which is by copying this list n times 
     * to obtain a list of length <code>n</code>. 
     *
     * @param len 
     *    a non-negative <code>int</code> value. 
     * @return 
     *    a <code>CyclicList</code> which is by copying this list n times. 
     * @throws IllegalArgumentException
     *    if n is negative. 
     * @throws EmptyCyclicListException
     *    if this list is empty and <code>len != 0</code>. 
     */
    public CyclicList<E> getCopy(int len) {

	if (len < 0) {
	    throw new IllegalArgumentException
		("Positive length expected; found: " + len + ". ");
	}
	if (this.isEmpty()) {
	    if (len == 0) {
		return new CyclicArrayList<E>();
	    } else {
		throw new EmptyCyclicListException();
	    }
	}
	// Here, len >= 0 and !this.isEmpty(). 
	
	List<E> newList = new ArrayList<E>(len);
	for (int i = 0; i < len/size(); i++) {
	    newList.addAll(this.list);
	    //System.arraycopy(this.list, 0, newList, i*size(), size());
	}
	newList.addAll(this.list.subList(0,len%size()));
	// System.arraycopy(this.list, 0, newList, len/size()*size(), len%size());
	return new CyclicArrayList<E>(newList);
    }


    public String toString() {
	StringBuffer res = new StringBuffer(30);
	res.append("<CyclicList>\n");
	for (int i = 0; i < this.size(); i++) {
	    res.append("" + this.get(i) + " ");
	}
	res.append("</CyclicList>\n");
	return res.toString();
    }

    /*----------------------------------------------------------------------*/
    /* methods implementing Cloneable                                       */
    /*----------------------------------------------------------------------*/

    /**
     * Returns a clone of this <code>CyclicArrayList</code>. 
     * This includes copying<code>vertices</code>. 
     *
     * @return 
     *     a clone of this <code>CyclicArrayList</code>. 
     *     This includes copying <code>vertices</code>. 
     */
    public Object clone() /* NOPMD */ {
	    return new CyclicArrayList<E>
		((List<E>) ((ArrayList<E>)this.list).clone());
    }
}