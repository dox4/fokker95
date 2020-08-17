import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import types.ParserResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static examples.Combinators.*;
import static examples.ElementaryParsers.*;
import static examples.Transformers.apply;
import static examples.Transformers.just;

public class MatchParensTest {
    static List<ParserResult<Character, Tree>> parens(List<Character> input) {
        return alt(
                apply(
                        // (char, (tree, (char, tree)))
                        seq(symbol('('),
                                // (tree, (char, tree))
                                seq(MatchParensTest::parens,
                                        // (char, tree)
                                        seq(symbol(')'), MatchParensTest::parens))),
                        r -> new Tree(r.getB().getA(), r.getB().getB().getB())),
                xs -> epsilon(input).stream().map(n -> new ParserResult<>(n.getSymbols(), Tree.single)).collect(Collectors.toList())).apply(input);

    }

    // nesting :: Parser Char Int
    private static List<ParserResult<Character, Integer>> nesting(List<Character> input) {
        Parser<Character, Character> open = symbol('(');
        Parser<Character, Character> close = symbol(')');
        return alt(apply(seq(seqB(open,
                seqA(MatchParensTest::nesting,
                        close)),
                MatchParensTest::nesting), r -> Math.max(r.getA() + 1, r.getB())),
                succeed(0)).apply(input);
    }

    @Test
    public void testParens() {
        List<Character> input = Arrays.asList('(', '(', ')', '(', ')', ')', '(', ')', '(', '(', ')', ')');
        List<ParserResult<Character, Tree>> parens = parens(input);
        Assertions.assertEquals(4, parens.size());
    }

    @Test
    public void testNesting() {
        List<Character> input = Arrays.asList('(', '(', '(', '(', ')', ')', ')', ')');
        Assertions.assertEquals(4, just(MatchParensTest::nesting).apply(input).get(0).getResult());
    }

    @Data
    static class Tree {
        static Tree single = new Tree();
        private Tree a;
        private Tree b;

        Tree(Tree a, Tree b) {
            this.a = a;
            this.b = b;
        }

        private Tree() {
        }

    }

}
