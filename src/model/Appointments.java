package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** This class is used for creating, modifying, and accessing data for appointments. */
public class Appointments {
    private static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<String> appointmentCancellations = FXCollections.observableArrayList();
    private static ObservableList<Appointments> filteredAppointments = FXCollections.observableArrayList();
    private int apptmtId;
    private String title;
    private String description;
    private String location;
    private String contactName;
    private int contactId;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int custId;
    private int userId;
    static boolean resultsFoundA = true;


    public Appointments () {
    }

    public Appointments(int apptmtId, String title, String description, String location, String contactName, int contactId, String type, LocalDateTime start, LocalDateTime end, int custId, int userId) {
        this.apptmtId = apptmtId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactName = contactName;
        this.contactId = contactId;
        this.type = type;
        this.start = start;
        this.end = end;
        this.custId = custId;
        this.userId = userId;
    }

    /**
     * @param newAppointment the appointment to add
     */
    public static void addAppointment(Appointments newAppointment) {
        allAppointments.add(newAppointment);
    }

    /**
     * @return all appointments
     */
    public static ObservableList<Appointments> getAllAppointments() {
        return allAppointments;
    }

    public static int allAppointmentsLength() {
        return allAppointments.size();
    }


    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the appointment's id
     */
    public int getApptmtId() {
        return apptmtId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the contact's name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName the contact name to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return the contact id
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the start
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * @return the customer's id
     */
    public int getCustId() {
        return custId;
    }

    /**
     * @return the user's id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param apptmtToDelete the appointment to delete
     */
    public static void deleteAppointment(Appointments apptmtToDelete) {
        allAppointments.remove(apptmtToDelete);
    }

    /**
     * @param index the index of the appointment to update
     * @param updatedApptmt the updated appointment
     */
    public static void updateAppointment(int index, Appointments updatedApptmt) {
        allAppointments.set(index, updatedApptmt);
    }

    public static Appointments lookupAppointment(int apptmtId) {
        for (Appointments apptmt : allAppointments) {
            if (apptmt.getApptmtId() == apptmtId) {
                return apptmt;
            }
        }
        return null;
    }

    public static ObservableList<Appointments> lookupAppointment(String apptmtTitle) {
        if(!(filteredAppointments.isEmpty())) {
            filteredAppointments.clear();
        }

        for (Appointments apptmt : allAppointments) {
            if (apptmt.getTitle().toLowerCase().contains(apptmtTitle.toLowerCase())) {
                filteredAppointments.add(apptmt);
            }
        }
        if (filteredAppointments.isEmpty()) {
            resultsFoundA = false;
            return allAppointments;
        }
        else {
            resultsFoundA = true;
            return filteredAppointments;
        }
    }

    public static boolean resultsFound() {
        return resultsFoundA;
    }


    /**
     * @param dateNow the current date
     * @return a list of appointments within one month of the current date
     */
    public ObservableList<Appointments> selectMonth(LocalDate dateNow) {
        ObservableList<Appointments> monthApptmts = FXCollections.observableArrayList();

        for (Appointments appointment : allAppointments) {
            LocalDate startDate = appointment.getStart().toLocalDate();

            if (startDate.isAfter(dateNow.minusDays((1))) && startDate.isBefore(dateNow.plusMonths(1).plusDays(1))) {
                monthApptmts.add(appointment);
            }
        }
        return monthApptmts;
    }

    /**
     * @param dateNow the current date
     * @return a list of appointments within one week of the current date
     */
    public ObservableList<Appointments> selectWeek(LocalDate dateNow) {
        ObservableList<Appointments> weekApptmts = FXCollections.observableArrayList();

        for (Appointments appointment : allAppointments) {
            LocalDate startDate = appointment.getStart().toLocalDate();

            if (startDate.isAfter(dateNow.minusDays(1)) && startDate.isBefore(dateNow.plusWeeks(1).plusDays(1))) {
                weekApptmts.add(appointment);
            }
        }
        return weekApptmts;
    }

    /**
     * @param apptmtStart the appointment's start
     * @param apptmtEnd the appointment's end
     * @return the start time of the appointment that overlaps or null if no appointment overlaps
     */
    public static LocalDateTime overlap(LocalDateTime apptmtStart, LocalDateTime apptmtEnd) {
        for (Appointments apptmt : allAppointments) {
            LocalDateTime currStart = apptmt.getStart();
            LocalDateTime currEnd =  apptmt.getEnd();

            if ((apptmtStart.isAfter(currStart) || apptmtStart.isEqual(currStart)) && apptmtStart.isBefore(currEnd)) {
                return currStart;
            }
            else if (apptmtEnd.isAfter(currStart) && (apptmtEnd.isBefore(currEnd) || apptmtEnd.isEqual(currEnd))) {
                return currStart;
            }
            else if ((apptmtStart.isBefore(currStart) || apptmtStart.isEqual(currStart)) && (apptmtEnd.isAfter(currEnd)) || apptmtEnd.isEqual(currEnd)) {
                return currStart;
            }
        }
        return null;
    }

    /**
     * @param apptmtStart the appointment's start
     * @param apptmtEnd the appointment's end
     * @param apptmtId the appointment's id
     * @return the start time of the appointment that overlaps or null if no appointment overlaps
     */
    public static LocalDateTime overlap(LocalDateTime apptmtStart, LocalDateTime apptmtEnd, int apptmtId) {
        for (Appointments apptmt : allAppointments) {
            LocalDateTime currStart = apptmt.getStart();
            LocalDateTime currEnd =  apptmt.getEnd();

            if (apptmt.getApptmtId()!= apptmtId) {
                if ((apptmtStart.isAfter(currStart) || apptmtStart.isEqual(currStart)) && apptmtStart.isBefore(currEnd)) {
                    return currStart;
                }

                else if (apptmtEnd.isAfter(currStart) && (apptmtEnd.isBefore(currEnd) || apptmtEnd.isEqual(currEnd))) {
                    return currStart;
                }

                else if ((apptmtStart.isBefore(currStart) || apptmtStart.isEqual(currStart)) && (apptmtEnd.isAfter(currEnd)) || apptmtEnd.isEqual(currEnd)) {
                    return currStart;
                }
            }

            else {
                continue;
            }
        }
        return null;
    }

    /**
     * @param type the appointment type
     * @param month the appointment month
     * @return the number of appointments matching that appointment type and month
     */
    public int returnApptmts(String type, int month) {
        int apptmtCount = 0;
        for (Appointments apptmt : allAppointments) {
            if (apptmt.getType().equals(type) && apptmt.getStart().getMonthValue() == month) {
                apptmtCount++;
            }
        }
        return apptmtCount;
    }


    /**
     * @param type the appointment type
     * @return the number of appointments matching that appointment type
     */
    public int returnApptmts(String type) {
        int apptmtCount = 0;
        for (Appointments apptmt : allAppointments) {
            if (apptmt.getType().equals(type)) {
                apptmtCount++;
            }
        }
        return apptmtCount;
    }


    /**
     * @param month the appointment month
     * @return the number of appointments matching that appointment month
     */
    public int returnApptmts(int month) {
        int apptmtCount = 0;

        for (Appointments apptmt : allAppointments) {
            if (apptmt.getStart().getMonthValue() == month) {
                apptmtCount++;
            }
        }
        return apptmtCount;
    }

    public static int apptmtsLength() {
        return allAppointments.size();
    }

    /**
     * @param cancellation data for the cancelled appointment
     */
    public void addCancellation(String cancellation) {
        appointmentCancellations.add(cancellation);
    }

    /**
     * @return all cancelled appointments
     */
    public ObservableList<String> getAppointmentCancellations() {
        return appointmentCancellations;
    }

    @Override
    public String toString() {
        return this.getApptmtId() + " " + this.getTitle() + "- " + this.getDescription() + ".";
    }



}
