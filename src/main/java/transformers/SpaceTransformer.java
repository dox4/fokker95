package transformers;

import parsers.Parser;

@FunctionalInterface
public interface SpaceTransformer<R> {
    Parser<Character, R> apply(Parser<Character, R> p);
}
