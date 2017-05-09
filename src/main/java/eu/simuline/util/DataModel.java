
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

    /**
     * Returns the current data model. 
     * If there is no valid data model, (not even {@link #Unknown}), 
     * then <code>null</code> is returned if <code>retNull</code> is set. 
     * Otherwise an exception is thrown. 
     *
     * @param retNull
     *    whether <code>null</code> is returned if the data model is invalid. 
     * @throws IllegalStateException
     *    if the data model is unknown and <code>retNull</code> is not set. 
     */
    public static DataModel getDataModel(boolean retNull) {
	String code = System.getProperty("sun.arch.data.model");
	//DataModel dataModel = null;
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

    /**
     * Returns true except for {@link #Unknown}. 
     */
    public boolean isKnown() {
	return true;
    }
}
