// Codility - MinAbsSum
// Time: O(N * max(abs(A))^2), extra space: O(sum(abs(A)))

class Solution {
    public int solution(int[] A) {
        int max = 0;
        int sum = 0;
        int[] count = new int[101];
        for (int a : A) {
            int v = Math.abs(a);           // the sign is chosen by S, so only |A[i]| matters
            count[v]++;
            sum += v;
            max = Math.max(max, v);
        }

        // dp[j] = -1 if sum j is unreachable,
        // otherwise how many copies of the current value are still unused after reaching j
        int[] dp = new int[sum + 1];
        java.util.Arrays.fill(dp, -1);
        dp[0] = 0;

        for (int v = 1; v <= max; v++) {
            if (count[v] == 0) {
                continue;
            }
            for (int j = 0; j <= sum; j++) {
                if (dp[j] >= 0) {
                    dp[j] = count[v];          // reachable without v, all copies left
                } else if (j >= v && dp[j - v] > 0) {
                    dp[j] = dp[j - v] - 1;     // reachable using one more copy of v
                }
            }
        }

        // best split: the subset sum closest to sum / 2
        int result = sum;
        for (int j = 0; j <= sum / 2; j++) {
            if (dp[j] >= 0) {
                result = Math.min(result, sum - 2 * j);
            }
        }
        return result;
    }
}
