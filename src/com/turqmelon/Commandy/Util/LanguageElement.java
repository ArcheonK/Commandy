package com.turqmelon.Commandy.Util;

import org.bukkit.ChatColor;


public class LanguageElement {

    private String key;
    private String text;

    public LanguageElement(String key, String text) {
        this.key = key;
        this.text = text;
    }

    public String getConvertedColorText(){
        return ChatColor.translateAlternateColorCodes('&', getText());
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }
}
