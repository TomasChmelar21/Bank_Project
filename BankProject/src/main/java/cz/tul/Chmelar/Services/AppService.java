package cz.tul.Chmelar.Services;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class AppService {


    public static Boolean deposit_To_Account(String email, String currency, double amount) throws IOException {
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
                        transaction.put("timestamp", java.time.LocalDateTime.now().toString());
                        transaction.put("account", currency);
                        transaction.put("action", "Deposit");
                        transaction.put("amount", amount);
                        history.put(transaction);
                        return write_To_File(json);
                    }
                }
            }
        }
        return false;

    }

    public static Boolean create_New_Account(String email, String currency) throws IOException {
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
                return write_To_File(json);
            }
        }
        return false;
    }

    private static Boolean write_To_File(JSONObject json) throws IOException {
        try {
            FileWriter file = new FileWriter("src/main/resources/userdb.json");
            file.write(json.toString());
            file.flush();
            file.close();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    private static String getContentOfJSON() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/main/resources/userdb.json")));
    }
    public static String getAccountsFromJSON(String email) throws IOException {
        String contents = getContentOfJSON();
        JSONObject json = new JSONObject(contents);
        JSONArray users = json.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                return (user.getJSONArray("accounts")).toString();
            }
        }
        return null;
    }



}
