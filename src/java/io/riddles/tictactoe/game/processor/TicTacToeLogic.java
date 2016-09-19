package io.riddles.tictactoe.game.processor;

import io.riddles.javainterface.exception.InvalidInputException;
import io.riddles.tictactoe.game.data.Coordinate;
import io.riddles.tictactoe.game.data.TicTacToeBoard;
import io.riddles.tictactoe.game.move.TicTacToeMove;
import io.riddles.tictactoe.game.player.TicTacToePlayer;
import io.riddles.tictactoe.game.state.TicTacToeState;

import java.util.ArrayList;

/**
 * Created by joost on 3-7-16.
 */
public class TicTacToeLogic {


    public TicTacToeLogic() {
    }

    /**
     * Takes a TicTacToeState and transforms it with a TicTacToeMove.
     *
     * Return
     * Returns nothing, but transforms the given TicTacToeState.
     * @param TicTacToeState The initial state
     * @param TicTacToeMove The move of the player
     * @return
     */
    public void transform(TicTacToeState state, TicTacToeMove move) throws InvalidInputException {
        if (move.getException() == null) {
            transformMove(state, move);
        } else {
        }
    }

    /**
     * Takes a TicTacToeState and applies the move.
     *
     * Return
     * Returns nothing, but transforms the given TicTacToeState.
     * @param TicTacToeState The initial state
     * @param TicTacToeMove The move of the player
     * @return
     */
    private void transformMove(TicTacToeState state, TicTacToeMove move) {
        TicTacToePlayer p = move.getPlayer();
        TicTacToeBoard board = state.getBoard();

        int pId = p.getId();
        TicTacToeBoard b = state.getBoard();
        Coordinate c = move.getCoordinate();
        b.updateMacroboard();

        if (c.getX() < b.getWidth() && c.getY() < b.getHeight() && c.getX() >= 0 && c.getY() >= 0) { /* Move within range */
            if (b.isInActiveMicroboard(c.getX(), c.getY())) { /* Move in active microboard */
                if (b.getFieldAt(c) == b.EMPTY_FIELD) { /*Field is available */
                    b.setFieldAt(c, pId);
                    b.setLastMove(c);
                    b.updateMacroboard();
                    /* Success */
                } else {
                    move.setException(new InvalidInputException("Error: chosen position is already filled"));
                }
            } else {
                move.setException(new InvalidInputException("Error: move not in active macroboard"));
            }
        } else {
            move.setException(new InvalidInputException("Error: move out of bounds"));
        }
    }
}