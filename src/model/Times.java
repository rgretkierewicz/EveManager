package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;


public class Times  {
    private static ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();
    private static ObservableList<LocalTime> businessHoursEnd = FXCollections.observableArrayList();


    public static ObservableList<LocalTime> getBusinessHours() {
        if (businessHours.isEmpty()) {
            prepareBusinessHours();
        }
            return businessHours;
    }

    public static ObservableList<LocalTime> getBusinessHoursEnd() {
        if (businessHoursEnd.isEmpty()) {
            prepareBusinessHours();
        }
        return businessHoursEnd;
    }

    public static void prepareBusinessHours() {
        ZonedDateTime estStart = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8,0), ZoneId.of("America/New_York"));

        LocalTime currLt = estStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime(); //equivalent of 8am est
        LocalTime bhEnd = currLt.plusHours(14);


        while (currLt.isBefore(bhEnd)) {
            businessHours.add(currLt);
            currLt = currLt.plusMinutes(15);
            businessHoursEnd.add(currLt);
        }
    }

    public static LocalDateTime localToUTC(LocalDateTime timeToConvert) {
        ZonedDateTime ttcZDT = ZonedDateTime.of(timeToConvert, ZoneId.systemDefault());
        LocalDateTime utcTime = ttcZDT.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return utcTime;

    }



}
