package com.buaa.texaspoker.util.message;

/**
 * 控制消息文本的接口
 * @author CPunisher
 */
public interface ITextMessage {

    /**
     * 不同消息对象根据自身算法特性最终返回一个文本作为结果
     * @return 消息对象格式化之后的文本
     */
    String format();
}
