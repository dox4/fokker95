import parsers.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import types.BinaryTuple;
import types.ParserResult;

import java.util.List;

import static examples.Combinators.*;
import static examples.ElementaryParsers.symbol;

public class CombinatorParsersTest {
    @Test
    public void testSeqcc() {
        Parser<Character, BinaryTuple<Character, Character>> ab = seqcc.apply(symbol.apply('a'), symbol.apply('b'));
        List<Character> input = TestUtil.basicChars();
        List<ParserResult<Character, BinaryTuple<Character, Character>>> results = ab.apply(input);
        Assertions.assertEquals(1, results.size());
        ParserResult<Character, BinaryTuple<Character, Character>> result = results.get(0);
        Assertions.assertEquals(1, result.getSymbols().size());
        Assertions.assertEquals('a', result.getResult().getA());
        Assertions.assertEquals('b', result.getResult().getB());
    }

    @Test
    public void testSeq() {
        Parser<Character, BinaryTuple<BinaryTuple<Character, Character>, Character>> abc
                = seq(seq(symbol.apply('a'), symbol.apply('b')), symbol.apply('c'));
        List<Character> input = TestUtil.basicChars();
        List<ParserResult<Character,
                BinaryTuple<BinaryTuple<Character, Character>, Character>>> results
                = abc.apply(input);
        Assertions.assertEquals(1, results.size());
        ParserResult<Character, BinaryTuple<BinaryTuple<Character, Character>, Character>> result = results.get(0);
        Assertions.assertEquals(0, result.getSymbols().size());
        Assertions.assertEquals('a', result.getResult().getA().getA());
        Assertions.assertEquals('b', result.getResult().getA().getB());
        Assertions.assertEquals('c', result.getResult().getB());
    }

    @Test
    void testAlt() {
        List<Character> input = TestUtil.basicChars();
        List<ParserResult<Character, Character>> results = altcc.apply(symbol.apply('a'), symbol.apply('b')).apply(input);
        Assertions.assertEquals(1, results.size());
        results = altcc.apply(symbol.apply('a'), symbol.apply('a')).apply(input);
        Assertions.assertEquals(2, results.size());
    }
}
