package eu.simuline.util;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Emulates the unix shell command <code>find</code> which is invoked as 
 * <code>find [-H] [-L] [-P] [-Olevel] 
 * [-D help|tree|search|stat|rates|opt|exec] [path...] (expression)*</code>. 
 * If <code>path</code> is left out (possibly more than one path?), 
 * then the default is <code>.</code>, i.e. the current directory. 
 * This need not be a directory. 
 * If it is a proper file then typically wildcards are used. 
 * Then the name is matched. 
 * If no expression is given, the default is <code>-print</code>. 
 * So we may assume, 
 * that a path and a non-empty sequence of expressions is given. 
 * <p>
 * The idea behind the <code>find</code> command is, 
 * that, starting with the files matching the <code>path</code>, 
 * each <code>expression</code> serves as a filter, 
 * feeding the filter corresponding to the following expression. 
 * Each filter can have a side effect. 
 * Expressions may be either tests or actions. 
 * For tests, the focus is on the filter-functionality, 
 * whereas actions are trivial filters just passing the files they receive. 
 * Actions are applied because of their side effects like <code>print</code>. 
 *
 *
 * Created: Wed Nov 21 17:29:41 2012
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
public abstract class Finder {

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

    // **** ordering deviates from original find 
    static class Primary extends Finder {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	private Stack<File> files;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	Primary(File file) {
	    this.files = new Stack<File>();
	    this.files.push(file);
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public boolean hasNext() {
	    return !this.files.isEmpty();
	}

	public File next() {
	    assert hasNext();
	    File file = this.files.pop();
	    if (file.isDirectory()) {
		File[] list = file.listFiles();
		if (list == null) {
		    System.out.println("cannot read "+file);
		} else {
		    this.files.addAll(Arrays.asList(list));
		}
	    }
	    return file;
	}
    } // class Primary 
    //<code></code>

    /**
     * Filters by name. See {@link Finder#name(String)}. 
     */
    class NameFilter extends Finder {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	private Pattern pattern;
	private File next;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	NameFilter(String pattern) {
	    this.pattern = Pattern.compile(pattern);
	    updateNext();
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	private void updateNext() {
	    boolean matches;
	    while (Finder.this.hasNext()) {
		this.next = Finder.this.next();
		assert this.next != null;
		matches = this.pattern.matcher(this.next.getName()).matches();
		if (matches) {
		    return;
		}
	    }
	    this.next = null;
	}

	public boolean hasNext() {
	    return this.next != null;
	}

	public File next() {
	    assert hasNext();
	    File res = this.next;
	    assert res != null;
	    updateNext();
	    return res;

	}
    } // class NameFilter 

    class PrintFilter extends Finder {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	private PrintStream str;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	PrintFilter(PrintStream str) {
	    this.str = str;
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public boolean hasNext() {
	    return Finder.this.hasNext();
	}

	public File next() {
	    assert hasNext();
	    File res = Finder.this.next();
	    this.str.println(res.toString());
	    return res;
	}
    } // class PrintFilter 

    /* -------------------------------------------------------------------- *
     * constructors and creator methods.                                    *
     * -------------------------------------------------------------------- */

    /**
     * To prevent instantiation.
     */
    private Finder() {
    }

    /**
     * Returns a basic finder, emitting the given file 
     * if it exists and is accessible and, recursively, 
     * if it is a directory all files therein. 
     */
    public static Finder path(File file) {
	return new Finder.Primary(file);
    }

    /**
     * Filter without side effect: 
     * just passes the files received by this finder 
     * the (short) names of which 
     * match the regular expression <code>pattern</code>. 
     */
    public Finder name(String pattern) {
	return new NameFilter(pattern);
    }

    /**
     * Filter passing all files received by this finder 
     * printing their full names to <code>str</code>. 
     */
    public Finder print(PrintStream str) {
	return new PrintFilter(str);
    }

    /* -------------------------------------------------------------------- *
     * abstract methods.                                                    *
     * -------------------------------------------------------------------- */

    /**
     * Returns whether this Finder can emit another file. 
     *
     * @see #next()
     */
    public abstract boolean hasNext();

    /**
     * Returns the next file this finder can emit. 
     * This does not throw an exception iff {@link #hasNext()} returns true. 
     */
    public abstract File next();

    public static void main(String[] args) {
	File file = new File(args[0]);
	Finder finder = Finder.path(file)
	    .name(".*\\.java")
	    .print(System.out);
	while (finder.hasNext()) {
	    System.out.println(""+finder.next());

	}
    }

}
