package com.algo.dp;

import com.algo.base.AlgorithmTestBase;
import com.algo.base.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoinChangeTest extends AlgorithmTestBase<CoinChangeCase> {

    private final CoinChange cc = new CoinChange();

    @Test
    void testCoinChange() {
        TestData<CoinChangeCase> td = loadTestData("com/algo/dp/coin-change.yaml");
        verifyAll(td, tc -> {
            int result = cc.coinChange(tc.inputs().coins(), tc.inputs().amount());
            assertEquals(tc.expected(), result);
        });
    }

    @Test
    void testLargeAmount() {
        assertEquals(-1, cc.coinChange(new int[]{2}, 1_000_001));
    }

}
