# seckill

* [1 秒杀系统业务分析](1-秒杀系统业务分析)
* [2 开发环境](#2-开发环境)
* [3 工程创建](#3-工程创建)
* [4 业务实现](#4-业务实现)
  * [4.1 数据库建表](#4.1-数据库建表)
  * [4.2 DAO实体和接口开发](#4.2-DAO实体和接口开发)
  * [4.3 Service层开发](#4.3-Service层开发)

-----------------------------

## 1 秒杀系统业务分析

首先分析，秒杀系统问题的本质其实是对商品库存的管理。主要业务逻辑如下图：

<div align="center"><img src="pics//1553423680(1).png" width="500px"></div>

用户针对库存业务分析：

<div align="center"><img src="pics//1553423931(1).png" width="500px"></div>

用户的购买行为：

<div align="center"><img src="pics//1553424057(1).png" width="500px"></div>

**难点**：如何高效地处理“竞争”。

## 2 开发环境

InteliJ IDEA + Maven + Tomcat8 + JDK8

## 3 工程创建

新建一个 Maven 工程，并完善相应的目录结构。

pom 文件的依赖可以分为 4 部分：

- **日志**。使用的是 slf4j + logback 的组合。
- **数据库**。数据库连接池 + DAO框架，MyBatis依赖。
- **Servlet web**。jsp 等。
- **Spring**。主要是 Spring 相关的依赖。

全部依赖参考 [pom.xml](https://github.com/MinheZ/seckill/blob/master/pom.xml)。

## 4 业务实现

### 4.1 数据库建表

- 秒杀库存表
- 秒杀成功明细表

创建 `seckill`表时，`end_time`字段默认值为`0000-00-00 00:00:00`，报错：1067 - Invalid default value for 'end_time'。

因为在MySQL5.7之后，默认值范围必须为`1970-01-01 10:00:00`~`2037-12-31 23:59:59`。

### 4.2 DAO实体和接口开发

首先是实体类 entity 的编写，分为 [Seckill](https://github.com/MinheZ/seckill/blob/master/src/main/java/com/seckill/entity/Seckill.java) 和 [SuccessKilled](https://github.com/MinheZ/seckill/blob/master/src/main/java/com/seckill/entity/SuccessKilled.java) 。

[SeckillDao](https://github.com/MinheZ/seckill/blob/master/src/main/java/com/seckill/dao/SeckillDao.java) 和 [SuccessKilledDao](https://github.com/MinheZ/seckill/blob/master/src/main/java/com/seckill/dao/SuccessKilledDao.java) 接口为查询数据库，或者修改数据库的一些方法。

剩下的就是一些 MyBatis 整合 Spring 的配置文件编写。

启用 Junit 单元测试的时候遇到一个小插曲，如下：

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/seckill?characterEncoding=UTF-8
jdbc.username=root
jdbc.password=****
```

之前没有遵循官方设计规范，没有添加`jdbc.`前缀，导致数据库连接异常 ERROR 1045 (28000)。

### 4.3 Service层开发

新建一个 [SeckillService](https://github.com/MinheZ/seckill/blob/master/src/main/java/com/seckill/service/SeckillService.java) 的接口，完成秒杀业务逻辑的一些方法。

业务逻辑为，当秒杀开始前，秒杀页面只显示秒杀商品类型和倒计时。只有当秒杀开始的时候，才暴露秒杀地址，防止脚本提前登录。

定义一个 Exposer 类来实现此功能。

<div align="center"><img src="pics//1553561741(1).png" width="500px"></div>

详情页流程逻辑

<div align="center"><img src="pics//1553561891(1).png" width="500px"></div>

Restful 规范：

- GET -> 查询操作
- POST -> 添加/修改操作
- PUT -> 修改操作（幂等操作）
- DELETE -> 删除操作

