package com.algo.linked.list;

public class ListNode<E> {
    E val;
    ListNode<E> prev;
    ListNode<E> next;

    ListNode(ListNode<E> prev, E val, ListNode<E> next) {
        this.val = val;
        this.prev = prev;
        this.next = next;
    }
}
