package com.zhx._00_string;

public class BruteForce {

    public static int bruteForce(String s, String p) {
        System.out.println("bruteForce ");
        int index = -1;
        int sLength = s.length();
        int pLength = p.length();
        if (sLength < pLength) {
            System.out.println(" match failed . sLength < pLength");
            return index;
        }
        int i = 0;
        int j = 0;
        while (i < sLength && j < pLength) {
            if (s.charAt(i) == p.charAt(j)) {
                i++;
                j++;
            } else {
                i = i - j + 1;  //主串回溯j，再加1，从新开始匹配
                j = 0; // 字串归 0
            }
        }
        if (j == pLength) {
            index = i - pLength;
            System.out.println("match successful : index = " + index);
        }else{
            System.out.println(" match failed .not found ");
        }
        return index;
    }
}
