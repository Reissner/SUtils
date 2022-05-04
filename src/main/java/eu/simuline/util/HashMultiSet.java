
package eu.simuline.util;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

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
public final class HashMultiSet<T> 
    extends AbstractMultiSet<Map<T, MultiSet.Multiplicity>, T> {


    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
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
