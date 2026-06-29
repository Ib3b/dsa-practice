package com.algo.calculator;

import java.util.List;

/**
 * 四则运算计算器门面 — 词法分析 → 语法分析 → 后序求值。<br>
 * 支持 + - * /、括号、一元负号。
 *
 * <pre>
 *   String expr = "1 + 2 * 3";
 *   int result = Calculator.calculate(expr); // 7
 *   Calculator.printAst(expr);              // 打印可视化二叉树
 * </pre>
 */
public class Calculator {

    private Calculator() {} // 工具类，不实例化

    // ===== 公开 API =====

    /**
     * 计算表达式值。
     *
     * @param expr 算术表达式，如 {@code "1+2*3"}、{@code "(1+2)*(3+4)"}
     * @return 计算结果（整数除法，向零取整）
     */
    public static int calculate(String expr) {
        return parse(expr).evaluate();
    }

    /**
     * 解析表达式并以二叉树形式返回 AST 可视化字符串。
     * <pre>
     *    +
     *   / \
     *  1   *
     *      / \
     *     2   3
     * </pre>
     */
    public static String dumpAst(String expr) {
        return parse(expr).render().toString();
    }

    /**
     * 将 AST 二叉树打印到终端。
     */
    public static void printAst(String expr) {
        System.out.print(dumpAst(expr));
    }

    // ===== 编译流水线 =====

    /** 词法分析 → 语法分析，返回 AST 根节点 */
    private static AstNode parse(String expr) {
        return Parser.parse(tokenize(expr));
    }

    /** 词法分析：字符串 → Token 序列 */
    private static List<Token> tokenize(String expr) {
        if (expr == null) throw new IllegalArgumentException("Expression must not be null");
        String src = expr.replaceAll("\\s+", "");
        if (src.isEmpty()) throw new IllegalArgumentException("Empty expression");
        return Lexer.tokenize(src);
    }

    // ===== 入口：方便调试 =====

    /**
     * 直接运行调试，默认计算 {@code 1+2*3}(7)。
     * <pre>
     *   java com.algo.calculator.Calculator        # 默认 "1+2*3"
     *   java com.algo.calculator.Calculator "(1+2)*3"
     * </pre>
     */
    public static void main(String[] args) {
        String expr = (args.length > 0) ? args[0] : "1+2*3";
        String src = expr.replaceAll("\\s+", "");

        // 词法分析 — 只要一次
        List<Token> tokens = Lexer.tokenize(src);
        System.out.println("表达式: " + expr);
        System.out.println();
        System.out.println("Token 序列:");
        tokens.forEach(t -> System.out.println("  " + t));
        System.out.println();

        // 语法分析 — 只要一次
        AstNode ast = Parser.parse(tokens);
        System.out.println("AST 二叉树:");
        System.out.print(ast.render());
        System.out.println();

        // 求值
        System.out.println("计算结果: " + expr + " = " + ast.evaluate());
    }
}
