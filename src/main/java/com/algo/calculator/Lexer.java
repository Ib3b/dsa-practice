package com.algo.calculator;

import java.util.ArrayList;
import java.util.List;

import static com.algo.calculator.Token.Type;

/**
 * 词法分析器（Lexer）— 将原始表达式字符串拆分为 Token 序列。
 *
 * <h3>工作方式</h3>
 * 逐个字符扫描输入字符串，根据字符类别产生对应 Token：
 * <ul>
 *   <li>数字字符 → 连续贪心地读取完整数字 → {@code NUMBER} token（含解析后的整数值）</li>
 *   <li>运算符 / 括号 → 单字符匹配 → 对应 Type 的 token</li>
 *   <li>空白字符 → 直接跳过</li>
 *   <li>其他字符 → 抛出异常</li>
 * </ul>
 *
 * <h3>示例</h3>
 * <pre>
 *   tokenize("1 + 2")   → [NUMBER(1), PLUS, NUMBER(2), EOF]
 *   tokenize("(1+2)*3") → [LPAREN, NUMBER(1), PLUS, NUMBER(2), RPAREN, STAR, NUMBER(3), EOF]
 * </pre>
 *
 * 输出的 Token 序列作为 {@link Parser Parser} 的输入，驱动递归下降语法分析。
 */
public class Lexer {

    private Lexer() {} // 工具类，不实例化

    /**
     * 执行词法分析，返回 Token 列表（末尾包含一个 EOF 标记）。
     *
     * @param source 已去除空格的表达式字符串
     * @return Token 序列
     */
    public static List<Token> tokenize(String source) {
        List<Token> tokens = new ArrayList<>();
        int start = 0;
        int current = 0;

        while (current < source.length()) {
            start = current;
            char c = source.charAt(current++);
            switch (c) {
                case '+' -> tokens.add(new Token(Type.PLUS, "+"));
                case '-' -> tokens.add(new Token(Type.MINUS, "-"));
                case '*' -> tokens.add(new Token(Type.STAR, "*"));
                case '/' -> tokens.add(new Token(Type.SLASH, "/"));
                case '(' -> tokens.add(new Token(Type.LPAREN, "("));
                case ')' -> tokens.add(new Token(Type.RPAREN, ")"));
                default -> {
                    if (Character.isDigit(c)) {
                        // 贪心地读取完整数字
                        while (current < source.length() && Character.isDigit(source.charAt(current)))
                            current++;
                        String lexeme = source.substring(start, current);
                        tokens.add(new Token(Type.NUMBER, lexeme, Integer.parseInt(lexeme)));
                    } else if (!Character.isWhitespace(c)) {
                        throw new IllegalArgumentException("Unexpected character: '" + c + "'");
                    }
                }
            }
        }
        tokens.add(new Token(Type.EOF, ""));
        return tokens;
    }
}
