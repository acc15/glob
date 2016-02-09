package com.github.acc15.glob.scanners;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
class MatchScanner implements Scanner {
    @Override
    public void scan(ScanContext context) {
        context.matchFound();
    }

    @Override
    public boolean matches(MatchContext context) {
        return context.getPosition() == context.getPath().getNameCount();
    }
}
