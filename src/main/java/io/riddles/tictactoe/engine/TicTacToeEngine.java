package io.riddles.tictactoe.engine;

import io.riddles.javainterface.configuration.Configuration;
import io.riddles.javainterface.engine.GameLoopInterface;
import io.riddles.javainterface.engine.TurnBasedGameLoop;
import io.riddles.javainterface.exception.TerminalException;
import io.riddles.javainterface.game.player.PlayerProvider;
import io.riddles.javainterface.io.IOHandler;
import io.riddles.tictactoe.game.TicTacToeSerializer;
import io.riddles.tictactoe.game.data.TicTacToeBoard;
import io.riddles.tictactoe.game.player.TicTacToePlayer;
import io.riddles.tictactoe.game.processor.TicTacToeProcessor;
import io.riddles.tictactoe.game.state.TicTacToePlayerState;
import io.riddles.tictactoe.game.state.TicTacToeState;
import io.riddles.javainterface.engine.AbstractEngine;

import java.util.ArrayList;

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

    public TicTacToeEngine(PlayerProvider<TicTacToePlayer> playerProvider, IOHandler ioHandler) throws TerminalException {
        super(playerProvider, ioHandler);
    }

    @Override
    protected Configuration getDefaultConfiguration() {
        Configuration cc = new Configuration();
        cc.put("maxRounds", 81);
        cc.put("fieldWidth", 9);
        cc.put("fieldHeight", 9);
        return cc;
    }

    @Override
    protected TicTacToePlayer createPlayer(int id) {
        TicTacToePlayer player = new TicTacToePlayer(id);
        return player;
    }

    @Override
    protected TicTacToeProcessor createProcessor() {

        return new TicTacToeProcessor(playerProvider);
    }

    @Override
    protected GameLoopInterface createGameLoop() {
        return new TurnBasedGameLoop();
    }


    @Override
    protected void sendSettingsToPlayer(TicTacToePlayer player) {
        player.sendSetting("your_botid", player.getId());
        //player.sendSetting("field_width", configuration.getInt("fieldWidth"));
        //player.sendSetting("field_height", configuration.getInt("fieldHeight"));
        player.sendSetting("max_rounds", configuration.getInt("maxRounds"));
    }

    @Override
    protected String getPlayedGame(TicTacToeState initialState) {
        TicTacToeSerializer serializer = new TicTacToeSerializer();
        return serializer.traverseToString(this.processor, initialState);
    }

    @Override
    protected TicTacToeState getInitialState() {

        int fieldWidth = configuration.getInt("fieldWidth");
        int fieldHeight = configuration.getInt("fieldHeight");

        TicTacToeBoard board = new TicTacToeBoard(fieldWidth, fieldHeight);

        ArrayList<TicTacToePlayerState> playerStates = new ArrayList<>();

        for (TicTacToePlayer player : this.playerProvider.getPlayers()) {
            TicTacToePlayerState playerState = new TicTacToePlayerState(player.getId());
            playerStates.add(playerState);
        }
        TicTacToeState s = new TicTacToeState(null, playerStates, 0);

        s.setBoard(board);
        s.setFieldPresentationString("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
        s.setPossibleMovesPresentationString("4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4");

        return s;
    }
}
