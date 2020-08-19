import examples.Transformers;
import func.Converter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import types.BinaryTuple;
import types.ParserResult;
import util.ListComprehension;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

import static examples.Combinators.*;
import static examples.ElementaryParsers.*;
import static examples.Transformers.*;

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


    @Test
    void testMany() {
        List<Character> input = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9');
        Parser<Character, List<Integer>> manyDigit = many(digit);
        Converter<List<Integer>, Integer> accu = arr -> arr.stream().reduce(0, (x, y) -> x * 10 + y);
        Parser<Character, Integer> natural = apply(manyDigit, accu);
        List<ParserResult<Character, Integer>> results = natural.apply(input);
        Assertions.assertEquals(123456789, results.get(0).getResult());

        List<Character> input2 = TestUtil.toCharArray("aaabc");
        Parser<Character, Character> a = symbolA;
        Parser<Character, List<Character>> manyA = many(a);
        Assertions.assertEquals(4, manyA.apply(input2).size());

        Parser<Character, List<Character>> a4 = many(a, 4, 4);
        Assertions.assertEquals(0, a4.apply(input2).size());
    }

    @Test
    void testOption() {
        Parser<Character, List<Character>> a = option(symbol('a'));
        Assertions.assertEquals(2, a.apply(TestUtil.basicChars()).size());
    }

    @Test
    void testPack() {
        Parser<Character, Character> lp = symbol('(');
        Parser<Character, Character> a = symbol('a');
        Parser<Character, List<Character>> many = many(a);
        Parser<Character, Character> rp = symbol(')');
        Parser<Character, List<Character>> pack = pack(lp, many, rp);
        Assertions.assertEquals(1, pack.apply(TestUtil.toCharArray("(aaa)")).size());
    }

    @Test
    void testWord() {
        List<ParserResult<Character, String>> word = Transformers.word.apply(TestUtil.basicChars());
        Assertions.assertEquals("abc", word.get(0).getResult());
    }

    @Test
    void testListOf() {
        List<Character> input1 = TestUtil.toCharArray("this is a test string");
        Parser<Character, Character> comma = symbol(' ');
        Parser<Character, String> word = apply(many(alpha), Transformers::toWord);
        Parser<Character, List<String>> listOf = listOf(word, comma);
        List<ParserResult<Character, List<String>>> results = listOf.apply(input1);
        Assertions.assertEquals(5, results.get(0).getResult().size());
    }

    @Test
    void testFold() {
        Integer sum = ListComprehension.foldl(Integer::sum, 0, Arrays.asList(1, 2, 3, 4));
        Assertions.assertEquals(10, sum);
        BinaryOperator<Integer> sub = (x, y) -> x - y;
        Integer diff = ListComprehension.foldr(sub, 0, Arrays.asList(1, 2, 3, 4));
        Assertions.assertEquals(-2, diff);
    }
}
