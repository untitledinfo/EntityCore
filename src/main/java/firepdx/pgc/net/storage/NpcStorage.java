package firepdx.pgc.net.storage;

import firepdx.pgc.net.npc.Npc;
import java.util.Collection;
import java.util.List;

public interface NpcStorage {
    List<Npc> load();

    void save(Collection<Npc> npcs);
}
