package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class is used for creating, modifying, and accessing data for users. */
public class Users {
    private int userId;
    private String userName;
    private String password;
    private static ObservableList<Users> allUsers = FXCollections.observableArrayList();


    public Users(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * @param newUser the user to add
     */
    public static void addUser(Users newUser) {
        allUsers.add(newUser);
    }

    /**
     * @return all users
     */
    public static ObservableList<Users> getAllUsers() {
        return allUsers;
    }

    /**
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return user id and username
     */
    @Override
    public String toString() {
        return "[" + userId + "] " + userName;
    }



}
