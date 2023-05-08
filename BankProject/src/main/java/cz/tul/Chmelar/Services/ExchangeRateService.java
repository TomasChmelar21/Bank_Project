package cz.tul.Chmelar.Services;

import cz.tul.Chmelar.Models.ExchangeRate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static cz.tul.Chmelar.Models.ExchangeRateRepository.getExchangeRateByCurrency;
import static cz.tul.Chmelar.Models.ExchangeRateRepository.getHtmlOfRates;

@Service
@EnableScheduling
public class ExchangeRateService {

    /**
     * refresh denni_kurz file with new rate every monday to friday at 14:45
     */
    @Scheduled(cron = "0 45 14 * * MON-FRI")
    public static void refreshFileExchangeRate() {
        if (!isHoliday(LocalDate.now())) {
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
    }

    /**
     * check if today is holiday in Czechia (except Easters)
     *
     * @param date - today´s date
     * @return true if today is Czechia holidays
     */
    private static boolean isHoliday(LocalDate date) {
        String[] holidays = {
                "01-01", // Nový rok
                "01-05", // Svátek práce
                "05-08", // Den vítězství
                "07-05", // Den věrozvěstů Cyrila a Metoděje
                "06-07", // Den upálení mistra Jana Husa
                "28-09", // Den české státnosti
                "28-10", // Den vzniku Československa
                "17-11", // Den boje za svobodu a demokracii
                "24-12", // Štědrý den
                "25-12", // 1. svátek vánoční
                "26-12"  // 2. svátek vánoční
                //Velikonoce
        };

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        String dateString = date.format(formatter);
        for (String holiday : holidays) {
            if (dateString.equals(holiday)) {
                return true;
            }
        }
        return false;
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
