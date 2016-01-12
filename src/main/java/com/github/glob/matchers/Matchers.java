package com.github.glob.matchers;

import java.util.Arrays;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-12-01
 */
public class Matchers {

    private static final Matcher ANY = new AnyCharMatcher();
    private static final Matcher ZERO_OR_MORE = new ZeroOrMoreMatcher();

    public static Matcher any() {
        return ANY;
    }

    public static Matcher zeroOrMore() {
        return ZERO_OR_MORE;
    }

    public static Matcher variant(Matcher... matchers) {
        return new VariantMatcher(Arrays.asList(matchers));
    }

    public static Matcher text(CharSequence sequence) {
        return new TextMatcher(sequence);
    }

    public static boolean matches(List<Matcher> pattern, CharSequence sequence) {
        return new MatchContext(pattern, sequence).matchNext();
    }

}
