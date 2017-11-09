USE `bleach`;

--
-- Table structure for table `t_human`
--
CREATE TABLE IF NOT EXISTS `t_human` (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `channel` varchar(255) NOT NULL COMMENT '渠道',
  `accountId` varchar(255) NOT NULL COMMENT '所属的账号ID',
  `originalServerId` int(11) NOT NULL COMMENT '初始服务器ID',
  `currentServerId` int(11) NOT NULL COMMENT '当前服务器ID',
  `name` varchar(50) NOT NULL COMMENT '名字',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `level` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '等级',
  `loginTime` datetime DEFAULT NULL COMMENT '登陆时间',
  `totalOnlineTime` bigint(20) NOT NULL COMMENT '总在线时间',
  `exp` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前经验值',
  `vipLevel` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT 'VIP等级',
  `vipExp` bigint(20) NOT NULL DEFAULT '0' COMMENT 'VIP经验',
  `energy` int(11) NOT NULL DEFAULT '0' COMMENT '当前体力',
  `lastEnergyRecoverTime` timestamp NOT NULL DEFAULT '1980-01-01 00:00:00' COMMENT '上次恢复体力的时间戳',
  `buyEnergyCounts` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '当日已购买体力次数',
  `lastBuyEnergyCountsResetTime` timestamp NOT NULL DEFAULT '1980-01-01 00:00:00' COMMENT '上次体力购买次数重置时间',
  `accumulatedChargeDiamond` bigint(20) NOT NULL DEFAULT '0' COMMENT '累计充值钻石',
  `accumulatedConsumedChargeDiamond` bigint(20) NOT NULL DEFAULT '0' COMMENT '累计消费的充值钻石',
  `chargeDiamond` bigint(20) NOT NULL DEFAULT '0' COMMENT '充值钻石',
  `freeDiamond` bigint(20) NOT NULL DEFAULT '0' COMMENT '免费钻石',
  `gold` bigint(20) NOT NULL DEFAULT '0' COMMENT '金币',
  `timesOfDailyReward` tinyint(4) NOT NULL DEFAULT '0' COMMENT '当月领取的每日奖励的次数（签到次数）',
  `lastDailyRewardTime` timestamp NOT NULL DEFAULT '1980-01-01 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `t_item`
--
CREATE TABLE IF NOT EXISTS `t_item` (
  `id` bigint(20) NOT NULL COMMENT '道具ID',
  `humanId` bigint(20) NOT NULL COMMENT '所属角色ID',
  `templateId` int(11) NOT NULL COMMENT '道具模板ID',
  `overlap` int(11) NOT NULL COMMENT '叠加数量',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `humanId` (`humanId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `t_hero`
--
CREATE TABLE IF NOT EXISTS `t_hero` (
  `id` bigint(20) NOT NULL,
  `humanId` bigint(20) NOT NULL COMMENT '所属角色ID',
  `level` int(11) NOT NULL COMMENT '级别',
  `starCount` int(11) NOT NULL COMMENT '星星数',
  `fragmentCount` int(11) NOT NULL COMMENT '英雄碎片数量',
  `templateId` int(11) NOT NULL COMMENT '模板ID',
  `skillTemplateId1` int(11) DEFAULT NULL,
  `skillLevel1` int(11) DEFAULT NULL,
  `skillTemplateId2` int(11) DEFAULT NULL,
  `skillLevel2` int(11) DEFAULT NULL,
  `skillTemplateId3` int(11) DEFAULT NULL,
  `skillLevel3` int(11) DEFAULT NULL,
  `skillTemplateId4` int(11) DEFAULT NULL,
  `skillLevel4` int(11) DEFAULT NULL,
  `skillTemplateId5` int(11) DEFAULT NULL,
  `skillLevel5` int(11) DEFAULT NULL,
  `skillTemplateId6` int(11) DEFAULT NULL,
  `skillLevel6` int(11) DEFAULT NULL,
  `equipTemplateId1` int(11) DEFAULT NULL,
  `equipEnchantLevel1` int(11) DEFAULT NULL,
  `equipEnchantExp1` int(11) DEFAULT NULL,
  `equipTemplateId2` int(11) DEFAULT NULL,
  `equipEnchantLevel2` int(11) DEFAULT NULL,
  `equipEnchantExp2` int(11) DEFAULT NULL,
  `equipTemplateId3` int(11) DEFAULT NULL,
  `equipEnchantLevel3` int(11) DEFAULT NULL,
  `equipEnchantExp3` int(11) DEFAULT NULL,
  `equipTemplateId4` int(11) DEFAULT NULL,
  `equipEnchantLevel4` int(11) DEFAULT NULL,
  `equipEnchantExp4` int(11) DEFAULT NULL,
  `equipTemplateId5` int(11) DEFAULT NULL,
  `equipEnchantLevel5` int(11) DEFAULT NULL,
  `equipEnchantExp5` int(11) DEFAULT NULL,
  `equipTemplateId6` int(11) DEFAULT NULL,
  `equipEnchantLevel6` int(11) DEFAULT NULL,
  `equipEnchantExp6` int(11) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `t_arena_snap`
--
CREATE TABLE IF NOT EXISTS `t_arena_snap` (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `name` varchar(50) NOT NULL COMMENT '角色名称',
  `level` int(11) NOT NULL COMMENT '角色等级',
  `arenaRank` int(11) NOT NULL COMMENT '竞技场排名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `t_server_status`
--
CREATE TABLE IF NOT EXISTS `bleach`.`t_server_status` (
  `serverId` int(11) NOT NULL COMMENT '服务器ID',
  `serverOpenTime` TIMESTAMP NOT NULL DEFAULT '1980-01-01 00:00:00' COMMENT '服务器的开服时间',
  PRIMARY KEY (`serverId`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT = '记录服务器相关信息的表';

--
-- Table structure for table `t_shop_discount`
--
CREATE TABLE IF NOT EXISTS `t_shop_discount` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `serverId` int(11) NOT NULL COMMENT '服务器Id',
  `shopTypeId` int(11) NOT NULL COMMENT '商店类型Id',
  `discount` int(11) NOT NULL COMMENT '折扣',
  `numMultiple` int(11) NOT NULL COMMENT '数量倍数',
  `startTime` datetime DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `t_shop`
--
CREATE TABLE IF NOT EXISTS `t_shop` (
  `id` bigint(20) NOT NULL COMMENT '商店id',
  `humanId` bigint(20) NOT NULL COMMENT '玩家角色Id',
  `shopType` int(11) NOT NULL COMMENT '商店类型Id',
  `goodBornTime` bigint(20) NOT NULL COMMENT '货物产生时间',
  `goodDataList` text NOT NULL COMMENT '货物数据列表',
  `manuallyRefreshCount` int(11) NOT NULL COMMENT '手动刷新次数',
  `lastResetManuallyRefreshCountTime` datetime DEFAULT NULL COMMENT '上次重置手动刷新次数的时间',
  `lastAutoRefreshTime` datetime DEFAULT NULL COMMENT '上次自动刷新时间',
  `isOpenForever` int(1) NOT NULL COMMENT '是否永久开放（1：永久开放， 0：非永久开放）',
  `closeTime` datetime DEFAULT NULL COMMENT '商店关闭时间（商店不是永久开放时此字段才有效）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `t_function`
--
CREATE TABLE IF NOT EXISTS `t_function` (
  `id` bigint(20) NOT NULL COMMENT '功能id',
  `humanId` bigint(20) NOT NULL COMMENT '玩家角色Id',
  `functionId` int(11) NOT NULL COMMENT '功能类型Id',
  `openTime` datetime DEFAULT NULL COMMENT '开启时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;