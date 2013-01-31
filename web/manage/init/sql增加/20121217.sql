-- 复制qsm里上传策略的表

CREATE TABLE `qstrategy` (
  `StrategyID` varchar(40) NOT NULL,
  `StrategyName` varchar(100) DEFAULT NULL,
  `FilePath` varchar(500) DEFAULT NULL COMMENT '策略下载并解压后存储的完整路径''',
  `FtpPath` varchar(500) DEFAULT NULL COMMENT '策略所存放的ftp的完整路径',
  `AgentIP` varchar(20) DEFAULT NULL COMMENT '分配的Agent的IP地址',
  `IsAutoRun` bit(1) DEFAULT NULL COMMENT '是否自动启动',
  `TransactTime` datetime DEFAULT NULL COMMENT '数据插入时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`StrategyID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储当前的策略信息、配置信息、运行信息';

-- 添加上传时间和审核通过时间
ALTER TABLE `qic_db`.`strategy_baseinfo` ADD COLUMN `pass_time` DATETIME NULL  COMMENT '审核通过时间' ;
ALTER TABLE `qic_db`.`strategy_baseinfo` ADD COLUMN `upload_time` DATETIME NULL  COMMENT '策略上传时间' ;

-- 增加策略基本信息表策略删除时间
ALTER TABLE qic_db.`strategy_baseinfo` ADD COLUMN del_time DATETIME NULL COMMENT '策略删除时间';