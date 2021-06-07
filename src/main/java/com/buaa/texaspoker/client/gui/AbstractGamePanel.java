package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;

import javax.swing.*;
import java.awt.*;

/**
 * <code>AbstractGamePanel</code>是绘制游戏内容的抽象类画布类
 * 提供了默认的透明背景和可聚焦设置
 * 并且针对<code>Graphics2D</code>进行统一的抗锯齿和字体美化
 * @author CPunisher
 */
public abstract class AbstractGamePanel extends JPanel implements IGamePanel {

    /**
     * 客户端实例
     * @see GameClient
     */
    protected final GameClient client;

    /**
     * 画布的宽度
     */
    private int width;

    /**
     * 画布的高度
     */
    private int height;

    /**
     * 生成绑定游戏客户端的画布实体，并进行默认设置
     * @param client 游戏客户端实例
     * @param width 画布宽度
     * @param height 画布高度
     */
    public AbstractGamePanel(GameClient client, int width, int height) {
        this.client = client;
        this.width = width;
        this.height = height;
        this.setFocusable(true);
        this.setOpaque(false);
    }

    /**
     * 在父类的基础上进行抗锯齿和字体优化，并绘制自定义内容
     * @param g <code>Graphics</code>对象
     * @see #draw
     */
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

    /**
     * @return 画布的区域
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.width, this.height);
    }

    /**
     * @return 画布宽度
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @return 画布高度
     */
    @Override
    public int getHeight() {
        return height;
    }
}
