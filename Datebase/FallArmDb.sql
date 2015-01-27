CREATE DATABASE  IF NOT EXISTS `fall_arm` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `fall_arm`;

--
-- Drop all tables in this database
--

DROP TABLE IF EXISTS `fall_record`;
DROP TABLE IF EXISTS `emergency_contact_list`;
DROP TABLE IF EXISTS `patient`;
DROP TABLE IF EXISTS `nurse`;

--
-- Table structure for table `nurse`
--

DROP TABLE IF EXISTS `nurse`;
CREATE TABLE `nurse` (
  `nurse_id` int(7) NOT NULL AUTO_INCREMENT,
  `email_address` varchar(100) NOT NULL,
  `password` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `gender` int(1) DEFAULT '0',
  `birthdate` date DEFAULT NULL,
  PRIMARY KEY (`nurse_id`),
  UNIQUE KEY `email_address_UNIQUE` (`email_address`)
) ENGINE=InnoDB AUTO_INCREMENT=500003 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `nurse`
--

LOCK TABLES `nurse` WRITE;
INSERT INTO `nurse` VALUES (500001,'lhc@hotmail.com','123','Hanchun','Lyu',1,'1988-01-01'),(500002,'sqy@hotmail.com','123','Qianyu','Shao',0,'1992-07-19');
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient` (
  `patient_id` int(7) NOT NULL AUTO_INCREMENT,
  `email_address` varchar(100) NOT NULL,
  `password` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `gender` int(1) NOT NULL DEFAULT '1',
  `birthdate` date DEFAULT NULL,
  `home_address` varchar(255) NOT NULL,
  `related_nurse_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`patient_id`),
  UNIQUE KEY `email_address_UNIQUE` (`email_address`),
  KEY `fk_patient_1_idx` (`related_nurse_id`),
  CONSTRAINT `fk_patient_1` FOREIGN KEY (`related_nurse_id`) REFERENCES `nurse` (`nurse_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100005 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
INSERT INTO `patient` VALUES (100001,'lhz@hotmail.com','123','Haizhou','Liu',1,'1988-10-29','33331 Jamie Cir, Fremont, CA 94555',500001),(100002,'lc@hotmail.com','123','Chang','Lv',0,'1990-01-01','123 ABC, Fremont, CA ',500002),(100003,'cyf@hotmail.com','123','Yufu','Cheng',1,'1987-01-01','123 ABC, Fremont, CA',500001),(100004,'ff@hotmail.com','123','Fan','Feng',1,'1990-01-01','123 ABC, Fremont, CA',500002);
UNLOCK TABLES;

--
-- Table structure for table `emergency_contact_list`
--

DROP TABLE IF EXISTS `emergency_contact_list`;
CREATE TABLE `emergency_contact_list` (
  `contact_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(7) NOT NULL,
  `email_address` varchar(100) NOT NULL,
  `phone` varchar(12) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `gender` int(1) DEFAULT '1',
  PRIMARY KEY (`contact_id`),
  KEY `fk_emergency_contact_list_1_idx` (`patient_id`),
  CONSTRAINT `fk_emergency_contact_list_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `emergency_contact_list`
--

LOCK TABLES `emergency_contact_list` WRITE;
INSERT INTO `emergency_contact_list` VALUES (1,100001,'lhz213@hotmail.com','203-683-8689','Krystal','Shao',0),(2,100001,'lhz213_game@hotmail.com','203-683-8689','Hanchun','Lyu',1);
UNLOCK TABLES;


--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `fall_record`;
CREATE TABLE `fall_record` (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(7) NOT NULL,
  `datetime` datetime DEFAULT NULL,
  `acc_x` double DEFAULT NULL,
  `acc_y` double DEFAULT NULL,
  `acc_z` double DEFAULT NULL,
  `gyr_x` double DEFAULT NULL,
  `gyr_y` double DEFAULT NULL,
  `gyr_z` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `state` int(11) DEFAULT '-1',
  PRIMARY KEY (`record_id`),
  KEY `fk_record_1_idx` (`patient_id`),
  CONSTRAINT `fk_record_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fall_record`
--

LOCK TABLES `fall_record` WRITE;
UNLOCK TABLES;
