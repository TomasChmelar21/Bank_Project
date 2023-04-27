package cz.tul.Chmelar.Models;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

//    File file = new File("log.json");

    public static List<User> findAll() {
        List<User> users = new ArrayList<>();

        try {
            FileReader reader = new FileReader("src/main/resources/log.json");
            JSONParser parser = new JSONParser();
            JSONObject logObject = (JSONObject) parser.parse(reader);

            JSONArray usersArray = (JSONArray) logObject.get("users");

            for (Object userObj : usersArray) {
                JSONObject userJson = (JSONObject) userObj;

                JSONArray accountsJsonArray = (JSONArray) userJson.get("ucty");
                Account[] accounts = new Account[accountsJsonArray.size()];
                for (int i = 0; i < accountsJsonArray.size(); i++) {
                    JSONObject accountJson = (JSONObject) accountsJsonArray.get(i);
                    Account account = new Account();
                    account.setCurrency((String) accountJson.get("mena"));
                    account.setAmount((double) accountJson.get("castka"));
                    accounts[i] = account;
                }

                User user = new User(
                        (String) userJson.get("email"),
                        (String) userJson.get("heslo"),
                        (String) userJson.get("jmeno"),
                        (String) userJson.get("prijmeni"),
                        (int) userJson.get("token"),
                        (int) userJson.get("overen"),
                        (String) userJson.get("ucet"),
                        accounts
                );
                users.add(user);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public static User findByEmail(String email) {
        try {
            FileReader reader = new FileReader("src/main/resources/userdb.json");
            JSONParser parser = new JSONParser();
            JSONObject logObject = (JSONObject) parser.parse(reader);

            JSONArray usersArray = (JSONArray) logObject.get("users");

            for (Object userObj : usersArray) {
                JSONObject userJson = (JSONObject) userObj;
                String userEmail = (String) userJson.get("email");
                if (userEmail.equals(email)) {

                    JSONArray accountsJsonArray = (JSONArray) userJson.get("ucty");
                    Account[] accounts = new Account[accountsJsonArray.size()];
                    for (int i = 0; i < accountsJsonArray.size(); i++) {
                        JSONObject accountJson = (JSONObject) accountsJsonArray.get(i);
                        Account account = new Account();
                        account.setCurrency((String) accountJson.get("mena"));
                        account.setAmount((double) accountJson.get("castka"));
                        accounts[i] = account;
                    }

                    User user = new User(
                            userEmail,
                            (String) userJson.get("heslo"),
                            (String) userJson.get("jmeno"),
                            (String) userJson.get("prijmeni"),
                            (int) userJson.get("token"),
                            (int) userJson.get("overen"),
                            (String) userJson.get("ucet"),
                            accounts
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
