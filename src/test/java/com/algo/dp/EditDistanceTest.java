package com.algo.dp;

import com.algo.base.AlgorithmTestBase;
import com.algo.base.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditDistanceTest extends AlgorithmTestBase<EditDistanceCase> {

    private final EditDistance ed = new EditDistance();

    @Test
    void testMinDistance() {
        TestData<EditDistanceCase> td = loadTestData("com/algo/dp/edit-distance.yaml");
        verifyAll(td, tc -> {
            int result = ed.minDistance(tc.inputs().word1(), tc.inputs().word2());
            assertEquals(tc.expected(), result);
        });
    }

    @Test
    void testLongStrings() {
        String a = "abcdefghij";
        String b = "abcdefghij";
        assertEquals(0, ed.minDistance(a, b));

        String c = "abcdefghij";
        String d = "";
        assertEquals(10, ed.minDistance(c, d));
    }

}
