package com.turqmelon.Commandy;

import com.turqmelon.Commandy.Commands.Cheat.ItemCommand;
import com.turqmelon.Commandy.Commands.Cheat.MoreCommand;
import com.turqmelon.Commandy.Commands.Utility.MotdCommand;
import com.turqmelon.Commandy.Commands.Utility.RulesCommand;
import com.turqmelon.Commandy.Commands.Utility.WhoCommand;
import com.turqmelon.Commandy.Exception.CommandyLanguageException;
import com.turqmelon.Commandy.Listeners.Connection.PlayerJoin;
import com.turqmelon.Commandy.Util.CommandyLogger;
import com.turqmelon.Commandy.Util.LanguageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Commandy extends JavaPlugin {

    private LanguageManager languageManager;
    private CommandyLogger logger;
    private List<String> motd = new ArrayList<>();
    private List<String> rules = new ArrayList<>();

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
        registerListeners();


        try {
            loadMOTD();
        } catch (IOException e) {
            getCommandyLogger().log(Level.SEVERE, "Failed to load motd.txt! (" + e.getMessage() + ")");
        }
        try {
            loadRules();
        } catch (IOException e) {
            getCommandyLogger().log(Level.SEVERE, "Failed to load rules.txt! (" + e.getMessage() + ")");
        }




    }

    private void loadRules() throws IOException {
        File file = new File(getDataFolder(), "rules.txt");
        if (!file.exists()){
            file.createNewFile();
            String newLine = System.getProperty("line.separator");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(
                    "&c&lOur Rules:"+newLine+
                    "&61)&f Be Nice"+newLine+
                    "&62)&f Be Respectful"+newLine+
                    "&63)&f Use Common Sense");
            writer.close();
            getCommandyLogger().log(Level.INFO, "Generated default rules file!");
        }

        FileInputStream fstream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        while ((strLine = br.readLine()) != null)   {
            getRules().add(strLine);
        }

        br.close();

    }

    private void loadMOTD() throws IOException {
        File file = new File(getDataFolder(), "motd.txt");
        if (!file.exists()){
            file.createNewFile();
            String newLine = System.getProperty("line.separator");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(
                    "&a&lWelcome to {SERVERNAME}, {NAME}!" + newLine +
                    "&eYou're currently in the &f{WORLD}&e world." + newLine +
                    "&eType &f/help&e for help, or &f/rules&e for our rules!" + newLine +
                    "&7Admins: Edit this MOTD in motd.txt in plugins/Commandy");
            writer.close();
            getCommandyLogger().log(Level.INFO, "Generated default motd file!");
        }

        FileInputStream fstream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        while ((strLine = br.readLine()) != null)   {
            getMotd().add(strLine);
        }

        br.close();

    }

    public void sendRules(Player player){
        if (player.hasPermission("commandy.rules")){
            for(String line : getRules()){
                line= ChatColor.translateAlternateColorCodes('&', line);
                line=line.replace("{NAME}", player.getName());
                line=line.replace("{DISPLAYNAME}", player.getDisplayName());
                line=line.replace("{WORLD}", player.getWorld().getName());
                line=line.replace("{SERVERNAME}", Bukkit.getServer().getName());
                line=line.replace("{ONLINEPLAYERS}", Bukkit.getServer().getOnlinePlayers().size()+"");
                line=line.replace("{MAXPLAYERS}", Bukkit.getServer().getMaxPlayers()+"");
                player.sendMessage(line);
            }
        }
    }

    public void sendMotd(Player player){
        if (player.hasPermission("commandy.motd")){
            for(String line : getMotd()){
                line= ChatColor.translateAlternateColorCodes('&', line);
                line=line.replace("{NAME}", player.getName());
                line=line.replace("{DISPLAYNAME}", player.getDisplayName());
                line=line.replace("{WORLD}", player.getWorld().getName());
                line=line.replace("{SERVERNAME}", Bukkit.getServer().getName());
                line=line.replace("{ONLINEPLAYERS}", Bukkit.getServer().getOnlinePlayers().size()+"");
                line=line.replace("{MAXPLAYERS}", Bukkit.getServer().getMaxPlayers()+"");
                player.sendMessage(line);
            }
        }
    }

    public List<String> getRules() {
        return rules;
    }

    public List<String> getMotd() {
        return motd;
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoin(this), this);
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
            MotdCommand cmd = new MotdCommand(this, "Show the MOTD", "commandy.motd", 0, "/motd", true);
            PluginCommand c = getCommand("motd");
            c.setExecutor(cmd);
            c.setDescription(cmd.getDescription());
        }
        {
            RulesCommand cmd = new RulesCommand(this, "Show the server rules", "commandy.rules", 0, "/rules", true);
            PluginCommand c = getCommand("rules");
            c.setExecutor(cmd);
            c.setDescription(cmd.getDescription());
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
