import java.util.*;

public class MinimumNetworkCost {

    // defining a union-find data structure
    static class UnionFind {
        int[] parent; // array to keep track of the parent of each node
        int[] rank;   // array to keep track of the rank of each node

        UnionFind(int n) {
            parent = new int[n + 1]; // initializing parent array
            rank = new int[n + 1];   // initializing rank array
            for (int i = 0; i <= n; i++) {
                parent[i] = i; // setting each node's parent to itself
                rank[i] = 1;   // setting initial rank to 1
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // applying path compression
            }
            return parent[x]; // returning the root of the node
        }

        boolean union(int x, int y) {
            int rootX = find(x); // finding the root of x
            int rootY = find(y); // finding the root of y
            if (rootX == rootY)
                return false; // returning false if they are already connected

            // performing union by rank
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX; // connecting rootY to rootX
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY; // connecting rootX to rootY
            } else {
                parent[rootY] = rootX; // connecting rootY to rootX
                rank[rootX]++; // increasing the rank of rootX
            }
            return true; // returning true if union is successful
        }
    }

    public static int minCostToConnectDevices(int n, int[] modules, int[][] connections) {
        // creating a list to store all edges (connections and module installations)
        List<int[]> edges = new ArrayList<>();

        // adding module installation edges
        for (int i = 0; i < n; i++) {
            edges.add(new int[] { 0, i + 1, modules[i] }); // connecting virtual node 0 to device i+1
        }

        // adding connection edges
        for (int[] connection : connections) {
            edges.add(new int[] { connection[0], connection[1], connection[2] }); // adding connection
        }

        // sorting edges by cost
        edges.sort((a, b) -> a[2] - b[2]);

        // initializing union-find
        UnionFind uf = new UnionFind(n);

        int totalCost = 0; // initializing total cost
        int edgesUsed = 0; // initializing count of edges used

        // applying kruskal's algorithm
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], cost = edge[2]; // getting the nodes and cost from edge
            if (uf.union(u, v)) { // performing union
                totalCost += cost; // adding cost to total cost
                edgesUsed++; // increasing the count of edges used
                if (edgesUsed == n) // checking if all devices are connected
                    break; // breaking the loop if all devices are connected
            }
        }

        return totalCost; // returning the total cost
    }

    public static void main(String[] args) {
        // test case 1
        int n1 = 3;
        int[] modules1 = { 1, 2, 2 };
        int[][] connections1 = { { 1, 2, 1 }, { 2, 3, 1 } };
        System.out.println("Test Case 1:");
        System.out.println("Input: n = 3, modules = [1, 2, 2], connections = [[1, 2, 1], [2, 3, 1]]");
        System.out.println("Output: " + minCostToConnectDevices(n1, modules1, connections1)); // expected: 3

        // test case 2
        int n2 = 4;
        int[] modules2 = { 3, 4, 2, 5 };
        int[][] connections2 = { { 1, 2, 2 }, { 2, 3, 3 }, { 3, 4, 1 }, { 1, 4, 4 } };
        System.out.println("\nTest Case 2:");
        System.out.println(
                "Input: n = 4, modules = [3, 4, 2, 5], connections = [[1, 2, 2], [2, 3, 3], [3, 4, 1], [1, 4, 4]]");
        System.out.println("Output: " + minCostToConnectDevices(n2, modules2, connections2)); // expected: 8

        // test case 3
        int n3 = 5;
        int[] modules3 = { 1, 1, 1, 1, 1 };
        int[][] connections3 = { { 1, 2, 1 }, { 2, 3, 1 }, { 3, 4, 1 }, { 4, 5, 1 } };
        System.out.println("\nTest Case 3:");
        System.out.println(
                "Input: n = 5, modules = [1, 1, 1, 1, 1], connections = [[1, 2, 1], [2, 3, 1], [3, 4, 1], [4, 5, 1]]");
        System.out.println("Output: " + minCostToConnectDevices(n3, modules3, connections3)); // expected: 5
    }
}