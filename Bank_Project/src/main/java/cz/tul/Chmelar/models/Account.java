package cz.tul.Chmelar.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Model of Account
 */
@Getter
@Setter
public class Account {

    private String currency;
    private double amount;

    /**
     * Account to string
     * @return string of account
     */
    @Override
    public String toString() {
        return "currency='" + currency + '\'' +
                " amount='" + amount + '\'';

    }

}
