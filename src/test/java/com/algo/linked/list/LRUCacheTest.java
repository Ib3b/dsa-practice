package com.algo.linked.list;

import com.algo.base.TestData;
import com.algo.base.AlgorithmTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LRUCacheTest extends AlgorithmTestBase<LRUCacheCase> {

    @Test
    void testLRUCache() {
        TestData<LRUCacheCase> testData = loadTestData("com/algo/linked/list/lru-cache.yaml");
        verifyAll(testData, tc -> {
            LRUCache cache = new LRUCache(tc.inputs().capacity());
            String[] ops = tc.inputs().ops();
            int[][] args = tc.inputs().args();

            for (int i = 0; i < ops.length; i++) {
                applyOp(cache, ops[i], args[i]);
            }

            int result = applyOp(cache, tc.inputs().queryOp(), tc.inputs().queryArg());
            assertEquals(tc.expected(), result);
        });
    }

    private static int applyOp(LRUCache cache, String op, int[] args) {
        return switch (op) {
            case "put" -> { cache.put(args[0], args[1]); yield 0; }
            case "get" -> cache.get(args[0]);
            default -> throw new IllegalArgumentException("Unknown op: " + op);
        };
    }
}
