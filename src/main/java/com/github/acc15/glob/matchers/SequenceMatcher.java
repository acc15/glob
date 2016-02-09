package com.github.acc15.glob.matchers;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
class SequenceMatcher implements Matcher {

    private final List<Matcher> matchers;

    public SequenceMatcher(List<Matcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean matches(MatchContext context) {
        return context.derive(matchers).matchNext();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SequenceMatcher && ((SequenceMatcher)obj).matchers.equals(matchers);
    }

    @Override
    public int hashCode() {
        return SequenceMatcher.class.hashCode() ^ matchers.hashCode();
    }
}
