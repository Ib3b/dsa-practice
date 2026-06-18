package com.algo.base;

import java.util.List;

public record TestData<C extends TestCase>(
    String algorithm,
    String type,
    String description,
    List<C> cases
) {
}
