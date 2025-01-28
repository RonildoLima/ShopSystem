package com.accenture.shopsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class ShopSystemApplicationTest {

    @Test
    void main() {
        try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
            ShopSystemApplication.main(new String[]{});

            mockedSpringApplication.verify(() -> SpringApplication.run(ShopSystemApplication.class, new String[]{}));
        }
    }
}
