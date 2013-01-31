-- 删除数据库
DROP DATABASE IF EXISTS qic_db ;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS qic_db DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci; 

USE qic_db;

-- 创建策略日收益率表
DROP TABLE IF EXISTS `qic_db`.`strategy_daily_yield`;

CREATE TABLE qic_db.strategy_daily_yield(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	strategyID VARCHAR(40) NOT NULL               COMMENT '策略id. uuid',
	yield      DECIMAL(16,4)                      COMMENT '收益率',
	updateDate DATE NOT NULL                      COMMENT '更新日期',
	PRIMARY KEY(id)
)COMMENT='策略日收益率表' ENGINE=INNODB DEFAULT CHARSET=utf8 ;

-- 创建用户表
CREATE TABLE user_info(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	`name` VARCHAR(200) NOT NULL                  COMMENT '名字',
	`account` VARCHAR(200) NOT NULL UNIQUE        COMMENT '帐号',
	pwd VARCHAR(200) NOT NULL                     COMMENT '密码, md5加密',
	phone VARCHAR(100)                            COMMENT '联系电话',
	email VARCHAR(100) NOT NULL UNIQUE            COMMENT 'email',
	idcard VARCHAR(100)                           COMMENT '身份证号码',
	sale_dep BIGINT                               COMMENT '营业部id',
	capital_account VARCHAR(100)                  COMMENT '资金帐号',
	address VARCHAR(200)                          COMMENT '联系地址',
	post    VARCHAR(50)                           COMMENT '邮编',
	sdate   DATE  NOT NULL                        COMMENT '启用日期',
	edate   DATE  NOT NULL                        COMMENT '结束日期',
	apply_date   DATE                              COMMENT '申请日期',
	`status`  INT   NOT NULL DEFAULT 10           COMMENT '状态, 1 禁用, 2 未激活, -100 删除 10 正常',
	utype     INT   NOT NULL DEFAULT  1           COMMENT '用户类型. 1. 营业部用户, 2. 系统用户',
	utime   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间,这个字段会在数据插入与更新时自动更新',
	PRIMARY KEY (id)                             /*指定主键*/
)COMMENT='用户信息表' ENGINE=INNODB DEFAULT CHARSET=utf8 ;

-- 创建营业部表
CREATE TABLE sale_department(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	`name`  VARCHAR(200) NOT NULL UNIQUE          COMMENT '名称',
	PRIMARY KEY (id)
)COMMENT='营业部表' ENGINE=INNODB DEFAULT CHARSET=utf8 ;

ALTER TABLE user_info 
	ADD INDEX fk_user_sale_dep(sale_dep),
	ADD CONSTRAINT fkc_user_sale_dep
	FOREIGN KEY (sale_dep)
	REFERENCES sale_department(id);
	
-- 创建角色表

CREATE TABLE role_info(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	`name` VARCHAR(50) NOT NULL  UNIQUE           COMMENT '名称',
	desp   VARCHAR(200)                           COMMENT '描述',
	PRIMARY KEY (id)
)COMMENT='角色表' ENGINE=INNODB DEFAULT CHARSET=utf8 ;

-- 创建 用户与角色 关联表
CREATE TABLE user_role(
	uid BIGINT NOT NULL,
	rid BIGINT NOT NULL,
	PRIMARY KEY(uid, rid)
)COMMENT='用户与角色关联表' ENGINE=INNODB DEFAULT CHARSET=utf8 ;

ALTER TABLE user_role
	ADD INDEX fk_user_role_uid(uid),
	ADD CONSTRAINT fkc_user_role_uid
	FOREIGN KEY (uid)
	REFERENCES user_info(id);
	
ALTER TABLE user_role
	ADD INDEX fk_user_role_rid(rid),
	ADD CONSTRAINT fkc_user_role_rid
	FOREIGN KEY (rid)
	REFERENCES role_info(id);

-- 创建功能点表

CREATE TABLE function_info(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	`name` VARCHAR(50) NOT NULL                   COMMENT '名称',
	`action` VARCHAR(200)                         COMMENT '功能点操作相应调用',
	`code`   VARCHAR(28) NOT NULL UNIQUE          COMMENT '每4个字符表示一层, 可以建立7层. 如 00010002. 同时通过层级里的序号可以反应前后关系',
	pid      BIGINT                               COMMENT '父菜单id, 根节点用null表示',
	PRIMARY KEY (id)
)COMMENT='功能点表' ENGINE=INNODB DEFAULT CHARSET=utf8 ;

ALTER TABLE function_info
	ADD CONSTRAINT fkc_function_parent
	FOREIGN KEY (pid)
	REFERENCES function_info(id);
	
-- 创建 功能点与角色关联表
CREATE TABLE role_function(
	rid  BIGINT NOT NULL                          COMMENT '对应于 role_info 表的id, 也就是角色id',
	fid  BIGINT NOT NULL                          COMMENT '对应于 function_info 表的id, 也就是功能点id',
	PRIMARY KEY (rid, fid) 
)COMMENT='功能点与角色关联表' ENGINE=INNODB DEFAULT CHARSET=utf8;	

ALTER TABLE role_function
	ADD CONSTRAINT fkc_role_function_rid
	FOREIGN KEY(rid)
	REFERENCES role_info(id);

ALTER TABLE role_function
	ADD CONSTRAINT fkc_role_function_fid
	FOREIGN KEY(fid)
	REFERENCES function_info(id);
	
	
-- 创建 策略基本信息表

CREATE TABLE strategy_baseinfo(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	st_uuid VARCHAR(40) NOT NULL                  COMMENT '策略的uuid,用java代码生成, qicore要用到这个值,通过这个值进行关联',
	`name` VARCHAR(50) NOT NULL UNIQUE            COMMENT '名称',
	trade_type SMALLINT    NOT NULL DEFAULT  1    COMMENT '交易类型: 1. 选股型 2. 择时型 3. 交易型 4. 其他',
	trade_variety  SMALLINT NOT NULL DEFAULT  1   COMMENT '交易品种: 1. 股票 2. 期货 3. 混合',
	up_time  DATETIME                             COMMENT '上架时间',
	down_time DATETIME                            COMMENT '下架时间',
	provider  VARCHAR(50)                         COMMENT '策略提供者',
	provider_desp  VARCHAR(200)                   COMMENT '策略提供者的简单描述',
	desp           VARCHAR(200)                   COMMENT '策略简介',
	lookback_stime DATETIME                       COMMENT '策略回测开始时间',
	lookback_etime DATETIME                       COMMENT '策略回测结束时间',
	`status` SMALLINT NOT NULL DEFAULT 1          COMMENT '策略状态: 1.待审核(也就是上传完成), 2. 沙箱测试  3. 回测中   4. 上架  5 下架 6 已回测 7 待下架  -100 回收站',
	discuss_total     INT NOT NULL DEFAULT 0      COMMENT '总评论分(也就是所有的评论总分)',
	discuss_count     INT NOT NULL DEFAULT 0      COMMENT '评论人数',
	collect_count     INT NOT NULL DEFAULT 0      COMMENT '收藏人数',
	order_count       INT NOT NULL DEFAULT 0      COMMENT '订阅人数',
	stup_uid          BIGINT                      COMMENT '策略上传用户, 关联到用户表. 这个字段可以为空',
	PRIMARY KEY (id)
)COMMENT='策略基本信息表' ENGINE=INNODB DEFAULT CHARSET=utf8 ;

ALTER TABLE strategy_baseinfo ADD INDEX i_strategy_info_uuid(st_uuid);

ALTER TABLE strategy_baseinfo
    ADD CONSTRAINT fkc_strategy_baseinfo_stuid
    FOREIGN KEY (stup_uid)
    REFERENCES user_info(id);
	
-- 创建 用户收藏策略关联表
CREATE TABLE user_strategy_collect(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	uid BIGINT NOT NULL                           COMMENT '对应于 user_info 表id, 也就是用户id',
	stid BIGINT NOT NULL                          COMMENT '对应于 strategy_baseinfo 表id, 也就是策略id',
	collect_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间,在insert时插入,在update时不会变',
	PRIMARY KEY(id)	
)COMMENT='用户收藏策略关联表' ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE user_strategy_collect
	ADD CONSTRAINT fkc_user_strategy_collect_uid
	FOREIGN KEY (uid)
	REFERENCES user_info(id);

ALTER TABLE user_strategy_collect
	ADD CONSTRAINT fkc_user_strategy_collect_stid
	FOREIGN KEY (stid)
	REFERENCES strategy_baseinfo(id);

ALTER TABLE user_strategy_collect ADD UNIQUE i_uscollect_uid_stid (uid, stid);

-- 创建 用户订阅策略关联表
DROP TABLE IF EXISTS user_strategy_order;

CREATE TABLE user_strategy_order(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	uid BIGINT NOT NULL                           COMMENT '对应于 user_info 表id, 也就是用户id',
	stid BIGINT NOT NULL                          COMMENT '对应于 strategy_baseinfo 表id, 也就是策略id',
	order_stime  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '订阅时间,在insert时插入,在update时不会变',
	order_etime  TIMESTAMP NOT NULL                 COMMENT '订阅到期时间',
	PRIMARY KEY(id)
)COMMENT='用户订阅策略关联表' ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE user_strategy_order
	ADD CONSTRAINT fkc_user_strategy_order_uid
	FOREIGN KEY (uid)
	REFERENCES user_info(id);

ALTER TABLE user_strategy_order
	ADD CONSTRAINT fkc_user_strategy_order_stid
	FOREIGN KEY (stid)
	REFERENCES strategy_baseinfo(id);

ALTER TABLE user_strategy_order ADD UNIQUE i_usorder_uid_stid (uid, stid);

-- 创建 用户评论策略关联表
CREATE TABLE user_strategy_discuss(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	uid BIGINT NOT NULL                           COMMENT '对应于 user_info 表id, 也就是用户id',
	stid BIGINT NOT NULL                          COMMENT '对应于 strategy_baseinfo 表id, 也就是策略id',
	dis_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间,在insert时插入,在update时不会变',
	content   VARCHAR(300)                        COMMENT '评论内容',
	star      SMALLINT  NOT NULL DEFAULT 0        COMMENT '星级',                    
	PRIMARY KEY(id)	
)COMMENT='用户订阅策略关联表' ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE user_strategy_discuss
	ADD CONSTRAINT fkc_user_strategy_discuss_uid
	FOREIGN KEY (uid)
	REFERENCES user_info(id);

ALTER TABLE user_strategy_discuss
	ADD CONSTRAINT fkc_user_strategy_discuss_stid
	FOREIGN KEY (stid)
	REFERENCES strategy_baseinfo(id);
	
ALTER TABLE user_strategy_discuss ADD UNIQUE i_usdiscuss_uid_stid (uid, stid);
	
-- 创建 模板表, 用于保存用户自定义模板
CREATE TABLE user_template(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	uid BIGINT NOT NULL                           COMMENT '对应于 user_info 表id, 也就是用户id',
	`name` VARCHAR(50) NOT NULL                   COMMENT '名称',
	`type` SMALLINT    NOT NULL DEFAULT  1        COMMENT '1. 自定义策略查询 2. 自定义股票池查询',
	content TEXT(60000) NOT NULL                  COMMENT '保存的内容,只提供存储,里面的内容自定义',
	PRIMARY KEY(id)
)COMMENT='用户自定义模板' ENGINE=INNODB DEFAULT CHARSET=utf8;	
	
ALTER TABLE user_template
	ADD INDEX fk_user_template_uid(uid),
	ADD CONSTRAINT fkc_user_template_uid
	FOREIGN KEY (uid)
	REFERENCES user_info(id);
	
-- 创建 股票池扩展属性
CREATE TABLE stock_pool_ext(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	spid    BIGINT   NOT NULL                     COMMENT '这里对应的是股票池的id, 不强制设置外键关系',
	discuss_total     INT NOT NULL DEFAULT 0      COMMENT '总评论分(也就是所有的评论总分)',
	discuss_count     INT NOT NULL DEFAULT 0      COMMENT '评论人数',
	collect_count     INT NOT NULL DEFAULT 0      COMMENT '收藏人数',	
	PRIMARY KEY(id)
)COMMENT='股票池扩展属性' ENGINE=INNODB DEFAULT CHARSET=utf8;
	
-- 创建 用户收藏股票池关联表
CREATE TABLE user_stock_pool_collect(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	uid BIGINT NOT NULL                           COMMENT '对应于 user_info 表id, 也就是用户id',
	spid BIGINT NOT NULL                          COMMENT '这里对应的是股票池的id, 不强制设置外键关系',
	collect_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间,在insert时插入,在update时不会变',
	PRIMARY KEY(id)	
)COMMENT='用户收藏股票池关联表' ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE user_stock_pool_collect
	ADD CONSTRAINT fkc_user_stock_pool_collect_uid
	FOREIGN KEY (uid)
	REFERENCES user_info(id);
	
ALTER TABLE user_stock_pool_collect ADD UNIQUE i_uspcollect_uid_stid (uid, spid);

-- 创建 用户评论股票池关联表
CREATE TABLE user_stock_pool_discuss(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	uid BIGINT NOT NULL                           COMMENT '对应于 user_info 表id, 也就是用户id',
	spid BIGINT NOT NULL                          COMMENT '这里对应的是股票池的id, 不强制设置外键关系',
	dis_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间,在insert时插入,在update时不会变',
	star      SMALLINT  NOT NULL DEFAULT 0        COMMENT '星级',                    
	PRIMARY KEY(id)	
)COMMENT='用户订阅策略关联表' ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE user_stock_pool_discuss
	ADD CONSTRAINT fkc_user_stock_pool_discuss_uid
	FOREIGN KEY (uid)
	REFERENCES user_info(id);
	
ALTER TABLE user_stock_pool_discuss ADD UNIQUE i_uspdiscuss_uid_stid (uid, spid);
	
-- 创建用户功能权限视图

CREATE VIEW v_user_functions AS 
(
SELECT DISTINCT 
  u.account, u.email, u.sdate, u.edate, u.status, f.id, f.action, f.code, f.name, f.pid 
FROM
  user_info u 
  INNER JOIN user_role ur 
    ON u.id = ur.uid 
  INNER JOIN role_info r 
    ON ur.rid = r.id 
  INNER JOIN role_function rf 
    ON rf.rid = r.id 
  INNER JOIN function_info f 
    ON rf.fid = f.id
) ;

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

ALTER TABLE `qic_db`.`strategy_baseinfo` ADD COLUMN `pass_time` DATETIME NULL  COMMENT '审核通过时间' ;
ALTER TABLE `qic_db`.`strategy_baseinfo` ADD COLUMN `upload_time` DATETIME NULL  COMMENT '策略上传时间' ;


-- 配置管理表
DROP TABLE IF EXISTS qic_db.config_manage;

CREATE TABLE qic_db.config_manage(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	key_name VARCHAR(40) NOT NULL                 COMMENT 'key_name 键',
	key_value  text COMMENT 'key_value 值',
	remark VARCHAR(100)                           COMMENT '备注',
	PRIMARY KEY(id)
)COMMENT='配置管理表' ENGINE=INNODB DEFAULT CHARSET=utf8 ;



-- 创建操作日志表
CREATE TABLE `operation_log_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增长',
  `cdate` date NOT NULL COMMENT '操作时间',
  `uid` bigint(20) NOT NULL COMMENT '用户ID',
  `func` varchar(200) NOT NULL COMMENT '操作功能',
  `content` varchar(200) NOT NULL COMMENT '操作内容',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='操作日志表';


--  股票池拓展信息表.
DROP TABLE IF EXISTS qic_db.stockpool_ext;
CREATE TABLE qic_db.stockpool_ext (
        id BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
        stockPoolCode VARCHAR (40) NOT NULL COMMENT '股票stockPoolCode',
        source VARCHAR(500)   COMMENT '股票来源',
        annualizedYield DECIMAL (16, 4) COMMENT '年化收益率',
        yearJensenRatio DECIMAL (16, 4) COMMENT '夏普比率',
        updateDate DATE NULL COMMENT '更新时间',
        orgId VARCHAR (40) NULL COMMENT 'orgId',
        PRIMARY KEY (id)
      ) COMMENT = '股票池拓展信息表' ENGINE = INNODB DEFAULT CHARSET = utf8 ;

 -- 高低频数据表
 DROP TABLE IF EXISTS qic_db.`strategy_high_low`;
CREATE TABLE qic_db.`strategy_high_low` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `StrategyID` VARCHAR(40) DEFAULT NULL,
  `RetainedProfits` DECIMAL(16,4) DEFAULT NULL COMMENT '总盈利+总亏损',
  `GrossProfit` DECIMAL(16,4) DEFAULT NULL COMMENT '净利润+总手续费',
  `OverallProfitability` DECIMAL(16,4) DEFAULT NULL COMMENT '∑每次盈利交易的金额;\n            一次交易的获利金额大于0，则该次交易为盈利交易\n            ',
  `OverallDeficit` DECIMAL(16,4) DEFAULT NULL COMMENT '∑每次亏损交易的金额；\n            一次交易的获利金额小于0，则该次交易为亏损交易\n            ',
  `CanhsiedRatio` DECIMAL(16,4) DEFAULT NULL COMMENT '总盈利/总亏损',
  `TradeCount` INT(11) DEFAULT NULL COMMENT 'Select count “资金变动流水表中，影响因素为平仓完全成交或平仓部\n分撤销的记录条数”',
  `LongPositionTradeCount` INT(11) DEFAULT NULL COMMENT '持仓方向为多头的交易次数',
  `ShortPositionTradeCount` INT(11) DEFAULT NULL COMMENT '持仓方向为空头的交易次数',
  `ProfitRatio` DECIMAL(16,4) DEFAULT NULL COMMENT '盈利次数 / 总交易次数',
  `ProfitCount` INT(11) DEFAULT NULL COMMENT '一次交易的获利大于0，记为1次盈利，将该次数汇总',
  `DeficitCount` INT(11) DEFAULT NULL COMMENT '一次交易的获利小于0，记为1次亏损，将该次数汇总',
  `PositionCloseCount` INT(11) DEFAULT NULL COMMENT '一次交易的获利等于0，记为1次亏损，将该次数汇总',
  `MAXSingleProfit` DECIMAL(16,4) DEFAULT NULL COMMENT 'Max(所有盈利交易的金额)',
  `MAXSingleDeficit` DECIMAL(16,4) DEFAULT NULL COMMENT 'Max{abs(所有亏交易的金额)|}',
  `MAXSingleProfitRatio` DECIMAL(16,4) DEFAULT NULL COMMENT '单次最大盈利/总盈利',
  `MAXSingleDeficitRatio` DECIMAL(16,4) DEFAULT NULL COMMENT '单次最大亏损/总亏损',
  `ProfitLossRatio` DECIMAL(16,4) DEFAULT NULL COMMENT '净利润/单次最大亏损',
  `SumOfCommission` DECIMAL(16,4) DEFAULT NULL COMMENT '∑每次盈利交易的金额',
  `Yield` DECIMAL(16,4) DEFAULT NULL COMMENT '净利润 / 初始资金',
  `AvgProfitOfMonth` DECIMAL(16,4) DEFAULT NULL COMMENT '(净利润 / 总交易的天数) ×21.75',
  `FloatingProfitAndLoss` DECIMAL(16,4) DEFAULT NULL COMMENT '回验结束时，如尚有未平持仓用结束点持仓的最新成交价计\n算浮动盈亏',
  `TotalAsset` DECIMAL(16,4) DEFAULT NULL,
  `YieldOfMonth` DECIMAL(16,4) DEFAULT NULL COMMENT '(收益率/总交易天数)× 21.75',
  `YieldOfYear` DECIMAL(16,4) DEFAULT NULL COMMENT '(收益率 / 总交易的天数)×252',
  `MAXSequentialDeficitCapital` DECIMAL(16,4) DEFAULT NULL COMMENT 'Max(连续亏损产生的亏损金额)',
  `LastSequentialDeficitCapital` DECIMAL(16,4) DEFAULT NULL,
  `MAXSequentialProfitCount` INT(11) DEFAULT NULL COMMENT 'Max（连续盈利的交易次数）',
  `LastSequentialProfitCount` INT(11) DEFAULT NULL,
  `MAXSequentialDeficitCount` INT(11) DEFAULT NULL COMMENT 'Max（连续亏损的交易次数）',
  `LastSequentialDeficitCount` INT(11) DEFAULT NULL,
  `TradeDays` INT(11) DEFAULT NULL COMMENT '依据交易时间区间和交易日历计算(SELECT  COUNT)',
  `MAXShortPositionTime` INT(11) DEFAULT NULL COMMENT '连续空仓的最大天数',
  `YieldOfMonthStandardDeviation` DECIMAL(16,4) DEFAULT NULL COMMENT 's^2=[(x1-x)^2+(x2-x)^2 +...(xn-x)^2]/（n-1）\n；其中，当交易天数小于30天时，xi是每日收益率的月化值，当交易天数大于30天时，xi是月收益率，x为xi序列均\n值',
  `SharpeIndex` DECIMAL(16,4) DEFAULT NULL COMMENT '√12（月化收益率-年无风险收益率/12）/月度收益率标准\n差',
  `MovingCost` DECIMAL(16,4) DEFAULT NULL COMMENT '(∑|理论成交价-实际成交价|)/交易次数',

  PRIMARY KEY (`ID`)
) ENGINE=INNODB AUTO_INCREMENT=10916 DEFAULT CHARSET=utf8 COMMENT='绩效指标扩展表';


-- 创建用户消息表 （系统发给用户的信息放在这个表）
CREATE TABLE qic_db.user_message (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长，用于主键',
  uid bigint(20) NOT NULL COMMENT '用户id',
  message text NOT NULL COMMENT '用户通知',
  message_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '通知时间,这个字段会在数据插入与更新时自动更新',
  status smallint(6) NOT NULL DEFAULT '1' COMMENT '1.未读，2.已读',
  PRIMARY KEY (id)
) COMMENT='用户消息表' ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 创建任务调度表 （主要是存放定时任务）
CREATE TABLE qic_db.scheduling_task (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长id',
  parameter text NOT NULL COMMENT '调度数据',
  module varchar(300) NOT NULL COMMENT '调度方法',
  runtime datetime NOT NULL COMMENT '调度时间',
  status smallint(6) NOT NULL DEFAULT '1' COMMENT '1.未执行，2已执行，3，执行中，4执行失败',
  PRIMARY KEY (id)
)  COMMENT='任务调度表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 回测服务器列表
DROP TABLE IF EXISTS `qic_db`.`backtest_server_list`;
DROP TABLE IF EXISTS `qic_db`.`server_list`;
CREATE TABLE `server_list` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ip` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '回测服务器ip',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '服务器状态 0 正常 -1禁用',
  `stype` int(4) NOT NULL DEFAULT '0' COMMENT '0 回测服务器  1： agent服务器',
  `memo` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注,备用的',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='回测服务器ip列表'
-- 为策略添加回测服务器id关联
ALTER TABLE `qic_db`.`strategy_baseinfo` ADD COLUMN `back_test_sid` INT DEFAULT -1  COMMENT '回测服务器id' ;

-- 增加策略基本信息表策略删除时间
ALTER TABLE qic_db.`strategy_baseinfo` ADD COLUMN del_time DATETIME NULL COMMENT '策略删除时间';

-- 为user_message 添加title字段
ALTER TABLE `qic_db`.`user_message` ADD COLUMN `title` VARCHAR(50) DEFAULT NULL  COMMENT '标题' ;
--修改user_message 把message字段类型改为text
ALTER TABLE `qic_db`.`user_message` MODIFY  message TEXT NOT NULL;