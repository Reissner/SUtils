package eu.simuline.util;

import java.util.List;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Describe class TwoSidedList here.
 *
 *
 * Created: Sun Aug 26 23:25:26 2007
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
public class TwoSidedList<E> implements List<E> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    enum Direction {
	Left2Right, Right2Left;
    } // enum Direction 

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    private List<E> list;

    /**
     * the first index in this <code>TwoSidedList</code>. 
     * Note that this integer may well be negative. 
     */
    private int firstIndex;

    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Creates a new <code>TwoSidedList</code> 
     * containing the elements of <code>list</code> in proper sequence 
     * with first index given by <code>firstIndex</code>. 
     *
     * @param firstIndex
     *    the index where this list starts growing. 
     * @param list 
     *    the list wrapped by this twosided list. 
     */
    public TwoSidedList(List<E> list, int firstIndex) {
	this.list = list;
	this.firstIndex = firstIndex;
    }

    /**
     * Creates a new empty <code>TwoSidedList</code> which starts growing 
     * with index <code>firstIndex</code>. 
     *
     * @param firstIndex 
     *    the index where this list starts growing. 
     */
    public TwoSidedList(int firstIndex) {
	this(new ArrayList<E>(),firstIndex);
    }

    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */


    int firstIndex() {
	return this.firstIndex;
    }

    void firstIndex(int firstIndex) {
	this.firstIndex = firstIndex;
    }

    // num may also be a negativeNumber
    int shiftRight(int num) {
	return this.firstIndex += num;
    }


//<code></code>

// Implementation of java.util.List

    public final boolean add(final E obj) {
	throw new UnsupportedOperationException
	    ("Use addFirst and addLast instead. ");
    }

    /**
     * Describe <code>add</code> method here. 
     *
     * @param obj 
     *    the <code>E</code> object to be added 
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean addLast(final E obj) {
	return this.list.add(obj);
    }

    public final boolean addFirst(final E obj) {
	this.firstIndex--;
	this.list.add(0,obj);
	return true;
    }

    public final void add(final int ind, final E obj) {
	throw new UnsupportedOperationException
	    ("Use add(int,E,Direction) instead. ");
    }

    /**
     * Describe <code>add</code> method here. 
     *
     * @param ind 
     *    an <code>int</code> value
     * @param obj 
     *    an <code>E</code> value
     * @param dir ****
     * @param dir 
     *    determines the direction to shift the list. 
     *    <ul>
     *    <li><code>Left2Right</code>: 
     *    just append <code>obj</code> at the end of the list. 
     *    <li><code>Right2Left</code>: 
     *    set <code>obj</code> at the end of the list 
     *    and shift the list one to the left such that . 
     *    </ul>
     */
    public final void add(final int ind, final E obj, Direction dir) {
	switch (dir) {
	    case Left2Right:
		this.list.add(ind-this.firstIndex, obj);
		break;
	    case Right2Left:
		this.firstIndex--;
		this.list.add(ind-this.firstIndex, obj);
		break;
	    default:
		throw new IllegalStateException();
	}
    }

    /**
     * Describe <code>indexOf</code> method here. 
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    an <code>int</code> value
     */
    public final int indexOf(final Object obj) {
	return this.list.indexOf(obj)+this.firstIndex;
    }

    /**
     * Describe <code>clear</code> method here. 
     *
     */
    public final void clear() {
	this.list.clear();
    }

    /**
     * Describe <code>contains</code> method here. 
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean contains(final Object obj) {
	return this.list.contains(obj);
    }

    /**
     * Describe <code>isEmpty</code> method here. 
     *
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean isEmpty() {
	return this.list.isEmpty();
    }

    /**
     * Describe <code>lastIndexOf</code> method here. 
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    an <code>int</code> value
     */
    public final int lastIndexOf(final Object obj) {
	return this.list.lastIndexOf(obj)+this.firstIndex;
    }

    /**
     * Describe <code>addAll</code> method here. 
     *
     * @param coll 
     *    a <code>Collection</code> value
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean addAll(final Collection<? extends E> coll) {
	throw new UnsupportedOperationException
	    ("Use addAllLast and addAllFirst instead. ");
    }

    public final boolean addAllFirst(final Collection<? extends E> coll) {
	this.firstIndex -= coll.size();
	return this.list.addAll(0,coll);
    }

    public final boolean addAllLast(final Collection<? extends E> coll) {
	return this.list.addAll(coll);
    }

    /**
     * Describe <code>addAll</code> method here. 
     *
     * @param ind 
     *    an <code>int</code> value
     * @param coll 
     *    a <code>Collection</code> value
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean addAll(final int ind, 
				final Collection<? extends E> coll) {
	throw new UnsupportedOperationException
	    ("Use addAll(int,Collection,Direction) instead. ");
    }

    public final boolean addAll(final int ind, 
				final Collection<? extends E> coll, 
				final Direction dir) {
	switch (dir) {
	    case Left2Right:
		return this.list.addAll(ind-this.firstIndex, coll);
    	    case Right2Left:
		this.firstIndex -= coll.size();
		return this.list.addAll(ind-this.firstIndex, coll);
	    default:
		throw new IllegalStateException();
	}
    }


    /**
     * Describe <code>get</code> method here. 
     *
     * @param ind 
     *    an <code>int</code> value
     * @return 
     *    an <code>E</code> value
     */
    public final E get(final int ind) {
	return this.list.get(ind-this.firstIndex);
    }

    /**
     * Describe <code>iterator</code> method here. 
     *
     * @return 
     *    an <code>Iterator</code> value
     */
    public final Iterator<E> iterator() {
	return this.list.iterator();
    }

    /**
     * Describe <code>size</code> method here. 
     *
     * @return 
     *    an <code>int</code> value
     */
    public final int size() {
	return this.list.size();
    }

    /**
     * Describe <code>toArray</code> method here. 
     *
     * @return 
     *    an <code>Object[]</code> value
     */
    public final Object[] toArray() {
	return this.list.toArray();
    }

    /**
     * Describe <code>toArray</code> method here. 
     *
     * @param objArr 
     *    an <code>Object[]</code> value
     * @return 
     *    an <code>Object[]</code> value
     */
    public final <E> E[] toArray(final E[] objArr) {
	return this.list.toArray(objArr);
    }

    /**
     * Describe <code>remove</code> method here. 
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean remove(final Object obj) {
	throw new UnsupportedOperationException
	    ("Use add(int,E,Direction) instead. ");
    }

    public final boolean removeFirst(final Object obj) {
	return this.list.remove(obj);
    }

    public final boolean removeLast(final Object obj) {
	throw new eu.simuline.util.NotYetImplementedException();
	//return this.list.remove(obj);
    }

    /**
     * Describe <code>remove</code> method here. 
     *
     * @param ind 
     *    an <code>int</code> value
     * @return 
     *    an <code>Object</code> value
     */
    public final E remove(final int ind) {
	throw new UnsupportedOperationException
	    ("Use remove(int,Direction) instead. ");
    }

    public final E remove(final int ind, Direction dir) {
	switch (dir) {
	    case Left2Right:
		return this.list.remove(ind-this.firstIndex);
	    case Right2Left:
		this.firstIndex--;
		return this.list.remove(ind-this.firstIndex);
	    default:
		throw new IllegalStateException();
	}
    }

    /**
     * Describe <code>set</code> method here. 
     *
     * @param ind 
     *    an <code>int</code> value
     * @param obj 
     *    an <code>E</code> value
     * @return 
     *    an <code>Object</code> value
     */
    public final E set(final int ind, final E obj) {
	return this.list.set(ind-this.firstIndex,obj);
    }

    /**
     * Describe <code>containsAll</code> method here. 
     *
     * @param coll 
     *    a <code>Collection</code> value
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean containsAll(final Collection coll) {
	return this.list.containsAll(coll);
    }

    /**
     * Describe <code>removeAll</code> method here. 
     *
     * @param coll 
     *    a <code>Collection</code> value
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean removeAll(final Collection coll) {
	throw new UnsupportedOperationException
	    ("Use removeAll(Collection,Direction) instead. ");
    }

    public final boolean removeAll(final Collection coll, Direction dir) {
	switch (dir) {
	    case Left2Right:
		return this.list.removeAll(coll);
	    case Right2Left:
		int oldSize = this.list.size();
		boolean ret = this.list.removeAll(coll);
		this.firstIndex -= oldSize - this.list.size();
		return ret;
	    default:
		throw new IllegalStateException();
	}
    }

    /**
     * Describe <code>retainAll</code> method here. 
     *
     * @param coll 
     *    a <code>Collection</code> value
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean retainAll(final Collection coll) {
	throw new UnsupportedOperationException
	    ("Use retainAll(Collection,Direction) instead. ");
    }

    public final boolean retainAll(final Collection coll, Direction dir) {
	switch (dir) {
	    case Left2Right:
		return this.list.retainAll(coll);
	    case Right2Left:
		int oldSize = this.list.size();
		boolean ret = this.list.retainAll(coll);
		this.firstIndex -= oldSize - this.list.size();
		return ret;
	    default:
		throw new IllegalStateException();
	}
    }

    /**
     * Describe <code>subList</code> method here. 
     *
     * @param indStart 
     *    an <code>int</code> value
     * @param indEnd 
     *    an <code>int</code> value
     * @return 
     *    a <code>List</code> value
     */
    public final List<E> subList(final int indStart, final int indEnd) {
	return this.list.subList(indStart-this.firstIndex, 
				 indEnd  -this.firstIndex);
    }

    /**
     * Describe <code>listIterator</code> method here. 
     *
     * @return 
     *    a <code>ListIterator</code> value
     */
    public final ListIterator<E> listIterator() {
	return this.list.listIterator();
    }

    /**
     * Describe <code>listIterator</code> method here. 
     *
     * @param ind 
     *    an <code>int</code> value
     * @return 
     *    a <code>ListIterator</code> value
     */
    public final ListIterator<E> listIterator(final int ind) {
	return this.list.listIterator(ind-this.firstIndex);
    }

    /**
     * The given object equals this twosided list 
     * if and only if it is as well a <code>TwoSidedList</code>, 
     * the two lists wrapped {@link #list} coincide 
     * and either as well the first indices {@link #firstIndex} coincide. 
     * CAUTION: 
     * Note that two empty lists with different first index are not equal. 
     * This is justified by the fact, 
     * that these two become different when the firsts element is added. 
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    a <code>boolean</code> value
     */
    public final boolean equals(final Object obj) {
	if (!(obj instanceof TwoSidedList)) {
	    return false;
	}
	TwoSidedList other = (TwoSidedList)obj;

	return this.list.equals(other.list) 
	    && (this.firstIndex == other.firstIndex);
    }

    /**
     * Describe <code>hashCode</code> method here. 
     *
     * @return 
     *    an <code>int</code> value
     */
    public final int hashCode() {
	return 
	    this.list.hashCode() + this.firstIndex;
    }

}
