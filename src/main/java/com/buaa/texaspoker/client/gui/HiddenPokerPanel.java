package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.util.Constant;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class HiddenPokerPanel extends JPanel implements MouseListener {
    private static final Poker UNKNOWN = new Poker(0, PokerType.UNKNOWN);
    private Poker poker;
    private boolean hidden = true;
    private boolean alwaysShow;
    private static Image pokerImage;
    static {
        pokerImage = new ImageIcon(Objects.requireNonNull(HiddenPokerPanel.class.getClassLoader().getResource("assets/image/poker.png"))).getImage();
    }

    public HiddenPokerPanel(Poker poker, boolean alwaysShow) {
        this.poker = poker;
        this.addMouseListener(this);
        this.alwaysShow = alwaysShow;
    }

    public static JPanel create(Poker poker, boolean alwaysShow) {
        return new HiddenPokerPanel(poker == null ? UNKNOWN : poker, alwaysShow);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPoker(g, getPoker(), 0, 0, getWidth(), getHeight());
    }

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
                case ACE -> sy = 435 * 3;
                case HEART -> sy = 435 * 2;
                case CLUB -> sy = 0;
                case DIAMOND -> sy = 435;
            }
        }
        g.drawImage(pokerImage, dx, dy, dx + width, dy + height, sx, sy, sx + 300, sy + 435, null);

    }

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

    @Override
    public void mouseEntered(MouseEvent e) {
        this.hidden = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.hidden = true;
    }
}