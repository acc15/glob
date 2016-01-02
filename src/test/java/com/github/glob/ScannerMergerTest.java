package com.github.glob;

import com.github.glob.scanners.ScannerMerger;
import com.github.glob.scanners.Scanners;
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
 * @since 2015-30-12
 */
public class ScannerMergerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testSimpleScan() throws Exception {

        final ScannerMerger scanner = new ScannerMerger();

        scanner.addSequence(Collections.singletonList(Scanners.path("src", "main")));
        scanner.addSequence(Collections.singletonList(Scanners.path("src", "main", "java")));
        scanner.addSequence(Collections.singletonList(Scanners.path("src", "test", "java")));

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
        final ScannerMerger scanner = new ScannerMerger();

        scanner.addSequence(Arrays.asList(Scanners.path("tree"), tree()));

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