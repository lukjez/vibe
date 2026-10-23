// Codility - MaxCounters
// Time: O(N + M), extra space: O(N)

class Solution {
    public int[] solution(int N, int[] A) {
        int[] counters = new int[N];
        int currentMax = 0; // highest counter value so far
        int floor = 0;      // value every counter was set to at the last "max counter"

        for (int a : A) {
            if (a <= N) {                       // increase(X)
                int i = a - 1;
                if (counters[i] < floor) {
                    counters[i] = floor;        // apply the pending reset
                }
                counters[i]++;
                if (counters[i] > currentMax) {
                    currentMax = counters[i];
                }
            } else {                            // max counter (a == N + 1)
                floor = currentMax;             // O(1) instead of filling the array
            }
        }

        // lift the counters that weren't touched since the last max counter
        for (int i = 0; i < N; i++) {
            if (counters[i] < floor) {
                counters[i] = floor;
            }
        }
        return counters;
    }
}
