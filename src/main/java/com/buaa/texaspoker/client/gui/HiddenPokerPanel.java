package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.util.Constant;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

/**
 * 可隐藏的扑克牌画布
 * 绘制扑克牌，并且可以根据需要修改显示或者隐藏扑克牌的状态
 * @author CPunisher
 */
public class HiddenPokerPanel extends JPanel implements MouseListener {
    /**
     * 未知扑克牌对象，可用户绘制隐藏或者未知的扑克牌
     */
    private static final Poker UNKNOWN = new Poker(0, PokerType.UNKNOWN);

    /**
     * 需要绘制的扑克牌
     */
    private Poker poker;

    /**
     * 当前是否处于隐藏的状态
     */
    private boolean hidden = true;

    /**
     * 是否总是显示扑克牌
     */
    private boolean alwaysShow;

    /**
     * 扑克牌的绘图资源
     */
    private static Image pokerImage;
    static {
        pokerImage = new ImageIcon(Objects.requireNonNull(HiddenPokerPanel.class.getClassLoader().getResource("assets/image/poker.png"))).getImage();
    }

    public HiddenPokerPanel(Poker poker, boolean alwaysShow) {
        this.poker = poker;
        this.addMouseListener(this);
        this.alwaysShow = alwaysShow;
    }

    /**
     * 构造扑克牌画布对象，如果输入的扑克牌为空那么就默认为未知扑克牌
     * @param poker 需要绘制的扑克牌
     * @param alwaysShow 是否总是显示
     * @return 扑克牌画布对象
     */
    public static JPanel create(Poker poker, boolean alwaysShow) {
        return new HiddenPokerPanel(poker == null ? UNKNOWN : poker, alwaysShow);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPoker(g, getPoker(), 0, 0, getWidth(), getHeight());
    }

    /**
     * 绘制扑克牌
     * @param g 进行绘制的{@link Graphics}对象
     * @param poker 需要绘制的扑克牌对象
     * @param dx 绘制的目标x坐标
     * @param dy 绘制的目标y坐标
     * @param width 绘制的扑克牌宽度
     * @param height 绘制的扑克牌高度
     */
    public static void drawPoker(Graphics g, Poker poker, int dx, int dy, int width, int height) {
        int point = poker.getPoint();
        PokerType pokerType = poker.getPokerType();
        int sx, sy = 0;
        if (pokerType == PokerType.UNKNOWN) {
            sx = 300 * 2;
            sy = 435 * 4;
        }
        else {
            sx = (point - 1) % 13 * 300;
            switch (pokerType) {
                case ACE:  sy = 435 * 3; break;
                case HEART: sy = 435 * 2; break;
                case CLUB: sy = 0; break;
                case DIAMOND: sy = 435; break;
            }
        }
        g.drawImage(pokerImage, dx, dy, dx + width, dy + height, sx, sy, sx + 300, sy + 435, null);

    }

    /**
     * 根据当前的显示状态获得当前绘制的扑克牌
     * @return 如果是隐藏的则返回未知扑克牌，否则返回实际的扑克牌
     */
    public Poker getPoker() {
        return this.hidden && !this.alwaysShow ? UNKNOWN : poker;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constant.PLAYER_WIDTH, Constant.PLAYER_HEIGHT);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * 鼠标悬停时扑克牌显示
     * @param e 鼠标事件
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        this.hidden = false;
    }

    /**
     * 鼠标离开时隐藏扑克牌
     * @param e 鼠标事件
     */
    @Override
    public void mouseExited(MouseEvent e) {
        this.hidden = true;
    }
}