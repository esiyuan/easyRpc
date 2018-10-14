# easyRpc
结合Spring、Netty、Zookeeper实现的RPC组件、支持服务自动注册发现。

使用步骤

1. git clone https://github.com/esiyuan/easyRpc.git
2. mvn install -DskipTests
3. 然后在项目中的pom引入刚刚安装到本地的依赖（注意如果是服务器上面，需要上传到私服仓库）
4. classpath路径下需要加入文件easyRpc.properties，并加入属性：
    rpc.zk.connectString=192.168.0.113:2181和rpc.port=5577作为zk的地址和Rpc服务启动的端口，当然要写真实值。
5. Spring扫描路径需要增加：com.easyrpc ，将rpc组件的依赖加入到Spring中进行管理。
6. 服务端需要在提供服务的Springbean的类上增加@Implement注解，例如:`@Implement(contract = PersonIntf.class, implCode = "zhangsan")`
   启动服务，组件会通过ServerRegister（[这是一个ApplicationListener如何使用可以参考](https://www.jianshu.com/p/d71ceaf7160e)），
   在spring容器中找到加入注解的类，并在zk上进行注册。
   **注意：Implement 注解两个属性：contract 是客户端和服务器都要存在的接口并且全限定名要一模一样，implCode是标志当前接口的不同实现。**
   
7. 消费端需要在注入的接口上增加注解@Reference。此注解的两个属性需要和Implement注解的一样。
    框架在spring启动后，会通过后处理器ReferenceBeanPostProcessor生成代理类，进行装配，具体实现可以参考我的博客：[RPC中动态代理](https://www.jianshu.com/p/9624d0ac4657)
8. 最后调用就如同本地方法调用一样，如同下面：

```
public interface PersonIntf {

    String getName();
}
@Component
@Implement(contract = PersonIntf.class, implCode = "zhangsan")
public class PersonImpl implements PersonIntf {

    @Override
    public String getName() {
        return "张三";
    }
}

@RestController
public class DemoController {

    @Reference(contract = PersonIntf.class, implCode = "zhangsan")
    private PersonIntf personIntf;

    @RequestMapping(path = "f1")
    public String f1() {
        return personIntf.getName();
    }
}

@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = "com")
public class Boot {

    public static void main(String[] args) {
        SpringApplication.run(Boot.class, args);
    }
}
```
**通过浏览器访问f1, 结果如下：**
```
2018-10-14 10:49:30,185 [qtp1333507457-30] INFO  com.easyrpc.client.ServerClient - 发送消息：Request(id=121409b7-d8d3-4577-94a4-219cfe405db9, contract=com.PersonIntf, implCode=zhangsan, method=getName, args=null, parameterTypes=null)
2018-10-14 10:49:30,301 [nioEventLoopGroup-2-1] INFO  com.easyrpc.server.ServerMsgHandler - request = InvocationMsg(id=121409b7-d8d3-4577-94a4-219cfe405db9, contract=com.PersonIntf, implCode=zhangsan, method=getName, args=null, parameterTypes=null, request=true, resp=null)
2018-10-14 10:49:30,303 [nioEventLoopGroup-4-1] INFO  com.easyrpc.client.ClientMsgHandler - 消息接收 : InvocationMsg(id=121409b7-d8d3-4577-94a4-219cfe405db9, contract=null, implCode=null, method=null, args=null, parameterTypes=null, request=false, resp="张三")
2018-10-14 10:50:00,304 [nioEventLoopGroup-2-1] INFO  com.easyrpc.server.ServerMsgHandler - 通道空闲，即将关闭
```
