package com.github.acc15.glob.scanners;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
class MatchScanner implements Scanner {

    private int order;

    public MatchScanner(int order) {
        this.order = order;
    }

    @Override
    public void scan(ScanContext context) {
        context.matchFound(order);
    }

    @Override
    public boolean matches(MatchContext context) {
        return context.getPosition() == context.getPath().getNameCount();
    }
}
