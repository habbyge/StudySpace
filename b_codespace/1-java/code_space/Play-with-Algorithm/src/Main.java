
public class Main {
	
	public static final int COUNT = 100000;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IUnionFind uf1 = new UnionFind1(COUNT);
		IUnionFind uf2 = new UnionFind2(COUNT);
		UnionFindHelper.testUF(COUNT,uf1);
		UnionFindHelper.testUF(COUNT,uf2);
	}

}
