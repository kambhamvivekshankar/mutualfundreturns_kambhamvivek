package com.reputation.mutualfundreturns_kambhamvivek;

import com.reputation.mutualfundreturns_kambhamvivek.exceptions.InvalidNumberException;
import com.reputation.mutualfundreturns_kambhamvivek.exceptions.InvalidSchemeException;
import com.reputation.mutualfundreturns_kambhamvivek.service.Accessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

@Component
public class MutualFundRunner implements ApplicationRunner {

    @Autowired
    Accessor dataAccessor;

    @Autowired
    MessageSource messageSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String menu = getBuildMenu();
        System.out.print(menu);
        System.out.println(getMessage("info.options"));
        try {
            String scheme = args.containsOption("scheme") ? getOptionValue("scheme", args) : "102885";
            System.out.println(getMessage("enter.Scheme") + scheme);
            long period = args.containsOption("period") ? Long.parseLong(getOptionValue("period", args)) : 1l;
            System.out.println(getMessage("enter.Period") + period);
            long horizon = args.containsOption("horizon") ? Long.parseLong(getOptionValue("horizon", args)) : 5l;
            System.out.println(getMessage("enter.Horizon") + horizon);
            if (period <= 0 || horizon <= 0)
                throw new InvalidNumberException(getMessage("invalid.Period"));
            TreeMap<LocalDate, Double> map = dataAccessor.getNavigableData(scheme);
            LocalDate today = LocalDate.now();
            LocalDate startHorizonDate = today.minusYears(horizon);
            System.out.format("%16s%16s%8s%s", "Month", "Returns", "", "Calculation");
            System.out.println();
            LocalDate currMonthDate = startHorizonDate;
            while (currMonthDate.isBefore(today)) {
                String month = currMonthDate.getMonth().name() + "-" + currMonthDate.getYear();
                LocalDate startNavDate = currMonthDate.minusYears(period);
                StringBuilder calculationBuilder = new StringBuilder();
                if (startNavDate.isBefore(map.firstKey()) || currMonthDate.isAfter(map.lastKey())) {
                    calculationBuilder.append(getMessage("invalid.dates"));
                } else {
                    buildCalculationColumn(map, currMonthDate, startNavDate, calculationBuilder);
                }
                double returns = 0;
                if (map.ceilingEntry(currMonthDate) == null || map.ceilingEntry(startNavDate) == null || startNavDate.isBefore(map.firstKey()))
                    returns = 0;
                else
                    returns = Math.pow(map.ceilingEntry(currMonthDate).getValue() / map.ceilingEntry(startNavDate).getValue(), (1.0 / period)) * 100 - 100;
                System.out.format("%16s%16.2f%%%8s%s", month, returns, " ", calculationBuilder.toString());
                System.out.println();
                currMonthDate = currMonthDate.plusMonths(1l);
            }
        } catch (NumberFormatException e) {
            System.err.println(getMessage("invalid.Number"));
        } catch (InvalidNumberException | InvalidSchemeException e) {
            System.err.println(e.getMessage());
        }
    }

    private String getMessage(String s) {
        return messageSource.getMessage(s, null, LocaleContextHolder.getLocale());
    }

    private void buildCalculationColumn(TreeMap<LocalDate, Double> map, LocalDate currMonthDate, LocalDate startNavDate, StringBuilder calculationBuilder) {
        calculationBuilder.append(getMessage("nav.start"));
        calculationBuilder.append(map.ceilingKey(startNavDate) != null ? map.ceilingKey(startNavDate).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")) : "");
        calculationBuilder.append("  ");
        calculationBuilder.append(map.ceilingEntry(startNavDate).getValue());
        calculationBuilder.append("   ");
        calculationBuilder.append(getMessage("nav.end"));
        calculationBuilder.append(map.ceilingKey(currMonthDate) != null ? map.ceilingKey(currMonthDate).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")) : "");
        calculationBuilder.append("  ");
        calculationBuilder.append(map.ceilingEntry(currMonthDate).getValue());
    }

    private String getOptionValue(String option, ApplicationArguments args) throws InvalidSchemeException {
        if (args.getOptionValues(option).size() == 1)
            return args.getOptionValues(option).get(0);
        else if (args.getOptionValues(option).isEmpty() || args.getOptionValues(option).size() > 1)
            throw new InvalidSchemeException(getMessage("invalid.schemes"));
        return null;
    }

    private String getBuildMenu() {
        StringBuilder buildMenu = new StringBuilder();
        buildMenu.append("\n");
        for (int i = 0; i < 25; i++) buildMenu.append("#");
        buildMenu.append("\n");
        buildMenu.append(getMessage("mfr"));
        for (int i = 0; i < 25; i++) buildMenu.append("#");
        buildMenu.append("\n");
        String menu = buildMenu.toString();
        return menu;
    }
}
