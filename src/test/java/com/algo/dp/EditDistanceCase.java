package com.algo.dp;

import com.algo.base.TestCase;

public record EditDistanceCase(Inputs inputs, int expected) implements TestCase {
    public record Inputs(String word1, String word2) {}
}
