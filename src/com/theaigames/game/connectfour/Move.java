package com.theaigames.game.connectfour;

import com.theaigames.game.AbstractMove;
import com.theaigames.game.AbstractPlayer;

public class Move extends AbstractMove {

	private int mColumn = 0;
	
	public Move(AbstractPlayer player) {
		super(player);
		// TODO Auto-generated constructor stub
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