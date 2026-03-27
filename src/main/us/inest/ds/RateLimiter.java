package us.inest.ds;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Sliding window per-tenant rate limiter.
 * Thread-safe: per-tenant fine-grained locking to avoid global contention.
 */
public class RateLimiter {
    private final long windowMillis;
    private final int maxRequests;
    private final ConcurrentHashMap<String, TenantBucket> buckets = new ConcurrentHashMap<>();

    // Optional: TTL eviction for inactive tenants
    private final long idleTtlMillis;

    public RateLimiter(int windowSeconds, int maxRequestsPerWindow) {
        this(windowSeconds, maxRequestsPerWindow, /*idleTtlSeconds*/ 300);
    }

    public RateLimiter(int windowSeconds, int maxRequestsPerWindow, int idleTtlSeconds) {
        if (windowSeconds <= 0 || maxRequestsPerWindow <= 0) {
            throw new IllegalArgumentException("windowSeconds and maxRequestsPerWindow must be > 0");
        }
        this.windowMillis = windowSeconds * 1000L;
        this.maxRequests = maxRequestsPerWindow;
        this.idleTtlMillis = Math.max(0, idleTtlSeconds * 1000L);
    }

    public boolean allow(String tenantId, long nowEpochMillis) {
        Objects.requireNonNull(tenantId, "tenantId");
        TenantBucket bucket = buckets.computeIfAbsent(tenantId, t -> new TenantBucket());
        return bucket.tryAcquire(nowEpochMillis);
    }

    /** Optional housekeeping; call periodically (e.g., on a timer) */
    public void sweep(long nowEpochMillis) {
        for (Map.Entry<String, TenantBucket> e : buckets.entrySet()) {
            TenantBucket tb = e.getValue();
            if (tb.tryLock()) {
                try {
                    tb.trim(nowEpochMillis, windowMillis);
                    if (idleTtlMillis > 0 && (nowEpochMillis - tb.lastTouched) > idleTtlMillis) {
                        buckets.remove(e.getKey(), tb);
                    }
                } finally {
                    tb.unlock();
                }
            }
        }
    }

    private final class TenantBucket {
        private final Deque<Long> timestamps = new ArrayDeque<>();
        private final ReentrantLock lock = new ReentrantLock();
        private long lastTouched = 0L;

        boolean tryAcquire(long now) {
            lock.lock();
            try {
                trim(now, windowMillis);
                if (timestamps.size() < maxRequests) {
                    timestamps.addLast(now);
                    lastTouched = now;
                    return true;
                }
                return false;
            } finally {
                lock.unlock();
            }
        }

        void trim(long now, long window) {
            while (!timestamps.isEmpty() && (now - timestamps.peekFirst()) >= window) {
                timestamps.removeFirst();
            }
            lastTouched = now;
        }

        boolean tryLock() {
            return lock.tryLock();
        }

        void unlock() {
            lock.unlock();
        }
    }

    // Simple demo
    public static void main(String[] args) throws InterruptedException {
        RateLimiter rl = new RateLimiter(10, 3);
        String t = "tenantA";
        long base = System.currentTimeMillis();

        System.out.println(rl.allow(t, base));          // true
        System.out.println(rl.allow(t, base + 3000));    // true
        System.out.println(rl.allow(t, base + 9000));    // true
        System.out.println(rl.allow(t, base + 9100));    // false

        Thread.sleep(1100);
        long now = System.currentTimeMillis();
        System.out.println(rl.allow(t, now));            // likely true after oldest falls out
    }
}

