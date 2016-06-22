
package eu.simuline.util;

import java.lang.ref.WeakReference;

/**
 * Collection of static methods related to strings. 
 * This class is required because class {@link String} is declared final. 
 */
public abstract class Strings {

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * An ever growing buffer of blanks used by {@link #getBlanks(int)}. 
     */
    private static WeakReference<StringBuilder> BLANKS = 
	new WeakReference<StringBuilder>(new StringBuilder());


    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    /**
     * Returns a string consisting of the given number of blanks. 
     */
    public static String getBlanks(int num) {
	StringBuilder blanks = BLANKS.get();
	if (blanks == null) {
	    blanks = new StringBuilder();
	    BLANKS = new WeakReference<StringBuilder>(blanks);
	}

	while (blanks.length() < num) {
	    blanks.append(' ');
	}
	assert blanks.length() >= num;

	return blanks.substring(0, num);
    }
}
