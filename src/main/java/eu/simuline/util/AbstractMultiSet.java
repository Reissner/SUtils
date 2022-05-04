
package eu.simuline.util;

import eu.simuline.util.MultiSet.Multiplicity;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;

/**
 * Represents an abstract MultiSet based on a {@link Map}. 
 * 
 * <p>
 * addAll's implementation seems strange, 
 * add seems to be buggy, 
 * Problem with overflow of multiplicities. 
 *
 * @param <MAP>
 *    the map assigning to each element of this multi-set its multiplicity. 
 * @param <T>
 *    the class of the elements of this multi-set. 
 *
 * Created: Sun Nov 23 23:32:06 2014
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public abstract class AbstractMultiSet<MAP extends Map<T, Multiplicity>, T> 
    implements MultiSet<T> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Serves as a wrapper object for a multiplicity {@link #mult}. 
     * Unlike <code>int</code>s we have real <code>Object</code>s 
     * which can be stored in a map, e.g. {@link AbstractMultiSet#obj2mult} 
     * and unlike <code>Integer</code>s these objects are mutable. 
     */
    // **** this implementation is not optimal: 
    // better would be immutable multiplicities 
    // or just using Integers with checks moved towards enclosing class 
    public static final class MultiplicityImpl implements Multiplicity {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	/**
	 * A positive integer representing a multiplicity. 
	 */
	private int mult;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	/**
	 * Creates a new <code>Multiplicity</code> instance 
	 * representing a <em>positive</em> multiplicity. 
	 *
	 * @param mult 
	 *    a strictly positive <code>int</code> value 
	 *    representing a multiplicity. 
	 * @throws IllegalArgumentException 
	 *    if <code>mult</code> is not strictly positive. 
	 * @see #set(int)
	 */
	private MultiplicityImpl(int mult) {
	    set(mult);
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	protected static MultiplicityImpl create(int mult) {
	    return new MultiplicityImpl(mult);
	}

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
	public int set(int mult) {
	    if (mult <= 0) {
		throw new IllegalArgumentException
		    ("Expected non-negative multiplicity; found " + 
		     mult + ". ");
	    }
	    int oldMult = this.mult;
	    this.mult = mult;
	    return oldMult;
	}

	/**
	 * Adds the specified multiplicity (which may well be negative) 
	 * to the wrapped multiplicity {@link #mult} which is thus modified. 
	 *
	 * @param mult 
	 *    an <code>int</code> such that <code>this.mult + mult &gt; 0</code> 
	 *    holds. 
	 * @return
	 *    the new multiplicity <code>this.mult + mult</code>. 
	 * @throws IllegalArgumentException 
	 *    if <code>this.mult + mult &lt; 0</code> holds. 
	 * @throws IllegalStateException 
	 *    if <code>this.mult + mult == 0</code> holds. 
	 *    This cannot occur: if it does this is a bug within this class. 
	 */
	public int add(int mult) {
	    // **** not completely ok: overflow 
	    this.mult += mult;
	    if (this.mult <= 0) {
		if (this.mult == 0) {
		    throw new IllegalStateException
			("should not occur: removed element implicitely. " );
		}

		this.mult -= mult;
		throw new IllegalArgumentException
		    ("Resulting multiplicity " + 
		     this.mult + " + " + mult + 
		     " should be non-negative. ");
	    }
	    return this.mult;
	}

	/**
	 * Returns the wrapped multiplicity. 
	 *
	 * @return 
	 *    {@link #mult}. 
	 */
	public int get() {
	    return this.mult;
	}

	/**
	 * Defines the natural ordering on natural numbers. 
	 *
	 * @param mult 
	 *    a <code>Multiplicity</code> which should in fact 
	 *    be another {@link Multiplicity}. 
	 * @return 
	 *    the difference of the wrapped {@link #mult}-values. 
	 * @throws NullPointerException 
	 *    for <code>mult == null</code>. 
	 * @throws ClassCastException 
	 *    if <code>mult</code> is neither <code>null</code> 
	 *    nor an instance of {@link Multiplicity}. 
	 */
	public int compareTo(Multiplicity mult) {
	    return this.get() - mult.get();
	}

	// api-docs provided by javadoc. 
	public String toString() {
	    return "Multiplicity " + get();
	}

	/**
	 * Returns <code>true</code> if and only if 
	 * <code>obj</code> is also an instance of <code>Multiplicity</code> 
	 * and if the wrapped multiplicities coincide. 
	 *
	 * @param obj 
	 *    an <code>Object</code> value 
	 *    which may well be <code>null</code>. 
	 * @return 
	 *    a <code>boolean</code> value which indicates 
	 *    whether <code>obj</code> is also an instance 
	 *    of <code>Multiplicity</code> 
	 *    and whether the wrapped multiplicity coincides with this one. 
	 * @see #compareTo
	 */
	public boolean equals(Object obj) {
	    if (!(obj instanceof Multiplicity)) {
		return false;
	    }
	    return ((Multiplicity) obj).get() == this.get();
	}

	// api-docs provided by javadoc. 
	public int hashCode() {
	    return this.mult;
	}
    } // class Multiplicity 

    /**
     * A canonical implementation of {@link MultiSetIterator} 
     * defining also the methods modifying the underlying {@link MultiSet}, 
     * namely {@link #remove()}, {@link #setMult(int)} 
     * and {@link #removeMult(int)}. 
     *
     * @param <T>
     *    the class of the elements of the underlying multi-set. 
     */
    protected static class MultiSetIteratorImpl<T> 
	implements MultiSetIterator<T> {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	/**
	 * An iterator on the entries 
	 * of the map {@link AbstractMultiSet#obj2mult} 
	 * associating each element of the underlying {@link MultiSet} 
	 * with its multiplicity. 
	 */
	private final Iterator<Map.Entry<T, Multiplicity>> entrySetIter;

	/**
	 * The element returned last by invoking {@link #next()} 
	 * in the iterator {@link #entrySetIter} 
	 * or <code>null</code> if {@link #next()} has not yet been invoked 
	 * or the element returned by the last invocation of {@link #next()} 
	 * has been removed in the meantime 
	 * invoking a method of this iterator (instance). 
	 */
	private Map.Entry<T, Multiplicity> last;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	MultiSetIteratorImpl(MultiSet<T> multiSet) {
	    this.entrySetIter = multiSet.getSetWithMults().iterator();
	    this.last = null;
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public final boolean hasNext() {
	    return this.entrySetIter.hasNext();
	}

	/**
	 * Returns the next element in the iteration 
	 * and, as a side effect, sets {@link #last} 
	 * with the mapping of that element to its current multiplicity. 
	 */
	public final T next() {
	    this.last = this.entrySetIter.next();
	    return this.last.getKey();
	}

	/**
	 * Removes from the underlying {@link MultiSet} 
	 * the last element returned by {@link #next()}, 
	 * provided that element was not removed in the meantime 
	 * and this method is supported by this iterator. 
	 * As a side effect, sets {@link #last} to <code>null</code> 
	 * indicating that this element has been removed. 
	 *
	 * @throws UnsupportedOperationException
	 *    this implementation does not throw the exception 
	 *    but the iterator of an immutable multi set of course do.  
	 */
	public final void remove()  {
	    // throws IllegalStateException if no longer present 
	    this.entrySetIter.remove();
	    this.last = null;
	}

	/**
	 * Returns the current multiplicity of the element 
	 * last read by {@link #next()}, 
	 * provided that element was not removed in the meantime. 
	 */
	public final int getMult() {
	    // getMultObj() may throw IllegalStateException 
	    return getMultObj().get();
	}

	public final Multiplicity getMultObj() {
	    if (this.last == null) {
		// no message as for method remove() 
		throw new IllegalStateException();
	    }
	    assert this.last != null;
	    return this.last.getValue();
	}
	// 
	/**
	 *
	 *
	 * @throws UnsupportedOperationException
	 *    this implementation does not throw the exception 
	 *    but the iterator of an immutable multi set of course do.  
	 */
	public final int setMult(int mult) {
	    // may throw IllegalStateException 
	    Multiplicity last = getMultObj();
	    assert last != null;
	    if (mult == 0) {
		int res = last.get();
		// may throw UnsupportedOperationException
		remove();
		return res;
	    }
	    // may throw IllegalArgumentException, 
	    // may throw UnsupportedOperationException
	    return last.set(mult);
	}

	/**
	 *
	 *
	 * @throws UnsupportedOperationException
	 *    this implementation does not throw the exception 
	 *    but the iterator of an immutable multi set of course do.  
	 */
	public final int removeMult(int mult)  {
	    // may throw IllegalStateException 
	    Multiplicity last = getMultObj();
	    assert last != null;
	    // return value is old multiplicity 
	    int oldMult = last.get();
	    if (mult == oldMult) {
		remove();
		return oldMult;
	    }

	    // may throw an IllegalArgumentException 
	    // may throw UnsupportedOperationException
	    last.add(-mult);
	    return oldMult;
	}

    } // class MultiSetIteratorImpl 

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * Maps objects to its multiplicities. 
     * The keys are objects whereas the corresponding values 
     * are strictly positive <code>Integer</code>s 
     * representing the corresponding multiplicities. 
     * If an object is not within this set, 
     * the corresponding value is <code>null</code>. 
     * **** maybe even: object not in keyset. 
     * In the key set no <code>null</code> values may occur. 
     */
    protected final MAP obj2mult;




    /* -------------------------------------------------------------------- *
     * constructors and creator methods.                                    *
     * -------------------------------------------------------------------- */

    public AbstractMultiSet(MAP t2mult) {
	this.obj2mult = t2mult;
    }

    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    // Query Operations

    /**
     * Returns the number of pairwise different elements 
     * in this <code>MultiSet</code>. 
     * If this <code>MultiSet</code> 
     * contains more than <code>Integer.MAX_VALUE</code> elements, 
     * returns <code>Integer.MAX_VALUE</code>. 
     * 
     * @return 
     *    the number of elements in this <code>MultiSet</code> 
     *    each multiple element counted as a single one. 
     * @see #sizeWithMult()
     */
    public final int size() {
	// works only, since multiplicities 0 are not allowed 
	// (null-elements in turn would still work here) 
	return this.obj2mult.size();
    }

    /**
     * Returns the number of elements 
     * in this <code>MultiSet</code> counted with multiplicities. 
     * If this <code>MultiSet</code> 
     * contains more than <code>Integer.MAX_VALUE</code> elements, 
     * returns <code>Integer.MAX_VALUE</code>. 
     * 
     * @return 
     *    the number of elements in this <code>MultiSet</code> 
     *    counted with multiplicities, 
     *    provided this does not exceed {@link Integer#MAX_VALUE}; 
     *    otherwise just {@link Integer#MAX_VALUE}. 
     * @see #size()
     */
    public final int sizeWithMult() {
	int result = 0;
	for (Multiplicity mult : this.obj2mult.values()) {
	    result += mult.get();
	    if (result < 0) {
		return Integer.MAX_VALUE;
	    }
	}
	assert result >= 0;
	
	return result;
    }

    /**
     * Returns whether this multiple set contains no element. 
     *
     * @return 
     *    whether this multiple set contains no element. 
     */
    public final boolean isEmpty() {
	return this.obj2mult.isEmpty();
    }

    /**
     * Returns a Map.Entry 
     * representing an element in this <code>MultiSet</code> 
     * with maximal multiplicity together with this multiplicity, 
     * except if this set is empty. 
     * For empty sets, <code>null</code> is returned. 
     *
     * @return 
     *    <ul>
     *    <li> if this <code>MultiSet</code> is not empty, 
     *    a <code>Map.Entry</code> object <code>em</code> is returned: 
     *    <code>em.getKey()</code> is an element of this <code>MultiSet</code> 
     *    and <code>em.getValue()</code> is a <code>Multiplicity</code> 
     *    wrapping its multiplicity <code>m = em.getValue().get()</code>. 
     *    This multiplicity is maximal 
     *    but if there is more than one such maximal multiplicity, 
     *    it is not specified which <code>Map.Entry</code> is returned. 
     *    <p>
     *    Note that <code>em.getKey()</code> may never be <code>null</code>
     *
     *    <li> if this <code>MultiSet</code> is empty, 
     *    <code>null</code> is returned. 
     *    </ul>
     * @see #getObjWithMaxMult()
     * @see #getMaxMult()
     */
    private Map.Entry<T, Multiplicity> getMaxObjWithMult() {
	// return value for empty set.
	Map.Entry<T, Multiplicity> maxCand = null;

	// search for greater value than maxVal
	int maxVal = 0;
	int cmpVal;
	for (Map.Entry<T, Multiplicity> cand : this.obj2mult.entrySet()) {
	    cmpVal = cand.getValue().get();
	    if (maxVal < cmpVal) {
		maxCand = cand;
		maxVal = cmpVal;
	    }
	}
	return maxCand;
    }

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
    public final T getObjWithMaxMult() {
	if (isEmpty()) {
	    return null;
	}
	return getMaxObjWithMult().getKey();
    }

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
    public final int getMaxMult() {
	if (isEmpty()) {
	    return 0;
	}
	return getMaxObjWithMult().getValue().get();
    }

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
    public final int getMultiplicity(Object obj) {
	// throws NullPointerException for obj==null 
	Multiplicity result = getMultiplicityObj(obj);
	return result == null ? 0 : result.get();
    }

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
    public final Multiplicity getMultiplicityObj(Object obj) {
	if (obj == null) {
	    throw new NullPointerException(); // NOPMD 
	}
	// Here, obj != null. 
	return this.obj2mult.get(obj);
    }

    /**
     * Returns <code>true</code> if this <code>MultiSet</code> 
     * contains the specified element. 
     * More formally, returns <code>true</code> if and only if this
     * <code>MultiSet</code> contains at least one element <code>e</code> 
     * such that <code>(o==null ? e==null : o.equals(e))</code>. 
     *
     * @param obj 
     *    element (not <code>null</code>) 
     *    whose presence in this <code>MultiSet</code> is to be tested.
     * @return 
     *    <code>true</code> if this <code>MultiSet</code> 
     *    contains the specified element. 
     * @throws NullPointerException
     *    for <code>obj==null</code>. 
     */
    public final boolean contains(Object obj) {
	// throws NullPointerException for obj==null 
	return getMultiplicityObj(obj) != null;
    }

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
     *    an <code>Iterator</code> over the elements in this collection 
     *    considering each element exactly once ignoring its multiplicity. 
     */
    public final MultiSetIterator<T> iterator() {
	return new MultiSetIteratorImpl<T>(this);
    }

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
    public final Object[] toArray() {
	return getSet().toArray(new Object[0]);
    }

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
     * is set to <code>null</code>. 
     * This is useful in determining the length of this
     * <code>MultiSet</code> because this <code>MultiSet</code> does
     * not contain any <code>null</code> elements. 
     * <p>
     * <!--
     * If this <code>MultiSet</code> makes any guarantees 
     * as to what order its elements are returned by its iterator, 
     * this method must return the elements in
     * the same order. 
     * -->
     * <!--
     * <p>
     * Like the <code>toArray</code> method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs. 
     * <p>
     * -->
     * Suppose <code>l</code> is a <code>List</code> known to contain only strings.
     * The following code can be used to dump the list into a newly allocated
     * array of <code>String</code>:
     *
     * <pre>
     *     String[] x = (String[]) v.toArray(new String[0]);
     * </pre><p>
     *
     * Note that <code>toArray(new Object[0])</code> is identical in function to
     * <code>toArray()</code>.
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
     *    if the specified array is <code>null</code>. 
     */
    public final T[] toArray(T[] arr) {
	return getSet().toArray(arr);
    }


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
    public final int addWithMult(T obj) {
	// throws NullPointerException for obj==null 
	return addWithMult(obj, 1);
    }

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
    public final int addWithMult(T obj, int addMult) {

	if (addMult < 0) {
	    throw new IllegalArgumentException
		("Expected non-negative multiplicity; found " + 
		 addMult + ". ");
	}
	// throws NullPointerException for obj==null 
	Multiplicity mult = getMultiplicityObj(obj);

	if (mult == null) {
	    // Here, this element is not in this set 
	    if (addMult != 0) {
		assert addMult > 0;
		mult = MultiplicityImpl.create(addMult);
		this.obj2mult.put(obj, mult);
	    }
	    return addMult;
	}
	// Here, obj is already in this set. 
	return mult.add(addMult);
    }

    /**
     * Adds <code>obj</code> to this <code>MultiSet</code>. 
     * In other words, increments the multiplicity of <code>obj</code> by one. 
     * Returns <code>true</code> if this <code>MultiSet</code> 
     * interpreted as a set changed as a result of the call. 
     * (Returns <code>false</code> if this <code>MultiSet</code> 
     * already contains the specified element (with nontrivial multiplicity). 
     * <!--
     * <p>
     * <Code>MultiSet</Code>s that support this operation 
     * may place limitations on what elements may be added 
     * to this <code>MultiSet</code>. 
     * In particular, some<code>MultiSet</code>s will refuse 
     * to add <code>null</code> elements, and others will impose restrictions 
     * on the type of elements that may be added. 
     * <Code>MultiSet</Code> classes should clearly specify 
     * in their documentation any 
     * restrictions on what elements may be added. 
     * <p>
     * If a <code>MultiSet</code> refuses to add a particular element 
     * for any reason other than that it already contains the element, 
     * it <i>must</i> throw an exception 
     * (rather than returning <code>false</code>).  
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
     *    <code>true</code> if and only if 
     *    the multiplicity of the specified element 
     *    was <code>0</code> before the call of this method. 
     * @throws NullPointerException 
     *    if the specified element is <code>null</code>. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    public final boolean add(T obj) {
	// throws NullPointerException for obj==null 
	Multiplicity mult = getMultiplicityObj(obj);
	if (mult == null) {
	    mult = MultiplicityImpl.create(1);
	    this.obj2mult.put(obj, mult);
	    return true;
	}
	mult.add(1);
	return false;
   }

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
    public final int removeWithMult(Object obj) {
	// throws NullPointerException for obj==null 
	return removeWithMult(obj, 1);
   }

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
    public final int removeWithMult(Object obj, int removeMult) {
	if (removeMult < 0) {
	    throw new IllegalArgumentException
		("Expected non-negative multiplicity; found " + 
		 removeMult + ". ");
	}

	// throws NullPointerException for obj==null 
	Multiplicity mult = getMultiplicityObj(obj);
	if (mult == null) {
	    if (removeMult != 0) {
		throw new IllegalArgumentException
		    ("Tried to remove object " + obj + 
		     " which is not in this MultiSet. ");
	    }
	    return 0;
	}
	// return value is old multiplicity 
	int ret = mult.get();
	if (ret == removeMult) {
	    this.obj2mult.remove(obj);
	} else {
	    mult.add(-removeMult);
	}
	return ret;
    }

    /**
     * Removes <em>all</em> instances of the specified element from this 
     * <code>MultiSet</code>, if it is present with nontrivial multiplicity. 
     * More formally,
     * immediately after having (successively) 
     * invoked <code>s.remove(o)</code>, 
     * the condition <code>s.contains(o) == false</code> is satisfied. 
     * Returns true if this <code>MultiSet</code> contained the specified 
     * element (or equivalently, if (the underlying set of) 
     * this <code>MultiSet</code> changed as a result of the call). 
     *
     * @param obj 
     *    element the multiplicity of which in this <code>MultiSet</code> 
     *    is to be increased by one. 
     * @return 
     *    <code>true</code> if and only if this <code>MultiSet</code> changed 
     *    as a result of the call. 
     * @throws NullPointerException 
     *    if the specified element is <code>null</code>. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    public final boolean remove(Object obj) {
	if (obj == null) {
	    throw new NullPointerException(); // NOPMD
	}
	// Here, obj != null. 

	return this.obj2mult.remove(obj) != null;
    }

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
    public final int setMultiplicity(T obj, int newMult) {
	if (obj == null) {
	    throw new IllegalArgumentException
		("Found null element. ");
	}
	if (newMult < 0) {
	    throw new IllegalArgumentException
		("Found negative multiplicity " + newMult + ". ");
	}

	Multiplicity oldMult = newMult == 0 
	    ? this.obj2mult.remove(obj)
	    : this.obj2mult.put(obj, MultiplicityImpl.create(newMult));
	return oldMult == null ? 0 : oldMult.get();
    }

    // Bulk Operations

    /**
     * Returns <code>true</code> if this <code>MultiSet</code> 
     * contains all of the elements in the specified collection 
     * with strictly positive multiplicity. 
     *
     * @param  coll 
     *    collection to be checked for containment 
     *    in this <code>MultiSet</code>.
     * @return 
     *    <code>true</code> if this <code>MultiSet</code> 
     *    contains all of the elements in the specified collection. 
     * @throws NullPointerException 
     *    if the specified collection contains one or more null elements.
     * @throws NullPointerException 
     *    if the specified collection is <code>null</code>.
     * @see #contains(Object)
     */
    public final boolean containsAll(Collection<?> coll) {
	for (Object cand : coll) {
	    // throws NullPointerException if cand == null
	    if (!contains(cand)) {
		return false;
	    }
	}
	return true;
    }

    /**
     * Adds <code>mvs</code> elementwise to this multi set 
     * increasing multiplicities 
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
    public final boolean addAll(MultiSet<? extends T> mvs) {

	int mvsMult;
	boolean added = false;
	for (T cand : mvs.getSet()) {
	    mvsMult = mvs.getMultiplicity(cand);
	    assert mvsMult > 0;
	    Multiplicity mult = this.obj2mult.get(cand);
	    if (mult == null) {
		// Here, cand has not been in this multi-set 
		this.obj2mult.put(cand, MultiplicityImpl.create(mvsMult));
		added = true;
	    } else {
		// Here, cand has been in this multi-set 
		mult.add(mvsMult);
	    }
	} // for 
	return added;
    }

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
    public final boolean addAll(Set<? extends T> set) {
	boolean added = false;
	for (T cand : set) {
	    Multiplicity mult = this.obj2mult.get(cand);
	    if (mult == null) {
		// Here, cand has not been in this multi-set 
		this.obj2mult.put(cand, MultiplicityImpl.create(1));
		added = true;
	    } else {
		// Here, cand has been in this multi-set 
		mult.add(1);
	    }
	} // for 
	return added;
    }

    /**
     * Removes all this <code>MultiSet</code>'s elements 
     * that are also contained in the specified collection. 
     * After this call returns, this <code>MultiSet</code> 
     * will contain no elements in common with the specified collection. 
     *
     * @param coll 
     *    elements to be removed from this <code>MultiSet</code>. 
     * @return 
     *    <code>true</code> if this <code>MultiSet</code> 
     *    changed as a result of the call. 
     * @throws NullPointerException 
     *    if the specified collection is <code>null</code>. 
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public final boolean removeAll(Collection<?> coll) {
	boolean thisChanged = false;
	for (Object cand : coll) {
	    // throws NullPointerException if cand == null 
	    thisChanged |= remove(cand);
	}
	return thisChanged;
    }

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
     *    <code>true</code> if this <code>MultiSet</code> changed 
     *    as a result of the call. 
     * @throws NullPointerException 
     *    if the specified collection is <code>null</code>.
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public final boolean retainAll(Collection<?> coll) {
	boolean result = false;

	Iterator<T> iter = iterator();
	T cand;
	while (iter.hasNext()) {
	    cand = iter.next();
	    if (!coll.contains(cand)) {
		iter.remove();
		result = true;
	    }
	}
	return result;
    }

    /**
     * Removes all of the elements from this <code>MultiSet</code>. 
     * This <code>MultiSet</code> will be empty after this method returns. 
     *
     * @throws UnsupportedOperationException
     *    if this <code>MultiSet</code> does not support this method. 
     */
    public final void clear() {
	this.obj2mult.clear();
    }

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
    public final Set<Map.Entry<T, Multiplicity>> getSetWithMults() {
	return this.obj2mult.entrySet();
    }

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
    public final boolean equals(Object obj) {
	if (!(obj instanceof MultiSet)) {
	    return false;
	}
	MultiSet<?> other = (MultiSet<?>) obj;
	return this.getSetWithMults().equals(other.getSetWithMults());
    }

    public final int hashCode() {
	int result = 0;
	for (T cand : getSet()) {
	    result += cand.hashCode() * getMultiplicity(cand);
	}
	return result;
    }
}
