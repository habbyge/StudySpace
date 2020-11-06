## 【B】Java -- 多线程并发



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



### 输出倒逼输入























### volatile关键字























































