-- 给用户表增加申请时间列
ALTER TABLE `qic_db`.`user_info` ADD COLUMN `apply_date` DATE NULL  COMMENT '申请时间' AFTER `edate`;

-- 配置管理表
DROP TABLE IF EXISTS qic_db.config_manage;

CREATE TABLE qic_db.config_manage(
	id BIGINT NOT NULL AUTO_INCREMENT             COMMENT '自增长,用于主键',
	key_name VARCHAR(40) NOT NULL                 COMMENT 'key_name 键',
	key_value  text COMMENT 'key_value 值',
	remark VARCHAR(100)                           COMMENT '备注',
	PRIMARY KEY(id)
)COMMENT='配置管理表' ENGINE=INNODB DEFAULT CHARSET=utf8 ;


-- 原来有这个表,修改了对应字段
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



--  创建操作日志表
CREATE TABLE `operation_log_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增长',
  `cdate` date NOT NULL COMMENT '操作时间',
  `uid` bigint(20) NOT NULL COMMENT '用户ID',
  `func` varchar(200) NOT NULL COMMENT '操作功能',
  `content` varchar(200) NOT NULL COMMENT '操作内容',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='操作日志表';


-- 创建用户消息表 （系统发给用户的信息放在这个表）
CREATE TABLE qic_db.user_message (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长，用于主键',
  uid bigint(20) NOT NULL COMMENT '用户id',
  message varchar(300) NOT NULL COMMENT '用户通知',
  messge_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '通知时间,这个字段会在数据插入与更新时自动更新',
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


--  策略拓展信息表.
DROP TABLE IF EXISTS qic_db.Strategy_ext;
CREATE TABLE Strategy_ext (
        id BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增长,用于主键',
        st_uuid VARCHAR (40) NOT NULL COMMENT '策略的uuid',
        tradeCount INT  NULL DEFAULT 0 COMMENT '交易次数',
        yield DECIMAL (16, 4) COMMENT '收益率',
        profitRatio DECIMAL (16, 4) COMMENT '盈利比率(胜率)',
        yieldOfMonthStandardDeviation DECIMAL (16, 4) COMMENT '月度收益标准差',
        PRIMARY KEY (id)
      ) COMMENT = '策略拓展信息表' ENGINE = INNODB DEFAULT CHARSET = utf8 ;

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
