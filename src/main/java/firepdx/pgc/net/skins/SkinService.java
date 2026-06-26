package firepdx.pgc.net.skins;

import firepdx.pgc.net.npc.SkinData;
import java.util.concurrent.CompletableFuture;

public interface SkinService {
    CompletableFuture<SkinData> resolve(String input);
}
