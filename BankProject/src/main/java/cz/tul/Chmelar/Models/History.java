package cz.tul.Chmelar.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class History {

    private String timestamp;
    private String account;
    private String action;
    private double amount;


    @Override
    public String toString() {
        return "timestamp='" + timestamp + '\'' +
                " account='" + account + '\'' +
                " action='" + action + '\'' +
                " amount='" + amount + '\'';

    }
}
