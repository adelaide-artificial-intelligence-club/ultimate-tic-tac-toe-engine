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

package com.theaigames.game.connectfour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.theaigames.engine.io.IOPlayer;
import com.theaigames.game.AbstractMove;
import com.theaigames.game.AbstractPlayer;
import com.theaigames.game.GameHandler;

public class Processor implements GameHandler {
	
	private int mRoundNumber = 0;
	private List<Player> mPlayers;
	private List<Move> mMoves;
	private List<MoveResult> mMoveResults;
	private Field mField;
	
	private final int MAX_TRIES = 3;

	public Processor(List<Player> players, Field field) {
		mPlayers = players;
		mField = field;
		mMoves = new ArrayList<Move>();
		mMoveResults = new ArrayList<MoveResult>();
	}

	@Override
	public void playRound(int roundNumber) {
		mRoundNumber = roundNumber;
		int playerNr = 1;
		for (Player player : mPlayers) {
			if (getWinner() == null) {
				String response = player.requestMove("move");
				System.out.println("Response from " + player.getName() + ": " + response);
				Move move = new Move(player);
				int counter = 0;
				while (!parseResponse(response, player) && counter < MAX_TRIES) { /* When move is invalid, keep asking for a new move until MAX_TRIES. */
					move.setIllegalMove(mField.getLastError());
					
					response = player.requestMove("move");
					System.out.println("Response from " + player.getName() + ": " + mField.getLastError());
					counter ++;
					if (counter >= MAX_TRIES) {
						move.setIllegalMove("Max tries exceeded.");
					}
					mMoves.add(move);
					MoveResult moveResult = new MoveResult(player, mField);
					moveResult.setIllegalMove(move.getIllegalMove());
					mMoveResults.add(moveResult);
				}
				player.sendUpdate("field", player, mField.toString());
				playerNr++;
			}
		}
		mField.dumpBoard();
		if (getWinner() != null) {
			System.out.println("** WINNER: " + mField.getWinType());
		}
	}
	
	/**
	 * Parses player response and inserts disc in field
	 * @param args : command line arguments passed on running of application
	 * @return : true if valid move, otherwise false
	 */
	private Boolean parseResponse(String r, Player player) {
		String[] parts = r.split(" ");

        if (parts[0].equals("place_disc")) {
        	int column = Integer.parseInt(parts[1]);
        	if (mField.addDisc(column, player.getName())) {
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
		String winner = mField.getWinnerName();
		if (winner != null) {
			for (Player player : mPlayers) {
				if (player.getName().equals(winner)) {
					return player;
				}
			}

		}
		return null;
	}

	@Override
	public String getPlayedGame() {
		// TODO Auto-generated method stub
		return null;
	}

}
