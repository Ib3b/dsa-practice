package com.algo.recursion;

import com.algo.base.AlgorithmTestBase;
import com.algo.base.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecursiveSortTest extends AlgorithmTestBase<RecursiveSortCase> {

    private final RecursiveSort sorter = new RecursiveSort();

    @Test
    void testQuickSort() {
        TestData<RecursiveSortCase> td = loadTestData("com/algo/recursion/recursive-sort.yaml");
        verifyAll(td, tc -> {
            int[] a = tc.inputs().arr().clone();
            sorter.quickSort(a);
            assertArrayEquals(tc.expected(), a);
        });
    }

    @Test
    void testMergeSort() {
        TestData<RecursiveSortCase> td = loadTestData("com/algo/recursion/recursive-sort.yaml");
        verifyAll(td, tc -> {
            int[] a = tc.inputs().arr().clone();
            sorter.mergeSort(a);
            assertArrayEquals(tc.expected(), a);
        });
    }

}
