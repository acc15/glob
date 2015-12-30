package com.github.glob.scan;

import com.github.glob.Glob;

import java.nio.file.Path;
import java.util.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class GlobScanner implements Glob {

    private final PathScannerNode root = new PathScannerNode(null);

    public void addSequence(List<PathScanner> scanners) {
        PathScannerNode node = root;
        for (PathScanner scanner: scanners) {
            node = node.addNext(scanner);
        }
    }

    public Set<Path> scan(final Path path) {
        final Set<Path> matchSet = new HashSet<>();
        new ScanContext(matchSet, root).scanNext(path);
        return matchSet;
    }

}
