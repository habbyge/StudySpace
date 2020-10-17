## 【B】计组 -- 第三部分:  存储与IO

### 理解存储器（寄存器）的层次结构

​		我们常常把 CPU 比喻成计算机的“大脑”。我们思考的东西，就好比 CPU 中的寄存器（Register）。寄存器与其说是存储器，其实它更像是 CPU 本身的一部分，只能存放极其有限的信息，但是速度非常快，和 CPU 同步。 而我们大脑中的记忆，就好比 CPU Cache（CPU 高速缓存，我们常常简称为“缓存”）。CPU Cache 用的是一种叫作 SRAM（Static Random-Access Memory，静态随机存取存储器）的芯片。



#### SRAM

​	在 CPU 里，通常会有 L1、L2、L3 这样三层高速缓存。每个 CPU 核心都有一块属于自己的 L1 高速缓存，通常分成指令缓存和数据缓存，分开存放 CPU 使用的指令和数据。

​	L1 的 Cache 往往就嵌在 CPU 核心的内部。L2 的 Cache 同样是每个 CPU 核心都有的，不过它往往不在 CPU 核心的内部。所以，L2 Cache 的访问速度会比 L1 稍微慢一些。而 L3 Cache，则通常是多个 CPU 核心共用的，尺寸会更大一些，访问速度自然也就更慢一些。



#### DRAM

​	内存用的芯片和 Cache 有所不同，它用的是一种叫作 **DRAM**（Dynamic Random Access Memory，动态随机存取存储器）的芯片，比起 SRAM 来说，它的密度更高，有更大的容量，而且它也比 SRAM 芯片便宜不少。

​	SSD（Solid-state drive 或 Solid-state disk，固态硬盘）、HDD（Hard Disk Drive，硬盘）这些被称为硬盘的外部存储设备，就是公共图书馆。

​	**各个存储器只和相邻的一层存储器打交道，并且随着一层层向下，存储器的容量逐层增大，访问速度逐层变慢**



#### Cache 的数据结构和访问逻辑

​		CPU 从内存中读取数据到 CPU Cache 的过程中，是一小块一小块来读取数据的，而不是按照单个数组元素来读取数据的。这样一小块一小块的数据，在 CPU Cache 里面，我们把它叫作 **Cache Line**（缓存块）。

​		对于读取内存中的数据，我们首先拿到的是数据所在的内存块（Block）的地址。而直接映射 Cache 采用的策略，就是**确保任何一个内存块的地址，始终映射到一个固定的 CPU Cache 地址**（Cache Line）。而这个映射关系，通常用 mod 运算（求余运算）来实现。

![img](.\images\522eade51bbfad19fd25eb4f3ce80f22.png)



​		实际计算中，有一个小小的技巧，通常我们会把**缓存块的数量设置成 2 的 N 次方**。这样在计算取模的时候，可以直接取地址的低 N 位，也就是二进制里面的后几位。比如这里的 8 个缓存块，就是 2 的 3 次方。那么，在对 21 取模的时候，可以对 21 的 2 进制表示 10101 取地址的**低三位**，也就是 101，对应的 5，就是对应的缓存块地址。这个时候，在对应的缓存块中，我们会存储一个组标记（Tag）。只存高2位就可以了。

​		除了组标记信息之外，缓存块中还有两个数据。一个自然是从主内存中加载来的实际存放的数据，另一个是**有效位**（valid bit）。它其实就是用来标记，对应的缓存块中的数据是否是有效的。如果有效位是 0，无论其中的组标记和 Cache Line 里的数据内容是什么，CPU 都不会管这些数据，而要直接访问内存，重新加载数据。

​		CPU 在读取数据的时候，并不是要读取一整个 Block，而是读取一个他需要的数据片段。这样的数据，我们叫作 CPU 里的一个**字**（Word）。具体是哪个字，就用这个字在整个 Block 里面的位置来决定。这个位置，我们叫作**偏移量**（Offset）。

​		总结一下，**一个内存的访问地址，最终包括高位代表的组标记、低位代表的索引，以及在对应的 Data Block 中定位对应字的位置偏移量。**

![img](.\images\1313fe1e4eb3b5c949284c8b215af8d4.png)





#### 从高速缓存角度理解Java中的volatile

> 常见的对volatile 两种错误理解:
>
> 1. 把volatile关键词，当成是一个锁，可以把long/double这样的数的操作自动加锁
> 2. 把volatile关键词，当成可以让整数自增的操作也变成原子性的



实际上，volatile 关键字的最核心知识点，要关系到 Java 内存模型（JMM，Java Memory Model）上。

虽然 JMM 只是 Java 虚拟机这个进程级虚拟机里的一个内存模型，但是这个内存模型，和计算机组成里的 CPU、高速缓存和主内存组合在一起的硬件体系非常相似。理解了 JMM，可以让你很容易理解计算机组成里 CPU、高速缓存和主内存之间的关系。



##### “隐身”的变量

实验程序：

```java

public class VolatileTest {
    private static volatile int COUNTER = 0;

    public static void main(String[] args) {
        new ChangeListener().start();
        new ChangeMaker().start();
    }
	
    /**
    *	监听COUNTER变量值变化
    */
    static class ChangeListener extends Thread {
        @Override
        public void run() {
            int threadValue = COUNTER;
            while ( threadValue < 5){
                if( threadValue!= COUNTER){
                    System.out.println("Got Change for COUNTER : " + COUNTER + "");
                    threadValue= COUNTER;
                }
            }
        }
    }

    /**
    *	打印COUNTER ,加 1 并休眠500ms
    */
    static class ChangeMaker extends Thread{
        @Override
        public void run() {
            int threadValue = COUNTER;
            while (COUNTER <5){
                System.out.println("Incrementing COUNTER to : " + (threadValue+1) + "");
                COUNTER = ++threadValue;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
}
```



打印结果

```shell
Incrementing COUNTER to : 1
Got Change for COUNTER : 1
Incrementing COUNTER to : 2
Got Change for COUNTER : 2
Incrementing COUNTER to : 3
Got Change for COUNTER : 3
Incrementing COUNTER to : 4
Got Change for COUNTER : 4
Incrementing COUNTER to : 5
Got Change for COUNTER : 5
```



当把volatile关键字去掉后：

```java
Incrementing COUNTER to : 1
Incrementing COUNTER to : 2
Incrementing COUNTER to : 3
Incrementing COUNTER to : 4
Incrementing COUNTER to : 5
```

而且程序没有结束 -- ChangeListener thread一直在盲等。



为ChangeListener加上睡眠, 打印就和上边一样了。

```java
static class ChangeListener extends Thread {
		@Override public void run() { 
			int threadValue = COUNTER; 
			while ( threadValue < 5){
				if( threadValue!= COUNTER){
					System.out.println("Got Change for COUNTER : " + COUNTER + "");
					threadValue= COUNTER; 
				} 
			 	try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
}
```



Thread.sleep() 会使线程让出CPU ， 休眠结束，再被CPU执行的时候，所有变量就又从内存获取了一遍。

**volatile 关键字则是会确保我们对于这个变量的读取和写入，都一定会同步到主内存里，而不是从Cache里面获取。**



##### CPU 高速缓存的写入

​		第一个问题是，写入 Cache 的性能也比写入主内存要快，那我们写入的数据，到底应该写到 Cache 里还是主内存呢？如果我们直接写入到主内存里，Cache 里的数据是否会失效呢？



**写直达（Write-Through）策略**

![img](.\images\8b9ad674953bf36680e815247de235d3.jpeg)

在这个策略里，每一次数据都要写入到主内存里面。在写直达的策略里面，写入前，我们会先去判断数据是否已经在 Cache 里面了。如果数据已经在 Cache 里面了，我们先把数据写入更新到 Cache 里面，再写入到主内存里面；如果数据不在 Cache 里，我们就只更新主内存。这个方式就有点儿像我们上面用 volatile 关键字，始终都要把数据同步到主内存里面。



**写回（Write-Back）策略**

![img](.\images\67053624d6aa2a5c27c295e1fda4890d.jpeg)



写回策略的过程是这样的：如果发现我们要写入的数据，就在 CPU Cache 里面，那么我们就只是更新 CPU Cache 里面的数据。同时，我们会标记 CPU Cache 里的这个 Block 是脏（Dirty）的。所谓脏的，就是指这个时候，我们的 CPU Cache 里面的这个 Block 的数据，和主内存是不一致的。



然而，无论是写回还是写直达，其实都还没有解决我们在上面 volatile 程序示例中遇到的问题，也就是**多个线程，或者是多个 CPU 核的缓存一致性的问题。这也就是我们在写入修改缓存后，需要解决的第二个问题。**



#### 缓存一致性问题

![img](E:\personal\study\blogs\images\a6146ddd5c78f2cbc1af56b0ee3292da.jpeg)



​	比方说，iPhone 降价了，我们要把 iPhone 最新的价格更新到内存里。为了性能问题，它采用了上一讲我们说的写回策略，先把数据写入到 L2 Cache 里面，然后把 Cache Block 标记成脏的。这个时候，数据其实并没有被同步到 L3 Cache 或者主内存里。1 号核心希望在这个 Cache Block 要被交换出去的时候，数据才写入到主内存里。

​	如果我们的 CPU 只有 1 号核心这一个 CPU 核，那这其实是没有问题的。不过，我们旁边还有一个 2 号核心呢！这个时候，2 号核心尝试从内存里面去读取 iPhone 的价格，结果读到的是一个错误的价格。这是因为，iPhone 的价格刚刚被 1 号核心更新过。但是这个更新的信息，只出现在 1 号核心的 L2 Cache 里，而没有出现在 2 号核心的 L2 Cache 或者主内存里面。**这个问题，就是所谓的缓存一致性问题，1 号核心和 2 号核心的缓存，在这个时候是不一致的。**



解决这个问题的机制需要满足两个条件：

第一点叫**写传播（Write Propagation）**。写传播是说，在一个 CPU 核心里，我们的 Cache 数据更新，必须能够传播到其他的对应节点的 Cache Line 里。

第二点叫**事务的串行化（Transaction Serialization）**，事务串行化是说，我们在一个 CPU 核心里面的读取和写入，在其他的节点看起来，顺序是一样的。



#### 总线嗅探机制和 MESI 协议

要解决缓存一致性问题，首先要解决的是多个 CPU 核心之间的数据传播问题。最常见的一种解决方案，叫作**总线嗅探**（Bus Snooping）本质上就是把所有的读写请求都通过总线（Bus）广播给所有的 CPU 核心，然后让各个核心去“嗅探”这些请求，再根据本地的情况进行响应。

​	有一种叫作**写广播（Write Broadcast）**的协议。在那个协议里，一个写入请求广播到所有的 CPU 核心，同时更新各个核心里的 Cache。

​	相对而言， **MESI** 协议是一种叫作**写失效（Write Invalidate）**的协议。它来自于我们对Cache Line的四个不同标记：

M：代表已修改（Modified）

E：代表独占（Exclusive）

S：代表共享（Shared）

I：代表已失效（Invalidated）

在写失效协议里，只有一个 CPU 核心负责写入数据，其他的核心，只是同步读取到这个写入。在这个 CPU 核心写入 Cache 之后，它会去广播一个“失效”请求告诉所有其他的 CPU 核心。其他的 CPU 核心，只是去判断自己是否也有一个“失效”版本的 Cache Block，然后把这个也标记成失效的就好了。写失效的协议的好处是，我们不需要在总线上传输数据内容，而只需要传输操作信号和地址信号就好了，不会那么占总线带宽。



![img](E:\personal\study\blogs\images\4ed6d05049cbbc8603346f617206cd59.jpeg)



​	展开来讲 ， “已修改”和“已失效”，这两个状态比较容易理解。所谓的“已修改”，就是我们上一讲所说的“脏”的 Cache Block。Cache Block 里面的内容我们已经更新过了，但是还没有写回到主内存里面。而所谓的“已失效“，自然是这个 Cache Block 里面的数据已经失效了，我们不可以相信这个 Cache Block 里面的数据。

​	“独占”和“共享”这两个状态。这就是 MESI 协议的精华所在了。无论是独占状态还是共享状态，缓存里面的数据都是“干净”的。“独占”即是说数据只存在于当前CPU ，对自己的Cache Block写入数据不需要发广播给别的CPU ；而在 “共享” 状态下 因为同样的数据在多个 CPU 核心的 Cache 里都有。所以，当我们想要更新 Cache 里面的数据的时候，不能直接修改，而是要先向所有的其他 CPU 核心广播一个请求，要求先把其他 CPU 核心里面的 Cache，都变成无效的状态，然后再更新当前 Cache 里面的数据。这个广播操作，一般叫作 **RFO**（Request For Ownership），也就是获取当前对应 Cache Block 数据的所有权。







### 理解内存（上）：虚拟内存和内存保护

虚拟内存地址究竟是怎么转换成物理内存地址的呢？



#### 简单页表

想要把虚拟内存地址，映射到物理内存地址，最直观的办法，就是来建一张映射表。这个映射表，能够实现虚拟内存里面的页，到物理内存里面的页的一一映射。这个映射表，在计算机里面，就叫作**页表**（Page Table）。

页表这个地址转换的办法，一个 32 位的内存地址为例，前面的高位，就是内存地址的**页号**。后面的低位，就是内存地址里面的**偏移量**。同一个页里面的内存，在物理层面是连续的。以一个页的大小是 4K 字节（4KB）为例，我们需要 20 位的高位，12 位的低位。举例：

![img](E:\personal\study\blogs\images\22bb79129f6363ac26be47b35748500f.jpeg)



总结一下，对于一个内存地址转换，其实就是这样三个步骤：

1. 把虚拟内存地址，切分成页号和偏移量的组合；
2. 从页表里面，查询出虚拟页号，对应的物理页号；
3. 直接拿物理页号，加上前面的偏移量，就得到了物理内存地址。





问题来了。32 位的内存地址空间，页表一共需要记录 2^20 个到物理页号的映射关系。这个存储关系，就好比一个 2^20 大小的数组。一个页号是完整的 32 位的 4 字节（Byte），这样一个页表就需要 4MB 的空间。我们每一个进程，都有属于自己独立的虚拟内存地址空间。这也就意味着，每一个进程都需要这样一个页表。不管我们这个进程，是个本身只有几 KB 大小的程序，还是需要几 GB 的内存空间，都需要这样一个页表。





#### 多级页表

​		事实上，我们只需要去存那些用到的页之间的映射关系就好了。这是一种叫作**多级页表**（Multi-Level Page Table）的解决方案。因其像一个多叉树的数据结构，所以我们常常称它为**页表树**（Page Table Tree）

为什么我们不用哈希表而用多级页表呢？在一个实际的程序进程里面，虚拟内存占用的地址空间，通常是两段连续的空间。而不是完全散落的随机的内存地址。而多级页表，就特别适合这样的内存地址分布。



![img](.\images\614034116a840ef565feda078d73cb76.jpeg)





![img](.\images\5ba17a3ecf3f9ce4a65546de480fcc4e.jpeg)





### 解析TLB和内存保护

​		“地址转换”是一个非常高频的动作，“地址转换”的性能就变得至关重要了。这就是我们今天要讲的第一个问题，也就是性能问题。我们的指令、数据都存放在内存里面，第二个问题，也就是内存安全问题。现代的 CPU 和操作系统，会通过什么样的方式来解决这两个问题呢？



#### 加速地址转换：TLB

​		多级页表数据结构虽然节约了存储空间，但是却带来了时间上的开销，变成了一个“以时间换空间”的策略。用了 4 级页表，我们就需要访问 4 次内存，才能找到物理页号。

​	CPU 里放了一块缓存芯片。这块缓存芯片我们称之为 **TLB**，全称是**地址变换高速缓冲（**Translation-Lookaside Buffer）。这块缓存存放了之前已经进行过地址转换的查询结果。当同样的虚拟地址需要进行地址转换的时候，我们可以直接在 TLB 里面查询结果，而不需要多次访问内存来完成一次转换。

​	为了性能，整个内存转换过程也要由硬件来执行。在 CPU 芯片里面，封装了**内存管理单元**（MMU，Memory Management Unit）芯片，用来完成地址转换。和 TLB 的访问和交互，都是由这个 MMU 控制的。

![img](.\images\432050446f68569a37c7699cccda75d9.jpeg)



#### 安全性与内存保护

​		第一个常见的安全机制，叫**可执行空间保护**（Executable Space Protection）。

​		第二个常见的安全机制，叫**地址空间布局随机化**（Address Space Layout Randomization）。













