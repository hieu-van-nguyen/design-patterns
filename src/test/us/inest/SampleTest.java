package us.inest;


import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SampleTest {
    @BeforeAll
    public static void beforeClass() {
        System.out.println("Before Class");
    }

    @BeforeEach
    public void before() {
        System.out.println("Before Each Test Case");
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @AfterAll
    public static void afterClass() {
        System.out.println("After Class");
    }

    @AfterEach
    public void after() {
        System.out.println("After Each Test Case");
    }
}
