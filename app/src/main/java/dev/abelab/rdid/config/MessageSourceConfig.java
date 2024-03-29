package dev.abelab.rdid.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import net.rakugakibox.util.YamlResourceBundle;

/**
 * Message Sourceの設定
 */
@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource(@Value("${spring.messages.basename}") String basename,
        @Value("${spring.messages.encoding}") String encoding) {

        final var messageSource = new YamlMessageSource();

        messageSource.setBasenames(basename);
        messageSource.setDefaultEncoding(encoding);

        return messageSource;
    }

    private static class YamlMessageSource extends ResourceBundleMessageSource {
        @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
        }

    }

}
