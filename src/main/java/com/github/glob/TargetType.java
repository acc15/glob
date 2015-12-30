package com.github.glob;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public enum TargetType {
    ANY,
    DIRECTORY,
    FILE;

    public boolean matches(Path path) {
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
