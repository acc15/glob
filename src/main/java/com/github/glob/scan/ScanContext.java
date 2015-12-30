package com.github.glob.scan;

import java.nio.file.Path;
import java.util.Set;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class ScanContext {

    private final Set<Path> matchedPaths;
    private final PathScannerNode node;

    ScanContext(Set<Path> matchedPaths, PathScannerNode node) {
        this.matchedPaths = matchedPaths;
        this.node = node;
    }

    public void scanNext(Path path) {
        if (node.nextNodes.isEmpty()) {
            matchedPaths.add(path);
            return;
        }
        for (PathScannerNode next : node.nextNodes.values()) {
            node.matcher.findMatches(path, new ScanContext(matchedPaths, next));
        }
    }

}
