## 【B】Java -- 面向对象语言特性篇

> 天行健，君子以自强不息；地势坤，君子以厚德载物。 -- 《易经》



### 输出倒逼输入

- String、StringBuffer、Stringbuilder , String为什么final、最大长度是多少？
- object有哪些方法？**及如何使用？**
- 接口和抽象类区别 
- **Java 值传递问题**
- 



#### 封装类问题

> 两个值相等的 Integer 对象，== 比较，判断是否相等？

```
Integer类中通过静态内部类 IntegerCache 缓存了一个Integer对象数组， 如果数值在 高位127 低位-128，即[-128,127] 区间内，通过Integer.valueOf(int)方法获得的Integer对象是从缓存池里取的。
```









#### Java 值传递问题

> 下面代码 str 值最终为多少？换成 Integer 值又为多少，是否会被改变？

```java
public void test() {
    String str = "123";
    changeValue(str); 
    System.out.println("str值为: " + str);  // str未被改变，str = "123"
}

public changeValue(String str) {
    str = "abc";
}
```

**原理** ：[Java 程序设计语言总是采用按值调用](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2FSnailclimb%2FJavaGuide%2Fblob%2Fmaster%2Fdocs%2Fessential-content-for-interview%2FPreparingForInterview%2F%E5%BA%94%E5%B1%8A%E7%94%9F%E9%9D%A2%E8%AF%95%E6%9C%80%E7%88%B1%E9%97%AE%E7%9A%84%E5%87%A0%E9%81%93Java%E5%9F%BA%E7%A1%80%E9%97%AE%E9%A2%98.md%23%E4%B8%80-%E4%B8%BA%E4%BB%80%E4%B9%88-java-%E4%B8%AD%E5%8F%AA%E6%9C%89%E5%80%BC%E4%BC%A0%E9%80%92)，方法得到的是所有参数值的一个拷贝，即方法**不能修改**传递给它的任何参数变量的内容。基本类型参数传递的是参数副本，对象类型参数传递的是**对象地址；无法修改实参本身的值，但是可以根据实参的对象地址，去修改对象的属性**

```java
public void test() {
			  Student str = new Student();
			  str.setAge(18);
			  str.setName("小花");
			  changeValue(str); 
			  System.out.println("str值为: " + str.age + " 的 "+ str.name);  // str未被改变，str = "123"
		}

		public void changeValue(Student str) {
			    str.setAge(81);
			    str.setName("老花");
		}
```

输出：str值为: 81 的 老花





### 参考资源