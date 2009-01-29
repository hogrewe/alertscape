-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.51a-3ubuntu5.4-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema alertscape
--

CREATE DATABASE IF NOT EXISTS alertscape;
USE alertscape;
CREATE TABLE  `alertscape`.`alert_major_tags` (
  `major_tag_id` bigint(20) unsigned NOT NULL auto_increment,
  `major_tag_name` varchar(100) NOT NULL,
  PRIMARY KEY  (`major_tag_id`),
  UNIQUE KEY `major_tag_name_unq` USING BTREE (`major_tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE  `alertscape`.`alert_minor_tags` (
  `minor_tag_id` bigint(20) unsigned NOT NULL auto_increment,
  `alertid` bigint(20) unsigned zerofill NOT NULL,
  `minor_tag_name` varchar(100) NOT NULL,
  `minor_tag_value` int(11) default NULL,
  PRIMARY KEY  (`minor_tag_id`),
  UNIQUE KEY `minor_tag_name_unq` (`alertid`,`minor_tag_name`),
  CONSTRAINT `minor_tag_alert_fk` FOREIGN KEY (`alertid`) REFERENCES `alerts` (`alertid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE  `alertscape`.`alert_sources` (
  `alert_source_id` int(11) NOT NULL,
  `alert_source_name` varchar(50) NOT NULL,
  `short_description` varchar(200) default NULL,
  `alert_id_seq` int(11) NOT NULL default '1',
  PRIMARY KEY  (`alert_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `alertscape`.`alert_sources` VALUES  (1,'UNKNOWN','The unknown source',0),
 (2,'KYRIAKI_NET','requests from kyriaki.net',0),
 (3,'HTS_COM','requests from healthtakenseriously.com',0),
 (4,'ATHENS_SYSLOG','syslog events from athens',0),
 (5,'SANDJ_US','requests from sandj.us',1),
 (6,'INTERMAPPER','Alerts pulled from intermapper',1),
 (7,'SYSLOG','Alerts pulled from syslog',1);
CREATE TABLE  `alertscape`.`alerts` (
  `alertid` bigint(20) unsigned zerofill NOT NULL default '00000000000000000000',
  `short_description` varchar(200) default NULL,
  `long_description` varchar(2000) default NULL,
  `severity` varchar(20) NOT NULL,
  `count` int(11) NOT NULL,
  `source_id` int(11) NOT NULL,
  `first_occurence` datetime NOT NULL,
  `last_occurence` datetime NOT NULL,
  `create_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `item` varchar(200) default NULL,
  `item_type` varchar(200) default NULL,
  `item_manager` varchar(200) default NULL,
  `item_manager_type` varchar(200) default NULL,
  `type` varchar(200) default NULL,
  `acknowledged_by` varchar(200) default NULL,
  PRIMARY KEY  USING BTREE (`alertid`,`source_id`),
  KEY `alert_alert_source_fk` (`source_id`),
  KEY `item_idx` (`item`),
  KEY `type_idx` (`type`),
  KEY `long_desc_idx` (`long_description`(255)),
  KEY `item_mgr_idx` (`item_manager`),
  CONSTRAINT `alert_alert_source_fk` FOREIGN KEY (`source_id`) REFERENCES `alert_sources` (`alert_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 245760 kB; (`source_id`) REFER `alertscape/aler';
CREATE TABLE  `alertscape`.`as_user` (
  `user_id` int(10) unsigned NOT NULL auto_increment,
  `username` varchar(200) NOT NULL,
  `password` varchar(200) default NULL,
  `fullname` varchar(400) default NULL,
  `email` varchar(200) NOT NULL,
  PRIMARY KEY  (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
INSERT INTO `alertscape`.`as_user` VALUES  (3,'josh','5bf1fd927dfb8679496a2e6cf00cbe50c1c87145','Joshua Hogrewe','josh@alertscape.com'),
 (4,'scott','625600233CB3BCAB32268C17610882E0FDAED295','Scott Nusz','scott@alertscape.com');
CREATE TABLE  `alertscape`.`attribute_definitions` (
  `attribute_definition_id` int(11) NOT NULL auto_increment,
  `attribute_name` varchar(100) NOT NULL,
  `active` tinyint(1) NOT NULL default '1',
  `attribute_display_name` varchar(100) default NULL,
  PRIMARY KEY  (`attribute_definition_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 18432 kB';
INSERT INTO `alertscape`.`attribute_definitions` VALUES  (1,'customer',1,NULL),
 (2,'ticket',1,NULL),
 (3,'region',1,NULL),
 (4,'stateprovince',1,NULL),
 (5,'city',1,NULL),
 (6,'folder',1,NULL);
CREATE TABLE  `alertscape`.`ext_alert_attributes` (
  `alertid` bigint(20) unsigned zerofill NOT NULL default '00000000000000000000',
  `customer` varchar(200) default NULL,
  `ticket` varchar(100) default NULL,
  `region` varchar(50) default NULL,
  `stateprovince` varchar(50) default NULL,
  `city` varchar(100) default NULL,
  `folder` varchar(200) default NULL,
  `source_id` int(11) NOT NULL default '0',
  PRIMARY KEY  USING BTREE (`alertid`,`source_id`),
  CONSTRAINT `ext_attr_alert_fk` FOREIGN KEY (`alertid`, `source_id`) REFERENCES `alerts` (`alertid`, `source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
