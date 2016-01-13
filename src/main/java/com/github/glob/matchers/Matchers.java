package com.github.glob.matchers;

import java.util.Arrays;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-12-01
 */
public class Matchers {

    private static final Matcher ANY = context -> {
        if (!context.hasNextChar()) {
            return false;
        }
        context.nextChar();
        return context.matchNext();
    };

    private static final Matcher ZERO_OR_MORE = context -> {
        while (!context.matchNext()) {
            if (!context.hasNextChar()) {
                return false;
            }
            context.nextChar();
        }
        return true;
    };

    public static Matcher any() {
        return ANY;
    }

    public static Matcher zeroOrMore() {
        return ZERO_OR_MORE;
    }

    public static Matcher variants(Iterable<Matcher> variants) {
        return new VariantMatcher(variants);
    }

    public static Matcher variants(Matcher... matchers) {
        return variants(Arrays.asList(matchers));
    }

    public static Matcher sequence(List<Matcher> matchers) { return new SequenceMatcher(matchers); }

    public static Matcher sequence(Matcher... matchers) { return sequence(Arrays.asList(matchers)); }

    public static Matcher text(CharSequence sequence) {
        return new TextMatcher(sequence);
    }


}
