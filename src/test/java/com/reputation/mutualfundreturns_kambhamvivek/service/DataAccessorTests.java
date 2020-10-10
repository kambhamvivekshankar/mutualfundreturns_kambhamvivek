package com.reputation.mutualfundreturns_kambhamvivek.service;


import com.reputation.mutualfundreturns_kambhamvivek.exceptions.InvalidSchemeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DataAccessorTests {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MessageSource messageSource;
    @Autowired
    Accessor dataAccessor;

    @Test
    void contextLoads() {
        assertThat(messageSource).isNotNull();
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void runWithInvalidScheme() throws Exception {
        try {
            dataAccessor.getNavigableData("1");
        } catch (InvalidSchemeException e) {
            assertThat(e.getMessage()).isEqualTo(getMessage("invalid.scheme"));
        }
    }

    @Test
    void runWithEmptyScheme() {
        try {
            dataAccessor.getNavigableData("");
        } catch (InvalidSchemeException e) {
            assertThat(e.getMessage()).isEqualTo(getMessage("invalid.page"));
        }
    }

    private String getMessage(String s) {
        return messageSource.getMessage(s, null, LocaleContextHolder.getLocale());
    }
}
