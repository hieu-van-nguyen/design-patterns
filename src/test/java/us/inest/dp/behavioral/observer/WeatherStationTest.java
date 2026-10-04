package us.inest.dp.behavioral.observer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherStationTest {
    @Test
    public void testWeatherUpdates() {
        WeatherData weatherData = new WeatherData();
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);

        // Testing that the measurements update and notify observers without throwing exceptions
        assertDoesNotThrow(() -> {
            weatherData.setMeasurements(80, 65, 30.4f);
            weatherData.setMeasurements(82, 70, 29.2f);
            weatherData.setMeasurements(78, 90, 29.2f);
        });
    }
}
