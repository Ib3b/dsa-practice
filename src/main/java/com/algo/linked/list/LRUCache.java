package com.algo.linked.list;

import java.util.HashMap;

public class LRUCache {
    private final HashMap<Integer, DoublyListNode> map;
    private final DoublyList cache;
    private final int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>(capacity);
        cache = new DoublyList();
    }

    private void makeRecently(int key) {
        DoublyListNode node = map.get(key);
        if (node == null) {
            return;
        }
        cache.remove(node);
        cache.addLast(node);
    }


    private void addToRecently(int key, int val) {
        DoublyListNode node = new DoublyListNode(key, val);
        map.put(key, node);
        cache.addLast(node);
    }

    private void deleteKey(int key) {
        DoublyListNode node = map.get(key);
        if (node == null) {
            return;
        }
        cache.remove(node);
        map.remove(key);
    }

    private void removeLeastRecently() {
        DoublyListNode leastUnUsed = cache.removeFirst();
        if (leastUnUsed == null) {
            return;
        }
        map.remove(leastUnUsed.key);
    }

    public void put(int key, int val) {
        if (map.containsKey(key)) {
            deleteKey(key);
            addToRecently(key, val);
            return;
        }
        if (cache.size() == capacity) {
            removeLeastRecently();
        }
        addToRecently(key, val);
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        makeRecently(key);
        return  map.get(key).val;
    }
}
