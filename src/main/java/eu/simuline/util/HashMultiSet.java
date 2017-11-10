
package eu.simuline.util;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Represents a set with multiplicities based on a {@link HashMap}. 
 * Mathematically this is something between a set and a family. 
 * Note that this kind of set does support <code>null</code> elements. 
 *
 * @param <T>
 *    the class of the elements of this multi-set. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public class HashMultiSet<T> 
    extends AbstractMultiSet<Map<T, MultiSet.Multiplicity>, T> 
    implements MultiSet<T> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Represents immutable <code>MultiSet</code>s 
     * as e.g. the one given by {@link HashMultiSet#emptyMultiSet()}. 
     * **** Idea: use {@link Collections#unmodifiableMap(Map)} 
     * but still modifications of multiplicities must be handled. 
     *
     * @param <T>
     *    the class of the elements of this multi-set. 
     */
    static final class Immutable<T> extends HashMultiSet<T> {

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
	Immutable(HashMultiSet<T> other) {
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
	public MultiSet<T> immutable() {
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
	public Set<T> getSet() {
	    return Collections.unmodifiableSet(super.getSet());
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


    private HashMultiSet(Map<T, Multiplicity> t2mult) {
	super(t2mult);
    }

    /**
     * Creates a new, empty <code>MultiSet</code>. 
     */
    public HashMultiSet() {
	this(new HashMap<T, Multiplicity>());
    }

    /**
     * Copy constructor. 
     *
     * @param other 
     *    another <code>MultiSet</code> instance. 
     */
    public HashMultiSet(MultiSet<? extends T> other) {
	this(new HashMap<T, Multiplicity>(other.getMap()));
    }

    /**
     * Creates a multi set with the elements of <code>sSet</code> 
     * and all elements with multiplicity <code>1</code>. 
     *
     * @param  sSet
     *    some instance of a sorted set. 
     */
    public HashMultiSet(Set<? extends T> sSet) {
	this();
	addAll(sSet);
    }

    /**
     * Returns an <em>immutable</em> empty <code>MultiSet</code>. 
     * @see HashMultiSet.Immutable
     */
    public static <T> MultiSet<T> emptyMultiSet() {
	return Immutable.createEmpty();
    }

    /**
     * Returns an immutable copy of this <code>MultiSet</code>. 
     */
    public MultiSet<T> immutable() {
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

    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

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
    public Set<T> getSet() {
	return this.obj2mult.keySet();
    }

    /**
     * Returns a view of the underlying map of this <code>MultiSet</code> 
     * as a map mapping each entry to its multiplicity. 
     */
     public Map<T, Multiplicity> getMap() {
	return this.obj2mult;
    }

    public String toString() {
	return "<MultiSet>" + this.obj2mult + "</MultiSet>";
    }

}
