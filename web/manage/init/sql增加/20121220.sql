-- 回测服务器列表 add by liujl 2012-12-20

CREATE TABLE `backtest_server_list` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ip` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '回测服务器ip',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '服务器状态 0 正常 -1禁用',
  `memo` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注,备用的',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='回测服务器ip列表';

-- 为策略添加回测服务器id关联 add by liujl 2012-12-20
ALTER TABLE `qic_db`.`strategy_baseinfo` ADD COLUMN `back_test_sid` INT DEFAULT -1  COMMENT '回测服务器id' ;