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
	`id_sweet`	TEXT NOT NULL,
	PRIMARY KEY(`name`)
);
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Agride','36','2');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Atcham','204','55');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Crocabulia','202','57');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Echo','201','54');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Meriana','205','58');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Ombre','50','35');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Oto-Mustam','22','36');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Rubilax','203','56');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Pandore','206','59');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Ush','207','60');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Julith','208','61');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Nidas','209','62');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Merkator','210','63');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Furye','211','64');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Brumen','212','65');
INSERT INTO `Server` (name,id_dofus,id_sweet) VALUES ('Ilyzaelle','222','66');
DROP TABLE IF EXISTS `RSS_Finder`;
CREATE TABLE IF NOT EXISTS `RSS_Finder` (
	`id_chan`	TEXT NOT NULL,
	`id_guild`	TEXT NOT NULL,
	`last_update`	INTEGER NOT NULL,
	FOREIGN KEY(`id_guild`) REFERENCES `Guild`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(`id_chan`,`id_guild`)
);
DROP TABLE IF EXISTS `Portal_Tracker`;
CREATE TABLE IF NOT EXISTS `Portal_Tracker` (
	`id_chan`	TEXT NOT NULL,
	`id_guild`	TEXT NOT NULL,
	PRIMARY KEY(`id_chan`,`id_guild`),
	FOREIGN KEY(`id_guild`) REFERENCES `Guild`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
);
DROP TABLE IF EXISTS `Portal_Guild`;
CREATE TABLE IF NOT EXISTS `Portal_Guild` (
	`name_portal`	TEXT NOT NULL,
	`id_guild`	TEXT NOT NULL,
	`pos`	TEXT,
	`utilisation`	INTEGER DEFAULT -1,
	`creation`	INTEGER DEFAULT -1,
	`last_update`	INTEGER DEFAULT -1,
	`creation_source`	TEXT,
	`update_source`	TEXT,
	PRIMARY KEY(`name_portal`,`id_guild`),
	FOREIGN KEY(`id_guild`) REFERENCES `Guild`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
);
DROP TABLE IF EXISTS `Portal`;
CREATE TABLE IF NOT EXISTS `Portal` (
	`name`	TEXT NOT NULL,
	`url`	TEXT,
	`color`	INTEGER,
	PRIMARY KEY(`name`)
);
INSERT INTO `Portal` (name,url,color) VALUES ('Enutrosor','http://www.sweet.ovh/images/portals/Enutrosor.png',16777064);
INSERT INTO `Portal` (name,url,color) VALUES ('Srambad','http://www.sweet.ovh/images/portals/Srambad.png',3097192);
INSERT INTO `Portal` (name,url,color) VALUES ('Xélorium','http://www.sweet.ovh/images/portals/Xelorium.png',7229801);
INSERT INTO `Portal` (name,url,color) VALUES ('Ecaflipus','http://www.sweet.ovh/images/portals/Ecaflipus.png',13490334);
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
