package io.riddles.tictactoe.engine;

import io.riddles.javainterface.configuration.CheckedConfiguration;
import io.riddles.javainterface.configuration.Configuration;
import io.riddles.javainterface.engine.GameLoop;
import io.riddles.javainterface.engine.TurnBasedGameLoop;
import io.riddles.javainterface.exception.TerminalException;
import io.riddles.javainterface.game.player.PlayerProvider;
import io.riddles.javainterface.io.BotIO;
import io.riddles.javainterface.io.IO;
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

    public TicTacToeEngine(PlayerProvider<TicTacToePlayer> playerProvider, IO ioHandler) throws TerminalException {
        super(playerProvider, ioHandler);
    }

    @Override
    protected CheckedConfiguration getConfiguration() {
        CheckedConfiguration cc = new CheckedConfiguration();
        cc.addRequiredKey("maxRounds");
        cc.addRequiredKey("fieldWidth");
        cc.addRequiredKey("fieldHeight");

        cc.put("maxRounds", 10);
        cc.put("fieldWidth", 9);
        cc.put("fieldHeight", 9);

        return cc;
    }

    @Override
    protected TicTacToePlayer createPlayer(int id, BotIO ioHandler) {
        TicTacToePlayer player = new TicTacToePlayer(id);
        player.setIoHandler(ioHandler);
        return player;
    }

    @Override
    protected TicTacToeProcessor createProcessor() {

        return new TicTacToeProcessor();
    }

    @Override
    protected GameLoop createGameLoop() {

        return new TurnBasedGameLoop(this.playerProvider);
    }

    @Override
    protected void sendSettingsToPlayer(TicTacToePlayer player, Configuration configuration) {
        player.sendSetting("your_botid", player.getId());
        player.sendSetting("field_width", configuration.getInt("fieldWidth"));
        player.sendSetting("field_height", configuration.getInt("fieldHeight"));
        player.sendSetting("max_rounds", configuration.getInt("maxRounds"));
    }

    @Override
    protected String getPlayedGame(TicTacToeState initialState) {
        TicTacToeSerializer serializer = new TicTacToeSerializer(this.playerProvider);
        return serializer.traverseToString(this.processor, initialState);
    }

    @Override
    protected TicTacToeState getInitialState(Configuration configuration) {
        TicTacToeState s = new TicTacToeState();

        int fieldWidth = configuration.getInt("fieldWidth");
        int fieldHeight = configuration.getInt("fieldHeight");

        TicTacToeBoard board = new TicTacToeBoard(fieldWidth, fieldHeight);

        ArrayList<TicTacToePlayerState> playerStates = new ArrayList<>();

        for (TicTacToePlayer player : this.playerProvider.getPlayers()) {
            TicTacToePlayerState playerState = new TicTacToePlayerState();
            playerState.setPlayerId(player.getId());

            playerStates.add(playerState);
        }
        s.setPlayerstates(playerStates);

        s.setBoard(board);
        s.setRoundNumber(-1);

        TicTacToeState s2 = s.createNextState(0);
        s2.setBoard(board.clone());
        s2.setPlayerstates(playerStates);



        return s2;
    }
}
