cp zoo_sample.cfg zoo.cfg 



zoo.cfg配置文件

```shell
# The number of milliseconds of each tick，基本事件单元，以毫秒为单位。它用来控制心跳和超时，默认情况下最小的会话超时时间为两倍的 tickTime。
tickTime=2000
# The number of ticks that the initial  初始化多少个tickTime
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.存放内存数据库快照的位置
# do not use /tmp for storage, /tmp here is just 
# example sakes.
dataDir=/tmp/zookeeper
# the port at which the clients will connect 客户端连接的端口
clientPort=2181
# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1
//集群ip地址配置
//server.myid=ip:通讯端口号：选举端口号 （leader和followers）
server.1=192.168.10.201:2888:3888
server.2=192.168.10.202:2888:3888
server.3=192.168.10.203:2888:3888
```

然后在dataDir的目录中创建文件myid，写入各自server的编号1,2,3

/bin/zkServer.sh start启动，jps查看QuorumPeerMain成功，status 查看状态

记得开启防火墙端口：firewall-cmd --zone=public --add-port=80/tcp --permanent



客户端：/bin/zkCli.sh -server node:2181

回车，help命令查看客户端支持的所有命令操作

ls / 查看已有节点

在客户端创建应用节点并存储数据：create /app appinfo

create /app/config appconfiginfo

get /app 获取路径数据

set /app appinfo2 更改路径的数据