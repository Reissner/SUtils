
package eu.simuline.util;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;

/**
 * An ordered cyclic list. 
 * The user of this interface has precise control 
 * over where in the list each element is inserted. 
 * The user can access elements by their integer index 
 * (position in the list) which is modulo its length, 
 * and search for elements in the list. 
 * <p>
 * Unlike sets, lists typically allow duplicate elements. 
 * More formally, 
 * lists typically allow pairs of elements <tt>e1</tt> and <tt>e2</tt> 
 * such that <tt>e1.equals(e2)</tt>, 
 * and they typically allow multiple null elements 
 * if they allow null elements at all. 
 * It is not inconceivable 
 * that someone might wish to implement a list that prohibits duplicates, 
 * by throwing runtime exceptions when the user attempts to insert them, 
 * but we expect this usage to be rare. 
 * <p>
 * The <tt>CyclicList</tt> interface places additional stipulations, 
 * beyond those specified in the <tt>Collection</tt> interface, 
 * on the contracts of the 
 * <tt>iterator</tt>, <tt>add</tt>, <tt>remove</tt>, <tt>equals</tt>, 
 * and <tt>hashCode</tt> methods. 
 * On the other hand, some methods do not make sense (such as add) 
 * and are hence not supported. 
 * This is the reason why <code>CyclicList</code> 
 * does not extend <tt>Collection</tt>. 
 * <p>
 * The <tt>CyclicList</tt> interface 
 * provides four methods for positional (indexed) access to list elements. 
 * <tt>CyclicLists</tt> (like Java arrays) are zero based. 
 * Note that these operations may execute 
 * in time proportional to the index value 
 * for some implementations 
 * (the <tt>LinkedCyclicList</tt> class, for example). 
 * Thus, iterating over the elements in a list is typically 
 * preferable to indexing through it 
 * if the caller does not know the implementation. 
 * <p>
 * The <tt>CyclicList</tt> interface provides a special iterator, 
 * called a <tt>CyclicIterator</tt>, 
 * that allows element insertion and replacement, 
 * and bidirectional access similar to the normal operations that the
 * <tt>ListIterator</tt> interface provides. 
 * A method is provided to obtain a <tt>CyclicIterator</tt> 
 * that starts at a specified position in the list. 
 * <p>
 * The <tt>CyclicList</tt> interface 
 * provides two methods to search for a specified object. 
 * From a performance standpoint, 
 * these methods should be used with caution. 
 * In many implementations they will perform costly linear searches. 
 * <p>
 * The <tt>CyclicList</tt> interface 
 * provides two methods to efficiently insert and 
 * remove multiple elements at an arbitrary point in the list. 
 * <p>
 * Note: While it is permissible for lists to contain themselves as elements, 
 * extreme caution is advised: the <tt>equals</tt> and <tt>hashCode</tt> 
 * methods are no longer well defined on a such a list. 
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
public interface CyclicList<E> extends Collection<E> {

    // ***** to be removed later. 
    int shiftIndex(int index);

    /**
     * Returns the number of elements in this list. 
     * If this list contains more than <tt>Integer.MAX_VALUE</tt> elements, 
     * returns <tt>Integer.MAX_VALUE</tt>.
     *
     * @return 
     *    the number of elements in this list.
     */
    int size();

    /**
     * Returns <tt>true</tt> iff this list contains no elements.
     *
     * @return <tt>true</tt> iff this list contains no elements.
     */
    boolean isEmpty();

    /**
     * Returns the inverse of this cyclic list: 
     * the list with inverse order. 
     *
     * @return 
     *    The list with the same entries but inverse order. 
     */
    CyclicList<E> getInverse();

    /**
     *
     * Returns <tt>true</tt> if this list contains the specified element. 
     * More formally, returns <tt>true</tt> 
     * if and only if this list contains at least one element <tt>e</tt> 
     * such that 
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>. 
     *
     * @param obj element whose presence in this list is to be tested.
     * @return <tt>true</tt> if this list contains the specified element.
     */
    boolean contains(Object obj);

    //boolean containsAll(Collection<? extends E> coll);

    /**
     * Returns {@link #iterator(int) iterator(index)} 
     * for some unspecified <code>index</code>. 
     *
     * @return 
     *    {@link #iterator(int) iterator(index)} 
     *    for some unspecified <code>index</code>. 
     */
    Iterator<E> iterator();

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
     *    Any index (even negative ones) are valid. 
     * @return 
     *    a cyclic iterator of the elements in this list 
     *    (in proper sequence), 
     *    starting at the specified position in this list. 
     */
    Iterator<E> iterator(int index);

    /**
     * Returns {@link #iterator(int) iterator(index)} 
     * for some unspecified <code>index</code>. 
     *
     * @return 
     *    {@link #iterator(int) iterator(index)} 
     *    for some unspecified <code>index</code>. 
     */
    CyclicIterator<E> cyclicIterator();

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
     *    Any index (even negative ones) are valid. 
     * @return 
     *    a cyclic iterator of the elements in this list 
     *    (in proper sequence), 
     *    starting at the specified position in this list. 
     */
    CyclicIterator<E> cyclicIterator(int index);
 
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
    Object[] toArray(int index);

    /**
     * Returns an array containing all of the elements in this cyclic list 
     * in proper sequence; 
     * the runtime type of the returned array is that of the specified array. 
     * Modifying the return value does not modify this CyclicList. 
     *
     * @param index 
     *    index of the element in the cyclic list 
     *    which comes first in the array returned. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @param array
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
    <E> E[] toArray(int index,E[] array);

    /**
     * Returns a List containing all of the elements in this cyclic list 
     * in proper sequence. 
     * Modifying the return value does not modify this CyclicList. 
     *
     * @param index 
     *    index of the element in the cyclic list 
     *    which comes first in the List returned. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    a list containing all of the elements in this cyclic list 
     *    in proper sequence. 
     * @throws ArrayStoreException 
     *    if the runtime type of the specified array 
     *    is not a supertype of the runtime type 
     *    of every element in this list. 
     */
    List<E> asList(int index);
    List<E> asList();

    /**
     * Returns a cyclic permutation <tt>p</tt> of this cyclic list. 
     *
     * @param num 
     *    an <code>int</code> value. 
     * @return 
     *    a cyclic permutation <tt>p</tt> of this cyclic list. 
     *    It satisfies <code>p.size() == this.size()</code> and 
     *    <code>p.get(i) == this.get(i+num)</code>. 
     */
    CyclicList<E> cycle(int num);

    // Modification Operations

    /**
     * Removes all of the elements from this list (optional operation). 
     * This list will be empty after this call returns 
     * (unless it throws an exception).
     *
     * @throws UnsupportedOperationException 
     *    if the <tt>clear</tt> method is not supported by this cyclic list.
     */
    void clear();

    /**
     * Returns a clone of this <code>CyclicArrayList</code>. 
     * This includes copying <code>vertices</code>. 
     *
     * @return 
     *     a clone of this <code>CyclicList</code>. 
     *     This includes copying <code>vertices</code>. 
     */
    //public Object clone();

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
     * across different implementations of the <tt>CyclicList</tt> interface. 
     *
     * @param obj 
     *    the object to be compared for equality with this list. 
     * @return 
     *    <tt>true</tt> if the specified object is equal to this list. 
     */
    boolean equals(Object obj);

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
    int hashCode();

    // Positional Access Operations

    /**
     * Returns the element at the specified position in this list. 
     *
     * @param index 
     *    index of element to return. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    the element at the specified position in this list. 
     */
    E get(int index);

    /**
     * Replaces the element at the specified position in this list 
     * with the specified element (optional operation). 
     *
     * @param index 
     *    index of element to replace. 
     * @param element 
     *    element to be stored at the specified position.
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    the element previously at the specified position.
     *
     * @throws UnsupportedOperationException 
     *    if the <tt>set</tt> method is not supported by this list. 
     * @throws ClassCastException 
     *    if the class of the specified element 
     *    prevents it from being added to this list. 
     * @throws IllegalArgumentException 
     *    if some aspect of the specified element 
     *    prevents it from being added to this list. 
     */
    E set(int index, E element);

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
     *
     * @throws UnsupportedOperationException 
     *    if the <tt>replace</tt> method is not supported by this list. 
     * @throws ClassCastException 
     *    if the class of some element returned by <code>iter.next()</code> 
     *    prevents it from being added to this list. 
     * @throws IllegalArgumentException 
     *    if some aspect of some element returned by <code>iter.next()</code> 
     *    prevents it from being added to this list. 
     * @throws EmptyCyclicListException 
     *    if this list is empty. 
     */
    void replace(int index, CyclicIterator<E> iter);

    /**
     * Replaces the element at the specified position in this list 
     * with the specified list (optional operation). 
     * Places the elements of that list as returned by <code>iter.next</code> 
     * in this list. 
     *
     * @param index index 
     *    index of element to replace. 
     * @param list 
     *    a <code>CyclicIterator</code> which determines an index in a list 
     *    which replaces <code>this.get(i)</code>. 
     *
     * @throws UnsupportedOperationException 
     *    if the <tt>replace</tt> method is not supported by this list. 
     * @throws ClassCastException 
     *    if the class of some element returned by <code>iter.next()</code> 
     *    prevents it from being added to this list. 
     * @throws IllegalArgumentException 
     *    if some aspect of some element returned by <code>iter.next()</code> 
     *    prevents it from being added to this list. 
     * @throws EmptyCyclicListException 
     *    if this list is empty. 
     * @throws EmptyCyclicIteratorException 
     *    if the specified iterator is empty. 
     */
    void replace(int index, List<E> list);


    /**
     * Inserts the specified element at the specified position in this list 
     * (optional operation). 
     * Contract: 
     * <code>list.add(i,o);return list.get(i)</code> yields <code>o</code>. 
     * In contrast to {@link #set}, 
     * the element currently at the specified position is not lost. 
     *
     * @param index 
     *    index at which the specified element is to be inserted.
     *    This is interpreted modulo the length of this cyclic list plus one 
     *    (The list emerging after the insertion). 
     *    In contrast to {@link java.util.List#add(int,Object)} 
     *    any index (even a negative one) is valid. 
     * @param element 
     *    element to be inserted. 
     *
     * @throws UnsupportedOperationException 
     *    if the <tt>add</tt> method is not supported by this list. 
     * @throws ClassCastException 
     *    if the class of the specified element 
     *    prevents it from being added to this list. 
     * @throws IllegalArgumentException 
     *    if some aspect of the specified element 
     *    prevents it from being added to this list.
     */
    void add(int index, E element);

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
     *    element to be inserted. *****
     *
     * @throws UnsupportedOperationException 
     *    if the <tt>add</tt> method is not supported by this list. 
     * @throws ClassCastException 
     *    if the class of the specified element 
     *    prevents it from being added to this list. 
     * @throws IllegalArgumentException 
     *    if some aspect of the specified element 
     *    prevents it from being added to this list.
     */
    void addAll(int index, CyclicIterator<E> iter);

    /**
     * Inserts the specified list at the given position 
     * in this cyclic list (optional operation). 
     * In contrast to {@link #replace(int, CyclicIterator)}, 
     * the element currently at the specified position is not lost. 
     *
     * @param index 
     *    index at which the specified list is to be inserted.
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @param list 
     *    the list to be inserted.
     *
     * @throws UnsupportedOperationException 
     *    if the <tt>add</tt> method is not supported by this list. 
     * @throws ClassCastException 
     *    if the class of the specified element 
     *    prevents it from being added to this list. 
     * @throws IllegalArgumentException 
     *    if some aspect of the specified element 
     *    prevents it from being added to this list.
     */
    void addAll(int index, List<? extends E> list);

    /**
     * Removes the element at the specified position in this list 
     * (optional operation). 
     * Returns the element that was removed from the list.
     *
     * @param index 
     *    the index of the element to removed. 
     *    This is interpreted modulo the length of this cyclic list. 
     *    Any index (even negative ones) are valid. 
     * @return 
     *    the element previously at the specified position. 
     *
     * @throws UnsupportedOperationException 
     *    if the <tt>remove</tt> method is not supported by this list. 
     * @throws EmptyCyclicListException
     *   if this list is empty. 
     */
    E remove(int index) throws EmptyCyclicListException;


    // Search Operations

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
    int getIndexOf(E obj);

    /**
     * Returns a <code>CyclicList</code> 
     * which is by copying this list step by step 
     * such that the length of the result is as specified. 
     * For example <tt>len == size()*n</tt> 
     * yields an n-fold copy of this cyclic list. 
     *
     * @param len 
     *    a non-negative <code>int</code> value. 
     * @return 
     *    a <code>CyclicList</code> 
     *    which is by copying this list step by step 
     *    such that the length of the result is as specified. 
     * @throws IllegalArgumentException
     *    if n is negative. 
     * @throws EmptyCyclicListException
     *    if this list is empty and <code>len != 0</code>. 
     */
    CyclicList<E> getCopy(int len);
 }
 
