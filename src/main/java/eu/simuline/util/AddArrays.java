
package eu.simuline.util;

import java.lang.reflect.Array;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;

/**
 * An add on to the class {@link java.util.Arrays}. 
 * Partially this is influenced by {@link java.util.Collections}. 
 *
 * @author <a href="mailto:ernst@rig29.rose.de">Ernst Reissner</a>
 * @version 1.0
 */
public class AddArrays<E> {

    /**
     * The class <code>double[]</code>. 
     */
    public final static Class DOUBLE_ARRAY1 = getArrayCls(Double.TYPE,1);

    /**
     * The class <code>double[][]</code>. 
     */
    public final static Class DOUBLE_ARRAY2 = getArrayCls(Double.TYPE,2);

    public final static Object[] EMPTY = new Object[] {};

    /**
     * Reverses the order of the elements in the specified array. 
     * This method runs in linear time.
     *
     * @param array 
     *    the array whose elements are to be reversed. 
     */
    public static void reverse(Object[] array) {
	Object tmp;
	for (int i = 0; i < array.length/2; i++) {
	    tmp = array[i];
	    array[i] = array[array.length-1-i];
	    array[array.length-1-i] = tmp;
	}
    }

    /**
     * Replaces all of the elements of the specified array 
     * with the specified element. 
     * This method runs in linear time.
     *
     * @param array 
     *    the array to be filled with the specified element. 
     * @param  obj 
     *    The element with which to fill the specified array. 
     */
    public static void fill(Object[] array,Object obj) {
	for (int i = 0; i < array.length; i++) {
	    array[i] = obj;
	}
    }

    /**
     * Returns an array consisting of <tt>n</tt> copies 
     * of the specified object. 
     * The newly allocated data object is tiny 
     * (it contains a single reference to the data object). 
     *
     * @param  num 
     *    the number of elements in the returned list.
     * @param  obj 
     *    the element to appear repeatedly in the returned list.
     * @return 
     *    an array consisting of <tt>n</tt> copies 
     * 	  of the specified object. 
     * @throws IllegalArgumentException 
     *    if n &lt; 0. 
     */
    public static Object[] nCopies(int num,Object obj) {
	if (num < 0) {
	     throw new IllegalArgumentException
		 ("Requested negative number of copies: " + num + ". ");
	}
	
	Object[] result = new Object[num];
	fill(result,obj);
	return result;
    }

    /**
     * Turns the given array recursively 
     * into a hierarchy of nested lists. 
     * Note that unlike Arrays.asList, this is not a simple wrapper! 
     *
     * @param array 
     *    an array of objects which may in turn be arrays of objects. 
     * @return 
     *    a list <code>list</code> of objects satisfying 
     *    <code>list.get(i) == array[i]</code> 
     *    if <code>array[i]</code> is not an <code>Object[]</code>; 
     *    <code>list.get(i) == recAsList(array[i])</code> otherwise. 
     *    Note that <code>list</code> may be a nested list. 
     * @see CollectionsAdd#recToArray(List)
     */
    public static List<Object> recAsList(Object[] array) {

	List<Object> result = new ArrayList<Object>();
	for (int i = 0; i < array.length; i++) {
	    if (array[i] instanceof Object[]) {
		result.add(recAsList((Object[])array[i]));
	    } else {
		result.add(array[i]);		 
	    }
	}
	return result;
    }

    // not so good because of ordering. 
    // use ListSet instead? 
    /**
     * Turns the given array recursively 
     * into a hierarchy of nested sets. 
     * Note that unlike Arrays.asList, this is not a simple wrapper 
     * and that the ordering gets lost 
     * 
     *
     * @param array 
     *    an array of objects which may in turn be arrays of objects. 
     * @return 
     *    a set <code>set</code> of objects satisfying 
     *    <code>set.contains(o) &lt;==> o == array[i]</code> 
     *    if <code>array[i]</code> is not an <code>Object[]</code>; 
     *    <code>o == recAsList(array[i])</code> otherwise. 
     *    Note that <code>set</code> may be a nested set. 
     */
    public static Set<Object> recAsSet(Object[] array) {

	Set<Object> result = new HashSet<Object>();
	for (int i = 0; i < array.length; i++) {
	    if (array[i] instanceof Object[]) {
		result.add(recAsSet((Object[])array[i]));
	    } else {
		result.add(array[i]);		 
	    }
	}
	return result;
    }

    /**
     * Converts <code>source</code> 
     * which is typically an array, 
     * recursively into an nested {@link java.util.List} 
     * with the given atomic entry type using the specified caster. 
     * Note that if <code>source</code> is not an array, 
     * it must be compatible with <code>cls</code> 
     * up to equivalence of basic classes and their wrappers 
     * and up to subclassing. 
     *
     * @param source 
     *    an arbitrary <code>Object</code>, 
     *    but typically an array as e.g. <code>Object[][]</code> 
     *    or <code>double[]</code>. 
     * @param cls 
     *    Up to compatibility defined by <code>caster</code>, 
     *    the type of the entries of the return value. 
     *    Recursive conversion from arrays to lists stops 
     *    if an entry of type compatible with <code>cls</code> is found. 
     *    <p>
     *    Note that this may also be a primitive type 
     *    such as <code>int</code> or an array type 
     *    <code>Double[][][]</code> or <code>List[][][]</code> 
     *    or even <code>int[][][]</code>. 
     * @return 
     *    <ul>
     *    <li>
     *    if <code>cls</code> and <code>source</code> 
     *    are assignment compatible 
     *    up to equivalence of basic classes and their wrappers, 
     *    <code>source</code> is returned. 
     *    <li>
     *    if <code>cls</code> and <code>source</code> are not compatible, 
     *    <code>source</code> must be an array; 
     *    otherwise an exception is thrown. 
     *    <p>
     *    In the former case, 
     *    a list <code>list</code> of objects is returned 
     *    satisfying <code>list.size() == Array.getLength(source)</code> and 
     *    <code>list.get(i) == recAsList(Array.get(source,i),cls)</code> 
     *    for all valid indices <code>i</code>. 
     *    <p>
     *    Note that the return value 
     *    is either the result of a casting process or a list. 
     *    In the latter case its entries are either lists themselves 
     *    or again the result of a casting and so on. 
     *    Clearly its atomic entries are always <code>Object</code>s 
     *    even when starting with sources 
     *    of type <code>int[]</code> for instance. 
     *    </ul>
     * @throws IllegalArgumentException 
     *    if <code>source</code> is neither an array, 
     *    nor assignment compatible with <code>cls</code> 
     *    up to equivalence between basic types and their wrappers. 
     *    Also if <code>source</code> <em>is</em> an array, 
     *    and this condition holds for some component. 
     *    This is a recursive definition. 
     */
    public static Object recAsList(Object source,Class cls) {
	return recAsList(source,cls,Caster.BASIC_TYPES);
    }

    /**
     * Converts <code>source</code> 
     * which is typically an array, 
     * recursively into an nested {@link java.util.List} 
     * with the given atomic entry type using the specified caster. 
     * Note that if <code>source</code> is not an array, 
     * it must be compatible with <code>cls</code> 
     * with respect to <code>caster</code>. 
     *
     * @param source 
     *    an arbitrary <code>Object</code>, 
     *    but typically an array as e.g. <code>Object[][]</code> 
     *    or <code>double[]</code>. 
     * @param cls 
     *    Up to compatibility defined by <code>caster</code>, 
     *    the type of the entries of the return value. 
     *    Recursive conversion from arrays to lists stops 
     *    if an entry of type compatible with <code>cls</code> is found. 
     *    <p>
     *    Note that this may also be a primitive type 
     *    such as <code>int</code> or an array type 
     *    <code>Double[][][]</code> or <code>List[][][]</code> 
     *    or even <code>int[][][]</code>. 
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
     *    Arrays are converted to <code>List</code>s 
     *    applying this method recursively to their entries. 
     *    <li>
     *    if <code>cls</code> and <code>source</code> are not compatible, 
     *    <code>source</code> must be an array; 
     *    otherwise an exception is thrown. 
     *    <p>
     *    In the former case, 
     *    a list <code>list</code> of objects is returned 
     *    satisfying <code>list.size() == Array.getLength(source)</code> and 
     *    <code>list.get(i) == recAsList(Array.get(source,i),.. ,caster)</code> 
     *    for all valid indices <code>i</code>. 
     *    <p>
     *    Note that the return value 
     *    is either the result of a casting process or a list. 
     *    In the latter case its entries are either lists themselves 
     *    or again the result of a casting and so on. 
     *    Clearly its atomic entries are always <code>Object</code>s 
     *    even when starting with sources 
     *    of type <code>int[]</code> for instance. 
     *    </ul>
     * @throws IllegalArgumentException 
     *    if <code>source</code> is neither an array, 
     *    nor compatible with <code>cls</code> 
     *    with respect to <code>caster</code>. 
     *    Also if <code>source</code> <em>is</em> an array, 
     *    and this condition holds for some component. 
     *    This is a recursive definition. 
     */
    public static Object recAsList(Object source,Class cls,Caster caster) {

	/// ***** problem: 
	// what about double[][][]? --- wrong caster?!? 
	// should use: Class compType = cls.getComponentType();
	if (caster.areCompatible(cls,source)) {
	    // Here, either source == null || cls.isInstance(source) 
	    // or source wraps something of type cls. 
	    return caster.cast(source);
	}
	// Here, source is not compatible with cls. 

	if (!source.getClass().isArray()) {
	    throw new IllegalArgumentException
		("Found incompatible types: " + source + " is no array. ");
	}
	// Here, source is an array and cls.isArray(). 

	List<Object> result = new ArrayList<Object>();

	for (int i = 0; i < Array.getLength(source); i++) {
	    // automatic wrapping of primitive objects if needed. 
	    result.add(recAsList(Array.get(source,i),cls,caster));
	}
	return result;
    }

    /**
     * Returns the class of an array with the given type of atomic entry 
     * and with the given dimension (dimension in the mathematical sense). 
     *
     * @param elementCls 
     *    The class of the element of the array class to be returned. 
     * @param dim 
     *    The dimension of the array; has to be non-negative. 
     * @return 
     *    The class of the array 
     *    <code>new (elementCls)[]...dim...[]</code>. 
     */
    public static Class getArrayCls(Class elementCls, int dim) {
	Class result = elementCls;
	for (int i = 0; i < dim; i++) {
	    result = Array.newInstance(result,0).getClass();
	}
	return result;
    }

    // ***** not yet ok 
    public static Class getArrayWrapperCls(Class cls) {

	if (cls.isArray()) {
	    return getArrayWrapperCls(cls.getComponentType());
	}
	// Here, cls is no array class. 
	return BasicTypesCompatibilityChecker.getWrapperCls(cls);
    }

    /**
     * Returns an empty array with type like <code>elemArray</code> 
     * with basic type replaced by its wrapper. 
     *
     * @param elemArray 
     *    an array with elementary entry type 
     *    such as <code>double[][][]</code>. 
     * @return 
     *    an empty array with type like <code>elemArray</code> 
     *    with basic type replaced by its wrapper. 
     *    For example 
     *    <code>createWrappedEmptyArray(new int[][][] {....})</code> 
     *    yields <code>new Integer[][][] {}</code>. 
     * @throws IllegalArgumentException 
     *    if <code>elemArray</code> is not an array 
     *    or if its entry type is not elementary 
     *    as e.g. for <code>new Integer(0)</code> 
     *    or for <code>new Integer[][] {}</code>.  
     */
    private static Object[] createWrappedEmptyArray(Object elemArray) {

	Object result;
	int counter = 0;
	Class type = elemArray.getClass();
	while (type.getComponentType() != null) {
	    counter++;
	    type = type.getComponentType();
	}
	// Here, type is no longer an array type. 

	try {
	    result = Array
	    .newInstance(BasicTypesCompatibilityChecker.getWrapperCls(type),
			 0);
	} catch (RuntimeException e) {// NOPMD
	    if (BasicTypesCompatibilityChecker.getWrapperCls(type) == null ||
		BasicTypesCompatibilityChecker.getWrapperCls(type) == Void.TYPE) {
		 throw new IllegalArgumentException
		     ("Expected array with basic type entries; found " + 
		      elemArray + ". ");
	    }
	    throw e;
	}
	while (counter > 1) {
	    counter--;
	    result = Array.newInstance(result.getClass(),0);
	}
	return (Object[])result;
    }

    /**
     * Returns an empty array with type like <code>wrappedArray</code> 
     * with basic type replaced by its wrapper. 
     *
     * @param wrappedArray 
     *    an array with wrapper entry type 
     *    such as <code>Double[][][]</code>. 
     * @return 
     *    an empty array with type like <code>wrapperArray</code> 
     *    with basic type replaced by its wrapper. 
     *    For example 
     *    <code>createUnWrappedEmptyArray(new Integer[][][] {....})</code> 
     *    yields <code>new int[][][] {}</code>. 
     * @throws IllegalArgumentException 
     *    if the entry type of <code>wrappedArray</code> is no wrapper type 
     *    as e.g. for <code>new int[][] {}</code>.  
     */
    private static Object createUnWrappedEmptyArray(Object[] wrappedArray) {

	Object result;
	int counter = 0;
	Class type = wrappedArray.getClass();
	while (type.getComponentType() != null) {
	    counter++;
	    type = type.getComponentType();
	}
	// Here, type is no longer an array type. 

	try {
	    result = Array
		.newInstance(BasicTypesCompatibilityChecker.getWrappedCls(type),
			     0);
	} catch (RuntimeException e) {// NOPMD
	    if (BasicTypesCompatibilityChecker.getWrappedCls(type) == null ||
		BasicTypesCompatibilityChecker.getWrapperCls(type) == Void.TYPE) {
		 throw new IllegalArgumentException
		     ("Expected array with wrapper type entries; found " + 
		      wrappedArray + ". ");
	    }
	    throw e;
	}

	while (counter > 1) {
	    counter--;
	    result = Array.newInstance(result.getClass(),0);
	}
	return result;
    }

    /**
     * Returns an array which corresponds with the given one 
     * except that the entries are wrapped. 
     *
     * @param elemArray 
     *    an array with elementary component type 
     *    as e.g. <code>new int[][] {}</code>. 
     *    May also be <code>null</code>. 
     * @return 
     *    <code>null</code> if <code>elemArray == null</code>. 
     *    an array with the same length as <code>elemArray</code>. 
     *    The type of the components is the wrapper type 
     *    of the type of the components of <code>elemArray</code>. 
     *    Also the entries are those of <code>elemArray</code> 
     *    just wrapped. 
     * @throws IllegalArgumentException
     *    if <code>elemArray</code> is not an array 
     *    or if its entry type is not elementary 
     *    as e.g. for <code>new Integer(0)</code> 
     *    or for <code>new Integer[][] {}</code>.  
     */
    public static Object[] wrapArray(Object elemArray) {

	if (elemArray == null) {
	    return null;
	}
	
	Object[] result;
	// The following throws an exception, if elemArray is not an array. 
	int len;
	try {
	    len = Array.getLength(elemArray);
	} catch (IllegalArgumentException e) {
	    throw new IllegalArgumentException
		("Required an array; found " + elemArray + ". ");
	}
	// Here, elemArray is an array and so compType is not null. 
	Class compType = elemArray.getClass().getComponentType();
	Class compWrapperType = BasicTypesCompatibilityChecker
	    .getWrapperCls(compType);
	if (compWrapperType == null) {
	    // Here, the entry type is not elementary. 
	    if (len == 0) {
		return createWrappedEmptyArray(elemArray);
	    }
	    Object[] wrap0th = wrapArray(Array.get(elemArray,0));
	    result = (Object[])Array.newInstance(wrap0th.getClass(),len);
	    result[0] = wrap0th;
	    for (int i = 1; i < len; i++) {
		result[i] = wrapArray(Array.get(elemArray,i));
	    }
	} else {
	    // Here, the entry type of elemArray is elementary, 
	    // e.g. elemArray has type int[]. 
	    result = (Object[])Array.newInstance(compWrapperType,len);
	    for (int i = 0; i < len; i++) {
		// Automatically wrapped. 
		result[i] = Array.get(elemArray,i);
	    }
	}
	return result;
    }

    /**
     * Converts an array of <code>Double</code>s 
     * into an array of according <code>double</code>s. 
     */
    public static double[] toPrimitive(Double[] arr) {
	double[] res = new double[arr.length];
	for (int i = 0; i < arr.length; i++) {
	    res[i] = arr[i];
	}
	return res;
    }

    /**
     * Converts an array of <code>Float</code>s 
     * into an array of according <code>float</code>s. 
     */
    public static float[] toPrimitive(Float[] arr) {
	float[] res = new float[arr.length];
	for (int i = 0; i < arr.length; i++) {
	    res[i] = arr[i];
	}
	return res;
    }

    /**
     * Converts an array of <code>Long</code>s 
     * into an array of according <code>long</code>s. 
     */
    public static long[] toPrimitive(Long[] arr) {
	long[] res = new long[arr.length];
	for (int i = 0; i < arr.length; i++) {
	    res[i] = arr[i];
	}
	return res;
    }

    /**
     * Converts an array of <code>Integer</code>s 
     * into an array of according <code>int</code>s. 
     */
    public static int[] toPrimitive(Integer[] arr) {
	int[] res = new int[arr.length];
	for (int i = 0; i < arr.length; i++) {
	    res[i] = arr[i];
	}
	return res;
    }

    /**
     * Converts an array of <code>Short</code>s 
     * into an array of according <code>short</code>s. 
     */
    public static short[] toPrimitive(Short[] arr) {
	short[] res = new short[arr.length];
	for (int i = 0; i < arr.length; i++) {
	    res[i] = arr[i];
	}
	return res;
    }

    /**
     * Converts an array of <code>Byte</code>s 
     * into an array of according <code>byte</code>s. 
     */
    public static byte[] toPrimitive(Byte[] arr) {
	byte[] res = new byte[arr.length];
	for (int i = 0; i < arr.length; i++) {
	    res[i] = arr[i];
	}
	return res;
    }

    /**
     * Converts an array of <code>Boolean</code>s 
     * into an array of according <code>boolean</code>s. 
     */
    public static boolean[] toPrimitive(Boolean[] arr) {
	boolean[] res = new boolean[arr.length];
	for (int i = 0; i < arr.length; i++) {
	    res[i] = arr[i];
	}
	return res;
    }

    /**
     * Converts an array of <code>Character</code>s 
     * into an array of according <code>char</code>s. 
     */
    public static char[] toPrimitive(Character[] arr) {
	char[] res = new char[arr.length];
	for (int i = 0; i < arr.length; i++) {
	    res[i] = arr[i];
	}
	return res;
    }

    /**
     * Returns an array which corresponds with the given one 
     * except that the entries are unwrapped. 
     *
     * @param wrappedArray 
     *    an array with a wrapper as component type 
     *    as e.g. <code>new Integer[][] {}</code>. 
     *    May also be <code>null</code>. 
     * @return 
     *    <code>null</code> if <code>wrappedArray == null</code>. 
     *    an array with the same length as <code>wrappedArray</code>. 
     *    The type of the components is the wrapped elementary type 
     *    of the type of the components of <code>wrappedArray</code>. 
     *    Also the entries are those of <code>wrappedArray</code> 
     *    just unwrapped. 
     * @throws IllegalArgumentException
     *    if the entry type of <code>wrappedArray</code> is no wrapper 
     *    as e.g. for <code>new int[][] {}</code> and for 
     *    <code>new Object[][] {}</code>.  
     */
    public static Object unWrapArray(Object[] wrappedArray) {

	if (wrappedArray == null) {
	    return null;
	}
	
	int len = wrappedArray.length;
	Class compType = wrappedArray.getClass().getComponentType();
	Class compWrappedType = BasicTypesCompatibilityChecker
	    .getWrappedCls(compType);
	if (compWrappedType == null) {
	    // Here, the entry type is not a wrapper. 
	    if (len == 0) {
		// Here, elemArray is at least two dimensional, e.g. int[][0] 
		return createUnWrappedEmptyArray(wrappedArray);
	    }
	    if (!compType.isArray()) {
		// Here, component type is neither array nor component. 
		 throw new IllegalArgumentException
		     ("Expected array with wrapper type entries; found " + 
		      wrappedArray + ". ");
	    }
	    // Here, compType is an array type. 
	    Object unWrap0th = unWrapArray((Object[])wrappedArray[0]);
	    Object[] result = (Object[])Array
		.newInstance(unWrap0th.getClass(),len);
	    result[0] = unWrap0th;
	    for (int i = 1; i < len; i++) {
		result[i] = unWrapArray((Object[])wrappedArray[i]);
	    }
	    return result;
	} else {
	    // Here, the entry type of elemArray is a wrapper, 
	    // e.g. wrappedArray has type Integer[]. 
	    Object result = Array.newInstance(compWrappedType,len);
	    for (int i = 0; i < len; i++) {
		// Automatically unwrapped. 
		Array.set(result,i,wrappedArray[i]);
	    }
	    return result;
	}
    }

    /**
     * Comparator class 
     * which implements a kind of lexical ordering on arrays 
     * based on the ordering of the components 
     * defined by <code>atomic</code>. 
     */
    static class ArrayComparator<Object> implements Comparator<Object[]> {

	/**
	 * A <code>Comparator</code> for components of an array. 
	 * This is allocated by the constructor 
	 * {@link #AddArrays.ArrayComparator}. 
	 */
	Comparator<Object> atomic;

	/**
	 * Creates a new <code>ArrayComparator</code> 
	 * to compare two object-arrays <!-- **** not so good --> 
	 * by lexical ordering. 
	 * Here, the orderin of the components 
	 * is defined by <code>atomic</code>. 
	 *
	 * @param atomic 
	 *    a <code>Comparator</code> for components of an array. 
	 */
	ArrayComparator(Comparator<Object> atomic) {
	    this.atomic = atomic;
	}

	/**
	 * Describe <code>compare</code> method here.
	 *
	 * @param arr1 
	 *    an <code>Object[]</code> object. 
	 *    The components <code>o1[i]</code> and <code>o2[i]</code> 
	 *    must be pairwise comparable with respect to {@link #atomic} 
	 *    provided both <code>o1[i]</code> and <code>o2[i]</code> exist. 
	 * @param arr2 
	 *    an <code>Object[]</code> object. 
	 *    The components <code>o1[i]</code> and <code>o2[i]</code> 
	 *    must be pairwise comparable with respect to {@link #atomic} 
	 *    provided both <code>o1[i]</code> and <code>o2[i]</code> exist. 
	 * @return 
	 *    <ul>
	 *    <li><code>0</code>
	 *    if and only if <code>o1.length == o2.length</code> 
	 *    and <code>atomic.compare(o1[i],o2[i]) == 0</code> 
	 *    for all indices <code>i</code>. 
	 *    Hence this comparator is consistent with equals 
	 *    if and only if this is true for {@link #atomic}. 
	 *
	 *    <li><code>-1</code> resp.<code>1</code> 
	 *    if <code>o1</code> is a true prefix of <code>o2</code> 
	 *    res. the other way round. 
	 *
	 *    <li><code>atomic.compare(o1[i],o2[i])</code> 
	 *    where <code>i</code> is the lowest index such that 
	 *    <code>atomic.compare(o1[i],o2[i]) != 0</code>. 
	 *    (Provided such an index in the common range exists. )
	 *    </ul>
	 * @throws NullPointerException 
	 *    if one of the arguments is <code>null</code>. 
	 * @throws ClassCastException 
	 *    if one of the arguments is no <code>Object[]</code>. 
	 *    in particular for type <code>int[]</code>. 
	 */
	public int compare(Object[] arr1, Object[] arr2) {
	    // throws NullPointerException if one of the arguments is null 
	    int minLen = Math.min(arr1.length,arr2.length);
	    int result;
	    for (int i = 0; i < minLen; i++) {
		result = this.atomic.compare(arr1[i],arr2[i]);
		if (result != 0) {
		    return result;
		}
	    } // end of for ()
	    return arr1.length-arr2.length;
	}

    } // class ArrayComparator 

    /**
     * Returns a comparator of the class {@link ArrayComparator} 
     * which implements a kind of lexical ordering on arrays 
     * based on the ordering of the components 
     * defined by <code>atomic</code>. 
     *
     * @param atomic 
     *    a <code>Comparator</code> for the components of arrays. 
     * @return 
     *    a <code>Comparator</code> for arrays. 
     */
    public static Comparator<Object[]> getComparator(Comparator<Object> atomic) {
	return new ArrayComparator<Object>(atomic);
    }
}
