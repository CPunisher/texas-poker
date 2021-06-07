package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.network.play.SPacketRequestBetting;
import com.buaa.texaspoker.util.message.TranslateMessage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 下注对话框
 * @author CPunisher
 */
public class BettingDialog extends JDialog implements PropertyChangeListener {

    /**
     * 放弃按钮
     */
    private JButton giveUpButton;

    /**
     * Check 按钮
     */
    private JButton checkButton;

    /**
     * All In 按钮
     */
    private JButton allInButton;

    /**
     * 下注按钮
     */
    private JButton betButton;

    /**
     * 下注值输入框
     */
    private JTextField textField;

    /**
     * 整个窗体的面板
     */
    private JOptionPane optionPane;

    /**
     * 实际的下注的值
     */
    private int value;

    /**
     * 本回合的累计赌注
     */
    private int sectionBetting;

    /**
     * 本次下注的最少金额
     */
    private int minimum;

    /**
     * 玩家持有的金额
     */
    private int money;

    public BettingDialog(Frame frame, SPacketRequestBetting packet, boolean canCheck) {
        super(frame, false);
        this.setTitle(new TranslateMessage("gui.betting_dialog.title").format());
        this.sectionBetting = packet.getSectionBetting();
        this.minimum = packet.getMinimum();
        this.money = packet.getPlayerMoney();
        this.textField = new JTextField(10);
        this.giveUpButton = new JButton(new TranslateMessage("gui.betting_dialog.give_up").format());
        this.checkButton = new JButton(new TranslateMessage("gui.betting_dialog.check").format());
        this.allInButton = new JButton(new TranslateMessage("gui.betting_dialog.all_in").format());
        this.betButton = new JButton(new TranslateMessage("gui.betting_dialog.bet").format());
        String msgString1 = new TranslateMessage("gui.betting_dialog.msg1").format();
        String msgString2 = new TranslateMessage("gui.betting_dialog.msg2", sectionBetting, packet.getSectionBonus(), minimum).format();

        this.giveUpButton.setEnabled(packet.canGiveUp());
        this.betButton.setEnabled(false);
        this.checkButton.setEnabled(canCheck);
        Object[] array = {msgString1, msgString2, textField};
        JButton[] options = {giveUpButton, checkButton, allInButton, betButton};
        for (JButton button : options) {
            button.setForeground(Color.BLACK);
            button.setBackground(Color.WHITE);
            Border line = new LineBorder(Color.BLACK);
            Border margin = new EmptyBorder(5, 15, 5, 15);
            Border compound = new CompoundBorder(line, margin);
            button.setBorder(compound);
        }
        optionPane = new JOptionPane(array,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                options,
                betButton);
        this.setContentPane(optionPane);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.giveUpButton.addActionListener(event -> setValueAndClose(-2));
        this.checkButton.addActionListener(event -> setValueAndClose(-1));
        this.allInButton.addActionListener(event -> setValueAndClose(packet.getPlayerMoney() + packet.getSectionBetting()));
        this.betButton.addActionListener(event -> setValueAndClose(Integer.parseInt(textField.getText())));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                optionPane.setValue(JOptionPane.CLOSED_OPTION);
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                textField.requestFocusInWindow();
            }
        });

        this.textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                optionPane.setValue(betButton.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                optionPane.setValue(betButton.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                optionPane.setValue(betButton.getText());
            }
        });
        this.optionPane.addPropertyChangeListener(this);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(frame);

        if (this.minimum == 0) this.minimum = 1;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (this.isVisible()
                && evt.getSource() == optionPane
                && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
                    JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
            if (betButton.getText().equals(value)) {
                String typedText = textField.getText();
                this.betButton.setEnabled(validate(typedText));
            }
        }
    }

    /**
     * 验证玩家输入的下注金额是否合法，包括是否为整数的判断和金额是否符合规则
     * <p>下注金额需要满足：不能超过玩家持有金额，且必须是最小金额的整倍数</p>
     * @param text 用户输入的下注
     * @return 如果下注合法则返回<code>True</code>
     */
    private boolean validate(String text) {
        try {
            int amount = Integer.parseInt(text);
            int inc = amount - sectionBetting;
            if (amount == sectionBetting
//                    || (!canCheck && amount == -1)
                    || (amount > 0 && amount % minimum != 0)
//                    || (amount < 0 && amount != -2)
                    || inc > money) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 完成下注，关闭窗口
     * @param value 下注的金额
     */
    private void setValueAndClose(int value) {
        this.value = value;
        this.setVisible(false);
    }

    /**
     * 获得下注金额数值
     * @return 下注金额
     */
    public int getValue() {
        return value;
    }
}
