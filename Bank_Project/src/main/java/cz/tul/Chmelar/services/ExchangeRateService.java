package cz.tul.Chmelar.services;

import cz.tul.Chmelar.models.ExchangeRate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static cz.tul.Chmelar.models.ExchangeRateRepository.getExchangeRateByCurrency;
import static cz.tul.Chmelar.models.ExchangeRateRepository.getHtmlOfRates;

@Service
@EnableScheduling
public class ExchangeRateService {

    
    private static String filePath = "data/denni_kurz.txt";
    
    /**
     * refresh denni_kurz file with new rate every monday to friday at 14:45
     */
    @Scheduled(cron = "0 45 14 * * MON-FRI")
    public static void refreshFileExchangeRate() throws IOException {
        if (!isHoliday(LocalDate.now())) {
            String url = "http://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt";
            String htmlContent = getHtmlOfRates(url);
            try {
                FileWriter writetoFile = new FileWriter(filePath, false);
                writetoFile.write(htmlContent);
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
    public static boolean isHoliday(LocalDate date) {
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
     * @param amount - amount we want to transfer
     * @return transform amount from currencyFrom to currencyTo
     */
    public static double transferExchangeRateCount(String filePath, String currencyFrom, double amount) {
        double output = amount;
        if (currencyFrom.equalsIgnoreCase("CZK")) {
            return output;
        }
        ExchangeRate exchangeFrom = getExchangeRateByCurrency(filePath, currencyFrom);
        if(exchangeFrom == null){ throw new RuntimeException(); }
        double fromAmount = Double.parseDouble(exchangeFrom.getAmount().replaceAll(",","."));
        double fromRate = Double.parseDouble(exchangeFrom.getExchangeRate().replaceAll(",","."));
        output = (amount / fromAmount) * fromRate;
        return output;
    }

    /**
     * check if exchange rates aren´t too old
     *
     * @param dateString - date from exchange file
     * @return true if file is the newest it could be
     */
    public static boolean isDateSameOrEarlierThanTomorrow(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        LocalDate today = LocalDate.now();
        LocalTime cutoffTime = LocalTime.of(14, 45);
        if (isHoliday(date) || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return true;
        }
        if (date.getDayOfWeek() == DayOfWeek.MONDAY && LocalTime.now().isBefore(cutoffTime)) {
            return true;
        }
        if (date.isEqual(today) || (date.isEqual(today.plusDays(1)) && LocalTime.now().isBefore(cutoffTime))) {
            return true;
        }
        return false;
    }
}
