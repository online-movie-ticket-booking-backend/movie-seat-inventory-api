package org.example.movie.seat.inventory.utility;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Converter {
    private Converter() {
    }

    public static String extractDate(LocalDateTime inputParam) {
        return inputParam.toLocalDate().getYear() + "-"
                + inputParam.toLocalDate().getMonthValue() + "-" +
                inputParam.toLocalDate().getDayOfMonth();
    }

    public static String extractTime(LocalDateTime inputParam) {
        return inputParam.getHour() + ":" + inputParam.getMinute();
    }

    public static Date convertToSQLDate(String localDateParameter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(localDateParameter, formatter);
        return Optional.ofNullable(localDate)
                .map(Date::valueOf)
                .orElseGet(() -> Date.valueOf(LocalDate.now()));
    }

}
