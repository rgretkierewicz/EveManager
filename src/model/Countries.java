package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used for creating and accessing data for countries.
 */
public class Countries {
    private int id;
    private String name;
    private static ObservableList<Countries> allCountries = FXCollections.observableArrayList();

    public Countries(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @param newCountry the country to add
     */
    public static void addCountry(Countries newCountry) {
        allCountries.add(newCountry);
    }

    /**
     * @return all countries
     */
    public static ObservableList<Countries> getAllCountries() {
        return allCountries;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the country name
     */
    @Override
    public String toString() {
        return name;
    }

}
