package com.github.glob.matchers;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-02-01
 */
public class ZeroOrMoreMatcher implements Matcher {
    @Override
    public boolean matches(MatchContext context) {
        while (!context.matchNext()) {
            if (!context.hasNextChar()) {
                return false;
            }
            context.nextChar();
        }
        return true;

    }
}
