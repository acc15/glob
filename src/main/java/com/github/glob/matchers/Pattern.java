package com.github.glob.matchers;

import com.github.glob.common.CharIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public class Pattern {

    private final List<Matcher> matchers;

    public Pattern(Matcher... matchers) {
        this.matchers = Arrays.asList(matchers);
    }

    public Pattern(List<Matcher> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(CharSequence sequence) {
        return MatchContext.create(matchers, sequence).matchNext();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pattern && ((Pattern)obj).matchers.equals(matchers);
    }

    @Override
    public int hashCode() {
        return matchers.hashCode();
    }


    public static Pattern compile(CharSequence expression) {
        final CharIterator iter = new CharIterator(expression);
        return new Pattern(parseSequence(iter, false));
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
}
