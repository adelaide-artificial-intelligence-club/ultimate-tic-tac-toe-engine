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

package io.riddles.tictactoenew.game.processor;

import java.util.ArrayList;

import io.riddles.javainterface.game.processor.AbstractProcessor;
import io.riddles.tictactoenew.game.move.*;
import io.riddles.tictactoenew.game.player.TicTacToePlayer;
import io.riddles.tictactoenew.game.state.TicTacToeState;

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


    /* Constructor */
    public TicTacToeProcessor(ArrayList<TicTacToePlayer> players) {
        super(players);
        this.gameOver = false;
    }

    /* preGamePhase may be used to set up the Processor before starting the game loop.
    * */
    @Override
    public void preGamePhase() {

    }

    /* playRound is called every cycle of SimpleGameLoop. It should:
    *   - Take a State
    *   - Ask players for response
    *   - Parse response into a move
    *   - Handle move logic
    *   - Create a new State and return it.
    *   returns: TicTacToeState
    * */
    @Override
    public TicTacToeState playRound(int roundNumber, TicTacToeState state) {
        this.roundNumber = roundNumber;

        ArrayList<TicTacToeMove> moves = new ArrayList();

        int moveNumber = (roundNumber-1)*this.players.size()+1;

        TicTacToeState nextState = state;

        for (TicTacToePlayer player : this.players) {
            if (!hasGameEnded(nextState)) {
                nextState = new TicTacToeState(nextState, moves, roundNumber);

                LOGGER.info(String.format("Playing round %d, move %d", roundNumber, moveNumber));

                String response = player.requestMove(ActionType.MOVE.toString());

                // parse the response
                TicTacToeMoveDeserializer deserializer = new TicTacToeMoveDeserializer(player);
                TicTacToeMove move = deserializer.traverse(response);

                TicTacToeLogic l = new TicTacToeLogic();

                try {
                    nextState = l.transformBoard(nextState, move, this.players);
                } catch (Exception e) {
                    LOGGER.info(e.toString());
                }

                //if (move.getException() != null) System.out.println(move.getException());

                // create the next state
                moves.add(move);

                // stop game if bot returns nothing
                if (response == null) {
                    this.gameOver = true;
                }

                nextState.setMoveNumber(moveNumber);
                int nextPlayer = getNextPlayerId(player);
                nextState.setFieldPresentationString(nextState.getBoard().toPresentationString(nextPlayer, false));
                nextState.setPossibleMovesPresentationString(nextState.getBoard().toPresentationString(nextPlayer, true));

                //nextState.getBoard().dump();
                //nextState.getBoard().dumpMacroboard();
                checkWinner(nextState);
                moveNumber++;
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
