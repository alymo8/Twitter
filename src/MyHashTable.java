
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;


public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
	// num of entries to the table
	private int numEntries;
	// num of buckets 
	private int numBuckets;
	// load factor needed to check for rehashing 
	private static final double MAX_LOAD_FACTOR = 0.75;
	// ArrayList of buckets. Each bucket is a LinkedList of HashPair
	private ArrayList<LinkedList<HashPair<K,V>>> buckets; 

	
	
	// constructor
	public MyHashTable(int initialCapacity) {

		this.numEntries = 0;
		this.numBuckets = initialCapacity;
		this.buckets = new ArrayList<LinkedList<HashPair<K,V>>>();
		//LinkedList<HashPair<K,V>> l = new LinkedList<HashPair<K,V>>();
		for(int i = 0; i< numBuckets; i++) {
		//	for(LinkedList<HashPair<K,V>> l: buckets) {
			buckets.add(new LinkedList<HashPair<K,V>>()) ;
		}
	}

	
	public int size() {
		return this.numEntries;
	}

	public boolean isEmpty() {
		return this.numEntries == 0;
	}

	public int numBuckets() {
		return this.numBuckets;
	}

	/**
	 * Returns the buckets variable. Useful for testing  purposes.
	 */
	public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){
		return this.buckets;
	}

	/**
	 * Given a key, return the bucket position for the key. 
	 */
	public int hashFunction(K key) {
		if(numBuckets!=0) {
			int hashValue = Math.abs(key.hashCode())%this.numBuckets;
			return hashValue;
		}
		return -1;
	}
	/**
	 * Takes a key and a value as input and adds the corresponding HashPair
	 * to this HashTable. Expected average run time  O(1)
	 */
	
    public V put(K key, V value) {
        //  ADD YOUR CODE BELOW HERE
    	if(size()+1>=(double) numBuckets*MAX_LOAD_FACTOR) rehash();
    	HashPair<K,V> p = new HashPair<K, V>(key, value);
    	//System.out.println(buckets.get(hashFunction(key)).size());
    	if(getBuckets().get(hashFunction(key)).size()==0) {
    		
    		buckets.get(hashFunction(key)).add(p);
    		numEntries++;
    		return null;
    	}
    	else {
    		for(int i = 0; i< getBuckets().get(hashFunction(key)).size(); i++) {
    			if(buckets.get(hashFunction(key)).get(i).getKey().equals(key)) {
    				if(buckets.get(hashFunction(key)).get(i).getValue()!=null) {
    					V v = buckets.get(hashFunction(key)).get(i).getValue();
    					buckets.get(hashFunction(key)).get(i).setValue(value);
    					//numEntries+=1;
    					return v;
    				}
    			}    			
			}
		buckets.get(hashFunction(key)).add(p);
		numEntries ++;
		return null;
		}
    }
    			


	/**
	 * Get the value corresponding to key. Expected average runtime O(1)
	 */

	public V get(K key) {
		if(buckets.get(hashFunction(key)).size()==0) return null;
		else {
			for(HashPair<K,V> p: buckets.get(hashFunction(key))) {
				if(p.getKey().equals(key)) return p.getValue();
			}
			return null;
		}
	}

	/**
	 * Remove the HashPair corresponding to key . Expected average runtime O(1) 
	 */
	public V remove(K key) {
		int toR  =0;
		//	if ((hashFunction(key)>= 0)  &&  (hashFunction(key)< numBuckets()) ){
		if(buckets.get(hashFunction(key)).size()==0) return null;
		
		else {
		//for(HashPair<K,V> p : buckets.get(hashFunction(key))) {
			for(int i =0;i<buckets.get(hashFunction(key)).size();i++){
				if(buckets.get(hashFunction(key)).get(i).getKey().equals(key)) {
					//buckets.get(hashFunction(key)).remove(i);
					numEntries--;
					return buckets.get(hashFunction(key)).remove(i).getValue();
					//return 
					//remove(buckets.get(hashFunction(key)).get(i));
				}
			}
			return null;
		}
	}
			

	


	/** 
	 * Method to double the size of the hashtable if load factor increases
	 * beyond MAX_LOAD_FACTOR.
	 * Made public for ease of testing.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */
	public void rehash() {
		numBuckets*=2;
		MyHashTable<K,V> t = new MyHashTable<K,V>(numBuckets());

		for(int i = 0; i<numBuckets()/2; i++) {
			for(int j = 0; j< buckets.get(i).size(); j++)
			//for(HashPair<K,V> p : buckets.get(j))
				t.put(buckets.get(i).get(j).getKey(), buckets.get(i).get(j).getValue());
				//t.put(p.getKey(), p.getValue());
		}
		this.buckets = t.getBuckets();
	}
		



	/**
	 * Return a list of all the keys present in this hashtable.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */

	public ArrayList<K> keys() {
		ArrayList<K> k = new ArrayList<K>(7);

			for(int i = 0; i<buckets.size();i++) {
				for(int j = 0; j<buckets.get(i).size(); j++) {
				k.add(buckets.get(i).get(j).getKey());
				}
			}
			return k;
		}

			
	

	/**
	 * Returns an ArrayList of unique values present in this hashtable.
	 * Expected average runtime is O(m) where m is the number of buckets
	 */
	public ArrayList<V> values() {
		//ADD CODE BELOW HERE
		//ArrayList<V> v = new ArrayList<V>();
		MyHashTable<V, Integer> s = new MyHashTable <V, Integer>(7);
		for(int i = 0; i<buckets.size();i++) {
			for(int j = 0; j<buckets.get(i).size(); j++) {
		//	k.add(buckets.get(i).get(j).getKey());
				s.put(buckets.get(i).get(j).getValue(), 0);
			}
		}
		return s.keys();
	}



	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
	public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
		ArrayList<K> sortedResults = new ArrayList<>();
		for (HashPair<K, V> entry : results) {
			V element = entry.getValue();
			K toAdd = entry.getKey();
			int i = sortedResults.size() - 1;
			V toCompare = null;
			while (i >= 0) {
				toCompare = results.get(sortedResults.get(i));
				if (element.compareTo(toCompare) <= 0 )
					break;
				i--;
			}
			sortedResults.add(i+1, toAdd);
		}
		return sortedResults;
	}
	

		
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
	public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
		ArrayList<K> k = results.keys();
		sort(k, results);
		return k;
	}
	
	private static <K, V extends Comparable<V>> void sort(ArrayList<K> k, MyHashTable<K, V> results) {
		int n =k.size();
		if(n<2) return;
		int mid = n/2;
		ArrayList<K> l = new ArrayList<K>();
		ArrayList<K> r = new ArrayList<K>();
		for(int i = 0; i< mid; i++) {
			l.add(k.get(i));
		} for(int i = mid; i<n; i++) {
			r.add(k.get(i));
		}
		sort(l, results);
		sort(r, results);
		merge(k, l, r, results);
	}
	private static <K, V extends Comparable<V>> void merge(ArrayList<K> k, ArrayList<K> l, ArrayList<K> r, MyHashTable<K, V> results) {
		int i=0;
		int j=0;
		int m=0;
		int li = l.size();
		int ri =r.size();
		
		while(i < li && j<ri) {
			V right = results.get(r.get(j));
			V left = results.get(l.get(i));
			if(left.compareTo(right)>0) {
				k.set(m++, l.get(i++));				
			}
			else {
				k.set(m++, r.get(j++));				
			}
		}
		while(i<li) {
			k.set(m++, l.get(i++));
		}
		while(j<ri) {
			k.set(m++, r.get(j++));
		}
	}



	   @Override
	public MyHashIterator iterator() {
		return new MyHashIterator();
	}   
	    
	private class MyHashIterator implements Iterator<HashPair<K,V>> {
		HashPair<K,V> p;
		int index;
		ArrayList<HashPair<K,V>> iter;

    	/**
    	 * Expected average runtime is O(m) where m is the number of buckets
    	 */
		private MyHashIterator() {
    	//ArrayList<HashPair<K,V>> iterList = new ArrayList<HashPair<K,V>>();
    	iter = new ArrayList<HashPair<K,V>>();
		 for(int i = 0; i <buckets.size();i++) {
			 for(int j = 0; j<buckets.get(i).size(); j++) {
				 iter.add(buckets.get(i).get(j));
			 }
		 }
    	//this.iterList = orderedList(iterList);
    	this.index = 0;
    	this.p = iter.get(0);
		}
		

		@Override
        /**
         * Expected average runtime is O(1)
         */
		public boolean hasNext() {
			return p!=null;
		}
		
		@Override
		/**
		 * Expected average runtime is O(1)
		 */
		public HashPair<K,V> next() {
			//ADD YOUR CODE BELOW HERE
			HashPair<K,V> temp = this.p ; 
			try {
			if (index ==iter.size()-1) {
				this.p = null;
			} else {
				this.p = this.iter.get(this.index+1);
			}

			index++;
		 return temp;
		 
			}catch(IndexOutOfBoundsException e) {
				
				throw new NoSuchElementException();
			}
		
		}
		
	}
}
