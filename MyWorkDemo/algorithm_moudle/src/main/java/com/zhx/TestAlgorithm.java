package com.zhx;

import com.zhx._00_string.BruteForce;
import com.zhx._00_string.Kmp;

/**
 * 算法模块入口
 */
public class TestAlgorithm {
    public static void main(String[] args) {
//        BruteForce.bruteForce(TestHelper.SOURCE,TestHelper.PATTERN);
        Kmp.kmp(TestHelper.SOURCE,TestHelper.PATTERN);
    }
}
