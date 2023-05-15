package cz.tul.Chmelar.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateServiceTest {

    @TempDir
    static Path tempDir;
    @Test
    void testRefreshFileExchangeRate() throws IOException {
        ExchangeRateService.refreshFileExchangeRate();
    }

    @Test
    void testIfItIsHoliday() {
        LocalDate holidayDate = LocalDate.of(2023, 1, 1);
        assertTrue(ExchangeRateService.isHoliday(holidayDate));

        LocalDate nonHolidayDate = LocalDate.of(2023, 1, 2);
        assertFalse(ExchangeRateService.isHoliday(nonHolidayDate));
    }

    @Test
    void testTransferExchangeRateCount() {
        String exchangeRateFileContent = "09.05.2023 #88\n" +
                "země|měna|množství|kód|kurz\n" +
                "Austrálie|dolar|1|AUD|14,410\n" +
                "Brazílie|real|1|BRL|4,258\n" +
                "Bulharsko|lev|1|BGN|11,948\n" +
                "Čína|žen-min-pi|1|CNY|3,079";

        Path testFilePath = tempDir.resolve("denni_kurz.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath.toFile()))) {
            writer.write(exchangeRateFileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        double result1 = ExchangeRateService.transferExchangeRateCount(testFilePath.toString(), "AUD", 10);
        Assertions.assertEquals(144.10, result1);


        double result3 = ExchangeRateService.transferExchangeRateCount(testFilePath.toString(), "CZK", 10);
        Assertions.assertEquals(10, result3);

        assertThrows(RuntimeException.class, () -> {
            ExchangeRateService.transferExchangeRateCount(testFilePath.toString(), "EUR", 10);
        });
    }

    @Test
    void testIsDateSameOrEarlierThanTomorrow() {
    }
}
