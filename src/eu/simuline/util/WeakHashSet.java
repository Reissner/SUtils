package eu.simuline.util;

import java.util.Set;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;

/**
 * A <code>HashSet</code> using <code>WeakReference</code>s. 
 * The implementation is based on <code>WeakHashMap</code>. 
 *
 *
 * Created: Sun Apr 15 00:09:53 2007
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
public class WeakHashSet<E> implements Set<E> {

    static final long serialVersionUID = -5024744406713321676L;

    private final Map<E,E> map;

    /**
     * Creates a new empty <code>WeakHashSet</code> 
     * with the default initial capacity (16) 
     * and the default load factor (0.75). 
     *
     * @see WeakHashSet(int initialCapacity, float loadFactor)
     */
    public WeakHashSet() {
	this.map = new WeakHashMap<E,E>();
    }

    /**
     * Constructs a new, empty <tt>WeakHashSet</tt> 
     * with the given initial capacity 
     * and the default load factor, which is <tt>0.75</tt>. 
     * **** depends on foreign class <tt>WeakHashMap</tt>
     *
     * @param  initialCapacity 
     *    The initial capacity of the <tt>WeakHashSet</tt>
     * @throws IllegalArgumentException 
     *    If the initial capacity is negative.
     */
    public WeakHashSet(int initialCapacity) {
	this.map = new WeakHashMap<E,E>(initialCapacity);
    }

    /**
     * Constructs a new, empty <tt>WeakHashSet</tt> 
     * with the given initial capacity and the given load factor.
     *
     * @param  initialCapacity 
     *    The initial capacity of the <tt>WeakHashMap</tt> 
     * @param  loadFactor 
     *    The load factor of the <tt>WeakHashMap</tt> 
     * @throws IllegalArgumentException  
     *    If the initial capacity is negative, 
     *    or if the load factor is nonpositive.
     */
    public WeakHashSet(int initialCapacity, float loadFactor) {
	this.map = new WeakHashMap<E,E>(initialCapacity, loadFactor);
    }

    /**
     * Constructs a new <tt>WeakHashSet</tt> 
     * with the same mappings as the specified <tt>WeakHashSet</tt>.  
     * The <tt>WeakHashSet</tt> is created with default load factor, 
     * which is <tt>0.75</tt> and an initial capacity 
     * sufficient to hold the mappings in the specified <tt></tt>. 
     *
     * @param   other  
     *    the <tt>WeakHashSet</tt> whose entries are to be placed in this one.
     * @throws  NullPointerException  
     *    if the specified <tt>WeakHashSet</tt> is null.
     */
    public WeakHashSet(WeakHashSet<? extends E> other) {
	this.map = new WeakHashMap<E,E>(other.map);
    }

    /**
     * Constructs a new <tt>WeakHashSet</tt> 
     * with the same mappings as the specified <tt>Collection</tt>.  
     * The <tt>WeakHashSet</tt> is created with default load factor, 
     * which is <tt>0.75</tt> and an initial capacity 
     * sufficient to hold the mappings in the specified <tt>WeakHashSet</tt>. 
     *
     * @param   other  
     *    the <tt>Collection</tt> 
     *    whose mappings are to be placed in this <tt>WeakHashSet</tt>.
     * @throws  NullPointerException  
     *    if the specified <tt>Collection</tt> is null.
     */
    public WeakHashSet(Collection<? extends E> other) {
	this.map = new WeakHashMap<E,E>();
	for (E cand : other) {
	    this.add(cand);
	}
    }



    /**
     * Returns the number of elements in this set (its cardinality).
     *
     * @return the number of elements in this set (its cardinality).
     */
    public int size() {
	return this.map.size();
    }

    /**
     * Returns <tt>true</tt> if this set contains no elements.
     *
     * @return <tt>true</tt> if this set contains no elements.
     */
    public boolean isEmpty() {
	return this.map.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     *
     * @param obj element whose presence in this set is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
     */
    public boolean contains(Object obj) {
	return this.map.containsKey(obj);
    }

    /**
     * Returns an iterator over the elements in this set.  
     * The elements are returned in no particular order.
     *
     * @return an Iterator over the elements in this set.
     * @see ConcurrentModificationException
     */
    public Iterator<E> iterator() {
	return this.map.keySet().iterator();
    }

    /**
     * Returns an array containing all of the elements in this collection.  
     * If the collection makes any guarantees as to what order its elements are
     * returned by its iterator, this method must return the elements in the
     * same order.  
     * The returned array will be "safe" in that no references to
     * it are maintained by the collection.  
     * (In other words, this method must allocate a new array 
     * even if the collection is backed by an Array). 
     * The caller is thus free to modify the returned array. 
     * <p>
     * This implementation allocates the array to be returned, 
     * and iterates over the elements in the collection, 
     * storing each object reference 
     * in the next consecutive element of the array, starting with element 0.
     *
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray() {
	return this.map.keySet().toArray();
    }

    public <T> T[] toArray(T[] a) {
	return this.map.keySet().toArray(a);
    }


    /**
     * Adds the specified element to this set if it is not already
     * present.
     *
     * @param obj element to be added to this set.
     * @return <tt>true</tt> if the set did not already contain the specified
     * element.
     */
    public boolean add(E obj) {
	if (obj == null) {
	    throw new NullPointerException();
	}

	return this.map.put(obj,obj) == null;
    }

    /**
     * Removes the specified element from this set if it is present.
     *
     * @param obj object to be removed from this set, if present.
     * @return <tt>true</tt> if the set contained the specified element.
     */
    public boolean remove(Object obj) {
	return this.map.remove(obj) != null;
    }

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements
     * in the specified collection. 
     * <p>
     * This implementation iterates over the specified collection, 
     * checking each element returned by the iterator in turn to see 
     * if it's contained in this collection.  
     * If all elements are so contained <tt>true</tt> is returned, 
     * otherwise <tt>false</tt>. 
     *
     * @param coll collection to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains all of the elements
     * 	       in the specified collection.
     * @throws NullPointerException if the specified collection is null.
     * 
     * @see #contains(Object)
     */
    public boolean containsAll(Collection<?> coll) {
	return this.map.keySet().containsAll(coll);
    }

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation).  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in
     * progress.  (This implies that the behavior of this call is undefined if
     * the specified collection is this collection, and this collection is
     * nonempty.) <p>
     *
     * This implementation iterates over the specified collection, and adds
     * each object returned by the iterator to this collection, in turn.<p>
     *
     * Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> unless <tt>add</tt> is
     * overridden (assuming the specified collection is non-empty).
     *
     * @param coll 
     *    collection whose elements are to be added to this collection.
     * @return 
     *    <tt>true</tt> if this collection changed as a result of the call.
     * @throws UnsupportedOperationException 
     *    if this collection does not support the <tt>addAll</tt> method.
     * @throws NullPointerException 
     *    if the specified collection is null.
     * 
     * @see #add(Object)
     */
    public boolean addAll(Collection<? extends E> coll) {
	// **** no idea why addAll is not supported by the set Map.entries()
	boolean result = false;
	for (E cand : coll) {
	    result |= this.add(cand);
	}
	return result;
    }

    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).  In other words, removes
     * from this collection all of its elements that are not contained in the
     * specified collection. <p>
     *
     * This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified collection.  If it's not so contained, it's removed
     * from this collection with the iterator's <tt>remove</tt> method.<p>
     *
     * Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the iterator returned by the
     * <tt>iterator</tt> method does not implement the <tt>remove</tt> method
     * and this collection contains one or more elements not present in the
     * specified collection.
     *
     * @param coll elements to be retained in this collection.
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call.
     * @throws UnsupportedOperationException if the <tt>retainAll</tt> method
     * 	       is not supported by this Collection.
     * @throws NullPointerException if the specified collection is null.
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection<?> coll) {
	return this.map.keySet().retainAll(coll);
    }

    /**
     * Removes from this collection all of its elements that are contained in
     * the specified collection (optional operation). <p>
     *
     * This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified collection.  If it's so contained, it's removed from
     * this collection with the iterator's <tt>remove</tt> method.<p>
     *
     * Note that this implementation will throw an
     * <tt>UnsupportedOperationException</tt> if the iterator returned by the
     * <tt>iterator</tt> method does not implement the <tt>remove</tt> method
     * and this collection contains one or more elements in common with the
     * specified collection.
     *
     * @param coll elements to be removed from this collection.
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call.
     * @throws UnsupportedOperationException if the <tt>removeAll</tt> method
     * 	       is not supported by this collection.
     * @throws NullPointerException if the specified collection is null.
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
     public boolean removeAll(Collection<?> coll) {
	return this.map.keySet().removeAll(coll);
    }

    /**
     * Removes all of the elements from this collection.
     * The collection will be empty after this call returns. 
     */
    public void clear() {
	this.map.clear();
    }

    public boolean equals(Object obj) {
	if (!(obj instanceof Set)) {
	    return false;
	}
	Set other = (Set)obj;
	if (other.size() != this.size()) {
	    return false;
	}
	try {
	    return this.containsAll(other);
	} catch (ClassCastException e) {
	    return false;
	} catch (NullPointerException e) {
	    return false;
	}
    }

    /**
     * Returns the hash code value for this set.  
     * The hash code of a set is defined 
     * to be the sum of the hash codes of the elements in the set. 
     * This ensures that <tt>s1.equals(s2)</tt> implies that 
     * <tt>s1.hashCode()==s2.hashCode()</tt> for any two sets <tt>s1</tt>
     * and <tt>s2</tt>, as required by the general contract of
     * Object.hashCode. 
     *
     * @return the hash code value for this set.
     */
    public int hashCode() {
	return this.map.hashCode();
    }

    public String toString() {
	StringBuilder buf = new StringBuilder();
	buf.append("[");
	Iterator iter = this.iterator();
	if (iter.hasNext()) {
	    buf.append(iter.next());
	    while (iter.hasNext()) {
		buf.append(", "+iter.next());
	    }
	}

	buf.append("]");
	return buf.toString();
    }

    public static void main(String[] args) {
	Set<Integer> set = new HashSet<Integer>();
	set.add(1);
	System.out.println("set"+ set);
	
    }

}
