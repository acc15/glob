package com.github.glob.matchers;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public class VariantMatcher implements Matcher {

    private final Iterable<Matcher> variants;

    public VariantMatcher(Iterable<Matcher> variants) {
        this.variants = variants;
    }

    @Override
    public boolean matches(MatchContext context) {
        for (Matcher variant: variants) {
            if (variant.matches(new MatchContext(context))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return variants.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VariantMatcher && ((VariantMatcher)obj).variants.equals(variants);
    }
}
