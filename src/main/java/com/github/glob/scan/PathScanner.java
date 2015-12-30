package com.github.glob.scan;

import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public interface PathScanner {
    void findMatches(Path path, ScanContext context);
}
