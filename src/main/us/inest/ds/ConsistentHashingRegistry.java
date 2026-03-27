package us.inest.ds;

import java.security.MessageDigest;
import java.util.*;

public class ConsistentHashingRegistry {
    private final int replicas;
    private final int replicationFactor;
    private final SortedMap<Integer, String> ring = new TreeMap<>();
    private final Map<String, String> serverRegions = new HashMap<>();

    public ConsistentHashingRegistry(int replicas, int replicationFactor) {
        this.replicas = replicas;
        this.replicationFactor = replicationFactor;
    }

    private int hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            return ((digest[0] & 0xFF) << 24) | ((digest[1] & 0xFF) << 16) |
                    ((digest[2] & 0xFF) << 8) | (digest[3] & 0xFF);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addServer(String serverId, String region) {
        serverRegions.put(serverId, region);
        for (int i = 0; i < replicas; i++) {
            ring.put(hash(serverId + ":" + i), serverId);
        }
    }

    public void removeServer(String serverId) {
        serverRegions.remove(serverId);
        for (int i = 0; i < replicas; i++) {
            ring.remove(hash(serverId + ":" + i));
        }
    }

    public List<String> getServers(String key) {
        if (ring.isEmpty()) return Collections.emptyList();
        List<String> result = new ArrayList<>();
        int hash = hash(key);
        SortedMap<Integer, String> tail = ring.tailMap(hash);
        Iterator<String> it = tail.values().iterator();
        while (result.size() < replicationFactor) {
            if (!it.hasNext()) it = ring.values().iterator();
            String server = it.next();
            if (!result.contains(server)) result.add(server);
        }
        return result;
    }

    public static void main(String[] args) {
        ConsistentHashingRegistry registry = new ConsistentHashingRegistry(3, 2);
        registry.addServer("S1", "US");
        registry.addServer("S2", "EU");
        registry.addServer("S3", "ASIA");

        System.out.println(registry.getServers("user123")); // e.g., [S2, S3]
    }
}

