package com.algo.array;

import com.algo.base.TestCase;

public record BinarySearchCase(Inputs inputs, int[] expected) implements TestCase {
    public record Inputs(int[] nums, int target) {}
}
