package com.github.glob.scan;

import com.github.glob.Glob;
import com.github.glob.GlobException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
class TreeScanner implements PathScanner {
    @Override
    public void findMatches(Path path, Glob.Context context) {
        /*
            Files.walkFileTree() is not suitable because:
            1. it doesnt enumerates directories (only in pre/postVisitDirectory)
            2. pre/postVisitDirectory is also called for root directory
        */
        walk(path, context);
    }

    private void walk(Path dir, Glob.Context context) {
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
