package com.github.glob.matchers;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public class PatternTest {
    
    @Test
    public void testParseSequence() throws Exception {
        final List<Matcher> pattern = Pattern.parseSequence(new Pattern.CharIterator("a\\*b*{x,y}?.c{,pp,xx}"), false);

        assertThat(pattern).isEqualTo(Arrays.asList(
                Matchers.text("a*b"),
                Matchers.zeroOrMore(),
                Matchers.variants(Matchers.text("x"), Matchers.text("y")),
                Matchers.any(),
                Matchers.text(".c"),
                Matchers.variants(Matchers.sequence(), Matchers.text("pp"), Matchers.text("xx"))));
    }
    
    @Test
    public void testCompile() throws Exception {

        final Pattern pattern = Pattern.compile("a*{x,y}\\??.c{,pp,xx}");

        assertThat(pattern.matches("axy?e.c")).isTrue();
        assertThat(pattern.matches("axy?d.cpp")).isTrue();
        assertThat(pattern.matches("axy?f.cxx")).isTrue();
        assertThat(pattern.matches("ayky?k.c")).isTrue();

        assertThat(pattern.matches("bxy.c")).isFalse();
        assertThat(pattern.matches("azl.c")).isFalse();
        assertThat(pattern.matches("ax.c")).isFalse();
        assertThat(pattern.matches("axy.c.cpp")).isFalse();

    }

    @Test
    public void testNestedVariants() throws Exception {

        final Pattern pattern = Pattern.compile("{x?z,x*q}");

        assertThat(pattern.matches("xyz")).isTrue();
        assertThat(pattern.matches("xaaaq")).isTrue();
        assertThat(pattern.matches("xaz")).isTrue();
        assertThat(pattern.matches("xq")).isTrue();

        assertThat(pattern.matches("xz")).isFalse();
        assertThat(pattern.matches("xad")).isFalse();

    }
}