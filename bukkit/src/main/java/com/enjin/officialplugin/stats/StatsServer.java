package com.enjin.officialplugin.stats;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.json.simple.JSONObject;

import com.enjin.officialplugin.EnjinMinecraftPlugin;

public class StatsServer {

    EnjinMinecraftPlugin plugin;

    long lastserverstarttime = System.currentTimeMillis();
    int totalkicks = 0;
    ConcurrentHashMap<String, Integer> playerkicks = new ConcurrentHashMap<String, Integer>();
    int creeperexplosions = 0;

    public StatsServer(EnjinMinecraftPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public StatsServer(EnjinMinecraftPlugin plugin, JSONObject serverstats) {
        this.plugin = plugin;
        totalkicks = StatsUtils.getInt(serverstats.get("totalkicks"));
        creeperexplosions = StatsUtils.getInt(serverstats.get("creeperexplosions"));
        Object okicks = serverstats.get("playerskickedlist");
        if (okicks instanceof JSONObject) {
            JSONObject kicks = (JSONObject) okicks;
            Set<Map.Entry> skicks = kicks.entrySet();
            for (Map.Entry kick : skicks) {
                playerkicks.put(kick.getKey().toString(), StatsUtils.getInt(kick.getValue()));
            }
        }
    }

    public long getLastserverstarttime() {
        return lastserverstarttime;
    }


    public void setLastserverstarttime(long lastserverstarttime) {
        this.lastserverstarttime = lastserverstarttime;
    }


    public int getTotalkicks() {
        return totalkicks;
    }

    public void addKick(String playername) {
        totalkicks++;
        int playerkick = 0;
        if (playerkicks.containsKey(playername)) {
            playerkick = playerkicks.get(playername);
        }
        playerkick++;
        playerkicks.put(playername, new Integer(playerkick));
    }


    public void setTotalkicks(int totalkicks) {
        this.totalkicks = totalkicks;
    }


    public int getCreeperexplosions() {
        return creeperexplosions;
    }

    public void addCreeperExplosion() {
        creeperexplosions++;
    }

    public void setCreeperexplosions(int creeperexplosions) {
        this.creeperexplosions = creeperexplosions;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getSerialized() {
        JSONObject serverbuilder = new JSONObject();
        serverbuilder.put("creeperexplosions", new Integer(creeperexplosions));
        int totalentities = 0;
        try {
            List<World> worlds = Bukkit.getWorlds();
            for (World world : worlds) {
                totalentities += world.getEntities().size();
            }
        } catch (Exception e) {

        }
        serverbuilder.put("totalentities", new Integer(totalentities));
        Runtime runtime = Runtime.getRuntime();
        long memused = runtime.totalMemory() / (1024 * 1024);
        long maxmemory = runtime.maxMemory() / (1024 * 1024);
        JSONObject serverinfo = new JSONObject();
        serverinfo.put("maxmemory", new Integer((int) maxmemory));
        serverinfo.put("memoryused", new Integer((int) memused));
        serverinfo.put("javaversion", System.getProperty("java.version") + " " + System.getProperty("java.vendor"));
        serverinfo.put("os", System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
        serverinfo.put("corecount", new Integer(runtime.availableProcessors()));
        serverinfo.put("serverversion", plugin.getServer().getVersion());
        serverinfo.put("laststarttime", new Integer((int) (lastserverstarttime / 1000)));
        JSONObject kickedplayers = new JSONObject();
        Set<Entry<String, Integer>> kicks = playerkicks.entrySet();
        for (Entry<String, Integer> kick : kicks) {
            kickedplayers.put(kick.getKey(), kick.getValue());
        }
        serverinfo.put("playerskickedlist", kickedplayers);
        serverinfo.put("totalkicks", new Integer(totalkicks));
        return serverinfo;
    }

    public ConcurrentHashMap<String, Integer> getPlayerkicks() {
        return playerkicks;
    }

    public void reset() {
        totalkicks = 0;
        playerkicks.clear();
        creeperexplosions = 0;
    }
}