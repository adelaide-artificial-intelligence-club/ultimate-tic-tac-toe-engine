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

package io.riddles.tictactoe

import io.riddles.javainterface.game.player.PlayerProvider
import io.riddles.javainterface.game.state.AbstractState
import io.riddles.javainterface.io.FileIOHandler
import io.riddles.tictactoe.engine.TicTacToeEngine

import io.riddles.tictactoe.game.player.TicTacToePlayer
import io.riddles.tictactoe.game.processor.TicTacToeProcessor
import io.riddles.tictactoe.game.state.TicTacToeState
import io.riddles.tictactoe.game.state.TicTacToeStateSerializer
import spock.lang.Ignore
import spock.lang.Specification

/**
 *
 * [description]
 *
 * @author joost
 */

class TicTacToeEngineSpec extends Specification {

    class TestEngine extends TicTacToeEngine {

        TestEngine(PlayerProvider<TicTacToeEngine> playerProvider, String wrapperInput) {
            super(playerProvider, null);
            this.ioHandler = new FileIOHandler(wrapperInput);
        }
    }

    @Ignore
    def "test engine setup 2 players"() {

        setup:
        String[] botInputs = new String[2]
        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot1_input.txt"
        botInputs[1] = "./src/test/resources/bot2_input.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState initialState = engine.willRun()
        AbstractState finalState = engine.run(initialState);
        engine.didRun(initialState, finalState);
        TicTacToeProcessor processor = engine.getProcessor();


        expect:
        finalState instanceof TicTacToeState;
        finalState.getBoard().toString() == "1,.,0,.,.,.,.,.,1,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,0,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.";
        processor.getWinnerId(finalState) == 1;
    }

    @Ignore
    def "test illegal moves"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot_input_illegal.txt"
        botInputs[1] = "./src/test/resources/bot2_input.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState initialState = engine.willRun()
        AbstractState finalState = engine.run(initialState);
        engine.didRun(initialState, finalState);
        TicTacToeProcessor processor = engine.getProcessor();


        expect:
        finalState instanceof TicTacToeState;
        finalState.getBoard().toString() == "1,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,0,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.";
        processor.getWinnerId(finalState) == 1;
    }

    @Ignore
    def "test out of bounds"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot_input_outofbounds.txt"
        botInputs[1] = "./src/test/resources/bot2_input.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState initialState = engine.willRun()
        AbstractState finalState = engine.run(initialState);
        engine.didRun(initialState, finalState);
        TicTacToeProcessor processor = engine.getProcessor();


        expect:
        finalState instanceof TicTacToeState;
        finalState.getBoard().toString() == ".,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.";
        processor.getWinnerId(finalState) == 1;
    }

    @Ignore
    def "test garbage input"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot_input_garbage.txt"
        botInputs[1] = "./src/test/resources/bot_input_garbage.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState initialState = engine.willRun()
        AbstractState finalState = engine.run(initialState);
        engine.didRun(initialState, finalState);
        TicTacToeProcessor processor = engine.getProcessor();

        //state.getBoard().dumpBoard();

        expect:
        finalState instanceof TicTacToeState;
        finalState.getBoard().toString() == ".,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.";
        processor.getWinnerId(finalState) == 1;
    }

    @Ignore
    def "test bot 0 win by p1 error"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot_input_win.txt"
        botInputs[1] = "./src/test/resources/bot_input_loose.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState initialState = engine.willRun()
        AbstractState finalState = engine.run(initialState);
        engine.didRun(initialState, finalState);
        TicTacToeProcessor processor = engine.getProcessor();

        expect:
        finalState instanceof TicTacToeState;
        finalState.getBoard().toString() == ".,.,1,.,.,1,.,.,1,.,1,.,.,1,.,0,0,0,1,.,.,.,.,.,.,.,1,.,.,.,0,0,0,.,.,.,.,.,.,.,1,.,.,1,.,.,.,.,1,.,.,.,.,.,0,.,.,.,.,.,0,0,0,.,0,.,.,.,.,.,.,.,.,.,0,.,.,.,1,.,.";
        processor.getWinnerId(finalState) == 0;
    }

    @Ignore
    def "test bot 1 win by p0 error"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot_input_0loose.txt"
        botInputs[1] = "./src/test/resources/bot_input_0win.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState initialState = engine.willRun()
        AbstractState finalState = engine.run(initialState);
        engine.didRun(initialState, finalState);
        TicTacToeProcessor processor = engine.getProcessor();

        expect:
        finalState instanceof TicTacToeState;
        finalState.getBoard().dump();
        finalState.getBoard().dumpMacroboard();
        finalState.getBoard().toString() == ".,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,0,1,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.";
        processor.getWinnerId(finalState) == 1;
    }

    @Ignore
    def "test bot 0 win"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot_input_win.txt"
        botInputs[1] = "./src/test/resources/bot_input_loose.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState initialState = engine.willRun()
        AbstractState finalState = engine.run(initialState);
        engine.didRun(initialState, finalState);
        TicTacToeProcessor processor = engine.getProcessor();

        expect:
        finalState instanceof TicTacToeState;
        finalState.getBoard().toString() == ".,.,1,.,.,1,.,.,1,.,1,.,.,1,.,0,0,0,1,.,.,.,.,.,.,.,1,.,.,.,0,0,0,.,.,.,.,.,.,.,1,.,.,1,.,.,.,.,1,.,.,.,.,.,0,.,.,.,.,.,0,0,0,.,0,.,.,.,.,.,.,.,.,.,0,.,.,.,1,.,.";
        processor.getWinnerId(finalState) == 0;
    }


    @Ignore
    def "test TicTacToeStateSerializer getMoveNr"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot_input_win.txt"
        botInputs[1] = "./src/test/resources/bot_input_loose.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState initialState = engine.willRun()
        TicTacToeProcessor processor = engine.getProcessor();

        TicTacToeStateSerializer serializer = new TicTacToeStateSerializer();

        int moveNrNoPossibleMoves = serializer.getMoveNumber(initialState, false);
        int moveNrPossibleMoves = serializer.getMoveNumber(initialState, true);

        expect:
        moveNrNoPossibleMoves == 1;
        moveNrPossibleMoves == 2;
    }

    //@Ignore
    def "test bot 0 test case Hull"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot1_input_hull.txt"
        botInputs[1] = "./src/test/resources/bot2_input_hull.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState initialState = engine.willRun()
        AbstractState finalState = engine.run(initialState);
        engine.didRun(initialState, finalState);
        TicTacToeProcessor processor = engine.getProcessor();

        expect:
        finalState instanceof TicTacToeState;
        finalState.getBoard().toString() == ".,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,0,.,.,.,.,.,.,.,.,.,.,1,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.";
        processor.getWinnerId(finalState) == 1;
    }
}

