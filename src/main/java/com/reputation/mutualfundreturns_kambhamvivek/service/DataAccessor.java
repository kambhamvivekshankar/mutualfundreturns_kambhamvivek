package com.reputation.mutualfundreturns_kambhamvivek.service;

import com.reputation.mutualfundreturns_kambhamvivek.exceptions.InvalidSchemeException;
import com.reputation.mutualfundreturns_kambhamvivek.model.Data;
import com.reputation.mutualfundreturns_kambhamvivek.model.Nav;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.TreeMap;

@Service
public class DataAccessor implements Accessor {

    final String ROOT_URI = "https://api.mfapi.in/mf/";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MessageSource messageSource;

    @Override
    public TreeMap<LocalDate, Double> getNavigableData(String schemeNumber) throws InvalidSchemeException {
        ResponseEntity<Data> data = null;
        try {
            data = restTemplate.getForEntity(ROOT_URI + schemeNumber, Data.class);
            if (data.getBody().getData().length == 0) throw new InvalidSchemeException(getMessage("invalid.scheme"));
        } catch (InvalidSchemeException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidSchemeException(getMessage("invalid.page"));
        }
        TreeMap<LocalDate, Double> map = new TreeMap<>();
        for (Nav n : data.getBody().getData()) {
            map.put(n.getDate(), n.getNav());
        }
        return map;
    }

    private String getMessage(String s) {
        return messageSource.getMessage(s, null, LocaleContextHolder.getLocale());
    }
}
