package com.github.acc15.glob.matchers;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-12-01
 */
public class MatchersTest {

    @Test
    public void testPattern() throws Exception {

        // pattern "abc?{xyz,yui}.zip"
        final GlobPattern pattern = new GlobPattern(
            Matchers.text("abc"),
            Matchers.zeroOrMore(),
            Matchers.variants(Matchers.text("xyz"), Matchers.text("yui")),
            Matchers.any(),
            Matchers.text(".zip"));

        assertThat(pattern.test("abcxyzl.zip")).isTrue();
        assertThat(pattern.test("abcdyuia.zip")).isTrue();
        assertThat(pattern.test("abceyuid.zip")).isTrue();

        assertThat(pattern.test("abceyuid.zi")).isFalse(); // ".zip" not matches
        assertThat(pattern.test("abcddkokk.zip")).isFalse(); // "{xyz,yui}" not matches
        assertThat(pattern.test("abdcyui2.zip")).isFalse(); // "abc" not matches

    }

    @Test
    public void testVariantMatcher() throws Exception {

        final GlobPattern pattern = new GlobPattern(
                Matchers.variants(Matchers.text("abc"), Matchers.text("xyz")),
                Matchers.text("ddd"));

        assertThat(pattern.test("abcddd")).isTrue();
        assertThat(pattern.test("xyzddd")).isTrue();
        assertThat(pattern.test("abcxyzddd")).isFalse();
        assertThat(pattern.test("xyzabcddd")).isFalse();

    }

    @Test
    public void testVariantWithSequence() throws Exception {

        final GlobPattern pattern = new GlobPattern(
                Matchers.variants(Matchers.sequence(Matchers.any(), Matchers.text("yz")), Matchers.text("yui")));

        assertThat(pattern.test("ayz")).isTrue();
        assertThat(pattern.test("byz")).isTrue();
        assertThat(pattern.test("zyz")).isTrue();
        assertThat(pattern.test("yui")).isTrue();

        assertThat(pattern.test("abc")).isFalse();
        assertThat(pattern.test("cde")).isFalse();
        assertThat(pattern.test("fdkj")).isFalse();

    }
}