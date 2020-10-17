## 【A】高级UI -- 自定义View（一）



对于自定义View的基本要求：

​	· xml中可以设置自定义属性

​	· Java代码中亦可以属性配置





### 自定义View关键步骤

#### 构造方法

> 几种构造方法的区别？







> TypeArray 用完后为什么要调用recycle() ？

```java
	类似Handler机制中的Message，通过享元模式，重复利用可能高频率"创建-使用-回收"的对象。避免内存抖动。
	具体来说，Resource对象内部持有一个SynchronizedPool<TypedArray> 对象缓存池，通过aquaire和release方法从池中存取重复利用对象。
```





#### onMeasure、onLayout、onDraw方法

View要实现 onMeasure、onDraw()

ViewGroup 要实现 onMeasure() 、onLayout() 其中onLayout是必须的。



#### MeasureSpec的意味

measureSpec与 LayoutParams关系？





#### 自定义View/ViewGroup解析





getMeasureWidth与getWidth的区别？



### 项目实践 -- 流式布局

setMeasureDimension(realWidth,realHeight)



### 项目实践 -- 处理自定义Viewgroup中 item高度设置无效的问题