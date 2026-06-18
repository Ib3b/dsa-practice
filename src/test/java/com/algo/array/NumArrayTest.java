package com.algo.array;

import com.algo.base.TestData;
import com.algo.base.AlgorithmTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NumArrayTest extends AlgorithmTestBase<NumArrayCase> {

    @Test
    void testSumRange() {
        TestData<NumArrayCase> testData = loadTestData("com/algo/array/num-array.yaml");
        verifyAll(testData, tc -> {
            NumArray na = new NumArray(tc.inputs().nums());
            int result = na.sumRange(tc.inputs().left(), tc.inputs().right());
            assertEquals(tc.expected(), result);
        });
    }
}
