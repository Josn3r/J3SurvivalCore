package store.j3studios.plugin.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import store.j3studios.plugin.SCore;

public class Config {
	
    private FileConfiguration config;
    private File file;
    private SCore main;

    public Config(SCore main, String resourceName) {
    	this.main = main;
        this.file = new File(this.main.getDataFolder(), resourceName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
        InputStream readConfig = this.main.getResource(resourceName + ".yml");
        YamlConfiguration setDefaults = YamlConfiguration.loadConfiguration(new InputStreamReader(readConfig));
        try {
            if (!this.file.exists()) {
            	this.config.addDefaults(setDefaults);
                this.config.options().copyDefaults(true);
                this.config.save(this.file);
            } else {
            	this.config.load(this.file);
            }
        }
        catch (IOException | InvalidConfigurationException exception) {
        }
    }

    public Config() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void sDefault(String path, String value) {
        if (!this.config.contains(path)) {
            this.config.set(path, (Object)value);
            this.save();
        }
    }

    public void save() {
        try {
            this.config.save(this.file);
        }
        catch (IOException exception) {
        }
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public File getFile() {
        return this.file;
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
        this.save();
    }

	public FileConfigurationOptions options() {
		return config.options();
	}

	public boolean isSet(String string) {
		return this.config.isSet(string);
	}
	
	//
	
	public String get(String path) {
        return config.getString(path);
    }

    public int getint(String path) {
        return this.config.getInt(path);
    }

    
}

