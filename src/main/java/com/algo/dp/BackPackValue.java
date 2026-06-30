package com.algo.dp;

public class BackPackValue {

    public int value(int capacity, int[] wt, int[] val) {
        int n2 = wt.length;
        int[][] dp = new int[capacity + 1][n2 + 1];
        for (int i = 0; i <= capacity; i++) {
            dp[i][0] = 0;
        }
        for (int i = 0; i <= n2; i++) {
            dp[0][i] = 0;
        }
        for (int i = 1; i <= capacity; i++) {
            for (int j = 1; j <= n2; j++) {
                if (wt[j - 1] > i) {
                    dp[i][j] = dp[i][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - wt[j - 1]][j - 1] + val[j - 1]);
                }
            }
        }
        return dp[capacity][n2];
    }
}
