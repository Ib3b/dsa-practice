# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test

```bash
mvn compile         # build production code
mvn test            # run all tests
mvn test -q         # quiet — only show failures
mvn test -Dtest=CycleArrayTest   # single test class
```

## Project Structure

```
src/main/java/com/algo/
├── array/       BinarySearch, CycleArray, Difference, MergeIntervals, NumArray, TwoSum
└── linked/list/ ListNode

src/test/java/com/algo/
├── base/        TestCase, TestData, AlgorithmTestBase — YAML-driven test framework
└── array/       One *Case.java + one *Test.java per algorithm

src/test/resources/com/algo/array/   — YAML test data (one per algorithm)
```

## Test Framework

Every algorithm gets 3 artifacts:

1. **`*Case.java`** — A record implementing `TestCase`, with nested `Inputs` record:
   ```java
   public record XxxCase(Inputs inputs, int[] expected) implements TestCase {
       public record Inputs(int[] nums, int target) {}
   }
   ```

2. **`*Test.java`** — Extends `AlgorithmTestBase<XxxCase>`, loads YAML, calls `verifyAll()`:
   ```java
   class XxxTest extends AlgorithmTestBase<XxxCase> {
       @Test
       void testFoo() {
           var data = loadTestData("com/algo/array/foo.yaml");
           verifyAll(data, tc -> { ... assertEquals(tc.expected(), result); });
       }
   }
   ```

3. **YAML test data** — Two supported formats:
   - **Full format** (num-array.yaml): key-value cases with `inputs: { ... }` + `expected: ...`
   - **Compact format** (all others): positional arrays with a `columns:` block matching the record fields; `expected` is implicitly the last column

`AlgorithmTestBase.verifyAll()` prints `X/Y passed` and continues on failure, then fails the test if any case didn't pass.

## Adding a New Algorithm

1. Create implementation in `main/`
2. Create `*Case.java` record in `test/` — note the `expected` field type must match what the algorithm returns
3. Create YAML test data — compact format preferred for new tests
4. Create `*Test.java` extending `AlgorithmTestBase`
5. `mvn test -Dtest=NewAlgoTest` to verify

## Dependencies

- Java 25, JUnit 5.12, Jackson 2.18 (databind + yaml, test scope only)
- No production dependencies beyond JDK
