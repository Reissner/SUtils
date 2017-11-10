package eu.simuline.util.sgml;

/**
 * Rudimentary Handler for SAXParseExceptions: 
 * The idea is that for html-parsers one needs a notify 
 * rather than throwing an exception as it is appropriate for xml. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public interface ParseExceptionHandler {

    /* --------------------------------------------------------------------- *
     * inner classes                                                         *
     * --------------------------------------------------------------------- */

    /**
     * Trivial implementation for enclosing interface. 
     */
    class Impl implements ParseExceptionHandler {

	public void foundMultipleAttribute(String attrName,
					   Object oldAttrValue) {
	    // is empty. 
	}

	public void foundIllegalCharInTag(char chr) {
	    // is empty. 
	}

	public void foundCharAfterEndOfEndTag(char chr) {
	    // is empty. 
	}

	public void foundUnexpectedEndOfDocument() {
	    // is empty. 
	}
    } // class Impl 

    /* --------------------------------------------------------------------- *
     * methods                                                               *
     * --------------------------------------------------------------------- */

    /**
     * Notifies the occurence of a duplicate attribute declaration 
     * within a start tag. 
     *
     * @param attrName 
     *    a non-empty <code>String</code> 
     *    representing the name of the attribute. 
     * @param oldAttrValue 
     *    an <code>Object</code> which is either a <code>String</code> 
     *    representing the value of the attribute 
     *    or the object {@link AttributesImpl#NO_VALUE} 
     *    which signifies the absence of a value. 
     *    Here the old value (which is overwritten in the attribute list) 
     *    should be passed to the application. 
     *    ****** it is not clear to me 
     *    whether the ordering of the attribute list is significant. 
     *    The former occurence of the attribute is lost. **** is this true? 
     */
    void foundMultipleAttribute(String attrName, Object oldAttrValue);

    /**
     * Notifies that an illegal character was found in a tag *****. 
     * To be more precise: at the beginning of the tag. 
     * Note that this is ignored and the next one is read. 
     *
     * @param chr 
     *    the illegal <code>char</code> value. 
     */
    void foundIllegalCharInTag(char chr);

    /**
     * Notifies that a character was found after the "/" of an end tag. 
     * Note that this is ignored and the next one is read. 
     *
     * @param chr 
     *    the illegal <code>char</code> value. 
     */
    void foundCharAfterEndOfEndTag(char chr);

    // ****** would need an argument 
    void foundUnexpectedEndOfDocument();
} // ParseExceptionHandler
