CREATE TABLE IF NOT EXISTS `user_group_role`
(
  `group_id`   INT UNSIGNED NOT NULL,
  `service_id` INT UNSIGNED NOT NULL,
  `role_id`    INT UNSIGNED NOT NULL,
  PRIMARY KEY (`group_id`, `service_id`, `role_id`),
  INDEX `fk_user_group_role_role_idx` (`role_id` ASC) VISIBLE,
  INDEX `fk_user_group_role_client_id_idx` (`service_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_group_role_group_id`
    FOREIGN KEY (`group_id`)
      REFERENCES `user_group` (`id`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
)
  ENGINE = InnoDB;
