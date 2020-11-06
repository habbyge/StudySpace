
/**
 *       测试用例
 * @author Administrator
 *
 */
public class UnionFindHelper {
	

    // 测试第一版本的并查集, 测试元素个数为n, 测试逻辑和之前是完全一样的
    // 思考一下: 这样的冗余代码如何消除?
    // 由于这个课程不是设计模式课程, 在这里就不过多引入相关的问题讲解了。留作给大家的思考题:)
    public static void testUF(int n ,IUnionFind iUnionFind){

        long startTime = System.currentTimeMillis();

        // 进行n次操作, 每次随机选择两个元素进行合并操作
        for( int i = 0 ; i < n ; i ++ ){
            int a = (int)(Math.random()*n);
            int b = (int)(Math.random()*n);
            iUnionFind.union(a,b);
        }
        // 再进行n次操作, 每次随机选择两个元素, 查询他们是否同属一个集合
        for(int i = 0 ; i < n ; i ++ ){
            int a = (int)(Math.random()*n);
            int b = (int)(Math.random()*n);
            iUnionFind.isConnected(a,b);
        }
        long endTime = System.currentTimeMillis();

        // 打印输出对这2n个操作的耗时
        System.out.println(iUnionFind.method()+", " + 2*n + " ops, " + (endTime-startTime) + "ms");
    }

}
