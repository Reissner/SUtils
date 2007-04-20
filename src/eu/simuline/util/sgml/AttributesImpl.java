package eu.simuline.util.sgml;

import eu.simuline.util.ListMap;

import org.xml.sax.Attributes;

/**
 * An **** partial **** implementation 
 * of the SAX-interface <code>Attributes</code> 
 * which allows attributes without values using {@link #NO_VALUE}. 
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
public class AttributesImpl implements Attributes {

    /* ----------------------------------------------------------------- *
     * class constants                                                   *
     * ----------------------------------------------------------------- */

    /**
     * Used as a value in {@link #name2value} 
     * to signify that the corresponding attribute has no value. 
     * This is much better than simply unsing <code>null</code>. 
     * The latter would not allow to rule out a multiple attribute 
     * without a value. 
     */
    public static final String NO_VALUE = new String();// NOPMD

    /* ----------------------------------------------------------------- *
     * fields                                                            *
     * ----------------------------------------------------------------- */

    /**
     * Maps the name of an attribute to its value. 
     * The name must be a string. 
     * If there is a value (which is mandatory in xml) 
     * the value is also a string value. 
     * Otherwise it is {@link #NO_VALUE}. 
     */
    private final ListMap<String,String> name2value;

    /* ----------------------------------------------------------------- *
     * constructors                                                      *
     * ----------------------------------------------------------------- */

    /**
     * Creates a new empty <code>AttributesImpl</code> 
     * which represents the given attribute list. 
     *
     * @param name2value 
     *    a <code>ListMap</code> representing an attribute list, 
     *    as specified for {@link #name2value}. 
     */
    AttributesImpl(ListMap<String,String> name2value) {
	this.name2value = name2value;
    }

    /* ----------------------------------------------------------------- *
     * methods                                                           *
     * ----------------------------------------------------------------- */
    /**
     * Converts {@link #NO_VALUE} to <code>null</code> 
     * simply casting other arguments to type <code>String</code>. 
     *
     * @param valueOrNot 
     *    a <code>String</code> or the object {@link #NO_VALUE}. 
     * @return 
     *    a <code>String</code> which is 
     *    <ul>
     *    <li>
     *    <code>null</code> for <code>valueOrNot == NO_VALUE</code>. 
     *    <li>
     *    <code>valueOrNot</code> itself casted to a string otherwise. 
     *    </ul>
     */
    private static String noValueToNull(Object valueOrNot) {
	return valueOrNot == NO_VALUE ? null : (String) valueOrNot;// NOPMD
    }

    /* ----------------------------------------------------------------- *
     * methods implementing Attributes                                   *
     * ----------------------------------------------------------------- */

    // apidoc provided by javadoc. 
    public int getLength() {
	return this.name2value.size();
    }
    public String getURI(int index) {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public String getLocalName(int index) {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public String getQName(int index) {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public String getType(int index) {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public String getValue(int index) {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public int getIndex(String uri,
			String localPart) {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public int getIndex(String qName)  {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public String getType(String uri,
			  String localName) {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public String getType(String qName) {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public String getValue(String uri,
			   String localName) {
	throw new eu.simuline.util.NotYetImplementedException();
    }
    public String getValue(String qName) {
	return noValueToNull(this.name2value.get(qName));
    }

    public AttributesImpl toLowerCase() {
	throw new eu.simuline.util.NotYetImplementedException();
    }

    public String toString() {
	return this.name2value.toString();
    }
} // class AttributesImpl 
