package com.algo.array;

import java.util.Stack;

public class StackAsQueue {
    private Stack<Integer> head;
    private Stack<Integer> tail;

    public StackAsQueue() {
        head = new Stack<>();
        tail = new Stack<>();
    }

    public void push(int val) {
        tail.push(val);
    }

    public int pop() {
        peek();
        return head.pop();
    }

    public int peek() {
        if (head.isEmpty()) {
            while (!tail.isEmpty()) {
                head.push(tail.pop());
            }
        }
        return head.peek();
    }

    public boolean empty() {
        return head.isEmpty() && tail.isEmpty();
    }
}
