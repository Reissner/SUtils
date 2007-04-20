

package eu.simuline.util;


import java.util.Set;
import java.util.Collection;
import java.util.SortedMap;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Iterator;

/**
 * A {@link SortedMap} with ordering 
 * given by the ordering by which the keys are added. 
 * **** Possibly this can be replaced by LinkedHashMap!!!!
 *
 * @author <a href="mailto:e.reissner@rose.de">Ernst Reissner</a>
 * @version 1.0
 */
public class ListMap<K,V> implements SortedMap<K,V> {

    /* -------------------------------------------------------------------- *
     * Fields.                                                              *
     * -------------------------------------------------------------------- */

    ArraySet<K> keys;

    Map<K,V> map;

    /* -------------------------------------------------------------------- *
     * Constructors.                                                        *
     * -------------------------------------------------------------------- */

    public ListMap() {
	this.keys = ArraySet.sortedAsAdded();
	this.map  = new HashMap<K,V>();
    }

    public ListMap(Map<K,V> map) {
	this.keys = ArraySet.sortedAsAdded();
	this.keys.addAll(map.keySet());
	this.map = map;
    }

    private ListMap(ArraySet<K> keys,Map<K,V> map) {
	this.keys = keys;
	this.map  = map;
    }

    /* -------------------------------------------------------------------- *
     * Methods.                                                             *
     * -------------------------------------------------------------------- */

    public void clear() {
	this.keys.clear();
	this.map.clear();
    }

    public boolean containsKey(Object key) {
	return this.map.containsKey(key);
    }

    public boolean containsValue(Object key) {
	return this.map.containsValue(key);
    }

    public Set<Map.Entry<K,V>> entrySet() {
	return this.map.entrySet();
    }

    public V get(Object key) {
	return this.map.get(key);
    }

    public boolean isEmpty() {
	return this.map.isEmpty();
    }

    public Set<K> keySet() {
	return this.keys;
    }

    public V put(K key, V value) {
	this.keys.add(key);
	return this.map.put(key,value);
    }

    public void putAll(Map<? extends K,? extends V> other) {
	Iterator<? extends K> iter = other.keySet().iterator();
	K key;
	while (iter.hasNext()) {
	    key = iter.next();
	    this.put(key,other.get(key));
	}
    }

    public V remove(Object key) {
	this.keys.remove(key);
	return this.map.remove(key);
    }

    public int size() {
	return this.map.size();
    }

    public Comparator<? super K> comparator() {
	return this.keys.comparator();
    }

    public Collection<V> values() {
	return this.map.values();
    }

    public K firstKey() {
	return keySet().iterator().next();
    }

    public K lastKey() {
	if (this.isEmpty()) {
	    throw new NoSuchElementException();
	}
	
	return this.keys.getList().get(this.keys.size()-1);
    }

    public Map<K,V> subMap(Set<K> someKeys) {
	Iterator<K> iter = someKeys.iterator();
	Map<K,V> map = new HashMap<K,V>();
	K key = iter.next();
	while (iter.hasNext()) {
	    map.put(key,this.map.get(key));
	}
	return map;
    }

    public SortedMap<K,V> headMap(K toKey) {
	ArraySet<K> headKeys = (ArraySet<K>)keys.headSet(toKey);
	return new ListMap<K,V>(headKeys,subMap(headKeys));
    }

    public SortedMap<K,V> tailMap(K fromKey) {
	ArraySet<K> headKeys = (ArraySet<K>)keys.tailSet(fromKey);
	return new ListMap<K,V>(headKeys,subMap(headKeys));
    }

    public SortedMap<K,V> subMap(K fromKey, K toKey) {
	ArraySet<K> headKeys = (ArraySet<K>)keys.subSet(fromKey,toKey);
	return new ListMap<K,V>(headKeys,subMap(headKeys));
    }

    public String toString() {
	StringBuffer result = new StringBuffer(40);
	result.append("<ListMap>\n");
	Object value;
	for (Object key : this.keys) {
	    value = this.map.get(key);
	    result.append("[" + key + " => " + value + "]\n");
	}
	result.append("</ListMap>\n");

	return result.toString();
    }
}
