package io.riddles.tictactoe.engine;

import io.riddles.javainterface.exception.TerminalException;
import io.riddles.tictactoe.game.TicTacToeSerializer;
import io.riddles.tictactoe.game.data.TicTacToeBoard;
import io.riddles.tictactoe.game.player.TicTacToePlayer;
import io.riddles.tictactoe.game.processor.TicTacToeProcessor;
import io.riddles.tictactoe.game.state.TicTacToeState;
import io.riddles.javainterface.engine.AbstractEngine;

/**
 * TicTacToeEngine:
 * - Creates a Processor, the Players and an initial State
 * - Parses the setup input
 * - Sends settings to the players
 * - Runs a game
 * - Returns the played game at the end of the game
 *
 * Created by joost on 6/27/16.
 */
public class TicTacToeEngine extends AbstractEngine<TicTacToeProcessor, TicTacToePlayer, TicTacToeState> {

    public TicTacToeEngine() throws TerminalException {

        super(new String[0]);
        setDefaults();
    }

    public TicTacToeEngine(String args[]) throws TerminalException {
        super(args);
    }

    public TicTacToeEngine(String wrapperFile, String[] botFiles) throws TerminalException {

        super(wrapperFile, botFiles);
        setDefaults();
    }

    private void setDefaults() {
        configuration.put("maxRounds", 20);
        configuration.put("fieldWidth", 16);
        configuration.put("fieldHeight", 16);
    }


    /* createPlayer creates and initialises a Player for the game.
     * returns: a Player
     */
    @Override
    protected TicTacToePlayer createPlayer(int id) {
        TicTacToePlayer p = new TicTacToePlayer(id);
        return p;
    }

    /* createProcessor creates and initialises a Processor for the game.
     * returns: a Processor
     */
    @Override
    protected TicTacToeProcessor createProcessor() {

        /* We're going for one-based indexes for playerId's so we can use 0's for empty fields.
         * This makes sure existing bots will still work with TicTacToe, when used with the new wrapper.
         */
        for (TicTacToePlayer player : this.players) {
            player.setId(player.getId() + 1);
        }
        return new TicTacToeProcessor(this.players);
    }

    /* sendGameSettings sends the game settings to a Player
     * returns:
     */
    @Override
    protected void sendGameSettings(TicTacToePlayer player) {
    }

    /* getPlayedGame creates a serializer and serialises the game
     * returns: String with the serialised game.
     */
    @Override
    protected String getPlayedGame(TicTacToeState state) {
        TicTacToeSerializer serializer = new TicTacToeSerializer();
        return serializer.traverseToString(this.processor, state);
    }

    /* getInitialState creates an initial state to start the game with.
     * returns: TicTacToeState
     */
    @Override
    protected TicTacToeState getInitialState() {
        TicTacToeState s = new TicTacToeState();
        s.setBoard(new TicTacToeBoard(9,9));
        return s;
    }
}
