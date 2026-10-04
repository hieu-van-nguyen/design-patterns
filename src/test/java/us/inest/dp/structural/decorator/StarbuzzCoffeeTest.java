package us.inest.dp.structural.decorator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StarbuzzCoffeeTest {
    @Test
    public void testEspresso() {
        Beverage beverage = new Espresso();
        assertEquals("Espresso", beverage.getDescription());
        assertEquals(1.99, beverage.cost(), 0.001);
    }

    @Test
    public void testDarkRoastWithMochaAndWhip() {
        Beverage beverage = new DarkRoast();
        beverage = new Mocha(beverage);
        beverage = new Mocha(beverage);
        beverage = new Whip(beverage);

        assertEquals("DarkRoast Coffee, Mocha, Mocha, Whip", beverage.getDescription());
        // 0.89 + 0.20 + 0.20 + 0.10 = 1.39
        assertEquals(1.39, beverage.cost(), 0.001);
    }

    @Test
    public void testHouseBlendWithSoyMochaAndWhip() {
        Beverage beverage = new HouseBlend();
        beverage = new Soy(beverage);
        beverage = new Mocha(beverage);
        beverage = new Whip(beverage);

        assertEquals("House Blend Coffee, Soy, Mocha, Whip", beverage.getDescription());
        // 0.89 + 0.15 + 0.20 + 0.10 = 1.34
        assertEquals(1.34, beverage.cost(), 0.001);
    }
}
