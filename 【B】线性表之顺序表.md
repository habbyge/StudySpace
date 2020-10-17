## 【B】线性表之顺序表



### 概念梳理

**数据结构 ：** 

​	相互之间存在一种或多种特定关系的数据元素的集合。



**逻辑结构与物理结构：**

​		逻辑结构是指数据对象中元素之间相互关系。包括:

1. 集合结构

2. 线性结构

3. 树形结构

4. 图形结构

   

   物理结构：是指数据的逻辑结构在计算机中的存储形式，包括：

   1. 顺序存储结构
   2. 链式存储结构



**数据结构研究的内容：**

1. 线性表 ： 0个或多个元素有序序列
2. 队列
3. 堆栈
4. 树
5. 图论
6. 排序和查找算法



### 线性表之顺序表

> ​		为什么会有线性表，线性表的基本原理是什么？

 线性表是指 0个或多个元素逻辑上有序序列，线性表的物理实现有顺序结构和链式结构两种。

 顺序表的特点是元素“一个接一个”地排队，头节点没有前驱、尾节点也没有后继，与顺序表相对的是链式存储结构 -- 链表



#### ArrayList

对数据结构的基本操作是增删改查，复杂操作有扩容、逆序等，为了更方便更高效地使用顺序存储结构，各很多语言都封装了自己的数据结构 ，ArrayList是Java中典型的顺序表数据结构，其内部封装了数组来保存数据元素。从ArrayList源码的角度分析顺序表数据结构及其增删改查操作：



##### ArrayList类继承关系

![image-20200921184104044](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200921184104044.png)



![image-20200922193631794](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200922193631794.png)



重点关注ArrayList的几个成员属性 -- 

- 存放元素的数组 Object[] elementData ，它是ArrayList作为顺序表的本质， int size属性则表示elementData中真实元素个数 , 声明如下：

  > ```java
  > transient Object[] elementData; 
  > ```
  >
  > 

- 

- serialVersionUID ： 表明ArrayList实现了Serializeable接口 -- 它是可以序列化的。由于elementData数组是可以扩容、自定义初始化大小的，为防止不必要空间浪费（比如故意把capacity设置得很大）所以elementData的声明是 transient 的，ArrayList实现了writeObject、readObject方法在写流和读流的时候，通过for循环读取真实元素个数，这样手动进行数组的序列化/反序列化。

```java
 		// Write out all elements in the proper order.
        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }
```





##### 增 -- add方法

直接在尾部增加元素：

```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  //  确保至少有size+1的空间大小，好往里放1个元素
    elementData[size++] = e;			// 扩容完后，把元素放到尾部
    return true;
}
```



```java
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity); // 最小容量DEFAULT_CAPACITY = 10
    }

    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```



扩容的核心方法：

```java
/**
 * Increases the capacity to ensure that it can hold at least the
 * number of elements specified by the minimum capacity argument.
 *
 * @param minCapacity the desired minimum capacity
 */
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);   // 扩容至oldCapacity的1.5倍大小
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;			// 扩容后容量仍不满足要求，则使用minCapacity的值
    if (newCapacity - MAX_ARRAY_SIZE > 0)  //超出了ArrayList所定义的最大容量，则调用hugeCapacity()
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);  //数组拷贝是耗时的操作，尽量提前预估容量，避免频繁扩容
}

/**
*	比较minCapacity和 MAX_ARRAY_SIZE，如果minCapacity大于MAX_ARRAY_SIZE，则新容量则为Integer.MAX_VALUE，否则，使用MAX_ARRAY_SIZE 即 Integer.MAX_VALUE - 8
*/
private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}
```



以上是尾部插入，在看中间插入的add方法：

```java
public void add(int index, E element) {
    if (index > size || index < 0)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));

    ensureCapacityInternal(size + 1);  // 扩容逻辑同上
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);		// index位置开始元素后移
    elementData[index] = element;		// 将目标元素放到目标位置index
    size++;
}
```



System.arrayCopy()方法：

```java
public static native void arraycopy(Object src,  int  srcPos,
                                    Object dest, int destPos,
                                    int length);
```





##### 删 -- remove方法

删除指定位置元素的remove方法：

```java
public E remove(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));

    modCount++;
    E oldValue = (E) elementData[index];

    int numMoved = size - index - 1;  // numMoved 表示需要移动的元素数量
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved); //index+1位置的元素至最后一个元素整体前移，覆盖掉index位置
    elementData[--size] = null; //将末尾元素位置置空，由 GC 回收内存

    return oldValue;
}
```



删除指定对象的remove方法：

```java
public boolean remove(Object o) {
    if (o == null) {
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {  //如果指定要删除的是null，查找第一个null的index并删除。其实也起到了判空效果
                fastRemove(index);
                return true;
            }
    } else {
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    return false;
}

/*
 * Private remove method that skips bounds checking and does not
 * return the value removed.
 */
private void fastRemove(int index) {
    modCount++;
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--size] = null; // clear to let GC do its work
}
```



顺便提一下ArrayList的缩容方法：

```java
public void trimToSize() {
    modCount++;
    if (size < elementData.length) {    //size代表实际存储的元素数量，
        elementData = (size == 0)
          ? EMPTY_ELEMENTDATA
          : Arrays.copyOf(elementData, size); //创建新的大小为size的数组并拷贝当前数组中所有元素，节约内存空间
    }
}
```



Arrays.copyOf方法是这样建新数组并拷贝数据的：

```java
public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
    T[] copy = ((Object)newType == (Object)Object[].class)
        ? (T[]) new Object[newLength]
        : (T[]) Array.newInstance(newType.getComponentType(), newLength);
    System.arraycopy(original, 0, copy, 0,
                     Math.min(original.length, newLength));
    return copy;
}
```



##### 改 -- set方法、查 -- get方法

ArrayList作为顺序表数据结构，其“改”和“查（随机访问）”的便捷性从方法实现中就可见一斑：

```java
/**
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public E get(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));

    return (E) elementData[index];
}

public E set(int index, E element) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));

    E oldValue = (E) elementData[index];  //取出index位置的元素并返回，index位置放入新元素
    elementData[index] = element;
    return oldValue;
}
```





##### 遍历方法

​	按性能降序排：

- 普通for循环

```java
List<Integer> list = new ArrayList<Integer>();
int size = list.size();
for(int j = 0; j < size; j++) {
	list.get(j);
}
```





- 显式调用迭代器：

```java
Iterator<Integer> iterator = list.iterator();
while(iterator.hasNext()) {
	Integer j = iterator.next();
}
```



- for each循环

```java
List<Integer> list = new ArrayList<Integer>();
for(Integer j : list) {

}
```

**foreach不必关心下标初始值和终止值及越界等，所以不易出错**。[Effective-Java](http://www.amazon.cn/Effective-Java中文版-Joshua-Bloch/dp/B001PTGR52/ref=as_li_wdgt_ex?&linkCode=wey&tag=trinea-23)中推荐使用此种写法遍历。

使用foreach结构的类对象必须实现了Iterable接口，Java的Collection继承自此接口，List实现了Collection，这个接口仅包含一个函数 -- iterator。



### Interview & Exercise

> 1. ArrayList的大小是如何自动增加的（如何扩容）？
>
> 2. 在什么情况下你会使用ArrayList?
>
> 3. 在索引中ArrayList的增加或者删除某个对象的运行过程？效率很低吗？解释一下为什么？
>
> 4. ArrayList如何顺序删除节点
>
> 5. 遍历ArrayList方法有哪些？
>
>    

#### 力扣时刻





### **参考**

https://www.cnblogs.com/aoguren/archive/2004/01/13/4765439.html





















