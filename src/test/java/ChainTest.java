import examples.ElementaryParsers;
import examples.Transformers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import types.ParserResult;

import java.util.List;
import java.util.function.BinaryOperator;

import static examples.Combinators.chainLeft;
import static examples.Combinators.chainRight;
import static examples.ElementaryParsers.symbol;
import static examples.Transformers.apply;

public class ChainTest {
    @Test
    void testChainLeft() {
        List<Character> input = TestUtil.toCharArray("1+2+3+4+5+6");
        Parser<Character, Integer> natual = ElementaryParsers::natual;
        Parser<Character, BinaryOperator<Integer>> l2r = apply(symbol('+'), r -> (i1, i2) -> i1 - i2);
        List<ParserResult<Character, Integer>> results = chainLeft(natual, l2r).apply(input);
        Assertions.assertEquals(-19, results.get(0).getResult());
        Parser<Character, BinaryOperator<Integer>> r2l = apply(symbol('+'), r -> (i1, i2) -> i1 - i2);
        results = chainRight(natual, r2l).apply(input);
        Assertions.assertEquals(-3, results.get(0).getResult());
    }
}
