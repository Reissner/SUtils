package eu.simuline.util;

import java.util.List;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Compared to a classical list,  
 * the first index of this list may well be positive 
 * and negative indices are allowed as well. 
 * <p>
 * As a consequence, one can add elements not only at the end of this list 
 * but also at its beginning. 
 * For details consider {@link #add(Object)}, {@link #addFirst(Object)} 
 * and {@link #addLast(Object)}. 
 * Method {@link #add(int ind, Object obj)} is not supported 
 * because inserting an element 
 * requires either shift of subsequent elements to the right 
 * or preceeding elements to the left. 
 * To determine the direction of the shift 
 * use {@link #add(int ind, Object obj, Direction dir)} instead. 
 * Similar considerations apply to methods removing elements. 
 * Also affected are the corresponding collections operations 
 * like <code>addAll</code>. 
 * <p>
 * Methods {@link #toArray()} and {@link #toArray(Object[])} 
 * satisfy a generalized contract 
 * based on the additional method {@link #firstIndex()}. 
 * <p>
 * Essentially this two sided list wrapps a classical list. 
 * Various constructors allow to pass that list. 
 * This allows to determine the performance behavior. 
 * The signatures of the constructors 
 * generalize the constructors known 
 * from implementations of classical <code>List</code>s. 
 * The observant reader observes 
 * that the generics are slightly more restrictive than for classical lists. 
 * This is for performance reasons. 
 * Note that the constructors do not create a copy of the wrapped list 
 * which may cause hidden dependencies. 
 * If full generality is needed 
 * the user is asked to use the corresponding factory methods. 
 * <p>
 * Additionally methods concerning indices are provided 
 * like {@link #firstIndex()}, {@link #minFreeIndex()} 
 * and it is possible to shift a list using {@link #shiftRight(int)}. 
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

    /**
     * Used as an argument for methods adding or removing elements 
     * from this list 
     * to determine in which direction this list has to shrink or grow. 
     */
    public static enum Direction {
	Left2Right {
	    // api-docs provided by javadoc 
	    void checkRange(int ind, TwoSidedList list) {
		list.checkRange("",
				ind,
				list.firstIndex(),
				list.minFreeIndex()+1);
	    }
	    // api-docs provided by javadoc 
	    void checkAdd1(TwoSidedList list) {
		list.checkIncMinFreeIndex();
	    }
	    void checkAddAll(int size, TwoSidedList list) {
		list.checkMinFreeIndex(size);
	    }
	}, Right2Left {
	    // api-docs provided by javadoc 
	    void checkRange(int ind, TwoSidedList list) {
		list.checkRange("",
				ind,
				list.firstIndex()-1,
				list.minFreeIndex());
	    }
	    // api-docs provided by javadoc 
	    void checkAdd1(TwoSidedList list) {
		list.decFirstIndex();
	    }
	    void checkAddAll(int size, TwoSidedList list) {
		list.subFirstIndex(size);
	    }
	};

	/**
	 * Checks whether index <code>ind</code> 
	 * is in the range of <code>list</code> 
	 * and throws an appropriate exception if not. 
	 *
	 * @param ind
	 *    the index to be checked. 
	 * @param list
	 *    the twosided list under consideration. 
 	 * @throws IndexOutOfBoundsException
	 *    <ul>
	 *    <li> for <code>this == Left2Right</code>if not 
	 *    <code>list.firstIndex()   <= ind&lt;list.minFreeIndex()+1</code>.
	 *    <li> for <code>this == Right2Left</code>if not 
	 *    <code>list.firstIndex()-1 <= ind&lt;list.minFreeIndex()  </code>.
	 *    </ul>
	 *    The message is always the same: 
	 *    <code>
	 * "Index: <ind> Range: <firstIndex> - <minFreeIndex()> exclusively. "
	 *    </code>. 
	 * @see #add   (int, Object,     Direction)
	 * @see #addAll(int, Collection, Direction)
	 *
	 * <!--used by 
	 * add   (int, Object obj, Direction)
	 * addAll(int, Collection, Direction) 
	 * -->
	 */
	abstract void checkRange(int ind, TwoSidedList list);

	/**
	 * Checks in {@link TwoSidedList#add(int, Object, Direction)} 
	 * whether by adding elements 
	 * causes underrun in {@link TwoSidedList#firstIndex()} 
	 * or      overrun in {@link TwoSidedList#minFreeIndex()}. 
	 * 
	 * @param list
	 *    the twosided list under consideration. 
	 * @throws IllegalStateException
	 *    if adding an object to this list would 
	 *    cause underrun in {@link TwoSidedList#firstIndex()} 
	 *    or     overrun in {@link TwoSidedList#minFreeIndex()} 
	 *    depending on this direction. 
	 * @see TwoSidedList#decFirstIndex()
	 * @see TwoSidedList#checkIncMinFreeIndex() 
	 * <!-- used only in add(int, E, Direction)  -->
	 */
	abstract void checkAdd1(TwoSidedList list);

	/**
	 * Checks in {@link TwoSidedList#addAll(int, Collection, Direction)} 
	 * whether by adding elements 
	 * causes underrun in {@link TwoSidedList#firstIndex()} 
	 * or      overrun in {@link TwoSidedList#minFreeIndex()}. 
	 * 
	 * @param size
	 *    the number of elements to be added. 
	 * @param list
	 *    the twosided list under consideration. 
	 * @throws IllegalStateException
	 *    if adding <code>size</code> objects to this list would 
	 *    cause underrun in {@link TwoSidedList#firstIndex()} 
	 *    or     overrun in {@link TwoSidedList#minFreeIndex()} 
	 *    depending on this direction. 
	 */
	abstract void checkAddAll(int size, TwoSidedList list);

    } // enum Direction 

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * The list backed by this two-sided list. 
     */
    private List<E> list;

    /**
     * The first index in this <code>TwoSidedList</code>. 
     * Note that this integer may well be negative. 
     * The inequality 
     * <code>{@link #minFreeIndex()} >= {@link #firstIndex}</code> 
     * is guaranteed. 
     * Casually, methods adding objects have to reject them 
     * in order not to hurt this 
     * alhough the backed list {@link #list} 
     * would allow adding further elements. 
     */
    private int firstIndex;

    /* -------------------------------------------------------------------- *
     * constructors and static factory methods.                             *
     * -------------------------------------------------------------------- */

    /**
     * Creates a new <code>TwoSidedList</code> 
     * containing the elements of {@link #list} in proper sequence 
     * with first index given by {@link #firstIndex}. 
     * <p>
     * Note the difference to reference implementations such as 
     * <code>java.util.ArrayList</code> where the type of the list argument 
     * is <code>List<? extends E></code>. 
     * We deviate from this solution for performance reason 
     * and provide as an alternative 
     * the factory method {@link #create(List,int)}. 
     * <p>
     * CAUTION: 
     * This list backs {@link #list} and so changes to one of the list 
     * affect the other list. 
     *
     * @param list 
     *    the list wrapped by this twosided list. 
     * @param firstIndex
     *    the index where this list starts growing. 
     * @throws IllegalStateException
     *    if <code>list</code> is so long 
     *    and <code>firstIndex</code> is so large 
     *    that {@link #minFreeIndex()} would overrun. 
     */
    public TwoSidedList(List<E> list, int firstIndex) {
	this.list = list;
	this.firstIndex = firstIndex;
	checkMinFreeIndex(list.size());
    }

    /**
     * Creates a new <code>TwoSidedList</code> 
     * containing the elements of <code>list</code> in proper sequence 
     * with first index given by <code>firstIndex</code>. 
     *
     * @param list 
     *    the list wrapped by this twosided list. 
     *    Changes to <code>list</code> do not influence this twosided list. 
     * @param firstIndex
     *    the index where this list starts growing. 
     * @throws IllegalStateException
     *    if <code>list</code> is so long 
     *    and <code>firstIndex</code> is so large 
     *    that {@link #minFreeIndex()} would overrun. 
     */
    public static <E> TwoSidedList<E> create(List<? extends E> list, 
					     int firstIndex) {
	return new TwoSidedList<E>(new ArrayList<E>(list), firstIndex);
    }

    /**
     * Creates a new <code>TwoSidedList</code> 
     * from a <code>List</code> with  <code>firstIndex == 0</code>. 
     * This is the canonical generalization of lists 
     * as mentioned in the documentation 
     * of {@link #indexOf(Object)} and of {@link #lastIndexOf(Object)}. 
     * <p>
     * Note the difference to reference implementations such as 
     * <code>java.util.ArrayList</code> where the type of the list argument 
     * is <code> List<? extends E></code>. 
     * We deviate from this solution for performance reason 
     * and provide as an alternative 
     * the factory method {@link #create(List,int)}. 
     * <p>
     * CAUTION: 
     * Changes to <code>list</code> influence this twosided list 
     * and may cause malfunction. 
     * Note that unlike {@link #TwoSidedList(List, int)} 
     * this constructor cannot throw an <code>IllegalStateException</code>. 
     *
     * @param list 
     *    the list wrapped by this twosided list. 
     */
    public TwoSidedList(List<E> list) {
	this(list,0);
    }

    /**
     * Creates a new <code>TwoSidedList</code> 
     * containing the elements of <code>list</code> in proper sequence. 
     *
     * @param list 
     *    the list wrapped by this twosided list. 
     *    Changes to <code>list</code> do not influence this twosided list. 
     */
    public static <E> TwoSidedList<E> create(List<? extends E> list) {
	return new TwoSidedList<E>(new ArrayList<E>(list));
    }

    /**
     * Copy constructor with shallow copy of the wrapped list {@link #list}. 
     * As a consequence, modifications of the list created 
     * may affect the original one and the other way round. 
     * Note that unlike {@link #TwoSidedList(List, int)} 
     * this constructor cannot throw an <code>IllegalStateException</code>. 
     *
     * @param other
     *    another <code>TwoSidedList</code>. 
     */
    public TwoSidedList(TwoSidedList<E> other) {
	this(other.list,other.firstIndex);

    }

    /**
     * Creates a new <code>TwoSidedList</code> 
     * as a copy of <code>other</code> 
     * copying also the wrapped list {@link #list}. 
     * As a consequence, the list created and the original one 
     * act independently. 
     * Note that unlike {@link #TwoSidedList(List, int)} 
     * this constructor cannot throw an <code>IllegalStateException</code>. 
     *
     * @param other
     *    another <code>TwoSidedList</code>. 
     */
    public static <E> TwoSidedList<E> create(TwoSidedList<? extends E> other) {
	return new TwoSidedList<E>(new ArrayList<E>(other.list),
				   other.firstIndex);
    }

    /**
     * Creates a new empty <code>TwoSidedList</code> which starts growing 
     * with index <code>firstIndex</code>. 
     * Note that unlike {@link #TwoSidedList(List, int)} 
     * this constructor cannot throw an <code>IllegalStateException</code>. 
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

    /**
     * Checks whether index <code>ind</code> is in range 
     * and throws an appropriate exception if not. 
     *
     * @param ind
     *    the index to be checked. 
     * @throws IndexOutOfBoundsException
     *    if not <code>firstIndex() <= ind &lt; minFreeIndex()</code> 
     *    with message 
     *    <code>
     * "Index: <ind> Range: <firstIndex> - <minFreeIndex()> exclusively. "
     *    </code>. 
     * <!-- used by 
     * get(int), 
     * set(int, obj), 
     * remove(int ind, Direction) **** is this correct? 
     * -->
     */
    private void checkRange(int ind) {
	checkRange("",ind,this.firstIndex,minFreeIndex());
    }

    /**
     * Checks whether index <code>ind</code> is in range 
     * and throws an appropriate exception if not. 
     *
     * @param ind
     *    the index to be checked. 
     * @param dir
     *    the direction in which this twosided list may grow. 
     * @throws IndexOutOfBoundsException
     *    <ul>
     *    <li> for <code>dir == Left2Right</code>
     *    if not <code>firstIndex() <= ind &lt; minFreeIndex()+1</code>. 
     *    <li> for <code>dir == Right2Left</code>
     *    if not <code>firstIndex()-1 <= ind &lt; minFreeIndex()</code>. 
     *    </ul>
     *    The message is always the same: 
     *    <code>
     * "Index: <ind> Range: <firstIndex> - <minFreeIndex()> exclusively. "
     *    </code>. 
     * @see #add(int, Object, Direction)
     * @see #addAll(int, Collection, Direction)
     *
     * <!--used by 
     * add   (int, Object obj, Direction)
     * addAll(int, Collection, Direction) 
     * -->
     */
    private void checkRange(int ind, Direction dir) {
	dir.checkRange(ind,this);
    }

    /**
     * Checks whether index <code>ind</code> is in range 
     * and throws an appropriate exception if not. 
     *
     * @param fromToNothing
     *    either <code>""</code>, <code>"from"</code> or <code>"to"</code>. 
     *    The latter two cases are used only in {@link #subList(int,int)} 
     *    to check the range of the start index and of the end index 
     *    of the sublist to be created. 
     * @param ind
     *    the index to be checked. 
     * @param min
     *    the minimal value for <code>ind</code> accepted. 
     * @param maxP
     *    <em>one plus</em> the maximal value for <code>ind</code> accepted. 
     * @throws IndexOutOfBoundsException
     *    if not <code>min <= ind &lt; maxP</code>. 
     *    The message is 
     *    <code>
     * "Index: <ind> Range: <firstIndex> - <minFreeIndex()> exclusively. "
     *    </code> preceeded by <code>fromToNothing</code>. 
     *
     * <!--used by 
     * Direction.checkRange(int, TwoSidedList) 
     * checkRange(int)
     * listIterator(int)
     * subList(int, int)
     * -->
     */
    private void checkRange(String fromToNothing, 
			    int ind, 
			    int min, 
			    int maxP) {
	if (ind < min || ind >= maxP) {
	    throw new IndexOutOfBoundsException
		(fromToNothing + "Index: " + ind + 
		 " Range: " + this.firstIndex + 
		 " - " + minFreeIndex() + " exclusively. ");
	}
    }

    /**
     * Decrements {@link #firstIndex} if possible; 
     * otherwise throws an exception. 
     * This method is used by methods adding objects at the head of this list. 
     * The error message is tailored to this usage. 
     *
     * @throws IllegalStateException
     *    if <code>{@link #firstIndex} == Integer.MIN_VALUE</code> 
     *    adding an element would cause index underrun. 
     */
    private void decFirstIndex() {
	if (this.firstIndex == Integer.MIN_VALUE) {
	    throw new IllegalStateException
		("Adding an object at top of this list " + 
		 "would cause index underrun. ");
	}
	this.firstIndex--;
    }

    /**
     * Increments {@link #firstIndex} if possible; 
     * otherwise throws an exception. 
     * This method is used by methods removing objects 
     * from the head of this list. 
     * The error message is tailored to this usage. 
     *
     * @throws IllegalStateException
     *    if <code>{@link #firstIndex} == Integer.MAX_VALUE</code> 
     *    removing an element would cause index overrun. 
     */
    private void incFirstIndex() {
	if (this.firstIndex == Integer.MAX_VALUE) {
	    throw new IllegalStateException
		("Removing an object at top of this list " + 
		 "would cause index overrun. ");
	}
	this.firstIndex++;
    }

    /**
     * Checks whether incrementing {@link #minFreeIndex()} 
     * would cause overrun of {@link #minFreeIndex()}. 
     *
     * @throws IllegalStateException
     *    if incrementing {@link #minFreeIndex()} 
     *    would cause overrun of {@link #minFreeIndex()}. 
     */
    private void checkIncMinFreeIndex() {
	if (minFreeIndex() == Integer.MAX_VALUE) {
	    throw new IllegalStateException
		("Adding an object at the tail of this list " + 
		 "would cause index overrun. ");
	}
    }

    /**
     * Subtracts <code>numAdded</code> from {@link #firstIndex} if possible; 
     * otherwise throws an exception. 
     * This method is used by methods adding objects at the head of this list. 
     * The error message is tailored to this usage. 
     *
     * @throws IllegalStateException
     *    if adding <code>numAdded</code> objects 
     *    at the head of this list 
     *    would cause underrun of {@link #firstIndex}. 
     */
     private void subFirstIndex(int numAdded) {
	if (this.firstIndex < this.firstIndex-numAdded) {
	    throw new IllegalStateException
		("Adding " + numAdded + " objects at top of this list " + 
		 "would cause index underrun. ");
	}
	this.firstIndex -= numAdded;
    }

    /**
     * Checks whether adding <code>numAdded</code> objects 
     * to this list **** or at the tail of this list 
     * would cause overrun of {@link #minFreeIndex()}. 
     *
     * @throws IllegalStateException
     *    if adding <code>numAdded</code> objects 
     *    to this list **** or at the tail of this list 
     *    would cause overrun of {@link #minFreeIndex()}. 
     */
    private void checkMinFreeIndex(int numAdded) {
	assert  numAdded >= 0;
	if (minFreeIndex() > minFreeIndex()+numAdded) {
	    throw new IllegalStateException
		("Adding " + numAdded + " objects at the tail of this list " + 
		 "would cause index overrun. ");
	}
    }

    /**
     * Returns {@link #firstIndex}. 
     */
    public int firstIndex() {
	return this.firstIndex;
    }

    /**
     * Sets {@link #firstIndex} according to the parameter. 
     */
    public void firstIndex(int firstIndex) {
	this.firstIndex = firstIndex;
    }

    // 
    public int minFreeIndex() {
	return this.list.size() + this.firstIndex;
    }

    /**
     * Shifts this list <code>num</code> indices to the right. 
     *
     * @param num 
     *    the number of shifts to the right 
     *    to be performed on this list. 
     *    A negative sign signifies a shift to the left. 
     * @return
     *    The new first index. 
     * @throws IllegalStateException
     *    if shifting would cause overrun of {@link #minFreeIndex()} 
     *    (occuring for proper shift to the right)
     *    or underrun of {@link #firstIndex()}
     *    (occuring for proper shift to the left). 
     */
    public int shiftRight(int num) {
	if (num > 0) {
	    if (minFreeIndex() > minFreeIndex()+num) {
		throw new IllegalStateException
		    ("Shifting this list by " + num + 
		     " would cause index overrun. ");
	    }
	} else {
	    assert num <= 0;
	    if (this.firstIndex < this.firstIndex+num) {
		throw new IllegalStateException
		    ("Shifting this list by " + num + 
		     " would cause index underrun. ");
	    }
	}

	return this.firstIndex += num;
    }

    // caution: not wrapped. 
    public List<E> list() {
	return this.list;
    }

    /* -------------------------------------------------------------------- *
     * methods implementing List.                                           *
     * -------------------------------------------------------------------- */

    /**
     * Returns the number of elements in this list. 
     * If this list 
     * contains more than <code>Integer.MAX_VALUE</code> elements, 
     * returns <code>Integer.MAX_VALUE</code>. 
     * **** gives rise to malfunction. **** 
     *
     * @return 
     *    an <code>int</code> value
     */
    public final int size() {
	return this.list.size();
    }

    /**
     * Returns true if this list contains no elements. 
     *
     * @return 
     *    whether this list contains no elements 
     *    as a <code>boolean</code> value. 
     */
    public final boolean isEmpty() {
	return this.list.isEmpty();
    }

    /**
     * Returns whether this list contains the specified element. 
     * More formally, returns <code>true</code> 
     * if and only if this list contains at least one element <code>obj</code> 
     * such that <code>(o==null ? e==null : o.equals(e))</code>.     
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    a <code>boolean</code> value
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    is incompatible with {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     */
    public final boolean contains(final Object obj) {
	return this.list.contains(obj);
    }

    /**
     * Returns the index of the first occurrence 
     * of the specified element <code>obj</code> in this list, 
     * or {@link #firstIndex()}-1 if this list does not contain the element. 
     * More formally, returns the lowest index <code>i</code> 
     * such that <code>(obj==null ? get(i)==null : obj.equals(get(i)))</code>, 
     * or {@link #firstIndex()}-1 if there is no such index. 
     * <p>
     * CAUTION: 
     * <ul>
     * <li>
     * This breaks **** "extends" contract for the interface List: 
     * Test for "element <code>obj</code> not in list" 
     * is no longer <code>this.indexOf(obj) == -1</code> but 
     * <code>this.indexOf(obj) < this.firstIndex()-1</code>. 
     * This is an extension in that 
     * wrapping an ordinary list in a twosided list 
     * is by specifying <code>firstIndex() == 0</code> 
     * (see {@link #TwoSidedList(List list)}). 
     * <li>
     * Note that for <code>firstIndex() == {@link Integer#MIN_VALUE}</code> 
     * <code>firstIndex()-1 > firstIndex()</code>. 
     * </ul>
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    <ul>
     *    <li>
     *    the index of the first occurrence of <code>obj</code> 
     *    if this object is in this list. 
     *    In this case, the return value is within the range 
     *    <code>firstIndex()..firstFreeIndex()-1</code>. 
     *    <li>
     *    <code>firstIndex()-1</code> if <code>obj</code> is not in this list. 
     *    </ul>
     */
    public final int indexOf(final Object obj) {
	return this.list.indexOf(obj)+this.firstIndex;
    }

    /**
     * Returns the index of the last occurrence 
     * of the specified element <code>obj</code> in this list, 
     * or {@link #firstIndex()}-1 if this list does not contain the element. 
     * More formally, returns the highest index <code>i</code> 
     * such that <code>(obj==null ? get(i)==null : obj.equals(get(i)))</code>, 
     * or {@link #firstIndex()}-1 if there is no such index. 
     * <p>
     * CAUTION: 
     * <ul>
     * <li>
     * This breaks **** "extends" contract for the interface List: 
     * Test for "element <code>obj</code> not in list" 
     * is no longer <code>this.indexOf(obj) == -1</code> but 
     * <code>this.indexOf(obj) < this.firstIndex()-1</code>. 
     * This is an extension in that 
     * wrapping an ordinary list in a twosided list 
     * is by specifying <code>firstIndex() == 0</code> 
     * (see {@link #TwoSidedList(List list)}). 
     * <li>
     * Note that for <code>firstIndex() == {@link Integer#MIN_VALUE}</code> 
     * <code>firstIndex()-1 > firstIndex()</code>. 
     * </ul>
     * **** one may expect 
     * that in case the specified object in not in the list 
     * 1+ the highest index is returned. 
     * this is in general not the case. 
     *
     * @param obj 
     *    an <code>Object</code> value
     * @return 
     *    <ul>
     *    <li>
     *    the index of the last occurrence of <code>obj</code> 
     *    if this object is in this list. 
     *    In this case, the return value is within the range 
     *    <code>firstIndex()..firstFreeIndex()-1</code>. 
     *    <li>
     *    <code>firstIndex()-1</code> if <code>obj</code> is not in this list. 
     *    </ul>
     */
    public final int lastIndexOf(final Object obj) {
	return this.list.lastIndexOf(obj)+this.firstIndex;
    }

    /**
     * Note that this generalizes the contract of the underlying interface: 
     * Instead of <code>this.toArray[i] == this.get(i)</code> 
     * now only <code>this.toArray[i] == this.get(i-firstIndex())</code>. 
     * Equality includes that the left hand side throws an exception 
     * if and only if so does te right hand side. 
     *
     * @return 
     *    an <code>Object[]</code> containing all elements in proper sequence. 
     */
    // api-docs provided by javadoc. 
    public final Object[] toArray(){
	return this.list.toArray();
    }

    /**
     * Note that this generalizes the contract of the underlying interface: 
     * Instead of <code>this.toArray[i] == this.get(i)</code> 
     * now only <code>this.toArray[i] == this.get(i-firstIndex())</code>. 
     * Equality includes that the left hand side throws an exception 
     * if and only if so does te right hand side. 
     *
     * @param objArr 
     *    an <code>Object[]</code> value
     * @return 
     *    an <code>Object[]</code> value
     */
    // api-docs provided by javadoc. 
    public final <E> E[] toArray(final E[] objArr) {
	return this.list.toArray(objArr);
    }

    /**
     * Returns the element at the specified position in this list. 
     *
     * @param ind 
     *    the index of the element to return as an <code>int</code> value. 
     * @return 
     *    the element at the specified position in this list. 
     * @throws IndexOutOfBoundsException
     *    as described for {@link #checkRange(int)}
     */
    public final E get(final int ind) {
	checkRange(ind);
	return this.list.get(ind-this.firstIndex);
    }

    /**
     * Replaces the element at the <code>ind</code>th position 
     * in this list with the specified element (optional operation).
     *
     * @param ind 
     *    the index of the element to replace as an <code>int</code> value. 
     * @param obj 
     *    the element to be stored at the specified position. 
     * @return 
     *    the element previously at the <code>ind</code>th position 
     * @throws UnsupportedOperationException 
     *    if the <code>set</code> operation is not supported 
     *    by {@link #list}. 
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException 
     *    if some property of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    as described for {@link #checkRange(int)}
     */
    public final E set(final int ind, final E obj) {
	checkRange(ind);
	return this.list.set(ind-this.firstIndex,obj);
    }


    /**
     * Not supported by this implementation. **** breaks contract 
     *
     * @throws UnsupportedOperationException
     *    use {@link #addFirst} and {@link #addLast} instead. 
     */
    public final boolean add(final E obj) {
	throw new UnsupportedOperationException
	    ("Use addFirst(Object) or addLast(E) instead. ");
    }

    /**
     * Adds <code>obj</code> at the beginning of this list. 
     * The first index returned by {@link #firstIndex()} 
     * is decremented. 
     *
     * @param obj 
     *    the <code>E</code> object to be added. 
     * @return 
     *    <code>true</code> by specification. 
     * @throws UnsupportedOperationException 
     *    if the <code>add(int,E)</code> operation is not supported 
     *    by {@link #list}. 
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException 
     *    if some property of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws IllegalStateException
     *    if <code>{@link #firstIndex} == Integer.MIN_VALUE</code> 
     *    adding an element would cause index underrun. 
     */
    public final boolean addFirst(final E obj) {
	// may throw an IllegalStateException 
	decFirstIndex();
	this.list.add(0,obj);
	return true;
    }

    /**
     * Adds <code>obj</code> at the end of this list. 
     * The first index returned by {@link #firstIndex()} remains unchanged. 
     *
     * @param obj 
     *    the <code>E</code> object to be added. 
     * @return 
     *    <code>true</code> by specification. 
     * @throws UnsupportedOperationException 
     *    if the <code>add(E)</code> operation is not supported 
     *    by {@link #list}. 
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException 
     *    if some property of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws IllegalStateException
     *    if adding objects to this list 
     *    would cause overrun of {@link #minFreeIndex()}. 
     */
    public final boolean addLast(final E obj) {
	checkIncMinFreeIndex();
	return this.list.add(obj);
    }

    /**
     * Not supported by this implementation. **** breaks contract 
     *
     * @throws UnsupportedOperationException
     *    use {@link #add(int,Object,Direction)} instead. 
     */
    public final void add(final int ind, final E obj) {
	throw new UnsupportedOperationException
	    ("Use add(int,E,Direction) instead. ");
    }

    /**
     * Inserts <code>obj</code> at the specified position <code>ind</code> 
     * in this list (optional operation). 
     * Shifts the element currently at that position (if any) 
     * and any following/preceeding elements to the direction <code>dir</code> 
     * (increments/decrements their indices).
     *
     * @param ind 
     *    the index where to insert <code>obj</code>. 
     *    After having performed this operation, 
     *    <code>ind</code> is the index of <code>obj</code>. 
     * @param obj 
     *    the <code>E</code> element to be inserted. 
     * @param dir 
     *    determines the direction to shift the list. 
     *    <ul>
     *    <li><code>Left2Right</code>: 
     *    shifts all subsequent objects in this list 
     *    starting with index <code>ind</code> to the right 
     *    adding one to their indices. 
     *    <li><code>Right2Left</code>: 
     *    shifts all objects in this list 
     *    to index <code>ind</code> to the left 
     *    subtracting one from their indices. 
     *    </ul>
     * @throws UnsupportedOperationException 
     *    if the <code>add</code> operation 
     *    is not supported by {@link #list}. 
     * @throws ClassCastException 
     *    if the class of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if <code>obj == null</code> 
     *    and {@link #list} does not permit <code>null</code> elements. 
     * @throws IllegalArgumentException 
     *    if some property of <code>obj</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    as described for {@link #checkRange(int,Direction)}
     */
    public final void add(final int ind, final E obj, final Direction dir) {
	checkRange(ind,dir);
	dir.checkAdd1(this);
	this.list.add(ind-this.firstIndex, obj);
    }

    /**
     * Not supported by this implementation. **** breaks contract 
     *
     * @throws UnsupportedOperationException
     *    use {@link #remove(int,Direction)} instead. 
     */
    public final E remove(final int ind) {
	throw new UnsupportedOperationException
	    ("Use remove(int,Direction) instead. ");
    }

    /**
     * Removes the element at the specified position in this list 
     * (optional operation). 
     * Shifts any following/preceeding elements 
     * to the direction <code>dir</code> 
     * (decrements/increments their indices). 
     * Returns the element that was removed from the list. 
     *
     * @param ind 
     *    the index of the element to be removed 
     *    as an <code>int</code> value. 
     * @return 
     *    the element previously at the specified position. 
     * @throws UnsupportedOperationException 
     *    if the <code>remove(int)</code> operation 
     *    is not supported by {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    as described for {@link #checkRange(int)}
     */
    public final E remove(final int ind, Direction dir) {
	checkRange(ind);
	E res = this.list.remove(ind-this.firstIndex);
	if (dir == Direction.Right2Left) {
	    // Note that this may not throw any exception in this case. 
	    incFirstIndex();
	}

	return res;
    }

    /**
     * Not supported by this implementation. **** breaks contract 
     *
     * @throws UnsupportedOperationException
     *    use {@link #removeFirst(Object)} and {@link #removeLast(Object)} instead. 
     */
    public final boolean remove(final Object obj) {
	throw new UnsupportedOperationException
	    ("Use removeFirst(E) or removeLast(E) instead. ");
    }

    /**
     * Removes the first occurrence of <code>obj</code> from this list, 
     * if present (optional operation). 
     * If this list does not contain <code>obj</code>, it is unchanged. 
     * More formally, removes the element with the lowest index <code>i</code> 
     * such that <code>(obj==null ? get(i)==null : obj.equals(get(i)))</code> 
     * (if such an element exists). 
     * Returns <code>true</code> if this list contained the specified element 
     * (or equivalently, if this list changed as a result of the call). 
     * The first index returned by {@link #firstIndex()} remains unchanged. 
     *
     * @param obj
     *    the element to be removed from this list, if present. 
     * @return
     *    whether this list contained the specified element
     * @throws UnsupportedOperationException 
     *    if the <code>remove(Object)</code> operation is not supported 
     *    by {@link #list}. 
     */
    public final boolean removeFirst(final Object obj) {
	return this.list.remove(obj);
    }

    /**
     * Removes the last occurrence of <code>obj</code> from this list, 
     * if present (optional operation). 
     * If this list does not contain <code>obj</code>, it is unchanged. 
     * More formally, 
     * removes the element with the highest index <code>i</code> 
     * such that <code>(obj==null ? get(i)==null : obj.equals(get(i)))</code> 
     * (if such an element exists). 
     * Returns <code>true</code> if this list contained the specified element 
     * (or equivalently, if this list changed as a result of the call). 
     * The first index returned by {@link #firstIndex()} is incremented 
     * if really an object was removed. 
     *
     * @param obj
     *    the element to be removed from this list, if present. 
     * @return
     *    whether this list contained the specified element
     * @throws UnsupportedOperationException 
     *    if the <code>remove(int)</code> **** not remove(Object) 
     *    operation is not supported 
     *    by {@link #list}. 
     */
    public final boolean removeLast(final Object obj) {
	int ind = this.list.lastIndexOf(obj);
	if (ind == -1) {
	    // obj \notin this 
	    return false;
	} else {
	    // obj \in this 
	    // Note that this may not throw any exception in this case. 
	    incFirstIndex();
	    this.list.remove(ind);
	    return true;
	}
    }

    /**
     * Removes all of the elements from this list (optional operation). 
     * The list will be empty after this call returns 
     * and {@link #firstIndex} is unmodified. 
     *
     * @throws UnsupportedOperationException 
     *    if the clear operation is not supported by {@link #list}. 
     */
    public final void clear() {
	this.list.clear();
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #addAllFirst} and {@link #addAllLast} instead. 
     */
    public final boolean addAll(final Collection<? extends E> coll) {
	throw new UnsupportedOperationException
	    ("Use addAllFirst or addAllLast instead. ");
    }

    /**
     * Adds <code>obj</code> at the beginning of this list. 
     * in the order that they are returned 
     * by <code>coll</code>'s iterator (optional operation). 
     * The behavior of this operation is undefined 
     * if the specified collection is modified 
     * while the operation is in progress. 
     * (Note that this will occur if <code>coll</code> is this list, 
     * and it's nonempty.)
     * The first index returned by {@link #firstIndex()} 
     * is reduced by <code>coll</code>'s size. 
     *
     * @param coll 
     *    a <code>Collection</code> value
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws    UnsupportedOperationException 
     *    if the addAll operation is not supported by this {@link #list}.  
     * @throws    ClassCastException 
     *    if the class of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     * @throws    NullPointerException 
     *    if the specified collection contains 
     *    one or more <code>null</code> elements 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if the specified collection is <code>null</code> 
     * @throws    IllegalArgumentException 
     *    if some property of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     * @throws IllegalStateException
     *    if adding <code>numAdded</code> objects 
     *    at the head of this list 
     *    would cause underrun of {@link #firstIndex}. 
     */
    public final boolean addAllFirst(final Collection<? extends E> coll) {
	subFirstIndex(coll.size());
	return this.list.addAll(0,coll);
    }

    /**
     * Appends all of the elements in <code>coll</code> 
     * to the end of this list, 
     * in the order that they are returned 
     * by <code>coll</code>'s iterator (optional operation). 
     * The behavior of this operation is undefined 
     * if the specified collection is modified 
     * while the operation is in progress. 
     * (Note that this will occur if <code>coll</code> is this list, 
     * and it's nonempty.)
     * The first index returned by {@link #firstIndex()} remains unchanged. 
     *
     * @param coll 
     *    another <code>Collection</code>. 
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws     UnsupportedOperationException 
     *    if the addAll operation is not supported by this {@link #list}.  
     * @throws    ClassCastException 
     *    if the class of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     * @throws    NullPointerException 
     *    if the specified collection contains 
     *    one or more <code>null</code> elements 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if the specified collection is <code>null</code> 
     * @throws    IllegalArgumentException 
     *    if some property of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     * @throws IllegalStateException
     *    if adding <code>numAdded</code> objects 
     *    to this list **** or at the tail of this list 
     *    would cause overrun of {@link #minFreeIndex()}. 
     */
    public final boolean addAllLast(final Collection<? extends E> coll) {
	checkMinFreeIndex(coll.size());
	return this.list.addAll(coll);
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #addAll(int,Collection,Direction)} instead. 
     */
    public final boolean addAll(final int ind, 
				final Collection<? extends E> coll) {
	throw new UnsupportedOperationException
	    ("Use addAll(int,Collection,Direction) instead. ");
    }

    /**
     * Inserts all of the elements in <code>coll</code> into this list 
     * at the specified position (optional operation). 
     * Shifts the elements currently at that positions (if any) 
     * and any following/preceeding elements to the direction <code>dir</code> 
     * (increasing/decreasing their indices by <code>coll</code>'s size). 
     * The new elements will appear in this list in the order 
     * that they are returned by the specified collection's iterator. 
     * The behavior of this operation is undefined 
     * if the specified collection is modified 
     * while the operation is in progress. 
     * (Note that this will occur if the specified collection is this list, 
     * and it's nonempty.)
     *
     * @param ind 
     *    index at which to insert the first element from <code>coll</code> 
     *    as an <code>int</code> value. 
     *    CAUTION: Note that <code>ind</code> 
     *    always references the first element in <code>coll</code> 
     *    independent from <code>dir</code>. 
     * @param coll 
     *    a <code>Collection</code> 
     *    containing elements to be added to this list. 
     * @param dir 
     *    determines the direction to shift the list. 
     *    <ul>
     *    <li><code>Left2Right</code>: 
     *    shifts all subsequent objects in this list 
     *    starting with index <code>ind</code> to the right 
     *    adding one to their indices. 
     *    <li><code>Right2Left</code>: 
     *    shifts all objects in this list 
     *    to index <code>ind</code> to the left 
     *    subtracting one from their indices. 
     *    </ul>
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws UnsupportedOperationException 
     *    if the <code>addAll</code> operation 
     *    is not supported by this {@link #list}.  
     * @throws ClassCastException 
     *    if the class of an element of <code>coll</code> 
     *    prevents it from being added to {@link #list}. 
     * @throws NullPointerException 
     *    if the <code>coll</code> contains 
     *    at least one <code>null</code> element 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if <code>coll == null</code>. 
     * @throws IllegalArgumentException 
     *    if some property of an element of the specified collection 
     *    prevents it from being added to {@link #list}. 
     * @throws IndexOutOfBoundsException
     *    as described for {@link #checkRange(int,Direction)}
     * @throws IllegalStateException
     *    if adding <code>coll</code> to this list 
     *    would cause underrun of {@link #firstIndex} 
     *    or overrun of {@link #minFreeIndex}. 
     */
    public final boolean addAll(final int ind, 
				final Collection<? extends E> coll, 
				final Direction dir) {
	checkRange(ind,dir);
	dir.checkAddAll(coll.size(),this);
	return this.list.addAll(ind-this.firstIndex, coll);
    }

    /*
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return 
     *    an iterator over the elements in this list in proper sequence. 
     */
    // api-docs provided by javadoc. 
    public final Iterator<E> iterator() {
	return this.list.iterator();
    }

    /*
     * Returns a list iterator over the elements in this list 
     * (in proper sequence). 
     *
     * @return 
     *    a <code>ListIterator</code> 
     */
    // api-docs provided by javadoc 
    public final ListIterator<E> listIterator() {
	return this.list.listIterator();
    }

    /**
     * ****maybe to be endowed with a direction. 
     *
     * @param ind 
     *    an <code>int</code> value
     * @return 
     *    a <code>ListIterator</code> value
     * @throws IndexOutOfBoundsException
     *    if not <code>firstIndex() &lt;= ind &lt;= minFreeIndex()</code> 
     *    with message 
     *    <code>
     * "Index: <ind> Range: <firstIndex> - <minFreeIndex()> exclusively. "
     *    </code>. 
     */
    public final ListIterator<E> listIterator(final int ind) {
	checkRange("",ind,this.firstIndex,minFreeIndex()+1);
	return this.list.listIterator(ind-this.firstIndex);
    }

    /**
     * Replaces the element at the <code>ind</code>th position 
     * in this list with the specified element (optional operation).
     *
     * @param coll 
     *    a <code>Collection</code> value
     * @return 
     *    a <code>boolean</code> value
     * @throws ClassCastException 
     *    if the class of one or more elements of <code>coll</code> 
     *    is incompatible with {@link #list}. 
     * @throws NullPointerException 
     *    if <code>coll</code> contains at least one <code>null</code> element 
     *    and {@link #list} is incompatible with <code>null</code> elements 
     *    or if <code>coll == null</code>. 
     */
    public final boolean containsAll(final Collection<?> coll) {
	return this.list.containsAll(coll);
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #removeAll(Collection,Direction)} instead. 
     */
    public final boolean removeAll(final Collection<?> coll) {
	throw new UnsupportedOperationException
	    ("Use removeAll(Collection,Direction) instead. ");
    }

    /**
     * Removes from this list all of its elements 
     * that are contained in <code>coll</code> (optional operation). 
     * Shifts the elements currently at that position (if any) 
     * and any following/preceeding elements to the direction <code>dir</code> 
     * (decreasing/increasing  their indices 
     * by the number of elements removed). 
     *
     * @param coll 
     *    a <code>Collection</code> 
     *    containing elements to be removed from this list. 
     * @param dir 
     *    determines the direction to shift the list. 
     *    <ul>
     *    <li><code>Left2Right</code>: 
     *    to close gaps by removing elements 
     *    shifts all objects preceeding gaps to the right 
     *    adding one to their indices. 
     *    <li><code>Right2Left</code>: 
     *    to close gaps by removing elements 
     *    shifts all objects following gaps to the left  
     *    subtracting one from their indices. 
     *    </ul>
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws UnsupportedOperationException 
     *    if the <code>removeAll</code> operation 
     *    is not supported by this {@link #list}.  
     * @throws ClassCastException 
     *    if the class of an element of <code>coll</code> 
     *    is incompatible with {@link #list}. 
     * @throws NullPointerException 
     *    if the <code>coll</code> contains 
     *    at least one <code>null</code> element 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if <code>coll == null</code>. 
     */
    public final boolean removeAll(final Collection<?> coll, Direction dir) {
	switch (dir) {
	    case Left2Right:
		return this.list.removeAll(coll);
	    case Right2Left:
		int oldSize = this.list.size();
		boolean ret = this.list.removeAll(coll);
		// This retains the inequality 
		// firstIndex() <= firstFreeIndex() 
		this.firstIndex += oldSize - this.list.size();
		return ret;
	    default:
		throw new IllegalStateException();
	}
    }

    /**
     * Not supported by this implementation. 
     *
     * @throws UnsupportedOperationException
     *    use {@link #retainAll(Collection,Direction)} instead. 
     */
    public final boolean retainAll(final Collection<?> coll) {
	throw new UnsupportedOperationException
	    ("Use retainAll(Collection,Direction) instead. ");
    }

    /**
     * Retains only the elements in this list 
     * that are contained in <code>coll</code> (optional operation). 
     * In other words, removes from this list all the elements 
     * that are not contained in <code>coll</code>. 
     * Shifts the elements currently at that position (if any) 
     * and any following/preceeding elements to the direction <code>dir</code> 
     * (decreasing/increasing  their indices 
     * by the number of elements removed). 
     *
     * @param coll 
     *    a <code>Collection</code> 
     *    containing elements to be retained in this list. 
     * @param dir 
     *    determines the direction to shift the list. 
     *    <ul>
     *    <li><code>Left2Right</code>: 
     *    to close gaps by removing elements 
     *    shifts all objects preceeding gaps to the right 
     *    adding one to their indices. 
     *    <li><code>Right2Left</code>: 
     *    to close gaps by removing elements 
     *    shifts all objects following gaps to the left  
     *    subtracting one from their indices. 
     *    </ul>
     * @return 
     *    whether this list changed as a result of the call 
     *    as a <code>boolean</code> value. 
     * @throws UnsupportedOperationException 
     *    if the <code>retainAll</code> operation 
     *    is not supported by this {@link #list}.  
     * @throws ClassCastException 
     *    if the class of an element of <code>coll</code> 
     *    is incompatible with {@link #list}. 
     * @throws NullPointerException 
     *    if the <code>coll</code> contains 
     *    at least one <code>null</code> element 
     *    and {@link #list} does not permit <code>null</code> elements 
     *    or if <code>coll == null</code>. 
     */
    public final boolean retainAll(final Collection<?> coll, Direction dir) {
	switch (dir) {
	    case Left2Right:
		return this.list.retainAll(coll);
	    case Right2Left:
		int oldSize = this.list.size();
		boolean ret = this.list.retainAll(coll);
		// This retains the inequality 
		// firstIndex() <= firstFreeIndex() 
		this.firstIndex += oldSize - this.list.size();
		return ret;
	    default:
		throw new IllegalStateException();
	}
    }

    /**
     * Returns a view of the portion of this twosided list as a list 
     * between the specified <code>fromIndex</code>, inclusive, 
     * and <code>toIndex</code>, exclusive. 
     *
     * @param indStart 
     *    low endpoint (inclusive) of the subList to be returned. 
     * @param indEnd 
     *    high endpoint (exclusive) of the subList to be returned. 
     * @return 
     *    view of the specified range within this list. 
     *    The returned list is backed by this list, 
     *    so non-structural changes in the returned list 
     *    are reflected in this list, and vice-versa. 
     * @throws IndexOutOfBoundsException
     *    if not 
     *    <code>firstIndex() <= indStart <= indEnd <= minFreeIndex()</code>
     */
    public final List<E> subList(final int indStart, final int indEnd) {
	if (indStart > indEnd) {
	    throw new IndexOutOfBoundsException
		("fromIndex(" + indStart + ") > toIndex(" + indEnd + ")");
	}
	// only one invocation can throw an exception. 
	checkRange("from",indStart,this.firstIndex,minFreeIndex()+1);
	checkRange("to",  indEnd,  this.firstIndex,minFreeIndex()+1);
	return this.list.subList(indStart-this.firstIndex, 
				 indEnd  -this.firstIndex);
    }
    /**
     * Returns a view of the portion of this twosided list 
     * between the specified <code>fromIndex</code>, inclusive, 
     * and <code>toIndex</code>, exclusive. 
     *
     * @param indStart 
     *    low endpoint (inclusive) of the subList to be returned. 
     * @param indEnd 
     *    high endpoint (exclusive) of the subList to be returned. 
     * @return 
     *    view of the specified range within this list. 
     *    The returned list is backed by this list, 
     *    so non-structural changes in the returned list 
     *    are reflected in this list, and vice-versa. 
     * @throws IndexOutOfBoundsException
     *    see {@link #subList(int,int)} 
     */
    public final TwoSidedList<E> subList2(final int indStart, 
					  final int indEnd) {
	return new TwoSidedList<E>(subList(indStart,indEnd),indStart);
    }

    /**
     * The given object equals this twosided list 
     * if and only if it is as well a <code>TwoSidedList</code>, 
     * the two lists wrapped {@link #list} coincide 
     * and either as well the first indices {@link #firstIndex} coincide. 
     * CAUTION: 
     * Note that two empty lists with different first index are not equal. 
     * This is justified by the fact, 
     * that these two become different when the same first element is added 
     * to both lists. 
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
     * Returns a hash code which conforms with {@link #equals(Object)}. 
     *
     * @return 
     *    the hash code as an <code>int</code> value. 
     */
    public final int hashCode() {
	return this.list.hashCode() + this.firstIndex;
    }

    // api-docs provided by javadoc 
    public String toString() {
	StringBuilder res = new StringBuilder();
	res.append("<TwoSidedList firstIndex=\"");
	res.append(this.firstIndex);
	res.append("\">");
	res.append(this.list);
	res.append("</TwoSidedList>");
	return res.toString();
    }

    public static void main(String[] args) {
    }
}
