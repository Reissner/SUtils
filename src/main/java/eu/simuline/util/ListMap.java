

package eu.simuline.util;


import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.AbstractCollection;
import java.util.SortedMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;

/**
 * A {@link SortedMap} with ordering 
 * given by the ordering by which the keys are added. 
 * This is the {@link Map} corresponding with {@link ListSet}. 
 * <p>
 * Did not use {@link java.util.AbstractMap}, 
 * e.g. because {@link #keySet()} shall return more than just a {@link Set}. 
 *
 * @param <K>
 *    the class of keys for this map. 
 * @param <V>
 *    the class of values for this map. 
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
public final class ListMap<K, V> implements SortedMap<K, V> {

    /* -------------------------------------------------------------------- *
     * Inner classes.                                                       *
     * -------------------------------------------------------------------- */

    /**
     * Class of entries of the encylosing map. 
     * **** I wonder whether there is no generic implementation 
     * for {@link java.util.Map.Entry}. 
     */
    private final class Entry implements Map.Entry<K, V> {

	/* ------------------------------------------------------------------ *
	 * fields.                                                            *
	 * ------------------------------------------------------------------ */

	/**
	 * The key of this entry. 
	 * The corresponding value is {@link #value}. 
	 */
	private final K key;

	/**
	 * The value of this entry corresponding with the key {@link #key}. 
	 * Note that in contrast to the key, 
	 * the value of this entry may be changed 
	 * (through {@link #setValue(Object)}). 
	 */
	private V value;

	/* ------------------------------------------------------------------ *
	 * constructors.                                                      *
	 * ------------------------------------------------------------------ */

	/**
	 * Creates a new entry for the enclosing map 
	 * defined by the given <code>key</code> and <code>value</code>. 
	 */
	private Entry(K key, V value) {
	    this.key = key;
	    this.value = value;
	}

	/* ------------------------------------------------------------------ *
	 * methods.                                                           *
	 * ------------------------------------------------------------------ */

	/**
	 * Returns the {@link #key} of this entry. 
	 */
	public K getKey() {
	    return this.key;
	}

	/**
	 * Returns the {@link #value} of this entry. 
	 */
	public V getValue() {
	    return this.value;
	}

	/**
	 * Sets a new {@link #value} of this entry 
	 * and returns the original one. 
	 */
	public V setValue(V value) {
	    V oldValue = this.value;
	    this.value = value;
	    return oldValue;
	}

	// fits with method equals 
	public int hashCode() {
	    return this.getKey().hashCode() + this.getValue().hashCode();
	}

	// **** seems to be a bug in javadoc 
	/**
	 * Returns whether <code>obj</code> equals this entry, 
	 * i.e. whether <code>obj</code> 
	 * is an instance of {@link java.util.Map.Entry} 
	 * and both, its key and its value equals key and value of this entry. 
	 * Note that both, key and value may be <code>null</code>. 
	 */
	public boolean equals(Object obj) {
	    if (!(obj instanceof Map.Entry)) {
		return false;
	    }

	    Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
	    return Objects.equals(this.getKey(),   other.getKey()) 
		&& Objects.equals(this.getValue(), other.getValue());
	}

    } // class Entry

    /**
     * Represents the key set of the enclosing map 
     * given by {@link ListMap#keySet()}. 
     * <p>
     * Like base class {@link AbstractSet}, 
     * this class supports neither {@link Set#add(Object)} 
     * nor {@link Set#addAll(Collection)}, 
     * i.e. those methods throw an {@link UnsupportedOperationException}. 
     * In contrast, this class supports methods 
     * {@link Set#clear()}, 
     * {@link Set#remove(Object)}, 
     * {@link Set#removeAll(Collection)} and 
     * {@link Set#retainAll(Collection)}. 
     * The iterator returned by {@link #iterator()} 
     * supports {@link Iterator#remove()}. 
     *
     * @see ListMap#keysSet
     */
    private class Keys extends AbstractSet<K> implements SortedSet<K> {

	/* ------------------------------------------------------------------ *
	 * methods implementing Set based on AbstractSet.                     *
	 * ------------------------------------------------------------------ */

	/**
	 * Returns actually an instance of {@link ListMap.KeysIterator}. 
	 */
        public Iterator<K> iterator() {
            return new KeysIterator();
        }

	/**
	 * Returns actually the size of the enclosing {@link ListMap} 
	 * delegating to {@link ListMap#size()}. 
	 */
	public int size() {
	    return ListMap.this.size();
        }

	/**
	 * Returns actually whether <code>obj</code> 
	 * is a key of the enclosing {@link ListMap} 
	 * delegating to {@link ListMap#containsKey(Object)}. 
	 */
        public boolean contains(Object obj) {
	    return ListMap.this.containsKey(obj);
        }

	/**
	 * Interpretes <code>obj</code> as key 
	 * of the enclosing {@link ListMap} 
	 * and removes the according key-value pair if present. 
	 * Returns whether the according key-value pair was present 
	 * when invoking this method using 
	 * {@link ListMap#removeIdx(int)}. 
	 */
         public boolean remove(Object obj) {
	    int idx = ListMap.this.keys.getList().indexOf(obj);
	    return ListMap.this.removeIdx(idx);
        }

	/**
	 * Clears actually the enclosing {@link ListMap} 
	 * delegating to {@link ListMap#clear()}. 
	 */
        public void clear() {
            ListMap.this.clear();
	}

	/* ------------------------------------------------------------------ *
	 * methods implementing SortedSet.                                    *
	 * ------------------------------------------------------------------ */

	public Comparator<? super K> comparator() {
	    return ListMap.this.comparator();
	}

	public K first() {
	    return ListMap.this.keys.first();
	}

	public K last() {
	    return ListMap.this.keys.last();
	}

	public SortedSet<K> subSet(K fromElement, K toElement) {
	    return ListMap.this.subMap(fromElement, toElement).keySet();
	}

	public SortedSet<K> headSet(K toElement) {
	    return ListMap.this.headMap(toElement).keySet();
	}

	public SortedSet<K> tailSet(K fromElement) {
	    return ListMap.this.tailMap(fromElement).keySet();
	}

    } // class Keys 

    /**
     * Class of iterator of class  {@link ListMap.Keys}. 
     *
     * @see ListMap.Keys#iterator()
     */
    private class KeysIterator extends XIterator implements Iterator<K> {
    	public K next() {
            return this.lIter.next();
        }
    } //  class KeysIterator


    /**
     * Represents the set of entries of the enclosing map 
     * given by {@link ListMap#entrySet()}. 
     * <p>
     * Like base class {@link AbstractSet}, 
     * this class supports neither {@link Set#add(Object)} 
     * nor {@link Set#addAll(Collection)}, 
     * i.e. those methods throw an {@link UnsupportedOperationException}. 
     * In contrast, this class supports methods 
     * {@link Set#clear()}, 
     * {@link Set#remove(Object)}, 
     * {@link Set#removeAll(Collection)} and 
     * {@link Set#retainAll(Collection)}. 
     * The iterator returned by {@link #iterator()} 
     * supports {@link Iterator#remove()}. 
     *
     * @see ListMap#entrySet
     */
    private class Entries
	extends  AbstractSet<Map.Entry<K, V>> 
	implements SortedSet<Map.Entry<K, V>> {

	/* ------------------------------------------------------------------ *
	 * methods implementing Set based on AbstractSet.                     *
	 * ------------------------------------------------------------------ */

	/**
	 * Returns actually an instance of {@link ListMap.EntriesIterator}. 
	 */
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntriesIterator();
	}

	/**
	 * Returns actually the size of the enclosing {@link ListMap} 
	 * delegating to {@link ListMap#size()}. 
	 */
        public int size() {
	    return ListMap.this.size();
        }

	/**
	 * Assumes that <code>obj</code> is a {@link java.util.Map.Entry} 
	 * and returns its key. 
	 *
	 * @throws NullPointerException 
	 *    if <code>obj</code> is <code>null</code>. 
	 * @throws ClassCastException 
	 *    if <code>obj</code> is neither <code>null</code> 
	 *    nor an instance of {@link java.util.Map.Entry}. 
	 */
	private Object getKey(Object obj) {
	    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
	    return entry.getKey();
	}

	/**
	 * Returns actually whether <code>obj</code> 
	 * is an instance of {@link java.util.Map.Entry} 
	 * with a key contained in the enclosing {@link ListMap} 
	 * delegating to {@link ListMap#containsKey(Object)}. 
	 */
        public boolean contains(Object obj) {
	    if (!(obj instanceof Map.Entry)) {
		return false;
	    }
	    return ListMap.this.containsKey(getKey(obj));
        }

	/**
	 * Removes <code>obj</code> from the entry set if present 
	 * and returns whether it was present. 
	 * Effectively checks whether <code>obj</code> 
	 * is an instance of {@link java.util.Map.Entry}. 
	 * If not returns <code>false</code>. 
	 * If it is an instance of {@link java.util.Map.Entry}
	 * removes the according key-value pair 
	 * from the enclosing {@link ListMap} if present. 
	 * Returns whether <code>obj</code>'s key was present 
	 * when invoking this method. 
	 * This is done 
	 * invoking {@link ListMap.Keys#remove(Object)}. 
	 */
        public boolean remove(Object obj) {
	    if (!(obj instanceof Map.Entry)) {
		return false;
	    }
	    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
	    entry.getClass().getTypeParameters();
	    return ListMap.this.keysSet.remove(getKey(entry));
        }

	/**
	 * Clears actually the enclosing {@link ListMap} 
	 * delegating to {@link ListMap#clear()}. 
	 */
        public void clear() {
	    ListMap.this.clear();
 	}

	/* ------------------------------------------------------------------ *
	 * methods implementing SortedSet.                                    *
	 * ------------------------------------------------------------------ */

	public Comparator<? super Map.Entry<K, V>> comparator() {
	    return new  Comparator<Map.Entry<K, V>>() {
		public int compare(Map.Entry<K, V> entry1, 
				   Map.Entry<K, V> entry2) {
		    return ListMap.this.comparator()
			.compare(entry1.getKey(), entry2.getKey());
		}
	    };
	}
	//Cannot allocate the member type ListMap<K,V>.Entry using a parameterized compound name; 
	//use its simple name and an enclosing instance of type ListMap<K,V>
	private Map.Entry<K, V> key2entry(K key) {
	    return new Entry(key, get(key));
	}

	public Map.Entry<K, V> first() {
	    return key2entry(ListMap.this.keys.first());
	}

	public Map.Entry<K, V> last() {
	    return key2entry(ListMap.this.keys.last());
	}

	public SortedSet<Map.Entry<K, V>> subSet(Map.Entry<K, V> fromElement, 
					         Map.Entry<K, V>   toElement) {
	    return ListMap.this.subMap(fromElement.getKey(),
				       toElement  .getKey()).entrySet();
	}

	public SortedSet<Map.Entry<K, V>> headSet(Map.Entry<K, V>  toElement) {
	    return ListMap.this.headMap(toElement  .getKey()).entrySet();
	}

	public SortedSet<Map.Entry<K, V>> tailSet(Map.Entry<K, V> fromElement) {
	    return ListMap.this.tailMap(fromElement.getKey()).entrySet();
	}

    } // class Entries 

    /**
     * Class of iterator of class  {@link ListMap.Entries}. 
     *
     * @see ListMap.Entries#iterator()
     */
    private class EntriesIterator extends XIterator 
	implements Iterator<Map.Entry<K, V>> {

    	public Map.Entry<K, V> next() {
	    int idx = this.lIter.nextIndex();
	    return new Entry(this.lIter.next(), 
			     ListMap.this.values.get(idx));
        }
    } //  class EntriesIterator

    /**
     * Represents the values collection of the enclosing map 
     * given by {@link ListMap#values()}. 
     * <p>
     * Like base class {@link AbstractCollection}, 
     * this class supports neither {@link Collection#add(Object)} 
     * nor {@link Collection#addAll(Collection)}, 
     * i.e. those methods throw an {@link UnsupportedOperationException}. 
     * In contrast, this class supports methods 
     * {@link Set#clear()}, 
     * {@link Collection#remove(Object)}, 
     * {@link Collection#removeAll(Collection)} and 
     * {@link Collection#retainAll(Collection)}. 
     * The iterator returned by {@link #iterator()} 
     * supports @link Iterator#remove()}. 
     *
     * @see ListMap#valuesColl
     */
    private class Values extends AbstractCollection<V> {

	/**
	 * Returns actually an instance of {@link ListMap.ValuesIterator}. 
	 */
        public Iterator<V> iterator() {
            return new ValuesIterator();
        }

	/**
	 * Returns actually the size of the enclosing {@link ListMap} 
	 * delegating to {@link ListMap#size()}. 
	 */
        public int size() {
	    return ListMap.this.size();
        }

	/**
	 * Returns actually whether <code>obj</code> 
	 * is a value of the enclosing {@link ListMap} 
	 * delegating to {@link ListMap#containsValue(Object)}. 
	 */
        public boolean contains(Object obj) {
	    return ListMap.this.containsValue(obj);
	}

	/**
	 * Interpretes <code>obj</code> as value 
	 * of the enclosing {@link ListMap} 
	 * and removes the first according key-value pair if present. 
	 * Returns whether the according key-value pair was present 
	 * when invoking this method invoking using 
	 * {@link ListMap#removeIdx(int)}. 
	 */
        public boolean remove(Object obj) {
	    int idx = ListMap.this.values.indexOf(obj);
	    return ListMap.this.removeIdx(idx);
        }

	/**
	 * Clears actually the enclosing {@link ListMap} 
	 * delegating to {@link ListMap#clear()}. 
	 */
          public void clear() {
	    ListMap.this.clear();
	}

    } // class Values 

    /**
     * Class of iterator of class  {@link ListMap.Values}. 
     *
     * @see ListMap.Values#iterator()
     */
    @edu.umd.cs.findbugs.annotations.SuppressWarnings
	(value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT", 
	 justification = "bug in findbugs")
    private class ValuesIterator extends XIterator implements Iterator<V> {
  	public V next() {
	    int idx = this.lIter.nextIndex();
            this.lIter.next(); // has side effect! 
	    return ListMap.this.values.get(idx);
        }
    } //  class ValuesIterator

    /**
     * Common superclass of iterators on key set, 
     * value collection and entries set 
     * providing all methods but {@link Iterator#next()}. 
     * Implementation is based on a {@link ListIterator}. 
     */
    // PMD: rethink AbstractClassWithoutAbstractMethod 
    private abstract class XIterator {// NOPMD 

	/* ------------------------------------------------------------------ *
	 * fields.                                                            *
	 * ------------------------------------------------------------------ */

	/**
	 * An iterator over the list view of the key set 
	 * of the enclosing {@link ListMap}. 
	 *
	 * @see ListMap#keys
	 * @see ListSet#getList()
	 */
	@SuppressWarnings("checkstyle:visibilitymodifier")
	protected final ListIterator<K> lIter;

	/* ------------------------------------------------------------------ *
	 * constructors.                                                      *
	 * ------------------------------------------------------------------ */

	/**
	 * Creates a pre-iterator. 
	 *
	 * @see ListMap#keys
	 * @see ListSet#getList()
	 */
	XIterator() {
	    this.lIter = ListMap.this.keys.getList().listIterator();
	}

	/* ------------------------------------------------------------------ *
	 * methods.                                                           *
	 * ------------------------------------------------------------------ */

	/**
	 * Returns whether the iterators based on this class 
	 * has a next value. 
	 *
	 * @see Iterator#hasNext()
	 */
    	public boolean hasNext() {
            return this.lIter.hasNext();
        }
    	//public abstract X next();

	/**
	 * Removes from the underlying collection the last element returned 
	 * by the iterator based on this class.	
	 *
	 * @see Iterator#remove()
	 */
    	public void remove() {
	    // get the index of the element returned by next() last 
	    int idxPrev = this.lIter.previousIndex();
	    if (idxPrev == -1) {
		// never called next() 
		throw new IllegalStateException();
	    }
	    ListMap.this.removeIdx(idxPrev);
        }
    } //  class XIterator

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    /**
     * The set of keys of this <code>ListMap</code>. 
     */
    private final ListSet<K> keys;

    /**
     * The values of this map. 
     * The according keys are in {@link #keys}. 
     */
    private final List<V> values;

    /**
     * The collection of values of this map returned by {@link #values()}. 
     * Note that each <code>ListMap</code> has a single values collection. 
     */
    private final Collection<V> valuesColl;

    /**
     * The set of keys of this map returned by {@link #keySet()}. 
     * Note that each <code>ListMap</code> has a single key set. 
     */
    private final SortedSet<K> keysSet;

    /**
     * The set of entries of this map returned by {@link #entrySet()}. 
     * Note that each <code>ListMap</code> has a single entry set. 
     */
    private final SortedSet<Map.Entry<K, V>> entrySet;

    /* -------------------------------------------------------------------- *
     * constructors and auxiliary methods.                                  *
     * -------------------------------------------------------------------- */

    /**
     * Creates an empty list map with key set 'ordered as added' 
     * as described by {@link ListSet#sortedAsAdded()}. 
     */
    public ListMap() {
	this(ListSet.sortedAsAdded(), new ArrayList<V>());
    }

    /**
     * Creates an list map with the same key-value pairs as <code>map</code> 
     * and with ordering 'as added'. 
     * If no additional keys are added, this reflects the ordering 
     * given by iterating through the key set of <code>map</code>. 
     *
     * @see #map2keys(Map)
     * @see #map2values(Map)
     */
    public ListMap(Map<K, V> map) {
	this(map2keys(map), map2values(map));
    }

    /**
     * Returns the key set of <code>map</code> as a <code>ListSet</code> 
     * with ordering 'ordered as added' 
     * as given by {@link ListSet#sortedAsAdded()}. 
     * This ordering reflects the ordering 
     * given by the iteration on the key set of <code>map</code>. 
     */
    private static <K, V> ListSet<K> map2keys(Map<K, V> map) {
	ListSet<K> res = ListSet.sortedAsAdded();
	res.addAll(map.keySet());
	return res;
    }

    /**
     * Returns the value collection of <code>map</code> as a <code>List</code> 
     * with ordering 
     * given by the iteration on the key set of <code>map</code>. 
     * Note that there is no documentation **** 
     * that this ordering is the same as the one 
     * of the value collection {@link Map#values() map#values()}. **** 
     */
    private static <K, V> List<V> map2values(Map<K, V> map) {
	List<V> res = new ArrayList<V>();
	// **** using entrySet() would be more elegant, 
	// but seems not to guarantee an ordering of iteration. 
	for (K key : map.keySet()) {
	    res.add(map.get(key));
	}
	return res;
    }

    /**
     * Creates a list map defined by the given keys and values 
     * <code>keys</code> and <code>values</code>. 
     * A key and a value correspond iff their index coincides. 
     * Thus <code>keys</code> and <code>values</code> must have the same size. 
     * Note that the entries of <code>keys</code> differ pairwise, 
     * whereas this is not necessary for <code>values</code>. 
     *
     * @param keys
     *    the set of keys as a {@link ListSet}. 
     * @param values
     *    the collection of according values as a {@link List} 
     *    with the same size as <code>keys</code>. 
     * @throws IllegalArgumentException
     *    if <code>keys</code> and <code>values</code> differ in size. 
     */
    private ListMap(ListSet<K> keys, List<V> values) {
	if (keys.size() != values.size()) {
	    throw new IllegalArgumentException
		("Collection of keys and values must have same size. ");
	}

	this.keys       = keys;
	this.values     = values;
	this.valuesColl = new Values();
	this.keysSet    = new Keys();
	this.entrySet   = new Entries();
    }

    /* -------------------------------------------------------------------- *
     * Methods.                                                             *
     * -------------------------------------------------------------------- */

    public void clear() {
	this.keys  .clear();
	this.values.clear();
    }

    public boolean containsKey(Object key) {
	return this.keys.contains(key);
    }

    public boolean containsValue(Object val) {
	return this.values.contains(val);
    }

    public SortedSet<Map.Entry<K, V>> entrySet() {
	return this.entrySet;
    }

    public V get(Object key) {
	int idx = this.keys.getList().indexOf(key);
	if (idx == -1) {
	    return null;
	}
	assert idx >= 0 && idx < size();
	return this.values.get(idx);
    }

    public boolean isEmpty() {
	assert this.keys.isEmpty() == this.values.isEmpty();
	return this.keys.isEmpty();
    }

    public SortedSet<K> keySet() {
	return this.keysSet;
    }

    public V put(K key, V value) {
	int idx = this.keys.getList().indexOf(key);
	if (idx == -1) {
	    // Here, key is to be added 
	    this.keys.getList().add(key);
	    this.values.add(value);
	    return null;
	}
	assert idx >= 0 && idx < size();

	return this.values.set(idx, value);
    }

    public void putAll(Map<? extends K, ? extends V> other) {
	Iterator<? extends K> iter = other.keySet().iterator();
	K key;
	while (iter.hasNext()) {
	    key = iter.next();
	    this.put(key, other.get(key));
	}
    }

    public V remove(Object key) {
	int idx = this.keys.getList().indexOf(key);
	if (idx == -1) {
	    return null;
	}
	assert idx >= 0 && idx < size();
	V res = this.values.get(idx);
	this.keys.getList().remove(idx);
	this.values.remove(idx);
	return res;
    }

    private boolean removeIdx(int idx) {
	if (idx == -1) {
	    return false;
	}
	assert idx >= 0 && idx < size();
	this.keys.getList().remove(idx);
	this.values.remove(idx);
	return true;
    }

    public int size() {
	return this.keys.size();
    }

    public Comparator<? super K> comparator() {
	return this.keys.comparator();
    }

    public Collection<V> values() {
	return this.valuesColl;
    }

    // also throws correct exception 
    public K firstKey() {
	return keySet().first();
    }

   // also throws correct exception 
    public K lastKey() {
	return keySet().last();
    }

    // better removed because easy to mix up with other methods 
    // offering just a view, not a copy of part of this map 
    // public Map<K,V> subMap(Set<K> someKeys) {
    // 	V val;
    // 	ListMap<K, V> res = new ListMap<K, V>();
    // 	for (K key : someKeys) {
    // 	    val = get(key);
    // 	    res.put(key, val);
    // 	}
    // 	return res;
    // }

    public ListMap<K, V> headMap(K toKey) {
	return subMap(0, this.keys.obj2idx(toKey));
    }

    public ListMap<K, V> tailMap(K fromKey) {
 	return subMap(this.keys.obj2idx(fromKey), size());
    }


    @SuppressWarnings("checkstyle:genericwhitespace")
    ListMap<K, V> subMap(int fromIdx, int toIdx) {
	ListSet<K> subKeys   = this.keys  .subSetIdx(fromIdx, toIdx);
	List   <V> subValues = this.values.subList  (fromIdx, toIdx);

	return new ListMap<K, V>(subKeys, subValues);
    }

    public ListMap<K, V> subMap(K fromKey, K toKey) {
	return subMap(this.keys.obj2idx(fromKey), this.keys.obj2idx(toKey));
    }

    public boolean equals(Object obj) {
	if (!(obj instanceof Map)) {
	    return false;
	}

	Map<?, ?> other = (Map<?, ?>) obj;
	return this.entrySet().equals(other.entrySet());
    }

    public int hashCode() {
	return this.entrySet().hashCode();
    }

    public String toString() {
	StringBuffer result = new StringBuffer();
	result.append("<ListMap>\n");
	for (int i = 0; i < size(); i++) {
	    result.append("[" + this.keys.getList().get(i) 
			  + " => " + this.values.get(i) + "]\n");
	}
	result.append("</ListMap>\n");

	return result.toString();
    }

    public static void main(String[] args) {
	List<Integer> list = new ArrayList<Integer>();
	//list.add(0);
	list.iterator().remove();
    }
}
