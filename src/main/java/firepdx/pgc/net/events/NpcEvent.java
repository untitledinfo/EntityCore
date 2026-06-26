package firepdx.pgc.net.events;

import firepdx.pgc.net.npc.Npc;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class NpcEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Npc npc;

    protected NpcEvent(Npc npc) {
        this.npc = npc;
    }

    public Npc npc() {
        return npc;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
