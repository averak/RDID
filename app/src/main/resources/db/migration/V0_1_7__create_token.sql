CREATE TABLE IF NOT EXISTS `token` (
  `user_id` INT UNSIGNED NOT NULL,
  `client_id` INT UNSIGNED NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  `expired_in` INT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `client_id`),
  INDEX `fk_token_client_id_idx` (`client_id` ASC) VISIBLE,
  CONSTRAINT `fk_token_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_token_client_id`
    FOREIGN KEY (`client_id`)
    REFERENCES `client` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
