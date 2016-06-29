package com.github.acc15.glob.scanners;

import com.github.acc15.glob.TargetType;
import com.github.acc15.glob.matchers.GlobPattern;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class Glob implements Predicate<Path> {

    private final Node root = new Node(null);

    public Glob() {
    }

    public Glob(Scanner... scanners) {
        addSequence(Arrays.asList(scanners));
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

    public boolean test(final Path path) {
        return new MatchContext(path, root, 0).matchNext(0);
    }

    public boolean test(final Path path, final Predicate<Path> predicate) {
        return predicate.test(path) && test(path);
    }

    public Set<Path> scan(final Path dir) throws IOException {
        final Set<Path> matches = new HashSet<>();
        new ScanContext(null, new RelativizingPathConsumer(dir, matches::add), root).scanNext(dir);
        return Collections.unmodifiableSet( matches );
    }

    public Set<Path> scan(final Path dir, final Predicate<Path> predicate) throws IOException {
        final Set<Path> matches = new HashSet<>();
        new ScanContext(null, new RelativizingPathConsumer(dir, new FilteringPathConsumer(predicate, matches::add)), root).scanNext(dir);
        return Collections.unmodifiableSet( matches );
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
        final Glob glob = new Glob();
        for (String e: expressions) {
            glob.addSequence(parseSequence(e));
        }
        return glob;
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

    static class RelativizingPathConsumer implements Consumer<Path> {
        private final Consumer<Path> next;
        private final Path baseDir;

        public RelativizingPathConsumer(Path baseDir, Consumer<Path> next) {
            this.baseDir = baseDir;
            this.next = next;
        }

        @Override
        public void accept(Path path) {
            Path relativePath = baseDir.relativize(path);
            next.accept(relativePath);
        }
    }

    static class FilteringPathConsumer implements Consumer<Path> {
        private Consumer<Path> next;
        private Predicate<Path> predicate;

        public FilteringPathConsumer(Predicate<Path> predicate, Consumer<Path> next) {
            this.predicate = predicate;
            this.next = next;
        }

        @Override
        public void accept(Path path) {
            if (!predicate.test(path)) {
                return;
            }
            next.accept(path);
        }
    }

}
