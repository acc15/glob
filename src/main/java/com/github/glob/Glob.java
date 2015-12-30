package com.github.glob;

import java.nio.file.Path;
import java.util.Set;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public interface Glob {

    Set<Path> scan(Path path);

}
