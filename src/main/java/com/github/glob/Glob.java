package com.github.glob;

import com.github.glob.scan.EmptyScanner;
import com.github.glob.scan.PathScanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class Glob {

    private final Node root = new Node(null);

    public void addSequence(List<PathScanner> scanners) {
        Node node = root;
        final Iterator<PathScanner> iter = scanners.iterator();
        while (iter.hasNext()) {
            final PathScanner scanner = iter.next();
            node = node.addNext(scanner, !iter.hasNext());
        }
    }

    public Set<Path> scan(final Path path, final Predicate<Path> predicate) throws IOException {
        final Set<Path> matchSet = new HashSet<>();
        new Context(matchSet, root, predicate).scanNext(path);
        return matchSet;
    }

    private static class Node {

        final PathScanner scanner;
        final Map<PathScanner, Node> nextNodes = new HashMap<>();

        public Node(PathScanner scanner) {
            this.scanner = scanner;
        }

        public Node addNext(PathScanner matcher, boolean last) {
            Node nextNode = nextNodes.get(matcher);
            if (nextNode != null) {
                if (nextNode.nextNodes.isEmpty() || last) {
                    nextNode.addNext(EmptyScanner.getInstance(), false);
                }
                return nextNode;
            }
            nextNode = new Node(matcher);
            nextNodes.put(matcher, nextNode);
            return nextNode;
        }

    }

    /**
     * @author Vyacheslav Mayorov
     * @since 2015-30-12
     */
    public static class Context {

        private final Set<Path> matchedPaths;
        private final Node node;
        private final Predicate<Path> matchPredicate;

        Context(Set<Path> matchedPaths, Node node, Predicate<Path> target) {
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
            for (Node next : node.nextNodes.values()) {
                next.scanner.findMatches(path, new Context(matchedPaths, next, matchPredicate));
            }
        }

    }
}
