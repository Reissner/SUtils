
package eu.simuline.util;

import java.lang.reflect.Array;

import java.util.List;
//import java.util.Iterator;
import java.util.Collections;// NOPMD for javadoc only. 
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;

/**
 * An add on of the core class {@link java.util.Collections}. 
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
public class CollectionsAdd<E> {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * A class of immutable <code>SortedSet</code>s 
     * throwing an <code>UnsupportedOperationException</code> 
     * when trying to modify it. 
     * Note that they still have a <code>Comparator</code>. 
     */
    private final static class ImmutableSortedSet<E> extends TreeSet<E> {
	private static final long serialVersionUID = -2479143000061671589L;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	/**
	 * Creates a new empty <code>ImmutableSortedSet</code> 
	 * instance <code>s</code> 
	 * satisfying <code>s.comparator() == null</code>. 
	 */
	ImmutableSortedSet() {
	    super();
	}

	/**
	 * Creates a new <code>ImmutableSortedSet</code> 
	 * instance <code>s</code> 
	 * satisfying <code>s.comparator() == c</code>. 
	 *
	 * @param cmp 
	 *    a <code>Comparator</code> value 
	 *    which may well be <code>null</code>. 
	 */
	ImmutableSortedSet(Comparator<? super E> cmp) {
	    super(cmp);
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean add(E obj) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean remove(Object obj) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void clear() {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean addAll(Collection<? extends E> cmp) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean retainAll(Collection<?> cmp) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean removeAll(Collection<?> cmp) {
	    throw new UnsupportedOperationException();
	}
    } // class ImmutableSortedSet 

    /* -------------------------------------------------------------------- *
     * class fields.                                                        *
     * -------------------------------------------------------------------- */


    /* -------------------------------------------------------------------- *
     * class methods.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Returns an immutable <code>SortedSet</code> with the specified comparator
     *
     * @param cmp 
     *    a <code>Comparator</code> instance or <code>null</code>. 
     * @return 
     *    an empty <code>SortedSet</code> instance <code>s</code> 
     *    which throws an <code>UnsupportedOperationException</code> 
     *    when trying to modify it. 
     *    By contract, <code>s.isEmpty() == true</code> and 
     *    <code>s.comparator() == c</code>. 
     */
    public static <E> SortedSet 
    getImmutableSortedSet(Comparator<? super E> cmp) {
	return new ImmutableSortedSet<E>(cmp);
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
     * @see AddArrays#recAsList(Object[])
     */
    public static Object[] recToArray(List list) {

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
     * @see AddArrays#recAsList(Object,Class)
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
     *    satisfying <code>array.length == ((Collection)source).size()</code> and 
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
     * @see AddArrays#recAsList(Object,Class,Caster)
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

	Collection coll = (Collection)source;
	Class compType = cls.getComponentType();
	Object result = Array.newInstance(compType,coll.size());
	int ind = 0;
	for (Object cand : coll) {
	    // automatic unwrapping if needed. 
	    Array.set(result,ind++,recToArray(cand,compType,caster));
	}
	return result;
    }

    /**
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
}
