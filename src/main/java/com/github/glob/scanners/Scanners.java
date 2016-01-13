package com.github.glob.scanners;

import com.github.glob.GlobException;
import com.github.glob.matchers.Pattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class Scanners {

    private static final Scanner MATCH = ScanContext::matchFound;

    private static final Scanner TREE = context -> {
        try (final Stream<Path> stream = Files.walk(context.getPath())) {
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

    public static Scanner pattern(Pattern pattern) {
        return new PatternScanner(pattern);
    }

    public static Scanner pattern(String pattern) {
        return pattern(Pattern.compile(pattern));
    }


}
