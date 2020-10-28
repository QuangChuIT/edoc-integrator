/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : edoc

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 08/09/2020 14:47:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for counter_
-- ----------------------------
DROP TABLE IF EXISTS `counter_`;
CREATE TABLE `counter_`  (
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `currentId` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of counter_
-- ----------------------------
INSERT INTO `counter_` VALUES ('com.bkav.edoc.service.database.entity.EdocAttachment', 11307);

-- ----------------------------
-- Table structure for edoc_attachment
-- ----------------------------
DROP TABLE IF EXISTS `edoc_attachment`;
CREATE TABLE `edoc_attachment`  (
  `attachment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organ_domain` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `full_path` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `type_` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size_` bigint(20) NULL DEFAULT NULL,
  `to_organ_domain` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `document_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`attachment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18061141 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_attachment
-- ----------------------------
INSERT INTO `edoc_attachment` VALUES (18061078, '000.00.13.H53', 'Báo cáo công tác QLNN về Thông tin và truyền thông tháng 8 và phương hướng tháng 9 năm 2020.doc', '2020-08-14 15:29:38', '000.00.13.H53/2020/8/14/272_1', 'application/msword', 134144, '000.00.13.H53', 272);
INSERT INTO `edoc_attachment` VALUES (18061079, '000.00.13.H53', 'Phụ lục báo cáo tháng 8.doc', '2020-08-14 15:29:38', '000.00.13.H53/2020/8/14/272_2', 'application/msword', 93184, '000.00.13.H53', 272);
INSERT INTO `edoc_attachment` VALUES (18061080, '000.00.13.H53', 'Báo cáo công tác QLNN về Thông tin và truyền thông tháng 8 và phương hướng tháng 9 năm 2020_Signed.pdf', '2020-08-14 15:29:38', '000.00.13.H53/2020/8/14/272_3', 'application/pdf', 448011, '000.00.13.H53', 272);
INSERT INTO `edoc_attachment` VALUES (18061081, '000.00.13.H53', 'phu luc kem theo_1.PDF', '2020-08-14 15:29:38', '000.00.13.H53/2020/8/14/272_4', 'application/pdf', 279030, '000.00.13.H53', 272);
INSERT INTO `edoc_attachment` VALUES (18061082, '000.00.13.H53', '1.1 HDSD xac thuc phan quyen tap trung_IAM.docx', '2020-08-14 17:06:54', '000.00.13.H53/2020/8/14/11253_1', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 2676732, NULL, NULL);
INSERT INTO `edoc_attachment` VALUES (18061083, '000.00.13.H53', '1.1 HDSD xac thuc phan quyen tap trung_IAM.docx', '2020-08-14 17:16:17', '000.00.13.H53/2020/8/14/11255_1', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 2676732, NULL, NULL);
INSERT INTO `edoc_attachment` VALUES (18061084, '000.00.13.H53', '1.1 HDSD xac thuc phan quyen tap trung_IAM.docx', '2020-08-14 17:25:44', '000.00.13.H53/2020/8/14/11256_1.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 2676732, NULL, NULL);
INSERT INTO `edoc_attachment` VALUES (18061085, '000.00.13.H53', '1.1 HDSD xac thuc phan quyen tap trung_IAM.docx', '2020-08-14 17:35:54', '000.00.13.H53/2020/8/14/11257_1.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 2676732, NULL, NULL);
INSERT INTO `edoc_attachment` VALUES (18061086, '000.00.13.H53', '5.5_TTS_Lập trình_Nguyễn Thị Phương.docx', '2020-08-14 17:36:04', '000.00.13.H53/2020/8/14/11258_1.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 15703, '000.00.21.H53', 273);
INSERT INTO `edoc_attachment` VALUES (18061087, '000.00.13.H53', '15_LGSP 12082019.xlsx', '2020-08-14 17:36:07', '000.00.13.H53/2020/8/14/11259_1.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 218629, '000.00.21.H53', 273);
INSERT INTO `edoc_attachment` VALUES (18061088, '000.00.13.H53', 'cử Thành viên tham gia Hội đồng cấp tỉnh xét tặng danh hiệu “Nghệ nhân nhân dân”, “Nghệ nhân ưu tú”.docx', '2020-08-14 17:53:21', '000.00.13.H53/2020/8/14/274_1', 'application/msword', 40265, '000.00.13.H53', 274);
INSERT INTO `edoc_attachment` VALUES (18061089, '000.00.13.H53', 'cử Thành viên tham gia Hội đồng cấp tỉnh xét tặng danh hiệu “Nghệ nhân nhân dân”, “Nghệ nhân ưu tú”_Signed.pdf', '2020-08-14 17:53:21', '000.00.13.H53/2020/8/14/274_2', 'application/pdf', 392185, '000.00.13.H53', 274);
INSERT INTO `edoc_attachment` VALUES (18061090, '000.00.13.H53', 'BC TT 032017.docx', '2020-08-14 18:01:17', '000.00.13.H53/2020/8/14/11260_1.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 26493, '000.00.13.H53', 275);
INSERT INTO `edoc_attachment` VALUES (18061091, '000.00.13.H53', 'CV_Hachinet_DungTD.xlsx', '2020-08-14 18:01:17', '000.00.13.H53/2020/8/14/11261_2.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 96129, '000.00.13.H53', 275);
INSERT INTO `edoc_attachment` VALUES (18061092, '000.00.13.H53', '13. Trục liên thông tích hợp.docx', '2020-08-14 18:56:52', '000.00.13.H53/2020/8/14/11262_1', 'application/docx', 705164, '000.00.21.H53', 276);
INSERT INTO `edoc_attachment` VALUES (18061093, '000.00.13.H53', '15_LGSP 12082019.xlsx', '2020-08-14 18:56:52', '000.00.13.H53/2020/8/14/11263_2', 'application/zip', 218629, '000.00.21.H53', 276);
INSERT INTO `edoc_attachment` VALUES (18061094, '000.00.13.H53', 'User_15-06-2020.xlsx', '2020-08-14 19:05:54', '000.00.13.H53/2020/8/14/11264_1', 'application/xlsx', 9216, '000.00.21.H53', 277);
INSERT INTO `edoc_attachment` VALUES (18061095, '000.00.13.H53', 'Văn bản đề nghị kết nối dữ liệu QLVB và HCC.doc', '2020-08-14 19:05:54', '000.00.13.H53/2020/8/14/11265_2', 'application/msword', 151552, '000.00.21.H53', 277);
INSERT INTO `edoc_attachment` VALUES (18061096, '000.00.13.H53', 'MetaData_Estimate_Review_xtel_Confirm.xlsx', '2020-08-14 19:13:23', '000.00.13.H53/2020/8/14/11266_1', 'application/xlsx', 165857, '000.00.13.H53', 278);
INSERT INTO `edoc_attachment` VALUES (18061097, '000.00.13.H53', 'Phần-mềm-nền-tảng-liên-thông-tích-hợp.xlsx', '2020-08-14 19:13:23', '000.00.13.H53/2020/8/14/11267_2', 'application/xlsx', 481173, '000.00.13.H53', 278);
INSERT INTO `edoc_attachment` VALUES (18061098, '000.00.13.H53', 'SSO_documentation.docx', '2020-08-14 19:13:23', '000.00.13.H53/2020/8/14/11268_3', 'application/docx', 127676, '000.00.13.H53', 278);
INSERT INTO `edoc_attachment` VALUES (18061099, '000.00.13.H53', 'TKTC-MTA-Chinh-Muc_Luc_Gửi_Chị_Hiền.docx', '2020-08-14 19:13:23', '000.00.13.H53/2020/8/14/11269_4', 'application/docx', 787131, '000.00.13.H53', 278);
INSERT INTO `edoc_attachment` VALUES (18061100, '000.00.13.H53', '13. Trục liên thông tích hợp.docx', '2020-08-15 09:06:47', '000.00.13.H53/2020/8/15/11270_1', 'application/docx', 705164, '000.00.13.H53', 279);
INSERT INTO `edoc_attachment` VALUES (18061101, '000.00.13.H53', 'API_web.docx', '2020-08-15 09:06:47', '000.00.13.H53/2020/8/15/11271_2', 'application/docx', 15825, '000.00.13.H53', 279);
INSERT INTO `edoc_attachment` VALUES (18061102, '000.00.13.H53', 'CV_Hachinet_DungTD.xlsx', '2020-08-15 09:06:47', '000.00.13.H53/2020/8/15/11272_3', 'application/xlsx', 96129, '000.00.13.H53', 279);
INSERT INTO `edoc_attachment` VALUES (18061103, '000.00.13.H53', 'Về việc góp ý dự thảo Kế hoạch truyền thông về thực hiện thủ tục hành chính trên môi trường điện tử trên địa bàn tỉnh Tây Ninh.doc', '2020-08-15 11:27:30', '000.00.13.H53/2020/8/15/280_1', 'application/msword', 34304, '000.00.23.H53', 280);
INSERT INTO `edoc_attachment` VALUES (18061104, '000.00.13.H53', 'Du thao Ke hoach tuyen truyen thuc hien TTHC tren moi truong dien tu_sua.doc', '2020-08-15 11:27:30', '000.00.13.H53/2020/8/15/280_2', 'application/msword', 92672, '000.00.23.H53', 280);
INSERT INTO `edoc_attachment` VALUES (18061105, '000.00.13.H53', 'Về việc góp ý dự thảo Kế hoạch truyền thông về thực hiện thủ tục hành chính trên môi trường điện tử trên địa bàn tỉnh Tây Ninh_Signed.pdf', '2020-08-15 11:27:30', '000.00.13.H53/2020/8/15/280_3', 'application/pdf', 583754, '000.00.23.H53', 280);
INSERT INTO `edoc_attachment` VALUES (18061106, '000.00.13.H53', 'Ket noi EMR.docx', '2020-08-15 13:17:25', '000.00.13.H53/2020/8/15/11273_1', 'application/docx', 479280, '000.00.21.H53', 281);
INSERT INTO `edoc_attachment` VALUES (18061107, '000.00.13.H53', 'MetaData_Estimate_Review_xtel_Confirm.xlsx', '2020-08-15 13:17:25', '000.00.13.H53/2020/8/15/11274_2', 'application/xlsx', 165857, '000.00.21.H53', 281);
INSERT INTO `edoc_attachment` VALUES (18061108, '000.00.13.H53', 'Phần-mềm-nền-tảng-liên-thông-tích-hợp.xlsx', '2020-08-15 13:17:25', '000.00.13.H53/2020/8/15/11275_3', 'application/xlsx', 481173, '000.00.21.H53', 281);
INSERT INTO `edoc_attachment` VALUES (18061109, '000.00.13.H53', '1.1 HDSD xac thuc phan quyen tap trung_IAM.docx', '2020-08-19 18:12:52', '000.00.13.H53/2020/8/19/11276_1', 'application/docx', 2676732, '000.00.01.H53', 282);
INSERT INTO `edoc_attachment` VALUES (18061110, '000.00.13.H53', '2_TLTVBQG_HuongDanKyThuatKetNoiLienThong_2.2_VPCP.pdf', '2020-08-19 18:12:52', '000.00.13.H53/2020/8/19/11277_2', 'application/pdf', 2305118, '000.00.01.H53', 282);
INSERT INTO `edoc_attachment` VALUES (18061111, '000.00.13.H53', '15_LGSP 12082019.xlsx', '2020-08-19 18:12:52', '000.00.13.H53/2020/8/19/11278_3', 'application/xlsx', 218629, '000.00.01.H53', 282);
INSERT INTO `edoc_attachment` VALUES (18061112, '000.00.13.H53', '9.Gioi-thieu-CQĐT-LGSP.pdf', '2020-08-19 18:29:30', '000.00.13.H53/2020/8/19/11279_1', 'application/pdf', 4703883, '000.01.01.H53', 283);
INSERT INTO `edoc_attachment` VALUES (18061113, '000.00.13.H53', '13. Trục liên thông tích hợp.docx', '2020-08-19 18:30:16', '000.00.13.H53/2020/8/19/11280_2', 'application/docx', 705164, '000.01.01.H53', 283);
INSERT INTO `edoc_attachment` VALUES (18061114, '000.00.13.H53', '15_LGSP 12082019.xlsx', '2020-08-19 18:30:32', '000.00.13.H53/2020/8/19/11281_3', 'application/xlsx', 218629, '000.01.01.H53', 283);
INSERT INTO `edoc_attachment` VALUES (18061115, '000.00.13.H53', '13. Trục liên thông tích hợp.docx', '2020-08-20 11:50:45', '000.00.13.H53/2020/8/20/11282_1', 'application/docx', 705164, '000.00.01.H53#000.02.01.H53#000.12.31.H53', 284);
INSERT INTO `edoc_attachment` VALUES (18061116, '000.00.13.H53', '15_LGSP 12082019.xlsx', '2020-08-20 11:50:45', '000.00.13.H53/2020/8/20/11283_2', 'application/xlsx', 218629, '000.00.01.H53#000.02.01.H53#000.12.31.H53', 284);
INSERT INTO `edoc_attachment` VALUES (18061117, '000.00.13.H53', '631_THH_THHT.pdf', '2020-08-20 11:50:45', '000.00.13.H53/2020/8/20/11284_3', 'application/pdf', 1338154, '000.00.01.H53#000.02.01.H53#000.12.31.H53', 284);
INSERT INTO `edoc_attachment` VALUES (18061118, '000.00.13.H53', '1163.PDF', '2020-08-22 21:42:17', '000.00.13.H53\\2020\\8\\22\\11285_1', 'application/zip', 378238, '000.28.31.H53#000.01.32.H53#000.01.33.H53', 285);
INSERT INTO `edoc_attachment` VALUES (18061119, '000.00.13.H53', 'VB 1142_Signed.pdf', '2020-08-22 21:48:00', '000.00.13.H53\\2020\\8\\22\\11286_1', 'application/pdf', 343413, '000.00.01.H53', 286);
INSERT INTO `edoc_attachment` VALUES (18061120, '000.00.13.H53', 'VB 1142.doc', '2020-08-22 21:48:00', '000.00.13.H53\\2020\\8\\22\\11287_2', 'application/msword', 44544, '000.00.01.H53', 286);
INSERT INTO `edoc_attachment` VALUES (18061121, '000.00.13.H53', 'ioc_danhsachnhansu_1597673181.xlsx', '2020-08-24 23:26:37', '000.00.13.H53\\2020\\8\\24\\11288_1', 'application/xlsx', 1789949, '000.10.32.H53', 287);
INSERT INTO `edoc_attachment` VALUES (18061122, '000.00.13.H53', 'CVDi 916 STTTT- CNTT[signed].pdf', '2020-08-24 23:26:39', '000.00.13.H53\\2020\\8\\24\\11289_2', 'application/pdf', 1105550, '000.10.32.H53', 287);
INSERT INTO `edoc_attachment` VALUES (18061123, '000.00.13.H53', 'DanhMucVaMaDinhDanhKetNoiCacHeThongQlvbdhCap4 (1).xlsx', '2020-08-31 00:28:18', '000.00.13.H53\\2020\\8\\31\\11290_1', 'application/xlsx', 1583566, '000.00.01.H53#000.20.31.H53', 288);
INSERT INTO `edoc_attachment` VALUES (18061124, '000.00.13.H53', 'DanhMucVaMaDinhDanhKetNoiCacHeThongQlvbdhCap4.xlsx', '2020-08-31 00:28:20', '000.00.13.H53\\2020\\8\\31\\11291_2', 'application/xlsx', 20394, '000.00.01.H53#000.20.31.H53', 288);
INSERT INTO `edoc_attachment` VALUES (18061125, '000.00.13.H53', 'DanhMucVaMaDinhDanhKetNoiCacHeThongQlvbdhCap4 (1).xlsx', '2020-08-31 00:31:16', '000.00.13.H53\\2020\\8\\31\\11292_1', 'application/xlsx', 1583566, '000.00.01.H53', 289);
INSERT INTO `edoc_attachment` VALUES (18061126, '000.00.13.H53', 'DanhMucVaMaDinhDanhKetNoiCacHeThongQlvbdhCap4.xlsx', '2020-08-31 00:31:16', '000.00.13.H53\\2020\\8\\31\\11293_2', 'application/xlsx', 20394, '000.00.01.H53', 289);
INSERT INTO `edoc_attachment` VALUES (18061127, '000.00.13.H53', 'ioc_danhsachchucvu.xlsx', '2020-09-02 17:21:41', '000.00.13.H53\\2020\\9\\2\\11294_1', 'application/xlsx', 8429, '000.02.01.H53', 290);
INSERT INTO `edoc_attachment` VALUES (18061128, '000.00.13.H53', 'ioc_danhsachnhansu.xlsx', '2020-09-02 17:21:41', '000.00.13.H53\\2020\\9\\2\\11295_2', 'application/xlsx', 596032, '000.02.01.H53', 290);
INSERT INTO `edoc_attachment` VALUES (18061129, '000.00.12.H53', '9.Gioi-thieu-CQ_T-LGSP.pdf', '2020-09-04 14:41:27', '000.00.12.H53/2020/9/4/11296_1', 'application/pdf', 4703883, '000.00.13.H53', 291);
INSERT INTO `edoc_attachment` VALUES (18061130, '000.00.12.H53', '[DMDC] Danh mục dùng chung.docx', '2020-09-04 14:41:27', '000.00.12.H53/2020/9/4/11297_2', 'application/docx', 3373683, '000.00.13.H53', 291);
INSERT INTO `edoc_attachment` VALUES (18061131, '000.00.13.H53', 'QungCV.xlsx', '2020-09-04 14:47:41', '000.00.13.H53/2020/9/4/11298_1', 'application/xlsx', 70374, '000.00.12.H53', 292);
INSERT INTO `edoc_attachment` VALUES (18061132, '000.00.13.H53', '[SYT QN] Sở Y Tế QN.xlsx', '2020-09-04 14:47:41', '000.00.13.H53/2020/9/4/11299_2', 'application/xlsx', 127185, '000.00.12.H53', 292);
INSERT INTO `edoc_attachment` VALUES (18061133, '000.00.13.H53', '[SGDQN] Sở GD QN.xlsx', '2020-09-04 14:47:41', '000.00.13.H53/2020/9/4/11300_3', 'application/xlsx', 69852, '000.00.12.H53', 292);
INSERT INTO `edoc_attachment` VALUES (18061134, '000.00.13.H53', 'Điều chỉnh web.docx', '2020-09-04 14:47:41', '000.00.13.H53/2020/9/4/11301_4', 'application/docx', 1860999, '000.00.12.H53', 292);
INSERT INTO `edoc_attachment` VALUES (18061135, '000.00.13.H53', '1.BoTTTT-GioiThieu NGSP.pdf', '2020-09-04 14:52:27', '000.00.13.H53/2020/9/4/11302_1', 'application/pdf', 2371797, '000.00.13.H53', 293);
INSERT INTO `edoc_attachment` VALUES (18061136, '000.00.13.H53', '587_Phuluc.pdf', '2020-09-04 14:52:27', '000.00.13.H53/2020/9/4/11303_2', 'application/pdf', 294318, '000.00.13.H53', 293);
INSERT INTO `edoc_attachment` VALUES (18061137, '000.00.13.H53', 'Authorization_Tài_liệu_phát_triển_CommonFlows_v1.1_Final.pdf', '2020-09-05 12:40:12', '000.00.13.H53/2020/9/5/11304_1', 'application/pdf', 790503, '000.00.13.H53', 294);
INSERT INTO `edoc_attachment` VALUES (18061138, '000.00.13.H53', 'Authorization_Tài_liệu_phát_triển_CommonFlows_v1.1_Final.docx', '2020-09-05 12:40:12', '000.00.13.H53/2020/9/5/11305_2', 'application/docx', 644470, '000.00.13.H53', 294);
INSERT INTO `edoc_attachment` VALUES (18061139, '000.00.12.H53', 'Authorization_Tài_liệu_phát_triển_CommonFlows_v1.1_Final.pdf', '2020-09-05 12:42:00', '000.00.12.H53/2020/9/5/11306_1', 'application/pdf', 790503, '000.00.13.H53', 295);
INSERT INTO `edoc_attachment` VALUES (18061140, '000.00.12.H53', 'Authorization_Tài_liệu_phát_triển_CommonFlows_v1.1_Final.docx', '2020-09-05 12:42:00', '000.00.12.H53/2020/9/5/11307_2', 'application/docx', 644470, '000.00.13.H53', 295);

-- ----------------------------
-- Table structure for edoc_dailycounter
-- ----------------------------
DROP TABLE IF EXISTS `edoc_dailycounter`;
CREATE TABLE `edoc_dailycounter`  (
  `dailyCounterId` bigint(20) NOT NULL AUTO_INCREMENT,
  `organDomain` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dateTime` datetime(0) NULL DEFAULT NULL,
  `sent` bigint(20) NULL DEFAULT NULL,
  `received` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`dailyCounterId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 712 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_dailycounter
-- ----------------------------
INSERT INTO `edoc_dailycounter` VALUES (7, '000.20.31.H53', '2020-08-31 22:28:02', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (8, '000.00.13.H53', '2020-08-31 22:28:02', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (9, '000.00.01.H53', '2020-08-31 22:28:02', 2, 1);
INSERT INTO `edoc_dailycounter` VALUES (10, '000.20.31.H53', '2020-08-31 22:32:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (11, '000.00.13.H53', '2020-08-31 22:32:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (12, '000.00.01.H53', '2020-08-31 22:32:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (13, '000.20.31.H53', '2020-08-31 22:34:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (14, '000.00.13.H53', '2020-08-31 22:34:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (15, '000.00.01.H53', '2020-08-31 22:34:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (16, '000.20.31.H53', '2020-08-31 22:38:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (17, '000.00.13.H53', '2020-08-31 22:38:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (18, '000.00.01.H53', '2020-08-31 22:38:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (19, '000.20.31.H53', '2020-08-31 22:40:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (20, '000.00.13.H53', '2020-08-31 22:40:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (21, '000.00.01.H53', '2020-08-31 22:40:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (22, '000.20.31.H53', '2020-08-31 22:42:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (23, '000.00.13.H53', '2020-08-31 22:42:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (24, '000.00.01.H53', '2020-08-31 22:42:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (25, '000.20.31.H53', '2020-08-31 22:44:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (26, '000.00.13.H53', '2020-08-31 22:44:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (27, '000.00.01.H53', '2020-08-31 22:44:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (28, '000.20.31.H53', '2020-08-31 22:46:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (29, '000.00.13.H53', '2020-08-31 22:46:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (30, '000.00.01.H53', '2020-08-31 22:46:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (31, '000.20.31.H53', '2020-08-31 22:48:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (32, '000.00.13.H53', '2020-08-31 22:48:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (33, '000.00.01.H53', '2020-08-31 22:48:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (34, '000.20.31.H53', '2020-08-31 22:50:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (35, '000.00.13.H53', '2020-08-31 22:50:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (36, '000.00.01.H53', '2020-08-31 22:50:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (37, '000.20.31.H53', '2020-08-31 22:52:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (38, '000.00.13.H53', '2020-08-31 22:52:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (39, '000.00.01.H53', '2020-08-31 22:52:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (40, '000.20.31.H53', '2020-08-31 22:54:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (41, '000.00.13.H53', '2020-08-31 22:54:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (42, '000.00.01.H53', '2020-08-31 22:54:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (43, '000.20.31.H53', '2020-08-31 22:56:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (44, '000.00.13.H53', '2020-08-31 22:56:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (45, '000.00.01.H53', '2020-08-31 22:56:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (46, '000.20.31.H53', '2020-08-31 22:58:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (47, '000.00.13.H53', '2020-08-31 22:58:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (48, '000.00.01.H53', '2020-08-31 22:58:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (49, '000.20.31.H53', '2020-08-31 23:00:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (50, '000.00.13.H53', '2020-08-31 23:00:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (51, '000.00.01.H53', '2020-08-31 23:00:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (52, '000.20.31.H53', '2020-08-31 23:02:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (53, '000.00.13.H53', '2020-08-31 23:02:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (54, '000.00.01.H53', '2020-08-31 23:02:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (55, '000.20.31.H53', '2020-08-31 23:04:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (56, '000.00.13.H53', '2020-08-31 23:04:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (57, '000.00.01.H53', '2020-08-31 23:04:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (58, '000.20.31.H53', '2020-08-31 23:06:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (59, '000.00.13.H53', '2020-08-31 23:06:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (60, '000.00.01.H53', '2020-08-31 23:06:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (61, '000.20.31.H53', '2020-08-31 23:08:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (62, '000.00.13.H53', '2020-08-31 23:08:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (63, '000.00.01.H53', '2020-08-31 23:08:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (64, '000.20.31.H53', '2020-08-31 23:10:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (65, '000.00.13.H53', '2020-08-31 23:10:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (66, '000.00.01.H53', '2020-08-31 23:10:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (67, '000.20.31.H53', '2020-08-31 23:12:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (68, '000.00.13.H53', '2020-08-31 23:12:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (69, '000.00.01.H53', '2020-08-31 23:12:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (70, '000.20.31.H53', '2020-08-31 23:14:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (71, '000.00.13.H53', '2020-08-31 23:14:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (72, '000.00.01.H53', '2020-08-31 23:14:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (73, '000.20.31.H53', '2020-08-31 23:18:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (74, '000.00.13.H53', '2020-08-31 23:18:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (75, '000.00.01.H53', '2020-08-31 23:18:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (76, '000.20.31.H53', '2020-08-31 23:20:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (77, '000.00.13.H53', '2020-08-31 23:20:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (78, '000.00.01.H53', '2020-08-31 23:20:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (79, '000.20.31.H53', '2020-08-31 23:22:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (80, '000.00.13.H53', '2020-08-31 23:22:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (81, '000.00.01.H53', '2020-08-31 23:22:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (82, '000.20.31.H53', '2020-08-31 23:24:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (83, '000.00.13.H53', '2020-08-31 23:24:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (84, '000.00.01.H53', '2020-08-31 23:24:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (85, '000.20.31.H53', '2020-08-31 23:26:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (86, '000.00.13.H53', '2020-08-31 23:26:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (87, '000.00.01.H53', '2020-08-31 23:26:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (88, '000.20.31.H53', '2020-08-31 23:28:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (89, '000.00.13.H53', '2020-08-31 23:28:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (90, '000.00.01.H53', '2020-08-31 23:28:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (91, '000.20.31.H53', '2020-08-31 23:30:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (92, '000.00.13.H53', '2020-08-31 23:30:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (93, '000.00.01.H53', '2020-08-31 23:30:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (94, '000.20.31.H53', '2020-08-31 23:32:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (95, '000.00.13.H53', '2020-08-31 23:32:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (96, '000.00.01.H53', '2020-08-31 23:32:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (97, '000.20.31.H53', '2020-08-31 23:34:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (98, '000.00.13.H53', '2020-08-31 23:34:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (99, '000.00.01.H53', '2020-08-31 23:34:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (100, '000.20.31.H53', '2020-08-31 23:36:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (101, '000.00.13.H53', '2020-08-31 23:36:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (102, '000.00.01.H53', '2020-08-31 23:36:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (103, '000.20.31.H53', '2020-08-31 23:40:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (104, '000.00.13.H53', '2020-08-31 23:40:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (105, '000.00.01.H53', '2020-08-31 23:40:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (106, '000.20.31.H53', '2020-08-31 23:42:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (107, '000.00.13.H53', '2020-08-31 23:42:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (108, '000.00.01.H53', '2020-08-31 23:42:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (109, '000.20.31.H53', '2020-08-31 23:44:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (110, '000.00.13.H53', '2020-08-31 23:44:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (111, '000.00.01.H53', '2020-08-31 23:44:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (112, '000.20.31.H53', '2020-08-31 23:46:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (113, '000.00.13.H53', '2020-08-31 23:46:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (114, '000.00.01.H53', '2020-08-31 23:46:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (115, '000.20.31.H53', '2020-08-31 23:48:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (116, '000.00.13.H53', '2020-08-31 23:48:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (117, '000.00.01.H53', '2020-08-31 23:48:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (118, '000.20.31.H53', '2020-08-31 23:50:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (119, '000.00.13.H53', '2020-08-31 23:50:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (120, '000.00.01.H53', '2020-08-31 23:50:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (121, '000.20.31.H53', '2020-08-31 23:52:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (122, '000.00.13.H53', '2020-08-31 23:52:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (123, '000.00.01.H53', '2020-08-31 23:52:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (124, '000.20.31.H53', '2020-08-31 23:54:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (125, '000.00.13.H53', '2020-08-31 23:54:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (126, '000.00.01.H53', '2020-08-31 23:54:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (127, '000.20.31.H53', '2020-08-31 23:56:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (128, '000.00.13.H53', '2020-08-31 23:56:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (129, '000.00.01.H53', '2020-08-31 23:56:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (130, '000.20.31.H53', '2020-08-31 23:58:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (131, '000.00.13.H53', '2020-08-31 23:58:00', 2, 0);
INSERT INTO `edoc_dailycounter` VALUES (132, '000.00.01.H53', '2020-08-31 23:58:00', 0, 2);
INSERT INTO `edoc_dailycounter` VALUES (133, '000.02.01.H53', '2020-09-02 17:22:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (134, '000.00.13.H53', '2020-09-02 17:22:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (135, '000.02.01.H53', '2020-09-02 17:24:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (136, '000.00.13.H53', '2020-09-02 17:24:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (137, '000.02.01.H53', '2020-09-02 17:26:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (138, '000.00.13.H53', '2020-09-02 17:26:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (139, '000.02.01.H53', '2020-09-02 17:28:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (140, '000.00.13.H53', '2020-09-02 17:28:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (141, '000.02.01.H53', '2020-09-02 17:30:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (142, '000.00.13.H53', '2020-09-02 17:30:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (143, '000.02.01.H53', '2020-09-02 17:32:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (144, '000.00.13.H53', '2020-09-02 17:32:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (145, '000.02.01.H53', '2020-09-02 17:34:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (146, '000.00.13.H53', '2020-09-02 17:34:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (147, '000.02.01.H53', '2020-09-02 17:36:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (148, '000.00.13.H53', '2020-09-02 17:36:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (149, '000.02.01.H53', '2020-09-02 17:38:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (150, '000.00.13.H53', '2020-09-02 17:38:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (151, '000.02.01.H53', '2020-09-02 17:40:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (152, '000.00.13.H53', '2020-09-02 17:40:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (153, '000.02.01.H53', '2020-09-02 17:42:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (154, '000.00.13.H53', '2020-09-02 17:42:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (155, '000.02.01.H53', '2020-09-02 17:44:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (156, '000.00.13.H53', '2020-09-02 17:44:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (157, '000.02.01.H53', '2020-09-02 17:46:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (158, '000.00.13.H53', '2020-09-02 17:46:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (159, '000.02.01.H53', '2020-09-02 17:48:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (160, '000.00.13.H53', '2020-09-02 17:48:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (161, '000.02.01.H53', '2020-09-02 17:50:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (162, '000.00.13.H53', '2020-09-02 17:50:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (163, '000.02.01.H53', '2020-09-02 17:52:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (164, '000.00.13.H53', '2020-09-02 17:52:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (165, '000.02.01.H53', '2020-09-02 17:54:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (166, '000.00.13.H53', '2020-09-02 17:54:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (167, '000.02.01.H53', '2020-09-02 17:56:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (168, '000.00.13.H53', '2020-09-02 17:56:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (169, '000.02.01.H53', '2020-09-02 17:58:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (170, '000.00.13.H53', '2020-09-02 17:58:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (171, '000.02.01.H53', '2020-09-02 18:00:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (172, '000.00.13.H53', '2020-09-02 18:00:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (173, '000.02.01.H53', '2020-09-02 18:02:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (174, '000.00.13.H53', '2020-09-02 18:02:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (175, '000.02.01.H53', '2020-09-02 18:04:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (176, '000.00.13.H53', '2020-09-02 18:04:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (177, '000.02.01.H53', '2020-09-02 18:06:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (178, '000.00.13.H53', '2020-09-02 18:06:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (179, '000.02.01.H53', '2020-09-02 18:08:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (180, '000.00.13.H53', '2020-09-02 18:08:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (181, '000.02.01.H53', '2020-09-02 18:10:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (182, '000.00.13.H53', '2020-09-02 18:10:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (183, '000.02.01.H53', '2020-09-02 18:12:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (184, '000.00.13.H53', '2020-09-02 18:12:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (185, '000.02.01.H53', '2020-09-02 18:14:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (186, '000.00.13.H53', '2020-09-02 18:14:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (187, '000.02.01.H53', '2020-09-02 18:16:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (188, '000.00.13.H53', '2020-09-02 18:16:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (189, '000.02.01.H53', '2020-09-02 18:18:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (190, '000.00.13.H53', '2020-09-02 18:18:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (191, '000.02.01.H53', '2020-09-02 18:20:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (192, '000.00.13.H53', '2020-09-02 18:20:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (193, '000.02.01.H53', '2020-09-02 18:22:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (194, '000.00.13.H53', '2020-09-02 18:22:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (195, '000.02.01.H53', '2020-09-02 18:24:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (196, '000.00.13.H53', '2020-09-02 18:24:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (197, '000.02.01.H53', '2020-09-02 18:26:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (198, '000.00.13.H53', '2020-09-02 18:26:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (199, '000.02.01.H53', '2020-09-02 18:28:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (200, '000.00.13.H53', '2020-09-02 18:28:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (201, '000.02.01.H53', '2020-09-02 18:30:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (202, '000.00.13.H53', '2020-09-02 18:30:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (203, '000.02.01.H53', '2020-09-02 18:32:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (204, '000.00.13.H53', '2020-09-02 18:32:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (205, '000.02.01.H53', '2020-09-02 18:34:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (206, '000.00.13.H53', '2020-09-02 18:34:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (207, '000.02.01.H53', '2020-09-02 18:36:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (208, '000.00.13.H53', '2020-09-02 18:36:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (209, '000.02.01.H53', '2020-09-02 18:38:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (210, '000.00.13.H53', '2020-09-02 18:38:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (211, '000.02.01.H53', '2020-09-02 18:40:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (212, '000.00.13.H53', '2020-09-02 18:40:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (213, '000.02.01.H53', '2020-09-02 18:42:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (214, '000.00.13.H53', '2020-09-02 18:42:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (215, '000.02.01.H53', '2020-09-02 18:44:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (216, '000.00.13.H53', '2020-09-02 18:44:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (217, '000.02.01.H53', '2020-09-02 18:46:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (218, '000.00.13.H53', '2020-09-02 18:46:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (219, '000.02.01.H53', '2020-09-02 18:48:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (220, '000.00.13.H53', '2020-09-02 18:48:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (221, '000.02.01.H53', '2020-09-02 18:50:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (222, '000.00.13.H53', '2020-09-02 18:50:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (223, '000.02.01.H53', '2020-09-02 18:52:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (224, '000.00.13.H53', '2020-09-02 18:52:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (225, '000.02.01.H53', '2020-09-02 18:54:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (226, '000.00.13.H53', '2020-09-02 18:54:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (227, '000.02.01.H53', '2020-09-02 18:56:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (228, '000.00.13.H53', '2020-09-02 18:56:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (229, '000.02.01.H53', '2020-09-02 18:58:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (230, '000.00.13.H53', '2020-09-02 18:58:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (231, '000.02.01.H53', '2020-09-02 19:48:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (232, '000.00.13.H53', '2020-09-02 19:48:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (233, '000.02.01.H53', '2020-09-02 19:50:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (234, '000.00.13.H53', '2020-09-02 19:50:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (235, '000.02.01.H53', '2020-09-02 19:52:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (236, '000.00.13.H53', '2020-09-02 19:52:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (237, '000.02.01.H53', '2020-09-02 19:54:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (238, '000.00.13.H53', '2020-09-02 19:54:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (239, '000.02.01.H53', '2020-09-02 19:56:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (240, '000.00.13.H53', '2020-09-02 19:56:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (241, '000.02.01.H53', '2020-09-02 19:58:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (242, '000.00.13.H53', '2020-09-02 19:58:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (243, '000.02.01.H53', '2020-09-02 20:00:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (244, '000.00.13.H53', '2020-09-02 20:00:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (245, '000.02.01.H53', '2020-09-02 20:02:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (246, '000.00.13.H53', '2020-09-02 20:02:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (247, '000.02.01.H53', '2020-09-02 20:04:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (248, '000.00.13.H53', '2020-09-02 20:04:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (249, '000.02.01.H53', '2020-09-02 20:06:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (250, '000.00.13.H53', '2020-09-02 20:06:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (251, '000.02.01.H53', '2020-09-02 20:08:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (252, '000.00.13.H53', '2020-09-02 20:08:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (253, '000.02.01.H53', '2020-09-02 20:10:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (254, '000.00.13.H53', '2020-09-02 20:10:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (255, '000.02.01.H53', '2020-09-02 20:12:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (256, '000.00.13.H53', '2020-09-02 20:12:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (257, '000.02.01.H53', '2020-09-02 20:14:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (258, '000.00.13.H53', '2020-09-02 20:14:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (259, '000.02.01.H53', '2020-09-02 20:16:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (260, '000.00.13.H53', '2020-09-02 20:16:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (261, '000.02.01.H53', '2020-09-02 20:18:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (262, '000.00.13.H53', '2020-09-02 20:18:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (263, '000.02.01.H53', '2020-09-02 20:20:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (264, '000.00.13.H53', '2020-09-02 20:20:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (265, '000.02.01.H53', '2020-09-02 20:22:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (266, '000.00.13.H53', '2020-09-02 20:22:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (267, '000.02.01.H53', '2020-09-02 20:24:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (268, '000.00.13.H53', '2020-09-02 20:24:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (269, '000.02.01.H53', '2020-09-02 20:26:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (270, '000.00.13.H53', '2020-09-02 20:26:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (271, '000.02.01.H53', '2020-09-02 20:28:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (272, '000.00.13.H53', '2020-09-02 20:28:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (273, '000.02.01.H53', '2020-09-02 20:30:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (274, '000.00.13.H53', '2020-09-02 20:30:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (275, '000.02.01.H53', '2020-09-02 20:32:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (276, '000.00.13.H53', '2020-09-02 20:32:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (277, '000.02.01.H53', '2020-09-02 20:34:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (278, '000.00.13.H53', '2020-09-02 20:34:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (279, '000.02.01.H53', '2020-09-02 20:36:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (280, '000.00.13.H53', '2020-09-02 20:36:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (281, '000.02.01.H53', '2020-09-02 20:38:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (282, '000.00.13.H53', '2020-09-02 20:38:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (283, '000.02.01.H53', '2020-09-02 20:40:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (284, '000.00.13.H53', '2020-09-02 20:40:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (285, '000.02.01.H53', '2020-09-02 20:42:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (286, '000.00.13.H53', '2020-09-02 20:42:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (287, '000.02.01.H53', '2020-09-02 20:44:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (288, '000.00.13.H53', '2020-09-02 20:44:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (289, '000.02.01.H53', '2020-09-02 20:46:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (290, '000.00.13.H53', '2020-09-02 20:46:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (291, '000.02.01.H53', '2020-09-02 20:48:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (292, '000.00.13.H53', '2020-09-02 20:48:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (293, '000.02.01.H53', '2020-09-02 20:50:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (294, '000.00.13.H53', '2020-09-02 20:50:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (295, '000.02.01.H53', '2020-09-02 20:52:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (296, '000.00.13.H53', '2020-09-02 20:52:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (297, '000.02.01.H53', '2020-09-02 20:54:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (298, '000.00.13.H53', '2020-09-02 20:54:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (299, '000.02.01.H53', '2020-09-02 20:56:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (300, '000.00.13.H53', '2020-09-02 20:56:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (301, '000.02.01.H53', '2020-09-02 20:58:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (302, '000.00.13.H53', '2020-09-02 20:58:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (303, '000.02.01.H53', '2020-09-02 21:00:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (304, '000.00.13.H53', '2020-09-02 21:00:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (305, '000.02.01.H53', '2020-09-02 21:02:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (306, '000.00.13.H53', '2020-09-02 21:02:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (307, '000.02.01.H53', '2020-09-02 21:04:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (308, '000.00.13.H53', '2020-09-02 21:04:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (309, '000.02.01.H53', '2020-09-02 21:06:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (310, '000.00.13.H53', '2020-09-02 21:06:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (311, '000.02.01.H53', '2020-09-02 21:08:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (312, '000.00.13.H53', '2020-09-02 21:08:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (313, '000.02.01.H53', '2020-09-02 21:10:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (314, '000.00.13.H53', '2020-09-02 21:10:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (315, '000.02.01.H53', '2020-09-02 21:12:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (316, '000.00.13.H53', '2020-09-02 21:12:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (317, '000.02.01.H53', '2020-09-02 21:16:05', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (318, '000.00.13.H53', '2020-09-02 21:16:05', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (319, '000.00.13.H53', '2020-08-02 21:16:05', 100, 100);
INSERT INTO `edoc_dailycounter` VALUES (320, '000.02.01.H53', '2020-08-02 21:16:05', 110, 110);
INSERT INTO `edoc_dailycounter` VALUES (321, '000.02.01.H53', '2020-09-02 21:20:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (322, '000.00.13.H53', '2020-09-02 21:20:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (323, '000.20.31.H53', '2020-08-02 21:16:05', 200, 200);
INSERT INTO `edoc_dailycounter` VALUES (324, '000.21.31.H53', '2020-08-31 21:16:05', 300, 300);
INSERT INTO `edoc_dailycounter` VALUES (325, '000.02.01.H53', '2020-09-02 21:22:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (326, '000.00.13.H53', '2020-09-02 21:22:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (327, '000.02.01.H53', '2020-09-02 21:24:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (328, '000.00.13.H53', '2020-09-02 21:24:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (329, '000.02.01.H53', '2020-09-02 21:26:39', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (330, '000.00.13.H53', '2020-09-02 21:26:39', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (331, '000.12.31.H53', '2020-07-01 21:27:06', 200, 300);
INSERT INTO `edoc_dailycounter` VALUES (332, '000.20.31.H53', '2020-07-03 21:27:06', 113, 112);
INSERT INTO `edoc_dailycounter` VALUES (333, '000.02.01.H53', '2020-09-02 21:30:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (334, '000.00.13.H53', '2020-09-02 21:30:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (335, '000.02.01.H53', '2020-09-02 21:32:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (336, '000.00.13.H53', '2020-09-02 21:32:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (337, '000.02.01.H53', '2020-09-02 21:34:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (338, '000.00.13.H53', '2020-09-02 21:34:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (339, '000.02.01.H53', '2020-09-02 21:36:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (340, '000.00.13.H53', '2020-09-02 21:36:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (341, '000.02.01.H53', '2020-09-02 21:38:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (342, '000.00.13.H53', '2020-09-02 21:38:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (343, '000.02.01.H53', '2020-09-02 21:40:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (344, '000.00.13.H53', '2020-09-02 21:40:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (345, '000.02.01.H53', '2020-09-02 21:42:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (346, '000.00.13.H53', '2020-09-02 21:42:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (347, '000.02.01.H53', '2020-09-02 21:44:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (348, '000.00.13.H53', '2020-09-02 21:44:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (349, '000.02.01.H53', '2020-09-02 21:48:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (350, '000.00.13.H53', '2020-09-02 21:48:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (351, '000.02.01.H53', '2020-09-02 21:50:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (352, '000.00.13.H53', '2020-09-02 21:50:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (353, '000.02.01.H53', '2020-09-02 21:52:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (354, '000.00.13.H53', '2020-09-02 21:52:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (355, '000.02.01.H53', '2020-09-02 21:54:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (356, '000.00.13.H53', '2020-09-02 21:54:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (357, '000.02.01.H53', '2020-09-02 21:56:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (358, '000.00.13.H53', '2020-09-02 21:56:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (359, '000.02.01.H53', '2020-09-02 21:58:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (360, '000.00.13.H53', '2020-09-02 21:58:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (361, '000.02.01.H53', '2020-09-02 22:00:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (362, '000.00.13.H53', '2020-09-02 22:00:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (363, '000.02.01.H53', '2020-09-02 22:02:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (364, '000.00.13.H53', '2020-09-02 22:02:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (365, '000.02.01.H53', '2020-09-02 22:04:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (366, '000.00.13.H53', '2020-09-02 22:04:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (367, '000.02.01.H53', '2020-09-02 22:06:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (368, '000.00.13.H53', '2020-09-02 22:06:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (369, '000.02.01.H53', '2020-09-02 22:08:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (370, '000.00.13.H53', '2020-09-02 22:08:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (371, '000.02.01.H53', '2020-09-02 22:10:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (372, '000.00.13.H53', '2020-09-02 22:10:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (373, '000.02.01.H53', '2020-09-02 22:12:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (374, '000.00.13.H53', '2020-09-02 22:12:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (375, '000.02.01.H53', '2020-09-02 22:14:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (376, '000.00.13.H53', '2020-09-02 22:14:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (377, '000.02.01.H53', '2020-09-02 22:16:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (378, '000.00.13.H53', '2020-09-02 22:16:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (379, '000.02.01.H53', '2020-09-02 22:18:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (380, '000.00.13.H53', '2020-09-02 22:18:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (381, '000.02.01.H53', '2020-09-02 22:20:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (382, '000.00.13.H53', '2020-09-02 22:20:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (383, '000.02.01.H53', '2020-09-02 22:22:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (384, '000.00.13.H53', '2020-09-02 22:22:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (385, '000.02.01.H53', '2020-09-02 22:24:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (386, '000.00.13.H53', '2020-09-02 22:24:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (387, '000.02.01.H53', '2020-09-02 22:26:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (388, '000.00.13.H53', '2020-09-02 22:26:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (389, '000.02.01.H53', '2020-09-02 22:28:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (390, '000.00.13.H53', '2020-09-02 22:28:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (391, '000.02.01.H53', '2020-09-02 22:30:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (392, '000.00.13.H53', '2020-09-02 22:30:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (393, '000.02.01.H53', '2020-09-02 22:32:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (394, '000.00.13.H53', '2020-09-02 22:32:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (395, '000.02.01.H53', '2020-09-02 22:34:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (396, '000.00.13.H53', '2020-09-02 22:34:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (397, '000.02.01.H53', '2020-09-02 22:36:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (398, '000.00.13.H53', '2020-09-02 22:36:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (399, '000.02.01.H53', '2020-09-02 22:38:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (400, '000.00.13.H53', '2020-09-02 22:38:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (401, '000.02.01.H53', '2020-09-02 22:40:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (402, '000.00.13.H53', '2020-09-02 22:40:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (403, '000.02.01.H53', '2020-09-02 22:42:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (404, '000.00.13.H53', '2020-09-02 22:42:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (405, '000.02.01.H53', '2020-09-02 22:44:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (406, '000.00.13.H53', '2020-09-02 22:44:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (407, '000.02.01.H53', '2020-09-02 22:46:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (408, '000.00.13.H53', '2020-09-02 22:46:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (409, '000.02.01.H53', '2020-09-02 22:48:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (410, '000.00.13.H53', '2020-09-02 22:48:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (411, '000.02.01.H53', '2020-09-02 22:50:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (412, '000.00.13.H53', '2020-09-02 22:50:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (413, '000.02.01.H53', '2020-09-02 22:52:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (414, '000.00.13.H53', '2020-09-02 22:52:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (415, '000.02.01.H53', '2020-09-02 22:54:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (416, '000.00.13.H53', '2020-09-02 22:54:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (417, '000.02.01.H53', '2020-09-02 22:56:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (418, '000.00.13.H53', '2020-09-02 22:56:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (419, '000.02.01.H53', '2020-09-02 22:58:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (420, '000.00.13.H53', '2020-09-02 22:58:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (421, '000.02.01.H53', '2020-09-02 23:00:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (422, '000.00.13.H53', '2020-09-02 23:00:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (423, '000.02.01.H53', '2020-09-02 23:02:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (424, '000.00.13.H53', '2020-09-02 23:02:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (425, '000.02.01.H53', '2020-09-02 23:04:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (426, '000.00.13.H53', '2020-09-02 23:04:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (427, '000.02.01.H53', '2020-09-02 23:08:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (428, '000.00.13.H53', '2020-09-02 23:08:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (429, '000.02.01.H53', '2020-09-02 23:10:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (430, '000.00.13.H53', '2020-09-02 23:10:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (431, '000.02.01.H53', '2020-09-02 23:12:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (432, '000.00.13.H53', '2020-09-02 23:12:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (433, '000.02.01.H53', '2020-09-02 23:14:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (434, '000.00.13.H53', '2020-09-02 23:14:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (435, '000.02.01.H53', '2020-09-02 23:16:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (436, '000.00.13.H53', '2020-09-02 23:16:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (437, '000.02.01.H53', '2020-09-02 23:18:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (438, '000.00.13.H53', '2020-09-02 23:18:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (439, '000.02.01.H53', '2020-09-02 23:20:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (440, '000.00.13.H53', '2020-09-02 23:20:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (441, '000.02.01.H53', '2020-09-02 23:22:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (442, '000.00.13.H53', '2020-09-02 23:22:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (443, '000.02.01.H53', '2020-09-02 23:24:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (444, '000.00.13.H53', '2020-09-02 23:24:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (445, '000.02.01.H53', '2020-09-02 23:26:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (446, '000.00.13.H53', '2020-09-02 23:26:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (447, '000.02.01.H53', '2020-09-02 23:28:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (448, '000.00.13.H53', '2020-09-02 23:28:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (449, '000.02.01.H53', '2020-09-02 23:30:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (450, '000.00.13.H53', '2020-09-02 23:30:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (451, '000.02.01.H53', '2020-09-02 23:32:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (452, '000.00.13.H53', '2020-09-02 23:32:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (453, '000.02.01.H53', '2020-09-02 23:34:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (454, '000.00.13.H53', '2020-09-02 23:34:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (455, '000.02.01.H53', '2020-09-02 23:36:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (456, '000.00.13.H53', '2020-09-02 23:36:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (457, '000.02.01.H53', '2020-09-02 23:38:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (458, '000.00.13.H53', '2020-09-02 23:38:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (459, '000.02.01.H53', '2020-09-02 23:40:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (460, '000.00.13.H53', '2020-09-02 23:40:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (461, '000.02.01.H53', '2020-09-02 23:42:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (462, '000.00.13.H53', '2020-09-02 23:42:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (463, '000.02.01.H53', '2020-09-02 23:44:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (464, '000.00.13.H53', '2020-09-02 23:44:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (465, '000.02.01.H53', '2020-09-02 23:46:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (466, '000.00.13.H53', '2020-09-02 23:46:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (467, '000.02.01.H53', '2020-09-02 23:48:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (468, '000.00.13.H53', '2020-09-02 23:48:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (469, '000.02.01.H53', '2020-09-02 23:50:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (470, '000.00.13.H53', '2020-09-02 23:50:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (471, '000.02.01.H53', '2020-09-02 23:52:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (472, '000.00.13.H53', '2020-09-02 23:52:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (473, '000.02.01.H53', '2020-09-02 23:54:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (474, '000.00.13.H53', '2020-09-02 23:54:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (475, '000.02.01.H53', '2020-09-02 23:56:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (476, '000.00.13.H53', '2020-09-02 23:56:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (477, '000.02.01.H53', '2020-09-02 23:58:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (478, '000.00.13.H53', '2020-09-02 23:58:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (479, '000.00.13.H53', '2020-09-04 14:42:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (480, '000.00.12.H53', '2020-09-04 14:42:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (481, '000.00.13.H53', '2020-09-04 14:44:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (482, '000.00.12.H53', '2020-09-04 14:44:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (483, '000.00.13.H53', '2020-09-04 14:46:00', 0, 1);
INSERT INTO `edoc_dailycounter` VALUES (484, '000.00.12.H53', '2020-09-04 14:46:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (485, '000.00.13.H53', '2020-09-04 14:48:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (486, '000.00.12.H53', '2020-09-04 14:48:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (487, '000.00.13.H53', '2020-09-04 14:50:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (488, '000.00.12.H53', '2020-09-04 14:50:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (489, '000.00.13.H53', '2020-09-04 14:52:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (490, '000.00.12.H53', '2020-09-04 14:52:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (491, '000.00.13.H53', '2020-09-04 14:54:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (492, '000.00.12.H53', '2020-09-04 14:54:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (493, '000.00.13.H53', '2020-09-04 14:56:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (494, '000.00.12.H53', '2020-09-04 14:56:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (495, '000.00.13.H53', '2020-09-04 14:58:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (496, '000.00.12.H53', '2020-09-04 14:58:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (497, '000.00.13.H53', '2020-09-04 15:00:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (498, '000.00.12.H53', '2020-09-04 15:00:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (499, '000.00.13.H53', '2020-09-04 15:02:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (500, '000.00.12.H53', '2020-09-04 15:02:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (501, '000.00.13.H53', '2020-09-04 15:04:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (502, '000.00.12.H53', '2020-09-04 15:04:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (503, '000.00.13.H53', '2020-09-04 15:06:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (504, '000.00.12.H53', '2020-09-04 15:06:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (505, '000.00.13.H53', '2020-09-04 15:08:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (506, '000.00.12.H53', '2020-09-04 15:08:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (507, '000.00.13.H53', '2020-09-04 15:10:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (508, '000.00.12.H53', '2020-09-04 15:10:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (509, '000.00.13.H53', '2020-09-04 15:12:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (510, '000.00.12.H53', '2020-09-04 15:12:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (511, '000.00.13.H53', '2020-09-04 15:14:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (512, '000.00.12.H53', '2020-09-04 15:14:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (513, '000.00.13.H53', '2020-09-04 15:16:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (514, '000.00.12.H53', '2020-09-04 15:16:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (515, '000.00.13.H53', '2020-09-04 15:18:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (516, '000.00.12.H53', '2020-09-04 15:18:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (517, '000.00.13.H53', '2020-09-04 15:20:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (518, '000.00.12.H53', '2020-09-04 15:20:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (519, '000.00.13.H53', '2020-09-04 15:22:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (520, '000.00.12.H53', '2020-09-04 15:22:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (521, '000.00.13.H53', '2020-09-04 15:24:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (522, '000.00.12.H53', '2020-09-04 15:24:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (523, '000.00.13.H53', '2020-09-04 15:26:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (524, '000.00.12.H53', '2020-09-04 15:26:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (525, '000.00.13.H53', '2020-09-04 15:28:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (526, '000.00.12.H53', '2020-09-04 15:28:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (527, '000.00.13.H53', '2020-09-04 15:30:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (528, '000.00.12.H53', '2020-09-04 15:30:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (529, '000.00.13.H53', '2020-09-04 15:32:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (530, '000.00.12.H53', '2020-09-04 15:32:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (531, '000.00.13.H53', '2020-09-04 15:34:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (532, '000.00.12.H53', '2020-09-04 15:34:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (533, '000.00.13.H53', '2020-09-04 15:36:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (534, '000.00.12.H53', '2020-09-04 15:36:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (535, '000.00.13.H53', '2020-09-04 15:38:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (536, '000.00.12.H53', '2020-09-04 15:38:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (537, '000.00.13.H53', '2020-09-04 15:40:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (538, '000.00.12.H53', '2020-09-04 15:40:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (539, '000.00.13.H53', '2020-09-04 15:42:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (540, '000.00.12.H53', '2020-09-04 15:42:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (541, '000.00.13.H53', '2020-09-04 15:44:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (542, '000.00.12.H53', '2020-09-04 15:44:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (543, '000.00.13.H53', '2020-09-04 15:46:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (544, '000.00.12.H53', '2020-09-04 15:46:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (545, '000.00.13.H53', '2020-09-04 15:48:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (546, '000.00.12.H53', '2020-09-04 15:48:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (547, '000.00.13.H53', '2020-09-04 15:50:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (548, '000.00.12.H53', '2020-09-04 15:50:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (549, '000.00.13.H53', '2020-09-04 15:52:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (550, '000.00.12.H53', '2020-09-04 15:52:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (551, '000.00.13.H53', '2020-09-04 15:54:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (552, '000.00.12.H53', '2020-09-04 15:54:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (553, '000.00.13.H53', '2020-09-04 15:56:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (554, '000.00.12.H53', '2020-09-04 15:56:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (555, '000.00.13.H53', '2020-09-04 15:58:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (556, '000.00.12.H53', '2020-09-04 15:58:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (557, '000.00.13.H53', '2020-09-04 16:00:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (558, '000.00.12.H53', '2020-09-04 16:00:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (559, '000.00.13.H53', '2020-09-04 16:02:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (560, '000.00.12.H53', '2020-09-04 16:02:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (561, '000.00.13.H53', '2020-09-04 16:04:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (562, '000.00.12.H53', '2020-09-04 16:04:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (563, '000.00.13.H53', '2020-09-04 16:06:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (564, '000.00.12.H53', '2020-09-04 16:06:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (565, '000.00.13.H53', '2020-09-04 16:08:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (566, '000.00.12.H53', '2020-09-04 16:08:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (567, '000.00.13.H53', '2020-09-04 16:10:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (568, '000.00.12.H53', '2020-09-04 16:10:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (569, '000.00.13.H53', '2020-09-04 16:12:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (570, '000.00.12.H53', '2020-09-04 16:12:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (571, '000.00.13.H53', '2020-09-04 16:14:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (572, '000.00.12.H53', '2020-09-04 16:14:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (573, '000.00.13.H53', '2020-09-04 16:16:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (574, '000.00.12.H53', '2020-09-04 16:16:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (575, '000.00.13.H53', '2020-09-04 16:18:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (576, '000.00.12.H53', '2020-09-04 16:18:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (577, '000.00.13.H53', '2020-09-04 16:20:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (578, '000.00.12.H53', '2020-09-04 16:20:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (579, '000.00.13.H53', '2020-09-04 16:22:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (580, '000.00.12.H53', '2020-09-04 16:22:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (581, '000.00.13.H53', '2020-09-04 16:24:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (582, '000.00.12.H53', '2020-09-04 16:24:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (583, '000.00.13.H53', '2020-09-04 16:26:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (584, '000.00.12.H53', '2020-09-04 16:26:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (585, '000.00.13.H53', '2020-09-04 16:28:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (586, '000.00.12.H53', '2020-09-04 16:28:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (587, '000.00.13.H53', '2020-09-04 16:30:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (588, '000.00.12.H53', '2020-09-04 16:30:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (589, '000.00.13.H53', '2020-09-04 16:32:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (590, '000.00.12.H53', '2020-09-04 16:32:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (591, '000.00.13.H53', '2020-09-04 16:34:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (592, '000.00.12.H53', '2020-09-04 16:34:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (593, '000.00.13.H53', '2020-09-04 16:36:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (594, '000.00.12.H53', '2020-09-04 16:36:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (595, '000.00.13.H53', '2020-09-04 16:38:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (596, '000.00.12.H53', '2020-09-04 16:38:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (597, '000.00.13.H53', '2020-09-04 16:40:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (598, '000.00.12.H53', '2020-09-04 16:40:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (599, '000.00.13.H53', '2020-09-04 16:42:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (600, '000.00.12.H53', '2020-09-04 16:42:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (601, '000.00.13.H53', '2020-09-04 16:44:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (602, '000.00.12.H53', '2020-09-04 16:44:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (603, '000.00.13.H53', '2020-09-04 16:46:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (604, '000.00.12.H53', '2020-09-04 16:46:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (605, '000.00.13.H53', '2020-09-04 16:48:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (606, '000.00.12.H53', '2020-09-04 16:48:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (607, '000.00.13.H53', '2020-09-04 16:50:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (608, '000.00.12.H53', '2020-09-04 16:50:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (609, '000.00.13.H53', '2020-09-04 16:52:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (610, '000.00.12.H53', '2020-09-04 16:52:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (611, '000.00.13.H53', '2020-09-04 16:54:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (612, '000.00.12.H53', '2020-09-04 16:54:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (613, '000.00.13.H53', '2020-09-04 16:56:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (614, '000.00.12.H53', '2020-09-04 16:56:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (615, '000.00.13.H53', '2020-09-04 16:58:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (616, '000.00.12.H53', '2020-09-04 16:58:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (617, '000.00.13.H53', '2020-09-04 17:00:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (618, '000.00.12.H53', '2020-09-04 17:00:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (619, '000.00.13.H53', '2020-09-04 17:02:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (620, '000.00.12.H53', '2020-09-04 17:02:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (621, '000.00.13.H53', '2020-09-04 17:04:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (622, '000.00.12.H53', '2020-09-04 17:04:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (623, '000.00.13.H53', '2020-09-04 17:06:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (624, '000.00.12.H53', '2020-09-04 17:06:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (625, '000.00.13.H53', '2020-09-04 17:08:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (626, '000.00.12.H53', '2020-09-04 17:08:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (627, '000.00.13.H53', '2020-09-04 17:10:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (628, '000.00.12.H53', '2020-09-04 17:10:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (629, '000.00.13.H53', '2020-09-04 17:12:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (630, '000.00.12.H53', '2020-09-04 17:12:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (631, '000.00.13.H53', '2020-09-04 17:14:00', 2, 2);
INSERT INTO `edoc_dailycounter` VALUES (632, '000.00.12.H53', '2020-09-04 17:14:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (633, '000.00.13.H53', '2020-09-05 12:42:00', 1, 1);
INSERT INTO `edoc_dailycounter` VALUES (634, '000.00.13.H53', '2020-09-05 12:44:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (635, '000.00.12.H53', '2020-09-05 12:44:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (636, '000.00.13.H53', '2020-09-05 12:46:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (637, '000.00.12.H53', '2020-09-05 12:46:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (638, '000.00.13.H53', '2020-09-05 12:48:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (639, '000.00.12.H53', '2020-09-05 12:48:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (640, '000.00.13.H53', '2020-09-05 12:50:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (641, '000.00.12.H53', '2020-09-05 12:50:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (642, '000.00.13.H53', '2020-09-05 12:52:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (643, '000.00.12.H53', '2020-09-05 12:52:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (644, '000.00.13.H53', '2020-09-05 12:54:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (645, '000.00.12.H53', '2020-09-05 12:54:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (646, '000.00.13.H53', '2020-09-05 12:56:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (647, '000.00.12.H53', '2020-09-05 12:56:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (648, '000.00.13.H53', '2020-09-05 12:58:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (649, '000.00.12.H53', '2020-09-05 12:58:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (650, '000.00.13.H53', '2020-09-05 13:00:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (651, '000.00.12.H53', '2020-09-05 13:00:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (652, '000.00.13.H53', '2020-09-05 13:02:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (653, '000.00.12.H53', '2020-09-05 13:02:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (654, '000.00.13.H53', '2020-09-05 13:04:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (655, '000.00.12.H53', '2020-09-05 13:04:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (656, '000.00.13.H53', '2020-09-05 13:06:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (657, '000.00.12.H53', '2020-09-05 13:06:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (658, '000.00.13.H53', '2020-09-05 13:08:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (659, '000.00.12.H53', '2020-09-05 13:08:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (660, '000.00.13.H53', '2020-09-05 13:10:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (661, '000.00.12.H53', '2020-09-05 13:10:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (662, '000.00.13.H53', '2020-09-05 13:12:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (663, '000.00.12.H53', '2020-09-05 13:12:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (664, '000.00.13.H53', '2020-09-05 13:14:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (665, '000.00.12.H53', '2020-09-05 13:14:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (666, '000.00.13.H53', '2020-09-05 13:16:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (667, '000.00.12.H53', '2020-09-05 13:16:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (668, '000.00.13.H53', '2020-09-05 13:18:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (669, '000.00.12.H53', '2020-09-05 13:18:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (670, '000.00.13.H53', '2020-09-05 13:20:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (671, '000.00.12.H53', '2020-09-05 13:20:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (672, '000.00.13.H53', '2020-09-05 13:22:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (673, '000.00.12.H53', '2020-09-05 13:22:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (674, '000.00.13.H53', '2020-09-05 13:24:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (675, '000.00.12.H53', '2020-09-05 13:24:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (676, '000.00.13.H53', '2020-09-05 13:26:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (677, '000.00.12.H53', '2020-09-05 13:26:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (678, '000.00.13.H53', '2020-09-05 13:28:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (679, '000.00.12.H53', '2020-09-05 13:28:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (680, '000.00.13.H53', '2020-09-05 13:30:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (681, '000.00.12.H53', '2020-09-05 13:30:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (682, '000.00.13.H53', '2020-09-05 13:32:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (683, '000.00.12.H53', '2020-09-05 13:32:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (684, '000.00.13.H53', '2020-09-05 13:34:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (685, '000.00.12.H53', '2020-09-05 13:34:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (686, '000.00.13.H53', '2020-09-05 13:36:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (687, '000.00.12.H53', '2020-09-05 13:36:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (688, '000.00.13.H53', '2020-09-05 13:38:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (689, '000.00.12.H53', '2020-09-05 13:38:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (690, '000.00.13.H53', '2020-09-05 13:40:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (691, '000.00.12.H53', '2020-09-05 13:40:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (692, '000.00.13.H53', '2020-09-05 13:42:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (693, '000.00.12.H53', '2020-09-05 13:42:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (694, '000.00.13.H53', '2020-09-05 13:44:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (695, '000.00.12.H53', '2020-09-05 13:44:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (696, '000.00.13.H53', '2020-09-05 13:46:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (697, '000.00.12.H53', '2020-09-05 13:46:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (698, '000.00.13.H53', '2020-09-05 13:48:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (699, '000.00.12.H53', '2020-09-05 13:48:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (700, '000.00.13.H53', '2020-09-05 13:50:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (701, '000.00.12.H53', '2020-09-05 13:50:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (702, '000.00.13.H53', '2020-09-05 13:52:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (703, '000.00.12.H53', '2020-09-05 13:52:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (704, '000.00.13.H53', '2020-09-05 13:54:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (705, '000.00.12.H53', '2020-09-05 13:54:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (706, '000.00.13.H53', '2020-09-05 13:56:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (707, '000.00.12.H53', '2020-09-05 13:56:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (708, '000.00.13.H53', '2020-09-05 13:58:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (709, '000.00.12.H53', '2020-09-05 13:58:00', 1, 0);
INSERT INTO `edoc_dailycounter` VALUES (710, '000.00.13.H53', '2020-09-05 14:00:00', 1, 2);
INSERT INTO `edoc_dailycounter` VALUES (711, '000.00.12.H53', '2020-09-05 14:00:00', 1, 0);

-- ----------------------------
-- Table structure for edoc_document
-- ----------------------------
DROP TABLE IF EXISTS `edoc_document`;
CREATE TABLE `edoc_document`  (
  `document_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `edXML_doc_id` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `modified_date` datetime(0) NULL DEFAULT NULL,
  `subject` varchar(550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code_number` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code_notation` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `promulgation_place` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `promulgation_date` datetime(0) NULL DEFAULT NULL,
  `document_type` tinyint(4) NULL DEFAULT NULL,
  `document_type_name` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `document_type_detail` tinyint(4) NULL DEFAULT NULL,
  `priority_id` bigint(20) NULL DEFAULT NULL,
  `is_draft` tinyint(4) NULL DEFAULT NULL,
  `sent_date` datetime(0) NULL DEFAULT NULL,
  `to_organ_domain` varchar(550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `from_organ_domain` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `visible` tinyint(4) NULL DEFAULT NULL,
  `send_ext` tinyint(4) NULL DEFAULT NULL,
  `visited` tinyint(4) NULL DEFAULT NULL,
  `document_ext_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`document_id`) USING BTREE,
  INDEX `fromOrganCode`(`code_number`, `code_notation`, `from_organ_domain`) USING BTREE,
  INDEX `fromOrganEdxmlId`(`edXML_doc_id`, `from_organ_domain`) USING BTREE,
  INDEX `fromOrgan`(`from_organ_domain`) USING BTREE,
  INDEX `isDraf`(`is_draft`) USING BTREE,
  INDEX `toOrganDomain`(`to_organ_domain`) USING BTREE,
  INDEX `fromOrganDraft`(`is_draft`, `from_organ_domain`) USING BTREE,
  INDEX `sentDate`(`sent_date`, `modified_date`, `create_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 296 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_document
-- ----------------------------
INSERT INTO `edoc_document` VALUES (272, '18661', '2020-08-14 00:00:00', '2020-08-14 00:00:00', 'Về việc báo cáo công tác QLNN ngành Thông tin và Truyền thông tháng 8 và phương hướng tháng 9', '1308', 'BC-STTTT_test', 'Sở Thông tin và Truyền thông', '2020-08-14 00:00:00', 1, 'Báo cáo', -1, 0, 0, '2020-08-14 00:00:00', '000.00.13.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (273, '53234', '2020-08-14 00:00:00', '2020-08-14 00:00:00', 'Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '1127', 'SGTVT-QLHT', 'Sở Thông tin và Truyền thông', '2020-08-12 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-14 00:00:00', '000.00.21.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (274, '18646', '2020-08-14 00:00:00', '2020-08-14 00:00:00', 'Về việc cử thành viên tham gia Hội đồng cấp tỉnh xét tặng danh hiệu Nghệ nhân nhân dân và nghệ nhân ưu tú lần thứ ba năm 2021', '1298', 'STTTT-VP_test', 'Sở Thông tin và Truyền thông', '2020-08-13 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-14 00:00:00', '000.00.13.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (275, '53234', '2020-08-14 00:00:00', '2020-08-14 00:00:00', 'Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '1099', 'SGTVT-QLHT', 'Sở Thông tin và Truyền thông', '2020-08-06 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-14 00:00:00', '000.00.13.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (276, '53234', '2020-08-14 00:00:00', '2020-08-14 00:00:00', 'Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '1099', 'SGTVT-QLHT', 'Sở Thông tin và Truyền thông', '2020-08-04 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-14 00:00:00', '000.00.21.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (277, '53234', '2020-08-14 00:00:00', '2020-08-14 00:00:00', 'Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '1099', 'SGTVT-QLHT', 'Sở Thông tin và Truyền thông', '2020-08-12 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-14 00:00:00', '000.00.21.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (278, '53234', '2020-08-14 00:00:00', '2020-08-14 00:00:00', 'Lấy ý kiến góp ý dự thảo quy định quản lý hoạt động xe trung chuyển hành khách tuyến cố định\n', '1099', 'QD-SGTVT', 'Sở Thông tin và Truyền thông', '2020-08-12 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-14 00:00:00', '000.00.13.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (279, '53234', '2020-08-15 00:00:00', '2020-08-15 00:00:00', 'Tổng kết đánh giá 10 năm thực hiện Chương trình phát triển vật liệu xây không nung trên địa bàn tỉnh', '1126', 'QD-SGTVT', 'Sở Thông tin và Truyền thông', '2020-08-13 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-15 00:00:00', '000.00.13.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (280, '18632', '2020-08-15 00:00:00', '2020-08-15 00:00:00', 'Về việc góp ý dự thảo Kế hoạch truyền thông về thực hiện thủ tục hành chính trên môi trường điện tử trên địa bàn tỉnh Tây Ninh ', '1295', 'STTTT-TTBCXB', 'Sở Thông tin và Truyền thông', '2020-08-12 00:00:00', 1, 'Báo cáo', -1, 0, 0, '2020-08-15 00:00:00', '000.00.23.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (281, '53234', '2020-08-15 00:00:00', '2020-08-15 00:00:00', 'Tổng kết đánh giá 10 năm thực hiện Chương trình phát triển vật liệu xây không nung trên địa bàn tỉnh\n', '1126', 'CV-SGTVT', 'Sở Thông tin và Truyền thông', '2020-08-15 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-15 00:00:00', '000.00.21.H53', '000.00.13.H53', 1, NULL, 1, NULL);
INSERT INTO `edoc_document` VALUES (282, '53234', '2020-08-19 00:00:00', '2020-08-19 00:00:00', 'Tờ trình Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '1111', 'TTr-SGTVT', 'Sở Thông tin và Truyền thông', '2020-08-19 00:00:00', 3, 'Tờ trình', -1, 0, 0, '2020-08-19 00:00:00', '000.00.01.H53', '000.00.13.H53', 1, 1, 1, NULL);
INSERT INTO `edoc_document` VALUES (283, '53234', '2020-08-19 00:00:00', '2020-08-19 00:00:00', 'Báo cáo tình hình thực hiện giải ngân kế hoạch vốn tháng 8/2020 Ql 27', '1127', 'SGTVT-QLHT', 'Sở Thông tin và Truyền thông', '2020-08-19 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-19 00:00:00', '000.01.01.H53', '000.00.13.H53', 1, 1, 1, NULL);
INSERT INTO `edoc_document` VALUES (284, '53234', '2020-08-20 00:00:00', '2020-08-20 00:00:00', 'Test gửi liên thông', '1099', 'SGTVT-QLHT', 'Sở Thông tin và Truyền thông', '2020-08-20 00:00:00', 3, 'Tờ trình', -1, 0, 0, '2020-08-20 00:00:00', '000.00.01.H53#000.02.01.H53#000.12.31.H53', '000.00.13.H53', 1, 1, 1, NULL);
INSERT INTO `edoc_document` VALUES (285, '53234', '2020-08-22 00:00:00', '2020-08-22 00:00:00', 'Giải quyết các tồn tại trong công tác bồi thường GPMB dự án Ql 27 đoạn tránh LK', '1163', 'BC_SGTVT', 'Sở Thông tin và Truyền thông', '2020-08-22 00:00:00', 2, 'Báo cáo', -1, 0, 0, '2020-08-22 00:00:00', '000.28.31.H53#000.01.32.H53#000.01.33.H53', '000.00.13.H53', 1, 1, 1, NULL);
INSERT INTO `edoc_document` VALUES (286, '53234', '2020-08-22 00:00:00', '2020-08-22 00:00:00', 'Về việc đề xuất điều chỉnh dự án xây dựng hệ thống đường giao thông Trung tâm thị trấn D’Ran, huyện Đơn Dương.', '1142', 'SGTVT-QLHT', 'Sở Thông tin và Truyền thông', '2020-08-22 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-22 00:00:00', '000.00.01.H53', '000.00.13.H53', 1, 1, 1, NULL);
INSERT INTO `edoc_document` VALUES (287, '53234', '2020-08-24 00:00:00', '2020-08-24 00:00:00', 'Test gửi văn bản rác ', '1221', 'STNMT', 'Sở Thông tin và Truyền thông', '2020-08-24 00:00:00', 1, 'Công văn', -1, 0, 1, '2020-08-24 00:00:00', '000.10.32.H53', '000.00.13.H53', 1, 1, 1, NULL);
INSERT INTO `edoc_document` VALUES (288, '53234', '2020-08-31 00:00:00', '2020-08-31 00:00:00', 'Test gửi liên thông ngày 30/08/2020', '1127', 'UBND', 'Sở Thông tin và Truyền thông', '2020-08-31 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-31 00:00:00', '000.00.01.H53#000.20.31.H53', '000.00.13.H53', 1, 0, 1, NULL);
INSERT INTO `edoc_document` VALUES (289, '53234', '2020-08-31 00:31:17', '2020-08-31 00:31:17', 'Test gửi liên thông tiếp', '1223', 'UBND', 'Sở Thông tin và Truyền thông', '2020-08-31 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-08-31 00:31:17', '000.00.01.H53', '000.00.13.H53', 1, 0, 1, NULL);
INSERT INTO `edoc_document` VALUES (290, '53234', '2020-09-02 17:21:47', '2020-09-02 17:21:47', 'Test gửi liên thông ngày 01/09/2020', '1332', 'CV-SGTVT', 'Sở Thông tin và Truyền thông', '2020-09-02 00:00:00', 1, 'Công văn', -1, 1, 0, '2020-09-02 17:21:47', '000.02.01.H53', '000.00.13.H53', 1, 0, 1, NULL);
INSERT INTO `edoc_document` VALUES (291, '53234', '2020-09-04 14:41:28', '2020-09-04 14:41:28', 'Test gửi cho sở thông tin truyền thông 04/09/2020', '1322', 'STNMT', 'Sở Tài nguyên và Môi trường', '2020-09-04 00:00:00', 2, 'Báo cáo', -1, 0, 0, '2020-09-04 14:41:28', '000.00.13.H53', '000.00.12.H53', 1, 0, 1, NULL);
INSERT INTO `edoc_document` VALUES (292, '53234', '2020-09-04 14:47:45', '2020-09-04 14:47:45', 'Test gửi liên thông văn bản cho sở tài nguyên môi trường', '1443', 'STTT-QD', 'Sở Thông tin và Truyền thông', '2020-09-05 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-09-04 14:47:45', '000.00.12.H53', '000.00.13.H53', 1, 0, 1, NULL);
INSERT INTO `edoc_document` VALUES (293, '53234', '2020-09-04 14:52:28', '2020-09-04 14:52:28', 'Test gửi liên thông tiếp cho sở thông tin truyền thông', '1543', 'STTT-CV', 'Sở Thông tin và Truyền thông', '2020-09-04 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-09-04 14:52:28', '000.00.13.H53', '000.00.13.H53', 1, 0, 1, NULL);
INSERT INTO `edoc_document` VALUES (294, '53234', '2020-09-05 12:40:13', '2020-09-05 12:40:13', 'Test gửi văn bản ', '13323', 'STTTT', 'Sở Thông tin và Truyền thông', '2020-09-05 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-09-05 12:40:13', '000.00.13.H53', '000.00.13.H53', 1, 0, 1, NULL);
INSERT INTO `edoc_document` VALUES (295, '53234', '2020-09-05 12:42:16', '2020-09-05 12:42:16', 'Test gửi sở tttt', '13333', 'STTTT', 'Sở Tài nguyên và Môi trường', '2020-09-05 00:00:00', 1, 'Công văn', -1, 0, 0, '2020-09-05 12:42:16', '000.00.13.H53', '000.00.12.H53', 1, 0, 1, NULL);

-- ----------------------------
-- Table structure for edoc_document_detail
-- ----------------------------
DROP TABLE IF EXISTS `edoc_document_detail`;
CREATE TABLE `edoc_document_detail`  (
  `document_id` bigint(20) NOT NULL,
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `signer_competence` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `signer_position` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `signer_fullname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `due_date` datetime(0) NULL DEFAULT NULL,
  `to_places` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `sphere_of_promulgation` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `typer_notation` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `promulgation_amount` int(11) NULL DEFAULT NULL,
  `page_amount` int(11) NULL DEFAULT NULL,
  `appendixes` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `response_for` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `steering_type` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`document_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_document_detail
-- ----------------------------
INSERT INTO `edoc_document_detail` VALUES (272, '', '', '', 'Trần Quốc Hùng', NULL, '', '', '', 0, 0, '', NULL, 0);
INSERT INTO `edoc_document_detail` VALUES (273, 'Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '', 'Giám đốc', 'Bùi Sơn Điền', NULL, '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (274, '', '', '', 'Trần Quốc Hùng', NULL, '', '', '', 0, 0, '', NULL, 0);
INSERT INTO `edoc_document_detail` VALUES (275, 'Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '', 'Giám đốc', 'Bùi Sơn Điền', NULL, '', '', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (276, 'Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '', 'Giám đốc', 'Bùi Sơn Điền', NULL, '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (277, 'Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '', 'Giám đốc', 'Bùi Sơn Điền', NULL, '', '', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (278, 'Lấy ý kiến góp ý dự thảo quy định quản lý hoạt động xe trung chuyển hành khách tuyến cố định', '', 'Giám đốc', 'Bùi Sơn Điền', NULL, '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (279, 'Tổng kết đánh giá 10 năm thực hiện Chương trình phát triển vật liệu xây không nung trên địa bàn tỉnh', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-08-15 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (280, '', '', '', 'Huỳnh Thanh Nam', NULL, '', '', '', 0, 0, '', NULL, 0);
INSERT INTO `edoc_document_detail` VALUES (281, 'Tổng kết đánh giá 10 năm thực hiện Chương trình phát triển vật liệu xây không nung trên địa bàn tỉnh', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-08-15 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (282, 'Tờ trình Về việc thẩm định dự toán khảo sát, lập báo cáo nghiên cứu khả thi dự án đầu tư xây dựng công trình đường giao thông xã B’Lá đi xã Lộc Quảng, huyện Bảo Lâm.', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-08-20 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (283, 'Báo cáo tình hình thực hiện giải ngân kế hoạch vốn tháng 8/2020 Ql 27', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-08-12 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (284, 'Test gửi liên thông', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-08-22 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (285, 'Giải quyết các tồn tại trong công tác bồi thường GPMB dự án Ql 27 đoạn tránh LK', '', 'Chuyên viên', 'Hoàng Anh Tuấn', NULL, '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (286, 'Về việc đề xuất điều chỉnh dự án xây dựng hệ thống đường giao thông Trung tâm thị trấn D’Ran, huyện Đơn Dương.', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-08-22 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (287, 'Test gửi văn bản rác ', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-08-24 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (288, 'Test gửi liên thông ngày 30/08/2020', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-08-25 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (289, '33333', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-08-31 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (290, 'Test gửi liên thông ngày 01/09/2020', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-09-03 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (291, 'Test gửi cho sở thông tin truyền thông 04/09/2020', '', 'Giám đốc', 'Bùi Sơn Điền', NULL, '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (292, 'Test gửi liên thông văn bản cho sở tài nguyên môi ', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-09-19 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (293, 'Test gửi liên thông tiếp cho sở thông tin truyền thông', '', 'Giám đốc', 'Bùi Sơn Điền', '2020-09-11 00:00:00', '', 'Nội bộ', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (294, '', '', 'Giám đốc', 'Quang', NULL, '', '', '', 3, 3, '', '', 1);
INSERT INTO `edoc_document_detail` VALUES (295, '', '', 'Giám đốc', 'Quang', '2020-09-19 00:00:00', '', '', '', 2, 2, '', '', 1);

-- ----------------------------
-- Table structure for edoc_document_type
-- ----------------------------
DROP TABLE IF EXISTS `edoc_document_type`;
CREATE TABLE `edoc_document_type`  (
  `edoc_document_type` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`edoc_document_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_document_type
-- ----------------------------
INSERT INTO `edoc_document_type` VALUES (1, 'Công văn');
INSERT INTO `edoc_document_type` VALUES (2, 'Báo cáo');
INSERT INTO `edoc_document_type` VALUES (3, 'Tờ trình');
INSERT INTO `edoc_document_type` VALUES (4, 'Hồ sơ');

-- ----------------------------
-- Table structure for edoc_dynamiccontact
-- ----------------------------
DROP TABLE IF EXISTS `edoc_dynamiccontact`;
CREATE TABLE `edoc_dynamiccontact`  (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `InCharge` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Domain` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Email` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Address` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Telephone` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Fax` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Website` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Type` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Version` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4000743 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_dynamiccontact
-- ----------------------------
INSERT INTO `edoc_dynamiccontact` VALUES (2752000, 'UBND Tỉnh Tây Ninh', 'Tây Ninh', '000.00.00.H53', '000.00.00.H53@bmail.com', 'Tay Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2752723, 'Văn phòng UBND tỉnh', 'Tây Ninh', '000.00.01.H53', '000.00.01.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2752737, 'Ban Tiếp Công dân', 'Tây Ninh', '000.01.01.H53', '000.01.01.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2752751, 'Trung tâm Hành chính công cấp tỉnh', 'Tây Ninh', '000.02.01.H53', '000.02.01.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753032, 'Thanh tra huyện​ Bến Cầu', 'Tây Ninh', '000.12.31.H53', '000.12.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753046, 'UBND Thị trấn Bến Cầu huyện​ Bến Cầu', 'Tây Ninh', '000.20.31.H53', '000.20.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753060, 'UBND Xã An Thạnh huyện​ Bến Cầu', 'Tây Ninh', '000.21.31.H53', '000.21.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753074, 'UBND Xã Long Chữ huyện​ Bến Cầu', 'Tây Ninh', '000.22.31.H53', '000.22.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753088, 'UBND Xã Long Giang huyện​ Bến Cầu', 'Tây Ninh', '000.23.31.H53', '000.23.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753102, 'UBND Xã Long Khánh huyện​ Bến Cầu', 'Tây Ninh', '000.24.31.H53', '000.24.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753116, 'UBND Xã Long Phước huyện​ Bến Cầu', 'Tây Ninh', '000.25.31.H53', '000.25.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753130, 'UBND Xã Long Thuận huyện​ Bến Cầu', 'Tây Ninh', '000.26.31.H53', '000.26.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753144, 'UBND Xã Lợi Thuận huyện​ Bến Cầu', 'Tây Ninh', '000.27.31.H53', '000.27.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753158, 'UBND Xã Tiên Thuận huyện​ Bến Cầu', 'Tây Ninh', '000.28.31.H53', '000.28.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753172, 'UBND huyện Châu Thành', 'Tây Ninh', '000.00.32.H53', '000.00.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753186, 'Văn phòng HĐND-UBND huyện​ Châu Thành', 'Tây Ninh', '000.01.32.H53', '000.01.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753200, 'Phòng Giáo dục và Đào tạo huyện​ Châu Thành', 'Tây Ninh', '000.02.32.H53', '000.02.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753214, 'Phòng Kinh tế và Hạ tầng​ huyện​ Châu Thành', 'Tây Ninh', '000.03.32.H53', '000.03.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753228, 'Phòng Lao động Thương binh và UBND Xã hội huyện​ Châu Thành', 'Tây Ninh', '000.04.32.H53', '000.04.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753242, 'Phòng Nội vụ huyện​ Châu Thành', 'Tây Ninh', '000.05.32.H53', '000.05.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753256, 'Phòng Nông nghiệp và Phát triển nông thôn huyện​ Châu Thành', 'Tây Ninh', '000.06.32.H53', '000.06.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753270, 'Phòng Tài chính - Kế hoạch huyện​ Châu Thành', 'Tây Ninh', '000.07.32.H53', '000.07.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753284, 'Phòng Tài nguyên và Môi trường huyện​ Châu Thành', 'Tây Ninh', '000.08.32.H53', '000.08.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753298, 'Phòng Tư pháp​ huyện​ Châu Thành', 'Tây Ninh', '000.09.32.H53', '000.09.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753312, 'Phòng Văn hóa và Thông tin​ huyện​ Châu Thành', 'Tây Ninh', '000.10.32.H53', '000.10.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753326, 'Phòng Y tế huyện​ Châu Thành', 'Tây Ninh', '000.11.32.H53', '000.11.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753340, 'Thanh tra huyện​ Châu Thành', 'Tây Ninh', '000.12.32.H53', '000.12.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753354, 'UBND Thị Trấn Châu Thành huyện​ Châu Thành', 'Tây Ninh', '000.20.32.H53', '000.20.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753368, 'UBND Xã An Bình huyện​ Châu Thành', 'Tây Ninh', '000.21.32.H53', '000.21.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753382, 'UBND Xã An Cơ huyện​ Châu Thành', 'Tây Ninh', '000.22.32.H53', '000.22.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753396, 'UBND Xã Biên Giới huyện​ Châu Thành', 'Tây Ninh', '000.23.32.H53', '000.23.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753410, 'UBND Xã Đồng Khởi huyện​ Châu Thành', 'Tây Ninh', '000.24.32.H53', '000.24.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753424, 'UBND Xã Hảo Đước huyện​ Châu Thành', 'Tây Ninh', '000.25.32.H53', '000.25.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753438, 'UBND Xã Hòa Hội huyện​ Châu Thành', 'Tây Ninh', '000.26.32.H53', '000.26.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753452, 'UBND Xã Hòa Thạnh huyện​ Châu Thành', 'Tây Ninh', '000.27.32.H53', '000.27.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753466, 'UBND Xã Long Vĩnh huyện​ Châu Thành', 'Tây Ninh', '000.28.32.H53', '000.28.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753480, 'UBND Xã Ninh Điền huyện​ Châu Thành', 'Tây Ninh', '000.29.32.H53', '000.29.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753494, 'UBND Xã Phước Vinh huyện​ Châu Thành', 'Tây Ninh', '000.30.32.H53', '000.30.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753508, 'UBND Xã Thanh Điền huyện​ Châu Thành', 'Tây Ninh', '000.31.32.H53', '000.31.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753522, 'UBND Xã Thái Bình huyện​ Châu Thành', 'Tây Ninh', '000.32.32.H53', '000.32.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753536, 'UBND Xã Thành Long huyện​ Châu Thành', 'Tây Ninh', '000.33.32.H53', '000.33.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753550, 'UBND Xã Trí Bình huyện​ Châu Thành', 'Tây Ninh', '000.34.32.H53', '000.34.32.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753564, 'UBND huyện Dương Minh Châu', 'Tây Ninh', '000.00.33.H53', '000.00.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753578, 'Văn phòng HĐND-UBND huyện​ Dương Minh Châu', 'Tây Ninh', '000.01.33.H53', '000.01.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753592, 'Phòng Giáo dục và Đào tạo huyện​ Dương Minh Châu', 'Tây Ninh', '000.02.33.H53', '000.02.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753606, 'Phòng Kinh tế và Hạ tầng​ huyện​ Dương Minh Châu', 'Tây Ninh', '000.03.33.H53', '000.03.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753620, 'Phòng Lao động Thương binh và UBND Xã hội huyện​ Dương Minh Châu', 'Tây Ninh', '000.04.33.H53', '000.04.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753634, 'Phòng Nội vụ huyện​ Dương Minh Châu', 'Tây Ninh', '000.05.33.H53', '000.05.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753648, 'Phòng Nông nghiệp và Phát triển nông thôn huyện​ Dương Minh Châu', 'Tây Ninh', '000.06.33.H53', '000.06.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753662, 'Phòng Tài chính - Kế hoạch huyện​ Dương Minh Châu', 'Tây Ninh', '000.07.33.H53', '000.07.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753676, 'Phòng Tài nguyên và Môi trường huyện​ Dương Minh Châu', 'Tây Ninh', '000.08.33.H53', '000.08.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753690, 'Phòng Tư pháp​ huyện​ Dương Minh Châu', 'Tây Ninh', '000.09.33.H53', '000.09.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753704, 'Phòng Văn hóa và Thông tin​ huyện​ Dương Minh Châu', 'Tây Ninh', '000.10.33.H53', '000.10.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753718, 'Phòng Y tế huyện​ Dương Minh Châu', 'Tây Ninh', '000.11.33.H53', '000.11.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753732, 'Thanh tra huyện​ Dương Minh Châu', 'Tây Ninh', '000.12.33.H53', '000.12.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753746, 'UBND Thị Trấn Dương Minh Châu huyện Dương Minh Châu', 'Tây Ninh', '000.20.33.H53', '000.20.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753760, 'UBND Xã Bàu Năng huyện​ Dương Minh Châu', 'Tây Ninh', '000.21.33.H53', '000.21.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753774, 'UBND Xã Bến Củi huyện​ Dương Minh Châu', 'Tây Ninh', '000.22.33.H53', '000.22.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753788, 'UBND Xã Cầu Khởi huyện​ Dương Minh Châu', 'Tây Ninh', '000.23.33.H53', '000.23.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753802, 'UBND Xã Chà Là huyện​ Dương Minh Châu', 'Tây Ninh', '000.24.33.H53', '000.24.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753816, 'UBND Xã Lộc Ninh huyện​ Dương Minh Châu', 'Tây Ninh', '000.25.33.H53', '000.25.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753830, 'UBND Xã Phan huyện​ Dương Minh Châu', 'Tây Ninh', '000.26.33.H53', '000.26.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753844, 'UBND Xã Phước Minh huyện​ Dương Minh Châu', 'Tây Ninh', '000.27.33.H53', '000.27.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753858, 'UBND Xã Phước Ninh huyện​ Dương Minh Châu', 'Tây Ninh', '000.28.33.H53', '000.28.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753872, 'UBND Xã Suối Đá huyện​ Dương Minh Châu', 'Tây Ninh', '000.29.33.H53', '000.29.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753886, 'UBND Xã Truông Mít huyện​ Dương Minh Châu', 'Tây Ninh', '000.30.33.H53', '000.30.33.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753900, 'Sở Giao thông Vận tải', 'Tây Ninh', '000.00.04.H53', '000.00.04.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753914, 'Sở Kế hoạch và Đầu tư', 'Tây Ninh', '000.00.05.H53', '000.00.05.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753928, 'Sở Ngoại vụ', 'Tây Ninh', '000.00.08.H53', '000.00.08.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753942, 'Sở Tài chính', 'Tây Ninh', '000.00.11.H53', '000.00.11.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753956, 'Thanh tra tỉnh', 'Tây Ninh', '000.00.18.H53', '000.00.18.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753970, 'Ban An toàn Giao thông', 'Tây Ninh', '000.00.19.H53', '000.00.19.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753984, 'Ban Quản lý các Khu di tích lịch sử Cách Mạng Miền Nam', 'Tây Ninh', '000.00.20.H53', '000.00.20.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2753998, 'Ban Quản lý Khu Du lịch Quốc Gia Núi Bà Đen', 'Tây Ninh', '000.00.21.H53', '000.00.21.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754012, 'Ban Quản lý Khu Kinh tế', 'Tây Ninh', '000.00.22.H53', '000.00.22.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754026, 'Ban Quản lý Vườn Quốc Gia Lò Gò Xa Mát', 'Tây Ninh', '000.00.23.H53', '000.00.23.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754040, 'Đài Phát thanh và Truyền hình Tây Ninh', 'Tây Ninh', '000.00.40.H53', '000.00.40.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754054, 'Tòa soạn báo', 'Tây Ninh', '000.00.41.H53', '000.00.41.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754068, 'Ban Quản lý Dự án Đầu tư xây dựng Tây Ninh', 'Tây Ninh', '000.00.42.H53', '000.00.42.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754082, 'Ban Quản lý Dự án Đầu tư xây dựng ngành Giao thông tỉnh Tây Ninh', 'Tây Ninh', '000.00.43.H53', '000.00.43.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754110, 'UBND huyện Bến Cầu', 'Tây Ninh', '000.00.31.H53', '000.00.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754124, 'Văn phòng HĐND-UBND huyện​ Bến Cầu', 'Tây Ninh', '000.01.31.H53', '000.01.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754138, 'Phòng Giáo dục và Đào tạo huyện​ Bến Cầu', 'Tây Ninh', '000.02.31.H53', '000.02.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2754152, 'Phòng Kinh tế và Hạ tầng​ huyện​ Bến Cầu', 'Tây Ninh', '000.03.31.H53', '000.03.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', '', '');
INSERT INTO `edoc_dynamiccontact` VALUES (2759349, 'Ban Quản lý DAĐTXD Khu kinh tế Tây Ninh', 'Tây Ninh', '000.00.24.H53', '000.00.24.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759363, 'Liên minh Hợp tác xã Tây Ninh', 'Tây Ninh', '000.00.25.H53', '000.00.25.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759377, 'Cục thi hành án tỉnh Tây Ninh', 'Tây Ninh', '000.00.26.H53', '000.00.26.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759391, 'Cục thuế tỉnh Tây Ninh', 'Tây Ninh', '000.00.27.H53', '000.00.27.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759405, 'Công ty CP Du lịch', 'Tây Ninh', '000.00.28.H53', '000.00.28.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759419, 'Cục thống kê tỉnh Tây Ninh', 'Tây Ninh', '000.00.29.H53', '000.00.29.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759433, 'Liên đoàn lao động tỉnh Tây Ninh', 'Tây Ninh', '000.00.39.H53', '000.00.39.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759447, 'Ngân hàng nhà nước tỉnh Tây Ninh', 'Tây Ninh', '000.00.45.H53', '000.00.45.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759461, 'Cục hải quan tỉnh Tây Ninh', 'Tây Ninh', '000.00.46.H53', '000.00.46.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759475, 'Viện kiểm sát tỉnh Tây Ninh', 'Tây Ninh', '000.00.47.H53', '000.00.47.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759489, 'Trường Chính trị tỉnh Tây Ninh', 'Tây Ninh', '000.00.48.H53', '000.00.48.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759607, 'Ban Quản lý Dự án Đầu tư Xây dựng ngành Nông nghiệp và Phát triển nông thôn tỉnh Tây Ninh', 'Tây Ninh', '000.00.44.H53', '000.00.44.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759608, 'Phòng Lao động Thương binh và UBND Xã hội huyện​ Bến Cầu', 'Tây Ninh', '000.04.31.H53', '000.04.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759609, 'Phòng Nội vụ huyện​ Bến Cầu', 'Tây Ninh', '000.05.31.H53', '000.05.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759610, 'Phòng Nông nghiệp và Phát triển nông thôn huyện​ Bến Cầu', 'Tây Ninh', '000.06.31.H53', '000.06.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759611, 'Phòng Tài chính - Kế hoạch huyện​ Bến Cầu', 'Tây Ninh', '000.07.31.H53', '000.07.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759612, 'Phòng Tài nguyên và Môi trường huyện​ Bến Cầu', 'Tây Ninh', '000.08.31.H53', '000.08.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759613, 'Phòng Tư pháp​ huyện​ Bến Cầu', 'Tây Ninh', '000.09.31.H53', '000.09.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759614, 'Phòng Văn hóa và Thông tin​ huyện​ Bến Cầu', 'Tây Ninh', '000.10.31.H53', '000.10.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759615, 'Phòng Y tế huyện​ Bến Cầu', 'Tây Ninh', '000.11.31.H53', '000.11.31.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759616, 'UBND huyện Gò Dầu', 'Tây Ninh', '000.00.34.H53', '000.00.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759617, 'Văn phòng HĐND-UBND huyện​ Gò Dầu', 'Tây Ninh', '000.01.34.H53', '000.01.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759618, 'Phòng Giáo dục và Đào tạo huyện​ Gò Dầu', 'Tây Ninh', '000.02.34.H53', '000.02.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759619, 'Phòng Kinh tế và Hạ tầng​ huyện​ Gò Dầu', 'Tây Ninh', '000.03.34.H53', '000.03.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759620, 'Phòng Lao động Thương binh và UBND Xã hội huyện​ Gò Dầu', 'Tây Ninh', '000.04.34.H53', '000.04.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759621, 'Phòng Nội vụ huyện​ Gò Dầu', 'Tây Ninh', '000.05.34.H53', '000.05.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759622, 'Phòng Nông nghiệp và Phát triển nông thôn huyện​ Gò Dầu', 'Tây Ninh', '000.06.34.H53', '000.06.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759623, 'Phòng Tài chính - Kế hoạch huyện​ Gò Dầu', 'Tây Ninh', '000.07.34.H53', '000.07.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759624, 'Phòng Tài nguyên và Môi trường huyện​ Gò Dầu', 'Tây Ninh', '000.08.34.H53', '000.08.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759625, 'Phòng Tư pháp​ huyện​ Gò Dầu', 'Tây Ninh', '000.09.34.H53', '000.09.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759626, 'Phòng Văn hóa và Thông tin​ huyện​ Gò Dầu', 'Tây Ninh', '000.10.34.H53', '000.10.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759627, 'Phòng Y tế huyện​ Gò Dầu', 'Tây Ninh', '000.11.34.H53', '000.11.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759628, 'UBND Thị Trấn Gò Dầu huyện​ Gò Dầu', 'Tây Ninh', '000.20.34.H53', '000.20.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759629, 'Thanh tra huyện​ Gò Dầu', 'Tây Ninh', '000.12.34.H53', '000.12.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759630, 'UBND Xã Bàu Đồn huyện​ Gò Dầu', 'Tây Ninh', '000.21.34.H53', '000.21.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759631, 'UBND Xã Cẩm Giang huyện​ Gò Dầu', 'Tây Ninh', '000.22.34.H53', '000.22.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759632, 'UBND Xã Hiệp Thạnh huyện​ Gò Dầu', 'Tây Ninh', '000.23.34.H53', '000.23.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759633, 'UBND Xã Phước Đông huyện​ Gò Dầu', 'Tây Ninh', '000.24.34.H53', '000.24.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759634, 'UBND Xã Phước Thạnh huyện​ Gò Dầu', 'Tây Ninh', '000.25.34.H53', '000.25.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759635, 'UBND Xã Phước Trạch huyện​ Gò Dầu', 'Tây Ninh', '000.26.34.H53', '000.26.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759636, 'UBND Xã Thanh Phước huyện​ Gò Dầu', 'Tây Ninh', '000.27.34.H53', '000.27.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759637, 'UBND Xã Thạnh Đức huyện​ Gò Dầu', 'Tây Ninh', '000.28.34.H53', '000.28.34.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759638, 'UBND huyện Hoà Thành', 'Tây Ninh', '000.00.35.H53', '000.00.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759639, 'Văn phòng HĐND-UBND huyện​ Hòa Thành', 'Tây Ninh', '000.01.35.H53', '000.01.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759640, 'Phòng Giáo dục và Đào tạo huyện​ Hòa Thành', 'Tây Ninh', '000.02.35.H53', '000.02.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759641, 'Phòng Kinh tế và Hạ tầng​ huyện​ Hòa Thành', 'Tây Ninh', '000.03.35.H53', '000.03.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759642, 'Phòng Lao động Thương binh và UBND Xã hội huyện​ Hòa Thành', 'Tây Ninh', '000.04.35.H53', '000.04.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759643, 'Phòng Nội vụ huyện​ Hòa Thành', 'Tây Ninh', '000.05.35.H53', '000.05.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759644, 'Phòng Nông nghiệp và Phát triển nông thôn huyện​ Hòa Thành', 'Tây Ninh', '000.06.35.H53', '000.06.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759645, 'Phòng Tài chính - Kế hoạch huyện​ Hòa Thành', 'Tây Ninh', '000.07.35.H53', '000.07.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759646, 'Phòng Tài nguyên và Môi trường huyện​ Hòa Thành', 'Tây Ninh', '000.08.35.H53', '000.08.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759647, 'Phòng Tư pháp​ huyện​ Hòa Thành', 'Tây Ninh', '000.09.35.H53', '000.09.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759648, 'Phòng Văn hóa và Thông tin​ huyện​ Hòa Thành', 'Tây Ninh', '000.10.35.H53', '000.10.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759649, 'Phòng Y tế huyện​ Hòa Thành', 'Tây Ninh', '000.11.35.H53', '000.11.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759650, 'Thanh tra huyện​ Hòa Thành', 'Tây Ninh', '000.12.35.H53', '000.12.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759651, 'UBND Thị trấn Hòa Thành huyện​ Hòa Thành', 'Tây Ninh', '000.20.35.H53', '000.20.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759652, 'UBND Xã Hiệp Tân huyện​ Hòa Thành', 'Tây Ninh', '000.21.35.H53', '000.21.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759653, 'UBND Xã Long Thành Bắchuyện​ Hòa Thành', 'Tây Ninh', '000.22.35.H53', '000.22.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759654, 'UBND Xã Long Thành Nam huyện​ Hòa Thành', 'Tây Ninh', '000.23.35.H53', '000.23.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759655, 'UBND Xã Long Thành Trung huyện​ Hòa Thành', 'Tây Ninh', '000.24.35.H53', '000.24.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759656, 'UBND Xã Trường Hòa huyện​ Hòa Thành', 'Tây Ninh', '000.25.35.H53', '000.25.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759657, 'UBND Xã Trường Đông huyện​ Hòa Thành', 'Tây Ninh', '000.26.35.H53', '000.26.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759658, 'UBND Xã Trường Tây huyện​ Hòa Thành', 'Tây Ninh', '000.27.35.H53', '000.27.35.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759659, 'UBND huyện Tân Biên', 'Tây Ninh', '000.00.36.H53', '000.00.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759660, 'Văn phòng HĐND - UBND huyện​ Tân Biên', 'Tây Ninh', '000.01.36.H53', '000.01.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759661, 'Phòng Giáo dục và Đào tạo huyện​ Tân Biên', 'Tây Ninh', '000.02.36.H53', '000.02.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759662, 'Phòng Kinh tế và Hạ tầng​ huyện​ Tân Biên', 'Tây Ninh', '000.03.36.H53', '000.03.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759663, 'Phòng Lao động Thương binh và UBND Xã hội huyện​ Tân Biên', 'Tây Ninh', '000.04.36.H53', '000.04.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759664, 'Phòng Nội vụ huyện​ Tân Biên', 'Tây Ninh', '000.05.36.H53', '000.05.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759665, 'Phòng Nông nghiệp và Phát triển nông thôn huyện​ Tân Biên', 'Tây Ninh', '000.06.36.H53', '000.06.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759666, 'Phòng Tài chính - Kế hoạch huyện​ Tân Biên', 'Tây Ninh', '000.07.36.H53', '000.07.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759667, 'Phòng Tài nguyên và Môi trường huyện​ Tân Biên', 'Tây Ninh', '000.08.36.H53', '000.08.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759668, 'Phòng Tư pháp​ huyện​ Tân Biên', 'Tây Ninh', '000.09.36.H53', '000.09.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759669, 'Phòng Văn hóa và Thông tin​ huyện​ Tân Biên', 'Tây Ninh', '000.10.36.H53', '000.10.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759670, 'Phòng Y tế huyện​ Tân Biên', 'Tây Ninh', '000.11.36.H53', '000.11.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759671, 'Thanh tra huyện​ Tân Biên', 'Tây Ninh', '000.12.36.H53', '000.12.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759672, 'UBND Thị trấn Tân Biên huyện​ Tân Biên', 'Tây Ninh', '000.20.36.H53', '000.20.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759673, 'UBND Xã Hòa Hiệp huyện​ Tân Biên', 'Tây Ninh', '000.21.36.H53', '000.21.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759674, 'UBND Xã Mỏ Công huyện​ Tân Biên', 'Tây Ninh', '000.22.36.H53', '000.22.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759675, 'UBND Xã Tân Bình huyện​ Tân Biên', 'Tây Ninh', '000.23.36.H53', '000.23.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759676, 'UBND Xã Tân Lập huyện​ Tân Biên', 'Tây Ninh', '000.24.36.H53', '000.24.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759677, 'UBND Xã Tân Phong huyện​ Tân Biên', 'Tây Ninh', '000.25.36.H53', '000.25.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759678, 'UBND Xã Thạnh Bắc huyện​ Tân Biên', 'Tây Ninh', '000.26.36.H53', '000.26.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759679, 'UBND Xã Thạnh Bình huyện​ Tân Biên', 'Tây Ninh', '000.27.36.H53', '000.27.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759680, 'UBND Xã Thạnh Tây huyện​ Tân Biên', 'Tây Ninh', '000.28.36.H53', '000.28.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759681, 'UBND Xã Trà Vong huyện​ Tân Biên', 'Tây Ninh', '000.29.36.H53', '000.29.36.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759682, 'UBND huyện Tân Châu', 'Tây Ninh', '000.00.37.H53', '000.00.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759683, 'Văn phòng HĐND-UBND huyện​ Tân Châu', 'Tây Ninh', '000.01.37.H53', '000.01.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759684, 'Phòng Giáo dục và Đào tạo huyện​ Tân Châu', 'Tây Ninh', '000.02.37.H53', '000.02.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759685, 'Phòng Kinh tế và Hạ tầng​ huyện​ Tân Châu', 'Tây Ninh', '000.03.37.H53', '000.03.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759686, 'Phòng Lao động Thương binh và UBND Xã hội huyện​ Tân Châu', 'Tây Ninh', '000.04.37.H53', '000.04.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759687, 'Phòng Nội vụ huyện​ Tân Châu', 'Tây Ninh', '000.05.37.H53', '000.05.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759688, 'Phòng Nông nghiệp và Phát triển nông thôn huyện​ Tân Châu', 'Tây Ninh', '000.06.37.H53', '000.06.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759689, 'Phòng Tài chính - Kế hoạch huyện​ Tân Châu', 'Tây Ninh', '000.07.37.H53', '000.07.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759690, 'Phòng Tài nguyên và Môi trường huyện​ Tân Châu', 'Tây Ninh', '000.08.37.H53', '000.08.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759691, 'Phòng Tư pháp​ huyện​ Tân Châu', 'Tây Ninh', '000.09.37.H53', '000.09.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759692, 'Phòng Văn hóa và Thông tin​ huyện​ Tân Châu', 'Tây Ninh', '000.10.37.H53', '000.10.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759693, 'Phòng Y tế huyện​ Tân Châu', 'Tây Ninh', '000.1137.H53', '000.1137.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759694, 'Thanh tra huyện​ Tân Châu', 'Tây Ninh', '000.12.37.H53', '000.12.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759695, 'UBND Thị trấn Tân Châu huyện​ Tân Châu', 'Tây Ninh', '000.20.37.H53', '000.20.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759696, 'UBND Xã Suối Dây huyện​ Tân Châu', 'Tây Ninh', '000.21.37.H53', '000.21.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759697, 'UBND Xã Suối Ngô huyện​ Tân Châu', 'Tây Ninh', '000.22.37.H53', '000.22.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759698, 'UBND Xã Tân Đông huyện​ Tân Châu', 'Tây Ninh', '000.23.37.H53', '000.23.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759699, 'UBND Xã Tân Hà huyện​ Tân Châu', 'Tây Ninh', '000.24.37.H53', '000.24.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759700, 'UBND Xã Tân Hiệp huyện​ Tân Châu', 'Tây Ninh', '000.25.37.H53', '000.25.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759701, 'UBND Xã Tân Hòa huyện​ Tân Châu', 'Tây Ninh', '000.26.37.H53', '000.26.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759702, 'UBND Xã Tân Hội huyện​ Tân Châu', 'Tây Ninh', '000.27.37.H53', '000.27.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759703, 'UBND Xã Tân Hưng huyện​ Tân Châu', 'Tây Ninh', '000.28.37.H53', '000.28.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759704, 'UBND Xã Tân Phú huyện​ Tân Châu', 'Tây Ninh', '000.29.37.H53', '000.29.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759705, 'UBND Xã Tân Thành huyện​ Tân Châu', 'Tây Ninh', '000.30.37.H53', '000.30.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759706, 'UBND Xã Thanh Đông huyện​ Tân Châu', 'Tây Ninh', '000.31.37.H53', '000.31.37.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759707, 'UBND huyện Trảng Bàng', 'Tây Ninh', '000.00.38.H53', '000.00.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759708, 'Văn phòng HĐND-UBND huyện​ Trảng Bàng', 'Tây Ninh', '000.01.38.H53', '000.01.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759709, 'Phòng Giáo dục và Đào tạo huyện​ Trảng Bàng', 'Tây Ninh', '000.02.38.H53', '000.02.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759710, 'Phòng Kinh tế và Hạ tầng​ huyện​ Trảng Bàng', 'Tây Ninh', '000.03.38.H53', '000.03.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759711, 'Phòng Lao động Thương binh và UBND Xã hội huyện​ Trảng Bàng', 'Tây Ninh', '000.04.38.H53', '000.04.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759712, 'Phòng Nội vụ huyện​ Trảng Bàng', 'Tây Ninh', '000.05.38.H53', '000.05.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759713, 'Phòng Nông nghiệp và Phát triển nông thôn huyện​ Trảng Bàng', 'Tây Ninh', '000.06.38.H53', '000.06.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759714, 'Phòng Tài nguyên và Môi trường huyện​ Trảng Bàng', 'Tây Ninh', '000.08.38.H53', '000.08.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759715, 'Phòng Tài chính - Kế hoạch huyện​ Trảng Bàng', 'Tây Ninh', '000.07.38.H53', '000.07.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759716, 'Phòng Tư pháp​ huyện​ Trảng Bàng', 'Tây Ninh', '000.09.38.H53', '000.09.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759717, 'Phòng Văn hóa và Thông tin​ huyện​ Trảng Bàng', 'Tây Ninh', '000.10.38.H53', '000.10.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759718, 'Phòng Y tế huyện​ Trảng Bàng', 'Tây Ninh', '000.11.38.H53', '000.11.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759719, 'Thanh tra huyện​ Trảng Bàng', 'Tây Ninh', '000.12.38.H53', '000.12.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759720, 'UBND Thị trấn Trảng Bàng huyện​ Trảng Bàng', 'Tây Ninh', '000.20.38.H53', '000.20.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759721, 'UBND Xã An Hòa huyện​ Trảng Bàng', 'Tây Ninh', '000.21.38.H53', '000.21.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759722, 'UBND Xã An Tịnh huyện​ Trảng Bàng', 'Tây Ninh', '000.22.38.H53', '000.22.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759723, 'UBND Xã Bình Thạnh huyện​ Trảng Bàng', 'Tây Ninh', '000.23.38.H53', '000.23.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759724, 'UBND Xã Đôn Thuận huyện​ Trảng Bàng', 'Tây Ninh', '000.24.38.H53', '000.24.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759725, 'UBND Xã Gia Bình huyện​ Trảng Bàng', 'Tây Ninh', '000.25.38.H53', '000.25.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759726, 'UBND Xã Gia Lộc huyện​ Trảng Bàng', 'Tây Ninh', '000.26.38.H53', '000.26.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759727, 'UBND Xã Hưng Thuận huyện​ Trảng Bàng', 'Tây Ninh', '000.27.38.H53', '000.27.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759728, 'UBND Xã Lộc Hưng huyện​ Trảng Bàng', 'Tây Ninh', '000.28.38.H53', '000.28.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759729, 'UBND Xã Phước Chỉ huyện​ Trảng Bàng', 'Tây Ninh', '000.29.38.H53', '000.29.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759730, 'UBND Xã Phước Lưu huyện​ Trảng Bàng', 'Tây Ninh', '000.30.38.H53', '000.30.38.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759731, 'Sở Công Thương', 'Tây Ninh', '000.00.02.H53', '000.00.02.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759732, 'Chi Cục quản lý thị trường - Sở Công thương', 'Tây Ninh', '000.01.02.H53', '000.01.02.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759733, 'Trung tâm Khuyến công và Tư vấn phát triển công nghiệp - Sở Công thương', 'Tây Ninh', '000.02.02.H53', '000.02.02.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759734, 'Trung tâm xúc tiến thương mại - Sở Công thương', 'Tây Ninh', '000.03.02.H53', '000.03.02.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759735, 'Sở Giáo dục và Đào tạo', 'Tây Ninh', '000.00.03.H53', '000.00.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759736, 'Trung tâm Giáo dục thường xuyên tỉnh Tây Ninh', 'Tây Ninh', '000.01.03.H53', '000.01.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759737, 'Trường Cao đẳng sư phạm Tây Ninh', 'Tây Ninh', '000.02.03.H53', '000.02.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759738, 'Trường Khuyết tật tỉnh Tây Ninh', 'Tây Ninh', '000.03.03.H53', '000.03.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759739, 'Trường Phổ Thông Dân tộc Nội trú', 'Tây Ninh', '000.04.03.H53', '000.04.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759740, 'Trường THPT Chuyên Hoàng Lê Kha', 'Tây Ninh', '000.05.03.H53', '000.05.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759741, 'Trường THPT Châu Thành', 'Tây Ninh', '000.06.03.H53', '000.06.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759742, 'Trường THPT Bình Thạnh', 'Tây Ninh', '000.07.03.H53', '000.07.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759743, 'Trường THPT Dương Minh Châu', 'Tây Ninh', '000.08.03.H53', '000.08.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759744, 'Trường THPT Hoàng Văn Thụ', 'Tây Ninh', '000.09.03.H53', '000.09.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759745, 'Trường THPT Huỳnh Thúc Kháng', 'Tây Ninh', '000.10.03.H53', '000.10.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759746, 'Trường THPT Lê Duẩn', 'Tây Ninh', '000.11.03.H53', '000.11.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759747, 'Trường THPT Lê Hồng Phong', 'Tây Ninh', '000.12.03.H53', '000.12.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759748, 'Trường THPT Lê Quý Ðôn', 'Tây Ninh', '000.13.03.H53', '000.13.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759749, 'Trường THPT Lộc Hưng ', 'Tây Ninh', '000.14.03.H53', '000.14.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759750, 'Trường THPT Lương Thế Vinh', 'Tây Ninh', '000.15.03.H53', '000.15.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759751, 'Trường THPT Lý Thường Kiệt', 'Tây Ninh', '000.16.03.H53', '000.16.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759752, 'Trường THPT Ngô Gia Tự', 'Tây Ninh', '000.17.03.H53', '000.17.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759753, 'Trường THPT Nguyễn An Ninh', 'Tây Ninh', '000.18.03.H53', '000.18.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759754, 'Trường THPT Nguyễn Bỉnh Khiêm', 'Tây Ninh', '000.19.03.H53', '000.19.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759755, 'Trường THPT Nguyễn Chí Thanh', 'Tây Ninh', '000.20.03.H53', '000.20.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759756, 'Trường THPT Nguyễn Ðình Chiểu', 'Tây Ninh', '000.21.03.H53', '000.21.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759757, 'Trường THPT Nguyễn Huệ', 'Tây Ninh', '000.22.03.H53', '000.22.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759758, 'Trường THPT Nguyễn Thái Bình', 'Tây Ninh', '000.23.03.H53', '000.23.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759759, 'Trường THPT Nguyễn Trãi', 'Tây Ninh', '000.24.03.H53', '000.24.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759760, 'Trường THPT Nguyễn Trung Trực', 'Tây Ninh', '000.25.03.H53', '000.25.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759761, 'Trường THPT Nguyễn Văn Trỗi', 'Tây Ninh', '000.26.03.H53', '000.26.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759762, 'Trường THPT Quang Trung', 'Tây Ninh', '000.27.03.H53', '000.27.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759763, 'Trường THPT Tân Châu', 'Tây Ninh', '000.28.03.H53', '000.28.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759764, 'Trường THPT Tân Ðông', 'Tây Ninh', '000.29.03.H53', '000.29.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759765, 'Trường THPT Tân Hưng', 'Tây Ninh', '000.30.03.H53', '000.30.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759766, 'Trường THPT Tây Ninh', 'Tây Ninh', '000.31.03.H53', '000.31.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759767, 'Trường THPT Trảng Bàng', 'Tây Ninh', '000.32.03.H53', '000.32.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759768, 'Trường THPT Trần Ðại Nghĩa', 'Tây Ninh', '000.33.03.H53', '000.33.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759769, 'Trường THPT Trần Phú', 'Tây Ninh', '000.34.03.H53', '000.34.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759770, 'Trường THPT Trần Quốc Ðại', 'Tây Ninh', '000.35.03.H53', '000.35.03.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759771, 'Sở Khoa học và Công nghệ', 'Tây Ninh', '000.00.06.H53', '000.00.06.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759772, 'Chi cục Tiêu chuẩn Đo lường Chất lượng - Sở KHCN', 'Tây Ninh', '000.01.06.H53', '000.01.06.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759773, 'Trung tâm kỹ thuật Tiêu chuẩn Đo lường Chất lượng - Chi cục TCĐLCL', 'Tây Ninh', '001.01.07.H53', '001.01.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759774, 'Trung tâm Thông tin, Ứng dụng tiến bộ khoa học và công nghệ - Sở KHCN', 'Tây Ninh', '000.02.06.H53', '000.02.06.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759775, 'Sở Lao động - Thương binh và Xã hội', 'Tây Ninh', '000.00.07.H53', '000.00.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759776, 'Chi cục Phòng, chống tệ nạn Xã hội - Sở LĐTBXH', 'Tây Ninh', '000.01.07.H53', '000.01.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759777, 'Trung Tâm Bảo Trợ Xã Hội tỉnh Tây Ninh - Sở LĐTBXH', 'Tây Ninh', '000.02.07.H53', '000.02.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759778, 'Trung tâm Dịch vụ việc làm - Sở LĐTBXH', 'Tây Ninh', '000.03.07.H53', '000.03.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759779, 'Trung tâm Giáo dục Lao động Xã hội - Sở LĐTBXH', 'Tây Ninh', '000.04.07.H53', '000.04.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759780, 'Trung tâm nuôi dạy trẻ khiếm thị Tây Ninh - Sở LĐTBXH', 'Tây Ninh', '000.05.07.H53', '000.05.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759781, 'Trường Cao đẳng Nghề Tây Ninh - Sở LĐTBXH', 'Tây Ninh', '000.06.07.H53', '000.06.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759782, 'Trường Trung cấp Kinh tế Kỹ thuật Tây Ninh - Sở LĐTBXH', 'Tây Ninh', '000.07.07.H53', '000.07.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759783, 'Trường Trung cấp Nghề Khu vực Nam Tây Ninh - Sở LĐTBXH', 'Tây Ninh', '000.08.07.H53', '000.08.07.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759784, 'Sở Nội vụ', 'Tây Ninh', '000.00.09.H53', '000.00.09.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759785, 'Ban Tôn giáo - Sở Nội vụ', 'Tây Ninh', '000.01.09.H53', '000.01.09.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759786, 'Ban Thi đua - Khen thưởng - Sở Nội vụ', 'Tây Ninh', '000.02.09.H53', '000.02.09.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759787, 'Chi cục Văn thư - Lưu trữ - Sở Nội vụ', 'Tây Ninh', '000.03.09.H53', '000.03.09.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759788, 'Sở Nông nghiệp và Phát triển nông thôn', 'Tây Ninh', '000.00.10.H53', '000.00.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759789, 'Ban Quản lý khu rừng phòng hộ Dầu Tiếng - Sở NNPTNT', 'Tây Ninh', '000.01.10.H53', '000.01.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759790, 'Ban Quản lý khu rừng VHLS Chàng Riệc - Sở NNPTNT', 'Tây Ninh', '000.02.10.H53', '000.02.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759791, 'Chi cục Chăn nuôi và Thú y - Sở NNPTNT', 'Tây Ninh', '000.03.10.H53', '000.03.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759792, 'Chi cục Kiểm lâm - Sở NNPTNT', 'Tây Ninh', '000.04.10.H53', '000.04.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759793, 'Chi cục Phát triển nông thôn - Sở NNPTNT', 'Tây Ninh', '000.05.10.H53', '000.05.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759794, 'Chi cục Quản lý chất lượng Nông lâm sản và Thủy sản - Sở NNPTNT', 'Tây Ninh', '000.06.10.H53', '000.06.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759795, 'Chi cục Thủy lợi - Sở NNPTNT', 'Tây Ninh', '000.07.10.H53', '000.07.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759796, 'Chi cục Trồng trọt và Bảo vệ thực vật - Sở NNPTNT', 'Tây Ninh', '000.08.10.H53', '000.08.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759797, 'Trung tâm Khuyến nông - Sở NNPTNT', 'Tây Ninh', '000.09.10.H53', '000.09.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759798, 'Trung tâm Nước sạch và Vệ sinh môi trường nông thôn - Sở NNPTNT', 'Tây Ninh', '000.10.10.H53', '000.10.10.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759799, 'Sở Tài nguyên và Môi trường', 'Tây Ninh', '000.00.12.H53', '000.00.12.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759800, 'Chi cục Bảo vệ môi trường - Sở TNMT', 'Tây Ninh', '000.01.12.H53', '000.01.12.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759801, 'Chi cục Quản lý đất đai - Sở TNMT', 'Tây Ninh', '000.02.12.H53', '000.02.12.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759802, 'Trung tâm Công nghệ thông tin tài nguyên và môi trường - Sở TNMT', 'Tây Ninh', '000.03.12.H53', '000.03.12.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759803, 'Trung tâm Phát triển quỹ đất - Sở TNMT', 'Tây Ninh', '000.04.12.H53', '000.04.12.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759804, 'Trung tâm Quan trắc tài nguyên và môi trường - Sở TNMT', 'Tây Ninh', '000.05.12.H53', '000.05.12.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759805, 'Văn phòng Đăng ký đất đai - Sở TNMT', 'Tây Ninh', '000.06.12.H53', '000.06.12.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759806, 'Sở Thông tin và Truyền thông', 'Tây Ninh', '000.00.13.H53', '000.00.13.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759808, 'Sở Tư pháp', 'Tây Ninh', '000.00.14.H53', '000.00.14.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759809, 'Phòng Công chứng số 1​ - Sở Tư pháp', 'Tây Ninh', '000.01.14.H53', '000.01.14.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759810, 'Phòng Công chứng số 2 - Sở Tư pháp', 'Tây Ninh', '000.02.14.H53', '000.02.14.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759811, 'Phòng Công chứng số 3 - Sở Tư pháp', 'Tây Ninh', '000.03.14.H53', '000.03.14.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759812, 'Trung tâm Dịch vụ bán đấu giá tài sản ​ - Sở Tư pháp', 'Tây Ninh', '000.04.14.H53', '000.04.14.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759813, 'Trung tâm Trợ giúp pháp lý nhà nước tỉnh Tây Ninh - Sở Tư pháp', 'Tây Ninh', '000.05.14.H53', '000.05.14.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759814, 'Sở Văn hóa, Thể thao và Du lịch', 'Tây Ninh', '000.00.15.H53', '000.00.15.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759815, 'Bảo tàng tỉnh - Sở VHTTDL', 'Tây Ninh', '000.01.15.H53', '000.01.15.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759816, 'Đoàn Nghệ thuật tỉnh - Sở VHTTDL', 'Tây Ninh', '000.02.15.H53', '000.02.15.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759817, 'Thư viện tỉnh - Sở VHTTDL', 'Tây Ninh', '000.03.15.H53', '000.03.15.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759818, 'Trung tâm Huấn luyện và Thi đấu Thể dục Thể thao - Sở VHTTDL', 'Tây Ninh', '000.04.15.H53', '000.04.15.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759819, 'Trung tâm Phát hành Phim và Chiếu bóng - Sở VHTTDL', 'Tây Ninh', '000.05.15.H53', '000.05.15.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759820, 'Trung tâm Văn hóa tỉnh - Sở VHTTDL', 'Tây Ninh', '000.06.15.H53', '000.06.15.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759821, 'Trung tâm Thông tin Xúc tiến du lịch Tây Ninh - Sở VHTTDL', 'Tây Ninh', '000.07.15.H53', '000.07.15.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759822, 'Sở Xây dựng', 'Tây Ninh', '000.00.16.H53', '000.00.16.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759823, 'Thanh Tra Sở - Sở Xây dựng', 'Tây Ninh', '000.01.16.H53', '000.01.16.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759824, 'Trung tâm Quy hoạch và Giám định chất lượng xây dựng - Sở Xây dựng', 'Tây Ninh', '000.02.16.H53', '000.02.16.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759825, 'Sở Y tế', 'Tây Ninh', '000.00.17.H53', '000.00.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759826, 'Bệnh viện Da khoa tỉnh Tây Ninh', 'Tây Ninh', '000.01.17.H53', '000.01.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759827, 'Bệnh viện Lao và Bệnh phổi tỉnh Tây Ninh', 'Tây Ninh', '000.02.17.H53', '000.02.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759828, 'Bệnh viện Phục hồi chức năng tỉnh Tây Ninh', 'Tây Ninh', '000.03.17.H53', '000.03.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759829, 'Bệnh viện Y dược Cổ truyền tỉnh Tây Ninh', 'Tây Ninh', '000.04.17.H53', '000.04.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759830, 'Chi cục An toàn vệ sinh Thực phẩm', 'Tây Ninh', '000.05.17.H53', '000.05.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759831, 'Chi cục Dân số - Kế hoạch hoá gia đình', 'Tây Ninh', '000.06.17.H53', '000.06.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759832, 'Trung tâm Chăm sóc Sức khỏe Sinh sản', 'Tây Ninh', '000.07.17.H53', '000.07.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759833, 'Trung tâm Giám định Y khoa tỉnh Tây Ninh', 'Tây Ninh', '000.08.17.H53', '000.08.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759834, 'Trung tâm Kiểm dịch Y tế Quốc tế', 'Tây Ninh', '000.09.17.H53', '000.09.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759835, 'Trung tâm Kiểm nghiệm thuốc, mỹ phẩm, thực phẩm ', 'Tây Ninh', '000.10.17.H53', '000.10.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759836, 'Trung tâm Kiểm soát bệnh tật tỉnh Tây Ninh', 'Tây Ninh', '000.11.17.H53', '000.11.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759837, 'Trung tâm Pháp y tỉnh Tây Ninh', 'Tây Ninh', '000.12.17.H53', '000.12.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759838, 'Trung tâm Y tế thành phố Tây Ninh', 'Tây Ninh', '000.13.17.H53', '000.13.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759839, 'Trung tâm Y tế huyện Bến Cầu', 'Tây Ninh', '000.14.17.H53', '000.14.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759840, 'Trung tâm Y tế huyện Châu Thành', 'Tây Ninh', '000.15.17.H53', '000.15.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759841, 'Trung tâm Y tế huyện Dương Minh Châu', 'Tây Ninh', '000.16.17.H53', '000.16.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759842, 'Trung tâm Y tế huyện Gò Dầu', 'Tây Ninh', '000.17.17.H53', '000.17.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759843, 'Trung tâm Y tế huyện Hoà Thành', 'Tây Ninh', '000.18.17.H53', '000.18.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759844, 'Trung tâm Y tế huyện Tân Biên', 'Tây Ninh', '000.19.17.H53', '000.19.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759845, 'Trung tâm Y tế huyện Tân Châu', 'Tây Ninh', '000.20.17.H53', '000.20.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759846, 'Trung tâm Y tế huyện Trảng Bàng', 'Tây Ninh', '000.21.17.H53', '000.21.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759847, 'Trường Trung cấp Y tế tỉnh Tây Ninh', 'Tây Ninh', '000.22.17.H53', '000.22.17.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759848, 'UBND Thành phố Tây Ninh', 'Tây Ninh', '000.00.30.H53', '000.00.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759849, 'Văn phòng HĐND-UBND thành phố Tây Ninh', 'Tây Ninh', '000.01.30.H53', '000.01.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759850, 'Phòng Giáo dục và Đào tạo thành phố Tây Ninh', 'Tây Ninh', '000.02.30.H53', '000.02.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759851, 'Phòng Kinh tế và Hạ tầng​ thành phố Tây Ninh', 'Tây Ninh', '000.03.30.H53', '000.03.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759852, 'Phòng Lao động Thương binh và UBND Xã hội thành phố Tây Ninh', 'Tây Ninh', '000.04.30.H53', '000.04.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759853, 'Phòng Nội vụ thành phố Tây Ninh', 'Tây Ninh', '000.05.30.H53', '000.05.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759854, 'Phòng Nông nghiệp và Phát triển nông thôn thành phố Tây Ninh', 'Tây Ninh', '000.06.30.H53', '000.06.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759855, 'Phòng Tài chính - Kế hoạch thành phố Tây Ninh', 'Tây Ninh', '000.07.30.H53', '000.07.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759856, 'Phòng Tài nguyên và Môi trường thành phố Tây Ninh', 'Tây Ninh', '000.08.30.H53', '000.08.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759857, 'Phòng Tư pháp​ thành phố Tây Ninh', 'Tây Ninh', '000.09.30.H53', '000.09.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759858, 'Phòng Văn hóa và Thông tin​ thành phố Tây Ninh', 'Tây Ninh', '000.10.30.H53', '000.10.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759859, 'Phòng Y tế thành phố Tây Ninh', 'Tây Ninh', '000.11.30.H53', '000.11.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759860, 'Thanh tra thành phố Tây Ninh', 'Tây Ninh', '000.12.30.H53', '000.12.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759861, 'UBND Phường 1 thành phố Tây Ninh', 'Tây Ninh', '000.20.30.H53', '000.20.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759862, 'UBND Phường 2 thành phố Tây Ninh', 'Tây Ninh', '000.21.30.H53', '000.21.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759863, 'UBND Phường 3 thành phố Tây Ninh', 'Tây Ninh', '000.22.30.H53', '000.22.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759864, 'UBND Phường 4 thành phố Tây Ninh', 'Tây Ninh', '000.23.30.H53', '000.23.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759865, 'UBND Xã Bình Minh thành phố Tây Ninh', 'Tây Ninh', '000.24.30.H53', '000.24.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759866, 'UBND Phường Hiệp Ninh thành phố Tây Ninh', 'Tây Ninh', '000.25.30.H53', '000.25.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759867, 'UBND Phường Ninh Sơn thành phố Tây Ninh', 'Tây Ninh', '000.26.30.H53', '000.26.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759868, 'UBND Phường Ninh Thạnh thành phố Tây Ninh', 'Tây Ninh', '000.27.30.H53', '000.27.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759869, 'UBND Xã Tân Bình thành phố Tây Ninh', 'Tây Ninh', '000.28.30.H53', '000.28.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2759870, 'UBND Xã Thạnh Tân thành phố Tây Ninh', 'Tây Ninh', '000.29.30.H53', '000.29.30.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2760014, 'Quỹ đầu tư phát triển tỉnh Tây Ninh', 'Tây Ninh', '000.00.49.H53', '000.00.49.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2760028, 'Ban quản lý Cửa khẩu quốc tế Mộc Bài', 'Tây Ninh', '000.00.50.H53', '000.00.50.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2760042, 'Ban quản lý Cửa khẩu quốc tế Xa Mát', 'Tây Ninh', '000.00.51.H53', '000.00.51.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2760065, 'Tỉnh Ủy Tây Ninh', 'Tây Ninh', '000.00.52.H53', '000.00.52.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2760079, 'VP HĐNĐ tỉnh Tây Ninh', 'Tây Ninh', '000.00.53.H53', '000.00.53.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2760093, 'Bộ chỉ huy quân sự tỉnh Tây Ninh ', 'Tây Ninh', '000.00.54.H53', '000.00.54.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2776441, 'Hội đồng nhân dân tỉnh Tây Ninh', 'Tây Ninh', '000.00.55.H53', '000.00.55.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2776455, 'Quỹ Bảo vệ Môi trường tỉnh Tây Ninh', 'Tây Ninh', '000.00.56.H53', '000.00.56.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2776469, 'Quỹ Hỗ trợ Phát triển Hợp tác xã tỉnh Tây Ninh', 'Tây Ninh', '000.00.57.H53', '000.00.57.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2776483, 'Quỹ Phát triển Đất tỉnh Tây Ninh', 'Tây Ninh', '000.00.58.H53', '000.00.58.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2808810, 'Kho bạc nhà nước tỉnh Tây Ninh', 'Tây Ninh', '000.00.59.H53', '000.00.59.H53@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2808858, 'TT Công nghệ thông tin và Truyền thông - Sở TTTT', 'TN', '000.01.13.H53', '000.01.13.H53@bmail.vn', 'TN', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853408, 'Bộ Công an', 'Tây Ninh', '000.00.00.G01', '000.00.00.G01@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853422, 'Bộ Công Thương', 'Tây Ninh', '000.00.00.G02', '000.00.00.G02@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853436, 'Bộ Giáo dục và Đào tạo', 'Tây Ninh', '000.00.00.G03', '000.00.00.G03@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853450, 'Bộ Giao thông vận tải', 'Tây Ninh', '000.00.00.G04', '000.00.00.G04@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853464, 'Bộ Kế hoạch Đầu tư', 'Tây Ninh', '000.00.00.G05', '000.00.00.G05@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853478, 'Bộ Khoa học và Công nghệ', 'Tây Ninh', '000.00.00.G06', '000.00.00.G06@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853492, 'Bộ Lao động, Thương binh và Xã hội', 'Tây Ninh', '000.00.00.G07', '000.00.00.G07@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853506, 'Bộ Ngoại giao', 'Tây Ninh', '000.00.00.G08', '000.00.00.G08@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853520, 'Bộ Nội vụ', 'Tây Ninh', '000.00.00.G09', '000.00.00.G09@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853534, 'Bộ Nông nghiệp và Phát triển nông thôn', 'Tây Ninh', '000.00.00.G10', '000.00.00.G10@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853548, 'Bộ Quốc phòng', 'Tây Ninh', '000.00.00.G11', '000.00.00.G11@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853562, 'Bộ Tài chính', 'Tây Ninh', '000.00.00.G12', '000.00.00.G12@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853576, 'Bộ Tài nguyên và Môi trường', 'Tây Ninh', '000.00.00.G13', '000.00.00.G13@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853590, 'Bộ Thông tin và Truyền thông', 'Tây Ninh', '000.00.00.G14', '000.00.00.G14@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853604, 'Bộ Tư Pháp', 'Tây Ninh', '000.00.00.G15', '000.00.00.G15@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853618, 'Bộ Văn hóa-Thể thao-Du lịch', 'Tây Ninh', '000.00.00.G16', '000.00.00.G16@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853632, 'Bộ Xây dựng', 'Tây Ninh', '000.00.00.G17', '000.00.00.G17@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853646, 'Bộ Y Tế ', 'Tây Ninh', '000.00.00.G18', '000.00.00.G18@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853660, 'Ngân hàng Nhà nước Việt Nam', 'Tây Ninh', '000.00.00.G19', '000.00.00.G19@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853674, 'Thanh tra Chính phủ', 'Tây Ninh', '000.00.00.G20', '000.00.00.G20@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853688, 'Ủy Ban Dân tộc TƯ', 'Tây Ninh', '000.00.00.G21', '000.00.00.G21@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853702, 'Văn phòng Chính phủ', 'Tây Ninh', '000.00.00.G22', '000.00.00.G22@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853716, 'Bảo hiểm xã hội Việt Nam', 'Tây Ninh', '000.00.00.G24', '000.00.00.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853730, 'Đài tiếng nói Việt Nam', 'Tây Ninh', '000.00.00.G27', '000.00.00.G27@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853744, 'Đài truyền hình Việt Nam', 'Tây Ninh', '000.00.00.G28', '000.00.00.G28@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2853763, 'Cục Tin học hóa - Bộ Thông tin và Truyền thông', 'VN', '000.00.27.G14', '000.00.27.G14@bmail.vn', 'VN', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2937666, 'Bảo hiểm xã hội tỉnh Tây Ninh', 'Tây Ninh', '000.00.80.G24', '000.00.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2937680, 'Bảo hiểm xã hội thành phố Tây Ninh, tỉnh Tây Ninh', 'Tây Ninh', '000.01.80.G24', '000.01.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2937694, 'Bảo hiểm xã hội huyện Tân Biên tỉnh Tây Ninh', 'Tây Ninh', '000.02.80.G24', '000.02.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2956008, 'Bảo hiểm xã hội huyện Tân Châu tỉnh Tây Ninh ', 'Tây Ninh', '000.03.80.G24', '000.03.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2956022, 'Bảo hiểm xã hội huyện Dương Minh Châu tỉnh Tây Ninh ', 'Tây Ninh', '000.05.80.G24', '000.05.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2956036, 'Bảo hiểm xã hội huyện Châu Thành tỉnh Tây Ninh', 'Tây Ninh', '000.06.80.G24', '000.06.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2956050, 'Bảo hiểm xã hội huyện Hòa Thành tỉnh Tây Ninh', 'Tây Ninh', '000.07.80.G24', '000.07.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2956064, 'Bảo hiểm xã hội huyện Gò Dầu tỉnh Tây Ninh', 'Tây Ninh', '000.08.80.G24', '000.08.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2956078, 'Bảo hiểm xã hội huyện Bến Cầu tỉnh Tây Ninh ', 'Tây Ninh', '000.09.80.G24', '000.09.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (2956092, 'Bảo hiểm xã hội huyện Trảng Bàng tỉnh Tây Ninh ', 'Tây Ninh', '000.10.80.G24', '000.10.80.G24@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3189541, 'Cục An toàn thông tin - Bộ Thông tin và Truyền thông', 'VN', '000.00.28.G14', '000.00.28.G14@bmail.vn', 'VN', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272269, 'Văn phòng Bộ Thông tin truyền thông', 'Việt Nam', '000.00.01.G14', '000.00.01.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272283, 'Thanh tra Bộ Thông tin truyền thông', 'Việt Nam', '000.00.02.G14', '000.00.02.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272297, 'Vụ Bưu chính - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.03.G14', '000.00.03.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272611, 'Vụ Công nghệ thông tin - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.04.G14', '000.00.04.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272625, 'Vụ Khoa học và Công nghệ - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.05.G14', '000.00.05.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272639, 'Vụ Kế hoạch - Tài chính - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.06.G14', '000.00.06.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272653, 'Vụ Quản lý doanh nghiệp - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.07.G14', '000.00.07.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272667, 'Vụ Hợp tác quốc tế - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.08.G14', '000.00.08.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272681, 'Vụ Pháp chế - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.09.G14', '000.00.09.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272695, 'Vụ Thi đua - Khen thưởng - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.10.G14', '000.00.10.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272709, 'Vụ Tổ chức cán bộ - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.11.G14', '000.00.11.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272723, 'Cục Báo chí - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.20.G14', '000.00.20.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272737, 'Cục phát thanh truyền hình và thông tin điện tử - Bộ TTTT', 'Việt Nam', '000.00.21.G14', '000.00.21.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272751, 'Cục xuất bản, In và Phát hành - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.22.G14', '000.00.22.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272765, 'Cục Thông tin cơ sở - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.23.G14', '000.00.23.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272779, 'Cục Thông tin đối ngoại - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.24.G14', '000.00.24.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272793, 'Cục Viễn thông - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.25.G14', '000.00.25.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272807, 'Cục Tần số vô tuyến điện - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.26.G14', '000.00.26.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272821, 'Cục Bưu điện Trung ương - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.29.G14', '000.00.29.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272835, 'Viện Chiến lược Thông tin và Truyền thông - Bộ TTTT', 'Việt Nam', '000.00.40.G14', '000.00.40.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272849, 'Trung tâm Thông tin - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.41.G14', '000.00.41.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272863, 'Báo Bưu điện Việt Nam - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.42.G14', '000.00.42.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272877, 'Báo điện tử VietnamNet - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.43.G14', '000.00.43.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272891, 'Tạp chí Thông tin và Truyền thông - Bộ Thông tin truyền thông', 'Việt Nam', '000.00.44.G14', '000.00.44.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272905, 'Học viện Công nghệ bưu chính, viễn thông', 'Việt Nam', '000.00.45.G14', '000.00.45.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272919, 'Trường Đào tạo, Bồi dưỡng cán bộ quản lý Thông tin và Truyền thông', 'Việt Nam', '000.00.46.G14', '000.00.46.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272933, 'Trung tâm Internet Việt Nam (VNNIC)', 'Việt Nam', '000.00.47.G14', '000.00.47.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272947, 'Viện Công nghiệp phần mềm và nội dung số Việt Nam', 'Việt Nam', '000.00.48.G14', '000.00.48.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272961, 'Nhà Xuất bản Thông tin và Truyền thông', 'Việt Nam', '000.00.49.G14', '000.00.49.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272975, 'Quỹ dịch vụ Viễn thông công ích Việt Nam', 'Việt Nam', '000.00.50.G14', '000.00.50.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3272989, 'Trung tâm Ứng cứu khẩn cấp máy tính Việt Nam', 'Việt Nam', '000.00.51.G14', '000.00.51.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3273003, 'Trường Cao đẳng CNTT Hữu nghị Việt - Hàn', 'Việt Nam', '000.00.52.G14', '000.00.52.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3273017, 'Trường Cao đẳng Công nghiệp In', 'Việt Nam', '000.00.53.G14', '000.00.53.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3273031, 'Trung tâm Chứng thực điện tử quốc gia', 'Việt Nam', '000.00.54.G14', '000.00.54.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3273045, 'Ban Quản lý Chương trình cung cấp dịch vụ viễn thông công ích', 'Việt Nam', '000.00.55.G14', '000.00.55.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3273059, 'Tổng công ty Bưu điện Việt Nam', 'Việt Nam', '000.00.80.G14', '000.00.80.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3273073, 'Tổng công ty Viễn thông MibiFone', 'Việt Nam', '000.00.81.G14', '000.00.81.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3273087, 'Tổng công ty Truyền thông đa phương tiện - VTC', 'Việt Nam', '000.00.82.G14', '000.00.82.G14@bmail.vn', 'Việt Nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3385114, 'Công an tỉnh Tây Ninh', 'TN', '000.00.60.H53', '000.00.60.H53@bmail.vn', 'TN', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3385128, 'Hội văn học nghệ thuật tỉnh Tây Ninh', 'TN', '000.00.61.H53', '000.00.61.H53@bmail.vn', 'TN', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3663231, 'Bộ Tư Pháp - CQCP', 'VN', '000.00.00.G15', '000.00.00.G15@bmail.vn', 'VN', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3688037, 'Cục bưu điện trung ương - Bộ thông tin và truyền thông', 'Tây Ninh', '000.00.29.G14', '000.00.29.G14@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3688054, 'Cục Viễn thông - Bộ thông tin và truyền thông', 'Tây Ninh', '000.00.25.G14', '000.00.25.G14@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3688074, 'Trung tâm Giám sát an toàn không gian mạng - Cục An toàn thông tin', 'Việt nam', '000.01.28.G14', '000.01.28.G14@bmail.vn', 'Việt nam', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (3688100, 'Cục xuất bản, In và Phát hành - Bộ Thông tin và Truyền thông', 'Tây Ninh', '000.00.22.G14', '000.00.22.G14@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (4000714, 'Cục Thông tin cơ sở', 'Tây Ninh', '000.00.23.G14', '000.00.23.G14@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (4000728, 'Cục Báo chí', 'Tây Ninh', '000.00.20.G14', '000.00.20.G14@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);
INSERT INTO `edoc_dynamiccontact` VALUES (4000742, 'Cục Tần số vô tuyến điện', 'Tây Ninh', '000.00.26.G14', '000.00.26.G14@bmail.vn', 'Tây Ninh', '0975280222', '', '', NULL, NULL);

-- ----------------------------
-- Table structure for edoc_notification
-- ----------------------------
DROP TABLE IF EXISTS `edoc_notification`;
CREATE TABLE `edoc_notification`  (
  `notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `receiver_id` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `document_id` bigint(20) NULL DEFAULT NULL,
  `send_number` int(11) NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `modified_date` datetime(0) NULL DEFAULT NULL,
  `due_date` datetime(0) NULL DEFAULT NULL,
  `taken` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`notification_id`) USING BTREE,
  INDEX `organTaken`(`receiver_id`, `taken`) USING BTREE,
  INDEX `documentId`(`document_id`) USING BTREE,
  INDEX `organDocument`(`receiver_id`, `document_id`) USING BTREE,
  INDEX `documentTaken`(`document_id`, `taken`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2718 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_notification
-- ----------------------------
INSERT INTO `edoc_notification` VALUES (2690, '000.00.21.H53', 273, 0, '2020-08-14 17:36:09', '2020-08-14 17:36:09', NULL, 1);
INSERT INTO `edoc_notification` VALUES (2691, '000.00.13.H53', 274, 0, '2020-08-14 17:53:21', '2020-08-14 17:53:21', NULL, 1);
INSERT INTO `edoc_notification` VALUES (2692, '000.00.13.H53', 275, 0, '2020-08-14 18:01:18', '2020-08-14 18:01:18', NULL, 1);
INSERT INTO `edoc_notification` VALUES (2693, '000.00.21.H53', 276, 0, '2020-08-14 18:57:01', '2020-08-14 18:57:01', NULL, 1);
INSERT INTO `edoc_notification` VALUES (2694, '000.00.21.H53', 277, 0, '2020-08-14 19:06:01', '2020-08-14 19:06:01', NULL, 1);
INSERT INTO `edoc_notification` VALUES (2695, '000.00.13.H53', 278, 0, '2020-08-14 19:13:25', '2020-08-14 19:13:25', NULL, 1);
INSERT INTO `edoc_notification` VALUES (2696, '000.00.13.H53', 279, 0, '2020-08-15 09:06:49', '2020-08-15 09:06:49', '2020-08-15 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2697, '000.00.23.H53', 280, 0, '2020-08-15 11:27:30', '2020-08-15 11:27:30', NULL, 0);
INSERT INTO `edoc_notification` VALUES (2698, '000.00.21.H53', 281, 0, '2020-08-15 13:21:58', '2020-08-15 13:21:58', '2020-08-15 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2699, '000.00.01.H53', 282, 0, '2020-08-19 18:18:06', '2020-08-19 18:18:06', '2020-08-20 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2700, '000.01.01.H53', 283, 0, '2020-08-19 18:43:32', '2020-08-19 18:43:32', '2020-08-12 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2701, '000.00.01.H53', 284, 0, '2020-08-20 11:53:15', '2020-08-20 11:53:15', '2020-08-22 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2702, '000.02.01.H53', 284, 0, '2020-08-20 11:53:15', '2020-08-20 11:53:15', '2020-08-22 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2703, '000.12.31.H53', 284, 0, '2020-08-20 11:53:15', '2020-08-20 11:53:15', '2020-08-22 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2704, '000.28.31.H53', 285, 0, '2020-08-22 21:42:19', '2020-08-22 21:42:19', NULL, 0);
INSERT INTO `edoc_notification` VALUES (2705, '000.01.32.H53', 285, 0, '2020-08-22 21:42:19', '2020-08-24 00:22:20', NULL, 1);
INSERT INTO `edoc_notification` VALUES (2706, '000.01.33.H53', 285, 0, '2020-08-22 21:42:19', '2020-08-24 00:17:34', NULL, 1);
INSERT INTO `edoc_notification` VALUES (2707, '000.00.01.H53', 286, 0, '2020-08-22 21:48:06', '2020-08-24 00:07:51', '2020-08-22 00:00:00', 1);
INSERT INTO `edoc_notification` VALUES (2708, '000.10.32.H53', 287, 0, '2020-08-24 23:27:10', '2020-08-24 23:27:10', '2020-08-24 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2709, '000.00.01.H53', 288, 0, '2020-08-31 00:28:47', '2020-08-31 00:28:47', '2020-08-25 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2710, '000.20.31.H53', 288, 0, '2020-08-31 00:28:47', '2020-08-31 00:28:47', '2020-08-25 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2711, '000.00.01.H53', 289, 0, '2020-08-31 00:31:17', '2020-08-31 00:31:17', '2020-08-31 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2712, '000.02.01.H53', 290, 0, '2020-09-02 17:21:47', '2020-09-02 17:21:47', '2020-09-03 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2713, '000.00.13.H53', 291, 0, '2020-09-04 14:41:28', '2020-09-04 14:41:28', NULL, 0);
INSERT INTO `edoc_notification` VALUES (2714, '000.00.12.H53', 292, 0, '2020-09-04 14:47:45', '2020-09-04 14:47:45', '2020-09-19 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2715, '000.00.13.H53', 293, 0, '2020-09-04 14:52:28', '2020-09-04 14:52:28', '2020-09-11 00:00:00', 0);
INSERT INTO `edoc_notification` VALUES (2716, '000.00.13.H53', 294, 0, '2020-09-05 12:40:13', '2020-09-05 12:40:13', NULL, 0);
INSERT INTO `edoc_notification` VALUES (2717, '000.00.13.H53', 295, 0, '2020-09-05 12:42:16', '2020-09-05 12:42:16', '2020-09-19 00:00:00', 0);

-- ----------------------------
-- Table structure for edoc_priority
-- ----------------------------
DROP TABLE IF EXISTS `edoc_priority`;
CREATE TABLE `edoc_priority`  (
  `priority_id` int(20) NOT NULL AUTO_INCREMENT,
  `value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`priority_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_priority
-- ----------------------------
INSERT INTO `edoc_priority` VALUES (1, 'Khẩn');
INSERT INTO `edoc_priority` VALUES (2, 'Thượng khẩn');
INSERT INTO `edoc_priority` VALUES (3, 'Hỏa tốc');
INSERT INTO `edoc_priority` VALUES (4, 'Hỏa tốc hẹn giờ');
INSERT INTO `edoc_priority` VALUES (5, 'Thường');

-- ----------------------------
-- Table structure for edoc_trace
-- ----------------------------
DROP TABLE IF EXISTS `edoc_trace`;
CREATE TABLE `edoc_trace`  (
  `trace_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `document_id` bigint(20) NULL DEFAULT NULL,
  `time_stamp` datetime(0) NULL DEFAULT NULL,
  `server_time_stamp` datetime(0) NULL DEFAULT NULL,
  `organization_in_charge` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `organ_name` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `organ_add` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `telephone` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `from_organ_domain` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fax` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `website` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `to_organ_domain` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code_` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `staff_name` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `comment_` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `promulgation_date` datetime(0) NULL DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status_code` bigint(20) NULL DEFAULT NULL,
  `enable` tinyint(4) NULL DEFAULT NULL,
  `staff_email` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `staff_mobile` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edxml_document_id` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`trace_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_trace
-- ----------------------------
INSERT INTO `edoc_trace` VALUES (1, 280, '2020-08-20 17:37:45', '2020-08-20 17:37:45', 'test', 'test', NULL, '', NULL, '000.00.13.H53', NULL, NULL, '000.00.13.H53', '1295/STTTT-TTBCXB', 'Văn thư', 'Nguyễn Thị Kim Hà kết thúc văn bản vào lúc 12/08/2020 16:21:45: ', '2020-08-12 00:00:00', '', 2, 0, NULL, '', '000.00.13.H53,2020/08/20,1295/STTTT-TTBCXB');
INSERT INTO `edoc_trace` VALUES (2, 272, '2020-08-20 17:37:45', '2020-08-20 17:37:45', 'test', 'test', NULL, '', NULL, '000.00.13.H53', NULL, NULL, '000.00.13.H53', '1308/BC-STTTT_test', 'Văn thư', 'Nguyễn Thị Kim Hà kết thúc văn bản vào lúc 14/08/2020 15:34:26: ', '2020-08-14 00:00:00', '', 2, 0, NULL, '', '000.00.13.H53,2020/08/20,1308/BC-STTTT_test');
INSERT INTO `edoc_trace` VALUES (3, 280, '2020-08-20 17:38:45', '2020-08-20 17:37:45', 'test', 'test', NULL, '', NULL, '000.00.13.H53', NULL, NULL, '000.00.13.H53', '1295/STTTT-TTBCXB', 'Văn thư', 'Nguyễn Thị Kim Hà kết thúc văn bản vào lúc 12/08/2020 16:21:45: ', '2020-08-12 00:00:00', '', 2, 0, NULL, '', '000.00.13.H53,2020/08/20,1295/STTTT-TTBCXB');
INSERT INTO `edoc_trace` VALUES (4, 280, '2020-08-20 17:39:45', '2020-08-20 17:37:45', 'test', 'test', NULL, '', NULL, '000.00.13.H53', NULL, NULL, '000.00.13.H53', '1295/STTTT-TTBCXB', 'Văn thư', 'Nguyễn Thị Kim Hà kết thúc văn bản vào lúc 12/08/2020 16:21:45: ', '2020-08-12 00:00:00', '', 2, 0, NULL, '', '000.00.13.H53,2020/08/20,1295/STTTT-TTBCXB');
INSERT INTO `edoc_trace` VALUES (5, 280, '2020-08-20 17:40:45', '2020-08-20 17:37:45', 'test', 'test', NULL, '', NULL, '000.00.13.H53', NULL, NULL, '000.00.13.H53', '1295/STTTT-TTBCXB', 'Văn thư', 'Nguyễn Thị Kim Hà kết thúc văn bản vào lúc 12/08/2020 16:21:45: ', '2020-08-12 00:00:00', '', 2, 0, NULL, '', '000.00.13.H53,2020/08/20,1295/STTTT-TTBCXB');

-- ----------------------------
-- Table structure for edoc_trace_header
-- ----------------------------
DROP TABLE IF EXISTS `edoc_trace_header`;
CREATE TABLE `edoc_trace_header`  (
  `trace_header_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organ_domain` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time_stamp` datetime(0) NULL DEFAULT NULL,
  `document_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`trace_header_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 168 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_trace_header
-- ----------------------------
INSERT INTO `edoc_trace_header` VALUES (144, '000.00.13.H53', '0020-02-11 00:00:00', 272);
INSERT INTO `edoc_trace_header` VALUES (145, '000.00.13.H53', '2020-08-14 00:00:00', 273);
INSERT INTO `edoc_trace_header` VALUES (146, '000.00.13.H53', '0020-02-12 00:00:00', 274);
INSERT INTO `edoc_trace_header` VALUES (147, '000.00.13.H53', '2020-08-14 00:00:00', 275);
INSERT INTO `edoc_trace_header` VALUES (148, '000.00.13.H53', '2020-08-14 00:00:00', 276);
INSERT INTO `edoc_trace_header` VALUES (149, '000.00.13.H53', '2020-08-14 00:00:00', 277);
INSERT INTO `edoc_trace_header` VALUES (150, '000.00.13.H53', '2020-08-14 00:00:00', 278);
INSERT INTO `edoc_trace_header` VALUES (151, '000.00.13.H53', '2020-08-15 00:00:00', 279);
INSERT INTO `edoc_trace_header` VALUES (152, '000.00.13.H53', NULL, 280);
INSERT INTO `edoc_trace_header` VALUES (153, '000.00.13.H53', '2020-08-15 00:00:00', 281);
INSERT INTO `edoc_trace_header` VALUES (154, '000.00.13.H53', '2020-08-19 00:00:00', 282);
INSERT INTO `edoc_trace_header` VALUES (155, '000.00.13.H53', '2020-08-19 00:00:00', 283);
INSERT INTO `edoc_trace_header` VALUES (156, '000.00.13.H53', '2020-08-20 00:00:00', 284);
INSERT INTO `edoc_trace_header` VALUES (157, '000.00.13.H53', '2020-08-22 00:00:00', 285);
INSERT INTO `edoc_trace_header` VALUES (158, '000.00.13.H53', '2020-08-22 00:00:00', 286);
INSERT INTO `edoc_trace_header` VALUES (159, '000.00.13.H53', '2020-08-24 00:00:00', 287);
INSERT INTO `edoc_trace_header` VALUES (160, '000.00.13.H53', '2020-08-31 00:00:00', 288);
INSERT INTO `edoc_trace_header` VALUES (161, '000.00.13.H53', '2020-08-31 00:00:00', 289);
INSERT INTO `edoc_trace_header` VALUES (162, '000.00.13.H53', '2020-09-02 00:00:00', 290);
INSERT INTO `edoc_trace_header` VALUES (163, '000.00.12.H53', '2020-09-04 00:00:00', 291);
INSERT INTO `edoc_trace_header` VALUES (164, '000.00.13.H53', '2020-09-04 00:00:00', 292);
INSERT INTO `edoc_trace_header` VALUES (165, '000.00.13.H53', '2020-09-04 00:00:00', 293);
INSERT INTO `edoc_trace_header` VALUES (166, '000.00.13.H53', '2020-09-05 00:00:00', 294);
INSERT INTO `edoc_trace_header` VALUES (167, '000.00.12.H53', '2020-09-05 00:00:00', 295);

-- ----------------------------
-- Table structure for edoc_trace_header_list
-- ----------------------------
DROP TABLE IF EXISTS `edoc_trace_header_list`;
CREATE TABLE `edoc_trace_header_list`  (
  `document_id` bigint(20) NOT NULL,
  `business_doc_type` tinyint(4) NULL DEFAULT NULL,
  `business_doc_reason` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `paper` tinyint(4) NULL DEFAULT NULL,
  `department` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `staff` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mobile` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `business_info` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`document_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edoc_trace_header_list
-- ----------------------------
INSERT INTO `edoc_trace_header_list` VALUES (272, 0, 'Văn bản mới', 1, 'Sở 4T', 'Nguyễn thị kim hà', '0987654321', 'hantk@s4t.tayninh.gov.vn', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (273, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (274, 0, 'Văn bản mới', 1, 'Sở 4T', 'Nguyễn thị kim hà', '0987654321', 'hantk@s4t.tayninh.gov.vn', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (275, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (276, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (277, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (278, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (279, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (280, 0, 'Văn bản mới', 1, 'Sở 4T', 'Nguyễn thị kim hà', '0987654321', 'hantk@s4t.tayninh.gov.vn', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (281, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (282, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (283, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (284, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (285, 0, 'Văn bản điện tử mới', 0, '', 'Chu Văn Quang', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (286, 0, 'Văn bản điện tử mới', 0, '', 'Chu Văn Quang', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (287, 0, 'Văn bản điện tử mới', 0, '', 'Chu Văn Quang', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (288, 0, 'Văn bản điện tử mới', 0, '', 'Chu Văn Quang', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (289, 0, 'Văn bản điện tử mới', 0, '', 'Chu Văn Quang', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (290, 0, 'Văn bản điện tử mới', 0, '', 'Hoàng Văn Sơn', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (291, 0, 'Văn bản điện tử mới', 0, '', 'Chu Văn Quang', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (292, 0, 'Văn bản điện tử mới', 0, '', 'Bùi Sơn Điền', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (293, 0, 'Văn bản điện tử mới', 0, '', 'Hoàng Ngọc Khánh', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (294, 0, 'Văn bản điện tử mới', 0, '', 'Test', '', '', NULL);
INSERT INTO `edoc_trace_header_list` VALUES (295, 0, 'Văn bản điện tử mới', 0, '', 'Quang', '', '', NULL);

-- ----------------------------
-- Table structure for user_
-- ----------------------------
DROP TABLE IF EXISTS `user_`;
CREATE TABLE `user_`  (
  `userId` bigint(20) NOT NULL,
  `createDate` datetime(0) NULL DEFAULT NULL,
  `modifiedDate` datetime(0) NULL DEFAULT NULL,
  `password` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `emailAddress` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `firstName` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `middleName` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lastName` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fullName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `jobTitle` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `organize` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `loginDate` datetime(0) NULL DEFAULT NULL,
  `loginIP` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lastLoginDate` datetime(0) NULL DEFAULT NULL,
  `lastLoginIP` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`userId`) USING BTREE,
  UNIQUE INDEX `IX_9782AD88`(`userId`) USING BTREE,
  UNIQUE INDEX `IX_615E9F7A`(`emailAddress`) USING BTREE,
  UNIQUE INDEX `IX_C5806019`(`username`) USING BTREE,
  INDEX `IX_F6039434`(`status`) USING BTREE,
  INDEX `IX_762F63C6`(`emailAddress`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_
-- ----------------------------
INSERT INTO `user_` VALUES (2760094, '2019-06-25 15:18:54', '2019-06-25 15:18:54', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'vphdnd', 'hdnd@tayninh.gov.vn', 'VP', 'Hội đồng', 'nhân dân', 'VP Hội đồng nhân dân', '', NULL, '2019-06-28 10:45:13', '113.190.209.114', '2019-06-28 09:16:51', '113.190.209.114', 0);
INSERT INTO `user_` VALUES (2767108, '2019-06-25 15:21:50', '2019-06-25 15:21:50', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'vpubnd', 'vpubnd@tayninh.gov.vn', 'VP', 'UBND', 'tỉnh Tây Ninh', 'VP UBND tỉnh Tây Ninh', '', NULL, '2019-11-26 11:12:14', '10.184.46.254', '2019-11-15 09:51:21', '10.184.46.254', 0);
INSERT INTO `user_` VALUES (2767122, '2019-06-26 08:28:53', '2019-06-26 08:28:53', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'songoaivu', 'songv@tayninh.gov.vn', 'Sở', 'Ngoại', 'Vụ', 'Sở Ngoại Vụ', '', NULL, '2019-09-05 11:43:33', '123.16.145.132', '2019-06-26 08:29:18', '123.16.145.132', 0);
INSERT INTO `user_` VALUES (2767136, '2019-06-26 13:59:41', '2019-06-26 13:59:41', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'ubndchauthanh', 'chauthanh@tayninh.gov.vn', 'UBND huyện', 'Châu', 'Thành', 'UBND huyện Châu Thành', '', NULL, '2019-06-26 14:01:22', '14.177.167.235', '2019-06-26 14:01:22', '14.177.167.235', 0);
INSERT INTO `user_` VALUES (2767149, '2019-06-26 14:01:03', '2019-06-26 14:01:03', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'ubndhoathanh', 'hoathanh@tayninh.gov.vn', 'UBND huyện', 'Hòa', 'Thành', 'UBND huyện Hòa Thành', '', NULL, '2019-06-26 14:02:40', '14.177.144.6', '2019-06-26 14:02:40', '14.177.144.6', 0);
INSERT INTO `user_` VALUES (2776401, '2019-06-27 16:14:44', '2019-06-27 16:14:44', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'stttt', 'stttt@tayninh.gov.vn', 'Sở', 'Thông tin', 'Truyền thông', 'Sở Thông tin Truyền thông', '', NULL, '2019-11-15 15:39:32', '10.184.46.254', '2019-11-08 14:29:21', '10.184.46.254', 0);
INSERT INTO `user_` VALUES (2808812, '2019-07-09 17:13:04', '2019-07-09 17:13:04', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'qptd', 'qptd@tayninh.gov.vn', 'Quỹ', 'phát triển', 'đất', 'Quỹ phát triển đất', '', NULL, '2019-07-09 17:13:34', '14.177.249.15', '2019-07-09 17:13:34', '14.177.249.15', 0);
INSERT INTO `user_` VALUES (2808828, '2019-07-11 13:42:43', '2019-11-26 11:04:19', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'sotnmt', 'tnmt@tayninh.gov.vn', 'Sở', 'Tài nguyên', 'Môi trường', 'Sở Tài nguyên Môi trường', '', NULL, '2019-11-26 11:04:47', '10.184.46.254', '2019-09-05 11:54:51', '10.184.46.254', 0);
INSERT INTO `user_` VALUES (2808864, '2019-07-16 08:57:40', '2019-07-16 08:57:40', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'sgtvt', 'gtvt@tayninh.gov.vn', 'Sở', 'Giao thông', 'vận tải', 'Sở Giao thông vận tải', '', NULL, '2019-07-16 09:08:58', '14.177.167.188', '2019-07-16 08:58:02', '14.177.167.188', 0);
INSERT INTO `user_` VALUES (2808878, '2019-07-16 09:01:17', '2019-11-26 11:03:41', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'sokhdt', 'skhdt@tayninh.gov.vn', 'Sở', 'Kế hoạch', 'đầu tư', 'Sở Kế hoạch đầu tư', '', NULL, '2019-11-18 10:40:37', '10.184.46.254', '2019-11-15 14:20:25', '10.184.46.254', 0);
INSERT INTO `user_` VALUES (2853765, '2019-07-26 09:54:39', '2019-07-26 09:54:39', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'stp', 'stp@tayninh.gov.vn', 'Sở', 'Tư', 'Pháp', 'Sở Tư Pháp', '', NULL, '2019-07-26 09:55:05', '14.177.160.202', '2019-07-26 09:55:05', '14.177.160.202', 0);
INSERT INTO `user_` VALUES (2853785, '2019-08-01 10:28:55', '2019-11-26 11:09:46', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'sotc', 'stc@tayninh.gov.vn', 'Sở', 'Tài', 'Chính', 'Sở Tài Chính', '', NULL, '2019-11-26 11:09:55', '10.184.46.254', '2019-10-09 17:05:04', '10.184.46.254', 0);
INSERT INTO `user_` VALUES (2853799, '2019-08-01 10:31:56', '2019-11-26 11:02:36', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'sonoivu', 'snv@tayninh.gov.vn', 'Sở', 'Nội', 'Vụ', 'Sở Nội Vụ', '', NULL, '2019-11-26 11:08:01', '10.184.46.254', '2019-11-26 10:46:53', '10.184.46.254', 0);
INSERT INTO `user_` VALUES (2882013, '2019-08-01 10:34:21', '2019-08-01 10:34:21', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'bantdkt', 'bantdkt@tayninh.gov.vn', 'Ban', 'thi đua', 'khen thưởng', 'Ban thi đua khen thưởng', '', NULL, '2019-08-01 10:34:37', '14.177.160.202', '2019-08-01 10:34:37', '14.177.160.202', 0);
INSERT INTO `user_` VALUES (2882041, '2019-08-02 09:43:30', '2019-08-02 09:43:30', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'svhttdl', 'svhttdl@tayninh.gov.vn', 'Sở', 'Văn hóa', 'thể thao du lịch', 'Sở Văn hóa thể thao du lịch', '', NULL, '2019-09-05 11:50:52', '123.16.145.132', '2019-08-02 11:33:51', '123.16.145.132', 0);
INSERT INTO `user_` VALUES (2882069, '2019-08-16 10:31:27', '2019-08-16 10:31:27', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'thanhtra', 'ttt@tayninh.gov.vn', 'Thanh', 'tra', 'tỉnh', 'Thanh tra tỉnh', '', NULL, '2019-08-16 10:31:50', '14.177.160.202', '2019-08-16 10:31:50', '14.177.160.202', 0);
INSERT INTO `user_` VALUES (2882088, '2019-08-21 14:49:19', '2019-08-21 14:49:19', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'ubndtanchau', 'ubndtanchau@tn.gov.vn', 'UBND', 'Tân', 'Châu', 'UBND Tân Châu', '', NULL, '2019-08-21 15:14:16', '14.184.16.178', '2019-08-21 14:50:42', '14.184.16.178', 0);
INSERT INTO `user_` VALUES (2937604, '2019-08-23 14:47:36', '2019-08-23 14:47:36', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'thanhpho', 'tp@tayninh.gov.vn', 'Thành', 'phố', 'TN', 'Thành phố TN', '', NULL, '2019-08-23 14:48:02', '123.16.236.221', '2019-08-23 14:48:02', '123.16.236.221', 0);
INSERT INTO `user_` VALUES (2956100, '2019-09-04 13:37:25', '2019-09-04 13:37:25', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'bhxhtn', 'bhxh@tayninh.gov.vn', 'Bảo hiểm xã hội', 'tỉnh', 'Tây Ninh', 'Bảo hiểm xã hội tỉnh Tây Ninh', '', NULL, '2019-10-12 11:11:21', '123.16.236.221', '2019-09-09 15:36:52', '123.16.236.221', 0);
INSERT INTO `user_` VALUES (2973715, '2019-09-05 11:23:51', '2019-09-05 11:23:51', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'bqlnbd', 'bqlnbd@tayninh.gov.vn', 'Ban quản lý', 'khu du lịch', 'Núi bà đen', 'Ban quản lý khu du lịch Núi bà đen', '', NULL, '2019-09-05 11:52:48', '123.16.145.132', '2019-09-05 11:24:21', '123.16.145.132', 0);
INSERT INTO `user_` VALUES (2973731, '2019-09-07 09:13:33', '2019-09-07 09:13:33', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'btnmt', 'btnmt@chinhphu.vn', 'Bộ', 'Tài nguyên', 'môi trường', 'Bộ Tài nguyên môi trường', '', NULL, '2019-09-07 10:45:13', '113.190.209.233', '2019-09-07 09:26:07', '113.190.209.233', 0);
INSERT INTO `user_` VALUES (2973744, '2019-09-07 09:15:01', '2019-09-07 09:15:01', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'btttt', 'btttt@chinhphu.vn', 'Bộ', 'Thông tin', 'truyền thông', 'Bộ Thông tin truyền thông', '', NULL, '2019-11-13 14:59:49', '10.184.46.254', '2019-09-07 09:15:45', '10.184.46.254', 0);
INSERT INTO `user_` VALUES (2973758, '2019-09-07 09:24:15', '2019-09-07 09:24:15', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'bvhttdl', 'bvhttdl@chinhphu.vn', 'Bộ', 'Văn hóa', 'thể thao du lịch', 'Bộ Văn hóa thể thao du lịch', '', NULL, '2019-09-07 09:24:31', '123.16.236.221', '2019-09-07 09:24:31', '123.16.236.221', 0);
INSERT INTO `user_` VALUES (2973777, '2019-09-11 13:51:06', '2019-09-11 13:51:06', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'snnptnt', 'snnptnt@tayninh.gov.vn', 'Sở Nông nghiệp', 'phát triển', 'nông thôn', 'Sở Nông nghiệp phát triển nông thôn', '', NULL, '2019-09-11 14:49:46', '14.162.193.255', '2019-09-11 13:51:25', '14.162.193.255', 0);
INSERT INTO `user_` VALUES (2973792, '2019-09-12 09:20:14', '2019-09-12 09:20:14', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'ubndgodau', 'ubndgd@tayninh.gov.vn', 'UBND huyện', 'Gò', 'Dầu', 'UBND huyện Gò Dầu', '', NULL, NULL, '', NULL, '', 0);
INSERT INTO `user_` VALUES (2999105, '2019-09-12 09:47:27', '2019-09-12 09:47:27', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'skhcn', 'khcn@tayninh.gov.vn', 'Sở Khoa', 'học', 'công nghệ', 'Sở Khoa học công nghệ', '', NULL, '2019-09-12 09:47:39', '123.16.236.221', '2019-09-12 09:47:39', '123.16.236.221', 0);
INSERT INTO `user_` VALUES (2999120, '2019-09-13 15:28:27', '2019-09-13 15:28:27', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'ubndtrb', 'vphdndubnd_trb@tayninh.gov.vn', 'UBND', 'huyện', 'Trảng Bàng', 'UBND huyện Trảng Bàng', '', NULL, '2019-09-13 15:35:49', '10.184.208.254', '2019-09-13 15:28:54', '10.184.208.254', 0);
INSERT INTO `user_` VALUES (2999139, '2019-09-18 15:31:09', '2019-09-18 15:31:09', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'sldtbxh', 'ldtbxh@tayninh.gov.vn', 'Sở Lao động thương binh', 'xã', 'hội', 'Sở Lao động thương binh xã hội', '', NULL, '2019-09-18 15:31:23', '123.16.236.221', '2019-09-18 15:31:23', '123.16.236.221', 0);
INSERT INTO `user_` VALUES (3273093, '2019-11-13 15:07:16', '2019-11-13 15:07:16', 'sNfXNqOfAyECZDLfPvSEJAORGew=', 'sogddt', 'gddt@tn.gov.vn', 'Sở', 'Giáo dục', 'đào tạo', 'Sở Giáo dục đào tạo', '', NULL, '2019-11-13 15:07:47', '10.184.46.254', '2019-11-13 15:07:47', '10.184.46.254', 0);

SET FOREIGN_KEY_CHECKS = 1;
