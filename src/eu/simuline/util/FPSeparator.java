package eu.simuline.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
     */
    private BigDecimal mantissa;

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
	this(double2BigDecimal(num));
    }

    /**
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
		    ("Only nonzero values allowed but found " + num + ". ");
	    default:
		throw new IllegalStateException();
	}
	assert this.mantissa.compareTo(BigDecimal.ZERO) > 0;
	
	this.exp = (int)Math.ceil(Math.log10(this.mantissa.doubleValue())
				  /Math.log10(2.0));
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
    } // FPSeparator constructor 

    
    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    /**
     * Converts a <code>double</code> value 
     * into a <code>BigDecimal</code> value. 
     *
     * @param num 
     *    a finite, nonzero <code>double</code> value. 
     * @throws IllegalArgumentException
     *    if <code>num</code> is either infinite or <code>NaN</code>. 
     */
    private static BigDecimal double2BigDecimal(double num) {
	if (Double.isNaN(num)) {
	    throw new IllegalArgumentException
		("No NaN allowed but found " + num + ". ");
	}
	if (Double.isInfinite(num)) {
	    throw new IllegalArgumentException
		("Only finite values allowed but found " + num + ". ");
	}
	return new BigDecimal(Double.toString(num));
    }

    // either -1 or 1 
    public int sign() {
	return this.sign;
    }

    public int exp() {
	return this.exp;
    }

    // is normalized. 
    public double mantissa() {
	return this.mantissa.doubleValue();
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
	result = result.multiply(new BigDecimal(Math.pow(2,this.exp)));
	
	
	return result.stripTrailingZeros();
    }

    public static void main(String[] args) {
	BigDecimal one = BigDecimal.ONE;
	one = one.setScale(8);
	System.out.println(": "+one.divide(new BigDecimal(32),
					   RoundingMode.UP));
	
    }

} // FPSeparator
