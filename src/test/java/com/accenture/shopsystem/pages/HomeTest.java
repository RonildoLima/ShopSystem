package com.accenture.shopsystem.pages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeTest {

    @Test
    void homePage() {
        Home home = new Home();

        String viewName = home.homePage();

        assertEquals("home", viewName);
    }
}
