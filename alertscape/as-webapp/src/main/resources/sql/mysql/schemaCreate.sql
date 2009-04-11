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
  `alert_source_id` int(10) unsigned NOT NULL auto_increment,
  `alert_source_name` varchar(50) NOT NULL,
  `short_description` varchar(200) default NULL,
  `alert_id_seq` int(11) NOT NULL default '1',
  `alert_source_type_sid` int(10) unsigned NOT NULL,
  `active` tinyint(1) NOT NULL default '1',
  `configuration` longtext,
  PRIMARY KEY  (`alert_source_id`),
  KEY `as_type_fk` (`alert_source_type_sid`),
  CONSTRAINT `as_type_fk` FOREIGN KEY (`alert_source_type_sid`) REFERENCES `alert_source_types` (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `alert_sources` VALUES  (1,'UNKNOWN','The unknown source',1,1,0,NULL);
CREATE TABLE `alert_source_props` (
  `name` varchar(50) NOT NULL,
  `value` varchar(2000) default NULL,
  `source_id` int(10) unsigned NOT NULL default '0',
  KEY `as_props_as_id_idx` (`source_id`),
  CONSTRAINT `as_props_as_fk` FOREIGN KEY (`source_id`) REFERENCES `alert_sources` (`alert_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
CREATE TABLE `alerts` (
  `alertid` bigint(20) unsigned zerofill NOT NULL default '00000000000000000000',
  `short_description` varchar(200) default NULL,
  `long_description` varchar(2000) default NULL,
  `severity` varchar(20) NOT NULL,
  `count` int(11) NOT NULL,
  `source_id` int(10) unsigned NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
CREATE TABLE `alertscape_properties` (
  `sid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(200) NOT NULL,
  `value` int(11) default NULL,
  PRIMARY KEY  (`sid`),
  KEY `as_prop_name_idx` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `as_user` (
  `user_id` int(10) unsigned NOT NULL auto_increment,
  `username` varchar(200) NOT NULL,
  `password` varchar(200) default NULL,
  `fullname` varchar(400) default NULL,
  `email` varchar(200) NOT NULL,
  PRIMARY KEY  (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
CREATE TABLE `attribute_definitions` (
  `attribute_definition_id` int(11) NOT NULL auto_increment,
  `attribute_name` varchar(100) NOT NULL,
  `active` tinyint(1) NOT NULL default '1',
  `attribute_display_name` varchar(100) default NULL,
  PRIMARY KEY  (`attribute_definition_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ;
INSERT INTO `attribute_definitions` VALUES  (1,'customer',1,NULL),
 (2,'ticket',1,NULL),
 (3,'region',1,NULL),
 (4,'stateprovince',1,NULL),
 (5,'city',1,NULL),
 (6,'folder',1,NULL);
CREATE TABLE `equators` (
  `sid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(200) NOT NULL,
  `deleted` tinyint(1) default '1',
  PRIMARY KEY  (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
CREATE TABLE `equator_attributes` (
  `sid` int(10) unsigned NOT NULL auto_increment,
  `attribute_name` varchar(100) NOT NULL,
  `equator_sid` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`sid`),
  KEY `eq_attr_eq_fk` (`equator_sid`),
  CONSTRAINT `eq_attr_eq_fk` FOREIGN KEY (`equator_sid`) REFERENCES `equators` (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
CREATE TABLE `ext_alert_attributes` (
  `alertid` bigint(20) unsigned zerofill NOT NULL default '00000000000000000000',
  `customer` varchar(200) default NULL,
  `ticket` varchar(100) default NULL,
  `region` varchar(50) default NULL,
  `stateprovince` varchar(50) default NULL,
  `city` varchar(100) default NULL,
  `folder` varchar(200) default NULL,
  `source_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  USING BTREE (`alertid`,`source_id`),
  CONSTRAINT `ext_attr_alert_fk` FOREIGN KEY (`alertid`, `source_id`) REFERENCES `alerts` (`alertid`, `source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `tree_configurations` (
  `sid` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `configuration` longtext,
  PRIMARY KEY  (`sid`),
  UNIQUE KEY `idx_tree_config_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
CREATE TABLE `alert_categories` (
  `sid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `alertid` bigint(20) unsigned zerofill NOT NULL,
  `source_id` int(10) unsigned NOT NULL,
  `category_sid` int(10) unsigned NOT NULL,
  `value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`sid`),
  KEY `alert_cat_cat_sid_fk` (`category_sid`),
  KEY `alert_cat_source_alert_fk` (`alertid`,`source_id`),
  CONSTRAINT `alert_cat_src_alert_fk` FOREIGN KEY (`alertid`, `source_id`) REFERENCES `alerts` (`alertid`, `source_id`) ON DELETE CASCADE,
  CONSTRAINT `alert_cat_cat_sid_fk` FOREIGN KEY (`category_sid`) REFERENCES `category_def` (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
CREATE TABLE `alert_labels` (
  `sid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `source_id` int(10) unsigned NOT NULL,
  `alertid` bigint(20) unsigned NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`sid`),
  KEY `alert_label_source_alertid_fk` (`alertid`,`source_id`),
  KEY `alert_label_name_idx` (`name`),
  CONSTRAINT `alert_label_source_alertid_fk` FOREIGN KEY (`alertid`, `source_id`) REFERENCES `alerts` (`alertid`, `source_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `category_def` (
  `sid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `allow_custom` tinyint(1) DEFAULT '1',
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`sid`),
  UNIQUE KEY `cat_def_name_unq` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
CREATE TABLE `category_value_def` (
  `sid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `category_sid` int(10) unsigned NOT NULL,
  `value` varchar(200) NOT NULL,
  `is_default` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`sid`),
  UNIQUE KEY `cat_value_def_cat_value_unq` (`category_sid`,`value`),
  KEY `cat_value_def_cat_sid_idx` (`category_sid`),
  CONSTRAINT `cat_value_def_cat_sid_fk` FOREIGN KEY (`category_sid`) REFERENCES `category_def` (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
