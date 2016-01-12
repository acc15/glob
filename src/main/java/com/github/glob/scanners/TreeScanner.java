package com.github.glob.scanners;

import com.github.glob.GlobException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
class TreeScanner implements Scanner {
    @Override
    public void scan(Path path, ScanContext context) {
        try (final Stream<Path> stream = Files.walk(path)) {
            stream.forEach(context::scanNext);
        } catch (IOException e) {
            throw new GlobException(e);
        }
    }
}
