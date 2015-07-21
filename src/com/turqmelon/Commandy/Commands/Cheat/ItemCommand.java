package com.turqmelon.Commandy.Commands.Cheat;


import com.turqmelon.Commandy.Commands.CommandyCommand;
import com.turqmelon.Commandy.Commandy;
import com.turqmelon.Commandy.Util.LanguageEntries;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand extends CommandyCommand {

    public ItemCommand(Commandy plugin, String description, String permission, int minimumArgs, String usage, boolean requirePlayer) {
        super(plugin, description, permission, minimumArgs, usage, requirePlayer);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        Player target = null;

        if (args.length>0){
            target= Bukkit.getPlayer(args[0]);
        }
        else if ((sender instanceof Player)){
            target=(Player)sender;
        }

        if (target==null){
            sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.TARGET_NOT_FOUND.getKey()).getConvertedColorText());
            return;
        }

        if (!target.getName().equals(sender.getName())&&!sender.hasPermission("commandy.give.other")){
            sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.INSUFFICIENT_PERMISSION.getKey()).getConvertedColorText());
            return;
        }

        Material material = Material.AIR;
        String materialInput = null;

        if (target.getName().equals(sender.getName())&&args.length>0){
            materialInput=args[0];
        }
        else if (args.length>1){
            materialInput=args[1];
        }

        boolean notfound = false;

        if (materialInput==null){
            notfound=true;
        }

        if (!notfound){
            try {
                material=Material.getMaterial(materialInput.toUpperCase());
            }catch (Exception ex){
                notfound=true;
            }
        }

        if (notfound){
            sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.MATERIAL_NOT_FOUND.getKey()).getConvertedColorText());
            return;
        }

        int amount = 1;
        String amountInput = null;

        if (target.getName().equals(sender.getName())&&args.length>1){
            amountInput=args[1];
        }
        else if (args.length>2){
            amountInput=args[2];
        }

        if (amountInput!=null){
            try {
                amount=Integer.parseInt(amountInput);
            }catch (NumberFormatException ex){
                sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.INVALID_INTEGER.getKey()).getConvertedColorText());
                return;
            }
        }

        if (amount<1||amount>material.getMaxStackSize()){
            sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.INVALID_STACK_SIZE.getKey()).getConvertedColorText().replace("{0}", material.getMaxStackSize()+""));
            return;
        }

        byte data = 0;
        String dataInput = null;

        if (target.getName().equals(sender.getName())&&args.length>2){
            dataInput=args[2];
        }
        else if (args.length>3){
            dataInput=args[3];
        }

        if (dataInput != null) {
            try {
                int given = Integer.parseInt(dataInput);
                data=(byte)given;
            }catch (NumberFormatException ex){
                sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.INVALID_INTEGER.getKey()).getConvertedColorText());
                return;
            }
        }

        ItemStack item = new ItemStack(material, amount, data);
        target.getInventory().addItem(item);

        if (target.getName().equals(sender.getName())){
            sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.GIVE_COMMAND_GIVEN.getKey()).getConvertedColorText()
                    .replace("{0}", material.name().toLowerCase().replace("_", " ")
                            .replace("{1}", amount+"")));
        }
        else{
            sender.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.GIVE_COMMAND_GIVEN_TO.getKey()).getConvertedColorText()
                    .replace("{0}", material.name().toLowerCase().replace("_", " ")
                            .replace("{1}", amount+"")
                                .replace("{2}", target.getName())));
            target.sendMessage(getPlugin().getLanguageManager().getElement(LanguageEntries.GIVE_COMMAND_GIVEN_FROM.getKey()).getConvertedColorText()
                    .replace("{0}", material.name().toLowerCase().replace("_", " ")
                            .replace("{1}", amount+"")
                            .replace("{2}", sender.getName())));
        }



    }
}
