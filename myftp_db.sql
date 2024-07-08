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
INSERT INTO `directories` VALUES ('12261f5e-7ebb-40cc-a483-feb213cd0be5','d3ce885b-8eba-4dc0-af6c-376b7d8f4973','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/abc test/asd asd/ongnam/vivu','vivu','2024-07-08T22:10:32.433197138'),('486ba122-a249-433e-b181-25d6e0604ecd','d4b59cf7-8012-4fda-aa07-2e54ef371b58','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa','tnqa','2024-07-08T05:02:48.688643866'),('a018cf9c-ac08-4b1b-a8f5-1f4e68a41bac','d3ce885b-8eba-4dc0-af6c-376b7d8f4973','/home/wen/Desktop/network programming/FiltraServer/upload/ongnam/vivu','vivu','2024-07-08T22:12:41.451587354'),('b1547a4f-cedf-4b11-bdf5-17e0cf452e48','d4b59cf7-8012-4fda-aa07-2e54ef371b58','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/abc test/asd asd','asd asd','2024-07-08T17:29:49.004222477'),('b454df08-5cda-4b39-8bfb-750fd04d8f86','d4b59cf7-8012-4fda-aa07-2e54ef371b58','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/abc test','abc test','2024-07-08T17:29:20.541417217'),('ebcc9a31-e584-426c-9806-474483751b2a','d3ce885b-8eba-4dc0-af6c-376b7d8f4973','/home/wen/Desktop/network programming/FiltraServer/upload/ongnam','ongnam','2024-07-08T17:27:25.828335609'),('f889a4bf-bc5f-4a08-8b96-98bb520181f5','d3ce885b-8eba-4dc0-af6c-376b7d8f4973','/home/wen/Desktop/network programming/FiltraServer/upload/ongnam/vivu/insidehaha','insidehaha','2024-07-08T22:14:56.556724028');
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
INSERT INTO `files` VALUES ('12cd2b68-c5fb-4db1-9b02-ab9b72f8947d','d4b59cf7-8012-4fda-aa07-2e54ef371b58','kali(6).iso','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/kali(6).iso','unknown','2024-07-09T00:58:55.371052886',4313143296),('254dd715-67dd-423e-a041-04990c561127','d4b59cf7-8012-4fda-aa07-2e54ef371b58','asd.txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/asd.txt','unknown','2024-07-08T21:26:06.310365996',15),('25a6eca8-287a-4d3a-abc7-1ec11c74216e','d3ce885b-8eba-4dc0-af6c-376b7d8f4973','kali.iso','/home/wen/Desktop/network programming/FiltraServer/upload/ongnam/kali.iso','unknown','2024-07-09T00:37:40.314826892',1011556352),('38bc66d9-0f7f-40e7-b3b7-748f1d07f63d','d4b59cf7-8012-4fda-aa07-2e54ef371b58','kali(1).iso','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/kali(1).iso','unknown','2024-07-09T00:14:18.155327980',4313143296),('38ec507d-b443-48ef-b30a-83fbe9db9c74','d4b59cf7-8012-4fda-aa07-2e54ef371b58','test(3).txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/test(3).txt','unknown','2024-07-09T00:26:38.145926098',10),('3d17f88f-66fa-4623-b2c9-36a0c160121e','d4b59cf7-8012-4fda-aa07-2e54ef371b58','abc.txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/abc.txt','unknown type','2024-07-08T05:08:07.972808414',16),('44cafb98-b699-450d-ae7c-626545c07a77','d3ce885b-8eba-4dc0-af6c-376b7d8f4973','kali.iso','/home/wen/Desktop/network programming/FiltraServer/upload/ongnam/kali.iso','unknown','2024-07-09T00:58:55.395946069',4313143296),('494f1be1-3391-4993-8e7b-a261441a86e5','d4b59cf7-8012-4fda-aa07-2e54ef371b58','test.txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/abc test/test.txt','unknown','2024-07-08T22:34:42.450618290',10),('4e553a64-feb7-47ae-ba7a-10583195f7ed','d3ce885b-8eba-4dc0-af6c-376b7d8f4973','asd.txt','/home/wen/Desktop/network programming/FiltraServer/upload/ongnam/vivu/insidehaha/asd.txt','unknown','2024-07-08T22:15:39.802282196',15),('52065f3e-e5b6-41d3-9642-b0ca1f0f77d2','d3ce885b-8eba-4dc0-af6c-376b7d8f4973','kali(1).iso','/home/wen/Desktop/network programming/FiltraServer/upload/ongnam/kali(1).iso','unknown','2024-07-09T00:46:01.764557747',561291264),('5dcdb547-1717-42d1-9162-94e510858b64','d4b59cf7-8012-4fda-aa07-2e54ef371b58','test.txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/test.txt','unknown','2024-07-08T22:31:22.946115931',10),('5e99ded6-fdfb-42c7-ab9c-f2902b5f7dee','d4b59cf7-8012-4fda-aa07-2e54ef371b58','asd(2).txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/asd(2).txt','unknown','2024-07-08T22:02:51.605782870',15),('6ce760c4-2c16-4fee-83bb-d0fedd783d46','d4b59cf7-8012-4fda-aa07-2e54ef371b58','kali.iso','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/kali.iso','unknown','2024-07-08T23:12:41.003907075',4313143296),('6dd874b9-6fbc-4e92-a489-ced5cfd0356d','d4b59cf7-8012-4fda-aa07-2e54ef371b58','test(1).txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/abc test/test(1).txt','unknown','2024-07-08T22:35:25.615770874',10),('73924f6c-625c-407f-92aa-a3b2b55b468e','d4b59cf7-8012-4fda-aa07-2e54ef371b58','kali(3).iso','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/kali(3).iso','unknown','2024-07-09T00:25:43.831368563',4313143296),('90511f6d-f11c-41b9-af2d-bb18eee27b4b','d4b59cf7-8012-4fda-aa07-2e54ef371b58','kali(2).iso','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/kali(2).iso','unknown','2024-07-09T00:16:35.235015870',4313143296),('a4cc1ad7-3e6e-4e17-aeb3-64d7d8f3c473','d4b59cf7-8012-4fda-aa07-2e54ef371b58','abc(2).txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/abc(2).txt','unknown type','2024-07-08T17:30:57.527440539',16),('b0f41aa7-5fbb-40a9-8267-85399323580e','d4b59cf7-8012-4fda-aa07-2e54ef371b58','asd(3).txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/asd(3).txt','unknown','2024-07-08T22:05:40.028618136',15),('b1c17b3d-7ca4-49c4-bb36-fcdf0ba18b42','d4b59cf7-8012-4fda-aa07-2e54ef371b58','abc(1).txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/abc(1).txt','unknown type','2024-07-08T05:08:14.422147384',16),('b4169d03-4531-47bc-81fa-b31f2c9b7e3c','d4b59cf7-8012-4fda-aa07-2e54ef371b58','kali(5).iso','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/kali(5).iso','unknown','2024-07-09T00:35:26.433963388',4313143296),('d1bdb26f-e732-4cfe-82c8-864214b83680','d4b59cf7-8012-4fda-aa07-2e54ef371b58','kali(4).iso','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/kali(4).iso','unknown','2024-07-09T00:29:15.730621636',4313143296),('d49efa05-d884-49a9-a000-912fcbe4ec5a','d4b59cf7-8012-4fda-aa07-2e54ef371b58','test(2).txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/test(2).txt','unknown','2024-07-08T22:37:20.336882042',10),('f7336082-67bc-4f8e-9949-f10ff61c0224','d4b59cf7-8012-4fda-aa07-2e54ef371b58','test(1).txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/test(1).txt','unknown','2024-07-08T22:32:44.525371819',10),('fb1721cd-f81c-4b0a-9d75-d348416591bd','d4b59cf7-8012-4fda-aa07-2e54ef371b58','asd(1).txt','/home/wen/Desktop/network programming/FiltraServer/upload/tnqa/asd(1).txt','unknown','2024-07-08T21:26:10.857461834',15);
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
  `id_permission` int NOT NULL AUTO_INCREMENT,
  `id_file` int NOT NULL,
  `id_directory` int NOT NULL,
  `id_user` varchar(45) NOT NULL,
  `read` tinyint(1) NOT NULL,
  `write` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id_role` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_role`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'admin'),(2,'client');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
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
  `id_role` tinyint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('d3ce885b-8eba-4dc0-af6c-376b7d8f4973','ongnam','asd','asdasd','Ong Nam','2024-07-08 17:27:26',1,0,2),('d4b59cf7-8012-4fda-aa07-2e54ef371b58','tnqa','asd','quocanh010104@gmail.com','Tong Nguyen Quoc Anh','2024-07-08 05:02:49',1,1,2);
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

-- Dump completed on 2024-07-09  1:06:18
