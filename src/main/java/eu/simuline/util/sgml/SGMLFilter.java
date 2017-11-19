package eu.simuline.util.sgml;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

/**
 * A ContentHandler wrapping another ContentHandler 
 * which filters information 
 * in a way that is useful/necessary for html-parsers: 
 * turn tags into lower case. 
 *
 *
 * Created: Mon Jul  4 19:42:05 2005
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public final class SGMLFilter implements ContentHandler {

    /* --------------------------------------------------------------------- *
     * attributes                                                            *
     * --------------------------------------------------------------------- */

    private final ContentHandler handler;

    /* --------------------------------------------------------------------- *
     * constructors                                                          *
     * --------------------------------------------------------------------- */

    SGMLFilter(ContentHandler handler) {
	this.handler = handler;
    }

    /* --------------------------------------------------------------------- *
     * methods                                                               *
     * --------------------------------------------------------------------- */


    /** <!-- api-docs inherited from interface implemented.  -->*/ 
    public void setDocumentLocator(Locator locator) {
	this.handler.setDocumentLocator(locator);
    }

    public void startDocument() throws SAXException {
	this.handler.startDocument();
    }

    public void endDocument() throws SAXException {
	this.handler.endDocument();
    }

    public void startPrefixMapping(String prefix,
				   String uri)
	throws SAXException {
	this.handler.startPrefixMapping(prefix, uri);
    }

    public void endPrefixMapping(String prefix)
	throws SAXException {
	this.handler.endPrefixMapping(prefix);
    }

    private static String toLowerCase(String str) {
	if (str == null) {
	    return null;
	}
	return str.toLowerCase(); // NOPMD
    }

    public void startElement(String namespaceURI,
			     String localName,
			     String qName,
			     Attributes atts)
	throws SAXException {
	this.handler.startElement(namespaceURI,
				  toLowerCase(localName),
				  toLowerCase(qName),
				  atts);
				  //((AttributesImpl) atts).toLowerCase());
    }

    public void endElement(String namespaceURI,
			   String localName,
			   String qName)
	throws SAXException {
	this.handler.endElement(namespaceURI,
				toLowerCase(localName),
				toLowerCase(qName));
    }

    public void characters(char[] chr,
			   int start,
			   int length)
	throws SAXException {
	this.handler.characters(chr, start, length);
    }

    public void ignorableWhitespace(char[] chr,
				    int start,
				    int length)
	throws SAXException {
	this.handler.ignorableWhitespace(chr, start, length);
    }

    public void processingInstruction(String target,
				      String data)
	throws SAXException {
	this.handler.processingInstruction(target, data);
    }

    public void skippedEntity(String name)
	throws SAXException {
	this.handler.skippedEntity(name);
    }

    public ContentHandler getWrapped() {
	return this.handler;
    }
}
