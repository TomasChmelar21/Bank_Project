package cz.tul.Chmelar.services;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateServiceTest {

    @Test
    void refreshFileExchangeRate() {
    }

    @Test
    void testIfItIsHoliday() {
        LocalDate holidayDate = LocalDate.of(2023, 1, 1);
        assertTrue(ExchangeRateService.isHoliday(holidayDate));

        LocalDate nonHolidayDate = LocalDate.of(2023, 1, 2);
        assertFalse(ExchangeRateService.isHoliday(nonHolidayDate));
    }
    @Test
    void transferExchangeRateCount() {
    }

    @Test
    void isDateSameOrEarlierThanTomorrow() {

    }

    @Test
    void testRefreshFileExchangeRate() {
    }

    @Test
    void isHoliday() {
    }

    @Test
    void testTransferExchangeRateCount() {
    }

    @Test
    void testIsDateSameOrEarlierThanTomorrow() {
    }
}