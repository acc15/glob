package com.github.glob.matchers;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-02-01
 */
public class MatchContext {

    private final MatchContext parent;
    private final List<Matcher> matchers;
    private final int nextMatcherIndex;
    private final CharSequence sequence;
    private int charIndex;

    private MatchContext(
            MatchContext parent, List<Matcher> matchers, int nextMatcherIndex, CharSequence sequence, int charIndex) {
        this.parent = parent;
        this.matchers = matchers;
        this.nextMatcherIndex = nextMatcherIndex;
        this.sequence = sequence;
        this.charIndex = charIndex;
    }

    public char nextChar() {
        return sequence.charAt(charIndex++);
    }

    public boolean hasNextChar() {
        return charIndex < sequence.length();
    }

    public boolean matchNext() {
        if (nextMatcherIndex < matchers.size()) {
            return matchers.get(nextMatcherIndex).matches(next());
        }
        if (parent != null) {
            return parent.reposition(charIndex).matchNext();
        }
        return charIndex == sequence.length();
    }

    public MatchContext derive(List<Matcher> matchers) {
        return new MatchContext(this, matchers, 0, sequence, charIndex);
    }

    public MatchContext copy() {
        return new MatchContext(parent, matchers, nextMatcherIndex, sequence, charIndex);
    }

    public MatchContext next() {
        return new MatchContext(parent, matchers, nextMatcherIndex + 1, sequence, charIndex);
    }

    public MatchContext reposition(int newCharIndex) {
        return new MatchContext(parent, matchers, nextMatcherIndex, sequence, newCharIndex);
    }

    public static MatchContext create(List<Matcher> matchers, CharSequence sequence) {
        return new MatchContext(null, matchers, 0, sequence, 0);
    }
}
