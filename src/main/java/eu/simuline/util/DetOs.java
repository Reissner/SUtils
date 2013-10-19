
package eu.simuline.util;

/**
 * Enumerates the most important operating system 
 * and determines the current operating system. 
 * This is needed for jne/jna-applications, 
 * not when used with pure jave. 
 */
public enum DetOs {
 
    Win() {
	boolean isThis() {
	    return osString().indexOf("win") >= 0;
 	}
    }, 
    // includes (li)nux but neither mac nor solaris 
    Nix() {
	boolean isThis() {
	    String osString = osString();
	    return osString.indexOf("mac") >= 0 
		|| osString.indexOf("nux") >= 0 
		|| osString.indexOf("aix") >= 0;
 	}
    }, 
    Mac() {
	boolean isThis() {
	    return osString().indexOf("mac") >= 0;
 	}
    }, 
    Solaris() {
	boolean isThis() {
	    return osString().indexOf("sunos") >= 0;
 	}
    };

    abstract boolean isThis();

    private static String osString() {
	return  System.getProperty("os.name").toLowerCase();
    }

    public static DetOs getOpSys() {
	DetOs detOs = null;
	for (DetOs cand : DetOs.values()) {
	    assert cand != null;
	    if (cand.isThis()) {
		if (detOs != null) {
		    throw new IllegalStateException
			("Two possible os: " + detOs + " and " + cand + ". ");
		}
		detOs = cand;
	    } // if 
	} // for 

	if (detOs == null) {
	    throw new IllegalStateException
			("Unknown os: " + osString() + ". ");
	}

	assert detOs != null;
	return detOs;
    }

    public static void main(String[] args) {
	System.out.println(osString());
	System.out.println(System.getProperty("sun.arch.data.model"));
	System.out.println(System.getProperties());

// {java.runtime.name=OpenJDK Runtime Environment, 
// 	sun.boot.library.path=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/amd64, 
// java.vm.version=24.0-b50, 
// chooseClasspath=/home/ernst/Software/target/test-classes, 
//  java.vm.vendor=Oracle Corporation, 
//  java.vendor.url=http://java.oracle.com/, 
//  path.separator=:, 
//  java.vm.name=OpenJDK 64-Bit Server VM, 
//  file.encoding.pkg=sun.io, 
//  user.country=US, 
//  sun.java.launcher=SUN_STANDARD, 
//  sun.os.patch.level=unknown, 
//  java.vm.specification.name=Java Virtual Machine Specification, 
//  user.dir=/home/ernst/Software/src/main/java/eu/simuline/util, 
//  java.runtime.version=1.7.0_40-b31, 
//  java.awt.graphicsenv=sun.awt.X11GraphicsEnvironment, 
//  java.endorsed.dirs=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/endorsed, 
//  os.arch=amd64, 
//  java.io.tmpdir=/tmp, 
//  line.separator=
// , 
//  java.vm.specification.vendor=Oracle Corporation, 
//  os.name=Linux, 
//  sun.jnu.encoding=UTF-8, 
//  java.library.path=/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib, 
//  java.specification.name=Java Platform API Specification, 
//  java.class.version=51.0, 
//  sun.management.compiler=HotSpot 64-Bit Tiered Compilers, 
//  os.version=3.7.10-1.16-desktop, 
//  user.home=/home/ernst, 
//  user.timezone=, 
//  java.awt.printerjob=sun.print.PSPrinterJob, 
//  file.encoding=UTF-8, 
//  java.specification.version=1.7, 
//  java.class.path=/home/ernst/Software/target/test-classes:/home/ernst/Software/target/classes:/home/ernst/Software/jars/junitLatest.jar:/home/ernst/Software/jars/fastutilLatest.jar:/home/ernst/Software/jars/javaoctaveLatest.jar:/home/ernst/Software/jars/commons-loggingLatest.jar:/home/ernst/Software/jars/antlr-3.5-complete.jar:/home/ernst/Software/jars/jnaLatest.jar:/home/ernst/Software/jars/jnaPlatformLatest.jar, 
//  user.name=ernst, 
//  java.vm.specification.version=1.7, 
//  sun.java.command=eu.simuline.util.DetOs, 
//  java.home=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre, 
//  sun.arch.data.model=64, 
//  user.language=en, 
//  java.specification.vendor=Oracle Corporation, 
//  awt.toolkit=sun.awt.X11.XToolkit, 
//  java.vm.info=mixed mode, 
//  java.version=1.7.0_40, 
//  java.ext.dirs=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/ext:/usr/java/packages/lib/ext, 
//  sun.boot.class.path=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/resources.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/rt.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/sunrsasign.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/jsse.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/jce.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/charsets.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/netx.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/plugin.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/rhino.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/jfr.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/classes, 
//  java.vendor=Oracle Corporation, 
//  sourcepath=/home/ernst/Software/src/main/java:/home/ernst/Software/src/test/java, 
//  file.separator=/, 
//  java.vendor.url.bug=http://bugreport.sun.com/bugreport/, 
//  sun.io.unicode.encoding=UnicodeLittl{java.runtime.name=OpenJDK Runtime Environment, 
//  sun.boot.library.path=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/amd64, 
//  java.vm.version=24.0-b50, 
//  chooseClasspath=/home/ernst/Software/target/test-classes, 
//  java.vm.vendor=Oracle Corporation, 
//  java.vendor.url=http://java.oracle.com/, 
//  path.separator=:, 
//  java.vm.name=OpenJDK 64-Bit Server VM, 
//  file.encoding.pkg=sun.io, 
//  user.country=US, 
//  sun.java.launcher=SUN_STANDARD, 
//  sun.os.patch.level=unknown, 
//  java.vm.specification.name=Java Virtual Machine Specification, 
//  user.dir=/home/ernst/Software/src/main/java/eu/simuline/util, 
//  java.runtime.version=1.7.0_40-b31, 
//  java.awt.graphicsenv=sun.awt.X11GraphicsEnvironment, 
//  java.endorsed.dirs=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/endorsed, 
//  os.arch=amd64, 
//  java.io.tmpdir=/tmp, 
//  line.separator=
// , 
//  java.vm.specification.vendor=Oracle Corporation, 
//  os.name=Linux, 
//  sun.jnu.encoding=UTF-8, 
//  java.library.path=/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib, 
//  java.specification.name=Java Platform API Specification, 
//  java.class.version=51.0, 
//  sun.management.compiler=HotSpot 64-Bit Tiered Compilers, 
//  os.version=3.7.10-1.16-desktop, 
//  user.home=/home/ernst, 
//  user.timezone=, 
//  java.awt.printerjob=sun.print.PSPrinterJob, 
//  file.encoding=UTF-8, 
//  java.specification.version=1.7, 
//  java.class.path=/home/ernst/Software/target/test-classes:/home/ernst/Software/target/classes:/home/ernst/Software/jars/junitLatest.jar:/home/ernst/Software/jars/fastutilLatest.jar:/home/ernst/Software/jars/javaoctaveLatest.jar:/home/ernst/Software/jars/commons-loggingLatest.jar:/home/ernst/Software/jars/antlr-3.5-complete.jar:/home/ernst/Software/jars/jnaLatest.jar:/home/ernst/Software/jars/jnaPlatformLatest.jar, 
//  user.name=ernst, 
//  java.vm.specification.version=1.7, 
//  sun.java.command=eu.simuline.util.DetOs, 
//  java.home=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre, 
//  sun.arch.data.model=64, 
//  user.language=en, 
//  java.specification.vendor=Oracle Corporation, 
//  awt.toolkit=sun.awt.X11.XToolkit, 
//  java.vm.info=mixed mode, 
//  java.version=1.7.0_40, 
//  java.ext.dirs=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/ext:/usr/java/packages/lib/ext, 
//  sun.boot.class.path=/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/resources.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/rt.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/sunrsasign.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/jsse.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/jce.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/charsets.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/netx.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/plugin.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/rhino.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/lib/jfr.jar:/usr/lib64/jvm/java-1.7.0-openjdk-1.7.0/jre/classes, 
//  java.vendor=Oracle Corporation, 
//  sourcepath=/home/ernst/Software/src/main/java:/home/ernst/Software/src/test/java, 
//  file.separator=/, 
//  java.vendor.url.bug=http://bugreport.sun.com/bugreport/, 
//  sun.io.unicode.encoding=UnicodeLittle, 
//  sun.cpu.endian=little, 
//  sun.cpu.isalist=}e, 
//  sun.cpu.endian=little, 
//  sun.cpu.isalist=}
     }

}
