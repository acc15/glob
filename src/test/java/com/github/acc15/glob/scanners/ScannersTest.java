package com.github.acc15.glob.scanners;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

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

        final Glob scanner = Glob.compile("src/main", "src/main/java", "src/test/java");

        temporaryFolder.newFolder("src", "main", "java");
        temporaryFolder.newFolder("src", "test", "java");

        final Collection<Path> matched = scanner.scan(basePath);
        assertThat(matched).containsOnly(
            Paths.get("src", "main"),
            Paths.get("src", "main", "java"),
            Paths.get("src", "test", "java"));

    }

    @Test
    public void testTreeScan() throws Exception {
        final Glob scanner = Glob.compile("tree/**");
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFolder("tree", "test");
        temporaryFolder.newFile("tree/a.txt");
        temporaryFolder.newFile("tree/test/b.txt");
        temporaryFolder.newFile("c.txt");

        final Path basePath = temporaryFolder.getRoot().toPath();

        final Collection<Path> matchedAny = scanner.scan(basePath);
        assertThat(matchedAny).hasSize(4);
        assertThat(matchedAny).containsOnly(
            Paths.get("tree", "a.txt"),
            Paths.get("tree", "test", "b.txt"),
            Paths.get("tree"),
            Paths.get("tree", "test"));

    }

    @Test
    public void testTreeScanShouldMatchFile() throws Exception {

        final Glob glob = Glob.compile("tree/a.txt/**");
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Collection<Path> matches = glob.scan(basePath);
        assertThat(matches).containsOnly(Paths.get("tree", "a.txt"));
    }

    @Test
    public void testTreeScanShouldSuggestInputPath() throws Exception {

        final Glob scanner = Glob.compile("tree/**/a.txt");
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Collection<Path> matchedFiles = scanner.scan(basePath);
        assertThat(matchedFiles).containsOnly(Paths.get("tree", "a.txt"));

    }
}