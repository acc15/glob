package com.github.glob.scan;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class Scanners {

    private static final MatchScanner MATCH = new MatchScanner();
    private static final TreeScanner TREE = new TreeScanner();

    public static PathScanner tree() {
        return TREE;
    }

    public static PathScanner match() {
        return MATCH;
    }

    public static PathScanner segment(String segment) {
        return new SegmentScanner(segment);
    }

}
