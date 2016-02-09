package com.github.acc15.glob.scanners;

import java.nio.file.Path;
import java.util.function.Predicate;

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

    public boolean startsWith(Path subPath) {
        return hasSubPath() && getSubPath().startsWith(subPath);
    }

    public boolean firstNameMatches(Predicate<CharSequence> predicate) {
        return hasSubPath() && predicate.test(getPath().getName(namePosition).toString());
    }

    public boolean isValidOffset(int offset) {
        return offset + namePosition <= path.getNameCount();
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
