package eu.simuline.util;

import java.util.Iterator;

/**
 * Generalization of iterator suitable e.g. for sets to {@link MultiSet}s. 
 * Needed in addition: methods on multiplicity of element read last. 
 * <p>
 * Not clear whether one should not allow addMult. 
 * why not unify addMult and removeMult? 
 * This would require a different implementation. 
 * Problem is also that {@link MultiSet} is not (yet) a collection. 
 *
 *
 * @param <E>
 *    the class of the elements of this multi-set. 
 *
 * Created: Fri Oct  3 21:37:29 2014
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public interface MultiSetIterator<E> extends Iterator<E> {

    /**
     * Removes from the underlying {@link MultiSet} 
     * the last element returned by {@link #next()}, 
     * provided that element was not removed in the meantime 
     * and this method is supported by this iterator. 
     * <p>
     * Invoking this method removes that element 
     * from the set associated with {@link MultiSet}. 
     * After this, neither {@link #setMult(int)} nor {@link #removeMult(int)} 
     * nor  {@link #getMult()} may be invoked 
     * before next invocation of {@link #next()}. 
     * <p>
     * The behavior of this iterator is unspecified 
     * if the underlying {@link MultiSet} is modified 
     * while the iteration is in progress 
     * in any way other than by calling methods of an iterator. 
     *
     * @throws UnsupportedOperationException 
     *    if removing is not supported by this iterator. 
     *    This is in particular true 
     *    if the underlying {@link MultiSet} is immutable. 
     * @throws IllegalStateException 
     *    provided this method is supported by this iterator: 
     *    if the method {@link #next()} has not yet been called 
     *    or if, after last invocation of {@link #next()} 
     *    the element returned by this invocation has been removed 
     *    from the underlying {@link MultiSet} 
     *    invoking {@link #remove()}, {@link #setMult(int)} 
     *    or {@link #removeMult(int)} or a combination in an appropriate way. 
     */
    void remove();

    /**
     * Returns the current multiplicity of the element 
     * last read by {@link #next()}, 
     * provided that element was not removed in the meantime. 
     * This multiplicity could have been modified 
     * after last invocation of {@link #next()} 
     * by invoking {@link #remove()}, 
     * {@link #setMult(int)} or {@link #removeMult(int)}. 
     * If the multiplicity became zero, 
     * this means that it has been completely removed 
     * and so its multiplicity cannot be queried any more. 
     * 
     * @return
     *    the multiplicity of the element this method refers to. 
     *    This is a positive integer. 
     * @throws IllegalStateException 
     *    if the method {@link #next()} has not yet been called 
     *    or if, after last invocation of {@link #next()} 
     *    the element returned by this invocation has been removed 
     *    from the underlying {@link MultiSet} 
     *    invoking {@link #remove()}, {@link #setMult(int)} 
     *    or {@link #removeMult(int)} or a combination in an appropriate way. 
     */
    int getMult();

    /**
     * Endows the element of the underlying {@link MultiSet} 
     * returned by the last invocation of {@link #next()} 
     * with multiplicity given by <code>setMult</code>, 
     * provided that element was not removed in the meantime 
     * and this method is supported by this iterator. 
     * <p>
     * For <code>setMult=0</code>, invoking this method removes that element 
     * from the set associated with {@link MultiSet}. 
     * After this, neither {@link #setMult(int)} nor {@link #removeMult(int)} 
     * nor {@link #remove()} nor {@link #getMult()} may be invoked 
     * before next invocation of {@link #next()}. 
     * <p>
     * The behavior of this iterator is unspecified 
     * if the underlying {@link MultiSet} is modified 
     * while the iteration is in progress 
     * in any way other than by calling methods of an iterator. 
     * 
     * @param setMult 
     *    a non-negative number signifying the new multiplicity 
     *    of the element read last by {@link #next()}. 
     * @return
     *    the multiplicity of the element this method refers to 
     *    before invoking this method. 
     * @throws UnsupportedOperationException 
     *    if setting multiplicity is not supported by this iterator. 
     *    This is in particular true 
     *    if the underlying {@link MultiSet} is immutable. 
     * @throws IllegalStateException 
     *    provided this method is supported by this iterator: 
     *    if the method {@link #next()} has not yet been called 
     *    or if, after last invocation of {@link #next()} 
     *    the element returned by this invocation has been removed 
     *    from the underlying {@link MultiSet} 
     *    invoking {@link #remove()}, {@link #setMult(int)} 
     *    or {@link #removeMult(int)} or a combination in an appropriate way. 
     * @throws IllegalArgumentException 
     *    if <code>setMult</code> is negative 
     *    and if the abovementioned exceptions are not thrown. 
     */
    int setMult(int setMult);

    //int addMult(int addMult);

    /**
     * Removes from the underlying {@link MultiSet} 
     * the last element returned by invocation of {@link #next()} 
     * with multiplicity given by <code>removeMult</code>, 
     * provided that element was not removed in the meantime 
     * and this method is supported by this iterator. 
     * <p>
     * If <code>removeMult</code> is the multiplicity of that element, 
     * invoking this method removes that element 
     * from the set associated with {@link MultiSet}. 
     * After this, neither {@link #setMult(int)} nor {@link #remove()} 
     * nor {@link #remove()} nor {@link #getMult()} may be invoked 
     * before next invocation of {@link #next()}. 
     * <p>
     * The behavior of this iterator is unspecified 
     * if the underlying {@link MultiSet} is modified 
     * while the iteration is in progress 
     * in any way other than by calling methods of an iterator. 
     *
     * @param removeMult 
     *    a non-negative number signifying the 
     * @return
     *    the multiplicity of the element this method refers to 
     *    before invoking this method. 
     * @throws UnsupportedOperationException 
     *    if removing multiplicity is not supported by this iterator. 
     *    This is in particular true 
     *    if the underlying {@link MultiSet} is immutable. 
     * @throws IllegalStateException 
     *    if the method {@link #next()} has not yet been called 
     *    or if, after last invocation of {@link #next()} 
     *    the element returned by this invocation has been removed 
     *    from the underlying {@link MultiSet} 
     *    invoking {@link #remove()}, {@link #setMult(int)} 
     *    or {@link #removeMult(int)} or a combination in an appropriate way. 
     * @throws IllegalArgumentException 
     *    if <code>removeMult</code> or resulting multiplicity is negative 
     *    and if the abovementioned exceptions are not thrown. 
     * @see #remove()
     */
    int removeMult(int removeMult);
}
