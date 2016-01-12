package com.github.glob.matchers;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-12-01
 */
public class MatchersTest {

    @Test
    public void testPattern() throws Exception {

        // pattern "abc?{xyz,yui}.zip"
        final List<Matcher> pattern = Arrays.asList(
            Matchers.text("abc"),
            Matchers.zeroOrMore(),
            Matchers.variants(Matchers.text("xyz"), Matchers.text("yui")),
            Matchers.any(),
            Matchers.text(".zip"));

        assertThat(Matchers.matches(pattern, "abcxyzl.zip")).isTrue();
        assertThat(Matchers.matches(pattern, "abcdyuia.zip")).isTrue();
        assertThat(Matchers.matches(pattern, "abceyuid.zip")).isTrue();

        assertThat(Matchers.matches(pattern, "abceyuid.zi")).isFalse(); // ".zip" not matches
        assertThat(Matchers.matches(pattern, "abcddkokk.zip")).isFalse(); // "{xyz,yui}" not matches
        assertThat(Matchers.matches(pattern, "abdcyui2.zip")).isFalse(); // "abc" not matches


    }

}