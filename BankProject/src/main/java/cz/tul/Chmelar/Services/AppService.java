package cz.tul.Chmelar.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class AppService {

    public static String getAccountsFromJSON(String email) throws IOException {
        String contents = getContentOfJSON();
        JSONObject json = new JSONObject(contents);
        JSONArray users = json.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                System.out.println((user.getJSONArray("accounts")).toString());
                return (user.getJSONArray("accounts")).toString();
            }
        }
        return null;
    }

    private static String getContentOfJSON() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/main/resources/userdb.json")));
    }

}
