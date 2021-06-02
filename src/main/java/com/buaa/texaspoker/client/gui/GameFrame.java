package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GameFrame extends JFrame implements IGameMainFrame, Runnable {

    private final int FPS = 30;
    private final int INTERVAL = 1000 / FPS;

    private final GameClient client;
    private Thread thread;
    private PlayerPanel playerPanel;
    private PokerPanel pokerPanel;
    private MessagePanel messagePanel;

    private Image background;

    public GameFrame(String title, GameClient client) throws HeadlessException {
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

    public IGamePanel getGamePanel() {
        return playerPanel;
    }

    public IMessagePanel getMessagePanel() {
        return messagePanel;
    }

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
