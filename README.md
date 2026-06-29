# dsa-practice

YAML 驱动的数据结构与算法练习项目。

## 目录

```
src/main/java/com/algo/
├── array/          BinarySearch, CycleArray, Difference, MergeIntervals, NumArray, StackAsQueue, TwoSum
├── calculator/     Lexer → Parser → AST → evaluate()，带 ASCII 树可视化
└── linked/list/    ListNode (泛型双向链表节点)

src/test/java/com/algo/
├── base/           AlgorithmTestBase — YAML 驱动测试框架
├── array/          每个算法一对 Case + Test
└── calculator/     CalculatorCase + CalculatorTest
```

## 构建与测试

```bash
mvn compile              # 编译
mvn test                 # 全部测试
mvn test -q              # 静默模式（只显示失败）
mvn test -Dtest=BinarySearchTest  # 单个测试类
```

## 测试框架

三个组件驱动测试：

1. **`*Case.java`** — record 实现 `TestCase`，嵌套 `Inputs` record
2. **`*Test.java`** — 继承 `AlgorithmTestBase<*Case>`，加载 YAML → `verifyAll()`
3. **YAML 测试数据** — 两种格式
   - **全量格式**：`inputs: {}` + `expected:` 键值对
   - **紧凑格式**：`columns:` 定义 schema，`cases:` 为位置数组（首选）

`verifyAll()` 逐条执行，输出 `X/Y passed`，失败不影响后续用例。

## 模块清单

| 模块 | 算法/数据结构 | 核心特性 |
|------|--------------|----------|
| `BinarySearch` | 二分查找 | 标准查找 + 左/右边界 |
| `CycleArray` | 环形数组 | 定长双端队列，头尾 O(1) 增删 |
| `Difference` | 差分数组 | 批量区间增减 |
| `MergeIntervals` | 区间合并 | 按起点排序合并 |
| `NumArray` | 前缀和 | 静态数组 O(1) 区间和 |
| `StackAsQueue` | 双栈队列 | 均摊 O(1) 队列操作 |
| `TwoSum` | 哈希表 | O(n) 两数之和 |
| `Calculator` | AST 计算器 | Lexer → Parser → 后序求值 + ASCII 树渲染 |

## 依赖

- Java 25，无生产依赖
- JUnit 5.12 + Jackson 2.18（仅 test scope）
