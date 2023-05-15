package cz.tul.Chmelar.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Model for User
 */
@Getter
@Setter
@AllArgsConstructor
public class User {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String token;
    private String account;

    private Account[] accounts;
    private History[] history;

    public User(String email, String token) {
        this.email = email;
        this.token = token;
    }


    /**
     * User to String
     *
     * @return user in string format
     */
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }


    /**
     * get all accounts of user in array
     *
     * @return array of users accounts
     */
    public Account[] getAccountsArray() {
        return accounts;
    }

    /**
     * get history of user in array
     *
     * @return array of users history
     */
    public History[] getHistoryArray() {
        return history;
    }

}
