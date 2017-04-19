/*
 * Copyright 2016 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package io.riddles.tictactoe.game.processor;

import io.riddles.javainterface.exception.InvalidInputException;
import io.riddles.javainterface.exception.InvalidMoveException;
import io.riddles.tictactoe.game.data.TicTacToeBoard;
import io.riddles.tictactoe.game.move.TicTacToeMove;
import io.riddles.tictactoe.game.state.TicTacToePlayerState;
import io.riddles.tictactoe.game.state.TicTacToeState;

import java.awt.*;
import java.util.Objects;

public class TicTacToeLogic {

    public TicTacToeLogic() {}

    /**
     * Takes a TicTacToeState and transforms it with a TicTacToeMove.
     *
     * Returns nothing, but transforms the given TicTacToeState.
     * @param state The initial TicTacToeState
     * @param playerState The TicTacToePlayerState involved
     */
    public void transform(TicTacToeState state, TicTacToePlayerState playerState) {
        TicTacToeMove move = playerState.getMove();

        if (move.getException() == null) {
            transformMove(state, playerState);
        }
    }

    /**
     * Takes a TicTacToeState and applies the move.
     * Returns nothing, but transforms the given TicTacToeState.
     * @param state The initial TicTacToeState
     * @param playerState The TicTacToePlayerState of the player
     */
    private void transformMove(TicTacToeState state, TicTacToePlayerState playerState) {
        int pId = playerState.getPlayerId();
        TicTacToeMove move = playerState.getMove();

        TicTacToeBoard b = state.getBoard();
        Point c = move.getCoordinate();

        if (c.getX() < b.getWidth() && c.getY() < b.getHeight() && c.getX() >= 0 && c.getY() >= 0) { /* Move within range */
            if (b.isInActiveMicroboard(c.x, c.y)) { /* Move in active microboard */
                if (Objects.equals(b.getFieldAt(c), TicTacToeBoard.EMPTY_FIELD)) { /* Field is available */
                    b.setFieldAt(c, String.valueOf(pId));
                    b.updateMacroboard(move.getCoordinate());

                    /* Success */
                } else {
                    move.setException(new InvalidMoveException("Chosen position is already filled"));
                }
            } else {
                move.setException(new InvalidMoveException("Move not in active macroboard"));
            }
        } else {
            move.setException(new InvalidMoveException("Move out of bounds"));
        }
    }
}