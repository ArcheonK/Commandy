package com.turqmelon.Commandy;

import com.turqmelon.Commandy.Commands.Cheat.ItemCommand;
import com.turqmelon.Commandy.Commands.Cheat.MoreCommand;
import com.turqmelon.Commandy.Commands.Utility.WhoCommand;
import com.turqmelon.Commandy.Exception.CommandyLanguageException;
import com.turqmelon.Commandy.Util.CommandyLogger;
import com.turqmelon.Commandy.Util.LanguageManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

public class Commandy extends JavaPlugin {

    private LanguageManager languageManager;
    private CommandyLogger logger;

    @Override
    public void onEnable(){

        if (!(new File(getDataFolder(), "config.yml")).exists()){
            saveDefaultConfig();
        }

        this.logger = new CommandyLogger(this); // Initialize the CommandyLogger
        this.languageManager = new LanguageManager(this); // Initialize our Language Manager

        File lang = new File(getDataFolder(), "lang.yml"); // Get our lang.yml file
        if (!lang.exists()){
            // If it doesn't exist, create it
            try {
                if (lang.createNewFile()){
                    getCommandyLogger().log(Level.INFO, "Created a lang.yml!");
                }
                else{
                    getCommandyLogger().log(Level.SEVERE, "Failed to create a lang.yml!");
                }
            } catch (IOException e) {
                getCommandyLogger().log(Level.SEVERE, "Failed to create a lang.yml! (" + e.getMessage() + ")");
            }
        }

        try {
            // Load entries from our language file, as well as update the language file if there are new entries since last run
            getLanguageManager().loadLanguageFile(lang);
            getLanguageManager().loadEntries();
            getLanguageManager().updateEntries();
        } catch (IOException | InvalidConfigurationException e) {
            getCommandyLogger().log(Level.SEVERE, "Failed to load lang.yml! (" + e.getMessage() + ")");
        } catch (CommandyLanguageException e) {
            e.sendToLogger();
        }

        registerCommands();


    }

    private void registerCommands() {
        {
            ItemCommand cmd = new ItemCommand(this, "Give yourself, or someone else, an item", "commandy.give", 0, "/give [Player] <Item> [Amount] [Data]", false);
            PluginCommand c = getCommand("item");
            c.setExecutor(cmd);
            c.setDescription(cmd.getDescription());
            c.setAliases(Arrays.asList("i", "give"));
        }
        {
            MoreCommand cmd = new MoreCommand(this, "Give more of an item", "commandy.more", 0, "/more [All]", true);
            PluginCommand c = getCommand("more");
            c.setExecutor(cmd);
            c.setDescription(cmd.getDescription());
            c.setAliases(Arrays.asList("stack"));
        }
        {
            WhoCommand cmd = new WhoCommand(this, "Show online players", "commandy.who", 0, "/who", false);
            PluginCommand c = getCommand("who");
            c.setExecutor(cmd);
            c.setDescription(cmd.getDescription());
            c.setAliases(Arrays.asList("list", "playerlist", "players", "online"));
        }
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public CommandyLogger getCommandyLogger() {
        return logger;
    }
}
