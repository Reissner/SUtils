package eu.simuline.util;

import java.util.List;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Describe class TwoSidedList here.
 *
 *
 * Created: Sun Aug 26 23:25:26 2007
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
public class TwoSidedList<E> implements List<E> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Used as an argument for methods adding or removing elements 
     * from this list 
     * to determine in which direction this list has to shrink or grow. 
     */
    static enum Direction {
	Left2Right, Right2Left;
    } // enum Direction 

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * The list wrapped by this two-sided list. 
     */
    private List<E> list;

    /**
     * the first index in this <code>TwoSidedList</code>. 
     * Note that this integer may well be negative. 
     */
    private int firstIndex;

    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Creates a new <code>TwoSidedList</code> 
     * containing the elements of <code>list</code> in proper sequence 
     * with first index given by <code>firstIndex</code>. 
     *
     * @param firstIndex
     *    the index where this list starts growing. 
     * @param list 
     *    the list wrapped by this twosided list. 
     */
    public TwoSidedList(List<E> list, int firstIndex) {
	this.list = list;
	this.firstIndex = firstIndex;
    }

    /**
     * Creates a new <code>TwoSidedList</code> 
     * from a <code>List</code> with  <code>firstIndex == 0</code>. 
     * This is the canonical generalization of lists 
     * as mentioned in the documentation 
     * of {@link #indexOf(Object)} and of {@link #lastIndexOf(Object)}. 
     *
     * @param list 
     *    the list wrapped by this twosided list. 
     */
    public TwoSidedList(List<E> list) {
	this(list,0);
    }

    /**
     * Creates a new empty <code>TwoSidedList</code> which starts growing 
     * with index <code>firstIndex</code>. 
     *
     * @param firstIndex 
     *    the index where this list starts growing. 
     */
    public TwoSidedList(int firstIndex) {
	this(new ArrayList<E>(),firstIndex);
    }

    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */


    public int firstIndex() {
	return this.firstIndex;
    }

    public void firstIndex(int firstIndex) {
	this.firstIndex = firstIndex;
    }

    // caution for empty list. 
    public int minFreeIndex() {
	return this.list.size() + this.firstIndex;
    }

    // num may also be a negativeNumber
    public int shiftRight(int num) {
	return this.firstIndex += num;
    }



// Implementation of java.util.List


    /**
     * Returns the number of elements in this list. 
     * If this list 
     * contains more than <code>Integer.MAX_VALUE</code> elements, 
     * returns <code>Integer.MAX_VALUE</code>. 
     * **** gives rise to malfunction. **** 
     *
     * @return 
     *    an <code>int</code> value
     */
    public final int size() {
	return this.list.size();
    }

    /**
     * Returns true if this list contains no elements. 
     *
     * @return 
     *    whether this list contains no elements 
     *    as a <code>boolean</code> value. 
     */
    public final boolean isEmpty() {
	return this.list.isEmpty();
    }

    /**
     * Returns whether this list contains the specified element. 
     * More formally, returns <code>true</code> 
     * if and only if this list contains at least one element <code>obj</code> 
     * such that <code>(o==null ? e==null : o.equals(e))</code>.     
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    a <code>boolean</code> value
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    is incompatible with {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     */
    public final boolean contains(final Object obj) {
	return this.list.contains(obj);
    }

    /**
     * Returns the index of the first occurrence 
     * of the specified element <code>obj</code> in this list, 
     * or {@link #firstIndex()-1} if this list does not contain the element. 
     * More formally, returns the lowest index <code>i</code> 
     * such that <code>(obj==null ? get(i)==null : obj.equals(get(i)))</code>, 
     * or {@link #firstIndex()-1} if there is no such index. 
     * <p>
     * CAUTION: 
     * <ul>
     * <li>
     * This breaks **** "extends" contract for the interface List: 
     * Test for "element <code>obj</code> not in list" 
     * is no longer <code>this.indexOf(obj) == -1</code> but 
     * <code>this.indexOf(obj) < this.firstIndex()-1</code>. 
     * This is an extension in that 
     * wrapping an ordinary list in a twosided list 
     * is by specifying <code>firstIndex() == 0</code> 
     * (see {@link #TwoSidedList(List<E> list)}). 
     * <li>
     * Collision occurs 
     * for <code>firstIndex() == {@link Integer#MIN_VALUE}</code>. 
     * </ul>
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    an <code>int</code> value
     */
    public final int indexOf(final Object obj) {
	return this.list.indexOf(obj)+this.firstIndex;
    }

    /**
     * Returns the index of the last occurrence 
     * of the specified element <code>obj</code> in this list, 
     * or {@link #firstIndex()-1} if this list does not contain the element. 
     * More formally, returns the highest index <code>i</code> 
     * such that <code>(obj==null ? get(i)==null : obj.equals(get(i)))</code>, 
     * or {@link #firstIndex()-1} if there is no such index. 
     * <p>
     * CAUTION: 
     * <ul>
     * <li>
     * This breaks **** "extends" contract for the interface List: 
     * Test for "element <code>obj</code> not in list" 
     * is no longer <code>this.indexOf(obj) == -1</code> but 
     * <code>this.indexOf(obj) < this.firstIndex()-1</code>. 
     * This is an extension in that 
     * wrapping an ordinary list in a twosided list 
     * is by specifying <code>firstIndex() == 0</code> 
     * (see {@link #TwoSidedList(List<E> list)}). 
     * <li>
     * Collision occurs 
     * for <code>firstIndex() == {@link Integer#MIN_VALUE}</code>. 
     * </ul>
     * **** one may expect 
     * that in case the specified object in not in the list 
     * 1+ the highest index is returned. 
     * this is in general not the case. 
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    an <code>int</code> value
     */
    public final int lastIndexOf(final Object obj) {
	return this.list.lastIndexOf(obj)+this.firstIndex;
    }

    /*
     * Describe <code>toArray</code> method here. 
     *
     * @return 
     *    an <code>Object[]</code> value
     */
    // api-docs provided by javadoc. 
    public final Object[] toArray() {
	return this.list.toArray();
    }

    /*
     * Describe <code>toArray</code> method here. 
     *
     * @param objArr 
     *    an <code>Object[]</code> value
     * @return 
     *    an <code>Object[]</code> value
     */
    // api-docs provided by javadoc. 
    public final <E> E[] toArray(final E[] objArr) {
	return this.list.toArray(objArr);
    }

    /**
     * Returns the element at the specified position in this list. 
     *
     * @param ind 
     *    the index of the element to return as an <code>int</code> value. 
     * @return 
     *    the element at the specified position in this list. 
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final E get(final int ind) {
	return this.list.get(ind-this.firstIndex);
    }

    /**
     * Replaces the element at the <code>ind</code>th position 
     * in this list with the specified element (optional operation).
     *
     * @param ind 
     *    the index of the element to replace as an <code>int</code> value. 
     * @param obj 
     *    the element to be stored at the specified position. 
     * @return 
     *    the element previously at the <code>ind</code>th position 
     * @throws UnsupportedOperationException 
     *    if the <code>set</code> operation is not supported 
     *    by {@link #list}. 
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException 
     *    if some property of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final E set(final int ind, final E obj) {
	return this.list.set(ind-this.firstIndex,obj);
    }


    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #addFirst} and {@link #addLast} instead. 
     */
    public final boolean add(final E obj) {
	throw new UnsupportedOperationException
	    ("Use addFirst(E) or addLast(E) instead. ");
    }

    /**
     * Adds <code>obj</code> at the beginning of this list. 
     * The first index returned by {@link #firstIndex()} 
     * is decremented. 
     *
     * @param obj 
     *    the <code>E</code> object to be added. 
     * @return 
     *    <code>true</code> by specification. 
     * @throws UnsupportedOperationException 
     *    if the <code>add(int,E)</code> operation is not supported 
     *    by {@link #list}. 
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException 
     *    if some property of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     */
    public final boolean addFirst(final E obj) {
	this.firstIndex--;
	this.list.add(0,obj);
	return true;
    }

    /**
     * Adds <code>obj</code> at the end of this list. 
     * The first index returned by {@link #firstIndex()} remains unchanged. 
     *
     * @param obj 
     *    the <code>E</code> object to be added. 
     * @return 
     *    <code>true</code> by specification. 
     * @throws UnsupportedOperationException 
     *    if the <code>add(E)</code> operation is not supported 
     *    by {@link #list}. 
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException 
     *    if some property of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     */
    public final boolean addLast(final E obj) {
	return this.list.add(obj);
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #add(int,E,Direction)} instead. 
     */
    public final void add(final int ind, final E obj) {
	throw new UnsupportedOperationException
	    ("Use add(int,E,Direction) instead. ");
    }

    /**
     * Inserts <code>obj</code> at the specified position <code>ind</code> 
     * in this list (optional operation). 
     * Shifts the element currently at that position (if any) 
     * and any following/preceeding elements to the direction <code>dir</code> 
     * (increments/decrements their indices).
     *
     * @param ind 
     *    the index where to insert <code>obj</code>. 
     *    After having performed this operation, 
     *    <code>ind</code> is the index of <code>obj</code>. 
     * @param obj 
     *    the <code>E</code> element to be inserted. 
     * @param dir 
     *    determines the direction to shift the list. 
     *    <ul>
     *    <li><code>Left2Right</code>: 
     *    shifts all subsequent objects in this list 
     *    starting with index <code>ind</code> to the right 
     *    adding one to their indices. 
     *    <li><code>Right2Left</code>: 
     *    shifts all objects in this list 
     *    to index <code>ind</code> to the left 
     *    subtracting one from their indices. 
     *    </ul>
     * @throws UnsupportedOperationException 
     *    if the <code>add</code> operation 
     *    is not supported by {@link #list}. 
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException 
     *    if some property of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final void add(final int ind, final E obj, Direction dir) {
	if (dir == Direction.Right2Left) {
	    this.firstIndex--;
	}
	this.list.add(ind-this.firstIndex, obj);
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #remove(int,Direction)} instead. 
     */
    public final E remove(final int ind) {
	throw new UnsupportedOperationException
	    ("Use remove(int,Direction) instead. ");
    }

    /**
     * Removes the element at the specified position in this list 
     * (optional operation). 
     * Shifts any following/preceeding elements 
     * to the direction <code>dir</code> 
     * (decrements/increments their indices). 
     * Returns the element that was removed from the list. 
     *
     * @param ind 
     *    the index of the element to be removed 
     *    as an <code>int</code> value. 
     * @return 
     *    the element previously at the specified position. 
     * @throws UnsupportedOperationException 
     *    if the <code>remove(int)</code> operation 
     *    is not supported by {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final E remove(final int ind, Direction dir) {
	E res = this.list.remove(ind-this.firstIndex);
	if (dir == Direction.Right2Left) {
	    this.firstIndex++;
	}
	return res;
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link removeFirst#(E)} and {@link removeLast#(E)} instead. 
     */
    public final boolean remove(final Object obj) {
	throw new UnsupportedOperationException
	    ("Use removeFirst(E) or removeLast(E) instead. ");
    }

    /**
     * Removes the first occurrence of <code>obj</code> from this list, 
     * if present (optional operation). 
     * If this list does not contain <code>obj</code>, it is unchanged. 
     * More formally, removes the element with the lowest index <code>i</code> 
     * such that <code>(obj==null ? get(i)==null : obj.equals(get(i)))</code> 
     * (if such an element exists). 
     * Returns <code>true</code> if this list contained the specified element 
     * (or equivalently, if this list changed as a result of the call). 
     * The first index returned by {@link #firstIndex()} remains unchanged. 
     *
     * @param obj
     *    the element to be removed from this list, if present. 
     * @return
     *    whether this list contained the specified element
     * @throws UnsupportedOperationException 
     *    if the <code>remove(Object)</code> operation is not supported 
     *    by {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final boolean removeFirst(final Object obj) {
	return this.list.remove(obj);
    }

    /**
     * Removes the last occurrence of <code>obj</code> from this list, 
     * if present (optional operation). 
     * If this list does not contain <code>obj</code>, it is unchanged. 
     * More formally, 
     * removes the element with the highest index <code>i</code> 
     * such that <code>(obj==null ? get(i)==null : obj.equals(get(i)))</code> 
     * (if such an element exists). 
     * Returns <code>true</code> if this list contained the specified element 
     * (or equivalently, if this list changed as a result of the call). 
     * The first index returned by {@link #firstIndex()} is decremented. 
     *
     * @param obj
     *    the element to be removed from this list, if present. 
     * @return
     *    whether this list contained the specified element
     * @throws UnsupportedOperationException 
     *    if the <code>remove(int)</code> **** not remove(Object) 
     *    operation is not supported 
     *    by {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final boolean removeLast(final Object obj) {
	int ind = this.list.lastIndexOf(obj);
	if (ind == -1) {
	    // obj \notin this 
	    return false;
	} else {
	    // obj \in this 
	    this.firstIndex++;
	    this.list.remove(ind);
	    return true;
	}
    }

    /**
     * Removes all of the elements from this list (optional operation). 
     * The list will be empty after this call returns 
     * and {@link firstIndex} is unmodified. 
     *
     * @throws UnsupportedOperationException 
     *    if the clear operation is not supported by {@link #list}. 
     */
    public final void clear() {
	this.list.clear();
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #addAllFirst} and {@link #addAllLast} instead. 
     */
    public final boolean addAll(final Collection<? extends E> coll) {
	throw new UnsupportedOperationException
	    ("Use addAllFirst or addAllLast instead. ");
    }

    /**
     * Adds <code>obj</code> at the beginning of this list. 
     * in the order that they are returned 
     * by <code>coll</code>'s iterator (optional operation). 
     * The behavior of this operation is undefined 
     * if the specified collection is modified 
     * while the operation is in progress. 
     * (Note that this will occur if <code>coll</code> is this list, 
     * and it's nonempty.)
     * The first index returned by {@link #firstIndex()} 
     * is reduced by <code>coll</code>'s size. 
     *
     * @param coll 
     *    a <code>Collection</code> value
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws     UnsupportedOperationException 
     *    if the addAll operation is not supported by this {@link #list}.  
     * @throws    ClassCastException 
     *    if the class of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     * @throws    NullPointerException 
     *    if the specified collection contains 
     *    one or more <code>null</code> elements 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if the specified collection is <code>null</code> 
     * @throws    IllegalArgumentException 
     *    if some property of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     */
    public final boolean addAllFirst(final Collection<? extends E> coll) {
	this.firstIndex -= coll.size();
	return this.list.addAll(0,coll);
    }

    /**
     * Appends all of the elements in <code>coll</code> 
     * to the end of this list, 
     * in the order that they are returned 
     * by <code>coll</code>'s iterator (optional operation). 
     * The behavior of this operation is undefined 
     * if the specified collection is modified 
     * while the operation is in progress. 
     * (Note that this will occur if <code>coll</code> is this list, 
     * and it's nonempty.)
     * The first index returned by {@link #firstIndex()} remains unchanged. 
     *
     * @param coll 
     *    another <code>Collection</code>. 
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws     UnsupportedOperationException 
     *    if the addAll operation is not supported by this {@link #list}.  
     * @throws    ClassCastException 
     *    if the class of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     * @throws    NullPointerException 
     *    if the specified collection contains 
     *    one or more <code>null</code> elements 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if the specified collection is <code>null</code> 
     * @throws    IllegalArgumentException 
     *    if some property of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     */
    public final boolean addAllLast(final Collection<? extends E> coll) {
	return this.list.addAll(coll);
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #addAll(int,Collection,Direction)} instead. 
     */
    public final boolean addAll(final int ind, 
				final Collection<? extends E> coll) {
	throw new UnsupportedOperationException
	    ("Use addAll(int,Collection,Direction) instead. ");
    }

    /**
     * Inserts all of the elements in <code>coll</code> into this list 
     * at the specified position (optional operation). 
     * Shifts the elements currently at that positions (if any) 
     * and any following/preceeding elements to the direction <code>dir</code> 
     * (increasing/decreasing their indices by <code>coll</code>'s size). 
     * The new elements will appear in this list in the order 
     * that they are returned by the specified collection's iterator. 
     * The behavior of this operation is undefined 
     * if the specified collection is modified 
     * while the operation is in progress. 
     * (Note that this will occur if the specified collection is this list, 
     * and it's nonempty.)
     *
     * @param ind 
     *    index at which to insert the first element from <code>coll</code> 
     *    as an <code>int</code> value. 
     *    CAUTION: Note that <code>ind</code> 
     *    always references the first element in <code>coll</code> 
     *    independent from <code>dir</code>. 
     * @param coll 
     *    a <code>Collection</code> 
     *    containing elements to be added to this list. 
     * @param dir 
     *    determines the direction to shift the list. 
     *    <ul>
     *    <li><code>Left2Right</code>: 
     *    shifts all subsequent objects in this list 
     *    starting with index <code>ind</code> to the right 
     *    adding one to their indices. 
     *    <li><code>Right2Left</code>: 
     *    shifts all objects in this list 
     *    to index <code>ind</code> to the left 
     *    subtracting one from their indices. 
     *    </ul>
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws UnsupportedOperationException 
     *    if the <code>addAll</code> operation 
     *    is not supported by this {@link #list}.  
     * @throws ClassCastException 
     *    if the class of an element of <code>coll</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if the <code>coll</code> contains 
     *    at least one <code>null</code> element 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if <code>coll == null</code>. 
     * @throws IllegalArgumentException 
     *    if some property of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final boolean addAll(final int ind, 
				final Collection<? extends E> coll, 
				final Direction dir) {
	if (dir == Direction.Right2Left) {
	    this.firstIndex -= coll.size();
	}
	return this.list.addAll(ind-this.firstIndex, coll);
    }

    /*
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return 
     *    an iterator over the elements in this list in proper sequence. 
     */
    // api-docs provided by javadoc. 
    public final Iterator<E> iterator() {
	return this.list.iterator();
    }

    /*
     * Returns a list iterator over the elements in this list 
     * (in proper sequence). 
     *
     * @return 
     *    a <code>ListIterator</code> 
     */
    // api-docs provided by javadoc 
    public final ListIterator<E> listIterator() {
	return this.list.listIterator();
    }

    /**
     * ****
     *
     * @param ind 
     *    an <code>int</code> value
     * @return 
     *    a <code>ListIterator</code> value
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final ListIterator<E> listIterator(final int ind) {
	return this.list.listIterator(ind-this.firstIndex);
    }

    /**
     * Replaces the element at the <code>ind</code>th position 
     * in this list with the specified element (optional operation).
     *
     * @param coll 
     *    a <code>Collection</code> value
     * @return 
     *    a <code>boolean</code> value
     * @throws ClassCastException 
     *    if the class of one or more elements of <code>coll</code> 
     *    is incompatible with {@link #list}. 
     * @throws NullPointerException 
     *    if <code>coll</code> contains at least one <code>null</code> element 
     *    and {@link #list} is incompatible with <code>null</code> elements 
     *    or if <code>coll == null</code>. 
     */
    public final boolean containsAll(final Collection coll) {
	return this.list.containsAll(coll);
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #removeAll(Collection,Direction)} instead. 
     */
    public final boolean removeAll(final Collection<?> coll) {
	throw new UnsupportedOperationException
	    ("Use removeAll(Collection,Direction) instead. ");
    }

    /**
     * Removes from this list all of its elements 
     * that are contained in <code>coll</code> (optional operation). 
     * Shifts the elements currently at that position (if any) 
     * and any following/preceeding elements to the direction <code>dir</code> 
     * (decreasing/increasing  their indices 
     * by the number of elements removed). 
     *
     * @param coll 
     *    a <code>Collection</code> 
     *    containing elements to be removed from this list. 
     * @param dir 
     *    determines the direction to shift the list. 
     *    <ul>
     *    <li><code>Left2Right</code>: 
     *    to close gaps by removing elements 
     *    shifts all objects preceeding gaps to the right 
     *    adding one to their indices. 
     *    <li><code>Right2Left</code>: 
     *    to close gaps by removing elements 
     *    shifts all objects following gaps to the left  
     *    subtracting one from their indices. 
     *    </ul>
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws UnsupportedOperationException 
     *    if the <code>removeAll</code> operation 
     *    is not supported by this {@link #list}.  
     * @throws ClassCastException 
     *    if the class of an element of <code>coll</code> 
     *    is incompatible with {@link #list}. 
     * @throws NullPointerException 
     *    if the <code>coll</code> contains 
     *    at least one <code>null</code> element 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if <code>coll == null</code>. 
     */
    public final boolean removeAll(final Collection<?> coll, Direction dir) {
	switch (dir) {
	    case Left2Right:
		return this.list.removeAll(coll);
	    case Right2Left:
		int oldSize = this.list.size();
		boolean ret = this.list.removeAll(coll);
		this.firstIndex += oldSize - this.list.size();
		return ret;
	    default:
		throw new IllegalStateException();
	}
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #retainAll(Collection,Direction)} instead. 
     */
    public final boolean retainAll(final Collection coll) {
	throw new UnsupportedOperationException
	    ("Use retainAll(Collection,Direction) instead. ");
    }

    /**
     * Retains only the elements in this list 
     * that are contained in <code>coll</code> (optional operation). 
     * In other words, removes from this list all the elements 
     * that are not contained in <code>coll</code>. 
     * Shifts the elements currently at that position (if any) 
     * and any following/preceeding elements to the direction <code>dir</code> 
     * (decreasing/increasing  their indices 
     * by the number of elements removed). 
     *
     * @param coll 
     *    a <code>Collection</code> 
     *    containing elements to be retained in this list. 
     * @param dir 
     *    determines the direction to shift the list. 
     *    <ul>
     *    <li><code>Left2Right</code>: 
     *    to close gaps by removing elements 
     *    shifts all objects preceeding gaps to the right 
     *    adding one to their indices. 
     *    <li><code>Right2Left</code>: 
     *    to close gaps by removing elements 
     *    shifts all objects following gaps to the left  
     *    subtracting one from their indices. 
     *    </ul>
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws UnsupportedOperationException 
     *    if the <code>retainAll</code> operation 
     *    is not supported by this {@link #list}.  
     * @throws ClassCastException 
     *    if the class of an element of <code>coll</code> 
     *    is incompatible with {@link #list}. 
     * @throws NullPointerException 
     *    if the <code>coll</code> contains 
     *    at least one <code>null</code> element 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if <code>coll == null</code>. 
     */
    public final boolean retainAll(final Collection coll, Direction dir) {
	switch (dir) {
	    case Left2Right:
		return this.list.retainAll(coll);
	    case Right2Left:
		int oldSize = this.list.size();
		boolean ret = this.list.retainAll(coll);
		this.firstIndex += oldSize - this.list.size();
		return ret;
	    default:
		throw new IllegalStateException();
	}
    }

    /**
     * Returns a view of the portion of this twosided list as a list 
     * between the specified <code>fromIndex</code>, inclusive, 
     * and <code>toIndex</code>, exclusive. 
     *
     * @param indStart 
     *    low endpoint (inclusive) of the subList. 
     * @param indEnd 
     *    high endpoint (exclusive) of the subList. 
     * @return 
     *    view of the specified range within this list 
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final List<E> subList(final int indStart, final int indEnd) {
	return this.list.subList(indStart-this.firstIndex, 
				 indEnd  -this.firstIndex);
    }
    /**
     * Returns a view of the portion of this twosided list 
     * between the specified <code>fromIndex</code>, inclusive, 
     * and <code>toIndex</code>, exclusive. 
     *
     * @param indStart 
     *    low endpoint (inclusive) of the subList. 
     * @param indEnd 
     *    high endpoint (exclusive) of the subList. 
     * @return 
     *    view of the specified range within this list 
     * @throws IndexOutOfBoundsException
     *    **** wrong indices. range check for all methods in next step 
     */
    public final TwoSidedList<E> subList2(final int indStart, 
					  final int indEnd) {
	return new TwoSidedList<E>(subList(indStart,indEnd),indStart);
    }

    /**
     * The given object equals this twosided list 
     * if and only if it is as well a <code>TwoSidedList</code>, 
     * the two lists wrapped {@link #list} coincide 
     * and either as well the first indices {@link #firstIndex} coincide. 
     * CAUTION: 
     * Note that two empty lists with different first index are not equal. 
     * This is justified by the fact, 
     * that these two become different when the firsts element is added. 
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean equals(final Object obj) {
	if (!(obj instanceof TwoSidedList)) {
	    return false;
	}
	TwoSidedList other = (TwoSidedList)obj;

	return this.list.equals(other.list) 
	    && (this.firstIndex == other.firstIndex);
    }

    /**
     * Returns a hash code which conforms with {@link #equals()}. 
     *
     * @return 
     *    the hash code as an <code>int</code> value. 
     */
    public final int hashCode() {
	return this.list.hashCode() + this.firstIndex;
    }

    // api-docs provided by javadoc 
    public String toString() {
	StringBuilder res = new StringBuilder();
	res.append("<TwoSidedList firstIndex=\"");
	res.append(this.firstIndex);
	res.append("\">");
	res.append(this.list);
	res.append("</TwoSidedList>");
	return res.toString();
    }
}
