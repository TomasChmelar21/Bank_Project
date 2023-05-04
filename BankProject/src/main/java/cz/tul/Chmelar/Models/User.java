package cz.tul.Chmelar.Models;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for User
 */
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

    /**
     * Constructor of User
     *
     * @param email - email of User
     * @param password - password of User
     * @param firstName - firstName of User
     * @param lastName - lastName of User
     * @param token - token of User
     * @param proved - proved of User
     * @param account - account number of User
     * @param accounts - accounts of User
     * @param history - history of User
     */
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
