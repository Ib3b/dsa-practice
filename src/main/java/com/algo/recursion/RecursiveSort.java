package com.algo.recursion;

/**
 * 快速排序与归并排序 —— 用递归思维来理解。
 *
 * 递归思维（分治）三步走：
 *   1. 定义子问题：给数组的一段区间排序。
 *   2. 拆解：将区间分为更小的子区间，分别排序（信任递归）。
 *   3. 合并：将子问题的解组合成原问题的解。
 *
 *   「信任递归」意味着：假设递归调用已经正确地对子区间排好了序，
 *   我们只关心「当前这层」要做什么。
 */
public class RecursiveSort {

    // ============================================================
    // 快速排序
    // ============================================================
    //
    // 递归思维：选一个基准(pivot)，小的放左边，大的放右边，
    //          然后递归地对左右两边排序。
    //
    // 位置：前序（先分区，再递归）——先处理当前区间，再交给子问题。
    // ───────────────────────────────────────────────
    // void sort(int[] arr, int lo, int hi) {
    //     if (lo >= hi) return;            // base case
    //     int p = partition(arr, lo, hi);  // 前序位置：先分区
    //     sort(arr, lo, p - 1);            // 信任递归：左边会排好
    //     sort(arr, p + 1, hi);            // 信任递归：右边会排好
    // }

    public void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    private void quickSort(int[] arr, int lo, int hi) {
        // base case：空的或只有一个元素的区间已经有序
        if (lo >= hi) return;

        // 前序位置：先分区（把基准元素放到正确位置上），
        //           再递归排序左右两边。
        int p = partition(arr, lo, hi);

        // 信任递归 —— 左右子区间会自己排好序
        quickSort(arr, lo, p - 1);
        quickSort(arr, p + 1, hi);
    }

    /**
     * 分区：选 arr[hi] 为基准，将区间分为三部分：
     *   [lo, i-1] < pivot,  [i] = pivot,  [i+1, hi] > pivot
     * 返回 pivot 最终位置 i。
     */
    private int partition(int[] arr, int lo, int hi) {
        int pivot = arr[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (arr[j] < pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, hi);
        return i;
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // ============================================================
    // 归并排序
    // ============================================================
    //
    // 递归思维：把数组从中间切开，分别排序两个子数组，
    //          再合并两个已排序的子数组。
    //
    // 位置：后序（先递归，再合并）——需要先得到子问题结果才能合并。
    // ───────────────────────────────────────────────
    // void sort(int[] arr, int lo, int hi) {
    //     if (lo >= hi) return;              // base case
    //     int mid = lo + (hi - lo) / 2;
    //     sort(arr, lo, mid);                // 信任递归：左半会排好
    //     sort(arr, mid + 1, hi);            // 信任递归：右半会排好
    //     merge(arr, lo, mid, hi);           // 后序位置：合并两个有序数组
    // }

    public void mergeSort(int[] arr) {
        if (arr.length < 2) return;
        temp = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1);
    }

    private int[] temp;  // 辅助空间

    private void mergeSort(int[] arr, int lo, int hi) {
        if (lo >= hi) return;

        int mid = lo + (hi - lo) / 2;

        mergeSort(arr, lo, mid);          // 信任递归：左半已排序
        mergeSort(arr, mid + 1, hi);      // 信任递归：右半已排序

        // 后序位置：左右都已排序，合并它们
        merge(arr, lo, mid, hi);
    }

    /**
     * 合并两个有序区间 [lo, mid] 和 [mid+1, hi]。
     * 这是归并排序的核心操作，本质上是 merge two sorted arrays。
     */
    private void merge(int[] arr, int lo, int mid, int hi) {
        // 拷贝到辅助数组
        for (int i = lo; i <= hi; i++) {
            temp[i] = arr[i];
        }

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                arr[k] = temp[j++];
            } else if (j > hi) {
                arr[k] = temp[i++];
            } else if (temp[i] <= temp[j]) {
                arr[k] = temp[i++];
            } else {
                arr[k] = temp[j++];
            }
        }
    }

}
