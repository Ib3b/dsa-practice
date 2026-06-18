# DSA Practice — 数据结构与算法练习项目

## 概述

使用 Java 练习数据结构和算法。每个算法以独立的 YAML 文件组织测试数据，测试框架通过 YAML 中声明的 `type` 字段自动反序列化为类型安全的 Java Record，再执行断言。

## 技术选型

| 项 | 选择 |
|---|------|
| 语言 | Java（最新 LTS） |
| 构建 | Maven |
| 测试 | JUnit 5（最新版） |
| YAML 解析 | Jackson (`jackson-databind` + `jackson-dataformat-yaml`) |
| 参数名保留 | `maven-compiler-plugin` 开启 `<parameters>true</parameters>` |

## 项目结构

```
<project-root>/
├── pom.xml
└── src/
    ├── main/java/com/algo/          ← 算法实现
    │   └── array/
    │       └── TwoSum.java
    └── test/
        ├── java/com/algo/
        │   ├── base/
        │   │   ├── TestCase.java          ← 标记接口
        │   │   └── AlgorithmTestBase.java ← 抽象基类
        │   └── array/
        │       └── TwoSumTest.java
        └── resources/com/algo/array/
            └── two-sum.yaml           ← 测试数据
```

YAML 文件路径与算法包路径保持一致，便于查找。每条测试用例表示为一个完整的输入-输出数据点。

## YAML 格式规范

```yaml
algorithm: two-sum           # 算法标识
type: com.algo.array.TwoSumCase  # 对应的 TestCase Record 全限定名
description: "两数之和"       # 简短描述
cases:                       # 测试用例列表（每个元素为一个 case）
  - inputs:                  #   入参对象
      nums: [2, 7, 11, 15]
      target: 9
    expected: [0, 1]         #   期望输出
  - inputs:
      nums: [3, 2, 4]
      target: 6
    expected: [1, 2]
```

### 设计要点

- `inputs` 和 `expected` 在同一层级，Record 将 `inputs` 映射为嵌套结构
- YAML 的结构完全由对应的 `TestCase` Record 定义，不同算法可直接自定义
- `type` 字段作为反序列化的中心枢纽，运行时由基类反射加载

## 核心类型

### 标记接口

```java
// TestCase.java — 所有 TestCase 的标记接口
public interface TestCase {}
```

### 数据包装

```java
// TestData.java — 根容器，承载元数据和用例列表
public record TestData<C extends TestCase>(
    String algorithm,
    String type,
    String description,
    List<C> cases
) {}
```

## 自动反序列化机制

基类 `AlgorithmTestBase<C extends TestCase>` 封装了反序列化过程：

1. 读取 YAML，提取 `type` 字段值（如 `com.algo.array.TwoSumCase`）
2. `Class.forName()` 加载对应的 Record 子类型
3. 构造 `TestData<具体子类型>` 的 `JavaType`，用 Jackson 完成完整反序列化
4. 返回 `TestData<C>` 给测试类

```java
public abstract class AlgorithmTestBase<C extends TestCase> {
    protected TestData<C> loadTestData(String yamlPath) {
        // ... 上述流程
    }
}
```

### 类型映射规则

| YAML | Java | 需特殊处理 |
|------|------|-----------|
| `int` | `int`/`long`/`Integer`/`Long` | 否（自动适配） |
| `float` | `double`/`float`/`Double`/`Float` | 否 |
| `true/false` | `boolean`/`Boolean` | 否 |
| `"string"` | `String` | 否 |
| `[1, 2, 3]` | `int[]`/`List<Integer>` | 否 |
| `{ field: val }` | Record / POJO | 否（递归反序列化） |
| `null` / `~` | 引用类型 / `Optional` | 否 |
| `[3, 1, null, 2]` | 树 / 图数组表示 | 需要自定义反序列化器 |

对于树/图等引用结构，测试数据使用数组（层序序列化）存储，通过写在对应算法的 Test 类或全局的反序列化器转换为内存对象。

## 测试模式

测试类继承 `AlgorithmTestBase<具体Case>`，反射注解 `@Test` 方法中加载 YAML 并循环断言：

```java
class TwoSumTest extends AlgorithmTestBase<TwoSumCase> {
    @Test
    void test() {
        TestData<TwoSumCase> testData = loadTestData("com/algo/array/two-sum.yaml");
        for (TwoSumCase tc : testData.cases()) {
            int[] result = new TwoSum().twoSum(tc.inputs().nums(), tc.inputs().target());
            assertArrayEquals(tc.expected(), result);
        }
    }
}
```

一条 YAML 中的多个 case 在单个 `@Test` 方法中循环执行。用例失败时框架会指出具体断言失败的原因。

## 扩展一个算法的工作流

1. 在 `src/main/java/com/algo/<category>/` 下写算法实现类
2. 在 `src/test/java/com/algo/<category>/` 下定义对应的 `XxxCase` Record（实现 `TestCase`）
3. 在 `src/test/resources/com/algo/<category>/` 下写 YAML 测试数据
4. 在 `src/test/java/com/algo/<category>/` 下写 `XxxTest`（继承 `AlgorithmTestBase`）
5. 运行测试

## pom.xml 依赖摘要

```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>

<!-- Jackson 核心 + YAML 扩展 -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.18.x</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
    <version>2.18.x</version>
    <scope>test</scope>
</dependency>

<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.12.x</version>
    <scope>test</scope>
</dependency>

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
```

## 运行测试

```bash
# 运行全部测试
mvn test

# 运行单个测试类
mvn test -Dtest=TwoSumTest
```
