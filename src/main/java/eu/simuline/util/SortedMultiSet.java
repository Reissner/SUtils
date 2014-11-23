package eu.simuline.util;

/**
 * Describe interface SortedMultiSet here.
 *
 *
 * Created: Sun Nov 23 22:36:30 2014
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
public interface SortedMultiSet<T> extends MultiSet<T> {

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return
     *    the first (lowest) element currently in this set
     * @throws NoSuchElementException
     *    if this set is empty. 
     */
    public T first();

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return
     *    the last (highest) element currently in this set. 
     * @throws NoSuchElementException
     *    if this set is empty. 
     */
    public T last();

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
    public MultiSet<T> headSet(T toElement);
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
    public MultiSet<T> tailSet(T fromElement);

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
    public MultiSet<T> subSet(T fromElement, T toElement);


}
