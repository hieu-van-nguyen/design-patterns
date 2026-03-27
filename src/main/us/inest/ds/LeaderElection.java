package us.inest.ds;

import java.util.*;

public class LeaderElection {
    public static Integer electLeader(List<Integer> nodes, Set<Integer> failed) {
        Integer leader = null;
        for (Integer node : nodes) {
            if (!failed.contains(node)) {
                if (leader == null || node > leader) {
                    leader = node;
                }
            }
        }
        return leader;
    }

    public static void main(String[] args) {
        List<Integer> nodes = Arrays.asList(1, 2, 3, 4, 5);
        Set<Integer> failed = new HashSet<>(Arrays.asList(2, 5));
        System.out.println("Leader: " + electLeader(nodes, failed)); // Leader = 4
    }
}

