package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** This class is used for creating, modifying, and accessing data for remote appointments. */
public class Remote extends Appointments{
    private static ObservableList<Appointments> allRemoteAppointments = FXCollections.observableArrayList();
    private static ObservableList<String> appointmentCancellations = FXCollections.observableArrayList();
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
    String apptmtPlatform;
    //Remote remote = new Remote();

    public Remote() {
    }
    //String apptmtPlatform,

    public Remote(int apptmtId, String title, String description, String location, String contactName, int contactId, String type, LocalDateTime start, LocalDateTime end, int custId, int userId) {
        super(apptmtId, title, description, location, contactName, contactId, type, start, end, custId, userId);
        //this.apptmtPlatform = apptmtPlatform;
    }

    /**
     * @param newAppointment the remote appointment to add
     */
    public static void addAppointment(Remote newAppointment) {
        allRemoteAppointments.add(newAppointment);
    }

    /**
     * @return all remote appointments
     */
    public static ObservableList<Appointments> getAllAppointments() {
        return allRemoteAppointments;
    }

    public static int allAppointmentsLength() {
        return allRemoteAppointments.size();
    }


    public static void deleteAppointment(Remote apptmtToDelete) {
        allRemoteAppointments.remove(apptmtToDelete);
    }

    /**
     * @param index the index of the appointment to update
     * @param updatedApptmt the updated appointment
     */
    public static void updateAppointment(int index, Remote updatedApptmt) {
        allRemoteAppointments.set(index, updatedApptmt);
    }


    /**
     * @param dateNow the current date
     * @return a list of appointments within one month of the current date
     */
    @Override
    public ObservableList<Appointments> selectMonth(LocalDate dateNow) {
        ObservableList<Appointments> monthApptmts = FXCollections.observableArrayList();

        for (Appointments rAppointment : allRemoteAppointments) {
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
    public  ObservableList<Appointments> selectWeek(LocalDate dateNow) {
        ObservableList<Appointments> weekApptmts = FXCollections.observableArrayList();

        for (Appointments appointment : allRemoteAppointments) {
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
        for (Appointments apptmt : allRemoteAppointments) {
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
    @Override
    public int returnApptmts(String type) {
        int apptmtCount = 0;
        for (Appointments apptmt : allRemoteAppointments) {
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

        for (Appointments apptmt : allRemoteAppointments) {
            if (apptmt.getStart().getMonthValue() == month) {
                apptmtCount++;
            }
        }
        return apptmtCount;
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

}
