package com.zhx._00_string;

public class Kmp {
    /**
     * 构造next表
     *
     * @param p
     * @return
     */
    public static int[] buildNext(String p) {
        int pLength = p.length();
        int[] next = new int[pLength];
        next[0] = -1; // 把第0 位置空出来，直接赋值 -1 ；从而实现PMT表右移；
        int i = 0; //  后缀
        int j = -1; // 前缀
        while (i < pLength - 1) {
            if (j == -1 || p.charAt(i) == p.charAt(j)) {
                i++;
                j++;
                if (p.charAt(i) != p.charAt(j)) {
                    next[i] = j;
                } else {
                    next[i] = next[j];
                }
            } else {
                j = next[j]; // 如果失配，取出成功上轮匹配成功时保存的数，子串回溯到 next[j]
            }
        }
        for (int index = 0; index < next.length; index++) {
            System.out.println(" " + index + " = " + next[index]);
        }
        return next;
    }

    public static void kmp(String s, String p) {
        int[] next = buildNext(p);
        int index = -1;
        int sLength = s.length();
        int pLength = p.length();
        if (sLength < pLength) {
            return;
        }
        int i = 0;
        int j = 0;
        while (i < sLength && j < pLength) {
            if (j == -1 || s.charAt(i) == p.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j >= pLength) {
            index = i - j;
            System.out.println("KMP Successful match,index is:" + index);
        } else {
            System.out.println("KMP Failed . not Found");
        }
    }

}
