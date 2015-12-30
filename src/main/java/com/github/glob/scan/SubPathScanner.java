package com.github.glob.scan;

import com.github.glob.Glob;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
class SubPathScanner implements PathScanner {

    private final Path subPath;

    public SubPathScanner(Path subPath) {
        this.subPath = subPath;
    }

    @Override
    public void findMatches(Path path, Glob.Context context) {
        final Path resolved = path.resolve(subPath);
        if (Files.exists(resolved)) {
            context.scanNext(resolved);
        }
    }

    @Override
    public int hashCode() {
        return subPath.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SubPathScanner && ((SubPathScanner)obj).subPath.equals(subPath);
    }
}
