CREATE TABLE IF NOT EXISTS `user_group_role` (
  `group_id` INT UNSIGNED NOT NULL,
  `client_id` INT UNSIGNED NOT NULL,
  `role_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`group_id`, `client_id`),
  INDEX `fk_user_group_role_client_id_idx` (`client_id` ASC) VISIBLE,
  INDEX `fk_user_group_role_role_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_group_role_client_id`
    FOREIGN KEY (`client_id`)
    REFERENCES `client` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_role_group_id`
    FOREIGN KEY (`group_id`)
    REFERENCES `user_group` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_role_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
