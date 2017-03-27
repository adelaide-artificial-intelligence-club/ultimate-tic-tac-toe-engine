package io.riddles.tictactoe.game.processor;

import io.riddles.javainterface.exception.InvalidInputException;
import io.riddles.tictactoe.game.data.TicTacToeBoard;
import io.riddles.tictactoe.game.move.TicTacToeMove;
import io.riddles.tictactoe.game.state.TicTacToePlayerState;
import io.riddles.tictactoe.game.state.TicTacToeState;

import java.awt.*;

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
     * @param state The initial TicTacToeState
     * @param playerState The TicTacToePlayerState involved
     * @return
     */
    public void transform(TicTacToeState state, TicTacToePlayerState playerState) throws InvalidInputException {
        TicTacToeMove move = (TicTacToeMove)playerState.getMove();

        if (move.getException() == null) {
            transformMove(state, playerState);
        } else {
        }
    }

    /**
     * Takes a TicTacToeState and applies the move.
     *
     * Return
     * Returns nothing, but transforms the given TicTacToeState.
     * @param state The initial TicTacToeState
     * @param playerState The TicTacToePlayerState of the player
     * @return
     */
    private void transformMove(TicTacToeState state, TicTacToePlayerState playerState) {
        int pId = playerState.getPlayerId();
        TicTacToeMove move = (TicTacToeMove)playerState.getMove();

        TicTacToeBoard b = state.getBoard();
        Point c = move.getCoordinate();

        if (c.getX() < b.getWidth() && c.getY() < b.getHeight() && c.getX() >= 0 && c.getY() >= 0) { /* Move within range */
            if (b.isInActiveMicroboard(c.x, c.y)) { /* Move in active microboard */
                if (b.getFieldAt(c) == b.EMPTY_FIELD) { /*Field is available */
                    b.setFieldAt(c, String.valueOf(pId));
                    b.updateMacroboard(move.getCoordinate());

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