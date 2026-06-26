package firepdx.pgc.net.holograms;

import firepdx.pgc.net.npc.Npc;

public interface HologramService {
    void render(Npc npc);

    void remove(Npc npc);
}
