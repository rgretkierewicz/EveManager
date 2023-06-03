package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;


public class Times {
    private static ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();
    private static ObservableList<LocalTime> businessHoursEnd = FXCollections.observableArrayList();

    /**
     * @return business hours
     */
    public static ObservableList<LocalTime> getBusinessHours() {
        if (businessHours.isEmpty()) {
            prepareBusinessHours();
        }
        return businessHours;
    }

    /**
     * @return the time that business hours end
     */
    public static ObservableList<LocalTime> getBusinessHoursEnd() {
        if (businessHoursEnd.isEmpty()) {
            prepareBusinessHours();
        }
        return businessHoursEnd;
    }

    /**
     * Take the business hours and the business' time zone and translate that to the user's local time to provide
     * the local business hours. Create a list of appointment times within those business hours with appointments starting every 15 minutes.
     * The last available appointment time is 15 minutes before the end of the business hours.
     */
    public static void prepareBusinessHours() {
        ZonedDateTime estStart = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 0), ZoneId.of("America/New_York"));

        LocalTime currLt = estStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime(); //Equivalent of 8am est
        LocalTime bhEnd = currLt.plusHours(14);

        while (currLt.isBefore(bhEnd)) {
            businessHours.add(currLt);
            currLt = currLt.plusMinutes(15);
            businessHoursEnd.add(currLt);
        }
    }

    /**
     * @param timeToConvert the local time to be converted
     * @return the time converted to UTC
     */
    public static LocalDateTime localToUTC(LocalDateTime timeToConvert) {
        ZonedDateTime ttcZDT = ZonedDateTime.of(timeToConvert, ZoneId.systemDefault());
        LocalDateTime utcTime = ttcZDT.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return utcTime;
    }


}
