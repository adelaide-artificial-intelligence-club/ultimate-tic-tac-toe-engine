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

package io.riddles.tictactoe.game.move;

import io.riddles.javainterface.game.data.Point;
import io.riddles.javainterface.exception.InvalidInputException;
import io.riddles.javainterface.game.move.AbstractMove;
import io.riddles.tictactoe.game.player.TicTacToePlayer;

/**
 * ${PACKAGE_NAME}
 *
 * This file is a part of TicTacToe
 *
 * Copyright 2016 - present Riddles.io
 * For license information see the LICENSE file in the project root
 *
 * @author Niko
 */


public class TicTacToeMove extends AbstractMove {

    private Point coordinate;

    public TicTacToeMove(Point c) {
        super();
        coordinate = c;

    }

    public TicTacToeMove(InvalidInputException exception) {
        super(exception);
    }
    public Point getCoordinate() { return this.coordinate; }

    public String toString() {
        return "TicTacToeMove " + this.coordinate;
    }

}