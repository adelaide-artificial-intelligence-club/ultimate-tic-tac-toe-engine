package io.riddles.tictactoe.game.data;

/**
 * Created by joost on 8/9/16.
 */

public class Disc {

    private Coordinate coordinate;

    public Disc(Coordinate c) {
        coordinate = c;
    }


    /**
     * @return : Coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
