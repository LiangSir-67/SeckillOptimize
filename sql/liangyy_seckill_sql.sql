/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.21 : Database - liangyy_seckill
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE
DATABASE /*!32312 IF NOT EXISTS*/`liangyy_seckill` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE
`liangyy_seckill`;

/*Table structure for table `item` */

DROP TABLE IF EXISTS `item`;

CREATE TABLE `item`
(
    `id`    int                                 NOT NULL AUTO_INCREMENT COMMENT '商品id',
    `title` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '""' COMMENT '商品名称',
    `price` double(10, 0
) NOT NULL DEFAULT '0' COMMENT '商品价格',
  `description` varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '""' COMMENT '商品描述',
  `sales` int NOT NULL DEFAULT '0' COMMENT '商品销量',
  `img_url` varchar(2083) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '""' COMMENT '商品描述图片的url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `item` */

insert into `item`(`id`, `title`, `price`, `description`, `sales`, `img_url`)
values (1, '芙蓉王', 25, '我的最爱', 2,
        'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3037852354,2526133870&fm=26&gp=0.jpg'),
       (2, '黑兰州', 18, '还好还好', 0,
        'https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3780913452,1218690967&fm=26&gp=0.jpg'),
       (3, '中华', 68, '是牌面，我买不起也不乐抽', 0,
        'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1777363204,1848825277&fm=26&gp=0.jpg');

/*Table structure for table `item_stock` */

DROP TABLE IF EXISTS `item_stock`;

CREATE TABLE `item_stock`
(
    `id`      int NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `stock`   int NOT NULL DEFAULT '0' COMMENT '商品库存',
    `item_id` int NOT NULL DEFAULT '0' COMMENT '商品id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `item_id_index` (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `item_stock` */

insert into `item_stock`(`id`, `stock`, `item_id`)
values (1, 10000, 1),
       (2, 500, 2),
       (3, 100, 3);

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '主键id',
    `user_id`     int                                                    NOT NULL DEFAULT '0' COMMENT '用户id',
    `item_id`     int                                                    NOT NULL DEFAULT '0' COMMENT '商品id',
    `item_price`  double                                                 NOT NULL DEFAULT '0' COMMENT '商品价格',
    `amount`      int                                                    NOT NULL DEFAULT '0' COMMENT '商品购买数量',
    `order_price` double                                                 NOT NULL DEFAULT '0' COMMENT '商品购买金额，若promoId非空，则表示秒杀商品价格',
    `promo_id`    int                                                    NOT NULL DEFAULT '0' COMMENT '秒杀商品id',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `order_info` */

insert into `order_info`(`id`, `user_id`, `item_id`, `item_price`, `amount`, `order_price`, `promo_id`)
values ('2021052700000100', 1, 1, 17, 1, 17, 1),
       ('2021052700000200', 1, 1, 17, 1, 17, 1);

/*Table structure for table `promo` */

DROP TABLE IF EXISTS `promo`;

CREATE TABLE `promo`
(
    `id`               int                                                     NOT NULL AUTO_INCREMENT COMMENT '秒杀商品id',
    `promo_name`       varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '""' COMMENT '秒杀商品名称',
    `start_date`       datetime                                                         DEFAULT NULL COMMENT '秒杀活动开始时间',
    `end_date`         datetime                                                         DEFAULT NULL COMMENT '秒杀活动结束时间',
    `item_id`          int                                                     NOT NULL DEFAULT '0' COMMENT '商品id',
    `promo_item_price` double                                                  NOT NULL DEFAULT '0' COMMENT '秒杀商品价格',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `promo` */

insert into `promo`(`id`, `promo_name`, `start_date`, `end_date`, `item_id`, `promo_item_price`)
values (1, '芙蓉王抢购活动', '2021-05-26 00:00:00', '2021-05-30 00:00:00', 1, 17),
       (2, '中华抢购活动', '2021-05-18 15:30:30', '2021-05-18 16:12:00', 3, 60);

/*Table structure for table `sequence_info` */

DROP TABLE IF EXISTS `sequence_info`;

CREATE TABLE `sequence_info`
(
    `name`          varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `current_value` int DEFAULT '0',
    `step`          int DEFAULT '0',
    PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sequence_info` */

insert into `sequence_info`(`name`, `current_value`, `step`)
values ('order_info', 3, 1);

/*Table structure for table `stock_log` */

DROP TABLE IF EXISTS `stock_log`;

CREATE TABLE `stock_log`
(
    `stock_log_id` varchar(60) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '""' COMMENT '库存流水id',
    `item_id`      int                                                    NOT NULL DEFAULT '0' COMMENT '商品id',
    `amount`       int                                                    NOT NULL DEFAULT '0' COMMENT '商品购买数量',
    `status`       int                                                    NOT NULL DEFAULT '0' COMMENT '1表示初始状态，2表示下单扣减库存成功，3表示下单回滚',
    PRIMARY KEY (`stock_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `stock_log` */

insert into `stock_log`(`stock_log_id`, `item_id`, `amount`, `status`)
values ('2192a80589514ef49fb4660ef0579a07', 1, 1, 2),
       ('642eb88debfa4ebc9ffabebbbe4f912c', 1, 1, 2);

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info`
(
    `id`             int                                                    NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `name`           varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '用户名',
    `gender`         tinyint                                                NOT NULL DEFAULT '0' COMMENT '用户性别',
    `age`            int                                                    NOT NULL DEFAULT '0' COMMENT '用户年龄',
    `telphone`       varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '用户电话号码',
    `register_mode`  varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '用户注册方式 // byphone,bywechat,byalipay',
    `third_party_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '用户三方登录id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `telphone_unique_index` (`telphone`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `user_info` */

insert into `user_info`(`id`, `name`, `gender`, `age`, `telphone`, `register_mode`, `third_party_id`)
values (1, 'LiangSir', 1, 21, '15500512465', 'byphone', ''),
       (2, '梁歪歪', 1, 21, '15500512464', 'byphone', '');

/*Table structure for table `user_password` */

DROP TABLE IF EXISTS `user_password`;

CREATE TABLE `user_password`
(
    `id`              int                                                     NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `encrpt_password` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '密文密码',
    `user_id`         int                                                     NOT NULL COMMENT '用户id',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `user_password` */

insert into `user_password`(`id`, `encrpt_password`, `user_id`)
values (1, 'ISMvKXpXpadDiUoOSoAfww==', 1),
       (2, 'entWqEGk0Ds/8Ox+Vlcd7w==', 2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
