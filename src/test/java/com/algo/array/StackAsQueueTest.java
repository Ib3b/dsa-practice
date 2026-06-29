package com.algo.array;

import com.algo.base.TestData;
import com.algo.base.AlgorithmTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StackAsQueueTest extends AlgorithmTestBase<StackAsQueueCase> {

    @Test
    void testStackAsQueue() {
        TestData<StackAsQueueCase> testData = loadTestData("com/algo/array/stack-as-queue.yaml");
        verifyAll(testData, tc -> {
            StackAsQueue q = new StackAsQueue();
            String[] ops = tc.inputs().ops();
            int[][] args = tc.inputs().args();

            for (int i = 0; i < ops.length; i++) {
                applyOp(q, ops[i], args[i]);
            }

            int result = applyOp(q, tc.inputs().queryOp(), tc.inputs().queryArg());
            assertEquals(tc.expected(), result);
        });
    }

    private static int applyOp(StackAsQueue q, String op, int[] args) {
        return switch (op) {
            case "push"  -> { q.push(args[0]); yield 0; }
            case "pop"   -> q.pop();
            case "peek"  -> q.peek();
            case "empty" -> q.empty() ? 1 : 0;
            default -> throw new IllegalArgumentException("Unknown op: " + op);
        };
    }
}
