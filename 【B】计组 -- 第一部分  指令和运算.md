## 【B】计组 -- 第一部分： 计算机基本组成、指令和运算

### 计算机组成原理学科定位

![img](.\images\aa5f644331319421eb7549d67d4f8773.jpeg)

### 计算机组成原理知识大纲

![b_poco_00](E:\personal\study\blogs\images\b_poco_00.png)



### 开篇 ： 计算机基本组成

#### 冯诺依曼体系结构

![img](.\images\fa8e0e3c96a70cc07b4f0490bfe66f2b.jpeg)



学习组成原理，就是在理解从控制器、运算器、存储器、输入设备以及输出设备，从电路这样的硬件，到最终开放给软件的接口，是怎么运作的，为什么要设计成这样，以及在软件开发层面怎么尽可能用好它。除此之外，你还需要了解计算机的两个核心指标，性能和功耗。性能和功耗也是我们在应用和设计五大基本组件中需要重点考虑的因素。



还有一个很特殊的设备，就是显卡（Graphics Card）。现在的主板都带了内置的显卡。如果你用计算机玩游戏，做图形渲染或者跑深度学习应用，你多半就需要买一张单独的显卡，插在主板上。显卡之所以特殊，是因为显卡里有除了 CPU 之外的另一个“处理器”，也就是 GPU（Graphics Processing Unit，图形处理器），GPU 一样可以做各种“计算”的工作。





#### 计算机性能 -- 时间的倒数

​		计算机性能两个标准：1. 响应时间/执行时间  2. 吞吐率。

​		Linux系统下 time命令可以计算出程序程序运行时间，像掐秒表一样用记录程序结束时间减去程序开始运行时间。这个时间也叫 **Wall Clock Time 或者 Elapsed Time.**

```shell
$ time seq 1000000 | wc -l
1000000

real  0m0.101s  // 即 Wall Clock Time
user  0m0.031s	// CPU 在运行你的程序，在用户态运行指令的时间
sys   0m0.016s	// CPU 在运行你的程序，在操作系统内核里运行指令的时间
```



> ​		程序实际占用CPU的时间，即**程序的CPU执行时间(CPU Time)** = user time + sys time; 

​		 

 Elapsed Time （Wall Clock Time ） 与 CPU执行时间(CPU Time) 差值的时间包括线程切换消耗的时间,执行其他线程消耗的时间,还包括从网络或者硬盘上读取数据的时间,以及从内存加载数据到CPU的时间.



**Cpu 主频与计算机性能**

​		单纯地比较 Elapsed Time 也是不准确的，时间这个性能还会受到主板、内存等硬件地影响。

​		需要把指标拆解成：

> ​		程序的 CPU 执行时间 = 时钟周期时间 * CPU 时钟周期数

​		

​		CPU 时钟周期时间 = 主频的倒数，比如Intel Core-i7-7700HQ 2.8GHz 的 CPU 上，这个时钟周期时间，就是 1/2.8G。

​		CPU 时钟周期数 = 指令数  ×  每条指令的平均时钟周期数（Cycles Per Instruction，简称 CPI）。

​		

​		不同的指令需要的 Cycles 是不同的，加法和乘法都对应着一条 CPU 指令，但是乘法需要的 Cycles 就比加法要多，自然也就慢。在		这样拆分了之后，我们的程序的 CPU 执行时间就可以变成这样三个部分的乘积 ：

​      

> ​		程序的 CPU 执行时间 = 指令数  ×  CPI ×  Clock Circle Time



​		总结CPU性能优化三方面：

		1. 提升计算机主频，降低时钟周期时间。摩尔定律提出集成电路上可容纳的晶体管数目，约每隔两年便会增加一倍，有预测认为摩尔定律的极限将在2025年左右到来。且由于功耗原因，一味提升CPU主频，以提升性能的做法已经不可行。
  		2. 降低每条指令的平均时钟周期数CPI 。 现代的 CPU 通过**流水线技术（Pipeline）**，让一条指令需要的 CPU Cycle 尽可能地少。
    		3. 指令数。

​	

#### 计算机功耗

> ​				功耗 ~= 1/2 ×负载电容×电压的平方×开关频率×晶体管数量	



为了提升主频，增加晶体管密度，晶体管从28nm 缩小到了7nm。但功耗也提升了，导致CPU散热跟不上，所以需要降低电压（功耗和电压的平方成正比）。到我们的 CPU 在奔腾 4 的年代，提升速度就已经不再容易了。



于是目光转向性能指标另一条  -- 吞吐率，多核CPU应运而生 ， 通过**并行计算**提高性能。



如果想要使用并行计算提高性能，需要满足这样几个条件。

第一，需要进行的计算，本身可以分解成几个可以并行的任务。好比上面的乘法和加法计算，几个人同时进行，不会影响最后的结果。

第二，需要能够分解好问题，并确保几个人的结果能够汇总到一起。

第三，在“汇总”这个阶段，是没有办法并行进行的，还是得顺序执行，一步一步来。



并行优化 -- 阿姆达尔定律（Amdahl’s Law）：

​		优化后的执行时间 = 受优化影响的执行时间/加速倍数 + 不受影响的执行时间

​		

​		

在“摩尔定律”和“并行计算”之外，在计算机组成层面，还有几个原则性的性能提升方法：

1. **加速大概率事件**

   最典型的就是深度学习，整个计算过程中，99% 都是向量和矩阵计算，于是，工程师们通过用 GPU 替代 CPU，大幅度提升了深度学习的模型训练过程。本来一个 CPU 需要跑几小时甚至几天的程序，GPU 只需要几分钟就好了。Google 更是不满足于 GPU 的性能，进一步地推出了 TPU。

2. **通过流水线提高性能**

   我们的 CPU 其实就是一个“运算工厂”。我们把 CPU 指令执行的过程进行拆分，细化运行。我们在后面也会讲到，现代 CPU 里是如何通过流水线来提升性能的，以及反面的，过长的流水线会带来什么新的功耗和效率上的负面影响。

3. **通过预测提高性能**

   通过预先猜测下一步该干什么，而不是等上一步运行的结果，提前进行运算，也是让程序跑得更快一点的办法。







### 第一部分：指令和运算

​		CPU 就是一个执行各种计算机指令（Instruction Code）的逻辑机器。这里的计算机指令，就好比一门 CPU 能够听得懂的语言，我们也可以把它叫作机器语言（Machine Language）。



##### **从编译到汇编，代码怎么变成机器码？**

Linux 操作系统上跑起来，我们需要把整个程序翻译成一个汇编语言（ASM，Assembly Language）的程序，这个过程我们一般叫编译（Compile）成汇编代码。针对汇编代码，我们可以再用汇编器（Assembler）翻译成机器码（Machine Code）。这些机器码由“0”和“1”组成的机器语言表示。这一条条机器码，就是一条条的计算机指令。这样一串串的 16 进制数字，就是我们 CPU 能够真正认识的计算机指令。



![img](.\images\67cf3c90ac9bde229352e1be0db24b5b.png)





##### **解析指令和机器码**

常见的指令可以分成五大类。

1. 算术类指令。我们的加减乘除，在 CPU 层面，都会变成一条条算术类指令。
2. 数据传输类指令。给变量赋值、在内存里读写数据，用的都是数据传输类指令。
3. 逻辑类指令。逻辑上的与或非，都是这一类指令。
4. 条件分支类指令。日常我们写的“if/else”，其实都是条件分支类指令。
5. 无条件跳转指令。我们常常需要写一些函数或者方法。在调用函数的时候，其实就是发起了一个无条件跳转指令。



![img](.\images\ebfd3bfe5dba764cdcf871e23b29f197.jpeg)



​	不同的 CPU 有不同的指令集，也就对应着不同的汇编语言和不同的机器码。我们选用最简单的 MIPS 指令集，来看看机器码是如何生成的。MIPS 的指令是一个 32 位的整数，高 6 位叫操作码（Opcode），也就是代表这条指令具体是一条什么样的指令，剩下的 26 位有三种格式，分别是 R、I 和 J 格式。

![img](.\images\b1ade5f8de67b172bf7b4ec9f63589bf.jpeg)

​	

**R 指令**是一般用来做算术和逻辑操作，里面有读取和写入数据的寄存器的地址。如果是逻辑位移操作，后面还有位移操作的位移量，而最后的功能码，则是在前面的操作码不够的时候，扩展操作码表示对应的具体指令的。

**I 指令**，则通常是用在数据传输、条件分支，以及在运算的时候使用的并非变量还是常数的时候。这个时候，没有了位移量和操作码，也没有了第三个寄存器，而是把这三部分直接合并成了一个地址值或者一个常数。

**J 指令**就是一个跳转指令，高 6 位之外的 26 位都是一个跳转后的地址。



以简单的加法算术指令作为示例：

```c
add t0,s1, $s2
```

add 对应的 MIPS 指令里 opcode 是 0，rs 代表第一个寄存器 s1 的地址是 17，rt 代表第二个寄存器 s2 的地址是 18，rd 代表目标的临时寄存器 t0 的地址，是 8。因为不是位移操作，所以位移量是 0。把这些数字拼在一起，就变成了一个 MIPS 的加法指令。

![img](.\images\8fced6ff11d3405cdf941f6742b5081d.jpeg)



#### 指令跳转：原来if...else就是goto

##### CPU 是如何执行指令的？

​		逻辑上，我们可以认为，CPU 其实就是由一堆寄存器组成的。而寄存器就是 CPU 内部，由多个触发器（Flip-Flop）或者锁存器（Latches）组成的简单电路。触发器和锁存器，其实就是两种不同原理的数字电路组成的逻辑门。N 个触发器或者锁存器，就可以组成一个 N 位（Bit）的寄存器，能够保存 N 位的数据。比方说，我们用的 64 位 Intel 服务器，寄存器就是 64 位的。一个 CPU 里面会有很多种不同功能的寄存器。

![img](.\images\cdba5c17a04f0dd5ef05b70368b9a96f.jpg)



**PC 寄存器（Program Counter Register）**，我们也叫**指令地址寄存器（Instruction Address Register）**。顾名思义，它就是用来存放下一条需要执行的计算机指令的内存地址。

**指令寄存器（Instruction Register）**，用来存放当前正在执行的指令。

**条件码寄存器（Status Register）**，用里面的一个一个标记位（Flag），存放 CPU 进行算术或者逻辑计算的结果。



​		一个程序执行的时候，CPU 会根据 PC 寄存器里的地址，从内存里面把需要执行的指令读取到指令寄存器里面执行，然后根据指令长度自增，开始顺序读取下一条指令。可以看到，一个程序的一条条指令，在内存里面是连续保存的，也会一条条顺序加载。而有些特殊指令，比如J类指令，也就是跳转指令，会修改PC寄存器里边的地址值。这也是我们可以在程序中使用条件语句 if ...else 和 while/for 循环语句的原因。



##### 从 if…else 来看程序的执行和跳转

```c
#include <time.h>
#include <stdlib.h>

int main()
{
  srand(time(NULL));
  int r = rand() % 2;
  int a = 10;
  if (r == 0)
  {
    a = 1;
  } else {
    a = 2;
  } 
}
```

对应的汇编代码是这样的：

```assembly

    if (r == 0)
  3b:   83 7d fc 00             cmp    DWORD PTR [rbp-0x4],0x0
  3f:   75 09                   jne    4a <main+0x4a>
    {
        a = 1;
  41:   c7 45 f8 01 00 00 00    mov    DWORD PTR [rbp-0x8],0x1
  48:   eb 07                   jmp    51 <main+0x51>
    }
    else
    {
        a = 2;
  4a:   c7 45 f8 02 00 00 00    mov    DWORD PTR [rbp-0x8],0x2
  51:   b8 00 00 00 00          mov    eax,0x0
    } 
```

​	可以看到，这里对于 r == 0 的条件判断，被编译成了 cmp 和 jne 这两条指令。

​	cmp 指令比较了前后两个操作数的值，这里的 DWORD PTR 代表操作的数据类型是 32 位的整数，而[rbp-0x4]则是一个寄存器的地址，意思就是从寄存器里拿到的变量 r 的值。第二个操作数 0x0 就是我们设定的常量 0 的 16 进制表示。cmp 指令的比较结果，会存入到条件码寄存器当中去。如果比较的结果是 True，就把**零标志条件码**（对应的条件码是 ZF，Zero Flag）设置为 1。

​	cmp 指令执行完成之后，PC 寄存器会自动自增，开始执行下一条 jne 的指令。jne 指令，是 jump if not equal 的意思。它会查看对应的零标志位。如果为 0，会跳转到后面跟着的操作数 4a 的位置。跳转到执行地址为 4a 的指令，可以看到它是一条 mov 指令，赋值操作。

​	



#### 函数调用：为什么会发生stack overflow ？

##### 为什么我们需要程序栈？

```c
#include <stdio.h>
int static add(int a, int b)
{
    return a+b;
}


int main()
{
    int x = 5;
    int y = 10;
    int u = add(x, y);
}
```

编译成汇编代码：

```Assembly 

int static add(int a, int b)
{
   0:   55                      push   rbp
   1:   48 89 e5                mov    rbp,rsp
   4:   89 7d fc                mov    DWORD PTR [rbp-0x4],edi
   7:   89 75 f8                mov    DWORD PTR [rbp-0x8],esi
    return a+b;
   a:   8b 55 fc                mov    edx,DWORD PTR [rbp-0x4]
   d:   8b 45 f8                mov    eax,DWORD PTR [rbp-0x8]
  10:   01 d0                   add    eax,edx
}
  12:   5d                      pop    rbp
  13:   c3                      ret    
0000000000000014 <main>:
int main()
{
  14:   55                      push   rbp
  15:   48 89 e5                mov    rbp,rsp
  18:   48 83 ec 10             sub    rsp,0x10
    int x = 5;
  1c:   c7 45 fc 05 00 00 00    mov    DWORD PTR [rbp-0x4],0x5
    int y = 10;
  23:   c7 45 f8 0a 00 00 00    mov    DWORD PTR [rbp-0x8],0xa
    int u = add(x, y);
  2a:   8b 55 f8                mov    edx,DWORD PTR [rbp-0x8]
  2d:   8b 45 fc                mov    eax,DWORD PTR [rbp-0x4]
  30:   89 d6                   mov    esi,edx
  32:   89 c7                   mov    edi,eax
  34:   e8 c7 ff ff ff          call   0 <add>
  39:   89 45 f4                mov    DWORD PTR [rbp-0xc],eax
  3c:   b8 00 00 00 00          mov    eax,0x0
}
  41:   c9                      leave  
  42:   c3                      ret    
```

​	add 函数编译之后，代码先执行了一条 push 指令和一条 mov 指令；在函数执行结束的时候，又执行了一条 pop 和一条 ret 指令。这四条指令的执行，其实就是在进行**压栈（Push）**和**出栈（Pop）**操作。



![img](.\images\d0c75219d3a528c920c2a593daaf77be.jpeg)

压栈的不只有函数调用完成后的返回地址。比如函数 A 在调用 B 的时候，需要传输一些参数数据，这些参数数据在寄存器不够用的时候也会被压入栈中。整个函数 A 所占用的所有内存空间，就是函数 A 的栈帧（Stack Frame）。

而实际的程序栈布局，顶和底与我们的乒乓球桶相比是倒过来的。底在最上面，顶在最下面，这样的布局是因为栈底的内存地址是在一开始就固定的。而一层层压栈之后，栈顶的内存地址是在逐渐变小。-- 栈的开口向下







##### 如何构造一个 stack overflow？

- ​	 函数无限递归，致使递归层数过深
- ​	在栈空间里面创建非常占内存的变量（比如一个巨大的数组）





##### 如何利用函数内联进行性能优化？

> 被调用的函数里没有调用其他函数，就可以把一个实际调用的函数产生的指令，直接插入到的它被调用的位置 -- 函数内联（Inline)。

​	在 GCC 编译的时候，加上对应的一个让编译器自动优化的参数 -O 或者程序中定义函数的地方使用 inline 的关键字，来提示编译器对函数进行内联。

​	内联带来的优化是，CPU 需要执行的指令数变少了，根据地址跳转的过程不需要了，压栈和出栈的过程也不用了。

​	内联带来的问题是：我们把可以复用的程序指令在调用它的地方完全展开了。如果一个函数在很多地方都被调用了，那么就会展开很多次，整个程序占用的空间就会变大了。





#### ELF和静态链接：为什么程序无法同时在Linux和Windows下运行 ？

##### 编译、链接和装载：拆解程序执行

​		事实上， “C 语言代码 - 汇编代码 - 机器码” 这个过程，在我们的计算机上进行的时候是由两部分组成的。

第一个部分由编译（Compile）、汇编（Assemble）以及链接（Link）三个阶段组成。在这三个阶段完成之后，我们就生成了一个可执行文件。

第二部分，我们通过装载器（Loader）把可执行文件装载（Load）到内存中。CPU 从内存中读取指令和数据，来开始真正执行程序。

![img](.\images\997341ed0fa9018561c7120c19cfa2a7.jpg)





##### ELF 格式和链接：理解链接过程

​		即便是可执行文件代码，也并不仅仅是一条条的指令。在 Linux 下，可执行文件和目标文件所使用的都是一种叫 **ELF（Execuatable and Linkable File Format）**的文件格式，中文名字叫可执行与可链接文件格式，这里面不仅存放了编译成的汇编指令，还保留了很多别的数据。用 objdump命令来查看可执行文件的内容：

```assembly

link_example:     file format elf64-x86-64
Disassembly of section .init:
...
Disassembly of section .plt:
...
Disassembly of section .plt.got:
...
Disassembly of section .text:
...

 6b0:   55                      push   rbp
 6b1:   48 89 e5                mov    rbp,rsp
 6b4:   89 7d fc                mov    DWORD PTR [rbp-0x4],edi
 6b7:   89 75 f8                mov    DWORD PTR [rbp-0x8],esi
 6ba:   8b 55 fc                mov    edx,DWORD PTR [rbp-0x4]
 6bd:   8b 45 f8                mov    eax,DWORD PTR [rbp-0x8]
 6c0:   01 d0                   add    eax,edx
 6c2:   5d                      pop    rbp
 6c3:   c3                      ret    
00000000000006c4 <main>:
 6c4:   55                      push   rbp
 6c5:   48 89 e5                mov    rbp,rsp
 6c8:   48 83 ec 10             sub    rsp,0x10
 6cc:   c7 45 fc 0a 00 00 00    mov    DWORD PTR [rbp-0x4],0xa
 6d3:   c7 45 f8 05 00 00 00    mov    DWORD PTR [rbp-0x8],0x5
 6da:   8b 55 f8                mov    edx,DWORD PTR [rbp-0x8]
 6dd:   8b 45 fc                mov    eax,DWORD PTR [rbp-0x4]
 6e0:   89 d6                   mov    esi,edx
 6e2:   89 c7                   mov    edi,eax
 6e4:   b8 00 00 00 00          mov    eax,0x0
 6e9:   e8 c2 ff ff ff          call   6b0 <add>
 6ee:   89 45 f4                mov    DWORD PTR [rbp-0xc],eax
 6f1:   8b 45 f4                mov    eax,DWORD PTR [rbp-0xc]
 6f4:   89 c6                   mov    esi,eax
 6f6:   48 8d 3d 97 00 00 00    lea    rdi,[rip+0x97]        # 794 <_IO_stdin_used+0x4>
 6fd:   b8 00 00 00 00          mov    eax,0x0
 702:   e8 59 fe ff ff          call   560 <printf@plt>
 707:   b8 00 00 00 00          mov    eax,0x0
 70c:   c9                      leave  
 70d:   c3                      ret    
 70e:   66 90                   xchg   ax,ax
...
Disassembly of section .fini:
...
```



函数名称，像 add、main 等等，乃至你自己定义的全局可以访问的变量名称，在 ELF 文件里面，存储在一个叫作符号表（Symbols Table）的位置里。符号表相当于一个地址簿，把名字和地址关联了起来。先只关注和我们的 add 以及 main 函数相关的部分。会发现，这里面，main 函数里调用 add 的跳转地址，不再是下一条指令的地址了，而是 add 函数的入口地址了，这就是 EFL 格式和链接器的功劳。

![img](.\images\276a740d0eabf5f4be905fe7326d9fb3.jpg)

ELF 文件格式把各种信息，分成一个一个的 Section 保存起来。ELF 有一个基本的文件头（**File Header**），用来表示这个文件的基本属性，比如是否是可执行文件，对应的 CPU、操作系统等等。除了这些基本属性之外，大部分程序还有这么一些 Section：

1. 首先是.text Section,也叫作**代码段**或者指令段（Code Section），用来保存程序的代码和指令；
2. 接着是.data Section，也叫**数据段**（Data Section），用来保存程序里面设置好的初始化数据信息；
3. 然后是.rel.text Section，也叫**重定位表**（Relocation Table）。重定位表里，保留的是当前文件里面，哪些跳转地址其实是我们不知道的。在链接发生之前，我们并不知道该跳转到哪里，
4. 最后是.symtab Section，叫作**符号表**（Symbol Table）。保留了我们所说的当前文件里面定义的函数名称和对应地址的地址薄 。



​	链接器会扫描所有输入的目标文件，然后把所有符号表里的信息收集起来，构成一个全局的符号表。然后再根据重定位表，把所有不确定要跳转地址的代码，根据符号里面存储的地址，再一次修正。最后，把所有的目标文件的对应段进行一次合并，变成最终的可执行代码。这也是为什么，可执行文件里面的函数调用的地址都是正确的。

![img](.\images\f62da9b29aa53218f8907851df27f912.jpeg)



​	链接器把程序变成可执行文件之后，装载器去执行程序不再需要考虑地址跳转问题，只需要解析 ELF 文件，把对应的指令和数据，加载到内存里边供CPU执行就可以了。



##### 总结

​		为什么同样一个程序，在 Linux 下可以执行而在 Windows 下不能执行了。其中一个非常重要的原因就是，两个操作系统下可执行文件的格式不一样。我们今天讲的是 Linux 下的 ELF 文件格式，而 Windows 的可执行文件格式是一种叫作 PE（Portable Executable Format）的文件格式。Linux 下的装载器只能解析 ELF 格式而不能解析 PE 格式。如果我们有一个可以能够解析 PE 格式的装载器，我们就有可能在 Linux 下运行 Windows 程序了。这样的程序真的存在吗？没错，Linux 下著名的开源项目 Wine，就是通过兼容 PE 格式的装载器，使得我们能直接在 Linux 下运行 Windows 程序的。而现在微软的 Windows 里面也提供了 WSL，也就是 Windows Subsystem for Linux，可以解析和加载 ELF 格式的文件。

​		Java程序之所以能跨平台，是因为Java实现了不同平台的虚拟机，Java只需javac编译成中间代码，在虚拟机上跑起来就行了。





#### 装载器与内存

​		过链接器，把多个文件合并成一个最终可执行文件。在运行这些可执行文件的时候，我们其实是通过一个装载器，解析 ELF 或者 PE 格式的可执行文件。装载器会把对应的指令和数据加载到内存里面来，让 CPU 去执行。装载器需要满足两个要求：



1. **可执行程序加载后占用的内存空间应该是连续的。**执行指令的时候，程序计数器是顺序地一条一条指令执行下去。这也就意味着，着一条条指令需要连续地存储在一起。
2. **我们需要同时加载多个程序，并不能让程序自己规定在内存中加载的位置。**虽然编译出来的指令里已经有了对应的各种各样的内存地址，但是实际加载的时候，我们其实没有办法确保，这个程序一定加载在哪一段内存地址上。因为我们现在的计算机通常会同时运行很多个程序，可能你想要的内存地址已经被其他加载了的程序占用了。



​	我们把指令里用到的内存地址叫作**虚拟内存地址**，实际在内存硬件里面的空间地址，叫**物理内存地址**。我们维护一个虚拟内存到物理内存的映射表，这样实际程序指令执行的时候，会通过虚拟内存地址找到对应的物理内存地址，然后执行。这种找出一段连续的物理内存和虚拟内存地址进行映射的方法，叫作**分段**（Segmentation），这里的段，就是指系统分配出来的那个连续的内存空间。

![img](.\images\24596e1e66d88c5d077b4c957d0d7f18.png)





分段办法虽好，但是有**内存碎片**（Memory Fragmetnation）的问题。

![img](.\images\57211af3053ed621aeb903433c6c10d1.png)



内存碎片问题，由**内存交换**（Memory Swapping）来解决。将上图中python程序占用的那256M内存，先写到硬盘上的某个分区，然后再读回来，不过读回来的时候不加载到原来的位置，而是接着已经被占用的512M内存（橘黄色部分）的后面，这样原来分开的两块128M内存碎片合为一块连续的256M内存了。Linux中有专门的swap硬盘分区



内存交换的空间如果太大，会造成整机卡顿。那么解决问题的办法就是，少出现一些内存碎片。另外，当需要进行内存交换的时候，让需要交换写入或者从磁盘装载的数据更少一点，这样就可以解决这个问题。这个办法，就叫作**内存分页**（Paging）。

**分段是分配一整段连续的空间给到程序，而分页是把整个物理内存空间切成一段段固定尺寸的大小。**对应的程序需要占用的虚拟内存空间，也会切成一段段固定尺寸的大小。这样一个连续并且尺寸固定的内存空间，我们叫**页**（Page）。从虚拟内存到物理内存的映射不再是拿整段连续的内存的物理地址，而是按照一个一个页来的。在Linux下，通常只设置成4k。可以通过命令查看：

```shell
$ getconf PAGE_SIZE
```

![img](.\images\0cf2f08e1ceda473df71189334857cf0.png)



通过**引入虚拟内存、页映射和内存交换**，这种内存使用优化技术我们的程序本身，就不再需要考虑对应的真实的内存地址、程序加载、内存管理等问题了。任何一个程序，都只需要把内存当成是一块完整而连续的空间来直接使用。





#### 动态链接 ： 程序内部的“共享单车”

​		程序的链接是把对应的不同文件内的代码段，合并到一起，成为最后的可执行文件，此为**静态链接（Static Link）**。虽然代码得到了“复用”，但是多次链接就需要多次装载进内存，显然是可以优化的。

![img](.\images\092dfd81e3cc45ea237bb85557bbfa51.jpg)

##### 链接可以分动、静，共享运行省内存		

**动态链接**的链接目标是加载到内存中的**共享库**(Shared Libraries) 。在 Windows 下，这些共享库文件就是.dll 文件，也就是 Dynamic-Link Libary（DLL，动态链接库）。在 Linux 下，这些共享库文件就是.so 文件，也就是 Shared Object（一般我们也称之为动态链接库）。

![img](.\images\2980d241d3c7cbfa3724cb79b801d160.jpg)





##### 地址无关很重要，相对地址解烦恼

​		我们编译出来的共享库文件的指令代码，必须是**地址无关码**（Position-Independent Code）。换句话说就是，这段代码，无论加载在哪个内存地址，都能够正常执行。

​		在动态链接对应的共享库，我们在共享库的 data section 里面，保存了一张**全局偏移表**（GOT，Global Offset Table）。**虽然共享库的代码部分的物理内存是共享的，但是数据部分是各个动态链接它的应用程序里面各加载一份的。**所有需要引用当前共享库外部的地址的指令，都会查询 GOT，来找到当前运行程序的虚拟内存里的对应位置。而 GOT 表里的数据，则是在我们加载一个个共享库的时候写进去的。

​		不同的进程，调用同样的 lib.so，各自 GOT 里面指向最终加载的动态链接库里面的虚拟内存地址是不同的。这样，虽然不同的程序调用的同样的动态库，各自的内存地址是独立的，调用的又都是同一个动态库，但是不需要去修改动态库里面的代码所使用的地址，而是各个程序各自维护好自己的 GOT，能够找到对应的动态库就好了。

​	objdump 查看动态库指令码（只关注main函数中 show_me_the_money函数调用）

```assembly
……
0000000000400540 <show_me_the_money@plt-0x10>:
  400540:       ff 35 12 05 20 00       push   QWORD PTR [rip+0x200512]        # 600a58 <_GLOBAL_OFFSET_TABLE_+0x8>
  400546:       ff 25 14 05 20 00       jmp    QWORD PTR [rip+0x200514]        # 600a60 <_GLOBAL_OFFSET_TABLE_+0x10>
  40054c:       0f 1f 40 00             nop    DWORD PTR [rax+0x0]

0000000000400550 <show_me_the_money@plt>:
  400550:       ff 25 12 05 20 00       jmp    QWORD PTR [rip+0x200512]        # 600a68 <_GLOBAL_OFFSET_TABLE_+0x18>
  400556:       68 00 00 00 00          push   0x0
  40055b:       e9 e0 ff ff ff          jmp    400540 <_init+0x28>
……
0000000000400676 <main>:
  400676:       55                      push   rbp
  400677:       48 89 e5                mov    rbp,rsp
  40067a:       48 83 ec 10             sub    rsp,0x10
  40067e:       c7 45 fc 05 00 00 00    mov    DWORD PTR [rbp-0x4],0x5
  400685:       8b 45 fc                mov    eax,DWORD PTR [rbp-0x4]
  400688:       89 c7                   mov    edi,eax
  40068a:       e8 c1 fe ff ff          call   400550 <show_me_the_money@plt>
  40068f:       c9                      leave  
  400690:       c3                      ret    
  400691:       66 2e 0f 1f 84 00 00    nop    WORD PTR cs:[rax+rax*1+0x0]
  400698:       00 00 00 
  40069b:       0f 1f 44 00 00          nop    DWORD PTR [rax+rax*1+0x0]
……
```

![img](.\images\1144d3a2d4f3f4f87c349a93429805c8.jpg)	

​	GOT 表位于共享库自己的数据段里。GOT 表在内存里和对应的代码段位置之间的偏移量，始终是确定的。这样，我们的共享库就是地址无关的代码，对应的各个程序只需要在物理内存里面加载同一份代码。而我们又要通过各个可执行程序在加载时，生成的各不相同的 GOT 表，来找到它需要调用到的外部变量和函数的地址。



> 1. 为什么要采用 PLT 和 GOT 两级跳转，直接用 GOT 有问题吗？
>
>     PLT是为了做延迟绑定，如果函数没有实际被调用到，就不需要更新GOT里面的数值。因为很多动态装载的函数库都是不会被实际调用到的。

> 2.   共享库在内存中也是采用分页机制么？ 如果是的话，那么怎么解决多进程同时调用共享库的问题呢？
>
>    共享库在内存中也是采用分页机制的。
>    同时调用共享库只要对应的指令代码是PIC的也就是地址无关的，并不会有什么问题。但是两个进程的数据段是不共享的而已。







#### 二进制编码：“手持两把锟斤拷，口中疾呼烫烫烫”？

​		一个 4 位的二进制数， 0011 就表示为 +3。而 1011 最左侧的第一位是 1，所以它就表示 -3。这个其实就是整数的**原码表示法**。

![image-20200930080310023](.\images\image-20200930080310023.png)



​		ASCII 码就好比一个字典，用 8 位二进制中的 128 个不同的数，映射到 128 个不同的字符里。比如，小写字母 a 在 ASCII 里面，就是第 97 个，也就是二进制的 0110 0001，对应的十六进制表示就是 61。

![img](.\images\bee81480de3f6e7181cb7bb5f55cc805.png)







####  浮点数和定点数

一个加法，无论是在 Python 还是在 JavaScript 里面，算出来的结果居然不是准确的 0.9 :

```python
>>> 0.3 + 0.6
0.8999999999999999
```



计算机通常用 16/32 个比特（bit）来表示一个数, 32个比特 只能表示 2 的 32 次方个不同的数.



##### 浮点数的表示

​		有一个 IEEE 的标准，它定义了两个基本的格式。一个是用 32 比特表示单精度的浮点数，也就是我们常常说的 **float** 或者 float32 类型。另外一个是用 64 比特表示双精度的浮点数，也就是我们平时说的 **double** 或者 float64 类型。

单精度的 32 个比特可以分成三部分。

![img](.\images\914b71bf1d85fb6ed76e1135f39b6941.jpg)



我们的浮点数就可以表示成下面这样：

> ​	(−1) s × 1.f × 2e

以 0.5 为例子。0.5 的符号为 s 应该是 0，f 应该是 0，而 e 应该是 -1，也就是0.5=(−1)0×1.0×2−1=0.5





##### 浮点数的二进制转化

​		比方说，我们输入了一个十进制浮点数 9.1。那么按照之前的讲解，在二进制里面，我们应该把它变成一个“符号位 s+ 指数位 e+ 有效位数 f”的组合。

​		首先，我们把这个数的整数部分，变成一个二进制。这个我们前面讲二进制的时候已经讲过了。这里的 9，换算之后就是 1001。

​		接着，我们把对应的小数部分也换算成二进制。小数部分转换成二进制是乘以 2，然后看看是否超过 1。如果超过 1，我们就记下 1，并把结果减去 1，进一步循环操作。在这里，我们就会看到，0.1 其实变成了一个无限循环的二进制小数，0.000110011。这里的“0011”会无限循环下去。

​		![9.1 这个十进制数就变成了 1001.000110011…这样一个二进制表示。](.\images\f9213c43f5fa658a2192a68cd26435ae.jpg)

9.1 这个十进制数就变成了 1001.000110011…这样一个二进制表示。科学计数法就是 1.001000110011…×2(3) , 把“s+e+f”拼一起得到：

![img](https://static001.geekbang.org/resource/image/9a/27/9ace5a7404d1790b03d07bd1b3cb5a27.jpeg)

​	010000010 0010 0011001100110011 001 换算成十进制， 实际准确的值是 9.09999942779541015625。所以 0.3+0.6=0.899999 应该不会感觉奇怪了。



##### 浮点数的加法和精度损失

​		浮点数的加法是怎么进行的。其实原理也很简单，你记住六个字就行了，那就是**先对齐、再计算。**

​		指数位较小的数，需要在有效位进行右移，在右移的过程中，最右侧的有效位就被丢弃掉了。这会导致对应的指数位较小的数，在加法发生之前，就丢失精度。









### 资源参考

极客时间 -- 深入浅出计算机组成原理

https://time.geekbang.org/column/article/91427