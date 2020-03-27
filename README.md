### Springboot 整合 rabbitmq

#### docker 安装 rabbitmq

下载镜像，带有management页面的

```shell
docker pull rabbitmq:3.8.2-management
```

启动

```shell
root@deployment:/# docker run -d --hostname rabbitmq:3.8.3-management --name rabbitmq -p 15672:15672 rabbitmq:3.8.3-management
```

或者

```shell
docker run -d --name rabbitmq-3.8.3-management -p 5672:5672 -p 15672:15672 -v /opt/rabbitmq/data:/var/lib/rabbitmq --hostname myRabbit -e RABBITMQ_DEFAULT_VHOST=my_vhost -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin 479479d8e188[镜像Id]
```

说明：

-d 后台运行容器；
–name 指定容器名；
-p 指定服务运行的端口（5672：应用访问端口；15672：控制台Web端口号）；
-v 映射目录或文件；
–hostname 主机名（RabbitMQ的一个重要注意事项是它根据所谓的 “节点名称” 存储数据，默认为主机名）；
-e 指定环境变量；
RABBITMQ_DEFAULT_VHOST：默认虚拟机名；
RABBITMQ_DEFAULT_USER：默认的用户名；
RABBITMQ_DEFAULT_PASS：默认用户名的密码

#### docker 安装 rabbitmq 延时消息插件

首先去 [github](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases) 上把插件下载下来

然后把插件上传到 linux ，看接下来步骤：

docker ps 看看rabbitmq 是否启动

如果启动了则`docker exec -it rabbitmq-3.8.3-management /bin/bash` 进入安装目录

```shell
root@deployment:~# docker exec -it rabbitmq-3.8.3-management /bin/bash
# 可以看到有 plugins 目录
root@myRabbit:/# ls
bin  boot  dev	etc  home  lib	lib64  media  mnt  opt	plugins  proc  root  run  sbin	srv  sys  tmp  usr  var
```

可以看到有 plugins 目录，接下来 ctrl+d 退出，把插件拷贝到 rabbitmq 安装目录的plugins 目录下

```shell
root@deployment:~# docker cp /usr/local/tmp/rabbitmq_delayed_message_exchange-3.8.0.ez rabbitmq-3.8.3-management:/plugins
```

为了保险你可以进入 plugins 目录看是否拷贝成功

```shell
root@deployment:~# docker exec -it rabbitmq-3.8.3-management /bin/bash
root@myRabbit:/# ls
bin  boot  dev	etc  home  lib	lib64  media  mnt  opt	plugins  proc  root  run  sbin	srv  sys  tmp  usr  var
root@myRabbit:/# cd plugins
# 有在呢
root@myRabbit:/plugins# ls -l|grep delay
-rw-r--r-- 1 root     root       43377 Mar 27 08:07 rabbitmq_delayed_message_exchange-3.8.0.ez
```

接下来，启用插件，并重启 rabbitmq

```shell
root@myRabbit:/plugins# rabbitmq-plugins enable rabbitmq_delayed_message_exchange
Enabling plugins on node rabbit@myRabbit:
rabbitmq_delayed_message_exchange
The following plugins have been configured:
  rabbitmq_delayed_message_exchange
  rabbitmq_management
  rabbitmq_management_agent
  rabbitmq_web_dispatch
Applying plugin configuration to rabbit@myRabbit...
The following plugins have been enabled:
  rabbitmq_delayed_message_exchange

started 1 plugins.
root@deployment:~# docker restart rabbitmq-3.8.3-management
```

最后你可以打开 rabbitmq管理页面，在Exchanges选项卡下，点击Add a new exchange，在Type里面看是否出现了x-delayed-message选项

### springboot 整合 shardingsphere分库分表

Sharding-JDBC（**简称 SJDBC**） 是当当网 2016 年开源的适用于微服务的分布式数据访问基础类库，完整的实现了分库分表，读写分离和分布式主键功能，并初步实现了柔性事务。在经历了整体架构的数次精炼以及稳定性打磨后，如今它已积累了足够的底蕴。

**2018 年 11 月 10 日** 正式进入 Apache 孵化器并更名为 **[Apache ShardingSphere (Incubator)](http://www.qfdmy.com/wp-content/themes/quanbaike/go.php?url=aHR0cHM6Ly9zaGFyZGluZ3NwaGVyZS5hcGFjaGUub3JnL2luZGV4X3poLmh0bWw=)** 它是一套 [**开源**](http://www.qfdmy.com/wp-content/themes/quanbaike/go.php?url=aHR0cHM6Ly9naXRodWIuY29tL2FwYWNoZS9pbmN1YmF0b3Itc2hhcmRpbmdzcGhlcmUvYmxvYi9kZXYvUkVBRE1FX1pILm1k) 的 **分布式数据库中间件（截至 2019 年 12 月 23 日制作的课件，目前还在孵化中）** 解决方案组成的生态圈，它由 **Sharding-JDBC**、**Sharding-Proxy** 和 **Sharding-Sidecar**（规划中）这 3 款相互独立，却又能够混合部署配合使用的产品组成。它们均提供标准化的数据分片、分布式事务和数据库治理功能，可适用于如 Java 同构、异构语言、云原生等各种多样化的应用场景。

ShardingSphere 定位为关系型数据库中间件，旨在充分合理地在分布式的场景下利用关系型数据库的计算和存储能力，而并非实现一个全新的关系型数据库。它通过关注不变，进而抓住事物本质。关系型数据库当今依然占有巨大市场，是各个公司核心业务的基石。

Apache 官方发布从 4.0.0 版本开始：

- GitHub：[https://github.com/apache/incubator-shardingsphere/blob/dev/README_ZH.md](http://www.qfdmy.com/wp-content/themes/quanbaike/go.php?url=aHR0cHM6Ly9naXRodWIuY29tL2FwYWNoZS9pbmN1YmF0b3Itc2hhcmRpbmdzcGhlcmUvYmxvYi9kZXYvUkVBRE1FX1pILm1k)
- 官方网站：[https://shardingsphere.apache.org/index_zh.html](http://www.qfdmy.com/wp-content/themes/quanbaike/go.php?url=aHR0cHM6Ly9zaGFyZGluZ3NwaGVyZS5hcGFjaGUub3JnL2luZGV4X3poLmh0bWw=)

#### 部署 2 台 MySQL 容器

```shell
root@deployment:~# cd /usr/local/docker/
#有两台 mysql-0 和 mysql-1， 用dockere 部署， docker-compose.yml配置如下
root@deployment:/usr/local/docker# ll
total 28
drwxr-xr-x  7 root root 4096 Mar 21 05:51 ./
drwxr-xr-x 12 root root 4096 Mar 27 08:07 ../
drwxr-xr-x  3 root root 4096 Mar 20 10:50 mysql/
drwxr-xr-x  3 root root 4096 Mar 20 10:21 mysql-0/
drwxr-xr-x  3 root root 4096 Mar 20 10:21 mysql-1/
drwxr-xr-x  6 root root 4096 Mar 21 05:52 nacos-docker/
drwxr-xr-x  3 root root 4096 Mar 20 06:26 tomcat/
root@deployment:/usr/local/docker# cat mysql-0/docker-compose.yml
version: '3.1'
services:
  mysql-0:
    image: mysql
    container_name: mysql-0
    environment:
      MYSQL_ROOT_PASSWORD: root
    command:
      --default-authentication-plugin=mysql_native_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
    ports:
      - 3310:3306
    volumes:
      - ./data:/var/lib/mysql
root@deployment:/usr/local/docker# cat mysql-1/docker-compose.yml
version: '3.1'
services:
  mysql-1:
    image: mysql
    container_name: mysql-1
    environment:
      MYSQL_ROOT_PASSWORD: root
    command:
      --default-authentication-plugin=mysql_native_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
    ports:
      - 3311:3306
    volumes:

```

#### 手动创建测试库与表

- 在 `mysql-0` 上手动创建一个名为 `myshop_0` 的数据库
- 在 `mysql-1` 上手动创建一个名为 `myshop_1` 的数据库
- 分别在两个数据库上创建测试表，建表语句如下

```sql
CREATE TABLE tb_order_0 (id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY, order_id BIGINT(20) NOT NULL, user_id BIGINT(20) NOT NULL);
CREATE TABLE tb_order_1 (id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY, order_id BIGINT(20) NOT NULL, user_id BIGINT(20) NOT NULL);
CREATE TABLE tb_order_item_0 (id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY, user_id BIGINT(20) NOT NULL, order_id BIGINT(20) NOT NULL, order_item_id BIGINT(20) NOT NULL);
CREATE TABLE tb_order_item_1 (id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY, user_id BIGINT(20) NOT NULL, order_id BIGINT(20) NOT NULL, order_item_id BIGINT(20) NOT NULL);
```

+ 此时的数据库结构如下

  ```txt
  mysql-0
      myshop_0
          tb_order_0
          tb_order_1
          tb_order_item_0
          tb_order_item_1
  mysql-1
      myshop_1
          tb_order_0
          tb_order_1
          tb_order_item_0
          tb_order_item_1
  ```

+ 另外，mysql 这个数据库是用来和我们工程做映射的，用做逻辑库

    ```shell
    CREATE TABLE tb_order (id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY, order_id BIGINT(20) NOT NULL, user_id BIGINT(20) NOT NULL);
    CREATE TABLE tb_order_item (id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY, user_id BIGINT(20) NOT NULL, order_id BIGINT(20) NOT NULL, order_item_id BIGINT(20) NOT NULL);
    ```

    ```txt
    mysql
        myshop
            tb_order
            tb_order_item
    ```
#### 项目配置

参见代码...