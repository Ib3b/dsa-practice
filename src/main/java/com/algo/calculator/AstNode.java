package com.algo.calculator;

/**
 * AST 节点 — 语法分析器输出的抽象语法树，支持后序求值与可视化渲染。
 *
 * <h3>节点类型</h3>
 * <ul>
 *   <li>{@link Num} — 叶子节点，存一个整数字面量</li>
 *   <li>{@link UnaryMinus} — 一元负号运算，包装一个子节点</li>
 *   <li>{@link BinOp} — 二元运算（+ - * /），含左右子节点</li>
 * </ul>
 *
 * <h3>后序求值</h3>
 * 每个节点实现 {@link #evaluate()}，先递归求值子节点，再计算自身。
 * 因为是后序遍历（左→右→根），优先级和结合性已在 {@link Parser Parser} 构建
 * 树结构时确定，求值过程无需考虑运算符优先级。
 *
 * <h3>可视化渲染</h3>
 * 每个节点实现 {@link #render()}，递归返回 {@link Box} 矩形，
 * 最终由 {@link Calculator#dumpAst} 拼合为字符串。
 */
public sealed interface AstNode {

    /** 后序求值：递归计算子树 → 合并自身结果 */
    int evaluate();

    /** 递归渲染为 Box，用于生成可视化的二叉树字符串 */
    Box render();

    // ================================================================
    //  数字字面量 — 对应 factor ::= NUMBER，是递归的"地基"
    // ================================================================

    /**
     * 数字字面量节点（叶子），存一个整数值。
     * {@code evaluate()} 直接返回字面量值。
     */
    record Num(int val) implements AstNode {
        public int evaluate() { return val; }

        public Box render() {
            String s = String.valueOf(val);
            return new Box(java.util.List.of(s), s.length(), 1, s.length() / 2);
        }
    }

    // ================================================================
    //  一元负号 — 对应 factor ::= '-' factor
    // ================================================================

    /**
     * 一元负号节点，只有一个子节点 child。
     * {@code evaluate()} 先求子节点值再取负。
     * 渲染为纵线布局：最上层为 '-'，下一行为 '│'，再下方为子树。
     */
    record UnaryMinus(AstNode child) implements AstNode {
        public int evaluate() { return -child.evaluate(); }

        public Box render() {
            Box c = child.render();
            var lines = new java.util.ArrayList<String>();
            lines.add(Box.padded(c.width(), c.rootCol(), '-'));
            lines.add(Box.padded(c.width(), c.rootCol(), '│'));
            lines.addAll(c.lines());
            return new Box(lines, c.width(), lines.size(), c.rootCol());
        }
    }

    // ================================================================
    //  二元运算 — 对应 expression/term 中的 + - * /
    //  优先级已由 Parser 体现在 AST 的深度层次中，求值时不关心优先级
    // ================================================================

    /**
     * 二元运算节点。
     * {@code evaluate()} 后序遍历：左子树 → 右子树 → 自身运算。<br>
     * {@code render()} 用 Box 组合算法绘制二叉树布局，
     * 操作符在顶层，{@code / \} 连接左右子树的根字符。
     */
    record BinOp(char op, AstNode left, AstNode right) implements AstNode {
        public int evaluate() {
            int a = left.evaluate(), b = right.evaluate();
            return switch (op) {
                case '+' -> a + b;
                case '-' -> a - b;
                case '*' -> a * b;
                case '/' -> {
                    if (b == 0) throw new ArithmeticException("Division by zero");
                    yield a / b;
                }
                default -> throw new IllegalStateException("Unknown op: " + op);
            };
        }

        public Box render() {
            Box l = left.render();
            Box r = right.render();
            int gap = 1 + Math.min(l.width(), r.width());
            int rs = l.width() + gap;
            int tw = rs + r.width();
            int rc = (l.rootCol() + rs + r.rootCol()) / 2;

            var lines = new java.util.ArrayList<String>();
            lines.add(Box.padded(tw, rc, op));
            lines.add(Box.connector(tw, l.rootCol(), rs + r.rootCol()));

            int mh = Math.max(l.height(), r.height());
            for (int i = 0; i < mh; i++) {
                char[] row = new char[tw];
                java.util.Arrays.fill(row, ' ');
                if (i < l.height()) {
                    String ll = l.lines().get(i);
                    for (int j = 0; j < ll.length(); j++) row[j] = ll.charAt(j);
                }
                if (i < r.height()) {
                    String rl = r.lines().get(i);
                    for (int j = 0; j < rl.length(); j++) row[rs + j] = rl.charAt(j);
                }
                lines.add(new String(row));
            }
            return new Box(lines, tw, lines.size(), rc);
        }
    }

    // ================================================================
    //  渲染盒子（Box）— 辅助可视化
    //
    //  每个 AstNode 递归调用 render() 返回一个 Box，包含：
    //    - lines:   该子树渲染后的各行文本
    //    - width:   整体宽度（字符数）
    //    - height:  整体高度（行数）
    //    - rootCol: 当前节点根字符所在的列号（父节点据此定位连接线）
    //
    //  父节点通过子节点的 rootCol 确定 / 和 \ 的位置，然后拼合。
    // ================================================================

    record Box(java.util.List<String> lines, int width, int height, int rootCol) {
        @Override
        public String toString() { return String.join("\n", lines) + "\n"; }

        /** 在 (col, 0) 处放一个字符 ch，其余为空格的一行文本 */
        static String padded(int w, int col, char ch) {
            char[] row = new char[w];
            java.util.Arrays.fill(row, ' ');
            row[col] = ch;
            return new String(row);
        }

        /** 生成 / 和 \ 的连接线行 */
        static String connector(int w, int leftCol, int rightCol) {
            char[] row = new char[w];
            java.util.Arrays.fill(row, ' ');
            row[leftCol] = '/';
            row[rightCol] = '\\';
            return new String(row);
        }
    }
}
