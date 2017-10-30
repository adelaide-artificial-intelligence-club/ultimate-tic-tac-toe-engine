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

    public TicTacToeSerializer() {
        super();
    }

    @Override
    public String traverseToString(TicTacToeProcessor processor, TicTacToeState initialState) {
        JSONObject game = new JSONObject();

        game = addDefaultJSON(initialState, game, processor);

        JSONArray states = new JSONArray();
        TicTacToeState state = initialState;

        TicTacToeStateSerializer stateSerializer = new TicTacToeStateSerializer();

        states.put(stateSerializer.traverseToJson(state, false));
        states.put(stateSerializer.traverseToJson(state, true));

        while (state.hasNextState()) {
            state = (TicTacToeState)state.getNextState();

            states.put(stateSerializer.traverseToJson(state, false));
            if (state.hasNextState()) {
                states.put(stateSerializer.traverseToJson(state, true));
            }
        }

        game.put("states", states);

        return game.toString();
    }
}

