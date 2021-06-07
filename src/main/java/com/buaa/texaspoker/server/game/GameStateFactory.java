package com.buaa.texaspoker.server.game;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * 享元模式静态工厂类
 * 批量确保各个游戏状态是单例的，防止资源浪费
 * @author CPunisher
 * @see IGameState
 */
public class GameStateFactory {

    /**
     * 将游戏状态类映射为唯一的游戏状态对象，实现享元模式
     */
    private static final HashMap<Class<? extends IGameState>, IGameState> stateMap = new HashMap<>();

    /**
     * 工厂方法
     * @param clazz 需要生成对象的游戏状态类
     * @param controller 游戏流程控制对象
     * @return 指定的游戏状态类对象
     */
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
