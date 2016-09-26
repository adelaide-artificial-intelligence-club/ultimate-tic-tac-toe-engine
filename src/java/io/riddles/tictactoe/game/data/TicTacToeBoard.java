package io.riddles.tictactoe.game.data;

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

public class TicTacToeBoard {
    protected int[][] board;
    protected int[][] macroboard;

    protected int width = 9;
    protected int height = 9;
    private Coordinate lastMove = null;

    public static final int EMPTY_FIELD = 0;
    public static final int AVAILABLE_FIELD = -1;



    public TicTacToeBoard(int w, int h) {
        this.width = w;
        this.height = h;
        this.board = new int[w][h];
        this.macroboard = new int[w / 3][h / 3];
        clearBoard();
    }

    public void clearBoard() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board[x][y] = EMPTY_FIELD;
            }
        }
        for (int x = 0; x < width / 3; x++) {
            for (int y = 0; y < height / 3; y++) {
                macroboard[x][y] = AVAILABLE_FIELD;
            }
        }
    }


    @Override
    /**
     * Creates comma separated String with player ids for the microboards.
     * @param :
     * @return : String with player names for every cell, or 'empty' when cell is empty.
     */
    public String toString() {
        String r = "";
        int counter = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (counter > 0) {
                    r += ",";
                }
                r += board[x][y];
                counter++;
            }
        }
        return r;
    }

    /**
     * Creates a string with comma separated ints for every cell.
     * @param :
     * @return : String with comma separated ints for every cell.
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
    public String toPresentationString(int nextPlayer, Boolean showPossibleMoves) {
        String r = "";
        int counter = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int b = 0;
                if (board[x][y] == 1) {
                    b = b | (1 << 0);
                }
                if (board[x][y] == 2) {
                    b = b | (1 << 1);
                }
                if (showPossibleMoves) {
                    if (isInActiveMicroboard(x, y) && nextPlayer == 1 && board[x][y] == EMPTY_FIELD) {
                        b = b | (1 << 2);
                    }
                    if (isInActiveMicroboard(x, y) && nextPlayer == 2 && board[x][y] == EMPTY_FIELD) {
                        b = b | (1 << 3);
                    }
                }
                if (macroboard[x/3][y/3] == 1) {
                    b = b | (1 << 4);
                }
                if (macroboard[x/3][y/3] == 2) {
                    b = b | (1 << 5);
                }
                if (counter > 0) {
                    r += ",";
                }
                r += b;
                counter++;
            }
        }
        return r;
    }

    /**
     * Creates comma separated String with player ids for the macroboard.
     * @param :
     * @return : String with player ids for every cell, or 0 when cell is empty, or -1 when cell is ready for a move.
     */
    public String macroboardToString() {
        String r = "";
        int counter = 0;
        for (int y = 0; y < this.height / 3; y++) {
            for (int x = 0; x < this.width / 3; x++) {
                if (counter > 0) {
                    r += ",";
                }
                r += macroboard[x][y];
                counter++;
            }
        }
        return r;
    }

    public void initialiseFromString(String input, int w, int h) {
        String[] s = input.split(",");
        this.width = w;
        this.height = h;
        this.board = new int[w][h];
        int x = 0, y = 0;
        for (int i = 0; i < s.length; i++) {
            this.board[x][y] = Integer.parseInt(s[i]);
            if (++x == w) {
                x = 0; y++;
            }
        }
    }

    /**
     * Checks whether the field is full
     * @param :
     * @return : Returns true when field is full, otherwise returns false.
     */
    public boolean boardIsFull() {
        for (int y = 0; y < this.height; y++)
            for (int x = 0; x < this.width; x++)
                if (board[x][y] == EMPTY_FIELD)
                    return false; // At least one cell is not filled
        // All cells are filled
        return true;
    }

    /**
     * Checks the microboards for wins and updates internal representation (macroboard)
     * @param :
     * @return :
     */
    public void updateMacroboard() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int winner = getMicroboardWinner(x, y);
                macroboard[x][y] = winner;
            }
        }
        if (lastMove != null && !microboardFullOrTaken(lastMove.getX()%3, lastMove.getY()%3)) {
            macroboard[lastMove.getX()%3][lastMove.getY()%3] = AVAILABLE_FIELD;
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
     * Last move is tracked to determine new active microboard
     * @param : Coordinate c
     * @return :
     */
    public void setLastMove(Coordinate c) { lastMove = c; }

    /**
     * Checks the microboard for a winner
     * @param : int x (0-2), int y (0-2)
     * @return : player id of winner or EMPTY_FIELD if no winner
     */
    public int getMicroboardWinner(int macroX, int macroY) {
        int startX = macroX*3;
        int startY = macroY*3;
		/* Check horizontal wins */
        for (int y = startY; y < startY+3; y++) {
            if (board[startX+0][y] == board[startX+1][y] && board[startX+1][y] == board[startX+2][y] && board[startX+0][y] != EMPTY_FIELD) {
                return board[startX+0][y];
            }
        }
		/* Check vertical wins */
        for (int x = startX; x < startX+3; x++) {
            if (board[x][startY+0] == board[x][startY+1] && board[x][startY+1] == board[x][startY+2] && board[x][startY+0] != EMPTY_FIELD) {
                return board[x][startY+0];
            }
        }
		/* Check diagonal wins */
        if (board[startX][startY] == board[startX+1][startY+1] && board[startX+1][startY+1] == board[startX+2][startY+2] && board[startX+0][startY+0] != EMPTY_FIELD) {
            return board[startX][startY];
        }
        if (board[startX+2][startY] == board[startX+1][startY+1] && board[startX+1][startY+1] == board[startX][startY+2] && board[startX+2][startY+0] != EMPTY_FIELD) {
            return board[startX+2][startY];
        }
        return EMPTY_FIELD;
    }


    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }


    public int getFieldAt(Coordinate c) {
        return board[c.getX()][c.getY()];
    }
    public void setFieldAt(Coordinate c, int v) {
        board[c.getX()][c.getY()] = v;
    }


    public void dump() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                System.out.print(board[x][y]);
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
     * @param : int x, int y
     * @return : Boolean
     */
    public Boolean microboardFullOrTaken(int x, int y) {
        if (x < 0 || y < 0) return true; /* empty board */

        if (macroboard[x][y] != EMPTY_FIELD && macroboard[x][y] != AVAILABLE_FIELD) { /* microboard is taken */
            return true;
        }
        for (int my = y*3; my < y*3+3; my++) {
            for (int mx = x*3; mx < x*3+3; mx++) {
                if (board[mx][my] == EMPTY_FIELD)
                    return false;
            }
        }
        return true; /* microboard is full */
    }

    /**
     * Returns whether field is in active microboard
     * @param : int x, int y
     * @return : Boolean
     */
    public Boolean isInActiveMicroboard(int x, int y) {
        return macroboard[(int) Math.floor(x/3)][(int) Math.floor(y/3)] == AVAILABLE_FIELD;
    }

    /**
     * Checks the macroboard for a winner
     * @param :
     * @return : player id of winner or EMPTY_FIELD if no winner
     */
    public int getMacroboardWinner() {
		/* Check horizontal wins */
        for (int y = 0; y < 3; y++) {
            if (macroboard[0][y] == macroboard[1][y] && macroboard[1][y] == macroboard[2][y] && macroboard[0][y] != EMPTY_FIELD) {
                return macroboard[0][y];
            }
        }
		/* Check vertical wins */
        for (int x = 0; x < 3; x++) {
            if (macroboard[x][0] == macroboard[x][1] && macroboard[x][1] == macroboard[x][2] && macroboard[x][0] != EMPTY_FIELD) {
                return macroboard[x][0];
            }
        }
		/* Check diagonal wins */
        if (macroboard[0][0] == macroboard[1][1] && macroboard[1][1] == macroboard[2][2] && macroboard[0][0] != EMPTY_FIELD) {
            return macroboard[0][0];
        }
        if (macroboard[2][0] == macroboard[1][1] && macroboard[1][1] == macroboard[0][2] && macroboard[2][0] != EMPTY_FIELD) {
            return macroboard[2][0];
        }
        return EMPTY_FIELD;
    }
}

