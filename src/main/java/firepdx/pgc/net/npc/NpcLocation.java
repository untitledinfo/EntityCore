package firepdx.pgc.net.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public record NpcLocation(String world, double x, double y, double z, float yaw, float pitch) {
    public static NpcLocation from(Location location) {
        String worldName = location.getWorld() == null ? "world" : location.getWorld().getName();
        return new NpcLocation(worldName, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public Location toBukkit() {
        World bukkitWorld = Bukkit.getWorld(world);
        return new Location(bukkitWorld, x, y, z, yaw, pitch);
    }
}
