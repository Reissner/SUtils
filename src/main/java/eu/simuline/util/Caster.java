
package eu.simuline.util;

/**
 * Provides ways to transform instances of various classes into one another. 
 * The core method is {@link #cast}: it performs the transformation. 
 * <p>
 * A simple example would be to transform {@link java.lang.Double}s 
 * into <code>eu.simuline.arithmetics.left2right.FPNumber</code>s. 
 * Another example is given by {@link #BASIC_TYPES}. 
 * It is the foundation to transform arrays of elementary types 
 * to arrays of their wrappers and the other way round 
 * such as <code>double[][]</code> and <code>Double[][]</code>. 
 * <p>
 * Casters may be used to transform descriptors of objects into these objects. 
 * A snippet of typical implementation for the {@link #cast}-method 
 * would be "<code>return new ExaClass(descriptor)</code>" in this case. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public abstract class Caster {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * A trivial caster for which compatibility 
     * is assignment compatibility. 
     */
    public static final Caster ASSIGNEMENT_COMPATIBLE = new Trivial() {
	    public boolean areCompatible(final Class<?> cls, final Object obj) {
		return cls.isInstance(obj);
	    }
	}; // ASSIGNEMENT_COMPATIBLE 

    /**
     * A trivial caster for which compatibility 
     * is equality of classes. 
     */
    public static final Caster STRICT = new Trivial() {
	    public boolean areCompatible(final Class<?> cls, final Object obj) {
		return cls == obj.getClass();
	    }
	}; // STRICT 

    /**
     * A trivial caster for which compatibility 
     * is assignment compatibility 
     * up to wrapping and unwrapping of primitive types. 
     * Used to implement 
     * {@link CollectionsExt#recToArray(Object,Class)} 
     * as a special case of 
     * {@link CollectionsExt#recToArray(Object,Class,Caster)}. 
     */
    public static final Caster BASIC_TYPES = new Trivial() {

	    /**
	     * Behaves like 
	     * {@link BasicTypesCompatibilityChecker#areCompatible}. 
	     *
	     * @param cls 
	     *    a <code>Class</code>. 
	     * @param obj 
	     *    an <code>Object</code>. 
	     * @return 
	     *    see {@link BasicTypesCompatibilityChecker#areCompatible}. 
	     */
	    public boolean areCompatible(final Class<?> cls, final Object obj) {
		return BasicTypesCompatibilityChecker.areCompatible(cls, obj);
	    }
	}; // BASIC_TYPES 

    /**
     * This is a trivial caster. 
     * Method {@link #areCompatible} is still to be implemented. 
     */
    abstract static class Trivial extends Caster { // NOPMD

	    /**
	     * Returns the input parameter unchanged. 
	     *
	     * @param obj 
	     *    An arbitrary object or <code>null</code>. 
	     * @return 
	     *    the input parameter without a change. 
	     */
	    public Object cast(final Object obj) {
		return obj;
	    }
	}; // Trivial  

    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    /**
     * Decides whether the given class and object 
     * are compatible with respect to this caster. 
     *
     * @param cls 
     *    a <code>Class</code>. 
     * @param obj 
     *    an <code>Object</code>. 
     * @return 
     *    <code>true</code> if and only if either of the following holds: 
     *    <ul>
     *    <li> <code>cast(obj)</code> may be casted to <code>cls</code>, i.e. 
     *         if either <code>cast(obj) == null</code> 
     *         (which is the case for <code>obj == null</code>) 
     *         or (sequentially) <code>cls.isInstance(cast(obj))</code>. 
     *    <li> <code>cls</code> is a basic type, 
     *         <code>cast(obj) != null</code> and (sequentially) 
     *         <code>cast(obj).getClass()</code> 
     *         is the corresponding wrapper type. 
     *    </ul>
     * @see BasicTypesCompatibilityChecker#areCompatible
     */
    public abstract boolean areCompatible(Class<?> cls, Object obj);

    /*
     * Provided the return value is not <code>null</code>, 
     * <code>cast(obj)</code> has to be either <code>null</code> 
     * or an instance of <code>targetClass(obj.getClass()))</code>. 
     * <p>
     * This means that {@link #cast} may be used 
     *
     * @param cls 
     *    a <code>Class</code> object; not <code>null</code>. 
     * @return 
     *    <ul>
     *    <li>
     *    <code>null</code> if {@link #cast} is not defined, 
     *    i.e. if it throws an **** exception. 
     *    <li>
     *    A class to which the return value of {@link #cast} may be casted 
     *    for input of the type <code>cls</code>. 
     *    This return value may well be <code>null</code> 
     *    or it may be an instance of a subclass. 
     *    </ul>
     */
    //Class targetClass(Class cls);

    /**
     * The return value <code>cast(obj)</code> 
     * (which may well be <code>null</code>), 
     * may be casted to class <code>cls</code> 
     * if {@link #areCompatible} returns <code>true</code>. 
     *
     * @param obj 
     *    an arbitrary <code>Object</code> or even <code>null</code>. 
     * @return 
     *    <ul>
     *    <li>
     *    <code>null</code> for <code>obj == null</code>. 
     *    <li>
     *    <code>null</code> if <code>obj != null</code> 
     *    but <code>targetClass(obj.getClass()) == null</code>. **** 
     *    <li>
     *    either <code>null</code> again 
     *    or an instance of <code>targetClass(obj.getClass())</code>
     *    </ul>
     */
    public abstract Object cast(Object obj);
}
