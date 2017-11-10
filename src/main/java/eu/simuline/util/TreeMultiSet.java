
package eu.simuline.util;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.NoSuchElementException; // for javadoc only 

/**
 * Represents a sorted set with multiplicities based on a {@link TreeMap}. 
 * Mathematically this is something between a set and a family. 
 * Note that this kind of set does not support <code>null</code> elements. 
 *
 * @param <T>
 *    the class of the elements of this multi-set. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public class TreeMultiSet<T> 
    extends AbstractMultiSet<NavigableMap<T, MultiSet.Multiplicity>, T> 
    implements SortedMultiSet<T> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */


    /**
     * Represents immutable <code>MultiSet</code>s 
     * as e.g. the one given by {@link TreeMultiSet#emptyMultiSet()}. 
     * **** Idea: use {@link Collections#unmodifiableMap(Map)} 
     * but still modifications of multiplicities must be handled. 
     *
     * @param <T>
     *    the class of the elements of this multi-set. 
     */
    static final class Immutable<T> extends TreeMultiSet<T> {

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	/**
	 * Creates a new, empty <code>Immutable</code>. 
	 */
	private Immutable() {
	    super();
	}

	/**
	 * Copy constructor. 
	 * Wrapps another <code>MultiSet</code> 
	 * in an equivalent but immutable one. 
	 *
	 * @param other 
	 *    another <code>MultiSet</code> instance. 
	 */
	Immutable(TreeMultiSet<T> other) {
	    super(other);
	}

	/**
	 * Creates an immutable <code>MultiSet</code> 
	 * with the elements of <code>sSet</code> 
	 * and all elements with multiplicity <code>1</code>. 
	 */
	Immutable(Set<? extends T> sSet) {
	    this();
	    super.addAll(sSet);
	}


	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */


	private static <T> Immutable<T> createEmpty() {
	    return new Immutable<T>();
	}

	// api-docs inherited from base class 
	public SortedMultiSet<T> immutable() {
	    return this;
	}

	/**
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
	public int removeWithMult(Object obj, int removeMult) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean remove(Object obj) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public int setMultiplicity(T obj, int newMult) {
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
	public boolean addAll(Set<? extends T> set) {
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

	/**
	 * Returns an unmodifyable view of the underlying set. 
	 */
	public SortedSet<T> getSet() {
	    return Collections.unmodifiableSortedSet(super.getSet());
	}

	/**
	 * Returns an unmodifyable Set view of the mapping 
	 * from the element of this <code>MultiSet</code> 
	 * to the according multiplicities. 
	 */
	public Set<Map.Entry<T, Multiplicity>> getSetWithMults() {
	    return Collections.unmodifiableSet(super.getSetWithMults());
	}

	/**
	 * Returns an iterator which does not allow modifications 
	 * of this underlying {@link MultiSet}. 
	 */
	public MultiSetIterator<T> iterator() {
	    return new MultiSetIteratorImpl<T>(this) {
		/**
		 * @throws UnsupportedOperationException
		 */
		public void remove()  {
		    throw new UnsupportedOperationException();
		}
		/**
		 * @throws UnsupportedOperationException
		 */
		public int setMult(int setMult) {
		    throw new UnsupportedOperationException();
		}
		/**
		 * @throws UnsupportedOperationException
		 */
		public int removeMult(int removeMult)  {
		    throw new UnsupportedOperationException();
		}
	    };
	}
    } // class Immutable 

    /* -------------------------------------------------------------------- *
     * class constants.                                                     *
     * -------------------------------------------------------------------- */


    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /* -------------------------------------------------------------------- *
     * constructors and creator methods.                                    *
     * -------------------------------------------------------------------- */


    private TreeMultiSet(NavigableMap<T, Multiplicity> t2mult) {
	super(t2mult);
    }

    /**
     * Creates a new, empty <code>MultiSet</code>. 
     */
    public TreeMultiSet() {
	this(new TreeMap<T, Multiplicity>());
    }

    /**
     * Creates a new, empty <code>MultiSet</code>. 
     */
    public TreeMultiSet(Comparator<? super T> comp) {
	this(new TreeMap<T, Multiplicity>(comp));
    }

    /**
     * Copy constructor. 
     *
     * @param other 
     *    another <code>MultiSet</code> instance. 
     */
    public TreeMultiSet(MultiSet<? extends T> other) {
	this(new TreeMap<T, Multiplicity>(other.getMap()));
    }

    /**
     * Creates a multi set with the elements of <code>sSet</code> 
     * and all elements with multiplicity <code>1</code>. 
     *
     * @param  sSet
     *    some instance of a sorted set. 
     */
    public TreeMultiSet(Set<? extends T> sSet) {
	this();
	addAll(sSet);
    }

    /**
     * Returns an <em>immutable</em> empty <code>MultiSet</code>. 
     * @see TreeMultiSet.Immutable
     */
    public static <T> MultiSet<T> emptyMultiSet() {
	return Immutable.createEmpty();
    }

    /**
     * Returns an immutable copy of this <code>MultiSet</code>. 
     */
    public SortedMultiSet<T> immutable() {
	return new Immutable<T>(this);
    }

    /**
     * Returns an immutable <code>MultiSet</code> 
     * with the elements of <code>sSet</code> 
     * and all elements with multiplicity <code>1</code>. 
     */
    public static <T> MultiSet<T> immutable(Set<? extends T> sSet) {
	return new Immutable<T>(sSet);
    }

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
    public Comparator<? super T> comparator() {
	return this.obj2mult.comparator();
    }

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return
     *    the first (lowest) element currently in this set
     * @throws NoSuchElementException
     *    if this set is empty. 
     */
    public final T first() {
	return this.obj2mult.firstKey();
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return
     *    the last (highest) element currently in this set. 
     * @throws NoSuchElementException
     *    if this set is empty. 
     */
    public final T last() {
	return this.obj2mult.lastKey();
    }

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
    public final MultiSet<T> headSet(T toElement) {
	return new TreeMultiSet<T>(this.obj2mult.headMap(toElement, false));
    }

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
    public final MultiSet<T> tailSet(T fromElement) {
	return new TreeMultiSet<T>(this.obj2mult.tailMap(fromElement, true));
    }

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
    public final MultiSet<T> subSet(T fromElement, T toElement) {
	return new TreeMultiSet<T>(this.obj2mult.subMap(fromElement, true, 
						    toElement, false));
    }

    /**
     * Returns a view of the underlying set of this <code>MultiSet</code>. 
     * For certain implementations, this set is immutable 
     * to prevent implicit modification of this <code>MultiSet</code>. 
     *
     * @return 
     *    the <code>Set</code> containing exactly the objects 
     *    with strictly positive multiplicity in this <code>MultiSet</code>. 
     * @see TreeMultiSet.Immutable#getSet()
     */
    public SortedSet<T> getSet() {
	return this.obj2mult.navigableKeySet();
    }

    /**
     * Returns a view of the underlying map of this <code>SortedMultiSet</code> 
     * as a map mapping each entry to its multiplicity. 
     */
     public NavigableMap<T, Multiplicity> getMap() {
	return this.obj2mult;
    }

    public String toString() {
	return "<MultiSet comparator=\"" + this.obj2mult.comparator() + 
	    "\">" + this.obj2mult + "</MultiSet>";
    }

}
