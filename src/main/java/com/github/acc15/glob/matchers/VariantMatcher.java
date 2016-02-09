package com.github.acc15.glob.matchers;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
class VariantMatcher implements Matcher {

    private final Iterable<Matcher> variants;

    public VariantMatcher(Iterable<Matcher> variants) {
        this.variants = variants;
    }

    @Override
    public boolean matches(MatchContext context) {
        for (Matcher variant: variants) {
            if (variant.matches(context.copy())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return VariantMatcher.class.hashCode() ^ variants.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VariantMatcher && ((VariantMatcher)obj).variants.equals(variants);
    }
}
