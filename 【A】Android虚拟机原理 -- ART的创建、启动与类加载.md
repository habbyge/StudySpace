## 【A】Android虚拟机原理 -- ART的创建、启动与类加载

> 

### 虚拟机的创建与启动

​		Android系统在启动的时候，会创建一个Zygote进程，充当应用程序进程孵化器。Zygote进程在启动的过程中，又会创建一个虚拟机。Zygote进程是通过复制自己来创建新的应用程序进程的。这意味着Zygote进程会将自己的虚拟机复制给应用程序进程。通过这种方式就可以大大地提高应用程序的启动速度，因为这种方式避免了每一个应用程序进程在启动的时候都要去创建一个虚拟机。事实上，Zygote进程通过自我复制的方式来创建应用程序进程，省去的不仅仅是应用程序进程创建虚拟机的时间，还能省去应用程序进程加载各种系统库和系统资源的时间，因为它们在Zygote进程中已经加载过了，并且也会连同虚拟机一起复制到应用程序进程中去。

​	









### dex的编译过程 - 理解dex2oat 

​	dex2oat 是个专门做编译的虚拟机进程，apk在安装时，PackageManagerService处理dex，优化为Odex，继而通过 dex2oat  ，将dex编译成机器码，连同原dex文件一起，保存为oat格式文件。





















### 类的加载、链接、初始化





### 







### 参考资源

- 《深入理解Android ：Java虚拟机ART》- 邓凡平

- ART dex2oat浅析

  https://blog.csdn.net/feelabclihu/article/details/105502166

  

- 老罗 - ART加载OAT文件的过程分析、加载类和方法过程分析

  https://blog.csdn.net/luoshengyang/article/details/39307813

  https://blog.csdn.net/luoshengyang/article/details/18006645

  https://blog.csdn.net/Luoshengyang/article/details/39533503

