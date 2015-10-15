package com.theaigames.tictactoe;

import com.theaigames.game.moves.AbstractMove;
import com.theaigames.game.player.AbstractPlayer;

public class MoveResult extends Move {
	private String mBoard;
	private int mPlayerId;

	public MoveResult(AbstractPlayer player, Field field, int playerId) {
		super(player);
		mBoard = field.toString();
		mPlayerId = playerId;
	}
	
	public String toString() {
		return mBoard;
	}
	

	public int getPlayerId() {
		return mPlayerId;
	}
}
