package com.github.glob.scanners;

import java.nio.file.Path;
import java.util.Set;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class ScanContext {

    private final Path path;
    private final Set<Path> matches;
    private final Glob.Node node;

    ScanContext(Path path, Set<Path> matches, Glob.Node node) {
        this.path = path;
        this.matches = matches;
        this.node = node;
    }

    public Path getPath() {
        return path;
    }

    public void scanNext(Path path) {
        for (Glob.Node next : node.nextNodes.values()) {
            next.scanner.scan(new ScanContext(path, matches, next));
        }
    }

    public void matchFound() {
        matches.add(path);
    }

}
