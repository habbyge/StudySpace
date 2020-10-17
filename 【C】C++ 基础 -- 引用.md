## **C++ 基础 -- 引用**

### 引用的概念

变量的别名

```c++
int a = 10;
int &b = a;
cout<<"b = "<<b;
b = 100;
cout<<"a = "<<b;
```

> 引用的性质：普通引用必须初始化（依附于某个变量）



### 引用的用法

#### 作函数参数



#### 引用作函数返回值

```c++
int& getA2()
{  
	int a =10;  
	return a;
}
```

*如果函数返回栈上的引用（引用作返回值），可能会有问题。不可以作为其他引用的初始值（不能用其他引用变量来接收）*



函数返回值是引用（一块内存空间），用什么类型去接收，结果不同：

![image-20200822212311373](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200822212311373.png)

####  引用作左值

​	   

​		



### 复杂数据类型的引用

​        别名等同于指针方式修改内存

![image-20200822205759592](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200822205759592.png)

而下面函数对于形参pT的修改，不会影响到实参

```c++
void printfT(Teacher pT)
{  
    cout<<"age1 = "<<pT.age<<endl;   
    pT.age = 45;
}
```





### 引用的本质

​					![image-20200822210317308](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200822210317308.png)

​				普通引用内部实现是常量指针，有自己的内存空间，也跟原变量操作同一块内存。

​				

### 指针的引用

先来看，指针作函数参数，间接修改实参值的情形。先定义一个结构体

```c++
struct Teacher{
    char name[64];
    int age;
};
```

```c++
int getTeacher(Teacher **p){
    Teacher *tmp = NULL;
    if(p == NULL){
        return -1;
    }
    tmp = (Teacher*)malloc(sizeof(Teacher));
    if (tmp == NULL){
        return -2;
    }
    tmp->age = 33;
    //p是实参地址，*在左边，像一把钥匙寻址并打开间接地修改实参的值
    *p = tmp;
}
```



```c++
int main() {
    Teacher *pT1 = NULL;
    getTeacher(&pT1);
    cout<<"pT1 ="<<pT1->age;
    return 0;
}
```



![image-20200827202014736](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200827202014736.png)





如果换成指针引用：

​	

```c
int getTeacher2(Teacher * &myp)
{
    //操作引用变量，即相当于操作原变量pT1自己，所以可以直接分配内存
    myp = (Teacher*)malloc(sizeof(Teacher));
    if (myp == NULL){
        return -1;
    }
    myp->age = 36;
}
```











