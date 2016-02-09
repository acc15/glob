package com.github.acc15.glob.matchers;

import com.github.acc15.glob.common.CharIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public class GlobPattern implements Predicate<CharSequence> {

    public static final char ESCAPE_CHAR = '\\';

    private final List<Matcher> matchers;

    public GlobPattern(Matcher... matchers) {
        this.matchers = Arrays.asList(matchers);
    }

    public GlobPattern(List<Matcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean test(CharSequence sequence) {
        return MatchContext.create(matchers, sequence).matchNext();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GlobPattern && ((GlobPattern)obj).matchers.equals(matchers);
    }

    @Override
    public int hashCode() {
        return matchers.hashCode();
    }


    public static GlobPattern compile(CharSequence expression) {
        final CharIterator iter = new CharIterator(expression);
        return new GlobPattern(parseSequence(iter, false));
    }

    static List<Matcher> resetBuffer(StringBuilder sb, List<Matcher> matchers) {
        if (sb.length() > 0) {
            matchers.add(Matchers.text(sb.toString()));
            sb.setLength(0);
        }
        return matchers;
    }

    static List<Matcher> parseSequence(CharIterator iter, boolean stopOnVariantChars) {
        final List<Matcher> matchers = new ArrayList<>();
        final StringBuilder textBuffer = new StringBuilder();

        boolean escape = false;
        while (iter.hasNext()) {

            final char v = iter.next();
            if (escape) {
                textBuffer.append(v);
                escape = false;
                continue;
            }
            switch (v) {
                case '*':
                    resetBuffer(textBuffer, matchers).add(Matchers.zeroOrMore());
                    break;

                case '?':
                    resetBuffer(textBuffer, matchers).add(Matchers.any());
                    break;

                case '{':
                    resetBuffer(textBuffer, matchers).add(Matchers.variants(parseVariants(iter)));
                    break;

                case '\\':
                    escape = true;
                    break;

                case '}':
                case ',':
                    if (stopOnVariantChars) {
                        return resetBuffer(textBuffer, matchers);
                    }

                default:
                    textBuffer.append(v);
                    break;

            }
        }
        resetBuffer(textBuffer, matchers);
        return matchers;
    }

    static List<Matcher> parseVariants(CharIterator iter) {
        final List<Matcher> matchers = new ArrayList<>();
        while (iter.hasNext()) {
            final List<Matcher> seq = parseSequence(iter, true);
            matchers.add(seq.size() == 1 ? seq.get(0) : Matchers.sequence(seq));
            if (iter.getPreviousChar() == '}') {
                break;
            }
        }
        return matchers;
    }

    public static boolean hasNonEscapedSpecialChars(String expression) {
        boolean escape = false;
        for (int i=0; i<expression.length(); i++) {
            final char c = expression.charAt(i);
            if (escape) {
                escape = false;
                continue;
            }
            if (c == ESCAPE_CHAR) {
                escape = true;
                continue;
            }
            if (c == '*' || c == '?' || c == '{' || c == '}') {
                return true;
            }
        }
        return false;
    }

    public static String unescape(String expression) {
        final StringBuilder b = new StringBuilder();
        boolean escape = false;
        for (int i=0; i<expression.length(); i++) {
            final char c = expression.charAt(i);
            if (escape) {
                b.append(c);
                escape = false;
                continue;
            }
            if (c == ESCAPE_CHAR) {
                escape = true;
            } else {
                b.append(c);
            }
        }
        return b.toString();

    }
}
