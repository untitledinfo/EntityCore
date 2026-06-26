package firepdx.pgc.net;

import firepdx.pgc.net.api.EntityCoreAPI;
import firepdx.pgc.net.commands.NpcCommand;
import firepdx.pgc.net.integrations.IntegrationRegistry;
import firepdx.pgc.net.npc.NpcManager;
import firepdx.pgc.net.scheduler.PlatformScheduler;
import firepdx.pgc.net.storage.YamlNpcStorage;
import java.util.Objects;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class EntityCore extends JavaPlugin {
    private NpcManager npcManager;
    private PlatformScheduler scheduler;
    private IntegrationRegistry integrationRegistry;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("npcs.yml", false);

        scheduler = new PlatformScheduler(this);
        integrationRegistry = new IntegrationRegistry(this);
        npcManager = new NpcManager(new YamlNpcStorage(this), scheduler);
        npcManager.load();
        integrationRegistry.detect();

        EntityCoreAPI.initialize(npcManager, integrationRegistry);
        registerCommands();

        getLogger().info("EntityCore enabled with " + npcManager.getAllNPCs().size() + " NPC(s).");
    }

    @Override
    public void onDisable() {
        if (npcManager != null) {
            npcManager.save();
            npcManager.despawnAll();
        }
        EntityCoreAPI.shutdown();
    }

    public NpcManager npcManager() {
        return npcManager;
    }

    public PlatformScheduler scheduler() {
        return scheduler;
    }

    public IntegrationRegistry integrationRegistry() {
        return integrationRegistry;
    }

    private void registerCommands() {
        PluginCommand command = Objects.requireNonNull(getCommand("npc"), "npc command missing from plugin.yml");
        NpcCommand npcCommand = new NpcCommand(this, npcManager);
        command.setExecutor(npcCommand);
        command.setTabCompleter(npcCommand);
    }
}
