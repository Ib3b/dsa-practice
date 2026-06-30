package com.algo.linked.list;

public class DoublyList {
    private DoublyListNode head, tail;
    private int size;

    public DoublyList() {
        head = new DoublyListNode(0, 0);
        tail = new DoublyListNode(0, 0);
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    public void addLast(DoublyListNode node) {
        DoublyListNode secondToLast = tail.prev;
        secondToLast.next = node;
        node.prev = secondToLast;
        node.next = tail;
        tail.prev = node;
        size++;
    }

    public void remove(DoublyListNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    public DoublyListNode removeFirst() {
        DoublyListNode first = head.next;
        if (first == tail) {
            return null;
        }
        remove(first);
        return first;
    }

    public int size() {
        return size;
    }
}
