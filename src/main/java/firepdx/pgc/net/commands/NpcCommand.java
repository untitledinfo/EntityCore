package firepdx.pgc.net.commands;

import firepdx.pgc.net.EntityCore;
import firepdx.pgc.net.npc.Npc;
import firepdx.pgc.net.npc.NpcManager;
import firepdx.pgc.net.npc.NpcLocation;
import firepdx.pgc.net.npc.NpcType;
import firepdx.pgc.net.npc.SkinData;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public final class NpcCommand implements CommandExecutor, TabCompleter {
    private static final List<String> SUBCOMMANDS = List.of(
            "create", "remove", "delete", "rename", "skin", "hologram", "edit", "move", "tp", "teleport",
            "list", "clone", "copy", "paste", "look", "rotate", "animation", "command", "gui", "inventory",
            "mount", "passenger", "sit", "stand", "follow", "patrol", "guard", "attack", "interact",
            "save", "reload", "debug", "export", "import", "help"
    );

    private final EntityCore plugin;
    private final NpcManager npcManager;

    public NpcCommand(EntityCore plugin, NpcManager npcManager) {
        this.plugin = plugin;
        this.npcManager = npcManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            return help(sender);
        }
        return switch (args[0].toLowerCase(Locale.ROOT)) {
            case "create" -> create(sender, args);
            case "remove", "delete" -> delete(sender, args);
            case "rename" -> rename(sender, args);
            case "list" -> list(sender);
            case "move" -> move(sender, args);
            case "tp", "teleport" -> teleport(sender, args);
            case "skin" -> skin(sender, args);
            case "hologram" -> hologram(sender, args);
            case "save" -> save(sender);
            case "reload" -> reload(sender);
            case "debug" -> debug(sender);
            case "export", "import", "clone", "copy", "paste", "look", "rotate", "animation", "command", "gui",
                 "inventory", "mount", "passenger", "sit", "stand", "follow", "patrol", "guard", "attack",
                 "interact", "edit" -> planned(sender, args[0]);
            default -> help(sender);
        };
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return SUBCOMMANDS.stream().filter(value -> value.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
        }
        if (args.length == 2 && List.of("delete", "remove", "rename", "skin", "hologram", "move", "tp", "teleport").contains(args[0].toLowerCase(Locale.ROOT))) {
            return npcManager.getAllNPCs().stream().map(Npc::name).filter(name -> name.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))).toList();
        }
        return List.of();
    }

    private boolean create(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return fail(sender, "Only players can create NPCs at their current location.");
        }
        if (!sender.hasPermission("entitycore.create")) {
            return fail(sender, "You do not have permission to create NPCs.");
        }
        if (args.length < 2) {
            return fail(sender, "Usage: /npc create <name> [type]");
        }
        NpcType type;
        try {
            type = args.length >= 3 ? NpcType.parse(args[2]) : NpcType.PLAYER;
        } catch (IllegalArgumentException exception) {
            return fail(sender, "Unknown NPC type. Try PLAYER, VILLAGER, ZOMBIE, SKELETON, PIGLIN, or ARMOR_STAND.");
        }
        Npc npc = npcManager.createNPC(args[1], type, player.getLocation());
        return ok(sender, "Created NPC " + npc.name() + " (" + shortId(npc.id()) + ").");
    }

    private boolean delete(CommandSender sender, String[] args) {
        if (!sender.hasPermission("entitycore.delete")) {
            return fail(sender, "You do not have permission to delete NPCs.");
        }
        Optional<Npc> npc = resolve(args, 1);
        if (npc.isEmpty()) {
            return fail(sender, "Usage: /npc delete <name|uuid>");
        }
        npcManager.removeNPC(npc.get().id());
        return ok(sender, "Deleted NPC " + npc.get().name() + ".");
    }

    private boolean rename(CommandSender sender, String[] args) {
        if (!sender.hasPermission("entitycore.edit")) {
            return fail(sender, "You do not have permission to edit NPCs.");
        }
        Optional<Npc> npc = resolve(args, 1);
        if (npc.isEmpty() || args.length < 3) {
            return fail(sender, "Usage: /npc rename <name|uuid> <newName>");
        }
        npc.get().rename(args[2]);
        npcManager.save();
        return ok(sender, "Renamed NPC to " + args[2] + ".");
    }

    private boolean list(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "EntityCore NPCs:");
        for (Npc npc : npcManager.getAllNPCs()) {
            sender.sendMessage(ChatColor.YELLOW + "- " + npc.name() + ChatColor.GRAY + " " + npc.type() + " " + shortId(npc.id()));
        }
        return true;
    }

    private boolean move(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return fail(sender, "Only players can move NPCs to their current location.");
        }
        Optional<Npc> npc = resolve(args, 1);
        if (npc.isEmpty()) {
            return fail(sender, "Usage: /npc move <name|uuid>");
        }
        npc.get().move(NpcLocation.from(player.getLocation()));
        npcManager.save();
        npcManager.spawn(npc.get());
        return ok(sender, "Moved NPC " + npc.get().name() + ".");
    }

    private boolean teleport(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return fail(sender, "Only players can teleport to NPCs.");
        }
        Optional<Npc> npc = resolve(args, 1);
        if (npc.isEmpty()) {
            return fail(sender, "Usage: /npc tp <name|uuid>");
        }
        player.teleport(npc.get().location().toBukkit());
        return ok(sender, "Teleported to " + npc.get().name() + ".");
    }

    private boolean skin(CommandSender sender, String[] args) {
        if (!sender.hasPermission("entitycore.skin")) {
            return fail(sender, "You do not have permission to edit NPC skins.");
        }
        Optional<Npc> npc = resolve(args, 1);
        if (npc.isEmpty() || args.length < 3) {
            return fail(sender, "Usage: /npc skin <name|uuid> <username|texture>");
        }
        npc.get().skin(SkinData.username(args[2]));
        npcManager.save();
        return ok(sender, "Updated skin for " + npc.get().name() + ".");
    }

    private boolean hologram(CommandSender sender, String[] args) {
        if (!sender.hasPermission("entitycore.hologram")) {
            return fail(sender, "You do not have permission to edit NPC holograms.");
        }
        Optional<Npc> npc = resolve(args, 1);
        if (npc.isEmpty() || args.length < 3) {
            return fail(sender, "Usage: /npc hologram <name|uuid> <line text...>");
        }
        npc.get().setHologramLines(List.of(String.join(" ", Arrays.copyOfRange(args, 2, args.length))));
        npcManager.save();
        return ok(sender, "Updated hologram for " + npc.get().name() + ".");
    }

    private boolean save(CommandSender sender) {
        npcManager.save();
        return ok(sender, "Saved NPC data.");
    }

    private boolean reload(CommandSender sender) {
        if (!sender.hasPermission("entitycore.reload")) {
            return fail(sender, "You do not have permission to reload EntityCore.");
        }
        plugin.reloadConfig();
        npcManager.load();
        return ok(sender, "Reloaded EntityCore.");
    }

    private boolean debug(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "EntityCore debug");
        sender.sendMessage(ChatColor.YELLOW + "NPCs: " + npcManager.getAllNPCs().size());
        sender.sendMessage(ChatColor.YELLOW + "Integrations: " + plugin.integrationRegistry().enabled());
        return true;
    }

    private boolean planned(CommandSender sender, String feature) {
        return fail(sender, "The /npc " + feature + " command is registered for API compatibility and needs its module implementation.");
    }

    private boolean help(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "EntityCore /npc commands");
        sender.sendMessage(ChatColor.YELLOW + "/npc create <name> [type]");
        sender.sendMessage(ChatColor.YELLOW + "/npc delete <name|uuid>");
        sender.sendMessage(ChatColor.YELLOW + "/npc rename <name|uuid> <newName>");
        sender.sendMessage(ChatColor.YELLOW + "/npc list");
        sender.sendMessage(ChatColor.YELLOW + "/npc move <name|uuid>");
        sender.sendMessage(ChatColor.YELLOW + "/npc tp <name|uuid>");
        sender.sendMessage(ChatColor.YELLOW + "/npc skin <name|uuid> <username>");
        sender.sendMessage(ChatColor.YELLOW + "/npc hologram <name|uuid> <text...>");
        return true;
    }

    private Optional<Npc> resolve(String[] args, int index) {
        if (args.length <= index) {
            return Optional.empty();
        }
        try {
            return npcManager.getNPC(UUID.fromString(args[index]));
        } catch (IllegalArgumentException ignored) {
            return npcManager.findByName(args[index]);
        }
    }

    private boolean ok(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.GREEN + message);
        return true;
    }

    private boolean fail(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.RED + message);
        return true;
    }

    private String shortId(UUID id) {
        return id.toString().substring(0, 8);
    }
}
