package com.github.glob;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public enum TargetType implements Predicate<Path> {
    ANY,
    DIRECTORY,
    FILE;

    @Override
    public boolean test(Path path) {
        switch (this){
            case ANY:
                return Files.exists(path);

            case DIRECTORY:
                return Files.isDirectory(path);

            case FILE:
                return Files.isRegularFile(path);

            default:
                throw new IllegalArgumentException("Unknown target type: " + this);
        }
    }
}
