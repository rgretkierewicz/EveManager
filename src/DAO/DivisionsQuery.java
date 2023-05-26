package DAO;

import model.Divisions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** This class executes a query to the 'first_level_divisions' table of the database.*/
public class DivisionsQuery {

    /*This method queries the first_level_divisions table to return all divisions. The divisions data
     * returned is used to create matching Divisions objects within the application.
     * @throws SQLException
     */
    public static void divisionsCreation() throws SQLException {
        String sql = "SELECT Division_ID, Division, Country_ID FROM first_level_divisions";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int divId = rs.getInt("Division_ID");
            String name = rs.getString("Division");
            int countryId = rs.getInt("Country_ID");

            Divisions newDivision = new Divisions(divId, name, countryId);

            if(countryId == 1) {
                Divisions.addUsDivision(newDivision);
            }

            else if(countryId == 2) {
                Divisions.addUkDivision(newDivision);
            }

            else if(countryId == 3) {
                Divisions.addCanadaDivision(newDivision);
            }
        }
    }

}
