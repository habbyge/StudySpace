package kmp;
/**
 * 暴力匹配
 * @author Administrator
 *
 */
public class BruteFroce {
	
	public static int bruteForce(String s,String p) {
		long beginTime = System.currentTimeMillis();
		int index = -1; //成功匹配的位置，匹配不到返回-1
		int sLength = s.length();
		int pLength = p.length();
		if(sLength < pLength) {
			 System.out.println("Error.The main string is greater than the sub string length.");
			return -1;
		}
		int i = 0;//当前遍历主串的下标
		int j = 0;//当前遍历字串的下标
		//暴力遍历串
		while(i < sLength && j < pLength)  {
			if(s.charAt(i) == p.charAt(j)) {
				//字符相等，指针后移
				i++;
				j++;
			}else {
				//主串回到上次匹配的字符的下一个字符，子串从0开始
				i = i - j + 1;//需要归还i向前走的j步，然后加1
				j = 0; //无须关注子串向前走了多少步，直接归零
			}
		}
		
		if(j == pLength) { //匹配成功
			index = i - j;
			System.out.println("BF Successful match,index is:" + index);
		} else {// 匹配失败
            System.out.println("Match failed.");
        }
		long endTime = System.currentTimeMillis();
		System.out.println("BF cost time = "+(endTime - beginTime));
		return index;
	}
}
