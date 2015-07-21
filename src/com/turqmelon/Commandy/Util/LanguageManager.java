package com.turqmelon.Commandy.Util;

import com.turqmelon.Commandy.Commandy;
import com.turqmelon.Commandy.Exception.CommandyLanguageException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class LanguageManager {

    private Commandy plugin;
    private LanguageElement defaultElement;
    private List<LanguageElement> elementList = new ArrayList<>();
    private YamlConfiguration languageFile = null;
    private File langFile = null;

    public LanguageManager(Commandy plugin) {
        this.plugin=plugin;
        this.defaultElement = new LanguageElement("no_element_result", "&4No language element could be found for that key.");
    }

    public YamlConfiguration getLanguageFile() {
        return languageFile;
    }

    public void updateEntries(){

        int updated = 0;

        for(LanguageEntries entries : LanguageEntries.values()){
            if (getElement(entries.name()).getKey().equals(getDefaultElement().getKey())){
                getElementList().add(new LanguageElement(entries.name().toLowerCase(), entries.getMessage()));
                getLanguageFile().set(entries.name().toLowerCase(), entries.getMessage());
                updated++;
            }
        }

        if (updated>0){
            try {
                getLanguageFile().save(getLangFile());
                getPlugin().getCommandyLogger().log(Level.INFO, getElement("language_entries_updated").getText().replace("{0}", updated+""));
            } catch (IOException e) {
                getPlugin().getCommandyLogger().log(Level.SEVERE, "Failed to save updated entries!");
                e.printStackTrace();
            }
        }


    }

    public void loadEntries() throws CommandyLanguageException {
        if (getLanguageFile()==null){
            throw new CommandyLanguageException(getPlugin(), 1, "Failed to load entries: No language file loaded.");
        }
        ConfigurationSection section = getLanguageFile().getConfigurationSection("");
        if (section==null){
            return;
        }
        Set<String> entries = section.getKeys(false);
        if (entries.size()==0){
            return;
        }
        for(String key : entries){
            String msg = getLanguageFile().getString(key);
            getElementList().add(new LanguageElement(key, msg));
        }
        getPlugin().getCommandyLogger().log(Level.INFO, getElement("language_entries_loaded").getText().replace("{0}", getElementList().size()+""));

    }

    public void loadLanguageFile(File file) throws IOException, InvalidConfigurationException {

        YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        getElementList().clear();
        this.languageFile=config;
        this.langFile=file;

    }

    public File getLangFile() {
        return langFile;
    }

    public LanguageElement getElement(String key){
        for(LanguageElement element : getElementList()){
            if (element.getKey().equalsIgnoreCase(key)){
                return element;
            }
        }
        return getDefaultElement();
    }

    public LanguageElement getDefaultElement() {
        return defaultElement;
    }

    public List<LanguageElement> getElementList() {
        return elementList;
    }

    public Commandy getPlugin() {
        return plugin;
    }
}
