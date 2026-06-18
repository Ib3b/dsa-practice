package com.algo.array;

import com.algo.base.TestCase;

public record MergeIntervalsCase(Inputs inputs, int[][] expected) implements TestCase {
    public record Inputs(int[][] intervals) {}
}
