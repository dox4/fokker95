import func.Bool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import types.ParserResult;
import types.ZeroTuple;
import util.ListComprehension;

import java.util.Arrays;
import java.util.List;

import static examples.ElementaryParsers.*;

public class ElementaryParsersTest {


    @Test
    public void testSymbolA() {
        List<Character> input = TestUtil.basicChars();
        List<ParserResult<Character, Character>> result = symbolA.apply(input);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getSymbols().size());
        Assertions.assertEquals('a', result.get(0).getResult());
    }

    @Test
    public void testSymbolC() {
        List<Character> input = TestUtil.basicChars();
        List<ParserResult<Character, Character>> result = symbol.apply('a').apply(input);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getSymbols().size());
        Assertions.assertEquals('a', result.get(0).getResult());
    }

    @Test
    public void testSymbol() {
        List<Character> input = TestUtil.basicChars();
        List<ParserResult<Character, Character>> result = symbol('a').apply(input);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getSymbols().size());
        Assertions.assertEquals('a', result.get(0).getResult());
    }

    @Test
    public void testTokenC() {
        List<Character> tok = TestUtil.basicChars();
        List<ParserResult<Character, List<Character>>> result1 = token.apply(tok).apply(tok);
        Assertions.assertEquals(1, result1.size());
        Assertions.assertEquals(0, result1.get(0).getSymbols().size());
        Assertions.assertEquals(3, result1.get(0).getResult().size());
        Assertions.assertTrue(ListComprehension.equal(tok, result1.get(0).getResult()));
        List<Character> input = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f');
        List<ParserResult<Character, List<Character>>> result2 = token.apply(tok).apply(input);
        Assertions.assertEquals(1, result2.size());
        Assertions.assertEquals(3, result2.get(0).getSymbols().size());
        Assertions.assertEquals(3, result2.get(0).getResult().size());
        Assertions.assertTrue(ListComprehension.equal(result2.get(0).getResult(), tok));
    }


    @Test
    public void testToken() {
        List<Character> tok = TestUtil.basicChars();
        List<ParserResult<Character, List<Character>>> result1 = token(tok).apply(tok);
        Assertions.assertEquals(1, result1.size());
        Assertions.assertEquals(0, result1.get(0).getSymbols().size());
        Assertions.assertEquals(3, result1.get(0).getResult().size());
        Assertions.assertTrue(ListComprehension.equal(tok, result1.get(0).getResult()));
        List<Character> input = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f');
        List<ParserResult<Character, List<Character>>> result2 = token(tok).apply(input);
        Assertions.assertEquals(1, result2.size());
        Assertions.assertEquals(3, result2.get(0).getSymbols().size());
        Assertions.assertEquals(3, result2.get(0).getResult().size());
        Assertions.assertTrue(ListComprehension.equal(result2.get(0).getResult(), tok));
    }

    @Test
    public void testSatisfyC() {
        List<Character> input = TestUtil.basicChars();
        List<ParserResult<Character, Character>> result = satisfy.apply(s -> s == 'a').apply(input);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getSymbols().size());
        Assertions.assertEquals('a', result.get(0).getResult());
    }

    @Test
    public void testSatisfy() {
        List<Character> input = TestUtil.basicChars();
        Bool<Character> bool = s -> s == 'a';
        List<ParserResult<Character, Character>> result = satisfy(bool).apply(input);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getSymbols().size());
        Assertions.assertEquals('a', result.get(0).getResult());
    }


    @Test
    public void testEpsilonSucceedFailC() {
        List<Character> input = TestUtil.basicChars();
        // epsilon
        Assertions.assertEquals(1, epsilon.apply(input).size());
        Assertions.assertEquals(3, epsilon.apply(input).get(0).getSymbols().size());
        Assertions.assertEquals(ZeroTuple.value(), epsilon.apply(input).get(0).getResult());

        // epsilon2
        Assertions.assertEquals(1, epsilon2.apply(input).size());
        Assertions.assertEquals(3, epsilon2.apply(input).get(0).getSymbols().size());
        Assertions.assertEquals(ZeroTuple.value(), epsilon2.apply(input).get(0).getResult());

        // succeed
        List<ParserResult<Character, Character>> result = succeed.apply('a').apply(input);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(3, result.get(0).getSymbols().size());
        Assertions.assertEquals('a', result.get(0).getResult());
        // fail
        Assertions.assertEquals(0, failc.apply(input).size());

    }
    @Test
    public void testEpsilonSucceedFail() {
        List<Character> input = TestUtil.basicChars();
        // epsilon
        Assertions.assertEquals(1, epsilon(input).size());
        Assertions.assertEquals(3, epsilon(input).get(0).getSymbols().size());
        Assertions.assertEquals(ZeroTuple.value(), epsilon(input).get(0).getResult());

        // epsilon2
        Assertions.assertEquals(1, epsilonBySucceed(input).size());
        Assertions.assertEquals(3, epsilonBySucceed(input).get(0).getSymbols().size());
        Assertions.assertEquals(ZeroTuple.value(), epsilonBySucceed(input).get(0).getResult());

        // succeed
        Parser<Character, Character> succeed = succeed('a');
        List<ParserResult<Character, Character>> result = succeed.apply(input);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(3, result.get(0).getSymbols().size());
        Assertions.assertEquals('a', result.get(0).getResult());
        // fail
        Assertions.assertEquals(0, fail(input).size());

    }
}
