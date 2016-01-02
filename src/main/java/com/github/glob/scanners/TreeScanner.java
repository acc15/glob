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
        /*
            Files.walkFileTree() is not suitable because:
            1. it doesnt enumerates directories (only in pre/postVisitDirectory)
            2. pre/postVisitDirectory is also called for root directory
        */
        walk(path, context);
    }

    private void walk(Path dir, ScanContext context) {
        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(p -> {
                if (Files.isDirectory(p)) {
                    walk(p, context);
                }
                context.scanNext(p);
            });
        } catch (IOException e) {
            throw new GlobException(e);
        }

    }
}
