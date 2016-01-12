package com.github.glob.matchers;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-02-01
 */
public class MatchContext {

    private final List<Matcher> matchers;
    private final int nextMatcherIndex;

    private final CharSequence sequence;
    private int charIndex;

    public MatchContext(List<Matcher> matchers, CharSequence sequence) {
        this.sequence = sequence;
        this.charIndex = 0;
        this.matchers = matchers;
        this.nextMatcherIndex = 0;
    }

    public MatchContext(MatchContext context) {
        this(context, context.nextMatcherIndex);
    }

    public MatchContext(MatchContext context, int nextMatcherIndex) {
        this.matchers = context.matchers;
        this.sequence = context.sequence;
        this.charIndex = context.charIndex;
        this.nextMatcherIndex = nextMatcherIndex;
    }

    public char nextChar() {
        return sequence.charAt(charIndex++);
    }

    public boolean hasNextChar() {
        return charIndex < sequence.length();
    }

    public boolean matchNext() {
        return nextMatcherIndex < matchers.size()
            ? matchers.get(nextMatcherIndex).matches(new MatchContext(this, nextMatcherIndex + 1))
            : charIndex == sequence.length();
    }

}
