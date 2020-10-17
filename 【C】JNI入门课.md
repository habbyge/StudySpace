## JNI入门课

> c技术栈包括：
>
> 1. c/c++ 基础与进阶学习
> 2. JNI入门、NDK开发实践
> 3. openCV、openGL、ffmpeg音视频开发技术



### JNI入门基础



Java层有两种field：静态成员和实例成员，两者在JNi层获取和赋值方法是不同的。Java层的field和method无论是public 、还是private，JNI都可以访问到，Java语言的封装性不见了。



在JNI层维护i一个Java对象，如何避免被VM 垃圾回收？

使用NewGlobalRef  告诉VM不要回收此对象，当本地代码最终结束该对象的引用时，DeleteGlobalRef释放之。

LocalRef：每个被创建的Java对象首先会被加入一个LocalRef table，这个table大小是有限的。



