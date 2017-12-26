-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.1.17-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.3.0.4984
-- --------------------------------------------------------

CREATE DATABASE IF NOT EXISTS hug CHARACTER SET utf8mb4;
USE hug;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL DEFAULT '',
  `email` varchar(255) NOT NULL DEFAULT '',
  `token` varchar(255) NOT NULL DEFAULT '',
  `inviteCode` varchar(10) NOT NULL,
  `challengerId` int(11) DEFAULT NULL,
  `completed` tinyint(1) NOT NULL DEFAULT '0',
  `started` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `image` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `challenges_fk` (`challengerId`),
  CONSTRAINT `challenges_fk` FOREIGN KEY (`challengerId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `invites`;
CREATE TABLE `invites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) NOT NULL DEFAULT '',
  `inviter` int(11) NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `inviter_fk` (`inviter`),
  CONSTRAINT `inviter_fk` FOREIGN KEY (`inviter`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP PROCEDURE IF EXISTS recursive_lookup;

DELIMITER //

CREATE PROCEDURE `recursive_lookup`(IN `root_id` INT, IN `isEffect` TINYINT)
BEGIN
  -- declare a variable for breaking the while loop
  DECLARE `done` TINYINT DEFAULT 0;

  -- table to hold challengerId and insert first id from parameter
  DROP TABLE IF EXISTS `challengers`;
  CREATE TEMPORARY TABLE `challengers`(`id` INT, UNIQUE KEY (`id`));
  INSERT INTO `challengers`(`id`) VALUES (`root_id`);

  -- table to hold results
  DROP TABLE IF EXISTS `results`;
  CREATE TEMPORARY TABLE `results`(`id` INT, `completed` TINYINT, UNIQUE KEY (`id`));
  INSERT INTO `results`(`id`, `completed`) SELECT id, completed FROM `users` where id = `root_id`;

  WHILE `done` = 0 DO
    -- insert new ids from previous loop (or none)
    INSERT IGNORE INTO `challengers`(`id`) SELECT `id` FROM `results`;

    -- insert new children
    INSERT IGNORE INTO `results`(`id`, `completed`) SELECT u.id, u.completed FROM `users` u JOIN `challengers` c ON u.challengerId = c.id;

    -- if no rows were inserted, break the loop
    -- rows already present in the table are not counted
    IF ROW_COUNT() = 0 THEN
      SET `done` = 1;
    END IF;
  END WHILE;
  IF `isEffect` = 0 THEN
        SELECT COUNT(*) total FROM `results`;
  ELSE
    SELECT COUNT(*) total FROM `results` WHERE `completed` = 1;
  END IF;
END;//

DELIMITER ;
