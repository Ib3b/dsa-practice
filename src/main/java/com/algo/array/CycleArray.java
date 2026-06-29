package com.algo.array;

/**
 * 环形数组（定长）— 支持在头部和尾部高效插入/删除。
 * 当数组满时再插入会抛出异常，调用前应通过 isFull() 检查。
 */
public class CycleArray {
    private final int[] data;
    private int start;
    private int count;

    public CycleArray(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        data = new int[capacity];
        start = 0;
        count = 0;
    }

    /**
     * 头部插入 — 将新元素放在所有现有元素之前
     */
    public void addFirst(int val) {
        if (isFull()) throw new IllegalStateException("Array is full");
        start = (start - 1 + data.length) % data.length;
        data[start] = val;
        count++;
    }

    /**
     * 尾部追加 — 将新元素放在所有现有元素之后
     */
    public void addLast(int val) {
        if (isFull()) throw new IllegalStateException("Array is full");
        int idx = (start + count) % data.length;
        data[idx] = val;
        count++;
    }

    /**
     * 移除并返回头部元素
     */
    public int removeFirst() {
        if (isEmpty()) throw new IllegalStateException("Array is empty");
        int val = data[start];
        start = (start + 1) % data.length;
        count--;
        return val;
    }

    /**
     * 移除并返回尾部元素
     */
    public int removeLast() {
        if (isEmpty()) throw new IllegalStateException("Array is empty");
        int idx = (start + count - 1 + data.length) % data.length;
        int val = data[idx];
        count--;
        return val;
    }

    /**
     * 按逻辑索引读取（0 = 第一个元素）
     */
    public int get(int index) {
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException();
        return data[(start + index) % data.length];
    }

    /**
     * 按逻辑索引更新（0 = 第一个元素）
     */
    public void set(int index, int val) {
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException();
        data[(start + index) % data.length] = val;
    }

    /**
     * 只读快照 — 按逻辑顺序返回所有元素
     */
    public int[] toArray() {
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = data[(start + i) % data.length];
        }
        return result;
    }

    public int size() {
        return count;
    }

    public int capacity() {
        return data.length;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == data.length;
    }
}
