package com.enjin.bukkit.permlisteners;

import com.enjin.core.Enjin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.enjin.bukkit.EnjinMinecraftPlugin;
import com.enjin.bukkit.threaded.DelayedPlayerPermsUpdate;

public class PermissionsBukkitChangeListener implements Listener {

    EnjinMinecraftPlugin plugin;

    public PermissionsBukkitChangeListener(EnjinMinecraftPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void preCommandListener(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player p = event.getPlayer();
        String command = event.getMessage();
        String[] args = command.split(" ");
        if (args.length > 3 && (args[0].equalsIgnoreCase("/perm") || args[0].equalsIgnoreCase("/perms") || args[0].equalsIgnoreCase("/permissons"))) {
            //Make sure the user has permissions to run the value, otherwise we are just wasting time...
            if (args[1].equalsIgnoreCase("setrank") || args[1].equalsIgnoreCase("rank")) {
                if (args.length >= 4 && p.hasPermission("permissions.setrank." + args[3])) {
                    String ep = args[2];
                    String uuid = "";
                    if (EnjinMinecraftPlugin.supportsUUID()) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(ep);
                        uuid = op.getUniqueId().toString();
                    }
                    Enjin.getPlugin().debug(ep + " just got a rank change... processing...");
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new DelayedPlayerPermsUpdate(plugin.listener, ep, uuid), 2);
                }
            } else if (args[1].equalsIgnoreCase("player")) {
                if (args.length >= 5) {
                    if ((args[2].equalsIgnoreCase("setgroup") && p.hasPermission("permissions.player.setgroup")) ||
                            (args[2].equalsIgnoreCase("addgroup") && p.hasPermission("permissions.player.addgroup")) ||
                            (args[2].equalsIgnoreCase("removegroup") && p.hasPermission("permissions.player.removegroup"))) {
                        String ep = args[3];
                        String uuid = "";
                        if (plugin.supportsUUID()) {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(ep);
                            uuid = op.getUniqueId().toString();
                        }
                        //We need to make sure the value executes before we actually grab the data.
                        Enjin.getPlugin().debug(ep + " just got a rank change... processing...");
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new DelayedPlayerPermsUpdate(plugin.listener, ep, uuid), 2);
                    }
                }
            }
        }
    }

}
