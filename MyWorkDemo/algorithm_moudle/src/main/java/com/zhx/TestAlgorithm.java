package com.zhx;

import com.zhx._00_string.BruteForce;
import com.zhx._00_string.Kmp;
import com.zhx._01_sort.ISort;
import com.zhx._01_sort.SortTestHelper;
import com.zhx._01_sort.bubble_sort.BubbleSort;

/**
 * 算法模块入口
 */
public class TestAlgorithm {
    public static void main(String[] args) {
        // 00_string
//        BruteForce.bruteForce(TestHelper.SOURCE,TestHelper.PATTERN);
//        Kmp.kmp(TestHelper.SOURCE,TestHelper.PATTERN);

        // 01_sort
        int[] oriArray = SortTestHelper.generateRandomArray(10, 0, 100);
        SortTestHelper.printArray(oriArray);
        ISort iSort = new BubbleSort();
        int[] sortedArray = iSort.sort(oriArray);
    }
}
