package com.algo.array;

import com.algo.base.TestData;
import com.algo.base.AlgorithmTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BinarySearchTest extends AlgorithmTestBase<BinarySearchCase> {

    @Test
    void testSearch() {
        TestData<BinarySearchCase> testData = loadTestData("com/algo/array/binary-search.yaml");
        BinarySearch bs = new BinarySearch();
        verifyAll(testData, tc -> {
            int result = bs.search(tc.inputs().nums(), tc.inputs().target());
            assertEquals(tc.expected()[0], result);
        });
    }

    @Test
    void testLeftBound() {
        TestData<BinarySearchCase> testData = loadTestData("com/algo/array/binary-search.yaml");
        BinarySearch bs = new BinarySearch();
        verifyAll(testData, tc -> {
            int result = bs.searchLeftBound(tc.inputs().nums(), tc.inputs().target());
            assertEquals(tc.expected()[1], result);
        });
    }

    @Test
    void testRightBound() {
        TestData<BinarySearchCase> testData = loadTestData("com/algo/array/binary-search.yaml");
        BinarySearch bs = new BinarySearch();
        verifyAll(testData, tc -> {
            int result = bs.searchRightBound(tc.inputs().nums(), tc.inputs().target());
            assertEquals(tc.expected()[2], result);
        });
    }
}
