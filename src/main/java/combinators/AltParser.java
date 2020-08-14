package combinators;

import parsers.Parser;

@FunctionalInterface
public interface AltParser<S, R> {
    Parser<S, R> apply(Parser<S, R> p1, Parser<S, R> p2);
}
