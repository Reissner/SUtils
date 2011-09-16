
package eu.simuline.util;

import eu.simuline.arithmetics.left2right.BuiltInTypes;

import eu.simuline.arithmetics.dual.MathContextAbsRel;
import eu.simuline.arithmetics.dual.BigDual;

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
     * Precision and rounding mode of a <code>double</code> value 
     * equivalent with {@link #REL_CONTEXT_DUAL_DOUBLE}. 
     *
     * @see #round(double, MathContext)
     * @see #ABS_CONTEXT_DUAL_DOUBLE
     */
    public static final MathContext CONTEXT_DUAL_DOUBLE = 
	new MathContext(BuiltInTypes.DOUBLE.mantissaLen(), 
			RoundingMode.HALF_EVEN);

    /**
     * Absolute precision and rounding mode of a <code>double</code> value. 
     *
     * @see #round(double, MathContext)
     * @see #REL_CONTEXT_DUAL_DOUBLE
     */
    public static final MathContextAbsRel ABS_CONTEXT_DUAL_DOUBLE = 
	MathContextAbsRel.createAbs(BuiltInTypes.DOUBLE.minExp(), 
				    RoundingMode.HALF_EVEN);

    /**
     * Relative precision and rounding mode of a <code>double</code> value 
     * equivalent with {@link #CONTEXT_DUAL_DOUBLE}. 
     *
     * @see #round(double, MathContext)
     * @see #ABS_CONTEXT_DUAL_DOUBLE
     */
    public static final MathContextAbsRel REL_CONTEXT_DUAL_DOUBLE = 
	MathContextAbsRel.createRel(BuiltInTypes.DOUBLE.mantissaLen(), 
				    RoundingMode.HALF_EVEN);

    /**
     * Precision and rounding mode of a <code>float</code> value 
     * equivalent with {@link #REL_CONTEXT_DUAL_FLOAT}. 
     *
     * @see #round(float, MathContext)
     * @see #ABS_CONTEXT_DUAL_FLOAT
     */
    public static final MathContext CONTEXT_DUAL_FLOAT = 
	    new MathContext(BuiltInTypes.FLOAT.mantissaLen(), 
			    RoundingMode.HALF_EVEN);

    /**
     * Absolute precision and rounding mode of a <code>float</code> value. 
     *
     * @see #round(float, MathContext)
     * @see #REL_CONTEXT_DUAL_FLOAT
     */
    public static final MathContextAbsRel ABS_CONTEXT_DUAL_FLOAT = 
	MathContextAbsRel.createAbs(BuiltInTypes.FLOAT.minExp(), 
				    RoundingMode.HALF_EVEN);

    /**
     * Relative precision and rounding mode of a <code>float</code> value 
     * equivalent with {@link #CONTEXT_DUAL_FLOAT}. 
     *
     * @see #round(float, MathContext)
     * @see #REL_CONTEXT_DUAL_FLOAT
     */
    public static final MathContextAbsRel REL_CONTEXT_DUAL_FLOAT = 
	MathContextAbsRel.createRel(BuiltInTypes.FLOAT.mantissaLen(), 
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

    // **** tests missing 
    /**
     * Returns a value arising from <code>num</code> 
     * by applying <code>numShifts</code> left shifts 
     * taking the sign of <code>numShifts</code> into account. 
     * In contrast to <code>num << numShifts</code>, 
     * the full <code>numShifts</code> is taken into account. 
     * This yields zero if <code>numShifts</code> 
     * exceeds the number of bits of <code>num</code> in absolute value. 
     */
    public static int shiftLeftForced(int num, int numShifts) {
	// > to exclude sign as well 
	if (Math.abs(numShifts) > BuiltInTypes.INTEGER.mantissaLen()) {
	    return 0;
	}
	return num << numShifts;
    }

    // **** tests missing 
    /**
     * Returns a value arising from <code>num</code> 
     * by applying <code>numShifts</code> left shifts 
     * taking the sign of <code>numShifts</code> into account. 
     * In contrast to <code>num << numShifts</code>, 
     * the full <code>numShifts</code> is taken into account. 
     * This yields zero if <code>numShifts</code> 
     * exceeds the number of bits of <code>num</code> in absolute value. 
     */
    public static long shiftLeftForced(long num, int numShifts) {
	// > to exclude sign as well 
	if (Math.abs(numShifts) > BuiltInTypes.LONG.mantissaLen()) {
	    return 0;
	}
	return num << numShifts;
    }

    // **** tests missing 
    /**
     * Returns a value arising from <code>num</code> 
     * by applying <code>numShifts</code> right shifts 
     * taking the sign of <code>numShifts</code> into account. 
     * In contrast to <code>num >> numShifts</code>, 
     * the full <code>numShifts</code> is taken into account. 
     * This yields zero if <code>numShifts</code> 
     * exceeds the number of bits of <code>num</code> in absolute value. 
     */
    public static int shiftRightForced(int num, int numShifts) {
	// > to exclude sign as well 
	if (Math.abs(numShifts) > BuiltInTypes.INTEGER.mantissaLen()) {
	    return 0;
	}
	return num >> numShifts;
    }

    // **** tests missing 
    /**
     * Returns a value arising from <code>num</code> 
     * by applying <code>numShifts</code> right shifts 
     * taking the sign of <code>numShifts</code> into account. 
     * In contrast to <code>num >> numShifts</code>, 
     * the full <code>numShifts</code> is taken into account. 
     * This yields zero if <code>numShifts</code> 
     * exceeds the number of bits of <code>num</code> in absolute value. 
     */
    public static long shiftRightForced(long num, int numShifts) {
	// > to exclude sign as well 
	if (Math.abs(numShifts) > BuiltInTypes.LONG.mantissaLen()) {
	    return 0;
	}
	return num >> numShifts;
    }

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
     * <code>num==sgn(num)*mantissaL(num)2^expL(num)</code>. 
     * This number is always even 
     * because it is twice of the value given by {@link #mantissaL2(double)}. 
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
     * @see #mantissaL2(double)
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
     * Contract: 
     * <code>num==sgn(num)*mantissaL2(num)*2^{expL2(num)-53}</code>. 
     *
     * @param num 
     *    a <code>double</code> value
     * @return 
     *    the unsigned mantissa of the given number 
     *    as a <code>long</code> value. 
     * @see #mantissaL(double)
     */
    public static long mantissaL2(double num) {
	long lRep = Double.doubleToRawLongBits(num);
	return ((lRep & 0x7ff0000000000000L) == 0) // exp=-1075? 
	    ?   (lRep &    0xfffffffffffffL) << 1  // denormalized number 
	    :   (lRep &    0xfffffffffffffL) | 
	    /*          */0x10000000000000L;
    }
    
    /**
     * Returns the unsigned mantissa of the given floating point number. 
     * This number is always even 
     * because it is twice of the value given by {@link #mantissaL2(float)}. 
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
     * Returns the unsigned mantissa of the given floating point number. 
     * Contract: 
     * <code>num==sgn(num)*mantissaL2(num)*2^{expL2(num)-53}</code>. 
     *
     * @param num 
     *    a <code>double</code> value
     * @return 
     *    the unsigned mantissa of the given number 
     *    as a <code>long</code> value. 
     * @see #mantissaL(double)
     */
    public static int mantissaL2(float num) {
	int iRep = Float.floatToRawIntBits(num);
	return ((iRep & 0x7f800000) == 0) // exp=-150? 
	    ?   (iRep & 0x007fffff) << 1  // denormalized number 
	    :   (iRep & 0x007fffff) | 
	    /*        */0x00800000;
    }
    
    /**
     * Returns the exponent of the given floating point number. 
     * Contract: 
     * <code>num==sgn(num)*mantissaL(num)2^expL(num)</code>. 
     * Note that since {@link #mantissaL(double)} is an integer 
     * the result is <code>e-p</code>, 
     * where <code>e</code> is the exponent of <code>num</code> 
     * and <code>p</code> is the length of the mantissa. 
     * In particular, the return value is at most <code>-p</code>. 
     *
     * @param num 
     *    a <code>double</code> value
     * @return 
     *    the exponent of the given number 
     *    as an <code>int</code> value. 
     * @see #mantissaL(double)
     * @see #expL2(double)
     */
    public static int expL(double num) {
	// no rounding and no loss of precision by casting. 
	return (int)Math.round(ld(Math.ulp(num)))-1;
    }

    /**
     * Returns the exponent of the given floating point number. 
     * Contract: 
     * <code>num==sgn(num)*mantissaL2(num)*2^{expL2(num)-53}</code>. 
     * Return value of this method is <code>54</code> 
     * plus value returned by {@link #expL(double)}. 
     *
     * @param num 
     *    a <code>double</code> value
     * @see #mantissaL2(double)
     * @see #expL(double)
     */
    public static int expL2(double num) {
	long lRep = Double.doubleToRawLongBits(num);
	return (int) ((lRep >> 52) & 0x7ffL) -1075+53;
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

    public static int expL2(float num) {
	int iRep = Float.floatToRawIntBits(num);
	return (int) ((iRep >> 23) & 0x7fL) -150+24;
    }

    // for denormalized numbers: one less because leading digit must be stored 
    // taking normalization of denormalized numbers into account, 
    public static int numDigs(double num) {
	long lRep = Double.doubleToRawLongBits(num);
	int res = BuiltInTypes.DOUBLE.mantissaLen();
	if (((lRep >> 52) & 0x7ffL) == 0) {
	    // Here the number is denormalized 
	    res--;
	}

	return res;
    }

    public static boolean isDenormalized(double num) {
	long lRep = Double.doubleToRawLongBits(num);
	return (((lRep >> 52) & 0x7ffL) == 0);
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
     *    absolute or relative precision 
     *    and rounding mode to round <code>num</code> 
     * @return 
     *    <code>num</code> rounded according to <code>mContext</code>. 
     * @throws ArithmeticException
     *    if rounding is necessary but not allowed 
     *    according to <code>mContext</code>. 
     * @see #round(double, MathContext)
     */
    public static double round(double num, MathContextAbsRel mContext) {
	if (Double.isInfinite(num) || Double.isNaN(num) || num == 0.0) {
	    return num;
	}
	assert !Double.isInfinite(num) && !Double.isNaN(num) && num != 0.0;
	// this condition allows to create an F%PSeparator below 

	if (!mContext.isAbsolute()) {
	    // Here, relative rounding is requested 
	    int numDigsMax = mContext.getNumDigits();
	    if (numDigsMax == 0 || 
		numDigsMax >= BuiltInTypes.DOUBLE.mantissaLen()) {
		// **** a problem for denormalized numbers 
assert !isDenormalized(num);
		return num;
	    }
	    int numDigsToNullify = 
		BuiltInTypes.DOUBLE.mantissaLen()-numDigsMax;
	    assert numDigsToNullify > 0;
	    // Here, rounding must be effected 

	    return roundRemove(num, numDigsToNullify, mContext.getRoundingMode());
 	}
	assert mContext.isAbsolute();
	// Here, absolute rounding is requested 

	// decompose num into sign, mantissa and exponent 
	FPSeparator sep = new FPSeparator(num);
	long mant = sep.mantissaL();
	int scale = sep.exp()-53;
	assert num == sep.sign() * Math.scalb((double)mant, scale);

	if (scale >= mContext.getMinPowBase()) {
	    // Here, rounding is not necessary 
//System.out.println("b: "+this);
	    return num;
	}

	// numDigsToNullify is the number of trailing digits to be removed 
	int numDigsToNullify = mContext.getMinPowBase()-scale;
	assert numDigsToNullify > 0;

	// if numDigsToNullify exceeds 53, *******
	if (53 < numDigsToNullify && !mContext.rndModeIsHalf()) {
//	if (53 < numDigsToNullify && !isHalf(mContext.getRoundingMode())) {
//	    System.out.println("  hhhhhh   : "+mant);
//	    int numDigsRemain = -numDigsToNullify+53;
	    // **** except if denormalized this.numDigits();
assert !isDenormalized(num);
//	    mant >>= 1-numDigsRemain;
//	    mant <<= 1-numDigsRemain;
	    numDigsToNullify = 53;
	    scale = mContext.getMinPowBase()-numDigsToNullify;
	    assert numDigsToNullify == mContext.getMinPowBase()-scale;
	    mant >>= 1;
	    num = sep.sign() * Math.scalb((double)mant, scale+1);
//System.out.println("  hhhhhh   : "+mant);
//System.out.println("  hhhhhh   : "+BigDual.valueOf((double)mant).digits());
	}
	assert numDigsToNullify <= 53 || mContext.rndModeIsHalf();

	return roundRemove(num, numDigsToNullify, mContext.getRoundingMode());
   }

    // // better to be integrated into MathContextAbsRel 
    // private static boolean isHalf(RoundingMode rndMode) {
    // 	return rndMode == RoundingMode.HALF_EVEN 
    // 	    || rndMode == RoundingMode.HALF_DOWN 
    // 	    || rndMode == RoundingMode.HALF_UP; 
    // }

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
     * @see #round(double, MathContextAbsRel)
     */
    public static double round(double num, MathContext mContext) {
	return round(num, MathContextAbsRel.create(mContext));
    }

    // invoked only for 
    // !Double.isInfinite(num) && !Double.isNaN(num) && num != 0.0;
    /**
     * Returns <code>num</code> rounded 
     * by removing the trailing <code>numDigsToNullify</code> digits 
     * and by rounding according to <code>mContext</code>. 
     */
    private static double roundRemove(double num, 
				      int numDigsToNullify, 
				      RoundingMode rndMode) {

	// decompose num into sign, mantissa and exponent 
	assert !Double.isInfinite(num) && !Double.isNaN(num) && num != 0.0;
	FPSeparator sep = new FPSeparator(num);
	long mant = sep.mantissaL();
	assert num == sep.sign() * Math.scalb((double)mant, sep.exp()-53);

	// modify mant according to rounding mode and numDigsToNullify 
	switch (rndMode) {
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
	    mant = mantRoundHalf(mant, numDigsToNullify, rndMode);
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
	    // - the 1st bit is zero because otherwise there were no carry 
	    assert (mant & 3L) == 0L;
	    // **** only problem: exceeding max value 
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
	mant = shiftRightForced(mant, numDigsToNullify);
	// mant >>= numDigsToNullify;
	mant <<= numDigsToNullify;
	return mant;
    }

    /**
     * Returns the bit sequence of the long value <code>mant</code>. 
     */
    public static String mantToString(long mant) {
	assert mant >= 0;
	StringBuilder res = new StringBuilder();
	while (mant > 0) {
	    res.insert(0, mant & 1);
//	    res.append(mant & 1);
	    mant >>= 1;
	}

	return res.toString();
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
	mant = shiftRightForced(mant, numDigsToNullify);
	//mant >>= numDigsToNullify;
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
	    // **** to think about: excess of max value. 
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
	int numTrailDigsToNullify = numDigsToNullify-1;
	// mant >>= numTrailDigsToNullify;
	mant = shiftRightForced(mant, numTrailDigsToNullify);

	// round up in abs. value in some cases (ignored by some rounding modes). 
	boolean roundUp = ((mant & 1) == 1);
	
	mant <<= numTrailDigsToNullify;
	if (mant != mantOrig) {
	    // Here, the failure of rounding up and for rounding down differ 
	    mant >>= numDigsToNullify;

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
	assert mant == mantOrig;// i.e. the last numTrailDigsToNullify digits are 0

	if (!roundUp) {
	    // Here, all digits to be removed are zero 
	    // and so rounding has no effect
	    return mant;
	}
	assert roundUp;

	// Here, rounding depends on the rounding mode 
	mant >>= numDigsToNullify;
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
	    roundUp = ((mant & 1) == 1);
	    if (roundUp) {
		mant++;
		// round(double num, MathContext mContext) 
		// handles cases where this increases number of digits 
		// and problems with overflow of the mantissa 
	    }
	    break;
	default:
	    throw new IllegalArgumentException("****");
	} // switch (rdMd) 
	mant <<= numDigsToNullify;

	return mant;
    }

    public static List<Integer> long2listDigs(long num) {
    	assert num >= 0;
    	List<Integer> res = new ArrayList<Integer>(64);
    	while (num != 0) {
    	    res.add((int)num & 1);//0, 
    	    num >>= 1;
    	}

    	return res;
    }

    public static void main(String[] args) {

	double num;
	for (int i = 0; i < 1000; i++) {
	    System.out.println("i: "+i);
	    num  = Math.random();
	    num *= Math.pow(2.0,100*Math.random()-50);
	    num *= MathExt.sgn(Math.random()-0.5);
	    System.out.println("-: "+  mantissaL(num));
	    System.out.println(" : "+  (mantissaL2(num)<<1));
	    assert mantissaL(num)>>1 == mantissaL2(num);
	    assert (sgn(num)*mantissaL(num)*Math.pow(2,expL(num)))
		== num;
	    assert expL(num)+54 == expL2(num);
	    assert (sgn(num)*Math.scalb((double)mantissaL2(num), expL2(num)-53))
		== num;
	    System.out.println("-: "+ expL (num));
	    System.out.println("-: "+ expL2(num));

	} // for 

    } // main 
}
