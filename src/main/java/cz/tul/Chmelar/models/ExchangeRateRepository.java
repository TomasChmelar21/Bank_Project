package cz.tul.Chmelar.models;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ExchangeRateRepository {

    /**
     * get List of Exchange Rates from ExchangeRateArray() method
     *
     * @return List of all Exchange rate from file
     */
    public static List<ExchangeRate> getListOfExchangeRates() {
        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        String[][] exchangeRateArray = getExchangeRateArray("src/main/resources/denni_kurz.txt");
        for (String[] exchangeRate : exchangeRateArray) {
            ExchangeRate exchangeRateObj = new ExchangeRate(
                    exchangeRate[0],
                    exchangeRate[1],
                    exchangeRate[2],
                    exchangeRate[3],
                    exchangeRate[4]
            );
            exchangeRateList.add(exchangeRateObj);
        }
        return exchangeRateList;
    }


    /**
     * transfer string from exchange rate file to String[][]
     *
     * @return String[][] of exchange rates
     */
    public static String[][] getExchangeRateArray(String filePath) {
        String[] exchangeRates = readExchangeRateFile(filePath).split("\n");
        String[][] output = new String[exchangeRates.length - 2][5];
        for (int i = 0; i < exchangeRates.length - 2; i++) {
            String line = exchangeRates[i + 2];
            output[i] = line.split("\\|");
        }
        return output;
    }

    /**
     * get date from first row of Exchange rate file
     *
     * @return date from exchange rate file
     */
    public static String getExchangeRateTime(String filePath) {
        String[] exchangeRates = readExchangeRateFile(filePath).split("\n");
        String output = exchangeRates[0];
        output = output.substring(0, output.indexOf(' '));
        return output;
    }

    /**
     * read exchange rates from src/main/resources/denni_kurz.txt file
     *
     * @return String of exchange rate text file
     */
    public static String readExchangeRateFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return contentBuilder.toString();
    }

    /**
     * get rates toString from Html file from url address
     *
     * @param url - URL address of html we want to read
     * @return - String of html page
     */
    public static String getHtmlOfRates(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw e;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get one Exchange rate from Currency type
     *
     * @param currency - shortcut of currency
     * @return ExchangeRate of currency
     */
    public static ExchangeRate getExchangeRateByCurrency(String filePath, String currency) {
        String[][] exchangeRates  = getExchangeRateArray(filePath);
        for (String[] rate : exchangeRates) {
            if (rate[3].equalsIgnoreCase(currency)) {
                ExchangeRate Exchangerate = new ExchangeRate(rate[0], rate[1], rate[2], rate[3], rate[4]);
                return Exchangerate;
            }
        }
        return null;
    }

    }
