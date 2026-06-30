package com.algo.tree;

import java.util.*;

/**
 * 二叉树的遍历 —— 从递归思维理解 DFS 与 BFS。
 * <p>
 * ===== DFS 三种位置的核心区别 =====
 * <p>
 * void traverse(TreeNode root) {
 * if (root == null) return;
 * // ── 前序位置（刚到达当前节点时） ──
 * //     此时只知道当前节点的值，不知道左右子树任何信息。
 * //     适合：复制树、序列化、记录从根到当前节点的路径。
 * traverse(root.left);
 * // ── 中序位置（左子树返回后、右子树开始前） ──
 * //     对 BST 来说，这个位置访问的值是「从小到大」的。
 * //     适合：BST 中序遍历输出有序序列、BST 验证。
 * traverse(root.right);
 * // ── 后序位置（左右子树都返回后） ──
 * //     此时已经知道左右子树的计算结果，可以向上传递。
 * //     适合：需要子树信息才能计算父节点的场景（高度、直径、最大路径和）。
 * }
 * <p>
 * 核心洞见：
 * 一个递归函数 = 前序代码 + 中序代码 + 后序代码三段拼接而成。
 * 把代码放在哪个位置，决定了它在执行流中的哪一刻触发。
 * 想清楚"我的逻辑是需要子树的结果，还是只需要当前节点"，
 * 决定了用后序还是前序。
 */
public class TreeTraversal {

    // ============================================================
    // DFS —— 递归遍历（三种顺序）
    // ============================================================

    /**
     * 前序遍历（递归）
     * 前序位置：刚进入节点，左右子树尚未遍历。
     */
    public List<Integer> preorderRecursive(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        pre(root, res);
        return res;
    }

    private void pre(TreeNode root, List<Integer> res) {
        if (root == null) return;
        res.add(root.val);       // 前序位置
        pre(root.left, res);
        pre(root.right, res);
    }

    /**
     * 中序遍历（递归）
     * 中序位置：左子树遍历完毕，回到当前节点。
     */
    public List<Integer> inorderRecursive(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        in(root, res);
        return res;
    }

    private void in(TreeNode root, List<Integer> res) {
        if (root == null) return;
        in(root.left, res);
        res.add(root.val);       // 中序位置
        in(root.right, res);
    }

    /**
     * 后序遍历（递归）
     * 后序位置：左右子树均已遍历完毕，回到当前节点。
     */
    public List<Integer> postorderRecursive(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        post(root, res);
        return res;
    }

    private void post(TreeNode root, List<Integer> res) {
        if (root == null) return;
        post(root.left, res);
        post(root.right, res);
        res.add(root.val);       // 后序位置
    }

    // ============================================================
    // DFS —— 迭代遍历（显式栈，体现遍历顺序的差异）
    // ============================================================

    /**
     * 前序迭代：根 → 左 → 右
     */
    public List<Integer> preorderIterative(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode cur = stack.pop();
            res.add(cur.val);
            if (cur.right != null) stack.push(cur.right);
            if (cur.left != null) stack.push(cur.left);
        }
        return res;
    }

    /**
     * 中序迭代：左 → 根 → 右
     */
    public List<Integer> inorderIterative(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            res.add(cur.val);
            cur = cur.right;
        }
        return res;
    }

    /**
     * 后序迭代：左 → 右 → 根（利用前序的变体 + 逆序输出）
     */
    public List<Integer> postorderIterative(TreeNode root) {
        LinkedList<Integer> res = new LinkedList<>();
        if (root == null) return res;
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode cur = stack.pop();
            res.addFirst(cur.val);     // 逆序插入 → 后序
            if (cur.left != null) stack.push(cur.left);
            if (cur.right != null) stack.push(cur.right);
        }
        return res;
    }

    // ============================================================
    // BFS —— 层序遍历（可记录深度）
    // ============================================================

    /**
     * 层序遍历，按层分组
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode cur = q.poll();
                level.add(cur.val);
                if (cur.left != null) q.offer(cur.left);
                if (cur.right != null) q.offer(cur.right);
            }
            res.add(level);
        }
        return res;
    }

    /**
     * 记录每个节点深度的 BFS（可得知任意节点的深度）
     */
    public List<int[]> levelWithDepth(TreeNode root) {
        List<int[]> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>();
        Queue<Integer> dq = new LinkedList<>();
        q.offer(root);
        dq.offer(0);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            int depth = dq.poll();
            res.add(new int[]{cur.val, depth});
            if (cur.left != null) {
                q.offer(cur.left);
                dq.offer(depth + 1);
            }
            if (cur.right != null) {
                q.offer(cur.right);
                dq.offer(depth + 1);
            }
        }
        return res;
    }

    /**
     * BFS 另一种写法：用 Entry 对记录深度
     */
    public List<int[]> levelWithDepth2(TreeNode root) {
        List<int[]> res = new ArrayList<>();
        if (root == null) return res;
        Queue<Map.Entry<TreeNode, Integer>> q = new LinkedList<>();
        q.offer(new AbstractMap.SimpleEntry<>(root, 0));
        while (!q.isEmpty()) {
            var e = q.poll();
            TreeNode cur = e.getKey();
            int depth = e.getValue();
            res.add(new int[]{cur.val, depth});
            if (cur.left != null) q.offer(new AbstractMap.SimpleEntry<>(cur.left, depth + 1));
            if (cur.right != null) q.offer(new AbstractMap.SimpleEntry<>(cur.right, depth + 1));
        }
        return res;
    }

}
