package com.algo.tree;

import com.algo.base.TestCase;

public record TreeTraversalCase(Inputs inputs, int[] expected) implements TestCase {
    public record Inputs(Integer[] tree, String traversalType) {}
}
