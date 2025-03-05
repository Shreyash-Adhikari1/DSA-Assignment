import java.util.*;
//4b
// You have a map of a city represented by a graph with n nodes (representing locations) and edges where 
// edges[i] = [ai, bi] indicates a road between locations ai and bi. Each location has a value of either 0 or 1, 
// indicating whether there is a package to be delivered. You can start at any location and perform the 
// following actions: 
// Collect packages from all locations within a distance of 2 from your current location. 
// Move to an adjacent location. 
// Your goal is to collect all packages and return to your starting location. 
// Goal: 
// Determine the minimum number of roads you need to traverse to collect all packages. 
// Input: 
// packages: An array of package values for each location. 
// roads: A 2D array representing the connections between locations. 
// Output: 
// The minimum number of roads to traverse. 
// Note that if you pass a roads several times, you need to count it into the answer several times. 
// Input: packages = [1, 0, 0, 0, 0, 1], roads = [[0, 1], [1, 2], [2, 3], [3, 4], [4, 5]] 
// Output:2  
// Explanation: Start at location 2, collect the packages at location 0, move to location 3, collect the 
// packages at location 5 then move back to location  2. 
// Input: packages = [0,0,0,1,1,0,0,1], roads = [[0,1],[0,2],[1,3],[1,4],[2,5],[5,6],[5,7]] 
// Output: 2 
// Explanation: Start at location 0, collect the package at location 4 and 3, move to location 2,  collect the 
// package  at location  7, then move back to location  0. 
public class PackageCollector {
    public static void main(String[] args) {
        int[] packages1 = {1, 0, 0, 0, 0, 1};
        int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
        System.out.println("Output: " + minRoadsToCollectPackages(packages1, roads1)); // Expected Output: 2

        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
        System.out.println("Output: " + minRoadsToCollectPackages(packages2, roads2)); // Expected Output: 2
    }

    public static int minRoadsToCollectPackages(int[] packages, int[][] roads) {
        int n = packages.length;
        Map<Integer, List<Integer>> graph = new HashMap<>();

        // Build the adjacency list
        for (int[] road : roads) {
            graph.computeIfAbsent(road[0], k -> new ArrayList<>()).add(road[1]);
            graph.computeIfAbsent(road[1], k -> new ArrayList<>()).add(road[0]);
        }

        Set<Integer> visited = new HashSet<>();
        return dfs(0, -1, graph, packages, visited);
    }

    private static int dfs(int node, int parent, Map<Integer, List<Integer>> graph, int[] packages, Set<Integer> visited) {
        visited.add(node);
        int roadsUsed = 0;

        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (neighbor == parent) continue; // Avoid going back directly
            if (!visited.contains(neighbor)) {
                roadsUsed += dfs(neighbor, node, graph, packages, visited);
            }
        }

        // If a package exists at this node or any child node required traversal, count the road
        if ((packages[node] == 1 || roadsUsed > 0) && parent != -1) {
            return roadsUsed + 2; // Moving to the node and coming back
        }
        return roadsUsed;
    }
}
