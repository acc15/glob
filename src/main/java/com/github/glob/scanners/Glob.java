package com.github.glob.scanners;

import com.github.glob.matchers.Pattern;

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

    public Glob() {
    }

    public Glob(List<Scanner> scanners) {
        addSequence(scanners);
    }

    public Glob(Set<List<Scanner>> scannerSet) {
        scannerSet.forEach(this::addSequence);
    }

    public void addSequence(List<Scanner> scanners) {
        Node node = root;
        for (Scanner scanner : scanners) {
            node = node.addNext(scanner);
        }
        node.addNext(Scanners.match());
    }

    public Set<Path> scan(final Path path, final Predicate<Path> predicate) throws IOException {
        final Set<Path> matchedPaths = new HashSet<>();
        new ScanContext(matchedPaths, root, predicate).scanNext(path);
        return matchedPaths;
    }

    static List<Scanner> parseSequence(String expression) {
        final List<Scanner> scanners = new ArrayList<>();
        final String[] segments = expression.split("/");
        for (String segment: segments) {
            if (segment.equals("**")) {
                scanners.add(Scanners.tree());
            } else if (Pattern.hasNonEscapedSpecialChars(segment)) {
                scanners.add(Scanners.pattern(segment));
            } else {
                scanners.add(Scanners.path(Pattern.unescape(segment)));
            }
        }
        return scanners;
    }

    public static Glob compile(String expression, String... others) {
        final Glob glob = new Glob(parseSequence(expression));
        for (String e: others) {
            glob.addSequence(parseSequence(e));
        }
        return glob;
    }

    static class Node {

        final Scanner scanner;
        final Map<Scanner, Node> nextNodes = new HashMap<>();

        public Node(Scanner scanner) {
            this.scanner = scanner;
        }

        public Node addNext(Scanner matcher) {
            Node nextNode = nextNodes.get(matcher);
            if (nextNode != null) {
                return nextNode;
            }
            nextNode = new Node(matcher);
            nextNodes.put(matcher, nextNode);
            return nextNode;
        }

    }

}
