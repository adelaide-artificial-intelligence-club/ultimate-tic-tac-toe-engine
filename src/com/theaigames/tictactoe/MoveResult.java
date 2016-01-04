package com.theaigames.tictactoe;

import com.theaigames.game.moves.AbstractMove;
import com.theaigames.game.player.AbstractPlayer;

public class MoveResult extends Move {
	private String mBoard, mMacroboard;
	private int mPlayerId;
	private int mPlayer1Fields, mPlayer2Fields;

	public MoveResult(AbstractPlayer player, Field field, int playerId) {
		super(player);
		mBoard = field.toString();
		mMacroboard = field.macroboardToString();
		mPlayerId = playerId;
	}
	
	public String toString() {
		return mBoard;
	}
	
	public String macroboardToString() {
		return mMacroboard;
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
}

