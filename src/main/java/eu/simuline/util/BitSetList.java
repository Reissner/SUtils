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
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public final class BitSetList extends AbstractList<Integer> 
    implements Cloneable {

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    private BitSet wrapped;
    private int size;
    //private List<Integer> test;
    //private int ones;// equals wrapped.cardinality() ****

    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Creates a new <code>BitSetList</code> instance.
     *
     */
    public BitSetList() {
	this(0);
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
	this(coll.size() / 2);
	this.addAll(coll);
	//assert this.test.equals(this);
    }

    public BitSetList(int initialCapacity) {
	this.wrapped = new BitSet(initialCapacity);
	this.size = 0;
	//this.test = new ArrayList<Integer>(initialCapacity);
	//assert this.test.equals(this);
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
	return bool ? 1 : 0;
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
     *    if <code>num</code> is neither <code>0</code> nor <code>1</code>. 
     * @see #bool2int
     */
    private static boolean int2bool(int num) {
	assert num == 0 || num == 1;
	return num == 0 ? false : true;
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

/*
    public Integer set(int index) {
	Integer ret = getW(index);
	this.wrapped.set(index);
	return ret;
    }
*/

/*
    public void setW(int index, Integer integer) {
	this.wrapped.set(index,int2bool(integer));
    }
*/
/*
    public Integer setW(int index, Integer integer) {
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

/*

    public Integer getW(int index) {
	return bool2int(this.wrapped.get(index));
    }
*/
    /**
     * Describe <code>hashCode</code> method here.
     *
     * @return an <code>int</code> value
     */
    public int hashCode() {
	int hashCode = 1;
	for (Integer cand : this) {
	    hashCode = 31 * hashCode + cand;
	}
	return hashCode;
    }


    public boolean equals(Object other) { // NOPMD
	return super.equals(other);
    }


/*
    public boolean equals(Object o) {
	if (o == this)
	    return true;
	if (!(o instanceof List))
	    return false;

	ListIterator<Integer> e1 = listIterator();
	ListIterator e2 = ((List) o).listIterator();
	while(e1.hasNext() && e2.hasNext()) {
	    Integer o1 = e1.next();
	    Object o2 = e2.next();
	    if (!(o1==null ? o2==null : o1.equals(o2)))
		return false;
	}
	return !(e1.hasNext() || e2.hasNext());
    }
*/


    /* -------------------------------------------------------------------- *
     * methods: implementations of List                                     *
     * -------------------------------------------------------------------- */

    /**
     * Describe <code>add</code> method here.
     *
     * @param integer an <code>Integer</code> value
     * @return a <code>boolean</code> value
     */
/*
    public boolean add(Integer integer) {
	// throws a NullPointerException for integer == null as specified. 
// System.out.println("1this.test"+this.test);
// System.out.println("1this.wrapped"+this.wrapped);
// System.out.println("1this.test.size()"+this.test.size());
// System.out.println("1this.size()"+this.size());
// System.out.println("integer"+integer);
	assert this.test.equals(this);


// 	this.wrapped.set(size(),int2bool(integer));

// 	switch (integer.intValue()) {
// 	    case 0:
// 		// nothing to to
// 		break;
// 	    case 1:
// 		this.wrapped.set(size()) ;
// 		break;
// 	    default:
// 		throw new IllegalArgumentException();
// 	}

	this.size++;

	assert this.test.add(integer);
	assert this.test.size() == this.size;
// System.out.println("2this.test"+this.test);
// System.out.println("2this.wrapped"+this.wrapped);
	assert this.test.equals(this);
	return true;
    }
*/

    /**
     * Describe <code>add</code> method here.
     *
     * @param index an <code>int</code> value
     * @param integer an <code>Integer</code> value
     */
    public void add(final int index, final Integer integer) {
//	assert this.test.size() == this.size;

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

//  	this.test.add(index,integer);

	// **** not quite optimal ****
	for (int i = size(); i > index; i--) {
	    this.wrapped.set(i, this.wrapped.get(i - 1));
	}

	this.wrapped.set(index, int2bool(integer));

// 	assert this.test.equals(this);
    }

    /**
     * Describe <code>clear</code> method here.
     *
     */
    public void clear() {
	this.wrapped.clear();
	this.size = 0;

// 	this.test.clear();
// 	assert this.test.equals(this);
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
    public boolean contains(final Object object) {
	int cand = ((Integer) object).intValue();
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
    public int indexOf(final Object object) {
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
    public int lastIndexOf(final Object object) {
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
    public boolean addAll(final Collection<? extends E> collection) {
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
    public boolean addAll(final int n, 
    Collection<? extends E> collection) {
	return false;
    }
*/

    /**
     * Describe <code>get</code> method here.
     *
     * @param index an <code>int</code> value
     * @return an <code>Object</code> value
     */
    public Integer get(final int index) {
// 	assert this.test.size() == this.size;
	if (index < 0 || index >= size()) {
	    throw new IndexOutOfBoundsException();
	}

	int ret = bool2int(this.wrapped.get(index));
/*
System.out.println("this.test"+this.test);
System.out.println("this"+this.wrapped);
System.out.println("index"+index);
System.out.println("this.test.get(index)"+this.test.get(index));
System.out.println("ret"+ret);
*/
//assert ret == this.test.get(index);
	//assert this.test.equals(this);
	return ret; // NOPMD
    }

    /**
     * Describe <code>iterator</code> method here.
     *
     * @return an <code>Iterator</code> value
     */
/*
    public Iterator<Integer> iterator() {
	return null;
    }
*/

    /**
     * Describe <code>size</code> method here.
     *
     * @return an <code>int</code> value
     */
    public int size() {
// 	assert this.size == this.test.size();
	return this.size;
    }

    /**
     * Describe <code>toArray</code> method here.
     *
     * @return an <code>Object[]</code> value
     */
/*
    public Integer[] toArray() {
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
    public <E> E[] toArray(final E[] objectArray) {
	return null;
    }
*/

    /**
     * Describe <code>remove</code> method here.
     *
     * @param object 
     *    the <code>Object</code> to be removed, if present. 
     * @return 
     *    the <code>boolean</code> value <code>true</code> 
     *    if this list contained the specified element. 
     */
    public boolean remove(final Object object) {

	int cand = ((Integer) object).intValue();

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
// 	assert this.test.equals(this);
		return true;
	    case 1:
		if (this.wrapped.length() == 0) {
		    // contains no 1's
		    return false;
		}
		this.wrapped.clear(this.wrapped.length() - 1);
// 	assert this.test.equals(this);
		return true;
	    default:
		// this may only contain 0 or 1 and thus not object. 
// 	assert this.test.equals(this);
		return false;
	}
    }

    /**
     * Describe <code>remove</code> method here.
     *
     * @param index an <code>int</code> value
     * @return an <code>Object</code> value
     */
    public Integer remove(final int index) {
	if (index < 0 || index >= size()) {
	    throw new IndexOutOfBoundsException();
	}
	Integer ret = bool2int(this.wrapped.get(index));
	// **** not quite optimal ****
	for (int i = index; i < size() - 1; i++) {
	    this.wrapped.set(i, this.wrapped.get(i + 1));
	}

//	this.wrapped.clear(this.wrapped.length()-1);
	this.size--;

// 	this.test.remove(index);
// 	assert this.test.equals(this);

	return ret;
    }

    /**
     * Describe <code>isEmpty</code> method here.
     *  This implementation returns <code>size() == 0</code>. 
     *
     * @return a <code>boolean</code> value
     */
/*
    public boolean isEmpty() {
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
    public Integer set(final int index, final Integer integer) {
/*
	if (0 != System.currentTimeMillis()) {
	    throw new NullPointerException();
	}
*/
	// may throw IndexOutOfBoundsException 
	Integer ret = get(index);
	this.wrapped.set(index, int2bool(integer));


// 	assert ret == this.test.set(index,integer);
// 	assert this.test.equals(this);
/*
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
*/
	return ret;
    }

    /*
     * Describe <code>containsAll</code> method here.
     *
     * @param collection a <code>Collection</code> value
     * @return a <code>boolean</code> value
     */
/*
    public boolean containsAll(final Collection<?> collection) {
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
    public boolean removeAll(final Collection<?> collection) {
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
    public boolean retainAll(final Collection<?> collection) {
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
    public List<Integer> subList(final int n, final int n1) {
	return null;
    }
*/

    /*
     * Describe <code>listIterator</code> method here.
     *
     * @return a <code>ListIterator</code> value
     */
/*
    public ListIterator<Integer> listIterator() {
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
    public ListIterator<Integer> listIterator(final int n) {
	return null;
    }
*/


    public BitSetList clone() throws CloneNotSupportedException {
	BitSetList res = (BitSetList) super       .clone();
	res.wrapped    = (BitSet    ) this.wrapped.clone();

// 	res.test = (List<Integer>)((ArrayList<Integer>)this.test).clone();
 	assert res.size == this.size;
	return res;
    }


}
