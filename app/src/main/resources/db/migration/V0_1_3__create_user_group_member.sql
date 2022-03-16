CREATE TABLE IF NOT EXISTS `user_group_member`
(
  `user_id`  INT UNSIGNED NOT NULL,
  `group_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `group_id`),
  INDEX `fk_user_group_member_group_id_idx` (`group_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_group_member_user_id`
    FOREIGN KEY (`user_id`)
      REFERENCES `user` (`id`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_member_group_id`
    FOREIGN KEY (`group_id`)
      REFERENCES `user_group` (`id`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
)
  ENGINE = InnoDB;
