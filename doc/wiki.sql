/*
 Navicat Premium Data Transfer

 Source Server         : stfu
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : wiki

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 31/07/2021 23:20:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `parent` bigint(20) NOT NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (100, 0, '前端开发', 100);
INSERT INTO `category` VALUES (101, 100, 'Vue', 101);
INSERT INTO `category` VALUES (102, 100, 'HTML & CSS', 102);
INSERT INTO `category` VALUES (200, 0, 'Java', 200);
INSERT INTO `category` VALUES (201, 200, '基础应用', 201);
INSERT INTO `category` VALUES (202, 200, '框架应用', 202);
INSERT INTO `category` VALUES (300, 0, 'Python', 300);
INSERT INTO `category` VALUES (301, 300, '基础应用', 301);
INSERT INTO `category` VALUES (302, 300, '进阶方向应用', 302);
INSERT INTO `category` VALUES (400, 0, '数据库', 400);
INSERT INTO `category` VALUES (401, 400, 'MySQL', 401);
INSERT INTO `category` VALUES (500, 0, '其它', 500);
INSERT INTO `category` VALUES (501, 500, '服务器', 501);
INSERT INTO `category` VALUES (502, 500, '开发工具', 502);
INSERT INTO `category` VALUES (503, 500, '热门服务端语言', 503);

-- ----------------------------
-- Table structure for content
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content`  (
  `id` bigint(20) NOT NULL COMMENT '文档id',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档内容' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` VALUES (1, '<blockquote><p>Typescript 开发的 Web 富文本编辑器， 轻量、简洁、易用、开源免费<br></p></blockquote>');
INSERT INTO `content` VALUES (2, '<p><font color=\"#4d80bf\" size=\"7\"><b>1111111</b></font><b></b></p>');
INSERT INTO `content` VALUES (3, '<table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><th></th><th></th><th></th><th></th><th></th></tr><tr><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td><td></td><td></td></tr></tbody></table><span style=\"background-color: rgb(123, 91, 161);\"></span><p><span style=\"background-color: rgb(123, 91, 161);\">发生撒地方撒地方撒地方撒地方</span></p>');
INSERT INTO `content` VALUES (72745612734500864, '<p>1111</p>');
INSERT INTO `content` VALUES (74322696762167296, '<blockquote><p><b><font size=\"7\">Vue起步</font></b><br></p></blockquote>');

-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '测试' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo
-- ----------------------------
INSERT INTO `demo` VALUES (1, '测试');

-- ----------------------------
-- Table structure for doc
-- ----------------------------
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `ebook_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '电子书id',
  `parent` bigint(20) NOT NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc
-- ----------------------------
INSERT INTO `doc` VALUES (1, 1, 0, '文档1', 1, 28, 13);
INSERT INTO `doc` VALUES (2, 1, 1, '文档1.1', 1, 21, 2);
INSERT INTO `doc` VALUES (3, 1, 0, '文档2', 2, 10, 2);
INSERT INTO `doc` VALUES (4, 1, 3, '文档2.1', 1, 8, 1);
INSERT INTO `doc` VALUES (5, 1, 3, '文档2.2', 2, 9, 2);
INSERT INTO `doc` VALUES (6, 1, 5, '文档2.2.1', 1, 8, 2);
INSERT INTO `doc` VALUES (74322696762167296, 2, 0, 'Vue起步', 1, 3, 0);

-- ----------------------------
-- Table structure for ebook
-- ----------------------------
DROP TABLE IF EXISTS `ebook`;
CREATE TABLE `ebook`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `category1_id` bigint(20) NULL DEFAULT NULL COMMENT '分类1',
  `category2_id` bigint(20) NULL DEFAULT NULL COMMENT '分类2',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `cover` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面',
  `doc_count` int(11) NOT NULL DEFAULT 0 COMMENT '文档数',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '电子书' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ebook
-- ----------------------------
INSERT INTO `ebook` VALUES (1, 'Spring Boot 入门教程', 200, 202, '零基础入门 Java 开发，企业级应用开发最佳首选框架', '/image/cover2.png', 6, 84, 22);
INSERT INTO `ebook` VALUES (2, 'Vue 入门教程', 100, 101, '零基础入门 Vue 开发，企业级应用开发最佳首选框架', '/image/cover2.png', 1, 3, 0);
INSERT INTO `ebook` VALUES (3, 'Python 入门教程', 300, 301, '零基础入门 Python 开发，企业级应用开发最佳首选框架', NULL, 0, 0, 0);
INSERT INTO `ebook` VALUES (4, 'Mysql 入门教程', 400, 401, '零基础入门 Mysql 开发，企业级应用开发最佳首选框架', NULL, 0, 0, 0);
INSERT INTO `ebook` VALUES (5, 'Oracle 入门教程', 400, 401, '零基础入门 Oracle 开发，企业级应用开发最佳首选框架', NULL, 0, 0, 0);

-- ----------------------------
-- Table structure for ebook_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `ebook_snapshot`;
CREATE TABLE `ebook_snapshot`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ebook_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '电子书id',
  `date` date NOT NULL COMMENT '快照日期',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `view_increase` int(11) NOT NULL DEFAULT 0 COMMENT '阅读增长',
  `vote_increase` int(11) NOT NULL DEFAULT 0 COMMENT '点赞增长',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ebook_id_date_unique`(`ebook_id`, `date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '电子书快照表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ebook_snapshot
-- ----------------------------
INSERT INTO `ebook_snapshot` VALUES (22, 1, '2021-07-25', 50, 5, 50, 5);
INSERT INTO `ebook_snapshot` VALUES (23, 2, '2021-07-25', 3, 0, 3, 0);
INSERT INTO `ebook_snapshot` VALUES (24, 3, '2021-07-25', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (25, 4, '2021-07-25', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (26, 5, '2021-07-25', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (37, 1, '2021-07-26', 72, 12, 22, 7);
INSERT INTO `ebook_snapshot` VALUES (38, 2, '2021-07-26', 3, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (39, 3, '2021-07-26', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (40, 4, '2021-07-26', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (41, 5, '2021-07-26', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (44, 1, '2021-07-27', 84, 22, 12, 10);
INSERT INTO `ebook_snapshot` VALUES (45, 2, '2021-07-27', 3, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (46, 3, '2021-07-27', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (47, 4, '2021-07-27', 0, 0, 0, 0);
INSERT INTO `ebook_snapshot` VALUES (48, 5, '2021-07-27', 0, 0, 0, 0);

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '测试' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO `test` VALUES (1, '测试', 'password');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `login_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登陆名',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_name_unique`(`login_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'test', '测试', 'a817b7a48e1713f44d12605176f19333');
INSERT INTO `user` VALUES (73284555045474304, 'test1234', 'test12341111', '16d7a4fca7442dda3ad93c9a726597e4');
INSERT INTO `user` VALUES (73294424230203392, 'test4321', 'test4321', 'e555fdfacffb3eca7c0f8988e1cc3934');
INSERT INTO `user` VALUES (73297080872996864, 'test5555', 'test5555', 'f5ac6451e1daf9532bb358283dd6ccab');

SET FOREIGN_KEY_CHECKS = 1;
