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

package io.riddles.tictactoe.game.data;

import java.awt.*;
import java.util.Objects;

public class TicTacToeBoard {
    private String[][] field;
    private String[][] macroboard;

    private int width = 9;
    private int height = 9;

    public static final String EMPTY_FIELD = ".";
    public static final String AVAILABLE_FIELD = "-1";

    public TicTacToeBoard(int w, int h) {
        this.width = w;
        this.height = h;
        this.field = new String[w][h];
        this.macroboard = new String[w / 3][h / 3];
        clearBoard();
    }

    public TicTacToeBoard(TicTacToeBoard board) {
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.field = new String[this.width][this.height];
        this.macroboard = new String[this.width / 3][this.height / 3];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.field[x][y] = board.getFieldAt(new Point(x,y));
            }
        }
        for (int x = 0; x < width / 3; x++) {
            for (int y = 0; y < height / 3; y++) {
                this.macroboard[x][y] = board.getMacroboardFieldAt(new Point(x,y));
            }
        }
    }

    private void clearBoard() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                field[x][y] = EMPTY_FIELD;
            }
        }
        for (int x = 0; x < width / 3; x++) {
            for (int y = 0; y < height / 3; y++) {
                macroboard[x][y] = AVAILABLE_FIELD;
            }
        }
    }

    /**
     * Creates comma separated String with player ids for the microboards.
     * @return String with player names for every cell, or 'empty' when cell is empty.
     */
    @Override
    public String toString() {
        StringBuilder r = new StringBuilder();
        int counter = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (counter > 0) {
                    r.append(",");
                }
                r.append(field[x][y]);
                counter++;
            }
        }
        return r.toString();
    }

    /**
     * Creates a string with comma separated ints for every cell.
     * Format:		 LSB
     * 0 0 0 0 0 0 0 0
     * | | | | | | | |_ Player 1
     * | | | | | | |___ Player 2
     * | | | | | |_____ Active Player 1
     * | | | | |_______ Active Player 2
     * | | | |_________ Taken Player 1
     * | | |___________ Taken Player 2
     * | |_____________ Reserved
     * |_______________ Reserved
     */
    public String toPresentationString(int nextPlayer, Boolean showPossibleMoves) { /* TODO: this forces bot ids 0 and 1*/
        StringBuilder r = new StringBuilder();
        int counter = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int b = 0;
                if (field[x][y].equals("0")) {
                    b = b | (1 << 0);
                }
                if (field[x][y].equals("1")) {
                    b = b | (1 << 1);
                }
                if (showPossibleMoves) {
                    if (isInActiveMicroboard(x, y) && nextPlayer == 1 && field[x][y].equals(EMPTY_FIELD)) {
                        b = b | (1 << 2);
                    }
                    if (isInActiveMicroboard(x, y) && nextPlayer == 0 && field[x][y].equals(EMPTY_FIELD)) {
                        b = b | (1 << 3);
                    }
                }
                if (macroboard[x/3][y/3].equals("0")) {
                    b = b | (1 << 4);
                }
                if (macroboard[x/3][y/3].equals("1")) {
                    b = b | (1 << 5);
                }
                if (counter > 0) {
                    r.append(",");
                }
                r.append(b);
                counter++;
            }
        }
        return r.toString();
    }

    /**
     * Creates comma separated String with player ids for the macroboard.
     * @return String with player ids for every cell, or 0 when cell is empty,
     * or -1 when cell is ready for a move.
     */
    public String macroboardToString(Point lastMove) {
        updateMacroboard(lastMove);
        StringBuilder r = new StringBuilder();
        int counter = 0;
        for (int y = 0; y < this.height / 3; y++) {
            for (int x = 0; x < this.width / 3; x++) {
                if (counter > 0) {
                    r.append(",");
                }
                r.append(macroboard[x][y]);
                counter++;
            }
        }
        return r.toString();
    }

    public void initialiseFromString(String input, int w, int h) {
        String[] s = input.split(",");
        this.width = w;
        this.height = h;
        this.field = new String[w][h];
        int x = 0, y = 0;
        for (String value : s) {
            this.field[x][y] = value;
            if (++x == w) {
                x = 0;
                y++;
            }
        }
    }

    /**
     * Checks whether the field is full
     * @return : Returns true when field is full, otherwise returns false.
     */
    public boolean boardIsFull() {
        for (int y = 0; y < this.height; y++)
            for (int x = 0; x < this.width; x++)
                if (field[x][y] == EMPTY_FIELD)
                    return false; // At least one cell is not filled
        // All cells are filled
        return true;
    }

    /**
     * Checks the microboards for wins and updates internal representation (macroboard)
     */
    public void updateMacroboard(Point lastMove) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                String winner = getMicroboardWinner(x, y);
                if (winner != null)
                    macroboard[x][y] = String.valueOf(winner);
                else
                    macroboard[x][y] = EMPTY_FIELD;
            }
        }
        if (lastMove != null && !microboardFullOrTaken(lastMove.x%3, lastMove.y%3)) {
            macroboard[lastMove.x%3][lastMove.y%3] = AVAILABLE_FIELD;
        } else {
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    if (!microboardFullOrTaken(x, y)) {
                        macroboard[x][y] = AVAILABLE_FIELD;
                    }
                }
            }
        }
    }

    /**
     * Checks the microboard for a winner
     * @return player id of winner or EMPTY_FIELD if no winner
     */
    private String getMicroboardWinner(int macroX, int macroY) {
        int startX = macroX*3;
        int startY = macroY*3;
		/* Check horizontal wins */
        for (int y = startY; y < startY+3; y++) {
            if (field[startX][y].equals(field[startX+1][y]) && field[startX+1][y].equals(field[startX+2][y]) && !field[startX][y].equals(EMPTY_FIELD) && !field[startX][y].equals(AVAILABLE_FIELD)) {
                return field[startX][y];
            }
        }
		/* Check vertical wins */
        for (int x = startX; x < startX+3; x++) {
            if (field[x][startY].equals(field[x][startY+1]) && field[x][startY+1].equals(field[x][startY+2]) && !field[x][startY].equals(EMPTY_FIELD) && !field[x][startY].equals(AVAILABLE_FIELD)) {
                return field[x][startY];
            }
        }
		/* Check diagonal wins */
        if (field[startX][startY].equals(field[startX+1][startY+1]) && field[startX+1][startY+1].equals(field[startX+2][startY+2]) && !field[startX][startY].equals(EMPTY_FIELD)  && !field[startX][startY].equals(AVAILABLE_FIELD) ) {
            return field[startX][startY];
        }
        if (field[startX+2][startY].equals(field[startX+1][startY+1]) && field[startX+1][startY+1].equals(field[startX][startY+2]) && !field[startX+2][startY].equals(EMPTY_FIELD) && !field[startX+2][startY].equals(AVAILABLE_FIELD)) {
            return field[startX+2][startY];
        }
        return null;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getFieldAt(Point c) {
        return field[c.x][c.y];
    }

    public void setFieldAt(Point c, String v) {
        field[c.x][c.y] = v;
    }

    public String getMacroboardFieldAt(Point c) {
        return macroboard[c.x][c.y];
    }

    public void setMacroboardFieldAt(Point c, String v) {
        macroboard[c.x][c.y] = v;
    }

    public void dump() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                System.out.print(field[x][y]);
            }
            System.out.println();
        }
    }

    public void dumpMacroboard() {
        for (int y = 0; y < this.height /3; y++) {
            for (int x = 0; x < this.width/3; x++) {
                System.out.print(macroboard[x][y]);
            }
            System.out.println();
        }
    }

    /**
     * Returns whether microboard is full OR taken
     * @return Boolean
     */
    private Boolean microboardFullOrTaken(int x, int y) {
        if (x < 0 || y < 0) return true; /* empty board */

        if (!macroboard[x][y].equals(EMPTY_FIELD) && !macroboard[x][y].equals(AVAILABLE_FIELD)) { /* microboard is taken */
            return true;
        }
        for (int my = y*3; my < y*3+3; my++) {
            for (int mx = x*3; mx < x*3+3; mx++) {
                if (Objects.equals(field[mx][my], EMPTY_FIELD))
                    return false;
            }
        }
        return true; /* microboard is full */
    }

    /**
     * Returns whether field is in active microboard
     * @return Boolean
     */
    public Boolean isInActiveMicroboard(int x, int y) {
        return macroboard[(int) Math.floor(x/3)][(int) Math.floor(y/3)] == AVAILABLE_FIELD;
    }

    /**
     * Checks the macroboard for a winner
     * @return player id of winner or EMPTY_FIELD if no winner
     */
    public Integer getMacroboardWinner() {
		/* Check horizontal wins */
        for (int y = 0; y < 3; y++) {
            if (macroboard[0][y].equals(macroboard[1][y]) && macroboard[1][y].equals(macroboard[2][y]) && !macroboard[0][y].equals(EMPTY_FIELD) && !macroboard[0][y].equals(AVAILABLE_FIELD)) {
                return Integer.parseInt(macroboard[0][y]);
            }
        }
		/* Check vertical wins */
        for (int x = 0; x < 3; x++) {
            if (Objects.equals(macroboard[x][0], macroboard[x][1]) && Objects.equals(macroboard[x][1], macroboard[x][2]) && !macroboard[x][0].equals(EMPTY_FIELD) && !macroboard[x][0].equals(AVAILABLE_FIELD)) {
                return Integer.parseInt(macroboard[x][0]);
            }
        }
		/* Check diagonal wins */
        if (macroboard[0][0].equals(macroboard[1][1]) && macroboard[1][1].equals(macroboard[2][2]) && !macroboard[0][0].equals(EMPTY_FIELD) && !macroboard[0][0].equals(AVAILABLE_FIELD)) {
            return Integer.parseInt(macroboard[0][0]);
        }
        if (macroboard[2][0].equals(macroboard[1][1]) && macroboard[1][1].equals(macroboard[0][2]) && !macroboard[2][0].equals(EMPTY_FIELD) && !macroboard[2][0].equals(AVAILABLE_FIELD)) {
            return Integer.parseInt(macroboard[2][0]);
        }
        return null;
    }
}

