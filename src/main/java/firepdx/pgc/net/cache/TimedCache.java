package firepdx.pgc.net.cache;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class TimedCache<K, V> {
    private final Duration ttl;
    private final Map<K, Entry<V>> values = new ConcurrentHashMap<>();

    public TimedCache(Duration ttl) {
        this.ttl = ttl;
    }

    public Optional<V> get(K key) {
        Entry<V> entry = values.get(key);
        if (entry == null || entry.expiresAt().isBefore(Instant.now())) {
            values.remove(key);
            return Optional.empty();
        }
        return Optional.of(entry.value());
    }

    public void put(K key, V value) {
        values.put(key, new Entry<>(value, Instant.now().plus(ttl)));
    }

    private record Entry<V>(V value, Instant expiresAt) {
    }
}
