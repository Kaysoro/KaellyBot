BEGIN TRANSACTION;
DROP TABLE IF EXISTS `Twitter`;
CREATE TABLE IF NOT EXISTS `Twitter` (
	`id_chan`	TEXT NOT NULL,
	`id_guild`	TEXT NOT NULL,
	FOREIGN KEY(`id_guild`) REFERENCES `Guild`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(`id_chan`,`id_guild`)
);
DROP TABLE IF EXISTS `Server`;
CREATE TABLE IF NOT EXISTS `Server` (
	`name`	TEXT NOT NULL,
	`id_dofus`	TEXT NOT NULL,
	PRIMARY KEY(`name`)
);
INSERT INTO `Server` (name,id_dofus) VALUES ('Brutas','407');
INSERT INTO `Server` (name,id_dofus) VALUES ('Dodge','406');
INSERT INTO `Server` (name,id_dofus) VALUES ('Grandapan','401');
INSERT INTO `Server` (name,id_dofus) VALUES ('Herdegrize','405');
INSERT INTO `Server` (name,id_dofus) VALUES ('Oshimo','403');
INSERT INTO `Server` (name,id_dofus) VALUES ('Terra Cogita','404');
DROP TABLE IF EXISTS `RSS_Finder`;
CREATE TABLE IF NOT EXISTS `RSS_Finder` (
	`id_chan`	TEXT NOT NULL,
	`id_guild`	TEXT NOT NULL,
	`last_update`	INTEGER NOT NULL,
	FOREIGN KEY(`id_guild`) REFERENCES `Guild`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(`id_chan`,`id_guild`)
);
DROP TABLE IF EXISTS `Order_User`;
CREATE TABLE IF NOT EXISTS `Order_User` (
	`id_user`	TEXT NOT NULL,
	`server_dofus`	TEXT NOT NULL,
	`name_city`	TEXT NOT NULL,
	`name_order`	TEXT NOT NULL,
	`level`	INTEGER NOT NULL,
	PRIMARY KEY(`id_user`,`server_dofus`,`name_city`,`name_order`)
);
DROP TABLE IF EXISTS `Job_User`;
CREATE TABLE IF NOT EXISTS `Job_User` (
	`name_job`	TEXT NOT NULL,
	`id_user`	TEXT NOT NULL,
	`server_dofus`	TEXT NOT NULL,
	`level`	INTEGER NOT NULL,
	PRIMARY KEY(`name_job`,`id_user`,`server_dofus`)
);
DROP TABLE IF EXISTS `Guild`;
CREATE TABLE IF NOT EXISTS `Guild` (
	`id`	TEXT NOT NULL,
	`name`	TEXT,
	`prefixe`	TEXT DEFAULT "!",
	`server_dofus`	TEXT,
	`lang`	TEXT DEFAULT 'FR',
	PRIMARY KEY(`id`)
);
DROP TABLE IF EXISTS `Command_Statistics`;
CREATE TABLE IF NOT EXISTS `Command_Statistics` (
	`name_command`	TEXT NOT NULL,
	`instant`	INTEGER NOT NULL
);
DROP TABLE IF EXISTS `Command_Guild`;
CREATE TABLE IF NOT EXISTS `Command_Guild` (
	`id_guild`	TEXT NOT NULL,
	`name_command`	TEXT NOT NULL,
	PRIMARY KEY(`id_guild`,`name_command`),
	FOREIGN KEY(`id_guild`) REFERENCES `Guild`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
);
DROP TABLE IF EXISTS `Channel_Language`;
CREATE TABLE IF NOT EXISTS `Channel_Language` (
	`id_chan`	TEXT NOT NULL,
	`lang`	TEXT NOT NULL,
	PRIMARY KEY(`id_chan`)
);
DROP TABLE IF EXISTS `Almanax_Calendar`;
CREATE TABLE IF NOT EXISTS `Almanax_Calendar` (
	`id_chan`	TEXT NOT NULL,
	`id_guild`	TEXT NOT NULL,
	PRIMARY KEY(`id_chan`,`id_guild`),
	FOREIGN KEY(`id_guild`) REFERENCES `Guild`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
);
COMMIT;