package com.github.glob.scanners;

import java.nio.file.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
class MatchContext {

    private final Path path;
    private final Glob.Node node;
    private int namePosition;

    public MatchContext(Path path, Glob.Node node, int namePosition) {
        this.path = path;
        this.node = node;
        this.namePosition = namePosition;
    }

    public Path getPath() {
        return path;
    }

    public boolean hasSubPath() {
        return namePosition < path.getNameCount();
    }

    public Path getSubPath() {
        return path.subpath(namePosition, path.getNameCount());
    }

    public boolean matchNext(int nameOffset) {
        for (Glob.Node next : node.nextNodes.values()) {
            if (next.scanner.matches(new MatchContext(path, next, namePosition + nameOffset))) {
                return true;
            }
        }
        return false;
    }

    public int getPosition() {
        return namePosition;
    }
}
