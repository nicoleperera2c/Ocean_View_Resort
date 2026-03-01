CREATE DATABASE  IF NOT EXISTS `oceanview_resort` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `oceanview_resort`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: oceanview_resort
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `action` varchar(100) NOT NULL,
  `table_name` varchar(50) DEFAULT NULL,
  `record_id` int DEFAULT NULL,
  `old_value` text,
  `new_value` text,
  `description` text,
  `user_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES (1,'RESERVATION_CREATED','reservations',10,NULL,'CONFIRMED','Reservation RES202602280001 created',16,'2026-02-28 08:51:44'),(2,'NOTIFICATION_BOOKING_CONFIRMATION','reservations',0,'no-email@placeholder.local','Booking Confirmation - RES202602280001','To: no-email@placeholder.local | Subject: Booking Confirmation - RES202602280001 | Reservation: RES202602280001',0,'2026-02-28 08:51:44'),(3,'CHECK_IN','reservations',10,'CONFIRMED','CHECKED_IN','Guest checked in for reservation RES202602280001',16,'2026-02-28 21:42:14'),(4,'NOTIFICATION_CHECK_IN_WELCOME','reservations',0,'Yeager44@gmail.com','Welcome to Ocean View Resort - Check-in Confirmed','To: Yeager44@gmail.com | Subject: Welcome to Ocean View Resort - Check-in Confirmed | Reservation: RES202602280001',0,'2026-02-28 21:42:14'),(5,'CHECK_OUT','reservations',10,'CHECKED_IN','CHECKED_OUT','Guest checked out for reservation RES202602280001',16,'2026-02-28 21:42:28'),(6,'NOTIFICATION_CHECK_OUT_THANKS','reservations',0,'Yeager44@gmail.com','Thank You for Staying - Check-out Complete','To: Yeager44@gmail.com | Subject: Thank You for Staying - Check-out Complete | Reservation: RES202602280001',0,'2026-02-28 21:42:28'),(7,'RESERVATION_CREATED','reservations',11,NULL,'CONFIRMED','Reservation RES202603010001 created',1,'2026-02-28 21:44:06'),(8,'NOTIFICATION_BOOKING_CONFIRMATION','reservations',0,'no-email@placeholder.local','Booking Confirmation - RES202603010001','To: no-email@placeholder.local | Subject: Booking Confirmation - RES202603010001 | Reservation: RES202603010001',0,'2026-02-28 21:44:06'),(9,'CHECK_IN','reservations',11,'CONFIRMED','CHECKED_IN','Guest checked in for reservation RES202603010001',1,'2026-02-28 21:44:16'),(10,'NOTIFICATION_CHECK_IN_WELCOME','reservations',0,'Yeager44@gmail.com','Welcome to Ocean View Resort - Check-in Confirmed','To: Yeager44@gmail.com | Subject: Welcome to Ocean View Resort - Check-in Confirmed | Reservation: RES202603010001',0,'2026-02-28 21:44:16');
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `available_rooms_view`
--

DROP TABLE IF EXISTS `available_rooms_view`;
/*!50001 DROP VIEW IF EXISTS `available_rooms_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `available_rooms_view` AS SELECT 
 1 AS `room_id`,
 1 AS `room_number`,
 1 AS `floor`,
 1 AS `type_name`,
 1 AS `capacity`,
 1 AS `price_per_night`,
 1 AS `status`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `current_occupancy_view`
--

DROP TABLE IF EXISTS `current_occupancy_view`;
/*!50001 DROP VIEW IF EXISTS `current_occupancy_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `current_occupancy_view` AS SELECT 
 1 AS `reservation_id`,
 1 AS `reservation_number`,
 1 AS `first_name`,
 1 AS `last_name`,
 1 AS `room_number`,
 1 AS `type_name`,
 1 AS `check_in_date`,
 1 AS `check_out_date`,
 1 AS `status`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `guests`
--

DROP TABLE IF EXISTS `guests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guests` (
  `guest_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `id_type` varchar(30) DEFAULT NULL,
  `id_number` varchar(50) DEFAULT NULL,
  `registered_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`guest_id`),
  UNIQUE KEY `phone` (`phone`),
  KEY `idx_guest_phone` (`phone`),
  KEY `idx_guest_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guests`
--

LOCK TABLES `guests` WRITE;
/*!40000 ALTER TABLE `guests` DISABLE KEYS */;
INSERT INTO `guests` VALUES (1,'John','Smith','5551234567','john.smith@email.com','123 Main St','New York','USA','Passport','P12345678','2026-02-16 17:56:07'),(2,'Emma','Johnson','5559876543','emma.j@email.com','456 Oak Ave','London','UK','Passport','P87654321','2026-02-16 17:56:07'),(3,'Michael','Brown','5555551234','mbrown@email.com','789 Elm St','Sydney','Australia','Driver License','DL456789','2026-02-16 17:56:07'),(4,'Sarah','Davis','5554443333','sarah.d@email.com','321 Pine Rd','Toronto','Canada','Passport','P11223344','2026-02-16 17:56:07'),(5,'David','Wilson','5552223333','dwilson@email.com','654 Maple Dr','Dublin','Ireland','National ID','ID998877','2026-02-16 17:56:07'),(6,'Nicole','Perera','0771234567','nicoleperera@gmail.com','Colombo','Colombo','Sri Lanka','NIC','200111111112','2026-02-24 07:32:29'),(7,'Jane','Doe','077518119','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-02-27 00:01:58'),(8,'John','Doe','077518165','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-02-27 00:01:58'),(9,'John','Doe','077518195','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-02-27 00:01:58'),(10,'John','Doe','077518223','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-02-27 00:01:58'),(11,'John','Doe','077518257','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-02-27 00:01:58'),(12,'John','Doe','077518288','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-02-27 00:01:58'),(13,'John','Doe','077518317','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-02-27 00:01:58'),(14,'John','Doe','077518381','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-02-27 00:01:58'),(15,'John','Doe','077518434','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-02-27 00:01:58'),(16,'Eren','Yeager','0711236548','Yeager44@gmail.com','Wijerama, Nugegoda','Nugegoda','Sri Lanka','NIC','200432326598','2026-02-28 08:50:16'),(17,'Jane','Doe','0773375895','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:02:55'),(18,'John','Doe','0773375946','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:02:55'),(19,'John','Doe','0773375977','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:02:55'),(20,'John','Doe','0773375993','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:02:56'),(21,'John','Doe','0773376025','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:02:56'),(22,'John','Doe','0773376056','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:02:56'),(23,'John','Doe','0773376087','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:02:56'),(24,'John','Doe','0773376135','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:02:56'),(25,'John','Doe','0773376165','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:02:56'),(26,'Jane','Doe','0773470611','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:04:30'),(27,'John','Doe','0773470667','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:04:30'),(28,'John','Doe','0773470706','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:04:30'),(29,'John','Doe','0773470723','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:04:30'),(30,'John','Doe','0773470753','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:04:30'),(31,'John','Doe','0773470781','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:04:30'),(32,'John','Doe','0773470814','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:04:30'),(33,'John','Doe','0773470854','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:04:30'),(34,'John','Doe','0773470884','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:04:30'),(35,'Jane','Doe','0773564575','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:06:04'),(36,'John','Doe','0773564621','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:06:04'),(37,'John','Doe','0773564653','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:06:04'),(38,'John','Doe','0773564668','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:06:04'),(39,'John','Doe','0773564697','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:06:04'),(40,'John','Doe','0773564726','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:06:04'),(41,'John','Doe','0773564754','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:06:04'),(42,'John','Doe','0773564794','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:06:04'),(43,'John','Doe','0773564824','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:06:04'),(44,'Jane','Doe','0773629305','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:07:09'),(45,'John','Doe','0773629350','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:07:09'),(46,'John','Doe','0773629383','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:07:09'),(47,'John','Doe','0773629398','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:07:09'),(48,'John','Doe','0773629430','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:07:09'),(49,'John','Doe','0773629458','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:07:09'),(50,'John','Doe','0773629487','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:07:09'),(51,'John','Doe','0773629525','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:07:09'),(52,'John','Doe','0773629551','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:07:09'),(53,'Jane','Doe','0773726826','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:08:46'),(54,'John','Doe','0773726871','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:08:46'),(55,'John','Doe','0773726909','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:08:46'),(56,'John','Doe','0773726925','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:08:46'),(57,'John','Doe','0773726960','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:08:46'),(58,'John','Doe','0773726987','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:08:47'),(59,'John','Doe','0773727016','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:08:47'),(60,'John','Doe','0773727054','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:08:47'),(61,'John','Doe','0773727080','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:08:47'),(62,'Jane','Doe','0773766683','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:09:27'),(63,'John','Doe','0773767184','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:09:27'),(64,'John','Doe','0773767224','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:09:27'),(65,'John','Doe','0773767242','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:09:27'),(66,'John','Doe','0773767280','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:09:27'),(67,'John','Doe','0773767310','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:09:27'),(68,'John','Doe','0773767340','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:09:27'),(69,'John','Doe','0773767385','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:09:27'),(70,'John','Doe','0773767417','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:09:27'),(71,'Jane','Doe','0773878507','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:11:18'),(72,'John','Doe','0773879018','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:11:19'),(73,'John','Doe','0773879059','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:11:19'),(74,'John','Doe','0773879077','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:11:19'),(75,'John','Doe','0773879116','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:11:19'),(76,'John','Doe','0773879147','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:11:19'),(77,'John','Doe','0773879178','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:11:19'),(78,'John','Doe','0773879223','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:11:19'),(79,'John','Doe','0773879253','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 00:11:19'),(80,'Jane','Doe','0778728519','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 01:32:08'),(81,'John','Doe','0778729006','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 01:32:09'),(82,'John','Doe','0778729047','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 01:32:09'),(83,'John','Doe','0778729066','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 01:32:09'),(84,'John','Doe','0778729106','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 01:32:09'),(85,'John','Doe','0778729136','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 01:32:09'),(86,'John','Doe','0778729167','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 01:32:09'),(87,'John','Doe','0778729215','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 01:32:09'),(88,'John','Doe','0778729244','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 01:32:09'),(89,'Jane','Doe','0772088251','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:01:28'),(90,'John','Doe','0772088751','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:01:28'),(91,'John','Doe','0772088794','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:01:28'),(92,'John','Doe','0772088814','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:01:28'),(93,'John','Doe','0772088856','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:01:28'),(94,'John','Doe','0772088886','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:01:28'),(95,'John','Doe','0772088916','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:01:28'),(96,'John','Doe','0772088960','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:01:28'),(97,'John','Doe','0772088990','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:01:29'),(98,'Jane','Doe','0773145923','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:19:06'),(99,'John','Doe','0773146487','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:19:06'),(100,'John','Doe','0773146535','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:19:06'),(101,'John','Doe','0773146556','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:19:06'),(102,'John','Doe','0773146598','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:19:06'),(103,'John','Doe','0773146631','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:19:06'),(104,'John','Doe','0773146663','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:19:06'),(105,'John','Doe','0773146707','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:19:06'),(106,'John','Doe','0773146736','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:19:06'),(107,'Jane','Doe','0773783733','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:29:44'),(108,'John','Doe','0773784198','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:29:44'),(109,'John','Doe','0773784240','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:29:44'),(110,'John','Doe','0773784258','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:29:44'),(111,'John','Doe','0773784299','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:29:44'),(112,'John','Doe','0773784330','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:29:44'),(113,'John','Doe','0773784361','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:29:44'),(114,'John','Doe','0773784406','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:29:44'),(115,'John','Doe','0773784435','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:29:44'),(116,'Jane','Doe','0773801709','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:30:02'),(117,'John','Doe','0773802189','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:30:02'),(118,'John','Doe','0773802234','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:30:02'),(119,'John','Doe','0773802253','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:30:02'),(120,'John','Doe','0773802293','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:30:02'),(121,'John','Doe','0773802323','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:30:02'),(122,'John','Doe','0773802354','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:30:02'),(123,'John','Doe','0773802397','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:30:02'),(124,'John','Doe','0773802425','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:30:02'),(125,'Jane','Doe','0773978470','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:32:58'),(126,'John','Doe','0773978978','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:32:58'),(127,'John','Doe','0773979027','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:32:59'),(128,'John','Doe','0773979047','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:32:59'),(129,'John','Doe','0773979088','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:32:59'),(130,'John','Doe','0773979120','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:32:59'),(131,'John','Doe','0773979152','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:32:59'),(132,'John','Doe','0773979198','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:32:59'),(133,'John','Doe','0773979229','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:32:59'),(134,'Jane','Doe','0774154312','jane.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:35:54'),(135,'John','Doe','0774154785','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:35:54'),(136,'John','Doe','0774154829','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:35:54'),(137,'John','Doe','0774154850','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:35:54'),(138,'John','Doe','0774154891','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:35:54'),(139,'John','Doe','0774154923','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:35:54'),(140,'John','Doe','0774154952','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:35:54'),(141,'John','Doe','0774154995','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:35:55'),(142,'John','Doe','0774155024','john.doe@example.com','123 Test Street','Colombo','Sri Lanka','NIC','123456789V','2026-03-01 08:35:55');
/*!40000 ALTER TABLE `guests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `reservation_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_method` enum('CASH','CREDIT_CARD','DEBIT_CARD','BANK_TRANSFER','ONLINE') NOT NULL,
  `status` enum('PENDING','COMPLETED','FAILED','REFUNDED') DEFAULT 'PENDING',
  `transaction_id` varchar(100) DEFAULT NULL,
  `payment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `processed_by` int NOT NULL,
  `notes` text,
  PRIMARY KEY (`payment_id`),
  KEY `processed_by` (`processed_by`),
  KEY `idx_payment_reservation` (`reservation_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`reservation_id`),
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`processed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,3,240.00,'CREDIT_CARD','COMPLETED','TXN123456','2026-02-16 17:56:07',1,NULL),(2,1,120.00,'CREDIT_CARD','COMPLETED','TXN123457','2026-02-16 17:56:07',1,NULL),(3,1,240.00,'CASH','COMPLETED','TXN-ACC517F4','2026-02-23 10:31:34',1,''),(4,5,240.00,'CASH','COMPLETED','TXN-DCE39742','2026-02-24 07:45:16',1,''),(5,7,360.00,'CASH','COMPLETED','TXN-46EBD5BB','2026-02-26 23:37:36',1,''),(6,8,150.00,'CREDIT_CARD','COMPLETED','TXN-F10B596A','2026-02-27 01:22:54',1,''),(7,8,150.00,'CASH','COMPLETED','TXN-DEC8A90B','2026-02-27 01:22:59',1,''),(8,8,150.00,'CASH','COMPLETED','TXN-B32F6CBD','2026-02-27 01:30:27',1,''),(9,9,300.00,'CREDIT_CARD','REFUNDED','OVR-78241923','2026-02-27 01:39:51',1,''),(10,9,300.00,'CASH','COMPLETED','OVR-0632C49A','2026-02-27 01:40:50',1,''),(11,11,300.00,'CASH','COMPLETED','OVR-C52E8342','2026-02-28 21:44:58',1,'EXP1234');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `reservation_id` int NOT NULL AUTO_INCREMENT,
  `reservation_number` varchar(20) NOT NULL,
  `guest_id` int NOT NULL,
  `room_id` int NOT NULL,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `actual_check_in` timestamp NULL DEFAULT NULL,
  `actual_check_out` timestamp NULL DEFAULT NULL,
  `number_of_guests` int NOT NULL,
  `status` enum('PENDING','CONFIRMED','CHECKED_IN','CHECKED_OUT','CANCELLED') DEFAULT 'PENDING',
  `total_amount` decimal(10,2) NOT NULL,
  `special_requests` text,
  `created_by` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`reservation_id`),
  UNIQUE KEY `reservation_number` (`reservation_number`),
  KEY `room_id` (`room_id`),
  KEY `created_by` (`created_by`),
  KEY `idx_reservation_dates` (`check_in_date`,`check_out_date`),
  KEY `idx_reservation_status` (`status`),
  KEY `idx_reservation_guest` (`guest_id`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`guest_id`) REFERENCES `guests` (`guest_id`),
  CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`),
  CONSTRAINT `reservations_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (1,'202602160001',1,3,'2026-02-20','2026-02-23',NULL,NULL,2,'CHECKED_OUT',240.00,NULL,1,'2026-02-16 17:56:07','2026-02-23 10:32:08'),(2,'202602160002',2,10,'2026-02-18','2026-02-21',NULL,NULL,2,'CONFIRMED',600.00,NULL,1,'2026-02-16 17:56:07','2026-02-16 17:56:07'),(3,'202602160003',3,5,'2026-02-17','2026-02-19',NULL,NULL,2,'CHECKED_OUT',240.00,NULL,1,'2026-02-16 17:56:07','2026-02-27 00:32:16'),(4,'202602160004',4,14,'2026-02-22','2026-02-25',NULL,NULL,4,'CONFIRMED',450.00,NULL,1,'2026-02-16 17:56:07','2026-02-16 17:56:07'),(5,'RES202602240001',6,5,'2026-02-24','2026-02-26',NULL,NULL,1,'CHECKED_OUT',240.00,'',1,'2026-02-24 07:41:30','2026-02-24 07:45:04'),(6,'RES202602240002',6,5,'2026-02-24','2026-02-26',NULL,NULL,2,'CHECKED_OUT',240.00,'',1,'2026-02-24 07:55:07','2026-02-24 08:08:08'),(7,'RES202602270001',1,9,'2026-02-27','2026-03-02',NULL,NULL,2,'CHECKED_OUT',360.00,'',1,'2026-02-26 23:36:53','2026-02-26 23:38:01'),(8,'RES202602270002',6,5,'2026-02-27','2026-02-28',NULL,NULL,2,'CHECKED_OUT',150.00,'',1,'2026-02-27 01:22:15','2026-02-27 01:22:45'),(9,'RES202602270003',6,5,'2026-02-27','2026-03-01',NULL,NULL,1,'CHECKED_IN',300.00,'',1,'2026-02-27 01:32:55','2026-02-27 01:33:55'),(10,'RES202602280001',16,6,'2026-02-28','2026-03-01',NULL,NULL,2,'CHECKED_OUT',100.00,'',16,'2026-02-28 08:51:44','2026-02-28 21:42:28'),(11,'RES202603010001',16,5,'2026-03-01','2026-03-03',NULL,NULL,2,'CHECKED_IN',300.00,'',1,'2026-02-28 21:44:06','2026-02-28 21:44:16');
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_checkin_update_room` AFTER UPDATE ON `reservations` FOR EACH ROW BEGIN
    IF NEW.status = 'CHECKED_IN' AND OLD.status != 'CHECKED_IN' THEN
        UPDATE rooms SET status = 'OCCUPIED' WHERE room_id = NEW.room_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_checkout_update_room` AFTER UPDATE ON `reservations` FOR EACH ROW BEGIN
    IF NEW.status = 'CHECKED_OUT' AND OLD.status != 'CHECKED_OUT' THEN
        UPDATE rooms SET status = 'CLEANING' WHERE room_id = NEW.room_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary view structure for view `revenue_summary`
--

DROP TABLE IF EXISTS `revenue_summary`;
/*!50001 DROP VIEW IF EXISTS `revenue_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `revenue_summary` AS SELECT 
 1 AS `booking_date`,
 1 AS `total_bookings`,
 1 AS `total_revenue`,
 1 AS `avg_booking_value`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `room_types`
--

DROP TABLE IF EXISTS `room_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_types` (
  `room_type_id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL,
  `description` text,
  `capacity` int NOT NULL,
  `price_per_night` decimal(10,2) NOT NULL,
  `amenities` text,
  PRIMARY KEY (`room_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_types`
--

LOCK TABLES `room_types` WRITE;
/*!40000 ALTER TABLE `room_types` DISABLE KEYS */;
INSERT INTO `room_types` VALUES (1,'Single Room','Comfortable single occupancy room',1,50.00,'WiFi, AC, TV, Mini Fridge'),(2,'Double Room','Spacious room with double bed',2,80.00,'WiFi, AC, TV, Mini Fridge, Safe'),(3,'Deluxe Room','Luxurious room with ocean view',2,120.00,'WiFi, AC, TV, Mini Bar, Safe, Balcony'),(4,'Suite','Premium suite with separate living area',4,200.00,'WiFi, AC, TV, Mini Bar, Safe, Balcony, Kitchen'),(5,'Family Room','Large room suitable for families',4,150.00,'WiFi, AC, TV, Mini Fridge, Safe, Extra Beds');
/*!40000 ALTER TABLE `room_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `room_number` varchar(10) NOT NULL,
  `room_type_id` int NOT NULL,
  `status` enum('AVAILABLE','OCCUPIED','RESERVED','MAINTENANCE','CLEANING') DEFAULT 'AVAILABLE',
  `floor` varchar(5) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `room_number` (`room_number`),
  KEY `room_type_id` (`room_type_id`),
  KEY `idx_room_status` (`status`),
  CONSTRAINT `rooms_ibfk_1` FOREIGN KEY (`room_type_id`) REFERENCES `room_types` (`room_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,'101',1,'AVAILABLE','1',1),(2,'102',1,'AVAILABLE','1',1),(3,'103',2,'AVAILABLE','1',1),(4,'104',2,'AVAILABLE','1',1),(5,'105',3,'OCCUPIED','1',1),(6,'201',2,'AVAILABLE','2',1),(7,'202',2,'AVAILABLE','2',1),(8,'203',3,'AVAILABLE','2',1),(9,'204',3,'AVAILABLE','2',1),(10,'205',4,'RESERVED','2',1),(11,'301',3,'AVAILABLE','3',1),(12,'302',3,'AVAILABLE','3',1),(13,'303',4,'AVAILABLE','3',1),(14,'304',5,'RESERVED','3',1),(15,'305',5,'AVAILABLE','3',1);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `role` enum('ADMIN','MANAGER','STAFF') NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=172 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','0192023a7bbd73250516f069df18b500','System Administrator','ADMIN','admin@oceanview.com','1234567890',1,'2026-02-16 17:56:07','2026-03-01 08:35:55'),(2,'manager','5f4dcc3b5aa765d61d8327deb882cf99','Hotel Manager','MANAGER','manager@oceanview.com','1234567891',1,'2026-02-16 17:56:07',NULL),(3,'staff','81dc9bdb52d04dc20036dbd8313ed055','Front Desk Staff','STAFF','staff@oceanview.com','1234567892',1,'2026-02-16 17:56:07',NULL),(4,'testuser1772150517159','L5Qm529J44xJKCsJUhMJrXoDWGfk08cWcuI9pmj1HvfweU4KkIQZj/tzPpDQfjMn','Test User','STAFF','test@example.com','0771234567',1,'2026-02-27 00:01:57',NULL),(5,'testuser1772150517357','4mghRb5NufffhcIkn2HS1s2VqnwjG5+AQtBcoKSL/JbSGxxlM0/JoAV8KUVTqI1b','Test User','STAFF','test@example.com','0771234567',0,'2026-02-27 00:01:57','2026-02-27 00:01:57'),(6,'testuser1772150517457','91Rhn1+D6BRlo8omX+R6Q5b092HatrCdN3EzytGZAl7c50vfLA8OYmjXNa/UBuL2','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-02-27 00:01:57',NULL),(7,'testuser1772150517545','X06GXAoM4lbthjh+eeSNQdI+V22iCujMo1sPiMIJqO5Q+PHZKNk6rGKPRNx1Iaim','Test User','STAFF','test@example.com','0771234567',0,'2026-02-27 00:01:57',NULL),(8,'testuser1772150517637','QX3daVp/AkaCwXhvssF/opTy9C646G+ZNoRKfJIme0H67vqHc56aC7s0JTNe8bDi','Test User','STAFF','test@example.com','0771234567',0,'2026-02-27 00:01:57',NULL),(9,'testuser1772150517707','xUooEu6nx8hfY2WSnFA8B1TP95pHD29c8YgQMdEQr43536EKzraXHfl5dB3fVldw','Test User','STAFF','test@example.com','0771234567',0,'2026-02-27 00:01:57',NULL),(10,'testuser1772150517802','0a1Ls9PANgUVMYTwT+8F8mozSkho+d7Dk+OgeSmD7oz/PyuhqZm5rYcXSYBR2VeY','Test User','STAFF','test@example.com','0771234567',0,'2026-02-27 00:01:57',NULL),(11,'testuser1772150517906','wiFmv2nehG6dfKpcsto6IY3Cbg48u7JjsX7YRPPQKB4VbDuIb/DA3u9/xPzrfmg2','Test User','STAFF','test@example.com','0771234567',0,'2026-02-27 00:01:57',NULL),(13,'testuser1772150518006','3SomHUGchefLlSZiUzNbGUo1q5yIn4t80GjCLHzoM2RzMAy8FC72QCxVV6CNyIAz','Test User','STAFF','test@example.com','0771234567',0,'2026-02-27 00:01:58',NULL),(14,'testuser1772150518058','Mk51HK0oVUh/eWCLCA0YBGL2m9nt10mC9sj4MwhdOeUgkZgTAYvGyDlMoD5UIcMS','Test User','STAFF','test@example.com','0771234567',0,'2026-02-27 00:01:58',NULL),(15,'manager1','pNVN8fRYEInxBk7XoHPNxodZmioLUH/2NGZSJ07Q9bNGFZJCEnE6I13YwNbi+6eT','Test Manager','MANAGER','manager@test.com','555-0100',1,'2026-02-28 08:29:04','2026-02-28 21:25:37'),(16,'staff1','482c811da5d5b4bc6d497ffa98491e38','Test Staff','STAFF','staff@test.com','555-0200',1,'2026-02-28 08:29:04','2026-02-28 08:48:16'),(17,'testuser1772323374874','YZ0dBwxKK4e3AaP5tmoQ1EEL01SP2fvM8a3Rilk8mbtUNEyDlBCJj81uGcM/eqid','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 00:02:55',NULL),(18,'testuser1772323376216','5cDOili3RDpLUdIs/JF5jnfWrY21mNvB+mOuuPqb1h/AL46u7qwpavFLHTEfJiDU','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:02:56','2026-03-01 00:02:56'),(19,'testuser1772323376274','D3pRjSf6FBWRuB499eKTNXdVmoyTIWcT8TV3JMUMr68Ihunym3u2KfBPqjUfeZu/','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 00:02:56',NULL),(20,'testuser1772323376343','5FpWwyjQcXxL5Kc7mzX2wJCcSd8yt6lWqdnGKaqVUjMKi+47CC4Y6NDkguxHww1/','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:02:56',NULL),(21,'testuser1772323376400','/q0apvouP4iwZoJdzFEQmiXp/kesl/y7yLj3SxwyxriBSUq2pXOVmFRNz9vlqYnj','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:02:56',NULL),(22,'testuser1772323376459','sojpaJ5GXROxNWn4BFRYl3yJl/YE/sN4rLbAMO8q+iKV70VemkpjOEHj2Q39vYOg','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:02:56',NULL),(23,'testuser1772323376503','208oTG6vszitOWddCQ0OaASzNDkYsLr3oEv3ya+h4uhKAf3aCNS273QrG1deUZQA','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:02:56',NULL),(24,'testuser1772323376545','49CP4VKZ1A0b3V+5jrF01jTv0qp7P5/4lOiWO4w2pPCFP1xP5UmGHKax6rUz0G4s','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:02:56',NULL),(26,'testuser1772323376611','XUbFvG3ZXk+MieY7HMf/0p6fafsr0iZxp4Nuyniw3tO+misFT/0pXpCBcl3Y+lV1','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:02:56',NULL),(27,'testuser1772323376638','jJVAWf/aBFB9blk4t+c1x27ctB0SndekcNRgvL41sXeLD57fGLBdM2wqmehLUVzL','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:02:56',NULL),(28,'testuser1772323470079','x+By8Kfad8RtL8BDMA/ozrl6hPml4MQraQSjoRkPzii2Y3R9gsNHLzcpzAHZhzpI','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:04:30','2026-03-01 00:04:30'),(29,'testuser1772323470160','C0Sb/ffohohVsh4ClskPSXBWDuZrLBdhFQaveen2dl7L2aNMJFEfwlXKhV77V092','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 00:04:30',NULL),(30,'testuser1772323470232','dfoo+R2Dofu2foQtdeKqXs2pVGNFF/AlMLztRN5CPL4685n/m7IcBFDb1N/G0TAl','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:04:30',NULL),(31,'testuser1772323470295','sUlTBGuZ7aRUwJhfGcXR5KfTrfDssRZMhCcD5c9nqA4uXyPUQeqYPyTNU1PeuA74','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:04:30',NULL),(32,'testuser1772323470364','tTgfoAcNDcrqqfLRPn8DatXa+w0Z6iozkYLyrW88qOSi3kZex7E1FwycpCi1RhaQ','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:04:30',NULL),(33,'testuser1772323470412','SuTKf6mJpH45MK2+1TRdrwHaGE2poLeNrlYHXXA1DsnGqRPfFqHXgvWT8eRYRnFs','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:04:30',NULL),(34,'testuser1772323470462','Ecj+cEbCUjgrGo1lDDCmTdNzRfYLmpWsJlWWZc0w9zndC7uQ0fjG2+t8wqYE8sAo','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:04:30',NULL),(36,'testuser1772323470524','AUOpHyF80opu3qQloLFP/p1a+xRnC5u4HuXfv8rGCexQu1LxfuniuPwHS20Vo7NF','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:04:30',NULL),(37,'testuser1772323470557','Mz8+hm4UFMny0FOPG9BjYcyp2UmRigPmXWrHO01OsWeXQvapHKqJ7OoDTB3YxDOa','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:04:30',NULL),(38,'testuser1772323470940','6CH+PgidwYBSK2m8Gzjh76KreJaOxZfap3YAyW8v1a+3NGrN0uFiltwm5X76/pOn','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 00:04:30',NULL),(39,'testuser1772323564067','18GNjd5xiad3eQQbO5OE9Czg4VyB3+Fe/SeY9KytIo+psyhguQTgRsVT0z/0yGXy','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:06:04','2026-03-01 00:06:04'),(40,'testuser1772323564146','F3pz0a1t0zbaJ6EePuUWfFj/5I0ClEP9tN7Zv+ooImozCHaDLkmHI/HKcBGfUx2P','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 00:06:04',NULL),(41,'testuser1772323564219','9xircNBIJtpod5OxgHn/sUCQZAdtSqmvUDGSnbacqBjcKc8tn1zyfLIFo74bRxV1','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:06:04',NULL),(42,'testuser1772323564278','3YNE7g9XvJX3stE0TO+GIdZdHR22qXk9O4DWKDRB6McDYQKuMfMNX9F5FI/JALv2','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:06:04',NULL),(43,'testuser1772323564344','M5SJMiv5KV0BXck7FcMMrFxVTPCA4Q2fTSubhY+NYI+8OHzrRq3/eGdwodeYVHSQ','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:06:04',NULL),(44,'testuser1772323564391','ZZ8hLcGquG7pXWVfcQe4zqh30jnl9ijmDIU9NtVKKKPLAm2Fe8w+/3TET9aL2KKH','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:06:04',NULL),(45,'testuser1772323564439','28Gd7jHWmvVz3HlelzlA3x88ST2Uu0J+e8Kh23/tCfaCzjwzUBlYIxAwKJRKfFS6','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:06:04',NULL),(47,'testuser1772323564497','tr/4FuIc2MaYLmuMZsFqrNHQ3XmGrBezMO9jpcR8xa8/J6otF2BqDs2+qhw3Upvm','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:06:04',NULL),(48,'testuser1772323564529','/5N46pCpfpWWAqGQIRCuGYWBdZufDRhsc8PgBpwlQF52oxjUugha+etqXV/SbdO2','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:06:04',NULL),(49,'testuser1772323564869','Mzv/0DNrnN8/obqyjNRkOwPphferg6eRCQu4g1ZsXYANV24Gi+kYHE1QIQ8o0mVJ','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 00:06:04',NULL),(50,'testuser1772323628699','5LYi8Ytq9GhU+42gHCET8BiHNCFLj0kTzhnefRA6sFtEy3oIaFEkN61APH+w9Gm3','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:07:08','2026-03-01 00:07:08'),(51,'testuser1772323628774','Sezbu6KgUGKhsKHeDZV/btwzRoVs9iMRhPAXiMWNc1NEzgjDMneGef5Qr7oC2dcq','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 00:07:08',NULL),(52,'testuser1772323628843','LdWT54F/gaAM9gUMlPYZ5pVKgt1qcIacH3XPoUBvRc39tpHyrmGsMlg1JtshnJHG','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:07:08',NULL),(53,'testuser1772323628904','CtGvmOLNkQUIwtTORTxAjsi6TBMGCN+/ZYMs/UThH3x9i2tdnNr6+ifr+bZRz5LC','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:07:08',NULL),(54,'testuser1772323628967','/7yLz6hjouyEtqzXn49jfAcqWI5lGFlRJXCIi2uoaPetSA5xMzr0/7rGTkW5V+Tc','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:07:08',NULL),(55,'testuser1772323629012','mlJtNX4E3JnB/VKZspUxj7hdXoyrTNh8bPPMiqnOh/M6Ys1DOvNrG8MTxL8ImHKg','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:07:09',NULL),(56,'testuser1772323629055','JO2cY98jc+DQ6falvrQZZBp+BLpDZLemZDbsY64a/uwZb8OEkn3KLAF2FW+6mEIq','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:07:09',NULL),(58,'testuser1772323629109','sOMeNdjgAVy2EKSvWkuIEAa4YoDaD3v3l1me8cImWLJ5lLsgQ8Vag7p9wGPpyUhj','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:07:09',NULL),(59,'testuser1772323629140','dOyukzClVSNnm6MkwKJQMkQNJmD8fqtP9UGEWQZ1omHpJxTeaChaYQDMRgd8tYqk','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:07:09',NULL),(60,'testuser1772323629186','QITsW1OlMAwWzOYG74DWGId33OHdQJ1Rg3/b8il3pZIkDX7P7rppWFj7877BGCTZ','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 00:07:09',NULL),(61,'testuser1772323726212','HZp/OxJNfQLZaHRgCVm09+dx2yhlLrlLOwnuOA/t+FGDRnlYK3q4fXU85kLw9Ly1','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:08:46','2026-03-01 00:08:46'),(62,'testuser1772323726284','B8Te8m4jcx2BlMx8zWdEgMWerd8b4qhOlP00hhPIEPlh03wi7CVwQK/6wUNc67Qc','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 00:08:46',NULL),(63,'testuser1772323726356','cXOt2BCWpRft5UwKKzCGKNXk2511rijTpFSGqhH7kuMGWpDPRf7cyq7MJ2dTCSkN','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:08:46',NULL),(64,'testuser1772323726418','39ooAfYbnFsz8o5NMuc72vCdRUGNgK5cV+kPx673HVLyUJBxbdzJZkGwGOTAU+GY','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:08:46',NULL),(65,'testuser1772323726479','5UM+GPOcYvpcH7xv7xrg9v/oIIcHsL00yNVrv7L4N4o2jc0c5qQQ24XP7zUb8f4b','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:08:46',NULL),(66,'testuser1772323726525','mlmSLewIAAbNtpbptqwYfWK+GNL9vRAuuE80/ecmzCDloQV+4MKeEZIACbamrE9C','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:08:46',NULL),(67,'testuser1772323726570','dHTqGuEWg8sCKlsP62udTpPnmxCeL8gBgx4rN12ToAn321hVLcdCa0k1EDvmMhxX','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:08:46',NULL),(69,'testuser1772323726629','CpQMssx+XTVOWBxxvXmZH4aWYO6m8FmsghP580FORYm1kcuR4cr6SKR4qYskEL/7','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:08:46',NULL),(70,'testuser1772323726662','fICb8Rndl4HSI4kJt3hA3GXuLNb1wYnqYmrwJXp0Os9oG3RNcaRDo8e016PbLHYy','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:08:46',NULL),(71,'testuser1772323726710','lTySyK9Vje6OtivRKIsB+olM4ubmTLvEBZz3lNfjkBpPOQG3cJSF0TeAr0SGPMeZ','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 00:08:46',NULL),(72,'testuser1772323767451','ejP6nMbLHsJTjUqqGxmnNJBBTmbX3jw2yKbnezxVdbIFHrh8mBa2QPhtBCbsbesT','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 00:09:27',NULL),(73,'testuser1772323767561','nyVEa0TuRh25j2KZbTw6IAcLLfnB/ku3VoBFeUkZtCafwZh0UsAFRKWFX5rESR7F','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:09:27','2026-03-01 00:09:27'),(74,'testuser1772323767618','lE5c/GhHe2B1r00JpCxlKqshnvnGbaRKRiQ1ShH/vC3NO+XbO861JAj54M7S/chI','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 00:09:27',NULL),(75,'testuser1772323767678','z4o17kOkMsFXSpqrvTKurXOaXnGTVSIKfVB7FGNJRh2ZUwveBm0Btx5yDlYcHS5t','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:09:27',NULL),(76,'testuser1772323767730','XQ7xNlRA+OEc2Qrjxa9G9+AUaNVZy06MsMSejxls04IeR16xadzhjaf5eFYtp7W0','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:09:27',NULL),(77,'testuser1772323767786','vejzYyAvuCEYp1FpOat0rXZAVIQZKVaBFY6OGuhZGIRsgxrAYcdwXP8fPd9sEaBI','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:09:27',NULL),(78,'testuser1772323767825','l63ffTVpvHDs3YddZyGfu/GdZBf9BkXtDWYu8Twxeku+qxZuY/Gv417FvV4aQvCV','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:09:27',NULL),(79,'testuser1772323767865','l69BcMlA8MzsfTmfCFKvWswY5eUQVZ8Y4EAdieJ4CeNW08LaDafLDgptpRSLHrvF','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:09:27',NULL),(81,'testuser1772323767919','ilQH07hj+xEbTTgrQZHIwV8wtPT8nVrc5VdF57YzxYU3jfBtCecJKFPpXapqwcii','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:09:27',NULL),(82,'testuser1772323767946','IqHKa3vp50bLDMeLYx4y31k3pHBtojnTkzqd+caCRL9dpj+ejxunOLbkEFZcuoCO','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:09:27',NULL),(83,'testuser1772323879288','CFd1hfuKSCKahcAjLGnj19UEa23tSwm7IxeZsCn6NxpsjwzyvZdcU2i1q67yy01j','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 00:11:19',NULL),(84,'testuser1772323879404','9Vt6DtAsJlRN03m6ZcbKzlaHvL+WFYjUYb4S1aLgqYL1JV3YIidzaU1Cs1b0Vn1u','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:11:19','2026-03-01 00:11:19'),(85,'testuser1772323879463','hRcdxCxxlPjMhGySS1yRk4uP6vpLtGDVnOfLNqjSlDsd+w+3niKHDASq3BoYaxl5','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 00:11:19',NULL),(86,'testuser1772323879523','v65dP99LEEEvXMXzlVrV2erU89jSgfmtgwj8PwsQAnBS2JD8aZSHyIx5102AToXd','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:11:19',NULL),(87,'testuser1772323879578','vuiys9Ha6Vq5Cadj0V1fzy+Jzy7xB6Cl3Y0S8MZeALSBvVtMUVXc9a+p60yCPZOz','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:11:19',NULL),(88,'testuser1772323879635','NDOI7qW4ZUSGPEfWWEMoba/y89sFqTLBIJ+4x2nn6RC3ZwVuPEMN8u6nFtvK3aPZ','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:11:19',NULL),(89,'testuser1772323879676','X8QNKt/Opl+mHyrT7A/CAIeypAtld01Ve6YQbLlvONTc2bZp8+AXAdx4FHnjVob4','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:11:19',NULL),(90,'testuser1772323879717','WM2hhRyy+ZoAIPwVOeJRYDrR0OypsufG49uIJIKcrkSOAwNZu2eMgKxMlIxQXzXv','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:11:19',NULL),(92,'testuser1772323879772','T9b2vgnjCBpZ3BDOLyCD9C4qpDHm7l9CjelPMDt+Uv6FW2sB9TzU8ZTbWGXOQTK/','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:11:19',NULL),(93,'testuser1772323879810','x0G1IOXAfxEUAbv3ZafWjoVnavFWUE8X0OnRp4iE1pgdlfj7Cw4umsB7BNuhqyQM','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 00:11:19',NULL),(94,'testuser1772328729279','X7uBZUPmOXWypxOWunnWXHm4k7ZrZ6xywGUsXasDwFLDmJqE151vVVfJfgc745Tq','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 01:32:09',NULL),(95,'testuser1772328729389','GpXGyP5ixC2riiyaGA0MV7nvSrN6AZgUkyaklrsB0cMDATUVnh9x4dv+HkSBFXFr','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 01:32:09','2026-03-01 01:32:09'),(96,'testuser1772328729451','yjIBUA9QwSgLDYkzCi6uZXc9sUPMA69aEbdvmqw7KEhSHGWQFrxi82P9jMXRJeeL','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 01:32:09',NULL),(97,'testuser1772328729511','zn5Edu54uRB4BGAP7N+pTeiEvroaZyV2/Rr0sbzikGRx6cHaI12bzHxjs3stnJtg','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 01:32:09',NULL),(98,'testuser1772328729564','DIZkAk3wHdCbjAVz3nhyYr/KC49cHg9Hem7LyMb/c8ro06L/f4U1saE+LBCdlHSW','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 01:32:09',NULL),(99,'testuser1772328729620','Ww9So0buTVWONMUIW93lY3A4wZMSTlkrZBg/1CEZOqpZ/vWIH84KerafFWkgbq6T','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 01:32:09',NULL),(100,'testuser1772328729660','LBfS5y7VPdyQYyR0eeHvbRYCbBWHkA7f/5SITsk5PZ96v5izrLlJrv3UwZoIhrPW','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 01:32:09',NULL),(101,'testuser1772328729699','faX5sqyMN9Zswq+8oM7gcvoNabyRbog7KttglJ4CarZZGyqJho3bBK7u+hnugYic','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 01:32:09',NULL),(103,'testuser1772328729754','lk3WpjSuhSNhg//UbnbbliCsowIUSWNeDx/SsrAcR/HELJ2zim5Z3q0RpLjBqLcT','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 01:32:09',NULL),(104,'testuser1772328729782','1Qruz+BWwcL+EC8d/78KKiGDmORRkEp4GZsusZx+ywyKYXbMl8HY3vqRSJeSwkLn','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 01:32:09',NULL),(105,'testuser1772352089028','QAfFZeuPDy+7GG0nWMc30RSOle3UyTGZSIVL1HdCuJBr2Juv/y+T/XX2scuEe2g+','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 08:01:29',NULL),(106,'testuser1772352089140','vpOOw78S/P/M0S2C2goVkQ0Z4YX7XvFODQaJkXq11CCknJUe+kr4CXzZH2ZVZnw6','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:01:29','2026-03-01 08:01:29'),(107,'testuser1772352089195','QDTh8horofphUCZUcU+dOYrC7E4xvkYjrCm26QdtJKRHDc3XpBcF+SDsmSFYNYsQ','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 08:01:29',NULL),(108,'testuser1772352089256','PyyuwBjUBc9cli7nwkKSVQ9wvGMLVB6H9/eesDrsGI7JmcTb9I4X4UPcib0Os8Pj','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:01:29',NULL),(109,'testuser1772352089313','qPlPFRXuSxCpcpBpzezxaT5+htorZWW43aYM4q5B50nNDQjnfEOIhJnegBnEqksf','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:01:29',NULL),(110,'testuser1772352089372','uJYHSn7OprGBEaJp4ruaQ0ATq6406hW/JU26LrVP7/R7zzSbhKpMygFFuWdfzz0E','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:01:29',NULL),(111,'testuser1772352089414','01mxMH61GKuBJrT3DKGJoQiPlmmB1kQqcziYWK3JHb8uSSpSXvT61vZXeXVFmNhI','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:01:29',NULL),(112,'testuser1772352089457','PyCUTUCwsbsgesP48pjHdmaH05vFRcnKB49SvSkrBf7aUAfmJJ40BeiknIpIjCFA','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:01:29',NULL),(114,'testuser1772352089512','O3PH4M3Oc7oBGZHv8STMlHVu4uKrt+RDNqYsQuNQk36aY/QBSn8UFQonBJSAcUDf','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:01:29',NULL),(115,'testuser1772352089540','56jqB+dEKjcrQhO9GXFOcov2myV+Esh2oowc9gzWsfaSSo13FNc+3D180Gk6vcVv','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:01:29',NULL),(116,'testuser1772352197840','C180CB0Dmt8yzTrVIHlSWtJ6CSPeWjsXzFvPKLktEY4fjCkceGNWVvpc2pggF8lI','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 08:03:18',NULL),(117,'testuser1772353377470','d9+BKbuGMmPhM9q0ziubvsdwqrxNTIHfwgXkTF8cETSXU79uTQ9edOqpEUMErb2l','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:22:57','2026-03-01 08:22:57'),(118,'testuser1772353377548','+6NOilohjR5l3somwNwajYynEmVCzzjdqH3U0WbtAirJB3pku3r2P5Mhf6ZrXT0G','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 08:22:57',NULL),(119,'testuser1772353377614','C0IBJaMy/OVW60bN1GRXueX8w9EKZBX0KKUua/PW6SBTaIUBfYJmWDL0Ui7n9XOB','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:22:57',NULL),(120,'testuser1772353377675','oq930BkvJ+i58Gg99hwTsGR5kePxe/7sW0NqdU70x4zVQY5CSLG4sjjzoWG9oDaM','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:22:57',NULL),(121,'testuser1772353377738','PWasraMEXTSB5e+wn+tJc/2HPOraBTimXDLFcU0OCz62sDEWFyZqNKLgXcJu5Wuy','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:22:57',NULL),(122,'testuser1772353377780','zNh3C6nm/gEIWHStG1Ax7vGzVbPeALEFqKsY2wgiMfzO5xuqX3WoJp5BjUc3PVuA','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:22:57',NULL),(123,'testuser1772353377826','Lgluiyny3ESuOeglKOc3lKMLMzqi7+BJdOuR2E9zh0ErmD69Qs7Z7Kn93tvxNy2O','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:22:57',NULL),(125,'testuser1772353377880','jGs47/H37QWE8q1pxyxydfn6dDP5O7vfQ1ZpZCRk4nXvWvtGIYTaX0A2x06DrXvn','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:22:57',NULL),(126,'testuser1772353377911','cpDahck2KA7mGPN6nKBUOdhBchy8EDIhDsMhhxEP13Codi5XvUjutQWJHxgEwAcL','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:22:57',NULL),(127,'testuser1772353582542','lbyjq3OuOpswBZXYDFu9Ao9fFLcUco8pvqZnDKtlOm9XJJjyiZ7E+0NE12Vr7Vpm','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 08:26:23',NULL),(128,'testuser1772353784472','AeHGE9xXTI0OhD2d/qpBNgNyMWve7ybcL1YMAU3mBcxoymmx5FAXvigK0xzN0FWL','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 08:29:44',NULL),(129,'testuser1772353784584','hmb28h1ay21f5G90asKM7NMvvc3tANHRzay1wSuJywaoZLB4n22MpOMZa6iBANAD','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:29:44','2026-03-01 08:29:44'),(130,'testuser1772353784641','THjmXk86LbtlRydMo5R4xLtXUxOuteaB/FIi9dUZnGY/DIRKsawgwQ40MVw7InI0','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 08:29:44',NULL),(131,'testuser1772353784700','M0fnDakQtv+6G+Z16phIv3n8gsVO1CsUwO192gBPbl0agPAQnjHlflaZ41YckeRg','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:29:44',NULL),(132,'testuser1772353784754','umpFpYCs3TAoSS24MZht5rHvPJFS6WCbkm2W/hYwnNZk5RKocO25zTzZf+hIALqM','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:29:44',NULL),(133,'testuser1772353784810','GOx7vBE+8dW57RwigC4l5hw9bLBwTfA/M6Mw0K+zuAxmf5A/VIb9NFJovvL0H+4p','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:29:44',NULL),(134,'testuser1772353784851','hSYrU827BP25Ghr8VBDVxklIDDqD/Scbxy7TnzPxcR5RBPtCLY67lyGDBGICa6yV','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:29:44',NULL),(135,'testuser1772353784891','nnPwWqmplQFhIgtzO8gIfZdv9XRDvHpqHbHFgRkvqdJGOM4BfRA+FPK83WpkE7pM','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:29:44',NULL),(137,'testuser1772353784942','H3OnhMW8+imvS09d6aW4mIuXdlnYFI03LxMBCGjn0NoiG8G16TwhYVWyyTgn8j50','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:29:44',NULL),(138,'testuser1772353784970','FStsMxf7XDraasOp8+xJIkwof0FW6YEuM5kJ1Y68XGIsDe4XwwYyapfZDhLutLmh','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:29:44',NULL),(139,'testuser1772353802460','6g0hcZ2M0qnLVhKEXCM+y3ROuZwX2qSDnE/VtBktWNxRKRAqtiZgoPjhsBg45NbO','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 08:30:02',NULL),(140,'testuser1772353802573','sFPVo6CQMsRiLjtbBJ8u8DPNNugGxw2ucsa1vz7xEAu0EyZaqv8Oyg8S3F47BiWR','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:30:02','2026-03-01 08:30:02'),(141,'testuser1772353802629','taEiRqe99B0ihkSLmIF9HipaijCoXD8EzcDLvTyA7LWGoHvGzfv5VJ/ou4wotzew','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 08:30:02',NULL),(142,'testuser1772353802691','DSfxoRDXB2Qrlrb83GtbZYToSVjP1iFmjkkRu5QjN8RN4eUFgVCa3qbNj1yfC4jt','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:30:02',NULL),(143,'testuser1772353802745','uy3mLpY0wgxJ0g4quFVOWpFPZRVKd/kRua1B5/e/QD05E1IjRyJUqYU1jqmrnVYd','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:30:02',NULL),(144,'testuser1772353802802','aKn1dihxfpqy57WcVEdxASBsGl4mRLHRQQTP2ch3+SAeudneEe/VsXxLfDnEdIrO','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:30:02',NULL),(145,'testuser1772353802841','5wekuEmppu9GKAOtAkXt3XW9oEb2YfbrBSlbWRu5TwmOK0BNACnOISaKlABLrb0b','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:30:02',NULL),(146,'testuser1772353802882','80kferhyEb6ZrztLj87eYGmooGuv6p7miNIVxKlJ5IAPzNcriBAAH1ZzfzM2WMCJ','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:30:02',NULL),(148,'testuser1772353802933','NCEk5ecWcuQpBf8kRAQrR7wpq48M9qsVujT4BsSj0zPs0hTXEMyEuxOrtejDf4NI','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:30:02',NULL),(149,'testuser1772353802961','2Muxezm/1YzlavkPyGIu1WIUKd43uAbLhFO6RkvCaubClnvUMTBhZ9VSU48VdSYQ','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:30:02',NULL),(150,'testuser1772353979267','pD6sMtnTC+8Iq+121bSw3SjpvrD/+qeKPfhnwLAeMqIVXS/7ERy/YOumTji8M2RS','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 08:32:59',NULL),(151,'testuser1772353979381','iRahJv37Bz7GvQfvxKKL2AfxnRQbKPaSvMrh1+CxuhiJ7fehDI5g6if8mQPeJBHV','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:32:59','2026-03-01 08:32:59'),(152,'testuser1772353979440','AwOXxyFXrA+kbicehMGl4HEGDQHUhoSJ/RnntpNUiXrcMW8r4PQhbievt7tj50mp','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 08:32:59',NULL),(153,'testuser1772353979498','VWqegkKNr5ZLA/DSndDi3lhXAElTjicSPifAdsW4bxNGz8p05sJ7CXzWVlgirOOy','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:32:59',NULL),(154,'testuser1772353979554','0a7kOUD2hL+BQLRp0rWvKjkMtnSrljFy7x/A+nEZVZtbsz7KyrCJ6nmIf6oEzPlI','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:32:59',NULL),(155,'testuser1772353979612','2teBS2X3anseO59xjsqe0OhYyabvxQfPTGdpWQt8qyzuK7oA8/qgyrGNQQ5NMRNl','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:32:59',NULL),(156,'testuser1772353979653','lGBG9SpNojEqulOljtIuo8jgfiVyVEH3KUIE7MrlQ3CF5BXsFqcPGU9LXQi0O3fT','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:32:59',NULL),(157,'testuser1772353979694','k3ZnECklzYERb8sTDs0r1pWt3ImAOrXwqigBz6xLOsrqw7Gm2fkWkVish69R37gi','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:32:59',NULL),(159,'testuser1772353979748','Y/QzWOZLpyhDyr4MrekWe0DdHDIx75cJHw8c9wwu2k8MGrpdSq/bZ75yYDsu8/ov','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:32:59',NULL),(160,'testuser1772353979778','tqwO5ESQAGr/ADSdQCjLBiTuy8UrYu2YPdMAlu91LF6EAtoRuPfzWEp/qzUKPxb+','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:32:59',NULL),(161,'testuser1772354155060','kV1itSxI/9PlZFDm7ZUVYKichUZUQTju8aa5+xhKrs+WvzxRliUtfRG1xRSjZDZt','Test User','STAFF','test@example.com','0771234567',1,'2026-03-01 08:35:55',NULL),(162,'testuser1772354155170','iauq6K9YS7hseyUxeX3HplwGaAqwRB92ugTLRVf77Gjj+fzmyasVwE6g8EpL1PHv','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:35:55','2026-03-01 08:35:55'),(163,'testuser1772354155228','SYljGLZ7NINScCWc0cMOGoy/P+QV7XacKDssFPDmMbI61GsEES9V17ggtj3Gf0iu','Updated Name','STAFF','updated@example.com','0771234567',0,'2026-03-01 08:35:55',NULL),(164,'testuser1772354155287','REYMU+TZ4rN6yftVGZLsFjJWEA7h4AoLhfQFqyRzw5nbgczcidt2GNg4ZO514jBN','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:35:55',NULL),(165,'testuser1772354155342','PFrG/XCkASW9IKDPjfepoAYts3fTw4XtUFstixkZjcbK0yqE10J4cRrsP/NFhqM2','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:35:55',NULL),(166,'testuser1772354155400','sceCXNhyD9kco4MWEXEctklUYbxzIjwgubtBQr/RYLpWMJYsKaaDu+lQBtZaffc1','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:35:55',NULL),(167,'testuser1772354155439','ZTe3ni7i+NnkPlv4wVyetoW+K0DCb3OLlvqlBp3UymQzGEXUqF79fZHmyVQYrJQC','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:35:55',NULL),(168,'testuser1772354155478','FmyBufNFseWB+qQkcz3tLDZlr6VtlFXuJmF3+qR2Q2CjnHt7/uJU907ZtrW8bWKY','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:35:55',NULL),(170,'testuser1772354155530','s4t3hXYcBgt1yyz0M/VY8JOWH/sISRFdWycw+GKGq7amHztbpX7Ywvn+gAAdoob7','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:35:55',NULL),(171,'testuser1772354155557','nt2haoJRaFdlKgaJ7eYth+HHXrFgvqAgF4BjXi0Ywwz1Te+xcBMXMfKSil3QxLxU','Test User','STAFF','test@example.com','0771234567',0,'2026-03-01 08:35:55',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'oceanview_resort'
--

--
-- Dumping routines for database 'oceanview_resort'
--
/*!50003 DROP PROCEDURE IF EXISTS `CheckRoomAvailability` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckRoomAvailability`(
    IN p_check_in DATE,
    IN p_check_out DATE
)
BEGIN
    SELECT r.room_id, r.room_number, rt.type_name, rt.price_per_night
    FROM rooms r
    INNER JOIN room_types rt ON r.room_type_id = rt.room_type_id
    WHERE r.active = TRUE
    AND r.room_id NOT IN (
        SELECT room_id FROM reservations
        WHERE status IN ('CONFIRMED', 'CHECKED_IN')
        AND NOT (check_out_date <= p_check_in OR check_in_date >= p_check_out)
    )
    ORDER BY rt.price_per_night;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GenerateOccupancyReport` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GenerateOccupancyReport`(IN report_date DATE)
BEGIN
    SELECT 
        COUNT(DISTINCT r.room_id) as total_rooms,
        COUNT(DISTINCT CASE WHEN r.status = 'AVAILABLE' THEN r.room_id END) as available_rooms,
        COUNT(DISTINCT CASE WHEN r.status = 'OCCUPIED' THEN r.room_id END) as occupied_rooms,
        COUNT(DISTINCT CASE WHEN r.status = 'RESERVED' THEN r.room_id END) as reserved_rooms,
        ROUND((COUNT(DISTINCT CASE WHEN r.status IN ('OCCUPIED', 'RESERVED') THEN r.room_id END) / 
               COUNT(DISTINCT r.room_id) * 100), 2) as occupancy_rate
    FROM rooms r
    WHERE r.active = TRUE;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `available_rooms_view`
--

/*!50001 DROP VIEW IF EXISTS `available_rooms_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `available_rooms_view` AS select `r`.`room_id` AS `room_id`,`r`.`room_number` AS `room_number`,`r`.`floor` AS `floor`,`rt`.`type_name` AS `type_name`,`rt`.`capacity` AS `capacity`,`rt`.`price_per_night` AS `price_per_night`,`r`.`status` AS `status` from (`rooms` `r` join `room_types` `rt` on((`r`.`room_type_id` = `rt`.`room_type_id`))) where ((`r`.`active` = true) and (`r`.`status` = 'AVAILABLE')) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `current_occupancy_view`
--

/*!50001 DROP VIEW IF EXISTS `current_occupancy_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `current_occupancy_view` AS select `res`.`reservation_id` AS `reservation_id`,`res`.`reservation_number` AS `reservation_number`,`g`.`first_name` AS `first_name`,`g`.`last_name` AS `last_name`,`r`.`room_number` AS `room_number`,`rt`.`type_name` AS `type_name`,`res`.`check_in_date` AS `check_in_date`,`res`.`check_out_date` AS `check_out_date`,`res`.`status` AS `status` from (((`reservations` `res` join `guests` `g` on((`res`.`guest_id` = `g`.`guest_id`))) join `rooms` `r` on((`res`.`room_id` = `r`.`room_id`))) join `room_types` `rt` on((`r`.`room_type_id` = `rt`.`room_type_id`))) where (`res`.`status` in ('CONFIRMED','CHECKED_IN')) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `revenue_summary`
--

/*!50001 DROP VIEW IF EXISTS `revenue_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `revenue_summary` AS select cast(`res`.`created_at` as date) AS `booking_date`,count(0) AS `total_bookings`,sum(`res`.`total_amount`) AS `total_revenue`,avg(`res`.`total_amount`) AS `avg_booking_value` from `reservations` `res` where (`res`.`status` in ('CONFIRMED','CHECKED_IN','CHECKED_OUT')) group by cast(`res`.`created_at` as date) order by `booking_date` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-01 14:20:11
