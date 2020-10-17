> ​		接天莲叶无穷碧，映日荷花别样红

## 【B】线性表之链表

​		链式存储结构的特点是用一组任意的存储单元存储线性表的数据元素，这组存储单元物理上可以是连续的也可以是不连续的。



### 单链表

像小朋友排队，"向前看齐"，手只贴到前一个小朋友的后背，不知道后边是谁。

#### 单链表的节点：

![](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200924114513871.png)

   与顺序存储结构相反，链式存储结构增、删是非常便捷、高效的。增删改查简单过程示意图如下：

#### ![](.\images\b_linked_table_01.png)



#### 单链表应用 -- JDK1.7 版本 HashMap 中hash冲突的处理方式

静态内部类Entry保存了Key和Value，HashMap 用Entry数组存储元素 ，Entry的结构是:

```java
 static class Entry<K,V> implements Map.Entry<K,V> {
        final K key;
        V value;
        Entry<K,V> next; //可见是个单链表数据结构，指针指向下一个节点
        int hash;

        /**
         * Creates new entry.
         */
        Entry(int h, K k, V v, Entry<K,V> n) {
            value = v;
            next = n; 
            key = k;
            hash = h;
        }
 }
```



由成员属性 Entry<K,V> next;  可见Entry类是个单链表数据结构，next指向下一个Entry节点。

这由于HashMap put操作向数组存储元素时，哈希碰撞总是无法完全避免，所以采用了 数组 + 链表的形式， 让哈希碰撞的键值对组成链表。同时为了避免尾部遍历，新插入的键值对是插入到链表头部的。

```java
 public V put(K key, V value) {
        if (key == null)
            return putForNullKey(value);
        int hash = hash(key);  //对key hash
        int i = indexFor(hash, table.length); //寻table下标index
        for (Entry<K,V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, key, value, i);
        return null;
    }

void createEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K,V> e = table[bucketIndex]; //对照Entry构造方法可知，拿到当前链表头节点
        table[bucketIndex] = new Entry<>(hash, key, value, e); //完成头插操作
        size++;
}
```











### 双链表

两个反向的单链表结合到一起，像小朋友手拉手，联系着前一个和后一个。

![image-20200924162559392](.\images\b_linked_table_02.png)



![image-20200924172728430](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200924172728430.png)

上图过程对于P节点而言是“尾插”操作：

1. 先断开P节点与其后的节点的连接 -- 先改变P节点的后继节点的指向

2. 狸猫换太子 -- 与P节点建立连接

   

对于P后继节点（称之为Q点）而言是“头插”操作：

1.  s.prev = q.prev ;  
2. s.next = q;
3. q.prev.next = s;
4. q.prev = s;





#### 双链表应用 -- LinkedList



节点结构 -- Link类：

```java
private static final class Link<ET> {
    ET data;

    Link<ET> previous, next;

    Link(ET o, Link<ET> p, Link<ET> n) {
        data = o;
        previous = p;
        next = n;
    }
}
```



add节点：

```java
public void add(int location, E object) {
    if (location >= 0 && location <= size) {
        Link<E> link = voidLink;
        if (location < (size / 2)) {		//对目标location二分判断，离头节点近还是尾近
            for (int i = 0; i <= location; i++) {  //拿到当前location处的节点元素
                link = link.next;
            }
        } else {
            for (int i = size; i > location; i--) {
                link = link.previous;
            }
        }
          //BEGIN -- 头插操作 
        Link<E> previous = link.previous; //1. 取前置节点
        Link<E> newLink = new Link<E>(object, previous, link);//2.构造新节点
        previous.next = newLink;
        link.previous = newLink;
         //END -- 头插操作 
        size++;
        modCount++;
    } else {
        throw new IndexOutOfBoundsException();
    }
}
```



remove节点：

```java
public E remove(int location) {
    if (location >= 0 && location < size) {
        Link<E> link = voidLink;
        if (location < (size / 2)) {
            for (int i = 0; i <= location; i++) {
                link = link.next;
            }
        } else {
            for (int i = size; i > location; i--) {
                link = link.previous;
            }
        }
         //BEGIN --remove操作 
        Link<E> previous = link.previous; 
        Link<E> next = link.next;
        previous.next = next;
        next.previous = previous;
          //END --remove操作 
        size--;
        modCount++;
        return link.data;
    }
    throw new IndexOutOfBoundsException();
}
```





### List 总结

![image-20200924173112711](.\images\b_linked_table_03.png)



1）List是一个接口，他继承于Collection接口。它代表着有序的队列

2）AbstractList 是一个抽象类，他继承于AbstractCollection AbstractList实现List接口中除size()、get(int location)之外的函数。

3）AbstractSequentialList是一个抽象类，他继承于AbstractList。实现了链表中根据索引值操作链表的全部函数。

4）ArrayList、LinkedList、Vector、Stack是List的4个实现类









### Interview&Exercise

**1.手写一个单链表，并且实现单链表元素的逆置，（a0, a1,a2,a3,..an）-> (an,an-1,… a1, a0),算法的空间复杂度和时间复杂度最低。**

////TODO





































**2.手写双向链表， 实现增删改查，同时对比自己的LinkList 和源码Linkedlist的异同点**

//// TODO













3.对比Arraylist 和LinkedList 的区别与优缺点。

|        | 优点                                        | 缺点                                         |
| ------ | ------------------------------------------- | -------------------------------------------- |
| 顺序表 | 存储空间连续 ；允许随机访问；尾插、尾删方便 | 插入、删除效率低；长度固定（需要扩容）       |
| 单链表 | 随意增删改；插删效率高；长度随意不需扩容    | 内存不连续，不能随机查找（需要遍历，性能差） |
| 双链表 | 同上；查找效率效率比单链表快一倍            | 应用：LinkedList                             |











#### 力扣时刻

https://leetcode.com/problems/merge-two-sorted-lists/



https://leetcode.com/problems/swap-nodes-in-pairs/



https://leetcode.com/problems/copy-list-with-random-pointer/











































