package com.algo.array;

public class Difference {
    private final int[] diff;

    public Difference(int[] nums) {
        diff = new int[nums.length];
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
    }

    public Difference increment(int left, int right, int val) {
        diff[left] += val;
        if (right + 1 < diff.length) {
            diff[right + 1] -= val;
        }
        return this;
    }

    public int[] result() {
        int[] res = new int[diff.length];
        res[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            res[i] = res[i - 1] + diff[i];
        }
        return res;
    }
}
