package cz.tul.Chmelar.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorControllerTest {

    @Test
    public void testErrorTrue() {
        ErrorController controller = new ErrorController();

        String result = controller.error();

        assertEquals("error_page", result);

    }

    @Test
    public void testErrorFalse() {
        ErrorController controller = new ErrorController();

        String result = controller.error();

        assertNotEquals("error_page_one", result);

    }
}