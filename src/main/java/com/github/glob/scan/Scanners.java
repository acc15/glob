package com.github.glob.scan;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    public static PathScanner subPath(String first, String... more) {
        return new SubPathScanner(Paths.get(first, more));
    }

    public static PathScanner subPath(Path subPath) {
        return new SubPathScanner(subPath);
    }

}
