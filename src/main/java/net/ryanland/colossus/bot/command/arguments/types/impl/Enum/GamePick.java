package net.ryanland.colossus.bot.command.arguments.types.impl.Enum;

import net.ryanland.colossus.sys.game.Game;
import net.ryanland.colossus.sys.game.GuessTheNumberGame;

import java.lang.reflect.InvocationTargetException;

public enum GamePick implements EnumArgument.InputEnum {

    GUESS_THE_NUMBER(GuessTheNumberGame.class)
    ;

    private final Class<? extends Game> game;

    GamePick(Class<? extends Game> game) {
        this.game = game;
    }

    public Game getGame() {
        try {
            return game.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @Override
    public String getTitle() {
        return getGame().getName();
    }
}
