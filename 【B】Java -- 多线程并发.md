## 【B】Java -- 多线程并发



### 输出倒逼输入

- 怎样获取当前线程是否是主线程
- 有三个线程在做计算，把结果发送给第四个线程
- 主线程等待所有线程执行完毕，再执行某个特定任务怎么实现？原理和源码看过没？
- 俩线程分别持续打印奇数和偶数，实现俩线程的交替打印（从小到大）
- wait和sleep的区别
- 三个线程wait，唤醒情况是什么样的
- **线程池的理解，Java线程池有哪些，以及创建线程池的几个关键参数 ？自己实现的话怎样去做**
- threadlocal了解吗
- 两个线程用不同的对象，怎么样
- **多线程和多进程产生的并发问题有什么区别** 
- **线程同步的问题，常用的线程同步**

- **volatile 的作用和原理 ？ 一个 int 变量用 volatile 修饰，多线程去操作 i++，是否线程安全？如何保证 i++ 线程安全？（什么叫指令重排）**
- 为什么Java设计了volatile这个关键词。为什么我们不在每个变量的前面加上volatile。
- **Integer类是不是线程安全的，为什么AtomicInteger 的底层实现原理？**

- **不使用同步锁如何实现线程安全**

- **Java的生产者-消费者模型** （非阻塞式）
- **synchroinzed的原理是什么 ？ synchronized 修饰 static 方法、普通方法、类、方法块区别**

- synchroinzed关键字和lock的区别? 他们对线程的控制原理简单说下
- .写一个死锁的程序。面试官就一直让我解释这段代码。考察得很细致。
- java锁的模型 ？乐观锁使用
- ReentrantLock 公平锁与非公平锁。
- **针对concurrent包下面的一些类的问题**
- CurrentHashMap原理， 读写锁是如何实现的？初始化大小是多少？
-   







### 知识体系

java中主要以多线程方式实现并发

- 并发基础：
  - AQS:
    1. AbstractqueuedSynchronizer同步器
    2. CLH同步队列
    3. 同步状态的获取和释放
    4. 线程阻塞和唤醒
  - CAS: Compare and Swap 缺陷
- Java内存模型JMM：线程通信、消息传递
- 内存模型：
  - 重排序
  - 顺序一致性
  - happens-before
  - as if serial
- synchronized：
  - 同步、重量级锁、`synchronized`原理
  - 锁优化：
    1. 自旋锁
    2. 轻量级锁
    3. 重量级锁
    4. 偏向锁
- 原子操作：
  - 基本类型：`AtomicBoolean`、`AtomicInteger`、`AtomicLong`
  - 数组：`AtomicIntegerArray`、`AtomicLongArray`、`AtomicReferenceArray`
  - 引用类型：`AtomicReference`、`AtomicReferenceArrayFieldUpdater`
- 线程池：
  - `ThreadPoolExecutor`（拒绝策略、参数优化）、`ScheduledExecutorService`
  - `Callable`和`Future`
- 并发集合：`ConcurrentHashMap`、`CopyOnWriteArrayList`、`ConcurrentLinkedQueue`、`BlockingQueue`、`ConcurrentSkipListMap`等
- 并发工具类：`Semaphore`、`CyclicBarrier`、`CountDownLatch`
- 锁：`ReentrantLock`、`Condition`、`ReentrantReadWriteLock`、`LockSupport`
- volatile：
  - `volatile`实现机制
  - 内存语义
  - 内存模型
- 其他：
  - `ThreadLocal`
  - `Fork/Join`



### volatile关键字







### CurrentHashMap



> CurrentHashMap 读写锁是如何实现的？

```
CurrentHashMap 读写锁是如何实现的？ 答 ：如果没有hash冲突的情况下，使用CAS进行插入，如果有hash冲突，使用synchronized加锁进行插入。当链表长度大于8且数据长度>=64时，会使用红黑树替代链表。 ReentrantReadWriteLock类中有readLock()和writeLock()方法可以分别获取WriteLock和ReadLock对象，调用他们的lock方法，就可以实现读写锁。实现原理就是AQS的acquire和acquireShared方法。
```







### 资源参考

- [Java 锁机制](http://blog.csdn.net/guuinbelieve/article/details/79587799)
- 





### 























































