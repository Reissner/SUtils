
package eu.simuline.util;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.Iterator; // for docs only 

/**
 * Represents a set with multiplicities. 
 * Mathematically this is something between a set and a family. 
 * Note that implementations of this kind of set 
 * need not support <code>null</code> elements. 
 * <p>
 * Allows also to create an immutable <code>MultiSet</code> 
 * either from a set or as a copy of another <code>MultiSet</code>. 
 * <p>
 * Note that this should extend Collection, but still does not *****. 
 * maybe it should even extend Set. 
 *
 * @param <T>
 *    the class of the elements of this multi-set. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public interface MultiSet<T> extends Iterable<T> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Represents the multiplicity of an entry of the enclosing multi-set. 
     */
    interface Multiplicity extends Comparable<Multiplicity> {

	/**
	 * Sets the multiplicity wrapped by this object 
	 * to the specified value. 
	 *
	 * @param mult 
	 *    a strictly positive <code>int</code> value 
	 *    representing the old multiplicity. 
	 * @throws IllegalArgumentException 
	 *    if <code>mult</code> is not strictly positive. 
	 */
	int set(int mult);

	/**
	 * Adds the specified multiplicity (which may well be negative) 
	 * to the wrapped multiplicity which is thus modified. 
	 *
	 * @param mult 
	 *    an <code>int</code> such that <code>this.mult + mult > 0</code> 
	 *    holds. 
	 * @return
	 *    the new multiplicity <code>this.mult + mult</code>. 
	 * @throws IllegalArgumentException 
	 *    if <code>this.mult + mult < 0</code> holds. 
	 * @throws IllegalStateException 
	 *    if <code>this.mult + mult == 0</code> holds. 
	 *    This cannot occur: if it does this is a bug within this class. 
	 */
	int add(int mult);

	/**
	 * Returns the wrapped multiplicity. 
	 */
	int get();

	/**
	 * Defines the natural ordering on natural numbers. 
	 *
	 * @param mult 
	 *    a <code>Multiplicity</code> which should in fact 
	 *    be another {@link MultiSet.Multiplicity}. 
	 * @return 
	 *    the difference of the wrapped multiplicities. 
	 * @throws NullPointerException 
	 *    for <code>mult == null</code>. 
	 * @throws ClassCastException 
	 *    if <code>mult</code> is neither <code>null</code> 
	 *    nor an instance of {@link MultiSet.Multiplicity}. 
	 */
	int compareTo(Multiplicity mult);
    } // class Multiplicity 

    /* -------------------------------------------------------------------- *
     * class constants.                                                     *
     * -------------------------------------------------------------------- */


    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */


    /* -------------------------------------------------------------------- *
     * constructors and creator methods.                                    *
     * -------------------------------------------------------------------- */


    /**
     * Returns an immutable copy of this <code>MultiSet</code>. 
     */
    MultiSet<T> immutable();

    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    // Query Operations

    /**
     * Returns the number of pairwise different elements 
     * in this <code>MultiSet</code>. 
     * If this <code>MultiSet</code> 
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, 
     * returns <tt>Integer.MAX_VALUE</tt>. 
     * 
     * @return 
     *    the number of elements in this <code>MultiSet</code> 
     *    each multiple element counted as a single one. 
     * @see #sizeWithMult()
     */
    int size();

    /**
     * Returns the number of elements 
     * in this <code>MultiSet</code> counted with multiplicities. 
     * If this <code>MultiSet</code> 
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, 
     * returns <tt>Integer.MAX_VALUE</tt>. 
     * 
     * @return 
     *    the number of elements in this <code>MultiSet</code> 
     *    counted with multiplicities, 
     *    provided this does not exceed {@link Integer#MAX_VALUE}; 
     *    otherwise just {@link Integer#MAX_VALUE}. 
     * @see #size()
     */
    int sizeWithMult();

    /**
     * Returns whether this multiple set contains no element. 
     *
     * @return 
     *    whether this multiple set contains no element. 
     */
    boolean isEmpty();


    /**
     * Returns one of the elements in this multiple set 
     * with maximal multiplicity. 
     * The return value is <code>null</code> 
     * if and only if this set is empty. 
     *
     * @return 
     *    a <code>Object o != null</code> with maximal multiplicity 
     *    or <code>null</code> if this multiple set is empty. 
     * @see #isEmpty
     */
    Object getObjWithMaxMult();

    /**
     * Returns the maximal multiplicity of an element in this set. 
     * In particular for empty sets returns <code>0</code>. 
     *
     * @return 
     *    a non-negative <code>int</code> value 
     *    which is the maximal mutliplicity of an element in this set. 
     *    In particular this is <code>0</code> 
     *    if and only if this set is empty. 
     */
    int getMaxMult();

    /**
     * Returns the multiplicity 
     * with which the given object occurs within this set. 
     *
     * @param obj 
     *    an <code>Object</code> and not null. 
     * @return 
     *    a non-negative <code>int</code> value 
     *    which is the mutliplicity of the given element in this set. 
     *    In particular this is <code>0</code> if and only if 
     *    <code>obj</code> is an instance which is not in this set. 
     * @throws NullPointerException
     *    for <code>obj==null</code>. 
     * @see #setMultiplicity(Object, int)
     * @see #getMultiplicityObj(Object)
     */
    int getMultiplicity(Object obj);

    /**
     * Returns the multiplicity object of the given object in this set 
     * or <code>null</code>. 
     *
     * @param obj 
     *    an <code>Object</code> and not null. 
     * @return 
     *    If <code>obj</code> is an instance which is in this set, 
     *    a multiplicity object wrapping the multiplicity is returned. 
     *    If <code>obj</code> is an instance which is not in this set, 
     *    <code>null</code> is returned. 
     * @throws NullPointerException
     *    for <code>obj==null</code>. 
     * @see #getMultiplicity(Object)
     */
    Multiplicity getMultiplicityObj(Object obj);

    /**
     * Returns <tt>true</tt> if this <code>MultiSet</code> 
     * contains the specified element. 
     * More formally, returns <tt>true</tt> if and only if this
     * <code>MultiSet</code> contains at least one element <tt>e</tt> 
     * such that <tt>(o==null ? e==null : o.equals(e))</tt>. 
     *
     * @param obj 
     *    element (not <code>null</code>) 
     *    whose presence in this <code>MultiSet</code> is to be tested.
     * @return 
     *    <tt>true</tt> if this <code>MultiSet</code> 
     *    contains the specified element. 
     * @throws NullPointerException
     *    for <code>obj==null</code>. 
     */
    boolean contains(Object obj);

    /**
     * Returns an iterator over the elements in this collection 
     * which emits each element exactly once, 
     * without regarding its multiplicity. 
     * <!-- There are no guarantees concerning the order 
     * in which the elements are returned
     * (unless this collection is an instance of some class 
     * that provides a guarantee). -->
     * For certain implementations, the iterator returned 
     * does not allow modifications of the underlying (multi-)set. 
     * 
     * @return 
     *    an <tt>Iterator</tt> over the elements in this collection 
     *    considering each element exactly once ignoring its multiplicity. 
     */
    MultiSetIterator<T> iterator();

    /**
     * Returns an array containing all of the elements 
     * in this <code>MultiSet</code> exactly once, ignoring its multiplicity. 
     * <!--
     * If the <code>MultiSet</code> makes any guarantees 
     * as to what order its elements are returned by its iterator, 
     * this method must return the elements in the same order. 
     * -->
     * <p>
     * The returned array will be "safe" in that no references to it 
     * are maintained by this <code>MultiSet</code>. 
     * (In other words, this method must allocate a new array 
     * even if this <code>MultiSet</code> is backed by an array). 
     * The caller is thus free to modify the returned array. 
     * <!-- <p>
     * This method acts as bridge 
     * between array-based and collection-based APIs. -->
     *
     * @return 
     *    an array containing all of the elements in this collection 
     * @see #iterator
     */
    Object[] toArray();

    /**
     * Returns an array containing all of the elements 
     * in this <code>MultiSet</code>; 
     * the runtime type of the returned array is that of the specified array. 
     * If the <code>MultiSet</code> fits in the specified array, 
     * it is returned therein. 
     * Otherwise, a new array is allocated with the runtime type 
     * of the specified array and the size of this <code>MultiSet</code>. 
     * <p>
     * If this <code>MultiSet</code> fits in the specified array 
     * with room to spare
     * (i.e., the array has more elements than this <code>MultiSet</code>), 
     * the elementin the array 
     * immediately following the end of the <code>MultiSet</code> 
     * is set to <tt>null</tt>. 
     * This is useful in determining the length of this
     * <code>MultiSet</code> because this <code>MultiSet</code> does
     * not contain any <tt>null</tt> elements. 
     * <p>
     * <!--
     * If this <code>MultiSet</code> makes any guarantees 
     * as to what order its elements are returned by its iterator, 
     * this method must return the elements in
     * the same order. 
     * -->
     * <!--
     * <p>
     * Like the <tt>toArray</tt> method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs. 
     * <p>
     * -->
     * Suppose <tt>l</tt> is a <tt>List</tt> known to contain only strings.
     * The following code can be used to dump the list into a newly allocated
     * array of <tt>String</tt>:
     *
     * <pre>
     *     String[] x = (String[]) v.toArray(new String[0]);
     * </pre><p>
     *
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     *
     * @param arr 
     *    the array into which the elements of this <code>MultiSet</code> 
     *    are to be stored, if it is big enough; 
     *    otherwise, a new array of the same runtime type 
     *    is allocated for this purpose. 
     * @return 
     *    an array containing each elements of this <code>MultiSet</code> 
     *    exactly once. 
     * @throws ArrayStoreException 
     *    the runtime type of the specified array is not a supertype 
     *    of the runtime type of every element in this <code>MultiSet</code>. 
     * @throws NullPointerException 
     *    if the specified array is <tt>null</tt>. 
     */
    T[] toArray(T[] arr);
    // Modification Operations

    /**
     * Adds <code>obj</code> to this <code>MultiSet</code> 
     * and returns the new multiplicity of this object. 
     * In other words, increments the multiplicity of <code>obj</code> by one. 
     *
     * @param obj 
     *    a <code>Object</code>. 
     *    Note that this object may not be <code>null</code>. 
     * @return 
     *    a strictly positive <code>int</code> value: 
     *    the new multiplicity of <code>obj</code>. 
     * @throws NullPointerException 
     *    if the specified element is null. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    int addWithMult(T obj);

    /**
     * Increases the multiplicity of <code>obj</code> 
     * in this <code>MultiSet</code> 
     * by the specified value <code>addMult</code> 
     * and returns the new multiplicity of this object. 
     *
     * @param obj 
     *    an <code>Object</code> instance. 
     * @param addMult 
     *    a non-negative integer specifying the multiplicity 
     *    with which <code>obj</code> is to be added. 
     * @return 
     *    a non-negative <code>int</code> value: 
     *    the new multiplicity of <code>obj</code>. 
     * @throws IllegalArgumentException 
     *    for <code>addMult &lt; 0</code>. 
     * @throws NullPointerException 
     *    for <code>obj==null</code> provided <code>addMult &ge; 0</code>. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    int addWithMult(T obj, int addMult);

    /**
     * Adds <code>obj</code> to this <code>MultiSet</code>. 
     * In other words, increments the multiplicity of <code>obj</code> by one. 
     * Returns <tt>true</tt> if this <code>MultiSet</code> 
     * interpreted as a set changed as a result of the call. 
     * (Returns <tt>false</tt> if this <code>MultiSet</code> 
     * already contains the specified element (with nontrivial multiplicity). 
     * <!--
     * <p>
     * <Code>MultiSet</Code>s that support this operation 
     * may place limitations on what elements may be added 
     * to this <code>MultiSet</code>. 
     * In particular, some<code>MultiSet</code>s will refuse 
     * to add <tt>null</tt> elements, and others will impose restrictions 
     * on the type of elements that may be added. 
     * <Code>MultiSet</Code> classes should clearly specify 
     * in their documentation any 
     * restrictions on what elements may be added. 
     * <p>
     * If a <code>MultiSet</code> refuses to add a particular element 
     * for any reason other than that it already contains the element, 
     * it <i>must</i> throw an exception 
     * (rather than returning <tt>false</tt>).  
     * This preservesthe invariant 
     * that a <code>MultiSet</code> always contains the specified element 
     * after this call returns. 
     * -->
     *
     * @param obj 
     *    element the multiplicity of which in this <code>MultiSet</code> 
     *    is to be increased by one. 
     *    Note that this may not be <code>null</code>. 
     * @return 
     *    <tt>true</tt> if and only if 
     *    the multiplicity of the specified element 
     *    was <code>0</code> before the call of this method. 
     * @throws NullPointerException 
     *    if the specified element is <code>null</code>. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    boolean add(T obj);

    /**
     * Decrements the multiplicity of <code>obj</code> 
     * in this <code>MultiSet</code> if it is present and 
     * returns the <em>old</em> multiplicity of <code>obj</code>; 
     * If this is <code>0</code> returns 
     * without altering this <code>MultiSet</code>. 
     *
     * @param obj 
     *    a <code>Object</code>. 
     *    Note that this object may not be <code>null</code>. 
     * @return 
     *    a non-negative <code>int</code> value: 
     *    the old multiplicity of <code>obj</code> 
     *    before a potential modification of this <code>MultiSet</code>. 
     * @throws NullPointerException 
     *    if the specified element is null. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    int removeWithMult(Object obj);

    /**
     * Decreases the multiplicity of <code>obj</code> 
     * in this <code>MultiSet</code> 
     * by the specified value <code>removeMult</code> if possible 
     * and returns the <em>old</em> multiplicity of <code>obj</code>. 
     *
     * @param obj 
     *    an <code>Object</code> instance. 
     * @param removeMult 
     *    a non-negative integer specifying the multiplicity 
     *    with which <code>obj</code> is to be removed. 
     * @return 
     *    a non-negative <code>int</code> value: 
     *    the old multiplicity of <code>obj</code> 
     *    before a potential modification of this <code>MultiSet</code>. 
     * @throws NullPointerException 
     *    for <code>obj == null</code>. 
     * @throws IllegalArgumentException 
     *    for <code>removeMult &lt; 0</code> and also if 
     *    <code>removeMult - obj.getMultiplicity() &lt; 0</code>. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    int removeWithMult(Object obj, int removeMult);

    /**
     * Removes <em>all</em> instances of the specified element from this 
     * <code>MultiSet</code>, if it is present with nontrivial multiplicity. 
     * More formally, immediately after having (successively) invoked 
     * <code>s.remove(o)</code>, 
     * the condition <code>s.contains(o) == false</code> is satisfied. 
     * Returns true if this <code>MultiSet</code> contained the specified 
     * element (or equivalently, if (the underlying set of) 
     * this <code>MultiSet</code> changed as a result of the call). 
     *
     * @param obj 
     *    element the multiplicity of which in this <code>MultiSet</code> 
     *    is to be increased by one. 
     * @return 
     *    <tt>true</tt> if and only if this <code>MultiSet</code> changed 
     *    as a result of the call. 
     * @throws NullPointerException 
     *    if the specified element is <code>null</code>. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    boolean remove(Object obj);

    /**
     * Sets the multiplicity of <code>obj</code> to the value 
     * specified by <code>mult</code>. 
     *
     * @param obj 
     *    an <code>Object</code> instance. 
     * @param newMult 
     *    a non-negative <code>int</code> value. 
     * @return 
     *    the old multiplicity of <code>obj</code> 
     *    as a non-negative <code>int</code> value. 
     * @throws IllegalArgumentException 
     *   if either <code>obj == null</code> or <code>mult &le; 0</code>. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     * @see #getMultiplicity(Object)
     */
    int setMultiplicity(T obj, int newMult);

    // Bulk Operations

    /**
     * Returns <tt>true</tt> if this <code>MultiSet</code> 
     * contains all of the elements in the specified collection 
     * with strictly positive multiplicity. 
     *
     * @param  coll 
     *    collection to be checked for containment 
     *    in this <code>MultiSet</code>.
     * @return 
     *    <tt>true</tt> if this <code>MultiSet</code> 
     *    contains all of the elements in the specified collection. 
     * @throws NullPointerException 
     *    if the specified collection contains one or more null elements.
     * @throws NullPointerException 
     *    if the specified collection is <tt>null</tt>.
     * @see #contains(Object)
     */
    boolean containsAll(Collection<?> coll);

    /**
     * Adds <code>mvs</code> elementwise to this multi set 
     * taking multiplicities into account 
     * and returns whether this caused a change 
     * of the underlying set. 
     * **** strange implementation; also: change 
     *
     * @param mvs 
     *    a <code>MultiSet</code> object. 
     * @return 
     *    returns whether adding changed this <code>MultiSet</code> 
     *    interpreted as a set. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    boolean addAll(MultiSet<? extends T> mvs);

    /**
     * Adds <code>set</code> elementwise to this multi set 
     * increasing multiplicities 
     * and returns whether this caused a change 
     * of the underlying set. 
     * **** strange implementation; also: change 
     *
     * @param set 
     *    a <code>Set</code> object. 
     * @return 
     *    returns whether adding changed this <code>MultiSet</code> 
     *    interpreted as a set. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    boolean addAll(Set<? extends T> set);

    /**
     * Removes all this <code>MultiSet</code>'s elements 
     * that are also contained in the specified collection. 
     * After this call returns, this <code>MultiSet</code> 
     * will contain no elements in common with the specified collection. 
     *
     * @param coll 
     *    elements to be removed from this <code>MultiSet</code>. 
     * @return 
     *    <tt>true</tt> if this <code>MultiSet</code> 
     *    changed as a result of the call. 
     * @throws NullPointerException 
     *    if the specified collection is <code>null</code>. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean removeAll(Collection<?> coll);

    /**
     * Retains only the elements in this <code>MultiSet</code> 
     * that are contained in the specified collection. 
     * In other words, removes from this <code>MultiSet</code> 
     * all of its elements that are not contained 
     * in the specified collection. 
     *
     * @param coll 
     *    elements to be retained in this <code>MultiSet</code>. 
     * @return 
     *    <tt>true</tt> if this <code>MultiSet</code> changed 
     *    as a result of the call. 
     * @throws NullPointerException 
     *    if the specified collection is <tt>null</tt>.
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean retainAll(Collection<?> coll);

    /**
     * Removes all of the elements from this <code>MultiSet</code>. 
     * This <code>MultiSet</code> will be empty after this method returns. 
     *
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    void clear();

    /**
     * Returns a view of the underlying set of this <code>MultiSet</code>. 
     * For certain implementations, this set is immutable 
     * to prevent implicit modification of this <code>MultiSet</code>. 
     *
     * @return 
     *    the <code>Set</code> containing exactly the objects 
     *    with strictly positive multiplicity in this <code>MultiSet</code>. 
     * @see HashMultiSet.Immutable#getSet()
     */
    Set<T> getSet();

    /**
     * Returns a view of the underlying map of this <code>MultiSet</code> 
     * as a map mapping each entry to its multiplicity. 
     */
    Map<T, Multiplicity> getMap();

    /**
     * Returns a Set view of the mapping 
     * from the element of this <code>MultiSet</code> 
     * to the according multiplicities. 
     * The set is backed by the <code>MultiSet</code>, 
     * so changes to the map are reflected in the set, and vice-versa. 
     * If the <code>MultiSet</code> is modified 
     * while an iteration over the set is in progress 
     * (except through the iterator's own remove operation, 
     * or through the setValue operation on a map entry 
     * returned by the iterator) the results of the iteration are undefined. 
     * The set may support element removal, 
     * which removes the corresponding element from the <code>MultiSet</code>, 
     * via the {@link Iterator#remove()}, {@link Set#remove(Object)}, 
     * {@link Set#removeAll(Collection)}, {@link Set#retainAll(Collection)} 
     * and {@link #clear()} operations. 
     * It does not support the methods 
     * {@link #add(Object)} or {@link Set#addAll(Collection)}. 
     */
    Set<Map.Entry<T, Multiplicity>> getSetWithMults();

    String toString();

    /**
     * Returns <code>true</code> if and only if <code>obj</code> 
     * is also a <code>MultiSet</code> 
     * and contains the same elements with the same multiplicities 
     * as this one. 
     *
     * @param obj 
     *    an <code>Object</code>, possibly <code>null</code>. 
     * @return 
     *    a <code>true</code> if and only if <code>obj</code> 
     *    is also a <code>MultiSet</code> 
     *    and contains the same elements with the same multiplicities 
     *    as this one. 
     */
    boolean equals(Object obj);

    int hashCode();
}
