
package eu.simuline.util;

import java.util.Map;
import java.util.HashMap;

/**
 * Provides methods to map basic types to their wrappers, 
 * to map wrapper types to the corresponding basic types 
 * and for compatibility checks. 
 *
 * @author <a href="mailto:">Ernst Reissner</a>
 * @version 1.0
 */
public abstract class BasicTypesCompatibilityChecker {

    /**
     * Maps wrapper classes to the corresponding basic classes. 
     * E.g. Boolean is mapped to boolean. 
     * Note that {@link Void#TYPE} is mapped to itself. 
     * @see #areCompatible 
     */
    private final static Map<Class<?>,Class<?>> MAP_WRAPPER2BASIC_TYPE = 
	new HashMap<Class<?>,Class<?>>();

    /**
     * Maps basic classes to the corresponding wrapper classes. 
     * E.g. boolean is mapped to Boolean. 
     * Note that Void is mapped to Void. 
     * @see #areCompatible 
     */
    private final static Map<Class<?>,Class<?>> MAP_BASIC_TYPE2WRAPPER = 
	new HashMap<Class<?>,Class<?>>();


    /**
     * Initializes {@link #MAP_WRAPPER2BASIC_TYPE} and 
     * {@link #MAP_BASIC_TYPE2WRAPPER}. 
     */
    static {
	BasicTypesCompatibilityChecker.MAP_WRAPPER2BASIC_TYPE
	    .put(Boolean  .class, Boolean  .TYPE);
	BasicTypesCompatibilityChecker.MAP_WRAPPER2BASIC_TYPE
	    .put(Character.class, Character.TYPE);
	BasicTypesCompatibilityChecker.MAP_WRAPPER2BASIC_TYPE
	    .put(Byte     .class, Byte     .TYPE);
	BasicTypesCompatibilityChecker.MAP_WRAPPER2BASIC_TYPE
	    .put(Short    .class, Short    .TYPE);
	BasicTypesCompatibilityChecker.MAP_WRAPPER2BASIC_TYPE
	    .put(Integer  .class, Integer  .TYPE);
	BasicTypesCompatibilityChecker.MAP_WRAPPER2BASIC_TYPE
	    .put(Long     .class, Long     .TYPE);
	BasicTypesCompatibilityChecker.MAP_WRAPPER2BASIC_TYPE
	    .put(Float    .class, Float    .TYPE);
	BasicTypesCompatibilityChecker.MAP_WRAPPER2BASIC_TYPE
	    .put(Double   .class, Double   .TYPE);
	BasicTypesCompatibilityChecker.MAP_WRAPPER2BASIC_TYPE
	    .put(Void     .TYPE,  Void     .TYPE);


	BasicTypesCompatibilityChecker.MAP_BASIC_TYPE2WRAPPER
	    .put(Boolean  .TYPE, Boolean  .class);
	BasicTypesCompatibilityChecker.MAP_BASIC_TYPE2WRAPPER
	    .put(Character.TYPE, Character.class);
	BasicTypesCompatibilityChecker.MAP_BASIC_TYPE2WRAPPER
	    .put(Byte     .TYPE, Byte     .class);
	BasicTypesCompatibilityChecker.MAP_BASIC_TYPE2WRAPPER
	    .put(Short    .TYPE, Short    .class);
	BasicTypesCompatibilityChecker.MAP_BASIC_TYPE2WRAPPER
	    .put(Integer  .TYPE, Integer  .class);
	BasicTypesCompatibilityChecker.MAP_BASIC_TYPE2WRAPPER
	    .put(Long     .TYPE, Long     .class);
	BasicTypesCompatibilityChecker.MAP_BASIC_TYPE2WRAPPER
	    .put(Float    .TYPE, Float    .class);
	BasicTypesCompatibilityChecker.MAP_BASIC_TYPE2WRAPPER
	    .put(Double   .TYPE, Double   .class);
	BasicTypesCompatibilityChecker.MAP_BASIC_TYPE2WRAPPER
	    .put(Void     .TYPE, Void     .TYPE);
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
     *    <li> <code>cls</code> is a primitive type 
     *         and <code>obj</code> may be casted to <code>cls</code>, i.e. 
     *         if either <code>obj == null</code> 
     *         or (sequentially) <code>cls.isInstance(obj)</code>. 
     *    <li> <code>cls</code> is a primitive type 
     *         and <code>obj.getClass()</code> 
     *         is the corresponding wrapper type. 
     *         In particular, <code>obj != null</code>. 
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
	    // Here, obj != null. 
	    Class<?> basicType = getWrappedCls(obj.getClass());
	    // return true includes basicType != null. 
	    return cls.equals(basicType);
	}
	// Here, cls represents a non-primitive class. 

	// Test whether obj may be casted to cls. 
	return obj == null || cls.isInstance(obj);
    }

    /**
     * Maps a wrapper class to wrapped primitive class. 
     * Note that Void is mapped to Void. 
     *
     * @param cls 
     *    some <code>Class</code> object. 
     * @return 
     *    the basic type wrapped by a <code>cls</code> class 
     *    if <code>cls</code> is a wrapper; 
     *    <code>null</code> otherwise. 
     *    Note that {@link Void#TYPE} wraps itself. 
     */
    public static Class<?> getWrappedCls(Class<?> cls) {
	return MAP_WRAPPER2BASIC_TYPE.get(cls);
    }

    /**
     * Maps primitive class to the corresponding wrapper class. 
     * Note that Void is mapped to Void. 
     *
     * @param cls 
     *    some <code>Class</code> object. 
     * @return 
     *    the basic type wrapped by a <code>cls</code> object 
     *    if <code>cls</code> is a basic class; 
     *    <code>null</code> otherwise. 
     *    Note that {@link Void#TYPE} is wrapped by itself. 
     */
    public static Class<?> getWrapperCls(Class<?> cls) {
	return MAP_BASIC_TYPE2WRAPPER.get(cls);
    }
}// NOPMD

