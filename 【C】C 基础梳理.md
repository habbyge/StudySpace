## C 基础梳理

### 零星知识点

#### 数据类型

1字节 = 8位二进制

带符号基本数据类型，首位表示正负，0正1负，所以正极大加1，进位，变成负数极小值



基本类型：字符、整型、浮点型

构造类型：数组、结构体、共同体、枚举

指针

![image-20200917193127118](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200917193127118.png)

![image-20200917193251649](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200917193251649.png)



论证：

```c
char c  = 128;// 2^7
printf("%d\n",c);//以signed int类型打印 -128
printf("%hhd\n",c);//以signed char类型打印整数 -128
printf("%hd\n",c);//短整型 输出-128
printf("%hu\n",c);//无符号短整型 输出65408
```



### 常用的预处理器

宏就是文本替换

![image-20200917201357510](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200917201357510.png)



```c
#define A '1
#define test(i) i >10?1:0
```



#### 宏函数

优点：

​	文本替换，每个使用到的地方被替换成宏定义。不会造成函数调用的开销。

缺点：生成目标文件太大，不会执行代码检查。



### 指针

#### 指针与指针数组





#### 函数指针

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



















#### 指针动态分配内存

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




### c语言中的字符串 -- 字符数组

​	c风格的字符串 == c字符串 == 字符数组 ， 以'\0' 结尾

声明方式：

```c
char str[10] = {'h','e','l','l','o','\0'};
char str2[10] = "hello";
char * str3 = "hello";
```

后两种方式相同点是可以省略 ‘\0’ 

前两种栈上分配内存，第三种方式 "hello"  是在常量区分配内存，可读不可写。



#### c字符串常用函数：

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



































