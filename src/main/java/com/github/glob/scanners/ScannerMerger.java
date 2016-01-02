package com.github.glob.scanners;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class ScannerMerger {

    private final Node root = new Node(null);

    public void addSequence(List<Scanner> scanners) {
        Node node = root;
        final Iterator<Scanner> iter = scanners.iterator();
        while (iter.hasNext()) {
            final Scanner scanner = iter.next();
            node = node.addNext(scanner, !iter.hasNext());
        }
    }

    public Set<Path> scan(final Path path, final Predicate<Path> predicate) throws IOException {
        final Set<Path> matchSet = new HashSet<>();
        new ScanContext(matchSet, root, predicate).scanNext(path);
        return matchSet;
    }

    static class Node {

        final Scanner scanner;
        final Map<Scanner, Node> nextNodes = new HashMap<>();

        public Node(Scanner scanner) {
            this.scanner = scanner;
        }

        public Node addNext(Scanner matcher, boolean last) {
            Node nextNode = nextNodes.get(matcher);
            if (nextNode != null) {
                if (nextNode.nextNodes.isEmpty() || last) {
                    nextNode.addNext(Scanners.dummy(), false);
                }
                return nextNode;
            }
            nextNode = new Node(matcher);
            nextNodes.put(matcher, nextNode);
            return nextNode;
        }

    }

}
