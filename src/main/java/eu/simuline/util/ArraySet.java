
package eu.simuline.util;

import java.util.Set;
import java.util.SortedSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;// for javadoc only. 
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.AbstractSet;

/**
 * This class implements the Set interface, 
 * backed by a {@link java.util.ArrayList java.util.ArrayList}. 
 * The iteration order of the set 
 * is the iteration order of the underlying ArrayList. 
 * This class permits the null element. 
 * <p>
 * The size, isEmpty, and iterator operations run in constant time. 
 * The add operation runs in amortized constant time, that is, 
 * adding n elements requires O(n) time. 
 * All of the other operations run in linear time (roughly speaking). 
 * <p>
 * Each ArraySet instance has a capacity. 
 * The capacity is the size of the array used 
 * to store the elements in the list. 
 * It is always at least as large as the set size. 
 * As elements are added an ArraySet, its capacity grows automatically. 
 * The details of the growth policy are not specified 
 * beyond the fact that adding an element has constant amortized time cost. 
 * <p>
 * An application can increase the capacity of an ArraySet instance 
 * before adding a large number of elements 
 * using the {@link #ensureCapacity} operation. 
 * This may reduce the amount of incremental reallocation. 
 * <p>
 * <em>Note that this implementation is not synchronized. </em>
 * If multiple threads access a set concurrently, 
 * and at least one of the threads modifies the set, 
 * it must be synchronized externally. 
 * This is typically accomplished by synchronizing on some object 
 * that naturally encapsulates the set. 
 * If no such object exists, the set should be "wrapped" 
 * using the Collections.synchronizedSet method. 
 * This is best done at creation time, 
 * to prevent accidental unsynchronized access to the ArraySet instance: 
 * <ore>Set s = Collections.synchronizedSet(new ArraySet(...));</pre>
 * <p>
 * The iterators returned by this class's iterator method are fail-fast: 
 * if the set ismodified at any time after the iterator is created, 
 * in any way except through the iterator's own remove method, 
 * the Iterator throws a ConcurrentModificationException. 
 * Thus, in the face of concurrent modification, 
 * the iterator fails quickly and cleanly, rather than risking arbitrary, 
 * non-deterministic behavior at an undetermined time in the future. 
 *
 * @see java.util.Collection
 * @see java.util.List
 * @see java.util.ArrayList#add
 * @see java.util.SortedSet
 * @see java.util.HashSet
 * @see java.util.TreeSet
 * @see java.util.AbstractSet
 * @see java.util.Collections
 */
public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * Contains the elements of this set. 
     * By contract, <code>list.get(a).equals(list.get(b))</code> 
     * implies <code>a == b</code>. 
     */
    private ArrayList<E> list;// NOPMD

    /**
     * The comparator initialized in the constructor 
     * and returned by {@link #comparator}. 
     * This may well be <code>null</code> and is nowhere used directly. 
     */
    private Comparator<? super E> outerCmp;

    /**
     * If {@link #outerCmp} is not <code>null</code>, 
     * the two comparators coincide. 
     * Otherwise this comparator reflects the natural ordering 
     * and throws a <code>ClassCastException</code> 
     * if feeded with no appropriate <code>Comparable</code>. 
     */
    private Comparator<? super E> innerCmp;

    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Creates a new <code>ArraySet</code>. 
     */
    public static <E> ArraySet<E> sortedAsAdded() {
	ArraySet<E> res = new ArraySet<E>();
	res.innerCmp = res.outerCmp =  res.asListed();
	return res;
    }


    /*
     * Creates a new set containing the elements of the specified collection. 
     * The corresponding iterator returns the elements in the order 
     * they are returned by the collection's iterator. 
     * The backing ArrayList instance {@link #list} 
     * has an initial capacity of 110% the size of the specified collection. 
     *
     * @param coll 
     *    the collection whose elements are to be placed into this set. 
     */
    public ArraySet(Collection<? extends E> coll) {//,boolean isSortedAsAdded
	this((Comparator<? super E>)null);
	addAll(coll);// NOPMD 
	//this(new OrderingDesc<E>(coll,isSortedAsAdded));
    }
/*
    void test(Comparator<E> cmp) {
	this.innerCmp = new Comparator<E>() {
	    public int compare(E o1,E o2) {
		return ((Comparable<E>)o1).compareTo(o2);
	    }
	};
    }
*/
    public ArraySet(Comparator<? super E> cmp) {

	this.list = new ArrayList<E>();
	this.outerCmp = cmp;

	if (cmp == null) {
	    this.innerCmp = new Comparator<E>() {
		public int compare(E obj1,E obj2) {
		    return ((Comparable<E>)obj1).compareTo(obj2);
		}
	    };
	} else {
	    this.innerCmp = cmp;
	}
	
	/*
	this.innerCmp = (cmp == null)
	    ? new Comparator<E>() {
		    public int compare(E o1,E o2) {
			return ((Comparable<E>)o1).compareTo(o2);
		    }
		}
	    : cmp;
	*/
    }

    public ArraySet() {
	this((Comparator<? super E>)null);
    }

    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    public Comparators.AsListed<E> asListed() {
	return Comparators.getAsListed(getList());
    }

    public final List<E> getList() {
	return this.list;
    }

    /**
     * Returns the number of elements in this set (its cardinality).  
     * If thisset contains more than <tt>Integer.MAX_VALUE</tt> elements, 
     * returns <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this set (its cardinality).
     */
    public int size() {
	return this.list.size();
	//return containsNull ? this.list.size()+1 : this.list.size();
    }

    /**
     * Increases the capacity of this ArraySet instance, if necessary, 
     * to ensure that it can hold at least the number of elements 
     * specified by the minimum capacity argument. 
     *
     * @param minCapacity 
     *    the desired minimum capacity. 
     */
    public void ensureCapacity(int minCapacity) {
	this.list.ensureCapacity(minCapacity);
    }

    /**
     * Returns <tt>true</tt> if this set contains no elements. 
     *
     * @return 
     *    <tt>true</tt> if this set contains no elements.
     */
     public boolean isEmpty() {
	return this.list.isEmpty();
	//return !containsNull && this.list.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element. 
     * More formally, returns <tt>true</tt> if and only 
     * if this set contains an element <code>e</code> 
     * such that <code>(obj==null ? e==null : obj.equals(e))</code>. 
     *
     * @param obj 
     *    obj is some object that may be in this collection. 
     * @return 
     *    <tt>true</tt> if this set contains the specified element.
     */
     public boolean contains(Object obj) {
	 try {
	     return Collections.binarySearch(this.list,
					     (E)obj,
					     this.innerCmp) >= 0;
	 } catch (ClassCastException e) {
	     // Here, obj is no instance of E 
	     return false;
	 }
	 
	//return o == null : containsNull : this.list.contains(o);
    }

    /**
     * Returns an iterator over the elements in this set. 
     * The elements are returned in the particular order 
     * in which they are stored in {@link #list}.
     *
     * @return 
     *    an iterator over the elements in this set.
     */
     public Iterator<E> iterator() {
	return this.list.iterator();
	// more complicated with null not in list. 
    }

    /**
     * Returns an iterator over the elements in this set. 
     * The elements are returned in the particular order 
     * in which they are stored in {@link #list}.
     *
     * @return 
     *    an iterator over the elements in this set.
     */
     public ListIterator listIterator() {
	return this.list.listIterator();
	// more complicated with null not in list. 
    }

    /**
     * Returns an array containing all of the elements in this set; 
     * the order is as in the backing list {@link #list}. 
     * Obeys the general contract of the <tt>Collection.toArray</tt> method.
     *
     * @return 
     *    an array containing all of the elements in this set.
     */
     public Object[] toArray() {
	return this.list.toArray();
    }

    /**
     * Returns an array containing all of the elements in this set 
     * whose runtime type is that of the specified array; 
     * the order is as in the backing list {@link #list}. 
     * Obeys the general contract 
     * of the <tt>Collection.toArray(Object[])</tt> method.
     *
     * @param arr 
     *    the array into which the elements of this set are to be stored, 
     *    if it is big enough; 
     *    otherwise, a new array of the same runtime type is allocated 
     *    for this purpose. 
     * @return 
     *    an array containing the elements of this set.
     * @throws ArrayStoreException 
     *    the runtime type of a is not a supertype 
     *    of the runtime type of every element in this set.
     */ 
    public <T> T[] toArray(T[] arr) {
	return this.list.toArray(arr);
    }

    /*----------------------------------------------------------------------*/
    /* Modification Operations                                              */
    /*----------------------------------------------------------------------*/

    /**
     * Adds the specified element to this set if it is not already present. 
     * More formally, adds the specified element, <code>o</code>, 
     * to this set if this set contains no element <code>e</code> 
     * such that <code>(o==null ? e==null : o.equals(e))</code>. 
     * If this set already contains the specified element, 
     * the call leaves this set unchanged and returns <tt>false</tt>. 
     * In combination with the restriction on constructors, 
     * this ensures that sets never contain duplicate elements. 
     * <p>
     * Note that also <code>null</code> may be added to this set, 
     * but only if it is not already present, this changes this set. 
     *
     * @param obj 
     *    element to be added to this set. 
     * @return 
     *    <tt>true</tt> if this set did not already contain the specified
     *    element.
     */
    public final boolean add(E obj) {
	int index = Collections.binarySearch(this.list,obj,this.innerCmp);
	if (index >= 0) {
	    return false;
	}
	index = -index-1;

	this.list.add(index,obj);
	return true;
    }

    /**
     * Removes the specified element from this set if it is present. 
     * More formally, removes an element <code>e</code> 
     * such that <code>(o==null ?  e==null : o.equals(e))</code>, 
     * if the set contains such an element. 
     * Returns <tt>true</tt> if the set contained the specified element 
     * (or equivalently, if the set changed as a result of the call). 
     * (The set will not contain the specified element once the call returns.)
     *
     * @param obj 
     *    object to be removed from this set, if present. 
     * @return 
     *    <code>true</code> if the set contained the specified element. 
     */
    public boolean remove(Object obj) {
	return this.list.remove(obj);
    }

    /*----------------------------------------------------------------------*/
    /* Bulk Operations                                                      */
    /*----------------------------------------------------------------------*/

    /**
     * Returns <tt>true</tt> if this set contains all of the elements 
     * of the specified collection. 
     * If the specified collection is also a set, 
     * this method returns <tt>true</tt> 
     * if it is a <i>subset</i> of this set. 
     *
     * @param coll 
     *    collection to be checked for containment in this set.
     * @return 
     *    <tt>true</tt> if this set contains all of the elements 
     *    of the specified collection. 
     */
    public boolean containsAll(Collection<?> coll) {
	if (isSortedWithSameComparator(coll)) {
	    // ***** here more efficient code would be possible 
	    int min = 0;
	    int sup = this.list.size();
	    int index;
	    Iterator iter = coll.iterator();
	    E elem;
	    while (iter.hasNext()) {
		try { /// **** not so good implementation. 
		    elem = (E)iter.next();
		} catch (ClassCastException cce) {
		    return false;
		}
		
		
		index = Collections.binarySearch(this.list.subList(min,sup),
						 elem,
						 this.innerCmp);
		//index = this.cmp.search(this.list.subList(min,sup),o);
		if (index < 0) {
		    return false;
		}
		min = index+1;
	    }
	    return true;
	} // same comparator 
	Iterator iter = coll.iterator();
	while (iter.hasNext()) {
	    if (!contains(iter.next())) {
		return false;
	    }
	}
	
	return true;
    }

    /**
     * Returns whether this set has the same comparator as 
     * the collection given. 
     *
     * @param coll
     *    a collection which may be a <code>SortedSet</code> or not. 
     * @return
     *    <code>true</code> if <code>c</code> is a sorted set 
     *    and if <code>c.comparator</code> yields the same result: 
     *    <ul>
     *    <li>
     *    Either they coincide as pointers, 
     *    including the case that both are <code>null</code> 
     *    <li>
     *    or at least this comparator is not <code>null</code> 
     *    and equals the other one. 
     *    This case implies that neither are <code>null</code>. 
     *    </ul>
     */
    private final boolean isSortedWithSameComparator(Collection coll) {
	if (!(coll instanceof SortedSet)) {
	    return false;
	}
	SortedSet sSet = (SortedSet)coll;
	if (sSet.comparator() == this.comparator() || 
	    (this.comparator() != null && 
	     this.comparator().equals(sSet.comparator()))) {
	    return true;
	}
	return false;
    }

    /**
     * Adds all of the elements in the specified collection to this set 
     * if they're not already present. 
     * If the specified collection is also a set, 
     * the <tt>addAll</tt> operation effectively modifies this set so 
     * that its value is the <i>union</i> of the two sets. 
     * The behavior of this operation is unspecified if the specified
     * collection is modified while the operation is in progress. 
     *
     * @param coll 
     *    collection whose elements are to be added to this set. 
     * @return 
     *    <tt>true</tt> if this set changed as a result of the call. 
     * @see #add
     */
    public final boolean addAll(Collection<? extends E> coll) {
	if (isSortedWithSameComparator(coll)) {
	    List<E> cList;
   	    if (coll instanceof ArraySet) {
		cList = ((ArraySet)coll).getList();
	    } else {
		cList = new ArrayList<E>(coll);
	    }
	    return addList(this.list,cList);
	} else {
	    boolean modified = false;
	    for (E entry : coll) {
		modified |= add(entry);
	    }
	    return modified;
	}
    }

//    public final static int[] ab = new int[2];

    /**
     * Merges <code>list2</code> into <code>list1</code> 
     * and returns whether <code>l1</code> had been modified. 
     * At the end, <code>list2</code> is not modified 
     * and <code>list1</code> is ordered again. 
     *
     * @param list1
     *    a list which is required to be sorted according to {@link #innerCmp} 
     *    without duplicates. 
     * @param list2
     *    a list which is required to be sorted according to {@link #innerCmp} 
     *    without duplicates. 
     * @return
     *    whether <code>list1</code> had been modified. 
     */
    private final boolean addList(List<E> list1, List<E> list2) {
	
	switch (list2.size()) {// NOPMD
	    case  0:
		return false;
	    case  1:
		int index = Collections.binarySearch(list1,
						     list2.get(0),
						     this.innerCmp);
		if (index >= 0) {
		    return false;
		} else {
		    index = -1-index;
		    list1.add(index,list2.get(0));
		    return true;
		}
	    default:
		// fall through 
	} // end of switch 
	int index2 = list2.size()/2;
	int index1 = Collections
	    .binarySearch(list1,list2.get(index2),this.innerCmp);
	    
	if (index1 >= 0) {
	    boolean isModified;
	    int index1b = index1, 
		index2b = index2;

	    do {
		index1b++;
		index2b++;
	    } while (index1b < list1.size() &&
		     index2b < list2.size() &&
		     this.innerCmp.compare(list1.get(index1b),
					   list2.get(index2b)) == 0);
	    if (index1b == list1.size()) {
		list1.addAll(list2.subList(index2b,list2.size()));
		return  addList(list1.subList(0,index1),
				list2.subList(0,index2));
	    } else {
		isModified  = addList(list1.subList(index1b,list1.size()),
				      list2.subList(index2b,list2.size()));
		isModified |= addList(list1.subList(0,index1),
				      list2.subList(0,index2));
		return isModified;
 
	    } 
		
	    /*
	      isModified  = addList(l1.subList(index1b,l1.size()),
	      l2.subList(index2b,l2.size()));
	      isModified |= addList(l1.subList(0,index1),
	      l2.subList(0,index2));
	      return isModified;
	    */
	    /*
	      isModified  = addList(l1.subList(index1+1,l1.size()),
	      l2.subList(index2+1,l2.size()));
	      isModified |= addList(l1.subList(0,index1),
	      l2.subList(0,index2));
	      return isModified;
	    */
	} else {
	    index1 = -1-index1;
	    if (index1 < list1.size()) {

		int index2b = index2+1;
		E obj = list1.get(index1);
		Comparator<? super E> cmp = this.innerCmp;
		while (index2b < list2.size() &&
		       cmp.compare(obj,list2.get(index2b)) > 0) {
		    index2b++;
		}

		addList(list1.subList(index1 ,list1.size()),
			list2.subList(index2b,list2.size()));
		if (index2b > 1+index2) {
		    list1.addAll(index1,list2.subList(index2,index2b));
		} else {
		    list1.add(index1,list2.get(index2));
		}
		    
		    
		//l1.addAll(index1,l2.subList(index2,index2b));
//System.out.println("string:: "+(index2b-index2));
		    
		addList(list1.subList(0,index1),
			list2.subList(0,index2));
		return true;

	    } else {
		list1.addAll(list2.subList(index2,list2.size()));
		/*
		  addList(l1.subList(index1,l1.size()),
		  l2.subList(index2,l2.size()));
		*/
		addList(list1.subList(0,index1),
			list2.subList(0,index2));
		return true;

	    } // end of else
		

	    /*
	      addList(l1.subList(index1,l1.size()),
	      l2.subList(index2,l2.size()));
	      addList(l1.subList(0,index1),
	      l2.subList(0,index2));
	      return true;
	    */
	} // end of else
    }

    /**
     * Retains only the elements in this set 
     * that are contained in the specified collection. 
     * In other words, removes from this set all of its elements 
     * that are not contained in the specified collection. 
     * If the specified collection is also a set, 
     * this operation effectively modifies this set so 
     * that its value is the <i>intersection</i> of the two sets. 
     *
     * @param coll 
     *    collection that defines which elements this set will retain. 
     * @return 
     *    <tt>true</tt> if this collection changed as a result of the call. 
     * @see #remove
     */
    public boolean retainAll(Collection<?> coll) {
/*
	if (isSortedWithSameComparator(coll)) {
	    // ***** here more efficient code would be possible 
	}
*/
	// **** not ideal if this and coll are both sorted! 
	return this.list.retainAll(coll);
    }

    /**
     * Removes from this set all of its elements 
     * that are contained in the specified collection. 
     * If the specified collection is also a set, 
     * this operation effectively modifies this set so 
     * that its value is the <i>asymmetric set difference</i> of the two sets.
     *
     * @param coll 
     *    collection that defines 
     *    which elements will be removed from this set. 
     * @return 
     *    <tt>true</tt> if this set changed as a result of the call. 
     * @see #remove
     */
    public boolean removeAll(Collection<?> coll) {
	// **** not ideal if this and coll are both sorted! 
	return this.list.removeAll(coll);
    }

    /**
     * Removes all of the elements from this set. 
     * This set will be empty after this call returns. 
     */
    public void clear() {
	this.list.clear();
    }

    /*----------------------------------------------------------------------*/
    /* methods implementing SortedSet                                       */
    /*----------------------------------------------------------------------*/

    public Comparator<? super E> comparator() {
	return this.outerCmp;
    }

    public SortedSet<E> subSet(E fromObj, E toObj) {
	int fromI = this.list.indexOf(fromObj);
	if (fromI == -1) {
	    return new ArraySet<E>(this.outerCmp);
	}
	
	int toI = this.list.indexOf(toObj);
	if (toI == -1) {
	    toI = this.list.size();
	}
	
	ArraySet<E> result = new ArraySet<E>(this.outerCmp);
	result.addAll(this.list.subList(fromI,toI));
	return result;
    }

    public SortedSet<E> headSet(E toObj) {
	int toI = this.list.indexOf(toObj);
	if (toI == -1) {
	    toI = this.list.size();
	}
	ArraySet<E> result = new ArraySet<E>(this.outerCmp);
	result.addAll(this.list.subList(0,toI));
	return result;
    }

    public SortedSet<E> tailSet(E from) {
	int fromI = this.list.indexOf(from);
	if (fromI == -1) {
	    return new ArraySet<E>(this.outerCmp);
	}
	
	ArraySet<E> result = new ArraySet<E>(this.outerCmp);
	result.addAll(this.list.subList(fromI,this.list.size()));
	return result;
    }

    public E first() {
	try {
	     return this.list.get(0);
	} catch (IndexOutOfBoundsException e) {
	    throw new NoSuchElementException();// NOPMD
	}
    }

    public E last() {
	try {
	     return this.list.get(this.list.size()-1);
	} catch (IndexOutOfBoundsException e) {
	    throw new NoSuchElementException();// NOPMD
	}
    }

    /*----------------------------------------------------------------------*/
    /* Comparison representation and hashing                                */
    /*----------------------------------------------------------------------*/

    /**
     * Compares the specified object with this set for equality. 
     * Returns <tt>true</tt> if the specified object is also a set, 
     * the two sets have the same size, 
     * and every member of the specified set is contained in this set 
     * (or equivalently, every member of this set 
     * is contained in the specified set). 
     * This definition ensures that the equals method works properly 
     * across different implementations of the set interface.
     *
     * @param obj 
     *    Object to be compared for equality with this set. 
     * @return 
     *    <tt>true</tt> if the specified Object is equal to this set. 
     */
    public boolean equals(Object obj) {

	if (!(obj instanceof Set)) {
	    return false;
	}
	Set<?> other = (Set<?>)obj;

	if (this.size() != other.size()) {
	    return false;
	}
	// **** what if this is sorted? 
	return this.containsAll(other);
    }

    /**
     * Returns a string representation of this set. 
     *
     * @return 
     *    a comma separated list of <code>String</code>-representations 
     *    of the elements of this list 
     *    enclosed in triangle brackets. 
     */
    public String toString() {
	StringBuffer result = new StringBuffer();
	result.append('[');
	if (!this.isEmpty()) {
	    result.append(this.list.get(0).toString());
	    for (int i = 1; i < size(); i++) {
		result.append(", ");
		result.append(this.list.get(i));
	    }
	}
	
	result.append(']');
	return result.toString();
    }

    /**
     * Returns the hash code value for this set. 
     * The hash code of a set is defined to be the sum 
     * of the hash codes of the elements in the set, 
     * where the hashcode of a <tt>null</tt> element is defined to be zero. 
     * This ensures that <code>s1.equals(s2)</code> implies that 
     * <code>s1.hashCode()==s2.hashCode()</code> for any two sets 
     * <code>s1</code> and <code>s2</code>, as required by the general 
     * contract of the <tt>Object.hashCode</tt> method. 
     *
     * @return 
     *    the hash code value for this set. 
     * @see Object#hashCode()
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    public int hashCode() {
	int code = 0;
	Iterator iter = this.iterator();
	Object cand;
	while (iter.hasNext()) {
	    cand = iter.next();
	    code += cand == null ? 0 : cand.hashCode();
	}
	return code;
    }
}
