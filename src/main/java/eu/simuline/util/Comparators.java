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


    static class Cascade<E> implements Comparator<E> {
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


    static class AsListed<E> implements Comparator<E> {
	private final List<E> seq;
	AsListed(List<E> seq) {
	    this.seq = seq;
	}

	public int compare(E obj1, E obj2) {
	    return this.seq.indexOf(obj2)-this.seq.indexOf(obj1);
	}
    } // class Cascade<E> 




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
    public static <E> Comparators.AsListed<E> getAsListed(List<E> seq) {
	return new AsListed<E>(seq);
    }
    

} // Comparators
