// Question 1a (Divide-and-Conquer Strategy Combined with Dynamic Programming)
public class CriticalTemperature {

    // finding the minimum number of measurements needed
    public static int findMinMeasurements(int k, int n) {
        // storing the results in a table
        int[][] dp = new int[k + 1][n + 1];

        // if only one sample is there, checking all temperatures one by one
        for (int i = 1; i <= n; i++) {
            dp[1][i] = i;
        }

        // if no temperature levels are there, no measurement is needed
        for (int i = 1; i <= k; i++) {
            dp[i][0] = 0;
        }

        // filling the table for more samples and temperature levels
        for (int i = 2; i <= k; i++) { // looping through samples
            for (int j = 1; j <= n; j++) { // looping through temperatures
                dp[i][j] = Integer.MAX_VALUE; // starting with a big number

                // trying different temperature levels
                for (int x = 1; x <= j; x++) {
                    // if the material reacts, checking below that level
                    // if it does not react, checking above that level
                    int res = 1 + Math.max(dp[i - 1][x - 1], dp[i][j - x]);

                    // storing the minimum number of measurements needed
                    dp[i][j] = Math.min(dp[i][j], res);
                }
            }
        }

        return dp[k][n]; // returning the minimum measurements needed
    }

    // testing the function with different cases
    public static void main(String[] args) {
        // storing test cases
        int[][] testCases = {
                { 1, 2, 2 },  // 1 sample, 2 temperatures, expected result 2
                { 2, 6, 3 },  // 2 samples, 6 temperatures, expected result 3
                { 3, 14, 4 }  // 3 samples, 14 temperatures, expected result 4
        };

        // running test cases
        for (int[] testCase : testCases) {
            int k = testCase[0]; // storing number of samples
            int n = testCase[1]; // storing number of temperatures
            int expected = testCase[2]; // storing expected result
            int result = findMinMeasurements(k, n); // getting the result

            // printing results
            System.out.println("Test Case: k = " + k + ", n = " + n);
            System.out.println("Expected Output: " + expected);
            System.out.println("Actual Output: " + result);
            System.out.println("Result: " + (result == expected ? "PASS" : "FAIL"));
            System.out.println();
        }
    }
}
