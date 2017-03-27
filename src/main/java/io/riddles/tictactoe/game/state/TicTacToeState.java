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

package io.riddles.tictactoe.game.state;

import io.riddles.javainterface.game.data.Board;
import io.riddles.javainterface.game.player.PlayerBound;
import io.riddles.javainterface.game.state.AbstractState;
import io.riddles.tictactoe.engine.TicTacToeEngine;
import io.riddles.tictactoe.game.data.TicTacToeBoard;
import io.riddles.tictactoe.game.move.TicTacToeMove;

import java.util.ArrayList;

/**
 * io.riddles.game.game.state.TicTacToeState - Created on 2-6-16
 *
 * TicTacToeState extends AbstractState and is used to store game specific data per state.
 * It can be initialised to store a TicTacToePlayerState, or multiple TicTacToePlayerStates in an ArrayList.
 *
 * @author joost
 */
public class TicTacToeState extends AbstractState<TicTacToePlayerState> implements PlayerBound {

    private TicTacToeBoard board;
    private String mPossibleMovesString, mFieldPresentationString;
    private int playerId;

    public TicTacToeState(TicTacToeState previousState, TicTacToePlayerState playerState, int roundNumber, String possibleMovesString, String fieldPresentationString) {
        super(previousState, playerState, roundNumber);
        this.mPossibleMovesString = possibleMovesString;
        this.mFieldPresentationString = fieldPresentationString;
        this.board = new TicTacToeBoard(previousState.getBoard());

    }

    public TicTacToeState(TicTacToeState previousState, ArrayList<TicTacToePlayerState> playerState, int roundNumber) {
        super(previousState, playerState, roundNumber);
        if (previousState != null) {
            this.board = new TicTacToeBoard(previousState.getBoard());
        } else
            this.board = new TicTacToeBoard(TicTacToeEngine.configuration.getInt("fieldWidth"), TicTacToeEngine.configuration.getInt("fieldHeight"));
    }

    /**
     * createNextState creates new objects needed for a new state.
     * @param roundNumber roundNumber
     * @return New TicTacToeState based on this state.
     */
    public TicTacToeState createNextState(int roundNumber) {
        TicTacToeState nextState = new TicTacToeState(this, new ArrayList<>(), roundNumber);
        TicTacToeBoard nextBoard = board.clone();
        nextState.setBoard(nextBoard);
        return nextState;
    }

    public TicTacToeBoard getBoard() {
        return this.board;
    }

    public void setBoard(TicTacToeBoard b) {
        this.board = b;
    }

    public String getPossibleMovesPresentationString() {
        return mPossibleMovesString;
    }
    public String getFieldPresentationString() {
        return mFieldPresentationString;
    }

    public void setPossibleMovesPresentationString(String s) {
        this.mPossibleMovesString = s;
    }
    public void setFieldPresentationString(String s) { this.mFieldPresentationString = s; }

    public int getPlayerId() { return this.playerId; }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
