package com.mbv.persist.enums;

/**
 * Created by arindamnath on 04/03/16.
 */
public enum Currency {

    INR("₹");

    private String symbol;

    Currency(String symbol){
        this.symbol =symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Currency getBySymbol(String symbol){
        Currency retVal = null;
        for(Currency currency: Currency.values()){
            if(currency.symbol.equals(symbol)){
                retVal = currency;
                break;
            }
        }
        return retVal;
    }

    public static Currency getByOrdinal(int ordinal){
        Currency retVal = null;
        for(Currency currency: Currency.values()){
            if(currency.ordinal() == ordinal){
                retVal = currency;
                break;
            }
        }
        return retVal;
    }
}
