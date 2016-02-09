package com.github.acc15.glob.scanners;

import com.github.acc15.glob.GlobException;
import com.github.acc15.glob.matchers.GlobPattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-12-01
 */
class PatternScanner implements Scanner {

    private final GlobPattern pattern;

    public PatternScanner(GlobPattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public void scan(ScanContext context) {
        if (!Files.isDirectory(context.getPath())) {
            return;
        }
        try (Stream<Path> stream = Files.list(context.getPath())) {
            stream.forEach(p -> {
                final String name = p.getFileName().toString();
                if (pattern.test(name)) {
                    context.scanNext(context.getPath().resolve(name));
                }
            });
        } catch (IOException e) {
            throw new GlobException(e);
        }
    }

    @Override
    public boolean matches(MatchContext context) {
        return context.firstNameMatches(pattern) && context.matchNext(1);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PatternScanner && ((PatternScanner)obj).pattern.equals(pattern);
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
    }
}
