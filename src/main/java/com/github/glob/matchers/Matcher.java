package com.github.glob.matchers;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-02-01
 */
public interface Matcher {

    boolean matches(MatchContext context);

}
