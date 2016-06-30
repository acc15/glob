package com.github.acc15.glob.scanners;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-12-01
 */
public class ScannersTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Path basePath;

    @Before
    public void setUp() throws Exception {
        basePath = temporaryFolder.getRoot().toPath();
    }

    @Test
    public void testSimpleScan() throws Exception {

        final Glob scanner = new Glob();

        scanner.addSequence(Collections.singletonList(Scanners.path("src", "main")));
        scanner.addSequence(Collections.singletonList(Scanners.path("src", "main", "java")));
        scanner.addSequence(Collections.singletonList(Scanners.path("src", "test", "java")));

        temporaryFolder.newFolder("src", "main", "java");
        temporaryFolder.newFolder("src", "test", "java");

        final Collection<Path> matched = scanner.scan(basePath);
        assertThat(matched).containsOnly(
            basePath.resolve(Paths.get("src", "main")),
            basePath.resolve(Paths.get("src", "main", "java")),
            basePath.resolve(Paths.get("src", "test", "java")));

    }

    @Test
    public void testTreeScan() throws Exception {
        final Glob scanner = new Glob();

        scanner.addSequence(Arrays.asList(Scanners.path("tree"), Scanners.tree()));

        temporaryFolder.newFolder("tree");
        temporaryFolder.newFolder("tree", "test");
        temporaryFolder.newFile("tree/a.txt");
        temporaryFolder.newFile("tree/test/b.txt");
        temporaryFolder.newFile("c.txt");

        final Path basePath = temporaryFolder.getRoot().toPath();

        final Collection<Path> matchedAny = scanner.scan(basePath);
        assertThat(matchedAny).hasSize(4);
        assertThat(matchedAny).containsOnly(
            basePath.resolve(Paths.get("tree", "a.txt")),
            basePath.resolve(Paths.get("tree", "test", "b.txt")),
            basePath.resolve(Paths.get("tree")),
            basePath.resolve(Paths.get("tree", "test")));

    }

    @Test
    public void testTreeScanShouldMatchFile() throws Exception {

        final Glob glob = new Glob(Scanners.path("tree"), Scanners.path("a.txt"), Scanners.tree());
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Collection<Path> matches = glob.scan(basePath);
        assertThat(matches).containsOnly(basePath.resolve(Paths.get("tree", "a.txt")));

    }

    @Test
    public void testTreeScanShouldSuggestInputPath() throws Exception {

        final Glob scanner = new Glob();

        scanner.addSequence(Arrays.asList(Scanners.path("tree"), Scanners.tree(), Scanners.path("a.txt")));
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Collection<Path> matchedFiles = scanner.scan(basePath);
        assertThat(matchedFiles).containsOnly(basePath.resolve(Paths.get("tree", "a.txt")));

    }
}