package com.algo.array;

import com.algo.base.TestData;
import com.algo.base.AlgorithmTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DifferenceTest extends AlgorithmTestBase<DifferenceCase> {

    @Test
    void testDifference() {
        TestData<DifferenceCase> testData = loadTestData("com/algo/array/difference.yaml");
        verifyAll(testData, tc -> {
            Difference d = new Difference(tc.inputs().nums());
            for (int[] op : tc.inputs().ops()) {
                d.increment(op[0], op[1], op[2]);
            }
            assertArrayEquals(tc.expected(), d.result());
        });
    }
}
