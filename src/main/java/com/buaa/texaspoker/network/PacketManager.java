package com.buaa.texaspoker.network;

import com.buaa.texaspoker.network.login.CPacketConnect;
import com.buaa.texaspoker.network.login.SPacketPlayerCreate;
import com.buaa.texaspoker.network.play.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 管理所有的{@link IPacket}，
 * 预先在这个类中注册，并为每一类{@link IPacket}分发唯一的<code>id</code>，
 * 在将{@link IPacket}数据序列化为字节数据时，通过本类获取<code>id</code>作为标识写入字节缓冲
 * 在将字节数据反序列化为{@link IPacket}对象时，通过本类查找读取的<code>id</code>标识所对应的{@link IPacket}类
 * @author CPunisher
 * @see IPacket
 */
public class PacketManager {

    /**
     * 从<code>id</code>到{@link IPacket}类的映射
     */
    private static Map<Integer, Class<? extends IPacket>> idToPacket = new HashMap<>();

    /**
     * 从{@link IPacket}类到<code>id</code>的映射
     */
    private static Map<Class<? extends IPacket>, Integer> packetToId = new HashMap<>();

    /**
     * 自增长<code>id</code>
     */
    private static int nextId = 0;

    /**
     * 初始化注册所有{@link IPacket}
     */
    public static void init() {
        register(CPacketConnect.class);
        register(SPacketPlayerCreate.class);
        register(SPacketPlayerJoin.class);
        register(SPacketPlayerLeave.class);
        register(SPacketPlayerDisconnect.class);
        register(SPacketPlayerDraw.class);
        register(SPacketRequestBetting.class);
        register(CPacketRespondBetting.class);
        register(SPacketShowPoker.class);
        register(SPacketGameEnd.class);
        register(SPacketRespondBetting.class);
        register(SPacketRemake.class);
    }

    /**
     * 为{@link IPacket}的类分配<code>id</code>，并进行双向映射记录
     * @param clazz {@link IPacket}的类
     */
    public static void register(Class<? extends IPacket> clazz) {
        idToPacket.put(nextId, clazz);
        packetToId.put(clazz, nextId);
        nextId++;
    }

    /**
     * 通过{@link IPacket}类查找他的<code>id</code>，用于序列化
     * @param clazz 需要查找的{@link IPacket}的类
     * @return 数据包的标识
     */
    public static Optional<Integer> getPacketId(Class<? extends IPacket> clazz) {
        return Optional.ofNullable(packetToId.get(clazz));
    }

    /**
     * 通过<code>id</code>查找{@link IPacket}的类，用于反序列化
     * @param id 数据包的标识
     * @return 标识所对应的数据包的类
     */
    public static Optional<Class<? extends IPacket>> getPacketClassById(int id) {
        return Optional.ofNullable(idToPacket.get(id));
    }

    /**
     * 通过给定数据包的<code>id</code>，利用反射构造{@link IPacket}对象
     * @param id 数据包的<code>id</code>
     * @return <code>id</code>所对应的数据包的对象
     */
    public static Optional<IPacket> newPacket(int id) {
        return getPacketClassById(id).map(clazz -> {
            try {
                return clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }).map(constructor -> {
            try {
                return constructor.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
