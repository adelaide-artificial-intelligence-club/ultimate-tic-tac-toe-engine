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

import io.riddles.javainterface.game.player.AbstractPlayer;
import io.riddles.javainterface.game.player.PlayerProvider;
import io.riddles.javainterface.game.state.AbstractState;
import io.riddles.tictactoe.TicTacToe;
import io.riddles.tictactoe.game.data.IncrementGenerator;
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

        TicTacToeStateSerializer stateSerializer = new TicTacToeStateSerializer(new IncrementGenerator(), processor);

        states.put(stateSerializer.traverseToJson(state, false));
        states.put(stateSerializer.traverseToJson(state, true));

        while (state.hasNextState()) {
            state = (TicTacToeState)state.getNextState();

            states.put(stateSerializer.traverseToJson(state, false));
            if (state.hasNextState())
                states.put(stateSerializer.traverseToJson(state, true));
        }
        game.put("states", states);


        return game.toString();
    }

    /**
     * Method that can be used for (almost) every game type. Will put everything
     * to the output file that every visualizer needs
     * @param game JSONObject that stores the full game output
     * @param processor Processor that is used this game
     * @return Updated JSONObject with added stuff
     */
    protected JSONObject addDefaultJSON(TicTacToeState state, JSONObject game, TicTacToeProcessor processor) {

        // put default settings (player settings)
        JSONArray playerNames = new JSONArray();
        for (Object obj : processor.getPlayerProvider().getPlayers()) {
            AbstractPlayer player = (AbstractPlayer) obj;
            playerNames.put(player.getName());
        }

        JSONObject players = new JSONObject();
        players.put("count", processor.getPlayerProvider().getPlayers().size());
        players.put("names", playerNames);

        JSONObject settings = new JSONObject();
        settings.put("players", players);


        JSONObject field = new JSONObject();
        field.put("width", 9);
        field.put("height", 9);

        settings.put("field", field);

        game.put("settings", settings);

        // Fast forward to last state
        TicTacToeState finalState = state;
        while (finalState.hasNextState()) {
            finalState = (TicTacToeState)finalState.getNextState();
        }

        if (processor.getWinnerId(finalState) != null) {
            game.put("winner", processor.getWinnerId(finalState));
        } else {
            game.put("winner", JSONObject.NULL);
        }

        // put score
        game.put("score", processor.getScore(finalState));

        return game;
    }

}

