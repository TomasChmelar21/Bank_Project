package cz.tul.Chmelar.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Model for History of each payment and deposit
 */
@Getter
@Setter
public class History {

    private String timestamp;
    private String account;
    private String action;
    private double amount;


    /**
     * History to string
     *
     * @return history in String format
     */
    @Override
    public String toString() {
        return "timestamp='" + timestamp + '\'' +
                " account='" + account + '\'' +
                " action='" + action + '\'' +
                " amount='" + amount + '\'';

    }
}
