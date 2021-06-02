package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractGamePanel extends JPanel implements IGamePanel {

    protected final GameClient client;
    private int width;
    private int height;

    public AbstractGamePanel(GameClient client, int width, int height) {
        this.client = client;
        this.width = width;
        this.height = height;
        this.setFocusable(true);
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(new Font("microsoft yahei", Font.PLAIN, 14));
        this.draw(g2d);
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.width, this.height);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
