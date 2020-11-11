## Java基础进阶 --  反射与动态代理





### 反射 

​	一般情况下，我们使用某个类时必定知道它是什么类，是用来做什么的，并且能够获得此类的引用。于是我们直接对这个类进行实例化，之后使用这个类对象进行操作。



​	反射则是一开始并不知道我要初始化的类对象是什么，自然也无法使用 new 关键字来创建对象了。这时候，我们使用 JDK 提供的反射 API 进行反射调用。***反射就是在运行状态中,对于任意一个类,都能够知道这个类的所有属性和方法;对于任意一个对象,都能够调用它的任意方法和属性;并且能改变它的属性。***	



























#### 反射获取泛型信息 -- Type接口体系

​	反射的核心是从通过Class的对象，获取属性、构造方法、成员方法等信息，而Class类则是实现了Type接口：

![image-20200907192548753](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200907192548753.png)

- TypeVariable  

 	泛型类型变量。可以泛型上下限等信息；

- ParameterizedType

 	具体的泛型类型，可以获得元数据中泛型签名类型(泛型真实类型)

- GenericArrayType

 	当需要描述的类型是泛型类的数组时，比如List[],Map[]，此接口会作为Type的实现。

- WildcardType

 	通配符泛型，获得上下限信息；



​	我们知道java编译时，将泛型信息擦除了，但是 java运行时仍可获取到泛型信息：

![image-20200907194257012](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200907194257012.png)



**Type体系原理 -- 泛型信息是如何保存到.class字节码文件、运行时被作为ParameterizedType的读取到的？**

![image-20200907214951973](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200907214951973.png)

.java 编译成.class字节码文件后，生成signature签名，记录了泛型信息。

native层 生成Class对象、Filed对象，并将原始signature签名 赋值给Filed signature成员。

反射得到Field ，调用getGenericType() 方法，最终由GenericDeclRepository 解析 signature字段 ，获取到到类型变量。













#### 反射获取注解信息



`小设计：实现View注入，替代findviewbyid`

![image-20200907221411046](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200907221411046.png)





> **反射为什么慢 ？ 为什么说反射机制会有性能问题？**







### 动态代理

先说java类的完整生命周期

![image-20200907195141266](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200907195141266.png)



相对于静态代理，动态代理在运行时再创建代理类和其实例，因此显然效率更低。要完成这个场景，需要在运行期动态创建一个Class。JDK提供了 Proxy 来完成这件事情。基本使用如下:

![image-20200907222427675](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200907222427675.png)



​	实际上，本质上与静态代理相同， Proxy.newProxyInstance 会创建一个代理了指定接口的Class，与静态代理不同的是，这个Class不是由具体的.java源文件编译 而来，即没有真正的文件，只是在内存中按照Class格式生成了一个Class。初始化获得method备用。

![image-20200907222752016](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200907222752016.png)



![image-20200907222948287](/Users/zhanghongxi/Library/Application Support/typora-user-images/image-20200907222948287.png)



























### 实践 

#### 事件的注入

定义注解

```java
/**
 * 定义Click事件注解，用以接收要注入的view id
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventType(listenerType = View.OnClickListener.class, listenerSetter = "setOnClickListener")
public @interface OnClick {
    /**
     * 定义id数组，可以绑定多个view
     *
     * @return
     */
    int[] value() default {View.NO_ID};

}
```



EventType注解用以指定对应事件的绑定方式 -- 调用哪个setter方法、设置哪个Listener对象：

```java
/**
 * 事件注解的注解
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventType {

    Class listenerType();

    /**
     * 用以接收方法名，供动态代理使用
     * @return
     */
    String listenerSetter() default "";

}
```



定义注入方法和InvokeHandler类

``

```java
/**
 * 事件注入
 */
public static void injectEvent(Activity activity) {

    Class<? extends Activity> cls = activity.getClass();
    //获得所有声明的方法
    Method[] methods = cls.getDeclaredMethods();
    for (Method method : methods) {
        Logger.e("method :: " + method.getName());
        //获得所有方法上声明的注解
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            Logger.e("annotation :: " + annotation.toString());
            //获取注解类型
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            Logger.e("annotationClass :: " + annotationClass.getSimpleName());
            if (annotationClass.isAnnotationPresent(EventType.class)) {
                //获取到事件注解在定义时指定的对应事件的Listener设置方法
                EventType eventType = annotationClass.getAnnotation(EventType.class);
                //listener对应接口
                Class listenerType = eventType.listenerType();
                //设置listner的方法名
                String listenerSetter = eventType.listenerSetter();

                //不需要知道具体是OnClick还是OnLongClick，知道接口和方法名即可动态代理之
                try {
                    //反射获取到需要绑定的 view ids
                    Method valueMethod = annotationClass.getDeclaredMethod("value");
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);

                    method.setAccessible(true);
                    ListenerInvokeHandler<Activity> handler = new ListenerInvokeHandler<>(activity, method);
                    //创建接口的动态代理类对象，无需关注具体类型，代理方法invoke之后，activity中声明的method方法将被调用到，以执行业务逻辑
                    Object listenerProxy = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, handler);

                    for (int viewId : viewIds) {
                        View view = activity.findViewById(viewId);
                        //获取指定的方法
                        Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                        //执行view.setXXXXListener方法，由动态代理对象invoke方法，进行传递调用
                        setter.invoke(view, listenerProxy);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```



```java
static class ListenerInvokeHandler<T> implements InvocationHandler {

    private Method method;

    private T target;

    public ListenerInvokeHandler(T target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Logger.e("ListenerInvokeHandler invoke method = " + method.getName());
        Logger.e("ListenerInvokeHandler this.method = " + this.method.getName());
        return this.method.invoke(target, args);
    }

```







### 资源参考

https://www.jianshu.com/p/f1a8356c615f

动态代理5种实现方式

https://cloud.tencent.com/developer/article/1461796