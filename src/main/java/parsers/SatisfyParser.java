package parsers;

import func.Bool;

@FunctionalInterface
public interface SatisfyParser<Symbol> {
    Parser<Symbol, Symbol> apply(Bool<Symbol> bool);
}
