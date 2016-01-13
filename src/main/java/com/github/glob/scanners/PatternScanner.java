package com.github.glob.scanners;

import com.github.glob.GlobException;
import com.github.glob.matchers.Pattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-12-01
 */
public class PatternScanner implements Scanner {

    private final Pattern pattern;

    public PatternScanner(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public void scan(Path path, ScanContext context) {
        try (Stream<Path> stream = Files.list(path)) {
            stream.forEach(p -> {
                final String name = p.getFileName().toString();
                if (pattern.matches(name)) {
                    context.scanNext(path.resolve(name));
                }
            });
        } catch (IOException e) {
            throw new GlobException(e);
        }
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
