package firepdx.pgc.net.npc;

import firepdx.pgc.net.extensions.ExtensionRegistry;
import firepdx.pgc.net.scheduler.PlatformScheduler;
import firepdx.pgc.net.storage.NpcStorage;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;

public final class NpcManager {
    private final NpcStorage storage;
    private final PlatformScheduler scheduler;
    private final ExtensionRegistry extensionRegistry = new ExtensionRegistry();
    private final Map<UUID, Npc> npcs = new LinkedHashMap<>();

    public NpcManager(NpcStorage storage, PlatformScheduler scheduler) {
        this.storage = storage;
        this.scheduler = scheduler;
    }

    public void load() {
        npcs.clear();
        storage.load().forEach(npc -> npcs.put(npc.id(), npc));
    }

    public void save() {
        storage.save(npcs.values());
    }

    public Npc createNPC(String name, NpcType type, Location location) {
        Npc npc = new Npc(UUID.randomUUID(), name, type, NpcLocation.from(location), SkinData.empty(), java.util.List.of(), java.time.Instant.now());
        npcs.put(npc.id(), npc);
        save();
        spawn(npc);
        return npc;
    }

    public boolean removeNPC(UUID id) {
        Npc removed = npcs.remove(id);
        if (removed == null) {
            return false;
        }
        despawn(removed);
        save();
        return true;
    }

    public Optional<Npc> getNPC(UUID id) {
        return Optional.ofNullable(npcs.get(id));
    }

    public Optional<Npc> findByName(String name) {
        return npcs.values().stream().filter(npc -> npc.name().equalsIgnoreCase(name)).findFirst();
    }

    public Collection<Npc> getAllNPCs() {
        return npcs.values().stream().sorted(Comparator.comparing(Npc::name, String.CASE_INSENSITIVE_ORDER)).toList();
    }

    public void spawn(Npc npc) {
        scheduler.runEntityTask(npc.location().toBukkit(), () -> {
            // Packet/NMS adapters attach here. The persisted model is intentionally platform neutral.
        });
    }

    public void despawn(Npc npc) {
        scheduler.runGlobalTask(() -> {
            // Packet/NMS adapters detach here.
        });
    }

    public void despawnAll() {
        npcs.values().forEach(this::despawn);
    }

    public ExtensionRegistry extensionRegistry() {
        return extensionRegistry;
    }
}
