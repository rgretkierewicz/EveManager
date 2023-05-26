package controller;

import DAO.CustomersQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Countries;
import model.Divisions;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class creates a new customer.
 */
public class AddCustomerController implements Initializable {
    @FXML
    private TextField addressTxt;
    @FXML
    private TextField custIdTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField phoneTxt;
    @FXML
    private TextField postTxt;
    @FXML
    private ComboBox<Countries> countryCombo;
    @FXML
    private ComboBox<Divisions> divisionCombo;
    @FXML
    private ComboBox<Users> usersCombo;

    /**
     * This method returns the application to the CustomersInfo scene.
     *
     * @param event The back button is clicked.
     * @throws IOException
     */
    @FXML
    public void onActionBack(ActionEvent event) throws IOException {
        Alert backConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        backConfirmation.setTitle("Customer Not Saved");
        backConfirmation.setHeight(400);
        backConfirmation.setContentText("If you would like to go back the data for this customer will not be saved. Please confirm you would like to proceed.");

        Optional<ButtonType> result = backConfirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SceneSwitcher CustomersInfoScene = new SceneSwitcher();
            CustomersInfoScene.buttonSwitchScene("/view/CustomersInfo.fxml", event);
        }
    }

    /**
     * This method creates a new customer from the entered data.
     * The application then returns to the CustomersInfo scene.
     *
     * @throws SQLException
     */
    @FXML
    public void onActionSaveCustomer(ActionEvent event) throws SQLException, IOException {
        try {
            String custName = nameTxt.getText();
            String address = addressTxt.getText();
            String postalCode = postTxt.getText();
            String phone = phoneTxt.getText();
            int divId = divisionCombo.getValue().getDivisionId();
            String user = usersCombo.getValue().getUserName();

            CustomersQuery.insert(custName, address, postalCode, phone, divId, user);

            SceneSwitcher CustomersInfoScene = new SceneSwitcher();
            CustomersInfoScene.buttonSwitchScene("/view/CustomersInfo.fxml", event);
        } catch (NullPointerException e) {
            Alert invalidInputError = new Alert(Alert.AlertType.ERROR);
            invalidInputError.setTitle("Invalid Input Error");
            invalidInputError.setContentText("Please enter a valid value for each field.");
            invalidInputError.showAndWait();
        }

    }

    /**
     * This method populates the divisions ComboBox based off of selected country.
     *
     * @param event A country is selected from the country ComboBox.
     */
    @FXML
    public void onActionCountrySelection(ActionEvent event) {
        if (countryCombo.getSelectionModel().getSelectedItem().getId() == 1) {
            divisionCombo.setItems(Divisions.getUsDivisions());
        } else if (countryCombo.getSelectionModel().getSelectedItem().getId() == 2) {
            divisionCombo.setItems(Divisions.getUkDivisions());
        } else if (countryCombo.getSelectionModel().getSelectedItem().getId() == 3) {
            divisionCombo.setItems(Divisions.getCanadaDivisions());
        }
    }


    /**
     * This is the initialize method.
     * This method will populate the countries ComboBox with all countries and
     * the users ComboBox with all users.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(Countries.getAllCountries());
        usersCombo.setItems(Users.getAllUsers());
    }
}
