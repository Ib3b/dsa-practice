# DSA Practice Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a Java project for practicing data structures and algorithms, with YAML-based test data and automatic deserialization through a reusable base class.

**Architecture:** A Maven project with `AlgorithmTestBase<C>` abstract class that reads YAML test data, resolves the concrete TestCase type via the `type` field, and deserializes cases automatically. Each algorithm gets its own implementation class + TestCase Record + YAML file + test class.

**Tech Stack:** Java 21, Maven, JUnit 5.12, Jackson 2.18 + jackson-dataformat-yaml

---

### Task 1: Project scaffold

**Files:**
- Create: `pom.xml`
- Create: directory structure for `src/main/java/com/algo/array/`, `src/test/java/com/algo/array/`, `src/test/java/com/algo/base/`, `src/test/resources/com/algo/array/`

- [ ] **Step 1: Create pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.algo</groupId>
    <artifactId>dsa-practice</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.18.3</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-yaml</artifactId>
            <version>2.18.3</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.12.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <parameters>true</parameters>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: Create directory structure**

```bash
mkdir -p src/main/java/com/algo/array
mkdir -p src/test/java/com/algo/array
mkdir -p src/test/java/com/algo/base
mkdir -p src/test/resources/com/algo/array
```

- [ ] **Step 3: Verify Maven resolves dependencies**

Run: `mvn dependency:resolve`
Expected: BUILD SUCCESS, all 3 dependencies downloaded

---

### Task 2: Framework base types

**Files:**
- Create: `src/test/java/com/algo/base/TestCase.java`
- Create: `src/test/java/com/algo/base/TestData.java`
- Create: `src/test/java/com/algo/base/AlgorithmTestBase.java`

- [ ] **Step 1: Create TestCase marker interface**

```java
package com.algo.base;

public interface TestCase {
}
```

- [ ] **Step 2: Create TestData generic record**

```java
package com.algo.base;

import java.util.List;

public record TestData<C extends TestCase>(
    String algorithm,
    String type,
    String description,
    List<C> cases
) {
}
```

- [ ] **Step 3: Create AlgorithmTestBase abstract class**

```java
package com.algo.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public abstract class AlgorithmTestBase<C extends TestCase> {

    protected TestData<C> loadTestData(String yamlPath) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            // Step 1: Read YAML as tree to extract the `type` field
            JsonNode root = mapper.readTree(
                getClass().getClassLoader().getResourceAsStream(yamlPath)
            );
            String typeName = root.get("type").asText();

            // Step 2: Load the concrete TestCase class
            @SuppressWarnings("unchecked")
            Class<C> caseType = (Class<C>) Class.forName(typeName);

            // Step 3: Construct TestData<C> parameterized type
            JavaType testDataType = TypeFactory.defaultInstance()
                .constructParametricType(TestData.class, caseType);

            // Step 4: Deserialize the full YAML into TestData<C>
            return mapper.readValue(
                getClass().getClassLoader().getResourceAsStream(yamlPath),
                testDataType
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data from: " + yamlPath, e);
        }
    }
}
```

- [ ] **Step 4: Compile-check the framework types**

Run: `mvn compile test-compile`
Expected: BUILD SUCCESS

---

### Task 3: Test data YAML + TestCase Record

**Files:**
- Create: `src/test/resources/com/algo/array/two-sum.yaml`
- Create: `src/test/java/com/algo/array/TwoSumCase.java`

- [ ] **Step 1: Create YAML test data**

```yaml
algorithm: two-sum
type: com.algo.array.TwoSumCase
description: "两数之和 - 寻找数组中两数之和为目标值的下标"
cases:
  - inputs: { nums: [2, 7, 11, 15], target: 9 }
    expected: [0, 1]
  - inputs: { nums: [3, 2, 4], target: 6 }
    expected: [1, 2]
  - inputs: { nums: [3, 3], target: 6 }
    expected: [0, 1]
```

- [ ] **Step 2: Create TwoSumCase Record with nested Inputs**

```java
package com.algo.array;

import com.algo.base.TestCase;

public record TwoSumCase(Inputs inputs, int[] expected) implements TestCase {
    public record Inputs(int[] nums, int target) {}
}
```

- [ ] **Step 3: Compile-check**

Run: `mvn test-compile`
Expected: BUILD SUCCESS

---

### Task 4: Write failing test

**Files:**
- Create: `src/test/java/com/algo/array/TwoSumTest.java`

- [ ] **Step 1: Write TwoSumTest**

```java
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
```

- [ ] **Step 2: Run test to verify it fails (compilation)**

Run: `mvn test`
Expected: COMPILATION ERROR — `TwoSum` class does not exist yet

---

### Task 5: Implement TwoSum

**Files:**
- Create: `src/main/java/com/algo/array/TwoSum.java`

- [ ] **Step 1: Write minimal implementation**

```java
package com.algo.array;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No solution found");
    }
}
```

- [ ] **Step 2: Run test to verify it passes**

Run: `mvn test`
Expected: BUILD SUCCESS, all 3 cases pass

---

### Task 6: Final verification

- [ ] **Step 1: Run full test suite**

Run: `mvn clean test`
Expected: BUILD SUCCESS

- [ ] **Step 2: Run single test**

Run: `mvn test -Dtest=TwoSumTest`
Expected: BUILD SUCCESS (1 test, 3 implicit cases verified in loop)

- [ ] **Step 3: Commit**

```bash
git init
git add .
git commit -m "feat: init dsa-practice project with YAML-driven test framework

- Maven project with Java 21, JUnit 5.12, Jackson YAML 2.18
- AlgorithmTestBase<T> with automatic deserialization via type field
- TwoSum example: implementation, TestCase Record, YAML test data, test"
```
