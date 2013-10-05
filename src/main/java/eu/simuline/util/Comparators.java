package eu.simuline.util;

import java.util.Comparator;
import java.util.Collection;
import java.util.List;

/**
 * Comparators.java
 *
 *
 * Created: Sat May 21 16:30:58 2005
 *
 * @author <a href="mailto:ernst@local">Ernst Reissner</a>
 * @version 1.0
 */
public abstract class Comparators {// NOPMD

    /* -------------------------------------------------------------------- *
     * inner classes.                                                       *
     * -------------------------------------------------------------------- */

 
    private static class Cascade<E> implements Comparator<E> {
	private final Collection<Comparator<E>> seq;
	Cascade(Collection<Comparator<E>> seq) {
	    this.seq = seq;
	}
	public int compare(E obj1, E obj2) {
	    int res;
	    for (Comparator<E> cmp : seq) {
		res = cmp.compare(obj1,obj2);
		if (res != 0) {
		    return res;
		}
	    }
	    // non the comparators could decide ordering. 

	    return 0;
	}
    } // class Cascade<E> 

    /**
     * Implements an ordering given by the list {@link #seq}. 
     * Elements not in the list are all minimal 
     * and pairwise equal with respect to this comparator. 
     * They are less than all elements in the list. 
     * The ordering on the list is ascending. 
     * <p>
     * CAUTION: This ordering is consistent with equals only 
     * if the elements of {@link #seq} are pairwise non-equal. 
     * If this condition is hurt, 
     * an element <code>a</code> may satisfy <code>a>a</code>. 
     * Even if this condition is satisfied, 
     * two elements <code>a</code>, <code>b</code> not in {@link #seq} 
     * satisfy <code>a=b</code> with respect to the ordering 
     * even if they are not equal 
     * with respect to the method {@link #equals(Object)}. 
     * Thus the ordering is consistent with equals only, 
     * if restricted to the elements of {@link #seq} 
     * plus one single object not in {@link #seq}. 
     * This suffices to make the method {@link SortedSet#add(Object)} 
     * to work properly. 
     * <p>
     * The ordering is always total, 
     * i.e. either <code>a\le b</code> or <code>b\le a</code> holds. 
     * This is clear if both elements are in {@link #seq} 
     * and also if one is not. 
     * Also the ordering is always transitive: 
     * Assume <code>a\le b</code> or <code>b\le c</code>. 
     * If <code>a\not\in seq</code>, then <code>a\le c</code>. 
     * If <code>b\not\in seq</code> then also <code>a\not\in seq</code>. 
     * If<code>c\not\in seq</code> then also <code>b\not\in seq</code>. 
     * So, if one of the elements is not in {@link #seq}, 
     * transitivity holds. 
     * Otherwise, all elements are in {@link #seq} 
     * and transitivity follows directly. 
     */
    private static class AsListed<E> implements Comparator<E> {

	/* ----------------------------------------------------------------- *
	 * fields.                                                           *
	 * ----------------------------------------------------------------- */


	private final List<E> seq;

	/* ----------------------------------------------------------------- *
	 * constructors.                                                     *
	 * ----------------------------------------------------------------- */

	// **** does not check that the elements of seq are pairwise different. 
	AsListed(List<E> seq) {
	    if (seq == null) {
		throw new NullPointerException();
	    }
	    this.seq = seq;
	    assert this.seq != null;
	}

	/* ----------------------------------------------------------------- *
	 * methods.                                                          *
	 * ----------------------------------------------------------------- */

	public int compare(E obj1, E obj2) {
	    // The result is 0, if neither object is in seq, 
	    // because the indices are both -1. 
	    // The result is the difference of the indices 
	    // if both objects are in seq. 
	    // This defines an ascending ordering on seq. 
	    // If the second object is not in seq, whereas the first one is, 
	    // the result is strictly negative. 
	    // Accordingly, 
	    // if the first object is not in seq, whereas the second one is, 
	    // the result is strictly positive. 
	    // Thus all elements in seq are smaller 
	    // than all elements not in seq. 
    
	    int idx1 = this.seq.indexOf(obj1);
	    int idx2 = this.seq.indexOf(obj2);
	    if (idx1 == -1 || idx2 == -1) {
		// If neither object is in seq, returns 0=-1-(-1) 
		// If obj1 is in seq whereas obj2 is not, returns <0 
		// If obj2 is in seq whereas obj1 is not, returns >0 
		return this.seq.indexOf(obj2)-this.seq.indexOf(obj1);
	    } else {
		// Here, both obj1 and obj2 are in seq. 
		// Returns the signed difference of the indices. 
		return this.seq.indexOf(obj1)-this.seq.indexOf(obj2);
	    }	    
	}
    } // class AsListed<E> 


    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */

 

    public static <E> Comparator<E> getNegative(Comparator<E> cmp) {
	return new Comparator<E>() {
	    public int compare(E obj1, E obj2) {
		return -compare(obj1,obj2);
	    }
	};
    }

    public static <E> Comparator<E> getInv(Comparator<E> cmp) {
	return new Comparator<E>() {
	    public int compare(E obj1, E obj2) {
		return compare(obj2,obj1);
	    }
	};
    }

    public static <E> Comparator<E> getCascade(Collection<Comparator<E>> seq) {
	return new Cascade<E>(seq);
    }

    /**
     * Returns an ordering given by the list {@link #seq}. 
     * Elements not in the list are all minimal 
     * and pairwise equal with respect to this comparator. 
     * They are less than all elements in the list. 
     * The ordering on the list is ascending. 
     * <p>
     * CAUTION: This ordering is consistent with equals only 
     * under certain conditions. 
     * The other ordering axioms hold. 
     * For more details see {@link Comparators.AsListed}. 
     * <p>
     * Caution: changing the list, affects the comparator. 
     *
     * @param seq
     *    a list which determines the ordering. 
     * @throws NullPointerException
     *    if <code>seq</code> is <code>null</code>. 
     */
    public static <E> Comparator<E> getAsListed(List<E> seq) {
	return new AsListed<E>(seq);
    }
    

} // Comparators
