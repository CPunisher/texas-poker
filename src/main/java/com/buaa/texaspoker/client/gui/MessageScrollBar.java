package com.buaa.texaspoker.client.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * 消息面板的滚动条
 * @author CPunisher
 */
public class MessageScrollBar extends BasicScrollBarUI {

    private final Dimension dimension = new Dimension();

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color;
        JScrollBar scrollBar = (JScrollBar) c;
        if (!scrollBar.isEnabled() || thumbBounds.width > thumbBounds.height) {
            return;
        } else if (isDragging) {
            color = new Color(207, 207, 207, 200);
        } else if (isThumbRollover()) {
            color = new Color(207, 207, 207, 200);
        } else {
            color = new Color(227, 227, 227, 200);
        }
        g2.setPaint(color);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
        g2.setPaint(Color.WHITE);
        g2.drawRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
        g2.dispose();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return dimension;
            }
        };
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return dimension;
            }
        };
    }
}
