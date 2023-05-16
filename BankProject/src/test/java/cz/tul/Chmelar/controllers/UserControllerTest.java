package cz.tul.Chmelar.controllers;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class UserControllerTest {
    UserController userController = new UserController();

    private Model model;
    private Authentication authentication;
    private Authentication authenticationWrong;
    @BeforeEach
    void setUp(){
        model = new Model() {
            private Map<String, Object> attributes = new HashMap<>();

            @Override
            public Model addAttribute(String attributeName, Object attributeValue) {
                attributes.put(attributeName, attributeValue);
                return this;
            }

            @Override
            public Model addAttribute(Object attributeValue) {
                return null;
            }

            @Override
            public Model addAllAttributes(Collection<?> attributeValues) {
                return null;
            }

            @Override
            public Model addAllAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public Model mergeAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public boolean containsAttribute(String attributeName) {
                return attributes.containsKey(attributeName);
            }

            @Override
            public Object getAttribute(String attributeName) {
                return attributes.get(attributeName);
            }

            @Override
            public Map<String, Object> asMap() {
                return attributes;
            }
        };

        authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "tom.chmelar@seznam.cz";
            }
        };

        authenticationWrong = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "wrong@seznam.cz";
            }
        };
    }

    @Test
    public void testDeposit() throws Exception {

        String result = userController.deposit(model, authentication);
        Assert.assertEquals("deposit", result);
        String resultWrong = userController.deposit(model, authenticationWrong);
        Assert.assertEquals("error_page", resultWrong);
    }

    @Test
    public void testPayment() throws Exception {
        String result = userController.payment(model, authentication);
        Assert.assertEquals("payment", result);
        String resultWrong = userController.payment(model, authenticationWrong);
        Assert.assertEquals("error_page", resultWrong);
    }

    @Test
    public void testOpen_account() throws Exception {
        String result = userController.open_account(model, authentication);
        Assert.assertEquals("open_account", result);
        String resultWrong = userController.open_account(model, authenticationWrong);
        Assert.assertEquals("error_page", resultWrong);
    }

    @Test
    public void testDelete_account() throws Exception {
        String result = userController.delete_account(model, authentication);
        Assert.assertEquals("delete_account", result);
        String resultWrong = userController.delete_account(model, authenticationWrong);
        Assert.assertEquals("error_page", resultWrong);
    }

    @Test
    public void testProcess_payment() throws Exception {
        String result = userController.process_payment("CZK", 10, model, authentication);
        Boolean success = (Boolean) model.getAttribute("success");
        String message = (String) model.getAttribute("message");

        Assert.assertEquals("account_details", result);
        Assert.assertTrue(success);
        Assert.assertEquals("Transakce v měně CZK proběhla úspěšně", message);
        String resultWrong = userController.process_payment("CZK", 10, model, authenticationWrong);
        Boolean successWrong = (Boolean) model.getAttribute("success");
        String messageWrong = (String) model.getAttribute("message");

        Assert.assertEquals("account_details", resultWrong);
        Assert.assertFalse(successWrong);
        Assert.assertEquals("Transakce v měně CZK neproběhla, protože nejde ze žádného účtu zaplatit", messageWrong);
    }

    @Test
    public void testProcess_deposit() throws Exception {
        String result = userController.process_deposit("CZK", 10, model, authentication);
        Boolean success = (Boolean) model.getAttribute("success");
        String message = (String) model.getAttribute("message");

        Assert.assertEquals("account_details", result);
        Assert.assertTrue(success);
        Assert.assertEquals("Transakce na účet CZK proběhla úspěšně", message);
        String resultWrong = userController.process_deposit("CZK", 10, model, authenticationWrong);
        Boolean successWrong = (Boolean) model.getAttribute("success");
        String messageWrong = (String) model.getAttribute("message");

        Assert.assertEquals("account_details", resultWrong);
        Assert.assertFalse(successWrong);
        Assert.assertEquals("Transakce na účet CZK nemůže být provedena, protože účet neexistuje", messageWrong);
    }

    @Test
    public void testProcess_new_account() throws Exception {
        String result = userController.process_new_account("TRY", model, authentication);
        Boolean success = (Boolean) model.getAttribute("success");
        String message = (String) model.getAttribute("message");

        Assert.assertEquals("account_details", result);
        Assert.assertTrue(success);
        Assert.assertEquals("Účet s měnou TRY je úspěšně založený", message);
        String resultWrong = userController.process_new_account("TRY", model, authenticationWrong);
        Boolean successWrong = (Boolean) model.getAttribute("success");
        String messageWrong = (String) model.getAttribute("message");

        Assert.assertEquals("account_details", resultWrong);
        Assert.assertFalse(successWrong);
        Assert.assertEquals("Účet s měnou TRY se nepodařilo vytvořit", messageWrong);
    }

    @Test
    public void testDelete_old_account() throws Exception {
        String result = userController.delete_old_account("TRY", model, authentication);
        Boolean success = (Boolean) model.getAttribute("success");
        String message = (String) model.getAttribute("message");

        Assert.assertEquals("account_details", result);
        Assert.assertTrue(success);
        Assert.assertEquals("Účet s měnou TRY byl úspěšně smazán", message);
        String resultWrong = userController.delete_old_account("TRY", model, authenticationWrong);
        Boolean successWrong = (Boolean) model.getAttribute("success");
        String messageWrong = (String) model.getAttribute("message");

        Assert.assertEquals("account_details", resultWrong);
        Assert.assertFalse(successWrong);
        Assert.assertEquals("Účet s měnou TRY neexistuje", messageWrong);
    }
}
