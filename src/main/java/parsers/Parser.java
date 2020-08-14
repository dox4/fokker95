package parsers;

import types.ParserResult;

import java.util.List;

// type Parser symbol result = [symbol] -> [(symbol), result]
// type Parser a b = [a] -> [(a), b]
@FunctionalInterface
public
interface Parser<Symbol, Result> {
    List<ParserResult<Symbol, Result>> apply(List<Symbol> input);
}
