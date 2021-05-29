package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.util.message.ITextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class MessagePanel extends JPanel implements IMessagePanel {

    private static final Logger logger = LogManager.getLogger();
    private static final int WIDTH = 280;
    private static final int HEIGHT = 720;
    private List<ITextMessage> messageList = new LinkedList<>();

    private final JList<String> list = new JList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public MessagePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.setBorder(BorderFactory.createEtchedBorder());

        this.list.setModel(listModel);
        this.add(list);
    }

    public void printMessage(ITextMessage textMessage) {
        this.addMessage(textMessage);
        logger.info(textMessage.format());
    }

    private void addMessage(ITextMessage textMessage) {
        this.messageList.add(textMessage);
        this.listModel.addElement(textMessage.format());
    }
}
