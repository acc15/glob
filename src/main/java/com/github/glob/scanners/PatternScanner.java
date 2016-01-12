package com.github.glob.scanners;

import com.github.glob.GlobException;
import com.github.glob.matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-12-01
 */
public class PatternScanner implements Scanner {

    private final List<Matcher> pattern;

    public PatternScanner(List<Matcher> pattern) {
        this.pattern = pattern;
    }

    @Override
    public void scan(Path path, ScanContext context) {
        try (Stream<Path> stream = Files.list(path)) {
            stream.forEach(p -> {
                final String name = p.getFileName().toString();
                if (Matchers.matches(pattern, name)) {
                    context.scanNext(path.resolve(name));
                }
            });
        } catch (IOException e) {
            throw new GlobException(e);
        }
    }
}
