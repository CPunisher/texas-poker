package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PokerPanel extends AbstractGamePanel {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 250;
    private static final Poker UNKNOWN = new Poker(0, PokerType.UNKNOWN);
    private Image pokerImage;

    public PokerPanel(GameClient client) {
        super(client, WIDTH, HEIGHT);
        this.pokerImage = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/image/poker.png"))).getImage();
    }

    private void drawPoker(Graphics2D g, Poker poker, int x, int y, int width, int height) {
        int point = poker.getPoint();
        PokerType pokerType = poker.getPokerType();
        int dx = x, dy = y;
        int sx, sy = 0;
        if (pokerType == PokerType.UNKNOWN) {
            sx = 300 * 2;
            sy = 435 * 4;
        }
        else {
            sx = (point - 1) % 13 * 300;
            switch (pokerType) {
                case ACE -> sy = 435 * 3;
                case HEART -> sy = 435 * 2;
                case CLUB -> sy = 0;
                case DIAMOND -> sy = 435;
            }
        }
        g.drawImage(pokerImage, dx, dy, dx + width, dy + height, sx, sy, sx + 300, sy + 435, null);
    }

    @Override
    public void draw(Graphics2D g) {
        int gutter = 20;
        int playerWidth = Constant.PLAYER_WIDTH, playerHeight = Constant.PLAYER_HEIGHT;
        int column = (this.getWidth() - gutter) / (playerWidth + gutter);
        int margin = (this.getWidth() - column * playerWidth - (column - 1) * gutter) / 2;

        // public pokers
        java.util.List<Poker> pokerList = client.getRoom().getPublicPokers();
        for (int i = 0; i < 5; i++) {
            int cx = gutter * i + i * playerWidth + margin;
            drawPoker(g, i < pokerList.size() ? pokerList.get(i) : UNKNOWN, cx, gutter, playerWidth, playerHeight);
        }
    }
}
