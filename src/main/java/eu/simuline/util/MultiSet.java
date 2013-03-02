
package eu.simuline.util;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Comparator;

/**
 * Represents a set with multiplicities. 
 * Mathematically this is something between a set and a family. 
 * Note that this should implement Collection, but still does not *****. 
 * addAll's implementation seems strange, 
 * add seems to be buggy, 
 * One should not require a TreeMap Alternative: HashMap. 
 *
 * @author <a href="mailto:ereissner@rig35.rose.de">Ernst Reissner</a>
 * @version 1.0
 */
public class MultiSet<T> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Serves as a wrapper object for a multiplicity {@link #mult}. 
     * Unlike <code>int</code>s we have real <code>Object</code>s 
     * which can be stored in a map, e.g. {@link MultiSet#obj2mult} 
     * and unlike <code>Integer</code>s these objects are mutable. 
     */
    private static class Multiplicity implements Comparable {

	/**
	 * A non-negative integer representing a multiplicity. 
	 */
	private int mult;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	/**
	 * Creates a new <code>Multiplicity</code> instance 
	 * representing multiplicity <code>0</code>. 
	 * @see #MultiSet.Multiplicity(int)
	 */
	private Multiplicity() {// even more private ;-)
	    this.mult = 0;
	}

	/**
	 * Creates a new <code>Multiplicity</code> instance 
	 * representing a <em>positive</em> multiplicity. 
	 *
	 * @param mult 
	 *    a strictly positive <code>int</code> value 
	 *    representing a multiplicity. 
	 * @throws IllegalArgumentException 
	 *    if <code>mult</code> is not strictly positive. 
	 * @see #set
	 * @see #MultiSet.Multiplicity()
	 */
	private Multiplicity(int mult) {
	    set(mult);
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	private static Multiplicity createZero() {
	    return new Multiplicity();
	}

	private static Multiplicity create(int mult) {
	    return new Multiplicity(mult);
	}

	/**
	 * Sets the multiplicity wrapped by this object 
	 * to the specified value. 
	 *
	 * @param mult 
	 *    a strictly positive <code>int</code> value 
	 *    representing a multiplicity. 
	 * @throws IllegalArgumentException 
	 *    if <code>mult</code> is not strictly positive. 
	 */
	int set(int mult) {
	    if (mult <= 0) {
		throw new IllegalArgumentException
		    ("Multiplicity " + mult + 
		     " should be strictly positive. ");
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
	 *    an <code>int</code> such that <code>this.mult + mult > 0</code> 
	 *    holds. 
	 * @return
	 *    the new multiplicity <code>this.mult + mult</code>. 
	 * @throws IllegalArgumentException 
	 *    if not <code>this.mult + mult > 0</code> holds. 
	 */
	int add(int mult) {
	    this.mult += mult;
	    if (this.mult <= 0) {
		this.mult -= mult;
		throw new IllegalArgumentException
		    ("Resulting multiplicity " + 
		     this.mult + " + " + mult + 
		     " should be strictly positive. ");
	    }
	    return this.mult;
	}

	/**
	 * Returns the wrapped multiplicity. 
	 *
	 * @return 
	 *    {@link #mult}. 
	 */
	int get() {
	    return this.mult;
	}

	/**
	 * Defines the natural ordering on natural numbers. 
	 *
	 * @param obj 
	 *    an <code>Object</code> which should in fact 
	 *    be another {@link MultiSet.Multiplicity}. 
	 * @return 
	 *    the difference of the wrapped {@link #mult}-values. 
	 * @throws NullPointerException 
	 *    for <code>obj == null</code>. 
	 * @throws ClassCastException 
	 *    if <code>obj</code> is neither <code>null</code> 
	 *    nor an instance of {@link MultiSet.Multiplicity}. 
	 */
	public int compareTo(Object obj) {
	    return this.get()-((Multiplicity)obj).get();
	}

	// api-docs provided by javadoc. 
	public String toString() {
	    return "Multiplicity " + get();
	}

	/**
	 * Returns <code>true</code> if and only if 
	 * <code>obj</code> is also an instance of <code>Multiplicity></code> 
	 * and if the wrapped multiplicities coincide. 
	 *
	 * @param obj 
	 *    an <code>Object</code> value 
	 *    which may well be <code>null</code>. 
	 * @return 
	 *    a <code>boolean</code> value which indicates 
	 *    whether <code>obj</code> is also an instance 
	 *    of <code>Multiplicity></code> 
	 *    and whether the wrapped multiplicity coincides with this one. 
	 * @see #compareTo
	 */
	public boolean equals(Object obj) {
	    if (!(obj instanceof Multiplicity)) {
		return false;
	    }
	    return ((Multiplicity)obj).get() == this.get();
	}

	// api-docs provided by javadoc. 
	public int hashCode() {
	    return this.mult;
	}
    } // class Multiplicity 

    /**
     * Represents immutable <code>MultiSet</code>s 
     * as e.g. the one in {@link MultiSet#EMPTY_SET}. 
     */
    public final static class Immutable<T> extends MultiSet<T> {

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	/**
	 * Creates a new, empty <code>ImmutableMultiSet</code>. 
	 */
	private Immutable() {
	    super();
	}

	/**
	 * Copy constructor. 
	 * Wrapps another multiset in an equivalent but immutable one. 
	 *
	 * @param other 
	 *    another <code>MultiSet</code> instance. 
	 */
	public Immutable(MultiSet<T> other) {
	    super(other);
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */


	private static Immutable createEmpty() {
	    return new Immutable();
	}

	/*
	 * This is not only not necessary; 
	 * the default constructor would become invalid with this definition. 
	 * @throws UnsupportedOperationException
	 */
	public void clear() {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public int addWithMult(T obj) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public int addWithMult(T obj, int addMult) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean add(T obj) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public int removeWithMult(Object obj) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public int removeWithMult(Object obj,int removeMult) {
	    throw new UnsupportedOperationException();
	}

	public boolean remove(Object obj) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public int setMultiplicity(T obj,int newMult) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean addAll(MultiSet<? extends T> mvs) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean removeAll(Collection<?> coll) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean retainAll(Collection<?> coll) {
	    throw new UnsupportedOperationException();
	}

    } // class ImmutableMultiSet 

    /* -------------------------------------------------------------------- *
     * class constants.                                                     *
     * -------------------------------------------------------------------- */

    /**
     * A constant containing an <em>immutable</em> empty set. 
     * @see MultiSet.Immutable
     */
    public final static MultiSet EMPTY_SET =  Immutable.createEmpty();

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
     * In the key set no <code>null</code> values may occur. 
     */
    protected final SortedMap<T,Multiplicity> obj2mult;

    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Creates a new, empty <code>MultiSet</code>. 
     */
    public MultiSet() {
	this.obj2mult = new TreeMap<T,Multiplicity>();
    }

    /**
     * Creates a new, empty <code>MultiSet</code>. 
     */
    public MultiSet(Comparator<? super T> comp) {
	this.obj2mult = new TreeMap<T,Multiplicity>(comp);
    }

    /**
     * Copy constructor. 
     *
     * @param other 
     *    another <code>MultiSet</code> instance. 
     */
    public MultiSet(MultiSet<? extends T> other) {
	this.obj2mult = new TreeMap<T,Multiplicity>(other.obj2mult);
    }

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
     * @see #sizeWithMult
     */
    public int size() {
	return this.obj2mult.size();
    }

    /**
     * Returns the number of elements 
     * in this <code>MultiSet</code> counted with multiplicities. 
     * If this <code>MultiSet</code> 
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, 
     * returns <tt>Integer.MAX_VALUE</tt>. 
     * 
     * @return 
     *    the number of elements in this <code>MultiSet</code> 
     *    counted with multiplicities. 
     * @see #size
     */
    public int sizeWithMult() {
	int result = 0;
	for (Multiplicity mult : this.obj2mult.values()) {
	    result += mult.get();
	}
	
	return result;
    }

    /**
     * Returns whether this multiple set contains no element. 
     *
     * @return 
     *    whether this multiple set contains no element. 
     */
    public boolean isEmpty() {
	return this.obj2mult.isEmpty();
    }

    /**
     * Returns an object wrapping an element in this <code>MultiSet</code> 
     * with maximal multiplicity together with this multiplicity. 
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
     *    <code>em.getKey() == null</code> and <code>em.getValue() == 0</code>. 
     *    Both conditions are equivalet with <code>this.isEmpty()</code>
     *    </ul>
     */
    private Map.Entry<T,Multiplicity> getMaxObjWithMult() {
	Map.Entry<T,Multiplicity> maxCand = 
	    new Map.Entry<T,Multiplicity>() {
		public T getKey() {
		    return null;
		}
		public Multiplicity getValue() {
		    return Multiplicity.createZero();
		}
		public Multiplicity setValue(Multiplicity value) {
		    throw new UnsupportedOperationException();
		}
	    };
	for (Map.Entry<T,Multiplicity> cand : this.obj2mult.entrySet()) {
	    if (          (maxCand.getValue())
			  .compareTo(   cand.getValue()) < 0) {
		maxCand = cand;
	    }
	}
	return maxCand;
    }

    /**
     * Returns the in this multiple set with maximal multiplicity. 
     * The return value is <code>null</code> 
     * if and only if this set is empty. 
     *
     * @return 
     *    a <code>Object o != null</code> with maximal multiplicity 
     *    or <code>null</code> if this multiple set is empty. 
     * @see #isEmpty
     */
    public Object getObjWithMaxMult() {
	return getMaxObjWithMult().getKey();
    }

    /**
     * Returns the maximal multiplicity of an element in this set. 
     *
     * @return 
     *    a non-negative <code>int</code> value 
     *    which is the maximal mutliplicity of an element in this set. 
     *    In particular this is <code>0</code> 
     *    if and only if this set is empty. 
     * @see #getMultiplicity
     */
    public int getMaxMult() {
	return getMaxObjWithMult().getValue().get();
    }

    /**
     * Returns the multiplicity 
     * which which the given object occurs within this set. 
     *
     * @param obj 
     *    an <code>Object</code> value. 
     * @return 
     *    a non-negative <code>int</code> value 
     *    which is the mutliplicity of the given element in this set. 
     *    In particular this is <code>0</code> if and only if 
     *    <ul>
     *    <li>
     *    <code>obj == null</code> or 
     *    <li>
     *    <code>obj</code> is an instance which is not in this set. 
     *    </ul>
     * @see #setMultiplicity
     */
    public int getMultiplicity(Object obj) {
	Multiplicity result = this.obj2mult.get(obj);
	return result == null ? 0 : result.get();
    }

    private Multiplicity getMultiplicityObj(Object obj) {
	Multiplicity result = this.obj2mult.get(obj);
	return result == null ? Multiplicity.createZero() : result;
    }

    /**
     * Returns <tt>true</tt> if this <code>MultiSet</code> 
     * contains the specified element. 
     * More formally, returns <tt>true</tt> if and only if this
     * <code>MultiSet</code> contains at least one element <tt>e</tt> 
     * such that <tt>(o==null ? e==null : o.equals(e))</tt>. 
     *
     * @param obj 
     *    element whose presence in this <code>MultiSet</code> 
     *    is to be tested.
     * @return 
     *    <tt>true</tt> if this <code>MultiSet</code> 
     *    contains the specified element. 
     *    Since <code>null</code> is not contained 
     *    in this <code>MultiSet</code>, 
     *    <code>contains(null) == false</code>. 
     */
    public boolean contains(Object obj) {
	return this.obj2mult.get(obj) != null;
    }

    /**
     * Returns an iterator over the elements in this collection 
     * which emits each element exactly once, 
     * without regarding its multiplicity. 
     * <!-- There are no guarantees concerning the order 
     * in which the elements are returned
     * (unless this collection is an instance of some class 
     * that provides a guarantee). -->
     * 
     * @return 
     *    an <tt>Iterator</tt> over the elements in this collection 
     *    considering each element exactly once ignoring its multiplicity. 
     */
    public Iterator iterator() {
	return getSet().iterator();
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
    public Object[] toArray() {
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
     * is set to <tt>null</tt>. 
     * This is useful in determining the length of this
     * <code>MultiSet</code> because this <code>MultiSet</code> does
     * not contain any <tt>null</tt> elements. 
     * <p>
     * <!--
     * If this <code>MultiSet</code> makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
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
    public T[] toArray(T[] arr) {
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
     */
    public int addWithMult(T obj) {
	if (obj == null) {
	    throw new NullPointerException();
	}
	// Here, obj != null. 

	Multiplicity mult = this.obj2mult.get(obj);
	if (mult == null) {
	    this.obj2mult.put(obj, Multiplicity.create(1));
	    return 1;
	} else {
	    mult.add(1);
	    return mult.get();
	}
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
     *    a strictly positive <code>int</code> value: 
     *    the new multiplicity of <code>obj</code>. 
     * @throws NullPointerException 
     *    for <code>obj == null</code>. 
     * @throws IllegalArgumentException 
     *    for <code>addMult &lt; 0</code>. 
     */
    public int addWithMult(T obj, int addMult) {
	if (obj == null) {
	    throw new NullPointerException();
	}
	if (addMult < 0) {
	    throw new IllegalArgumentException
		("Expected non-negative multiplicity; found " + 
		 addMult + ". ");
	}
	// Here, obj != null. 

	Multiplicity mult = this.obj2mult.get(obj);
	if (mult == null) {
	    if (addMult != 0) {
		this.obj2mult.put(obj,Multiplicity.create(addMult));
	    }
	    return addMult;
	} else {
	    return mult.add(addMult);
	}
    }

    /**
     * Adds <code>obj</code> to this <code>MultiSet</code> 
     * and returns the new multiplicity of this object. 
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
     */
    public boolean add(T obj) {
	if (obj == null) {
	    throw new NullPointerException();
	}
	
	Multiplicity mult = this.obj2mult.get(obj);
	if (mult == null) {
	    this.obj2mult.put(obj,Multiplicity.create(1));
	    return true;
	} else {
	    mult.add(1);
	    return false;
	}
   }

    /**
     * Decrements the multiplicity of <code>obj</code> in this <code>MultiSet</code> 
     * if it is present and returns the <em>old</em> multiplicity of <code>obj</code>; 
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
     */
    public int removeWithMult(Object obj) {
	if (obj == null) {
	    throw new NullPointerException();
	}
	// Here, obj != null. 


	Multiplicity mult = getMultiplicityObj(obj);
	int multInt = mult.get();
	switch (multInt) {
	case 0:
	    // nothing to be done. 
	    return 0;
	case  1:
	    // Here, the new multiplicity is 0
	    this.obj2mult.remove(obj);
	    return 1;
	default:
	    // Here, the new multiplicity is strictly positive. 
	    return mult.set(--multInt);
	}
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
     */
    public int removeWithMult(Object obj,int removeMult) {
	if (obj == null) {
	    throw new NullPointerException();
	}
	// Here, obj != null. 
	if (removeMult < 0) {
	    throw new IllegalArgumentException
		("Expected non-negative multiplicity; found " + 
		 removeMult + ". ");
	}
	// Here, obj != null. 

	Multiplicity mult = this.obj2mult.get(obj);
	if (mult == null) {
	    if (removeMult != 0) {
		throw new IllegalArgumentException
		    ("Tried to remove object " + obj + 
		     " which is not in this MultiSet. ");
	    }
	    return 0;
	}
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
     * immediately after having (successively) invoked <code>s.remove(o)</code>, 
     * the condition <code>s.contains(o) == false</code> is satisfied. 
     * Returns true if this <code>MultiSet</code> contained the specified 
     * element (or equivalently, if this <code>MultiSet</code> changed 
     * as a result of the call). 
     *
     * @param obj 
     *    element the multiplicity of which in this <code>MultiSet</code> 
     *    is to be increased by one. 
     * @return 
     *    <tt>true</tt> if and only if  this <code>MultiSet</code> changed 
     *    as a result of the call. 
     * @throws NullPointerException 
     *    if the specified element is <code>null</code>. 
     */
    public boolean remove(Object obj) {
	if (obj == null) {
	    throw new NullPointerException();
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
     * @see #getMultiplicity
     */
    public int setMultiplicity(T obj,int newMult) {
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
	    : this.obj2mult.put(obj,Multiplicity.create(newMult));
	return oldMult == null ? 0 : oldMult.get();
    }

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
    public boolean containsAll(Collection coll) {
	for (Object cand : coll) {
	    if (cand == null) {
		throw new NullPointerException();
	    }
	    if (!contains(cand)) {
		return false;
	    }
	}
	return true;
    }

    /**
     * Adds <code>mvs</code> elementwise to this set 
     * and returns whether this caused a change. 
     * **** strange implementation; also: change 
     *
     * @param mvs 
     *    a <code>MultiSet</code> object. 
     * @return 
     *    returns whether adding changed this MultiSet 
     *    interpreted as a set. 
     */
    public boolean addAll(MultiSet<? extends T> mvs) {

	int mvsMult;
	boolean added = false;
	for (T cand : mvs.obj2mult.keySet()) {
	    mvsMult = mvs.getMultiplicity(cand);
	    // Here, mvsMult != 0 
	    Multiplicity mult = this.obj2mult.get(cand);
	    if (mult == null) {
		this.obj2mult.put(cand,Multiplicity.create(mvsMult));
		added = true;
	    } else {
		mult.add(mvsMult);
	    }
	}
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
     *    <tt>true</tt> if this <code>MultiSet</code> 
     *    changed as a result of the call. 
     * @throws NullPointerException 
     *    if the specified collection is <code>null</code>. 
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection<?> coll) {
	boolean thisChanged = false;
	for (Object cand : coll) {
	    if (cand == null) {
		throw new NullPointerException();
	    }
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
     *    <tt>true</tt> if this <code>MultiSet</code> changed 
     *    as a result of the call. 
     * @throws NullPointerException 
     *    if the specified collection is <tt>null</tt>.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection<?> coll) {
	boolean result = false;
	Iterator iter = iterator();
	Object cand;
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
     */
    public void clear() {
	this.obj2mult.clear();
    }

    /**
     * Returns the underlying set of this <code>MultiSet</code>. 
     *
     * @return 
     *    the <code>Set</code> containing exactly the objects 
     *    with strictly positive multiplicity in this <code>MultiSet</code>. 
     */
    public Set<T> getSet() {
	return this.obj2mult.keySet();
    }

    public String toString() {
	return "<MultiSet comparator=\"" + this.obj2mult.comparator() + 
	    "\">" + this.obj2mult + "</MultiSet>";
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
    public boolean equals(Object obj) {
	if (!(obj instanceof MultiSet)) {
	    return false;
	}
	MultiSet other = (MultiSet)obj;
	return this.obj2mult.equals(other.obj2mult);
    }

    public int hashCode() {
	int result = 0;
	for (T cand : getSet()) {
	    result += cand.hashCode()*getMultiplicity(cand);
	}
	return result;
    }
}
