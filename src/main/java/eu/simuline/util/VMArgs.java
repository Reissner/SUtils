package eu.simuline.util;

public final class VMArgs {
    /**
     * Returns whether assertions are enabled through -ea-switch. 
     */
    public static boolean isAssertionSet() {
//import java.lang.management.ManagementFactory;
//import java.lang.management.RuntimeMXBean;
// RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
// List<String> jvmArgs = runtimeMXBean.getInputArguments();
// for (String arg : jvmArgs) {
//     System.out.println(arg);
// }

	// possible "accidental assignment" warning 
	boolean assertOn = false;
	// *assigns* true iff assertions are on; else remains false. 
	// if assertions are on, the expression evaluates to tru 
	assert assertOn = true; 
	return assertOn;

	// ArithContext.class.desiredAssertionStatus();
	// ClassLoader sysClsLdr = ClassLoader.getSystemClassLoader();
	// sysClsLdr.clearAssertionStatus();
	// sysClsLdr.setDefaultAssertionStatus(true);
 	// sysClsLdr.setClassAssertionStatus("eu.simuline.octave.ArithContext", 
	// 				  true);
	// sysClsLdr.setPackageAssertionStatus("eu.simuline.octave", true);

	// try {
	//     assert false;
	//     // Here, assertions are off 
	//     return false;
	// } catch (AssertionError e) {
	//     // Here, assertions are on 
	//     return true;
	// }
    }

    public static void main(String[] args) {
	System.out.println("sun.java.command: "+System.getProperty("sun.java.command"));
	System.out.println("isAssertionSet():"+VMArgs.isAssertionSet());
	
    }


}
