package com.algo.dp;

import com.algo.base.TestCase;

public record BackPackValueCase(Inputs inputs, int expected) implements TestCase {
    public record Inputs(int capacity, int[] wt, int[] val) {}
}
