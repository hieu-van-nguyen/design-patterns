package us.inest.ds;

public class TokenBucket {
    private final double rate;
    private final double capacity;
    private double tokens;
    private long lastRefill;

    public TokenBucket(double rate, double capacity) {
        this.rate = rate;
        this.capacity = capacity;
        this.tokens = capacity;
        this.lastRefill = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefill) / 1_000_000_000.0;
        tokens = Math.min(capacity, tokens + elapsedSeconds * rate);
        lastRefill = now;

        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        TokenBucket bucket = new TokenBucket(5, 10); // 5 req/sec, burst 10
        for (int i = 1; i <= 15; i++) {
            System.out.println("Request " + i + ": " + bucket.allowRequest());
            Thread.sleep(200);
        }
    }
}

