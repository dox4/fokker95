package combinators;

import parsers.Parser;

import java.util.List;

@FunctionalInterface
public interface ManyCombinator<S, R> {
    Parser<S, List<R>> apply(Parser<S, R> p);
}
