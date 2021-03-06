package com.enjin.bukkit.listeners;

import com.enjin.bukkit.EnjinMinecraftPlugin;
import com.enjin.bukkit.modules.impl.SignStatsModule;
import com.enjin.bukkit.statsigns.SignData;
import com.enjin.bukkit.statsigns.SignType;
import com.enjin.bukkit.util.PermissionsUtil;
import com.enjin.bukkit.util.serialization.SerializableLocation;
import com.google.common.base.Optional;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.util.ArrayList;

public class SignListener implements Listener {
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        SignStatsModule module = EnjinMinecraftPlugin.getInstance().getModuleManager().getModule(SignStatsModule.class);
        if (module != null) {
            String line = event.getLine(0);
            for (SignType type : SignType.values()) {
                Optional<Integer> index   = type.matches(line);
                SignType.SubType  subType = null;
                Optional<Integer> itemId  = Optional.absent();

                if (index.isPresent()) {
                    if (PermissionsUtil.hasPermission(event.getPlayer(), "enjin.sign.set")) {
                        if (!type.getSupportedSubTypes().isEmpty()) {
                            String line2 = event.getLine(1);

                            if (line2 != null && !line2.isEmpty()) {
                                switch (line2.toLowerCase()) {
                                    case "day":
                                        subType = SignType.SubType.DAY;
                                        break;
                                    case "week":
                                        subType = SignType.SubType.WEEK;
                                        break;
                                    case "month":
                                        subType = SignType.SubType.MONTH;
                                        break;
                                    case "total":
                                        subType = SignType.SubType.TOTAL;
                                        break;
                                    default:
                                        subType = SignType.SubType.ITEMID;
                                        break;
                                }

                                if (!type.getSupportedSubTypes().contains(subType)) {
                                    subType = type.getDefaultSubType();
                                }

                                if (subType == SignType.SubType.ITEMID) {
                                    try {
                                        itemId = Optional.fromNullable(Integer.parseInt(line2));
                                    } catch (NumberFormatException e) {
                                        itemId = Optional.absent();
                                    }
                                }
                            }
                        }

                        if (itemId.isPresent()) {
                            module.add(new SignData(event.getBlock(), type, subType, itemId.get(), index.get()));
                        } else {
                            module.add(new SignData(event.getBlock(), type, subType, index.get()));
                        }

                        return;
                    } else {
                        event.getPlayer()
                             .sendMessage(ChatColor.RED + "You do not have permission to set Enjin stat signs.");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Sign) {
            SignStatsModule module = EnjinMinecraftPlugin.getInstance()
                                                         .getModuleManager()
                                                         .getModule(SignStatsModule.class);
            if (module != null) {
                SerializableLocation location = new SerializableLocation(event.getBlock().getLocation());

                for (SignData data : new ArrayList<>(module.getConfig().getSigns())) {
                    if (data.getLocation().equals(location)) {
                        if (!PermissionsUtil.hasPermission(event.getPlayer(), "enjin.sign.remove")) {
                            event.getPlayer()
                                 .sendMessage(ChatColor.RED + "You do not have permission to remove Enjin stat signs.");
                            event.setCancelled(true);
                        } else {
                            module.remove(location);
                        }

                        return;
                    }
                }
            }
        }
    }
}
