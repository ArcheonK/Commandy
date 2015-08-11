package com.turqmelon.Commandy;

import com.turqmelon.Commandy.Commands.Cheat.ItemCommand;
import com.turqmelon.Commandy.Commands.Cheat.MoreCommand;
import com.turqmelon.Commandy.Commands.Utility.KitCommand;
import com.turqmelon.Commandy.Commands.Utility.MotdCommand;
import com.turqmelon.Commandy.Commands.Utility.RulesCommand;
import com.turqmelon.Commandy.Commands.Utility.WhoCommand;
import com.turqmelon.Commandy.Exception.CommandyLanguageException;
import com.turqmelon.Commandy.Listeners.Connection.PlayerJoin;
import com.turqmelon.Commandy.Util.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class Commandy extends JavaPlugin {

    private LanguageManager languageManager;
    private CommandyLogger logger;
    private List<String> motd = new ArrayList<>();
    private List<String> rules = new ArrayList<>();
    private List<Kit> kits = new ArrayList<>();
    private List<ItemAlias> aliases = new ArrayList<>();

    public List<ItemAlias> getAliases() {
        return aliases;
    }

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

        Kit.setPlugin(this);

        try {
            loadKits();
        } catch (IOException | InvalidConfigurationException e) {
            getCommandyLogger().log(Level.SEVERE, "Failed to load kits.yml! (" + e.getMessage() + ")");
        }

        try {
            loadItemAliases();
        } catch (IOException | InvalidConfigurationException e) {
            getCommandyLogger().log(Level.SEVERE, "Failed to load itemaliases.yml! (" + e.getMessage() + ")");
        }


    }

    private void loadRules() throws IOException {
        File file = new File(getDataFolder(), "rules.txt");
        if (!file.exists()){
            file.createNewFile();
            String newLine = System.getProperty("line.separator");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(
                    "&c&lOur Rules:" + newLine +
                            "&61)&f Be Nice" + newLine +
                            "&62)&f Be Respectful" + newLine +
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

    public ItemAlias getItemByAlias(String name){
        for(ItemAlias alias : getAliases()){
            if (alias.getAlias().equalsIgnoreCase(name)){
                return alias;
            }
        }
        return null;
    }

    private void loadItemAliases() throws IOException, InvalidConfigurationException {
        File file = new File(getDataFolder(), "itemaliases.yml");
        if (!file.exists()){
            file.createNewFile();
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            List<String> example = new ArrayList<>();
            example.add("blue");
            example.add("bluewool");
            config.set("wool,11", example);
            config.save(file);
            getCommandyLogger().log(Level.INFO, "Generated default item aliases file!");
        }
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);

        ConfigurationSection section = config.getConfigurationSection("");
        if (section!=null){
            Set<String> keys = section.getKeys(false);
            for(String key : keys){
                String[] k = key.split(",");
                try {

                    if (k.length<=1){
                        throw new Exception();
                    }

                    Material material = Material.valueOf(k[0].toUpperCase());
                    int data = Integer.parseInt(k[1]);

                    List<String> aliases = config.getStringList(key);
                    for(String alias : aliases){
                        if (getItemByAlias(alias)==null){
                            getAliases().add(new ItemAlias(alias, material, (byte) data));
                        }
                    }

                }catch (Exception ex){
                    getCommandyLogger().log(Level.SEVERE, "Skipped " + key + " due to nonexistant item.");
                }
            }
        }

        getCommandyLogger().log(Level.INFO, getLanguageManager().getElement(LanguageEntries.ITEM_ALIASES_LOADED.getKey()).getConvertedColorText()
                .replace("{0}", getAliases().size()+""));

    }

    private void loadKits() throws IOException, InvalidConfigurationException {
        File file = new File(getDataFolder(), "kits.yml");
        if (!file.exists()){
            file.createNewFile();
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            List<String> items = new ArrayList<>();
            items.add("wood_pickaxe");
            items.add("wood_spade");
            items.add("wood_axe");
            items.add("wood_sword");
            items.add("wood,64");
            config.set("kits.Default.items", items);
            config.set("kits.Default.delay", 300);
            config.set("kits.Default.check-permission", false);
            config.save(file);
            getCommandyLogger().log(Level.INFO, "Generated default kits file!");
        }
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);

        getCommandyLogger().log(Level.INFO, " === LOADING KITS === ");

        ConfigurationSection section = config.getConfigurationSection("kits");
        if (section!=null){
            Set<String> names = section.getKeys(false);
            for(String name : names){
                getCommandyLogger().log(Level.INFO, "Loading kit " + name + "...");
                List<String> items = config.getStringList("kits." + name + ".items");
                int cooldown = config.getInt("kits." + name + ".delay");
                getCommandyLogger().log(Level.INFO, "Cooldown: " + cooldown + "s");
                boolean checkPermission = config.getBoolean("kits." + name + ".check-permission");
                getCommandyLogger().log(Level.INFO, "Permission Check: " + checkPermission);
                List<ItemStack> stacks = new ArrayList<>();
                for(String item : items){
                    int amount = 1;
                    byte data = 0;
                    String[] qtySplit = item.split(",");
                    String material;
                    if (qtySplit.length>1) {
                        amount = Integer.parseInt(qtySplit[1]);
                        String[] dSplit = qtySplit[0].split(":");
                        if (dSplit.length>1){
                            data=(byte)Integer.parseInt(dSplit[1]);
                            material=dSplit[0];
                        }
                        else{
                            material=qtySplit[0];
                        }
                    }
                    else{
                        String[] dSplit = item.split(":");
                        if (dSplit.length>1){
                            data=(byte)Integer.parseInt(dSplit[1]);
                            material=dSplit[0];
                        }
                        else{
                            material=item;
                        }

                    }

                    try {
                        Material type = Material.getMaterial(material.toUpperCase());
                        stacks.add(new ItemStack(type, amount, data));
                    }catch (Exception ex){
                        getCommandyLogger().log(Level.SEVERE, "Failed to load \"" + item + "\" in kit \"" + name + "\"! Invalid syntax!");
                    }
                }
                getCommandyLogger().log(Level.INFO, "Item Count: " + stacks.size());
                Kit kit = new Kit(name, (cooldown*1000), checkPermission, stacks);

                ConfigurationSection userSection = config.getConfigurationSection("kits." + name + ".users");
                if (userSection!=null){
                    Set<String> uuids = userSection.getKeys(false);
                    for(String uuid : uuids){
                        long lastUse = config.getLong("kits." + name + ".users." + uuid);
                        kit.getLastUse().put(UUID.fromString(uuid), lastUse);
                    }
                }

                getCommandyLogger().log(Level.INFO, "Loaded kit " + name + "!");
                getKits().add(kit);

            }
        }

        getCommandyLogger().log(Level.INFO, " === KITS LOADED === ");


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

    public List<Kit> getKits() {
        return kits;
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
        {
            KitCommand cmd = new KitCommand(this, "List kits or show them", "commandy.kit", 0, "/kit <Kit>", true);
            PluginCommand c = getCommand("kit");
            c.setExecutor(cmd);
            c.setDescription(cmd.getDescription());
        }
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public CommandyLogger getCommandyLogger() {
        return logger;
    }
}
