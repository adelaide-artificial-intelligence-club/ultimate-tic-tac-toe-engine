package io.riddles.tictactoe.game.state;

import io.riddles.javainterface.game.state.AbstractPlayerState;
import io.riddles.tictactoe.game.move.TicTacToeMove;


/**
 * Created by Niko on 23/11/2016.
 */
public class TicTacToePlayerState extends AbstractPlayerState {

    public TicTacToePlayerState(int playerId) {
        super(playerId);
    }

    public TicTacToePlayerState clone() {

        TicTacToePlayerState psClone = new TicTacToePlayerState(this.playerId);
        return psClone;
    }



    public int getPlayerId() { return this.playerId; }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String toString() {
        return "PlayerState p" + this.getPlayerId();
    }
}
