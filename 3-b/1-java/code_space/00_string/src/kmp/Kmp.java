package kmp;

/**
 * KMP算法
 *
 */
public class Kmp {
	//第一步：构造next表
	private static int[] buildNext(String p) {
		int[] N = new int[p.length()];
		int m = p.length();
		int j = 0;
		int t = N[0] = -1;
		while(j < m - 1) {
			if(t < 0 || p.charAt(j) == p.charAt(t)) {
				j++;
				t++;
				N[j] = t;
			}else {
				t = N[t];
			}
		}
		return N;
	}
	
	//第二步：利用next表尽量多地往右移动
	public static void kmp(String s ,String p) {
		long beginTime = System.currentTimeMillis();
		int[] next = buildNext(p);
		int index = -1;
		int sLength = s.length();
		int pLength = p.length();
		if(sLength < pLength) {
			return;
		}
		int i = 0;
		int j = 0;
		while(i < sLength && j <pLength) {
			if(j == -1 || s.charAt(i) == p.charAt(j)) {
				i++;
				j++;
			}else {
				j = next[j];
			}
		}
		if(j >= pLength) {
			 index = i -j;
			 System.out.println("KMP Successful match,index is:" + index);
		}else {
			 System.out.println("Match failed.");
		}
		long endTime = System.currentTimeMillis();
		System.out.println("KMP cost time = "+(endTime - beginTime));
	}
}













