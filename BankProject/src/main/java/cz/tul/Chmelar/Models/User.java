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
    private String ucet;
    private int token;
    private int overen;

    private Account[] accounts;

    public User(String email, String password, String firstName, String lastName, int token, int overen, String ucet, Account[] accounts) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
        this.token = token;
        this.overen = overen;
        this.ucet = ucet;
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


}
