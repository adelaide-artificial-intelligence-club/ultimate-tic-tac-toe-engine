package io.riddles.tictactoenew.game.processor;

import io.riddles.javainterface.exception.InvalidInputException;
import io.riddles.tictactoenew.game.data.Coordinate;
import io.riddles.tictactoenew.game.data.TicTacToeBoard;
import io.riddles.tictactoenew.game.move.TicTacToeMove;
import io.riddles.tictactoenew.game.player.TicTacToePlayer;
import io.riddles.tictactoenew.game.state.TicTacToeState;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by joost on 3-7-16.
 */
public class TicTacToeLogic {


    public TicTacToeLogic() {
    }

    public TicTacToeState transformBoard(TicTacToeState state, TicTacToeMove move, ArrayList<TicTacToePlayer> players) throws InvalidInputException {

        if (move.getException() == null) {
            transformMoveLocation(state, move, players);
        } else {
        }
        return state;
    }

    private void transformMoveLocation(TicTacToeState state, TicTacToeMove move, ArrayList<TicTacToePlayer> players) {
        TicTacToePlayer p = move.getPlayer();
        TicTacToeBoard board = state.getBoard();

        int pId = p.getId();
        TicTacToeBoard b = state.getBoard();
        Coordinate c = move.getCoordinate();
        b.updateMacroboard();

        if (c.getX() < b.getWidth() && c.getY() < b.getHeight() && c.getX() >= 0 && c.getY() >= 0) { /* Move within range */
            if (b.isInActiveMicroboard(c.getX(), c.getY())) { /* Move in active microboard */
                if (b.getFieldAt(c) == 0) { /*Field is available */
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