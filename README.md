## 静态代理的缺点
```
要代理的方法越多，重复的逻辑越多：
如果目标类有100个方法，代理类要对这100个方法进行委托，但是他们代理的逻辑可能是相同的
```

## 动态代理的两类实现：基于接口代理与基于继承代理

### JDK代理

#### 特点
- 类：java.lang.reflect.Proxy
```
通过该类动态生成代理类
```
- 接口：InvocationHandler
```
生成的代理类的构造器以该接口作为参数
```
- 只能基于接口进行动态代理

#### 源码解读


- --->调用该方法生成代理类
```java
Proxy.newProxyInstance
```
- --->通过getProxyClass0寻找或生成代理类
```java
/*
 * Look up or generate the designated proxy class.
 */
Class<?> cl = getProxyClass0(loader, intfs);

-->line:419 会先在缓存中找，如果没有生成缓存
private static Class<?> getProxyClass0(ClassLoader loader,Class<?>... interfaces) {
if (interfaces.length > 65535) {
throw new IllegalArgumentException("interface limit exceeded");
}

// If the proxy class defined by the given loader implementing
// the given interfaces exists, this will simply return the cached copy;
// otherwise, it will create the proxy class via the ProxyClassFactory
return proxyClassCache.get(loader, interfaces);
}

-->line:567 通过apply方法找到目标类，生成(字节码byte[])代理类
public Class<?> apply(ClassLoader loader, Class<?>[] interfaces)

-->line:634 代理类的名称
long num = nextUniqueNumber.getAndIncrement();
String proxyName = proxyPkg + proxyClassNamePrefix + num;

-->line:639 generateProxyClass方法生成符合java规范的字节码
byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
proxyName, interfaces, accessFlags);

```
- --->通过设置系统变量可以查看生成的字节码
```java
System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

生成后的类也实现了目标类对应的接口
```

### JDK与Cglib代理对比

- JDK只能针对有接口的类的接口方法进行动态代理
- Cglib基于继承实现代理，无法对static、final类进行代理
- 都无法对private、static方法进行代理

### SpringAOP的选择

- 如果目标对象实现了接口，则默认采用JDK动态代理
- 如果目标对象没有实现接口，则采用Cglib进行代理
- 如果对象实现了接口，想强制使用cglib代理：
```java
@SpringBootApplication
//强制使用cglib代理
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AopDemoApplication.class,args);
    }
}
```

### AOP如何链式调用

- 责任链第一种方式，每个handler依赖另外一个handler，耦合度高
- 第二种方式，把所有handler存放在Chain里，并在Chain里提供递归方法来遍历所有handler，handler目标方法执行完后回到Chain中执行下一条