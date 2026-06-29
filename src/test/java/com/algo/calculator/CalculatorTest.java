package com.algo.calculator;

import com.algo.base.TestData;
import com.algo.base.AlgorithmTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest extends AlgorithmTestBase<CalculatorCase> {

    @Test
    void testCalculate() {
        TestData<CalculatorCase> testData = loadTestData("com/algo/calculator/calculator.yaml");
        verifyAll(testData, tc -> {
            int result = Calculator.calculate(tc.inputs().expression());
            assertEquals(tc.expected(), result);
        });
    }

    @Test
    void testPrintAstSamples() {
        System.out.println("=== AST 二叉树示例 ===");
        for (String expr : new String[]{"1+2", "1+2*3", "(1+2)*3", "-3+2", "-(3+2)"}) {
            System.out.println(expr + " = " + Calculator.calculate(expr));
            Calculator.printAst(expr);
        }
    }
}
