package cz.tul.Chmelar.Services;

import cz.tul.Chmelar.Models.ExchangeRate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cz.tul.Chmelar.Models.ExchangeRateRepository.getExchangeRateByCurrency;
import static cz.tul.Chmelar.Models.ExchangeRateRepository.getHtmlOfRates;

@Service
public class ExchangeRateService {

    /**
     * refresh denni_kurz file with new rate
     */
    public static void refreshFileExchangeRate() {
        String url = "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt";
        String htmlContent = getHtmlOfRates(url);
        try {
            FileWriter writetoFile = new FileWriter("src/main/resources/denni_kurz.txt", false);
            writetoFile.write(htmlContent);
            writetoFile.write("Česko|koruna|1|CZK|1");
            writetoFile.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * transfer amount from one rate to other
     *
     * @param currencyFrom - currency from we transfering
     * @param currencyTo - currency to we transfering
     * @param amount - amount we want to transfer
     * @return transform amount from currencyFrom to currencyTo
     */
    public static double transferExchangeRateCount(String currencyFrom, String currencyTo, double amount) {
        double output = amount;
        if (currencyFrom.equalsIgnoreCase(currencyTo)) {
            return output;
        }
        ExchangeRate exchangeFrom = getExchangeRateByCurrency(currencyFrom);
        ExchangeRate exchangeTo = getExchangeRateByCurrency(currencyTo);
        double fromAmount = Double.parseDouble(exchangeFrom.getAmount().replaceAll(",","."));
        double fromRate = Double.parseDouble(exchangeFrom.getExchangeRate().replaceAll(",","."));
        double toAmount = Double.parseDouble(exchangeTo.getAmount().replaceAll(",","."));
        double toRate = Double.parseDouble(exchangeTo.getExchangeRate().replaceAll(",","."));
        output = (amount / fromAmount) * (fromRate / toRate) * toAmount;
        return output;
    }
}
