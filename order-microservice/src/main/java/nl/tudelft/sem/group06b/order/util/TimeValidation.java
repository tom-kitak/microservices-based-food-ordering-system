package nl.tudelft.sem.group06b.order.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeValidation {

    /**
     * Checks if the time is valid and is before the deadline.
     *
     * @param time time to check
     * @param deadlineOffset integer of how many minutes before the provided time is the deadline
     * @return String that indicates if the time is valid or not
     * @throws Exception indication the format of time is not correct
     */
    public Boolean isTimeValid(String time, int deadlineOffset) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            LocalDateTime orderDate = LocalDateTime.parse(time, formatter);
            LocalDateTime currentDate = LocalDateTime.now();

            if (orderDate.minusMinutes(deadlineOffset).isBefore(currentDate)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            throw new Exception("Please provide the correct time format: dd/MM/yyyy HH:mm:ss");
        }
    }
}
