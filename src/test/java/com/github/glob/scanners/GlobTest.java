package com.github.glob.scanners;

import org.fest.assertions.core.Condition;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public class GlobTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Path basePath;

    @Before
    public void setUp() throws Exception {
        basePath = temporaryFolder.getRoot().toPath();
    }

    private class MatchCondition extends Condition<Path> {
        private final Glob glob;

        public MatchCondition(Glob glob) {
            this.glob = glob;
        }

        @Override
        public boolean matches(Path value) {
            return glob.test(basePath.relativize(value));
        }
    }

    @Test
    public void testCompile() throws Exception {

        final Glob glob = Glob.compile("src/main/cpp/**/*.c{,xx,pp}");

        temporaryFolder.newFolder("src", "main", "cpp", "dir1");
        temporaryFolder.newFile("src/main/cpp/a.c");
        temporaryFolder.newFile("src/main/cpp/a.cpp");
        temporaryFolder.newFile("src/main/cpp/b.tmp");

        temporaryFolder.newFile("src/main/cpp/dir1/b.c");
        temporaryFolder.newFile("src/main/cpp/dir1/g.tmp");

        final Set<Path> matches = glob.scan(basePath);

        assertThat(matches).hasSize(3);

        assertThat(matches).contains(basePath.resolve(Paths.get("src", "main", "cpp", "a.c")));
        assertThat(matches).contains(basePath.resolve(Paths.get("src", "main", "cpp", "a.cpp")));
        assertThat(matches).contains(basePath.resolve(Paths.get("src", "main", "cpp", "dir1", "b.c")));

        assertThat(matches).are(new MatchCondition(glob));

    }

    @Test
    public void testSimpleScan() throws Exception {

        final Glob glob = Glob.compile("src/main", "src/main/java", "src/test/java");

        temporaryFolder.newFolder("src", "main", "java");
        temporaryFolder.newFolder("src", "test", "java");

        final Set<Path> matched = glob.scan(basePath);
        assertThat(matched).contains(basePath.resolve(Paths.get("src", "main")));
        assertThat(matched).contains(basePath.resolve(Paths.get("src", "main", "java")));
        assertThat(matched).contains(basePath.resolve(Paths.get("src", "test", "java")));

        assertThat(matched).are(new MatchCondition(glob));

    }

    @Test
    public void testTreeScan() throws Exception {

        final Glob glob = Glob.compile("tree/**");

        temporaryFolder.newFolder("tree");
        temporaryFolder.newFolder("tree", "test");
        temporaryFolder.newFile("tree/a.txt");
        temporaryFolder.newFile("tree/test/b.txt");
        temporaryFolder.newFile("c.txt");

        final Set<Path> matchedAny = glob.scan(basePath);
        assertThat(matchedAny).hasSize(4);
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree", "a.txt")));
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree", "test", "b.txt")));
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree")));
        assertThat(matchedAny).contains(basePath.resolve(Paths.get("tree", "test")));

        assertThat(matchedAny).are(new MatchCondition(glob));

    }

    @Test
    public void testTreeScanShouldMatchFile() throws Exception {

        final Glob glob = Glob.compile("tree/a.txt/**");
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Set<Path> matches = glob.scan(basePath);
        assertThat(matches).contains(basePath.resolve(Paths.get("tree", "a.txt")));
        assertThat(matches).are(new MatchCondition(glob));

    }

    @Test
    public void testTreeScanShouldSuggestInputPath() throws Exception {

        final Glob glob = Glob.compile("tree/a.txt");
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Set<Path> matchedFiles = glob.scan(basePath);
        assertThat(matchedFiles).isEqualTo(Collections.singleton(basePath.resolve(Paths.get("tree", "a.txt"))));
        assertThat(matchedFiles).are(new MatchCondition(glob));

    }

}