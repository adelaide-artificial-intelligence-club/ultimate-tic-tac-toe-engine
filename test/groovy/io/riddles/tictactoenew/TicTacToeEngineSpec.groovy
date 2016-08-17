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

package io.riddles.tictactoenew

import io.riddles.tictactoenew.engine.TicTacToeEngine
import io.riddles.javainterface.io.IOHandler
import io.riddles.tictactoenew.game.state.TicTacToeState
import spock.lang.Specification

/**
 *
 * [description]
 *
 * @author joost
 */

class TicTacToeEngineSpec extends Specification {

    class TestEngine extends TicTacToeEngine {
        protected TicTacToeState finalState = null

        TestEngine(IOHandler ioHandler) {
            super();
            this.ioHandler = ioHandler;
        }

        TestEngine(String wrapperFile, String[] botFiles) {
            super(wrapperFile, botFiles)

        }

        IOHandler getIOHandler() {
            return this.ioHandler;
        }

        @Override
        protected void finish(TicTacToeState finalState) {
            this.finalState = finalState;
            super.finish(finalState);
        }

        @Override
        protected TicTacToeState getInitialState() {
            return super.getInitialState();
            //TicTacToeState s = new TicTacToeState();
            //return s;
        }
    }


    def "test if TicTacToeEngine is created"() {
        println("test if TicTacToeEngine is created")

        setup:
        String[] botInputs = new String[2]

        def wrapperInput = "./test/resources/wrapper_input.txt"
        botInputs[0] = "./test/resources/bot1_input.txt"
        botInputs[1] = "./test/resources/bot2_input.txt"

        def engine = new TestEngine(wrapperInput, botInputs)
        engine.run()

        expect:
        engine.finalState instanceof TicTacToeState;
    }
}