package io.sqks.spawncontrol.events;
import io.sqks.spawncontrol.SpawnControl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
  private final SpawnControl plugin = SpawnControl.getPlugin(SpawnControl.class);

  @EventHandler
public void onPlayerJoin(PlayerJoinEvent event) {
      // TODO need to set so this only goes off on player login
      final Player player = event.getPlayer();
      Bukkit.getLogger().info("SpawnControl is moving player: "+player.toString());

      plugin.getLocationHelper().sendPlayerToSpawn(player);
  }
}
