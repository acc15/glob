package com.github.glob.matchers;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public class PatternTest {
    
    @Test
    public void testCompile() throws Exception {

        final Pattern pattern = Pattern.compile("a*{x,y}?.c{,pp,xx}");

        assertThat(pattern.matches("axy.c")).isTrue();
        assertThat(pattern.matches("axy.cpp")).isTrue();
        assertThat(pattern.matches("axy.cxx")).isTrue();
        assertThat(pattern.matches("aykyk.c")).isTrue();

        assertThat(pattern.matches("bxy.c")).isFalse();
        assertThat(pattern.matches("azl.c")).isFalse();
        assertThat(pattern.matches("ax.c")).isFalse();
        assertThat(pattern.matches("axy.c.cpp")).isFalse();

    }
}