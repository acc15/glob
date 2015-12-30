package com.github.glob.scan;

import com.github.glob.Glob;

import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
class MatchScanner implements PathScanner {
    @Override
    public void findMatches(Path path, Glob.Context context) {
        context.scanNext(path);
    }

}
