package eu.simuline.util;

import eu.simuline.arithmetics.left2right.BuiltInTypes;
import eu.simuline.arithmetics.dual.BigDual;

import java.math.BigDecimal;

/**
 * Offers a unified interface for {@link BigDecimal}s and for {@link Double}s. 
 * This is still to be extended to be useful in a general context. 
 *
 * Created: Sat Jul  1 13:42:48 2006
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
public interface NumberWrapper {

    /* --------------------------------------------------------------------- *
     * inner classes implementing NumberWrapper.                             *
     * --------------------------------------------------------------------- */


    public static class Dbl implements NumberWrapper {

	/* ---------------------------------------------------------------- *
	 * attributes.                                                      *
	 * ---------------------------------------------------------------- */

	double num;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	public Dbl(double num) {
	    this.num = num;
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public int shiftLeft2() {
	    this.num *= 2;
	    if (this.num >= 1) {
		this.num -= 1;
		return 1;
	    }
	    return 0;
	}
	public boolean isZero() {
	    return this.num == 0;
	}
	public boolean isGEOne() {
	    return this.num >= 1;
	}
	public boolean isLEZero() {
	    return this.num <= 0;
	}
	public NumberWrapper copy() {
	    return new Dbl(this.num);
	}
	public String toString() {
	    return Double.toString(this.num);
	}
 	public boolean equals(Object obj) {
	    Dbl other = (Dbl)obj;
	    return this.num == other.num;
 	}
	public double doubleValue() {
	    return this.num;
	}
    } //  class Dbl 


    public static class Flt implements NumberWrapper {

	/* ---------------------------------------------------------------- *
	 * attributes.                                                      *
	 * ---------------------------------------------------------------- */

	float num;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	public Flt(float num) {
	    this.num = num;
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public int shiftLeft2() {
	    this.num *= 2;
	    if (this.num >= 1) {
		this.num -= 1;
		return 1;
	    }
	    return 0;
	}
	public boolean isZero() {
	    return this.num == 0;
	}
	public boolean isGEOne() {
	    return this.num >= 1;
	}
	public boolean isLEZero() {
	    return this.num <= 0;
	}
	public NumberWrapper copy() {
	    return new Flt(this.num);
	}
	public String toString() {
	    return Double.toString(this.num);
	}
 	public boolean equals(Object obj) {
	    Flt other = (Flt)obj;
	    return this.num == other.num;
 	}
	public double doubleValue() {
	    return this.num;
	}
    } //  class Flt 



    // **** intended to be faster than Dbl 
    // to be analyzed why this is not the case. 
    public static class Long53 implements NumberWrapper {

	/* ---------------------------------------------------------------- *
	 * class constants.                                                 *
	 * ---------------------------------------------------------------- */


	/**
	 * The length of the mantissa of a double number 
	 * as returned by {@link BuiltInTypes.DOUBLE.mantissaLen()}. 
	 */
	private final static int MLEN = BuiltInTypes.DOUBLE.mantissaLen();
	
	/**
	 * Digit <code>1</code> at the <code>MLEN</code>th digit 
	 * (counted from the right starting with index <code>0</code>). 
	 * This is the highest possible digit in {@link #mantissa} 
	 * which may be <code>1</code>. 
	 */
	private final static long MASK53 = 1L << MLEN;
	
	/**
	 * Has Digit <code>1</code> at the positions where {@link #mantissa} 
	 * may be <code>1</code> except for the highest possible digit. 
	 * This is used for masking the highest digit. 
	 */
	private final static long MASK53N = MASK53-1;

	/* ---------------------------------------------------------------- *
	 * attributes.                                                      *
	 * ---------------------------------------------------------------- */

	long mantissa;
	int exponent;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	public Long53(double num) {
	    if (num <= 0 || num >= 1) {
		throw new IllegalArgumentException
		    ("Expected number in (0,1) but found " + num + ". ");
	    }
	    this.mantissa = MathExt.mantissaL(num);
	    this.exponent = MathExt.expL(num);
	    assert this.exponent <= 0;
	    assert this.mantissa >= 0;
	    //assert this.mantissa < 1 && this.mantissa > 0;
	}

	public Long53(Long53 other) {
	    this.mantissa = other.mantissa;
	    this.exponent = other.exponent;
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public int shiftLeft2() {
	    if (this.exponent < -1-MLEN) {
		this.exponent++;
		return 0;
	    }
	    int ret = (int)((this.mantissa & MASK53) >> MLEN);
	    //assert ret == 0 || ret == 1;
	    this.mantissa = (this.mantissa & MASK53N) << 1;
	    //this.mantissa <<= 1;
	    return ret;
	}
	public boolean isZero() {
	    return this.mantissa == 0;
	}
	// false because this case is prevented by check in constructor
	public boolean isGEOne() {
	    return false;//this.num >= 1;// **** hack
	}
	// false because this case is prevented by check in constructor
	public boolean isLEZero() {
	    return false;//this.num <= 0;// **** hack
	}
	public NumberWrapper copy() {
	    return new Long53(this);
	}
	public String toString() {
	    return Double
		.toString(this.mantissa*Math.pow(2,this.exponent));
	}
	public boolean equals(Object obj) {
	    Long53 other = (Long53)obj;
	    return 
		this.exponent == other.exponent && 
		this.mantissa == other.mantissa;
	}
	public double doubleValue() {
	    // **** possible loss of precision 
	    return 1.0*this.mantissa * Math.pow(2,this.exponent);
	}
    } //  class Long53

    public static class BigDec10 extends BigDec {

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

  	public BigDec10(BigDecimal num) {
	    super(num);
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public int shiftLeft2() {
	    this.num = this.num.multiply(BigDecimal.TEN);
	    if (this.num.compareTo(BigDecimal.ONE) >= 0) {
		BigDecimal res = this.num.divideToIntegralValue(BigDecimal.ONE);
		this.num = this.num.subtract(res);
		return res.intValue();
	    }
	    return 0;
	}
	public NumberWrapper copy() {
	    return new BigDec10(this.num);
	}
    } //  class BigDec10 

    public static class BigDec implements NumberWrapper {
	/**
	 * Constant with value <code>2</code> 
	 * needed by {@link NumberWrapper.BigD#shiftLeft()}. 
	 */
	final static BigDecimal TWO = new BigDecimal(2.0);//NOPMD

	/* ---------------------------------------------------------------- *
	 * attributes.                                                      *
	 * ---------------------------------------------------------------- */

	BigDecimal num;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

  	public BigDec(BigDecimal num) {
	    this.num = num;
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public int shiftLeft2() {
	    this.num = this.num.multiply(TWO);
	    if (this.num.compareTo(BigDecimal.ONE) >= 0) {
		this.num = this.num.subtract(BigDecimal.ONE);
		return 1;
	    }
	    return 0;
	}
	public boolean isZero() {
	    return this.num.compareTo(BigDecimal.ZERO) == 0;
	}
	public boolean isGEOne() {
	    return this.num.compareTo(BigDecimal.ONE) >= 0;
	}
	public boolean isLEZero() {
	    return this.num.compareTo(BigDecimal.ZERO) <= 0;
	}
	public NumberWrapper copy() {
	    return new BigDec(this.num);
	}
	public String toString() {
	    return this.num.toString();
	}
	public double doubleValue() {
	    return this.num.doubleValue();
	}
    } // class BigDec 

    public static class BigDu implements NumberWrapper {
	/**
	 * Constant with value <code>2</code> 
	 * needed by {@link NumberWrapper.BigD#shiftLeft()}. 
	 */
	//final static BigDecimal TWO = new BigDecimal(2.0);//NOPMD

	/* ---------------------------------------------------------------- *
	 * attributes.                                                      *
	 * ---------------------------------------------------------------- */

	BigDual num;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

  	public BigDu(BigDual num) {
	    this.num = num;
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public int shiftLeft2() {
//System.out.println("BigDual.TWO: "+BigDual.TWO);
	    
	    this.num = this.num.multiply(BigDual.TWO);
//	    this.num = this.num.pow2(1);
	    if (this.num.compareTo(BigDual.ONE) >= 0) {
		this.num = this.num.subtract(BigDual.ONE);
		return 1;
	    }
	    return 0;
	}
	public boolean isZero() {
	    return this.num.compareTo(BigDual.ZERO) == 0;
	}
	public boolean isGEOne() {
	    return this.num.compareTo(BigDual.ONE) >= 0;
	}
	public boolean isLEZero() {
	    return this.num.compareTo(BigDual.ZERO) <= 0;
	}
	public NumberWrapper copy() {
	    return new BigDu(this.num);
	}
	public String toString() {
	    return this.num.toString();
	}
	public double doubleValue() {
	    return this.num.doubleValue();
	}

    } // class BigDu


    /* --------------------------------------------------------------------- *
     * methods.                                                              *
     * --------------------------------------------------------------------- */

    int  shiftLeft2();
    boolean isZero();
    // for a check in constructor of MChunk only 
    boolean isGEOne();
    //for a check in constructor of MChunk only 
    boolean isLEZero();
    NumberWrapper copy();
    double doubleValue();

}
