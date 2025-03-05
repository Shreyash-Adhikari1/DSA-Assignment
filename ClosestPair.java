import java.util.Arrays;

public class ClosestPair {

    public static int[] findClosestPair(int[] xCoords, int[] yCoords) {
        int n = xCoords.length;
        int minDistance = Integer.MAX_VALUE; // starting with the largest possible distance
        int[] result = new int[2]; // array to store the indices of the closest pair

        // comparing every point with each other to find the closest pair
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    continue; // skipping comparing a point with itself

                // calculating the distance between points
                int distance = Math.abs(xCoords[i] - xCoords[j]) + Math.abs(yCoords[i] - yCoords[j]);

                // if closet pair found, update the result
                if (distance < minDistance
                        || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance; 
                    result[0] = i; // storing the index of first point
                    result[1] = j; // storing the index of second point
                }
            }
        }

        return result; // returning the indices of the closest pair
    }

    public static void main(String[] args) {
        // test Case 1: general case with many points
        int[] xCoords = { 1, 2, 3, 2, 4 };
        int[] yCoords = { 2, 3, 1, 2, 3 };
        System.out.println("Test Case 1:");
        System.out.println("Input: xCoords = [1, 2, 3, 2, 4], yCoords = [2, 3, 1, 2, 3]");
        System.out.println("Output: " + Arrays.toString(findClosestPair(xCoords, yCoords))); // Expected: [0, 3]

        // test Case 2: all points have the same x-coordinate
        int[] xCoords2 = { 1, 1, 1, 1, 1 };
        int[] yCoords2 = { 1, 2, 3, 4, 5 };
        System.out.println("\nTest Case 2:");
        System.out.println("Input: xCoords = [1, 1, 1, 1, 1], yCoords = [1, 2, 3, 4, 5]");
        System.out.println("Output: " + Arrays.toString(findClosestPair(xCoords2, yCoords2))); // Expected: [0, 1]

        // test Case 3: points are evenly spaced on straight line
        int[] xCoords3 = { 0, 10, 20 };
        int[] yCoords3 = { 0, 10, 20 };
        System.out.println("\nTest Case 3:");
        System.out.println("Input: xCoords = [0, 10, 20], yCoords = [0, 10, 20]");
        System.out.println("Output: " + Arrays.toString(findClosestPair(xCoords3, yCoords3))); // Expected: [0, 1]
    }
}
