
package eu.simuline.util;

import java.util.Map;
import java.util.HashMap;

/**
 * Provides methods to map basic types to their wrappers, 
 * to map wrapper types to the corresponding basic types 
 * and for compatibility checks. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public final class BasicTypesCompatibilityChecker {

    /* -------------------------------------------------------------------- *
     * class constants.                                                     *
     * -------------------------------------------------------------------- */

    /**
     * Maps wrapper classes to the corresponding basic classes. 
     * E.g. Boolean is mapped to boolean. 
     * Note that {@link Void#TYPE} is mapped to itself. 
     *
     * @see #areCompatible(Class, Object)
     */
    private static final Map<Class<?>, Class<?>> MAP_WRAPPER2BASIC_TYPE = 
	new HashMap<Class<?>, Class<?>>();

    /**
     * Maps basic classes to the corresponding wrapper classes. 
     * E.g. boolean is mapped to Boolean. 
     * Note that {@link Void#TYPE} is mapped to itself. 
     *
     * @see #areCompatible (Class, Object)
     */
    private static final Map<Class<?>, Class<?>> MAP_BASIC_TYPE2WRAPPER = 
	new HashMap<Class<?>, Class<?>>();


    /* -------------------------------------------------------------------- *
     * static initializer.                                                  *
     * -------------------------------------------------------------------- */

    /**
     * Initializes {@link #MAP_WRAPPER2BASIC_TYPE} and 
     * {@link #MAP_BASIC_TYPE2WRAPPER}. 
     */
    static {
	// initialize MAP_WRAPPER2BASIC_TYPE 
	MAP_WRAPPER2BASIC_TYPE.put(Boolean  .class, Boolean  .TYPE);
	MAP_WRAPPER2BASIC_TYPE.put(Character.class, Character.TYPE);
	MAP_WRAPPER2BASIC_TYPE.put(Byte     .class, Byte     .TYPE);
	MAP_WRAPPER2BASIC_TYPE.put(Short    .class, Short    .TYPE);
	MAP_WRAPPER2BASIC_TYPE.put(Integer  .class, Integer  .TYPE);
	MAP_WRAPPER2BASIC_TYPE.put(Long     .class, Long     .TYPE);
	MAP_WRAPPER2BASIC_TYPE.put(Float    .class, Float    .TYPE);
	MAP_WRAPPER2BASIC_TYPE.put(Double   .class, Double   .TYPE);
	MAP_WRAPPER2BASIC_TYPE.put(Void     .TYPE,  Void     .TYPE);

	// initialize MAP_BASIC_TYPE2WRAPPER 
	MAP_BASIC_TYPE2WRAPPER.put(Boolean  .TYPE, Boolean  .class);
	MAP_BASIC_TYPE2WRAPPER.put(Character.TYPE, Character.class);
	MAP_BASIC_TYPE2WRAPPER.put(Byte     .TYPE, Byte     .class);
	MAP_BASIC_TYPE2WRAPPER.put(Short    .TYPE, Short    .class);
	MAP_BASIC_TYPE2WRAPPER.put(Integer  .TYPE, Integer  .class);
	MAP_BASIC_TYPE2WRAPPER.put(Long     .TYPE, Long     .class);
	MAP_BASIC_TYPE2WRAPPER.put(Float    .TYPE, Float    .class);
	MAP_BASIC_TYPE2WRAPPER.put(Double   .TYPE, Double   .class);
	MAP_BASIC_TYPE2WRAPPER.put(Void     .TYPE, Void     .TYPE);
    } // static 

    
    /**
     * Prevents <code>BasicTypesCompatibilityChecker</code> 
     * from being instantiated.
     */
    private BasicTypesCompatibilityChecker() {}

    /* -------------------------------------------------------------------- *
     * static methods.                                                      *
     * -------------------------------------------------------------------- */

    /**
     * Returns whether <code>cls</code> represents a primitive type. 
     * Note that also {@link Void#TYPE} is a primitive type. 
     * An implementation of {@link Class#isPrimitive()}. 
     *
     * @return
     *    Whether <code>cls</code> represents a primitive type. 
     * @see #wrapsPrimitive(Class)
     */
    public static boolean isPrimitive(Class<?> cls) {
	return MAP_BASIC_TYPE2WRAPPER.keySet().contains(cls);
    }

    /**
     * Returns whether <code>cls</code> represents a type 
     * wrapping a primitive type. 
     * As an example {@link Integer} wraps <code>int</code> and 
     * {@link Void#TYPE} wraps itself. 
     *
     * @return
     *    Whether <code>cls</code> wraps a primitive type. 
     * @see #isPrimitive(Class)
     */
    public static boolean wrapsPrimitive(Class<?> cls) {
	return MAP_WRAPPER2BASIC_TYPE.keySet().contains(cls);
    }


    /**
     * Decides whether the given class and object are compatible. 
     *
     * @param cls 
     *    a <code>Class</code> (of course not <code>null</code>). 
     * @param obj 
     *    an <code>Object</code> including <code>null</code>. 
     * @return 
     *    <code>true</code> if and only if either of the following holds: 
     *    <ul>
     *    <li> <code>cls</code> does not refer to a primitive type 
     *         and <code>obj</code> may be casted to <code>cls</code>, i.e. 
     *         if either <code>obj == null</code> 
     *         or (sequentially) <code>cls.isInstance(obj)</code>. 
     *    <li> <code>cls</code> refers to a primitive type 
     *         and (sequentially) <code>obj != null</code> 
     *         and <code>obj.getClass()</code> 
     *         is the corresponding wrapper type. 
     *    </ul>
     * @see #MAP_WRAPPER2BASIC_TYPE 
     */
    public static boolean areCompatible(Class<?> cls, Object obj) {

	if (cls.isPrimitive()) {
	    // Here, cls represents a primitive class. 
	    if (obj == null) {
		// objects of a primitive class may not be null.  
		return false;
	    }
	    assert obj != null;
	    return getWrapperCls(cls, false).isInstance(obj);
	    // Class<?> basicType = getWrappedCls(obj.getClass(), true);
	    // // return true includes basicType != null. 
	    // return cls.equals(basicType);
	}
	// Here, cls represents a non-primitive class. 

	// Test whether obj may be casted to cls. 
	return obj == null || cls.isInstance(obj);
    }

    /**
     * Maps a wrapper class <code>cls</code> to wrapped primitive class. 
     *
     * @param cls 
     *    some <code>Class</code> object. 
     * @param allowsVoid
     *    whether {@link Void#TYPE} is wrapped by itself. 
     * @return 
     *    the primitive type wrapped type <code>cls</code> 
     *    if <code>cls</code> is the wrapper of a primitive type. 
     *    Note that {@link Void#TYPE} may be wrapped by itself. 
     * @throws IllegalArgumentException
     *    if <code>cls</code> wraps no primitive type 
     *    or if <code>cls</code> is {@link Void#TYPE} 
     *    but this is not allowed by <code>allowsVoid</code>. 
     */
    public static Class<?> getWrappedCls(Class<?> cls, boolean allowsVoid) {
	Class<?> res = MAP_WRAPPER2BASIC_TYPE.get(cls);
	if (res == null || (!allowsVoid && res == Void.TYPE)) {
	    throw new IllegalArgumentException
		("Expected wrapper class of primitive type " + 
		 (allowsVoid ? "or" : "but not") + 
		 " void; found " + cls + ". ");
	}
	assert res != null && (allowsVoid || res != Void.TYPE);
	return res;
    }

    /**
     * Maps a primitive class <code>cls</code> 
     * to the corresponding wrapper class. 
     *
     * @param cls 
     *    some <code>Class</code> object. 
     * @param allowsVoid
     *    whether {@link Void#TYPE} wraps itself. 
     * @return 
     *    the wrapper type of type <code>cls</code> 
     *    if <code>cls</code> is a primitive type. 
     *    Note that {@link Void#TYPE} may wrap itself. 
     * @throws IllegalArgumentException
     *    if <code>cls</code> is no primitive type 
     *    or if <code>cls</code> is {@link Void#TYPE} 
     *    but this is not allowed by <code>allowsVoid</code>. 
     */
    public static Class<?> getWrapperCls(Class<?> cls, boolean allowsVoid) {
	Class<?> res = MAP_BASIC_TYPE2WRAPPER.get(cls);
	if (res == null || (!allowsVoid && res == Void.TYPE)) {
	    throw new IllegalArgumentException
		("Expected primitive type " + 
		 (allowsVoid ? "or" : "but not") + 
		 " void; found " + cls + ". ");
	}
	assert res != null && (allowsVoid || res != Void.TYPE);
	return res;
    }
}

