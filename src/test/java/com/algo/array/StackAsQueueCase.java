package com.algo.array;

import com.algo.base.TestCase;

public record StackAsQueueCase(Inputs inputs, int expected) implements TestCase {
    public record Inputs(String[] ops, int[][] args, String queryOp, int[] queryArg) {}
}
