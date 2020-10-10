package com.reputation.mutualfundreturns_kambhamvivek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class MutualfundreturnsKambhamvivekApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutualfundreturnsKambhamvivekApplication.class, args).close();
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
}