## 【P】Gradle api 的使用 -- 配置编译过程

> ​	

​	

### 输出倒闭输入

- 多渠道打包如何实现（Flavor、Dimension应用）？渠道标识替换原理？
- MultiDex原理 -- 用MultiDex解决何事？其根本原因在于？SDK 21 不分 dex，直接全部加载会不会有什么问题？
- Dex如何优化？主Dex放哪些东西？主Dex和其他Dex调用、关联？



### 正文

​	Android Plugin for Gradle 与该构建工具包搭配使用，可以更改我们的工程组织和构建方式，使用它们提供的API进行配置可满足一些日常开发需求和解决一些实际问题，比如：多渠道打包、MultiDex、组件化配置 与 提升编译速度等。





其他实用Tips 





### 资源参考

- Android 官方文档

  https://developer.android.com/studio/build

  https://google.github.io/android-gradle-dsl/current/