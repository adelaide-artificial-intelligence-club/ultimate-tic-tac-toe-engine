package io.riddles.tictactoe.game.state;

import io.riddles.javainterface.game.state.AbstractPlayerState;
import io.riddles.tictactoe.game.move.TicTacToeMove;


/**
 * Created by Niko on 23/11/2016.
 */
public class TicTacToePlayerState extends AbstractPlayerState {

    int playerId = 0;

    public TicTacToePlayerState() {
    }

    public TicTacToePlayerState(TicTacToeMove move) {

        super(move);
    }


    public TicTacToePlayerState clone() {

        TicTacToePlayerState psClone = new TicTacToePlayerState();
        psClone.setPlayerId(this.playerId);
        return psClone;
    }



    public int getPlayerId() { return this.playerId; }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public TicTacToeMove getMove() {
        if (this.getMoves().size() > 0) {
            return (TicTacToeMove)this.getMoves().get(0);
        }
        return null;
    }


    public String toString() {
        return "PlayerState p" + this.getPlayerId();
    }
}
