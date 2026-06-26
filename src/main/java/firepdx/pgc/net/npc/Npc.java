package firepdx.pgc.net.npc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class Npc {
    private final UUID id;
    private String name;
    private NpcType type;
    private NpcLocation location;
    private SkinData skin;
    private final List<HologramLine> hologramLines;
    private Instant updatedAt;

    public Npc(UUID id, String name, NpcType type, NpcLocation location, SkinData skin, List<HologramLine> hologramLines, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.skin = skin == null ? SkinData.empty() : skin;
        this.hologramLines = new ArrayList<>(hologramLines == null ? List.of() : hologramLines);
        this.updatedAt = updatedAt == null ? Instant.now() : updatedAt;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public void rename(String name) {
        this.name = name;
        touch();
    }

    public NpcType type() {
        return type;
    }

    public void type(NpcType type) {
        this.type = type;
        touch();
    }

    public NpcLocation location() {
        return location;
    }

    public void move(NpcLocation location) {
        this.location = location;
        touch();
    }

    public SkinData skin() {
        return skin;
    }

    public void skin(SkinData skin) {
        this.skin = skin;
        touch();
    }

    public List<HologramLine> hologramLines() {
        return Collections.unmodifiableList(hologramLines);
    }

    public void setHologramLines(List<String> lines) {
        hologramLines.clear();
        for (int i = 0; i < lines.size(); i++) {
            hologramLines.add(new HologramLine(i, lines.get(i)));
        }
        touch();
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void touch() {
        updatedAt = Instant.now();
    }
}
