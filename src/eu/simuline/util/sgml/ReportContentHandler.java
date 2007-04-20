
package eu.simuline.util.sgml;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

/**
 * A <code>ContentHandler</code> which reports certain events 
 * to {@link System#out}
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
class ReportContentHandler implements ContentHandler {

    /** <!-- api-docs inherited from interface implemented.  -->*/ 
    public void setDocumentLocator(Locator locator) {
	// is empty. 
    }

    public void startDocument() throws SAXException {
	System.out.println("start of document");
    }

    public void endDocument() throws SAXException {
	System.out.println("end of document");
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
	System.out.println("T<" + qName + ">");
    }

    public void endElement(String namespaceURI,
			   String localName,
			   String qName)
	throws SAXException {
	System.out.println("T</" + qName + ">");
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
	System.out.println("P<" + target + ">");
    }

    public void skippedEntity(String name)
	throws SAXException {
	// is empty. 
    }

} // class ReportContentHandler 
