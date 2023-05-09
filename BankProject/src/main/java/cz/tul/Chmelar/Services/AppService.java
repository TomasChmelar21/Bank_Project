package cz.tul.Chmelar.Services;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import static cz.tul.Chmelar.Services.ExchangeRateService.transferExchangeRateCount;

/**
 * Service of all user functions
 * deposit, payment, new account, delete account, write to file
 */
@Service
public class AppService {

    /**
     * deposit money to users account
     *
     * @param email - users email
     * @param currency - account where user want to deposit money
     * @param amount - amount of depositing money
     * @return true if everything is success
     * @throws IOException
     */
    public static Boolean depositToAccount(String email, String currency, double amount) throws IOException {
        String json_content = getContentOfJSON();
        JSONObject json = new JSONObject(json_content);
        JSONArray users = json.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                JSONArray accounts = user.getJSONArray("accounts");
                for (int j = 0; j < accounts.length(); j++) {

                    JSONObject account = accounts.getJSONObject(j);
                    if (account.getString("currency").equals(currency)) {
                        double current_Amount = account.getDouble("amount");
                        double new_Amount = current_Amount + amount;
                        account.put("amount", new_Amount);
                        JSONArray history = user.getJSONArray("history");
                        JSONObject transaction = new JSONObject();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                        String formattedDateTime = LocalDateTime.now().format(formatter);
                        transaction.put("timestamp", formattedDateTime.toString());
                        transaction.put("account", currency);
                        transaction.put("action", "Přidáno");
                        transaction.put("amount", amount);
                        history.put(transaction);
                        return writeToFile(json);
                    }
                }
            }
        }
        return false;

    }

    /**
     * pay money from users account
     *
     * @param email - users email
     * @param currency - currency in which user want to pay
     * @param amount - amount of paying money
     * @return true if everything is success
     * @throws IOException
     */
    public static Boolean paymentFromAccount(String email, String currency, double amount) throws IOException {
        if (amount <= 0) {
            return false;
        }
        String json_content = getContentOfJSON();
        JSONObject json = new JSONObject(json_content);
        JSONArray users = json.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                JSONArray accounts = user.getJSONArray("accounts");
                for (int j = 0; j < accounts.length(); j++) {

                    JSONObject account = accounts.getJSONObject(j);
                    if (account.getString("currency").equals(currency)) {
                        double current_Amount = account.getDouble("amount");
                        if (current_Amount > amount) {
                            double new_Amount = current_Amount - amount;
                            new_Amount = Math.round(new_Amount * 100.0) / 100.0;
                            account.put("amount", new_Amount);
                            JSONArray history = user.getJSONArray("history");
                            JSONObject transaction = new JSONObject();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                            String formattedDateTime = LocalDateTime.now().format(formatter);
                            transaction.put("timestamp", formattedDateTime.toString());
                            transaction.put("account", currency);
                            transaction.put("action", "Odesláno");
                            transaction.put("amount", amount);
                            history.put(transaction);
                            return writeToFile(json);
                        }
                        else {
                            return paymentFromAnyAccount(user, accounts, currency, amount, json);
                        }
                    }
                }
                return paymentFromAnyAccount(user, accounts, currency, amount, json);
            }
        }

        return false;
    }

    /**
     * pay from any users account that has enough money
     *
     * @param user - user as JSONObject
     * @param accounts - users accounts as JSONArray
     * @param currency - currency in which user want to pay
     * @param amount - amount of money user want to pay
     * @param json - file of users
     * @return true if everything is success
     * @throws IOException
     */
    public static Boolean paymentFromAnyAccount(JSONObject user, JSONArray accounts, String currency, double amount, JSONObject json) throws IOException {
        for (int k = 0; k < accounts.length(); k++) {
            JSONObject account = accounts.getJSONObject(k);
            double current_Amount = account.getDouble("amount");
            double transferedtoCurrency = transferExchangeRateCount(currency, account.getString("currency"), amount);
            transferedtoCurrency = Math.round(transferedtoCurrency * 100.0) / 100.0;
            if(transferedtoCurrency < current_Amount) {
                double new_Amount = current_Amount - transferedtoCurrency;
                new_Amount = Math.round(new_Amount * 100.0) / 100.0;
                account.put("amount", new_Amount);
                JSONArray history = user.getJSONArray("history");
                JSONObject transaction = new JSONObject();
                transaction.put("timestamp", java.time.LocalDateTime.now().toString());
                transaction.put("account", account.getString("currency"));
                transaction.put("action", "Odesláno");
                transaction.put("amount", transferedtoCurrency);
                history.put(transaction);
                return writeToFile(json);

            }
        }
        return false;
    }

    /**
     * method to create new users account
     *
     * @param email - users email
     * @param currency - currency user want to create account
     * @return true if everything is success
     * @throws IOException
     */
    public static Boolean createNewAccount(String email, String currency) throws IOException {
        String contents = getContentOfJSON();
        JSONObject json = new JSONObject(contents);
        JSONArray users = json.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                JSONArray accounts = user.getJSONArray("accounts");
                JSONObject new_Account = new JSONObject();
                new_Account.put("currency", currency);
                new_Account.put("amount", 0);
                accounts.put(new_Account);
                return writeToFile(json);
            }
        }
        return false;
    }

    /**
     * delete users account
     *
     * @param email - users email
     * @param currency - currency of account user want to delete
     * @return true if everything is success
     * @throws IOException
     */
    public static Boolean deleteOldAccount(String email, String currency) throws IOException {
        String contents = getContentOfJSON();
        JSONObject json = new JSONObject(contents);
        JSONArray users = json.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                JSONArray accounts = user.getJSONArray("accounts");
                for (int j = 0; j < accounts.length(); j++) {
                    JSONObject account = accounts.getJSONObject(j);
                    if (account.getString("currency").equals(currency)) {
                        accounts.remove(j);
                        return writeToFile(json);
                    }
                }
            }
        }
        return false;
    }

    /**
     * rewrite userdb file
     *
     * @param json - json file which we want to override
     * @return true if everything is success
     * @throws IOException
     */
    private static Boolean writeToFile(JSONObject json) throws IOException {
        try {
            FileWriter file = new FileWriter("src/main/resources/userdb.json");
            file.write(json.toString());
            file.flush();
            file.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * get content of json file
     *
     * @return String of json file
     * @throws IOException
     */
    private static String getContentOfJSON() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/main/resources/userdb.json")));
    }

    /**
     * check if user has account with certain currency
     *
     * @param email - users email
     * @param currency - currency of account which method searching
     * @return true if user has account of currency
     * @throws IOException
     */
    public static Boolean userHasAccountOfCurrency(String email, String currency) throws IOException {
        String contents = getContentOfJSON();
        JSONObject json = new JSONObject(contents);
        JSONArray users = json.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                JSONArray accounts = user.getJSONArray("accounts");
                for (int j = 0; j < accounts.length(); j++) {
                    JSONObject account = accounts.getJSONObject(j);
                    if (account.getString("currency").equals(currency)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



}
