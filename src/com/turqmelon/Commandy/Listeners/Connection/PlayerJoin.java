package com.turqmelon.Commandy.Listeners.Connection;

import com.turqmelon.Commandy.Commandy;
import com.turqmelon.Commandy.Listeners.CommandyListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
public class PlayerJoin extends CommandyListener {

    public PlayerJoin(Commandy plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        new BukkitRunnable(){
            @Override
            public void run(){
                getPlugin().sendMotd(player);
            }
        }.runTaskLater(getPlugin(), 20L);
    }

}
