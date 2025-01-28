package com.accenture.shopsystem.pages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginTest {

    @Test
    void loginPage() {
        Login login = new Login();

        String viewName = login.loginPage();

        assertEquals("login", viewName);
    }
}
