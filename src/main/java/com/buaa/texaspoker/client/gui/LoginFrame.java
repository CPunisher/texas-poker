package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.ClientNetworkSystem;
import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.network.login.CPacketConnect;
import com.buaa.texaspoker.util.ConsoleUtil;
import com.buaa.texaspoker.util.PropertiesManager;
import com.buaa.texaspoker.util.message.ITextMessage;
import com.buaa.texaspoker.util.message.TranslateMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class LoginFrame extends JFrame implements ActionListener {

    private static final Logger logger = LogManager.getLogger();
    private static PropertiesManager propertiesManager = PropertiesManager.getInstance();

    private JLabel nameLabel;
    private JLabel hostLabel;
    private JLabel messageLabel;
    private JTextField nameTextField;
    private JTextField hostTextField;
    private JButton connectButton;
    private JButton cancelButton;

    private final GameClient client;
    private final ClientNetworkSystem networkSystem;

    public LoginFrame(String title, GameClient client, ClientNetworkSystem networkSystem) {
        super(title);
        this.client = client;
        this.networkSystem = networkSystem;
        this.nameLabel = new JLabel(new TranslateMessage("gui.login_frame.name").format());
        this.hostLabel = new JLabel(new TranslateMessage("gui.login_frame.host").format());
        this.messageLabel = new JLabel("");
        this.nameTextField = new JTextField(propertiesManager.getValue("name", ""), 15);
        this.hostTextField = new JTextField();
        this.nameLabel.setVerticalTextPosition(JLabel.CENTER);
        this.hostLabel.setVerticalTextPosition(JLabel.CENTER);
        this.messageLabel.setPreferredSize(new Dimension(getWidth(), 50));
        this.messageLabel.setAlignmentY(JLabel.TOP_ALIGNMENT);
        this.connectButton = new JButton(new TranslateMessage("gui.login_frame.connect").format());
        this.cancelButton = new JButton(new TranslateMessage("gui.login_frame.cancel").format());
        this.connectButton.addActionListener(this);
        this.cancelButton.addActionListener(evt -> LoginFrame.this.dispose());
        setButtonStyle(this.cancelButton);
        setButtonStyle(this.connectButton);
        this.messageLabel.setForeground(Color.RED);

        JPanel labelPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        labelPanel.add(nameLabel);
        labelPanel.add(hostLabel);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        textPanel.add(nameTextField);
        textPanel.add(hostTextField);

        JPanel editPanel = new JPanel();
        editPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        editPanel.add(labelPanel);
        editPanel.add(textPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(connectButton);

        JPanel operationPanel = new JPanel(new BorderLayout());
        operationPanel.add(messageLabel, BorderLayout.CENTER);
        operationPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 3, 10));
        panel.add(editPanel, BorderLayout.CENTER);
        panel.add(operationPanel, BorderLayout.SOUTH);
        this.add(panel);
        this.pack();

        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connectButton) {
            String name = nameTextField.getText().trim();
            String host = hostTextField.getText().trim();
            InetSocketAddress remoteAddress = new InetSocketAddress(host, 8888);
            printMessage(new TranslateMessage("message.client_network.connecting", remoteAddress));
            this.connectButton.setEnabled(false);

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    ChannelFuture endpoint = LoginFrame.this.networkSystem.connect(remoteAddress)
                            .addListener((ChannelFutureListener) future -> {
                                if (future.isSuccess()) {
                                    future.channel().writeAndFlush(new CPacketConnect(name));
                                } else {
                                    printMessage(new TranslateMessage("message.client_network.timeout"));
                                }
                                connectButton.setEnabled(true);
                            });
                    endpoint.syncUninterruptibly();
                    endpoint.channel().eventLoop().schedule(() -> {
                        endpoint.channel().closeFuture().addListener((ChannelFutureListener) future -> {
                            if (client.getGui() != null) {
                                networkSystem.resetHandler();
                                client.backToLogin();
                            }
                            printMessage(new TranslateMessage("message.client_network.retry"));
                        }).syncUninterruptibly();
                    }, 0, TimeUnit.MILLISECONDS);
                    return null;
                }
            };
            worker.execute();
        }
    }

    private void printMessage(ITextMessage textMessage) {
        this.messageLabel.setText("<html>" + textMessage.format() + "</html>");
        logger.info(textMessage.format());
    }

    public static void setButtonStyle(JButton button) {
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        button.setBorder(compound);
        button.setFocusPainted(false);
    }
}
