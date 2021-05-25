package com.buaa.texaspoker.network;

import com.buaa.texaspoker.network.login.CPacketConnect;
import com.buaa.texaspoker.network.login.SPacketPlayerCreate;
import com.buaa.texaspoker.network.play.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PacketManager {

    private static Map<Integer, Class<? extends IPacket>> idToPacket = new HashMap<>();
    private static Map<Class<? extends IPacket>, Integer> packetToId = new HashMap<>();
    private static int nextId = 0;

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
    }

    public static void register(Class<? extends IPacket> clazz) {
        idToPacket.put(nextId, clazz);
        packetToId.put(clazz, nextId);
        nextId++;
    }

    public static Optional<Integer> getPacketId(Class<? extends IPacket> clazz) {
        return Optional.ofNullable(packetToId.get(clazz));
    }

    public static Optional<Class<? extends IPacket>> getPacketClassById(int id) {
        return Optional.ofNullable(idToPacket.get(id));
    }

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
