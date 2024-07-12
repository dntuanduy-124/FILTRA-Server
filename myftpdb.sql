-- MySQL dump 10.13  Distrib 8.0.37, for Linux (x86_64)
--
-- Host: localhost    Database: myftpdb
-- ------------------------------------------------------
-- Server version	8.0.37-0ubuntu0.22.04.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `block_users`
--

DROP TABLE IF EXISTS `block_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `block_users` (
  `id` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `block_users`
--

LOCK TABLES `block_users` WRITE;
/*!40000 ALTER TABLE `block_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `block_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `directories`
--

DROP TABLE IF EXISTS `directories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `directories` (
  `id_directory` varchar(45) NOT NULL,
  `id_user` varchar(45) NOT NULL,
  `path_directory` varchar(255) NOT NULL,
  `name_directory` varchar(45) NOT NULL,
  `created_date` varchar(45) NOT NULL,
  PRIMARY KEY (`id_directory`,`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `directories`
--

LOCK TABLES `directories` WRITE;
/*!40000 ALTER TABLE `directories` DISABLE KEYS */;
INSERT INTO `directories` VALUES ('73b7d53a-33f1-4d71-aaff-56c104797e38','d3ce885b-8eba-4dc0-af6c-376b7d8f4973','/home/wen/Desktop/network programming/FiltraServer/upload/ongnam','ongnam','2024-07-11T20:45:22.929183821'),('dfca2c48-afa4-4ac9-8577-663c1546e1ad','41ac22c0-6db0-4994-973c-179bb606790e','/home/wen/Desktop/network programming/FiltraServer/upload/alex','alex','2024-07-11T20:46:35.691407488'),('e175cf64-ea6a-4f21-8c84-345ce01d0bb3','41ac22c0-6db0-4994-973c-179bb606790e','/home/wen/Desktop/network programming/FiltraServer/upload/alex/alexdirhaha','alexdirhaha','2024-07-11T20:46:47.739005902'),('fcfccd9b-4237-4abf-ab08-8a9d0d5385f2','d4b59cf7-8012-4fda-aa07-2e54ef371b58','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa','tnqa','2024-07-11T20:44:41.811095236');
/*!40000 ALTER TABLE `directories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `files` (
  `id_file` varchar(45) NOT NULL,
  `id_user_upload` varchar(45) NOT NULL,
  `filename` varchar(45) NOT NULL,
  `filepath` varchar(255) NOT NULL,
  `filetype` varchar(45) NOT NULL,
  `upload_date` varchar(45) NOT NULL,
  `filesize` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id_file`,`id_user_upload`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
INSERT INTO `files` VALUES ('4490f530-f880-4c61-88f0-d3513b506bbf','d4b59cf7-8012-4fda-aa07-2e54ef371b58','kali.iso','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/kali.iso','unknown','2024-07-11T20:45:13.292708769',4313143296),('a2ce3dd7-b2ea-4eeb-995a-cb1c776d6892','41ac22c0-6db0-4994-973c-179bb606790e','test.txt','/home/wen/Desktop/network programming/FiltraServer/upload/alex/alexdirhaha/test.txt','unknown','2024-07-11T20:51:21.535363499',10);
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id_notify` int NOT NULL AUTO_INCREMENT,
  `id_user_share` int NOT NULL,
  `id_user_receive` int NOT NULL,
  `id_directory` int NOT NULL,
  `id_file` int NOT NULL,
  `message` varchar(255) NOT NULL,
  PRIMARY KEY (`id_notify`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `id_permission` varchar(45) NOT NULL,
  `id_file` varchar(45) DEFAULT NULL,
  `id_directory` varchar(45) DEFAULT NULL,
  `id_user` varchar(45) NOT NULL,
  `isRead` tinyint(1) NOT NULL,
  `isWrite` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES ('63637c46-2d6e-411d-9686-8515cb1de257','4490f530-f880-4c61-88f0-d3513b506bbf',NULL,'d3ce885b-8eba-4dc0-af6c-376b7d8f4973',1,1),('a5e1167a-6a3d-402b-be61-f907f2f535fc',NULL,'e175cf64-ea6a-4f21-8c84-345ce01d0bb3','d3ce885b-8eba-4dc0-af6c-376b7d8f4973',0,1);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `fullname` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `date_created` datetime NOT NULL COMMENT '	',
  `anonymous` tinyint NOT NULL,
  `activated` tinyint NOT NULL,
  `max_size` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('41ac22c0-6db0-4994-973c-179bb606790e','alex','asd','alex@gmail.com','Alexander Grahambell','2024-07-11 17:29:23',1,0,524288000),('c871e9e7-7cbb-413c-bb93-a335fe9f7da5','aladin','asd','aladin@gmail.com','Aladin Balaka','2024-07-10 00:03:39',1,0,5368709120),('d3ce885b-8eba-4dc0-af6c-376b7d8f4973','ongnam','asd','ongnam@gmail.com','Ong Nam','2024-07-08 17:27:26',1,0,5368709120),('d4b59cf7-8012-4fda-aa07-2e54ef371b58','tnqa','asd','quocanh010104@gmail.com','Tong Nguyen Quoc Anh','2024-07-08 05:02:49',1,1,5368709120),('f31dec56-7631-40d6-864d-dc0b871646b6','bigdick','123','jkdhaskjshkjdhkj!@dkjahkjdahskjh.com','bigdick','2024-07-11 20:01:21',1,0,5368709120);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-12 22:40:32
