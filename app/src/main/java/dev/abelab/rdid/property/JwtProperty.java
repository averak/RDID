package dev.abelab.rdid.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("jwt")
public class JwtProperty {

    /**
     * Secret key
     */
    String secret;

    /**
     * Issuer
     */
    String issuer;

    /**
     * 有効期限[s]
     */
    Integer timeout;

}
