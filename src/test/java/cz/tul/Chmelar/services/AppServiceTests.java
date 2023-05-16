package cz.tul.Chmelar.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.tul.Chmelar.services.AppService.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppServiceTests {

    " "users": [\n" +
" {\n" +
" "firstName": "Tomas",\n" +
" "lastName": "Chmelar",\n" +
" "proved": "0",\n" +
" "password": "$2a$10$t3P9PdFohS00sSb4CqnZHOk9yKw2EgZjLoeivzwvJuHpBflsdljx2",\n" +
" "accounts": [\n" +
" {\n" +
" "amount": 5000,\n" +
" "currency": "CZK"\n" +
" },\n" +
" {\n" +
" "amount": 1000,\n" +
" "currency": "EUR"\n" +
" }\n" +
" ],\n" +
" "history": [\n" +
" {\n" +
" "amount": 100,\n" +
" "action": "Deposit",\n" +
" "account": "CZK",\n" +
" "timestamp": "2023-05-02 13:26:12.123"\n" +
" },\n" +
" {\n" +
" "amount": 200,\n" +
" "action": "Payment",\n" +
" "account": "EUR",\n" +
" "timestamp": "2023-05-02 13:28:11.321"\n" +
" }\n" +
" ],\n" +
" "email": "tom.chmelar@seznam.cz",\n" +
" "account": "4287 4522 1564 9911",\n" +
" "token": "123456"\n" +
" },\n" +
" {\n" +
" "firstName": "Pepa",\n" +
" "lastName": "Tester",\n" +
" "proved": "0",\n" +
" "password": "$2a$10$q.ta9BP8.4Cn7lGBsA82h.f8L8IDowxO.Org9hBKxTiAf8umgtuny",\n" +
" "accounts": [\n" +
" {\n" +
" "amount": 971.26,\n" +
" "currency": "CZK"\n" +
" },\n" +
" {\n" +
" "amount": 6097.06,\n" +
" "currency": "EUR"\n" +
" },\n" +
" {\n" +
" "amount": 0,\n" +
" "currency": "ZAR"\n" +
" },\n" +
" {\n" +
" "amount": 12345.1,\n" +
" "currency": "IDR"\n" +
" }\n" +
" ],\n" +
" "history": [\n" +
" {\n" +
" "amount": 20,\n" +
"

"action": "Odesláno",\n" +
" "account": "CZK",\n" +
" "timestamp": "09.05.2023 14:07"\n" +
" },\n" +
" {\n" +
" "amount": 20,\n" +
" "action": "Odesláno",\n" +
" "account": "CZK",\n" +
" "timestamp": "09.05.2023 14:10"\n" +
" },\n" +
" {\n" +
" "amount": 21.22,\n" +
" "action": "Odesláno",\n" +
" "account": "CZK",\n" +
" "timestamp": "13.05.2023 14:14"\n" +
" }\n" +
" ],\n" +
" "email": "misty211@seznam.cz",\n" +
" "account": "4581 1235 1237 6625",\n" +
" "token": "972535"\n" +
" }\n" +
" ]\n" +
"}";

    @TempDir
    Path tempDir;

    @Test
    void writeToFile_shouldReturnTrueWhenFileIsWritten() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JSONObject json = new JSONObject();
        File tempFile = File.createTempFile("test", ".json");
        String filePath = tempFile.getAbsolutePath();

        Method method = AppService.class.getDeclaredMethod("writeToFile", JSONObject.class, String.class);
        method.setAccessible(true);

        AppService appService = new AppService();
        boolean result = (boolean) method.invoke(appService, json, filePath);

        assertTrue(result, "Expected writeToFile to return true");
    }

    @Test
    void writeToFile_shouldReturnFalseWhenFileWritingFails() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JSONObject json = new JSONObject();
        File tempFile = File.createTempFile("test", ".json");
        String filePath = tempFile.getAbsolutePath();
        tempFile.setWritable(false);

        Method method = AppService.class.getDeclaredMethod("writeToFile", JSONObject.class, String.class);
        method.setAccessible(true);

        AppService appService = new AppService();
        boolean result = (boolean) method.invoke(appService, json, filePath);

        assertFalse(result, "Expected writeToFile to return false");

        tempFile.delete();
    }


    @Test
    void getContentOfJSON_shouldReturnContentOfJson() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, JSONException {
        JSONObject obj = new JSONObject();
        obj.put("Name", "Tomas");
        obj.put("Author", "Test");

        File tempFile = File.createTempFile("test", ".json");
        String filePath = tempFile.getAbsolutePath();
        FileWriter file = new FileWriter(filePath);
        String expected = obj.toString();
        file.write(expected);
        file.flush();
        file.close();

        Method method = AppService.class.getDeclaredMethod("getContentOfJSON", String.class);
        method.setAccessible(true);

        AppService appService = new AppService();
        String content = (String) method.invoke(appService, filePath);

        assertEquals(expected, content, "Expected file content to match");

        tempFile.delete();
    }


    @Test
    void userHasAccountOfCurrency_shouldReturnFalseIfUserDoesNotHaveAccount() throws IOException, NoSuchMethodException {
        String email = "misty211@seznam.cz";
        String currency = "PHP";

        Path tempFile = tempDir.resolve("test_userdb.json");
        Files.writeString(tempFile, originalContent);
        /*File tempFile = File.createTempFile("test", ".json");
        String filePath = tempFile.getAbsolutePath();
        FileWriter file = new FileWriter(filePath);
        file.write(originalContent);
        file.flush();
        file.close();


        AppService appService = mock(AppService.class);
        when(appService.getContentOfJSON(anyString())).thenReturn(originalContent);*/

        boolean result = AppService.userHasAccountOfCurrency(email, currency, tempFile.toString());

        assertFalse(result, "Expected userHasAccountOfCurrency to return false");
    }

    @Test
    void userHasAccountOfCurrency_shouldReturnTrueIfUserHaveAccount() throws IOException {
        String email = "misty211@seznam.cz";
        String currency = "CZK";

        Path tempFile = tempDir.resolve("test_userdb.json");
        Files.writeString(tempFile, originalContent);

        boolean result = AppService.userHasAccountOfCurrency(email, currency, tempFile.toString());

        assertTrue(result, "Expected userHasAccountOfCurrency to return true");

    }

    @Test
    void userHasAccountOfCurrency_shouldReturnFalseIfUserNotExist() throws IOException {
        String email = "neexistuju@seznam.cz";
        String currency = "CZK";

        Path tempFile = tempDir.resolve("test_userdb.json");
        Files.writeString(tempFile, originalContent);

        boolean result = AppService.userHasAccountOfCurrency(email, currency, tempFile.toString());

        assertFalse(result, "Expected userHasAccountOfCurrency to return false");

    }

    @Test
    void deleteOldAccount_shouldReturnTrueIfAccoutIsDeleted() throws IOException {

        Path tempFile = tempDir.resolve("test_userdb.json");
        Files.writeString(tempFile, originalContent);

        String email = "misty211@seznam.cz";
        String currency = "ZAR";
        boolean result = deleteOldAccount(email, currency, tempFile.toString());

        assertTrue(result, "The account deletion should be successful.");


    }

    @Test
    void deleteOldAccount_shouldReturnFalseIfAccoutWasNotFound() throws IOException {

        Path tempFile = tempDir.resolve("test_userdb.json");
        Files.writeString(tempFile, originalContent);

        String email = "misty211@seznam.cz";
        String currency = "PHP";
        boolean result = deleteOldAccount(email, currency, tempFile.toString());

        assertFalse(result, "The account wasnt found.");


    }



}
