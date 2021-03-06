
package eu.simuline.util;

import java.lang.ref.WeakReference;

/**
 * Collection of static methods related to strings. 
 * This class is required because class {@link String} is declared final. 
 */
public final class Strings {

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * An ever growing buffer of blanks used by {@link #getBlanks(int)}. 
     */
    private static WeakReference<StringBuilder> bLANKS = 
	new WeakReference<StringBuilder>(new StringBuilder());


    /* -------------------------------------------------------------------- *
     * Constructors.                                                        *
     * -------------------------------------------------------------------- */

    /**
     * Prevents <code>Strings</code> from being instantiated. 
     */
    private Strings() {}
    
    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

    /**
     * Returns a string consisting of the given number of blanks. 
     */
    public static String getBlanks(int num) {
	StringBuilder blanks = bLANKS.get();
	if (blanks == null) {
	    blanks = new StringBuilder();
	    bLANKS = new WeakReference<StringBuilder>(blanks);
	}

	while (blanks.length() < num) {
	    blanks.append(' ');
	}
	assert blanks.length() >= num;

	return blanks.substring(0, num);
    }
}
