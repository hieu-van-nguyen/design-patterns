package us.inest.dp.creational.factory_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class PizzaStoreTest {

    @Test
    @DisplayName("NY Pizza Store should create NY Style Cheese Pizza")
    public void testNYPizzaStoreCheesePizza() {
        PizzaStore nyStore = new NYPizzaStore();
        Pizza pizza = nyStore.orderPizza("cheese");

        assertNotNull(pizza, "Pizza should not be null");
        assertEquals("NY Style Sauce and Cheese Pizza", pizza.getName(), "Should be NY Style Sauce and Cheese Pizza");
    }

    @Test
    @DisplayName("Chicago Pizza Store should create Chicago Style Cheese Pizza")
    public void testChicagoPizzaStoreCheesePizza() {
        PizzaStore chicagoStore = new ChicagoPizzaStore();
        Pizza pizza = chicagoStore.orderPizza("cheese");

        assertNotNull(pizza, "Pizza should not be null");
        assertEquals("Chicago Style Deep Dish Cheese Pizza", pizza.getName(), "Should be Chicago Style Deep Dish Cheese Pizza");
    }
}
