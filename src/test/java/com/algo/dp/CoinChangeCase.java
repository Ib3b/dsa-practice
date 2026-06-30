package com.algo.dp;

import com.algo.base.TestCase;

public record CoinChangeCase(Inputs inputs, int expected) implements TestCase {
    public record Inputs(int[] coins, int amount) {}
}
