package DAO;

import lambda.ConvertToLocal;
import model.Times;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

/**
 * This class executes queries to the 'users' table of the database.
 */
public class UsersQuery {

    /**
     * @param userUsername the username to validate
     * @param userPassword the password to validate
     * @return username if login is validated, return null if login fails
     * @throws SQLException
     */
    public static String validateLogin(String userUsername, String userPassword) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userUsername);
        ps.setString(2, userPassword);
        ResultSet validateLogin = ps.executeQuery();

        if (validateLogin.next()) {
            String userName = validateLogin.getString("User_Name");

            sql = "UPDATE users SET Num_Login_Fails = 0 WHERE User_Name = ?";
            ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, userUsername);
            ps.executeUpdate();

            return userName;
        } else {
            LocalDateTime utcLoginFail = Times.localToUTC(LocalDateTime.now());

            sql = "UPDATE users SET Num_Login_Fails = Num_Login_Fails + 1, Last_Login_Fail_Time = ? WHERE User_Name = ?";
            ps = JDBC.connection.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(utcLoginFail));
            ps.setString(2, userUsername);
            ps.executeUpdate();

            return null;
        }


    }

    /**
     * This method queries the users table to return all users. The user data
     * returned is used to create matching Users objects within the application.
     *
     * @throws SQLException
     */
    public static void userCreation() throws SQLException {
        String sql = "SELECT * FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");

            Users newUser = new Users(userId, userName, password);
            Users.addUser(newUser);
        }
    }

    /**
     * @param userUsername the username whose status is to be checked
     * @return true if the account is locked, false if account is not locked
     * @throws SQLException
     */
    public static boolean accountStatus(String userUsername) throws SQLException {
        boolean locked = false;

        String sql = "SELECT User_Name, Last_Login_Fail_Time FROM users WHERE Num_Login_Fails >= 5 AND User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userUsername);
        ResultSet rs = ps.executeQuery();

        //If query returns the username, user does have 5 or more login fails
        if (rs.next()) {
            LocalDateTime utcLoginFail = rs.getTimestamp("Last_Login_Fail_Time").toLocalDateTime();

            ConvertToLocal convert = (utcLDT) -> {
                ZonedDateTime utcZDT = ZonedDateTime.of(utcLDT.toLocalDate(), utcLDT.toLocalTime(), ZoneId.of("UTC"));
                LocalTime localTime = utcZDT.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
                LocalDate localDate = utcZDT.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
                LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
                return ldt;
            };

            //Convert db login failure time to local time
            LocalDateTime localLoginFail = convert.utcToLocal(utcLoginFail);

            //Get local time
            LocalDateTime ldtNow = LocalDateTime.now(); //Get local time

            if (ldtNow.isBefore(localLoginFail.plusMinutes(10))) {
                locked = true;
            } else {
                sql = "UPDATE users SET Num_Login_Fails = 0 WHERE User_Name = ?";
                ps = JDBC.connection.prepareStatement(sql);
                ps.setString(1, userUsername);
                ps.executeUpdate();
            }
        }
        return locked;
    }

}
