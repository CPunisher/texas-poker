package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;
import com.buaa.texaspoker.entity.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, IGamePanel {

    private final GameClient client;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 720;

    private static final int PLAYER_WIDTH = 160;
    private static final int PLAYER_HEIGHT = 200;

    private static final Poker UNKNOWN = new Poker(0, PokerType.UNKNOWN);

    private Thread thread;
    private boolean running;
    private final int FPS = 30;
    private final int INTERVAL = 1000 / FPS;

    private BufferedImage bufferedImage;
    private Graphics2D g;

    public GamePanel(GameClient client) {
        this.client = client;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.requestFocus();
    }

    private void drawPlayerInfo(Player player, int x, int y, int width, int height) {
        int radius = 40, gap = 12, extend = 10;
        int centerX = x + width / 2 - radius / 2;
        int ovalY = y + height / 6 - radius / 2;
        int th1 = ovalY + radius + gap, th2 = (int) (ovalY + radius + (radius + extend) * 0.5 * Math.sqrt(3) + gap);
        g.setColor(player.getData().isBetting() ? Color.RED : Color.BLACK);
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
        }
    }

    private void drawPoker(Poker poker, int x, int y, int width, int height) {
        int point = poker.getPoint();
        PokerType pokerType = poker.getPokerType();
        g.setColor(pokerType == PokerType.HEART || pokerType == PokerType.DIAMOND ? Color.RED : Color.BLACK);
        g.setStroke(new BasicStroke(3.0f));
        g.drawRoundRect(x, y, width, height, 5, 5);
        g.setFont(g.getFont().deriveFont(30.0f));
        g.drawString(pokerType.getSymbol(), x + 15, y + 35);
        if (point > 0) {
            g.drawString(String.valueOf(point != 14 ? point : "A"), x + width / 2, y + height / 2);
        }
    }

    public void draw() {
        // background
        g.setColor(new Color(238, 238, 238));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.ORANGE);
        g.setFont(g.getFont().deriveFont(14.0f));
        g.drawString("本轮奖池: " + client.getRoom().getRoundBonus(), 10, HEIGHT - 30);

        // players
        int gutter = 20;
        int column = (this.getWidth() - gutter) / (PLAYER_WIDTH + gutter);
        int margin = (this.getWidth() - column * PLAYER_WIDTH - (column - 1) * gutter) / 2;
        java.util.List<Player> playerList = client.getRoom().getPlayerList();
        for (int i = 0, j = 0; i * column + j < playerList.size();) {
            int px = gutter * j + j * PLAYER_WIDTH + margin;
            int py = gutter * (i + 1) + i * PLAYER_HEIGHT;
            drawPlayerInfo(playerList.get(i * column + j), px, py, PLAYER_WIDTH, PLAYER_HEIGHT);
            if (++j >= column) {
                i++;
                j = 0;
            }
        }

        // public pokers
        java.util.List<Poker> pokerList = client.getRoom().getPublicPokers();
        for (int i = 0; i < 5; i++) {
            int cx = gutter * i + i * PLAYER_WIDTH + margin;
            drawPoker(i < pokerList.size() ? pokerList.get(i) : UNKNOWN, cx, 3 * gutter + 2 * PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (this.thread == null) {
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    @Override
    public void run() {
        this.init();
        long start, elapsed, wait;
        while (this.running) {
            start = System.nanoTime();
            // game loop
            draw();
            drawToScreen();

            elapsed = System.nanoTime() - start;
            wait = INTERVAL - elapsed / 1000000;
            if (wait < 0) wait = INTERVAL;

            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        this.running = true;
        this.bufferedImage = new BufferedImage(WIDTH, HEIGHT, 1);
        this.g = (Graphics2D) bufferedImage.getGraphics();
        this.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void drawToScreen() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), null);
        g2.dispose();
    }
}
