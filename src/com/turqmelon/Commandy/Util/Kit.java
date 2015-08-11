package com.turqmelon.Commandy.Util;


import com.turqmelon.Commandy.Commandy;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Kit {

    private static Commandy plugin;

    public static Commandy getPlugin() {
        return plugin;
    }

    public static void setPlugin(Commandy plugin) {
        Kit.plugin = plugin;
    }

    private String name;
    private long cooldown;
    private boolean checkPermission;
    private List<ItemStack> items;
    private Map<UUID, Long> lastUse = new HashMap<>();

    public Kit(String name, long cooldown, boolean checkPermission, List<ItemStack> items) {
        this.name = name;
        this.cooldown = cooldown;
        this.checkPermission = checkPermission;
        this.items = items;
    }


    public void save() throws IOException, InvalidConfigurationException {

        File file = new File(getPlugin().getDataFolder(), "kits.yml");
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);

        for(UUID uuid : getLastUse().keySet()){
            long last = getLastUse().get(uuid);
            config.set("kits." + getName() + ".users." + uuid.toString(), last);
        }
        config.save(file);

    }

    public String getName() {
        return name;
    }

    public long getCooldown() {
        return cooldown;
    }

    public boolean isCheckPermission() {
        return checkPermission;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public Map<UUID, Long> getLastUse() {
        return lastUse;
    }
}
