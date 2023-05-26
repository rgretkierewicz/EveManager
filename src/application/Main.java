package application;

import DAO.*;
import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;


/** This class launches the application. */
public class Main extends Application {

    /**
     * This is the start method. This will launch the first screen of the application.
     * @param primaryStage The primary stage, the first scene of the application is shown here.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    /**
     * This is the main method, this will connect the application to the database and execute queries
     * that will collect data to be used in the application.
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();

        AppointmentsQuery.appointmentsCreation();
        ContactsQuery.contactsCreation();
        CountriesQuery.countriesCreation();
        CustomersQuery.customersCreation();
        DivisionsQuery.divisionsCreation();
        UsersQuery.userCreation();

        /*
        for (Appointments appointment : Appointments.getAllAppointments()) {
            System.out.println("Appointment Class: " + appointment);
        }

        for (Appointments appointment : InOffice.getAllAppointments()) {
            System.out.println("In-Office Class: " + appointment);
        }

        for (Appointments appointment : Remote.getAllAppointments()) {
            System.out.println("Remote Class: " + appointment);
        }
        */


        launch(args);

        JDBC.closeConnection();
    }

}
