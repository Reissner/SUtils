
package eu.simuline.util;

import eu.simuline.arithmetics.left2right.BuiltInTypes;

import java.math.MathContext;
import java.math.RoundingMode;
import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.List;
import java.util.ArrayList;

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

    // **** both, MathExt and BuiltInTypes shall be in package arithmetics 

    /**
     * Precision and rounding mode of a <code>double</code> value. 
     *
     * @see #round(double, MathContext)
     */
    public static final MathContext CONTEXT_DUAL_DOUBLE = 
	    new MathContext(BuiltInTypes.DOUBLE.mantissaLen(), 
			    RoundingMode.HALF_EVEN);

    /**
     * Precision and rounding mode of a <code>float</code> value. 
     *
     * @see #round(double, MathContext)
     */
    public static final MathContext CONTEXT_DUAL_FLOAT = 
	    new MathContext(BuiltInTypes.FLOAT.mantissaLen(), 
			    RoundingMode.HALF_EVEN);


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
     * Returns the quotient of the given arguments rounded away from zero, 
     * i.e. rounded up in absolute value. 
     * For rounding downwards, i.e. towards zero, 
     * just compute <code>dividend / divisor</code>. 
     * This works for positive numbers only. 
     *
     * @param dividend 
     *    a non-negative number 
     * @param divisor 
     *    a positive number 
     * @return
     *    The least integer above <code>dividend / divisor</code> 
     *    computed by <code>(a+b-1)/b=a/b+1-1/b</code>. 
     *    To prove that this formula is correct, we distinguish: 
     *    <ul>
     *    <li>
     *    i.e. if <code>a/b</code> goes without rounding, 
     *    rounding <code>a/b</code> upwards is <code>a/b</code> itself and 
     *    rounding <code>(a+b-1)/b=a/b+(1-1/b)</code> towards zero yields 
     *    <code>a/b</code> as well. 
     *    <li>
     *    If <code>b</code> does not divide <code>a</code>, 
     *    then rounding <code>(a+b-1)/b=(a-1)/b+1</code> towards zero 
     *    is the same as rounding down <code>a/b+1</code> 
     *    which is rounding upwards <code>a/b</code>. 
     *    </ul>
     */
    public static int divRoundUp(int dividend, int divisor) {
	return (dividend+divisor-1)/divisor;
    }

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
     * @param num 
     *    a <code>float</code> value
     * @return 
     *    the exponent of the given number 
     *    as an <code>int</code> value. 
     */
    public static int expL(float num) {
	// no rounding and no loss of precision by casting. 
	return (int)Math.round(ld(Math.ulp(num)))-1;
    }

    /**
     * Returns <code>num</code> rounded according to <code>mContext</code>. 
     * If <code>num</code> is not finite, 
     * i.e. if it is infinite or not a number, 
     * <code>num</code> itself is returned. 
     * The same is true for the numbers <code>\pm0</code>. 
     * <p>
     * **** better design would be if the MathContexts provided 
     * their own rounding facilities. 
     * **** a lot of code ist never tested. 
     * according branches are endowed with <code>assert false</code>. 
     *
     * @param num 
     *    a <code>double</code> number to be rounded. 
     * @param mContext 
     *    precision and rounding mode to round <code>num</code> 
     * @return 
     *    <code>num</code> rounded according to <code>mContext</code>. 
     * @throws ArithmeticException
     *    if rounding is necessary but not allowed 
     *    according to <code>mContext</code>. 
     */
    public static double round(double num, MathContext mContext) {
	if (Double.isInfinite(num) || Double.isNaN(num) || num == 0.0) {
	    return num;
	}
	assert !Double.isInfinite(num) && !Double.isNaN(num) && num != 0.0;
	// this condition allows to create an FPSeparator below 

	if (mContext.getPrecision() == 0 || 
	    mContext.getPrecision() >= BuiltInTypes.DOUBLE.mantissaLen()) {
	    // **** a problem for denormalized numbers 
	    return num;
	}
	int numDigsToNullify = 
	    BuiltInTypes.DOUBLE.mantissaLen()-mContext.getPrecision();
	assert numDigsToNullify > 0;
	// Here, rounding must be effected 

	// decompose num into sign, mantissa and exponent 
	FPSeparator sep = new FPSeparator(num);
	long mant = sep.mantissaL();
	assert num == sep.sign() * Math.scalb((double)mant, sep.exp()-53);

	// modify mant according to rounding mode and numDigsToNullify 
	switch (mContext.getRoundingMode()) {
	case UNNECESSARY:// round only cutting off trailing zero digits 
	    mant = mantRoundAwayFrom0(mant, numDigsToNullify, false);
	    break;
	case DOWN:
	    mant = mantRoundTowards0 (mant, numDigsToNullify);
	    break;
	case UP:
	    mant = mantRoundAwayFrom0(mant, numDigsToNullify, true);
	    break;
	case CEILING:
	    mant = (sep.sign() == +1)
		? mantRoundAwayFrom0(mant, numDigsToNullify, true)
		: mantRoundTowards0 (mant, numDigsToNullify);
	    break;
	case FLOOR:
	    mant = (sep.sign() == -1)
		? mantRoundAwayFrom0(mant, numDigsToNullify, true)
		: mantRoundTowards0 (mant, numDigsToNullify);
	    break;
	case HALF_UP:
	case HALF_DOWN:
	case HALF_EVEN:
	    mant = mantRoundHalf(mant, 
				 numDigsToNullify, 
				 mContext.getRoundingMode());
	    break;
	default:
	    throw new IllegalStateException("****");
	}// Here, sign, mantissa and exponent of the result are determined 

	if (       (mant & 0x7fe0000000000000L) != 0L) {
	    // Here, the mantissa was incremented and this caused a carry 

	    // The carry yields one additional high digit ... 
	    assert (mant & 0x7fe0000000000000L) == 0x0020000000000000L;
	    // ... but looses two low digits: 
	    // - the 0th bit is zero by shift 
	    // - the 1st bit is zero because otherwise thhere were no carry 
	    assert (mant & 3L) == 0L;
	}
	// The following formula holds whether there is an overflow or not 

	return sep.sign() * Math.scalb((double)mant, sep.exp()-53);
    }

    /**
     * Contributes to rounding a floating number to zero 
     * by rounding a mantissa <code>mant</code> to zero 
     * nullifying the last <code>numDigsToNullify</code> digits 
     * of the mantissa. 
     *
     * @param mant 
     *    the mantissa to be rounded 
     * @param numDigsToNullify
     *    the number of trailing digits to be nullified 
     * @return
     *    <code>mant</code> modified as described above. 
     */
    private static long mantRoundTowards0(long mant, int numDigsToNullify) {
	mant >>= numDigsToNullify;
	mant <<= numDigsToNullify;
	return mant;
    }

    /**
     * Contributes to rounding a floating number away from zero 
     * by rounding a mantissa <code>mant</code> away from zero 
     * by rounding up the mantissa 
     * eliminating  the last <code>numDigsToNullify</code> digits 
     * of the mantissa. 
     * <p>
     * If the last <code>numDigsToNullify</code> digits 
     * of the mantissa are zero, <code>mant</code> is returned. 
     * Otherwise, if no modification is allowed 
     * according to <code>chgIsAllowed</code>, an exception is thrown. 
     * Otherwise, neglecting the last <code>numDigsToNullify</code> digits, 
     * <code>mant</code> is incremented. 
     * **** note that desired precision is exceeded 
     * if increasing the mantissa yields a longer mantissa. 
     * ****
     *
     * @param mant 
     *    the mantissa to be rounded 
     * @param numDigsToNullify
     *    the number of trailing digits of the mantissa to be nullified 
     * @param chgIsAllowed
     *    whether a trial to change in <code>mant</code> causes an exception. 
     * @return
     *    <code>mant</code> modified or not as described above. 
     * @throws ArithmeticException
     *    if rounding is necessary according to the rounding mode 
     *    but not allowed according to <code>chgIsAllowed</code>. 
     * @see #round(double num, MathContext mContext) 
     */
    private static long mantRoundAwayFrom0(long mant, 
					   int numDigsToNullify, 
					   boolean chgIsAllowed) {
	long mantOrig = mant;
	mant >>= numDigsToNullify;
	mant <<= numDigsToNullify;
	if (mantOrig != mant) {
	    // Here, rounding caused an error
	    if (!chgIsAllowed) {
		throw new ArithmeticException("Rounding necessary");
	    }
	    mant >>= numDigsToNullify;
	    mant++;
	    mant <<= numDigsToNullify;
	    // round(double num, MathContext mContext) 
	    // handles cases where this increases number of digits 
	    // and problems with overflow of the mantissa 
	}
	return mant;
    }
    /**
     * Contributes to rounding a floating number away from zero 
     * by rounding a mantissa away from zero 
     * by rounding the mantissa <code>mant</code> to even 
     * eliminating  the last <code>numDigsToNullify</code> digits 
     * of the mantissa. 
     * The particular rounding mode is given by  <code>rdMd</code>. 
     * 
     * @param mant 
     *    the mantissa to be rounded 
     * @param numDigsToNullify
     *    the number of trailing digits to be nullified 
     * @param rdMd
     *    the rounding mode to be applied. 
     *    This is one of 
     *    {@link RoundingMode.HALF_UP}, {@link RoundingMode.HALF_DOWN} and 
     *    {@link RoundingMode.HALF_EVEN}. 
     * @see #round(double num, MathContext mContext) 
     */
    private static long mantRoundHalf(long mant,
				      int numDigsToNullify, 
				      RoundingMode rdMd) {
	long mantOrig = mant;
	mant >>= (numDigsToNullify-1);
	mant <<= (numDigsToNullify-1);
	if (mant != mantOrig) {
	    // Here, the failure of rounding up and for rounding down differ 
	    mant >>= (numDigsToNullify-1);
	    boolean roundUp = ((mant & 1) == 1);
	    // rounding up in absolute value. 
	    
	    mant >>= 1;
	    if (roundUp) {
		mant++;
	    // round(double num, MathContext mContext) 
	    // handles cases where this increases number of digits 
	    // and problems with overflow of the mantissa 
		assert mant >= 0;
	    }
	    mant <<= numDigsToNullify;
	    assert mant >= 0;
	    return mant;
	} // if 


	// Here, rounding depends on the rounding mode 
	mant >>= numDigsToNullify;
	mant <<= numDigsToNullify;
	switch (rdMd) {
	case HALF_DOWN:
	    break;
	case HALF_UP:
	    mant++;
	    // round(double num, MathContext mContext) 
	    // handles cases where this increases number of digits 
	    // and problems with overflow of the mantissa 
	    break;
	case HALF_EVEN:
	    if ((mant & 1) == 1) {
		mant++;
	    // round(double num, MathContext mContext) 
	    // handles cases where this increases number of digits 
	    // and problems with overflow of the mantissa 
	    }
	    break;
	default:
	    throw new IllegalArgumentException("****");
	} // switch (rdMd) 

	return mant;
    }

    // public static List<Integer> long2listDigs(long num) {
    // 	assert num >= 0;
    // 	List<Integer> res = new ArrayList<Integer>(64);
    // 	while (num != 0) {
    // 	    res.add(0, (int)num & 1);
    // 	    num >>= 1;
    // 	}

    // 	return res;
    // }

    public static void main(String[] args) {
    } // main 
}
