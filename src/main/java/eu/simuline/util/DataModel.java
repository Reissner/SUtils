
package eu.simuline.util;


/**
 * Enumerates categories of data models 
 * and determines the current data model. 
 * This is needed for jne/jna-applications, 
 * not when used with pure jave. 
 */
public enum DataModel {
 
    /*----------------------------------------------------------------------*
     * constants                                                            *
     *----------------------------------------------------------------------*/

    /**
     * Represents a data model known to be based on 64-bit entities. 
     */
    Bits64("64"),

    /**
     * Represents a data model known to be based on 32-bit entities. 
     */
    Bits32("32"),

    /**
     * Represents a data model with unknown number of bits or data model 
     * representable neither by {@link #Bits64} nor by {@link #Bits32}. 
     */
    Unknown("unknown") {
	public boolean isKnown() {
	    return false;
	}
    };

    /*----------------------------------------------------------------------*
     * fields                                                               *
     *----------------------------------------------------------------------*/

    private final String code;

    /*----------------------------------------------------------------------*
     * constructors                                                         *
     *----------------------------------------------------------------------*/

    DataModel(String code) {
	this.code = code;
    }

    /*----------------------------------------------------------------------*
     * methods                                                              *
     *----------------------------------------------------------------------*/

    public static DataModel getDataModel(boolean retNull) {
	String code = System.getProperty("sun.arch.data.model");
	DataModel dataModel = null;
	for (DataModel cand : DataModel.values()) {
	    if (cand.code.equals(code)) {
		return cand;
	    } // if 
	} // for 
	if (retNull) {
	    return null;
	}
	throw new IllegalStateException
	    ("Unknown data model: " + code + ". ");
    }

    public String getCode() {
	return this.code;
    }

    public boolean isKnown() {
	return true;
    }
}
