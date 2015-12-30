package com.github.glob;

import com.github.glob.scan.EmptyScanner;
import com.github.glob.scan.PathScanner;

import java.nio.file.Path;
import java.util.*;

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

    public Set<Path> scan(final Path path, final TargetType target) {
        final Set<Path> matchSet = new HashSet<>();
        new Context(matchSet, root, target).scanNext(path);
        return matchSet;
    }

    static class Node {

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
        private final TargetType targetType;

        Context(Set<Path> matchedPaths, Node node, TargetType target) {
            this.matchedPaths = matchedPaths;
            this.node = node;
            this.targetType = target;
        }

        public void scanNext(Path path) {
            if (node.nextNodes.isEmpty()) {
                if (targetType.matches(path)) {
                    matchedPaths.add(path);
                }
                return;
            }
            for (Node next : node.nextNodes.values()) {
                next.scanner.findMatches(path, new Context(matchedPaths, next, targetType));
            }
        }

    }
}
