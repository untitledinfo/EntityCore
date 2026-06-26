package firepdx.pgc.net.npc;

public enum NpcType {
    PLAYER,
    VILLAGER,
    ZOMBIE,
    SKELETON,
    PIGLIN,
    ARMOR_STAND,
    DISPLAY,
    ITEM_DISPLAY,
    TEXT_DISPLAY,
    BLOCK_DISPLAY,
    VANILLA_MOB;

    public static NpcType parse(String value) {
        return NpcType.valueOf(value.toUpperCase().replace('-', '_'));
    }
}
