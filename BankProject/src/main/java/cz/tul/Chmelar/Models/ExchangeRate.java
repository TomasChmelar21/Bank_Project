package cz.tul.Chmelar.Models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Model of Exchange Rate
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private String state;
    private String currency;
    private String amount;
    private String code;
    private String exchangeRate;
}