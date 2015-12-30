package com.github.glob.scan;

import com.github.glob.Glob;

import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public interface PathScanner {
    void findMatches(Path path, Glob.Context context);
}
