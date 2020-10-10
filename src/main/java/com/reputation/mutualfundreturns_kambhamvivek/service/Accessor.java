package com.reputation.mutualfundreturns_kambhamvivek.service;

import com.reputation.mutualfundreturns_kambhamvivek.exceptions.InvalidSchemeException;

import java.time.LocalDate;
import java.util.TreeMap;

public interface Accessor {
    TreeMap<LocalDate, Double> getNavigableData(String schemeNumber) throws InvalidSchemeException;
}
