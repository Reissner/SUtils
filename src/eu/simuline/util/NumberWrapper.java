package eu.simuline.util;

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

    /**
     * Constant with value <code>2</code> 
     * needed by {@link NumberWrapper.BigD#shiftLeft()}. 
     */
    final static BigDecimal TWO = new BigDecimal(2.0);//NOPMD

    public static class Dbl implements NumberWrapper {
	double num;
	public Dbl(double num) {
	    this.num = num;
	}
	public void shiftLeft() {
	    this.num *= 2;
	}
	public void subtractOne() {
	    this.num -= 1;
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
    } //  class Dbl 

    public static class BigD implements NumberWrapper {
	BigDecimal num;
	public BigD(BigDecimal num) {
	    this.num = num;
	}
	public void shiftLeft() {
	    this.num = this.num.multiply(TWO);
	}
	public void subtractOne() {
	    this.num = this.num.subtract(BigDecimal.ONE);
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
	    return new BigD(this.num);
	}
	public String toString() {
	    return this.num.toString();
	}
    } // class BigD 

    void shiftLeft();
    void subtractOne();
    boolean isZero();
    boolean isGEOne();
    boolean isLEZero();
    NumberWrapper copy();
}
