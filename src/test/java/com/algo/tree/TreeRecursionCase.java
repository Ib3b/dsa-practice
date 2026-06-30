package com.algo.tree;

import com.algo.base.TestCase;

public record TreeRecursionCase(Inputs inputs, int expected) implements TestCase {
    public record Inputs(Integer[] tree, String algorithm) {}
}
