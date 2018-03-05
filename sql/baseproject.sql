/*
Navicat MySQL Data Transfer

Source Server         : my
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : baseproject

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-03-05 11:15:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `learn_resource`
-- ----------------------------
DROP TABLE IF EXISTS `learn_resource`;
CREATE TABLE `learn_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `author` varchar(20) DEFAULT NULL COMMENT '名称',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `url` varchar(100) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1034 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of learn_resource
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_operlogs`
-- ----------------------------
DROP TABLE IF EXISTS `sys_operlogs`;
CREATE TABLE `sys_operlogs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `loginName` varchar(100) DEFAULT NULL COMMENT '操作人',
  `operTime` datetime NOT NULL COMMENT '操作时间',
  `content` varchar(5000) NOT NULL COMMENT '内容 当type2用|分割, ',
  `ip` varchar(100) DEFAULT NULL COMMENT '操作ip',
  `types` int(2) NOT NULL COMMENT '0为系统管理员操作,1为用户操作,2用户订单操作 当type=2时orderId必须有值',
  `orderId` bigint(20) DEFAULT NULL COMMENT '产品id 产品表的id字段',
  PRIMARY KEY (`id`),
  KEY `orderId` (`orderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_operlogs
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `available` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `parentIds` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `resourceType` enum('menu','button') DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `ico` varchar(255) DEFAULT 'icon-cogs' COMMENT '图标',
  `sort` int(50) DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '', '员工管理', '58', '0/', 'user:all', 'menu', '/user', 'icon-cogs', '1');
INSERT INTO `sys_permission` VALUES ('2', '', '用户添加', '1', '0/1', 'user:add', 'button', '/user/add', 'icon-cogs', '2');
INSERT INTO `sys_permission` VALUES ('3', '', '用户删除', '1', '0/1', 'user:delete', 'button', '/user/delete', 'icon-cogs', '3');
INSERT INTO `sys_permission` VALUES ('4', '', '角色管理', '58', '0/', 'role:all', 'menu', '/role', 'icon-cogs', '4');
INSERT INTO `sys_permission` VALUES ('7', '', '菜单管理', '52', '0/', 'menu:all', 'menu', '/menu', 'icon-cogs', '7');
INSERT INTO `sys_permission` VALUES ('52', '', '系统配置', '0', null, '', 'menu', '', 'icon-beaker', '52');
INSERT INTO `sys_permission` VALUES ('57', '', '操作日志', '52', null, 'logs:all', 'menu', '/logs', 'icon-cogs', '57');
INSERT INTO `sys_permission` VALUES ('58', '', '管理中心', '0', null, '', 'menu', '', 'icon-desktop', '58');
INSERT INTO `sys_permission` VALUES ('97', '', '测试用例', '0', null, '', 'menu', '', 'icon-beer', '97');
INSERT INTO `sys_permission` VALUES ('99', '', '功能展示', '97', null, 'learn:all', 'menu', '/learn', 'icon-cogs', '99');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `available` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '', '管理员', 'admin');

-- ----------------------------
-- Table structure for `sys_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `roleId` bigint(20) NOT NULL,
  `permissionId` bigint(20) NOT NULL,
  KEY `FK_pn90qffgw1e6lo1xhw964qadf` (`roleId`),
  KEY `FK_qr3wmwfxapktvdv5g6d4mbtta` (`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1', '1');
INSERT INTO `sys_role_permission` VALUES ('1', '4');
INSERT INTO `sys_role_permission` VALUES ('1', '7');
INSERT INTO `sys_role_permission` VALUES ('1', '57');
INSERT INTO `sys_role_permission` VALUES ('1', '58');
INSERT INTO `sys_role_permission` VALUES ('1', '52');
INSERT INTO `sys_role_permission` VALUES ('1', '97');
INSERT INTO `sys_role_permission` VALUES ('1', '99');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `state` tinyint(4) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `UK_45fvrme4q2wy85b1vbf55hm6s` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('9', '系统管理员', 'admin', '92a8fbaa4dea030e3c6a63b9ca0e8e37', 'jDiIDEvp8iltw8GYnpP9hSDPDctiacXH', '0');

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `uid` bigint(20) NOT NULL,
  `roleId` bigint(20) NOT NULL,
  KEY `FK_io5ssq2ol6uqcx9nll8gfnm4n` (`uid`),
  KEY `FK_suwqmd7mystg1lwv8o4ffxaag` (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('9', '1');
