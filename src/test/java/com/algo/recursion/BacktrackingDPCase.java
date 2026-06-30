package com.algo.recursion;

import com.algo.base.TestCase;

public record BacktrackingDPCase(Inputs inputs, int expected) implements TestCase {
    public record Inputs(String algorithm, int n, int[] coins, int amount, int[] nums) {}
}
