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
    protected TicTacToePlayer createPlayer(int id) {
        return new TicTacToePlayer(id);
    }

    @Override
    protected Configuration getDefaultConfiguration() {
        return new Configuration();
    }

    @Override
    protected TicTacToeProcessor createProcessor() {
        return new TicTacToeProcessor(this.playerProvider);
    }

    @Override
    protected GameLoopInterface createGameLoop() {
        return new TurnBasedGameLoop();
    }

    @Override
    protected void sendSettingsToPlayer(TicTacToePlayer player) {
        player.sendSetting("your_botid", player.getId());
    }

    @Override
    protected String getPlayedGame(TicTacToeState initialState) {
        TicTacToeSerializer serializer = new TicTacToeSerializer();
        return serializer.traverseToString(this.processor, initialState);
    }

    @Override
    protected TicTacToeState getInitialState() {
        TicTacToeBoard board = new TicTacToeBoard(9, 9);

        ArrayList<TicTacToePlayerState> playerStates = new ArrayList<>();

        for (TicTacToePlayer player : this.playerProvider.getPlayers()) {
            TicTacToePlayerState playerState = new TicTacToePlayerState(player.getId());
            playerStates.add(playerState);
        }
        TicTacToeState state = new TicTacToeState(playerStates, board);

        return state.createNextState(0);
    }
}
