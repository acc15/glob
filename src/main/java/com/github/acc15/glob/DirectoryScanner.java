package com.github.acc15.glob;

import com.github.acc15.glob.scanners.Glob;

import java.nio.file.Path;
import java.util.Set;

/**
 * Created by acc15 on 30.06.2016.
 */
public class DirectoryScanner {

    private Glob includes = Glob.compile("**/*");
    private Glob excludes = Glob.empty();
    private Path dir;



}
