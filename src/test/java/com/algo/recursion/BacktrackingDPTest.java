package com.algo.recursion;

import com.algo.base.AlgorithmTestBase;
import com.algo.base.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BacktrackingDPTest extends AlgorithmTestBase<BacktrackingDPCase> {

    private final BacktrackingDP bdp = new BacktrackingDP();

    @Test
    void testDPAlgorithms() {
        TestData<BacktrackingDPCase> td = loadTestData("com/algo/recursion/backtracking-dp.yaml");
        verifyAll(td, tc -> {
            var in = tc.inputs();
            int result = switch (in.algorithm()) {
                case "fib"          -> bdp.fib(in.n());
                case "coinChange"   -> bdp.coinChange(in.coins(), in.amount());
                case "lis"          -> bdp.lengthOfLIS(in.nums());
                default -> throw new IllegalArgumentException(in.algorithm());
            };
            assertEquals(tc.expected(), result);
        });
    }

    // ========== 回溯（inline 测试，输出为集合类型） ==========

    @Test
    void testSubsets() {
        var res = bdp.subsets(new int[]{1, 2, 3});
        assertEquals(8, res.size());  // 2^n

        assertTrue(res.contains(List.of()));
        assertTrue(res.contains(List.of(1)));
        assertTrue(res.contains(List.of(2)));
        assertTrue(res.contains(List.of(3)));
        assertTrue(res.contains(List.of(1, 2, 3)));
    }

    @Test
    void testPermute() {
        var res = bdp.permute(new int[]{1, 2, 3});
        assertEquals(6, res.size());  // n!

        assertTrue(res.contains(List.of(1, 2, 3)));
        assertTrue(res.contains(List.of(3, 2, 1)));
    }

    @Test
    void testNQueens() {
        var res4 = bdp.solveNQueens(4);
        assertEquals(2, res4.size());

        var res1 = bdp.solveNQueens(1);
        assertEquals(1, res1.size());
        assertEquals("Q", res1.get(0).get(0));
    }

}
