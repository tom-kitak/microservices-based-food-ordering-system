package nl.tudelft.sem.group06b.order.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeValidationTest {

    @Test
    void validTime() throws Exception {
        TimeValidation timeValidation = new TimeValidation();
        String time = "24/12/3020 13:12:10";
        int offset = 30;
        Assertions.assertThat(timeValidation.isTimeValid(time, offset)).isTrue();
    }

    @Test
    void invalidTime() throws Exception {
        TimeValidation timeValidation = new TimeValidation();
        String time = "24/12/2020 13:12:10";
        String wrongTime = "24/12/2020";
        int offset = 30;
        Assertions.assertThat(timeValidation.isTimeValid(time, offset)).isFalse();
        assertThrows(Exception.class, () -> {
            timeValidation.isTimeValid(wrongTime, offset);
        });
    }

}
