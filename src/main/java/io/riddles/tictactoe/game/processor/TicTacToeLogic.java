package io.riddles.tictactoe.game.processor;

import java.awt.*;
import java.util.Objects;

import io.riddles.javainterface.exception.InvalidInputException;
import io.riddles.tictactoe.game.data.TicTacToeBoard;
import io.riddles.tictactoe.game.move.TicTacToeMove;
import io.riddles.tictactoe.game.state.TicTacToePlayerState;
import io.riddles.tictactoe.game.state.TicTacToeState;


class TicTacToeLogic {

    /**
     * Takes a TicTacToeState and transforms it with a TicTacToeMove.
     *
     * Return
     * Returns nothing, but transforms the given TicTacToeState.
     * @param state The initial state
     * @param playerState The playerState involved
     * @return
     */
    public void transform(TicTacToeState state, TicTacToePlayerState playerState) throws InvalidInputException {
        TicTacToeMove move = playerState.getMove();

        if (move.getException() == null) {
            transformMove(state, playerState);
        }
    }

    /**
     * Takes a TicTacToeState and applies the move.
     *
     * Return
     * Returns nothing, but transforms the given TicTacToeState.
     * @param state The initial state
     * @param playerState The move of the player
     * @return
     */
    private void transformMove(TicTacToeState state, TicTacToePlayerState playerState) {
        int pId = playerState.getPlayerId();
        TicTacToeMove move = playerState.getMove();

        TicTacToeBoard b = state.getBoard();
        Point c = move.getCoordinate();

        if (c.x < b.getWidth() && c.y < b.getHeight() && c.x >= 0 && c.y >= 0) { /* Move within range */
            if (b.isInActiveMicroboard(c.x, c.y)) { /* Move in active microboard */
                if (Objects.equals(b.getFieldAt(c), TicTacToeBoard.EMPTY_FIELD)) { /*Field is available */
                    b.setFieldAt(c, String.valueOf(pId));
                    b.updateMacroboard(playerState.getMove().getCoordinate());

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