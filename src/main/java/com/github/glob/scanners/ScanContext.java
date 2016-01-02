package com.github.glob.scanners;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
class ScanContext {

    private final Set<Path> matchedPaths;
    private final Glob.Node node;
    private final Predicate<Path> matchPredicate;

    ScanContext(Set<Path> matchedPaths, Glob.Node node, Predicate<Path> target) {
        this.matchedPaths = matchedPaths;
        this.node = node;
        this.matchPredicate = target;
    }

    public void scanNext(Path path) {
        if (node.nextNodes.isEmpty()) {
            if (matchPredicate.test(path)) {
                matchedPaths.add(path);
            }
            return;
        }
        for (Glob.Node next : node.nextNodes.values()) {
            next.scanner.scan(path, new ScanContext(matchedPaths, next, matchPredicate));
        }
    }

}
