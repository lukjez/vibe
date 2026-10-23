// Codility - StoneWall
// Time: O(N), extra space: O(N)

class Solution {
    public int solution(int[] H) {
        int[] stack = new int[H.length]; // heights of blocks still "open" to the right
        int top = 0;
        int blocks = 0;

        for (int h : H) {
            // blocks taller than the current height must end here
            while (top > 0 && stack[top - 1] > h) {
                top--;
            }
            // a block of exactly this height continues; otherwise start a new one
            if (top == 0 || stack[top - 1] < h) {
                stack[top++] = h;
                blocks++;
            }
        }
        return blocks;
    }
}
