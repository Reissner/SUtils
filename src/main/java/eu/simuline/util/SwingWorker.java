package eu.simuline.util;

import javax.swing.SwingUtilities;

/* ********FROM SUN but FREE ************* */

/** 
 * This is the 3rd version of SwingWorker (also known as
 * SwingWorker 3), an abstract class that you subclass to
 * perform GUI-related work in a dedicated thread. 
 * For instructions on using this class, see:
 * 
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 *
 * Note that the API changed slightly in the 3rd version:
 * You must now invoke start() on the SwingWorker after
 * creating it. 
 */
public abstract class SwingWorker {
    private Object value;  // see getValue(), setValue()
    //private Thread thread;

    /** 
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private static class ThreadVar {
        private Thread thread;
        ThreadVar(Thread thr) {
	    thread = thr;
	}
        synchronized Thread get() {
	    return thread;
	}
        synchronized void clear() {
	    thread = null;
	}
    }

    private final ThreadVar threadVar;

    /** 
     * Get the value produced by the worker thread, or null if it 
     * hasn't been constructed yet.
     */
    protected final synchronized Object getValue() { 
        return this.value; 
    }

    /** 
     * Set the value produced by worker thread. 
     */
    private synchronized void setValue(Object obj) { 
        this.value = obj; 
    }

    /** 
     * Compute the value to be returned by the <code>get</code> method. 
     */
    public abstract Object construct();

    /**
     * Called on the event dispatching thread (not on the worker thread)
     * after the <code>construct</code> method has returned.
     */
    public void finished() {
	// is empty. 
    }

    /**
     * A new method that interrupts the worker thread.  Call this method
     * to force the worker to stop what it's doing.
     */
    public final void interrupt() {
        Thread thr = threadVar.get();
        if (thr != null) {
            thr.interrupt();
        }
        threadVar.clear();
    }

    /**
     * Return the value created by the <code>construct</code> method.  
     * Returns null if either the constructing thread or the current
     * thread was interrupted before a value was produced.
     * 
     * @return the value created by the <code>construct</code> method
     */
    public final Object get() {
        while (true) {  
            Thread thr = threadVar.get();
            if (thr == null) {
                return getValue();
            }
            try {
                thr.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }


    /**
     * Start a thread that will call the <code>construct</code> method
     * and then exit.
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
           public void run() {
	       finished();
	   }
        };

        Runnable doConstruct = new Runnable() { 
            public void run() {
                try {
                    setValue(construct());
                } finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        Thread thr = new Thread(doConstruct);
        threadVar = new ThreadVar(thr);
    }

    /**
     * Start the worker thread.
     */
    public final void start() {
        Thread thr = threadVar.get();
        if (thr != null) {
            thr.start();
        }
    }
}
