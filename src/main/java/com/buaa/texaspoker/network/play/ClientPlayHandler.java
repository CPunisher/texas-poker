package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.client.gui.BettingDialog;
import com.buaa.texaspoker.client.gui.IMessagePanel;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.util.ConsoleUtil;
import com.buaa.texaspoker.util.message.TextMessage;

import java.util.Iterator;
import java.util.List;

public class ClientPlayHandler implements IClientPlayHandler {

    private IMessagePanel messagePanel;
    private NetworkManager networkManager;
    private GameClient client;

    public ClientPlayHandler(NetworkManager networkManager, GameClient client) {
        this.networkManager = networkManager;
        this.client = client;
        this.messagePanel = client.getGui().getMessagePanel();
    }

    @Override
    public void processPlayerJoin(SPacketPlayerJoin packet) {
        List<Player> clientList = client.getRoom().getPlayerList();
        List<SPacketPlayerJoin.PlayerJoinData> serverList = packet.getData();

        boolean newPlayer = clientList.isEmpty();
        Iterator<SPacketPlayerJoin.PlayerJoinData> iterator1 = serverList.iterator();
        while (iterator1.hasNext()) {
            SPacketPlayerJoin.PlayerJoinData data = iterator1.next();
            if (!clientList.stream().anyMatch(player -> player.getUuid().equals(data.getUuid()))) {
                Player player = new ClientPlayer(data.getUuid(), data.getName(), client);
                player.setMoney(data.getMoney());
                clientList.add(player);
                if (!newPlayer || data.getUuid().equals(client.getPlayer().getUuid())) {
                    messagePanel.printMessage(new TextMessage("%s join the game", data.getName()));
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
            if (serverList.stream().noneMatch(profile -> profile.getUuid().equals(player.getUuid()))) {
                messagePanel.printMessage(new TextMessage("%s leaves the game", player.getName()));
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
        messagePanel.printMessage(new TextMessage("You get card: "));
        messagePanel.printMessage(new TextMessage(packet.getPokers()));
        this.client.getRoom().getPublicPokers().clear();
        this.client.getPlayer().getData().setPokers(packet.getPokers());
        client.getGui().loadPokers();
    }

    @Override
    public void processRequestBetting(SPacketRequestBetting packet) {
        Player player = client.getRoom().getPlayerByUuid(packet.getPlayerUuid());
        player.setMoney(packet.getPlayerMoney());
        player.getData().setBetting(true);
        if (packet.getPlayerUuid().equals(client.getPlayer().getUuid())) {
            boolean canCheck = packet.getSectionBetting() >= packet.getMinimum();
            messagePanel.printMessage(new TextMessage("Your section betting: %d, Section Bonus: %d", packet.getSectionBetting(), packet.getSectionBonus()));
            messagePanel.printMessage(new TextMessage("How much do you want to bet [min: %d] [check with -1: %s]: ", packet.getMinimum(), canCheck));
            new Thread(() -> {
                int minimum = packet.getMinimum() == 0 ? 1 : packet.getMinimum();
                // TODO input validate
//                int amount = ConsoleUtil.nextInt();
//                int inc = amount - packet.getSectionBetting();
//                while (amount == packet.getSectionBetting() ||
//                        (!canCheck && amount <= 0) ||
//                        (amount > 0 && amount % minimum != 0) ||
//                        inc > player.getMoney()) {
//                    messagePanel.printMessage(new TextMessage("Invalid betting"));
//                    amount = ConsoleUtil.nextInt();
//                    inc = amount - packet.getSectionBetting();
//                }
                BettingDialog dialog = new BettingDialog(client.getGui(), packet.getSectionBetting(), packet.getSectionBonus(), minimum, packet.getPlayerMoney(), canCheck);
                dialog.setVisible(true);
                dialog.dispose();
                this.networkManager.sendPacket(new CPacketRespondBetting(this.client.getPlayer().generateProfile(), dialog.getValue()));
            }).start();
        } else {
            messagePanel.printMessage(new TextMessage("Waiting for %s's betting [min: %d]", player.getName(), packet.getMinimum()));
        }
    }

    @Override
    public void processRespondBetting(SPacketRespondBetting packet) {
        Player player = client.getRoom().getPlayerByUuid(packet.getProfile().getUuid());
        if (packet.getAmount() > 0) {
            messagePanel.printMessage(new TextMessage("%s Place a %d yuan bet", packet.getProfile().getName(), packet.getAmount()));
            player.setMoney(player.getMoney() + player.getData().getSection() - packet.getAmount());
            player.getData().setSection(packet.getAmount());
            client.getRoom().getPlayerList().forEach(player1 -> player1.getData().setChecked(false));
        } else {
            messagePanel.printMessage(new TextMessage("%s Check", packet.getProfile().getName()));
            player.getData().setChecked(true);
        }
        player.getData().setBetting(false);
    }

    @Override
    public void processShowPoker(SPacketShowPoker packet) {
        Poker newPoker = new Poker(packet.getPoint(), packet.getPokerType());
        messagePanel.printMessage(new TextMessage("Public poker is known: %s", newPoker));
        client.getRoom().getPublicPokers().add(newPoker);
        client.getRoom().setRoundBonus(packet.getRoundBonus());
        for (Player player : client.getRoom().getPlayerList()) {
            player.getData().setChecked(false);
            player.getData().setSection(0);
        }
        client.getGui().loadPokers();
    }

    @Override
    public void processPlayerOut(SPacketPlayerOut packet) {
        this.client.getRoom().getPlayerByUuid(packet.getProfile().getUuid()).setOut(true);
    }

    @Override
    public void processGameEnd(SPacketGameEnd packet) {
        // TODO Display players card
        Player winner = client.getRoom().getPlayerByUuid(packet.getWinner().getUuid());
        winner.setMoney(packet.getWinnerMoney());
        messagePanel.printMessage(new TextMessage("Round complete, winner: %s", packet.getWinner().getName()));
    }

    @Override
    public void onDisconnect() {
        messagePanel.printMessage(new TextMessage("You have disconnected from server"));
        this.networkManager.closeChannel();
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}
