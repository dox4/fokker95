package parsers;

import java.util.List;

@FunctionalInterface
public interface TokenParser<Symbol> {
    Parser<Symbol, List<Symbol>> apply(List<Symbol> input);
}
