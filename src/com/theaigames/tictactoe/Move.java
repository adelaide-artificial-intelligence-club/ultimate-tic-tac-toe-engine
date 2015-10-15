package com.theaigames.tictactoe;

import com.theaigames.game.moves.AbstractMove;
import com.theaigames.game.player.AbstractPlayer;

public class Move extends AbstractMove {

	private int mColumn = 0;
	
	public Move(AbstractPlayer player) {
		super(player);
	}
	
	/**
	 * @param column : Sets the column of a move
	 */
	public void setColumn(int column) {
		this.mColumn = column;
	}
	
	/**
	 * @return : Column of move
	 */
	public int getColumn() {
		return mColumn;
	}
}
