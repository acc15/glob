package com.github.acc15.glob.scanners;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public interface Scanner {
    void scan(ScanContext context);
    boolean matches(MatchContext context);
}
