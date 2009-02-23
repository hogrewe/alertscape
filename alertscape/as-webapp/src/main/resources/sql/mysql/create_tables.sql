CREATE DATABASE  `alertscape`.`alertscape` ;

CREATE TABLE  `alertscape`.`alert_sources` (
  `alert_source_id` int(11) NOT NULL,
  `alert_source_name` varchar(50) NOT NULL,
  `short_description` varchar(200) default NULL,
  PRIMARY KEY  (`alert_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `alertscape`.`alerts` (
  `alertid` bigint(20) unsigned zerofill NOT NULL,
  `short_description` varchar(200) default NULL,
  `long_description` varchar(2000) default NULL,
  `severity` varchar(20) NOT NULL,
  `count` int(11) NOT NULL,
  `source_id` int(11) NOT NULL,
  `first_occurence` datetime NOT NULL,
  `last_occurence` datetime NOT NULL,
  `item` varchar(200) NULL,
  `item_type` varchar(200) NULL,
  `item_manager` varchar(200) NULL,
  `item_manager_type` varchar(200) NULL,
  PRIMARY KEY  (`alertid`),
  KEY `alert_alert_source_fk` (`source_id`),
  CONSTRAINT `alert_alert_source_fk` FOREIGN KEY (`source_id`) REFERENCES `alert_sources` (`alert_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `alertscape`.`alert_major_tags` (
  `major_tag_id` bigint(20) unsigned NOT NULL auto_increment,
  `alertid` bigint(20) unsigned zerofill NOT NULL,
  `major_tag_name` varchar(100) NOT NULL,
  `major_tag_value` varchar(2000) default NULL,
  PRIMARY KEY  (`major_tag_id`),
  UNIQUE KEY `major_tag_name_unq` (`major_tag_name`,`alertid`),
  KEY `major_tag_alert_fk` (`alertid`),
  CONSTRAINT `major_tag_alert_fk` FOREIGN KEY (`alertid`) REFERENCES `alerts` (`alertid`) ON DELETE CASCADE
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


