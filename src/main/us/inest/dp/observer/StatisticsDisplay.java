package us.inest.dp.observer;
import java.util.Observable;
import java.util.Observer;

public class StatisticsDisplay implements Observer, DisplayElement {
    private float min, max, avg;
    private Observable observable;

    public StatisticsDisplay(Observable observable) {
        this.observable = observable;
        this.min = Float.MAX_VALUE;
        this.max = Float.MIN_VALUE;
        observable.addObserver(this);
    }

    public void update(Observable obs, Object arg) {
        if (obs instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) obs;
            this.min = Math.min(min, weatherData.getTemperature());
            this.max = Math.max(max, weatherData.getTemperature());
            display();
        }
    }

    public void display() {
        System.out.printf("Avg/Max/Min: %.2f/%.2f/%.2f%n", (max + min) * 0.5, max, min);
    }
}
