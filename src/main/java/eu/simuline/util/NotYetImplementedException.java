package eu.simuline.util;

/**
 * Thrown if a place in the code is reached, 
 * where implementation is not finished. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public class NotYetImplementedException extends RuntimeException {
    private static final long serialVersionUID = -2479143000061671589L;
    public NotYetImplementedException() {
	// is empty. 
    }
}
