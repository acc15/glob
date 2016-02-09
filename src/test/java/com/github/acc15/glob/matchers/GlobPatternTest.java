package com.github.acc15.glob.matchers;

import com.github.acc15.glob.common.CharIterator;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public class GlobPatternTest {
    
    @Test
    public void testParseSequence() throws Exception {
        final List<Matcher> pattern = GlobPattern.parseSequence(new CharIterator("a\\*b*{x,y}?.c{,pp,xx}"), false);

        Assertions.assertThat(pattern).isEqualTo(Arrays.asList(
                Matchers.text("a*b"),
                Matchers.zeroOrMore(),
                Matchers.variants(Matchers.text("x"), Matchers.text("y")),
                Matchers.any(),
                Matchers.text(".c"),
                Matchers.variants(Matchers.sequence(), Matchers.text("pp"), Matchers.text("xx"))));
    }
    
    @Test
    public void testCompile() throws Exception {

        final GlobPattern pattern = GlobPattern.compile("a*{x,y}\\??.c{,pp,xx}");

        assertThat(pattern.test("axy?e.c")).isTrue();
        assertThat(pattern.test("axy?d.cpp")).isTrue();
        assertThat(pattern.test("axy?f.cxx")).isTrue();
        assertThat(pattern.test("ayky?k.c")).isTrue();

        assertThat(pattern.test("bxy.c")).isFalse();
        assertThat(pattern.test("azl.c")).isFalse();
        assertThat(pattern.test("ax.c")).isFalse();
        assertThat(pattern.test("axy.c.cpp")).isFalse();

    }

    @Test
    public void testNestedVariants() throws Exception {

        final GlobPattern pattern = GlobPattern.compile("{x?z,x*q}");

        assertThat(pattern.test("xyz")).isTrue();
        assertThat(pattern.test("xaaaq")).isTrue();
        assertThat(pattern.test("xaz")).isTrue();
        assertThat(pattern.test("xq")).isTrue();

        assertThat(pattern.test("xz")).isFalse();
        assertThat(pattern.test("xad")).isFalse();

    }
}