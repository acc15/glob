package com.github.glob.scanners;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static com.github.glob.scanners.Scanners.tree;
import static org.fest.assertions.api.Assertions.assertThat;

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

        final Set<Path> matched = scanner.scan(basePath);
        assertThat(matched).contains(basePath.resolve(Paths.get("src", "main")));
        assertThat(matched).contains(basePath.resolve(Paths.get("src", "main", "java")));
        assertThat(matched).contains(basePath.resolve(Paths.get("src", "test", "java")));

    }

    @Test
    public void testTreeScan() throws Exception {
        final Glob scanner = new Glob();

        scanner.addSequence(Arrays.asList(Scanners.path("tree"), tree()));

        temporaryFolder.newFolder("tree");
        temporaryFolder.newFolder("tree", "test");
        temporaryFolder.newFile("tree/a.txt");
        temporaryFolder.newFile("tree/test/b.txt");
        temporaryFolder.newFile("c.txt");

        final Path basePath = temporaryFolder.getRoot().toPath();

        final Set<Path> matchedAny = scanner.scan(basePath);
        assertThat(matchedAny).hasSize(4);
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree", "a.txt")));
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree", "test", "b.txt")));
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree")));
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree", "test")));

    }

    @Test
    public void testTreeScanShouldMatchFile() throws Exception {

        final Glob glob = new Glob(Scanners.path("tree"), Scanners.path("a.txt"), tree());
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Set<Path> matches = glob.scan(basePath);
        assertThat(matches).contains(basePath.resolve(Paths.get("tree", "a.txt")));

    }

    @Test
    public void testTreeScanShouldSuggestInputPath() throws Exception {

        final Glob scanner = new Glob();

        scanner.addSequence(Arrays.asList(Scanners.path("tree"), tree(), Scanners.path("a.txt")));
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Set<Path> matchedFiles = scanner.scan(basePath);
        assertThat(matchedFiles).isEqualTo(Collections.singleton(basePath.resolve(Paths.get("tree", "a.txt"))));

    }
}