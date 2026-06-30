package com.algo.tree;

import com.algo.base.AlgorithmTestBase;
import com.algo.base.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeRecursionTest extends AlgorithmTestBase<TreeRecursionCase> {

    private final TreeRecursion tr = new TreeRecursion();

    @Test
    void testDepthAlgorithms() {
        TestData<TreeRecursionCase> td = loadTestData("com/algo/tree/tree-recursion.yaml");
        verifyAll(td, tc -> {
            Integer[] arr = tc.inputs().tree();
            TreeNode root = (arr == null || arr.length == 0) ? null
                : TreeNode.fromLevelOrder(arr);
            int result = switch (tc.inputs().algorithm()) {
                case "maxDepth" -> tr.maxDepth(root);
                case "minDepth" -> tr.minDepth(root);
                default -> throw new IllegalArgumentException();
            };
            assertEquals(tc.expected(), result);
        });
    }

    // ========== 布尔类递归算法（inline 测试） ==========

    @Test
    void testIsBalanced() {
        // balanced: [3,9,20,null,null,15,7]
        var b1 = TreeNode.fromLevelOrder(new Integer[]{3, 9, 20, null, null, 15, 7});
        assertTrue(tr.isBalanced(b1));

        // unbalanced: [1,2,2,3,3,null,null,4,4]
        var u1 = TreeNode.fromLevelOrder(new Integer[]{1, 2, 2, 3, 3, null, null, 4, 4});
        assertFalse(tr.isBalanced(u1));
    }

    @Test
    void testIsSymmetric() {
        // symmetric
        var s1 = TreeNode.fromLevelOrder(new Integer[]{1, 2, 2, 3, 4, 4, 3});
        assertTrue(tr.isSymmetric(s1));

        // not symmetric
        var s2 = TreeNode.fromLevelOrder(new Integer[]{1, 2, 2, null, 3, null, 3});
        assertFalse(tr.isSymmetric(s2));

        assertTrue(tr.isSymmetric(null));
    }

    @Test
    void testHasPathSum() {
        var root = TreeNode.fromLevelOrder(new Integer[]{5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1});
        assertTrue(tr.hasPathSum(root, 22));   // 5→4→11→2
        assertTrue(tr.hasPathSum(root, 26));   // 5→8→13
        assertFalse(tr.hasPathSum(root, 99));
        assertFalse(tr.hasPathSum(null, 0));
    }

    // ========= 树变换类递归算法（inline 测试） ==========

    @Test
    void testInvertTree() {
        var root = TreeNode.fromLevelOrder(new Integer[]{4, 2, 7, 1, 3, 6, 9});
        var inv = tr.invertTree(root);
        // inverted: 4,7,2,9,6,3,1
        assertEquals(List.of(4, 7, 9, 6, 2, 3, 1),
            new TreeTraversal().preorderRecursive(inv));
    }

    @Test
    void testDiameter() {
        var root = TreeNode.fromLevelOrder(new Integer[]{1, 2, 3, 4, 5});
        assertEquals(3, tr.diameterOfBinaryTree(root));

        assertEquals(0, tr.diameterOfBinaryTree(null));
        assertEquals(0, tr.diameterOfBinaryTree(new TreeNode(1)));
    }

    @Test
    void testLowestCommonAncestor() {
        // 树: [3,5,1,6,2,0,8,null,null,7,4]
        var root = TreeNode.fromLevelOrder(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        var node5 = root.left;
        var node1 = root.right;
        var node4 = root.left.right.right;

        assertEquals(root, tr.lowestCommonAncestor(root, node5, node1));
        assertEquals(node5, tr.lowestCommonAncestor(root, node5, node4));
    }

    @Test
    void testBuildTree() {
        int[] pre = {3, 9, 20, 15, 7};
        int[] in  = {9, 3, 15, 20, 7};
        TreeNode root = tr.buildTree(pre, in);
        assertEquals(List.of(3, 9, 20, 15, 7),
            new TreeTraversal().preorderRecursive(root));
    }

}
