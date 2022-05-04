package eu.simuline.util;

import java.lang.reflect.Array;

import java.util.List;
import java.util.Iterator;
import java.util.Spliterator; // for javadoc only 
import java.util.ListIterator;
import java.util.Collections;
import java.util.Collection;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.Comparator;
import java.util.WeakHashMap;
import java.util.EnumSet;
import java.util.Map;

import java.util.stream.Stream; // for javadoc only 

import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.NavigableMap;

/**
 * An add on of the core class {@link java.util.Collections}. 
 * This class provides various kinds of immutable collections, 
 * where immutability is configurable 
 * via a {@link CollectionsExt.Modification} object. 
 * <p>
 * Also this class yields weak hash sets via {@link #weakHashSet()}. 
 * Moreover, there are methods to convert arrays and lists in one another 
 * also recursively. 
 * Finally, there are methods {@link #getUnique(Collection)} 
 * to retrieve the unique element and {@link #reverse(List)}
 * reverses a list. 
 *
 * @param <E>
 *    the class of the elements of collections under consideration. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public abstract class CollectionsExt<E> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Enumerates the kinds of modifications on a {@link Collection}. 
     * A collection is modified either directly or via its iterator's 
     * method {@link Iterator#remove()}. 
     * The direct modifications split in adding and removing a single object, 
     * clearing the whole collection 
     * and bulk operations like adding, removing, retaining 
     * all objects which are in another {@link Collection}. 
     * Subinterfaces of {@link Collection} 
     * may offer further kinds of modifications: 
     * {@link SortedSet}s for example define views on the original set 
     * 
     */
    public enum Modification {
	/**
	 * Modification adding an element 
	 * which may be by {@link Collection#add(Object)}, 
	 * {@link Collection#addAll(Collection)},  
	 * by {@link List#add(int, Object)}, 
	 * {@link List#addAll(int, Collection)}, 
	 * <p>
. 	 * Modification by {@link ListIterator#add(Object)} 
	 * of (list)-iterator attached with underlying {@link List} 
	 * via {@link List#listIterator()} or {@link List#listIterator(int)}. 
	 */
	AddObj, 

	/**
	 * Modification by {@link Collection#remove(Object)}, 
	 * {@link Collection#removeAll(Collection)}, 
	 * {@link Collection#retainAll(Collection)}, 
	 * {@link Collection#removeIf(Predicate)}, 
	 * {@link Collection#clear()} 
	 * and by {@link List#remove(int)}. 
	 * <p>
	 * Modification by {@link Iterator#remove()} 
	 * of iterator attached with underlying {@link Collection} 
	 * via {@link Collection#iterator()}, {@link List#listIterator()} 
	 * or {@link List#listIterator(int)}. 
	 */
	RemoveObj, 

	/**
	 * Modification by {@link List#set(int, Object)}, 
	 * {@link List#replaceAll(UnaryOperator)} and 
	 * {@link List#sort(Comparator)}. 
	 * <p>
	 * Modification by {@link ListIterator#set(Object)} 
	 * of iterator attached with underlying {@link List} 
	 * via {@link List#listIterator()} or {@link List#listIterator(int)}. 
	 */
	SetObj;
    } // enum Modification 

    /**
     * A class of {@link Collection}s of elements of class <code>E</code>
     * extending <code>C</code> 
     * initially throwing an <code>UnsupportedOperationException</code> 
     * when trying to modify the collection 
     * either directly or via its iterator(s) 
     * or via a transparent view 
     * like {@link List#subList(int, int)}. 
     * Note that neither arrays given e.g. by {@link Collection#toArray()} 
     * nor {@link Spliterator}s given by {@link Collection#spliterator()} 
     * nor {@link Stream}s given by {@link Collection#parallelStream()} 
     * give no rise to modifications of the underlying collection. 
     * **** currently, replacing (done for lists, e.g.), 
     * is not considered a modification but the objects are transformed... 
     * very esotheric... 
     * **** what must be done is overwrite the default implementation. 
     * Since it is not clear whether the wrapped collection did also overwrite, 
     * it is not clear which kind of operation is done. 
     * Thus it seems appropriate to define another kind of modification: 
     * SetAll

     * New releases of the jdk may add default methods to interfaces 
     * and may thus add ways to modify a list. 
     * The problem is not so much a default implementation of the interface 
     * since this is based on elementary methods of that interface 
     * but if a class overwrites this 
     * using internal methods or direct access to fields. 
     * In particular, a each default method must be reimplemented: 
     * E.g. {@link Collection#removeIf(Predicate)} 
     * uses method {@link Collection#remove(Object)} 
     * but it is possible to implement {@link Collection#removeIf(Predicate)} 
     * without invoking {@link Collection#remove(Object)} 
     * in implementations of implementing classes of {@link Collection}. 
     * 
     * it is possible, although not likely, 
     * that 


     * <p>
     * Instances of this class can be created via creator methods like 
     * {@link #getImmutableCollection(Collection)}. 
     * The collection with the original restrictions can be regained 
     * by {@link #unrestricted()}. 
     * A posteriory modifications can be allowed 
     * using {@link #allowModification(CollectionsExt.Modification)} or 
     * using {@link #allowModifications(Set)}. 
     * This class is <code>public</code> 
     * and {@link #getImmutableCollection(Collection)} 
     * returns instances of this class, to allow invoking methods 
     * {@link #allowModification(CollectionsExt.Modification)} and 
     * {@link #allowModifications(Set)}. 
     * <p>
     * Methods defining non-modifying access, 
     * are just delegated to the collection 
     * returned by {@link #unrestricted()}, 
     * whereas methods for modification are blocked 
     * if the modification is not allowed according to 
     * {@link #allowedModifications()}. 
     *
     * @param <C>
     *    the class extending {@link Collection} with elements in E. 
     * @param <E>
     *    the class of the elements of this collection. 
     */
    public abstract static 
	class AbstractImmutableCollection<C extends Collection<E>, E> 
	extends AbstractCollection<E> {

    	//private static final long serialVersionUID = -2479143000061671589L;

    	/* ---------------------------------------------------------------- *
    	 * fields.                                                          *
    	 * ---------------------------------------------------------------- */

	/**
	 * The set of allowed modifications. 
	 */
    	private final Set<Modification> mods;

    	/* ---------------------------------------------------------------- *
    	 * constructors.                                                    *
    	 * ---------------------------------------------------------------- */

	/**
	 * Creates an empty collection with no allowed modifications. 
	 *
	 * @see #AbstractImmutableCollection(Set)
	 */
    	private AbstractImmutableCollection() {
   	    this(EnumSet.noneOf(Modification.class));
    	}

	/**
	 * Creates an empty collection with the allowed modifications 
	 * given by <code>mods</code>. 
	 * CAUTION: This is not a collection containing <code>mods</code>. 
	 *
	 * @param mods
	 *    The modifications currently allowed. 
	 */
    	AbstractImmutableCollection(Set<Modification> mods) {
   	    this.mods = mods;
    	}

    	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	/**
	 * Allows in addition modification <code>mod</code> 
	 * for this set, does not alter contents and returns <code>this</code>. 
	 *
	 * @param mod
	 *    A modification which shall be allowed in addition. 
	 */
    	public final AbstractImmutableCollection<C, E> 
	    allowModification(Modification mod) {
    	    this.mods.add(mod);
    	    return this;
    	}

	/**
	 * Allows in addition modifications in <code>mods</code> 
	 * for this set, does not alter contents and returns <code>this</code>. 
	 *
	 * @param mods
	 *    modifications which shall be allowed in addition. 
	 */
    	public final void allowModifications(Set<Modification> mods) {
    	    this.mods.addAll(mods);
    	}

	// This method allows handling modifications more tightly 
	// so that the other methods are mere convenience methods. 
	/**
	 * Returns the set of allowed modification of this set 
	 * without backup: Changing the returned set 
	 * changes the allowed modifications for this set. 
	 * Also applying {@link #allowModifications(Set)} 
	 * modifies the returned set. 
	 *
	 * @return
	 *    the set of currently allowed modifications. 
	 */
    	public final Set<Modification> allowedModifications() {
    	    return this.mods;
    	}

	/**
	 * Returns the underlying set without the restrictions 
	 * imposed by this {@link CollectionsExt.ImmutableCollection}. 
	 * Note that the result 
	 * may still throw {@link UnsupportedOperationException}s 
	 * depending on the implementation. 
	 */
	public abstract C unrestricted();

	// from iterable 
	// forEach 

	// query operations 

	// iterator comes below 

	/**
	 * Returns the size of this collection. 
	 */
	public final int size() {
	    return unrestricted().size();
	}

	// inherited from AbstractCollection: 
	// isEmpty() 
	// boolean contains(Object o) 
	// Object[] toArray() 
	// T[] toArray(T[] a) 


	// Modification Operations

     	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#AddObj AddObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to add an object 
	 *    which is the case only <code>this</code> 
	 *    does not already contain <code>obj</code>. 
    	 */
	// overwrites method that throws UnsupportedOperationException
    	public final boolean add(E obj) {
    	    if (this.mods.contains(Modification.AddObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().add(obj);
    	    }

    	    throw new UnsupportedOperationException();
    	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#RemoveObj RemoveObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to remove an object 
	 *    which is the case only <code>this</code> 
	 *    contains <code>obj</code>. 
      	 */
    	public final boolean remove(Object obj) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().remove(obj);
    	    }
    	    throw new UnsupportedOperationException();
    	}

	// inherited from AbstractCollection: 
	// containsAll() 

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#RemoveObj RemoveObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to clear <code>this</code> 
	 *    which is the case only if <code>this</code> is not empty already. 
     	 */
    	public final void clear() {
    	    if (this.mods.contains(Modification.RemoveObj)) {
		// may throw UnsupportedOperationException
    		unrestricted().clear();
    	    }
    	    throw new UnsupportedOperationException();
    	}


	// Bulk Operations

	// inherited from AbstractCollection: 
	// boolean containsAll(Collection<?> c)

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#AddObj AddObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to add objects 
	 *    which is the case only if <code>coll</code> 
	 *    is not a subcollection of <code>this</code>. 
    	 */
	// **** if modifications do not distinguish between add and addAll, 
	// this method need not be implemented: 
	// Extending AbstractSet, this method is supported 
	// iff so is add(E) 
    	public final boolean addAll(Collection<? extends E> coll) {
    	    if (this.mods.contains(Modification.AddObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().addAll(coll);
    	    }
    	    throw new UnsupportedOperationException();
    	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#RemoveObj RemoveObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to remove objects 
	 *    which is the case only if <code>this</code> 
	 *    is not a subcollection of <code>coll</code>. 
    	 */
	// **** if modifications do not distinguish 
	// between remove and removeAll and retainAll, 
	// this method need not be implemented: 
	// Extending AbstractSet, this method is supported 
	// iff so is remove(E) 
    	public final boolean retainAll(Collection<?> coll) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().retainAll(coll);
    	    }
    	    throw new UnsupportedOperationException();
    	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#RemoveObj RemoveObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to remove objects 
	 *    which is the case only if <code>this</code> 
	 *    and <code>coll</code> are not disjoint. 
     	 */
	// **** see retainAll(...) 
    	public final boolean removeAll(Collection<?> cmp) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
 		// may throw UnsupportedOperationException
   		return unrestricted().removeAll(cmp);
    	    }
    	    throw new UnsupportedOperationException();
    	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#RemoveObj RemoveObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to remove objects 
	 *    which is the case only if <code>filter</code> 
	 *    accepts an element of <code>this</code>. 
     	 */
	public final boolean removeIf(Predicate<? super E> filter) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
 		// may throw UnsupportedOperationException
   		return unrestricted().removeIf(filter);
    	    }
    	    throw new UnsupportedOperationException();
	}

    	/**
    	 * Returns an iterator with method {@link Iterator#remove()} 
	 * which throws an {@link UnsupportedOperationException} 
	 * if either {@link #unrestricted()}'s iterator 
	 * does not allow this operation 
	 * or {@link CollectionsExt.Modification#RemoveObj RemoveObj} 
	 * is no allowed operation 
	 * according to {@link #allowedModifications()}. 
    	 */
    	public final Iterator<E> iterator() {
	    return new Iterator<E>() {
		private Iterator<E> wrapped = AbstractImmutableCollection.this
		    .unrestricted().iterator();

		public boolean hasNext() {
		    return this.wrapped.hasNext();
		}

		public E next() {
		    return this.wrapped.next();
		}

		public void forEachRemaining(Consumer<? super E> action) {
		    this.wrapped.forEachRemaining(action);
		}

		/**
		 * @throws UnsupportedOperationException
		 *    if either this iterator does not allow remove  
		 *    or 
		 *{@link CollectionsExt.Modification#RemoveObj RemoveObj} 
		 *    is no allowed operation 
		 *    according to {@link #allowedModifications()}. 
		 */
		public void remove() {
		    if (AbstractImmutableCollection.this.mods
			.contains(Modification.RemoveObj)) {
			// may throw UnsupportedOperationException
			this.wrapped.remove();
		    }
		    throw new UnsupportedOperationException();
		}
	    };
    	}

	/**
	 * Returns a string representation of this set, 
	 * including the allowed modifications 
	 * and the wrapped set. 
	 */
	public final String toString() {
	    StringBuilder res = new StringBuilder();
	    res.append("<Immutable modifications=\"");
	    res.append(this.mods);
	    res.append("\">");
	    res.append(unrestricted().toString());
	    res.append("</Immutable>");
	    return res.toString();
	}

	// default void forEach(Consumer<? super T> action) 
	// is inherited from AbstractCollection 

    } // class AbstractImmutableCollection 


    /**
     * A collection which prevents being modified 
     * by throwing an exception for the modifying methods. 
     *
     * @param <E>
     *    the class of the elements of this collection. 
     */
    public static final class ImmutableCollection<E> 
	extends AbstractImmutableCollection<Collection<E>, E> {

   	/* ---------------------------------------------------------------- *
    	 * fields.                                                          *
    	 * ---------------------------------------------------------------- */

	/**
	 * The enclosed set containing the elements of this set. 
	 */
	private final Collection<E> coll;

    	/* ---------------------------------------------------------------- *
    	 * constructors.                                                    *
    	 * ---------------------------------------------------------------- */

    	/**
    	 * Creates a new empty <code>ImmutableCollection</code> 
    	 * which equals <code>coll</code> but cannot be modified 
	 * neither directly nor via its iterator. 
	 *
	 * @throws NullPointerException
	 *    if <code>coll==null</code>. 
   	 */
	ImmutableCollection(Collection<E> coll) {
	    if (coll == null) {
		throw new NullPointerException(); // NOPMD
	    }
	    this.coll = coll;
	}

     	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	public Collection<E> unrestricted() {
	    return this.coll;
	}

   } // class ImmutableCollection<E>

    /**
     * A set which prevents being modified 
     * by throwing an exception for the modifying methods. 
     *
     * @param <E>
     *    the class of the elements of this set. 
     */
    public static final class ImmutableSet<E> 
	extends AbstractImmutableCollection<Set<E>, E> 
	implements Set<E> {

   	/* ---------------------------------------------------------------- *
    	 * fields.                                                          *
    	 * ---------------------------------------------------------------- */

	/**
	 * The enclosed set containing the elements of this set. 
	 */
	private final Set<E> set;

   	/* ---------------------------------------------------------------- *
    	 * constructors.                                                    *
    	 * ---------------------------------------------------------------- */

	ImmutableSet(Set<E> set) {
	    if (set == null) {
		throw new NullPointerException(); // NOPMD 
	    }
	    this.set = set;
	}

	ImmutableSet(Set<Modification> mods, Set<E> set) {
	    super(mods);
	    if (set == null) {
		throw new NullPointerException(); // NOPMD 
	    }
	    this.set = set;
	}

      	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	public Set<E> unrestricted() {
	    return this.set;
	}

    } // class ImmutableSet<E> 

    // **** modifications of views are allowed if allowed for original set. 
    // changes in allowances are reflected in views. 
    /**
     * A sorted set which prevents being modified 
     * by throwing an exception for the modifying methods. 
     *
     * @param <E>
     *    the class of the elements of this list. 
     */
    public static final class ImmutableSortedSet<E> 
	extends AbstractImmutableCollection<SortedSet<E>, E> 
	implements SortedSet<E> {

   	/* ---------------------------------------------------------------- *
    	 * fields.                                                          *
    	 * ---------------------------------------------------------------- */

	/**
	 * The enclosed sorted set containing the elements of this sorted set. 
	 */
	private final SortedSet<E> set;

    	/* ---------------------------------------------------------------- *
    	 * constructors.                                                    *
    	 * ---------------------------------------------------------------- */

	ImmutableSortedSet(SortedSet<E> set) {
	    if (set == null) {
		throw new NullPointerException(); // NOPMD
	    }
	    this.set = set;
	}

	ImmutableSortedSet(Set<Modification> mods, SortedSet<E> set) {
	    super(mods);
	    if (set == null) {
		throw new NullPointerException(); // NOPMD 
	    }
	    this.set = set;
	}

     	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	public SortedSet<E> unrestricted() {
	    return this.set;
	}

	public Comparator<? super E> comparator() {
	    return unrestricted().comparator();
	}

	public E first() {
	    return unrestricted().first();
	}

	public E last() {
	    return unrestricted().last();
	}

	/**
	 * Returns a view of the portion of this set 
	 * whose elements are strictly less than <code>toElement</code>. 
	 * For information on backing see {@link #subSet(Object, Object)}. 
	 */
	public SortedSet<E> headSet(E toElement) {
	    SortedSet<E> res0 = unrestricted().headSet(toElement);
	    return new ImmutableSortedSet<E>(allowedModifications(), res0);
	}

	/**
	 * Returns a view of the portion of this set 
	 * whose elements range from <code>fromElement</code>, 
	 * inclusive, to <code>toElement</code>, exclusive. 
	 * (If fromElement and toElement are equal, the returned set is empty.) 
	 * The returned set is backed by this set, 
	 * so changes in the returned set are reflected in this set, 
	 * and vice-versa. 
	 * The returned set supports all optional set operations 
	 * that this set supports. 
	 * In particular, changes of the allowed modifications 
	 * (returned by {@link #allowedModifications()}) 
	 * of the returned set are reflected in this set, and vice-versa. 
	 */
	public SortedSet<E> subSet(E fromElement, E toElement) {
	    SortedSet<E> res0 = unrestricted().subSet(fromElement, toElement);
	    return new ImmutableSortedSet<E>(allowedModifications(), res0);
	}

	/**
	 * Returns a view of the portion of this set 
	 * whose elements are greater than or equal to <code>fromElement</code>.
	 * For information on backing see {@link #subSet(Object, Object)}. 
	 */
	public SortedSet<E> tailSet(E fromElement) {
	    SortedSet<E> res0 = unrestricted().tailSet(fromElement);
	    return new ImmutableSortedSet<E>(allowedModifications(), res0);
	}
    } // class ImmutableSortedSet<E> 

    /**
     * A list which prevents being modified 
     * by throwing an exception for the modifying methods. 
     *
     * @param <E>
     *    the class of the elements of this list. 
     */
    public static final class ImmutableList<E> 
	extends AbstractImmutableCollection<List<E>, E> 
	implements List<E> {

   	/* ---------------------------------------------------------------- *
    	 * fields.                                                          *
    	 * ---------------------------------------------------------------- */

	/**
	 * The enclosed list containing the elements of this list. 
	 */
	private final List<E> list;

    	/* ---------------------------------------------------------------- *
    	 * constructors.                                                    *
    	 * ---------------------------------------------------------------- */

	ImmutableList(List<E> list) {
	    if (list == null) {
		throw new NullPointerException(); // NOPMD
	    }
	    this.list = list;
	}

	ImmutableList(Set<Modification> mods, List<E> list) {
	    super(mods);
	    if (list == null) {
		throw new NullPointerException(); // NOPMD
	    }
	    this.list = list;
	}

    	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	public List<E> unrestricted() {
	    return this.list;
	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#AddObj AddObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
    	 */
	public void add(int index, E obj) {
    	    if (!allowedModifications().contains(Modification.AddObj)) {
		throw new UnsupportedOperationException();
    	    }
	    // may throw UnsupportedOperationException
	    unrestricted().add(index, obj);
 	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#AddObj AddObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
 	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to add objects 
	 *    which is the case only <code>coll</code> is not empty. 
   	 */
	public boolean addAll(int index, Collection<? extends E> coll) {
    	    if (allowedModifications().contains(Modification.AddObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().addAll(index, coll);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public E get(int index) {
	    return unrestricted().get(index);
	}

	// must be implemented because contract differs from that of Collection 
	public int hashCode() {
	    return unrestricted().hashCode();
	}

	// must be implemented because hashCode() changed also  
	public boolean equals(Object obj) {
	    return unrestricted().equals(obj);
	}

	public int indexOf(Object obj) {
	    return unrestricted().indexOf(obj);
	}

	public int lastIndexOf(Object obj) {
	    return unrestricted().lastIndexOf(obj);
	}

	public ListIterator<E> listIterator() {
	    return listIterator(0);
	}

	public ListIterator<E> 	listIterator(int index) {
	    return new ListIterator<E>() {

		private ListIterator<E> wrapped = ImmutableList.this
		    .unrestricted().listIterator();

		public boolean hasNext() {
		    return this.wrapped.hasNext();
		}
		public E next() {
		    return this.wrapped.next();
		}
		public int nextIndex() {
		    return this.wrapped.nextIndex();
		}
		public boolean hasPrevious() {
		    return this.wrapped.hasPrevious();
		}
		public E previous() {
		    return this.wrapped.previous();
		}
		public int previousIndex() {
		    return this.wrapped.previousIndex();
		}

		public void forEachRemaining(Consumer<? super E> action) {
		    this.wrapped.forEachRemaining(action);
		}

		/**
		 * @throws UnsupportedOperationException
		 *    if either this iterator does not allow remove  or 
		 *    {@link CollectionsExt.Modification#RemoveObj RemoveObj}
		 *    is no allowed operation 
		 *    according to {@link #allowedModifications()}. 
		 */
		public void remove() {
		    if (ImmutableList.this.allowedModifications()
			.contains(Modification.RemoveObj)) {
			// may throw UnsupportedOperationException
			this.wrapped.remove();
		    }
		    throw new UnsupportedOperationException();
		}

		/**
		 * @throws UnsupportedOperationException
		 *    if either this iterator does not allow remove  
		 *    or {@link CollectionsExt.Modification#AddObj AddObj} 
		 *    is no allowed operation 
		 *    according to {@link #allowedModifications()}. 
		 */
		public void add(E element) {
		    if (ImmutableList.this.allowedModifications()
			.contains(Modification.AddObj)) {
			// may throw UnsupportedOperationException
			this.wrapped.add(element);
		    }
		    throw new UnsupportedOperationException();
		}

		/**
		 * @throws UnsupportedOperationException
		 *    if either this iterator does not allow remove  
		 *    or {@link CollectionsExt.Modification#SetObj SetObj} 
		 *    is no allowed operation 
		 *    according to {@link #allowedModifications()}. 
		 *    In the latter case it does not matter, 
		 *    whether effectively it is tried to set an object 
		 *    which is the case only if <code>element</code> 
		 *    is not yet on the place this iterator points to. 
 		 */
		public void set(E element) {
		    if (ImmutableList.this.allowedModifications()
			.contains(Modification.SetObj)) {
			// may throw UnsupportedOperationException
			this.wrapped.add(element);
		    }
		    throw new UnsupportedOperationException();
		}
	    };
	} // listIterator 

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#RemoveObj RemoveObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
    	 */
	public E remove(int index) {
   	    if (allowedModifications().contains(Modification.RemoveObj)) {
		// may throw UnsupportedOperationException
		return unrestricted().remove(index);
    	    }
	    throw new UnsupportedOperationException();
	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#SetObj SetObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to set an object 
	 *    which is the case only if <code>element</code> 
	 *    is not at place <code>index</code> in <code>this</code>. 
     	 */
	public E set(int index, E element) {
   	    if (allowedModifications().contains(Modification.SetObj)) {
		// may throw UnsupportedOperationException
		return unrestricted().set(index, element);
    	    }
	    throw new UnsupportedOperationException();
	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#SetObj SetObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to replace an object 
	 *    which is the case only if <code>operator</code> 
	 *    really changes an object. 
     	 */
	public void replaceAll(UnaryOperator<E> operator) {
   	    if (!allowedModifications().contains(Modification.SetObj)) {
		throw new UnsupportedOperationException();
  	    }
	    // may throw UnsupportedOperationException
	    unrestricted().replaceAll(operator);
  	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#SetObj SetObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
	 *    In the latter case it does not matter, 
	 *    whether effectively it is tried to replace an object 
	 *    which is the case only if <code>this</code> 
	 *    is not completely sorted according to <code>cmp</code>. 
	 *    This definition is in conjunction with the requirement, 
	 *    of {@link List#sort(Comparator)} 
	 *    that this exception is thrown iff the list iterator 
	 *    does not permit the {@link ListIterator#set(Object)} method. 
     	 */
	public void sort(Comparator<? super E> cmp) {
   	    if (!allowedModifications().contains(Modification.SetObj)) {
		throw new UnsupportedOperationException();
    	    }
	    // may throw UnsupportedOperationException
	    unrestricted().sort(cmp);
	}

	public List<E> subList(int fromIndex, int toIndex) {
	    List<E> res0 = unrestricted().subList(fromIndex, toIndex);
	    return new ImmutableList<E>(allowedModifications(), res0);
	}

    } // class ImmutableList

    /**
     * An interator which prevents modification on the underlying list, 
     * by throwing an exception for the modifying methods. 
     * This is used in conjunction 
     * with {@link CollectionsExt.ImmutableCyclicList}. 
     *
     * @param <E>
     *    the class of the elements to iterate over. 
     */
    public static final class NonModifyingCyclicIterator<E> 
	implements CyclicIterator<E> {

	private final CyclicIterator<E> wrapped;

	NonModifyingCyclicIterator(CyclicIterator<E> wrapped) {
	    this.wrapped = wrapped;
	}

	public int getFirstIndex() {
	    return this.wrapped.getFirstIndex();
	}
	public int getIndex() {
	    return this.wrapped.getIndex();
	}
	public CyclicList<E> getCyclicList() {
	    return this.wrapped.getCyclicList();
	}
	public boolean hasNext() {
	    return this.wrapped.hasNext();
	}
	public E next() {
	    return this.wrapped.next();
	}

	public boolean hasPrev() {
	    return this.wrapped.hasPrev();
	}

	public E previous() {
	    return this.wrapped.previous();
	}

	public int getNextIndexOf(E obj) {
	    return this.wrapped.getNextIndexOf(obj);
	}

	public void setIndex(int index) {
	    this.wrapped.setIndex(index);
	}

	public void add(E obj) {
	    throw new UnsupportedOperationException();
	}

	public void addAll(List<? extends E> list) {
	    throw new UnsupportedOperationException();
	}

	public void set(E obj) {
	    throw new UnsupportedOperationException();
	}
	public void remove() {
	    throw new UnsupportedOperationException();
	}

	public void refresh() {
	    throw new UnsupportedOperationException();
	}

	public boolean retEquals(CyclicIterator<?> other) {
	    throw new NotYetImplementedException();
	}

	@edu.umd.cs.findbugs.annotations.SuppressWarnings
	    (value = "EQ_UNUSUAL", 
	     justification = "special kind of exception")
	public boolean equals(Object other) {
	    throw new NotYetImplementedException();
	}
	public int hashCode() {
	    throw new NotYetImplementedException();
	}
	public double dist(CyclicIterator<E> other) {
	    throw new NotYetImplementedException();
	}

    } // NonModifyingCyclicIterator 

    /**
     * Represents an immutable cyclic list 
     * by throwing an exception 
     * when invoking a method which modifies this list. 
     *
     * @param <E>
     *    the class of the elements of this list. 
     */
    public static final class ImmutableCyclicList<E> implements CyclicList<E> {

	private final CyclicList<E> wrapped;

	ImmutableCyclicList(CyclicList<E> wrapped) {
	    this.wrapped = wrapped;
	}

	// ***** to be removed later. 
	public int shiftIndex(int index) {
	    throw new NotYetImplementedException();
	}
	public int size() {
	    return this.wrapped.size();
	}

	// based on size() 
	public boolean isEmpty() {
	    return this.wrapped.isEmpty();
	}

	public CyclicList<E> getInverse() {
	    throw new NotYetImplementedException();
	}

	// based on getIndexOf(...)
	public boolean contains(Object obj) {
	    return this.wrapped.contains(obj);
	}

	// based on contains(Object) 
	public boolean containsAll(Collection<?> coll) {
	    throw new NotYetImplementedException();
	}

	public Iterator<E> iterator() {
	    throw new NotYetImplementedException();
	}

	// public Iterator<E> iterator(int index) {
	//     throw new NotYetImplementedException();
	// }

	public CyclicIterator<E> cyclicIterator(int index) {
	    CyclicIterator<E> tbWrapped = this.wrapped.cyclicIterator(index);
	    return new NonModifyingCyclicIterator<E>(tbWrapped);
	}

	public Object[] toArray(int index) {
	    throw new NotYetImplementedException();
	}
	public <E> E[] toArray(int index, E[] array) {
	    throw new NotYetImplementedException();
	}
	public Object[] toArray() {
	    throw new NotYetImplementedException();
	}
	public <E> E[] toArray(E[] ret) {
	    throw new NotYetImplementedException();
	}
	public List<E> asList(int index) {
	    throw new NotYetImplementedException();
	}
	public List<E> asList() {
	    return Collections.unmodifiableList(this.wrapped.asList());
	}
	public CyclicList<E> cycle(int num) {
	    throw new NotYetImplementedException();
	}

	public void clear() {
    	    throw new UnsupportedOperationException();
	}

	public boolean equals(Object obj) {
	    return this.wrapped.equals(obj);
	}
	public boolean equalsCyclic(Object obj) {
	    return this.wrapped.equalsCyclic(obj);
	}
	public int hashCode() {
	    return this.wrapped.hashCode();
	}
	public int hashCodeCyclic() {
	    return this.wrapped.hashCodeCyclic();
	}
	public E get(int index) {
	    return this.wrapped.get(index);
	}
	public E set(int index, E element) {
    	    throw new UnsupportedOperationException();
	}
	public void replace(int index, Iterator<E> iter) {
   	    throw new UnsupportedOperationException();
	}
	public void replace(int index, List<E> list) {
   	    throw new UnsupportedOperationException();
	}
	public boolean add(E element) {
	    throw new UnsupportedOperationException();
	}
	public void add(int index, E element) {
   	    throw new UnsupportedOperationException();
	}
	public void addAll(int index, Iterator<E> iter) {
   	    throw new UnsupportedOperationException();
	}
	public void addAll(int index, List<? extends E> list) {
   	    throw new UnsupportedOperationException();
	}
	public boolean addAll(Collection<? extends E> coll) {
	    throw new UnsupportedOperationException(); // for cyclic lists 
	}
	public E remove(int index) throws EmptyCyclicListException {
   	    throw new UnsupportedOperationException();
	}
	public boolean remove(Object obj) {
   	    throw new UnsupportedOperationException();
	}
	public boolean removeAll(Collection<?> coll) {
   	    throw new UnsupportedOperationException();
	}
	public boolean retainAll(Collection<?> coll) {
   	    throw new UnsupportedOperationException();
	}
	public int getIndexOf(int idx, Object obj) {
	    throw new NotYetImplementedException();
	}
	public CyclicList<E> getCopy(int len) {
	    throw new NotYetImplementedException();
	}

    } // class ImmutableCyclicList 

    /**
     * An immutable implementation of the multiplicity interface. 
     */
    private static class ImmutableMultiplicity 
	implements MultiSet.Multiplicity {

	private final MultiSet.Multiplicity wrapped;
	private final Set<Modification> mod;

	ImmutableMultiplicity(MultiSet.Multiplicity wrapped, 
			      Set<Modification> mod) {
	    this.wrapped = wrapped;
	    this.mod = mod;
	}

	private Set<Modification> allowedModifications() {
	    return this.mod;
	}

	public int set(int mult) {
	    int multOrg = this.wrapped.get();

	    // **** may cause problems with over/underflow 
	    switch ((int) Math.signum(mult - multOrg)) {
	    case -1:
		assert mult < multOrg;
		if (allowedModifications()
		    .contains(Modification.RemoveObj)) {
		    // may throw UnsupportedOperationException
		    return this.wrapped.set(mult);
		}
		break;
	    case +1:
		assert mult > multOrg;
		if (allowedModifications()
		    .contains(Modification.AddObj)) {
		    // may throw UnsupportedOperationException
		    return this.wrapped.set(mult);
		}
		break;
	    case 0:
		assert mult == multOrg;
		// this does not modify the multiplicity 
		// but just returns the current value. 
		// could also return multOrg 
		return this.wrapped.set(mult);
	    default:
		throw new IllegalStateException("****");
	    }
	    throw new UnsupportedOperationException();
	}

	public int add(int mult) {
	    switch ((int) Math.signum(mult)) {
	    case -1:
		assert mult < 0;
		if (allowedModifications()
		    .contains(Modification.RemoveObj)) {
		    // may throw UnsupportedOperationException
		    return this.wrapped.add(mult);
		}
		break;
	    case +1:
		assert mult > 0;
		if (allowedModifications()
		    .contains(Modification.AddObj)) {
		    // may throw UnsupportedOperationException
		    return this.wrapped.add(mult);
		}
		break;
	    case 0:
		assert mult == 0;
		// this does not modify the multiplicity 
		// but just returns the current value. 
		return this.wrapped.add(mult);
	    default:
		throw new IllegalStateException("****");
	    }
	    throw new UnsupportedOperationException();
	}

	public int get() {
	    return this.wrapped.get();
	}

	public int compareTo(MultiSet.Multiplicity mult) {
	    return this.wrapped.compareTo(mult);
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
	    return this.wrapped.equals(obj);
	}

	// api-docs provided by javadoc. 
	public int hashCode() {
	    return this.wrapped.hashCode();
	}


    } // class ImmutableMultiplicity 

    /**
     * A MultiSet which prevents being modified 
     * by throwing an exception for the modifying methods. 
     *
     * @param <C>
     *    the class extending {@link MultiSet} with elements in E. 
     * @param <E>
     *    the class of the elements of this collection. 
     */
    abstract static class AbstractImmutableMultiSet<C extends MultiSet<E>, E> 
	implements MultiSet<E> {

    	//private static final long serialVersionUID = -2479143000061671589L;

    	/* ---------------------------------------------------------------- *
    	 * fields.                                                          *
    	 * ---------------------------------------------------------------- */

	// ***** same as for AbstractImmutableCollection 
	/**
	 * The set of allowed modifications. 
	 */
    	private final Set<Modification> mods;

    	/* ---------------------------------------------------------------- *
    	 * constructors.                                                    *
    	 * ---------------------------------------------------------------- */

	// ***** same as for AbstractCollection 
	private AbstractImmutableMultiSet() {
   	    this(EnumSet.noneOf(Modification.class));
	}

	// ***** same as for AbstractCollection 
	AbstractImmutableMultiSet(Set<Modification> mods) {
   	    this.mods = mods;
	}

    	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

    	public final Set<Modification> allowedModifications() {
    	    return this.mods;
    	}

	/**
	 * Returns the underlying multiset without the restrictions 
	 * imposed by this {@link CollectionsExt.ImmutableCollection}. 
	 * Note that the result 
	 * may still throw {@link UnsupportedOperationException}s 
	 * depending on the implementation. 
	 */
	public abstract C unrestricted();

	// query methods 

	public int size() {
	    return unrestricted().size();
	}

	public int sizeWithMult() {
	    return unrestricted().sizeWithMult();
	}

	public boolean isEmpty() {
	    return unrestricted().isEmpty();
	}

	public Object getObjWithMaxMult() {
	    return unrestricted().getObjWithMaxMult();
	}

	public int getMaxMult() {
	    return unrestricted().getMaxMult();
	}

	public int getMultiplicity(Object obj) {
	    return unrestricted().getMultiplicity(obj);
	}

	public boolean contains(Object obj) {
	    return unrestricted().contains(obj);
	}

	public Object[] toArray() {
	    return unrestricted().toArray();
	}

	public E[] toArray(E[] arr) {
	    return unrestricted().toArray(arr);
	}

	// What follows are query methods the return value of which 
	// is required to prevent modifications. 

	public Multiplicity getMultiplicityObj(Object obj) {
	    Multiplicity res0 = this.unrestricted().getMultiplicityObj(obj);
	    return new ImmutableMultiplicity(res0, this.allowedModifications());
	}

	public MultiSetIterator<E> iterator() {
	    return new MultiSetIterator<E>() {
		private MultiSetIterator<E> wrapped = 
		    AbstractImmutableMultiSet.this.unrestricted().iterator();

		public boolean hasNext() {
		    return this.wrapped.hasNext();
		}

		public E next() {
		    return this.wrapped.next();
		}

		public void forEachRemaining(Consumer<? super E> action) {
		    this.wrapped.forEachRemaining(action);
		}

		public void remove() {
		    if (AbstractImmutableMultiSet.this.mods
			.contains(Modification.RemoveObj)) {
			// may throw UnsupportedOperationException
			this.wrapped.remove();
		    }
		    throw new UnsupportedOperationException();
		}

		public int getMult() {
		    return this.wrapped.getMult();
		}

		public MultiSet.Multiplicity getMultObj() {
		    return this.wrapped.getMultObj();
		}

		// this is always the same as for mutable multi-sets. 
		// I think, one has to decide: 
		// There is a single method exposing Multiplicities: 
		// MultiSet.getMultiplicityObj(Object). 
		// If this is ok, then we may replace in MultiSetIterator 
		// setMult and removeMult by getMultObj.set() 
		// and getMultObj.remove(int) 
		//
		// If we agree that Multiplicities are not public, 
		// then we must turn 
		// MultiSet.getMultiplicityObj(Object) protected
		// but we must define iterator with method 
		// protected getMultObj and implement 
		// setMult and removeMult in terms of getMultObj. 
		// Moreover we need a method iteratorInternal 
		// which exposes getMultObj to the immutable iterator 
		// and make sure that setMult and removeMult 
		// are implemented as in base class. 

		// Here getMultObj() returns an immutable Multiplicity 
		public int setMult(int mult) {
		    // **** copy from MultiSetIteratorImpl 
		    // can be avoided using default final in interface 
		    // may throw IllegalStateException 
		    Multiplicity last = getMultObj();
		    assert last != null;
		    if (mult == 0) {
			int res = last.get();
			// may throw UnsupportedOperationException
			remove();
			return res;
		    }
		    // may throw IllegalArgumentException
		    // may throw UnsupportedOperationException
		    return last.set(mult);
		    //throw new NotYetImplementedException();
		}

		// Here getMultObj() returns an immutable Multiplicity 
		public int removeMult(int mult) {
		    // **** copy from MultiSetIteratorImpl
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
	    };
	}

	public int addWithMult(E obj) {
    	    if (this.mods.contains(Modification.AddObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().addWithMult(obj);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public int addWithMult(E obj, int addMult) {
    	    if (this.mods.contains(Modification.AddObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().addWithMult(obj, addMult);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public boolean add(E obj) {
    	    if (this.mods.contains(Modification.AddObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().add(obj);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public int removeWithMult(Object obj) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().removeWithMult(obj);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public int removeWithMult(Object obj, int removeMult) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().removeWithMult(obj, removeMult);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public boolean remove(Object obj) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().remove(obj);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public int setMultiplicity(E obj, int newMult) {
	    throw new NotYetImplementedException();
	}

	public boolean containsAll(Collection<?> coll) {
	    return unrestricted().containsAll(coll);
	}

	public boolean addAll(MultiSet<? extends E> mvs) {
    	    if (this.mods.contains(Modification.AddObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().addAll(mvs);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public boolean addAll(Set<? extends E> set) {
    	    if (this.mods.contains(Modification.AddObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().addAll(set);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> coll) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().removeAll(coll);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> coll) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
		// may throw UnsupportedOperationException
    		return unrestricted().retainAll(coll);
    	    }
    	    throw new UnsupportedOperationException();
	}

	public void clear() {
    	    if (!this.mods.contains(Modification.RemoveObj)) {
		throw new UnsupportedOperationException();
  	    }
	    // may throw UnsupportedOperationException
	    unrestricted().clear();
  	}

	public Set<E> getSet() {
	    return new ImmutableSet<E>(allowedModifications(),
				       unrestricted().getSet());
	}

	public Map<E, Multiplicity> getMap() {
	    throw new NotYetImplementedException();
	}

	public Set<Map.Entry<E, Multiplicity>> getSetWithMults() {
	    return new ImmutableSet<Map.Entry<E, Multiplicity>>
		(allowedModifications(), unrestricted().getSetWithMults());
	}

	// **** as in AbstractImmutableCollection
	public String toString() {
	    StringBuilder res = new StringBuilder();
	    res.append("<Immutable modifications=\">");
	    res.append(this.mods);
	    res.append("\">");
	    res.append(unrestricted().toString());
	    res.append("</Immutable>");
	    return res.toString();
	}

	public boolean equals(Object obj) {
	    return unrestricted().equals(obj);
	}

	public int hashCode() {
	    return unrestricted().hashCode();
	}

    } // class AbstractImmutableMultiSet 

    /**
     * A multi-set which prevents being modified 
     * by throwing an exception for the modifying methods. 
     *
     * @param <E>
     *    the class of the elements of this list. 
     */
    static class ImmutableMultiSet<E> 
	extends AbstractImmutableMultiSet<MultiSet<E>, E> {

   	/* ---------------------------------------------------------------- *
    	 * fields.                                                          *
    	 * ---------------------------------------------------------------- */

	/**
	 * The enclosed multi-set containing the elements of this multi-set. 
	 */
	private final MultiSet<E> mSet;

    	/* ---------------------------------------------------------------- *
    	 * constructors.                                                    *
    	 * ---------------------------------------------------------------- */

    	/**
    	 * Creates a new empty <code>ImmutableMultiSet</code> 
    	 * which equals <code>mSet</code> but cannot be modified 
	 * neither directly nor via its iterator. 
	 *y
	 * @throws NullPointerException
	 *    if <code>coll==null</code>. 
   	 */
	ImmutableMultiSet(MultiSet<E> mSet) {
	    if (mSet == null) {
		throw new NullPointerException(); // NOPMD
	    }
	    this.mSet = mSet;
	}

    	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	public MultiSet<E> unrestricted() {
	    return this.mSet;
	}
    } // class ImmutableMultiSet

    /**
     * A sorted multi-set which prevents being modified 
     * by throwing an exception for the modifying methods. 
     *
     * @param <E>
     *    the class of the elements of this list. 
     */
    static class ImmutableSortedMultiSet<E> 
	extends AbstractImmutableMultiSet<SortedMultiSet<E>, E> 
	implements SortedMultiSet<E> {

   	/* ---------------------------------------------------------------- *
    	 * fields.                                                          *
    	 * ---------------------------------------------------------------- */

	private final SortedMultiSet<E> mSet;

    	/* ---------------------------------------------------------------- *
    	 * constructors.                                                    *
    	 * ---------------------------------------------------------------- */

	/**
    	 * Creates a new empty <code>ImmutableSortedMultiSet</code> 
    	 * which equals <code>mSet</code> but cannot be modified 
	 * neither directly nor via its iterator. 
	 *y
	 * @throws NullPointerException
	 *    if <code>coll==null</code>. 
   	 */
	ImmutableSortedMultiSet(SortedMultiSet<E> mSet) {
	    if (mSet == null) {
		throw new NullPointerException(); // NOPMD
	    }
	    this.mSet = mSet;
	}

	ImmutableSortedMultiSet(Set<Modification> mods,
				SortedMultiSet<E> mSet) {
	    super(mods);
	    if (mSet == null) {
		throw new NullPointerException(); // NOPMD 
	    }
	    this.mSet = mSet;
	}

    	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	// ****
	public SortedMultiSet<E> unrestricted() {
	    return this.mSet;
	}

	public SortedSet<E> getSet() {
	    return new ImmutableSortedSet<E>(allowedModifications(),
					     unrestricted().getSet());
	}

     	public NavigableMap<E, Multiplicity> getMap() {
	    //return Collections
	    //.unmodifiableNavigableMap(unrestricted().getMap())
	    throw new NotYetImplementedException();
	    // return new ImmutableNavigableMap(unrestricted().getMap());
	}

	public Comparator<? super E> comparator() {
	    return unrestricted().comparator();
	}

	public E first() {
	    return unrestricted().first();
	}

	public E last() {
	    return unrestricted().last();
	}

	public SortedMultiSet<E> headSet(E toElement) {
	    SortedMultiSet<E> res0 = unrestricted()
		.headSet(toElement);
	    return new ImmutableSortedMultiSet<E>(allowedModifications(), res0);
	}

    	public SortedMultiSet<E> tailSet(E fromElement) {
	    SortedMultiSet<E> res0 = unrestricted()
		.tailSet(fromElement);
	    return new ImmutableSortedMultiSet<E>(allowedModifications(), res0);
	}

	public SortedMultiSet<E> subSet(E fromElement, E toElement) {
	    SortedMultiSet<E> res0 = unrestricted()
		.subSet(fromElement, toElement);
	    return new ImmutableSortedMultiSet<E>(allowedModifications(), res0);
	}

    } // class ImmutableSortedMultiSet


    /* -------------------------------------------------------------------- *
     * class fields.                                                        *
     * -------------------------------------------------------------------- */



    /* -------------------------------------------------------------------- *
     * class methods.                                                       *
     * -------------------------------------------------------------------- */


    /**
     * Returns an unmodifiable view 
     * of the specified collection <code>coll</code>. 
     * This method allows modules to provide users 
     * with "read-only" access to internal collections. 
     * Query operations on the returned collection 
     * "read through" to the specified collection, 
     * and attempts to modify the returned collection, 
     * whether direct or via its iterator, 
     * result in an <code>UnsupportedOperationException</code>. 
     *
     * @param coll 
     *    an instance of a <code>Collection</code>. 
     * @return 
     *    a <code>Collection</code> which equals <code>coll</code>, 
     *    i.e. with the same elements 
     *    returned by the iterator in the same ordering 
     *    but is immutable. 
     *    Note that the return type offers methods 
     *    {@link CollectionsExt.ImmutableCollection
     *#allowModification(CollectionsExt.Modification)}, 
     *    {@link CollectionsExt.ImmutableCollection#allowedModifications()} 
     *    and {@link CollectionsExt.ImmutableCollection#unrestricted()}. 
     * @throws NullPointerException
     *    if <code>coll==null</code>. 
     */
    public static <E> ImmutableCollection<E> 
	getImmutableCollection(Collection<E> coll) {
	return new ImmutableCollection<E>(coll);
    }

    // as for {@link #getImmutableCollection(Collection)} 
    public static <E> ImmutableSet<E> getImmutableSet(Set<E> set) {
	return new ImmutableSet<E>(set);
    }

    // as for {@link #getImmutableCollection(Collection)} 
    public static 
	<E> ImmutableSortedSet<E> getImmutableSortedSet(SortedSet<E> set) {
	return new ImmutableSortedSet<E>(set);
    }

    // as for {@link #getImmutableCollection(Collection)} 
    public static <E> ImmutableList<E> getImmutableList(List<E> list) {
	return new ImmutableList<E>(list);
    }

    public static <E> CyclicList<E> getImmutableCyclicList(CyclicList<E> cyc) {
	return new ImmutableCyclicList<E>(cyc);
    }

    public static 
	<E> MultiSet<E> getImmutableMultiSet(MultiSet<E> mSet) {
	return new ImmutableMultiSet<E>(mSet);
    }

    public static <E> 
	MultiSet<E> getImmutableSortedMultiSet(SortedMultiSet<E> mSet) {
	return new ImmutableMultiSet<E>(mSet);
    }



    /**
     * Retuns a weak hash set, i.e. a hash set of weak references. 
     *
     * @see java.util.HashMap
     * @see Collections#newSetFromMap(Map)
     */
    public static <E> Set<E> weakHashSet() {
	return Collections.newSetFromMap(new WeakHashMap<E, Boolean>());
    }

    /**
     * Converts the list given recursively into an array. 
     *
     * @param list 
     *    a <code>List</code>. 
     * @return 
     *    an array <code>array</code> of objects satisfying 
     *    <code>array[i] == list.get(i)</code> 
     *    if <code>list.get(i)</code> is not a list; 
     *    <code>array[i] == recToArray(list.get(i))</code> otherwise. 
     *    Note that <code>array</code> may be a nested array 
     *    but that the dimension is always one. 
     * @see #recToArray(Object, Class)
     * @see ArraysExt#recAsList(Object[])
     */
    public static Object[] recToArray(List<?> list) {

	Object[] result = new Object[list.size()];
	int index = 0;
	for (Object cand : list) {
	    if (cand instanceof List) {
		// cand is a list. 
		result[index] = recToArray((List<?>) cand);
	    } else {
		// cand is no list. 
		result[index] = cand;
	    }
	    index++;
	}
	return result;
    }

    /**
     * Converts <code>source</code> 
     * which is typically a {@link java.util.List}, 
     * recursively into an array with the given type. 
     *
     * @param source
     *    an arbitrary <code>Object</code>, even an array 
     *    but typically a <code>List</code>. 
     * @param cls
     *    Up to compatibility 
     *    (see {@link BasicTypesCompatibilityChecker#areCompatible}), 
     *    the type of the return value. 
     *    Note that typically this is an array type 
     *    such as <code>Double[][][]</code> or <code>List[][][]</code> 
     *    or even <code>int[][][]</code> but this need not be the case. 
     * @return 
     *    <ul>
     *    <li>
     *    if <code>cls</code> and <code>source</code> are compatible 
     *    (see {@link BasicTypesCompatibilityChecker#areCompatible}), 
     *    e.g. if <code>source</code> is an instance of <code>cls</code> 
     *    or if <code>source == null</code>, 
     *    the object <code>source</code> is returned. 
     *    <p>
     *    If <code>cls</code> is not an elementary type 
     *    as e.g. {@link java.lang.Boolean#TYPE}, 
     *    and if <code>source != null</code>, 
     *    compatibility means that <code>source</code> 
     *    is an instance of the corresponding wrapper class. 
     *    <li>
     *    if <code>cls</code> and <code>source</code> are not compatible, 
     *    <code>cls</code> must be an array type 
     *    and <code>source</code> must be a list; 
     *    otherwise an exception is thrown. 
     *    <p>
     *    In the former case, 
     *    an array <code>array</code> of objects is returned 
     *    satisfying <code>array.length == ((List)source).size()</code> and 
     *    <code>array[i] == recToArray(list.get(i), cls2)</code> 
     *    for all valid indices <code>i</code> is returned. 
     *    The <code>cls2</code> argument for the recursive invocation 
     *    is the element type of <code>cls</code>. 
     *    <p>
     *    Note that although the return value is always an array, 
     *    its type need not be a subtype of <code>Object[]</code> 
     *    or of <code>Object[][]</code>. 
     *    Consider for instance the case 
     *    where <code>source</code> is a list of <code>Integer</code>s 
     *    and <code>cls</code> is <code>int[]</code>: 
     *    This yields an
     *    </ul>
     * @throws IllegalArgumentException
     *    if neither of the following is true: 
     *    <ul>
     *    <li>
     *    <code>cls</code> and <code>source</code> are compatible. 
     *    <li>
     *    <code>cls</code> is an array type and 
     *    <code>source</code> is a <code>List</code>. 
     *    </ul>
     * @see ArraysExt#recAsList(Object, Class)
     */
    public static Object recToArray(Object source, Class<?> cls) {
	return recToArray(source, cls, Caster.BASIC_TYPES);
    }

    /**
     * Converts <code>source</code> 
     * which is typically a {@link java.util.Collection}, 
     * recursively into an array with the given type 
     * using the specified caster 
     * for the elementary objects in <code>source</code>. 
     *
     * @param source
     *    an arbitrary <code>Object</code>, even an array 
     *    but typically a <code>Collection</code>. 
     * @param cls
     *    Up to compatibility defined by <code>caster</code>, 
     *    the type of the return value. 
     *    Note that typically this is an array type 
     *    such as <code>Double[][][]</code> or <code>List[][][]</code> 
     *    or even <code>int[][][]</code> but this need not be the case. 
     *    Note that the base type 
     *    has to be compatible with the source objects 
     *    with respect to the specified <code>caster</code>. 
     * @param caster 
     *    performs the conversion of the top-level elements 
     *    of the <code>source</code>. 
     * @return 
     *    <ul>
     *    <li>
     *    if <code>cls</code> and <code>source</code> are compatible 
     *    (see {@link Caster#areCompatible}), 
     *    the {@link Caster#cast cast of} <code>source</code> is returned. 
     *    <p>
     *    Note that compatibility is up to wrapping of elementary types 
     *    and unwrapping of their wrappers. 
     *    <li>
     *    if <code>cls</code> and <code>source</code> are not compatible, 
     *    <code>cls</code> must be an array type 
     *    and <code>source</code> must be a list; 
     *    otherwise an exception is thrown. 
     *    <p>
     *    In the former case, 
     *    an array <code>array</code> of objects is returned 
     *    satisfying <code>array.length == ((Collection)source).size()</code> 
     *    and 
     *    <code>array[i] == recToArray(list.get(i), ... , caster)</code> 
     *    for all valid indices <code>i</code>. 
     *    The <code>cls2</code> argument for the recursive invocation 
     *    is the element type of <code>cls</code>. 
     *    <p>
     *    Note that although the return value 
     *    is either the result of a casting process or an array, 
     *    in the latter case its type need not be 
     *    a subtype of <code>Object[]</code> 
     *    or of <code>Object[][]</code>. 
     *    Consider for instance the case 
     *    where <code>source</code> is a list of <code>Integer</code>s 
     *    and <code>cls</code> is <code>int[]</code>: 
     *    </ul>
     * @throws IllegalArgumentException
     *    if neither of the following is true: 
     *    <ul>
     *    <li>
     *    <code>cls</code> and <code>source</code> are compatible 
     *    with respect to {@link Caster#areCompatible caster}. 
     *    <li>
     *    <code>cls</code> is an array type and 
     *    <code>source</code> is a <code>List</code>. 
     *    </ul>
     * @see ArraysExt#recAsList(Object, Class, Caster)
     */
    public static Object recToArray(Object source, 
				    Class<?> cls, 
				    Caster caster) {

	if (caster.areCompatible(cls, source)) {
	    // Here, either source == null || cls.isInstance(source) 
	    // or source wraps something of type cls. 
	    return caster.cast(source);
	}
	// Here, source is not compatible with cls. 

	if (!cls.isArray()) {
	    // Here, the result type is not an array type. 
	    throw new IllegalArgumentException
		("Found incompatible types: " + source + 
		 " is not an instance of " + cls + 
		 " but of " + source.getClass() + ". ");
	}

	// Here, the result type is an array type. 
	if (!(source instanceof Collection)) {
	    throw new IllegalArgumentException
		("Found incompatible types: " + source + 
		 " is not an instance of List. ");
	}
	// Here, source instanceof Collection and cls.isArray(). 

	Collection<?> coll = (Collection<?>) source;
	Class<?> compType = cls.getComponentType();
	Object result = Array.newInstance(compType, coll.size());
	int ind = 0;
	for (Object cand : coll) {
	    // automatic unwrapping if needed. 
	    Array.set(result, ind++, recToArray(cand, compType, caster));
	}
	return result;
    }

    /*
     * Fills the given map with key-value pairs given by <code>keyVal</code> 
     * and returns a trace of the replaced objects. 
     * Note that this method is misplaced in this class but .... 
     *
     * @param map 
     *    a <code>Map</code> object. 
     * @param keyVal 
     *    an array of key-value pairs. 
     *    In particular, <code>keyVal[i].length == 2</code> whenever defined. 
     * @return 
     *    an array of key-value pairs. 
     *    An entry <code>new Object[] {key,value}</code> means, 
     *    that before invoking this method, 
     *    <code>map.get(key) == value</code> hold. 
     *    Note that <code>value == null</code> is possible. 
     * @throws ClassCastException 
     *    if an element of an entry <code>new Object[] {key, value}</code> 
     *    of <code>keyVal</code> has not the right type. 
     */
/*
    public static <K, V> Object[][] fillMap(Map<K, V> map, Object[][] keyVal) {
	List<Object[]> replaced = new ArrayList<Object[]>();
	V repl;
	for (int i = 0; i < keyVal.length; i++) {
	    repl = map.put((K)keyVal[i][0], (V)keyVal[i][1]);
	    if (map.keySet().contains(keyVal[i][0])) {
		replaced.add(new Object[] {keyVal[i][0], repl});
	    }
	}
	return (Object[][])replaced.toArray(new Object[][] {});
    }
*/

    /**
     * Returns the unique element of the collection <code>coll</code>. 
     *
     * @param coll
     *    a collection of <code>T</code>'s. 
     * @return
     *    the unique element of the collection <code>coll</code>. 
     * @throws IllegalStateException
     *    if <code>coll</code> does not contain a unique element. 
     */
    public static <T> T getUnique(Collection<? extends T> coll) {
	if (coll.size() != 1) {
	    throw new IllegalStateException
		("Expected a single element; found collection " + coll + ". ");
	}
	assert !coll.isEmpty();

	return coll.iterator().next();
	// for (T res : coll) {
	//     return res;
	// }
	// This place is never reached, because coll is not empty 
	//throw new IllegalStateException();
    }

    /**
     * Returns the reverse of the given list. 
     * In fact the list returned is an {@link java.util.ArrayList}. 
     */
    public static <T> List<T> reverse(List<T> list) {
	List<T> res = new ArrayList<T>(list.size());
	// NOPMD
	for (int i = 0; i < list.size(); i++) {// NOPMD
	    res.add(list.get(list.size() - 1 - i));
	}
	return res;
    }

    public static void main(String[] args) {
    }

}


