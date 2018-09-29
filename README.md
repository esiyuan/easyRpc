# easyRpc
结合Spring、Netty、Zookeeper实现的RPC组件、支持服务自动注册发现。

使用步骤

1. git clone 本仓库
2. 选择合适的分支版本
3. 本地安装，或者打包上传到仓库、然后本地pom引入
4. classpath路径下需要加入文件easyRpc.properties，并加入属性：
    rpc.zk.connectString=192.168.0.113:2181和rpc.port=5577作为zk的地址，和Rpc服务启动的端口，当然值需要填写真实值。
5. Spring扫描路径需要增加：com.easyrpc
6. 服务端需要在Springbean的类上增加@Implement注解，消费端需要在注入的接口上增加注解@Reference。
7. 最后调用就如同本地方法调用一样。


