package com.zhx._01_sort.bubble_sort;

import com.zhx._01_sort.ISort;
import com.zhx._01_sort.SortTestHelper;

/**
 * 冒泡排序
 */
public class BubbleSort implements ISort {

    @Override
    public int[] sort(int[] array) {
        System.out.println("BubbleSort start --- ");
        if (array.length == 0) {
            return array;
        }
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j + 1] > array[j]) {
                    int temp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temp;
                }
            }
        }
        SortTestHelper.printArray(array);
        System.out.println("BubbleSort end --- ");
        return array;
    }
}
