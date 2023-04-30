package cz.tul.Chmelar.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String token;
    private String proved;
    private String account;

    private Account[] accounts;

    public User(String email, String password, String firstName, String lastName, String token, String proved, String account, Account[] accounts) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
        this.token = token;
        this.proved = proved;
        this.account = account;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public String getAccounts() {
        StringBuilder out = new StringBuilder();
        for (Account account : accounts) {
            out.append(account).append(",");
        }
        return out.toString();
    }

    public Account[] getAccountsArray() {
        return accounts;
    }


}
