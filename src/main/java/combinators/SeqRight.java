package combinators;

import parsers.Parser;

@FunctionalInterface
public interface SeqRight<S, R1, R2> {
    Parser<S, R2> apply(Parser<S, R1> p1, Parser<S, R2> p2);
}
