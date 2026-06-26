package firepdx.pgc.net.packets;

import firepdx.pgc.net.npc.Npc;

public interface NpcRenderer {
    void spawn(Npc npc);

    void destroy(Npc npc);

    void update(Npc npc);
}
