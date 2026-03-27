package us.inest.ds;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ConsistentHashing {
    private final int replicas;
    private final SortedMap<Integer, String> ring = new TreeMap<>();

    public ConsistentHashing(int replicas) {
        this.replicas = replicas;
    }

    private int hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            return ((digest[0] & 0xFF) << 24) | ((digest[1] & 0xFF) << 16) |
                    ((digest[2] & 0xFF) << 8) | (digest[3] & 0xFF);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void addServer(String serverId) {
        for (int i = 0; i < replicas; i++) {
            ring.put(hash(serverId + ":" + i), serverId);
        }
    }

    public void removeServer(String serverId) {
        for (int i = 0; i < replicas; i++) {
            ring.remove(hash(serverId + ":" + i));
        }
    }

    public String getServer(String key) {
        if (ring.isEmpty()) return null;
        int hash = hash(key);
        SortedMap<Integer, String> tailMap = ring.tailMap(hash);
        Integer nodeHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        return ring.get(nodeHash);
    }

    public static void main(String[] args) {
        ConsistentHashing ch = new ConsistentHashing(3);
        ch.addServer("A");
        ch.addServer("B");
        ch.addServer("C");

        System.out.println(ch.getServer("user123"));
        System.out.println(ch.getServer("video456"));
    }
}

