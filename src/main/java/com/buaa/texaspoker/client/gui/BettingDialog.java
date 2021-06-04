package com.buaa.texaspoker.client.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BettingDialog extends JDialog implements PropertyChangeListener {

    private JButton giveUpButton;
    private JButton checkButton;
    private JButton betButton;
    private JTextField textField;
    private JOptionPane optionPane;

    private int value;
    private int sectionBetting;
    private int minimum;
    private int money;
    private boolean canCheck;

    public BettingDialog(Frame frame, int sectionBetting, int sectionBonus, int minimum, int money, boolean canCheck) {
        super(frame, true);
        this.setTitle("Betting");
        this.sectionBetting = sectionBetting;
        this.minimum = minimum;
        this.money = money;
        this.canCheck = canCheck;
        this.textField = new JTextField(10);
        this.giveUpButton = new JButton("Give up");
        this.checkButton = new JButton("Check");
        this.betButton = new JButton("Bet");
        String msgString1 = "How much do you want to bet?";
        String msgString2 = String.format("Your section betting: %d, Section Bonus: %d, Min: %d", sectionBetting, sectionBonus, minimum);

        this.betButton.setEnabled(false);
        this.checkButton.setEnabled(canCheck);
        Object[] array = {msgString1, msgString2, textField};
        Object[] options = {giveUpButton, checkButton, betButton};
        optionPane = new JOptionPane(array,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                options,
                options[0]);
        this.setContentPane(optionPane);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.giveUpButton.addActionListener(event -> setValueAndClose(-2));
        this.checkButton.addActionListener(event -> setValueAndClose(-1));
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

    private void setValueAndClose(int value) {
        this.value = value;
        this.setVisible(false);
    }

    public int getValue() {
        return value;
    }
}
