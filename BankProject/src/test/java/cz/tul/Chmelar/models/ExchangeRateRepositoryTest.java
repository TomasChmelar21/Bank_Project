package cz.tul.Chmelar.models;

import cz.tul.Chmelar.services.ExchangeRateService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.*;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ExchangeRateRepositoryTest {
    @TempDir
    static Path tempDir;
    @Test
    public void testGetListOfExchangeRates() {
        List<ExchangeRate> exchangeRates = ExchangeRateRepository.getListOfExchangeRates();

        // Assert that the exchangeRates list is not null
        Assert.assertNotNull(exchangeRates);

        // Assert that the exchangeRates list is not empty
        Assert.assertFalse(exchangeRates.isEmpty());

        // Assert that all parameters of the exchange rate are not null
        for (ExchangeRate exchangeRate : exchangeRates) {
            Assert.assertNotNull(exchangeRate.getState());
            Assert.assertNotNull(exchangeRate.getCurrency());
            Assert.assertNotNull(exchangeRate.getAmount());
            Assert.assertNotNull(exchangeRate.getCode());
            Assert.assertNotNull(exchangeRate.getExchangeRate());
        }

    }

    @Test
    void getExchangeRateArray() {

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

        // Set the exchangeRateFileContent directly
        ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepository();

        // Invoke the method and verify the result
        String[][] expectedOutput = {
                {"Austrálie", "dolar", "1", "AUD", "14,410"},
                {"Brazílie", "real", "1", "BRL", "4,258"},
                {"Bulharsko", "lev", "1", "BGN", "11,948"},
                {"Čína", "žen-min-pi", "1", "CNY", "3,079"}
        };


        String[][] actualOutput = exchangeRateRepository.getExchangeRateArray(testFilePath.toString());
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    void getExchangeRateTime() {
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

        String exchangeRateTime = ExchangeRateRepository.getExchangeRateTime(testFilePath.toString());
        assertEquals("09.05.2023", exchangeRateTime);
    }


    @Test
    void testGetExchangeRateCurrency() {
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

        String currency = "BRL";
        ExchangeRate expectedRate = new ExchangeRate("Brazílie", "real", "1", "BRL", "4,258");
        ExchangeRate actualRate = ExchangeRateRepository.getExchangeRateByCurrency(testFilePath.toString(), currency);
        assertEquals(expectedRate.getState(), actualRate.getState());
        assertEquals(expectedRate.getCurrency(), actualRate.getCurrency());
        assertEquals(expectedRate.getExchangeRate(), actualRate.getExchangeRate());
        assertEquals(expectedRate.getCode(), actualRate.getCode());
        assertEquals(expectedRate.getAmount(), actualRate.getAmount());
    }







    @Test
    void readExchangeRateFile() throws IOException {
        String expectedContent = "Line 1\nLine 2\nLine 3\n";

        Path testFilePath = tempDir.resolve("denni_kurz.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath.toFile()))) {
            writer.write(expectedContent);
        }

        String actualContent = ExchangeRateRepository.readExchangeRateFile(testFilePath.toString());

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testReadExchangeRateFile_IOException() {
        String filePath = "invalid-file-path.txt";

        assertThrows(RuntimeException.class, () -> {
            ExchangeRateRepository.readExchangeRateFile(filePath);
        });
    }

    @Test
    void getHtmlOfRates() throws IOException {
        String expectedString = """
                10.05.2023 #89
                země|měna|množství|kód|kurz
                Austrálie|dolar|1|AUD|14,430
                Brazílie|real|1|BRL|4,295
                Bulharsko|lev|1|BGN|11,971
                Čína|žen-min-pi|1|CNY|3,088
                Dánsko|koruna|1|DKK|3,144
                EMU|euro|1|EUR|23,415
                Filipíny|peso|100|PHP|38,392
                Hongkong|dolar|1|HKD|2,731
                Indie|rupie|100|INR|26,068
                Indonesie|rupie|1000|IDR|1,452
                Island|koruna|100|ISK|15,620
                Izrael|nový šekel|1|ILS|5,833
                Japonsko|jen|100|JPY|15,802
                Jižní Afrika|rand|1|ZAR|1,142
                Kanada|dolar|1|CAD|15,976
                Korejská republika|won|100|KRW|1,615
                Maďarsko|forint|100|HUF|6,314
                Malajsie|ringgit|1|MYR|4,796
                Mexiko|peso|1|MXN|1,207
                MMF|ZPČ|1|XDR|28,852
                Norsko|koruna|1|NOK|2,027
                Nový Zéland|dolar|1|NZD|13,532
                Polsko|zlotý|1|PLN|5,163
                Rumunsko|leu|1|RON|4,757
                Singapur|dolar|1|SGD|16,101
                Švédsko|koruna|1|SEK|2,092
                Švýcarsko|frank|1|CHF|23,969
                Thajsko|baht|100|THB|63,513
                Turecko|lira|1|TRY|1,094
                USA|dolar|1|USD|21,384
                Velká Británie|libra|1|GBP|26,973
                """;
        String url = "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt;?date=10.05.2023";
        String html = ExchangeRateRepository.getHtmlOfRates(url);

        assertEquals(expectedString, html);

        String invalidUrl = "invalid-url";

        // Use assertThrows to verify that an IOException is thrown
        assertThrows(IOException.class, () -> {
            ExchangeRateRepository.getHtmlOfRates(invalidUrl);
        });

    }
}