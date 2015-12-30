package com.github.glob.scan;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
class PathScannerNode {

    final PathScanner matcher;
    final Map<PathScanner, PathScannerNode> nextNodes = new HashMap<>();

    public PathScannerNode(PathScanner matcher) {
        this.matcher = matcher;
    }

    public PathScannerNode addNext(PathScanner matcher) {
        PathScannerNode nextNode = nextNodes.get(matcher);
        if (nextNode != null) {
            return nextNode;
        }
        nextNode = new PathScannerNode(matcher);
        nextNodes.put(matcher, nextNode);
        return nextNode;
    }

}
