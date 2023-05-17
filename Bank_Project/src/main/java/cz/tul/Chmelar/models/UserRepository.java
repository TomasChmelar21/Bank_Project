package cz.tul.Chmelar.models;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

/**
 * User Repository with finding user in json methods
 */
@Repository
public class UserRepository {

    private static String filePathjson = "data/userdb.json";

    /**
     * return user based on email
     *
     * @param email - users email
     * @return identified user
     */
    public static User findByEmail(String email) {
        try {
            FileReader reader = new FileReader(filePathjson);
            JSONParser parser = new JSONParser();
            JSONObject logObject = (JSONObject) parser.parse(reader);

            JSONArray usersArray = (JSONArray) logObject.get("users");

            for (Object userObj : usersArray) {
                JSONObject userJson = (JSONObject) userObj;
                String userEmail = (String) userJson.get("email");
                if (userEmail.equals(email)) {

                    JSONArray accountsJsonArray = (JSONArray) userJson.get("accounts");
                    Account[] accounts = new Account[accountsJsonArray.size()];
                    for (int i = 0; i < accountsJsonArray.size(); i++) {
                        JSONObject accountJson = (JSONObject) accountsJsonArray.get(i);
                        Account account = new Account();
                        account.setCurrency((String) accountJson.get("currency"));
                        account.setAmount(((Number) accountJson.get("amount")).doubleValue());
                        accounts[i] = account;
                    }

                    JSONArray historyJsonArray = (JSONArray) userJson.get("history");
                    History[] histories = new History[historyJsonArray.size()];
                    for (int i = 0; i < historyJsonArray.size(); i++) {
                        JSONObject historyJson = (JSONObject) historyJsonArray.get(i);
                        History history = new History();
                        history.setTimestamp((String) historyJson.get("timestamp"));
                        history.setAccount((String) historyJson.get("account"));
                        history.setAction((String) historyJson.get("action"));
                        history.setAmount(((Number) historyJson.get("amount")).doubleValue());
                        histories[i] = history;
                    }

                    User user = new User(
                            userEmail,
                            (String) userJson.get("password"),
                            (String) userJson.get("firstName"),
                            (String) userJson.get("lastName"),
                            (String) userJson.get("token"),
                            (String) userJson.get("account"),
                            accounts,
                            histories
                    );
                    reader.close();
                    return user;
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
