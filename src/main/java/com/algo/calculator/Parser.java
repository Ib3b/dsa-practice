package com.algo.calculator;

import java.util.List;

import static com.algo.calculator.Token.Type;

/**
 * 递归下降语法分析器（Parser）— 将 Token 序列解析为 AST。
 *
 * <h3>文法（Grammar）</h3>
 * <pre>
 * expression ::= term (('+' | '-') term)*
 * term       ::= factor (('*' | '/') factor)*
 * factor     ::= NUMBER | '(' expression ')' | '-' factor
 * </pre>
 *
 * <h3>AST 构建过程</h3>
 * 分析器的每个方法对应一条文法产生式，返回一个 {@link AstNode}。
 * 调用链 <code>expression() → term() → factor()</code> 自上而下构建完整的 AST。
 *
 * <h4>1. 优先级通过文法层级体现</h4>
 * <code>expression</code> 处理加减（优先级低），它先调用 <code>term</code> 处理乘除（优先级高），
 * 因此 <code>BinOp('*')</code> 节点在 AST 中会比外围的 <code>BinOp('+')</code> 更深。
 * 后序求值时乘除先算，自然满足运算优先级。
 *
 * <h4>2. 左结合通过循环实现</h4>
 * 例如 <code>expression()</code> 从 <code>term()</code> 拿到左子树后进入 <code>while</code> 循环：
 * 每匹配一个 {@code +} 或 {@code -}，就用当前左子树和新解析的右子树创建 <code>BinOp</code>，
 * 然后以该 <code>BinOp</code> 作为新的左子树继续循环。这保证了连续加减是左结合的：
 * <pre>
 *   输入: 1 - 2 - 3
 *   循环过程:
 *     left = term() → Num(1)
 *     匹配 '-', right = term() → Num(2)
 *     left = BinOp(-, Num(1), Num(2))
 *     匹配 '-', right = term() → Num(3)
 *     left = BinOp(-, BinOp(-, Num(1), Num(2)), Num(3))   ✓ 左结合
 * </pre>
 *
 * <h4>3. 括号通过递归构建子树</h4>
 * <code>factor()</code> 遇到 {@code (} 时消耗左括号，然后<em>重新调用</em> <code>expression()</code>
 * 解析括号内的表达式，该调用会构建一个完整的子树。遇到 {@code )} 后返回该子树。
 *
 * <h4>4. 一元负号创建特殊节点</h4>
 * <code>factor()</code> 遇到 {@code -} 时消耗该符号，然后递归调用自身解析操作数，
 * 结果包装在 {@link AstNode.UnaryMinus} 中。
 * 递归调用走 <code>factor()</code> 而非 <code>term()</code>，
 * 确保 {@code -3*2} 解析为 <code>BinOp(*, UnaryMinus(3), 2)</code>（即 (-3)*2），
 * 而非 <code>UnaryMinus(BinOp(*, 3, 2))</code>（即 -(3*2)）。
 *
 * <h4>示例：{@code 1 + 2 * 3} 的 AST 构建</h4>
 * <pre>
 * parse()
 *   ├─ expression()
 *   │    ├─ term()
 *   │    │    ├─ factor() → Num(1)
 *   │    │    └─ 不匹配 * 或 /，返回
 *   │    ├─ 匹配 '+', pos++
 *   │    ├─ term()
 *   │    │    ├─ factor() → Num(2)
 *   │    │    ├─ 匹配 '*', pos++
 *   │    │    ├─ factor() → Num(3)
 *   │    │    └─ BinOp(*, Num(2), Num(3))
 *   │    └─ BinOp(+, Num(1), BinOp(*, Num(2), Num(3)))
 *   └─ 返回 AST 根节点
 * </pre>
 */
public class Parser {

    private Parser() {} // 工具类，不实例化

    /**
     * 解析完整 Token 序列，返回 AST 根节点。
     *
     * @param tokens 词法分析器输出的 Token 序列
     * @return AST 根节点
     */
    public static AstNode parse(List<Token> tokens) {
        ParseResult result = expression(tokens, 0);
        if (result.pos() >= tokens.size() || tokens.get(result.pos()).type() != Type.EOF) {
            Token next = tokens.get(Math.min(result.pos(), tokens.size() - 1));
            throw new IllegalArgumentException("Unexpected token after expression: " + next);
        }
        return result.node();
    }

    // ============================================================
    //  产生式实现
    //  每个方法返回 ParseResult = (AstNode, int)，即
    //  "解析到的新节点" + "消费完该产生式后的位置"
    //  位置显式传递，不依赖可变状态。
    // ============================================================

    /**
     * expression ::= term (('+' | '-') term)*
     * <p>
     * 先解析一个 term 作为左子树，然后循环匹配 {@code +} 或 {@code -}。
     * 每匹配一个运算符就用当前左子树和新 term 构建 BinOp，成为新的左子树。
     * 连续加减因此是左结合的：{@code 1-2-3} → {@code ((1-2)-3)}。
     */
    private static ParseResult expression(List<Token> tokens, int pos) {
        ParseResult leftResult = term(tokens, pos);
        AstNode left = leftResult.node();
        pos = leftResult.pos();

        while (pos < tokens.size()) {
            Token t = tokens.get(pos);
            if (t.type() == Type.PLUS || t.type() == Type.MINUS) {
                pos++;
                ParseResult rightResult = term(tokens, pos);
                left = new AstNode.BinOp(t.lexeme().charAt(0), left, rightResult.node());
                pos = rightResult.pos();
            } else {
                break;
            }
        }
        return new ParseResult(left, pos);
    }

    /**
     * term ::= factor (('*' | '/') factor)*
     * <p>
     * 与 expression 结构相同，但处理更高优先级的乘除。
     * 因为 term 在 expression 内部被调用，乘法节点在 AST 中位于更深的层次，
     * 后序求值时先于外层加减被求值。
     */
    private static ParseResult term(List<Token> tokens, int pos) {
        ParseResult leftResult = factor(tokens, pos);
        AstNode left = leftResult.node();
        pos = leftResult.pos();

        while (pos < tokens.size()) {
            Token t = tokens.get(pos);
            if (t.type() == Type.STAR || t.type() == Type.SLASH) {
                pos++;
                ParseResult rightResult = factor(tokens, pos);
                left = new AstNode.BinOp(t.lexeme().charAt(0), left, rightResult.node());
                pos = rightResult.pos();
            } else {
                break;
            }
        }
        return new ParseResult(left, pos);
    }

    /**
     * factor ::= NUMBER | '(' expression ')' | '-' factor
     * <p>
     * 三种情况：
     * <ul>
     *   <li><b>NUMBER</b> — 消耗数字 token，返回 {@link AstNode.Num} 叶子节点</li>
     *   <li><b>( expression )</b> — 消耗左括号，递归调用 {@link #expression} 解析子表达式，
     *       消耗右括号，返回子表达式对应的整棵子树</li>
     *   <li><b>- factor</b> — 消耗负号，递归调用自身解析操作数，
     *       返回 {@link AstNode.UnaryMinus} 包装节点</li>
     * </ul>
     */
    private static ParseResult factor(List<Token> tokens, int pos) {
        Token t = tokens.get(pos);

        if (t.type() == Type.NUMBER) {
            return new ParseResult(new AstNode.Num(t.literal()), pos + 1);
        }

        if (t.type() == Type.LPAREN) {
            ParseResult inner = expression(tokens, pos + 1);
            if (inner.pos() >= tokens.size() || tokens.get(inner.pos()).type() != Type.RPAREN) {
                throw new IllegalArgumentException("Missing closing parenthesis");
            }
            return new ParseResult(inner.node(), inner.pos() + 1);
        }

        if (t.type() == Type.MINUS) {
            ParseResult operand = factor(tokens, pos + 1);
            return new ParseResult(new AstNode.UnaryMinus(operand.node()), operand.pos());
        }

        throw new IllegalArgumentException("Unexpected token: " + t);
    }

    // ============================================================
    //  中间结果 — 用于函数式组合
    //  一个解析方法返回 (解析出的节点, 消费后的位置)。
    //  位置显式传递，不需要实例变量。
    // ============================================================

    private record ParseResult(AstNode node, int pos) {}
}
