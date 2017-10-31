
package eu.simuline.util;

import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;// for javadoc only 

/**
 * An iterator over a {@link CyclicList}. 
 * <code>CyclicIterator</code> corresponds with <code>CyclicList</code>s 
 * as <code>Iterator</code>s or <code>ListIterator</code>s do 
 * with <code>List</code>s. 
 *
 * @see CyclicList
 * @see java.util.ListIterator
 * @author <a href="mailto:Ernst.Reissner@rose.de">Ernst Reissner</a>
 * @version 1.0
 */
public interface CyclicIterator<E> extends Iterator<E> {

    /*------------------------------------------------------------------*/
    /* Query Operations (boolean and others)                            */
    /*------------------------------------------------------------------*/

    /**
     * Returns the cursor of this iterator 
     * immediately after it has been created 
     * (if not modified since then which is currently not possible.). 
     *
     * @return 
     *    the cursor of this iterator 
     *    immediately after it has been created 
     *    (if not modified since then). 
     * @see CyclicList#cyclicIterator(int)
     */
    int getFirstIndex();

    /**
     * Returns the current cursor of this iterator. 
     *
     * @return 
     *    the current cursor of this iterator. 
     */
    int getIndex();

    /**
     * Returns the cyclic list to which this iterator points. 
     * Contract: 
     * <code>cyclicList.cyclicIterator(int).getCyclicList() == 
     *       cyclicList</code> again. 
     *
     * @return 
     *    the cyclic list to which this iterator points. 
     *    This may be empty but it may not be <code>null</code>. 
     * @see CyclicList#cyclicIterator(int)
     */
    CyclicList<E> getCyclicList();

    /**
     * Returns <code>true</code> if the iteration has more elements. 
     * (In other words, returns <code>true</code> 
     * if <code>next</code> would return an element 
     * rather than throwing an exception.)
     *
     * @return 
     *    <code>true</code> if the iterator has more elements. 
     */
    boolean hasNext();
 
    /**
     * Returns the next element in the interation.
     * This method may be called  repeatedly to iterate through the list, 
     * or intermixed with calls to <code>previous</code> to go back and forth. 
     * (Note that alternating calls to <code>next</code> and <code>previous</code> 
     * will return the same element repeatedly.)
     *
     * @return 
     *    the next element in the interation.
     * @exception NoSuchElementException 
     *    iteration has no more elements.
     */
    E next();

    /**
     * Returns <code>true</code> if this iterator has more elements when 
     * traversing the cyclic list in the reverse direction. 
     * (In other words, returns <code>true</code> 
     * if <code>previous</code> would return an element 
     * rather than throwing an exception.) 
     *
     * @return 
     *    <code>true</code> if the list iterator has more elements 
     *    when traversing the list in the reverse direction. 
     */
    boolean hasPrev();

    /**
     * Returns the previous element in the cyclic list. 
     * This method may be called repeatedly 
     * to iterate through the list backwards, 
     * or intermixed with calls to <code>next</code> to go back and forth. 
     * (Note that alternating calls to <code>next</code> and <code>previous</code> 
     * will return the same element repeatedly.) 
     *
     * @return 
     *    the previous element in the list. 
     * @throws NoSuchElementException 
     *    if the iteration has no previous element. 
     */
    E previous();
 
    /*
     * Returns the index of the element 
     * that would be returned by a subsequent call to <code>next</code>.
     *
     * @return 
     *    the index of the element 
     *    that would be returned by a subsequent call to <code>next</code>. 
     *    The range is <code>0,...,size()-1</code>. 
     */
    //int nextIndex();

    /*
     * Returns the index of the element 
     * that would be returned by a subsequent call to <code>previous</code>. 
     *
     * @return 
     *    the index of the element 
     *    that would be returned by a subsequent call to <code>previous</code>. 
     *    The range is <code>0,...,size()-1</code>. 
     */
    //int previousIndex();

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
     *    the index minimal index <code>ind in {0,...,this.list.size()-1}</code> 
     *    satisfying <code>obj.equals(this.list.get(ind))</code> 
     *    if possible;
     *    <li>
     *    <code>-1</code> if there is no such index. 
     *    </ul>
     */
    int getNextIndexOf(E obj);

    /*------------------------------------------------------------------*/
    /* Modification Operations                                          */
    /*------------------------------------------------------------------*/


    /**
     * Sets the given index as cursor of this iterator. 
     * Consider the case first 
     * that the underlying list {@link #getCyclicList} is not empty. 
     * Then <code>it.setIndex(index); return it.getIndex();</code> 
     * returns <code>index</code> again 
     * up to <code>it.getCyclicList().size()</code>. 
     * For <code>it.getCyclicList().isEmpty()</code>, 
     * this method does not modify this iterator. 
     *
     * @param index 
     *    an arbitrary <code>int</code> value, 
     *    which may also be negative. 
     */
    void setIndex(int index);

    /**
     * Inserts the specified element into the underlying cyclic list 
     * (optional operation). 
     * The element is inserted immediately before the next element 
     * that would be returned by <code>next</code>, if any, 
     * and after the next element 
     * that would be returned by <code>previous</code>, if any. 
     * (If the cyclic list is empty, 
     * the new element becomes the sole element on the cyclic list.) 
     * <p>
     * The new element is inserted before the implicit cursor: 
     * a subsequent call to <code>next</code> would be unaffected, 
     * and a subsequent call to <code>previous</code> 
     * would return the new element. 
     * (This call increases by one the value that would be returned by a call 
     * to <code>nextIndex</code> or <code>previousIndex</code>.) 
     *
     * @param obj 
     *    the element to be inserted.
     * @exception UnsupportedOperationException 
     *    if the <code>add</code> method is not supported by this iterator. 
     * @exception ClassCastException 
     *    if the class of the specified element 
     *    prevents it from being added to the underlying cyclic list. 
     * @exception IllegalArgumentException 
     *    if some aspect of this element 
     *    prevents it from being added to the underlying cyclic list. 
     * @see #addAll
     */
   void add(E obj);

    /**
     * Inserts the specified list into the underlying cyclic list 
     * (optional operation). 
     * The list is inserted immediately before the next element 
     * that would be returned by <code>next</code>, if any, 
     * and after the next element 
     * that would be returned by <code>previous</code>, if any. 
     * (If the cyclic list is empty, 
     * the new cyclic list comprises the given list.) 
     * <p>
     * The given list is inserted before the implicit cursor: 
     * a subsequent call to <code>next</code> would be unaffected, 
     * and a subsequent call to <code>previous</code> 
     * would return the given list in reversed order. 
     * (This call increases by <code>list.size()</code> 
     * the value that would be returned by a call 
     * to <code>nextIndex</code> or <code>previousIndex</code>.) 
     * <p>
     * If <code>list.size()</code> contains a single element <code>e</code>, 
     * <code>addAll(list)</code> is equivalent with <code>add(e)</code>.
     *
     * @param list 
     *    the list to be inserted.
     * @throws UnsupportedOperationException 
     *    if the <code>add</code> method is not supported by this iterator. 
     * @throws ClassCastException 
     *    if the class of the an element in the specified list 
     *    prevents it from being added to the underlying list. 
     * @throws IllegalArgumentException 
     *    if some aspect of the an element in the specified list 
     *    prevents it from being added to the underlying list. 
     * @see #add
     */
     void addAll(List<? extends E> list);

    /**
     * Replaces the last element 
     * returned by <code>next</code> or <code>previous</code> 
     * with the specified element (optional operation).
     * This call can be made only 
     * if neither <code>ListIterator.remove</code> nor <code>add</code> 
     * have been called after the last call to 
     * <code>next</code> or <code>previous</code>. 
     *
     * @param obj
     *    the element with which to replace the last element 
     *    returned by next or previous. 
     * @exception UnsupportedOperationException 
     *    if the <code>set</code> operation 
     *    is not supported by this iterator. 
     * @exception ClassCastException 
     *    if the class of the specified element 
     *    prevents it from being added to this cyclic list. 
     * @exception IllegalArgumentException 
     *    if some aspect of the specified element 
     *    prevents it from being added to this list.
     * @exception IllegalStateException 
     *    if neither <code>next</code> nor <code>previous</code> 
     *    have been called, 
     *    or <code>remove</code> or <code>add</code> have been called 
     *    after the last call to <code>next</code> or <code>previous</code>. 
     */
    void set(E obj);

    /**
     * Removes from the underlying <code>CyclicList</code> 
     * the last element returned by <code>next</code> or <code>previous</code> 
     * (optional operation). 
     * This method can be called only once 
     * per call to <code>next</code> or <code>previous</code>. 
     * It can be made only if <code>add</code> has not been called after 
     * the last call to <code>next</code> or <code>previous</code>.          
     *
     * @exception UnsupportedOperationException 
     *    if the <code>remove</code> operation 
     *    is not supported by this CyclicIterator. 
     * @exception IllegalStateException 
     *    if neither <code>next</code> nor <code>previous</code> 
     *    have been called, 
     *    or <code>remove</code> or <code>add</code> have been called 
     *    after the last call to <code>next</code> or <code>previous</code>. 
     */
    void remove();

    /**
     * Reinitialize this iterator without changing the cursor 
     * but such that all elements of the corresponding cyclic list 
     * may be accessed successively through {@link #next}. 
     * On the other hand, {@link #previous} throws an exception. 
     */
    void refresh();

    //boolean equals(Object other);
    boolean retEquals(CyclicIterator<?> other);

    /**
     * Returns <code>false</code> if <code>other</code> is not an instance of 
     * <code>CyclicIterator</code>. 
     * The implementation of this interface should not play a role. 
     *
     * @param other 
     *    another <code>Object</code>. 
     * @return 
     *    <code>false</code> if <code>other</code> is not an instance of 
     *    <code>CyclicIterator</code>. 
     *    The implementation of this interface should not play a role. 
     */
    boolean equals(Object other);

    double dist(CyclicIterator<E> other);
}
