package com.buaa.texaspoker.entity;

import com.buaa.texaspoker.util.message.TranslateMessage;

/**
 * 扑克牌类型的枚举类
 * @author CPunisher
 */
public enum PokerType {
    /**
     * 黑桃
     */
    ACE("entity.poker.ace"),

    /**
     * 红桃
     */
    HEART("entity.poker.heart"),

    /**
     * 梅花
     */
    CLUB("entity.poker.club"),

    /**
     * 方片
     */
    DIAMOND("entity.poker.diamond"),

    /**
     * 未知
     */
    UNKNOWN("entity.poker.unknown");

    /**
     * 扑克牌类型的名称，使用<code>raw language</code>实现本地化
     */
    private String name;

    /**
     * 使用<code>raw language</code>作为名称构造扑克牌类型
     * @param name
     */
    PokerType(String name) {
        this.name = name;
    }

    /**
     * 通过{@link TranslateMessage}获得翻译后的扑克牌名称
     * @return 翻译后的扑克牌名称
     * @see TranslateMessage#format()
     */
    public String getName() {
        return new TranslateMessage(this.name).format();
    }
}
