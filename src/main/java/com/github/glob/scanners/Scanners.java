package com.github.glob.scanners;

import com.github.glob.matchers.Pattern;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class Scanners {

    private static final Scanner MATCH = new MatchScanner();

    private static final Scanner TREE = new TreeScanner();

    public static Scanner tree() {
        return TREE;
    }

    public static Scanner match() {
        return MATCH;
    }

    public static Scanner path(Path subPath) {
        return new PathScanner(subPath);
    }

    public static Scanner path(String first, String... more) {
        return path(Paths.get(first, more));
    }

    public static Scanner pattern(Pattern pattern) {
        return new PatternScanner(pattern);
    }

    public static Scanner pattern(String pattern) {
        return pattern(Pattern.compile(pattern));
    }


}
