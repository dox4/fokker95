package examples;

import parsers.Parser;

import static util.ListComprehension.dropWhile;

public class Transformers {
    public static <R> Parser<Character, R> sp(Parser<Character, R> p) {
        return (xs) -> p.apply(dropWhile(c -> c == ' ', xs));
    }
}
