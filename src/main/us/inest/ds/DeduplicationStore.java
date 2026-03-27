package us.inest.ds;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeduplicationStore {
    private final long windowMillis;
    private final ConcurrentHashMap<String, Long> store = new ConcurrentHashMap<>();

    public DeduplicationStore(long windowMillis) {
        this.windowMillis = windowMillis;
    }

    public boolean processEvent(String eventId, long now) {
        Long prev = store.putIfAbsent(eventId, now);
        if (prev != null) return false;

        // Clean-up old entries asynchronously or lazily
        store.entrySet().removeIf(e -> now - e.getValue() > windowMillis);

        return true;
    }

    public static void main(String[] args) {
        DeduplicationStore ds = new DeduplicationStore(60000); // 1 min window
        System.out.println(ds.processEvent("e1", System.currentTimeMillis())); // true
        System.out.println(ds.processEvent("e1", System.currentTimeMillis())); // false
    }
}

