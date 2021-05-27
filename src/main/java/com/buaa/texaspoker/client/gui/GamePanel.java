package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    private final GameClient client;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 720;

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

    private void draw() {
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
    }

    private void drawToScreen() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), null);
        g2.dispose();
    }
}
