/*
  Copyright (C) Simuline Inc, Ernst Rei3ner

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the 
  Free Software Foundation, Inc., 
  51 Franklin Street, Fifth Floor, 
  Boston, MA  02110-1301, USA.
*/

package eu.simuline.util;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FilterInputStream;
import java.io.IOException;

import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

/**
 * Represents a path to find class and source files on. 
 *
 * Created: Wed Jun 14 23:12:06 2006
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public final class JavaPath {

    /* -------------------------------------------------------------------- *
     * constants.                                                           *
     * -------------------------------------------------------------------- */

    /**
     * The entry of property <code>file.separator</code> as a char. 
     */
    private static final char FILE_SEP = 
			System.getProperty("file.separator").charAt(0);

    /**
     * The entry of property <code>path.separator</code>. 
     */
    private static final String PATH_SEP = 
			System.getProperty("path.separator");

    /**
     * Java's class separator <code>.</code> 
     * separating classes from their packages 
     * and also packages from their subpackages. 
     */
    private static final char CLASS_SEP = '.';

    /**
     * Java's class separator <code>$</code> 
     * separating inner classes from their enclosing classes. 
     */
    private static final String INNER_CLASS_SEP = "\\$";

    /**
     * File ending <code>.zip</code> identifying zip-files. 
     */
    private static final String ZIP_END = ".zip";

    /**
     * File ending <code>.jar</code> identifying jar-files. 
     */
    private static final String JAR_END = ".jar";

    /**
     * The length of a buffer to read at once. 
     */
    private static final int LEN_BUFFER = 1000;

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */


    /**
     * Determines whether a class file or a source file is meant. 
     */
    public enum ClsSrc {
			Class {
	    	String fileEnding() {
					return ".class";
	    	}
	    	String trimInnerClass(String clsName) {
					return clsName;
	    	}
			},
			Source {
	    	String fileEnding() {
					return ".java";
	    	}
	    	String trimInnerClass(String clsName) {
					return clsName.replaceAll(INNER_CLASS_SEP + ".*", "");
	    	}
			};

	/**
	 * Returns the ending of a class file or of a source file. 
	 *
	 * @return
	 *    either <code>.class</code> or <code>.java</code>. 
	 */
	abstract String fileEnding();

	/**
	 * Returns the name of the class enclosing <code>clsName</code> 
	 * (including <code>clsName</code> itself) 
	 * which has its own source/class-file. 
	 *
	 * @param clsName
	 *    the name of a class as a <code>String</code> value. 
	 * @return
	 *    The name of the class enclosing <code>clsName</code> 
	 *    (including <code>clsName</code> itself) 
	 *    which has its own source/class-file. 
	 *    Note that for class files, 
	 *    this is just <code>clsName</code> itself, 
	 *    whereas for source files the part of the classname 
	 *    following {@link #INNER_CLASS_SEP} is stripped off. 
	 */
	abstract String trimInnerClass(String clsName);
    } // enum ClsSrc 

    /**
     * Wrapps a file directly found within a directory 
     * or within a zip-archive which also includes jar-archives. 
     * Accordingly, there are two implementations, 
     * {@link JavaPath.OrdFileWrapper} and {@link JavaPath.ZipEntryWrapper}. 
     */
    interface FileWrapper {

		/**
		 * Returns whether the wrapped file is within a zip-archive. 
		 * If this is the case, the file wrapped was created newly. 
		 *
		 * @return 
		 *    a <code>boolean</code> value signifying 
		 *    whether the wrapped file is within a zip-archive. 
		 */
		boolean coversZip();

		/**
		 * Returns the file wrapped.  
		 * If the wrapped file is within a zip-archive, 
		 * it was created newly by this <code>FileWrapper</code>. 
		 *
		 * @return 
		 *    the <code>File</code> wrapped. 
		 * @throws IOException 
		 *    if an error occurs
		 */
		File getFile() throws IOException;

		/**
		 * Returns an input stream for the file wrapped.  
		 *
		 * @return 
		 *    the <code>InputStream</code> of the file wrapped. 
		 * @throws IOException 
	 	 *    if an error occurs
		 */
		InputStream getInputStream() throws IOException;
  } // interface FileWrapper 

  /**
   * Represents an ordinary file {@link #file} on a file system. 
   */
  static class OrdFileWrapper implements FileWrapper {

		private final File file;

		OrdFileWrapper(File file) {
		    this.file = file;
		}

		// api-docs inherited from interface FileWrapper 
		public boolean coversZip() {
		    return false;
		}

		// api-docs inherited from interface FileWrapper 
		public File getFile() {
		    return this.file;
		}

		// api-docs inherited from interface FileWrapper 
		public InputStream getInputStream() throws IOException {
	    return new FileInputStream(this.file);
		}
  } // class OrdFileWrapper 

  // **** missing: closing of InputStream: 
  // both stream and zipfile: zipFile.close();  stream.close();
  /**
   * Represents a file {@link #entry} 
   * which is an entry in a zip-file {@link #zipFile}. 
   * The zip-file may be in any zip format 
   * and in particular a jar-archive 
   * (which is the common application case). 
   */
  static class ZipEntryWrapper implements FileWrapper {

		/**
		 * A filter input stream 
		 * closing {@link JavaPath.ZipEntryWrapper#zipFile} 
		 * when closing the stream. 
		 */
		class WrappedInputStream extends FilterInputStream {
			WrappedInputStream(InputStream inputStream) {
				super(inputStream);
	    }
	    public void close() throws IOException {
				super.close();
				ZipEntryWrapper.this.zipFile.close();
	    }
		} // class WrappedInputStream 

		private final ZipFile zipFile;
		private final ZipEntry entry;

		ZipEntryWrapper(ZipFile zipFile, ZipEntry entry) {
	    this.zipFile = zipFile;
	    this.entry = entry;
		}

		// api-docs inherited from interface FileWrapper 
		public boolean coversZip() {
	    return true;
		}

		// api-docs inherited from interface FileWrapper 
		public File getFile() throws IOException {
	    // by extraction. 
	    // may throw IOException
	    File ret = File.createTempFile("simuline_JavaPath"
				+ System.currentTimeMillis(),
				ClsSrc.Source.fileEnding());
	    try (
		 	//new File("/tmp/HI.java"); //this.entry.getName());
		 	//ret.createNewFile(); 
		 	// **** does not work well: shall be rec!!!
			 // may throw IOException
				InputStream inStream = getInputStream();
		 	// may throw IOException
		 		FileOutputStream outStream = new FileOutputStream(ret)) {
		    byte[] buf = new byte[LEN_BUFFER];
		    int numRead = inStream.read(buf, 0, LEN_BUFFER - 1);
		    while (numRead != -1) {
					/*     */outStream.write(buf, 0, numRead);
					numRead = inStream.read (buf, 0, LEN_BUFFER - 1);
		    }
	    } // try 

	    return ret;
		}

		// api-docs inherited from interface FileWrapper 
		public InputStream getInputStream() throws IOException {
	    return this.zipFile.getInputStream(this.entry);
		}

  } // class ZipEntryWrapper 


  /* -------------------------------------------------------------------- *
   * fields.                                                              *
   * -------------------------------------------------------------------- */

  /**
   * The list of entries of this path. 
   * This serves as root path for the source files and class files 
   * under consideration. 
   * CAUTION: Note that besides directories 
   * also <code>.zip</code>-files and  <code>.jar</code>-files are allowed. 
   */
  private final List<File> roots;

  /* -------------------------------------------------------------------- *
   * constructor.                                                         *
   * -------------------------------------------------------------------- */

  /**
   * Creates a new <code>JavaPath</code> instance.
   * Essentially, {@link #roots} is initialized. 
   *
   * @param path
   *    a path as a <code>String</code> value. 
   *    note that the entries of the path must not be the empty string. 
   * @throws IllegalArgumentException
   *    if two path separators immediately follow on one another 
   *    or if they stand at the beginning or at the end 
   *    of <code>path</code>. 
   */
  public JavaPath(String path) {
		// split the path along the path separator. 
		String[] fileNames = path.split(PATH_SEP);
		if (fileNames.length == 0) {
	    throw new IllegalArgumentException
				("String \"" + path + "\" is not a path. ");
		}

		this.roots = new ArrayList<File>(fileNames.length);
		for (String fileName :fileNames) {
	    if (fileName.length() == 0) {
				System.out.println
		  	  ("Warning: Found file \"\" in path \"" + path + "\". ");

// 		throw new IllegalArgumentException
// 		    ("Found file \"\" in path \"" + path + "\". ");
	    }
// 	for (int i = 0; i < fileNames.length; i++) {
// 	    if (fileNames[i].length() == 0) {
// 		System.out.println
// 		    ("Warning: Found file \"\" in path \"" + path + "\". ");

// // 		throw new IllegalArgumentException
// // 		    ("Found file \"\" in path \"" + path + "\". ");
// 	    }
	    this.roots.add(new File(fileName));
		}
  } // JavaPath 

  /* -------------------------------------------------------------------- *
   * methods.                                                             *
   * -------------------------------------------------------------------- */

  /**
   * Converts a class name into the corresponding name 
   * of a local source file or class file. 
   *
   * @param clsName 
   *    the name of the class as a <code>String</code> value. 
   * @param clsSrc 
   *    a <code>ClsSrc</code> which determines 
   *    whether to convert the class name 
   *    into a class file or into a source file. 
   * @return 
   *    the name of the local class or source file 
   *    of the given class. 
   *    Note that this is no absolute pathname of course: 
   *    {@link #roots} is not read. 
   */
  private String cls2locFile(String clsName, ClsSrc clsSrc) {
/*
	if (cls.isArray()) {
	    throw new IllegalArgumentException
		("Tried to load array " + cls + " by path. ");
	}
	if (cls.isPrimitive()) {
	    throw new IllegalArgumentException
		("Tried to load primitive class " + cls + " by path. ");
	}
*/
		// replace file separator / by .
		String localFilename = clsName.replace(CLASS_SEP, FILE_SEP);
		// for source files: remove names of inner classes 
		localFilename = clsSrc.trimInnerClass(localFilename);
		// append the appropriate file ending 
		StringBuffer fileNameBuf = new StringBuffer(localFilename);
		fileNameBuf.append(clsSrc.fileEnding());
		return fileNameBuf.toString();
  }

  /**
   * Converts a class name into the corresponding source file or class file 
   * if possible. 
   *
   * @param clsName 
   *    the name of the class as a <code>String</code> value. 
   * @param clsSrc 
   *    a <code>ClsSrc</code> which determines 
   *    whether to convert the class name 
   *    into a class file or into a source file. 
   * @return 
   *    the source or class <code>File</code> 
   *    corresponding with the given classname if there is one; 
   *    otherwise returns <code>null</code>. 
   *    Note that if the file is found within a zip- or jar-file, 
   *    a temporal file is created. 
   *    In any case, the file returned exists 
   *    unless <code>null</code> is returned. 
	 * @throws IOException
	 *   if an IOException occurs 
	 *   while searching <code>clsName</code> 
	 *   in a zip/jar file 
	 *   or while extracting that file from zip file.
	 */
  public File getFile(String clsName, ClsSrc clsSrc) throws IOException {
		return getFile(cls2locFile(clsName, clsSrc));
  }

  /**
	 * Returns the file given by <code>localFilename</code> 
	 * if it is on the path; else <code>null</code>. 
	 * Also files in zip files are found. 
	 * 
	 * @param localFilename
	 *   the relative filename of the file to be found on this path. 
	 * @return
	 *   the file <code>localFilename</code>, 
	 *   if found in a zip file as a new temporary file
	 *   or <code>null</code> if not found on the path. 
	 * @throws IOException
	 *   if an IOException occurs 
	 *   while searching <code>localFilename</code> 
	 *   in a zip file 
	 *   or while extracting file <code>localFilename</code> from zip file. 
	 */
  public File getFile(String localFilename) throws IOException {
		// locFile2Wrapper may cause IOException during search
		FileWrapper fileWr = locFile2Wrapper(localFilename);
		// getFile may cause IOException if found in zip file 
		// and reading or writing causes problems. 
		return fileWr == null ? null : fileWr.getFile();
  }

	/**
	 * Returns the input stream 
	 * to read the class with the given name <code>clsName</code> 
	 * from a file or zip or jar file 
	 * or <code>null</code> if no such file exists on this path. 
	 * 
	 * @return
	 *   the class file as an input stream if found on the path 
	 *   and <code>null</code> if it is not on this path. 
	 * @throws IOException
	 *   if during the search of <code>clsName</code> 
	 *   a zip file (or in particular a jar file) was not searchable 
	 *   or if no input stream could be formed from that file.  
	 */
  public InputStream getInputStream(String clsName) throws IOException {
		// locFile2Wrapper searches on path and in jar file. 
		FileWrapper fileWrapper = locFile2Wrapper(cls2locFile(clsName,
																							ClsSrc.Class));
		return (fileWrapper == null) ? null : fileWrapper.getInputStream();
  }

  /**
   * Converts a local file name into the wrapper 
   * of the file found on the path. 
   * Note that also within zip- or jar-file is searched. 
   *
   * @param localFilename 
   *    the name of a local file as a <code>String</code> value. 
   * @return 
   *    a <code>FileWrapper</code> representing the first file 
   *    found on the path with the appropriate name or <code>null</code> 
	 *    if no such file is found. 
   *    If the file is found in a directory of the path, 
   *    an {@link OrdFileWrapper JavaPath.OrdFileWrapper} is returned, 
   *    if it is found within a zip- or jar-file, 
   *    a {@link ZipEntryWrapper JavaPath.ZipEntryWrapper} is returned. 
	 * @throws IOException
	 *   if there is a zip file (or in particular a jar file) in the path 
	 *   which is not accessible during search of the file <code>localFilename</code>. 
	 *   A corrupt zip file causes an exception only if it is on this path 
	 *   in a position so that the file <code>localFilename</code> is not on the path 
	 *   before that file. 
   */
	//@SuppressWarnings("PMD.CognitiveComplexity")// reportLevel is 15, maybe shall be 16
  private FileWrapper locFile2Wrapper(String localFilename) throws IOException {
		File cand = null;
		// otherwise: might not be initialized. 
		// this is impossible because this.files is not empty. 

	  // search for the file with the given name along the path. 
		for (File candParent : this.roots) {
			if (candParent.isDirectory()) {
				// find directly in the directory candParent
				cand = new File(candParent, localFilename);
				if (cand.exists() && cand.isFile()) {
	    		return new OrdFileWrapper(cand);
				}
    	} else {
				// find directly in the zip- or jar-file candParent
				assert !candParent.isDirectory();
				if (!(candParent.getName().endsWith(ZIP_END)
				   || candParent.getName().endsWith(JAR_END))) {
	    		continue;
				}
				// here, we have a zip-file at least 
				// if not a jar-file. 
				ZipFile zipFile;
	    	zipFile = new ZipFile(candParent);
				// Here, the zip-file is readable. 
				ZipEntry entry = zipFile.getEntry(localFilename);
				if (entry == null) {
	    		// file not found 
	    		continue;
				}
				return new ZipEntryWrapper(zipFile, entry);
    	}
		} // for 
		// Here, no file has been found 
		return null;
  }

  // **** no support for zip- and jar-files. 
  // returns null if not found on the path. 
  public String getLocFileName(File absFile) {
		String fileName = absFile.getPath();
		for (File cand : this.roots) {

	    if (fileName.startsWith(cand.getPath())) {
				fileName = fileName.substring(cand.getPath().length() + 1,
					         fileName                         .length());
				return fileName;
	    }
		}
		// Here, the file was not found on the path 
		return null;
  }

  public String locFile2cls(String locFileName, ClsSrc clsSrc) {
		if (!locFileName.endsWith(clsSrc.fileEnding())) {
	    throw new IllegalArgumentException
				("Expected filename with ending \"" + clsSrc.fileEnding()
				 + "\" but found \"" + locFileName + "\". ");
		}
		locFileName = locFileName.substring(0,
				  /*    */locFileName.length()
					- clsSrc.fileEnding().length());
		locFileName = locFileName.replace(FILE_SEP, CLASS_SEP);
		return locFileName;
  }

  public String absFile2cls(File absFile, ClsSrc clsSrc) {
		String locFileName = getLocFileName(absFile);
		System.out.println("locFileName: " + locFileName);
	
		if (locFileName == null) {
	    return null;
		}
		return locFile2cls(locFileName, clsSrc);
  }

  public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("<JavaPath>");
		ret.append(this.roots);
		ret.append("</JavaPath>");
		return ret.toString();
  }

  public static void main(String[] args) {
		//System.out.println("tt: "+new JavaPath(":"));
		//System.out.println("tt: "+new JavaPath(""));
		System.out.println("tt: " + new JavaPath("/home/ernst"));
		System.out.println("tt: " + new JavaPath("/home/ernst:/usr/bin"));

		//JavaPath jPath = new JavaPath("/home/ernst/.../src/");
/*
	System.out.println("tt: "+
			   jPath.getFile(AddArrays.class, ClsSrc.Class));
	System.out.println("tt: "+
			   jPath.getFile(AddArrays.class, ClsSrc.Source));

*/
  }
}
