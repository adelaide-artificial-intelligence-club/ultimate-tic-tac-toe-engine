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

package io.riddles.tictactoe.game;

import io.riddles.javainterface.game.player.PlayerProvider;
import io.riddles.tictactoe.game.processor.TicTacToeProcessor;
import io.riddles.tictactoe.game.state.TicTacToeState;
import io.riddles.tictactoe.game.state.TicTacToeStateSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import io.riddles.javainterface.game.AbstractGameSerializer;

/**
 * TicTacToeSerializer takes a TicTacToeState and serialises it and all previous states into a JSON String.
 * Customize this to add all game specific data to the output.
 *
 * @author jim
 */
public class TicTacToeSerializer extends AbstractGameSerializer<TicTacToeProcessor, TicTacToeState> {

    public TicTacToeSerializer(PlayerProvider playerProvider) {
        super(playerProvider);
    }


    private final int SIZE = 9;

    @Override
    public String traverseToString(TicTacToeProcessor processor, TicTacToeState initialState) {
        JSONObject game = new JSONObject();

        game = addDefaultJSON(initialState, game, processor);


        // add all states
        JSONArray states = new JSONArray();
        TicTacToeState state = initialState;
        TicTacToeStateSerializer stateSerializer = new TicTacToeStateSerializer();

        int currentRoundNumber = state.getRoundNumber();
        while (state.hasNextState()) {
            int tries = 0;
            while (state.getRoundNumber() == currentRoundNumber && tries < 4) { /* Max 4 players */
                if (state.hasNextState()) state = (TicTacToeState) state.getNextState();
                tries++;
            }
            states.put(stateSerializer.traverseToJson(state));
            currentRoundNumber = state.getRoundNumber();
        }
        game.put("states", states);


        return game.toString();
    }


}
