package combinators;

import parsers.Parser;

import java.util.List;

@FunctionalInterface
public interface OptionCombinator<S, R> {
    Parser<S, List<R>> apply(Parser<S, R> parser);
}
