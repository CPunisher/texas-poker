package com.buaa.texaspoker.server.game;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * 享元模式静态工厂类
 */
public class GameStateFactory {

    private static final HashMap<Class<? extends IGameState>, IGameState> stateMap = new HashMap<>();

    public static IGameState getState(Class<? extends IGameState> clazz, GameController controller) {
        IGameState state = stateMap.get(clazz);
        if (state == null) {
            try {
                state = clazz.getDeclaredConstructor(GameController.class).newInstance(controller);
                stateMap.put(clazz, state);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return state;
    }
}
