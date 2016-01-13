package com.github.glob.matchers;

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
        final Pattern pattern = new Pattern(
            Matchers.text("abc"),
            Matchers.zeroOrMore(),
            Matchers.variants(Matchers.text("xyz"), Matchers.text("yui")),
            Matchers.any(),
            Matchers.text(".zip"));

        assertThat(pattern.matches("abcxyzl.zip")).isTrue();
        assertThat(pattern.matches("abcdyuia.zip")).isTrue();
        assertThat(pattern.matches("abceyuid.zip")).isTrue();

        assertThat(pattern.matches("abceyuid.zi")).isFalse(); // ".zip" not matches
        assertThat(pattern.matches("abcddkokk.zip")).isFalse(); // "{xyz,yui}" not matches
        assertThat(pattern.matches("abdcyui2.zip")).isFalse(); // "abc" not matches

    }

}