
package eu.simuline.util;


/**
 * Enumerates categories of data models 
 * and determines the current data model. 
 * This is needed for jne/jna-applications, 
 * not when used with pure jave. 
 */
public enum DataModel {
 
    BitsB("64"), 
    BitsA("32"), 
    Unknown("unknown");

    private final String code;

    DataModel(String code) {
	this.code = code;
    }

    public static DataModel getDataModel() {
	String code = System.getProperty("sun.arch.data.model");
	DataModel dataModel = null;
	for (DataModel cand : DataModel.values()) {
	    if (cand.code.equals(code)) {
		return cand;
	    } // if 
	} // for 

	throw new IllegalStateException
	    ("Unknown data model: " + code + ". ");
    }

}
