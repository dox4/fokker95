import func.Converter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import types.BinaryTuple;
import types.ParserResult;
import util.ListComprehension;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static examples.Combinators.*;
import static examples.ElementaryParsers.symbol;
import static examples.Transformers.apply;
import static examples.Transformers.digit;

public class FixedTest {
    @Test
    void testFixed() {
        List<Character> input = Arrays.asList('1', '2', '3', '4', '.', '6', '7', '8', '9');
        // natual
        Parser<Character, List<Integer>> manyDigit = many(digit);
        Converter<List<Integer>, Integer> accu = arr -> arr.stream().reduce(0, (x, y) -> x * 10 + y);
        Parser<Character, Integer> natural = apply(manyDigit, accu);
        // fraction
        BiFunction<Integer, Double, Double> op = (x, y) -> (x + y)/10.0;
        Parser<Character, Double> fract = apply(manyDigit, r -> ListComprehension.foldr(op, 0.0, r));
        // fixed
        Parser<Character, Double> opt = apply(option(seqB(symbol('.'), fract)), () -> 0.0, x -> x.get(0));
        Parser<Character, BinaryTuple<Integer, Double>> seq = seq(natural, opt);
        Parser<Character, Double> fixed = apply(seq, r -> r.getA().doubleValue() + r.getB());
        List<ParserResult<Character, Double>> results = fixed.apply(input);
        Assertions.assertEquals(1234.6789, results.get(0).getResult());
        // possible negative decimal
        Parser<Character, Double> pnd = apply(seq(apply(option(symbol('-')), () -> 1, x -> -1), fixed), r -> r.getA() * r.getB());
        Assertions.assertEquals(1234.6789, pnd.apply(input).get(0).getResult());
        input = Arrays.asList('-', '1', '2', '3', '4', '.', '6', '7', '8', '9');
        Assertions.assertEquals(-1234.6789, pnd.apply(input).get(0).getResult());
    }
}
