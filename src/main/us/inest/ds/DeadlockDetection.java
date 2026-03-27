package us.inest.ds;

import java.util.*;

public class DeadlockDetection {
    public static boolean hasDeadlock(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();

        for (String node : graph.keySet()) {
            if (dfs(node, graph, visited, stack)) {
                return true;
            }
        }
        return false;
    }

    private static boolean dfs(String node, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> stack) {
        if (stack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        stack.add(node);
        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (dfs(neighbor, graph, visited, stack)) {
                return true;
            }
        }
        stack.remove(node);
        return false;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("T1", Arrays.asList("T2"));
        graph.put("T2", Arrays.asList("T3"));
        graph.put("T3", Arrays.asList("T1")); // cycle -> deadlock

        System.out.println("Deadlock? " + hasDeadlock(graph)); // true
    }
}

