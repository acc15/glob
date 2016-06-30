package com.github.acc15.glob.scanners;

import java.nio.file.Path;

/**
 * Created by acc15 on 30.06.2016.
 */
interface PathCollector {

    void collect(Path path, int order);

}
