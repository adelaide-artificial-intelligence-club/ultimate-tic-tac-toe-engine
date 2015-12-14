// Copyright 2015 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//	
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package com.theaigames.tictactoe;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.theaigames.engine.io.IOPlayer;
import com.theaigames.game.moves.AbstractMove;
import com.theaigames.game.player.AbstractPlayer;
import com.theaigames.game.GameHandler;

public class Processor implements GameHandler {
	
	private int mRoundNumber = 0;
	private List<Player> mPlayers;
	private List<Move> mMoves;
	private List<Disc> mWinningDiscs;
	private List<MoveResult> mMoveResults;
	private Field mField;
	private int mGameOverByPlayerErrorPlayerId = 0;

	public Processor(List<Player> players, Field field) {
		mPlayers = players;
		mField = field;
		mMoves = new ArrayList<Move>();
		mMoveResults = new ArrayList<MoveResult>();
	}

	@Override
	public void playRound(int roundNumber) {
		for (Player player : mPlayers) {
			if (getWinner() == null) {
				player.sendUpdate("round", mRoundNumber);
				player.sendUpdate("macroboard", mField.macroboardToString());
				String response = player.requestMove("move");
				Move move = new Move(player);
				MoveResult moveResult = new MoveResult(player, mField, player.getId());
				if (parseResponse(response, player)) {
					move.setColumn(mField.getLastX());
					move.setIllegalMove(mField.getLastError());
					mMoves.add(move);
					moveResult = new MoveResult(player, mField, player.getId());
					moveResult.setColumn(mField.getLastX());
					moveResult.setIllegalMove(mField.getLastError());
					moveResult.setPlayer1Fields(mField.getPlayerFields(1));
					moveResult.setPlayer2Fields(mField.getPlayerFields(2));
					mMoveResults.add(moveResult);
				} else {
					move = new Move(player); moveResult = new MoveResult(player, mField, player.getId());
					move.setColumn(mField.getLastX());
					move.setIllegalMove(mField.getLastError() + " (first try)");
					mMoves.add(move);
					moveResult.setColumn(mField.getLastX());
					moveResult.setIllegalMove(mField.getLastError() + " (first try)");
					moveResult.setPlayer1Fields(mField.getPlayerFields(1));
					moveResult.setPlayer2Fields(mField.getPlayerFields(2));
					mMoveResults.add(moveResult);
					player.sendUpdate("field", mField.toString());
					response = player.requestMove("move");
					if (parseResponse(response, player)) {
						move = new Move(player); moveResult = new MoveResult(player, mField, player.getId());
						move.setColumn(mField.getLastX());
						mMoves.add(move);
						moveResult.setColumn(mField.getLastX());
						moveResult.setPlayer1Fields(mField.getPlayerFields(1));
						moveResult.setPlayer2Fields(mField.getPlayerFields(2));
						mMoveResults.add(moveResult);
					} else {
						move = new Move(player); moveResult = new MoveResult(player, mField, player.getId());
						move.setColumn(mField.getLastX());
						move.setIllegalMove(mField.getLastError() + " (second try)");
						mMoves.add(move);
						moveResult.setColumn(mField.getLastX());
						moveResult.setIllegalMove(mField.getLastError() + " (second try)");
						mMoveResults.add(moveResult);
						moveResult.setPlayer1Fields(mField.getPlayerFields(1));
						moveResult.setPlayer2Fields(mField.getPlayerFields(2));
						player.sendUpdate("field", mField.toString());
						response = player.requestMove("move");
						if (parseResponse(response, player)) {
							move = new Move(player); moveResult = new MoveResult(player, mField, player.getId());
							move.setColumn(mField.getLastX());
							mMoves.add(move);							
							moveResult.setColumn(mField.getLastX());
							moveResult.setPlayer1Fields(mField.getPlayerFields(1));
							moveResult.setPlayer2Fields(mField.getPlayerFields(2));
							mMoveResults.add(moveResult);
						} else { /* Too many errors, other player wins */
							move = new Move(player); moveResult = new MoveResult(player, mField, player.getId());
							move.setColumn(mField.getLastX());
							move.setIllegalMove(mField.getLastError() + " (last try)");
							mMoves.add(move);
							moveResult.setColumn(mField.getLastX());
							moveResult.setIllegalMove(mField.getLastError() + " (last try)");
							moveResult.setPlayer1Fields(mField.getPlayerFields(1));
							moveResult.setPlayer2Fields(mField.getPlayerFields(2));
							mMoveResults.add(moveResult);
							mGameOverByPlayerErrorPlayerId = player.getId();
						}
					}
				}
				mRoundNumber++;
				player.sendUpdate("field", mField.toString());
				mField.dumpBoard();
			}
		}
	}
	
	/**
	 * Parses player response and inserts disc in field
	 * @param args : command line arguments passed on running of application
	 * @return : true if valid move, otherwise false
	 */
	private Boolean parseResponse(String r, Player player) {
		String[] parts = r.split(" ");
		System.out.println("r " + r);
        if (parts[0].equals("place_move")) {
        	int column = Integer.parseInt(parts[1]);
        	int row = Integer.parseInt(parts[2]);

        	if (mField.addMove(column, row, player.getId())) {
        		return true;
        	}
        }
        return false;
	}

	@Override
	public int getRoundNumber() {
		return this.mRoundNumber;
	}

	@Override
	public AbstractPlayer getWinner() {
		int winner = mField.getWinner();
		if (mGameOverByPlayerErrorPlayerId > 0) { /* Game over due to too many player errors. Look up the other player, which became the winner */
			for (Player player : mPlayers) {
				if (player.getId() != mGameOverByPlayerErrorPlayerId) {
					return player;
				}
			}
		}
		if (winner != 0) {
			for (Player player : mPlayers) {
				if (player.getId() == winner) {
					return player;
				}
			}
		}
		return null;
	}

	@Override
	public String getPlayedGame() {
		JSONObject output = new JSONObject();
		AbstractPlayer winner = getWinner();
		
		/* Create array of winning discs */
		try {
			
			JSONArray playerNames = new JSONArray();
			for(Player player : this.mPlayers) {
				playerNames.put(player.getName());
			}
			
			output.put("settings", new JSONObject()
			.put("field", new JSONObject()
					.put("width", String.valueOf(getField().getNrColumns()))
					.put("height", String.valueOf(getField().getNrRows())))
			.put("players", new JSONObject()
					.put("count", this.mPlayers.size()) // could maybe be removed
					.put("names", playerNames))
					.put("winnerplayer", winner.getName())
			);		
			//output.put("windiscs", winDiscsJSON);
			
			JSONArray states = new JSONArray();
			JSONObject state = new JSONObject();
			int counter = 0;
			String winnerstring = "";
			for (MoveResult move : mMoveResults) {
				/* TODO: fix this, for example when a game is a draw */
				if (counter == mMoveResults.size()-1) {
					winnerstring = winner.getName();
				}
				state = new JSONObject();
				state.put("field", move.toString());
				state.put("macroboard", move.macroboardToString());
				state.put("round", counter);
				state.put("column", move.getColumn());
				state.put("winner", winnerstring);
				state.put("player", move.getPlayerId());
				state.put("illegalMove", move.getIllegalMove());
				state.put("player1fields", move.getPlayer1Fields());
				state.put("player2fields", move.getPlayer2Fields());
				states.put(state);
				counter++;
			}
			output.put("states", states);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}
	

	/**
	 * Returns a List of Moves played in this game
	 * @param args : 
	 * @return : List with Move objects
	 */
	public List<Move> getMoves() {
		return mMoves;
	}
	
	public Field getField() {
		return mField;
	}

	@Override
	public boolean isGameOver() {
		return (getWinner() != null || mField.isFull());
	}
}