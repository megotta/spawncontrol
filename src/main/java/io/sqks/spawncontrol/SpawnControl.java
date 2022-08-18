package io.sqks.spawncontrol;

import io.sqks.spawncontrol.commands.SpawnControlCommand;
import io.sqks.spawncontrol.events.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class SpawnControl extends JavaPlugin {

    public LocationHelper getLocationHelper() { return new LocationHelper(); }
    private List <ItemStack> items;

    public String description = getDescription().getDescription();
    public String name = getDescription().getName();
    public String version = getDescription().getVersion();
    public String website = getDescription().getWebsite();
    @Override
    public void onEnable() {
        saveDefaultConfig();

        // disable if following checks don't pass
        if(!this.ensureConfigIsPrepared() || !this.getLocationHelper().checkConfigLocations()){
            Bukkit.getLogger().warning("SpawnControl is disabling itself, please check the above errors for details.");
            getServer().getPluginManager().disablePlugin(this);
        }

        if(getConfig().getBoolean("spawn-control-enabled")) {
            Bukkit.getLogger().warning("SpawnControl is disabled, if you thing this is a mistake please check your config.yml file to ensure 'spawn-control'enabled' is set to true");
            getServer().getPluginManager().disablePlugin(this);
        }

        // set commands
        getCommand("spawncontrol").setExecutor(new SpawnControlCommand());

        // register events
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        Bukkit.getLogger().info("SpawnControl has been enabled with the following locations:");
        getLocationHelper().showLocations();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("SpawnControl is being disabled...");
    }

    /**
     * This does a quick check of the config, ensuring paramaters required for the plugin are present
     * @return returns true if everything passes, false if not
     */
    private boolean ensureConfigIsPrepared() {
        Bukkit.getLogger().info("SpawnControl is checking its config file...");
        Bukkit.getLogger().info("");
        Boolean fail = false;
        if (!getConfig().contains("spawn-control-enabled", false)) {
            Bukkit.getLogger().severe("SpawnControl is missing 'spawn-control-enabled' in config.yml.");
            Bukkit.getLogger().info("Please add 'spawn-control-enabled' with the a boolean value of true to SpawnControl's config.");
            fail = true;
        }
        if (!getConfig().contains("start-spawn-locations", false)) {
            Bukkit.getLogger().severe("SpawnControl is missing 'start-spawn-locations' in config.yml.");
            Bukkit.getLogger().info("Please add 'start-spawn-locations' and its list of cords to SpawnControl's config.");
            fail = true;
        }

        if(fail) {
            Bukkit.getLogger().severe("SpawnControl failed, the config is not prepared.");
            Bukkit.getLogger().info("... If you need help, please go to htth://sqks.io/spawncontrol for help.");
            return false;
        }
        Bukkit.getLogger().info("SpawnControl's config file passed preliminary checks.");
        return true;
    }
}
