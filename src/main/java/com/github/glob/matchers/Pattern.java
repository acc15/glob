package com.github.glob.matchers;

import java.util.Arrays;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public class Pattern {

    private final List<Matcher> matchers;

    public Pattern(Matcher... matchers) {
        this.matchers = Arrays.asList(matchers);
    }

    public Pattern(List<Matcher> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(CharSequence sequence) {
        return new MatchContext(matchers, sequence).matchNext();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pattern && ((Pattern)obj).matchers.equals(matchers);
    }

    @Override
    public int hashCode() {
        return matchers.hashCode();
    }

    public static Pattern compile(String expression) {
        throw new RuntimeException("Not implemented yet!");
    }
}
