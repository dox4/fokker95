import examples.Combinators;
import examples.ElementaryParsers;
import examples.Transformers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import types.ParserResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransformersTest {
    @Test
    public void testSp() {
        List<Character> list = new ArrayList<>();
        list.add(' ');
        list.add(' ');
        list.addAll(TestUtil.basicChars());
        List<ParserResult<Character, Character>> result = Transformers.sp(ElementaryParsers.symbol('a')).apply(list);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getSymbols().size());
        Assertions.assertEquals('a', result.get(0).getResult());
    }

    @Test
    public void testJust() {
        List<Character> list = TestUtil.basicChars();
        Assertions.assertEquals(0, Transformers.just(ElementaryParsers.symbol('a')).apply(list).size());
        Assertions.assertEquals(1,
                Transformers.just(Combinators.seq(ElementaryParsers.symbol('a'),
                        Combinators.seq(ElementaryParsers.symbol('b'),
                                ElementaryParsers.symbol('c'))))
                        .apply(list).size());
    }

    @Test
    public void testDigit() {
        List<Character> list = Collections.singletonList('9');
        Assertions.assertEquals(9, Transformers.digit.apply(list).get(0).getResult());
    }
}
