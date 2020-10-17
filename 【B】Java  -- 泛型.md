## Java基础进阶 --  泛型

### 知识梳理

#### 基本概念与使用

   	 Java 容器中所持有的数据类型被称为负载类型（payload type），Java5 开始 使用菱形句法比如<T>为容器类显式指定负载类型。其中T被称为类型参数，它可以代表任何一个具体的、实际的负载类型（即实际类型参数）。

`Plate<T>中的”T”称为类型参数` 

`Plate<Banana>中的”Banana”称为实际类型参数`

声明中带有一个或多个类型参数的类或者接口，那么这个结构就被称为泛型类型。

`	“Plate<T>” 整个成为泛型类型` 

`Plate<Banana>”整个称为参数化的类型 Plate 就叫原生态（原始）类型（raw type）`



##### 泛型存在的意义在于：

- ​	简洁。引入泛型之前，从容器里取数据需要强转。
- ​	健壮性。泛型提供给了编译器一种类型安全检查机制，将类型安全检查提到编译期，而不用等到运行时。
- ​	架构设计的角度来说。代码灵活、复用，泛型起到访问权限约束的作用



##### 使用

泛型类、泛型接口、泛型方法

虽然没有强制要求，但是建议日常代码设计中，使用如下类型参数命名：

E：代表元素（在Java集合框架中有广泛的应用）

K：代表键

N：代表数字

T：代表类型

V：代表值

S,U,V 等：第二，第三，第四个类型



#### 泛型擦除 

##### 什么是泛型擦除？为什么会有泛型擦除？

​	Java 泛型是在 Java1.5 以后出现的，为保持对以前版本的兼容，使用了擦除的方法实现泛型。擦除是指在 一定程度无视类型参数 T，直接从 T 所在的类开始向上 T 的父类去擦除，如调用泛型方法， 传入类型参数 T 进入方法内部，若没在声明时做类似 public T methodName(T extends Father t){}，Java就进行了向上类型的擦除，直接把参数t当做Object类来处理，而不是传进去的T。 即在有泛型的任何类和方法内部，它都无法知道自己的泛型参数，擦除和转型都是在边界上 发生，即传进去的参在进入类或方法时被擦除掉，但传出来的时候又被转成了我们设置的 T。 在泛型类或方法内，任何涉及到具体类型(即擦除后的类型的子类)操作都不能进行，如 new T()，或者 T.play()(play 为某子类的方法而不是擦除后的类的方法)。

​	Java虚拟机只认识class，不认识泛型类型，所以Java在编译时擦除了泛型信息，只保留原始类型，这样就不会产生新的类型到字节码。

![image-20200828151754290](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828151754290.png)

两个泛型类getClasss的结果是相同的，都是原始类型（raw rype）Box.class：![image-20200828151837418](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828151837418.png)

可见编译后泛型信息被擦除了。



从泛型类的声明来看泛型擦除原理：

![image-20200828154032983](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828154032983.png)



对于泛型类/接口/方法，编译时，擦除类型变量，替换为限定类型，没有限定类型则替换为Object类型;

对于泛型接口的实现类而言，声明时便已经确定了具体的类型参数，并实现了具体的接口方法，由于泛型擦除机制的存在，编译后泛型接口的实现类与泛型接口本身方法签名不同了，为解决这个问题，保证多态性（调用接口的方法时，可以正确调用到实现类的方法），java编译器自动生成了桥方法、并提供类型检查。

![image-20200828163313111](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828163313111.png)



##### 泛型擦除带来的问题？

1. 泛型类型变量不能使用基本数据类型

   A: 比如没有ArrayList<int>,只有ArrayList<Integer>.当类型擦除后，

      	ArrayList的原始类中的类型变量(T)替换成Object,但Object类型不能

     	 存放int值

   

2. 不能直接创建（new）一个泛型（类型参数）的实例

   ![image-20200828180922556](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828180922556.png)

3. 泛型类不能使用instanceof 运算符

   ![image-20200828180651107](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828180651107.png)

      因为擦除后，ArrayList<String>只剩下原始类型，泛型信息String

      不存在了，所有没法使用 instanceof

   

4. 协变不遵循 -- 不能创建所谓的泛型数组

   什么是数组的协变？

   ​		A 类继承自 B，则 A[] 也是 B[] 的子类 ，即 instanceof 关系成立。

   且看Object[] 与 String[] 印证数组的协变特性：

   ![image-20200828213829506](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828213829506.png)

   

   但是数组协变是有风险的，把objects[1]  赋值为Box对象，编译虽然可以通过objects[]确实可以存任何对象，但是运行时报错 java.lang.ArrayStoreException 。为了避免这种情况，编译器对于方法参数是有类型检查的，所以面试题： “可以把List< String>传递给一个接受List< Object>参数的方法吗？” 回答是“不可以”。

   

   同样的道理，由于泛型擦除的存在，泛型中类型参数退化成Object，形如List<Fruit>[]、List<Apple>[]这样的结构，是编译不通过的。

   

   但是使用通配符Box<?>[] 创建数组是可以的，它的元素可以是任意已Box类（及其子类）为原始类型的泛型类。



5. 泛型，继承和子类型

   类型参数间继承关系 与 泛型类之间的关系无关

   

   虽然类型参数 Fruit 与 Apple有继承关系，类型参数只表示容器中负载因子的类型而已，不能决定容器（泛型类）之间的继承关系。

   如下图，他们两个泛型类公共基类只有Object，也就意味着他们没有关系。Plate<Fruit>与Plate<Apple>为什么无关呢？

   ![image-20200828202302050](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828202302050.png)

   原始类型间继承关系具有传递性 :

   

![image-20200828190133969](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828190133969.png)







6. 通配符 与 泛型层级关系

   既然类型参数间的继承关系没有意义，那么如何实现泛型类之间的转型、传递呢？答案是通配符 “？”

   通配符代表“满足条件的任何一个实际类型参数”，T 表示声明时不确定、具体实现（调用）时，才能确定的，形式参数

7. ![image-20200828195619063](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828195619063.png)

   

   ![image-20200828195455419](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828195455419.png)



#### 通配符与PECS原则 -- 泛型在框架源码设计中的应用

我们回到这个问题 “可以把List< String>传递给一个接受List< Object>参数的方法吗？”，答案是不可以，如何实现相同语义的需求呢，使用通配符，把List< Object> 替换成List<?>也就是 List<? extends Object> ：

这样编译器就允许我们传入任何类型的List了，为了避免使用List<Object>时可能出现的java.lang.ArrayStoreException问题，编译器规定PECS原则 ：

​	***如果你只需要从集合中获得类型T , 使用<? extends T>通配符***

​	***如果你只需要将类型T放到集合中, 使用<? super T>通配符***

​	如果你既要获取又要放置元素，则不使用任何通配符。例如List<Apple>

​	PECS即 Producer extends Consumer super， 为了便于记忆。

如图可见，向List<? extends Object> 中添加元素编译不通过。

![image-20200828213121516](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828213121516.png)









​    框架设计 -- JDK 、RxJava

![image-20200828194533164](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200828194533164.png)





### 常见问题

- 泛型的作用及使用场景

- 什么是泛型擦除，为什么要这样做，泛型擦除会带来什么问题？

- List<? extends E> 在编译后会变成什么样

- List<? extends T>和List <? super T>之间有什么区别

- 你可以把List< String>传递给一个接受List< Object>参数的方法吗，为什么

- C++模板和java泛型之间有何不同？

  C++ template是reified generic，Java generic用的是type erasure。
  C++是在call site做instantiate type，Java是在call site插入cast。
  C++ template在call site可以做inline，Java generic因为并没有在call site生成代码所以不行。
  C++在runtime没有额外的开销，Java在runtime有cast的开销。
  C++的每个reified generic type都有一份独立的代码，Java只有一份type erased之后的代码
  C++的type check是在编译时做的，Java的type check在编译期和运行期都要做一些工作。

  

### 资源参考

https://zhuanlan.zhihu.com/p/35387281

