package combinators;

import parsers.Parser;
import types.BinaryTuple;

@FunctionalInterface
public interface SeqParser<Symbol, R1, R2> {
    Parser<Symbol, BinaryTuple<R1, R2>> apply(Parser<Symbol, R1> p1, Parser<Symbol, R2> p2);
}
