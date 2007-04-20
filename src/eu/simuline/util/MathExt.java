
package eu.simuline.util;

/**
 * Provides basic mathematical operations missing in {@link Math}. 
 *
 * @author <a href="mailto:Ernst.Reissner@local.de">Ernst Reissner</a>
 * @version 1.0
 */
public final class MathExt {

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
}
