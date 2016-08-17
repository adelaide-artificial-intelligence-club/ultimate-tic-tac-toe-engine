package io.riddles.tictactoenew.engine;

import io.riddles.tictactoenew.game.TicTacToeSerializer;
import io.riddles.tictactoenew.game.data.TicTacToeBoard;
import io.riddles.tictactoenew.game.player.TicTacToePlayer;
import io.riddles.tictactoenew.game.processor.TicTacToeProcessor;
import io.riddles.tictactoenew.game.state.TicTacToeState;
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

    public TicTacToeEngine() {

        super();
    }

    public TicTacToeEngine(String wrapperFile, String[] botFiles) {

        super(wrapperFile, botFiles);
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


    /* parseSetupInput parses setup input received from the server.
     * returns:
     */
    @Override
    protected void parseSetupInput(String input) {
        String[] split = input.split(" ");
        String command = split[0];
        if (command.equals("bot_ids")) {
            String[] ids = split[1].split(",");
            for (int i = 0; i < ids.length; i++) {
                TicTacToePlayer player = createPlayer(Integer.parseInt(ids[i]));

                if (this.botInputFiles != null)
                    player.setInputFile(this.botInputFiles[i]);
                this.players.add(player);
            }
        } else if (command.equals("bot_emails")) {
            String[] emails = split[1].split(",");
            for (int i = 0; i < emails.length; i++) {
                this.players.get(i).setEmail(emails[i]);
            }
        }
    }
}
