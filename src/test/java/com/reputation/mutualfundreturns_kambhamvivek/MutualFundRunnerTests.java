package com.reputation.mutualfundreturns_kambhamvivek;

import com.reputation.mutualfundreturns_kambhamvivek.service.Accessor;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MutualFundRunnerTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    @Autowired
    MessageSource messageSource;
    @Autowired
    Accessor accessor;
    @Autowired
    ApplicationRunner mutualRunner;

    @Before
    public void setup() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void contextLoads() {
        assertThat(messageSource).isNotNull();
        assertThat(accessor).isNotNull();
    }

    @Test
    void runWithInvalidPeriod() throws Exception {
        ApplicationArguments mocked = getApplicationArguments(true, false, false, new String[]{"1.8"}, new String[0], new String[0]);
        mutualRunner.run(mocked);
        assertThat(errContent.toString()).contains(getMessage("invalid.Number"));
    }

    @Test
    void runWithInvalidEpmtyPeriod() throws Exception {
        ApplicationArguments mocked = getApplicationArguments(true, false, false, new String[]{""}, new String[0], new String[0]);
        mutualRunner.run(mocked);
        assertThat(errContent.toString()).contains(getMessage("invalid.Period"));
    }

    @Test
    void runWithInvalidHorizon() throws Exception {
        ApplicationArguments mocked = getApplicationArguments(false, false, true, new String[0], new String[0], new String[]{"1.8"});
        mutualRunner.run(mocked);
        assertThat(errContent.toString()).contains(getMessage("invalid.Number"));
    }

    @Test
    void runWithEmptyHorizon() throws Exception {
        ApplicationArguments mocked = getApplicationArguments(false, false, true, new String[0], new String[0], new String[]{""});
        mutualRunner.run(mocked);
        assertThat(errContent.toString()).contains(getMessage("invalid.Period"));
    }

    @Test
    void runWithInvalidScheme() throws Exception {
        ApplicationArguments mocked = getApplicationArguments(false, true, false, new String[0], new String[]{"1"}, new String[0]);
        mutualRunner.run(mocked);
        assertThat(errContent.toString()).contains(getMessage("invalid.scheme"));
    }

    @Test
    void runWithEmptyScheme() throws Exception {
        ApplicationArguments mocked = getApplicationArguments(false, true, false, new String[0], new String[]{""}, new String[0]);
        mutualRunner.run(mocked);
        assertThat(errContent.toString()).contains(getMessage("invalid.page"));
    }

    private ApplicationArguments getApplicationArguments(boolean isperiod, boolean isscheme, boolean ishorizon, String[] periods, String[] schemes, String[] horizons) {
        ApplicationArguments mocked = Mockito.mock(ApplicationArguments.class);
        Mockito.when(mocked.containsOption("period")).thenReturn(isperiod);
        Mockito.when(mocked.containsOption("scheme")).thenReturn(isscheme);
        Mockito.when(mocked.containsOption("horizon")).thenReturn(ishorizon);
        List<String> periodOptions = new ArrayList<>();
        for (String s : periods)
            periodOptions.add(s);
        List<String> schemeOptions = new ArrayList<>();
        for (String s : schemes)
            schemeOptions.add(s);
        List<String> horizonOptions = new ArrayList<>();
        for (String s : horizons)
            horizonOptions.add(s);
        Mockito.when(mocked.getOptionValues("period")).thenReturn(periodOptions);
        Mockito.when(mocked.getOptionValues("scheme")).thenReturn(schemeOptions);
        Mockito.when(mocked.getOptionValues("horizon")).thenReturn(horizonOptions);
        return mocked;
    }

    private String getMessage(String s) {
        return messageSource.getMessage(s, null, LocaleContextHolder.getLocale());
    }
}
