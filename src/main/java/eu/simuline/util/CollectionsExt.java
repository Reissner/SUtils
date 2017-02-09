
package eu.simuline.util;

import java.lang.reflect.Array;

import java.util.List;
import java.util.Iterator;
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

/**
 * An add on of the core class {@link java.util.Collections}. 
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
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
	 * Modification by {@link Collection#add(Object)} 
	 * and by {@link List#add(int, Object)}. 
	 */
	AddObj, 
	/**
	 * Modification by {@link Collection#remove(Object)} 
	 * and by {@link List#remove(int)}. 
	 */
	RemoveObj, 
	/**
	 * Modification by {@link List#set(int, Object)}. 
	 */
	SetObj,
	// not clear whether this is needed: 
	// maybe retainAll and removeAll shall be defined iff so is remove. 
	// same for clear. 
	// on the other hand, maybe useful for debugging. 
	/**
	 * Modification by {@link Collection#clear()}. 
	 */
	Clear, 

	/**
	 * Modification by {@link Collection#addAll(Collection)} 
	 * and by {@link List#addAll(int, Collection)}. 
	 */
	AddAll, 
	/**
	 * Modification by {@link Collection#retainAll(Collection)}. 
	 */
	RetainAll, 
	/**
	 * Modification by {@link Collection#removeAll(Collection)}. 
	 */
	RemoveAll, 
	//Iterator, // seems not appropriate: iterator as such does not change 
	/**
	 * Modification by {@link Iterator#remove()} 
	 * of iterator attached with underlying {@link Collection} 
	 * via {@link Collection#iterator()}, {@link List#listIterator()} 
	 * or {@link List#listIterator(int)}. 
	 */
	RemoveIter,
	// occurs for Lists only via ListIterators 
	/**
	 * Modification by {@link ListIterator#add(Object)} 
	 * of iterator attached with underlying {@link List} 
	 * via {@link List#listIterator()} or {@link List#listIterator(int)}. 
	 */
	AddIter, 
	/**
	 * Modification by {@link ListIterator#set(Object)} 
	 * of iterator attached with underlying {@link List} 
	 * via {@link List#listIterator()} or {@link List#listIterator(int)}. 
	 */
	SetIter;
    } // enum Modification 

    /**
     * A class of {@link Collection}s of elements of class <code>E</code>
     * extending <code>C</code> 
     * initially throwing an <code>UnsupportedOperationException</code> 
     * when trying to modify the collection 
     * either directly or via its iterator or via a transparent sight 
     * like {@link List#subList(int, int)}. 
     * <p>
     * Instances of this class can be created vial creator methods like 
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
     */
    public abstract static 
	class AbstractImmutableCollection<C extends Collection<E>, E> 
	extends AbstractCollection<E> {

    	private static final long serialVersionUID = -2479143000061671589L;

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

    	private AbstractImmutableCollection() {
   	    this(EnumSet.noneOf(Modification.class));
    	}

    	AbstractImmutableCollection(Set<Modification> mods) {
   	    this.mods = mods;
    	}

    	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	/**
	 * Returns this set but with additional modification <code>mod</code>. 
	 */
    	public AbstractImmutableCollection<C,E> 
	    allowModification(Modification mod) {
    	    this.mods.add(mod);
    	    return this;
    	}

    	public void allowModifications(Set<Modification> mods) {
    	    this.mods.addAll(mods);
    	}

	/**
	 * Returns the set of allowed modification of this set. 
	 */
    	public Set<Modification> allowedModifications() {
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

	public int size() {
	    return unrestricted().size();
	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#AddObj AddObj} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
    	 */
    	public boolean add(E obj) {
    	    if (this.mods.contains(Modification.AddObj)) {
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
    	 */
    	public boolean remove(Object obj) {
    	    if (this.mods.contains(Modification.RemoveObj)) {
    		return unrestricted().remove(obj);
    	    }
    	    throw new UnsupportedOperationException();
    	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#Clear Clear} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
    	 */
    	public void clear() {
    	    if (this.mods.contains(Modification.Clear)) {
    		unrestricted().clear();
    	    }
    	    throw new UnsupportedOperationException();
    	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#AddAll AddAll} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
    	 */
	// **** if modifications do not distinguish between add and addAll, 
	// this method need not be implemented: 
	// Extending AbstractSet, this method is supported 
	// iff so is add(E) 
    	public boolean addAll(Collection<? extends E> cmp) {
    	    if (this.mods.contains(Modification.AddAll)) {
    		return unrestricted().addAll(cmp);
    	    }
    	    throw new UnsupportedOperationException();
    	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#RetainAll RetainAll} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
    	 */
	// **** if modifications do not distinguish 
	// between remove and removeAll and retainAll, 
	// this method need not be implemented: 
	// Extending AbstractSet, this method is supported 
	// iff so is remove(E) 
    	public boolean retainAll(Collection<?> cmp) {
    	    if (this.mods.contains(Modification.RetainAll)) {
    		return unrestricted().retainAll(cmp);
    	    }
    	    throw new UnsupportedOperationException();
    	}

    	/**
    	 * @throws UnsupportedOperationException
	 *    if either {@link #unrestricted()} does not allow this operation 
	 *    or {@link CollectionsExt.Modification#RemoveAll RemoveAll} 
	 *    is no allowed operation 
	 *    according to {@link #allowedModifications()}. 
    	 */
	// **** see retainAll(...) 
    	public boolean removeAll(Collection<?> cmp) {
    	    if (this.mods.contains(Modification.RemoveAll)) {
    		return unrestricted().removeAll(cmp);
    	    }
    	    throw new UnsupportedOperationException();
    	}

    	/**
    	 * Returns an iterator with method {@link Iterator#remove()} 
	 * which throws an {@link UnsupportedOperationException} 
	 * if either {@link #unrestricted()}'s iterator 
	 * does not allow this operation 
	 * or {@link CollectionsExt.Modification#RemoveIter RemoveIter} 
	 * is no allowed operation 
	 * according to {@link #allowedModifications()}. 
    	 */
    	public Iterator<E> iterator() {
	    return new Iterator<E>() {
		Iterator<E> wrapped = AbstractImmutableCollection.this
		    .unrestricted().iterator();
		public boolean hasNext() {
		    return this.wrapped.hasNext();
		}

		public E next() {
		    return this.wrapped.next();
		}

		/**
		 * @throws UnsupportedOperationException
		 *    if either this iterator does not allow remove  
		 *    or 
		 *{@link CollectionsExt.Modification#RemoveItrt RemoveIter} 
		 *    is no allowed operation 
		 *    according to {@link #allowedModifications()}. 
		 */
		public void remove() {
		    if (AbstractImmutableCollection.this.mods
			.contains(Modification.RemoveIter)) {
			this.wrapped.remove();
		    }
		    throw new UnsupportedOperationException();
		}
	    };
    	}

	// **** modifications missing 
	public String toString() {
	    StringBuilder res = new StringBuilder();
	    res.append("<Immutable>");
	    res.append(unrestricted().toString());
	    res.append("</Immutable>");
	    return res.toString();
	}
    } // class AbstractImmutableCollection 


    public static class ImmutableCollection<E> 
	extends AbstractImmutableCollection<Collection<E>, E> 
	implements Collection<E> {

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
    	 * which equals <code>set</code> but cannot be modified 
	 * neither directly nor via its iterator. 
	 *
	 * @throws NullPointerException
	 *    if <code>coll==null</code>. 
   	 */
	ImmutableCollection(Collection<E> coll) {
	    if (coll == null) {
		throw new NullPointerException();// NOPMD
	    }
	    this.coll = coll;
	}

     	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	public Collection<E> unrestricted() {
	    return this.coll;
	}

   } // class ImmutableSet<E>

    public static class ImmutableSet<E> 
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
		throw new NullPointerException();// NOPMD 
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
    public static class ImmutableSortedSet<E> 
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
	    this.set = set;
	}

	ImmutableSortedSet(Set<Modification> mods, SortedSet<E> set) {
	    super(mods);
	    if (set == null) {
		throw new NullPointerException();// NOPMD 
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

	public SortedSet<E> headSet(E toElement) {
	    SortedSet<E> res0 = unrestricted().headSet(toElement);
	    return new ImmutableSortedSet<E>(allowedModifications(), res0);
	}

	public SortedSet<E> subSet(E fromElement, E toElement) {
	    SortedSet<E> res0 = unrestricted().subSet(fromElement, toElement);
	    return new ImmutableSortedSet<E>(allowedModifications(), res0);
	}

	public SortedSet<E> tailSet(E fromElement) {
	    SortedSet<E> res0 = unrestricted().tailSet(fromElement);
	    return new ImmutableSortedSet<E>(allowedModifications(), res0);
	}
    } // class ImmutableSortedSet<E> 

    public static class ImmutableList<E> 
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
		throw new NullPointerException();// NOPMD
	    }
	    this.list = list;
	}

	ImmutableList(Set<Modification> mods, List<E> list) {
	    super(mods);
	    if (list == null) {
		throw new NullPointerException();// NOPMD
	    }
	    this.list = list;
	}

    	/* ---------------------------------------------------------------- *
    	 * methods.                                                         *
    	 * ---------------------------------------------------------------- */

	public List<E> unrestricted() {
	    return this.list;
	}

	public void add(int index, E obj) {
    	    if (!allowedModifications().contains(Modification.AddObj)) {
		throw new UnsupportedOperationException();
    	    }
	    unrestricted().add(index, obj);
 	}

	public boolean addAll(int index, Collection<? extends E> coll) {
	    throw new NotYetImplementedException();
	}

	public E get(int index) {
	    return unrestricted().get(index);
	}

	// must be implemented because contract differs from Collection 
	public int hashCode() {
	    return unrestricted().hashCode();
	}

	// must be implemented because of contract differs from Collection 
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
		ListIterator<E> wrapped = ImmutableList.this
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
		/**
		 * @throws UnsupportedOperationException
		 *    if either this iterator does not allow remove  
		 *    or 
		 *{@link CollectionsExt.Modification#RemoveIter RemoveIter} 
		 *    is no allowed operation 
		 *    according to {@link #allowedModifications()}. 
		 */
		public void remove() {
		    if (ImmutableList.this.allowedModifications()
			.contains(Modification.RemoveIter)) {
			this.wrapped.remove();
		    }
		    throw new UnsupportedOperationException();
		}
		/**
		 * @throws UnsupportedOperationException
		 *    if either this iterator does not allow remove  
		 *    or 
		 *{@link CollectionsExt.Modification#AddIter AddIter} 
		 *    is no allowed operation 
		 *    according to {@link #allowedModifications()}. 
		 */
		public void add(E element) {
		    if (ImmutableList.this.allowedModifications()
			.contains(Modification.AddIter)) {
			this.wrapped.add(element);
		    }
		    throw new UnsupportedOperationException();
		}
		/**
		 * @throws UnsupportedOperationException
		 *    if either this iterator does not allow remove  
		 *    or 
		 *{@link CollectionsExt.Modification#SetIter SetIter} 
		 *    is no allowed operation 
		 *    according to {@link #allowedModifications()}. 
		 */
		public void set(E element) {
		    if (ImmutableList.this.allowedModifications()
			.contains(Modification.SetIter)) {
			this.wrapped.add(element);
		    }
		    throw new UnsupportedOperationException();
		}
	    };
	}

	public E remove(int index) {
   	    if (allowedModifications().contains(Modification.RemoveObj)) {
		return unrestricted().remove(index);
    	    }
	    throw new UnsupportedOperationException();
	}

	public E set(int index, E element) {
   	    if (allowedModifications().contains(Modification.SetObj)) {
		return unrestricted().set(index, element);
    	    }
	    throw new UnsupportedOperationException();
	}

	public List<E> subList(int fromIndex, int toIndex) {
	    List<E> res0 = unrestricted().subList(fromIndex, toIndex);
	    return new ImmutableList<E>(allowedModifications(), res0);
	}

    } // class ImmutableList

    public final static class NonModifyingCyclicIterator<E> 
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

	public boolean retEquals(CyclicIterator<E> other) {
	    throw new NotYetImplementedException();
	}

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


    public final static class ImmutableCyclicList<E> implements CyclicList<E> {

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
	public <E> E[] toArray(int index,E[] array) {
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
	    throw new UnsupportedOperationException();// for cyclic lists 
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
     *    {@link CollectionsExt.ImmutableCollection#allowModification(CollectionsExt.Modification)}, 
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
     * @see #recToArray(Object,Class)
     * @see ArraysExt#recAsList(Object[])
     */
    public static Object[] recToArray(List<?> list) {

	Object[] result = new Object[list.size()];
	int index = 0;
	for (Object cand : list) {
	    if (cand instanceof List) {
		// cand is a list. 
		result[index] = recToArray((List)cand);
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
     * @see ArraysExt#recAsList(Object,Class)
     */
    public static Object recToArray(Object source,Class cls) {
	return recToArray(source,cls,Caster.BASIC_TYPES);
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
     * @see ArraysExt#recAsList(Object,Class,Caster)
     */
    public static Object recToArray(Object source,Class cls,Caster caster) {

	if (caster.areCompatible(cls,source)) {
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

	Collection<?> coll = (Collection<?>)source;
	Class<?> compType = cls.getComponentType();
	Object result = Array.newInstance(compType,coll.size());
	int ind = 0;
	for (Object cand : coll) {
	    // automatic unwrapping if needed. 
	    Array.set(result,ind++,recToArray(cand,compType,caster));
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
     *    if an element of an entry <code>new Object[] {key,value}</code> 
     *    of <code>keyVal</code> has not the right type. 
     */
/*
    public static <K,V> Object[][] fillMap(Map<K,V> map,Object[][] keyVal) {
	List<Object[]> replaced = new ArrayList<Object[]>();
	V repl;
	for (int i = 0; i < keyVal.length; i++) {
	    repl = map.put((K)keyVal[i][0],(V)keyVal[i][1]);
	    if (map.keySet().contains(keyVal[i][0])) {
		replaced.add(new Object[] {keyVal[i][0],repl});
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
	for (int i = 0; i < list.size(); i++) {
	    res.add(list.get(list.size()-1-i));
	}
	return res;
    }

    public static void main(String[] args) {
	
	    System.out.println(""+Collections.unmodifiableSortedSet(null));
	    System.out.println(""+Collections.unmodifiableSet(null));
	    System.out.println(""+Collections.unmodifiableCollection(null));
	
    }

}


