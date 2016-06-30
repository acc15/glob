package com.github.acc15.glob.scanners;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Created by acc15 on 30.06.2016.
 */
public class MatchedPath implements Comparable<MatchedPath> {
    private final Path path;
    private final int order;

    public MatchedPath(Path path, int order) {
        this.path = path;
        this.order = order;
    }

    public Path getPath() {
        return path;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public int compareTo(MatchedPath o) {
        int order = Integer.compare(this.order, o.order);
        return order == 0 ? path.compareTo(o.path) : order;
    }

}
