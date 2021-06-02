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

    private JPanel publicPokers;
    private JPanel privatePokers;

    public PokerPanel(GameClient client) {
        super(client, WIDTH, HEIGHT);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
        this.publicPokers = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        this.privatePokers = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        this.publicPokers.setOpaque(false);
        this.privatePokers.setOpaque(false);

        this.loadPokers();
        this.add(publicPokers);
        this.add(privatePokers);
    }

    public void loadPokers() {
        this.publicPokers.removeAll();
        this.privatePokers.removeAll();
        java.util.List<Poker> publicList = client.getRoom().getPublicPokers();
        java.util.List<Poker> privateList = client.getPlayer().getData().getPokers();
        for (int i = 0; i < 5; i++) {
            this.publicPokers.add(HiddenPokerPanel.create(elementAt(publicList, i), true));
        }
        this.privatePokers.add(HiddenPokerPanel.create(elementAt(privateList, 0), false));
        this.privatePokers.add(HiddenPokerPanel.create(elementAt(privateList, 1), false));
        this.revalidate();
        this.repaint();
    }

    @Override
    public void draw(Graphics2D g) {}

    public <T> T elementAt(java.util.List<T> list, int index) {
        if (index < list.size()) return list.get(index);
        return null;
    }
}
