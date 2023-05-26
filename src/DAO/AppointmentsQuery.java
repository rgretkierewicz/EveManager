package DAO;

import javafx.scene.control.Alert;
import lambda.ConvertToLocal;
import model.Appointments;
import model.InOffice;
import model.Remote;
import model.Times;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;



/** This class executes queries, updates, inserts, and deletes to the 'appointments' table of the database.*/
public class AppointmentsQuery {
    Appointments appointments = new Appointments();

    /**
     * This method queries the appointments table to return all appointments. The appointment data
     * returned is used to create matching Appointments objects within the application.
     *
     * A lambda expression is used in this method for converting database appointment times
     * from UTC to local time. The lambda expression takes in a LocalDateTime as an argument.
     * The LocalDateTime is made into a ZonedDateTime object with the ZoneId of "UTC".
     * It is then converted to local time by using the system's ZoneId.
     * This expression will be used to convert both the start and end appointment times for each appointment
     * object.
     *
     * @throws SQLException
     */
    public static void appointmentsCreation() throws SQLException {
        String sql = "SELECT Appointment_ID, Venue, Title, Description, Location, contacts.Contact_Name, contacts.Contact_ID, Type, Start, End, appointments.Customer_ID, User_ID FROM appointments JOIN contacts ON contacts.contact_ID = appointments.contact_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int apptmtID = rs.getInt("Appointment_ID");
            String venue = rs.getString("Venue");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String contactName = rs.getString("Contact_Name");
            int contactId = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            int custID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            ConvertToLocal convert = (utcLDT) -> {
                ZonedDateTime utcZDT = ZonedDateTime.of(utcLDT.toLocalDate(), utcLDT.toLocalTime(), ZoneId.of("UTC"));
                LocalTime localTime = utcZDT.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
                LocalDate localDate =  utcZDT.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
                LocalDateTime ldt = LocalDateTime.of(localDate, localTime);

                return ldt;
            };


            LocalDateTime utcStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime localTimeStart = convert.utcToLocal(utcStart);

            LocalDateTime utcEnd = rs.getTimestamp("End").toLocalDateTime();
            LocalDateTime localTimeEnd = convert.utcToLocal(utcEnd );

            /*
            Appointments newAppointment = new Appointments(apptmtID, title, description, location, contactName, contactId, type, localTimeStart, localTimeEnd, custID, userID);
            Appointments.addAppointment(newAppointment);

             */

            if (venue.equals("In-Office")) {
                InOffice newApptmt = new InOffice(apptmtID, title, description, location, contactName, contactId, type, localTimeStart, localTimeEnd, custID, userID);
                InOffice.addAppointment(newApptmt);
                Appointments.addAppointment(newApptmt);
            }
            //Venue = Remote
            else {
                Remote newApptmt = new Remote(apptmtID, title, description, location, contactName, contactId, type, localTimeStart, localTimeEnd, custID, userID);
                Remote.addAppointment(newApptmt);
                Appointments.addAppointment(newApptmt);

            }


        }
    }

    /**
     * This method removes an appointment from the database.
     * @param apptmtId The id of the appointment to be removed.
     * @return 1 if delete is successful, 0 if delete fails
     * @throws SQLException
     */
    public static int delete(int apptmtId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptmtId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * This method checks all appointment's date and time against the user's current local time.
     * If an appointment is beginning within 15 minutes of the local time, an alert is displayed notifying the
     * user of the upcoming appointment. If there are no appointments starting within 15 minutes of the
     * local time, an alert is displayed notifying the user that there are no upcoming appointments.
     * @throws SQLException
     */
    public static void upcomingApptmts() throws SQLException {
        String sql = "SELECT Start, End, Appointment_ID FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        boolean apptmt = false;

        while (rs.next()) {
            int apptmtId = rs.getInt("Appointment_ID");
            Timestamp startTS = rs.getTimestamp("Start");
            LocalDateTime startLdt = startTS.toLocalDateTime();

            ZonedDateTime dbStart = ZonedDateTime.of(startLdt.toLocalDate(), startLdt.toLocalTime(), ZoneId.of("UTC"));
            LocalTime apptmtStartTime =  dbStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
            LocalDate apptmtStartDate = dbStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
            LocalDateTime ldtStart = LocalDateTime.of(apptmtStartDate, apptmtStartTime);

            LocalDateTime currentTime = LocalDateTime.now();

            long timeDifference = ChronoUnit.MINUTES.between(ldtStart, currentTime);

            if (timeDifference >= -15 && timeDifference <= 0) {

                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss, yyyy-MM-dd");

                Alert upcomingAppointment = new Alert(Alert.AlertType.CONFIRMATION);
                upcomingAppointment.setTitle("Upcoming Appointment");
                upcomingAppointment.setContentText("Welcome. Appointment " + apptmtId + " will begin in " + (timeDifference * -1) +  " minutes at " + ldtStart.format(dateFormat) + ".");
                upcomingAppointment.showAndWait();

                apptmt = true;
            }
        }

        if (!apptmt) {
            Alert upcomingAppointment = new Alert(Alert.AlertType.INFORMATION);
            upcomingAppointment.setTitle("No Upcoming Appointments");
            upcomingAppointment.setContentText("Welcome. There are no upcoming appointments.");
            upcomingAppointment.showAndWait();
        }
    }

    /**
     * This method takes in all appointment values to update and executes an update on the database.
     * The application's appointment object is updated.
     * @param apptmtId the id of the appointment to update
     * @param title the appointment title
     * @param loc the appointment location
     * @param desc the appointment description
     * @param contactId the contact's id
     * @param contactName the contact's name
     * @param type the appointment type
     * @param start the appointment start
     * @param end the appointment end
     * @param user the user
     * @param custId the customer's id
     * @param userId the user's id
     * @return 1 if appointment update succeeds, return 0 if appointment update fails
     * @throws SQLException
     */
    public static int update(int apptmtId, String title, String loc, String desc, int contactId, String contactName, String type, LocalDateTime start, LocalDateTime end, String user, int custId, int userId, String venue) throws SQLException {
        LocalDateTime utcStart = Times.localToUTC(start);
        LocalDateTime utcEnd = Times.localToUTC(end);

        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = NOW(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?, Venue = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, loc);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(utcStart));
        ps.setTimestamp(6, Timestamp.valueOf(utcEnd));
        ps.setString(7,user);
        ps.setInt(8, custId);
        ps.setInt(9, userId);
        ps.setInt(10, contactId);
        ps.setString(11, venue);
        ps.setInt(12, apptmtId);

        int rowsAffected = ps.executeUpdate();

        Appointments updatedApptmt = new Appointments(apptmtId, title, desc, loc, contactName, contactId, type, start, end, custId, userId);

        int index = -1;
        for (Appointments apptmt : Appointments.getAllAppointments()) {
            index++;

            if (apptmt.getApptmtId() == apptmtId) {
                Appointments.updateAppointment(index, updatedApptmt);
            }
        }

        if (venue.equals("In-Office")) {
            index = -1;
            for (Appointments apptmt : InOffice.getAllAppointments()) {
                index++;

                if (apptmt.getApptmtId() == apptmtId) {
                    InOffice.updateAppointment(index, updatedApptmt);
                }

            }
        }
        if (venue.equals("Remote")) {
            index = -1;
            for (Appointments apptmt : Remote.getAllAppointments()) {
                index++;

                if (apptmt.getApptmtId() == apptmtId) {
                    Remote.updateAppointment(index, updatedApptmt);
                }
            }
        }

        return rowsAffected;

    }

    /**
     *  This method takes in all appointment values and inserts that appointment into the database.
     *  The application's appointment object is created.
     * @param title the appointment title
     * @param loc the appointment location
     * @param desc the appointment description
     * @param contactId the contact's id
     * @param contactName the contact's name
     * @param type the appointment type
     * @param start the appointment start
     * @param end the appointment end
     * @param user the user
     * @param custId the customer's id
     * @param userId the user's id
     * @throws SQLException
     */
    public static void insert(String title, String desc, String loc, int contactId, String contactName, String type, LocalDateTime start, LocalDateTime end, String user, int custId, int userId, String venue) throws SQLException {
        LocalDateTime utcStart = Times.localToUTC(start);
        LocalDateTime utcEnd = Times.localToUTC(end);

        String sql = "INSERT INTO appointments VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, venue);
        ps.setString(2, title);
        ps.setString(3, desc);
        ps.setString(4, loc);
        ps.setString(5, type);
        ps.setTimestamp(6, Timestamp.valueOf(utcStart));
        ps.setTimestamp(7, Timestamp.valueOf(utcEnd));
        ps.setString(8, user);
        ps.setString(9, user);
        ps.setInt(10, custId);
        ps.setInt(11, userId);
        ps.setInt(12, contactId);
        ps.execute();

        ps = JDBC.connection.prepareStatement("SELECT LAST_INSERT_ID()");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int apptmtId = rs.getInt(1);


        if (type.equals("Remote")) {
            Remote newApptmt = new Remote(apptmtId, title, desc, loc, contactName, contactId, type, start, end, custId, userId);
            Remote.addAppointment(newApptmt);
            Appointments.addAppointment(newApptmt);

        }

        else {
            InOffice newApptmt = new InOffice(apptmtId, title, desc, loc, contactName, contactId, type, start, end, custId, userId);
            InOffice.addAppointment(newApptmt);
            Appointments.addAppointment(newApptmt);
        }

    }


}
