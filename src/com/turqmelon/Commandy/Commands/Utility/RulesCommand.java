package com.turqmelon.Commandy.Commands.Utility;

import com.turqmelon.Commandy.Commands.CommandyCommand;
import com.turqmelon.Commandy.Commandy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class RulesCommand extends CommandyCommand {

    public RulesCommand(Commandy plugin, String description, String permission, int minimumArgs, String usage, boolean requirePlayer) {
        super(plugin, description, permission, minimumArgs, usage, requirePlayer);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        getPlugin().sendRules((Player) sender);

    }
}
