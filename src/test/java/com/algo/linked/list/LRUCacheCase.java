package com.algo.linked.list;

import com.algo.base.TestCase;

public record LRUCacheCase(Inputs inputs, int expected) implements TestCase {
    public record Inputs(int capacity, String[] ops, int[][] args, String queryOp, int[] queryArg) {}
}
