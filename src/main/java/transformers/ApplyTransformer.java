package transformers;

import func.Converter;
import parsers.Parser;

@FunctionalInterface
public interface ApplyTransformer<S, R1, R2> {
    Parser<S, R2> apply(Parser<S, R1> p, Converter<R1, R2> converter);
}
