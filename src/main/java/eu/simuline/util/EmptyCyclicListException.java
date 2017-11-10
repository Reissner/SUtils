
package eu.simuline.util;

/**
 * Thrown by various methods in {@link eu.simuline.util.CyclicList}. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public class EmptyCyclicListException extends RuntimeException {
    private static final long serialVersionUID = -2479143000061671589L;
    public EmptyCyclicListException() {
	super();
    }

}
