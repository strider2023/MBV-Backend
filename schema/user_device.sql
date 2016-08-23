CREATE TABLE `lender`.`user_device` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NULL,
  `device_id` VARCHAR(45) NULL,
  `imei_id` VARCHAR(45) NULL,
  `os_type` INT NULL,
  `os_version` VARCHAR(45) NULL,
  `brand` VARCHAR(45) NULL,
  `model` VARCHAR(45) NULL,
  `device_name` VARCHAR(45) NULL,
  `manufacturer` VARCHAR(45) NULL,
  `device_type` INT NULL,
  `operator_name` VARCHAR(45) NULL,
  `language` VARCHAR(45) NULL,
  `status` INT NOT NULL DEFAULT 4,
  `create_user` INT NULL DEFAULT -1,
  `create_ts` DATETIME NOT NULL,
  `update_user` INT NULL DEFAULT -1,
  `update_ts` DATETIME NOT NULL,
  PRIMARY KEY (`id`));