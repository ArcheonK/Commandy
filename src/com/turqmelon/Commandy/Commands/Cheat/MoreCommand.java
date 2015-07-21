package com.turqmelon.Commandy.Commands.Cheat;

import com.turqmelon.Commandy.Commands.CommandyCommand;
import com.turqmelon.Commandy.Commandy;
import com.turqmelon.Commandy.Util.LanguageEntries;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class MoreCommand extends CommandyCommand {

    public MoreCommand(Commandy plugin, String description, String permission, int minimumArgs, String usage, boolean requirePlayer) {
        super(plugin, description, permission, minimumArgs, usage, requirePlayer);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        Player target = (Player)sender;

        boolean allItems = false;

        if (args.length>0&&args[0].equalsIgnoreCase("all")){
            allItems=true;
        }

        if (allItems){

            for(ItemStack item : target.getInventory()){
                if (item!=null&&item.getType()!=Material.AIR){
                    item.setAmount(item.getType().getMaxStackSize());
                }
            }
            target.updateInventory();

            sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.MORE_COMMAND_ALL.getKey()).getConvertedColorText());


        }
        else{

            ItemStack hand = target.getItemInHand();
            if (hand!=null&&hand.getType()!= Material.AIR){
                hand.setAmount(hand.getType().getMaxStackSize());
                target.updateInventory();
                sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.MORE_COMMAND_HAND.getKey()).getConvertedColorText());

            }
            else{
                sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.EMPTY_HAND.getKey()).getConvertedColorText());
            }

        }

    }
}
