package com.algo.tree;

import com.algo.base.AlgorithmTestBase;
import com.algo.base.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeTraversalTest extends AlgorithmTestBase<TreeTraversalCase> {

    private final TreeTraversal tt = new TreeTraversal();

    @Test
    void testTraversals() {
        TestData<TreeTraversalCase> td = loadTestData("com/algo/tree/tree-traversal.yaml");
        verifyAll(td, tc -> {
            TreeNode root = TreeNode.fromLevelOrder(tc.inputs().tree());
            List<Integer> result = switch (tc.inputs().traversalType()) {
                case "pre"  -> tt.preorderRecursive(root);
                case "in"   -> tt.inorderRecursive(root);
                case "post" -> tt.postorderRecursive(root);
                default -> throw new IllegalArgumentException("unknown type: " + tc.inputs().traversalType());
            };
            assertArrayEquals(tc.expected(), result.stream().mapToInt(Integer::intValue).toArray());
        });
    }

    @Test
    void testIterative() {
        Integer[][] trees = {
            {3, 9, 20, null, null, 15, 7},
            {1},
            {1, null, 2, null, 3}
        };
        for (Integer[] arr : trees) {
            TreeNode root = TreeNode.fromLevelOrder(arr);
            assertEquals(tt.preorderRecursive(root),  tt.preorderIterative(root));
            assertEquals(tt.inorderRecursive(root),   tt.inorderIterative(root));
            assertEquals(tt.postorderRecursive(root), tt.postorderIterative(root));
        }
    }

    @Test
    void testLevelOrder() {
        TreeNode root = TreeNode.fromLevelOrder(new Integer[]{3, 9, 20, null, null, 15, 7});
        var levels = tt.levelOrder(root);
        assertEquals(List.of(3), levels.get(0));
        assertEquals(List.of(List.of(9, 20), List.of(15, 7)), levels.subList(1, 3));
    }

    @Test
    void testLevelOrderEmpty() {
        assertTrue(tt.levelOrder(null).isEmpty());
    }

    @Test
    void testLevelWithDepth() {
        TreeNode root = TreeNode.fromLevelOrder(new Integer[]{1, 2, 3, 4, 5, null, null});
        var result = tt.levelWithDepth(root);
        // 1@0, 2@1, 3@1, 4@2, 5@2
        assertArrayEquals(new int[]{1, 0}, result.get(0));
        assertArrayEquals(new int[]{2, 1}, result.get(1));
        assertArrayEquals(new int[]{3, 1}, result.get(2));
        assertArrayEquals(new int[]{4, 2}, result.get(3));

        var result2 = tt.levelWithDepth2(root);
        for (int i = 0; i < result.size(); i++) {
            assertArrayEquals(result.get(i), result2.get(i));
        }
    }

}
