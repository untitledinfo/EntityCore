package firepdx.pgc.net.storage;

import firepdx.pgc.net.npc.HologramLine;
import firepdx.pgc.net.npc.Npc;
import firepdx.pgc.net.npc.NpcLocation;
import firepdx.pgc.net.npc.NpcType;
import firepdx.pgc.net.npc.SkinData;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class YamlNpcStorage implements NpcStorage {
    private final JavaPlugin plugin;
    private final File file;

    public YamlNpcStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), plugin.getConfig().getString("storage.file", "npcs.yml"));
    }

    @Override
    public List<Npc> load() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection root = yaml.getConfigurationSection("npcs");
        if (root == null) {
            return List.of();
        }
        List<Npc> result = new ArrayList<>();
        for (String key : root.getKeys(false)) {
            ConfigurationSection section = root.getConfigurationSection(key);
            if (section != null) {
                result.add(readNpc(UUID.fromString(key), section));
            }
        }
        return result;
    }

    @Override
    public void save(Collection<Npc> npcs) {
        YamlConfiguration yaml = new YamlConfiguration();
        ConfigurationSection root = yaml.createSection("npcs");
        for (Npc npc : npcs) {
            writeNpc(root.createSection(npc.id().toString()), npc);
        }
        try {
            yaml.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe("Could not save NPC data: " + exception.getMessage());
        }
    }

    private Npc readNpc(UUID id, ConfigurationSection section) {
        ConfigurationSection loc = section.getConfigurationSection("location");
        NpcLocation location = new NpcLocation(
                loc == null ? "world" : loc.getString("world", "world"),
                loc == null ? 0 : loc.getDouble("x"),
                loc == null ? 0 : loc.getDouble("y"),
                loc == null ? 0 : loc.getDouble("z"),
                loc == null ? 0 : (float) loc.getDouble("yaw"),
                loc == null ? 0 : (float) loc.getDouble("pitch")
        );
        ConfigurationSection skin = section.getConfigurationSection("skin");
        List<String> hologramText = section.getStringList("hologram");
        List<HologramLine> lines = new ArrayList<>();
        for (int i = 0; i < hologramText.size(); i++) {
            lines.add(new HologramLine(i, hologramText.get(i)));
        }
        return new Npc(
                id,
                section.getString("name", "NPC"),
                NpcType.parse(section.getString("type", "PLAYER")),
                location,
                skin == null ? SkinData.empty() : new SkinData(skin.getString("source", ""), skin.getString("value", ""), skin.getString("signature", "")),
                lines,
                Instant.parse(section.getString("updated-at", Instant.now().toString()))
        );
    }

    private void writeNpc(ConfigurationSection section, Npc npc) {
        section.set("name", npc.name());
        section.set("type", npc.type().name());
        section.set("updated-at", npc.updatedAt().toString());
        section.set("location.world", npc.location().world());
        section.set("location.x", npc.location().x());
        section.set("location.y", npc.location().y());
        section.set("location.z", npc.location().z());
        section.set("location.yaw", npc.location().yaw());
        section.set("location.pitch", npc.location().pitch());
        section.set("skin.source", npc.skin().source());
        section.set("skin.value", npc.skin().value());
        section.set("skin.signature", npc.skin().signature());
        section.set("hologram", npc.hologramLines().stream().map(HologramLine::text).toList());
    }
}
