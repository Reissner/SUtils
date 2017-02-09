

package eu.simuline.util.sgml;

import eu.simuline.util.ListMap;

import java.io.Reader;
import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A rudimentary <code>SGML</code> parser with something like a SAX-api. 
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
public class SGMLParser {

	private final static String QUOTE_DOT = "\". ";

    /* --------------------------------------------------------------------- *
     * inner classes                                                         *
     * --------------------------------------------------------------------- */

    /**
     * A <code>ContentHandler</code> which simply ignores all events.  
     * May be used for debugging. 
     */
    static class TrivialContentHandler implements ContentHandler {

	/** <!-- api-docs inherited from interface implemented.  -->*/ 
	public void setDocumentLocator(Locator locator) {
	    // is empty. 
	}

	public void startDocument() throws SAXException {
	    // is empty. 
	}

	public void endDocument() throws SAXException {
	    // is empty. 
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
	    // is empty. 
	}

	public void endElement(String namespaceURI,
			       String localName,
			       String qName)
	    throws SAXException {
	    // is empty. 
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
	    // is empty. 
	}

	public void skippedEntity(String name)
	    throws SAXException {
	    // is empty. 
	}
    } // class TrivialContentHandler 


    /**
     * An **** partial **** implementation 
     * of the SAX-interface <code>Attributes</code> 
     * which allows to set name-value-pairs by method {@link #addAttribute}. 
     */
    class AttributesWrapper {

	/* ----------------------------------------------------------------- *
	 * fields                                                            *
	 * ----------------------------------------------------------------- */

	/**
	 * See {@link AttributesImpl#name2value}. 
	 */
	private final ListMap<String,String> name2value;

	/* ----------------------------------------------------------------- *
	 * constructors                                                      *
	 * ----------------------------------------------------------------- */

	/**
	 * Creates a new empty <code>AttributesWrapper</code> 
	 * which represents an empty attribute list. 
	 */
	AttributesWrapper() {
	    this.name2value = new ListMap<String,String>();
	}

	/* ----------------------------------------------------------------- *
	 * methods                                                           *
	 * ----------------------------------------------------------------- */


	/**
	 * Adds an attribute with the given name and value. 
	 *
	 * @param attName 
	 *     the <code>String</code> representation 
	 *     of the name of an attribute. 
	 * @param attValue 
	 *     the value of an attribute as a <code>String</code>. 
	 *     If no value is provided, 
	 *     this is {@link AttributesImpl#NO_VALUE}. 
	 */
	void addAttribute(String attName, String attValue) {
	    String oldAttValue = this.name2value
		.put(attName, attValue);
	    if (oldAttValue != null) {
		// Here, the attribute has occured before. 
		SGMLParser.this.parseExceptionHandler
		    .foundMultipleAttribute(attName,
					    oldAttValue);
	    }
 	}

	Attributes getAttributes() {
	    return new AttributesImpl(this.name2value);
	}
    } // class AttributesWrapper 


    /**
     * Provides a single method which decides whether the given character 
     * passes a certain test. 
     */
    interface CharTester {

	/**
	 * Returns whether the given character <code>ch</code> 
	 * passes the test given by this <code>CharTester</code>. 
	 *
	 * @param chr 
	 *    an <code>int</code> value representing a character. 
	 * @return the <code>boolean</code> 
	 *    signifying whether the given character <code>ch</code> 
	 *    passes the test given by this <code>CharTester</code>. 
	 */
	boolean testChar(char chr);

    } // interface CharTester 

    /**
     * Tests for blank, <code>/</code>, <code>&gt;</code>. 
     */
    private static final CharTester TEST_BLANK_GT_SLASH = new CharTester() {
	public boolean testChar(char chr) {
	    return Character.isWhitespace(chr) 
		|| chr == '/' 
		|| chr == '>';
	}
    };

    /**
     * Tests for blank or <code>&gt;</code>. 
     */
    private static final CharTester TEST_BLANK_GT = new CharTester() {
	public boolean testChar(char chr) {
	    return Character.isWhitespace(chr) 
		|| chr == '>';
	}
    };

    /*
     * Tests for <code>/</code> or <code>&gt;</code>. 
     */
/*
    private static final CharTester TEST_GT_SLASH = new CharTester() {
	    public boolean testChar(char ch) {
		return ch == '/' 
		    || ch == '>';
	    }
	};
*/

    /**
     * Tests for <code>&lt;</code>. 
     */
    private static final CharTester TEST_LT = new CharTester() {
	    public boolean testChar(char chr) {
		return chr == '<';
	    }
	};

    /**
     * Tests for <code>&gt;</code>. 
     */
    private static final CharTester TEST_GT = new CharTester() {
	    public boolean testChar(char chr) {
		return chr == '>';
	    }
	};

    /**
     * Tests for <code>=</code> and for <code>&gt;</code>. 
     */
    private static final CharTester TEST_BLANK_EQUALS_GT = new CharTester() {
	    public boolean testChar(char chr) {
		return Character.isWhitespace(chr) 
		    || chr == '='
		    || chr == '>';
	    }
	};

    /**
     * Tests for whitespace. 
     */
    private static final CharTester TEST_NO_WHITESPACE = new CharTester() {
	    public boolean testChar(char chr) {
		return !Character.isWhitespace(chr);
	    }
	};

    /**
     * Tests for quote both for<code>'</code> and for <code>"</code>. 
     */
/*
    private static final CharTester TEST_QUOTE = new CharTester() {
	    public boolean testChar(char chr) {
		return chr == '\'' 
		    || chr == '"';
	    }
	};
*/

    /*
     * Tests for end of comment <code>--></code>. 
     * This tests for a sequence of characters 
     * and confirms after having read the last one. 
     */
    private static final CharTester TEST_END_OF_COMMENT = new CharTester() {

	    /**
	     * Contains the sequence <code>--></code> 
	     * representing the end of a comment. 
	     */
	    static final String END_OF_COMMENT = "-->";

	    /**
	     * Contains the index in {@link #END_OF_COMMENT} 
	     * which is to be compared next by {@link #testChar}. 
	     */
	    private int index = 0;

	    /**
	     * Returns whether the last characters tested 
	     * are <code>--></code>. 
	     *
	     * @param chr 
	     *    a <code>char</code>. 
	     * @return
	     *    whether the last characters tested 
	     *    including <code>char</code> are <code>--></code>. 
	     *    In particular, if less than three characters are read 
	     *    this is <code>false</code>. 
	     */
	    public boolean testChar(char chr) {		
		if (END_OF_COMMENT.charAt(index++) == chr) {
		    if (this.index == END_OF_COMMENT.length()-1) {
			this.index = 0;
			return true;
		    } else {
			return false;
		    }
		} else {
		    this.index = 0;
		    return false;
		}
	    }
	}; // TEST_END_OF_COMMENT 

    /**
     * A <code>CharTester</code> which allows to specify 
     * the character which passes the test. 
     */
    static class SpecCharTester implements CharTester {

	/**
	 * The character which passes the test {@link #testChar}. 
	 */
	private char chr;

	/**
	 * Sets {@link #chr} to the specified character value. 
	 *
	 * @param chr 
	 *    a <code>char</code> value. 
	 */
	void setChar(char chr) {
	    this.chr = chr;
	}

	/**
	 * Returns whether the given character coincides with {@link #chr}. 
	 *
	 * @param chr 
	 *    a <code>char</code> value. 
	 * @return 
	 *    whether <code>ch</code> coincides with {@link #chr}. 
	 */
	public boolean testChar(char chr) {
	    return chr == this.chr;
	}
    } // SpecCharTester 

    /**
     * Tests for a specified character. 
     * This is used for quotes which allow the cases 
     * <code>'</code> and <code>"</code>. 
     *
     * @see XMLsGMLspecifica#parseAttribute
     */
    private static final SpecCharTester TEST_SPEC = new SpecCharTester();

    /**
     * Class which buffers the read stream. 
     */
    static class Buffer {

	/* ----------------------------------------------------------------- *
	 * fields                                                            *
	 * ----------------------------------------------------------------- */

	/**
	 * The reader buffered. 
	 */
	private final Reader reader;

	/**
	 * The current buffer. 
	 * The current parts to be read start with 
	 * <code>bufferArray[{@link #start}]</code> and end with 
	 * <code>bufferArray[{@link #end}]</code>, exclusively. 
	 */
	private final char[] bufferArray;

	/**
	 * The first index in {@link #bufferArray} 
	 * read in from {@link #reader} but not returned 
	 * by {@link #readArray} or {@link #readChar}. 
	 */
	private int start;

	/**
	 * Set by {@link #readArray} and read by {@link #getStartAndMove}. 
	 * When invoking {@link #readArray} <code>newStart</code> 
	 * is set to {@link #start} and increased 
	 * by the number of read charactersincreases. 
	 * Then {@link #getStartAndMove} updates {@link #start} 
	 * according to <code>newStart</code>. 
	 */
	private int newStart;

	/**
	 * The first index in {@link #bufferArray} not read 
	 * from {@link #reader} 
	 * or <code>-1</code> if the end of the stream is reached. 
	 * This means that <code>bufferArray[end]</code> 
	 * either does not exist or at least is not significant. 
	 */
	private int end;

	/* ----------------------------------------------------------------- *
	 * constructors                                                      *
	 * ----------------------------------------------------------------- */

	/**
	 * Creates a new <code>Buffer</code> from the given reader 
	 * with the given size. 
	 *
	 * @param reader 
	 *    the <code>Reader</code> to be buffered. 
	 * @param length 
	 *    the length of the buffer. 
	 * @exception IOException 
	 *    if an error occurs 
	 */
	Buffer(Reader reader, int length) throws IOException {
	    this.reader = reader;
	    this.bufferArray = new char[length];
	    this.start = 0;
	    this.end = this.start;// signifies: reading necessary. 
	}

	/* ----------------------------------------------------------------- *
	 * methods                                                           *
	 * ----------------------------------------------------------------- */


	/**
	 * Returns whether this buffer is currently empty. 
	 * When this is the case and someone tries to read further characters 
	 * this will lead to a trial 
	 * to read further pieces from {@link #reader}. 
	 *
	 * @return a <code>boolean</code> value 
	 *    signifying whether this buffer is currently empty. 
	 */
	boolean isEmpty() {
	    return this.end == this.start;
	}

	/**
	 * Returns whether the end of the stream is reached. 
	 *
	 * @return 
	 *    a <code>boolean</code> specifying 
	 *    whether the end of the stream is reached. 
	 */
	boolean reachedEOS() {
	    return this.end == -1;
	}

	/**
	 * Reads a single <code>char</code> and returns it. 
	 *
	 * @return 
	 *    an <code>int</code> value 
	 *    which is either the next <code>char</code> read in 
	 *    or <code>-1</code> which signifies the end of the stream. 
	 * @exception IOException 
	 *    if an error occurs
	 */
	int readChar() throws IOException {
	    if (reachedEOS()) {
		return -1;
	    }
	    if (isEmpty()) {
		this.start = 0;
		this.end = this.reader.read(this.bufferArray);
		if (reachedEOS()) {
		    return -1;
		}
	    }
	    return this.bufferArray[this.start++];
	}

	/**
	 * Reads an array from {@link #reader}. 
	 * As a side effect, writes the field {@link #newStart}. 
	 * Also, if the portion of {@link #bufferArray} 
	 * to be read, i.e. between {@link #start} and {@link #end}, 
	 * is empty, a new portion is buffered. 
	 *
	 * @param charTester 
	 *    a <code>CharTester</code> which signifies 
	 *    when to end reading from the buffer. 
	 * @return 
	 *    an <code>int</code> signifying the number of <code>char</code>s 
	 *    read or <code>-1</code> which signifies the end of the stream. 
	 *    It is read to the next &lt; or, if there is none, 
	 *    to the end of the stream. 
	 *    Thus there is a difference between the return values 
	 *    <code>-1</code> and <code>0</code>. 
	 * @exception IOException 
	 *    if an error occurs
	 */
	int readArray(CharTester charTester) throws IOException {
	    if (reachedEOS()) {
		return -1;
	    }
	    if (isEmpty()) {
		this.start = 0;
		this.end = this.reader.read(this.bufferArray);
//System.out.println("read: "+this.end);
		if (reachedEOS()) {
		    return -1;
		}
	    }

	    for (int i = this.start; i < this.end; i++) {
		if (charTester.testChar(this.bufferArray[i])) {
		    // found match described by charTester 
		    this.newStart = i;
		    return this.newStart - this.start;
		}
	    }
            // Here, the test always failed. 
	    this.newStart = this.end;
	    return this.end - this.start;
	}

	/**
	 * Describe <code>readStringBuffer</code> method here.
	 *
	 * @param charTester 
	 *    a <code>CharTester</code> which determines 
	 *    the first character not read 
	 *    into the resulting <code>StringBuffer</code>. 
	 * @param elementName
	 *    a <code>String</code> which determines 
	 *    the element under consideration. 
	 *    This is only used for generating the message of a 
	 *    <code>SAXParseException</code>. 
	 *    <p>
	 *    Allowed values: {@link #START_TAG}, {@link #END_TAG}, 
	 *    {@link #PROC_INSTR}, 
	 *    {@link #ATTR_NAME}, {@link #WHITESP_IN_ATTR} 
	 *    and {@link #ATTR_VALUE}. ****** comment and <!element missing. 
	 * @return 
	 *    a <code>StringBuffer</code> containing characters 
	 *    starting with the current one until one 
	 *    <code>charTester</code> returns <code>true</code>. 
	 * @exception IOException 
	 *    if an io-error occurs
	 * @exception SAXParseException 
	 *    if the parser faces the end of the stream 
	 *    while scanning the current element. 
	 */
	StringBuffer readStringBuffer(CharTester charTester, 
				      String elementName) 
	    throws IOException, SAXParseException {

	    StringBuffer qName = new StringBuffer();
	    int numRead = 0;
	    do {
		numRead = readArray(charTester);
		if (numRead == -1) {
		    throw new SAXParseException
			("End of stream while scanning " 
			 + elementName + ". " 
			 + "Read so far: \"" 
			 + qName + QUOTE_DOT, null);
		}
		qName.append(getChars(),
			     getStartAndMove(),
			     numRead);
	    } while (isEmpty());

	    return qName;
	}

	/**
	 * Returns the buffer of <code>char</code>s. 
	 *
	 * @return 
	 *    the <code>char[]</code> {@link #bufferArray}. 
	 */
	char[] getChars() {
	    return this.bufferArray;
	}

	/**
	 * Moves {@link #newStart} to {@link #start} 
	 * and returns the old value of {@link #start}. 
	 *
	 * @return 
	 *    the old <code>int</code> value of {@link #start}. 
	 */
	int getStartAndMove() {
	    int ret = this.start;
	    this.start = this.newStart;
	    return ret;
	}

	/**
	 * Get method for {@link #start}. 
	 *
	 * @return {@link #start}
	 */
	int getStart() {
	    return this.start;
	}

	/**
	 * Get method for {@link #end}. 
	 *
	 * @return {@link #end}
	 */
	int getEnd() {
	    return this.end;
	}
    } // class Buffer

    /**
     * Provides a bunch of methods fpr parsing 
     * with implementations specific to xml and sgml. 
     */
    interface XMLsGMLspecifica {
	// **** SGMLParser.this.currChar must be the character 
	// after the attribute list. 
	/**
	 * Parses one attribute and adds it to the given attribute list. 
	 *
	 * @param attributes 
	 *    an <code>AttributesImpl</code> 
	 *    to which the attribute parsed is added. 
	 * @exception IOException 
	 *    if an io-error occurs
	 * @exception SAXException 
	 *    if a syntactical error occurs
	 */
	void parseAttribute(AttributesWrapper attributes) 
	    throws IOException, SAXException;

	/**
	 * Parses a comment or any declaration 
	 * starting with <code><!...</code> and notifying the handler. 
	 *
	 * @exception IOException 
	 *    if an io-error occurs
	 * @exception SAXException 
	 *    if a syntactical error occurs
	 */
	void parseCommentElemTypeDecl() throws IOException, SAXException;

	/**
	 * Parses a processing instruction or any declaration 
	 * starting with <code><?...</code> and notifying the handler. 
	 *
	 * @exception IOException 
	 *    if an io-error occurs
	 * @exception SAXException 
	 *    if a syntactical error occurs
	 */
	void parseExtProcessingInstruction() throws IOException, SAXException;

    } // interface XML_SGML_Specifica 

    /**
     * Contains the <code>HTML</code>-specific part of the parser. 
     */
    private final XMLsGMLspecifica HTML_ATTRIBUTE_PARSER = 
    new XMLsGMLspecifica() {

	public void parseAttribute(AttributesWrapper attributes) 
	    throws IOException, SAXException {
	    String attName;
	    String attValue;
	    StringBuffer qName;

	    // Parse attribute name 
	    qName = SGMLParser.this.buffer.
		readStringBuffer(TEST_BLANK_EQUALS_GT, ATTR_NAME);
	    qName.insert(0, (char) SGMLParser.this.currChar);
	    attName = qName.toString().toLowerCase();
//System.out.println("attName: |"+attName+"|");
	    
	    // Here, the attribute may have a value or not. 

	    // Skip whitespace either after having parsed the attribute 
	    // or between its name and its value. 
	    SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD
	    if (Character.isWhitespace((char) SGMLParser.this.currChar)) {
		qName = SGMLParser.this.buffer.
		    readStringBuffer(TEST_NO_WHITESPACE, WHITESP_IN_ATTR);
		SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD
	    }

	    // Here is the decision whether a value is provided or not. 
	    if (SGMLParser.this.currChar != '=') {
		// Here, no value may be given 
		attributes.addAttribute(attName, AttributesImpl.NO_VALUE);
//System.out.println("attName: |"+attName+"|");
//System.out.println("noValue@@"+(char)SGMLParser.this.currChar+"|");
		return;
	    }
	    // Here, clearly a value must follow 

	    // Skip whitespaces 
	    qName = SGMLParser.this.buffer.
		readStringBuffer(TEST_NO_WHITESPACE, WHITESP_IN_ATTR);
	    SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD


	    // Parse the attribute value. 
	    switch (SGMLParser.this.currChar) {
		case '\'':
		    // fall through 
		case '"':
		    // the attribute value is quoted. 
		    char quote = (char) SGMLParser.this.currChar;
		    TEST_SPEC.setChar(quote);
		    //SGMLParser.this.currChar = 
		    //	SGMLParser.this.buffer.readChar();

//System.out.println("quote@@"+SGMLParser.this.currChar);
		    qName = new StringBuffer();
		    while (true) {
			qName.append(SGMLParser.this.buffer.
				     readStringBuffer(TEST_SPEC, ATTR_VALUE));
			if (qName.length() != 0 
			    && qName.charAt(qName.length() - 1) == '\\') {
			    qName.setCharAt(qName.length() - 1, quote);
			} else {
			    // read the quote 
			    SGMLParser.this.currChar = //NOPMD
				SGMLParser.this.buffer.readChar();
			    break;
			}
		    }
		    break;
		default:
//System.out.println("no quote@@"+SGMLParser.this.currChar);
		    // the attribute value is not quoted. 
		    qName = SGMLParser.this.buffer.
			readStringBuffer(TEST_BLANK_GT, ATTR_VALUE);
		    qName.insert(0, (char) SGMLParser.this.currChar);
		    break;
	    }
	    // read the character after the attribute value 
	    SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD

	    attValue = qName.toString();
	    attributes.addAttribute(attName, attValue);
//System.out.println("attName: |"+attName+"|");
//System.out.println("attValue: |"+attValue+"|");
	}

	public void parseCommentElemTypeDecl() 
	    throws IOException, SAXException {
//System.out.println("comment?");

	    SGMLParser.this.currChar = //NOPMD
		SGMLParser.this.buffer.readChar();
	    if (SGMLParser.this.currChar != '-') {
		//int numRead = 
		SGMLParser.this.buffer.readArray(TEST_GT);
		SGMLParser.this.buffer.getStartAndMove();
		return;
	    }
	    // Here, object starts with "<!-....."

	    SGMLParser.this.currChar = //NOPMD
		SGMLParser.this.buffer.readChar();
	    if (SGMLParser.this.currChar != '-') {
		throw new SAXParseException
		    ("Comments must start with \"<!--\" but found " 
		     + "\"<!-" + (char) SGMLParser.this.currChar + QUOTE_DOT, 
		     null);
	    }
//System.out.println("comment!");

	    int numRead = 0;
	    do {
		numRead = SGMLParser.this.buffer
		    .readArray(TEST_END_OF_COMMENT);
		if (numRead == -1) {
		    StringBuffer qName = new StringBuffer();
		    qName.append(SGMLParser.this.buffer.getChars(),
				 SGMLParser.this.buffer.getStartAndMove(),
				 numRead);
		    throw new SAXParseException
			("End of stream while scanning comment. " 
			 + "Recently read: \"" + qName + QUOTE_DOT, 
			 null);
		}
		
		SGMLParser.this.buffer.getStartAndMove();
	    } while (SGMLParser.this.buffer.isEmpty());
/*
	    StringBuffer qName = new StringBuffer();
	    qName.append(SGMLParser.this.buffer.getChars(),
			 SGMLParser.this.buffer.getStartAndMove(),
			 numRead);

	    System.out.println("read so far: |"+qName+"|");
*/

	    SGMLParser.this.buffer.getStartAndMove();	    
	    // NO NOTIFY!!
	}

	public void parseExtProcessingInstruction() 
	    throws IOException, SAXException {
	    parseStartOrStartEndTag();
	}
    }; // htmlXML_SGML_Specifica

    /**
     * Contains the <code>XML</code>-specific part of the parser. 
     */
    private final XMLsGMLspecifica XML_ATTRIBUTE_PARSER = 
    new XMLsGMLspecifica() {

	public void parseAttribute(AttributesWrapper attributes) 
	    throws IOException, SAXException {
	    String attName;
	    String attValue;
	    StringBuffer qName;

	    // Parse attribute name 
	    qName = SGMLParser.this.buffer.
		readStringBuffer(TEST_BLANK_EQUALS_GT, ATTR_NAME);
	    qName.insert(0, (char) SGMLParser.this.currChar);
	    attName = qName.toString();
//System.out.println("attName: |"+attName+"|");
	    
	    // Here, the attribute may have a value or not. 

	    // Skip whitespace either after having parsed the attribute 
	    // or between its name and its value. 
	    SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD
	    if (Character.isWhitespace((char) SGMLParser.this.currChar)) {
		qName = SGMLParser.this.buffer.
		    readStringBuffer(TEST_NO_WHITESPACE, WHITESP_IN_ATTR);
		SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD
	    }

	    // Here is the decision whether a value is provided or not. 
	    if (SGMLParser.this.currChar != '=') {
		// Here, a value is missing. 
		throw new SAXParseException
		    ("Missing value for attribute \"" 
		     + attName + QUOTE_DOT, null);
	    }
	    // Here, clearly a value must follow ****


	    // Skip whitespaces 
	    qName = SGMLParser.this.buffer.
		readStringBuffer(TEST_NO_WHITESPACE, WHITESP_IN_ATTR);
	    SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD


	    // Parse the attribute value. 
	    switch (SGMLParser.this.currChar) {
		case '\'':
		    // fall through 
		case '"':
		    // the attribute value is quoted. 
		    char quote = (char) SGMLParser.this.currChar;
		    TEST_SPEC.setChar(quote);
		    //SGMLParser.this.currChar = 
		    //	SGMLParser.this.buffer.readChar();

//System.out.println("quote@@"+SGMLParser.this.currChar);
		    qName = new StringBuffer();
		    while (true) {
			qName.append(SGMLParser.this.buffer.
				     readStringBuffer(TEST_SPEC, ATTR_VALUE));
			if (qName.length() != 0 
			    && qName.charAt(qName.length() - 1) == '\\') {
			    qName.setCharAt(qName.length() - 1, quote);
			} else {
			    // read the quote 
			    SGMLParser.this.currChar = //NOPMD
				SGMLParser.this.buffer.readChar();
			    break;
			}
		    }
		    break;
		default:
		    throw new SAXParseException
			("Value of attribute \"" + attName + 
			 "\" is not quoted. ",
			 null);
	    }
	    // read the character after the attribute value 
	    SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD

	    attValue = qName.toString();
	    attributes.addAttribute(attName, attValue);
//System.out.println("attName: |"+attName+"|");
//System.out.println("attValue: |"+attValue+"|");
	}

	public void parseCommentElemTypeDecl() 
	    throws IOException, SAXException {
	    // ******** comments will not work that way!!!*****

	    SGMLParser.this.buffer.readStringBuffer(TEST_GT, PROC_INSTR);
	    //**** comment
	    // Here, also the empty processing instruction or comment 
	    // would be possible. 
	
	    //this.buffer.getStart();
//System.out.println("-qName: |"+qName+"|");
	    SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD
	    //assert this.currChar == '>';
	    SGMLParser.this.contentHandler.processingInstruction(null, null);
	}


	public void parseExtProcessingInstruction() 
	    throws IOException, SAXException {

	    //StringBuffer qName = 
	    SGMLParser.this.buffer.readStringBuffer(TEST_GT, PROC_INSTR);
	    // Here, also the empty processing instruction would be possible. 
	
	    //this.buffer.getStart();
//System.out.println("-qName: |"+qName+"|");
	    SGMLParser.this.currChar = SGMLParser.this.buffer.readChar();//NOPMD
	    //assert this.currChar == '>';
	    SGMLParser.this.contentHandler.processingInstruction(null, null);
	}

    }; // xmlXML_SGML_Specifica

    /* --------------------------------------------------------------------- *
     * class constants                                                       *
     * --------------------------------------------------------------------- */


    /**
     * The size of the buffer used internally. 
     * This must be at least <code>1</code>. 
     * I found no significant difference in speed when increasing this number. 
     * The buffer coming from a stream from a URL seems to hav maximal size 
     * of <code>1448</code> whereas for file streams there seems no bound. 
     * In the cases considered, the file is read in as a whole. 
     */
    private static final int BUFFER_SIZE = 999999;

    // for notification of a sax parse exception with Buffer.readStringBuffer. 
    /**
     * Short string representation of the object currently parsed. 
     * Contains the specific part of the message of the exception 
     * that may be thrown by {@link SGMLParser.Buffer#readStringBuffer}. 
     */
    private static final String START_TAG = "start tag";

    /**
     * Short string representation of the object currently parsed. 
     * Contains the specific part of the message of the exception 
     * that may be thrown by {@link SGMLParser.Buffer#readStringBuffer}. 
     */
    private static final String END_TAG = "end tag";

    /**
     * Short string representation of the object currently parsed. 
     * Contains the specific part of the message of the exception 
     * that may be thrown by {@link SGMLParser.Buffer#readStringBuffer}. 
     */
    private static final String PROC_INSTR = "processing instruction";

    /**
     * Short string representation of the object currently parsed. 
     * Contains the specific part of the message of the exception 
     * that may be thrown by {@link SGMLParser.Buffer#readStringBuffer}. 
     */
    private static final String ATTR_NAME = "attribute name";

    /**
     * Short string representation of the object currently parsed. 
     * Contains the specific part of the message of the exception 
     * that may be thrown by {@link SGMLParser.Buffer#readStringBuffer}. 
     */
    private static final String WHITESP_IN_ATTR = "whitespace in attribute";

    /**
     * Short string representation of the object currently parsed. 
     * Contains the specific part of the message of the exception 
     * that may be thrown by {@link SGMLParser.Buffer#readStringBuffer}. 
     */
    private static final String ATTR_VALUE = "attribute value";

    /* --------------------------------------------------------------------- *
     * fields                                                                *
     * --------------------------------------------------------------------- */

    /**
     * Contains class with methods specific for xml and sgml, respectively. 
     */
    private XMLsGMLspecifica xmlSgmlSpecifica = HTML_ATTRIBUTE_PARSER;

    /**
     * The current character or <code>-1</code> 
     * to signfy the end of the stream. 
     */
    private int currChar;

    /**
     * The <code>ContentHandler</code> registered. 
     */
    private ContentHandler contentHandler;

    /**
     * The <code>ParseExceptionHandler</code> registered. 
     */
    private ParseExceptionHandler parseExceptionHandler;

    /**
     * The buffer of the input stream. 
     */
    private Buffer buffer;

    /* --------------------------------------------------------------------- *
     * constructors                                                          *
     * --------------------------------------------------------------------- */

    /**
     * Creates a new <code>SGMLParser</code> 
     * with the default handlers for content and exceptions. 
     */
    public SGMLParser() {
	this.       contentHandler = new TrivialContentHandler();
	this.parseExceptionHandler = new ParseExceptionHandler.Impl();
    }

    /* --------------------------------------------------------------------- *
     * methods                                                               *
     * --------------------------------------------------------------------- */


    /**
     * Parses the <code>InputSource</code> given 
     * but delegates everything inside a tag or a processing instruction 
     * to {@link #parseTagOrPI}. 
     *
     * @param inSource 
     *    an <code>InputSource</code>. 
     * @exception IOException if an error occurs
     * @exception SAXException if an error occurs
     */
    void parse(InputSource inSource) throws IOException, SAXException {
	parse(inSource.getCharacterStream());
    }

    /**
     * Parses the given <code>InputStream</code>. 
     *
     * @param reader 
     *     an <code>Reader</code> sequentializing an SGML document. 
     * @exception IOException 
     *     if an error reading the stream occurs. 
     * @exception SAXException 
     *    if an error with the sgml-syntax occurs. 
     */
    public void parse(Reader reader) throws IOException, SAXException {

	this.buffer = new Buffer(reader, BUFFER_SIZE);
	int numRead = this.buffer.readArray(TEST_LT);
	// notify handler that first part of document was successfully read. 
	this.contentHandler.startDocument();
	while (numRead != -1) {
	    this.currChar = this.buffer.readChar();// the '<' char? 
	    if (this.currChar == '<') {
		// a tag or a PI. 
		numRead = parseTagOrPI();
	    } else {
		// either characters or ignoreableWhitespace
		numRead = parseText();
	    }
	}
	// Here, the document is finished. 
	this.contentHandler.endDocument();
    }

    /**
     * Parses everything outside a tag, a processing instruction, ... 
     * everything within brackets <code>&lt;</code> and <code>&gt;</code>. 
     * ***** Missing: distinction between notification 
     * of characters and whitespace. ****
     *
     * @exception IOException 
     *     if an error reading the stream occurs. 
     * @exception SAXException 
     *    if an error with the sgml-syntax occurs. 
     * @see #parseTagOrPI
     */
    private int parseText() throws IOException, SAXException {
	int numRead = this.buffer.readArray(TEST_LT);
	if (numRead != -1) {
/*
  System.out.println("text: |"+new String(buffer.getChars(),
  buffer.getStartAndMove(),
  numRead)+"|");
*/
	    this.contentHandler.characters(this.buffer.getChars(),
					   this.buffer.getStartAndMove(),
					   numRead);
	}
	
//buffer.getStartAndMove();
	return numRead;
    }

    /**
     * Parses an end-tag notifying the underlying handler 
     *
     * @exception IOException 
     *     if an error reading the stream occurs. 
     * @exception SAXException 
     *    if an error with the sgml-syntax occurs. 
     */
    void parseEndTag() throws IOException, SAXException {

	StringBuffer qName = this.buffer.readStringBuffer(TEST_GT, END_TAG);
	// Here, also the empty tag would be possible. 
	
	//this.buffer.getStart();
//System.out.println("end tag: |"+qName+"|");
	this.currChar = this.buffer.readChar();
	//assert this.currChar == '>';
	this.contentHandler.endElement(null,
				       null,
				       qName.toString());
	this.currChar = this.buffer.readChar();
    }
/*
  public void parseCommentElemTypeDecl() 
  throws IOException, SAXException {
  // ******** comments will not work that way!!!*****

  StringBuffer qName = this.buffer
  .readStringBuffer(TEST_GT, PROC_INSTR);//**** comment
  // Here, also the empty processing instruction or comment 
  // would be possible. 
	
  //this.buffer.getStart();
//System.out.println("-qName: |"+qName+"|");
this.currChar = this.buffer.readChar();
//assert this.currChar == '>';
this.handler.processingInstruction(null, null);
}
*/

    /**
     * Parses a start-tag or, for xml, an empty tag. 
     *
     * @exception IOException 
     *     if an error reading the stream occurs. 
     * @exception SAXException 
     *    if an error with the sgml-syntax occurs. 
     */
    void parseStartOrStartEndTag() throws IOException, SAXException {

	// ***** Better read the name of the tag and 
	// then single out problems with chars by a handler
	if (!Character.isLetter((char)this.currChar)) {
	    this.parseExceptionHandler
		.foundIllegalCharInTag((char)this.currChar);
	    // Ignore the previously read char. 
	    this.currChar = this.buffer.readChar();
	}


	StringBuffer qName = this.buffer
	    .readStringBuffer(TEST_BLANK_GT_SLASH, START_TAG);
	qName.insert(0, (char) this.currChar);
	// Here, also the empty tag would be possible. 
//System.out.println("start tag: |"+qName+"|");

	// Skip whitespaces 
	this.currChar = this.buffer.readChar();
	while (Character.isWhitespace((char) this.currChar)) {
	    this.buffer.
		readStringBuffer(TEST_NO_WHITESPACE, WHITESP_IN_ATTR);
	    this.currChar = this.buffer.readChar();
	}

	AttributesWrapper attributesWrapper = new AttributesWrapper();
	// Here, either /, > or an attribute occurs
//System.out.println("this.currChar: |"+(char)this.currChar+"|");
	while (this.currChar != '/' && this.currChar != '>') {
	    // parse the following attribute list
	    this.xmlSgmlSpecifica.parseAttribute(attributesWrapper);

	    // Skip whitespaces 
	    while (Character.isWhitespace((char) this.currChar)) {
		this.buffer
		    .readStringBuffer(TEST_NO_WHITESPACE, WHITESP_IN_ATTR);
		this.currChar = this.buffer.readChar();
	    }
	} // end parsing attribute list 
//System.out.println("-this.currChar: |"+(char)this.currChar+"|");


	Attributes attributes = attributesWrapper.getAttributes();
	switch (this.currChar) {
	    case '/':
		// start-end-tag called empty tag

		// skip illegal characters between "/" and ">" **** 
	skipped: while (true) {//NOPMD
		    this.currChar = this.buffer.readChar();
		    switch (this.currChar) {
			case '>':
			    break skipped;
			case -1:
			    this.parseExceptionHandler
				.foundUnexpectedEndOfDocument();
				break;
			default:
			    this.parseExceptionHandler
				.foundCharAfterEndOfEndTag
				((char) this.currChar);
			    break;
		    } // switch
		}
		
		this.contentHandler.startElement(null,
						 null,
						 qName.toString(),
						 attributes);
		this.contentHandler.endElement(null,
					       null,
					       qName.toString());
		break;
	    case '>':
		this.contentHandler.startElement(null,
						 null,
						 qName.toString(),
						 attributes);
		break;
	    default:
		throw new SAXParseException
		    ("Expected finishing tag \"" + qName 
		     + "\" with character '/' or '>' " 
		     + "but found '" + (char) this.currChar + "'. ", null);
	}
    }

    /**
     * Parses everything within a tag, a processing instruction, ... 
     * everything within brackets <code>&lt;</code> and <code>&gt;</code>. 
     *
     * @see #parseText
     */
    private int parseTagOrPI() throws IOException, SAXException {
//System.out.println("parseTagOrPI");
	
	this.currChar = this.buffer.readChar();
	//this.currChar = this.reader.read();
	switch (this.currChar) {
	    case '/':
		// parsing an end-tag 
		parseEndTag();
		//this.currChar = this.reader.read();
		break;
	    case '!':
		// parsing no tag at all: 
		// a processing instruction or a comment
		this.xmlSgmlSpecifica.parseCommentElemTypeDecl();
		//this.currChar = this.reader.read();
		break;
	    case '?':
		// parsing no tag at all: 
		// a processing instruction or a comment
		this.xmlSgmlSpecifica.parseExtProcessingInstruction();
		//this.currChar = this.reader.read();
		break;
	    default:
		// parsing a start-tag or an empty-element-tag 
		parseStartOrStartEndTag();
		break;
	} // end of switch ()
	//this.currChar = this.buffer.readChar();
	// Here, the buffer is ready 
	// to read the first character. after the generalized tag. 

//System.out.println("read last: |"+(char)this.currChar+"|");
//System.out.println("read last: |"+      this.currChar+"|");

	return 1;
    }

    /**
     * Sets {@link #contentHandler}. 
     *
     * @param contentHandler 
     *    a <code>ContentHandler</code>. 
     */
    public void setContentHandler(ContentHandler contentHandler) {
	if (isXMLParser()) {
	    this.contentHandler = contentHandler;
	} else {
	    this.contentHandler = new SGMLFilter(contentHandler);
	}
    }

    /**
     * Returns {@link #contentHandler}. 
     *
     * @return
     *    the <code>ContentHandler</code> {@link #contentHandler}. 
     */
    public ContentHandler getContentHandler() {
	if (isXMLParser()) {
	    return this.contentHandler;
	} else {
	    return ((SGMLFilter)this.contentHandler).getWrapped();
	}
    }

    /**
     * Sets {@link #parseExceptionHandler}. 
     *
     * @param peHandler 
     *    a <code>ParseExceptionHandler</code>. 
     */
    public void setExceptionHandler(ParseExceptionHandler peHandler) {
	this.parseExceptionHandler = peHandler;
    }

    /**
     * Returns {@link #parseExceptionHandler}. 
     *
     * @return
     *    the <code>ContentHandler</code> {@link #parseExceptionHandler}. 
     */
    public ParseExceptionHandler getExceptionHandler() {
	return this.parseExceptionHandler;
    }

    /**
     * Sets whether this parser is used as an xml-parser. 
     * If this is false, which is the default, 
     * it s an html-parser. 
     *
     * @param xml 
     *    a <code>boolean</code> value signifying 
     *    whether this parser will be used as an xml-parser in the sequel. 
     * @return 
     *    a <code>boolean</code> value signifying 
     *    whether before invoking this method 
     *    this parser was used as an xml-parser
     */
    public boolean parseXML(boolean xml) {
	boolean result = this.xmlSgmlSpecifica == XML_ATTRIBUTE_PARSER;
	this.xmlSgmlSpecifica = xml 
	    ? XML_ATTRIBUTE_PARSER 
	    : HTML_ATTRIBUTE_PARSER;
	return result;
    }

    public boolean isXMLParser() {
	return this.xmlSgmlSpecifica == XML_ATTRIBUTE_PARSER;
    }
}
