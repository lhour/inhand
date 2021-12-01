package com.hour.v1;

public class SkipListNode<K extends Comparable<K>,V> {
    
    // data
    public K key;
    public V value;

    // links
    public SkipListNode<K,V> up;
    public SkipListNode<K,V> down;
    public SkipListNode<K,V> left;
    public SkipListNode<K,V> right;

    // constructor
    public SkipListNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        if(key == null){
            return " ";
        }
        return key.toString()+"";
    }
}
