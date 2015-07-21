package com.turqmelon.Commandy.Commands.Utility;

import com.turqmelon.Commandy.Commands.CommandyCommand;
import com.turqmelon.Commandy.Commandy;
import com.turqmelon.Commandy.Util.LanguageEntries;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
public class WhoCommand extends CommandyCommand {

    public WhoCommand(Commandy plugin, String description, String permission, int minimumArgs, String usage, boolean requirePlayer) {
        super(plugin, description, permission, minimumArgs, usage, requirePlayer);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        List<String> online = new ArrayList<>();

        for(Player player : Bukkit.getOnlinePlayers()){
            online.add(player.getDisplayName() + "§f");
        }

        sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.WHO_COMMAND_HEADER.getKey()).getConvertedColorText()
        .replace("{0}", Bukkit.getOnlinePlayers().size()+"")
        .replace("{1}", Bukkit.getMaxPlayers()+""));
        sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.WHO_COMMAND_LIST.getKey()).getConvertedColorText()
        .replace("{0}", online.toString().replace("[", "").replace("]", "")));

    }
}
