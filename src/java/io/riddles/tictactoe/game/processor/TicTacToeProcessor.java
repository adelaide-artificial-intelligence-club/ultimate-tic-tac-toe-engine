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

import java.util.ArrayList;

import io.riddles.javainterface.game.processor.AbstractProcessor;
import io.riddles.tictactoe.game.move.*;
import io.riddles.tictactoe.game.player.TicTacToePlayer;
import io.riddles.tictactoe.game.state.TicTacToeState;
import io.riddles.tictactoe.game.data.TicTacToeBoard;

/**
 * This file is a part of TicTacToe
 *
 * Copyright 2016 - present Riddles.io
 * For license information see the LICENSE file in the project root
 *
 * TicTacToeProcessor shall process a State and return the result.
 *
 * @author joost
 */
public class TicTacToeProcessor extends AbstractProcessor<TicTacToePlayer, TicTacToeState> {

    private int roundNumber;
    private boolean gameOver;
    private TicTacToePlayer winner;
    private TicTacToeLogic logic;


    /* Constructor */
    public TicTacToeProcessor(ArrayList<TicTacToePlayer> players) {
        super(players);
        this.gameOver = false;
        this.logic = new TicTacToeLogic();
    }

    /* preGamePhase may be used to set up the Processor before starting the game loop.
    * */
    @Override
    public void preGamePhase() {

    }

    /**
     * Play one round of the game. It takes a TicTacToeState,
     * asks all living players for a response and delivers a new TicTacToeState.
     *
     * Return
     * the TicTacToeState that will be the state for the next round.
     * @param roundNumber The current round number
     * @param ConnectfourState The current state
     * @return The TicTacToeState that will be the start of the next round
     */
    @Override
    public TicTacToeState playRound(int roundNumber, TicTacToeState state) {
        LOGGER.info(String.format("Playing round %d", roundNumber));
        this.roundNumber = roundNumber;

        TicTacToeState nextState = state;

        int playerCounter = 0;
        for (TicTacToePlayer player : this.players) {
            if (!hasGameEnded(nextState)) {
                nextState = new TicTacToeState(nextState, new ArrayList<>(), roundNumber);
                nextState.setMoveNumber(roundNumber*2 + playerCounter - 1);
                TicTacToeBoard nextBoard = nextState.getBoard();

                player.sendUpdate("field", player, nextBoard.toString());
                String response = player.requestMove(ActionType.MOVE.toString());

                // parse the response
                TicTacToeMoveDeserializer deserializer = new TicTacToeMoveDeserializer(player);
                TicTacToeMove move = deserializer.traverse(response);

                try {
                    logic.transform(nextState, move);
                } catch (Exception e) {
                    LOGGER.info(String.format("Unknown response: %s", response));
                }

                // add the next move
                nextState.getMoves().add(move);

                // stop game if bot returns nothing
                if (response == null) {
                    this.gameOver = true;
                }

                int nextPlayer = getNextPlayerId(player);
                nextState.setFieldPresentationString(nextState.getBoard().toPresentationString(nextPlayer, false));
                nextState.setPossibleMovesPresentationString(nextState.getBoard().toPresentationString(nextPlayer, true));

                nextState.getBoard().dump();
                nextState.getBoard().dumpMacroboard();
                checkWinner(nextState);
                playerCounter++;
            }

        }
        return nextState;
    }

    private int getNextPlayerId(TicTacToePlayer p) {
        return (p.getId() == 1) ? 2 : 1;
    }

    /* hasGameEnded should check all conditions on which a game should end
    *  returns: boolean
    * */
    @Override
    public boolean hasGameEnded(TicTacToeState state) {
        boolean returnVal = false;
        if (roundNumber > 30) returnVal = true;
        checkWinner(state);
        if (this.winner != null) returnVal = true;
        return returnVal;
    }

    /* getWinner should check if there is a winner.
    *  returns: if there is a winner, the winning Player, otherwise return null.
    *  */
    @Override
    public TicTacToePlayer getWinner() {
        return this.winner;
    }

    public void checkWinner(TicTacToeState s) {
        this.winner = null;
        s.getBoard().updateMacroboard();
        int winner = s.getBoard().getMacroboardWinner();
        if (winner != 0) {
            for (TicTacToePlayer player : this.players) {
                if (player.getId() == winner) {
                    this.winner = player;
                }
            }
        }
    }

    /* getScore should return the game score if applicable.
    *  returns: double Score
    *  */
    @Override
    public double getScore() {
        return 0;
    }

}
