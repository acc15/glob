package com.github.acc15.glob.scanners;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class ScanContext {

    private final Path path;
    private final PathCollector collector;
    private final Glob.Node node;

    ScanContext(Path path, PathCollector collector, Glob.Node node) {
        this.path = path;
        this.collector = collector;
        this.node = node;
    }

    public Path getPath() {
        return path;
    }

    public void scanNext(Path path) {
        Collection<Glob.Node> nextNodes = node.nextNodes.values();
        for (Glob.Node next : nextNodes) {
            next.scanner.scan(new ScanContext(path, collector, next));
        }
    }

    public void matchFound(int order) {
        collector.collect(path, order);
    }

}
