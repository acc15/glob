package com.github.glob.common;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-13-01
 */
public final class CharIterator {
    private final CharSequence sequence;
    private int position;

    public CharIterator(CharSequence sequence) {
        this.sequence = sequence;
    }

    public CharIterator(CharSequence sequence, int position) {
        this.sequence = sequence;
        this.position = position;
    }

    public char getPreviousChar() {
        return sequence.charAt(position - 1);
    }

    public char next() {
        return sequence.charAt(position++);
    }

    public boolean hasNext() {
        return position < sequence.length();
    }
}
