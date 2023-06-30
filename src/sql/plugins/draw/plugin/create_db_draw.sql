
--
-- Structure for table draw_user
--

DROP TABLE IF EXISTS draw_user;
CREATE TABLE draw_user (
id_user int AUTO_INCREMENT,
name long varchar NOT NULL,
lastname long varchar NOT NULL,
email varchar(255) default '' NOT NULL,
phone int default '0' NOT NULL,
adress long varchar NOT NULL,
status int default 0,
PRIMARY KEY (id_user)
);

--
-- Structure for table draw_draw
--

DROP TABLE IF EXISTS draw_draw;
CREATE TABLE draw_draw (
id_draw int AUTO_INCREMENT,
iduser int default '0' NOT NULL,
number int default '0' NOT NULL,
datecreation date NOT NULL,
PRIMARY KEY (id_draw)
);
