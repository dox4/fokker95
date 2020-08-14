package types;

import lombok.Data;

import java.util.List;

// parse result [(symbol). result]
// parse result [(a), b]
@Data
public class ParserResult<Symbol, Result> {
    private List<Symbol> symbols;
    private Result result;

    public ParserResult(List<Symbol> symbols, Result result) {
        this.symbols = symbols;
        this.result = result;
    }
}
