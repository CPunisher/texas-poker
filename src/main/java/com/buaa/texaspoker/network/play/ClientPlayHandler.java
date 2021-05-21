package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.NetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ClientPlayHandler implements IClientPlayHandler {

    private static final Logger logger = LogManager.getLogger();
    private NetworkManager networkManager;
    private GameClient client;

    public ClientPlayHandler(NetworkManager networkManager, GameClient client) {
        this.networkManager = networkManager;
        this.client = client;
    }

    @Override
    public void processPlayerJoin(SPacketPlayerJoin packet) {
        List<Player> clientList = client.getRoom().getPlayerList();
        List<PlayerProfile> serverList = packet.getProfiles();
        Iterator<Player> iterator = clientList.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (!serverList.stream().anyMatch(profile -> profile.getUuid().equals(player.getUuid()))) {
                logger.info("{} leaves the game", player.getName());
                iterator.remove();
            }
        }

        boolean newPlayer = clientList.isEmpty();
        Iterator<PlayerProfile> iterator1 = serverList.iterator();
        while (iterator1.hasNext()) {
            PlayerProfile profile = iterator1.next();
            if (!clientList.stream().anyMatch(player -> player.getUuid().equals(profile.getUuid()))) {
                clientList.add(new ClientPlayer(profile.getUuid(), profile.getName(), client));
                if (!newPlayer || profile.getUuid().equals(client.getPlayer().getUuid())) {
                    logger.info("{}({}) join the game", profile.getName(), profile.getUuid());
                }
            }
        }
    }

    @Override
    public void processPlayerLeave(SPacketPlayerLeave packet) {

    }

    @Override
    public void processPlayerDisconnect(SPacketPlayerDisconnect packet) {
        logger.info("You have disconnected from server");
        this.networkManager.closeChannel();
    }

    @Override
    public void processPlayerDraw(SPacketPlayerDraw packet) {
        logger.info("You get card: ");
        logger.info(Arrays.toString(packet.getPokers()));
    }

    @Override
    public void processRequestBetting(SPacketRequestBetting packet) {
        if (packet.getPlayerUuid().equals(client.getPlayer().getUuid())) {
            logger.info(String.format("How much do you want to bet [min: %d]: ", packet.getMinimum()));
        } else {
            logger.info("");
        }
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}
