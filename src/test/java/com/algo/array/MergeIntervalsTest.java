package com.algo.array;

import com.algo.base.TestData;
import com.algo.base.AlgorithmTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MergeIntervalsTest extends AlgorithmTestBase<MergeIntervalsCase> {

    @Test
    void testMerge() {
        TestData<MergeIntervalsCase> testData = loadTestData("com/algo/array/merge-intervals.yaml");
        MergeIntervals solver = new MergeIntervals();
        verifyAll(testData, tc -> {
            int[][] result = solver.merge(tc.inputs().intervals());
            assertArrayEquals(tc.expected(), result);
        });
    }
}
