CREATE TABLE `lender`.`user_kyc` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `type` INT NOT NULL,
  `kyc_id` VARCHAR(45) NOT NULL,
  `image_url` VARCHAR(200) NULL,
  `is_verified` INT(1) NOT NULL DEFAULT 0,
  `status` INT NOT NULL,
  `create_user` INT NULL,
  `create_ts` DATETIME NOT NULL,
  `update_user` INT NULL,
  `update_ts` DATETIME NOT NULL,
  PRIMARY KEY (`id`));