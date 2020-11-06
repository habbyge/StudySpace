
/**
 * 并查集基础实现
 * @apiNote 设计思想：
 * 		构建一棵指向父节点的树，显示地使用数组s[] 存储
 * 		每个元素i对应的值s[i]表示元素所指向的父节点
 * 		根节点的特征：parent == p
 */
public class UnionFind1 implements IUnionFind{

	private int[] parent;
	private int count;
	
	//constructor
	public UnionFind1(int count) {
		parent = new int[count];
		this.count = count;
		
		//initiate
		for(int i=0;i<count;i++) {
			parent[i]=i; //每个元素指向自己，自成集合
		}
	}
	
	/**
	 * 查找元素p
	 * @param p
	 * O(h)复杂度, h为树的高度
	 */
	private int find(int p) {
		assert(p>=0 && p<count);
		//不断追溯自己的父节点，直到根节点，根节点的特征：parent == p
		while(p != parent[p]) {
			p = parent[p];//继续向上追溯
		}
		return p;
	}
	
	/**
	 * 查看元素p和元素q是否属于一个集合（是否连接）
	 * O(h)复杂度, h为树的高度
	 */
	public boolean isConnected(int p, int q) {
		return find(p) == find(q);
	}
	/**
	 * 合并元素p和元素q所属的集合
	 * @param p
	 * @param q
	 * O(h)复杂度, h为树的高度
	 */
	public void union(int p ,int q) {
		int pRoot = find(p);
		int qRoot = find(q);
		if(pRoot == qRoot) {
			return;
		}
		parent[pRoot] = qRoot;
	}

	@Override
	public String method() {
		// TODO Auto-generated method stub
		return "UnionFind1";
	}
}






























