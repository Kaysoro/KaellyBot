BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Server_Label" (
	"server"	TEXT NOT NULL,
	"language"	TEXT NOT NULL,
	"label"	TEXT NOT NULL,
	PRIMARY KEY("server","language")
);
CREATE TABLE IF NOT EXISTS "Channel_Server" (
	"id_chan"	TEXT NOT NULL,
	"server"	TEXT NOT NULL,
	PRIMARY KEY("id_chan")
);
CREATE TABLE IF NOT EXISTS "Server" (
	"name"	TEXT NOT NULL,
	"id_dofus"	TEXT NOT NULL,
	"id_sweet"	TEXT NOT NULL,
	"game"	TEXT DEFAULT 'DOFUS',
	PRIMARY KEY("name")
);
CREATE TABLE IF NOT EXISTS "Portal_Tracker" (
	"id_chan"	TEXT NOT NULL,
	"id_guild"	TEXT NOT NULL,
	PRIMARY KEY("id_chan","id_guild"),
	FOREIGN KEY("id_guild") REFERENCES "Guild"("id") ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Job_User" (
	"name_job"	TEXT NOT NULL,
	"id_user"	TEXT NOT NULL,
	"server_dofus"	TEXT NOT NULL,
	"level"	INTEGER NOT NULL,
	PRIMARY KEY("name_job","id_user","server_dofus")
);
CREATE TABLE IF NOT EXISTS "Order_User" (
	"id_user"	TEXT NOT NULL,
	"server_dofus"	TEXT NOT NULL,
	"name_city"	TEXT NOT NULL,
	"name_order"	TEXT NOT NULL,
	"level"	INTEGER NOT NULL,
	PRIMARY KEY("id_user","server_dofus","name_city","name_order")
);
CREATE TABLE IF NOT EXISTS "Channel_Language" (
	"id_chan"	TEXT NOT NULL,
	"lang"	TEXT NOT NULL,
	PRIMARY KEY("id_chan")
);
CREATE TABLE IF NOT EXISTS "Portal" (
	"name"	TEXT NOT NULL,
	"url"	TEXT,
	"color"	INTEGER,
	PRIMARY KEY("name")
);
CREATE TABLE IF NOT EXISTS "Almanax_Calendar" (
	"id_chan"	TEXT NOT NULL,
	"id_guild"	TEXT NOT NULL,
	PRIMARY KEY("id_chan","id_guild"),
	FOREIGN KEY("id_guild") REFERENCES "Guild"("id") ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Twitter" (
	"id_chan"	TEXT NOT NULL,
	"id_guild"	TEXT NOT NULL,
	PRIMARY KEY("id_chan","id_guild"),
	FOREIGN KEY("id_guild") REFERENCES "Guild"("id") ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "RSS_Finder" (
	"id_chan"	TEXT NOT NULL,
	"id_guild"	TEXT NOT NULL,
	"last_update"	INTEGER NOT NULL,
	PRIMARY KEY("id_chan","id_guild"),
	FOREIGN KEY("id_guild") REFERENCES "Guild"("id") ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Command_Guild" (
	"id_guild"	TEXT NOT NULL,
	"name_command"	TEXT NOT NULL,
	PRIMARY KEY("id_guild","name_command"),
	FOREIGN KEY("id_guild") REFERENCES "Guild"("id") ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Guild" (
	"id"	TEXT NOT NULL,
	"name"	TEXT,
	"prefixe"	TEXT DEFAULT "!",
	"server_dofus"	TEXT,
	"lang"	TEXT NOT NULL DEFAULT "FR",
	PRIMARY KEY("id")
);
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Pandore','EN','Pandora');
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Pandore','ES','Pandora');
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Oto-Mustam','EN','Oto Mustam');
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Oto-Mustam','ES','Oto Mustam');
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Ombre','EN','Shadow');
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Agride','ES','Agrid');
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Crocabulia','ES','Cocabulia');
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Echo','ES','Eco');
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Ombre','ES','Sombra');
INSERT INTO "Server_Label" ("server","language","label") VALUES ('Furye','ES','Furyha');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Agride','36','2','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Atcham','204','55','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Crocabulia','202','57','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Echo','201','54','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Meriana','205','58','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Ombre','50','35','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Oto-Mustam','22','36','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Rubilax','203','56','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Pandore','206','59','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Ush','207','60','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Julith','208','61','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Nidas','209','62','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Merkator','210','63','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Furye','211','64','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Brumen','212','65','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Ilyzaelle','222','66','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Brutas','407','0','DOFUS_TOUCH');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Dodge','406','0','DOFUS_TOUCH');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Grandapan','401','0','DOFUS_TOUCH');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Herdegrize','405','0','DOFUS_TOUCH');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Oshimo','403','0','DOFUS_TOUCH');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Terra Cogita','404','0','DOFUS_TOUCH');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Henual','0','0','DOFUS_TOUCH');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Eratz','1','0','DOFUS_TOUCH');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Jahash','240','84','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Thanatena','239','83','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis I','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis II','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis III','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis IV','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis V','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis VI','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis VII','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis VIII','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis IX','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis X','1','1','DOFUS');
INSERT INTO "Server" ("name","id_dofus","id_sweet","game") VALUES ('Temporis XI','1','1','DOFUS');
INSERT INTO "Portal" ("name","url","color") VALUES ('Enutrosor','https://i.imgur.com/ssMAcx3.png',16777064);
INSERT INTO "Portal" ("name","url","color") VALUES ('Srambad','https://i.imgur.com/jzpizTm.png',3097192);
INSERT INTO "Portal" ("name","url","color") VALUES ('Xélorium','https://i.imgur.com/vfQhS5D.png',7229801);
INSERT INTO "Portal" ("name","url","color") VALUES ('Ecaflipus','https://i.imgur.com/sLK4FmQ.png',13490334);
COMMIT;
