// defining the minimum rewards class
public class MinimumRewards {

    public static int minRewards(int[] ratings) {
        int n = ratings.length; // getting the number of ratings
        if (n == 0) // checking if there are no ratings
            return 0; // returning 0 if no ratings

        int[] rewards = new int[n]; // creating an array to store rewards
        // giving every employee at least one reward initially
        for (int i = 0; i < n; i++) {
            rewards[i] = 1; // setting initial reward to 1
        }

        // performing left-to-right pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) { // checking if current rating is greater than the previous one
                rewards[i] = rewards[i - 1] + 1; // increasing reward based on previous reward
            }
        }

        // performing right-to-left pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) { // checking if current rating is greater than the next one
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1); // ensuring the maximum reward condition
            }
        }

        // summing up the total rewards
        int minRewards = 0; // initializing total rewards
        for (int reward : rewards) {
            minRewards += reward; // adding each reward to total
        }

        return minRewards; // returning the total minimum rewards
    }

    public static void main(String[] args) {
        // test case 1
        int[] ratings1 = { 1, 0, 2 };
        System.out.println("Test Case 1:");
        System.out.println("Input: ratings = [1, 0, 2]");
        System.out.println("Output: " + minRewards(ratings1)); // expected: 5

        // test case 2
        int[] ratings2 = { 1, 2, 2 };
        System.out.println("\nTest Case 2:");
        System.out.println("Input: ratings = [1, 2, 2]");
        System.out.println("Output: " + minRewards(ratings2)); // expected: 4

        // test case 3
        int[] ratings3 = { 4, 3, 2, 1, 2, 3, 4 };
        System.out.println("\nTest Case 3:");
        System.out.println("Input: ratings = [4, 3, 2, 1, 2, 3, 4]");
        System.out.println("Output: " + minRewards(ratings3)); // expected: 19
    }
}