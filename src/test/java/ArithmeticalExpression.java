import examples.ElementaryParsers;
import examples.Transformers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import types.ParserResult;

import java.util.List;
import java.util.function.BinaryOperator;

import static examples.Combinators.*;
import static examples.ElementaryParsers.symbol;
import static examples.Transformers.*;

public class ArithmeticalExpression {

    static abstract class Expr {
        abstract int execute();
    }

    static class Int extends Expr {
        private int value;

        Int(int value) {
            this.value = value;
        }

        @Override
        int execute() {
            return value;
        }
    }

    static class Mul extends Expr {
        private Expr left;
        private Expr right;

        @Override
        int execute() {
            return left.execute() * right.execute();
        }

        Mul(Expr l, Expr r) {
            this.left = l;
            this.right = r;
        }
    }

    static class Div extends Expr {
        private Expr left;
        private Expr right;

        @Override
        int execute() {
            return left.execute() / right.execute();
        }

        Div(Expr l, Expr r) {
            this.left = l;
            this.right = r;
        }
    }

    static class Add extends Expr {
        private Expr left;
        private Expr right;

        @Override
        int execute() {
            return left.execute() + right.execute();
        }

        Add(Expr l, Expr r) {
            this.left = l;
            this.right = r;
        }
    }

    static class Sub extends Expr {
        private Expr left;
        private Expr right;

        @Override
        int execute() {
            return left.execute() - right.execute();
        }

        Sub(Expr l, Expr r) {
            this.left = l;
            this.right = r;
        }
    }

    static BinaryOperator<Expr> mulConvert(Character ch) {
        if (ch == '*') return Mul::new;
        else if (ch == '/') return Div::new;
        throw new RuntimeException("unrecognized operator: `" + ch + "`.");
    }

    static BinaryOperator<Expr> addConvert(Character ch) {
        if (ch == '+') return Add::new;
        else if (ch == '-') return Sub::new;
        throw new RuntimeException("unrecognized operator: `" + ch + "`.");
    }

    @Test
    void testArithmetic() {
        Parser<Character, Integer> natual = ElementaryParsers::natual;
        Parser<Character, Expr> fact1 = apply(natual, Int::new);
        Parser<Character, BinaryOperator<Expr>> mul = apply(symbol('*'), ArithmeticalExpression::mulConvert);
        Parser<Character, BinaryOperator<Expr>> div = apply(symbol('/'), ArithmeticalExpression::mulConvert);
        Parser<Character, Expr> term = chainRight(fact1, alt(mul, div));
        Parser<Character, BinaryOperator<Expr>> add = apply(symbol('+'), ArithmeticalExpression::addConvert);
        Parser<Character, BinaryOperator<Expr>> sub = apply(symbol('-'), ArithmeticalExpression::addConvert);
        Parser<Character, Expr> expr = chainRight(term, alt(add, sub));
        List<Character> chs = TestUtil.toCharArray("123*456+789-12/4");
        List<ParserResult<Character, Expr>> results = expr.apply(chs);
        Assertions.assertEquals(123*456+789-12/4, results.get(0).getResult().execute());
    }
}
