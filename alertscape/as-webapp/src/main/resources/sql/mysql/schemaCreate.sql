CREATE TABLE `alert_major_tags` (
  `major_tag_id` bigint(20) unsigned NOT NULL auto_increment,
  `major_tag_name` varchar(100) NOT NULL,
  PRIMARY KEY  (`major_tag_id`),
  UNIQUE KEY `major_tag_name_unq` USING BTREE (`major_tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `alert_minor_tags` (
  `minor_tag_id` bigint(20) unsigned NOT NULL auto_increment,
  `alertid` bigint(20) unsigned zerofill NOT NULL,
  `minor_tag_name` varchar(100) NOT NULL,
  `minor_tag_value` int(11) default NULL,
  PRIMARY KEY  (`minor_tag_id`),
  UNIQUE KEY `minor_tag_name_unq` (`alertid`,`minor_tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `alert_source_props` (
  `name` varchar(50) NOT NULL,
  `value` varchar(2000) default NULL,
  `source_id` int(11) NOT NULL default '0',
  KEY `as_props_as_id_idx` (`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `alert_source_types` (
  `sid` int(10) unsigned NOT NULL auto_increment,
  `type` varchar(50) NOT NULL,
  `source_class` varchar(500) NOT NULL,
  `mapping_file` varchar(255) default NULL,
  PRIMARY KEY  (`sid`),
  UNIQUE KEY `as_type_type_idx` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
INSERT INTO `alert_source_types` VALUES  (1,'File','','fileOnrampMapping.xml'),
 (2,'DB','','dbOnrampMapping.xml');
CREATE TABLE `alert_sources` (
  `alert_source_id` int(11) NOT NULL,
  `alert_source_name` varchar(50) NOT NULL,
  `short_description` varchar(200) default NULL,
  `alert_id_seq` int(11) NOT NULL default '1',
  `alert_source_type_sid` int(10) unsigned NOT NULL,
  `active` tinyint(1) NOT NULL default '1',
  `configuration` longtext,
  PRIMARY KEY  (`alert_source_id`),
  KEY `as_type_fk` (`alert_source_type_sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `alert_sources` VALUES  (1,'UNKNOWN','The unknown source',1,1,0,null);
CREATE TABLE `alerts` (
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
  KEY `item_mgr_idx` (`item_manager`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `tree_configurations` (
  `sid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `configuration` longtext,
  PRIMARY KEY  (`sid`),
  UNIQUE KEY `idx_tree_config_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
