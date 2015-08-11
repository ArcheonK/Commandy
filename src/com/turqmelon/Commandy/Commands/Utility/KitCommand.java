package com.turqmelon.Commandy.Commands.Utility;


import com.turqmelon.Commandy.Commands.CommandyCommand;
import com.turqmelon.Commandy.Commandy;
import com.turqmelon.Commandy.Util.Kit;
import com.turqmelon.Commandy.Util.LanguageEntries;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class KitCommand extends CommandyCommand {

    public KitCommand(Commandy plugin, String description, String permission, int minimumArgs, String usage, boolean requirePlayer) {
        super(plugin, description, permission, minimumArgs, usage, requirePlayer);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        Kit kit = null;
        if (args.length>0){
            for(Kit k : getPlugin().getKits()){
                if (k.getName().equalsIgnoreCase(args[0])){
                    kit=k;
                    break;
                }
            }
        }

        if (kit==null){

            if (args.length>0){
                sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.KIT_COMMAND_NOT_FOUND.getKey()).getConvertedColorText());
            }
            if (sender.hasPermission("commandy.kit.list")){
                List<String> validKits = new ArrayList<>();
                for(Kit k : getPlugin().getKits()){
                    if (!k.isCheckPermission()||sender.hasPermission("commandy.kit." + k.getName().toLowerCase())){
                        validKits.add(k.getName());
                    }
                }

                sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.KIT_COMMAND_LIST.getKey()).getConvertedColorText()
                .replace("{0}", validKits.toString().replace("[", "").replace("]", "")));
            }


            return;
        }

        if (kit.isCheckPermission()&&!sender.hasPermission("commandy.kit." + kit.getName().toLowerCase())){
            sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.KIT_COMMAND_NOACCESS.getKey()).getConvertedColorText());
            return;
        }

        long cooldown = kit.getCooldown();

        Player player = (Player)sender;

        long lastUse = (kit.getLastUse().containsKey(player.getUniqueId())?kit.getLastUse().get(player.getUniqueId()):0);

        if (!player.hasPermission("commandy.kit.nocooldowns")&&(!player.hasPermission("commandy.kit." + kit.getName().toLowerCase() + ".nocooldown"))&&(System.currentTimeMillis()-lastUse<cooldown)){
            sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.KIT_COMMAND_COOLDOWN.getKey()).getConvertedColorText());
            return;
        }

        for(ItemStack item : kit.getItems()){
            player.getInventory().addItem(item);
        }

        kit.getLastUse().put(player.getUniqueId(), System.currentTimeMillis());
        try {
            kit.save();
        } catch (IOException | InvalidConfigurationException e) {
            getPlugin().getCommandyLogger().log(Level.SEVERE, "Failed to save kits.yml! (" + e.getMessage() + ")");
        }

        sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.KIT_COMMAND_GIVEN.getKey()).getConvertedColorText()
        .replace("{0}", kit.getName()));


    }
}
