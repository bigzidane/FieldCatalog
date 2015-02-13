-- Host: localhost    Database: fieldcatalog
-- ------------------------------------------------------
-- Server version	5.6.21-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cust_field`
--

DROP TABLE IF EXISTS `cust_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TARGET` varchar(45) NOT NULL,
  `FIELD_TYPE` varchar(45) NOT NULL,
  `FIELD_LABEL` varchar(45) NOT NULL,
  `FIELD_PREFACE` varchar(45) DEFAULT NULL,
  `FIELD_TITLE` varchar(255) DEFAULT NULL,
  `FIELD_ISREQUIRED` varchar(1) DEFAULT NULL,
  `FIELD_VALUE` varchar(1024) DEFAULT NULL,
  `FIELD_VALUE_DEF` varchar(1024) DEFAULT NULL,
  `FIELD_ID` varchar(45) NOT NULL,
  `FIELD_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_field`
--

LOCK TABLES `cust_field` WRITE;
/*!40000 ALTER TABLE `cust_field` DISABLE KEYS */;
INSERT INTO `cust_field` VALUES (30,'Screen','INPUT_TEXT','Developer Notes','','','N','',NULL,'DeveloperNote',1);
/*!40000 ALTER TABLE `cust_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TARGET` varchar(45) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `EVENT_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (29,'Field','Click',1);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field`
--

DROP TABLE IF EXISTS `field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(45) NOT NULL,
  `IS_ACTIVE` varchar(1) DEFAULT 'Y',
  `EFFECTIVE_DATE` datetime NOT NULL,
  `EXPIRATION_DATE` datetime DEFAULT NULL,
  `OWNER_ID` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_OWNER_ID` int(11) NOT NULL,
  `FILE` varchar(100) DEFAULT NULL,
  `BUSINESS_RULE` text,
  `CODE_RULE` text,
  `TAG_LEFT` double DEFAULT NULL,
  `TAG_TOP` double DEFAULT NULL,
  `TAG_WIDTH` double DEFAULT NULL,
  `TAG_HEIGHT` double DEFAULT NULL,
  `SCREEN_ID` int(11) NOT NULL,
  `DEPENDENT_ID` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field`
--

LOCK TABLES `field` WRITE;
/*!40000 ALTER TABLE `field` DISABLE KEYS */;
INSERT INTO `field` VALUES (98,'Insert button','To insert object','Y','2015-02-08 16:00:00',NULL,4,'2015-02-09 11:24:52','2015-02-09 11:24:52',4,NULL,'','',110,31,42,18,119,NULL);
/*!40000 ALTER TABLE `field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_cust_field`
--

DROP TABLE IF EXISTS `field_cust_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_cust_field` (
  `CUST_FIELD_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `VALUE` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`CUST_FIELD_ID`,`FIELD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_cust_field`
--

LOCK TABLES `field_cust_field` WRITE;
/*!40000 ALTER TABLE `field_cust_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_cust_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_event`
--

DROP TABLE IF EXISTS `field_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_event` (
  `EVENT_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  `BUSINESS_RULE` text,
  `CODE_RULE` text,
  PRIMARY KEY (`EVENT_ID`,`FIELD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_event`
--

LOCK TABLES `field_event` WRITE;
/*!40000 ALTER TABLE `field_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_group`
--

DROP TABLE IF EXISTS `field_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_group` (
  `FIELD_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  `LEVEL` varchar(1) NOT NULL,
  PRIMARY KEY (`FIELD_ID`,`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_group`
--

LOCK TABLES `field_group` WRITE;
/*!40000 ALTER TABLE `field_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_message`
--

DROP TABLE IF EXISTS `field_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_message` (
  `FIELD_ID` int(11) NOT NULL,
  `MESSAGE_ID` int(11) NOT NULL,
  `IS_ACTIVE` varchar(1) NOT NULL,
  PRIMARY KEY (`FIELD_ID`,`MESSAGE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_message`
--

LOCK TABLES `field_message` WRITE;
/*!40000 ALTER TABLE `field_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_tag`
--

DROP TABLE IF EXISTS `field_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_tag` (
  `TAG_ID` int(11) NOT NULL,
  `FIELD_ID` int(11) NOT NULL,
  PRIMARY KEY (`TAG_ID`,`FIELD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_tag`
--

LOCK TABLES `field_tag` WRITE;
/*!40000 ALTER TABLE `field_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(45) NOT NULL,
  `IS_ACTIVE` varchar(1) DEFAULT 'Y',
  `EFFECTIVE_DATE` datetime NOT NULL,
  `EXPIRATION_DATE` datetime DEFAULT NULL,
  `OWNER_ID` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_OWNER_ID` int(11) NOT NULL,
  `FILE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (106,'Demo','This is a demo','Y','2015-02-08 16:00:00','2016-02-08 16:00:00',4,'2015-02-09 10:48:58','2015-02-09 10:48:58',4,NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_cust_field`
--

DROP TABLE IF EXISTS `product_cust_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_cust_field` (
  `CUST_FIELD_ID` int(11) NOT NULL,
  `PRODUCT_ID` int(11) NOT NULL,
  `VALUE` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`CUST_FIELD_ID`,`PRODUCT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_cust_field`
--

LOCK TABLES `product_cust_field` WRITE;
/*!40000 ALTER TABLE `product_cust_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_cust_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_group`
--

DROP TABLE IF EXISTS `product_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_group` (
  `PRODUCT_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  `LEVEL` varchar(1) NOT NULL,
  PRIMARY KEY (`PRODUCT_ID`,`GROUP_ID`,`LEVEL`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_group`
--

LOCK TABLES `product_group` WRITE;
/*!40000 ALTER TABLE `product_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_tag`
--

DROP TABLE IF EXISTS `product_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_tag` (
  `TAG_ID` int(11) NOT NULL,
  `PRODUCT_ID` int(11) NOT NULL,
  PRIMARY KEY (`TAG_ID`,`PRODUCT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_tag`
--

LOCK TABLES `product_tag` WRITE;
/*!40000 ALTER TABLE `product_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_version`
--

DROP TABLE IF EXISTS `product_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_version` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PRODUCT_ID` int(11) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_version`
--

LOCK TABLES `product_version` WRITE;
/*!40000 ALTER TABLE `product_version` DISABLE KEYS */;
INSERT INTO `product_version` VALUES (33,106,'01.01.000',NULL,'Y');
/*!40000 ALTER TABLE `product_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(2,'DESIGNER'),(3,'ROLE_VIEWER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screen`
--

DROP TABLE IF EXISTS `screen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screen` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PRODUCT_VERSION_ID` int(11) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(45) NOT NULL,
  `IS_ACTIVE` varchar(1) DEFAULT 'Y',
  `EFFECTIVE_DATE` datetime NOT NULL,
  `EXPIRATION_DATE` datetime DEFAULT NULL,
  `OWNER_ID` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_OWNER_ID` int(11) NOT NULL,
  `FILE` varchar(100) DEFAULT NULL,
  `BUSINESS_RULE` text,
  `CODE_RULE` text,
  `PARENT_ID` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screen`
--

LOCK TABLES `screen` WRITE;
/*!40000 ALTER TABLE `screen` DISABLE KEYS */;
INSERT INTO `screen` VALUES (119,33,'New Micorosoft Word','New Micorosoft Word','Y','2015-02-08 16:00:00',NULL,4,'2015-02-09 11:10:38','2015-02-09 11:10:38',4,'winword_20102015-02-09-11.10.38.png','<span style=\"font-weight: bold;\">Rule 1</span><br>Word should have automatically content recolonization during copy/paste<br><br><span style=\"font-weight: bold;\">Rule 2</span><br><br><ol><li>Item 2.1</li><li>Item 2.2</li><li><br></li></ol>','if (action == \"Paste) then\r\n    {\r\n    	executePaste()\r\n    }',NULL),(120,33,'Open document','Open document','Y','2015-02-08 16:00:00',NULL,4,'2015-02-09 11:48:32','2015-02-09 11:48:32',4,NULL,'','',NULL),(121,33,'Print Document','Print Document','Y','2015-02-08 16:00:00',NULL,4,'2015-02-09 11:49:15','2015-02-09 11:49:15',4,NULL,'','',NULL),(122,33,'Page Setup','Page Setup','Y','2015-02-08 16:00:00',NULL,4,'2015-02-09 11:49:47','2015-02-09 11:49:47',4,NULL,'','',NULL),(123,33,'Open dialog','Open dialog','Y','2015-02-08 16:00:00',NULL,4,'2015-02-09 12:01:35','2015-02-09 12:01:35',4,NULL,'','',',119,120,');
/*!40000 ALTER TABLE `screen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screen_cust_field`
--

DROP TABLE IF EXISTS `screen_cust_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screen_cust_field` (
  `CUST_FIELD_ID` int(11) NOT NULL,
  `SCREEN_ID` int(11) NOT NULL,
  `VALUE` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`CUST_FIELD_ID`,`SCREEN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screen_cust_field`
--

LOCK TABLES `screen_cust_field` WRITE;
/*!40000 ALTER TABLE `screen_cust_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `screen_cust_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screen_event`
--

DROP TABLE IF EXISTS `screen_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screen_event` (
  `EVENT_ID` int(11) NOT NULL,
  `SCREEN_ID` int(11) NOT NULL,
  `BUSINESS_RULE` text,
  `CODE_RULE` text,
  PRIMARY KEY (`EVENT_ID`,`SCREEN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screen_event`
--

LOCK TABLES `screen_event` WRITE;
/*!40000 ALTER TABLE `screen_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `screen_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screen_group`
--

DROP TABLE IF EXISTS `screen_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screen_group` (
  `SCREEN_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  `LEVEL` varchar(1) NOT NULL,
  PRIMARY KEY (`SCREEN_ID`,`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screen_group`
--

LOCK TABLES `screen_group` WRITE;
/*!40000 ALTER TABLE `screen_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `screen_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screen_message`
--

DROP TABLE IF EXISTS `screen_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screen_message` (
  `SCREEN_ID` int(11) NOT NULL,
  `MESSAGE_ID` int(11) NOT NULL,
  `IS_ACTIVE` varchar(1) NOT NULL,
  PRIMARY KEY (`SCREEN_ID`,`MESSAGE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screen_message`
--

LOCK TABLES `screen_message` WRITE;
/*!40000 ALTER TABLE `screen_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `screen_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `screen_tag`
--

DROP TABLE IF EXISTS `screen_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `screen_tag` (
  `TAG_ID` int(11) NOT NULL,
  `SCREEN_ID` int(11) NOT NULL,
  PRIMARY KEY (`TAG_ID`,`SCREEN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screen_tag`
--

LOCK TABLES `screen_tag` WRITE;
/*!40000 ALTER TABLE `screen_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `screen_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_group`
--

DROP TABLE IF EXISTS `system_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `IS_ACTIVE` varchar(1) DEFAULT 'Y',
  `EFFECTIVE_DATE` datetime NOT NULL,
  `EXPIRATION_DATE` datetime DEFAULT NULL,
  `OWNER_ID` int(11) NOT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_OWNER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_group`
--

LOCK TABLES `system_group` WRITE;
/*!40000 ALTER TABLE `system_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_message`
--

DROP TABLE IF EXISTS `system_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_message` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `IS_ACTIVE` varchar(1) DEFAULT 'Y',
  `EFFECTIVE_DATE` datetime NOT NULL,
  `EXPIRATION_DATE` datetime DEFAULT NULL,
  `OWNER_ID` int(11) NOT NULL,
  `CREATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_OWNER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_message`
--

LOCK TABLES `system_message` WRITE;
/*!40000 ALTER TABLE `system_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `tag_PK` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EMAIL` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `FIRST_NAME` varchar(45) NOT NULL,
  `LAST_NAME` varchar(45) NOT NULL,
  `IS_LOCKED` varchar(1) DEFAULT 'N',
  `IS_ACTIVE` varchar(1) DEFAULT 'Y',
  `CREATED_DATE` datetime NOT NULL,
  `LAST_UPDATED_DATE` datetime DEFAULT NULL,
  `LAST_UPDATED_OWNER_ID` int(11) DEFAULT NULL,
  `MIDDLE_NAME` varchar(45) DEFAULT NULL,
  `EFFECTIVE_DATE` datetime NOT NULL,
  `EXPIRATION_DATE` datetime DEFAULT NULL,
  `COMPANY` varchar(100) DEFAULT NULL,
  `EMPLOYEES` int(11) DEFAULT NULL,
  `UUID` varchar(100) DEFAULT NULL,
  `PHONE` varchar(100) DEFAULT NULL,
  `IS_APPROVED` varchar(1) DEFAULT 'Y',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group` (
  `USER_ID` int(11) NOT NULL,
  `GROUP_ID` int(11) NOT NULL,
  `IS_ACTIVE` varchar(1) DEFAULT 'Y',
  PRIMARY KEY (`USER_ID`,`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_group`
--

LOCK TABLES `user_group` WRITE;
/*!40000 ALTER TABLE `user_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `USER_ID` int(11) NOT NULL,
  `ROLE_ID` int(11) NOT NULL,
  `IS_ACTIVE` varchar(1) DEFAULT 'Y',
  `EFFECTIVE_DATE` datetime DEFAULT NULL,
  `EXPIRED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`USER_ID`,`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `login` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `version`
--

DROP TABLE IF EXISTS `version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `version` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `version`
--

LOCK TABLES `version` WRITE;
/*!40000 ALTER TABLE `version` DISABLE KEYS */;
/*!40000 ALTER TABLE `version` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-02-09 12:52:34
