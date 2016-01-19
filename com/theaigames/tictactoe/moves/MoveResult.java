package com.theaigames.tictactoe.moves;

import com.theaigames.game.player.AbstractPlayer;
import com.theaigames.tictactoe.field.Field;

public class MoveResult extends Move {
	private String mBoard, mPresentationString;
	private int mPlayerId;
	private int mPlayer1Fields, mPlayer2Fields;
	private int mRoundNumber = 0;

	public MoveResult(AbstractPlayer player, Field field, int playerId) {
		super(player);
		mBoard = field.toString();
		mPresentationString = field.toPresentationString(playerId);
		mPlayerId = playerId;
	}
	
	public String toString() {
		return mBoard;
	}
	
	public String getPresentationString() {
		return mPresentationString;
	}

	public int getPlayerId() {
		return mPlayerId;
	}
	
	public void setPlayer1Fields(int fields) {
		mPlayer1Fields = fields;
	}
	
	public int getPlayer1Fields() {
		return mPlayer1Fields;
	}

	public void setPlayer2Fields(int fields) {
		mPlayer2Fields = fields;
	}
	
	public int getPlayer2Fields() {
		return mPlayer2Fields;
	}
	
	public void setRoundNumber(int roundNumber) {
		mRoundNumber = roundNumber;
	}
	
	public int getRoundNumber() {
		return mRoundNumber;
	}
}
