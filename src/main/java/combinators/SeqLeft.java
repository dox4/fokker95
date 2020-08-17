package combinators;

import parsers.Parser;

@FunctionalInterface
public interface SeqLeft<S, R1, R2> {
    Parser<S, R1> apply(Parser<S, R1> p1, Parser<S, R2> p2);
}
