package com.github.acc15.glob.scanners;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class ScanContext {

    private final Path path;
    private final Consumer<Path> consumer;
    private final Glob.Node node;

    ScanContext(Path path, Consumer<Path> consumer, Glob.Node node) {
        this.path = path;
        this.consumer = consumer;
        this.node = node;
    }

    public Path getPath() {
        return path;
    }

    public void scanNext(Path path) {
        for (Glob.Node next : node.nextNodes.values()) {
            next.scanner.scan(new ScanContext(path, consumer, next));
        }
    }

    public void matchFound() {
        consumer.accept(path);
    }

}
