package io.sqks.spawncontrol;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocationHelper {

    private final SpawnControl plugin = JavaPlugin.getPlugin(SpawnControl.class);
    public List<Location> locations = new ArrayList<Location>();
    public List<List> configLoc = (List<List>) plugin.getConfig().getList("start-spawn-locations");

    /**
     * Returns all locations that are set.
     * If locations hasn't been sent, then retrieve from config.
     * @return returns a list of locations
     */
    public List<Location> getLocations(){
        if(this.locations.size() < 1) {
            this.setLocationsFromConfig();
        }
        return this.locations;
    }

    /**
     * sets provided location to locations list.
     * @param loc Location
     */
    public void setLocation(Location loc) {
        this.locations.add(loc);
    }

    /**
     * Retrieves location from config, does a check and sets them.
     */
    public void setLocationsFromConfig() {
        if((checkLocations(configLoc))){
            Bukkit.getLogger().info(configLoc.toString());
            for(List loc : configLoc){
                World newWorld = plugin.getServer().getWorld(loc.get(0).toString());
                Location newLoc = new Location(
                        newWorld,
                        Double.parseDouble(loc.get(1).toString()),
                        Double.parseDouble(loc.get(2).toString()),
                        Double.parseDouble(loc.get(3).toString()));
                setLocation(newLoc);
            }
        }
    }

    /**
     * checks the list of locations provided in the config
     * @param configLoc list of locations
     * @return boolean value if, true if all locations are good to go
     */
    public boolean checkLocations(List<List> configLoc) {
        Boolean bad = false;
        if(configLoc.size() < 1) {
            Bukkit.getLogger().severe("SpawnControl's config for 'start-spawn-locations' seems to be empty.");
            bad = true;
        } else {
            for (List location : configLoc) {
                if (!checkLocation(location)) {
                    bad = true;
                }
            }
        }
        if(bad) {
            Bukkit.getLogger().severe("SpawnControl config file is incorrect!");
            Bukkit.getLogger().warning("Please check and correct your configs for 'start-spawn-locations'");
            return false;
        }
        Bukkit.getLogger().info("SpawnControl has successfully checked the locations");
        return true;
    }

    /**
     * Will check provided location and return true if valid
     * @param loc Location to check
     * @return true if all checks pass. False if not.
     */
    public boolean checkLocation(List loc) {
        Bukkit.getLogger().info("CHECKING LOCATION: "+loc.toString());
        if(loc.size() != 4){
            if(loc.size() < 1){
                Bukkit.getLogger().severe("SpawnControl cannot find any locations set in config.");
                return false;
            }
            if(loc.size() != 4 ){
                Bukkit.getLogger().severe("SpawnControl config is incorrect for "+loc.toString()+".");
                return false;
            }
            Bukkit.getLogger().severe("Unresolved error for location line: "+loc.toString()+".");
            return false;
        }
        if(!(loc.get(0) instanceof String)) {
            Bukkit.getLogger().severe("SpawnControl location world name must be string: "+loc.get(0)+".");
            return false;
        }
        if(!(loc.get(1) instanceof Number)) {
            Bukkit.getLogger().severe("SpawnControl x location must be an integer: "+loc.get(1)+".");
            return false;
        }
        if(!(loc.get(2) instanceof Number)) {
            Bukkit.getLogger().severe("SpawnControl y location must be an integer: "+loc.get(2)+".");
            return false;
        }
        if(!(loc.get(3) instanceof Number)) {
            Bukkit.getLogger().severe("SpawnControl z location must be an integer: "+loc.get(3)+".");
            return false;
        }
        Bukkit.getLogger().info("SpawnControl location '"+loc+"' should be good to go.");
        return true;
    }

    /**
     * will print out all locations to the getlogger
     */
    public void showLocations(){
        for(Location location : getLocations() ){
            String formatMsg = "'"+location.getWorld()+"': [ x:"+location.getX()+" y:"+ location.getY()+" z:"+ location.getZ()+" ]";
            Bukkit.getLogger().info(formatMsg);
        }
    }

    /**
     * this will send a player to a random location
     * @param p the player being transported
     */
    public void sendPlayerToSpawn(Player p){
        Location spawn = this.getLocations().get(new Random().nextInt(0,this.getLocations().size()));
        PaperLib.teleportAsync(p, spawn).thenAccept(result -> {
            if (result) {
                Bukkit.getLogger().info("SpawnControl has moved and started " + p.getName() + " at " + spawn);
            }
        });

    }

    /**
     * Checks locations in config file.
     * @return result from checkLocation(configLocations)
     */
    public boolean checkConfigLocations() {
        List<List> configLoc = (List<List>) plugin.getConfig().getList("start-spawn-locations");
        return checkLocations(configLoc);
    }
}
