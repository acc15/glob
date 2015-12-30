package com.github.glob;

import com.github.glob.scan.SimpleScanner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
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
    public void testScan() throws Exception {

        final Glob scanner = new Glob();

        scanner.addSequence(Arrays.asList(new SimpleScanner("src"), new SimpleScanner("main")));
        scanner.addSequence(Arrays.asList(new SimpleScanner("src"), new SimpleScanner("main"), new SimpleScanner("java")));
        scanner.addSequence(Arrays.asList(new SimpleScanner("src"), new SimpleScanner("test"), new SimpleScanner("java")));

        temporaryFolder.newFolder("src", "main", "java");
        temporaryFolder.newFolder("src", "test", "java");

        final Set<Path> matched = scanner.scan(temporaryFolder.getRoot().toPath(), TargetType.DIRECTORY);
        assertThat(matched).hasSize(3);


    }
}