package com.enjin.officialplugin.packets;

import java.io.BufferedInputStream;

import org.bukkit.Bukkit;

import com.enjin.officialplugin.EnjinMinecraftPlugin;

/**
 * 
 * @author OverCaste (Enjin LTE PTD).
 * This software is released under an Open Source license.
 * @copyright Enjin 2012.
 * 
 */

public class Packet12ExecuteCommand {

	public static void handle(BufferedInputStream in, EnjinMinecraftPlugin plugin) {
		try {
			String command = PacketUtilities.readString(in);
			String[] commandsplit = command.split("\0");
			if(commandsplit.length > 1) {
				try {
					long time = System.currentTimeMillis() + (Long.parseLong(commandsplit[1]) * 1000);
					EnjinMinecraftPlugin.debug("Executing command \"" + command + "\" as console in " + commandsplit[1] + " seconds.");
					plugin.commexecuter.addCommand(Bukkit.getConsoleSender(), commandsplit[0], time);
				}catch (NumberFormatException e) {
					EnjinMinecraftPlugin.debug("Failed to get the time on a timed command, adding as a regular command");
					plugin.commandqueue.addCommand(Bukkit.getConsoleSender(), commandsplit[0]);
				}
			}else {
				EnjinMinecraftPlugin.debug("Executing command \"" + command + "\" as console.");
				plugin.commandqueue.addCommand(Bukkit.getConsoleSender(), command);
			}
			//Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new CommandExecuter(Bukkit.getConsoleSender(), command));
			//Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PacketUtilities.readString(in));
		} catch (Throwable t) {
			Bukkit.getLogger().warning("Failed to dispatch command via 0x12, " + t.getMessage());
			t.printStackTrace();
		}
	}
}