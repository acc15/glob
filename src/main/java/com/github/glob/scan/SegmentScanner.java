package com.github.glob.scan;

import com.github.glob.Glob;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
class SegmentScanner implements PathScanner {

    private final String segment;

    public SegmentScanner(String segment) {
        this.segment = segment;
    }

    @Override
    public void findMatches(Path path, Glob.Context context) {
        final Path resolved = path.resolve(segment);
        if (Files.exists(resolved)) {
            context.scanNext(resolved);
        }
    }

    @Override
    public int hashCode() {
        return segment.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SegmentScanner && ((SegmentScanner)obj).segment.equals(segment);
    }
}
