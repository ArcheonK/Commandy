package com.turqmelon.Commandy.Util;

import org.bukkit.Material;

/**
 * ***********************************************************************
 * Copyright Turqmelon (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Turqmelon. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the liscense, and void any
 * agreements with you, the third party.
 * Thanks.
 * ************************************************************************
 */
public class ItemAlias {

    private String alias;
    private Material type;
    private byte data;

    public ItemAlias(String alias, Material type, byte data) {
        this.alias = alias;
        this.type = type;
        this.data = data;
    }

    public String getAlias() {
        return alias;
    }

    public Material getType() {
        return type;
    }

    public byte getData() {
        return data;
    }
}
