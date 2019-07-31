package com.traphan.currencyconverter;

import com.traphan.currencyconverter.database.CurrencyEntity;

import java.util.LinkedList;
import java.util.List;

public class CurrencyTestHelper {

    protected static CurrencyEntity getCurrency() {
        return new CurrencyEntity(Util.getRandomString(6), Util.getRandomString(3), Util.getRandomString(3),
                Util.getRandomNumberInRange(1, 1000), Util.getRandomString(10), Util.getRandomFloat(1, 300), Util.getRandomFloat(1, 300));
    }

    protected static List<CurrencyEntity> getCurrencies(int size) {
        List<CurrencyEntity> currencyEntities = new LinkedList<>();
        int i = 0;
        while (i < size) {
            currencyEntities.add(getCurrency());
            i++;
        }
        return currencyEntities;
    }
}
