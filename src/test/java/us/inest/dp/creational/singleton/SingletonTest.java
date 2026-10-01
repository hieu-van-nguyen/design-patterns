package us.inest.dp.creational.singleton;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// @Disabled("Entire class disabled for maintenance")
public class SingletonTest {

    @BeforeEach
    public void setUp() {
        Singleton.resetInstance();
    }

    @Test
    @DisplayName("Should return the same instance when called multiple times in the same thread")
    public void testSameInstanceSameThread() {
        Singleton instance1 = Singleton.getInstance("First");
        Singleton instance2 = Singleton.getInstance("Second");

        assertSame(instance1, instance2, "Both calls should return the same instance");
    }

    @Test
    @DisplayName("Should maintain the value provided during the first initialization")
    public void testInitializationValueIsPreserved() {
        // This test assumes it runs first or Singleton is reset.
        // Since uniqueInstance is static, we test that subsequent calls don't change it.
        String initialValue = "Initial";
        Singleton instance = Singleton.getInstance(initialValue);

        // We can't easily check the private 'data' field without reflection,
        // but we can verify that calling getInstance again with a different value doesn't change the instance.
        Singleton secondCall = Singleton.getInstance("Different Value");
        assertSame(instance, secondCall, "Instance should not change on subsequent calls");
        assertEquals(initialValue, secondCall.getData());
    }
}
