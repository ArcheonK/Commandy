package com.turqmelon.Commandy.Listeners;

import com.turqmelon.Commandy.Commandy;
import org.bukkit.event.Listener;

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
public abstract class CommandyListener implements Listener {

    private Commandy plugin;

    public CommandyListener(Commandy plugin) {
        this.plugin = plugin;
    }

    public Commandy getPlugin() {
        return plugin;
    }
}
