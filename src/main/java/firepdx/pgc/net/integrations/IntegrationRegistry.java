package firepdx.pgc.net.integrations;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class IntegrationRegistry {
    private static final String[] KNOWN = {
            "PlaceholderAPI", "LuckPerms", "Vault", "ProtocolLib", "DecentHolograms", "HolographicDisplays",
            "WorldGuard", "WorldEdit", "Essentials", "Multiverse-Core", "CMI", "MythicMobs", "ModelEngine",
            "ItemsAdder", "Oraxen", "Citizens", "DiscordSRV", "Geyser-Spigot", "floodgate", "ViaVersion"
    };

    private final Plugin plugin;
    private final Set<String> enabled = new LinkedHashSet<>();

    public IntegrationRegistry(Plugin plugin) {
        this.plugin = plugin;
    }

    public void detect() {
        enabled.clear();
        for (String name : KNOWN) {
            if (Bukkit.getPluginManager().getPlugin(name) != null) {
                enabled.add(name);
            }
        }
        plugin.getLogger().info("Detected integrations: " + (enabled.isEmpty() ? "none" : String.join(", ", enabled)));
    }

    public boolean isEnabled(String pluginName) {
        return enabled.contains(pluginName);
    }

    public Set<String> enabled() {
        return Collections.unmodifiableSet(enabled);
    }
}
