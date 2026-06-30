package com.algo.recursion;

import com.algo.base.TestCase;

public record RecursiveSortCase(Inputs inputs, int[] expected) implements TestCase {
    public record Inputs(int[] arr) {}
}
