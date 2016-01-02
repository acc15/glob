package com.github.glob.matchers;

import java.util.List;

/**
 * *{abc,xyz}.cpp
 *
 * abcabcxyz.cpp
 *
 * @author Vyacheslav Mayorov
 * @since 2016-02-01
 */
public class VariantMatcher implements Matcher {

    private List<Matcher> variants;

    public VariantMatcher(List<Matcher> variants) {
        this.variants = variants;
    }

    @Override
    public boolean matches(MatchContext context) {
        for (Matcher variant: variants) {
            if (context.matchBy(variant)) {
                return true;
            }
        }
        return false;
    }
}
