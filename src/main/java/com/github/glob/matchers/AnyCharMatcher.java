package com.github.glob.matchers;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-02-01
 */
public class AnyCharMatcher implements Matcher {
    @Override
    public boolean matches(MatchContext context) {
        if (!context.hasNextChar()) {
            return false;
        }
        context.nextChar();
        return context.matchNext();
    }
}
