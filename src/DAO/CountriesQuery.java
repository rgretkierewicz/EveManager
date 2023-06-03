package DAO;

import model.Countries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class executes a query to the 'countries' table of the database.*/
public class CountriesQuery {

     /** This method queries the countries table to return all countries. The countries data
     * returned is used to create matching Countries objects within the application.
     * @throws SQLException
     */
    public static void countriesCreation() throws SQLException {
        String sql = "SELECT Country_ID, Country FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int countryId = rs.getInt("Country_ID");
            String name = rs.getString("Country");

            Countries newCountry = new Countries(countryId, name);
            Countries.addCountry(newCountry);
        }

    }

}
