package com.algo.array;

import com.algo.base.TestData;
import com.algo.base.AlgorithmTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CycleArrayTest extends AlgorithmTestBase<CycleArrayCase> {

    @Test
    void testCycleArray() {
        TestData<CycleArrayCase> testData = loadTestData("com/algo/array/cycle-array.yaml");
        verifyAll(testData, tc -> {
            CycleArray ca = new CycleArray(tc.inputs().capacity());
            String[] ops = tc.inputs().ops();
            int[][] args = tc.inputs().args();

            for (int i = 0; i < ops.length; i++) {
                applyOp(ca, ops[i], args[i]);
            }

            int result = applyOp(ca, tc.inputs().queryOp(), tc.inputs().queryArg());
            assertEquals(tc.expected(), result);
        });
    }

    private static int applyOp(CycleArray ca, String op, int[] args) {
        return switch (op) {
            case "addLast"     -> { ca.addLast(args[0]);  yield 0; }
            case "addFirst"    -> { ca.addFirst(args[0]); yield 0; }
            case "removeFirst" -> ca.removeFirst();
            case "removeLast"  -> ca.removeLast();
            case "get"         -> ca.get(args[0]);
            case "set"         -> { ca.set(args[0], args[1]); yield 0; }
            case "size"        -> ca.size();
            case "isEmpty"     -> ca.isEmpty() ? 1 : 0;
            case "isFull"      -> ca.isFull() ? 1 : 0;
            default -> throw new IllegalArgumentException("Unknown op: " + op);
        };
    }
}
