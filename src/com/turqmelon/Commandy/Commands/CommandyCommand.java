package com.turqmelon.Commandy.Commands;


import com.turqmelon.Commandy.Commandy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandyCommand implements CommandExecutor {

    private Commandy plugin;

    private String description;
    private String permission;
    private int minimumArgs;
    private String usage;
    private boolean requirePlayer;

    public CommandyCommand(Commandy plugin, String description, String permission, int minimumArgs, String usage, boolean requirePlayer) {
        this.plugin = plugin;
        this.description = description;
        this.permission = permission;
        this.minimumArgs = minimumArgs;
        this.usage = usage;
        this.requirePlayer = requirePlayer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings){
        if (isRequirePlayer()&&(!(sender instanceof Player))){
            sender.sendMessage(getPlugin().getLanguageManager().getElement("command_must_be_player").getConvertedColorText());
            return true;
        }
        if (strings.length<getMinimumArgs()){
            sender.sendMessage(getPlugin().getLanguageManager().getElement("command_insufficient_args").getConvertedColorText());
            sender.sendMessage(getPlugin().getLanguageManager().getElement("command_usage_example").getConvertedColorText().replace("{0}", getUsage()));
            return true;
        }
        if (getPermission()!=null&&!sender.hasPermission(getPermission())){
            sender.sendMessage(getPlugin().getLanguageManager().getElement("insufficient_permission").getConvertedColorText());
            return true;
        }
        execute(sender, command, s, strings);
        return true;
    }

    public Commandy getPlugin() {
        return plugin;
    }

    public abstract void execute(CommandSender sender, Command command, String label, String[] args);

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public int getMinimumArgs() {
        return minimumArgs;
    }

    public String getUsage() {
        return usage;
    }

    public boolean isRequirePlayer() {
        return requirePlayer;
    }
}
