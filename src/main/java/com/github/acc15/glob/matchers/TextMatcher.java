package com.github.acc15.glob.matchers;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
class TextMatcher implements Matcher {

    private final CharSequence sequence;

    public TextMatcher(CharSequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean matches(MatchContext context) {
        for (int i=0; i<sequence.length(); i++) {
            if (!context.hasNextChar()) {
                return false;
            }
            if (context.nextChar() != sequence.charAt(i)) {
                return false;
            }
        }
        return context.matchNext();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TextMatcher && ((TextMatcher)obj).sequence.equals(sequence);
    }

    @Override
    public int hashCode() {
        return sequence.hashCode();
    }
}
