DROP TABLE
IF
	EXISTS seckill;
DROP TABLE
IF
	EXISTS success_killed;
-- 用户表
-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;

-- 创建秒杀库存表
CREATE TABLE seckill(
`seckillId` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar (120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '库存数量',
`start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '秒杀开始时间',
`end_time` timestamp NOT NULL DEFAULT '1970-01-01 10:00:00' COMMENT '秒杀结束时间',
`creatTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
-- 设置索引
PRIMARY KEY (seckillId),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(creatTime)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

------------------------------------------------------------------------------

-- 初始化数据
insert into seckill(name, number, start_time, end_time)
values
  ('1000元秒杀iPhone X', 100, '2019-3-24 00:00:00', '2019-3-24 01:00:00'),
  ('500元秒杀小米 9', 100, '2019-3-24 00:00:00', '2019-3-24 01:00:00'),
  ('3000元秒杀 2080 Ti', 100, '2019-3-24 00:00:00', '2019-3-24 01:00:00'),
  ('2000元秒杀 6D Mart II', 100, '2019-3-24 00:00:00', '2019-3-24 01:00:00'),


-- 秒杀成功明细表
-- 用户登录认证相关信息
CREATE TABLE success_killed(
`seckillId` bigint NOT NULL '秒杀商品id',
`userPhone` bigint NOT NULL COMMENT '用户手机号',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标识:-1:无效；0：成功；1：已付款',
`creatTime` timestamp NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckillId, userPhone),  /*联合主键，防止一个用户对同一个产品做秒杀*/
KEY idx_create_time(creatTime)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';


