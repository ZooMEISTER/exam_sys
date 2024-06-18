/*
Navicat MySQL Data Transfer

Source Server         : mySQL
Source Server Version : 80030
Source Host           : localhost:3306
Source Database       : exam_sys_db

Target Server Type    : MYSQL
Target Server Version : 80030
File Encoding         : 65001

Date: 2024-06-18 19:33:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for application_add_course
-- ----------------------------
DROP TABLE IF EXISTS `application_add_course`;
CREATE TABLE `application_add_course` (
  `id` bigint NOT NULL,
  `subject_id` bigint NOT NULL,
  `icon` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `teachby` bigint NOT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `approve_status` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `aac_fk_1` (`subject_id`),
  KEY `aac_fk_2` (`teachby`),
  CONSTRAINT `aac_fk_1` FOREIGN KEY (`subject_id`) REFERENCES `sys_subject` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `aac_fk_2` FOREIGN KEY (`teachby`) REFERENCES `user_teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for application_to_teacher
-- ----------------------------
DROP TABLE IF EXISTS `application_to_teacher`;
CREATE TABLE `application_to_teacher` (
  `id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `approve_status` int NOT NULL DEFAULT '0',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `att_fk_1` (`student_id`),
  CONSTRAINT `att_fk_1` FOREIGN KEY (`student_id`) REFERENCES `user_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for relation_course_exam
-- ----------------------------
DROP TABLE IF EXISTS `relation_course_exam`;
CREATE TABLE `relation_course_exam` (
  `id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `exam_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rce_fk_1` (`course_id`),
  KEY `rce_fk_2` (`exam_id`),
  CONSTRAINT `rce_fk_1` FOREIGN KEY (`course_id`) REFERENCES `sys_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rce_fk_2` FOREIGN KEY (`exam_id`) REFERENCES `sys_exam` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for relation_course_student
-- ----------------------------
DROP TABLE IF EXISTS `relation_course_student`;
CREATE TABLE `relation_course_student` (
  `id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rcs_fk_1` (`course_id`),
  KEY `rcs_fk_2` (`student_id`),
  CONSTRAINT `rcs_fk_1` FOREIGN KEY (`course_id`) REFERENCES `sys_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rcs_fk_2` FOREIGN KEY (`student_id`) REFERENCES `user_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for relation_exam_aeskey
-- ----------------------------
DROP TABLE IF EXISTS `relation_exam_aeskey`;
CREATE TABLE `relation_exam_aeskey` (
  `id` bigint NOT NULL,
  `exam_id` bigint NOT NULL,
  `aes_key` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rea_fk_1` (`exam_id`),
  CONSTRAINT `rea_fk_1` FOREIGN KEY (`exam_id`) REFERENCES `sys_exam` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for relation_exam_paper
-- ----------------------------
DROP TABLE IF EXISTS `relation_exam_paper`;
CREATE TABLE `relation_exam_paper` (
  `id` bigint NOT NULL,
  `exam_id` bigint NOT NULL,
  `paper_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rep_fk_1` (`exam_id`),
  KEY `rep_fk_2` (`paper_id`),
  CONSTRAINT `rep_fk_1` FOREIGN KEY (`exam_id`) REFERENCES `sys_exam` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rep_fk_2` FOREIGN KEY (`paper_id`) REFERENCES `sys_paper` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for relation_subject_course
-- ----------------------------
DROP TABLE IF EXISTS `relation_subject_course`;
CREATE TABLE `relation_subject_course` (
  `id` bigint NOT NULL,
  `subject_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rsc_fk_1` (`subject_id`),
  KEY `rsc_fk_2` (`course_id`),
  CONSTRAINT `rsc_fk_1` FOREIGN KEY (`subject_id`) REFERENCES `sys_subject` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rsc_fk_2` FOREIGN KEY (`course_id`) REFERENCES `sys_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for respondent_exam_student
-- ----------------------------
DROP TABLE IF EXISTS `respondent_exam_student`;
CREATE TABLE `respondent_exam_student` (
  `id` bigint NOT NULL,
  `exam_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `respondent_path` varchar(255) NOT NULL,
  `final_score` int NOT NULL DEFAULT '-1',
  `sha256_code` varchar(255) NOT NULL,
  `sign` varchar(255) NOT NULL,
  `publickey` varchar(255) NOT NULL,
  `is_sign_verify_good` int NOT NULL DEFAULT '-1',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `res_fk_1` (`exam_id`),
  KEY `res_fk_2` (`student_id`),
  CONSTRAINT `res_fk_1` FOREIGN KEY (`exam_id`) REFERENCES `sys_exam` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `res_fk_2` FOREIGN KEY (`student_id`) REFERENCES `user_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for sys_course
-- ----------------------------
DROP TABLE IF EXISTS `sys_course`;
CREATE TABLE `sys_course` (
  `id` bigint NOT NULL,
  `icon` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `teachby` bigint NOT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sc_fk_1` (`teachby`),
  CONSTRAINT `sc_fk_1` FOREIGN KEY (`teachby`) REFERENCES `user_teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` bigint NOT NULL,
  `icon` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `subject_count` int NOT NULL DEFAULT '0',
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for sys_exam
-- ----------------------------
DROP TABLE IF EXISTS `sys_exam`;
CREATE TABLE `sys_exam` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `teachby` bigint NOT NULL,
  `type` int NOT NULL DEFAULT '1',
  `published` varchar(255) NOT NULL DEFAULT '0',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `se_fk_1` (`teachby`),
  CONSTRAINT `se_fk_1` FOREIGN KEY (`teachby`) REFERENCES `user_teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for sys_paper
-- ----------------------------
DROP TABLE IF EXISTS `sys_paper`;
CREATE TABLE `sys_paper` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `teachby` bigint NOT NULL,
  `score` int NOT NULL DEFAULT '100',
  `path` varchar(255) NOT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `aes_key` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sp_fk_1` (`teachby`),
  CONSTRAINT `sp_fk_1` FOREIGN KEY (`teachby`) REFERENCES `user_teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for sys_subject
-- ----------------------------
DROP TABLE IF EXISTS `sys_subject`;
CREATE TABLE `sys_subject` (
  `id` bigint NOT NULL,
  `icon` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `belongto` bigint NOT NULL,
  `course_count` int NOT NULL DEFAULT '0',
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `ss_fk_1` (`belongto`),
  CONSTRAINT `ss_fk_1` FOREIGN KEY (`belongto`) REFERENCES `sys_department` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_admin
-- ----------------------------
DROP TABLE IF EXISTS `user_admin`;
CREATE TABLE `user_admin` (
  `id` bigint NOT NULL,
  `avatar` longtext,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  `profilev` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_student
-- ----------------------------
DROP TABLE IF EXISTS `user_student`;
CREATE TABLE `user_student` (
  `id` bigint NOT NULL,
  `avatar` longtext,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  `profilev` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_super_admin
-- ----------------------------
DROP TABLE IF EXISTS `user_super_admin`;
CREATE TABLE `user_super_admin` (
  `id` bigint NOT NULL,
  `avatar` longtext,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  `profilev` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_teacher
-- ----------------------------
DROP TABLE IF EXISTS `user_teacher`;
CREATE TABLE `user_teacher` (
  `id` bigint NOT NULL,
  `avatar` longtext,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `deleted` int NOT NULL DEFAULT '0',
  `profilev` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
