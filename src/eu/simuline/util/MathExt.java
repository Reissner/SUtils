
package eu.simuline.util;

/**
 * Provides basic mathematical operations missing in {@link Math}. 
 *
 * @author <a href="mailto:Ernst.Reissner@local.de">Ernst Reissner</a>
 * @version 1.0
 */
public final class MathExt {

    /* -------------------------------------------------------------------- *
     * constants.                                                           *
     * -------------------------------------------------------------------- */

    private final static double LN2 = Math.log(2);

    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Formally to create a new <code>MathExt</code> instance 
     * but intended in contrary 
     * to prevent an <code>MathExt</code> from being instantiated. 
     * Note that the modifiers <code>final</code> and <code>abstract</code> 
     * are mutually exclusive 
     * and so this trick is the only remaining possibility. 
     */
    private MathExt() {
	// is empty. 
    }

    /* -------------------------------------------------------------------- *
     * class methods.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * The sgn function. 
     *
     * @param dNum 
     *    some <code>double</code> value. 
     * @return 
     *    Either <code>-1</code>, <code>0</code> or <code>1</code> 
     *    depending on the sign of <code>dNum</code>. 
     *    Note that also <code>NaN</code> is mapped to <code>0</code>. 
     */
    public static int sgn(double dNum) {
        // compareTo(dNum,0) or the other way round. 
        if (dNum > 0) {
            return 1;
        }
        if (dNum < 0) {
            return -1;
        }
        return 0;
    }

    /**
     * The sgn function. 
     *
     * @param lNum 
     *    some <code>long</code> value. 
     * @return 
     *    Either <code>-1</code>, <code>0</code> or <code>1</code> 
     *    depending on the sign of <code>lNum</code>. 
     */
    public static int sgn(long lNum) {
        // compareTo(dNum,0) or the other way round. 
        if (lNum > 0) {
            return 1;
        }
        if (lNum < 0) {
            return -1;
        }
        return 0;
    }

    /**
     * The general real logarithm function. 
     *
     * @param base
     *    the base of this logarithm. 
     * @param num
     *    the argument of this logarithm. 
     * @return 
     *    the logarithm of the given number with the base specified. 
     */
    public static double log(double base,double num) {
	return Math.log(num)/Math.log(base);
    }

    /**
     * The logarithm function for base <code>2</code>. 
     *
     * @param base
     *    the base of this logarithm. 
     * @param num
     *    the argument of this logarithm. 
     * @return 
     *    the logarithm of the given number with the base specified. 
     */
    public static double ld(double num) {
	return Math.log(num)/LN2;
    }

    /**
     * Returns the unsigned mantissa of the given floating point number. 
     * Contract: 
     * <code>num==sgn(num)*mantissaL(num)*Math.pow(2,expL(num))</code>. 
     *
     * @param num 
     *    a <code>double</code> value
     * @return 
     *    the unsigned mantissa of the given number 
     *    as a <code>long</code> value. 
     *    Note that 
     *    <code>$2\cdot\frac{\\absV(d)}{\\ulp(d)}=m\cdot2^p$</code>
     *    where <code>p</code> is the length of the mantissa 
     *    is the mantissa written as an integer. 
     *    This fits into the least <code>53</code> bits 
     *    of a <code>long</code> value. 
     */
    public static long mantissaL(double num) {
	// correction by multiplying with 2 is eliminated 
	// in method expL by subtraction of 1. 
	// This correction is necessary, to avoid cut off of the last digit. 
	// no rounding: used only to convert into a <code>long</code> value. 
	return Math.round(2*Math.abs(num)/Math.ulp(num));
    }

    /**
     * Returns the unsigned mantissa of the given floating point number. 
     *
     * @param num 
     *    a <code>double</code> value
     * @return 
     *    the unsigned mantissa of the given number 
     *    as an <code>int</code> value. 
     * @see #mantissaL(double)
     */
    public static int mantissaL(float num) {
	// correction by multiplying with 2 is eliminated 
	// in method expL by subtraction of 1. 
	// This correction is necessary, to avoid cut off of the last digit. 
	// no rounding: used only to convert into a <code>long</code> value. 
	return Math.round(2*Math.abs(num)/Math.ulp(num));
    }

    /**
     * Returns the exponent of the given floating point number. 
     * Contract: 
     * <code>num==sgn(num)*mantissaL(num)*Math.pow(2,expL(num))</code>. 
     * Note that since {@link #mantissaL(double)} is an integer 
     * the result is <code>e-p</code>, 
     * where <code>e</code> is the exponent of <code>num</code> 
     * and <code>p</code> is the length of the mantissa. 
     * In particular, the return value is at most <code>-p</code>. 
     * @param num 
     *    a <code>double</code> value
     * @return 
     *    the exponent of the given number 
     *    as an <code>int</code> value. 
     * @see #mantissaL(double)
     */
    public static int expL(double num) {
	// no rounding and no loss of precision by casting. 
	return (int)Math.round(ld(Math.ulp(num)))-1;
    }

    /**
     * Returns the exponent of the given floating point number. 
     *
     * @param num a <code>float</code> value
     * @return an <code>int</code> value
     */
    public static int expL(float num) {
	// no rounding and no loss of precision by casting. 
	return (int)Math.round(ld(Math.ulp(num)))-1;
    }
}
