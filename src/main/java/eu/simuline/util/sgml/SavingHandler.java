
package eu.simuline.util.sgml;

import java.util.List;
import java.util.ArrayList;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

/**
 * Saves all events in a list to be returned by {@link #getEvents}. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public final class SavingHandler 
    implements ContentHandler, ParseExceptionHandler {

    /* --------------------------------------------------------------------- *
     * class constants                                                       *
     * --------------------------------------------------------------------- */

    public static final String START_OF_DOCUMENT = "start of document";
    public static final String   END_OF_DOCUMENT =   "end of document";

    /* --------------------------------------------------------------------- *
     * fields                                                                *
     * --------------------------------------------------------------------- */

    /**
     * The sequence of events taken place so far. 
     */
    private List<String> events;

    /**
     * Whether to save things like processing instructions as well. 
     */
    private boolean strict;

    /* --------------------------------------------------------------------- *
     * constructors                                                          *
     * --------------------------------------------------------------------- */

    /**
     * Creates a new <code>SavingHandler</code> instance.
     *
     * @param strict 
     *    a <code>boolean</code> value signifying 
     *    whether to save things like processing instructions as well. 
     */
    public SavingHandler(boolean strict) {
	this.events = new ArrayList<String>();
	this.strict = strict;
    }

    /**
     * Creates a new <code>SavingHandler</code>. 
     */
    public SavingHandler() {
	this(true);
    }


    /* --------------------------------------------------------------------- *
     * methods                                                               *
     * --------------------------------------------------------------------- */

    /**
     * Returns the sequence of events taken place so far. 
     *
     * @return 
     *    {@link #events}. 
     */
    public List<String> getEvents() {
	return this.events;
    }

    /* --------------------------------------------------------------------- *
     * methods implementing ContentHandler                                   *
     * --------------------------------------------------------------------- */

    public void setDocumentLocator(Locator locator) {
	// is empty. 
    }

    public void startDocument() throws SAXException {
	this.events.add(START_OF_DOCUMENT);
    }

    public void endDocument() throws SAXException {
	this.events.add(END_OF_DOCUMENT);
    }

    public void startPrefixMapping(String prefix,
				   String uri)
	throws SAXException {
	// is empty. 
    }

    public void endPrefixMapping(String prefix)
	throws SAXException {
	// is empty. 
    }

    public void startElement(String namespaceURI,
			     String localName,
			     String qName,
			     Attributes atts)
	throws SAXException {
	this.events.add("TS<" + qName + ">");
    }

    public void endElement(String namespaceURI,
			   String localName,
			   String qName)
	throws SAXException {
	this.events.add("TE</" + qName + ">");
    }

    public void characters(char[] chr,
			   int start,
			   int length)
	throws SAXException {
	// is empty. 
    }

    public void ignorableWhitespace(char[] chr,
				    int start,
				    int length)
	throws SAXException {
	// is empty. 
    }

    public void processingInstruction(String target,
				      String data)
	throws SAXException {
	if (this.strict) {
	    this.events.add("PI<!" + target + ", " + data + ">");
	}
	//System.out.println("P<" + target + ">");
    }

    public void skippedEntity(String name)
	throws SAXException {
	// is empty. 
    }

    /* --------------------------------------------------------------------- *
     * methods implementing ParseExceptionHandler                            *
     * --------------------------------------------------------------------- */

    public void foundMultipleAttribute(String attrName,
				       Object oldAttrValue) {
	StringBuilder buf = new StringBuilder();
	buf.append("Found second value for attribute \"");
	buf.append(attrName);
	buf.append("\"; overwritten ");
	if (oldAttrValue == AttributesImpl.NO_VALUE) {
	    buf.append("no value. ");
	} else {
	    buf.append("old value \"");
	    buf.append(oldAttrValue);
	    buf.append('\"');
	}

	this.events.add(buf.toString());
    }


    public void foundIllegalCharInTag(char chr) {
	this.events.add("exc: ill letter in tag: " + chr);
    }

    public void foundCharAfterEndOfEndTag(char chr) {
	this.events.add("exc: etter after eo EndTag: " + chr);
    }

    public void foundUnexpectedEndOfDocument() {
	// is empty. 
    }
} // class SavingHandler 

