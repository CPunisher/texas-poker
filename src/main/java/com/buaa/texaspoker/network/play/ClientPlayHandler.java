package com.buaa.texaspoker.network.play;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.client.gui.BettingDialog;
import com.buaa.texaspoker.client.gui.IMessagePanel;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.entity.player.PlayerProfile;
import com.buaa.texaspoker.network.NetworkManager;
import com.buaa.texaspoker.util.message.TextMessage;
import com.buaa.texaspoker.util.message.TranslateMessage;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
        for (SPacketPlayerJoin.PlayerJoinData data : serverList) {
            if (clientList.stream().noneMatch(player -> player.getUuid().equals(data.getUuid()))) {
                Player player = new ClientPlayer(data.getUuid(), data.getName(), client);
                player.setMoney(data.getMoney());
                clientList.add(player);
                if (!newPlayer || data.getUuid().equals(client.getPlayer().getUuid())) {
                    messagePanel.printMessage(new TranslateMessage("message.client_player.join", data.getName()));
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
                messagePanel.printMessage(new TranslateMessage("message.client_player.leave", player.getName()));
                iterator.remove();
            }
        }
    }

    @Override
    public void processPlayerDisconnect(SPacketPlayerDisconnect packet) {
        this.onDisconnect();
    }

    @Override
    public void processRemake(SPacketRemake packet) {
        messagePanel.printMessage(new TranslateMessage("message.client_player.remake"));
        this.client.getRoom().getPlayerList().forEach(player -> {
            player.setMoney(packet.getInitMoney());
            player.setOut(false);
            player.clearData();
        });
    }

    @Override
    public void processPlayerDraw(SPacketPlayerDraw packet) {
        messagePanel.printMessage(new TranslateMessage("message.client_player.get_card"));
        messagePanel.printMessage(new TextMessage(packet.getPokers()));
        this.client.getRoom().getPublicPokers().clear();
        this.client.getPlayer().getData().setPokers(packet.getPokers());
        this.client.getRoom().getPlayerList().forEach(Player::clearData);
        client.getGui().loadPokers();
    }

    @Override
    public void processRequestBetting(SPacketRequestBetting packet) {
        Player player = client.getRoom().getPlayerByUuid(packet.getPlayerUuid());
        player.setMoney(packet.getPlayerMoney());
        player.getData().setBetting(true);
        if (packet.getPlayerUuid().equals(client.getPlayer().getUuid())) {
            boolean canCheck = packet.getSectionBetting() >= packet.getMinimum();
            messagePanel.printMessage(new TranslateMessage("message.client_player.bet_info", packet.getSectionBetting(), packet.getSectionBonus()));
            messagePanel.printMessage(new TranslateMessage("message.client_player.check_info", packet.getMinimum(), canCheck));

            BettingDialog dialog = new BettingDialog(client.getGui(), packet, canCheck);
            dialog.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    networkManager.sendPacket(new CPacketRespondBetting(client.getPlayer().generateProfile(), dialog.getValue()));
                    dialog.dispose();
                }
            });
            dialog.setVisible(true);

        } else {
            messagePanel.printMessage(new TranslateMessage("message.client_player.wait", player.getName(), packet.getMinimum()));
        }
    }

    @Override
    public void processRespondBetting(SPacketRespondBetting packet) {
        Player player = client.getRoom().getPlayerByUuid(packet.getProfile().getUuid());
        if (packet.getAmount() > 0) {
            messagePanel.printMessage(new TranslateMessage("message.client_player.player_bet", packet.getProfile().getName(), packet.getAmount()));
            player.setMoney(player.getMoney() + player.getData().getSection() - packet.getAmount());
            player.getData().setSection(packet.getAmount());
            client.getRoom().getPlayerList().forEach(player1 -> player1.getData().setChecked(false));
        } else if (packet.getAmount() == -1) {
            messagePanel.printMessage(new TranslateMessage("message.client_player.player_check", packet.getProfile().getName()));
            player.getData().setChecked(true);
        } else if (packet.getAmount() == -2) {
            messagePanel.printMessage(new TranslateMessage("message.client_player.player_give_up", packet.getProfile().getName()));
            player.getData().setGiveUp(true);
        }
        player.getData().setBetting(false);
    }

    @Override
    public void processShowPoker(SPacketShowPoker packet) {
        Poker newPoker = new Poker(packet.getPoint(), packet.getPokerType());
        messagePanel.printMessage(new TranslateMessage("message.client_player.show_poker", newPoker));
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
        winner.getData().setWinner(true);
        winner.getData().setPokers(packet.getPokers());
        messagePanel.printMessage(new TranslateMessage("message.client_player.winner", packet.getWinner().getName()));
    }

    @Override
    public void onDisconnect() {
        messagePanel.printMessage(new TranslateMessage("message.client_player.disconnect"));
        this.networkManager.closeChannel();
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}
