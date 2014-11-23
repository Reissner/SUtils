
package eu.simuline.util;

import eu.simuline.util.MultiSet.Multiplicity;

import java.util.Map;
import java.util.Iterator;

/**
 * Describe class AbstractMultiSet here.
 *
 *
 * Created: Sun Nov 23 23:32:06 2014
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
public abstract class AbstractMultiSet<MAP extends Map<T, Multiplicity>, T> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Serves as a wrapper object for a multiplicity {@link #mult}. 
     * Unlike <code>int</code>s we have real <code>Object</code>s 
     * which can be stored in a map, e.g. {@link MultiSet#obj2mult} 
     * and unlike <code>Integer</code>s these objects are mutable. 
     */
    // **** this implementation is not optimal: 
    // better would be immutable multiplicities 
    // or just using Integers with checks moved towards enclosing class 
    public static class MyMultiplicity implements Multiplicity {

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
	private MyMultiplicity(int mult) {
	    set(mult);
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	protected static MyMultiplicity create(int mult) {
	    return new MyMultiplicity(mult);
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
	 *    if <code>this.mult + mult < 0</code> holds. 
	 * @throws IllegalStateException 
	 *    if <code>this.mult + mult == 0</code> holds. 
	 *    This cannot occur: if it does this is a bug within this class. 
	 */
	public int add(int mult) {
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
	    return this.get()-mult.get();
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
     * A canonical implementation of {@link MultiSetIterator} 
     * defining also the methods modifying the underlying {@link MultiSet}, 
     * namely {@link #remove()}, {@link #setMult(int)} 
     * and {@link #removeMult(int)}. 
     */
    protected static class MultiSetIteratorImpl<T> 
	implements MultiSetIterator<T> {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	/**
	 * An iterator on the entries of the map {@link MultiSet#obj2mult} 
	 * associating each element of the underlying {@link MultiSet} 
	 * with its multiplicity. 
	 */
	private final Iterator<Map.Entry<T,Multiplicity>> entrySetIter;

	/**
	 * The element returned last by invoking {@link #next()} 
	 * in the iterator {@link #entrySetIter} 
	 * or <code>null</code> if {@link #next()} has not yet been invoked 
	 * or the element returned by the last invocation of {@link #next()} 
	 * has been removed in the meantime 
	 * invoking a method of this iterator (instance). 
	 */
	private Map.Entry<T,Multiplicity> last;

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

	public boolean hasNext() {
	    return this.entrySetIter.hasNext();
	}

	/**
	 * Returns the next element in the iteration 
	 * and, as a side effect, sets {@link #last} 
	 * with the mapping of that element to its current multiplicity. 
	 */
	public T next() {
	    return (this.last = this.entrySetIter.next()).getKey();
	}

	/**
	 * Removes from the underlying {@link MultiSet} 
	 * the last element returned by {@link #next()}, 
	 * provided that element was not removed in the meantime 
	 * and this method is supported by this iterator. 
	 * As a side effect, sets {@link #last} to <code>null</code> 
	 * indicating that this element has been removed. 
	 */
	public void remove()  {
	    // throws IllegalStateException if no longer present 
	    this.entrySetIter.remove();
	    this.last = null;
	}

	/**
	 * Returns the current multiplicity of the element 
	 * last read by {@link #next()}, 
	 * provided that element was not removed in the meantime. 
	 */
	public int getMult() {
	    if (this.last == null) {
		// no message as for method remove() 
		throw new IllegalStateException();
	    }
	    assert this.last != null;
	    return this.last.getValue().get();
	}

	public int setMult(int setMult) {
	    if (this.last == null) {
		// no message as for method remove() 
		throw new IllegalStateException();
	    }
	    assert this.last != null;
	    if (setMult < 0) {
		throw new IllegalArgumentException
		    ("Expected non-negative multiplicity; found " + 
		     setMult + ". ");
	    }

	    if (setMult == 0) {
		int res = this.last.getValue().get();
		remove();
		return res;
	    }
	    assert setMult > 0;
	    // thus set(...) does not throw an exception. 
	    return this.last.getValue().set(setMult);
	}

	public int removeMult(int removeMult)  {
	    if (this.last == null) {
		// no message as for method remove() 
		throw new IllegalStateException();
	    }
	    assert this.last != null;
	    if (removeMult < 0) {
		throw new IllegalArgumentException
		    ("Expected non-negative multiplicity; found " + 
		     removeMult + ". ");
	    }

	    // return value is old multiplicity 
	    int oldMult = this.last.getValue().get();
	    if (removeMult == oldMult) {
		remove();
	    } else {
		// throws an IllegalArgumentException if resulting 
		this.last.getValue().add(-removeMult);
	    }

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

}
