package com.theaigames.tictactoe.moves;

import com.theaigames.tictactoe.field.Field;
import com.theaigames.tictactoe.player.Player;

public class MoveResult {
	private Field mOldField, mNewField;
	private int mMoveNumber = 0;
	private Player mPlayer;
	private Move mMove;

	public MoveResult(Player player, Move move, Field oldField, Field newField) {
	    mPlayer = player;
	    mMove = move;
		mOldField = oldField;
		mNewField = newField;
	}
	
	public String getOldFieldPresentationString() {
	    return mOldField.toPresentationString(mPlayer.getId());
	}
	
	public String getNewFieldPresentationString() {
        return mNewField.toPresentationString(mPlayer.getId());
    }

	public Player getPlayer() {
		return mPlayer;
	}
	
	public Move getMove() {
	    return mMove;
	}
	
	public void setMoveNumber(int moveNumber) {
		mMoveNumber = moveNumber;
	}
	
	public int getMoveNumber() {
		return mMoveNumber;
	}
}
