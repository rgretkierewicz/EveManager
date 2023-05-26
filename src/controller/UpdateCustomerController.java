package controller;

import DAO.CustomersQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class takes in entered data and updates an existing customer. */
public class UpdateCustomerController implements Initializable {
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
     * This method updates the customer with the entered data.
     * After update is executed, an update successful or update failed alert will be displayed.
     * The application then returns to the CustomersInfo scene.
     * @param event The save button is clicked.
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    public void onActionSaveCustomer (ActionEvent event) throws SQLException, IOException {
        try {
            int custId = Integer.parseInt(custIdTxt.getText());
            String custName = nameTxt.getText();
            String address = addressTxt.getText();
            String postalCode = postTxt.getText();
            String phone = phoneTxt.getText();
            int divId = divisionCombo.getValue().getDivisionId();
            String user = usersCombo.getValue().getUserName();

            int rowsAffected = CustomersQuery.update(custId, custName, address, postalCode, phone, divId, user);

            if (rowsAffected > 0) {
                Alert updateSuccess = new Alert(Alert.AlertType.INFORMATION);
                updateSuccess.setTitle("Update Success");
                updateSuccess.setContentText("Customer update successful.");
                updateSuccess.showAndWait();
            }
            
            else {
                Alert updateFailed = new Alert(Alert.AlertType.INFORMATION);
                updateFailed.setTitle("Update Failed");
                updateFailed.setContentText("Customer update has failed.");
                updateFailed.showAndWait();
            }

            SceneSwitcher CustomersInfoScene = new SceneSwitcher();
            CustomersInfoScene.buttonSwitchScene("/view/CustomersInfo.fxml", event);
        }

        catch (Exception e) {
            Alert invalidInputError = new Alert(Alert.AlertType.ERROR);
            invalidInputError.setTitle("Invalid Input Error");
            invalidInputError.setContentText("Please enter a valid value for each field.");
            invalidInputError.showAndWait();
        }
    }

    /**
     * This method populates the divisions ComboBox based off of selected country.
     * @param event A country is selected from the country ComboBox.
     */
    @FXML
    public void onActionCountrySelection (ActionEvent event) {
        if (countryCombo.getSelectionModel().getSelectedItem().getId() == 1) {
                divisionCombo.setItems(Divisions.getUsDivisions());
        }

        else if (countryCombo.getSelectionModel().getSelectedItem().getId() == 2) {
                divisionCombo.setItems(Divisions.getUkDivisions());
        }

        else if (countryCombo.getSelectionModel().getSelectedItem().getId() == 3) {
                divisionCombo.setItems(Divisions.getCanadaDivisions());
        }
    }


    /**
     * This method receives the customer selected in the CustomersInfo scene and populates the UpdateCustomer
     * fields with the customer's current values.
     * @param customer The customer to be updated.
     */
    public void sendCustomer(Customers customer) {
        custIdTxt.setText(String.valueOf(customer.getCustId()));
        nameTxt.setText(customer.getCustName());
        addressTxt.setText(customer.getAddress());
        postTxt.setText(customer.getPostalCode());
        phoneTxt.setText(customer.getPhone());

        int countryId = 0;

        for (Countries country : Countries.getAllCountries()) {
            if (country.getName().equals(customer.getCountry())) {
                countryCombo.setValue(country);

                countryId = country.getId();

                if (countryId == 1) {
                    divisionCombo.setItems(Divisions.getUsDivisions());
                }

                else if (countryId == 2) {
                    divisionCombo.setItems(Divisions.getUkDivisions());
                }

                else if (countryId == 3) {
                    divisionCombo.setItems(Divisions.getCanadaDivisions());
                }

                Divisions division = Divisions.divByCountryId(customer, countryId);
                divisionCombo.setValue(division);
            }
        }

        for (Users user : Users.getAllUsers()) {
            if (user.getUserName().equals(customer.getUser()))
                usersCombo.setValue(user);
        }

    }


    /**
     * This is the initialize method.
     * This method will populate the country ComboBox with all countries and the users ComboBox with all users.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(Countries.getAllCountries());
        usersCombo.setItems(Users.getAllUsers());
    }
}
