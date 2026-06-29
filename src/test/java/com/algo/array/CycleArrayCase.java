package com.algo.array;

import com.algo.base.TestCase;

public record CycleArrayCase(Inputs inputs, int expected) implements TestCase {
    public record Inputs(int capacity, String[] ops, int[][] args, String queryOp, int[] queryArg) {}
}
