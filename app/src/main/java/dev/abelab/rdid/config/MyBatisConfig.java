package dev.abelab.rdid.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan("dev.abelab.rdid.db.mapper")
@Configuration
public class MyBatisConfig {
}
