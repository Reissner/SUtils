
package eu.simuline.util.sgml;

import eu.simuline.testhelpers.Actions;
import eu.simuline.testhelpers.Accessor;
//import eu.simuline.testhelpers.Assert;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


import org.xml.sax.SAXParseException;

import java.io.Reader;
import java.io.StringReader;

import java.util.List;
import java.util.ArrayList;

@RunWith(Suite.class)
@SuiteClasses({SGMLParserTest.TestAll.class})
public class SGMLParserTest {

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */

    static SGMLParserTest TEST = new SGMLParserTest();

    public static class TestAll {
	@Test public void testBufferBasics() throws Exception {
	    SGMLParserTest.TEST.testBufferBasics();
	}
	@Test public void testParseTagOrPI() throws Exception {
	    SGMLParserTest.TEST.testParseTagOrPI();
	}
    } // class TestAll


    /* -------------------------------------------------------------------- *
     * methods for tests.                                                   *
     * -------------------------------------------------------------------- */

    public void testBufferBasics() throws Exception {
	Reader reader;
	SGMLParser.Buffer buffer;

	// test method readChar, isEmpty and reachedEOS 

	// testcase 1
	// 
	// empty reader
	// 
	reader = new StringReader("");
	buffer = new SGMLParser.Buffer(reader,1);
	assertTrue(buffer.isEmpty());
	assertEquals(0,buffer.getStart());
	assertEquals(0,buffer.getEnd());

	assertEquals(-1,buffer.readChar());
	assertEquals(0,buffer.getStart());
	assertEquals(-1,buffer.getEnd());

	assertEquals(-1,buffer.readChar());
	assertEquals(0,buffer.getStart());
	assertEquals(-1,buffer.getEnd());


	// testcase 2
	// 
	// non-empty reader
	// 
	reader = new StringReader("Hallo");
	buffer = new SGMLParser.Buffer(reader,1);
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(0,buffer.getEnd());

	assertEquals('H',(char)buffer.readChar());
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(1,buffer.getEnd());

	assertEquals('a',(char)buffer.readChar());
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(1,buffer.getEnd());

	assertEquals('l',(char)buffer.readChar());
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(1,buffer.getEnd());

	assertEquals('l',(char)buffer.readChar());
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(1,buffer.getEnd());

	assertEquals('o',(char)buffer.readChar());
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(1,buffer.getEnd());

	assertEquals(-1,buffer.readChar());
	//assertTrue(buffer.isEmpty());
	assertTrue(buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(-1,buffer.getEnd());


	// testcase 2
	// 
	// non-empty reader
	// 
	reader = new StringReader("Hallo");
	buffer = new SGMLParser.Buffer(reader,2);
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(0,buffer.getEnd());

	assertEquals('H',(char)buffer.readChar());
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(2,buffer.getEnd());

	assertEquals('a',(char)buffer.readChar());
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(2,buffer.getStart());
	assertEquals(2,buffer.getEnd());

	assertEquals('l',(char)buffer.readChar());
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(2,buffer.getEnd());

	assertEquals('l',(char)buffer.readChar());
	assertTrue( buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(2,buffer.getStart());
	assertEquals(2,buffer.getEnd());

	assertEquals('o',(char)buffer.readChar());
	assertTrue( buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(1,buffer.getEnd());

	assertEquals(-1,buffer.readChar());
	//assertTrue(buffer.isEmpty());
	assertTrue(buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(-1,buffer.getEnd());



	// test readArray 

	// testcase 1
	// 
	// empty reader
	// 
	reader = new StringReader("");
	buffer = new SGMLParser.Buffer(reader,4);
	assertTrue(buffer.isEmpty());
	assertEquals(0,buffer.getStart());
	assertEquals(0,buffer.getEnd());

	assertEquals(-1,buffer.readArray(null));
	assertEquals(0,buffer.getStart());
	assertEquals(-1,buffer.getEnd());

	assertEquals(-1,buffer.readArray(null));
	assertEquals(0,buffer.getStart());
	assertEquals(-1,buffer.getEnd());



	SGMLParser.CharTester TEST_LT = (SGMLParser.CharTester)Accessor
	    .getField(SGMLParser.class,"TEST_LT");

	// testcase 2
	// 
	// non-empty reader without '<'
	// 
	reader = new StringReader("Hallo");
	buffer = new SGMLParser.Buffer(reader,3);
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(0,buffer.getEnd());

	assertEquals(3,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(3,buffer.getEnd());
	assertEquals(0,buffer.getStartAndMove());
	assertEquals(3,buffer.getStartAndMove());
	assertEquals(3,buffer.getStartAndMove());

	assertEquals(2,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(2,buffer.getEnd());
	assertEquals(0,buffer.getStartAndMove());
	assertEquals(2,buffer.getStartAndMove());
	assertEquals(2,buffer.getStartAndMove());

	assertEquals(-1,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue( buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(-1,buffer.getEnd());
	assertEquals(0,buffer.getStartAndMove());
	assertEquals(2,buffer.getStartAndMove());
	assertEquals(2,buffer.getStartAndMove());



	// testcase 3
	// 
	// non-empty reader with '<' as first symbol in read portion 
	// 
	reader = new StringReader("Hal<lo");
	buffer = new SGMLParser.Buffer(reader,3);
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(0,buffer.getEnd());

	assertEquals(3,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(3,buffer.getEnd());
	assertEquals(0,buffer.getStartAndMove());
	assertEquals(3,buffer.getStartAndMove());
	assertEquals(3,buffer.getStartAndMove());

	assertEquals(0,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(3,buffer.getEnd());
	assertEquals(0,buffer.getStartAndMove());
	assertEquals(0,buffer.getStartAndMove());

	assertEquals('<',(char)buffer.readChar());
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(3,buffer.getEnd());

	assertEquals(2,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(1,buffer.getStart());
	assertEquals(3,buffer.getEnd());
	assertEquals(1,buffer.getStartAndMove());
	assertEquals(3,buffer.getStartAndMove());
	assertEquals(3,buffer.getStartAndMove());


	assertEquals(-1,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue( buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(-1,buffer.getEnd());




	// testcase 4
	// 
	// non-empty reader with '<' as subsequent symbol in read portion 
	// 
	reader = new StringReader("Ha<llo");
	buffer = new SGMLParser.Buffer(reader,3);
	assertTrue(buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(0,buffer.getEnd());

	assertEquals(2,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(3,buffer.getEnd());
	assertEquals(0,buffer.getStartAndMove());
	assertEquals(2,buffer.getStartAndMove());
	assertEquals(2,buffer.getStartAndMove());

	assertEquals(0,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(2,buffer.getStart());
	assertEquals(3,buffer.getEnd());
	assertEquals(2,buffer.getStartAndMove());
	assertEquals(2,buffer.getStartAndMove());

	assertEquals('<',(char)buffer.readChar());
	assertTrue( buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(3,buffer.getStart());
	assertEquals(3,buffer.getEnd());


	assertEquals(3,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue(!buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(3,buffer.getEnd());
	assertEquals(0,buffer.getStartAndMove());
	assertEquals(3,buffer.getStartAndMove());
	assertEquals(3,buffer.getStartAndMove());


	assertEquals(-1,buffer.readArray(TEST_LT));
	assertTrue(!buffer.isEmpty());
	assertTrue( buffer.reachedEOS());
	assertEquals(0,buffer.getStart());
	assertEquals(-1,buffer.getEnd());

    } //  testBufferBasics

    public void testParseTagOrPI() throws Exception {
	Reader reader;
	SGMLParser parser;
	SavingHandler eventsSaver;
	List<String> eventsCmp;


	// testcase 0
	// 
	// ProcessingInstruction
	// 
	reader = new StringReader("<?ProcessingInstruction>");
	parser = new SGMLParser();
	parser.parseXML(true);
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	eventsCmp.add("PI<!null, null>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());


	// testcase 1
	// 
	// Comment 
	// 
	reader = new StringReader("<!ProcessingInstruction>");
	parser = new SGMLParser();
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	try {
	    parser.parse(reader);
	} catch (SAXParseException e) {
	    assertEquals("Comments must start with \"<!--\". ",
			 e.getMessage());
	} // end of try-catch


	// testcase 1a
	// 
	// Comment 
	// 
	reader = new StringReader("<!--comment-->");
	parser = new SGMLParser();
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	//eventsCmp.add("PI<!null, null>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());


	// testcase 1b
	// 
	// Comment 
	// 
	reader = new StringReader
	    ("<!-- f-web file; generated by MAKEFWEB  "//);
	     + "V-1.07  (29-JUL-2003) on Fri, Mar 5 2004, at 4:01:15 -->");
	parser = new SGMLParser();
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	//eventsCmp.add("PI<!null, null>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());


	// testcase 2
	// 
	// start tag without attributes. 
	// 
	reader = new StringReader("<StartTag>");
	parser = new SGMLParser();
	parser.parseXML(true);
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	eventsCmp.add("TS<StartTag>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());



	// testcase 3
	// 
	// start tag with attributes. 
	// 
	reader = new StringReader("<StartTag att=\"blabla\">");
	parser = new SGMLParser();
	parser.parseXML(true);
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	eventsCmp.add("TS<StartTag>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());


	// testcase 3b
	// 
	// start tag with attributes. 
	// more blanks
	// 
	reader = new StringReader("<StartTag   att=\"blabla\">");
	parser = new SGMLParser();
	parser.parseXML(true);
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	eventsCmp.add("TS<StartTag>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());


	// testcase 4
	// 
	// end tag
	// 
	reader = new StringReader("</EndTag>");
	parser = new SGMLParser();
	parser.parseXML(true);
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	eventsCmp.add("TE</EndTag>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());

	// testcase 4a
	// 
	// empty end tag: should work 
	// although not allowed in xml: job of a filter
	// 
	reader = new StringReader("</>");
	parser = new SGMLParser();
	parser.parseXML(true);
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	eventsCmp.add("TE</>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());


	// testcase 4b
	// 
	// incomplete end tag 
	// 
	reader = new StringReader("</HalfOfAnEndTa");
	parser = new SGMLParser();
	parser.parseXML(true);
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	try {
	    parser.parse(reader);
	} catch (SAXParseException e) {
	    assertEquals("End of stream while scanning end tag. " 
			 + "Read so far: \"" 
			 + "HalfOfAnEndTa" + "\". ",
			 e.getMessage());
	}


	// testcase 5
	// 
	// combined start-end tag without attributes. 
	// 
	reader = new StringReader("<StartEndTag/>");
	parser = new SGMLParser();
	parser.parseXML(true);
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	eventsCmp.add("TS<StartEndTag>");
	eventsCmp.add("TE</StartEndTag>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());


	// testcase 6
	// 
	// combined start-end tag with attributes. 
	// 
	reader = new StringReader("<StartEndTag att=\"blabla\"/>@");
	parser = new SGMLParser();
	parser.parseXML(true);
	eventsSaver = new SavingHandler(true);
	parser.setContentHandler(eventsSaver);
	parser.parse(reader);
	eventsCmp = new ArrayList<String>();
	eventsCmp.add(SavingHandler.START_OF_DOCUMENT);
	eventsCmp.add("TS<StartEndTag>");
	eventsCmp.add("TE</StartEndTag>");
	eventsCmp.add(SavingHandler.  END_OF_DOCUMENT);
	assertEquals(eventsCmp,eventsSaver.getEvents());

    } // testParseTagOrPI

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String args[]) {
	Actions.runFromMain();
     }

}
