package us.inest.dp.observer;

import java.util.ArrayList;
import java.util.List;

public class StatisticsDisplay implements Observer, DisplayElement{
    private float min, max, avg;
    private final Subject weatherData;

    public StatisticsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        this.min = Float.MAX_VALUE;
        this.max = Float.MIN_VALUE;
        weatherData.registerObserver(this);
    }

    public void update(float temperature, float humidity, float pressure) {
        this.min = Math.min(min, temperature);
        this.max = Math.max(max, temperature);
        display();
    }

    public void display() {
        System.out.println(String.format("Avg/Max/Min: %.2f/%.2f/%.2f", (max + min) * 0.5, max, min));
    }
}
