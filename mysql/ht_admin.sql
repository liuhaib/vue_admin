/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : ht_admin

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 10/03/2023 13:00:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名字',
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图标',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '归属'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('user', 'el-icon-user', 'icon');
INSERT INTO `sys_dict` VALUES ('s-tools', 'el-icon-s-tools', 'icon');
INSERT INTO `sys_dict` VALUES ('goods', 'el-icon-goods', 'icon');
INSERT INTO `sys_dict` VALUES ('user-solid', 'el-icon-user-solid', 'icon');
INSERT INTO `sys_dict` VALUES ('s-tools', 'el-icon-s-tools', 'icon');
INSERT INTO `sys_dict` VALUES ('setting', 'el-icon-setting', 'icon');
INSERT INTO `sys_dict` VALUES ('more', 'el-icon-more', 'icon');
INSERT INTO `sys_dict` VALUES ('more-outline', 'el-icon-more-outline', 'icon');
INSERT INTO `sys_dict` VALUES ('s-order', 'el-icon-s-order', 'icon');
INSERT INTO `sys_dict` VALUES ('s-home', 'el-icon-s-home', 'icon');
INSERT INTO `sys_dict` VALUES ('s-cooperation', 'el-icon-s-cooperation', 'icon');
INSERT INTO `sys_dict` VALUES ('s-grid', 'el-icon-s-grid', 'icon');
INSERT INTO `sys_dict` VALUES ('menu', 'el-icon-menu', 'icon');
INSERT INTO `sys_dict` VALUES ('s-custom', 'el-icon-s-custom', 'icon');
INSERT INTO `sys_dict` VALUES ('tickets', 'el-icon-tickets', 'icon');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件名称',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件类型',
  `size` bigint(0) NULL DEFAULT NULL COMMENT '文件大小(kb)',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '下载链接',
  `md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件md5',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `enable` tinyint(1) NULL DEFAULT 1 COMMENT '是否禁用链接',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES (1, '2022-09-15_41928a3ae05d480ebb4459863151f40c.jpg', 'jpg', 1, 'http://lhbtb.xcxan.cn/2022-09-15_41928a3ae05d480ebb4459863151f40c.jpg', '98c3e82598a8ea3079ab7de4ac57f4b2', 0, 1);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路径',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图标',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `pid` bigint(0) NULL DEFAULT NULL COMMENT '为空是父级菜单  不然是父级菜单的id',
  `page_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '页面路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '主页', '/home', 'el-icon-user', '主页', NULL, 'Home');
INSERT INTO `sys_menu` VALUES (2, '系统管理', NULL, 'el-icon-menu', '系统管理', NULL, NULL);
INSERT INTO `sys_menu` VALUES (3, '用户管理', '/user', 'el-icon-user', '用户管理', 2, 'User');
INSERT INTO `sys_menu` VALUES (4, '角色管理', '/role', 'el-icon-user', '角色管理', 2, 'Role');
INSERT INTO `sys_menu` VALUES (5, '菜单管理', '/menu', 'el-icon-document', '菜单管理', 2, 'Menu');
INSERT INTO `sys_menu` VALUES (6, '文件管理', '/file', 'el-icon-document', '文件管理', 2, 'File');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `flag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '唯一标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', '全部权限', 'ROLE_ADMIN');
INSERT INTO `sys_role` VALUES (2, '普通用户', '部分权限', 'ROLE_USER');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `roleid` bigint(0) NOT NULL,
  `menuid` bigint(0) NOT NULL,
  PRIMARY KEY (`roleid`, `menuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (1, 5);
INSERT INTO `sys_role_menu` VALUES (1, 6);
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '注册时间',
  `avatar_url` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像图片',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色权限',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '123456', '管理员', '1626590287@qq.com', '17679263031', '天津', '2022-01-28 16:27:55', 'http://lhbtb.xcxan.cn/2022-09-15_41928a3ae05d480ebb4459863151f40c.jpg', 'ROLE_ADMIN');
INSERT INTO `sys_user` VALUES (5, 'user', '666666', '用户', '1626590287@qq.com', '17679263031', '南极', '2022-04-28 18:42:56', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', 'ROLE_USER');
INSERT INTO `sys_user` VALUES (6, 'zzz', '123', 'cs', '1626590287@qq.com', '17679263031', 'nj', '2022-05-28 20:37:20', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (7, '杀杀杀', '1111', 'mm', '1626590287@qq.com', '17679263031', '11', '2022-04-03 13:24:22', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (9, '123', '666666', '123', '1626590287@qq.com', '17679263031', '123', '2022-06-01 16:19:19', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (11, 'lhb', '666666', '海滨', '1626590287@qq.com', '123456', '江西', '2022-08-03 18:34:08', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (12, 'emo', '666666', '零九百', '123456@qq.com', '123456789', 'nh', '2022-08-03 18:35:13', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (13, 'sdfg', '666666', 'dfg', '1626590287@qq.com', '17679263031', 'dfg', '2022-03-01 18:35:43', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (14, '123', '666666', '123', '1626590287@qq.com', '123456', '123', '2022-05-06 14:35:36', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (15, '123456', '666666', '123456', '1626590287@qq.com', '123456', '123456', '2022-11-06 19:50:34', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (16, '110', '666666', '456', '1626590287@qq.com', '17679263031', 'ccc', '2022-10-01 19:53:08', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (17, 'success', '666666', '编辑部', '1626590287@qq.com', '17679263031', 'success', '2022-05-01 19:56:54', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (18, 'abc', '666666', 'success', '1626590287@qq.com', '17679263031', 'success', '2022-06-01 19:57:13', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', NULL);
INSERT INTO `sys_user` VALUES (20, 'sir', '123456', '用户', '123456', '123456', '表空间', '2022-12-01 19:29:51', 'http://localhost:8081/file/2022-04-14_6c1d297e70fd4986a6ec2c3c488e60b8.jpg', '');

SET FOREIGN_KEY_CHECKS = 1;
