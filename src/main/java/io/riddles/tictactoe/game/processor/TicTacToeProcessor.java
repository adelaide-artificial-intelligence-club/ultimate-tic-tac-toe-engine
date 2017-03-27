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

import io.riddles.javainterface.engine.AbstractEngine;
import io.riddles.javainterface.exception.InvalidMoveException;
import io.riddles.javainterface.game.player.PlayerProvider;
import io.riddles.javainterface.game.processor.PlayerResponseProcessor;
import io.riddles.javainterface.game.state.AbstractPlayerState;
import io.riddles.javainterface.io.PlayerResponse;
import io.riddles.tictactoe.game.move.ActionType;
import io.riddles.tictactoe.game.move.TicTacToeMove;
import io.riddles.tictactoe.game.move.TicTacToeMoveDeserializer;
import io.riddles.tictactoe.game.player.TicTacToePlayer;
import io.riddles.tictactoe.game.state.TicTacToePlayerState;
import io.riddles.tictactoe.game.state.TicTacToeState;

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

public class TicTacToeProcessor extends PlayerResponseProcessor<TicTacToeState, TicTacToePlayer> {

    public TicTacToeProcessor(PlayerProvider<TicTacToePlayer> playerProvider) {
        super(playerProvider);
    }

    /**
     *
     * Return
     * the TicTacToeState that will be the state for the next round.
     * @param state The current TicTacToeState
     * @param roundNumber The current round number
     * @param input The input to process.
     */
    @Override
    public TicTacToeState createNextStateFromResponse(TicTacToeState state, PlayerResponse input, int roundNumber) {
        /* Clone playerStates for next State */
        ArrayList<TicTacToePlayerState> nextPlayerStates = clonePlayerStates(state.getPlayerStates());

        TicTacToeLogic logic = new TicTacToeLogic();
        TicTacToeState nextState = state.createNextState(roundNumber);

        nextState.setPlayerId(input.getPlayerId());

        TicTacToePlayerState playerState = getActivePlayerState(nextPlayerStates, input.getPlayerId());

        // parse the response
        TicTacToeMoveDeserializer deserializer = new TicTacToeMoveDeserializer();
        TicTacToeMove move = deserializer.traverse(input.getValue());

        if (move.getException() != null) {
            //System.out.println("EXCEPTION '" + input.getValue() + "' " + move.getException().toString());
        }
        playerState.setMove(move);
        try {
            logic.transform(nextState, playerState);
        } catch (Exception e) {
            move.setException(new InvalidMoveException("Error transforming move."));
            //System.out.println("EXCEPTION " + e.toString());
            //e.printStackTrace();
        }
        nextState.setPlayerstates((ArrayList)nextPlayerStates);
        nextState.setFieldPresentationString(nextState.getBoard().toPresentationString(playerState.getPlayerId(), false));
        nextState.setPossibleMovesPresentationString(nextState.getBoard().toPresentationString(playerState.getPlayerId(), true));

        //nextState.getBoard().dump();
        //nextState.getBoard().dumpMacroboard();



        return nextState;
    }

    private ArrayList<TicTacToePlayerState> clonePlayerStates(ArrayList<TicTacToePlayerState> playerStates) {
        ArrayList<TicTacToePlayerState> nextPlayerStates = new ArrayList<>();
        for (TicTacToePlayerState playerState : playerStates) {
            TicTacToePlayerState nextPlayerState = playerState.clone();
            nextPlayerStates.add(nextPlayerState);
        }
        return nextPlayerStates;
    }

    @Override
    public void sendUpdates(TicTacToeState state, TicTacToePlayer player) {

        player.sendUpdate("round", state.getRoundNumber());
        TicTacToeMove move = null;

        ArrayList<TicTacToePlayerState> playerStates = state.getPlayerStates();
        for (TicTacToePlayerState playerState : playerStates) {
            if (playerState.getMove() != null) {
                move = (TicTacToeMove)playerState.getMove();
            }
        }

        player.sendUpdate("field", state.getBoard().toString());


        if (move != null && move.getCoordinate() != null) {
            state.getBoard().updateMacroboard(move.getCoordinate());
            player.sendUpdate("macroboard", state.getBoard().macroboardToString(move.getCoordinate()));
        } else {
            player.sendUpdate("macroboard", state.getBoard().macroboardToString(null));
        }

    }

    @Override
    public Enum getActionType(TicTacToeState ticTacToeState, AbstractPlayerState abstractPlayerState) {
        return ActionType.MOVE;
    }

    /* hasGameEnded should check all conditions on which a game should end
    *  returns: boolean
    * */
    @Override
    public boolean hasGameEnded(TicTacToeState state) {
        boolean returnVal = false;
        TicTacToePlayerState ps = state.getPlayerStateById(state.getPlayerId());
        TicTacToeMove move = (TicTacToeMove)ps.getMove();

        if (getWinnerId(state) != null) returnVal = true;
        if (state.getBoard().boardIsFull()) returnVal = true;
        if (state.getRoundNumber() > AbstractEngine.configuration.getInt("maxRounds")) returnVal = true;
        if (move != null && move.getException() != null) {
            returnVal = true;
            //System.out.println("Ended with exception " + move.getException());
        }

        return returnVal;
    }

    /**
     * Returns the winner of the game, if there is one.
     * @return null if there is no winner, a player ID otherwise
     */
    /* Returns winner playerId, or null if there's no winner. */
    @Override
    public Integer getWinnerId(TicTacToeState state) {
        TicTacToePlayerState ps = state.getPlayerStateById(state.getPlayerId());
        TicTacToeMove move = (TicTacToeMove)ps.getMove();


        if (move != null && move.getCoordinate() != null) {
            state.getBoard().updateMacroboard(move.getCoordinate());
        } else {
            state.getBoard().updateMacroboard(null);
        }
        if (move != null && move.getException() != null) {
            // Player messed up
            Integer otherPlayerId = getOtherPlayerId(state.getPlayerId());
            return otherPlayerId;
        }
        return state.getBoard().getMacroboardWinner();
    }

    private Integer getOtherPlayerId(int playerId) {
        for (TicTacToePlayer player : playerProvider.getPlayers()) {
            if (player.getId() != playerId) return player.getId();
        }
        return null;
    }


    @Override
    public double getScore(TicTacToeState state) {
        return state.getRoundNumber();
    }

    private TicTacToePlayerState getActivePlayerState(ArrayList<TicTacToePlayerState> playerStates, int id) {
        for (TicTacToePlayerState playerState : playerStates) {
            if (playerState.getPlayerId() == id) { return playerState; }
        }
        return null;
    }

}
