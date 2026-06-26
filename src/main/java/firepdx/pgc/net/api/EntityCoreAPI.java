package firepdx.pgc.net.api;

import firepdx.pgc.net.integrations.IntegrationRegistry;
import firepdx.pgc.net.npc.Npc;
import firepdx.pgc.net.npc.NpcManager;
import firepdx.pgc.net.npc.NpcType;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;

public final class EntityCoreAPI {
    private static NpcManager npcManager;
    private static IntegrationRegistry integrations;

    private EntityCoreAPI() {
    }

    public static void initialize(NpcManager manager, IntegrationRegistry registry) {
        npcManager = manager;
        integrations = registry;
    }

    public static void shutdown() {
        npcManager = null;
        integrations = null;
    }

    public static Npc createNPC(String name, NpcType type, Location location) {
        return manager().createNPC(name, type, location);
    }

    public static boolean removeNPC(UUID id) {
        return manager().removeNPC(id);
    }

    public static Optional<Npc> getNPC(UUID id) {
        return manager().getNPC(id);
    }

    public static Collection<Npc> getAllNPCs() {
        return manager().getAllNPCs();
    }

    public static void registerTrait(String key, Object traitFactory) {
        manager().extensionRegistry().register("trait", key, traitFactory);
    }

    public static void registerAnimation(String key, Object animation) {
        manager().extensionRegistry().register("animation", key, animation);
    }

    public static void registerPlaceholder(String key, Object placeholder) {
        manager().extensionRegistry().register("placeholder", key, placeholder);
    }

    public static void registerCommand(String key, Object command) {
        manager().extensionRegistry().register("command", key, command);
    }

    public static void registerListener(String key, Object listener) {
        manager().extensionRegistry().register("listener", key, listener);
    }

    public static IntegrationRegistry integrations() {
        if (integrations == null) {
            throw new IllegalStateException("EntityCoreAPI is not initialized");
        }
        return integrations;
    }

    private static NpcManager manager() {
        if (npcManager == null) {
            throw new IllegalStateException("EntityCoreAPI is not initialized");
        }
        return npcManager;
    }
}
