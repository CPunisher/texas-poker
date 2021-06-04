package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.util.Constant;

import java.awt.*;

public class PlayerPanel extends AbstractGamePanel {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 470;

    public PlayerPanel(GameClient client) {
        super(client, WIDTH, HEIGHT);
    }

    private void drawPlayerInfo(Graphics2D g, Player player, int x, int y, int width, int height) {
        int radius = 40, gap = 12, extend = 10;
        int centerX = x + width / 2 - radius / 2;
        int ovalY = y + height / 6 - radius / 2;
        int th1 = ovalY + radius + gap, th2 = (int) (ovalY + radius + (radius + extend) * 0.5 * Math.sqrt(3) + gap);
        g.setColor(player.getData().isBetting() ? Color.RED : Color.ORANGE);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(x, y, width, height, 5, 5);
        g.fillOval(centerX, ovalY, radius, radius);
        g.fillPolygon(new int[] {x + width / 2 - radius / 2 - extend, x + width / 2 + radius / 2 + extend, x + width / 2}, new int [] {th1, th1, th2}, 3);
        g.setFont(g.getFont().deriveFont(14.0f));
        g.drawString("昵称: " + player.getName(), x + 10, th2 + 30);
        g.drawString("持币: " + player.getMoney(), x + 10, th2 + 50);
        g.drawString("下注: " + player.getData().getSection(), x + 10, th2 + 70);
        if (player.getData().isChecked()) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("Check", x + 10, th2 + 90);
        } else if (player.isOut()) {
            g.setColor(Color.RED);
            g.drawString("Out", x + 10, th2 + 90);
        } else if (player.getData().isGiveUp()) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("Give up", x + 10, th2 + 90);
        }

        int overlap = 30;
        if (player.getData().isWinner()) {
            for (int i = 0; i < 2; i++) {
                Poker poker = player.getData().getPokers().get(i);
                HiddenPokerPanel.drawPoker(g, poker, x + i * overlap, y + height / 4, width, height);
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.setFont(g.getFont().deriveFont(14.0f));
        g.drawString("本轮奖池: " + client.getRoom().getRoundBonus(), 10, HEIGHT - 5);
        int playerWidth = Constant.PLAYER_WIDTH, playerHeight = Constant.PLAYER_HEIGHT;

        // players
        int gutter = 20;
        int column = (this.getWidth() - gutter) / (playerWidth + gutter);
        int margin = (this.getWidth() - column * playerWidth - (column - 1) * gutter) / 2;
        java.util.List<Player> playerList = client.getRoom().getPlayerList();
        for (int i = 0, j = 0; i * column + j < playerList.size();) {
            int px = gutter * j + j * playerWidth + margin;
            int py = gutter * (i + 1) + i * playerHeight;
            drawPlayerInfo(g, playerList.get(i * column + j), px, py, playerWidth, playerHeight);
            if (++j >= column) {
                i++;
                j = 0;
            }
        }
    }
}
