package com.github.glob.scanners;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class Scanners {

    private static final MatchScanner MATCH = new MatchScanner();
    private static final TreeScanner TREE = new TreeScanner();

    public static Scanner tree() {
        return TREE;
    }

    public static Scanner match() {
        return MATCH;
    }

    public static Scanner subPath(String first, String... more) {
        return new PathScanner(Paths.get(first, more));
    }

    public static Scanner subPath(Path subPath) {
        return new PathScanner(subPath);
    }

}
