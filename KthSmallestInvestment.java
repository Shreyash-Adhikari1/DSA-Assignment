// Question 1b (Algorithm Design Technique - Binary Search Approach)
public class KthSmallestInvestment {

    // Counting the number of pairs where product is <= mid
    private static int countPairs(int[] returns1, int[] returns2, long mid) {
        int count = 0; // Storing count of valid pairs
        int m = returns1.length, n = returns2.length;
        int j = n - 1; // Pointer for returns2 array

        // Going through the first array
        for (int i = 0; i < m; i++) {
            // Moving pointer to find valid pairs
            while (j >= 0 && (long) returns1[i] * returns2[j] > mid) {
                j--; // Move left
            }
            count += (j + 1); // Adding valid pairs
        }
        return count;
    }

    // Finding the kth smallest product using binary search
    public static int kthSmallestProduct(int[] returns1, int[] returns2, int k) {
        // Finding the smallest and largest product
        long left = (long) returns1[0] * returns2[0];
        long right = (long) returns1[returns1.length - 1] * returns2[returns2.length - 1];

        // Adjusting range to consider negative numbers
        left = Math.min(left, (long) returns1[0] * returns2[returns2.length - 1]);
        left = Math.min(left, (long) returns1[returns1.length - 1] * returns2[0]);
        right = Math.max(right, (long) returns1[0] * returns2[returns2.length - 1]);
        right = Math.max(right, (long) returns1[returns1.length - 1] * returns2[0]);

        // Using binary search to find the kth smallest product
        while (left < right) {
            long mid = left + (right - left) / 2;
            int count = countPairs(returns1, returns2, mid);

            // Checking if we need to move left or right
            if (count < k) {
                left = mid + 1; // Move right
            } else {
                right = mid; // Move left
            }
        }
        return (int) left; // Returning the kth smallest product
    }

    // Testing the function with different cases
    public static void main(String[] args) {
        // Test Case 1
        int[] returns1 = { 2, 5 };
        int[] returns2 = { 3, 4 };
        int k = 2;
        System.out.println("Test Case 1:");
        System.out.println("Input: returns1 = [2, 5], returns2 = [3, 4], k = " + k);
        System.out.println("Output: " + kthSmallestProduct(returns1, returns2, k)); // Expected: 8

        // Test Case 2
        int[] returns3 = { -4, -2, 0, 3 };
        int[] returns4 = { 2, 4 };
        k = 6;
        System.out.println("\nTest Case 2:");
        System.out.println("Input: returns1 = [-4, -2, 0, 3], returns2 = [2, 4], k = " + k);
        System.out.println("Output: " + kthSmallestProduct(returns3, returns4, k)); // Expected: 0
    }
}
