-- 将原来的backtest_server_list表替换成server_list表  新添加了代理ip
DROP TABLE IF EXISTS `qic_db`.`backtest_server_list`;
DROP TABLE IF EXISTS `qic_db`.`server_list`;
CREATE TABLE `server_list` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ip` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '回测服务器ip',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '服务器状态 0 正常 -1禁用',
  `stype` int(4) NOT NULL DEFAULT '0' COMMENT '0 回测服务器  1： agent服务器',
  `memo` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注,备用的',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='回测服务器ip列表';
