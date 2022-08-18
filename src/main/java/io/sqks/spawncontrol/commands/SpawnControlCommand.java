package io.sqks.spawncontrol.commands;
import io.sqks.spawncontrol.SpawnControl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnControlCommand implements CommandExecutor {

    private final SpawnControl plugin = JavaPlugin.getPlugin(SpawnControl.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {

        // if '/spawncontrol'
        if(args.length < 1)  {
            this.showWelcome(sender);
            plugin.getLocationHelper().showLocations();
            return true;
        }

        // if '/spawncontrol [args]
        switch (args[0]){
            case "help":
            case "h":
            case "info":
            case "i":
                this.showHelp(sender);
                break;
            case "list":
            case "l":
                this.showList(sender);
                break;
            case "version":
            case "v":
                this.showVersion(sender);
                break;

            default:
                break;

        }
        return true;
    }

    /**
     * Shows the welcome message.
     * @param sender CommandSender
     */
    private void showWelcome(CommandSender sender) {
        sender.sendMessage("Welcome to "+plugin.name+"!" + plugin.description);
        sender.sendMessage(("Use '/spawncontrol help' for usage information."));
    }

    private void showHelp(CommandSender sender){
        sender.sendMessage("/spawncontrol :: Will display welcome message.");
        sender.sendMessage("/spawncontrol help :: Will display usable commands");
        sender.sendMessage("/spawncontrol list :: Will list all locations");
    }

    private void showVersion(CommandSender sender){
        sender.sendMessage("SpawnControl version " + plugin.version);

    }

    private void showList(CommandSender sender){
        sender.sendMessage("Hey!  This list will only show in the console");
        plugin.getLocationHelper().showLocations();
    }
}
