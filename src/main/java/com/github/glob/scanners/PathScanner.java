package com.github.glob.scanners;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
class PathScanner implements Scanner {

    private final Path path;

    public PathScanner(Path path) {
        this.path = path;
    }

    @Override
    public void scan(Path path, ScanContext context) {
        final Path resolved = path.resolve(this.path);
        if (Files.exists(resolved)) {
            context.scanNext(resolved);
        }
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PathScanner && ((PathScanner)obj).path.equals(path);
    }
}
