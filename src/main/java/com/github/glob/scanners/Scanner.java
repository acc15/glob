package com.github.glob.scanners;

import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public interface Scanner {
    void scan(Path path, ScanContext context);
}
