package net.daechler.phantomminer;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;

public class PhantomMiner extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        getLogger().info(ChatColor.GREEN + getName() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + getName() + " has been disabled!");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if the player has the required permission and is in spectator mode
        if (player.hasPermission("phantomminer.use") && player.getGameMode() == GameMode.SPECTATOR) {
            // Check if the action was a left click
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                // Perform a ray trace to find the first entity or block the player is looking at
                RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), Double.MAX_VALUE);

                // Check if the ray trace hit an entity
                if (rayTrace != null && rayTrace.getHitEntity() != null) {
                    // Remove the entity
                    rayTrace.getHitEntity().remove();
                    // Cancel the event so no further processing is done
                    event.setCancelled(true);
                } else {
                    // If no entity was found, check for a block
                    Block targetBlock = player.getTargetBlock(null, Integer.MAX_VALUE);
                    if (targetBlock != null) {
                        // Remove the block without dropping items
                        targetBlock.setType(Material.AIR);
                        // Cancel the event so no further processing is done
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
