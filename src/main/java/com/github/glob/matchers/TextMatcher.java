package com.github.glob.matchers;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-02-01
 */
public class TextMatcher implements Matcher {

    private final CharSequence text;

    public TextMatcher(CharSequence text) {
        this.text = text;
    }

    @Override
    public boolean matches(MatchContext context) {
        for (int i=0; i<text.length(); i++) {
            if (!context.hasNextChar()) {
                return false;
            }
            if (context.nextChar() != text.charAt(i)) {
                return false;
            }
        }
        return context.matchNext();
    }
}
