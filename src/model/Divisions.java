package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class is used for creating and accessing data for divisions. */
public class Divisions {
    private static ObservableList<Divisions> usDivisions = FXCollections.observableArrayList();
    private static ObservableList<Divisions> ukDivisions = FXCollections.observableArrayList();
    private static ObservableList<Divisions> canadaDivisions = FXCollections.observableArrayList();
    private int divisionId;
    private String divisionName;
    private int countryId;

    public Divisions(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**
     * @return all United States divisions
     */
    public static ObservableList<Divisions> getUsDivisions() {
        return usDivisions;
    }

    /**
     * @return all United Kingdom divisions
     */
    public static ObservableList<Divisions> getUkDivisions() {
        return ukDivisions;
    }

    /**
     * @return all Canada divisions
     */
    public static ObservableList<Divisions> getCanadaDivisions() {
        return canadaDivisions;
    }

    /**
     * @param addDivision the division to add to the UK divisions
     */
    public static void addUkDivision(Divisions addDivision) {
        ukDivisions.add(addDivision);
    }

    /**
     * @param addDivision the division to add to the US divisions
     */
    public static void addUsDivision(Divisions addDivision) {
        usDivisions.add(addDivision);
    }


    /**
     * @param addDivision the division to add to the Canada divisions
     */
    public static void addCanadaDivision(Divisions addDivision) {
        canadaDivisions.add(addDivision);
    }

    /**
     * @return the division id
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId the division id to set
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @return the division name
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @param divisionName the division name to set
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * @return the division's country id
     */
    public int getCountryId() {
        return countryId;
    }


    /**
     * @param customer the customer to find the division id of
     * @param countryId the country id of the customer
     * @return the division of the customer
     */
    public static Divisions divByCountryId(Customers customer, int countryId) {
        if (countryId == 1) {
            for (Divisions division : Divisions.getUsDivisions()) {
                if (division.getDivisionId() == customer.getDivisionId()) {
                    return division;
                }
            }
        }

        if (countryId == 2) {
            for (Divisions division : Divisions.getUkDivisions()) {
                if (division.getDivisionId() == customer.getDivisionId()) {
                    return division;
                }
            }
        }

        if (countryId == 3) {
            for (Divisions division : Divisions.getCanadaDivisions()) {
                if (division.getDivisionId() == customer.getDivisionId()) {
                    return division;
                }
            }
        }
        return null;
    }


    /**
     * @return the division name
     */
    @Override
    public String toString() {
        return divisionName;
    }


}
