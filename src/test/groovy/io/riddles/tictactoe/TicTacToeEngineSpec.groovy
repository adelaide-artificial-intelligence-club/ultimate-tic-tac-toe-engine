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

import io.riddles.javainterface.exception.TerminalException
import io.riddles.javainterface.game.player.PlayerProvider
import io.riddles.javainterface.game.state.AbstractState
import io.riddles.javainterface.io.FileIOHandler
import io.riddles.tictactoe.engine.TicTacToeEngine
import io.riddles.javainterface.io.IOHandler
import io.riddles.tictactoe.game.player.TicTacToePlayer
import io.riddles.tictactoe.game.processor.TicTacToeProcessor
import io.riddles.tictactoe.game.state.TicTacToeState
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

        AbstractState state = engine.willRun()
        state = engine.run(state);
        engine.didRun(state);

        /* Fast forward to final state */
        while (state.hasNextState()) state = state.getNextState();

        //state.getBoard().dumpBoard();
        TicTacToeProcessor processor = engine.getProcessor();

        expect:
        state instanceof TicTacToeState;
        state.getBoard().toString() == ".,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,1,.,.,.,.,2,2,2,2,.,.,.,.,.,.,.,1,.,.,.,.,2,.,.,.,.,.,.,.,.,.,.,1,.,.,1,1,2,.,.,.,.,.,.,.,.,.,.,1,1,1,1,.,2,.,.,.,.,.,.,.,.,.,.,.,.,.,.,2,2,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.";
        processor.getWinnerId(state) == 2;
    }

    @Ignore
    def "test illegal moves"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./test/resources/wrapper_input.txt"
        botInputs[0] = "./test/resources/bot_input_illegal.txt"
        botInputs[1] = "./test/resources/bot2_input.txt"

        def engine = new TestEngine(wrapperInput, botInputs)
        engine.run()

        expect:
        engine.finalState instanceof TicTacToeState;
    }

    @Ignore
    def "test out of bounds"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./test/resources/wrapper_input.txt"
        botInputs[0] = "./test/resources/bot_input_outofbounds.txt"
        botInputs[1] = "./test/resources/bot2_input.txt"

        def engine = new TestEngine(wrapperInput, botInputs)
        engine.run()

        expect:
        engine.finalState instanceof TicTacToeState;
    }

    @Ignore
    def "test garbage input"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./test/resources/wrapper_input.txt"
        botInputs[0] = "./test/resources/bot_input_garbage.txt"
        botInputs[1] = "./test/resources/bot_input_garbage.txt"

        def engine = new TestEngine(wrapperInput, botInputs)
        engine.run()

        expect:
        engine.finalState instanceof TicTacToeState;
    }

    //@Ignore
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

        AbstractState state = engine.willRun()
        state = engine.run(state);
        engine.didRun(state);

        /* Fast forward to final state */
        while (state.hasNextState()) state = state.getNextState();

        //state.getBoard().dumpBoard();
        TicTacToeProcessor processor = engine.getProcessor();

        expect:
        state instanceof TicTacToeState;
        state.getBoard().toString() == ".,.,1,.,.,1,.,.,1,.,1,.,.,1,.,0,0,0,1,.,.,.,.,.,.,.,1,.,.,.,0,0,0,.,.,.,.,.,.,.,1,.,.,1,.,.,.,.,1,.,.,.,.,.,0,.,.,.,.,.,0,0,0,.,0,.,.,.,.,.,.,.,.,.,0,.,.,.,1,.,.";
        processor.getWinnerId(state) == 0;
    }

    //Ignore
    def "test bot 1 win"() {

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./src/test/resources/wrapper_input.txt"
        botInputs[0] = "./src/test/resources/bot_input_loose.txt"
        botInputs[1] = "./src/test/resources/bot_input_win.txt"

        PlayerProvider<TicTacToePlayer> playerProvider = new PlayerProvider<>();
        TicTacToePlayer player1 = new TicTacToePlayer(0); player1.setIoHandler(new FileIOHandler(botInputs[0])); playerProvider.add(player1);
        TicTacToePlayer player2 = new TicTacToePlayer(1); player2.setIoHandler(new FileIOHandler(botInputs[1])); playerProvider.add(player2);

        def engine = new TestEngine(playerProvider, wrapperInput)

        AbstractState state = engine.willRun()
        state = engine.run(state);
        engine.didRun(state);

        /* Fast forward to final state */
        while (state.hasNextState()) state = state.getNextState();

        //state.getBoard().dumpBoard();
        TicTacToeProcessor processor = engine.getProcessor();

        expect:
        state instanceof TicTacToeState;
        state.getBoard().toString() == ".,.,0,.,.,.,.,.,0,.,.,.,.,.,.,1,.,.,.,.,.,.,.,.,.,.,.,.,.,.,1,1,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,1,.,.,.,.,.,.";
        processor.getWinnerId(state) == 1;
    }
}