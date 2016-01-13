package com.github.glob.scanners;

import com.github.glob.GlobException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
class TreeScanner implements Scanner {
    @Override
    public void scan(ScanContext context) {
        try (final Stream<Path> stream = Files.walk(context.getPath())) {
            stream.forEach(context::scanNext);
        } catch (IOException e) {
            throw new GlobException(e);
        }
    }

    @Override
    public boolean matches(MatchContext context) {
        for (int i=0; context.getPosition() + i <= context.getPath().getNameCount(); i++) {
            if (context.matchNext(i)) {
                return true;
            }
        }
        return false;
    }
}
