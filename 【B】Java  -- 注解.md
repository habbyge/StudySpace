## Java基础进阶 --  注解

![image-20200831173142738](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200831173142738.png)





### 注解

​	JDK 5.0引入的一种注释机制。提供有关于程序但不属于程序本身的数据。注解对它们注解的代码的操作没有直接影响。就像我们读书的时候写的注释，注释本身不属于文章内容，读者可以利用注释提供的信息更好地理解文章。Java的注释也是如此，在它被处理的时候，提供有用的信息。

![image-20200914165559345](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200914165559345.png)





### 元注解与自定义注解

​	作常用的用于注解的注解，称为元注解。常用的元注解：表示声明的注解允许作用于哪些节点使用@Target；保留级别由@Retention 。注解的三个保留级别与应用场景：

- RetentionPolicy.SOURCE 

标记的注解仅保留在源级别中，并被编译器忽略。



- RetentionPolicy.CLASS 

标记的注解在编译时由编译器保留，但 Java 虚拟机(JVM)会忽略。



- RetentionPolicy.RUNTIME 

标记的注解由 JVM 保留，因此运行时环境可以使用它。



![image-20200907191856001](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200907191856001.png)



自定义注解的关键是使用“元注解”来注释自定义注解的类型的定义

![image-20200914180644684](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200914180644684.png)









### 注解实践 -- 通过自定义注解与反射实现页面跳转的参数注入：

new Intent().putExtra("name","Lance").putExtra("boy",true); //页面跳转携带参数

@Autowired
public String name;
@Autowired("boy")
public boolean isBoy

getIntent().getStringExtra("name");//使用属性名
getIntent().getBooleanExtra("boy");//读取注解元素



```java
/**
 * intent参数注入
 */
public static void injectAutowired(Activity activity) {
    Class<? extends Activity> cls = activity.getClass();

    //读取Intent
    Intent intent = activity.getIntent();
    Bundle extras = intent.getExtras();
    if (extras == null) {
        return;
    }
    Field[] fields = cls.getDeclaredFields();
    for (Field field : fields) {
        if (field.isAnnotationPresent(Autowired.class)) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            //是否有自定义字段名
            String key = TextUtils.isEmpty(autowired.value()) ? field.getName() : autowired.value();
            if (extras.containsKey(key)) {
                Object obj = extras.get(key);
                //对于Parcelable[]需要特殊处理
                Class<?> componentType = field.getType().getComponentType();
                if (field.getType().isArray() && Parcelable.class.isAssignableFrom(componentType)) {
                    //手动创建Object数组并拷贝，进行数据类型转换
                    Object[] objs = (Object[]) obj;
                    Object[] objects = Arrays.copyOf(objs, objs.length, (Class<? extends Object[]>) field.getType());
                    obj = objects;
                }

                field.setAccessible(true);
                try {
                    field.set(activity,obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```















