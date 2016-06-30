package com.github.acc15.glob.scanners;

import com.github.acc15.glob.TargetType;
import org.assertj.core.api.Condition;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
            return glob.test(value);
        }
    }

    @Test
    public void testParseSequenceWithoutSpecialChars() throws Exception {
        final List<Scanner> scanners= Glob.parseSequence("s\\r\\c/**/ab\\c");
        assertThat(scanners).containsExactly(Scanners.path("src"), Scanners.tree(), Scanners.path("abc"));
    }

    @Test
    public void testParseSequenceWithEscapedSpecialChars() throws Exception {
        final List<Scanner> scanners= Glob.parseSequence("s\\r\\c/**/\\{abc\\}");
        assertThat(scanners).containsExactly(Scanners.path("src"), Scanners.tree(), Scanners.path("{abc}"));
    }

    @Test
    public void testParseSequenceWithSpecialChars() throws Exception {
        final List<Scanner> scanners= Glob.parseSequence("s\\r\\c/**/aaa{abc,xyz}.java");
        assertThat(scanners).containsExactly(Scanners.path("src"), Scanners.tree(), Scanners.pattern("aaa{abc,xyz}.java"));
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

        final Collection<Path> matches = glob.scan(basePath);

        assertThat(matches).containsOnly(
            Paths.get("src", "main", "cpp", "a.c"),
            Paths.get("src", "main", "cpp", "a.cpp"),
            Paths.get("src", "main", "cpp", "dir1", "b.c"));

        assertThat(matches).are(new MatchCondition(glob));

    }

    @Test
    public void testSimpleScan() throws Exception {

        final Glob glob = Glob.compile("src/main", "src/main/java", "src/test/java");

        temporaryFolder.newFolder("src", "main", "java");
        temporaryFolder.newFolder("src", "test", "java");

        final Collection<Path> matched = glob.scan(basePath);
        assertThat(matched).containsOnly(
            Paths.get("src", "main"),
            Paths.get("src", "main", "java"),
            Paths.get("src", "test", "java"));

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

        final Collection<Path> matchedAny = glob.scan(basePath);
        assertThat(matchedAny).containsOnly(
            Paths.get("tree", "a.txt"),
            Paths.get("tree", "test", "b.txt"),
            Paths.get("tree"),
            Paths.get("tree", "test"));

        assertThat(matchedAny).are(new MatchCondition(glob));

    }

    @Test
    public void testTreeScanShouldMatchFile() throws Exception {

        final Glob glob = Glob.compile("tree/a.txt/**");
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Collection<Path> matches = glob.scan(basePath);
        assertThat(matches).containsOnly(Paths.get("tree", "a.txt"));
        assertThat(matches).are(new MatchCondition(glob));

    }

    @Test
    public void testTreeScanShouldSuggestInputPath() throws Exception {

        final Glob glob = Glob.compile("tree/a.txt");
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");

        final Collection<Path> matchedFiles = glob.scan(basePath);
        assertThat(matchedFiles).containsOnly(Paths.get("tree", "a.txt"));
        assertThat(matchedFiles).are(new MatchCondition(glob));

    }

    @Test
    public void testDoubleStarWillMatchWholeTreeIncludingBaseDirectory() throws Exception {

        final Glob glob = Glob.compile("**");
        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");
        temporaryFolder.newFile("a.txt");

        final Collection<Path> matchedFiles = glob.scan(basePath);
        assertThat(matchedFiles).containsOnly(
            Paths.get(""),
            Paths.get("tree"),
            Paths.get("a.txt"),
            Paths.get("tree", "a.txt"));

        assertThat(matchedFiles).are(new MatchCondition(glob));

    }

    @Test
    public void testScanWithTargetTypeShouldReturnOnlyFiles() throws Exception {

        temporaryFolder.newFolder("tree");
        temporaryFolder.newFile("tree/a.txt");
        temporaryFolder.newFile("a.txt");

        final Collection<Path> matchedFiles = Glob.compile("**").scan(basePath, TargetType.FILE);
        assertThat(matchedFiles).containsOnly(
            Paths.get("a.txt"),
            Paths.get("tree", "a.txt"));

    }

    @Test
    public void testScanShouldNotAddNonExistentPaths() throws Exception {

        final Glob glob = Glob.compile("**");

        final Collection<Path> matches = glob.scan(basePath.resolve("abc"));
        assertThat(matches).isEmpty();
    }

    @Test
    public void testMatch() throws Exception {

        final Glob glob = Glob.compile("**/a.txt");
        temporaryFolder.newFile("b.txt");

        assertThat(glob.test(Paths.get(""))).isFalse();
        assertThat(glob.test(Paths.get("b.txt"))).isFalse();
        assertThat(glob.test(Paths.get("ddd/any/dir/a.txt"))).isTrue();
        assertThat(glob.test(Paths.get("a.txt"))).isTrue();

    }

    @Test
    public void testPredicate() throws Exception {

        final Glob glob = Glob.compile("glob*.java");

        final List<Path> paths = Arrays.asList(
            Paths.get("global.txt"),
            Paths.get("glob.java"),
            Paths.get("glob.java.cpp"));

        assertThat( paths.stream().anyMatch(glob) ).isTrue();
        assertThat( paths.stream().allMatch(glob) ).isFalse();

        final List<Path> matches = paths.stream().filter(glob).collect(Collectors.toList());
        assertThat(matches).containsExactly(Paths.get("glob.java"));

    }

    @Test
    public void testIncludesAndExcludes() throws Exception {
        Glob glob = Glob.compile("a{a,b}.jar");

        temporaryFolder.newFile("aa.jar");
        temporaryFolder.newFile("ab.jar");

        Collection<Path> paths = glob.scan(basePath, Glob.compile("ab.jar").negate());
        assertThat(paths).containsExactly(Paths.get("aa.jar"));
    }

    @Test
    public void testPathsOrder() throws Exception {

        for (char c = 'a'; c <= 'z'; c++) {
            temporaryFolder.newFile(c + ".jar");
        }

        Collection<Path> paths = Glob.compile("o.jar", "r.jar", "d.jar", "*.jar").scan(basePath);
        assertThat(paths).hasSize(26);
        assertThat(paths).containsSequence(Paths.get("o.jar"), Paths.get("r.jar"), Paths.get("d.jar"));
    }

    @Test
    public void testPathsOrderWithSubdirectories() throws Exception {

        temporaryFolder.newFolder("a");
        temporaryFolder.newFolder("b");

        temporaryFolder.newFile("a/a.js");
        temporaryFolder.newFile("a/a.css");
        temporaryFolder.newFile("b/b.js");
        temporaryFolder.newFile("b/b.css");

        Collection<Path> paths = Glob.compile("**/*.js", "**/*.css").scan(basePath);
        assertThat(paths).containsExactly(
            Paths.get("a", "a.js"),
            Paths.get("b", "b.js"),
            Paths.get("a", "a.css"),
            Paths.get("b", "b.css"));

    }

    @Test
    public void emptyGlob() throws Exception {
        assertThat(Glob.empty().test(Paths.get("a.txt"))).isFalse();
    }
}