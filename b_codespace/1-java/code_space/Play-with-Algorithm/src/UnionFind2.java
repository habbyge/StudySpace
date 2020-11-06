
/**
 * size 优化
 * @author Administrator
 *
 */
public class UnionFind2 implements IUnionFind{

	private int count;
	private int[] parent;
	private int[] sz;
	
	//constructor
	public UnionFind2(int n) {
		this.count = n;
		this.parent = new int[n];
		this.sz = new int[n];
		//initiate
		for (int i = 0; i < n; i++) {
			parent[i] = i;//每个元素指向自己
			sz[i] = 1;//每个数据集大小为1 
		}
	}
	
	/**
	 * 查找根节点
	 * @return
	 */
	private int find(int p) {
		assert(p >= 0 && p < count);
		while(p != parent[p]) {
			p = parent[p];
		}
		return p;
	}
	
	public boolean isConnected(int p , int q) {
		return find(p) == find(q);
	}
	
	public void union(int p , int q) {
		int pRoot = find(p);
		int qRoot = find(q);
		if(pRoot == qRoot) {
			return;
		}
		 // 根据两个元素所在树的元素个数不同判断合并方向
        // 将元素个数少的集合合并到元素个数多的集合上
		if(sz[p] < sz[q]) {
			parent[p] = qRoot;
			sz[qRoot] += sz[pRoot];
		}else {
			parent[q] = pRoot;
			sz[pRoot] += sz[qRoot]; 
		}
	}

	@Override
	public String method() {
		// TODO Auto-generated method stub
		return "UnionFind2";
	}
}






















