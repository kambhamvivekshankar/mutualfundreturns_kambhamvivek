package com.reputation.mutualfundreturns_kambhamvivek;

import com.reputation.mutualfundreturns_kambhamvivek.service.Accessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MutualfundreturnsKambhamvivekApplicationTests {

    @Autowired
    ApplicationRunner runner;
    @Autowired
    Accessor accessor;

    @Test
    void contextLoads() {
        assertThat(runner).isNotNull();
        assertThat(accessor).isNotNull();
    }

}
