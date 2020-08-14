package parsers;

@FunctionalInterface
public interface SucceedParser<Symbol, Result> {
    Parser<Symbol, Result> apply(Result result);
}
