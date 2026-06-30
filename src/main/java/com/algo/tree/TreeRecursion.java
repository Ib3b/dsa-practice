package com.algo.tree;

import java.util.*;

/**
 * 二叉树经典递归算法 —— 每种算法都标注了「递归位置选择」。
 * <p>
 * 递归思维的关键两步：
 * 1. 定义子问题（这个函数「做什么」）
 * 2. 确定位置（需要子树的结果 → 后序；只处理当前节点 → 前序）
 */
public class TreeRecursion {

    /**
     * 最大深度
     * 位置：后序。需要先知道左右子树的高度，才能算出当前节点的高度。
     */
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        int left = maxDepth(root.left);
        int right = maxDepth(root.right);
        return 1 + Math.max(left, right);  // 后序位置
    }

    /**
     * 最小深度（根到最近叶子节点的距离）
     * 位置：前序 + 后序混合。
     * 前序判断叶子，后序传递深度。
     */
    public int minDepth(TreeNode root) {
        if (root == null) return 0;
        if (root.left == null) return 1 + minDepth(root.right);
        if (root.right == null) return 1 + minDepth(root.left);
        return 1 + Math.min(minDepth(root.left), minDepth(root.right));
    }

    /**
     * 判断平衡二叉树
     * 位置：后序。需要左右子树的高度来计算平衡因子。
     * 返回 -1 表示不平衡，否则返回高度。
     */
    public boolean isBalanced(TreeNode root) {
        return balanceHeight(root) != -1;
    }

    private int balanceHeight(TreeNode root) {
        if (root == null) return 0;
        int left = balanceHeight(root.left);
        if (left == -1) return -1;
        int right = balanceHeight(root.right);
        if (right == -1) return -1;
        if (Math.abs(left - right) > 1) return -1;
        return 1 + Math.max(left, right);  // 后序位置
    }

    /**
     * 镜像对称
     * 位置：前序。同时比较对称的两个节点。
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;
        return symmetric(root.left, root.right);
    }

    private boolean symmetric(TreeNode a, TreeNode b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.val == b.val                                             // 前序位置
                && symmetric(a.left, b.right)
                && symmetric(a.right, b.left);
    }

    /**
     * 翻转二叉树
     * 位置：前序（先交换，再递归翻转子树）。
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode tmp = root.left;                 // 前序位置
        root.left = root.right;
        root.right = tmp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }

    /**
     * 路径总和：是否存在根到叶子的路径和为 target
     * 位置：前序。在到达叶子节点时检查路径和。
     */
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        return pathSumCheck(root, 0, targetSum);
    }

    private boolean pathSumCheck(TreeNode root, int cur, int target) {
        if (root == null) return false;
        cur += root.val;              // 前序位置：累加当前节点
        if (root.left == null && root.right == null) {
            return cur == target;     // 叶子节点：检查是否满足
        }
        return pathSumCheck(root.left, cur, target)
                || pathSumCheck(root.right, cur, target);
    }

    /**
     * 二叉树直径（任意两个节点间最长路径的边数）
     * 位置：后序。需要左右子树的最大深度来计算经过当前节点的路径长度。
     */
    public int diameterOfBinaryTree(TreeNode root) {
        diameter = 0;
        depth(root);
        return diameter;
    }

    private int diameter;

    private int depth(TreeNode root) {
        if (root == null) return 0;
        int left = depth(root.left);
        int right = depth(root.right);
        diameter = Math.max(diameter, left + right);  // 后序位置
        return 1 + Math.max(left, right);
    }

    /**
     * 最近公共祖先
     * 位置：后序。需要左右子树的查找结果才能判断当前节点是否为 LCA。
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        // 后序位置：根据左右子树的结果判断
        if (left != null && right != null) return root;
        return left != null ? left : right;
    }

    /**
     * 从前序与中序遍历序列构造二叉树
     * 位置：前序 + 中序配合。
     * 前序的第一个值是根，在中序中找到根的位置分割左右子树。
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        valToIdx = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            valToIdx.put(inorder[i], i);
        }
        return build(preorder, 0, preorder.length - 1,
                0);
    }

    private Map<Integer, Integer> valToIdx;

    private TreeNode build(int[] pre, int preL, int preR,
                           int inL) {
        if (preL > preR) return null;
        int rootVal = pre[preL];
        int idx = valToIdx.get(rootVal);
        int leftSize = idx - inL;
        TreeNode root = new TreeNode(rootVal);
        // 前序位置确定根，中序位置确定左右子树范围
        root.left = build(pre, preL + 1, preL + leftSize,
                inL);
        root.right = build(pre, preL + leftSize + 1, preR,
                idx + 1);
        return root;
    }

}
