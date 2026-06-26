package firepdx.pgc.net.placeholders;

import firepdx.pgc.net.npc.Npc;

public final class EntityCorePlaceholders {
    private EntityCorePlaceholders() {
    }

    public static String resolve(Npc npc, String placeholder) {
        return switch (placeholder.toLowerCase()) {
            case "entitycore_name" -> npc.name();
            case "entitycore_id" -> npc.id().toString();
            case "entitycore_skin" -> npc.skin().value();
            case "entitycore_world" -> npc.location().world();
            default -> "";
        };
    }
}
