package eu.simuline.util;

import java.util.SortedSet;
import java.util.NavigableMap;
import java.util.Comparator;
import java.util.NoSuchElementException; // for javadoc only 

/**
 * Represents a sorted set with multiplicities. 
 *
 * If MultiSet extend Set, SortedMultiSet shall extend SortedSet
 *
 * @param <T>
 *    the class of the elements of this multi-set. 
 *
 * Created: Sun Nov 23 22:36:30 2014
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public interface SortedMultiSet<T> extends MultiSet<T> {

    /**
     * Returns a view of the underlying sorted set 
     * of this <code>SortedMultiSet</code>. 
     * For certain implementations, this set is immutable 
     * to prevent implicit modification of this <code>SortedMultiSet</code>. 
     *
     * @return 
     *    the <code>SortedSet</code> containing exactly the objects 
     *    with strictly positive multiplicity 
     *    in this <code>SortedMultiSet</code>. 
     */
    SortedSet<T> getSet();

    /**
     * Returns a view of the underlying map of this <code>MultiSet</code> 
     * as a map mapping each entry to its multiplicity. 
     */
    NavigableMap<T, Multiplicity> getMap();

    /**
     * Returns the comparator used to order the elements in this set, 
     * or <code>null</code> 
     * if this set uses the natural ordering of its elements. 
     *
     * @return
     *    the comparator used to order the elements in this set, 
     *    or <code>null</code> 
     *    if this set uses the natural ordering of its elements. 
     */
    Comparator<? super T> comparator();

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return
     *    the first (lowest) element currently in this set
     * @throws NoSuchElementException
     *    if this set is empty. 
     */
    T first();

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return
     *    the last (highest) element currently in this set. 
     * @throws NoSuchElementException
     *    if this set is empty. 
     */
    T last();

    /**
     * Returns a view of the portion of this multi-set 
     * whose elements are strictly less than <code>toElement</code>. 
     * The returned multi-set is backed by this multi-set, 
     * so changes in the returned set are reflected in this multi-set, 
     * and vice-versa. 
     * The returned multi-set supports all optional multi-set operations 
     * that this multi-set supports.
     * <p>
     * The returned multi-set 
     * will throw an <code>IllegalArgumentException</code> 
     * on an attempt to insert an element outside its range. 
     *
     * @param toElement
     *    high endpoint (exclusive) of the returned multi-set. 
     * @return
     *    a view of the portion of this multi-set 
     *    whose elements are strictly less than <code>toElement</code>. 
     * @throws ClassCastException
     *    if <code>toElement</code> is not compatible 
     *    with this multi-set's comparator 
     *    (or, if the set has no comparator, 
     *    if <code>toElement</code> does not implement {@link Comparable}). 
     *    Implementations may, but are not required to, 
     *    throw this exception if <code>toElement</code> cannot be compared 
     *    to elements currently in this multi-set. 
     * @throws NullPointerException
     *    if <code>toElement</code> is <code>null</code> 
     *    and this multi-set does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException
     *    if this multi-set itself has a restricted range, 
     *    and <code>toElement</code> lies outside the bounds of the range. 
     */
    SortedMultiSet<T> headSet(T toElement);

    /**
     * Returns a view of the portion of this multi-set 
     * whose elements are greater than or equal to <code>fromElement</code>. 
     * The returned multi-set is backed by this multi-set, 
     * so changes in the returned set are reflected in this multi-set, 
     * and vice-versa. 
     * The returned multi-set supports all optional multi-set operations 
     * that this multi-set supports.
     * <p>
     * The returned multi-set 
     * will throw an <code>IllegalArgumentException</code> 
     * on an attempt to insert an element outside its range. 
     *
     * @param fromElement
     *    low endpoint (inclusive) of the returned multi-set. 
     * @return
     *    a view of the portion of this multi-set 
     *    whose elements are greater than or equal to <code>fromElement</code>. 
     * @throws ClassCastException
     *    if <code>fromElement</code> is not compatible 
     *    with this multi-set's comparator 
     *    (or, if the set has no comparator, 
     *    if <code>fromElement</code> does not implement {@link Comparable}). 
     *    Implementations may, but are not required to, 
     *    throw this exception if <code>fromElement</code> cannot be compared 
     *    to elements currently in this multi-set. 
     * @throws NullPointerException
     *    if <code>fromElement</code> is <code>null</code> 
     *    and this multi-set does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException
     *    if this multi-set itself has a restricted range, 
     *    and <code>fromElement</code> lies outside the bounds of the range. 
     */
    SortedMultiSet<T> tailSet(T fromElement);

    /**
     * Returns a view of the portion of this multi-set 
     * whose elements range from <code>fromElement</code> inclusively 
     * to <code>toElement</code> exclusively. 
     * The returned multi-set is backed by this multi-set, 
     * so changes in the returned set are reflected in this multi-set, 
     * and vice-versa. 
     * The returned multi-set supports all optional multi-set operations 
     * that this multi-set supports.
     * <p>
     * The returned multi-set 
     * will throw an <code>IllegalArgumentException</code> 
     * on an attempt to insert an element outside its range. 
     *
     * @param fromElement
     *    low endpoint (inclusive) of the returned multi-set. 
     * @param toElement
     *    high endpoint (exclusive) of the returned multi-set. 
     * @return
     *    a view of the portion of this multi-set 
     *    from <code>fromElement</code> inclusively 
     *    to <code>toElement</code> exclusively. 
     * @throws ClassCastException **** maybe original documentation wrong. **** 
     *    if <code>fromElement</code> and <code>toElement</code> 
     *    cannot be compared to one another using this set's comparator 
     *    (or, if the set has no comparator, using natural ordering). 
     *    or if <code>fromElement</code> is not compatible 
     *    with this multi-set's comparator 
     *    (or, if the set has no comparator, 
     *    if <code>fromElement</code> does not implement {@link Comparable}). 
     *    Implementations may, but are not required to, 
     *    throw this exception 
     *    if <code>fromElement</code> or <code>toElement</code> 
     *    cannot be compared to elements currently in this multi-set. 
     * @throws NullPointerException
     *    if <code>fromElement</code> or <code>toElement</code> 
     *    is <code>null</code> 
     *    and this multi-set does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException
     *    if <code>fromElement</code> is greater than <code>toElement</code> 
     *    or if this multi-set itself has a restricted range, 
     *    and <code>fromElement</code> or <code>toElement</code> 
     *    lies outside the bounds of the range. 
     */
    SortedMultiSet<T> subSet(T fromElement, T toElement);
}
