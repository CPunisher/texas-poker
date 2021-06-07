package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 绘制扑克牌列表的画布，包括公共扑克牌和个人持有的扑克牌
 * @author CPunisher
 * @see Poker
 * @see HiddenPokerPanel
 */
public class PokerPanel extends AbstractGamePanel {

    /**
     * 扑克牌列表画布的宽度
     */
    private static final int WIDTH = 1280;

    /**
     * 扑克牌列表画布的高度
     */
    private static final int HEIGHT = 250;

    /**
     * 公共扑克牌展示区域
     */
    private JPanel publicPokers;

    /**
     * 个人持有的扑克牌展示区域
     */
    private JPanel privatePokers;

    /**
     * 创建{@link PokerPanel}实例，初始化两种扑克牌的展示面板，并加载扑克牌信息
     * @param client 附属的{@link GameClient}引用
     */
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

    /**
     * 将房间内的扑克牌信息装载到<code>publicPokers</code>和<code>privatePokers</code>中
     * 首先移除原有的全部扑克牌信息，加入所有由{@link HiddenPokerPanel}包装的扑克牌之后，请求重新绘制
     * @see HiddenPokerPanel
     */
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

    /**
     * 对{@link java.util.List#get(int)}的优化，不抛出{@link IndexOutOfBoundsException}
     * @param list 列表
     * @param index 获取的元素下标
     * @param <T> 列表的泛型
     * @return 如果下标不越界则返回对应的元素，否则返回<code>null</code>
     */
    public <T> T elementAt(java.util.List<T> list, int index) {
        if (index < list.size()) return list.get(index);
        return null;
    }
}
