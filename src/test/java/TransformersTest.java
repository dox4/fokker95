import examples.ElementaryParsers;
import examples.Transformers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import types.ParserResult;

import java.util.ArrayList;
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
}
