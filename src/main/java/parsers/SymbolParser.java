package parsers;

@FunctionalInterface
public interface SymbolParser<Symbol> {
    Parser<Symbol, Symbol> apply(Symbol a);
}
