package com.buaa.texaspoker.client.gui;

import com.buaa.texaspoker.util.message.ITextMessage;

/**
 * 显示消息的面板接口
 * 仅暴露{@link IMessagePanel#printMessage(ITextMessage)}方法
 * @author CPunisher
 */
public interface IMessagePanel {

    /**
     * 打印消息
     * @param textMessage 需要打印的消息
     */
    void printMessage(ITextMessage textMessage);
}
