package firepdx.pgc.net.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public final class PlatformScheduler {
    private final Plugin plugin;

    public PlatformScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    public void runGlobalTask(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }

    public void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    public void runEntityTask(Location location, Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }
}
