package com.algo.calculator;

import com.algo.base.TestCase;

public record CalculatorCase(Inputs inputs, int expected) implements TestCase {
    public record Inputs(String expression) {}
}
