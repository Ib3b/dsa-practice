package com.algo.recursion;

import java.util.*;

/**
 * 用递归思维理解回溯算法与动态规划。
 *
 * ===== 回溯 =====
 * 回溯是 DFS 在决策树上的应用。
 * 递归思维：在当前层做选择，然后信任递归处理剩下的层。
 *
 * 模板：
 *   void backtrack(路径, 选择列表) {
 *       if (满足结束条件) { 记录结果; return; }
 *       for (选择 : 选择列表) {
 *           做选择;                 // 前序位置
 *           backtrack(新的路径, 新的选择列表);  // 信任递归
 *           撤销选择;               // 后序位置
 *       }
 *   }
 *
 * ===== 动态规划 =====
 * DP 是「带 memo 的递归 + 状态转移方程」。
 * 递归思维：定义 dp(状态) = 从当前状态到目标的答案。
 *          当前层做一步选择，然后信任递归处理剩余状态。
 *          加 memo 消去重叠子问题。
 *
 * 模板：
 *   int dp(状态) {
 *       if (memo.containsKey(状态)) return memo.get(状态);
 *       if (base case) return 0;
 *       for (选择 : 所有可能的选择) {
 *           结果 = min(结果, dp(新状态) + 本次代价);  // 后序位置
 *       }
 *       memo.put(状态, 结果);
 *       return 结果;
 *   }
 */
public class BacktrackingDP {

    // ============================================================
    // 回溯 —— 子集
    // ============================================================
    // 递归思维：对每个元素，选或不选，两种分支。
    // 位置：前序（每次到达节点时记录当前路径）。

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        backtrackSubset(nums, 0, new ArrayList<>(), res);
        return res;
    }
    private void backtrackSubset(int[] nums, int start,
                                 List<Integer> path, List<List<Integer>> res) {
        // 前序位置：每个节点都是一种子集
        res.add(new ArrayList<>(path));

        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);                    // 做选择
            backtrackSubset(nums, i + 1, path, res); // 信任递归
            path.remove(path.size() - 1);         // 撤销选择
        }
    }

    // ============================================================
    // 回溯 —— 全排列
    // ============================================================
    // 递归思维：逐个确定每个位置的值，用过的元素不能再用。
    // 位置：前序（在叶子节点记录完整排列）。

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrackPermute(nums, used, new ArrayList<>(), res);
        return res;
    }
    private void backtrackPermute(int[] nums, boolean[] used,
                                  List<Integer> path, List<List<Integer>> res) {
        if (path.size() == nums.length) {
            res.add(new ArrayList<>(path));   // 叶子节点：找到一个排列
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            path.add(nums[i]);                // 做选择
            backtrackPermute(nums, used, path, res);  // 信任递归
            path.remove(path.size() - 1);     // 撤销选择
            used[i] = false;
        }
    }

    // ============================================================
    // 回溯 —— N 皇后
    // ============================================================
    // 递归思维：逐行放置皇后，每行选一个不冲突的列。

    public List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        char[][] board = new char[n][n];
        for (char[] row : board) Arrays.fill(row, '.');
        backtrackNQ(board, 0, res);
        return res;
    }
    private void backtrackNQ(char[][] board, int row, List<List<String>> res) {
        if (row == board.length) {
            List<String> snapshot = new ArrayList<>();
            for (char[] r : board) snapshot.add(new String(r));
            res.add(snapshot);
            return;
        }
        for (int col = 0; col < board.length; col++) {
            if (!isValid(board, row, col)) continue;
            board[row][col] = 'Q';                       // 做选择
            backtrackNQ(board, row + 1, res);             // 信任递归
            board[row][col] = '.';                       // 撤销选择
        }
    }
    private boolean isValid(char[][] board, int row, int col) {
        int n = board.length;
        // 检查同列
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q') return false;
        }
        // 检查左上对角线
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q') return false;
        }
        // 检查右上对角线
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
            if (board[i][j] == 'Q') return false;
        }
        return true;
    }

    // ============================================================
    // DP —— 斐波那契（记忆化递归）
    // ============================================================
    // 递归思维：fib(n) = fib(n-1) + fib(n-2)，
    //          加 memo 避免重复计算。

    public int fib(int n) {
        int[] memo = new int[n + 1];
        return fibMemo(n, memo);
    }
    private int fibMemo(int n, int[] memo) {
        if (n == 0 || n == 1) return n;          // base case
        if (memo[n] != 0) return memo[n];         // 查 memo
        memo[n] = fibMemo(n - 1, memo)           // 后序位置：需要子问题结果
                + fibMemo(n - 2, memo);
        return memo[n];
    }

    // ============================================================
    // DP —— 零钱兑换（最少硬币数）
    // ============================================================
    // 递归思维：dp(amount) = min(dp(amount - coin) + 1) for coin in coins
    //          当前选一枚硬币，然后信任递归处理剩下的金额。
    //          加 memo 避免对相同 amount 重复计算。

    public int coinChange(int[] coins, int amount) {
        int[] memo = new int[amount + 1];
        Arrays.fill(memo, -2);  // -2 表示未计算
        return coinDp(coins, amount, memo);
    }
    private int coinDp(int[] coins, int amount, int[] memo) {
        if (amount == 0) return 0;               // base case
        if (amount < 0) return -1;               // 无解
        if (memo[amount] != -2) return memo[amount];  // 查 memo

        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int sub = coinDp(coins, amount - coin, memo);  // 信任递归
            if (sub != -1) {
                min = Math.min(min, sub + 1);     // 后序位置
            }
        }
        memo[amount] = (min == Integer.MAX_VALUE) ? -1 : min;
        return memo[amount];
    }

    // ============================================================
    // DP —— 最长递增子序列（LIS）
    // ============================================================
    // 递归思维：dp(i) = 以 nums[i] 结尾的最长递增子序列长度。
    //          dp(i) = max(dp(j) + 1) for j < i where nums[j] < nums[i]

    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] memo = new int[n];
        Arrays.fill(memo, -1);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, lisDp(nums, i, memo));
        }
        return ans;
    }
    private int lisDp(int[] nums, int i, int[] memo) {
        if (memo[i] != -1) return memo[i];
        int best = 1;  // 至少包含自己
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                best = Math.max(best, lisDp(nums, j, memo) + 1);  // 后序位置
            }
        }
        memo[i] = best;
        return best;
    }

}
