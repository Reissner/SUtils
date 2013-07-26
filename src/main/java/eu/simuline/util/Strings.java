

package eu.simuline.util;

public abstract class Strings {

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * An ever growing buffer of blanks used by {@link #getBlanks(int)}. 
     */
    private final static StringBuilder BLANKS = new StringBuilder();


    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    /**
     * Returns a string consisting of the given number of blanks. 
     */
    public static String getBlanks(int num) {
	while (BLANKS.length() < num) {
	    BLANKS.append(' ');
	}
	assert BLANKS.length() >= num;

	return BLANKS.substring(0,num);
    }
}
