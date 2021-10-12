CREATE TABLE IF NOT EXISTS `client` (
  `id` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
