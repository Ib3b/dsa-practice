package com.algo.array;

import com.algo.base.TestData;
import com.algo.base.AlgorithmTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TwoSumTest extends AlgorithmTestBase<TwoSumCase> {

    @Test
    void testTwoSum() {
        TestData<TwoSumCase> testData = loadTestData("com/algo/array/two-sum.yaml");
        TwoSum solver = new TwoSum();
        for (TwoSumCase tc : testData.cases()) {
            int[] result = solver.twoSum(tc.inputs().nums(), tc.inputs().target());
            assertArrayEquals(tc.expected(), result);
        }
    }
}
