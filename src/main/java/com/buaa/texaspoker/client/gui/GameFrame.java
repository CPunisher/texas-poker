package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 游戏的主界面，包含玩家信息列表，扑克牌信息列表和消息列表。后台线程以固定的FPS对界面进行重绘
 * @author CPunisher
 */
public class GameFrame extends JFrame implements IGameMainFrame, Runnable {

    /**
     * 重绘固定的FPS
     */
    private final int FPS = 30;

    /**
     * 根据FPS计算出每次重绘的时间间隔
     */
    private final int INTERVAL = 1000 / FPS;

    /**
     * 附属的{@link GameClient}引用
     */
    private final GameClient client;

    /**
     * 控制重绘的线程
     */
    private Thread thread;

    /**
     * 玩家信息列表画布
     */
    private PlayerPanel playerPanel;

    /**
     * 扑克牌信息列表画布
     */
    private PokerPanel pokerPanel;

    /**
     * 消息列表面板
     */
    private MessagePanel messagePanel;

    /**
     * 背景图片资源对象
     */
    private Image background;

    /**
     * 构造{@link GameFrame}实例，加载背景图片，加载三个面板和画布
     * @param title 窗口标题
     * @param client 附属的{@link GameClient}引用
     */
    public GameFrame(String title, GameClient client) {
        super(title);
        this.client = client;
        this.playerPanel = new PlayerPanel(client);
        this.pokerPanel = new PokerPanel(client);
        this.messagePanel = new MessagePanel(client);
        this.background = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/image/background.png"))).getImage();

        BackgroundPanel panel = new BackgroundPanel(this.background);
        panel.setLayout(new BorderLayout(0, 0));
        panel.add(this.playerPanel, BorderLayout.WEST);
        panel.add(this.messagePanel, BorderLayout.EAST);
        panel.add(this.pokerPanel, BorderLayout.SOUTH);
        this.add(panel, BorderLayout.CENTER);
        this.setResizable(false);
        this.setUndecorated(false);
        this.pack();

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void run() {
        long start, elapsed, wait;
        while (true) {
            start = System.nanoTime();
            // game loop
            this.playerPanel.repaint();
            this.pokerPanel.repaint();

            // 计算两次重绘之间的时间间隔
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

    @Override
    public void addNotify() {
        super.addNotify();
        if (this.thread == null) {
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    /**
     * 调用{@link PokerPanel}进行重新加载扑克牌
     * @see {@link PokerPanel#loadPokers()}
     */
    public void loadPokers() {
        this.pokerPanel.loadPokers();
    }

    public IGamePanel getPlayerPanel() {
        return playerPanel;
    }

    public IGamePanel getPokerPanel() { return pokerPanel; }

    /**
     * 获得消息面板，以便其他类进行消息的添加
     * @return 消息面板
     */
    public IMessagePanel getMessagePanel() {
        return messagePanel;
    }

    /**
     * 装载背景图片的最底层面板
     * @author CPunisher
     */
    private static class BackgroundPanel extends JPanel {
        private Image background;

        public BackgroundPanel(Image background) {
            this.background = background;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, this);
        }
    }
}
