package eu.simuline.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.BitSet;

/**
 * Describe class BitSetList here. 
 * E extends Integer which in turn is final. 
 * This means E is nothing but Integer. 
 *
 *
 * Created: Mon May 29 19:37:38 2006
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
public class BitSetList<E extends Integer>// & E super Integer> 
    extends AbstractList<Integer> 
    implements Cloneable {

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    public BitSet wrapped;
    private int size;
    //private int ones;// equals wrapped.cardinality() ****

    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Creates a new <code>BitSetList</code> instance.
     *
     */
    public BitSetList() {
	this(0,0);
    }

    /**
     * Creates a new <code>BitSetList</code> instance.
     *
     * @param coll
     *    a collection which may not contain <code>null</code> pointers. 
     * @throws NullPointerException
     *     if <code>coll</code> contains a <code>null</code> pointer. 
     * @throws IllegalArgumentException
     *     if <code>coll</code> contains an <code>Integer</code> object 
     *     other than <code>0</code> or <code>1</code>. 
     */
    public BitSetList(Collection<? extends Integer> coll) {
	this(coll.size()/2,0);
	this.addAll(coll);
    }

    public BitSetList(int initialCapacity,int size) {
	this.wrapped = new BitSet(initialCapacity);
	this.size = size;
    }


    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    /* -------------------------------------------------------------------- *
     * methods: conversion boolean integer                                  *
     * -------------------------------------------------------------------- */

    /**
     * Returns the C-representation 
     * of the given <code>boolean</code> as an <code>int</code>. 
     *
     * @param bool 
     *    a <code>boolean</code> value. 
     * @return 
     *    an <code>int</code> representation of <code>bool</code>: 
     *    <ul>
     *    <li> <code>1</code> if <code>bool == true </code>. 
     *    <li> <code>0</code> if <code>bool == false</code>. 
     *    </ul>
     * @see #int2bool
     */
    private static Integer bool2int(boolean bool) {
	return Integer.valueOf(bool ? 1 : 0);
    }

    /**
     * Converts the given C-style-representation of a <code>boolean</code>. 
     *
     * @return
     *    an <code>bool</code> representation of <code>num</code>: 
     *    <ul>
     *    <li> <code>true </code> if <code>num == 1</code>. 
     *    <li> <code>false</code> if <code>num == 0</code>. 
     *    </ul>
     * @throws IllegalArgumentException
     *    if <code>num</code> is neither <code>0</code> nor <code>0</code>. 
     * @see #bool2int
     */
    private static boolean int2bool(int num) {
	switch (num) {// NOPMD
	    case 0:
		return false;
	    case 1:
		return true;
	    default:
		throw new IllegalArgumentException
		    ("Expected 0 or 1 but found " + num + ". ");
	}
    }

    /* -------------------------------------------------------------------- *
     * methods: implementation specific                                     *
     * -------------------------------------------------------------------- */

    public int cardinality() {
	return this.wrapped.cardinality();
    }

    public int sizeInternally() {
	return this.wrapped.size();
    }

    /**
     *  Returns the "logical size" of this List: 
     * The index of the highest digit <code>1</code> in the List plus one. 
     * Returns zero if the List contains <code>0</code>'s only.
     *
     * @return
     *  the logical size of this List.
     */
    public int length1() {
	return this.wrapped.length();
    }

    public void setW(int index) {
	this.wrapped.set(index);
    }

    public Integer set(int index) {
	Integer ret = getW(index);
	this.wrapped.set(index);
	return ret;
    }

    public final void setW(final int index, final Integer integer) {
	switch (integer.intValue()) {
	    case 0:
		this.wrapped.clear(index);
		break;
	    case 1:
		this.wrapped.set(index);
		break;
	    default:
		throw new IllegalArgumentException();
	}
    }

/*
    public final Integer setW(final int index, final Integer integer) {
	Integer ret = getW(index);

	switch (integer.intValue()) {
	    case 0:
		this.wrapped.clear(index);
		break;
	    case 1:
		this.wrapped.set(index) ;
		break;
	    default:
		throw new IllegalArgumentException();
	}
	return ret;
    }
*/

    public Integer getW(int index) {
	return bool2int(this.wrapped.get(index));
    }

    /**
     * Describe <code>hashCode</code> method here.
     *
     * @return an <code>int</code> value
     */
    public final int hashCode() {
	int hashCode = 1;
	for (Integer cand : this) {
	    hashCode = 31*hashCode + cand;
	}
	return hashCode;
    }

    public boolean equals(Object other) {// NOPMD
	return super.equals(other);
    }

    /* -------------------------------------------------------------------- *
     * methods: implementations of List                                     *
     * -------------------------------------------------------------------- */

    /**
     * Describe <code>add</code> method here.
     *
     * @param integer an <code>Integer</code> value
     * @return a <code>boolean</code> value
     */
    public final boolean add(final Integer integer) {
	// throws a NullPointerException for integer == null as specified. 
	switch (integer.intValue()) {
	    case 0:
		// nothing to to
		break;
	    case 1:
		this.wrapped.set(size()) ;
		break;
	    default:
		throw new IllegalArgumentException();
	}
	this.size++;

	return true;
    }

    /**
     * Describe <code>add</code> method here.
     *
     * @param index an <code>int</code> value
     * @param integer an <code>Integer</code> value
     */
    public final void add(final int index, final Integer integer) {
	if (index <  0 || index > size()) {
	    throw new IndexOutOfBoundsException();
	}
	switch (integer.intValue()) {
	    case 0:
		// nothing to to
		break;
	    case 1:
		// nothing to to
		break;
	    default:
		throw new IllegalArgumentException();
	}
	this.size++;

	// **** not quite optimal ****
	for (int i = this.wrapped.length(); i > index; i--) {
	    this.wrapped.set(i,this.wrapped.get(i-1));
	}

	this.wrapped.set(index,int2bool(integer)) ;

    }

    /**
     * Describe <code>clear</code> method here.
     *
     */
    public final void clear() {
	this.wrapped.clear();
	this.size = 0;
    }

    /**
     * Describe <code>equals</code> method here.
     *
     * @param object an <code>Object</code> value
     * @return a <code>boolean</code> value
     */
/*
    public final boolean equals(final Object object) {
	return false;
    }
*/

    /**
     * Describe <code>contains</code> method here.
     *
     * @param object an <code>Object</code> value
     * @return a <code>boolean</code> value
     */
    public final boolean contains(final Object object) {
	// intended to throw ClassCastException 
	// intended to throw NullPointerException
	int cand = ((Integer)object).intValue();
	switch (cand) {
	    case 0:
		return this.wrapped.length() == this.wrapped.cardinality()
		    ? false : true;
	    case 1:
		return this.wrapped.length() == 0         
		    ? false : true;
	    default:
		// this may only contain 0 or 1 and thus not object. 
		return false;
	}
    }

    /**
     * Describe <code>indexOf</code> method here.
     *
     * @param object an <code>Object</code> value
     * @return an <code>int</code> value
     */
/*
    public final int indexOf(final Object object) {
	return 0;
    }
*/

    /**
     * Describe <code>lastIndexOf</code> method here.
     *
     * @param object an <code>Object</code> value
     * @return an <code>int</code> value
     */
/*
    public final int lastIndexOf(final Object object) {
	return 0;
    }
*/

    /**
     * Describe <code>addAll</code> method here.
     *
     * @param collection a <code>Collection</code> value
     * @return a <code>boolean</code> value
     */
/*
    public final boolean addAll(final Collection<? extends E> collection) {
	return false;
    }
*/

    /**
     * Describe <code>addAll</code> method here.
     *
     * @param n an <code>int</code> value
     * @param collection a <code>Collection</code> value
     * @return a <code>boolean</code> value
     */
/*
    public final boolean addAll(final int n, 
    final Collection<? extends E> collection) {
	return false;
    }
*/

    /**
     * Describe <code>get</code> method here.
     *
     * @param index an <code>int</code> value
     * @return an <code>Object</code> value
     */
    public final Integer get(final int index) {
	if (index < 0 || index >= size()) {
	    throw new IndexOutOfBoundsException();
	}
	return bool2int(this.wrapped.get(index));
    }

    /**
     * Describe <code>iterator</code> method here.
     *
     * @return an <code>Iterator</code> value
     */
/*
    public final Iterator<Integer> iterator() {
	return null;
    }
*/

    /**
     * Describe <code>size</code> method here.
     *
     * @return an <code>int</code> value
     */
    public final int size() {
	return this.size;
    }

    /**
     * Describe <code>toArray</code> method here.
     *
     * @return an <code>Object[]</code> value
     */
/*
    public final Integer[] toArray() {
	return null;
    }
*/

    /**
     * Describe <code>toArray</code> method here.
     *
     * @param objectArray an <code>Object[]</code> value
     * @return an <code>Object[]</code> value
     */
/*
    public final <E> E[] toArray(final E[] objectArray) {
	return null;
    }
*/

    /**
     * Describe <code>remove</code> method here.
     *
     * @param object an <code>Object</code> value
     * @return a <code>boolean</code> value
     */
    public final boolean remove(final Object object) {
	// intended to throw ClassCastException 
	// intended to throw NullPointerException
	int cand = ((Integer)object).intValue();

	switch (cand) {
	    case 0:
		if (this.wrapped.length() == this.wrapped.cardinality()) {
		    // contains no 1's
		    return false;
		}
		assert this.wrapped.length() > this.wrapped.cardinality();
		for (int i = 0; i < this.wrapped.length(); i++) {
		    if (!this.wrapped.get(i)) {
			// Here found a 0. 
			remove(i);
		    }
		}
		return true;
	    case 1:
		if (this.wrapped.length() == 0) {
		    // contains no 1's
		    return false;
		}
		this.wrapped.clear(this.wrapped.length()-1);
		return true;
	    default:
		// this may only contain 0 or 1 and thus not object. 
		return false;
	}
    }

    /**
     * Describe <code>remove</code> method here.
     *
     * @param index an <code>int</code> value
     * @return an <code>Object</code> value
     */
    public final Integer remove(final int index) {
	if (index < 0 || index >= size()) {
	    throw new IndexOutOfBoundsException();
	}
	Integer ret = bool2int(this.wrapped.get(index));
	// **** not quite optimal ****
	for (int i = index; i < this.wrapped.length()-1; i++) {
	    this.wrapped.set(i,this.wrapped.get(i+1));
	}

	this.wrapped.clear(this.wrapped.length()-1) ;
	return ret;
    }

    /**
     * Describe <code>isEmpty</code> method here.
     *  This implementation returns <code>size() == 0</code>. 
     *
     * @return a <code>boolean</code> value
     */
/*
    public final boolean isEmpty() {
	return false;
    }
*/

    /**
     * Describe <code>set</code> method here.
     *
     * @param index an <code>int</code> value
     * @param integer an <code>Object</code> value
     * @return an <code>Object</code> value
     */
    public final Integer set(final int index, final Integer integer) {
	// may throw IndexOutOfBoundsException 
	Integer ret = get(index);

	switch (integer.intValue()) {
	    case 0:
		this.wrapped.clear(index) ;
		break;
	    case 1:
		this.wrapped.set(index) ;
		break;
	    default:
		throw new IllegalArgumentException();
	}
	return ret;
    }

    /*
     * Describe <code>containsAll</code> method here.
     *
     * @param collection a <code>Collection</code> value
     * @return a <code>boolean</code> value
     */
/*
    public final boolean containsAll(final Collection<?> collection) {
	return false;
    }
*/

    /*
     * Describe <code>removeAll</code> method here.
     *
     * @param collection a <code>Collection</code> value
     * @return a <code>boolean</code> value
     */
/*
    public final boolean removeAll(final Collection<?> collection) {
	return false;
    }
*/

    /*
     * Describe <code>retainAll</code> method here.
     *
     * @param collection a <code>Collection</code> value
     * @return a <code>boolean</code> value
     */
/*
    public final boolean retainAll(final Collection<?> collection) {
	return false;
    }
*/

    /*
     * Describe <code>subList</code> method here.
     *
     * @param n an <code>int</code> value
     * @param n1 an <code>int</code> value
     * @return a <code>List</code> value
     */
/*
    public final List<Integer> subList(final int n, final int n1) {
	return null;
    }
*/

    /*
     * Describe <code>listIterator</code> method here.
     *
     * @return a <code>ListIterator</code> value
     */
/*
    public final ListIterator<Integer> listIterator() {
	return null;
    }
*/

    /*
     * Describe <code>listIterator</code> method here.
     *
     * @param n an <code>int</code> value
     * @return a <code>ListIterator</code> value
     */
/*
    public final ListIterator<Integer> listIterator(final int n) {
	return null;
    }
*/


    protected Object clone() throws CloneNotSupportedException {
	BitSetList res = (BitSetList)super.clone();
	res.wrapped    = (BitSet    )this .clone();
	return res;
    }


}
