package com.github.glob.scanners;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public class GlobTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testCompile() throws Exception {

        final Glob glob = Glob.compile("src/main/cpp/**/*.c{,xx,pp}");

        temporaryFolder.newFolder("src", "main", "cpp", "dir1");
        temporaryFolder.newFile("src/main/cpp/a.c");
        temporaryFolder.newFile("src/main/cpp/a.cpp");
        temporaryFolder.newFile("src/main/cpp/b.tmp");

        temporaryFolder.newFile("src/main/cpp/dir1/b.c");
        temporaryFolder.newFile("src/main/cpp/dir1/g.tmp");

        final Path basePath = temporaryFolder.getRoot().toPath();
        final Set<Path> matches = glob.scan(basePath);

        assertThat(matches).hasSize(3);

        assertThat(matches).contains(basePath.resolve(Paths.get("src", "main", "cpp", "a.c")));
        assertThat(matches).contains(basePath.resolve(Paths.get("src", "main", "cpp", "a.cpp")));
        assertThat(matches).contains(basePath.resolve(Paths.get("src", "main", "cpp", "dir1", "b.c")));

    }
}