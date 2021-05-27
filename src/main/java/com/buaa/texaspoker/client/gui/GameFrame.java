package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final GameClient client;
    private GamePanel gamePanel;
    private MessagePanel messagePanel;

    public GameFrame(String title, GameClient client) throws HeadlessException {
        super(title);
        this.client = client;
        this.gamePanel = new GamePanel(client);
        this.messagePanel = new MessagePanel();
        this.add(this.gamePanel, BorderLayout.WEST);
        this.add(this.messagePanel, BorderLayout.EAST);
        this.setResizable(false);
        this.pack();

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public MessagePanel getMessagePanel() {
        return messagePanel;
    }
}
