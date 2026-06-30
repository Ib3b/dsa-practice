package com.algo.dp;

import com.algo.base.AlgorithmTestBase;
import com.algo.base.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BackPackValueTest extends AlgorithmTestBase<BackPackValueCase> {

    private final BackPackValue bp = new BackPackValue();

    @Test
    void testValue() {
        TestData<BackPackValueCase> td = loadTestData("com/algo/dp/backpack-value.yaml");
        verifyAll(td, tc -> {
            int result = bp.value(tc.inputs().capacity(), tc.inputs().wt(), tc.inputs().val());
            assertEquals(tc.expected(), result);
        });
    }

}
