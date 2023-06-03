package controller;

import DAO.AppointmentsQuery;
import DAO.UsersQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lambda.LoginReports;
import model.Times;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * This class is the first screen shown to the user, the Login form.
 * This class allows the user to enter a username and password.
 * Once validated, the user will be sent to the Home scene.
 */
public class LoginFormController implements Initializable {
    @FXML
    private Button exitBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Label passwordLbl;
    @FXML
    private TextField passwordTxt;
    @FXML
    private Label usernameLbl;
    @FXML
    private TextField usernameTxt;
    @FXML
    private Label welcomeLbl;
    @FXML
    private Label locLbl;
    @FXML
    private Label userLocLbl;

    ResourceBundle rb;

    ZoneId localZoneId;

    /**
     * This method terminates the application.
     *
     * @param event The exit button is clicked.
     */
    public void onActionExit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * This method validates a username and password.
     * The first thing that will be checked is if the account is logged out due to too many failed login attempts.
     * If the account is locked an alert will be displayed notifying the user of the account status.
     * <p>
     * If the account is unlocked and the credentials are valid, the user is navigated to the Appointments Info scene and
     * an alert will be displayed notifying the user if there are any upcoming appointments.
     * If credentials are invalid, an error message is displayed.
     * <p>
     * A lambda expression is used in this method for adding to the login reports file,
     * login_activity.txt. The lambda expression takes in a string as an argument. The string
     * tells whether the login attempt passed or failed and the name of the user who made the attempt.
     * The lambda expression creates the FileWriter and PrintWriter objects, adds
     * the date and time converted to UTC to the string, then adds that string to the login report.
     * The lambda expression is called for both the conditions of a login success or a login failure.
     *
     * @param event The Login button is clicked.
     * @throws Exception
     */
    public void onActionLogin(ActionEvent event) throws Exception {
        String userPassword = passwordTxt.getText();
        String userUsername = usernameTxt.getText();

        boolean locked = UsersQuery.accountStatus(userUsername);

        if (locked) {
            Alert invalidEntry = new Alert(Alert.AlertType.ERROR);
            invalidEntry.setContentText(rb.getString("Locked"));
            invalidEntry.setHeight(400);
            invalidEntry.showAndWait();
        } else {
            String validatedUser = UsersQuery.validateLogin(userUsername, userPassword);
            String login;

            //LAMBDA
            LoginReports report = (loginTxt) -> {
                String fileName = "src/login_activity.txt";
                FileWriter fileWriter = new FileWriter(fileName, true);
                PrintWriter outputFile = new PrintWriter(fileWriter);

                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime currentTimeToUTC = Times.localToUTC(currentTime);
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                loginTxt += dateFormat.format(currentTimeToUTC) + " UTC.";

                outputFile.println(loginTxt);
                outputFile.close();
            };

            if (validatedUser == null) {
                login = "Failed login attempt by the user: " + userUsername + " at ";
                report.addLoginReport(login);


                Alert invalidEntry = new Alert(Alert.AlertType.ERROR);
                invalidEntry.setContentText(rb.getString("Error"));
                invalidEntry.showAndWait();

            } else {
                SceneSwitcher AppointmentsInfoScene = new SceneSwitcher();
                AppointmentsInfoScene.buttonSwitchScene("/view/AppointmentsInfo.fxml", event);

                login = "Successful login by the user: " + userUsername + " at ";
                report.addLoginReport(login);

                try {
                    AppointmentsQuery.upcomingApptmts();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }

    /**
     * This is the initialize method.
     * This method will translate all form text to the user's local language of English or French.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = ResourceBundle.getBundle("Lan", Locale.getDefault());
        localZoneId = ZoneId.of(TimeZone.getDefault().getID());

        if (Locale.getDefault().getLanguage().equals("fr")) {
            welcomeLbl.setText(rb.getString("Welcome!"));
            usernameLbl.setText(rb.getString("Username"));
            passwordLbl.setText(rb.getString("Password"));
            loginBtn.setText(rb.getString("Login"));
            locLbl.setText(rb.getString("Location"));
            exitBtn.setText(rb.getString("Exit"));
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            locLbl.setText(rb.getString("Location"));
        }
        userLocLbl.setText(String.valueOf(localZoneId));
    }

}