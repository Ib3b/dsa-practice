package com.algo.calculator;

/**
 * 词法单元（Token）— 词法分析的最小结果单位。
 * <p>
 * 一个 Token 包含三个字段：
 * <ul>
 *   <li>{@code type} — 类型枚举，标示这是什么种类的 token：数字 / 运算符 / 括号 / 结束</li>
 *   <li>{@code lexeme} — 原文，即源代码中匹配到的原始字符串片段</li>
 *   <li>{@code literal} — 字面量值，仅 {@code NUMBER} 类型有效（存解析后的整数值），其他类型为 0</li>
 * </ul>
 * <p>
 * {@link Lexer Lexer} 将 {@code "1+2"} 分解为三个 Token：
 * {@code [NUMBER(1), PLUS, NUMBER(2)]}，然后 {@link Parser Parser} 消费这些 Token 构建 AST。
 *
 * @param type    词法单元类型
 * @param lexeme  原文片段
 * @param literal 字面量值（仅 NUMBER 有效）
 */
public record Token(Type type, String lexeme, int literal) {

    /** Token 类型枚举 */
    public enum Type {
        NUMBER, PLUS, MINUS, STAR, SLASH, LPAREN, RPAREN, EOF
    }

    /** 非数字 token 的快捷构造（literal 默认为 0） */
    public Token(Type type, String lexeme) {
        this(type, lexeme, 0);
    }

    @Override
    public String toString() {
        if (type == Type.NUMBER) return "NUMBER(" + literal + ")";
        return type.name();
    }
}
