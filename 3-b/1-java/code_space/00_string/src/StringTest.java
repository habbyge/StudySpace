import kmp.BruteFroce;
import kmp.Kmp;

public class StringTest {

	public static final String s = "\r\n\"			+ \\\"suffix(prefix(P, j), t), 0�� t �� j } shift = j (P, j), t)\\\"\\r\\n\" + \r\n" + 
			"			+ \"shift = j (P, j), t) = suffix(prefix(P, j), t), 0�� A t �� j } B \"\r\n";
	public static final String pattern = "A t �� j } B";
	
	public static void main(String[] args) {
		BruteFroce.bruteForce(s,pattern);
		Kmp.kmp(s, pattern);
	}
}
