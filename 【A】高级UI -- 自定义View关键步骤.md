## 【A】高级UI -- 自定义View关键步骤

> 自我管理应该着重于精力的管理，而非时间的安排 。



### 自定义View知识脉络

#### 构造方法

> 几种构造方法的区别？







> TypeArray 用完后为什么要调用recycle() ？

```java
	类似Handler机制中的Message，通过享元模式，重复利用可能高频率"创建-使用-回收"的对象。避免内存抖动。
	具体来说，Resource对象内部持有一个SynchronizedPool<TypedArray> 对象缓存池，通过aquaire和release方法从池中存取重复利用对象。
```





#### 核心方法 ：onMeasure、onLayout、onDraw

View要实现 onMeasure、onDraw()

ViewGroup 要实现 onMeasure() 、onLayout() 其中onLayout是必须的。





> 为什么要measure ？ 为什么measure是不可或缺的？









#### MeasureSpec的意味

> measureSpec是什么 ？ measureSpec与 LayoutParams关系？
>















#### 自定义View/ViewGroup解析

getMeasureWidth与getWidth的区别？







处理自定义Viewgroup中 item高度设置无效的问题







### 项目实践 -- 流式布局

setMeasureDimension(realWidth,realHeight)