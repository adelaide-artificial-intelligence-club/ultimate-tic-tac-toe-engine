package com.theaigames.tictactoe.field;

public class Field {
	/* For visuals only */
	private final int WINTYPE_NONE = 0;
	private final int WINTYPE_HORIZONTAL1 = 1;
	private final int WINTYPE_HORIZONTAL2 = 2;
	private final int WINTYPE_HORIZONTAL3 = 3;
	private final int WINTYPE_VERTICAL1 = 4;
	private final int WINTYPE_VERTICAL2 = 5;
	private final int WINTYPE_VERTICAL3 = 6;
	private final int WINTYPE_DIAGONAL = 7;
	private final int WINTYPE_ANTIDIAGONAL = 8;
	
	private int[][] mBoard;
	private int[][] mMacroboard;
	private int[][] mMacroboardWinTypes;

	private int mCols = 0, mRows = 0;
	private String mLastError = "";
	private int mLastX = 0, mLastY = 0;
	private Boolean mAllMicroboardsActive = true;
	
	public Field() {
		mCols = 9;
		mRows = 9;
		mBoard = new int[mCols][mRows];
		mMacroboard = new int[mCols / 3][mRows / 3];
		mMacroboardWinTypes = new int[mCols / 3][mRows / 3];
		clearBoard();
	}
	
	public Field(int[][] mBoard, int[][] mMacroboard, int[][] mMacroboardWinTypes, 
	        int mCols, int mRows, String mLastError, int mLastX, int mLastY, Boolean mAllMicroboardsActive) {
	    this.mBoard = mBoard;
	    this.mMacroboard = mMacroboard;
	    this.mMacroboardWinTypes = mMacroboardWinTypes;
	    this.mCols = mCols;
	    this.mRows = mRows;
	    this.mLastError = mLastError;
	    this.mLastX = mLastX;
	    this.mLastY = mLastY;
	    this.mAllMicroboardsActive = mAllMicroboardsActive;
	}
	
	public Field copy() {
	    return new Field(mBoard, mMacroboard, mMacroboardWinTypes, mCols, mRows, 
	            mLastError, mLastX, mLastY, mAllMicroboardsActive);
	}
	
	public void clearBoard() {
		for (int x = 0; x < mCols; x++) {
			for (int y = 0; y < mRows; y++) {
				mBoard[x][y] = 0;
			}
		}
	}
	
	public void dumpBoard() {
		System.out.print("\n\n");
		for (int y = 0; y < mRows; y++) {
			for (int x = 0; x < mCols; x++) {
				System.out.print(mBoard[x][y]);
				if (x < mCols-1) {
					String s = ", ";
					if (x % 3 == 2) {
						s = "| ";
					}
					if (x == mLastX && y == mLastY) {
						s = "* ";
					}
					System.out.print(s);
				}
			}
			if (y % 3 == 2) {
				System.out.print("\n");
				for (int x = 0; x < mCols-1; x++) {
					System.out.print("---");
				}
			}
			System.out.print("\n");
		}
		System.out.print("Macroboard:\n");
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {

				System.out.print(padRight(mMacroboard[x][y] + "",3));
			}
			System.out.print("\n");
		}
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}
	
	/**
	 * Adds a move to the board
	 * @param args : int x, int y, int move
	 * @return : true if disc fits, otherwise false
	 */
	public Boolean addMove(int x, int y, int move) {
		mLastError = "";
		if (x < mCols && y < mRows && x >= 0 && y >= 0) { /* Move within range */
			if (isInActiveMicroboard(x, y)) { /* Move in active microboard */
				if (mBoard[x][y] == 0) { /*Field is available */
					mBoard[x][y] = move;
					mLastX = x;
					mLastY = y;
					mAllMicroboardsActive = false;
					updateMacroboards();
					return true;
				} else {
					mLastError = "Position is full.";
					//System.out.println(mLastError);
				}
			} else {
				mLastError = "Not in active macroboard.";
				//System.out.println(mLastError);
			}
		} else {
			mLastError = "Move out of bounds.";
			//System.out.println(mLastError);
		}
		return false;
	}
	
	public Boolean isInActiveMicroboard(int x, int y) {
		if (mAllMicroboardsActive) {
			return (mMacroboard[x/3][y/3] == 0);
		}
		return (Math.floor(x/3) == getActiveMicroboardX() && Math.floor(y/3) == getActiveMicroboardY()); 
	}
	
	public int getActiveMicroboardX() {
		if (mAllMicroboardsActive) return -1;
		return mLastX%3;
	}
	
	public int getActiveMicroboardY() {
		if (mAllMicroboardsActive) return -1;
		return mLastY%3;
	}
	
	/**
	 * Returns reason why addMove returns false
	 * @param args : 
	 * @return : reason why addMove returns false
	 */
	public String getLastError() {
		return mLastError;
	}
	
	public void setLastError(String error) {
	    mLastError = error;
	}
	
	/**
	 * Returns last inserted column
	 * @param args : 
	 * @return : last inserted column
	 */
	public int getLastX() {
		return mLastX;
	}
	
	/**
	 * Returns last inserted row
	 * @param args : 
	 * @return : last inserted row
	 */
	public int getLastY() {
		return mLastY;
	}
	
	@Override
	/**
	 * Creates comma separated String with player ids for the microboards.
	 * @param args : 
	 * @return : String with player names for every cell, or 'empty' when cell is empty.
	 */
	public String toString() {
		String r = "";
		int counter = 0;
		for (int y = 0; y < mRows; y++) {
			for (int x = 0; x < mCols; x++) {
				if (counter > 0) {
					r += ",";
				}
				r += mBoard[x][y];
				counter++;
			}
		}
		return r;
	}
	
	/**
	 * Creates a string with comma separated ints for every cell.
	 * @param args : 
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
	public String toPresentationString(int currentPlayer) {
		String r = "";
		int counter = 0;
		for (int y = 0; y < mRows; y++) {
			for (int x = 0; x < mCols; x++) {
				int b = 0;
				if (mBoard[x][y] == 1) {
					b = b | (1 << 0);
				}
				if (mBoard[x][y] == 2) {
					b = b | (1 << 1);
				}
				if (isInActiveMicroboard(x, y) && currentPlayer == 1 && mBoard[x][y] == 0) {
					b = b | (1 << 2);
				}
				if (isInActiveMicroboard(x, y) && currentPlayer == 2 && mBoard[x][y] == 0) {
					b = b | (1 << 3);
				}
				if (mMacroboard[x/3][y/3] == 1) {
					b = b | (1 << 4);
				}
				if (mMacroboard[x/3][y/3] == 2) {
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
	 * @param args : 
	 * @return : String with player ids for every cell, or 0 when cell is empty, or -1 when cell is ready for a move.
	 */
	public String macroboardToString() {
		String r = "";
		int counter = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (counter > 0) {
					r += ",";
				}
				r += mMacroboard[x][y];
				counter++;
			}
		}
		return r;
	}
	
	/**
	 * Creates comma separated String with win types for all cells (visualisation purposes only)
	 * @param args : 
	 * @return : String with win types for every cell
	 */
	public String macroboardWinTypesToString() {
		updateMacroboardWinTypes();
		String r = "";
		int counter = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (counter > 0) {
					r += ",";
				}
				r += mMacroboardWinTypes[x][y];
				counter++;
			}
		}
		return r;
	}
	
//	/**
//	 * Returns the number of fields a player has in the macroboard
//	 * @param args int player id: 
//	 * @return : Int with the number of fields a player has in the macroboard
//	 */
//	public int getPlayerFields(int playerid) {
//		int counter = 0;
//		for (int y = 0; y < 3; y++) {
//			for (int x = 0; x < 3; x++) {
//				if (mMacroboard[x][y] == playerid) {
//					counter++;
//				}
//			}
//		}
//		return counter;
//	}
	
	/**
	 * Checks whether the field is full
	 * @param args : 
	 * @return : Returns true when field is full, otherwise returns false.
	 */
	public boolean isFull() {
		for (int x = 0; x < mCols; x++)
		  for (int y = 0; y < mRows; y++)
		    if (mBoard[x][y] == 0)
		      return false; // At least one cell is not filled
		// All cells are filled
		return true;
	}
	
	/**
	 * Checks if there is a winner, if so, returns player id.
	 * @param args : 
	 * @return : Returns player id if there is a winner, otherwise returns 0.
	 */
	public int getWinner() {
		updateMacroboards();
		return checkMacroboardWinner();
	}
	
	/**
	 * Checks the microboards for wins and updates internal representation (macroboard)
	 * @param args : 
	 * @return : 
	 */
	public void updateMacroboards() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				int winner = getMicroboardWinner(x, y);
				mMacroboard[x][y] = winner;
				if (x == getActiveMicroboardX() && y == getActiveMicroboardY()) {
					if (mMacroboard[x][y] == 0) {
						mMacroboard[x][y] = -1;
					} else {
						mAllMicroboardsActive = true;
					}
				}
			}
		}
	}	
	
	/**
	 * Update internal representation of wintypes per cell.
	 * @param args : 
	 * @return : 
	 */
	public void updateMacroboardWinTypes() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				int wintype = getMicroboardWinType(x, y);
				mMacroboardWinTypes[x][y] = wintype;
				if (x == getActiveMicroboardX() && y == getActiveMicroboardY()) {
					if (mMacroboardWinTypes[x][y] == 0) {
						mMacroboardWinTypes[x][y] = -1;
					}
				}
			}
		}
	}
	
	/**
	 * Checks the microboard for a winner
	 * @param args : int x (0-2), int y (0-2)
	 * @return : player id of winner or 0
	 */
	public int getMicroboardWinner(int macroX, int macroY) {
		int startX = macroX*3;
		int startY = macroY*3;
		/* Check horizontal wins */
		for (int y = startY; y < startY+3; y++) {
			if (mBoard[startX+0][y] == mBoard[startX+1][y] && mBoard[startX+1][y] == mBoard[startX+2][y] && mBoard[startX+0][y] > 0) {
				//System.out.println("FOUND A HORIZONTAL WIN AT " + y);
				return mBoard[startX+0][y];
			}
		}
		/* Check vertical wins */
		for (int x = startX; x < startX+3; x++) {
			if (mBoard[x][startY+0] == mBoard[x][startY+1] && mBoard[x][startY+1] == mBoard[x][startY+2] && mBoard[x][startY+0] > 0) {
				//System.out.println("FOUND A VERTICAL WIN AT " + x);
				return mBoard[x][startY+0];
			}
		}
		/* Check diagonal wins */
		if (mBoard[startX][startY] == mBoard[startX+1][startY+1] && mBoard[startX+1][startY+1] == mBoard[startX+2][startY+2] && mBoard[startX+0][startY+0] > 0) {
			//System.out.println("FOUND A DIAGONAL WIN AT " + startX);
			return mBoard[startX][startY];
		}
		if (mBoard[startX+2][startY] == mBoard[startX+1][startY+1] && mBoard[startX+1][startY+1] == mBoard[startX][startY+2] && mBoard[startX+2][startY+0] > 0) {
			//System.out.println("FOUND A ANTIDIAGONAL WIN AT " + startX);
			return mBoard[startX+2][startY];
		}
		return 0;
	}
	
	/**
	 * Checks the microboard for a winner
	 * @param args : int x (0-2), int y (0-2)
	 * @return : player id of winner or 0
	 */
	public int getMicroboardWinType(int macroX, int macroY) {
		int startX = macroX*3;
		int startY = macroY*3;
		/* Check horizontal wins */
		for (int y = startY; y < startY+3; y++) {
			if (mBoard[startX+0][y] == mBoard[startX+1][y] && mBoard[startX+1][y] == mBoard[startX+2][y] && mBoard[startX+0][y] > 0) {
				int tWin = y-macroY*3;
				return WINTYPE_HORIZONTAL1 + tWin;
			}
		}
		/* Check vertical wins */
		for (int x = startX; x < startX+3; x++) {
			if (mBoard[x][startY+0] == mBoard[x][startY+1] && mBoard[x][startY+1] == mBoard[x][startY+2] && mBoard[x][startY+0] > 0) {
				int tWin = x-macroX*3;
				return WINTYPE_VERTICAL1 + tWin;
			}
		}
		/* Check diagonal wins */
		if (mBoard[startX][startY] == mBoard[startX+1][startY+1] && mBoard[startX+1][startY+1] == mBoard[startX+2][startY+2] && mBoard[startX+0][startY+0] > 0) {
			return WINTYPE_DIAGONAL;
		}
		if (mBoard[startX+2][startY] == mBoard[startX+1][startY+1] && mBoard[startX+1][startY+1] == mBoard[startX][startY+2] && mBoard[startX+2][startY+0] > 0) {
			return WINTYPE_ANTIDIAGONAL;
		}
		return 0;
	}
	
	public int checkMacroboardWinner() {
		/* Check horizontal wins */
		for (int y = 0; y < 3; y++) {
			if (mMacroboard[0][y] == mMacroboard[1][y] && mMacroboard[1][y] == mMacroboard[2][y] && mMacroboard[0][y] > 0) {
				//System.out.println("FOUND A HORIZONTAL MACROWIN AT " + y);
				return mMacroboard[0][y];
			}
		}
		/* Check vertical wins */
		for (int x = 0; x < 3; x++) {
			if (mMacroboard[x][0] == mMacroboard[x][1] && mMacroboard[x][1] == mMacroboard[x][2] && mMacroboard[x][0] > 0) {
				//System.out.println("FOUND A VERTICAL MACROWIN AT " + x);
				return mMacroboard[x][0];
			}
		}
		/* Check diagonal wins */
		if (mMacroboard[0][0] == mMacroboard[1][1] && mMacroboard[1][1] == mMacroboard[2][2] && mMacroboard[0][0] > 0) {
			//System.out.println("FOUND A DIAGONAL MACROWIN");
			return mMacroboard[0][0];
		}
		if (mMacroboard[2][0] == mMacroboard[1][1] && mMacroboard[1][1] == mMacroboard[0][2] && mMacroboard[2][0] > 0) {
			//System.out.println("FOUND A ANTIDIAGONAL MACROWIN");
			return mMacroboard[2][0];
		}
		return 0;
	}

	public int getNrColumns() {
		return mCols;
	}
	
	public int getNrRows() {
		return mRows;
	}
}
