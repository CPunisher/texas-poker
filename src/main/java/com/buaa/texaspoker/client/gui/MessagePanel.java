package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.util.message.ITextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 显示消息的面板
 * @author CPunisher
 * @see ITextMessage
 */
public class MessagePanel extends JPanel implements IMessagePanel {
    private static final Logger logger = LogManager.getLogger();

    /**
     * 消息面板的宽度
     */
    private static final int WIDTH = 280;

    /**
     * 消息面板的高度
     */
    private static final int HEIGHT = 470;

    /**
     * 消息面板的消息列表
     */
    private List<ITextMessage> messageList = new LinkedList<>();

    /**
     * 包含HTML样式的富文本显示框
     */
    private JTextPane textPane;

    /**
     * 消息面板的滚动条包装
     */
    private JScrollPane scrollPane;

    public MessagePanel(GameClient client) {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.textPane = new MessageTextPane();
        this.textPane.setLayout(null);
        this.textPane.setEditable(false);
        this.textPane.setOpaque(false);
        this.textPane.setFont(new Font("microsoft yahei", Font.PLAIN, 15));
        this.scrollPane = new JScrollPane(this.textPane);
        this.scrollPane.setBounds(0, 70, WIDTH, HEIGHT);
        this.scrollPane.setOpaque(false);
        this.scrollPane.getViewport().setOpaque(false);
        this.scrollPane.setBorder(BorderFactory.createLineBorder(new Color(59, 59, 59, 128)));
        this.scrollPane.getVerticalScrollBar().setUI(new MessageScrollBar());
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(this.scrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(59, 59, 59, 96));
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    /**
     * 将消息添加至消息列表，日志文件也做记录
     * @param textMessage 需要打印的消息
     */
    public void printMessage(ITextMessage textMessage) {
        this.addMessage(textMessage);
        logger.info(textMessage.format());
    }

    /**
     * 消息添加到消息列表的具体实现，将消息格式化之后插入富文本框
     * @param textMessage 需要打印的消息
     */
    private void addMessage(ITextMessage textMessage) {
        this.messageList.add(textMessage);

        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attributeSet, new Color(206, 195, 1));
        insertString(textMessage.format(), attributeSet);
        updateScroll();
    }

    /**
     * 更新滚动条
     */
    private void updateScroll() {
        this.textPane.setCaretPosition(this.textPane.getStyledDocument().getLength());
    }

    /**
     * 对{@link Document#insertString(int, String, AttributeSet)}的封装，设定插入消息至文本框末尾，并且自带换行
     * @param text 需要打印的消息文本
     * @param attributeSet 消息文本的样式属性
     */
    private void insertString(String text, SimpleAttributeSet attributeSet) {
        text += '\n';
        Document document = this.textPane.getStyledDocument();
        try {
            document.insertString(document.getLength(), text, attributeSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
