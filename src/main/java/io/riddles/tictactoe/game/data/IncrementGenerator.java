package io.riddles.tictactoe.game.data;

import io.riddles.tictactoe.lang.Generator;

/**
 * Created by joost on 3/24/17.
 */
public class IncrementGenerator implements Generator<Integer> {

    int value;
    int incrementBy;

    public IncrementGenerator(int start, int incrementBy) {
        this.value = start;
        this.incrementBy = incrementBy;
    }

    public IncrementGenerator() {
        this.value = 0;
        this.incrementBy = 1;
    }

    public Integer next() {
        value += incrementBy;
        return value;
    }

    public Integer getValue() { return value; }
}
