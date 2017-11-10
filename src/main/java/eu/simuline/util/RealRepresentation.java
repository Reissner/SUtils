

package eu.simuline.util;

import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.Map;
import java.util.HashMap;

/**
 * Describes the representation of a real value, 
 * which may be either a double, resp. a Double-value 
 * or a {@link BigDecimal} for use in tables. 
 * At a rudimentary stage, {@link RealRepresentation} wraps the number 
 * whereas {@link RealRepresentation.Desc} describes 
 * how to format it, e.g. where to add blanks. 
 * <p>
 * Also {@link #asInteger} provides some support for formatting integers. 
 * These are the two supported levels of precision. 
 * It is characterized by the {@link #mantissa} and the {@link #exponent}. 
 * The mantissa in turn 
 * splits into the {@link #integer} and the {@link #fraction}al part. 
 * The essential point is that these three parts are properly aligned 
 * and filled up with <code>0</code>'s and blanks by need. 
 * <p>
 * The splitting between integer, fraction and exponent is 
 * as specified for {@link Double#toString(double)}. 
 * The difference between the two levels of precision 
 * is made as transparent as possible. 
 * Transparence is given up in (at least) the following cases: 
 * <ul>
 * <li>
 * unlike <code>BigDecimal</code>s 
 * <code>double</code>s (influenced by the standard IEEE 754), 
 * distinguish between <code>+0</code> and <code>-0</code>. 
 * <li>
 * for doubles by specification of the string representation, 
 * <pre>
 * "There must be at least one digit to represent the fractional part, 
 * and beyond that as many, but only as many, 
 * more digits as are needed to uniquely distinguish the argument value 
 * from adjacent values of type double. 
 * That is, suppose that x is the exact mathematical value 
 * represented by the decimal representation produced by this method 
 * for a finite nonzero argument d. 
 * Then d must be the double value nearest to x; 
 * or if two double values are equally close to x, 
 * then d must be one of them 
 * and the least significant bit of the significand of d must be 0. "
 * </pre>
 * This does not make sense for <code>BigDecimals</code> 
 * because there is no nearest <code>BigDecimal</code>: 
 * The <code>BigDecimal</code>s are "almost" dense in the real numbers. 
 * Instead we neglect any trailing zeros. 
 * In later versions, this should be different. 
 * </ul>
 *
 * Also infinite <code>double</code>'s and <code>NaN</code> are not supported. 
 * This is because they have no splitting into integer, 
 * fractional part and exponent. 
 * They do not make sense for <code>BigDecimal</code>s 
 * and so this would make transparency more difficult. 
 * Note that denormalization of <code>double</code>'s 
 * does not affect their string representation 
 * such that this is completely as forfor<code>BigDecimal</code>s 
 * for which automatic denormalization does not make sense 
 * (although can be forced by the user). 
 *
 */
public class RealRepresentation {

    /* -------------------------------------------------------------------- *
     * class constants.                                                     *
     * -------------------------------------------------------------------- */

    /**
     * Describe the action to be taken if a sequence of symbols, 
     * e.g. digits are longer as needed. 
     *
     * @see #alignLeft
     * @see #alignRight
     */
    public static final Cutter ALIGN_CUT_OFF_LEFT = new Cutter() {
	    public String cut(String str, int len) {
		int strLen = str.length();
		return str.substring(strLen - len, strLen);
	    }
	};

    /**
     * Describe the action to be taken if a sequence of symbols, 
     * e.g. digits are longer as needed. 
     *
     * @see #alignLeft
     * @see #alignRight
     */
    public static final Cutter ALIGN_CUT_OFF_RIGHT = new Cutter() {
	    public String cut(String str, int len) {
		return str.substring(0, len);
	    }
	};

    /**
     * Describe the action to be taken if a sequence of symbols, 
     * e.g. digits are longer as needed. 
     *
     * @see #alignLeft
     * @see #alignRight
     * @see #trimExponent
     */
    public static final Cutter ALIGN_EXCEPTION = new Cutter() {
	    public String cut(String str, int len) {
		throw new IllegalArgumentException
		    ("String \"" + str + "\" longer than expected (" + 
		     len + "). ");

	    }
	};

    /**
     * Describe the action to be taken if a sequence of symbols, 
     * e.g. digits are longer as desired. 
     *
     * @see #alignLeft
     * @see #alignRight
     * @see #trimExponent
     */
    public static final Cutter ALIGN_LEAVE_UNCHANGED = new Cutter() {
	    public String cut(String str, int len) {
		return str;
	    }
	};

    /**
     * The keys are Characters (intended for either " " or "0") 
     * and the associated values are strings 
     * consisting of a number of characters given by the key. 
     *
     * @see #fill
     */
    private static final Map<Character, String> FILL_STRINGS = 
	new HashMap<Character, String>();

    /**
     * A string containing the character signifying an exponent. 
     * This is <code>e</code> but could also be <code>E</code>.
     */
    public  static final String EXP_CHAR = "e";

    /**
     * Substitutes the place for the point 
     * in case the real number represented has no fractional part. 
     * This is either " " or "". 
     * The default value is " ". 
     *
     * @see #compensatePoint
     */
    private       static String   noPointS = " ";

    /**
     * A string constant containing the decimal point. 
     */
    private static final String    POINT_S = ".";

    /**
     * A character constant containing <code>+</code>. 
     */
    private static final String SIGN_PLUS  = "+";

    /**
     * A character constant containing <code>+</code>. 
     */
    private static final String SIGN_MINUS = "-";

    /**
     * Sets {@link #noPointS} according to the parameter: 
     * <code>true</code> means that {@link #noPointS} is attached with " "; 
     * otherwise it is "". 
     *
     * @param compensate 
     *    a <code>boolean</code> deciding 
     *    whether to compensate a decimal point 
     *    in case the real number represented has no fractional part. 
     */
    public static void compensatePoint(boolean compensate) {
	noPointS = compensate ? " " : "";
    }

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Describes the representation of a {@link BigDecimal}-value. 
     * It is characterized 
     * by the {@link #mantissa} and the {@link #exponent}. 
     * The mantissa in turn 
     * splits into the {@link #integer} and the {@link #fraction}al part. 
     *
     * @author <a href="mailto:e.reissner@rose.de">Ernst Reissner</a>
     * @version 1.0
     */
    private static final class BigDecimalRep extends RealRepresentation {

	/**
	 * The number of digits right of the point 
	 * such that the representation is without exponent. 
	 * This evaluates to <code>-3</code> as specified for double's 
	 * in {@link Double#toString(double)} and is used by 
	 * {@link #RealRepresentation.BigDecimalRep(BigDecimal)}. 
	 */
	private static final int MAX_FRAC_DIGITS_NO_EXP = -3;

	/**
	 * The number of digits left of the point 
	 * such that the representation is without exponent. 
	 * This evaluates to <code>7</code> as specified for double's 
	 * in {@link Double#toString(double)} and is used by 
	 * {@link #RealRepresentation.BigDecimalRep(BigDecimal)}. 
	 */
	private static final int MAX_INT_DIGITS_NO_EXP = 7;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	/**
	 * Constructs a newly allocated <code>BigDecimalRep</code> 
	 * that represents given <code>BigDecimal</code> argument. 
	 *
	 * @param dec 
	 *    the value to be represented 
	 *    by this <code>BigDecimalRep</code>. 
	 */
	BigDecimalRep(BigDecimal dec) {
	    switch (dec.signum()) {
	    case -1:
		this.sign = SIGN_MINUS;
		dec = dec.abs();
		break;
	    case  0:
		//assert exp == 0;
		this.sign = SIGN_PLUS;
		this.integer = "0";
		//this.mantissa ="0.0";
		this.fraction = "0";
		this.exponent = "";
		return;
	    case  1:
		this.sign = SIGN_PLUS;
		break;
	    default:
		throw new IllegalStateException
		    ("Found unexpected sign " + dec.signum() + ". ");
	    }
	    // Here, d is not zero, mantissa is not "0" and has no sign. 

	    int exp = -dec.scale();
	    String mantissa = dec.unscaledValue().toString();

	    if (new BigDecimal("1.0e" + MAX_FRAC_DIGITS_NO_EXP).compareTo(dec)
		<= 0 && 
		new BigDecimal("1.0e" +  MAX_INT_DIGITS_NO_EXP).compareTo(dec)
		> 0) {
		// the representation is xxx.yyy without exponent. 
		this.exponent = "";
		if (BigDecimal.valueOf(1).compareTo(dec) > 0) {
		    // Here, 0 < d < 1. 
		    mantissa = alignRight(mantissa,
					  1 - exp,
					  '0',
					  ALIGN_EXCEPTION);
		}
		// the representation xxx.yyy with nontrivial integer part. 
		this.integer  = mantissa
		    . substring(0,
				mantissa.length() + exp);
		this.fraction = mantissa
		    . substring(mantissa.length() + exp,
				mantissa.length());
	    } else {
		// representation a.yyyezzz with exponent 
		// where a is a digit != 0. 
		this.exponent = Integer
		    .toString(exp + mantissa.length() - 1);
		this.integer  = mantissa.substring(0, 1);
		this.fraction = mantissa
		    . substring(1, mantissa.length());
	    }

	    // remove trailing zeros. 
	    this.fraction = this.fraction.replaceFirst("0*$", "");
	    // add a zero if nothing else is left. 
	    if (this.fraction.length() == 0) {
		this.fraction = "0";
	    }
	    //this.mantissa = 
	    //	this.integer + POINT_S + this.fraction;
	}

	/**
	 * Constructs a newly allocated 
	 * <code>BigDecimalRep</code> object 
	 * that represents the floating-point value of type double 
	 * represented by the string. 
	 * The string is converted to a double value 
	 * as if by the method {@link Double#valueOf}. 
	 *
	 * @param dStr 
	 *    a string to be converted 
	 *    to a <code>BigDecimalRep</code>. 
	 * @see Double#Double(String)
	 */
	BigDecimalRep(String dStr) {
	    this(new BigDecimal(dStr));
	}
    } // class BigDecimalRep

    /**
     * Describes the representation of a double, resp. a Double-value. 
     * It is characterized 
     * by the {@link #mantissa} and the {@link #exponent}. 
     * The mantissa in turn 
     * splits into the {@link #integer} and the {@link #fraction}al part. 
     *
     * @author <a href="mailto:e.reissner@rose.de">Ernst Reissner</a>
     * @version 1.0
     */
    private static final class DoubleRep extends RealRepresentation {

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	/**
	 * Constructs a newly allocated 
	 * <code>DoubleRep</code> object 
	 * that represents the primitive <code>double</code> argument. 
	 *
	 * @param dbl 
	 *    the value to be represented 
	 *    by this <code>DoubleRep</code>. 
	 * @see Double#Double(double)
	 * @throws NumberFormatException
	 *    if <code>d</code> is either infinite or not a number. 
	 */
	DoubleRep(double dbl) {

	    // Exclude that d is NaN or infinite 
	    if (Double.isNaN(dbl) || Double.isInfinite(dbl)) {
		// Replace dbl by |dbl| 
		// because of bug in constructor BigDecimal(double). 
		throw new NumberFormatException
		    ("For input string: \"" + Math.abs(dbl) + "\"");
	    }
	    // Here d is neither NaN nor infinite. 

	    String str = Double.toString(dbl);
	    String mantissa;
	    int index;
	    index = str.indexOf('E');
	    if (index == -1) {
		index = str.indexOf('e');
	    }
	    // Here, either index == -1 or index is the index of the exp. 

	    if (index == -1) {
		// Here, no exponent is given. 
		this.exponent = "";
		mantissa = str;
	    } else {
		// Here, an exponent is given. 
		this.exponent = str.substring(index + 1, str.length());
		mantissa = str.substring(0, index);
		initSignExp();
	    }
	    // Here, mantissa and exponent are separated 
	    // and also str is not empty. 

	    // extract sign (also think of the distinction -0.0 and +0.0). 
	    Number2SignUnsigned sus = new Number2SignUnsigned(mantissa);
	    this.sign = sus.getSign();
	    mantissa = sus.getUnSigned();
	    // Here, str is the unsigned mantissa. 

	    index = mantissa.indexOf(POINT_S);
	    if (index == -1) {
		// no decimal point: mantissa is an integer. 
		// according to the docs of Double.toString(double) 
		// this does not occur, but...
		throw new IllegalStateException
		    ("Unlike specified for Double.toString(double) " + 
		     "found no decimal point in \"" + mantissa + "\". ");
	    }
	    this.integer  = mantissa.substring(0, index);
	    this.fraction = mantissa.substring(index + 1,
					       mantissa.length());
	    // Here, mantissa is separated into integer and fraction. 
	}

	/**
	 * Constructs a newly allocated 
	 * <code>DoubleRep</code> object 
	 * that represents the floating-point value of type double 
	 * represented by the string. 
	 * The string is converted to a double value 
	 * as if by the method {@link Double#valueOf}. 
	 *
	 * @param dStr 
	 *    a string to be converted 
	 *    to a <code>DoubleRep</code>. 
	 * @see Double#Double(String)
	 * @throws IllegalArgumentException
	 *    if <code>d</code> is either infinite or not a number. 
	 * @throws NumberFormatException
	 *    if <code>d</code> has not the appropriate number format. 
	 */
	DoubleRep(String dStr) {
	    this(Double.parseDouble(dStr));
	}

	/**
	 * Creates a new <code>DoubleRep</code>. 
	 * An analog to {@link #RealRepresentation.DoubleRep(double)}. 
	 *
	 * @param dbl 
	 *    a <code>Double</code> object. 
	 * @throws IllegalArgumentException
	 *    if <code>d</code> is either infinite or not a number. 
	 */
	DoubleRep(Double dbl) {
	    this(dbl.doubleValue());
	}

	/* ---------------------------------------------------------------- *
	 * general methods.                                                 *
	 * ---------------------------------------------------------------- */

    } // class DoubleRep 

    /**
     * A <code>Cutter</code> is used to shorten numbers. 
     * This can either be done by cutting, by rounding or somenthing else. 
     */
    public interface Cutter {
	/**
	 * Returns a substring of <code>str</code> 
	 * with length <code>len</code> or throws an exception. 
	 *
	 * @param str 
	 *    a <code>String</code> with <code>str.length() >= len</code>. 
	 * @param len 
	 *    the desired length of the resulting string. 
	 * @return 
	 *    a substring of <code>str</code> with length <code>len</code>. 
	 * @throws RuntimeException
	 *    by need if the cut is not allowed. 
	 */
	String cut(String str, int len);
    } // interface Cutter 

    /**
     * Container for number splitted into sign and unsigned. 
     */
    private static class Number2SignUnsigned {

	/**
	 * The sign which is either {@link #SIGN_PLUS} or {@link #SIGN_MINUS}. 
	 */
	private final String sign;

	/**
	 * The unsigned part of a number. 
	 */
	private final String unSigned;

	/**
	 * Creates a new <code>Number2SignUnsigned</code> instance 
	 * wrapping a string representation of a number.
	 *
	 * @param signed 
	 *    a <code>String</code> representation of a number. 
	 *    There may be either an explicit sign 
	 *    or the first symbol is a digit. 
	 * @throws NumberFormatException
	 *    if the first symbol is neither a sign nor a digit. 
	 */
	Number2SignUnsigned(String signed) {
	    // extract sign (also think of the distinction -0.0 and +0.0). 
	    switch (signed.charAt(0)) {
	    case '+':
		// explicit + sign 
		this.sign = SIGN_PLUS;
		this.unSigned = signed.substring(1, signed.length());
		break;
	    case '-':
		// explicit - sign 
		this.sign = SIGN_MINUS;
		this.unSigned = signed.substring(1, signed.length());
		break;
	    default:
		// implicit + sign 
		// Here, one should have some digit at the 0th place. 
		if (!signed.substring(0, 1).matches("\\d")) {
		    throw new NumberFormatException
			("Expected unsigned number found " + signed + ". ");
		}
		this.sign = SIGN_PLUS;
		this.unSigned = signed;
	    }
	}

	String getSign() {
	    return this.sign;
	}

	String getUnSigned() {
	    return this.unSigned;
	}

    } // class Number2SignUnsigned 


    /**
     * Describes the way 
     * the real number represented by {@link RealRepresentation} 
     * is displayed as a string. 
     */
    public static class Desc {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	/**
	 * Signifies whether an attempt 
	 * to cut off parts of the integer will result in an exception; 
	 * otherwise it is just ignored. 
	 */
	private boolean strictInteger = false;

	/**
	 * Signifies whether an attempt 
	 * to cut off parts of the exponent will result in an exception; 
	 * otherwise it is just ignored. 
	 */
	private boolean strictExponent = false;

	private  Cutter fractionCutter = ALIGN_CUT_OFF_RIGHT;

	/**
	 * The maximal length of the integer part of a number
	 * displayed in a specific column of the table.
	 */
	private int lenI;

	/**
	 * The maximal length of the mantissa of a number
	 * displayed in a specific column of the table.
	 * @see #lenI
	 */
	private int lenM;

	/**
	 * The maximal length of the exponent part of a number
	 * displayed in a specific column of the table.
	 * @see #lenI
	 */
	private int lenE;


	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	// **** under construction. 
	public Desc(int lenI, int lenM, int lenE) {
	    this.lenI = lenI;
	    this.lenM = lenM;
	    this.lenE = lenE;
	}
    } // class Desc 


    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * The sign of the double value represented. 
     * This may be either {@link #SIGN_PLUS +} or {@link #SIGN_MINUS -}. 
     * Note that the standard IEEE 754 distinguishes <code>-0</code> 
     * from <code>+0</code> and so do we in the subclass 
     * {@link RealRepresentation.DoubleRep}. 
     */
    protected String sign;

    /*
     * The mantissa of the double value represented. 
     */
    //protected String mantissa;

    /**
     * The integer part of the mantissa of the double value represented. 
     */
    protected String integer;

    /**
     * The fractional part of the mantissa of the double value represented. 
     * This may never be empty but it may be <code>0</code>. 
     */
    protected String fraction;


    /**
     * The exponent of the double value represented. 
     */
    protected String exponent;

    private String signOfExp;

    private String unsignedExp;

    /* -------------------------------------------------------------------- *
     * create methods: out of Strings, doubles, Doubles and BigDecimals.    *
     * -------------------------------------------------------------------- */

    protected final void initSignExp() {
	Number2SignUnsigned sus = new Number2SignUnsigned(this.exponent);
	this.signOfExp = sus.getSign();
	this.unsignedExp = sus.getUnSigned();
    }

    /**
     * Converts the given string representation of a real number 
     * to a <code>RealRepresentation</code> with the given precision. 
     *
     * @param val 
     *    a <code>String</code> representation of a real number. 
     * @param precision 
     *    a <code>boolean</code> which offers choice between 
     *    full precision (<code>true</code>) and 
     *    double precision (<code>false</code>). 
     * @return 
     *    a <code>RealRepresentation</code> of the real value 
     *    given by the string representation <code>val</code> 
     *    with the precision determined by <code>precision</code>. 
     * @throws NumberFormatException
     *   if <code>val</code> is not the string representation of a double 
     *   or if it is <code>NaN</code> or represents an infinite value. 
     */
    public static RealRepresentation create(String val, boolean precision) {
	return precision ? new BigDecimalRep(val) : new     DoubleRep(val);
    }

    /**
     * Converts the given high precision representation of a real number 
     * to a <code>RealRepresentation</code> 
     * preserving full precision if so specified. 
     *
     * @param val 
     *    a <code>Number</code> value 
     *    which is either a {@link BigDecimal} or a <code>Double</code>. 
     * @param precision 
     *    specifies whether to preserve full precision. 
     *    If set to <code>false</code> 
     *    only <code>double</code> precision is used. 
     * @return 
     *    a <code>RealRepresentation</code> of the real value 
     *    given by <code>val</code> 
     *    preserving full precision is specified so. 
     * @throws ClassCastException 
     *    if <code>val</code> is neither a {@link BigDecimal} 
     *    nor a <code>Double</code>. 
     */
    public static RealRepresentation create(Number val,
					    boolean precision) {
	if (precision) {
	    return (val instanceof BigDecimal)
		? new BigDecimalRep( (BigDecimal) val               )
		: new     DoubleRep(((Double    ) val).doubleValue());
	} else {
	    return new DoubleRep(val.doubleValue());
	}
    }

    /**
     * Converts the given <code>double</code> number 
     * to a <code>RealRepresentation</code> with the given precision. 
     *
     * @param val 
     *    a <code>double</code> value. 
     * @param precision 
     *    a <code>boolean</code> which offers choice between 
     *    full precision (<code>true</code> which is obsolete****) and 
     *    double precision (<code>false</code>). 
     * @return 
     *    a <code>RealRepresentation</code> of the given <code>double</code> 
     *    with the precision determined by <code>precision</code>. 
     * @throws NumberFormatException
     *   if <code>val</code> is either <code>NaN</code> or infinite. 
     */
    public static RealRepresentation create(double val,
					    boolean precision) {
	return create(new Double(val), precision);
    }

    /**
     * Converts the given number 
     * to a <code>RealRepresentation</code> with the natural precision. 
     *
     * @param val 
     *    a <code>Number</code> value 
     *    which is either a {@link BigDecimal} or a <code>Double</code>. 
     * @return 
     *    a <code>RealRepresentation</code> of the given number 
     *    with the natural precision: 
     *    <code>create((BigDecimal)val, true)</code> or 
     *    <code>create((Double    )val, true)</code>. 
     * @throws ClassCastException 
     *    if <code>val</code> is neither a {@link BigDecimal} 
     *    nor a <code>Double</code>. 
     */
    public static RealRepresentation create(Number val) {
	return val instanceof BigDecimal 
	    ? create((BigDecimal) val, true) 
	    : create((Double    ) val, false);
    }

    /* -------------------------------------------------------------------- *
     * methods for aligning strings.                                        *
     * -------------------------------------------------------------------- */

    /**
     * Returns a <code>String</code> 
     * consisting of <code>len</code> copies of the character <code>c</code>. 
     *
     * @param chr0 
     *    a <code>char</code>. 
     * @param len 
     *    the number of repetitions of <code>c</code> needed. 
     *    This should be a non-negative integer. 
     * @return 
     *    a <code>String</code> consisting of <code>len</code> characters 
     *    <code>c</code>. 
     */
    public static String fill(char chr0, int len) {
	Character chr = new Character(chr0);
	String cutString = FILL_STRINGS.get(chr);
	// by contract, cutString is either null or 
	// its length is 2^n for some natural number n. 
	boolean copyBack = false;
	if (cutString == null) {
	    cutString = chr.toString(); //Character.toString(c);
	    copyBack  = true;
	}
	
	// Here, cutString != null && cutString.length() > 0 

	if (cutString.length() < len) {
	    copyBack  = true;
	    StringBuffer cutBuf = new StringBuffer(cutString);
	    while (cutBuf.length() < len) {
		cutBuf.append(cutBuf);
	    }
	    cutString = cutBuf.toString();
	}

	// Here, cutString != null && cutString.length() >= len 
	if (copyBack) {
	    FILL_STRINGS.put(chr, cutString);
	}
	// Here, (String)FILL_STRINGS.get(chr).equals(cutString) again.  

	return cutString.substring(0, len);
    }

    /**
     * Returns a <code>String</code> which is 
     * by attaching the minimal number of <code>filler</code>s 
     * to the right hand side of <code>str</code> 
     * such that the length of the result is at least <code>int</code>. 
     * For <code>strict == true</code> even equality is assured. 
     *
     * @param str 
     *    the <code>String</code> to be aligned. 
     * @param len 
     *    an <code>int</code> value which determines 
     *    the length of the result string: 
     * @param filler 
     *    a <code>char</code> value. 
     * @param  cutter
     *    takes effect if <code>str.length > len</code>: 
     *    <ul>
     *    <li>
     *    for {@link #ALIGN_CUT_OFF_RIGHT} superfluous digits are cut off, 
     *    <li> {
     *    for {@link #ALIGN_EXCEPTION} superfluous digits cause an exception, 
     *    <li>
     *    for {@link #ALIGN_LEAVE_UNCHANGED} 
     *    superfluous digits are left unchanged, 
     *    </ul>
     * @return 
     *    <ul>
     *    <li> for <code>str.length()&le;len</code> 
     *    copies of  <code>filler</code>s 
     *    are attached to the right hand side of <code>str</code> 
     *    such that the length of the result is least <code>int</code>. 
     *    <li> for <code>str.length()>len</code> 
     *    <ul>
     *    <li> 
     *    <code>str</code> is returned as is 
     *    provided <code>cutter == {@link #ALIGN_LEAVE_UNCHANGED}</code> 
     *    <li> 
     *    For <code>cutter == {@link #ALIGN_EXCEPTION}</code> 
     *    there is no return value: 
     *    an exception is thrown and finally 
     *    <li> 
     *    for <code>cutter == {@link #ALIGN_CUT_OFF_RIGHT}</code> 
     *    the overhead is silently cut off. 
     *    </ul>
     *    </ul>
     * @throws IllegalArgumentException 
     *    for <code>cutter == {@link #ALIGN_EXCEPTION}</code> 
     *    if <code>str.length()</code> exceeds <code>len</code>. 
     */
    public static String alignLeft(String str, 
				   int len,
				   char filler,
				   Cutter cutter) {
	int strLen = str.length();
	return strLen > len 
	    ? cutter.cut(str, len) 
	    : str + fill(filler, len - strLen);
    }

    /**
     * Returns a <code>String</code> which is 
     * by attaching the minimal number of <code>filler</code>s 
     * to the left hand side of <code>str</code> 
     * such that the length of the result is at least <code>int</code>. 
     * For <code>strict == true</code> even equality is assured. 
     *
     * @param str 
     *    the <code>String</code> to be aligned. 
     * @param len 
     *    an <code>int</code> value which determines 
     *    the length of the result string: 
     * @param filler 
     *    a <code>char</code> value. 
     * @param  cutter
     *    takes effect if <code>str.length > len</code>:  { {
     *    <ul>
     *    <li>
     *    for {@link #ALIGN_CUT_OFF_LEFT} superfluous digits are cut off, 
     *    <li>
     *    for {@link #ALIGN_EXCEPTION} superfluous digits cause an exception, 
     *    <li>
     *    for {@link #ALIGN_LEAVE_UNCHANGED} 
     *    superfluous digits are left unchanged, 
     *    </ul>
     * @return 
     *    <ul>
     *    <li> for <code>str.length()&le;len</code> 
     *    copies of  <code>filler</code>s 
     *    are attached to the right hand side of <code>str</code> 
     *    such that the length of the result is least <code>int</code>. 
     *    <li> for <code>str.length()>len</code> 
     *    <ul>
     *    <li> 
     *    <code>str</code> is returned as is 
     *    provided <code>cutter == {@link #ALIGN_LEAVE_UNCHANGED}</code> 
     *    <li> 
     *    For <code>cutter == {@link #ALIGN_EXCEPTION}</code> 
     *    there is no return value: 
     *    an exception is thrown and finally 
     *    <li> 
     *    for <code>cutter == {@link #ALIGN_CUT_OFF_LEFT}</code> 
     *    the overhead is silently cut off. 
     *    </ul>
     *    </ul>
     * @throws IllegalArgumentException 
     *    for <code>cutter == {@link #ALIGN_EXCEPTION}</code> 
     *    if <code>str.length()</code> exceeds <code>len</code>. 
     */
    public static String alignRight(String str,
				    int len,
				    char filler,
				    Cutter cutter) {
	int strLen = str.length();
	return  strLen > len 
	    ? cutter.cut(str, len) 
	    : fill(filler, len - strLen) + str;
    }

    /* -------------------------------------------------------------------- *
     * trim and cut methods.                                                *
     * -------------------------------------------------------------------- */

    /*
    private void updateMantissa() {
	this.mantissa = 
	    this.integer +
	    (hasFraction() ? POINT_S : noPointS) + 
	    this.fraction;
    }
    */

    // **** for align methods: message of exception 
    // Cannot trim integer " + this.integer +  " to " + numDigits + " digits. "
    // better than current one. 
    /**
     * Trims the integer part 
     * to length <code>numDigits</code> if possible. 
     * If this leads to a prolongation of the integer part, 
     * it is filled up with characters <code>blankOrNull</code> 
     * from the left hand side. 
     * If the integer part is already longer than specified, 
     * the action taken relies on the parameter <code>strict</code>. 
     * Caution: ***** this does not work very well 
     * for <code>numDigits = 0</code>. 
     * Caution: **** not ok with signs. 
     * Caution: **** what to do if ".4" is needed? 
     *
     * @param numDigits 
     *    a non-negative <code>int</code> value signifying the length 
     *    of the integer part. 
     * @param blankOrNull 
     *    a <code>char</code> with which the integer part is filled up 
     *    by need from the left hand side 
     *    to reach length <code>numDigits</code>. 
     *    Useful settings are probably blank and <code>'0'</code> only. 
     * @param strict 
     *    a <code>boolean</code> signifying whether an attempt 
     *    to cut off parts of the integer will result in an exception; 
     *    otherwise it is just ignored. 
     * @return 
     *    the new integer part as a <code>String</code>. 
     * @throws IllegalArgumentException 
     *    for <code>strict( == true)</code> 
     *    if the length of the integer part exceeds <code>numDigits</code>. 
     */
    public final String trimInteger(int numDigits,
			      char blankOrNull,
			      boolean strict) {
	Cutter cutter = strict ? ALIGN_EXCEPTION : ALIGN_LEAVE_UNCHANGED;
	this.integer = alignRight(this.integer,
				  numDigits,
				  blankOrNull,
				  cutter);
	//updateMantissa();
	return this.integer;
    }

    /**
     * Trims the exponent part 
     * to length <code>numDigits</code> if possible. 
     * If this leads to a prolongation of the exponent part, 
     * it is filled up with characters <code>blankOrNull</code> 
     * from the left hand side. 
     * If the exponent part is already longer than specified, 
     * the action taken relies on the parameter <code>strict</code>. 
     * Caution: ***** this does not work very well 
     * for <code>numDigits = 0</code>. 
     * Caution: **** not ok with signs. 
     * Caution: **** not ok if there are no exponents at all. 
     *
     * @param numDigits 
     *    a non-negative <code>int</code> value signifying the length 
     *    of the exponent part. 
     * @param blankOrNull 
     *    a <code>char</code> with which the exponent part is filled up 
     *    by need from the left hand side 
     *    to reach length <code>numDigits</code>. 
     *    Useful settings are probably blank and <code>'0'</code> only. 
     * @param strict 
     *    a <code>boolean</code> signifying whether an attempt 
     *    to cut off parts of the exponent will result in an exception; 
     *    otherwise it is just ignored. 
     * @return 
     *    the new exponent as a <code>String</code>. 
     * @throws IllegalArgumentException 
     *    for <code>strict( == true)</code> 
     *    if the length of the exponent part exceeds <code>numDigits</code>. 
     */
    public final String trimExponent(int numDigits,
			       char blankOrNull,
			       boolean strict) {
	Cutter cutter = strict ? ALIGN_EXCEPTION : ALIGN_LEAVE_UNCHANGED;
	this.exponent = alignRight(this.exponent,
				   numDigits,
				   blankOrNull,
				   cutter);
	return this.exponent;
    }

    /**
     * Trims the fractional part 
     * to length <code>numDigits</code> if possible. 
     * If this leads to a prolongation of the fractional part, 
     * it is filled up with characters <code>blankOrNull</code>. 
     * If the fractional part is already longer than specified, 
     * the action taken relies on the code <code>cutter</code>. 
     * Caution: ***** this does not work very well 
     * for <code>numDigits = 0</code>. 
     * Caution: **** this method simply cuts off digits 
     * without rounding. 
     *
     * @param numDigits 
     *    a non-negative <code>int</code> value signifying the length 
     *    of the fractional part. 
     * @param blankOrNull 
     *    a <code>char</code> with which the fractional part is filled up 
     *    by need to reach length <code>numDigits</code>. 
     *    Useful settings are probably <code>'0'</code> and blank only. 
     * @param cutter 
     *    an <code>int</code> code which is relevant only, 
     *    if the current fractional part is longer 
     *    than specified by <code>numDigits</code>. 
     * @return 
     *    the new trimmed fractional part as a <code>String</code>. 
     * @throws IllegalArgumentException 
     *    for <code>cutter == {@link #ALIGN_EXCEPTION}</code> 
     *    if the length of the fractional part 
     *    exceeds <code>numDigits</code>. 
     */
    public final String trimFraction(int numDigits,
			       char blankOrNull,
			       Cutter cutter) {
	this.fraction = 
	    alignLeft(this.fraction, numDigits, blankOrNull, cutter);
	//updateMantissa();
	return this.fraction;
    }

    /* -------------------------------------------------------------------- *
     * representation methods.                                              *
     * -------------------------------------------------------------------- */

    /**
     * Converts this representation such that 
     * there is neither a {@link #fraction} nor an {@link #exponent}. 
     * Of course in general thereby the current format is lost. 
     *
     * @throws NumberFormatException 
     *    if this representation cannot be written as an integer. 
     */
    public final void asInteger() {
	String trimmedExponent = this.exponent.trim();
	BigInteger expVal = "".equals(trimmedExponent) 
	    ? BigInteger.ZERO 
	    : new BigInteger(trimmedExponent);
	// remove trailing 0's and blanks. 
	String trimmedFraction = this.fraction.replaceAll("0* *$", "");
	expVal = expVal.subtract
	    (new BigInteger(Integer.toString(trimmedFraction.length())));
	this.integer += trimmedFraction;
	// Here the representation this.integer.0EexpVal is without fraction. 

	this.integer = this.integer.replaceAll("^[0 ]*", "");
	if (this.integer.equals("")) { // also possible with "" but to dangerous
	    this.integer = "0";
	}
	// Here, this.integer has no leading 0's or blanks. 
	
	switch (expVal.signum()) {
	case -1:
	    throw new NumberFormatException
		("Could not represent " + this + " as an integer. ");
	case  0:
	    // nothing to do: representation is already without exponent. 
	    break;
	case  1:
	    this.integer += fill('0', expVal.intValue());
	    break;
	default:
	    throw new IllegalStateException
		("Found signum " + expVal.signum()
		 + " which may be only -1, 0 or 1. ");
	}

	this.fraction = "";
	this.exponent = "";
    }

    /* -------------------------------------------------------------------- *
     * get methods.                                                         *
     * -------------------------------------------------------------------- */

    public final boolean hasBlankFraction() {
	return !this.fraction.matches("^ *$");
    }

    public final boolean hasFraction() {
	return !this.fraction.matches("^0* *$");
    }

    public boolean hasInteger() {
	return !this.integer.matches("^ *0*$");
    }

    public final boolean hasExponent() {
	return !this.exponent.matches("^ *$");
    }

    public final String sign() {
	return this.sign;
    }

    public final String exponent() {
	return this.exponent;
    }

    public final String mantissa() {
	return this.integer +
	    (hasBlankFraction() ? POINT_S : noPointS) + 
	    this.fraction;
 
	//return this.mantissa;
    }

    public final String integer() {
	return this.integer;
    }

    public final String fraction() {
	return this.fraction;
    }

    /* -------------------------------------------------------------------- *
     * inverse create methods:                                              *
     * retrieve Strings, doubles, Doubles and BigDecimals.                  *
     * -------------------------------------------------------------------- */

    /**
     * Returns a <code>Double</code> represented.
     *
     * @return 
     *    a <code>Double</code> <code>d</code> satisfying 
     *    {@link #create(Number, boolean) 
     *            create(d, false).this2Double().compareTo(d) == 0}. 
     */
    public final Double this2Double() {
	return new Double(this.sign + mantissa() + getExpWithE());
    }

    /**
     * Returns a <code>double</code> represented.
     *
     * @return 
     *    a <code>double</code> <code>d</code> satisfying 
     *    {@link #create(double, boolean) create(d, false).this2double() == d}. 
     */
    public final double this2double() {
	return this2Double().doubleValue();
    }

    /**
     * Returns a <code>BigDecimal</code> represented.
     *
     * @return 
     *    a <code>BigDecimal</code> <code>d</code> satisfying 
     *    {@link #create(Number, boolean) 
     *            create(d, true).this2BigDecimal().compareTo(d) == 0}. 
     */
    public final BigDecimal this2BigDecimal() {
	//**** this does not work with trimmed mantissas
	return new BigDecimal(this.sign + mantissa() + getExpWithE());
    }

    private String getExpWithE() {
	return this.exponent.equals("") ? "" : EXP_CHAR + this.exponent;
    }

    /**
     * Returns a String representation of this real number. 
     * Formatting is determined by previous invocations of the methods 
     * {@link #trimInteger}, {@link #trimFraction} and {@link #trimExponent}. 
     *
     * @return 
     *    a <code>String</code> reflecting this representation 
     *    of a real number. 
     *    In front of the sign (which may be empty), 
     *    between the sign and the integer part, 
     *    between the fractional part 
     *    and the exponent or the end of the string 
     *    and also between the *****
     * @see #toStringDecomp
     */
    public final String toString() {
	return 
	    (this.sign   == SIGN_PLUS ? "" : this.sign) + 
	    mantissa() + 
	    (hasExponent() ? "e" : "") + exponent();
    }

    public final String toString(Desc desc) {

	trimInteger (desc.lenI, ' ', desc.strictInteger);
	trimFraction(desc.lenM - 
		     desc.lenI, ' ', desc.fractionCutter);
	trimExponent(desc.lenE, ' ', desc.strictExponent);

	return 
	    (this.sign   == SIGN_PLUS ? "" : this.sign) + 
	    mantissa() + 
	    (hasExponent() ? "E" : " ") + exponent();
    }

    /**
     * Returns a string showing the decomposition of the underlying real number 
     * into sign, mantissa and exponent. 
     *
     * @return a <code>String</code> value
     * @see #toString
     */
    public final String toStringDecomp() {
	return 
	    "sign:     " + this.sign + "\n" + 
	    "integer:  " + this.integer + "\n" + 
	    "fraction: " + this.fraction + "\n" + 
	    "exponent: " + this.exponent;
    }
}
