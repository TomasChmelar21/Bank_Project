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
    private History[] history;

    public User(String email, String password, String firstName, String lastName, String token, String proved, String account, Account[] accounts, History[] history) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
        this.token = token;
        this.proved = proved;
        this.account = account;
        this.history = history;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }


    public Account[] getAccountsArray() {
        return accounts;
    }

    public History[] getHistoryArray() {
        return history;
    }

}
