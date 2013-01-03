package eu.simuline.util;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Emulates the unix shell command <code>find</code> which is invoked as 
 * <code>find [-H] [-L] [-P] [-Olevel] 
 * [-D help|tree|search|stat|rates|opt|exec] [path...] (expression)*</code>. 
 * If <code>path</code> is left out (possibly more than one path?), 
 * then the default is <code>.</code>, i.e. the current directory. 
 * The <code>path</code> need not be a directory. 
 * If it is a proper file then typically wildcards are used. 
 * Then the name is matched. 
 * If no expression is given, the default is <code>-print</code>. 
 * So we may assume, 
 * that both, a path and a non-empty sequence of expressions, are given. 
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
 * <p>
 * The most basic kind of finder corresponds with the command 
 * <code>find path</code> iterating just over the files in the path. 
 * This kind of finder is returned by the static method {@link #path(File)}. 
 * Starting with this basic finder, further finders can be added to the pipe 
 * using the member methods 
 * {@link #name(String)} and {@link # print(PrintStream)}. 
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

    // ordering is the same as in original find 
    static class Primary extends Finder {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	private final Stack<File> files;

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
		    // push in inverse order 
		    for (int i = list.length-1; i>=0; i--) {
			this.files.push(list[i]);
		    }
		    
		    //this.files.addAll(Arrays.asList(list));
		}
	    }
	    return file;
	}

	// ****
	public boolean pass(File file) {
	    throw new UnsupportedOperationException();
	}

    } // class Primary 

    class Multiplexer {

	/* ---------------------------------------------------------------- *
	 * inner classes.                                                   *
	 * ---------------------------------------------------------------- */

	class Slave extends Finder {
	    public boolean hasNext() {
		if (Multiplexer.this.read.contains(Slave.this)) {
		    return Finder.this.hasNext();
		}
		return Multiplexer.this.file != null;
	    }

	    public File next() {
		if (Multiplexer.this.read.contains(Slave.this)) {
		    assert read.size() == slaves.size();// **** 
		    Multiplexer.this.read.clear();
		    Multiplexer.this.file = Finder.this.next();
		}
		Multiplexer.this.read.add(Slave.this);

		return Multiplexer.this.file;
	    }

	    public boolean pass(File file) {
		throw new UnsupportedOperationException();
	    }

	} // class Slave 

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	private final Set<Finder> slaves;
	private final Set<Finder> read;
	private File file;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	Multiplexer() {
	    this.slaves = new HashSet<Finder>();
	    this.read   = new HashSet<Finder>();
	    if (Finder.this.hasNext()) {
		this.file = Finder.this.next();
	    } else {
		this.file = null;
	    }
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	Finder finder() {
	    Finder res = new Slave();
	    this.slaves.add(res);
	    return res;
	}


    } // class Multiplexer 

    /**
     * Filters files by name. See {@link Finder#name(String)}. 
     */
    class NameFilter extends Secondary {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	/**
	 * The pattern to filter the filenames with. 
	 */
	private final Pattern pattern;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	NameFilter(Finder encl, String pattern) {
	    super(encl);
	    this.pattern = Pattern.compile(pattern);
	    updateNext();
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public boolean pass(File file) {
	    return this.pattern.matcher(file.getName()).matches();
	}

    } // class NameFilter 

    abstract static class Secondary extends Finder {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	/**
	 * The next file to be returned by {@link #next()} if any, 
	 * i.e. if  {@link #hasNext()} returns <code>true</code>; 
	 * otherwise this field is <code>null</code>. 
	 */
	protected File next;

	Finder encl;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	Secondary(Finder encl) {
	    this.encl = encl;
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	protected void updateNext() {
//	    while (Finder.this.hasNext()) {
	    while (this.encl.hasNext()) {
//		this.next = Finder.this.next();
		this.next = this.encl.next();
		assert this.next != null;
		if (pass(this.next)) {
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
    } // class Secondary 

    class PrintFilter extends Secondary {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	private final PrintStream str;


	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	PrintFilter(Finder encl, PrintStream str) {
	    super(encl);
	    this.str = str;
	    updateNext();
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */

	public boolean pass(File file) {
	    return true;
	}

	public File next() {
	    assert hasNext();
	    File res = this.next;
	    assert res != null;
	    this.str.println(res.toString());
	    updateNext();
	    return res;
	}
    } // class PrintFilter 

    class ExecFilter extends Secondary {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	/**
	 * The command to be executed including arguments 
	 * separated by space as to be passed to {@link Runtime#exec(String)}. 
	 */
	private final String[] cmd;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	ExecFilter(Finder encl, String[] cmd) {
	    super(encl);
	    this.cmd = cmd;
	    updateNext();
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */


	public boolean pass(File file) {
	    Process proc;
	    int exitVal;
	    String nextStr = file.toString();

	    // replace argument by filename 
	    String[] cmd = new String[this.cmd.length];
	    System.arraycopy(this.cmd, 0, cmd, 0, this.cmd.length);
	    for (int idx=0; idx < this.cmd.length; idx++) {
		if (cmd[idx] == EXEC_ARG) {
		    cmd[idx] = nextStr;
		}
	    }

	    try {
		//System.out.println(":"+(this.cmdName+" "+this.next));
		proc = Runtime.getRuntime().exec(cmd);
	    } catch (IOException e) {
		System.out.println("exec: "+e.getMessage());
		return false;
	    }
	    try {
		exitVal = proc.waitFor();// exitValue
	    } catch (InterruptedException e) {
		System.out.println("exec:"+e.getMessage());
		return false;
	    }
	    return exitVal == 0;
	}

    } // class ExecFilter 

    class NegFilter extends Secondary {

	/* ---------------------------------------------------------------- *
	 * fields.                                                          *
	 * ---------------------------------------------------------------- */

	private final Finder negFilter;

	/* ---------------------------------------------------------------- *
	 * constructors.                                                    *
	 * ---------------------------------------------------------------- */

	NegFilter(Finder encl, Finder negFilter) {
	    super(encl);
	    this.negFilter = negFilter;
	    updateNext();
	}

	/* ---------------------------------------------------------------- *
	 * methods.                                                         *
	 * ---------------------------------------------------------------- */


	public boolean pass(File file) {
	    return !this.negFilter.pass(file);
	}

    } // class NegFilter 

    /* -------------------------------------------------------------------- *
     * Class constants.                                                     *
     * -------------------------------------------------------------------- */

    public final static String EXEC_ARG = "{}";

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

    Multiplexer multiplexer() {
	return new Multiplexer();
    }

    /**
     * Filter without side effect: 
     * just passes the files received by this finder 
     * the (short) names of which 
     * match the regular expression <code>pattern</code>. 
     */
    public Finder name(String pattern) {
	return new NameFilter(Finder.this, pattern);
    }

    /**
     * Filter passing all files received by this finder 
     * printing their full names to <code>str</code>. 
     */
    public Finder print(PrintStream str) {
	return new PrintFilter(Finder.this, str);
    }

    public Finder exec(String[] cmd) {
	return new ExecFilter(Finder.this, cmd);
    }

    public Finder neg(Finder filter) {
	return new NegFilter(Finder.this, filter);
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

    public abstract boolean pass(File file);

    public static void main(String[] args) throws Exception {
	//	int res = Runtime.getRuntime()
//	    .exec(new String[] {"grep", "Octave", "./src/eu/simuline/matlab/Matlab.g"})
//	    .waitFor() ;
//	System.out.println("res: "+res);
//	System.exit(0);

	File file = new File(args[0]);

	Finder finder = Finder.path(file)
	    .name(".*\\.java")
	    .exec(new String[] {"grep", "matlab", EXEC_ARG})
	    .print(System.out);

	// Finder finder = Finder.path(file);
	// Multiplexer mult = finder.multiplexer();
	// Finder negFilter = mult.finder()
	//     .exec(new String[] {"grep", "'", EXEC_ARG});
	// finder = mult.finder().neg(negFilter);

	while (finder.hasNext()) {
	    finder.next();
//	    System.out.println(""+finder.next());
	}
    }

}
