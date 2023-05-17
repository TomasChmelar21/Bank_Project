package cz.tul.Chmelar.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
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

    String originalContent = "{"
            + "\"users\": ["
            + "{"
            + "\"firstName\": \"Tomas\","
            + "\"lastName\": \"Chmelar\","
            + "\"proved\": \"0\","
            + "\"password\": \"$2a$10$t3P9PdFohS00sSb4CqnZHOk9yKw2EgZjLoeivzwvJuHpBflsdljx2\","
            + "\"accounts\": ["
            + "{"
            + "\"amount\": 5000,"
            + "\"currency\": \"CZK\""
            + "},"
            + "{"
            + "\"amount\": 1000,"
            + "\"currency\": \"EUR\""
            + "}"
            + "],"
            + "\"history\": ["
            + "{"
            + "\"amount\": 100,"
            + "\"action\": \"Deposit\","
            + "\"account\": \"CZK\","
            + "\"timestamp\": \"2023-05-02 13:26:12.123\""
            + "},"
            + "{"
            + "\"amount\": 200,"
            + "\"action\": \"Payment\","
            + "\"account\": \"EUR\","
            + "\"timestamp\": \"2023-05-02 13:28:11.321\""
            + "}"
            + "],"
            + "\"email\": \"tom.chmelar@seznam.cz\","
            + "\"account\": \"4287 4522 1564 9911\","
            + "\"token\": \"123456\""
            + "},"
            + "{"
            + "\"firstName\": \"Pepa\","
            + "\"lastName\": \"Tester\","
            + "\"proved\": \"0\","
            + "\"password\": \"$2a$10$q.ta9BP8.4Cn7lGBsA82h.f8L8IDowxO.Org9hBKxTiAf8umgtuny\","
            + "\"accounts\": ["
            + "{"
            + "\"amount\": 971.26,"
            + "\"currency\": \"CZK\""
            + "},"
            + "{"
            + "\"amount\": 6097.06,"
            + "\"currency\": \"EUR\""
            + "},"
            + "{"
            + "\"amount\": 0,"
            + "\"currency\": \"ZAR\""
            + "},"
            + "{"
            + "\"amount\": 12345.1,"
            + "\"currency\": \"IDR\""
            + "}"
            + "],"
            + "\"history\": ["
            + "{"
            + "\"amount\": 20,"
            + "\"action\": \"Odesláno\","
            + "\"account\": \"CZK\","
            + "\"timestamp\": \"09.05.2023 14:07\""
            + "},"
            + "{"
            + "\"amount\": 20,"
            + "\"action\": \"Odesláno\","
            + "\"account\": \"CZK\","
            + "\"timestamp\": \"09.05.2023 14:10\""
            + "},"
            + "{"
            + "\"amount\": 21.22,"
            + "\"action\": \"Odesláno\","
            + "\"account\": \"CZK\","
            + "\"timestamp\": \"13.05.2023 14:14\""
            + "}"
            + "],"
            + "\"email\": \"misty211@seznam.cz\","
            + "\"account\": \"4581 1235 1237 6625\","
            + "\"token\": \"972535\""
            + "}"
            + "]"
            + "}";
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

        Path testFilePath = tempDir.resolve("denni_kurz.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath.toFile()))) {
            writer.write(originalContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean result = AppService.userHasAccountOfCurrency(email, currency, testFilePath.toString());

        assertFalse(result, "Expected userHasAccountOfCurrency to return false");
    }

    @Test
    void userHasAccountOfCurrency_shouldReturnTrueIfUserHaveAccount() throws IOException {
        String email = "misty211@seznam.cz";
        String currency = "CZK";

        Path testFilePath = tempDir.resolve("denni_kurz.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath.toFile()))) {
            writer.write(originalContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean result = AppService.userHasAccountOfCurrency(email, currency, testFilePath.toString());

        assertTrue(result, "Expected userHasAccountOfCurrency to return true");

    }

    @Test
    void userHasAccountOfCurrency_shouldReturnFalseIfUserNotExist() throws IOException {
        String email = "neexistuju@seznam.cz";
        String currency = "CZK";

        Path testFilePath = tempDir.resolve("denni_kurz.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath.toFile()))) {
            writer.write(originalContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean result = AppService.userHasAccountOfCurrency(email, currency, testFilePath.toString());

        assertFalse(result, "Expected userHasAccountOfCurrency to return false");

    }

    @Test
    void deleteOldAccount_shouldReturnTrueIfAccoutIsDeleted() throws IOException {

        Path testFilePath = tempDir.resolve("denni_kurz.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath.toFile()))) {
            writer.write(originalContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = "misty211@seznam.cz";
        String currency = "ZAR";
        boolean result = deleteOldAccount(email, currency, testFilePath.toString());

        assertTrue(result, "The account deletion should be successful.");


    }

    @Test
    void deleteOldAccount_shouldReturnFalseIfAccoutWasNotFound() throws IOException {

        Path testFilePath = tempDir.resolve("denni_kurz.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath.toFile()))) {
            writer.write(originalContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String email = "misty211@seznam.cz";
        String currency = "PHP";
        boolean result = deleteOldAccount(email, currency, testFilePath.toString());

        assertFalse(result, "The account wasnt found.");


    }



}
