package combinators;

import parsers.Parser;

@FunctionalInterface
public interface PackCombinator<S, R1, R2, R3> {
    Parser<S, R2> apply(Parser<S, R1> p1, Parser<S, R2> p2, Parser<S, R3> p3);
}
