package com.github.glob.scanners;

import com.github.glob.GlobException;
import com.github.glob.matchers.Matcher;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class Scanners {

    private static final Scanner MATCH = (path, context) -> context.matchFound(path);

    private static final Scanner TREE = (path, context) -> {
        try (final Stream<Path> stream = Files.walk(path)) {
            stream.forEach(context::scanNext);
        } catch (IOException e) {
            throw new GlobException(e);
        }
    };

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

    public static Scanner pattern(List<Matcher> pattern) {
        return new PatternScanner(pattern);
    }


}
