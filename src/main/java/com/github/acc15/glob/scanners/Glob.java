package com.github.acc15.glob.scanners;

import com.github.acc15.glob.matchers.GlobPattern;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class Glob implements Predicate<Path> {

    private final Node root;

    private Glob(Node node) {
        this.root = node;
    }

    public boolean test(final Path path) {
        return new MatchContext(path, root, 0).matchNext(0);
    }

    public Set<Path> scan(final Path dir) throws IOException {
        final TreeSet<MatchedPath> matches = new TreeSet<>();
        RelativizingPathCollector collector = new RelativizingPathCollector(dir, new AppendingPathCollector(matches));
        new ScanContext(null, collector, root).scanNext(dir);
        return toPathSet(matches);
    }

    public Set<Path> scan(final Path dir, final Predicate<Path> predicate) throws IOException {
        final TreeSet<MatchedPath> matches = new TreeSet<>();
        RelativizingPathCollector collector = new RelativizingPathCollector(dir,
            new FilteringPathCollector(predicate, new AppendingPathCollector(matches)));
        new ScanContext(null, collector, root).scanNext(dir);
        return toPathSet(matches);
    }


    private static void addSequence(Node root, List<Scanner> scanners, int sequence) {
        Node node = root;
        for (Scanner scanner : scanners) {
            node = node.addNext(scanner);
        }
        node.addNext(Scanners.match(sequence));
    }


    static List<Scanner> parseSequence(String expression) {
        final List<Scanner> scanners = new ArrayList<>();
        final String[] segments = expression.split("/");
        for (String segment: segments) {
            if (segment.equals("**")) {
                scanners.add(Scanners.tree());
            } else if (GlobPattern.hasNonEscapedSpecialChars(segment)) {
                scanners.add(Scanners.pattern(segment));
            } else {
                scanners.add(Scanners.path(GlobPattern.unescape(segment)));
            }
        }
        return scanners;
    }

    public static Glob compile(Iterable<String> expressions) {
        final Node root = new Node(null);

        int seq = 0;
        for (String e: expressions) {
            addSequence(root, parseSequence(e), seq);
            ++seq;
        }
        return new Glob(root);
    }

    public static Glob compile(String expression, String... others) {
        final Node root = new Node(null);

        addSequence(root, parseSequence(expression), 0);

        int seq = 1;
        for (String e: others) {
            addSequence(root, parseSequence(e), seq);
            ++seq;
        }
        return new Glob(root);
    }

    public static Glob empty() {
        return new Glob(new Node(null));
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



    private static class RelativizingPathCollector implements PathCollector {
        private final PathCollector next;
        private final Path baseDir;

        public RelativizingPathCollector(Path baseDir, PathCollector next) {
            this.baseDir = baseDir;
            this.next = next;
        }

        @Override
        public void collect(Path path, int order) {
            Path relativePath = baseDir.relativize(path);
            next.collect(relativePath, order);
        }
    }

    private static class FilteringPathCollector implements PathCollector {
        private PathCollector next;
        private Predicate<Path> predicate;

        public FilteringPathCollector(Predicate<Path> predicate, PathCollector next) {
            this.predicate = predicate;
            this.next = next;
        }

        @Override
        public void collect(Path path, int order) {
            if (!predicate.test(path)) {
                return;
            }
            next.collect(path, order);
        }
    }

    private static class AppendingPathCollector implements PathCollector {
        private Collection<MatchedPath> collection;

        public AppendingPathCollector(Collection<MatchedPath> collection) {
            this.collection = collection;
        }

        @Override
        public void collect(Path path, int order) {
            collection.add(new MatchedPath(path, order));
        }
    }

    private static Set<Path> toPathSet(Collection<MatchedPath> matchedPaths) {
        return matchedPaths.stream().map(MatchedPath::getPath).collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
