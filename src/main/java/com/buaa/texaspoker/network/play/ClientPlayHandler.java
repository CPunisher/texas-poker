package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.util.ConsoleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    }

    @Override
    public void processPlayerDisconnect(SPacketPlayerDisconnect packet) {
        this.onDisconnect();
    }

    @Override
    public void processPlayerDraw(SPacketPlayerDraw packet) {
        logger.info("You get card: ");
        logger.info(packet.getPokers());
        this.client.getPlayer().getData().setPokers(packet.getPokers());
    }

    @Override
    public void processRequestBetting(SPacketRequestBetting packet) {
        if (packet.getPlayerUuid().equals(client.getPlayer().getUuid())) {
            boolean canCheck = packet.getSectionBetting() >= packet.getMinimum();
            logger.info("Your section betting: {}, Section Bonus: {}", packet.getSectionBetting(), packet.getSectionBonus());
            logger.info(String.format("How much do you want to bet [min: %d] [check with -1: %s]: ",
                    packet.getMinimum(), canCheck));
            new Thread(() -> {
                int minimum = packet.getMinimum() == 0 ? 1 : packet.getMinimum();
                // TODO input validate
                int amount = ConsoleUtil.nextInt();
                while (amount == packet.getSectionBetting() ||
                        (!canCheck && amount <= 0) ||
                        (amount > 0 && amount % minimum != 0)) {
                    logger.info("Invalid betting");
                    amount = ConsoleUtil.nextInt();
                }
                this.networkManager.sendPacket(new CPacketRespondBetting(this.client.getPlayer().generateProfile(), amount));
            }).start();
        } else {
            Player player = client.getRoom().getPlayerByUuid(packet.getPlayerUuid());
            logger.info("Waiting for {}'s betting [min: {}]", player.getName(), packet.getMinimum());
        }
    }

    @Override
    public void processRespondBetting(SPacketRespondBetting packet) {
        if (packet.getAmount() > 0) {
            logger.info("{} Place a {} yuan bet", packet.getProfile().getName(), packet.getAmount());
        } else {
            logger.info("{} Check", packet.getProfile().getName());
        }
    }

    @Override
    public void processShowPoker(SPacketShowPoker packet) {
        logger.info("Public poker is known: {}", new Poker(packet.getPoint(), packet.getPokerType()));
    }

    @Override
    public void processGameEnd(SPacketGameEnd packet) {
        logger.info("Round complete, winner: {}, your money: {}", packet.getWinner().getName(), packet.getPlayerMoney());
    }

    @Override
    public void onDisconnect() {
        logger.info("You have disconnected from server");
        this.networkManager.closeChannel();
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}
