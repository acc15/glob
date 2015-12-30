package com.github.glob.scan.scanners;

import com.github.glob.scan.PathScanner;
import com.github.glob.scan.ScanContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class SimpleScanner implements PathScanner {

    private final String segment;

    public SimpleScanner(String segment) {
        this.segment = segment;
    }

    @Override
    public void findMatches(Path path, ScanContext context) {
        final Path resolved = path.resolve(Paths.get(segment));
        if (Files.exists(resolved)) {
            context.scanNext(resolved);
        }
    }
}
