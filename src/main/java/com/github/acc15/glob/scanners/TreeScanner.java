package com.github.acc15.glob.scanners;

import com.github.acc15.glob.GlobException;

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
        if (!Files.exists(context.getPath())) {
            return;
        }
        try (final Stream<Path> stream = Files.walk(context.getPath())) {
            stream.forEach(p -> callScan(context, p));
        } catch (IOException e) {
            throw new GlobException(e);
        }
    }

    void callScan(ScanContext context, Path p) {
        context.scanNext(p);
    }

    @Override
    public boolean matches(MatchContext context) {
        for (int i=0; context.isValidOffset(i); i++) {
            if (context.matchNext(i)) {
                return true;
            }
        }
        return false;
    }
}
