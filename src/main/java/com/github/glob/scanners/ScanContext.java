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
    private final ScannerMerger.Node node;
    private final Predicate<Path> matchPredicate;

    ScanContext(Set<Path> matchedPaths, ScannerMerger.Node node, Predicate<Path> target) {
        this.matchedPaths = matchedPaths;
        this.node = node;
        this.matchPredicate = target;
    }

    public void scanNext(Path path) {
        for (ScannerMerger.Node next : node.nextNodes.values()) {
            next.scanner.scan(path, new ScanContext(matchedPaths, next, matchPredicate));
        }
    }

    public void matchFound(Path path) {
        if (matchPredicate.test(path)) {
            matchedPaths.add(path);
        }
    }

}
