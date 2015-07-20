package com.turqmelon.Commandy;

import com.turqmelon.Commandy.Exception.CommandyLanguageException;
import com.turqmelon.Commandy.Util.CommandyLogger;
import com.turqmelon.Commandy.Util.LanguageManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Commandy extends JavaPlugin {

    private LanguageManager languageManager;
    private CommandyLogger logger;

    @Override
    public void onEnable(){
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


    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public CommandyLogger getCommandyLogger() {
        return logger;
    }
}
