## C 语言

### C语言基础篇

#### 数据类型

| 类型     | 说明                             |
| -------- | -------------------------------- |
| 基本类型 | 整型、浮点型                     |
| 构造类型 | 指针、数组、结构体、共同体、枚举 |

| 类型           | 32位 | 64位 | 取值范围          |
| -------------- | ---- | ---- | ----------------- |
| char           | 1    | 1    | -128～127或0～255 |
| Short          | 2    | 2    |                   |
| int            | 4    | 4    |                   |
| Long           | 4    | 8    |                   |
| unsigned char  |      |      |                   |
| unsigned short |      |      |                   |
| unsigned int   |      |      | 0～65535          |
| unsigned long  |      |      |                   |

1字节 = 8位二进制

带符号基本数据类型，首位表示正负，0正1负，所以正极大加1，进位，变成负数极小值



| 格式控制符   | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| %c           | 读取一个单一的字符                                           |
| %hd、%d、%ld | 读取一个十进制数，并分别赋值给short、int、long类型           |
| %ho、%o、%lo | 读取一个八进制数，并分别赋值给short、int、long类型           |
| %hx、%x、%lx | 读取一个十六进制数，并分别赋值给short、int、long类型         |
| %hu、%u、%lu | 读取一个无符号整数，并分别赋值给unsigned short、unsigned int、unsigned long类型 |
| %f、%lf      | 读取一个十进制小数，并分别赋值给float、double类型            |
| %s           | 读取一个字符串                                               |



```c
char c  = 128;// 2^7
printf("%d\n",c);//以signed int类型打印 -128
printf("%hhd\n",c);//以signed char类型打印整数 -128
printf("%hd\n",c);//短整型 输出-128
printf("%hu\n",c);//无符号短整型 输出65408
```



#### 变量与常量

##### 声明变量

1. 声明同时定义变量。声明变量时即建立存储空间 
2. 声明而不定义。extern关键字。



##### 定义常量

1. #define 宏预处理器

宏就是文本替换

```c
#define A '1
#define test(i) i >10?1:0
```

优点：

​	文本替换，每个使用到的地方被替换成宏定义。不会造成函数调用的开销。

缺点：生成目标文件太大，不会执行代码检查。



2. const关键字



#### 存储类

- auto

- register

  定义寄存器变量，只用于需要快速访问的变量，比如计数器。

- static

  程序的生命周期内，声明它的文件内的全局变量，不需要在它每次进出作用域时进行创建和销毁。

- extern

  该存储类用于提供一个全局变量的引用，全局变量对于所有的程序文件都是可见的。当您使用一个extern时，对于无法初始化的变量，会把变量名指向一个之前定义过的存储位置。





#### 枚举

1. 先定义枚举类型再定义枚举变量

   ```c
      enum DAY {
           MON = 1, TUE, WEB, THU, FRI, SAT, SUN
       };
   
       enum DAY day;
   ```

2. 定义枚举类型的同时定义枚举变量

   ```c
   enum DAY {
       MON = 1, TUE, WEB, THU, FRI, SAT, SUN
   } day;
   ```

   

3. 省略枚举名称，直接定义枚举变量

   ```c
   enum {
       MON = 1, TUE, WEB, THU, FRI, SAT, SUN
   } day;
   ```





#### 指针

##### 指针的算术运算

int* ptr++ 指针将向后移动4字节，指向下一个整数int的位置。

```c
const int MAX = 3;

int main() {

    int var[] = {10,100,200};
    int i , *ptr;
    //指针指向数组首元素地址
    ptr = var;
    for (i = 0; i < MAX; i++) {
        printf("存储地址：var[%d] = %p \n",i,ptr);
        printf("存储值：var[%d] = %d \n",i,*ptr);
        ptr ++;
    }
    return 0;
}

运行结果：
存储地址：var[0] = 0x7ffee29b1a0c 
存储值：var[0] = 10 
存储地址：var[1] = 0x7ffee29b1a10 
存储值：var[1] = 100 
存储地址：var[2] = 0x7ffee29b1a14 
存储值：var[2] = 200 
```



##### 指针数组

指针类型的数组，每个元素都是指向相应类型变量的指针。

```c
#include<stdio.h>

const int MAX = 3;

int main() {

    int var[] = {10, 100, 200};

    int j, *p[MAX];
    //将数组中int变量地址赋给指针数组中的指针变量
    for (j = 0; j < MAX; j++) {
        p[j] = &var[j];
    }

    for (j = 0; j < MAX; j++) {
        printf("存储地址：var[%d] = %p \n", j, p[j]);
        printf("存储值：var[%d] = %d \n", j, *p[j]);
    }
    return 0;
}
```



##### 指向指针的指针

通常指针保存的是一个变量的内存地址，这个变量如果也是一个指针，那么指向指针的指针行成一种多级间接寻址的形式。





##### 传递指针给函数

```c
#include<stdio.h>
#include<time.h>

void getSeconds(unsigned long *par);

int main() {

    unsigned long sec;

    getSeconds(&sec);

    printf("Now time : %ld", sec);
    return 0;
}

void getSeconds(unsigned long *par) {
    /* 获取当前时间*/
    *par = time(NULL);
    return;
}
```



##### 从函数返回指针

```c
#include<stdio.h>
#include<time.h>
#include <stdlib.h>

/*生成并返回随机数的函数*/
int *getRandom() {
    static int r[10];
    /*设置种子*/
    srand((unsigned) time(NULL));
    for (int j = 0; j < 10; ++j) {
        r[j] = rand();
        printf("%d\n", r[j]);
    }
    return r;
}

int main() {
    /*定义一个指向整数的指针*/
    int *pInt;
    pInt = getRandom();
    for (int i = 0; i < 10; ++i) {
        printf("*p+[%d] : %d", i, *(pInt + i));
    }
    return 0;
}
```



##### 函数指针与回调函数

```c
void println(char *buffer){
    printf("%s\n",buffer);
}

//接收一个函数作为参数
void say(void(*p)(char*),char *buffer){
    p(buffer);
}

int main() {
    void(*p)(char*) = println;
    p("hello");
    say(p,"world");
    return 0;
}
```



利用typedef创建别名，类似Java中的回调函数

```c
typedef void(*callback)(int);

void test(callback callback){
    callback("success");
}
```



















##### 指针动态分配内存

- malloc

  作用:   堆上分配连续的内存空间，并返回这块内存的指针。在申请完空间后，需手动判空及初始化这块空间或通过realloc函数初始化内存空间。

  参数：需要分配的字节数

  实例：

  ```c
  int * pmalloc = NULL;
  pmalloc = (int*)malloc(sizeof(int)*5);
  if (NULL == pmalloc){
      printf("alloc memory failed");
      return 0;
  }
  ```

- calloc

  会把内存空间初始化为0

  ```c
  int * pcalloc = NULL;
  pcalloc = calloc(5,sizeof(int));
  if (NULL == pcalloc){
      printf("alloc memory failed");
      return 0;
  }
  ```

- realloc

  作用：修改已分配的内存块的大小，接在原内存块后边扩容或缩小，或另外开辟内存空间。

  需要使用返回的新指针，旧指针不能用了。

- free()函数




#### c字符串 -- 字符数组

​	c风格的字符串 == c字符串 == 字符数组 ， 以'\0' 结尾

声明方式：

```c
char str[10] = {'h','e','l','l','o','\0'};
char str2[10] = "hello";
char * str3 = "hello";
```

后两种方式相同点是可以省略 ‘\0’ 

前两种栈上分配内存，第三种方式 "hello"  是在常量区分配内存，可读不可写。



##### c字符串常用函数：

- scanf函数接收字符串输入的问题：

1、 空格、回车 中断输入问题

2、输入溢出问题



- get()函数 没有 空格、回车 中断输入问题但是还有溢出的问题，溢出数组部分可能乱码或造成崩溃。

- 推荐 fgets() 函数 ，可以严格约束要输入的字符长度。

```c
int main() {
    //字符串数组接收输入字符串
    char str[10];
    printf(" input something : \n");
    fgets(str,10,stdin);
    gets(str);
    printf(" input str length : %d",(int)sizeof(str));
    printf(" input : %s",str);
    return 0;
}
```



- c字符串赋值函数

  ```c
  strcpy(dest,source);
  ```

- 读取字符串长度

  ```c
  #include <stdio.h>
  #include <string.h>
  
  int main() {
      char str[10];
      char str2[10];
      printf(" input something : \n");
      char * s = gets(str);
      //给字符数组赋值
      strcpy(str2,s);
      printf(" str2 : %s",str2);
      //计算字符串（字符数组）大小/长度
      printf(" input str array length : %d \n",(int)sizeof(str));
      printf(" input str length : %d",(int)strlen(str));
      return 0;
  }
  ```

  输出结果：

  ```c
   input something :
   hello
   str2 : hello input str array length : 10
   input str length : 5
  ```

  strlen()函数计算真实字符串长度，sizeof()计算数组占用内存大小



- 字符串拼接函数：

```c
strcat(dest,source);
```







### C语言提高篇



















### 参考资源

- DevYK音视频学习（一）-- c语言

  https://juejin.im/post/6844904022827073543#heading-35
  
- 传智播客c++深入浅出系列课 -- c语言提高

  链接：https://pan.baidu.com/s/1pOqODgwxIdbp9KORFwMtBA 
  提取码：uk9b 
  
- 

  





















