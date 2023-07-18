package ru.netology.conditionalapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConditionalAppApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    private final GenericContainer<?> devapp = new GenericContainer<>("devapp").withExposedPorts(8080);
    private final GenericContainer<?> prodapp = new GenericContainer<>("prodapp").withExposedPorts(8081);

    @BeforeEach
    public void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void contextLoads() {

        ResponseEntity<String> entityFromDev = restTemplate.getForEntity(
                "http://localhost:" + devapp.getMappedPort(8080) + "/profile", String.class);

        ResponseEntity<String> entityFromProd = restTemplate.getForEntity(
                "http://localhost:" + prodapp.getMappedPort(8081) + "/profile", String.class);

        Assertions.assertEquals(entityFromDev.getBody(), "Current profile is dev");
        Assertions.assertEquals(entityFromProd.getBody(), "Current profile is production");
    }

}
