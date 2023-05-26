package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** This class is used for creating, modifying, and accessing data for in-office appointments. */
public class InOffice extends Appointments{
    private static ObservableList<Appointments> allInOfficeAppointments = FXCollections.observableArrayList();

    /*
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

     */

    public InOffice(int apptmtId, String title, String description, String location, String contactName, int contactId, String type, LocalDateTime start, LocalDateTime end, int custId, int userId) {
        super(apptmtId, title, description, location, contactName, contactId, type, start, end, custId, userId);
    }

    public InOffice() {
    }

    /**
     * @param newAppointment the in-office appointment to add
     */
    public static void addAppointment(InOffice newAppointment) {
        allInOfficeAppointments.add(newAppointment);
    }


    /**
     * @return all in-office appointments
     */
    public static ObservableList<Appointments> getAllAppointments() {
        return allInOfficeAppointments;
    }

    public static int allAppointmentsLength() {
        return allInOfficeAppointments.size();
    }


    /**
     * @param apptmtToDelete the appointment to delete
     */
    public static void deleteAppointment(InOffice apptmtToDelete) {
        allInOfficeAppointments.remove(apptmtToDelete);
    }

    /**
     * @param index the index of the appointment to update
     * @param updatedApptmt the updated appointment
     */
    public static void updateAppointment(int index, InOffice updatedApptmt) {
        allInOfficeAppointments.set(index, updatedApptmt);
    }

    /**
     * @param dateNow the current date
     * @return a list of appointments within one month of the current date
     */
    @Override
    public ObservableList<Appointments> selectMonth(LocalDate dateNow) {
        ObservableList<Appointments> monthApptmts = FXCollections.observableArrayList();

        for (Appointments rAppointment : allInOfficeAppointments) {
            LocalDate startDate = rAppointment.getStart().toLocalDate();

            if (startDate.isAfter(dateNow.minusDays((1))) && startDate.isBefore(dateNow.plusMonths(1).plusDays(1))) {
                monthApptmts.add(rAppointment);
            }
        }
        return monthApptmts;
    }

    /**
     * @param dateNow the current date
     * @return a list of appointments within one week of the current date
     */
    @Override
    public ObservableList<Appointments> selectWeek(LocalDate dateNow) {
        ObservableList<Appointments> weekApptmts = FXCollections.observableArrayList();

        for (Appointments appointment : allInOfficeAppointments) {
            LocalDate startDate = appointment.getStart().toLocalDate();

            if (startDate.isAfter(dateNow.minusDays(1)) && startDate.isBefore(dateNow.plusWeeks(1).plusDays(1))) {
                weekApptmts.add(appointment);
            }
        }
        return weekApptmts;
    }



    /**
     * @param type the appointment type
     * @param month the appointment month
     * @return the number of appointments matching that appointment type and month
     */
    @Override
    public int returnApptmts(String type, int month) {
        int apptmtCount = 0;
        for (Appointments apptmt : allInOfficeAppointments) {
            if (apptmt.getType().equals(type) && apptmt.getStart().getMonthValue() == month) {
                apptmtCount++;
                //TEST
                System.out.println("In-Office Test -----" + apptmt);
            }
        }
        return apptmtCount;
    }


    /**
     * @param type the appointment type
     * @return the number of appointments matching that appointment type
     */
    @Override
    public int returnApptmts(String type) {
        int apptmtCount = 0;
        for (Appointments apptmt : allInOfficeAppointments) {
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
    @Override
    public int returnApptmts(int month) {
        int apptmtCount = 0;

        for (Appointments apptmt : allInOfficeAppointments) {
            if (apptmt.getStart().getMonthValue() == month) {
                apptmtCount++;
            }
        }
        return apptmtCount;
    }



}
