package com.github.glob.scanners;

import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
class DummyScanner implements Scanner {
    @Override
    public void scan(Path path, ScanContext context) {
        context.scanNext(path);
    }

}
