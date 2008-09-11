package eu.simuline.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import eu.simuline.arithmetics.left2right.BuiltInTypes;

/**
 * Separates the aspects of a non-zero, finite floating point number: 
 * the sign, mantissa and exponent. 
 *
 * Created: Wed Jul 13 17:45:06 2005
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
public class FPSeparator {

    /* -------------------------------------------------------------------- *
     * constants.                                                           *
     * -------------------------------------------------------------------- */

    private final static BigDecimal FIVE  = new BigDecimal(5.0);// NOPMD
    private final static BigDecimal TWO   = new BigDecimal(2.0);// NOPMD
    private final static BigDecimal HALF  = new BigDecimal(0.5);// NOPMD

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * The sign of the number wrapped. 
     * This is either <code>-1</code> or <code>1</code>. 
     * Note that <code>0</code> cannot occur because the constructor 
     * does not allow <code>0.0</code> as an argument. 
     */
    private int sign;

    /**
     * The exponent as an integer value. 
     */
    private int exp;

    /**
     * The mantissa as a <code>double</code> value in <code>[1/2,1)</code>. 
     * In particular, this mantissa is normalized. 
     *
     * @see #mantissaL
     */
    private BigDecimal mantissa;

    /**
     * The mantissa as a <code>long</code> value. 
     * In particular, this mantissa is normalized. 
     * This can be computed from {@link #mantissa} 
     * by a left shift with {@link #doubleMantLen()} digits. 
     * Note that 
     */
    private long       mantissaL;

    /* -------------------------------------------------------------------- *
     * constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Creates a new <code>FPSeparator</code> instance. 
     *
     * @param num 
     *    a finite, nonzero <code>double</code> value. 
     * @throws IllegalArgumentException
     *    if <code>num</code> is either <code>0</code>, 
     *    infinite or <code>NaN</code>. 
     */
    public FPSeparator(double num) {
	if (Double.isNaN(num)) {
	    throw new IllegalArgumentException
		("No NaN allowed but found " + num + ". ");
	}
	if (Double.isInfinite(num)) {
	    throw new IllegalArgumentException
		("Only finite values allowed but found " + num + ". ");
	}
	if (num == 0.0) {
	    throw new IllegalArgumentException
		("Only nonzero values allowed but found " + num + ". ");
	}

	long lRep = Double.doubleToRawLongBits(num);
	this.sign = (((lRep >> 63) == 0) ? 1 : -1);
	this.exp  = (int) ((lRep >> 52) & 0x7ffL) -1075+53;
	this.mantissaL = ((lRep & 0x7ff0000000000000L) == 0) ?
	    (lRep & 0xfffffffffffffL) << 1 :
	    (lRep & 0xfffffffffffffL) | 0x10000000000000L;
	this.mantissa = new BigDecimal
	    (Math.floor(this.mantissaL)*Math.pow(2,-doubleMantLen()));
    }

    /** **** maybe useful: with BigDual 
     * Creates a new <code>FPSeparator</code> instance. 
     *
     * @param num 
     *    a nonzero <code>BigDecimal</code> value. 
     */
    public FPSeparator(BigDecimal num) {
	this.mantissa = num;
	this.sign = num.signum();
	switch (this.sign) {
	    case -1:
		this.mantissa = this.mantissa.negate();
		break;
	    case  1:
		// nothing to do  
		break;
	    case 0:
		throw new IllegalArgumentException
		    ("Only nonzero values allowed but found " 
		     + num.doubleValue() + ". ");
	    default:
		throw new IllegalStateException();
	}
	assert this.mantissa.compareTo(BigDecimal.ZERO) >= 0;
	
	this.exp = (int)Math.ceil(MathExt.ld(this.mantissa.doubleValue()));
	// mantissa := mantissa/2^exp
	BigDecimal pow;
	if (this.exp < 0) {
	    pow = TWO.pow(-this.exp);
	    this.mantissa = this.mantissa.multiply(pow);
	} else {
	    pow = FIVE.pow(this.exp);
	    this.mantissa = this.mantissa.movePointLeft(this.exp);
	    this.mantissa = this.mantissa.multiply(pow);
	}

	if (this.mantissa.compareTo(BigDecimal.ONE) == 0) {
	    this.mantissa = HALF;
	    this.exp++;
	}
	assert 
	    this.mantissa.compareTo(BigDecimal.ZERO) > 0 && 
	    this.mantissa.compareTo(BigDecimal.ONE ) < 0;	
	assert num.compareTo(this.bigDecimalValue()) == 0;
	//assert num.doubleValue() == this.doubleValue();


	long lRep = Double.doubleToRawLongBits(num.doubleValue());
	assert this.sign == (((lRep >> 63) == 0) ? 1 : -1);
 
	assert this.exp == (int) ((lRep >> 52) & 0x7ffL) -1075+53;

	this.mantissaL = ((lRep & 0x7ff0000000000000L) == 0) ?
	    (lRep & 0xfffffffffffffL) << 1 :
	    (lRep & 0xfffffffffffffL) | 0x10000000000000L;
    } // FPSeparator constructor 

    
    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    private int doubleMantLen() {
	return BuiltInTypes.DOUBLE.mantissaLen();
    }

    // either -1 or 1 
    public int sign() {
	return this.sign;
    }

    // the value of the number separated is 
    //            sign()*mantissaBig()*2^ exp()
    // together with mantissaL we have      
    //            sign()*mantissaL    *2^{exp()-doubleMantLen()}
    public int exp() {
	return this.exp;
    }

    // is normalized. 
    public double mantissa() {
	return this.mantissa.doubleValue();
    }

    public long mantissaL() {
	return this.mantissaL;
    }

    // is normalized. 
    public BigDecimal mantissaBig() {
	return this.mantissa;
    }

    public double doubleValue() {
	return this.sign*this.mantissa.doubleValue()*Math.pow(2,this.exp);
    }

    public final BigDecimal bigDecimalValue() {
	
	BigDecimal result = this.mantissa;
	if (this.sign == -1) {
	    result = this.mantissa.negate();
	}
	result = result.stripTrailingZeros();
	result = result.multiply(new BigDecimal(Math.pow(2,this.exp)));	
	return result.stripTrailingZeros();
    }

    public static void main(String[] args) {
	long time;
	double num;

	time = System.currentTimeMillis();
	for (int i = 0; i < 10000000; i++) {
	    num = Math.random();
	    for (int j = 0; j < 100; j++) {
		num *= 2;
	    }
	}
	System.out.println("time: "+(System.currentTimeMillis()-time));

	time = System.currentTimeMillis();
	int exp;
	long lRep;
	for (int i = 0; i < 10000000; i++) {
	    num = Math.random();
	    lRep = Double.doubleToRawLongBits(num);
	    exp = (int) ((lRep >> 52) & 0x7ffL) -1075+53;
	    for (int j = 0; j < 100; j++) {
		exp++;
	    }
	}
	System.out.println("time: "+(System.currentTimeMillis()-time));




	
// 	BigDecimal one = BigDecimal.ONE;
// 	one = one.setScale(8);
// 	System.out.println(": "+one.divide(new BigDecimal(32),
// 					   RoundingMode.UP));
	
    }


} // FPSeparator
