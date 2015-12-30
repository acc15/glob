package com.github.glob;

import com.github.glob.scan.SimpleScanner;
import com.github.glob.scan.TreeScanner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2015-30-12
 */
public class GlobScannerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testSimpleScan() throws Exception {

        final Glob scanner = new Glob();

        scanner.addSequence(Arrays.asList(new SimpleScanner("src"), new SimpleScanner("main")));
        scanner.addSequence(Arrays.asList(new SimpleScanner("src"), new SimpleScanner("main"), new SimpleScanner("java")));
        scanner.addSequence(Arrays.asList(new SimpleScanner("src"), new SimpleScanner("test"), new SimpleScanner("java")));

        temporaryFolder.newFolder("src", "main", "java");
        temporaryFolder.newFolder("src", "test", "java");

        final Path basePath = temporaryFolder.getRoot().toPath();
        final Set<Path> matched = scanner.scan(basePath, TargetType.DIRECTORY);
        assertThat(matched).contains(basePath.resolve(Paths.get("src", "main")));
        assertThat(matched).contains(basePath.resolve(Paths.get("src", "main", "java")));
        assertThat(matched).contains(basePath.resolve(Paths.get("src", "test", "java")));

    }

    @Test
    public void testTreeScan() throws Exception {
        final Glob scanner = new Glob();

        scanner.addSequence(Arrays.asList(new SimpleScanner("tree"), new TreeScanner()));

        temporaryFolder.newFolder("tree");
        temporaryFolder.newFolder("tree", "test");
        temporaryFolder.newFile("tree/a.txt");
        temporaryFolder.newFile("tree/test/b.txt");
        temporaryFolder.newFile("c.txt");

        final Path basePath = temporaryFolder.getRoot().toPath();

        final Set<Path> matchedFiles = scanner.scan(basePath, TargetType.FILE);
        assertThat(matchedFiles).hasSize(2);
        assertThat(matchedFiles).contains(basePath.resolve(Paths.get("tree", "a.txt")));
        assertThat(matchedFiles).contains(basePath.resolve(Paths.get("tree", "test", "b.txt")));

        final Set<Path> matchedDirs = scanner.scan(basePath, TargetType.DIRECTORY);
        assertThat(matchedDirs).hasSize(1);
        assertThat(matchedDirs).contains(basePath.resolve(Paths.get("tree", "test")));

        final Set<Path> matchedAny = scanner.scan(basePath, TargetType.ANY);
        assertThat(matchedAny).hasSize(3);
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree", "a.txt")));
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree", "test", "b.txt")));
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree", "test")));

    }
}