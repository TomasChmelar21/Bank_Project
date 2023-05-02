package cz.tul.Chmelar.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {

    private String currency;
    private double amount;


    @Override
    public String toString() {
        return "mena='" + currency + '\'' +
                " castka='" + amount + '\'';

    }

}
