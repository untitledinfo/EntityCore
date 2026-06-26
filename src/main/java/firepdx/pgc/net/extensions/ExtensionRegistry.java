package firepdx.pgc.net.extensions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ExtensionRegistry {
    private final Map<String, Map<String, Object>> extensions = new LinkedHashMap<>();

    public void register(String category, String key, Object value) {
        extensions.computeIfAbsent(category, ignored -> new LinkedHashMap<>()).put(key.toLowerCase(), value);
    }

    public Map<String, Object> category(String category) {
        return Collections.unmodifiableMap(extensions.getOrDefault(category, Map.of()));
    }
}
