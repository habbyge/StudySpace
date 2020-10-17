## Android Studio 构建过程与Gradle

> 知识浩如烟海，学技术如苦海行舟，要有自己的目的性和学习主线，方能在最短的时间内获得提升，到达彼岸；
>
> 读书恰恰相反，海纳百川，不求实用，厚积薄发，量变自然产生质变。																																							-- 《安东札记》





### 输出倒逼输入

- APK打包流程	?   APK的结构？
- 安卓的混淆原理是什么？Android打包哪些类型文件不能混淆？
- **APK 为什么要签名？是否了解过具体的签名机制？**
- **多渠道打包如何实现（Flavor、Dimension应用）？渠道标识替换原理？**
- 运行时删除项目的多余的资源文件 是否影响项目运行？ 提成R文件内指向的数字是否影响 ？（可以参考下网上关于arsc的资料来回答这个问题）
- 安卓资源id如何查找，资源id的前两位代表着什么？

- 为什么将class文件打成dex ?

- Dex如何优化？主Dex放哪些东西？主Dex和其他Dex调用、关联？

-  MultiDex原理 -- 用MultiDex解决何事？其根本原因在于？SDK 21 不分 dex，直接全部加载会不会有什么问题？

- 对于gradle的掌握程度

  



### 构建过程

#### APK结构

一个 APK 打包完之后，通常有下面几个目录，用来存放不同的文件。
**assets**
	原生资源文件，不会被压缩或者处理
**classes.dex**
	java 代码通过 javac 转化成 class 文件，再通过 dx 文件转化成 dex 文件。如果有多个 dex 文件，其命名会是这样的：
classes.dex classes2.dex classes3.dex ...
在其中保存了类信息。
**lib/**
	保存了 native 库 .so 文件，其中会根据 cpu 型号划分不同的目录，比如 ARM，x86 等等。
**res/**
	保存了处理后的二进制资源文件。
**resources.arsc**
	保存了资源 id 名称以及资源对应的值／路径的映射。
**META-INF/**
	用来验证 APK 签名，其中有三个重要的文件 MANIFEST.MT，CERT.SF，CERT.RSA。
**MANIFEST.MF** 保存了所有文件对应的摘要，部分内容如下：

```
Manifest-Version: 1.0
Built-By: Generated-by-ADT
Created-By: Android Gradle 3.4.0

Name: AndroidManifest.xml
SHA-256-Digest: QxJh66y6ssDSNFgZSlf5jIWXfRdWnqL1c3BSwSDUYLQ=

Name: META-INF/android.arch.core_runtime.version
SHA-256-Digest: zFL2eISLgUNzdXtGA4O/YZYOSUPCA3Na3eCjULPlCYk=
```

**CERT.SF** 保存了MANIFEST.MF 中每条信息的摘要，部分内容如下：

```
Signature-Version: 1.0
Created-By: 1.0 (Android)
SHA-256-Digest-Manifest: j8YGFgHsujCHud09pT6Igh21XQKSnG+Gqy8VUE55u+g=
X-Android-APK-Signed: 2

Name: AndroidManifest.xml
SHA-256-Digest: qLofC3g32qJ5LmbjO/qeccx2Ie/PPpWSEPBIUPrlKlY=

Name: META-INF/android.arch.core_runtime.version
SHA-256-Digest: I65bgli5vdqHKel7MD74YlSuuyCR/5NDrXr2kf5FigA=
```

**CERT.RSA**     包含了对 CERT.SF 文件的签名以及签名用到的证书。

**AndroidManifest.xml**    这里是编译处理过的二进制的文件。







#### 编译过程关键步骤概述

1. 使用AAPT/AAPT2 编译资源文件生成resources.arsc以及R.java
2. 使用aidl处理aidl文件，生成java文件
3. 使用javac编译java文件，生成class文件
4. 使用dx/d8处理class文件，生成dex文件
5. 使用Android NDK处理native代码生成.so文件
6. 使用apkbuilder生成未签名的apk
7. 使用apksigner对apk进行签名，生成最终apk



#### 编译资源文件 -- AAPT/AAPT2

​	AAPT2 会解析资源、为资源编制索引，并将资源编译为针对 Android 平台进行过优化的二进制格式。

- [编译](https://developer.android.com/studio/command-line/aapt2?hl=zh-cn#compile)：将资源文件编译，生成一个扩展名为 `.flat` 的中间二进制文件。
- [链接](https://developer.android.com/studio/command-line/aapt2?hl=zh-cn#link)：将编译后的 xxx.flat 资源链接，生成一个完整的 resource.arsc，二进制资源和 R.java



##### **为什么要转化为二进制文件？**

- 二进制格式的XML文件占用空间更小。这是由于所有XML元素的标签、属性名称、属性值和内容所涉及到的字符串都会被统一收集到一个字符串资源池中去，并且会去重。有了这个字符串资源池，原来使用字符串的地方就会被替换成一个索引到字符串资源池的整数值，从而可以减少文件的大小。
- 二进制格式的XML文件解析速度更快。这是由于二进制格式的XML元素里面不再包含有字符串值，因此就避免了进行字符串解析，从而提高速度。 有了资源ID以及资源索引表之后，Android资源管理框架就可以迅速将根据设备当前配置信息来定位最匹配的资源了。



##### R文件生成与Resources.arsc解析资源id

1. 第一个字节是指资源所属包，7F代表普通应用程序
2. 第二个字节是指资源的类型，如02指drawable，03指layout
3. 第三四个字节是指资源的编号，往往从00开始递增



R文件中 id都是static final 修饰的静态常量，由于java编译器的优化，在编译时，所有**使用静态常量的地方，会被直接替换为常量值。**这样一来，R文件里的id在编译完java文件后，就没有被引用的地方了，此时如果开启proGuard混淆，就会删除整个R文件，从而会**减少field数和包大小。**



对于aar的编译，生成的R文件里的id，并不是public static final的常量，而是**public static的变量**，这是为什么呢？因为如果是常量，则会在编译打包时，调用的地方被替换为常量值，而这个值是AAR内部生成的临时id，是不对的，这样的话**主工程编译时将无法修改这个值**，就有问题了。aar中含有的是一个R.txt文本文件，这个文件以文本的形式记录了AAR中所有资源的类型、名字等，以便于**主工程打包时，可以依据这些资源信息统一生成最终的R.java文件。**



主工程编译时，最终**除了会在主工程包名下，生成一个包含主工程和AAR所有资源的R.java文件之外，还会在每个AAR相应的包名下，生成一个包含AAR资源的R.java文件**，当然，相同资源的id是一样的。这就是为什么我们可以在主工程中调用主包名的R文件，和AAR包名的R文件，都可以获取到一些资源id的原因。此时生成的所有R.java文件里的id值，都是**public static final的静态常量**，因为此刻的id值都已经确定了。然后在编译java文件时，常量值会被替换(包括资源文件中的引用也会被替换)，从而使R文件的field无引用，可以通过proGuard删除。



还有一个问题，就是**AAR里面的文件，使用到资源id的地方，并没有被替换为相应的常量值，但是R文件里面的资源id确实是常量。**

这是因为AAR的class文件，在主工程编译时，不会再次进行编译，也就是说**AAR的class文件原封不动的打包进apk**。而资源id为常量是在主工程编译时才行程的，但AAR生成class时，使用的是上面说到的变量，所以一直被保留了下来。



如何根据R.java中的资源id查找到对应的资源呢？

@link 【A】Amdroid系统架构-资源管理篇





### Gradle理解与应用



#### 打包插件开发



#### buildSrc统筹依赖管理



#### 多渠道打包配置



















### 参考链接

 **AndroidStudio 编译apk打包过程**

​		https://github.com/5A59/android-training/blob/master/common-tec/android-%E6%89%93%E5%8C%85.md

​    	https://juejin.im/post/5a69c0ccf265da3e2a0dc9aa



 **aapt/appt2 工作原理 --  R 文件生成 与 resource.arsc 文件解析**

​		https://blog.csdn.net/beyond702/article/details/51744082

[		Android R文件生成_移动开发_依生依世-CSDN博客](https://blog.csdn.net/qq_15827013/article/details/103717702)

​		https://juejin.im/post/6844903933769433095



Android资源查找过程源码分析

​		https://sharrychoo.github.io/blog/android-source/resources-find-and-open



**签名机制** 

​	https://www.jianshu.com/p/286d2b372334



 **极客时间 - 张绍文 Android开发高手课 -- 关于编译你需要了解什么？**

[**https://time.geekbang.org/column/intro/142**](https://time.geekbang.org/column/intro/142)





































